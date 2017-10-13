package com.xuanwu.mos.rest.resource;

import com.alibaba.fastjson.JSONObject;
import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.config.Platform;
import com.xuanwu.mos.domain.entity.*;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.domain.enums.StatusCode;
import com.xuanwu.mos.dto.JsonResp;
import com.xuanwu.mos.dto.QueryParameters;
import com.xuanwu.mos.file.FileType;
import com.xuanwu.mos.file.FileUtil;
import com.xuanwu.mos.file.handler.FileHandler;
import com.xuanwu.mos.file.handler.FileHandlerFactory;
import com.xuanwu.mos.file.handler.RowHandler;
import com.xuanwu.mos.rest.service.ContactService;
import com.xuanwu.mos.rest.service.ContactShareGroupService;
import com.xuanwu.mos.rest.service.UserMgrService;
import com.xuanwu.mos.service.UserService;
import com.xuanwu.mos.utils.*;
import com.xuanwu.mos.vo.ContactVo;
import com.xuanwu.mos.vo.FileParams;
import com.xuanwu.mos.vo.MmsInfoVo;
import com.xuanwu.msggate.common.sbi.entity.MsgPack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by Jiang.Ziyuan on 2017/3/24.
 */
public class BaseSendResource {

    private static final int MAX_ERROR_PHONE_SIZE = 50000;

    private static final String ERROR_PHONE_MSG = "号码格式不正确";

    private FastDateFormat bDayFmt = FastDateFormat.getInstance("yyyy-MM-dd");

    protected Map<String, String> sendingMap = new ConcurrentHashMap<String, String>();

    @Autowired
    private ContactShareGroupService csgs;
    @Autowired
    private UserMgrService userMgrService;
    @Autowired
    private ContactService cs;

    @Autowired
    private ContactShareGroupService contactShareGroupService;

    // 性别(0:女士,1:先生) 出生日期(yyyy-MM-dd)
    protected final String[] keys = { "手机号码", "姓名", "性别", "编号", "出生日期", "备注" };

    protected JsonResp importFile(HttpServletRequest req, Config config, BizDataType bizDataType, FileParams fileParams) {
        String delimiter = fileParams.getDelimiter();
        String oldName = fileParams.getOldName();
        String newName = fileParams.getNewName();
        FileType fileType = FileUtil.getFileType(newName);
        File file = FileUtil.getImportedFile(bizDataType, newName, Config.getContextPath());
        FileHandler fileHandler = FileHandlerFactory.getFileHandler(fileType);

        SimpleRowHandler rowHandler = new SimpleRowHandler(config.getPhonePattern());
        fileHandler.readAll(file.getAbsolutePath(), delimiter, (RowHandler) rowHandler);
        if (rowHandler.getHeader() == null || rowHandler.getHeader().length() == 0) {
            // 文件头或文件内容为空
            return JsonResp.fail(StatusCode.NoExistFileHead.getIndex(),StatusCode.NoExistFileHead.getStateDesc());
        }
        List<String[]> failedList = rowHandler.getFailedPhoneList();
        String failFileName = "";
        if (ListUtil.isNotBlank(failedList)) {
            Charset charset = Charset.defaultCharset();
            if (fileType == FileType.Text || fileType == FileType.Csv) {
                charset = Charset.forName(FileUtil.getCharset(file));
            }
            File failFile = FileUtil.createTmpFile(Config.getContextPath(),
                    fileType.getType());
            failFileName = failFile.getName();
            fileHandler.writeFile(failFile,
                    fileType == FileType.Csv ? Delimiters.COMMA : "：", true,
                    failedList, charset);
        }
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("oldName",oldName);
        resultMap.put("newName",newName);
        resultMap.put("failedFileName",failFileName);
        resultMap.put("delimiter",delimiter);
        resultMap.put("header",rowHandler.getHeader());
        resultMap.put("viewRow",rowHandler.getViewRow());
        resultMap.put("sucCount",rowHandler.getSucCount());
        resultMap.put("totalCount",rowHandler.getTotalCount());
        return JsonResp.success(resultMap);
    }

