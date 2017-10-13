package com.xuanwu.mos.rest.resource;

import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.config.PlatformMode;
import com.xuanwu.mos.domain.Parameters;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.entity.PackStatInfo;
import com.xuanwu.mos.domain.enums.*;
import com.xuanwu.mos.dto.*;
import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.mtclient.FrontKitPackSender;
import com.xuanwu.mos.rest.service.BizTypeService;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.rest.service.msgservice.MsgFrameService;
import com.xuanwu.mos.rest.service.msgservice.MsgPackService;
import com.xuanwu.mos.rest.service.msgservice.MsgTicketService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.*;
import com.xuanwu.mos.vo.MmsInfoVo;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import com.xuanwu.msggate.common.sbi.entity.MsgSingle;
import com.xuanwu.msggate.common.sbi.entity.impl.MassMsgFrame;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgContent;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgPack;
import com.xuanwu.msggate.common.sbi.entity.impl.PMsgSingle;
import com.xuanwu.msggate.common.sbi.exception.CoreException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jiang.Ziyuan on 2017/3/29.
 */
@Component
public class BaseMsgResource {
    @Autowired
    private Config config;
    @Autowired
    private UserService userService;
    @Autowired
    private MsgPackService packService;
    @Autowired
    private MsgFrameService frameService;
    @Autowired
    private MsgTicketService ticketService;
    @Autowired
    private FrontKitPackSender packSender;
    @Autowired
    private BizTypeService bizTypeService;
    @Autowired
    private UserMgrService userMgrService;
    @Autowired
    private MsgFrameService msgFrameService;
    @Autowired
    private PlatformMode platformMode;


    /**
     * 批次详情的号码发送数
     */
    public MmsMsgPack statBatchPhoneDetail(String packId, MsgContent.MsgType msgType, boolean ticketState, Date postTime) {

//        int failed = 0; // 已知失败数
//        int success = 0; // 已知成功送达数
//        int sendedPhones = 0; // 已发送号码数
//        int illegalCarrierTeleseg = 0;// 运营商号码段不存在
        int repeatPhones = 0;// 重复号码数
        int illegalKeys = 0;// 非法关键字
        int illegalPhones = 0;// 非法号码
        int blackPhones = 0;// 黑名单

//        if (ticketState) {
//            List<PackStatInfo> ticketStats = ticketService.findPackStatInfo(packId, msgType, postTime);
//            for (PackStatInfo p : ticketStats) {
//                if (p.getSendResult().intValue() != 7) {
//                    sendedPhones += p.getStatCount();
//                }
//                // 已知成功送达数 = 提交成功数 - 状态报告已返回并失败数
//                if (p.getSendResult().intValue() == 1 && (p.getReportResult() == null || p.getReportResult() == 0)) {
//                    success += p.getStatCount();
//                } else if (p.getReportResult() != null && p.getReportResult() > 0) {
//                    failed += p.getStatCount();
//                }
//            }
//        }

        MmsMsgPack mmsMsgPack = new MmsMsgPack();
        List<PackStatInfo> frameStats = frameService.findPackStatInfos(packId, msgType.getIndex(), postTime);
        for (PackStatInfo p : frameStats) {
            mmsMsgPack.setScheduleTime(p.getScheduleTime());
            int count = p.getStatCount();
            switch (p.getBizForm()) {
                case REPEAT_PHONE:
                    repeatPhones += count;
                    break;
                case ILLEGALKEY:
                    illegalKeys += count;
                    break;
                case ILLEGAL_PHONE:
                    illegalPhones += count;
                    break;
                case BLACK:
                    blackPhones += count;
                    break;
                default:
                    break;
            }
        }

        mmsMsgPack.setRepeatTickets(repeatPhones);
        mmsMsgPack.setIllegalKeyTickets(illegalKeys);
        mmsMsgPack.setIllegalTickets(illegalPhones);
        mmsMsgPack.setBlackTickets(blackPhones);
        return mmsMsgPack;
    }

