package com.xuanwu.auth.resource;


import com.xuanwu.auth.service.RoleService;
import com.xuanwu.auth.service.UserService;
import com.xuanwu.auth.utils.CacheUtils;
import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.domain.entity.Enterprise;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.LoginTypeEnum;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.PageDataEncode;
import com.xuanwu.mos.dto.RsaEncode;
import com.xuanwu.mos.exception.AccountLockdException;
import com.xuanwu.mos.exception.IncorrectCredentialsTimesException;
import com.xuanwu.mos.exception.PasswdIsNullException;
import com.xuanwu.mos.mtclient.FrontKitPackSender;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.utils.Md5Utils;
import com.xuanwu.mos.utils.Messages;
import com.xuanwu.mos.utils.RSAUtil;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.VerifyCodeUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author zhangz
 * @date 2017/10/17
 */
@Component
@Path("/login")
public class LoginResource {
    private static final Logger logger = LoggerFactory
            .getLogger(LoginResource.class);

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private Config config;

    @Autowired
    private UserService userService;

    @Autowired
    private FrontKitPackSender packSender;

    @Resource(name = "frontKit")
    private PlatformMode platformMode;

    /*@Autowired
    private SysLogService sysLogService;*/

    @Autowired
    private RoleService roleService;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp login(Map<String, String> reqMap, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        String userName = reqMap.get("username");
        String passWord = reqMap.get("password");
        String imgVerifyCode = reqMap.get("imgVerifyCode");
        try {
            if (StringUtils.isBlank(userName) || StringUtils.isBlank(passWord)) {
                return JsonResp.fail(Messages.ACCOUNT_OR_PASSWD_BLANK);
            }

            if (StringUtils.isBlank(imgVerifyCode)) {
                return JsonResp.fail(Messages.IMGVERIFYCODE_BLANK);
            }

            String sessionCode = SessionUtil.getImageVerifyCode();
            if (StringUtils.isBlank(sessionCode)) {
                return JsonResp.fail(5, Messages.IMGVERIFYCODE_EXPIRED);
            }
            if (!imgVerifyCode.equalsIgnoreCase(sessionCode)) {
                return JsonResp.fail(5, Messages.IMGVERIFYCODE_INCORRECT);
            }

            PrivateKey keyPair = (PrivateKey) SessionUtil.getTokenVerify();
            userName = RSAUtil.decode(userName, keyPair);
            passWord = RSAUtil.decode(passWord, keyPair);

            Subject subject = SecurityUtils.getSubject();
            String mixLoginPasswd = Md5Utils.mixLoginPasswd(passWord, config.getMixKey());
            UsernamePasswordToken token = new UsernamePasswordToken(userName, mixLoginPasswd);
            subject.login(token);

            SimpleUser user = SessionUtil.getCurUser();
            //缓存前台菜单，按钮，权限信息
            Map<String, List<?>> menus = roleService.getMenusAndBtns(user.getId(), platformMode.getPlatform());
            SessionUtil.setMenus(menus);
            //发送短信用登陆密码的明文md5加密
            user.setSendMd5Password(DigestUtils.md5Hex(passWord));
            Enterprise enterprise = userService.getLoginEnt(user.getEnterpriseId());
            //判断企业是否被禁或者暂停
            if (enterprise.getState() != UserState.NORMAL) {
                return JsonResp.fail("企业账号已经被停用，请联系管理员");
            }
            SessionUtil.setCurEnterprise(enterprise);
            if (enterprise != null && enterprise.getTrustFlag() == true) {
                boolean needValid = false;
                //需要过滤企业默认添加的ip
                String defaultIp = config.getValidateIp();
                if (StringUtils.isBlank(defaultIp)) {
                    defaultIp = config.getDefaultValidateIp();
                }
                String trustIps = userService.getTrustIps(enterprise.getId(), platformMode.getPlatform(), defaultIp);
                if (StringUtils.isNotBlank(trustIps)) {
                    String ip = request.getRemoteAddr();
                    if (request.getHeader("x-forwarded-for") != null) {
                        ip = request.getHeader("x-forwarded-for");
                    }
                    String[] realIps = ip.split(Delimiters.COMMA);
                    List<String> trustIpList = Arrays.asList(trustIps.split(Delimiters.COMMA));
                    ip = realIps[0];
                    if (!trustIpList.contains(ip)) {
                        needValid = true;
                    }
                } else { //信任ip列表为空，默认全部不需要手机验证码验证登录
                    needValid = false;
                }
                if (needValid) {
                    logger.error(Messages.IP_IS_NOT_ALLOWD);
                    //设置手机验证码登录验证的会话时间
                    SessionUtil.newRequestTimer();
                    List<String> phoneList = userService.getEnterprisePhonesById(user.getEnterpriseId());
                    return JsonResp.fail(1, phoneList, Messages.IP_IS_NOT_ALLOWD);
                }
            }

            if (user.isFirstTimeLogin()) {
                logger.info(Messages.FIRST_TIME_LOGIN);
                return JsonResp.fail(2, Messages.FIRST_TIME_LOGIN);
            }

            if (user.getValidDay() > 0 && user.getLastUpdateTime() != null) {
                if (DateUtil.getDays(user.getLastUpdateTime(), new Date()) > user.getValidDay()) {
                    logger.info(Messages.EXPIRE_TIME_LOGIN);
                    return JsonResp.fail(2, Messages.EXPIRE_TIME_LOGIN);
                }
            }


            String ip = request.getRemoteAddr();
            if (request.getHeader("x-forwarded-for") != null) {
                ip = request.getHeader("x-forwarded-for");
            }
            // 设置用户登录的方式
            SessionUtil.setLoginType(LoginTypeEnum.USERNAME_PASSWORD);
            //TODO 调用前台微服务打日志
            /**String logContent = "【" + ip + "】:【" + userName + "】";
             sysLogService.addLog(user, OperationType.LOGIN,"系统","Public","Login", logContent); //添加访问日志*/

        } catch (UnknownAccountException e) {// 用户不存在或密码错误
            return JsonResp.fail(3, Messages.ACCOUNT_NOT_EXIST);
        } catch (PasswdIsNullException e) {// 登录密码为空，需要系统管理员重新设置
            return JsonResp.fail(Messages.PASSWD_NEED_CHANGE);
        } catch (AccountLockdException e) {// 跨平台登录或者账号被禁用
            return JsonResp.fail(Messages.ACCOUNT_LOCK);
        } catch (IncorrectCredentialsTimesException e) {// 密码错误
            String msg = e.getErrTimes() == 0 ? Messages.ACCOUNT_LOCK : "您还有" + e.getErrTimes() + "次机会.如忘记密码,可联系您所在企业管理员重置密码";
            return JsonResp.fail(4, msg);
        } catch (DisabledAccountException e) {// 跨平台登录或者账号被禁用
            return JsonResp.fail(Messages.ACCOUNT_STAT_NOT_NORMAL);
        } catch (Exception e) {
            return JsonResp.fail(Messages.SYSTEM_ERROR);
        }
        String sessionId = SessionUtil.getSessionId(request);
        return JsonResp.success();
    }