    /**
     * 加载联系人，及处理文件请求
     *
     * @param contactService
     * @param rs
     *            原始请求列表
     * @param mass
     *            是否群发
     * @param contacts
     *            加载出的联系人（输出）
     * @param fileNames
     *            解析后的文件名及分隔符（输出）
     */
    protected void loadContacts(ContactService contactService,Config config, List<String> rs, boolean mass, List<Contact> contacts, List<String[]> fileNames) {
        SimpleUser user = SessionUtil.getCurUser();
        if (ListUtil.isBlank(rs))
            return;
        List<ContactGroup> cgs = null;
        List<Integer> fgs = null;
        Contact contact = null;
        String[] arr = null;
        List<Integer> ids = null;
        for (String str : rs) {
            char ch = str.charAt(0);
            switch (ch) {
                case 'g': // 通讯录组，如果包含多个组，path由英文逗号分隔
                    if (cgs == null)
                        cgs = new ArrayList<ContactGroup>();
                    for (String path : str.substring(1).split(Delimiters.COMMA)) {
                        ContactGroup cg = new ContactGroup();
                        if (path.endsWith("#")) {
                            cg.setPath(path.substring(0, path.length() - 1));
                            cg.setContainChild(false);
                        } else {
                            cg.setPath(path);
                            cg.setContainChild(true);
                        }
                        cg.setType(0);
                        cg.setUserId(user.getId());
                        cgs.add(cg);
                    }
                    break;
                case 's': // 共享通讯录组，如果包含多个组，path由英文逗号分隔
                    if (cgs == null)
                        cgs = new ArrayList<ContactGroup>();
                    for (String path : str.substring(1).split(Delimiters.COMMA)) {
                        //若选择根节点，获取该企业下所有共享通讯录联系人
                        if("1.".equals(path)){
                            QueryParameters userParams = new QueryParameters();
                            userParams.addParam("enterpriseId", user.getEnterpriseId());
                            //userParams.addParam("userId", user.getId());
                            userParams.addParam("groupId", 1);
                            List<Contact> contactList = csgs.findShareContactDetail(userParams);
                            addContacts(contactList, contacts, mass);
                        }else{
                            QueryParameters params = new QueryParameters();
                            QueryParameters userParams = new QueryParameters();
                            userParams.addParam("platformId", Platform.FRONTKIT.getIndex());
                            userParams.addParam("enterpriseId", user.getEnterpriseId());
                            List<Integer> userIds = new ArrayList<>();
                            List<User> users = userMgrService.listUsers(userParams);
                            for (User u : users) {
                                userIds.add(u.getId());
                            }
                            params.addParam("userIds",userIds);
                            params.addParam("enterpriseId", user.getEnterpriseId());
                            if (path.endsWith("#")) {
                                params.addParam("showChild", false);
                                path = path.substring(0, path.length() - 1);
                            } else {
                                params.addParam("showChild", true);
                            }
                            params.addParam("path", path);
                            ContactShareGroup exist = csgs.findShareContactGroupByPath(params);
                            List<Contact> contactList = new ArrayList<>();
                            params.addParam("groupId",exist.getGroupId());
                            contactList = csgs.findShareContactDetail(params);
                            addContacts(contactList, contacts, mass);
                        }
                    }
                    break;
                case 'w': // 微信会员
                    if (fgs == null)
                        fgs = new ArrayList<Integer>();
                    for (String fansId : str.substring(1).split(Delimiters.COMMA)) {
                        fgs.add(Integer.valueOf(fansId));
                    }
                    break;
                case 'p': // 单个号码
                    contact = new Contact();
                    contact.setPhone(str.substring(1));
                    if (!mass) {
                        arr = new String[1];
                        arr[0] = contact.getPhone();
                        contact.setArray(arr);
                    }
                    contacts.add(contact);
                    break;
                case 'q': // 查询结果（参数）
                    List<Contact> rets = loadContactsByParams(contactService,
                            config, str.substring(1));
                    if (ListUtil.isNotBlank(rets)) {
                        addContacts(rets, contacts, mass);
                    }
                    break;
                case 'f': // 导入文件
                    int idx = 0;
                    idx = str.indexOf('$');
                    if (idx == -1) {
                        continue;
                    }
                    arr = new String[2];// 文件名,分隔符
                    arr[0] = str.substring(1, idx);
                    idx++;
                    if (idx == str.length()) {
                        arr[1] = " ";// 为空时以空格分隔
                    } else {
                        arr[1] = str.substring(idx);
                    }
                    fileNames.add(arr);
                    break;
                default: // 联系人ID
                    if (ids == null) {
                        ids = new ArrayList<Integer>();
                    }
                    ids.add(Integer.parseInt(str));
            }
        }

        // 根据联系人ID列表，找出相应记录
        if (ListUtil.isNotBlank(ids)) {
            List<Contact> contactList = new ArrayList<Contact>();
            Map<Integer, Integer> contactSumMap = new HashMap<Integer, Integer>();
            Iterator<Integer> it = ids.iterator();
            while (it.hasNext()) {
                int contactId = it.next();
                if (contactSumMap.containsKey(contactId)) {
                    Integer tempSum = contactSumMap.get(contactId);
                    tempSum++;
                    contactSumMap.put(contactId, tempSum);
                } else {
                    contactSumMap.put(contactId, 1);
                }
            }

            List<Contact> rets = contactService.findContacts(ids);
            Iterator<Contact> retsIt = rets.iterator();
            while (retsIt.hasNext()) {
                Contact tempContact = retsIt.next();
                contactList.add(tempContact);
                Integer contactSum = contactSumMap.get(tempContact.getId());
                for (int i = 1; i < contactSum; i++) {
                    try {
                        Contact temp = (Contact) tempContact.clone();
                        contactList.add(temp);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
            addContacts(contactList, contacts, mass);
        }

        // 根据联系人组PATH，找出相应记录
        if (ListUtil.isNotBlank(cgs)) {
            List<Contact> rets = contactService.findContacts(cgs, null);
            addContacts(rets, contacts, mass);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Contact> loadContactsByParams(ContactService contactService,
                                               Config config, String params) {
        //前端数据 s 开头为共享通讯录查询，
        char ch = params.charAt(0);
        Boolean isPerson = true;
        if('s' == ch){
            isPerson = false;
        }
        JSONObject paramsMap = JSONObject.parseObject(params.substring(1));
        String paths = (String) paramsMap.get("path");
        DynamicParam dynParam = null;
        boolean showChildren = true;
        if (paramsMap.size() > 1) {
            String temp = null;
            dynParam = new DynamicParam();
            Contact c = new Contact();
            c.setName((String) paramsMap.get("_lk_name"));
            c.setPhone((String) paramsMap.get("_lk_phone"));
            c.setIdentifier((String) paramsMap.get("_lk_identifier"));
            String sex = (String) paramsMap.get("sex");
            String vip = (String) paramsMap.get("vip");
            c.setSex(Integer.valueOf(sex));
            c.setVipVal(Integer.valueOf(vip));
            showChildren = (boolean) paramsMap.get("showChild");
            c.getGroup().setContainChild(showChildren);
            dynParam.setExt(c);
            dynParam.setExt1(DateUtil.parse((String) paramsMap.get("_gt_beginDate"),
                    DateUtil.DateTimeType.Date));
            dynParam.setExt2(DateUtil.parse((String) paramsMap.get("_lt_endDate"),
                    DateUtil.DateTimeType.Date));
        }
        List<ContactGroup> cgs = new ArrayList<ContactGroup>();
        for (String path : paths.split(Delimiters.COMMA)) {
            ContactGroup cg = new ContactGroup();
            if (showChildren) {
                if (path.endsWith("#")) {
                    path = path.substring(0, path.length() - 1);
                    cg.setContainChild(false);
                } else {
                    cg.setContainChild(true);
                }
            } else {
                cg.setContainChild(false);
            }
            SimpleUser user = SessionUtil.getCurUser();
            if(isPerson){
                cg.setUserId(user.getId());
            }else if("1.".equals(path)){
                //若为根目录查询，需要对各个组联系人做去重
                QueryParameters userParams = new QueryParameters();
                userParams.addParam("enterpriseId", user.getEnterpriseId());
                userParams.addParam("_lk_name", paramsMap.get("_lk_name"));
                userParams.addParam("_lk_phone", paramsMap.get("_lk_phone"));
                userParams.addParam("sex", paramsMap.get("sex"));
                userParams.addParam("_gt_beginDate", paramsMap.get("_gt_beginDate"));
                userParams.addParam("_lt_endDate", paramsMap.get("_lt_endDate"));
                userParams.addParam("vip", paramsMap.get("vip"));
                userParams.addParam("_lk_identifier", paramsMap.get("_lk_identifier"));
                userParams.addParam("groupId", 1);
                return csgs.findShareContactDetail(userParams);
            }
            cg.setPath(path);
            cgs.add(cg);
        }
        return contactService.findContacts(cgs, dynParam);
    }

    /**
     * 封装contact.array
     * @param src
     * @param dest
     * @param mass
     */
    private void addContacts(List<Contact> src, List<Contact> dest, boolean mass) {
        if (ListUtil.isBlank(src))
            return;
        if (mass) {
            dest.addAll(src);
            return;
        }
        String[] arr = null;
        for (Contact ret : src) {//protected final String[] keys = {  "发送号码","姓名","手机号码","性别","出生日期","编号","VIP","备注" };
            arr = new String[8];
            arr[0] = ret.getPhone()==null?"":ret.getPhone();
            arr[1] = ret.getName()==null?"":ret.getName();
            arr[2] = ret.getPhone()==null?"":ret.getPhone();
            arr[3] = ret.getSex() > 0 ? "先生" : "女士";
            arr[4] = (ret.getBirthday() == null ? "" : bDayFmt.format(ret
                    .getBirthday()));
            arr[5] = ret.getIdentifier()==null?"":ret.getIdentifier();
            arr[6] = ret.isVip()?"是":"否";
            arr[7] = ret.getRemark()==null?"":ret.getRemark();
            ret.setArray(arr);
            dest.add(ret);
        }
    }

    /**
     * 导入失败文件下载
     *
     * @param response
     * @param fileName
     * @throws Exception
     */
    protected void download(HttpServletResponse response, String fileName)
            throws Exception {
        FileInputStream fis = null;
        ServletOutputStream sos = null;
        try {
            File file = FileUtil.getTmpFile(Config.getContextPath(), fileName);
            response.setContentType(getContentType(FileType.getType(fileName))
                    + "; charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename="
                    + fileName);
            fis = new FileInputStream(file);
            sos = response.getOutputStream();
            int bytesRead = 0;
            byte[] buffer = new byte[8096];
            while ((bytesRead = fis.read(buffer)) != -1)
                sos.write(buffer, 0, bytesRead);
        } finally {
            FileUtil.closeInputStrem(fis);
            FileUtil.closeOutputStrem(sos);
        }
    }

    //region 解析前台传入的文件名称或者是号码
    public String[] preHandlerConcact(MmsInfoVo mmsInfoVo) {
        List<ContactVo> vos = mmsInfoVo.getContactItem();
        String[] contacts = new String[vos.size()];
        for (int i = 0; i < vos.size(); i++) {
            contacts[i] = vos.get(i).getValue();
        }
        return contacts;
    }
    //endregion

    private String getContentType(FileType fileType) {
        switch (fileType) {
            case Zip:
                return "application/zip";
            case Text:
            case Csv:
                return "text/plain";
            case Excel:
                return "application/vnd.ms-excel";
            case ExcelX:
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default:
                return "application/x-msdownload";
        }
    }

    protected boolean checkBizExist(UserService userService, int userId,
                                    int bizId) {
        if (userId <= 0 || bizId <= 0) {
            return false;
        }
        List<BizType> bizs = userService.findBizTypes(userId, 0);
        if (ListUtil.isBlank(bizs)) {
            return false;
        }
        for (BizType biz : bizs) {
            if (bizId == biz.getId()) {
                return true;
            }
        }
        return false;
    }

    protected MTResp checkPack(MsgPack pack) {
        if (pack == null || ListUtil.isBlank(pack.getFrames())) {
            return MTResp.build(MTResult.INVALID_PARAM, "信息包或信息帧为空");
        }
        Object value = pack.getParameter(WebConstants.SEND_SMS_ERROR);
        if(value != null){
            return MTResp.build(MTResult.INVALID_PARAM, (String)value);
        }
        // 只有联系人为空时才会发生
        if (ListUtil.isBlank(pack.getFrames().get(0).getAllMsgSingle())) {
            return MTResp.build(MTResult.INVALID_PARAM, "联系人信息已被删除，号码列表为空");
        }
        return null;
    }

    private class SimpleRowHandler implements RowHandler {

        public SimpleRowHandler(String phonePattern) {
            this.pattern = Pattern.compile(phonePattern);
        }

        private int sucCount = 0;

        private int totalCount = 0;

        private String header;

        public List<String> getViewRow() {
            return viewRow;
        }

        public void setViewRow(List<String> viewRow) {
            this.viewRow = viewRow;
        }

        private List<String> viewRow = new ArrayList<>();

        private StringBuilder sb = new StringBuilder();

        private Pattern pattern;

        private List<String[]> failedPhoneList = new ArrayList<String[]>();

        @Override
        public boolean handleHead(String[] cells) {
            // 转成数组
            sb.delete(0, sb.length());
            sb.append('[');
            for (int i = 1; i < cells.length; i++) {
                //判断txt第一行为空
                if(cells.length == 1 && StringUtils.isEmpty(cells[0])){
                    return false;
                }
                if (i > 1)
                    sb.append(Delimiters.COMMA);
                sb.append('\"').append(StringUtil.fixJsonStr(cells[i]))
                        .append('\"');
            }
            sb.append(']');
            header = sb.toString();
            return true;
        }

        @Override
        public boolean handleRow(String[] cells) {
            if (cells.length > 0) {
                totalCount++;
                if (pattern.matcher(cells[0]).matches()) {
                    if (sucCount <= 10) {
                        // 转成数组
                        sb.delete(0, sb.length());
                        sb.append('[');
                        for (int i = 1; i < cells.length; i++) {
                            if (i > 1)
                                sb.append(Delimiters.COMMA);
                            sb.append('\"')
                                    .append(StringUtil.fixJsonStr(cells[i]))
                                    .append('\"');
                        }
                        sb.append(']');
                    }
                    if(viewRow != null && viewRow.size()<10){
                        viewRow.add(sb.toString());
                    }
                    sucCount++;
                } else {
                    String[] strArray = { cells[0], ERROR_PHONE_MSG };
                    failedPhoneList.add(strArray);
                    if (failedPhoneList.size() >= MAX_ERROR_PHONE_SIZE) {
                        sucCount = 0;
                        return false;
                    }
                }
            }
            return true;
        }

        public String getHeader() {
            if (header == null) {
                return null;
            }
            return header.toString();
        }


        public int getSucCount() {
            return sucCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public List<String[]> getFailedPhoneList() {
            return failedPhoneList;
        }
    }
}