    /**
     * 通过packId获取该批次短彩信的审核记录
     * 审核记录包括前台审核和后台审核
     */
    public MmsMsgPack findAuditRecord(MmsMsgPack pack, String packId) {
        QueryParameters params = new QueryParameters();
        params.addParam("packId", packId);
        MmsMsgPack front = packService.findFrontAuditRecord(params);
        MmsMsgPack back = packService.findBackAuditRecord(params);
        if (front != null) {
            pack.setFrontAuditUser(front.getFrontAuditUser());
            pack.setFrontAuditTime(front.getFrontAuditTime());
            pack.setFrontAuditState(front.getFrontAuditState());
            pack.setFrontAuditRemark(front.getFrontAuditRemark());
        }
        if (back != null) {
            pack.setBackAuditRemark(back.getBackAuditRemark());
            pack.setBackAuditState(back.getBackAuditState());
            pack.setBackAuditTime(back.getBackAuditTime());
            pack.setBackAuditUser(back.getBackAuditUser());
        }
        return pack;
    }

    /**
     * 返回查询多天时的总数据条数
     */
    public int[] findMmsMsgPacksCountMultiDays(QueryParameters query, Parameters params) {
        return packService.findMmsMsgPacksCountMultiDays(query, params);
    }

    public List<MmsMsgPack> findMmsMsgPacksMultiDays(QueryParameters query, Parameters params, int offset, int count, int[] totals) {
        return packService.findMmsMsgPacksMultiDays(query, params, offset, count, totals);
    }

    /**
     * 获取彩信所有的号码发送记录
     */

    public int[] findAllMmsNumberRecordCountMultiDays(QueryParameters query, Parameters params) {
        return ticketService.findAllMmsNumberRecordCountMultiDays(query, params);
    }

    public List<MsgTicket> findAllMmsNumberRecordMultiDays(QueryParameters query, Parameters params, int offset, int count, int[] totals) {
        return ticketService.findAllMmsNumberRecordMultiDays(query, params, offset, count, totals);
    }

    public <T> Integer calculateTotal(List<T> list, PageReqt reqt) {
        int total = 1;
        if (list.size()<=0 || list.size() < reqt.getCount()) {
            // 如果当查询到数据的数量为0 或者是 查询的数据的数量小于当前请求需要的数量
            // 1、 没有查询到数据
            // 2、这是查询的最后一次了
            total = reqt.getPage() * reqt.getCount();
        } else {
            total = (reqt.getPage()+1)*reqt.getCount();
        }
        return total;
    }

    /**
     * 根据dataScope来确定用户的query的参数
     */
    public QueryParameters checkUserDataScope(QueryParameters query, DataScope dataScope) {
        SimpleUser curUser = SessionUtil.getCurUser();
        query.addParam("enterpriseId", curUser.getEnterpriseId());
        String path = null;
        switch (dataScope) {
            case PERSONAL:
                // 如果是用户的权限是个人的，就算用户传了他人的userId，也要替换为他自己的
                query.addParam("userId", curUser.getId());
                break;
            case DEPARTMENT:
                // 获取当前用户所在部门,子部门，所有userId
                Integer deptId = curUser.getParentId();
                path = userService.findPathById(deptId);
                path += deptId + Delimiters.DOT;
                QueryParameters p = new QueryParameters();
                p.addParam("showAllChild", true);
                p.addParam("path", path);
                p.addParam("platformId", Platform.FRONTKIT.getIndex());
                p.addParam("enterpriseId", curUser.getEnterpriseId());
                List<User> users = userMgrService.listUsers(p);
                List<Integer> userIds = new ArrayList<>();
                for (User u : users) {
                    userIds.add(u.getId());
                }
                query.addParam("userIds", userIds);
                break;
            default:
                query.addParam("nterpriseId", curUser.getEnterpriseId());
                break;
        }
        return query;
    }

    //region 获取彩信标题或者是短信的内容
    public String getMmsContent(Parameters params) {
        MsgFrame frame = frameService.findSingleMsgFrameByPackId(params);
        if (frame != null) {
            return frame.getTitle();
        }
        return "";
    }
    //endregion