    @POST
    @Path("checkLogin")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp checkLogin(@Context HttpServletRequest request) {
        String sessionId = SessionUtil.getSessionId(request);
        /*if (SessionUtil.isLogin()) {
            //登录成功之后清空消息通知版本号，强制重新加载数据
            SimpleUser curUser = SessionUtil.getCurUser();
            noticeService.resetUserNoticeVersion(curUser.getId());
            fileTaskService.resetUserUpAndLoadVersion(curUser.getId());
            return JsonResp.success(curUser.getUsername());
        }
        return JsonResp.fail(Messages.LOGIN_TIMEOUT);*/
        return JsonResp.success("d");
    }

    /**
     * 自动生成验证码
     *//*
    @GET
    @Path("autocode")
    @Produces({MediaType.APPLICATION_JSON})
    public String autoCode(@Context HttpServletResponse response, @Context HttpServletRequest request) throws IOException {
        String sessionId = SessionUtil.getSessionId(request);
        if (StringUtils.isBlank(sessionId)) {
            sessionId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(SessionUtil.FRONTKIT_SESSIONID, sessionId);
            response.addCookie(cookie);
        }
        OutputStream os = response.getOutputStream();
        response.setContentType("image/jpg");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "No-cache");
        String imageVerifyCode = VerifyCodeUtils.outputVerifyImage(100, 30, os, VerifyCodeUtils.VERIFY_FOUR);
        os.flush();
        cacheUtils.setImageVerifyCode(sessionId, imageVerifyCode);
        return imageVerifyCode;
    }*/
    /**
     * 自动生成验证码
     */
    @GET
    @Path("autocode")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp autoCode(@Context HttpServletResponse response) throws IOException {
        // 必须，在此处重新new一个subject到ThreadContext线程里，因之前logout后会清空当前线程下的subject，需要重新new一个
        SessionUtil.getSession();
        OutputStream os = response.getOutputStream();
        response.setContentType("image/jpg");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "No-cache");
        String imageVerifyCode = VerifyCodeUtils.outputVerifyImage(100, 30, os, VerifyCodeUtils.VERIFY_FOUR);
        SessionUtil.setImageVerifyCode(imageVerifyCode);
        os.flush();
        return JsonResp.success();
    }

    /**
     * 生产令牌
     */
    @POST
    @Path("token")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp token(@Context HttpServletResponse response, @Context HttpServletRequest request) throws IOException {
        Map<String, String> typeMap = new HashMap<>();
        PageDataEncode rsaEncode = new RsaEncode();
        PageDataEncode.RsaParams rsa = rsaEncode.init();
        typeMap.put("exp", rsa.getExponent());
        typeMap.put("mod", rsa.getModulus());
        String sessionId = SessionUtil.getSessionId(request);
        cacheUtils.setTokenVerify(sessionId, rsa.getKey());
        return JsonResp.success(typeMap);
    }
}
