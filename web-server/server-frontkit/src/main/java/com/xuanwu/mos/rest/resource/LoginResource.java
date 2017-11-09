package com.xuanwu.mos.rest.resource;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.domain.entity.Enterprise;
import com.xuanwu.mos.domain.entity.MTResp;
import com.xuanwu.mos.domain.entity.MTResult;
import com.xuanwu.mos.domain.entity.SimpleUser;
import com.xuanwu.mos.domain.enums.LoginTypeEnum;
import com.xuanwu.mos.domain.enums.OperationType;
import com.xuanwu.mos.domain.enums.UserState;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.exception.AccountLockdException;
import com.xuanwu.mos.exception.IncorrectCredentialsTimesException;
import com.xuanwu.mos.exception.PasswdIsNullException;
import com.xuanwu.mos.exception.RepositoryException;
import com.xuanwu.mos.mtclient.FrontKitPackSender;
import com.xuanwu.mos.service.RoleService;
import com.xuanwu.mos.service.SysLogService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.AuthSecurityUtil;
import com.xuanwu.mos.utils.Constants;
import com.xuanwu.mos.utils.DateUtil;
import com.xuanwu.mos.utils.Delimiters;
import com.xuanwu.mos.utils.Md5Utils;
import com.xuanwu.mos.utils.Messages;
import com.xuanwu.mos.utils.RSAUtil;
import com.xuanwu.mos.utils.SessionUtil;
import com.xuanwu.mos.utils.VerifyCodeUtils;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.impl.GroupMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgContent;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgPack;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgSingle;
import com.xuanwu.msggate.common.sbi.exception.CoreException;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("login")
public class LoginResource {
    private Logger logger = LoggerFactory.getLogger(LoginResource.class);

    @Autowired
    private Config config;

    @Autowired
    private UserService userService;

    @Autowired
    private FrontKitPackSender packSender;

    @Autowired
    private PlatformMode platformMode;
    
    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private RoleService roleService;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public JsonResp login(@Context HttpServletResponse response) {
       //当AuthorizationFilter鉴权session超时,需要重新登录
        response.setHeader(Constants.HEADER_ACCESS_STATE, "unlogin");
        return JsonResp.fail(Messages.LOGIN_TIMEOUT);
    }

    /*@POST
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp login(Map<String, String> reqMap, @Context HttpServletRequest request, @Context HttpServletResponse response) throws RepositoryException {
        return null;
    }*/
    



    @POST
    @Path("updatePasswd")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp updatePasswd(Map<String, String> reqMap) throws Exception {
        String userName = reqMap.get("userName");
        String newPasswd = reqMap.get("newPasswd");
        PrivateKey keyPair = (PrivateKey)SessionUtil.getTokenVerify();
        userName = RSAUtil.decode(userName,keyPair);
        newPasswd = RSAUtil.decode(newPasswd,keyPair);

        SimpleUser user = userService.getLoginUser(userName, platformMode.getPlatform());
        String mixLoginPasswd = Md5Utils.mixLoginPasswd(newPasswd, config.getMixKey());
        if (mixLoginPasswd.equals(user.getPassword())) {
            return JsonResp.fail("新设置的登录密码不能和发送密码相同");
        }
        String mixTransmitPasswd = AuthSecurityUtil.encrypt(newPasswd);
        if (mixTransmitPasswd.equals(user.getTransmitPassword())) {
            return JsonResp.fail("新设置的登录密码不能和透传密码相同");
        }
        userService.updatePasswd(user.getId(), mixLoginPasswd);
        //同步更新session中的登录密码
        user.setSecondPassword(mixLoginPasswd);
        sysLogService.addLog(user,OperationType.RESET_LOGIN_PWD,"用户账号","Public","updatePasswd",user.getUsername()); //添加访问日志
        
        return JsonResp.success();
    }