    //region 取消发送短彩信
    public JsonResp cancelSendMms(HttpServletRequest request, Parameters params) {
        MmsMsgPack msgPack = packService.findMmsMsgPackById(params);
        String failedResult = "";
        if (msgPack == null) {
            return JsonResp.fail("该批次不存在");
        } else if (msgPack.getPackState() == PackStateEnum.OVER) {
            return JsonResp.fail("不能取消发送完成的批次");
        }
        List<MsgFrame> notHandleFrameList = frameService.findNotHandleFrame(msgPack.getPackId(), msgPack.getPostTime(), msgPack.getMsgType().getIndex());
        if(notHandleFrameList == null || notHandleFrameList.isEmpty()){
            return JsonResp.fail("该批次已经发送完成无法取消");
        }
        try {
            SimpleUser user = SessionUtil.getCurUser();
            Map<String, String> accountMap = covertAccountMap(user, NetUtil.checkUserTrueIp(request));
            MTResp resp = packSender.cancel(accountMap, msgPack.getPackId(), msgPack.getUuid(), AuditStateEnum.CANCEL.getIndex(), msgPack.getMsgType().getIndex(), msgPack.getPostTime());

            if (resp.getResult() == MTResult.SUCCESS) {
                return JsonResp.success("批次取消发送成功");
            } else {
                if (StringUtils.isBlank(resp.getMessage())) {
                    failedResult = "批次取消发送失败：其它错误";
                } else {
                    failedResult = resp.getMessage();
                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
            failedResult = JsonUtil.toJSON("系统错误：" + e.getMessage(), -1);
        }
        return JsonResp.fail(failedResult);
    }
    //endregion

    //region 检核详情
    /**
     * 获取整个批次中被过滤的号码数
     */
    public int checkDetailCount(Parameters params) {
        return msgFrameService.findMsgCountByPackId(params);
    }

    /**
     * 查看当前的批次的全部检核详情或者是过滤详情
     */
    public PageResp checkPackDetail(Parameters params, PageReqt reqt) {
        params.getQuery().addParam("enterpriseId", SessionUtil.getCurUser().getEnterpriseId());
        int total = checkDetailCount(params);
        if (total > 0) {
            // 分页信息
            PageInfo pageInfo = new PageInfo(reqt.getPage(), reqt.getCount(), total);
            params.getQuery().setPage(pageInfo);

            // 获取当前packId的frame
            List<MsgFrame> msgFrames = frameService.checkRecordDetail(params);
            List<MsgTicket> ticketList = new ArrayList<>();

            // 将frame转为tickets
            for (MsgFrame frame : msgFrames) {
                ticketList.addAll(FrameTicketUtil.unZipSMSContent(frame));
            }
            // 获取目标条数
            int begin = params.getQuery().getPage().getFrom();
            int end = params.getQuery().getPage().getTo();
            List<MsgTicket> tickets = new ArrayList<>();
            if (ticketList.size() >= end) {
                tickets = ticketList.subList(begin, end);
            }
            List<Map<String, Object>> mapList = VoUtil.assembleCheckTicketVo(tickets, params.getMsgType());
            return PageResp.success(total, mapList);
        }
        return PageResp.success(total,Collections.emptyList());
    }

    //region 获取当前用户的账户余额和可发送数量
    public MmsInfoVo getBalanceByBizId(SpecsvsNumVo vo) throws Exception {
        SimpleUser user = SessionUtil.getCurUser();
        MmsInfoVo result = new MmsInfoVo();

        BigDecimal balance = new BigDecimal(userService.getExsitBindAccountOfUser(user.getId(), user.getEnterpriseId())+"").setScale(4,BigDecimal.ROUND_DOWN);
        //获取通道价格
        List<SpecsvsNumVo> list = bizTypeService.getCarrierDetailByBizId(user.getEnterpriseId(), vo.getBizTypeId(), vo.getMsgType());
        BigDecimal price = BigDecimal.ZERO;
        BigDecimal restSendNum = BigDecimal.ZERO;
        if (list.size() > 0 && balance.compareTo(BigDecimal.ZERO) == 1) {
            for (SpecsvsNumVo item : list) {
                if (price.compareTo(item.getPrice()) == -1) {
                    price = item.getPrice().setScale(2,BigDecimal.ROUND_DOWN);
                }
            }
            restSendNum = balance.divide(price,0,BigDecimal.ROUND_DOWN);
        }

        //计算可发送数量
        result.setBalance(balance);
        result.setRestSendNum(restSendNum.intValue());

        // 如果是彩信的话，还要添加最小通道的彩信最大容量的属性
        if (vo.getMsgType() == MsgTypeEnum.MMS.getIndex()) {
            // 彩信的最大容量是1000KB
            int mmsMaxLength = 1001 * 1024;
            for (SpecsvsNumVo numVo : list) {
                if (mmsMaxLength > numVo.getMmsMaxLength()) {
                    mmsMaxLength = numVo.getMmsMaxLength();
                }
            }
            result.setMmsMaxLength(mmsMaxLength);
        }

        Enterprise loginEnt = userService.getLoginEnt(user.getEnterpriseId());
        result.setEnableKeywordFilter(loginEnt.isWarningKeyWord());
        return result;
    }
    //endregion
    //endregion

    //region 重新格式化时间---由于从前台接收的时间都是字符串，因此在后台进行转换为Date类型，否则数据库报错
    public static void formatDate(QueryParameters query) {
        String beginTimeStr = (String) query.getParams().get("beginTime");
        String endTimeStr = (String) query.getParams().get("endTime");
        query.addParam("beginTime", DateUtil.parse(beginTimeStr, DateUtil.DateTimeType.DateTime));
        query.addParam("endTime", DateUtil.parse(endTimeStr, DateUtil.DateTimeType.DateTime));
    }
    //endregion

    //region 自动补全代码段
    //region 获取自动补全用户或者是自动补全部门时dataScope的判断
    public QueryParameters checkAutoDataScope(QueryParameters query, DataScope dataScope) {
        SimpleUser curUser = SessionUtil.getCurUser();
        switch (dataScope) {
            case PERSONAL:
                // 获取当前用户的id
                query.addParam("userId", curUser.getId());
                query.addParam("parentId", curUser.getParentId());
                break;
            case DEPARTMENT:
                // 获取当前用户所在部门的id
                String deptPath = userService.findPathById(curUser.getParentId()) + curUser.getParentId() + Delimiters.DOT;
                query.addParam("path", deptPath);
                query.addParam("deptId", curUser.getParentId());
                break;
            default:
                // 全局或者是none直接使用enterpriseId查询就好
                break;
        }
        return query;
    }
    //endregion

    //endregion
    //region 获取当前用户
    public JsonResp autoCompleteUser(HttpServletRequest request,DataScope dataScope, String userName) {
        if (null != request) {
            String contextPath = request.getPathInfo().replace("/fetchUserData", "");
            dataScope = SessionUtil.getDataSope(contextPath);
        }
        QueryParameters params = new QueryParameters();
        SimpleUser user = SessionUtil.getCurUser();
        params.addParam("userName", userName);
        params.addParam("platformId", Platform.FRONTKIT.getIndex());
        params.addParam("enterpriseId", user.getEnterpriseId());
        params.addParam("selectALL", "true");
        checkAutoDataScope(params, dataScope);
        List<SimpleUser> userList = userService.autoCompleteUserName(params);
        List<Map<String, String>> result = new ArrayList<>();
        for (SimpleUser u : userList) {
            Map<String, String> item = new HashMap<>();
            item.put("id", String.valueOf(u.getId()));
            item.put("name", u.getUsername());
            result.add(item);
        }
        return JsonResp.success(result);
    }
    /**
     *
     * @param dataScope
     * @param deptName
     * @return
     */
    public JsonResp autoCompleteDepartment(HttpServletRequest request,DataScope dataScope, String deptName) {
        if(null != request){
            String contextPath = request.getPathInfo().replace("/fetchDeptData","");
            dataScope = SessionUtil.getDataSope(contextPath);
        }
        SimpleUser user = SessionUtil.getCurUser();
        QueryParameters params = new QueryParameters();
        params.addParam("deptName", deptName);
        params.addParam("selectALL", "true");
        params.addParam("enterpriseId",user.getEnterpriseId());
        checkAutoDataScope(params,dataScope);
        List<Department> childDepartments = userMgrService.autoCompleteDepartments(params);
        List<Map<String, String>> result = new ArrayList<>();
        for (Department department : childDepartments) {
            Map<String, String> item = new HashMap<>();
            item.put("id", String.valueOf(department.getId()));
            item.put("name", department.getDeptName());
            item.put("path", department.getPath());
            result.add(item);
        }
        return JsonResp.success(result);
    }
    //region
    // 查询短彩信发送记录，发送详情，接收记录时的dataScope的判断
    // 最终该方法中的query中的params会存在userId,dataScope,userIds中的一个
    public QueryParameters checkTableDataScope(QueryParameters query, DataScope dataScope) {
        SimpleUser curUser = SessionUtil.getCurUser();
        query.addParam("enterpriseId", curUser.getEnterpriseId());

        //判断是否勾选了子部门，
        // 由于在短彩信的发送子部门中没有部门查询的选项，因此我们可以认为它是查当前部门下所有的用户的数据
        // 此时isSubDeptObj为null
        Object isSubDeptObj = query.getParams().get("subDept");
        boolean isSubDept = (isSubDeptObj != null) ? (boolean) isSubDeptObj : true;
        Object deptIdObj = query.getParams().get("deptId");

        switch (dataScope) {
            case PERSONAL:
                // 如果是用户的权限是个人的，就算用户传了他人的userId，也要替换为他自己的
                query.addParam("userId", curUser.getId());
                break;
            case DEPARTMENT:
                Integer deptId = curUser.getParentId();
                String path = userService.findPathById(deptId);
                path += deptId + Delimiters.DOT;

                if (null == deptIdObj && isSubDept) {
                    // 没有选择发送部门，但是勾选了包括子部门(或者是当查询的大表是短彩信的发送详情的查询会进入该分支)
                    // 显示当前这个用户所在的部门及其子部门的全部用户的数据记录
                    query.addParam("path", path);
                    query.addParam("dataScope", "hasValue");
                    query.addParam("deptId", deptId);
                } else {
                    findUsersByDeptId(query, deptIdObj, isSubDept, curUser);
                }
                break;
            case GLOBAL:
            case NONE:
                if (null == deptIdObj && isSubDept) {
                    // 没有选择发送部门，但是勾选了包括子部门应当要出现当前企业下所有的用户的数据
                    query.addParam("dataScope", "hasValue");
                } else {
                    // 没有选择发送部门，但是勾选了子部门，应当定位到该企业节点下
                    // 注意：企业的admin用户的path为空，但是其他用户的path都是以admin用户的id开始的。因此我们可以通过
                    // 获取非admin用户的path来获得admin用户的id(admin用户的id同时也可以视为所有的用户的根节点)
                    if (deptIdObj == null) {
                        String entPath = curUser.getPath();
                        if (StringUtils.isNotBlank(entPath)) {
                            String[] tempPath = entPath.split("\\"+Delimiters.DOT);
                            deptIdObj = tempPath[0];
                        } else {
                            // 如果当前用户的path为空，则说明当前用户为该企业的admin用户
                            deptIdObj = String.valueOf(curUser.getId());
                        }
                    }
                    findUsersByDeptId(query, deptIdObj, isSubDept, curUser);
                }
                break;
            default:
                break;
        }
        return query;
    }

    /**
     * 短彩信的查询大表(发送记录，发送详情，接受记录)
     * 只有当短彩信的查询大表中带有部门查询的时候，才会进入该分支
     * @param query
     * @param deptIdObj 需要查询的部门的Id对象
     * @param querySubDept 是否需要查询子部门(true为要查子部门，false为不查子部门)
     * @param curUser 当前用户
     * @return 返回符合条件的userIds，并插入到QueryParameters中
     */
    private QueryParameters findUsersByDeptId(QueryParameters query, Object deptIdObj, boolean querySubDept,SimpleUser curUser){

        // 获取查询部门的id
        int queryDeptId = (deptIdObj != null) ? Integer.valueOf((String) deptIdObj) : curUser.getParentId();

        String queryPath = userService.findPathById(queryDeptId);
        queryPath += queryDeptId + Delimiters.DOT;

        QueryParameters queryUserIdsParams = new QueryParameters();
        queryUserIdsParams.addParam("enterpriseId", curUser.getEnterpriseId());
        queryUserIdsParams.addParam("platformId", Platform.FRONTKIT.getIndex());
        queryUserIdsParams.addParam("path", queryPath);
        queryUserIdsParams.addParam("selectAllState", true);
        if (querySubDept) {
            queryUserIdsParams.addParam("showAllChild", true);
        } else {
            queryUserIdsParams.addParam("parentId", queryDeptId);
            queryUserIdsParams.addParam("showAllChild", false);
        }

        // 查询符合条件的所有的user
        List<User> users = userMgrService.listUsers(queryUserIdsParams);
        List<Integer> userIds = new ArrayList<>();
        for (User u : users) {
            userIds.add(u.getId());
        }
        query.addParam("userIds", userIds);
        return query;
    }
    //endregion

    /**
     * 根据packid，获取第一条ticket
     */
    public MsgTicket getTicketByPackId(String packId, Integer msgType, Date postTime) {
        QueryParameters query = new QueryParameters();
        query.addParam("packId", packId);
        Parameters params = new Parameters(query);
        params.setQueryTime(postTime);
        params.setMsgType(msgType);
        MsgFrame frame = msgFrameService.findSingleMsgFrameByPackId(params);
        List<MsgTicket> tickets = FrameTicketUtil.unZipSMSContent(frame);
        if (tickets != null && tickets.size() > 0) {
            return tickets.get(0);
        }
        return null;
    }

    public MTResp checkPack(MsgPack pack) {
        if (pack == null || ListUtil.isBlank(pack.getFrames())) {
            return MTResp.build(MTResult.INVALID_PARAM, "信息包或信息帧为空");
        }
        Object value = pack.getParameter(WebConstants.SEND_SMS_ERROR);
        if (value != null) {
            return MTResp.build(MTResult.INVALID_PARAM, (String) value);
        }
        // 只有联系人为空时才会发生
        if (ListUtil.isBlank(pack.getFrames().get(0).getAllMsgSingle())) {
            return MTResp.build(MTResult.INVALID_PARAM, "联系人信息已被删除，号码列表为空");
        }
        return null;
    }

    //region 判断发送短彩信时，发送用户是否为企业信任IP
    /*
    public TrustIpStatus checkTrustIp(HttpServletRequest request) {
        Enterprise enterprise = SessionUtil.getCurEnterprise();

        if (enterprise != null && enterprise.getTrustFlag() == true) {
            // 企业开启了信任ip验证
            if (SessionUtil.getLoginType() == LoginTypeEnum.PHONE_VERIFY_CODE){
                // 企业登录成功的方式是电话号码+验证码，则让其通过信任ip验证
                return TrustIpStatus.SUCCEED;
            }

            String userIp = NetUtil.checkUserTrueIp(request);
            String trustIps = userService.getTrustIps(SessionUtil.getCurUser().getEnterpriseId(), platformMode.getPlatform());
            if (StringUtils.isBlank(trustIps)) {
                // 如果企业开启了IP限制，但是没有填入任何的信任IP，就让其通过
                return TrustIpStatus.SUCCEED;
            } else if (StringUtils.isNotBlank(trustIps) && StringUtils.contains(trustIps, userIp)) {
                // 如果开启了IP限制，但是有输入信任IP，就必须要判断这个信任IP是否和当前登录的机子的IP是否是一致的
                // 如果一致，让其通过，否则拦截其请求
                return TrustIpStatus.SUCCEED;
            }
        } else {
            return TrustIpStatus.SUCCEED;
        }
        return TrustIpStatus.FAILED;
    }*/
    //endregion

    /**
     * 该方法仅用于短信审核和彩信审核使用
     * 当前的方法仅仅当用户在短信/彩信审核功能中，查询的权限是全局，而审核的权限是部门，这时候因为前台的hasPermission()方法只能判断有无，
     * 而不能判断权限是(Global，Department,Personal,NONE中的哪一种)，因此审核的按钮会显示出来。
     * 这时候会出现一种情况就是：具有部门的权限的用户能够去审核不在该用户的部门及其子部门下的用户。因此为了杜绝这种情况，我们必须去判断，
     * 当前的用户是部门权限时，需要被审核的记录对应的用户是不是该用户的部门及其子部门下的用户。
     * 有一个很棒的规律就是，如果A是B的部门及其子部门下的用户，那么A的path必定是以B的path开头的。
     *
     * @param request 需要从该变量中，获取到审核的权限的dataScope的值
     * @param userId  要进行审核的pack的拥有者的id
     * @return 如果要审核的pack的拥有者是当前审核人的部门(包括子部门)下的用户就返回true，否则返回false。
     */
    public boolean checkQueryAndAuditRelation(HttpServletRequest request, int userId) {
        DataScope dataScope = SessionUtil.getDataSope(request.getPathInfo().replace("/preAudit", ""));
        if (DataScope.DEPARTMENT == dataScope) {
            SimpleUser user = SessionUtil.getCurUser();
            String curUserPath = user.getPath();
            String queryUserPath = userService.findPathById(userId);

            // 如果大表中的用户是当前用户的部门及其子部门的用户，那么一定会满足
            if (!queryUserPath.startsWith(curUserPath)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param content 短信内容
     * @param contacts 发送号码集合
     * @param mmsInfoVo 发送批次相关数据
     * @param msgType 短信类型
     * @return
     * @throws CoreException
     */
    public MTResp sendMoMsg(HttpServletRequest request,String content, List<Contact> contacts, MmsInfoVo mmsInfoVo, com.xuanwu.msggate.common.sbi.entity.MsgContent.MsgType msgType, SimpleUser user) throws CoreException {
        PMsgPack pack = new PMsgPack();
        pack.setMsgType(msgType);
        pack.setBatchName(mmsInfoVo.getBatchName());
        pack.setRemark(mmsInfoVo.getRemark());
        pack.setBizType(mmsInfoVo.getBizTypeId());
        pack.setUserID(mmsInfoVo.getUserId());
        pack.setScheduleTime(DateUtil.parse(mmsInfoVo.getScheduledTime(), DateUtil.DateTimeType.DateTime));
        pack.setDistinct(mmsInfoVo.isDistinct());
        //mos只有群发，所以直接hardcode
        pack.setSendTypeIndex(MsgPack.SendType.MASS.getIndex());
        MassMsgFrame frame = new MassMsgFrame();
        final List<MsgSingle> msgs = frame.getAllMsgSingle();
        PMsgContent msgContent = new PMsgContent();
        msgContent.setContent(content);
        frame.setContent(msgContent);
        final String phoneReg = config.getPhonePattern();
        for (Contact contact : contacts) {
            if (!contact.getPhone().matches(phoneReg)) {
                throw new BusinessException("发送号码不合法！");
            }
            msgs.add(new PMsgSingle(msgType, contact.getPhone(), null,
                    null, null, contact.isVip(), 0));
        }
        frame.setScheduleTime(pack.getScheduleTime());
        frame.setBizType(pack.getBizType());
        frame.setMsgType(pack.getMsgType());
        frame.setReportState(true);
        pack.getFrames().add(frame);
        MTResp resp = checkPack(pack);
        if (resp == null) {
            resp = packSender.send(covertAccountMap(user,NetUtil.checkUserTrueIp(request)), pack);
        }
        return resp;
    }

    /**
     * 关于企业信任ip的问题 -- mos5.0之后，如果此功能有变动，希望后续开发人员能够继续维护该注释，谢谢
     * 当我们基于mos4.2重构mos5.0的代码时，
     *
     * mos4.2的做法是：当后台人员在对一个企业进行企业信任ip操作时，
     *
     * 两个规则：
     * A. 当添加了一个接收验证手机号码时，企业信任列表中会自动添加一个ip为10.10.10.10的ip地址，当最后一个接收验证号码被删除时，这个10.10.10.10的企业信任ip也随之被删除
     * B. remoteIp是网关中登录步骤中，必须要校验的一个关键点
     *  b1. 企业打开了信任ip功能，且信任列表为空，不校验remoteIp的值，
     *  b2. 企业打开了信任ip功能，且信任列表不为空，检验remoteIp是否在ip信任列表中，在则通过ip校验的步骤，不在则返回登录失败(ip不在信任列表中)
     * C. 10.10.10.10是内网IP，在真正的在线应用中，企业信任ip列表添加的都是公网IP，是无法出现内网IP的。
     *
     * 五种业务场景
     *
     * 场景    | 是否开启ip信任 | 是否添加信任ip | 是否校验手机号码 | 信任ip登录方式 | 非信任ip登录方式 |  remoteIp的值
     *  ------  -------------  -------------   --------------   ------------   --------------   -------------
     * 场景 一 |      N       |     -        |        -       |   用户名+密码  |   用户名+密码    |       -
     * 场景 二 |      Y       |     N        |        N       |   用户名+密码  |   用户名+密码    |       -
     * 场景 三 |      Y       |     Y        |        N       |   用户名+密码  |    无法登录      |  登录用户的ip
     * 场景 四 |      Y       |     N        |        Y       |   用户名+密码  |   用户名+密码    |  默认配置的10.10.10.10这个ip
     * 场景 五 |      Y       |     Y        |        Y       |   用户名+密码  |   手机号码+验证码 |  信任ip(为登录用户的ip)，非信任ip(10.10.10.10)
     *
     * 场景 一. 关闭企业信任ip
     * a. remoteIp 可以随便填写，因为网关不会对不开启企业信任ip功能的remoteIp进行校验。
     *
     * 场景 二. 当企业信任ip功能打开时，但是既没有添加任何的信任ip(此时ip信任列表为空)，也没有添加任何接收验证手机号码。
     * a. 此时任何用户采用用户名+密码 --> 登录成功，且能够正常的发送短彩信
     * b. 此时无法通过手机号+验证码进行登录(没有添加任何接收验证手机号码，发给谁？？？) --> 无法使用该方式登录
     * c. remoteIp可以任意填写，因为当ip信任列表为空时，网关不会去校验这个remoteIp
     *
     * 场景 三. 当企业信任ip功能打开时，添加了至少一个信任ip(此时ip信任列表不为空)，但是没有添加任何接收验证手机号码。
     * a. 此时只有用户在客户端的ip在信任ip列表中，这些用户账号才能够通过采用用户名+密码 --> 登录成功，且能够正常发送短信
     * b. 此时无法通过手机号+验证码进行登录(没有添加任何接收验证手机号码，发给谁？？？) --> 无法使用该方式登录
     * c. remoteIp必须是客户端的Ip，可以通过NetUtils#checkUserTrueIp方法获取remoteIp的值
     *
     * 场景 四. 当企业信任ip功能打开时，没有手动添加一个信任ip，但是添加了至少一个接收验证手机号码。
     * a. 此时的企业信任ip列表是不为空的，因为添加了第一个接收手机号码时，是会自动添加10.10.10.10这个ip到企业信任ip列表中的。
     * b. 此时所有的用户使用 用户名+密码 --> 登录成功，且能够正常发送短彩信
     * c. 此时所有用户只能采用 手机号码+验证码 --> 页面永远不会以这种方式出现，所以不会采用这个方式进行登录。
     * d. remoteIp为10.10.10.10(如果这个ip不存在，则使用192.188.188.188这个ip)
     *
     * 场景 五. 当企业信任ip功能打开时，添加了至少一个信任ip，而且添加了至少一个接收验证手机号码。
     * a. 此时企业信任ip列表不为可空，同时包含了10.10.10.10这个ip
     * b. 此时用户在客户端的ip在信任ip列表中，能够通过采用用户名+密码 --> 登录成功，且能够正常发送短信 (remoteIp为客户端的真实ip)
     * c. 此时用户通过手机号码+验证码进行登录(接收验证手机号码列表中的手机号) --> 登录成功，且能够正常发送短信 (remoteIp为10.10.10.10，如果这个ip不存在，则使用192.188.188.188这个ip)
     *
     *
     * 程序设计总结：
     * 1. 采用用户名+密码的验证方式进行登录时：
     *    a. 可信任列表为 10.10.10.10 remoteIp为10.10.10.10
     *    b. 可信任列表不为 10.10.10.10 remoteIp为客户端的真实ip即可
     *
     * 2. 采用手机号码+验证码的验证方式登录时：
     *    a. config.getValidateIp有值存在, remoteIp就使用10.10.10.10
     *    b. config.getValidateIp值不存在, remoteIp就使用192.188.188.188
     *
     * @param user 当前登录的用户
     * @param remote 当前登录的用户的客户端的真实ip地址
     * @return 构造好的用户map，用于传到MtClient中登录账号使用
     */
    public Map<String, String> covertAccountMap(SimpleUser user, String remote) {
        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("username", user.getUsername());
        accountMap.put("password", user.getSendMd5Password());

        if (SessionUtil.getLoginType() == LoginTypeEnum.USERNAME_PASSWORD) {
            // 获取当前用户的信任ip列表中仅有10.10.10.10这个ip，那么当使用用户名+密码登录时，将remoteIp设置为10.10.10.10.10
            String trustIps = userService.getTrustIps(SessionUtil.getCurEnterprise().getId(),platformMode.getPlatform(),"");
            if (config.getValidateIp().equals(trustIps)) {
                accountMap.put("remoteIp", config.getValidateIp());
            } else {
                accountMap.put("remoteIp", remote);
            }
        } else if (SessionUtil.getLoginType() == LoginTypeEnum.PHONE_VERIFY_CODE) {
            if (StringUtils.isBlank(config.getValidateIp())) {
                accountMap.put("remoteIp", config.getDefaultValidateIp());
            } else {
                accountMap.put("remoteIp", config.getValidateIp());
            }
        }
        return accountMap;
    }
}