    @POST
    @Path("sendPhoneVerifyCode")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp sendPhoneVerifyCode(String phone) throws RepositoryException {
        if (SessionUtil.isSessionTimeout()) {
            SessionUtil.logout();
            return JsonResp.fail(1,"登录超时，请重新登录!!!");
        }
        String phoneVerifyCode = VerifyCodeUtils.generateVerifyCode(VerifyCodeUtils.VERIFY_SIX);
        logger.info("验证码:" + phoneVerifyCode);
        SessionUtil.setPhoneVerifyCode(phoneVerifyCode);
        PMsgPack msgPack = buildPack(phoneVerifyCode, phone);
        String sendUser = config.getSendValidateCodeAccount();
        String md5SendPsswd = DigestUtils.md5Hex(config.getSendValidateCodePassWord());
        MTResp resp = null;
        try {
            resp = packSender.send(sendUser, md5SendPsswd, "" ,msgPack);
        } catch (CoreException e) {
            e.printStackTrace();
        }
        if (resp.getResult() != MTResult.SUCCESS) {
            return JsonResp.fail("短信验证码发送失败，请稍后重试");
        }
        return JsonResp.success();
    }


    @POST
    @Path("validatePhone")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp validatePhone(Map<String, String> param, @Context HttpServletRequest request) {
        if (SessionUtil.isSessionTimeout()) {
            SessionUtil.logout();
            return JsonResp.fail(1,"登录超时，请重新登录!!!");
        }
        String code = param.get("code");
        String phone = param.get("phone");
        String phoneVerifyCode = SessionUtil.getPhoneVerifyCode();
        if (phoneVerifyCode == null) {
            return JsonResp.fail("请先获取验证码");
        } else if (Constants.TIMEOUT.equals(phoneVerifyCode)){
            return JsonResp.fail("验证码已经失效，请重新获取");
        } else if (code.equalsIgnoreCase(phoneVerifyCode)){
            //验证通过，设置登录成功
            SessionUtil.setLogin();
            String ip = request.getRemoteAddr();
            if (request.getHeader("x-forwarded-for") != null) {
                ip =  request.getHeader("x-forwarded-for");
            }

            // 设置用户登录的方式
            SessionUtil.setLoginType(LoginTypeEnum.PHONE_VERIFY_CODE);

            String logContent = "【" + ip + "】:【" + phone + "】";
            SimpleUser user = SessionUtil.getCurUser();
            sysLogService.addLog(user,OperationType.VALID_PHONE,"系统","Public","Login", logContent); //添加访问日志
            return JsonResp.success("验证通过！！！");
        }
        return JsonResp.fail("验证码输入有误，请重新输入");
    }


    @GET
    @Path("unauthorized")
    @Produces({MediaType.APPLICATION_JSON})
    public Response unauthorized() {
        return Response.ok().header(Constants.HEADER_ACCESS_STATE, "unauthorized").build();
    }

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

    @POST
    @Path("logout")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp logout() {
    	SimpleUser user = SessionUtil.getCurUser();
        String logContent = "【" + user.getUsername() + "】";
        SessionUtil.logout();
        sysLogService.addLog(user,OperationType.LOGOUT,"系统","Public","logout",logContent); //添加访问日志
        return JsonResp.success("退出成功！");
    }

    /**
     * 生产令牌
     *//*
    @POST
    @Path("token")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonResp token() throws IOException {
        Map<String, String> typeMap = new HashMap<>();

        PageDataEncode rsaEncode = new RsaEncode();
        PageDataEncode.RsaParams rsa = rsaEncode.init();

        typeMap.put("exp", rsa.getExponent());
        typeMap.put("mod", rsa.getModulus());
        SessionUtil.setTokenVerify(rsa.getKey());
        return JsonResp.success(typeMap);
    }*/

    private PMsgPack buildPack(String msg, String phone) {
        PMsgPack pack = new PMsgPack();
        pack.setMsgType(MsgContent.MsgType.LONGSMS);
        pack.setBizType(0);
        pack.setDistinct(false);
        pack.setSendTypeIndex(MsgPack.SendType.GROUP.getIndex());

        GroupMsgFrame frame = new GroupMsgFrame();
        PMsgContent msgContent = new PMsgContent();
        msgContent.setContent(msg);
        List<MsgSingle> msgs = new ArrayList<MsgSingle>();
        msgs.add(new PMsgSingle(MsgContent.MsgType.LONGSMS, phone, msgContent, null, null, false, 0));
        frame.setBizType(pack.getBizType());
        frame.setMsgType(pack.getMsgType());
        frame.setReportState(true);
        frame.setAllMsgSingle(msgs);
        pack.getFrames().add(frame);
        return pack;
    }

}
