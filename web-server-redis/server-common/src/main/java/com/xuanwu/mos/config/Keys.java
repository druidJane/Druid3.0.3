package com.xuanwu.mos.config;

/**
 * Created by Administrator on 2017/3/20.
 */
public class Keys {
    //public
    public static final String JSON_DATA = "jsonData";
    // 首页统计
    public static final String HOME_INDEX = "Home/Index";
    public static final String HOME_STATISTICS = "Statistics";
    
    //通道管理
    public static final String CHANNELMGR_SPECSVSNUM = "ChannelMgr/SpecsvsNum";//端口分配
    public static final String CHANNELMGR_SPECSVSNUM_INDEX = "Index";//端口分配
    public static final String CHANNELMGR_SPECSVSNUM_DETAIL = "Detail";//端口分配详细信息
    public static final String CHANNELMGR_SPECSVSNUM_LIST = "GetAllSpecsvsNumBindByEnterpriseID";//查询

    //通讯录管理
    public static final String CONTACTMGR_ECONTACT = "ContactMgr/EContact";//企业通讯录
    public static final String CONTACTMGR_ECONTACT_INDEX = "Index";//查询个人通讯录列表
    public static final String CONTACTMGR_ECONTACT_ADDCONTACT = "AddContact";//添加企业联系人
    public static final String CONTACTMGR_ECONTACT_ADDCONTACTGROUP = "AddContactGroup";//添加企业通讯录
    public static final String CONTACTMGR_ECONTACT_DELETECONTACT = "DeleteContact";//删除企业联系人
    public static final String CONTACTMGR_ECONTACT_DELETECONTACTGROUP = "DeleteContactGroup";//删除企业通讯录
    public static final String CONTACTMGR_ECONTACT_EXPORTCONTACTS = "ExportContacts";//导出企业联系人
    public static final String CONTACTMGR_ECONTACT_IMPORTCONTACTS = "ImportContacts";//导入企业联系人
    public static final String CONTACTMGR_ECONTACT_UPDATECONTACT = "UpdateContact";//修改企业联系人
    public static final String CONTACTMGR_ECONTACT_UPDATECONTACTGROUP = "UpdateContactGroup";//修改企业通讯录

    public static final String CONTACTMGR_PCONTACT = "ContactMgr/PContact";//个人通讯录
    public static final String CONTACTMGR_PCONTACT_INDEX = "Index";//查询个人通讯录列表
    public static final String CONTACTMGR_PCONTACT_IMPORTCONTACTS_TREE = "Index/getPTree";//查询个人通讯录列表
    public static final String CONTACTMGR_PCONTACT_ADDCONTACTGROUP = "AddContactGroup";//添加个人通讯录
    public static final String CONTACTMGR_PCONTACT_UPDATECONTACTGROUP = "UpdateContactGroup";//添加个人通讯录
    public static final String CONTACTMGR_PCONTACT_DELETECONTACTGROUP = "DeleteContactGroup";//删除个人通讯录
    public static final String CONTACTMGR_PCONTACT_ADDCONTACT = "AddContact";//添加个人联系人
    public static final String CONTACTMGR_PCONTACT_UPDATECONTACT = "UpdateContact";//修改个人联系人
    public static final String CONTACTMGR_PCONTACT_DELETECONTACT = "DeleteContact";//删除个人联系人
    public static final String CONTACTMGR_PCONTACT_EXPORTCONTACTS = "ExportContacts";//导出个人联系人
    public static final String CONTACTMGR_PCONTACT_IMPORTCONTACTS = "ImportContacts";//导入个人联系人

    //资料管理
    public static final String INFO_BLACKLIST = "InformationMgr/BlackList";//黑名单
    public static final String INFO_BLACKLIST_INDEX = "Index";//黑名单
    public static final String INFO_BLACKLIST_ADD = "AddBlackList";//增加
    public static final String INFO_BLACKLIST_DEL = "DeleteBlackPhone";//删除
    public static final String INFO_BLACKLIST_EXPORT= "ExportBlackPhone";//导出
    public static final String INFO_BLACKLIST_LIST = "GetAllBlackPhone";//查询
    public static final String INFO_BLACKLIST_IMPORT = "ImportingBlackPhone";//导入
    public static final String INFO_BLACKLIST_UPDATE = "UpdateBlackList";//修改

    /** 业务类型 */
    public static final String INFO_BIZTYPE = "InformationMgr/BusinessType";//业务类型
    public static final String INFO_BIZTYPE_INDEX = "Index";//业务类型
    public static final String INFO_BIZTYPE_DETAIL = "/infomgmt/biztype/detail";
    public static final String INFO_BIZTYPE_ADD = "AddBusinessType";//增加
    public static final String INFO_BIZTYPE_DEL = "DeleteBusinessType";//删除
    public static final String INFO_BIZTYPE_LIST = "GetBusinessTypeList";//查询
    public static final String INFO_BIZTYPE_UPDATE = "UpdateBusinessType";//修改

    public static final String INFO_CARRIERDNSEG = "InformationMgr/CarrierDnSeg";//运营商号段
    public static final String INFORMATIONMGR_CARRIERDNSEG_INDEX="index";
    public static final String INFORMATIONMGR_CARRIERDNSEG_DELETECARRIERDNSEGBYID = "DeleteCarrierDnSegById";//删除
    public static final String INFO_CARRIERDNSEG_LIST= "GetAllCarrierDnSegPhone";//查询
    public static final String INFORMATIONMGR_CARRIERDNSEG_UPDATECARRIERDNSEG = "UpdateCarrierDnSeg";//修改
    public static final String INFORMATIONMGR_ELEMENTINFO_ADDENTERELEMENTCATE = "AddEnterElementCate";//增加
    public static final String INFORMATIONMGR_ELEMENTINFO_ADDPERELEMENTCATE = "AddPerElementCate";//增加


    public static final String INFO_KEYWORD = "InformationMgr/Keyword";//非法关键字
    public static final String INFO_KEYWORD_INDEX = "Index";
    public static final String INFO_KEYWORD_ADD = "AddKeyword";//增加
    public static final String INFO_KEYWORD_DELETE = "DeleteKeyword";//删除
    public static final String INFO_KEYWORD_EXPORT = "ExportKeyword";//导出
    public static final String INFO_KEYWORD_LIST = "GetAllKeyword";//查询
    public static final String INFO_KEYWORD_IMPORT = "KeywordImporting";//导入
    public static final String INFO_KEYWORD_UPDATE = "UpdateKeyword";//修改

    public static final String INFO_WITELISTWEB = "InformationMgr/WiteListweb";//企业白名单管理
    public static final String INFO_WITELISTWEB_INDEX = "Index";
    public static final String INFO_WITELISTWEB_ADD = "Addwhitelist";//增加
    public static final String INFO_WITELISTWEB_DEL = "Deletewhitephone";//删除
    public static final String INFO_WITELISTWEB_LIST = "Getwhitelistweb";//查询
    public static final String INFO_WITELISTWEB_IMPORT = "Importwhitephoneweblist";//导入
    public static final String INFO_WITELISTWEB_UPDATE = "Updatewhitelist";//修改


    //彩信管理
//    public static final String INFORMATIONMGR_ELEMENTINFO = "InformationMgr/ElementInfo";//素材管理
//    public static final String INFORMATIONMGR_ELEMENTINFO_INDEX = "Index";
//    public static final String INFORMATIONMGR_ELEMENTINFO_APPROVAL = "Approval";//素材审批
//    public static final String INFORMATIONMGR_ELEMENTINFO_DELENTERELEMENTCATE = "DelEnterElementCate";//删除
//    public static final String INFORMATIONMGR_ELEMENTINFO_DELPERELEMENTCATE = "DelPerElementCate";//删除
//    public static final String INFORMATIONMGR_ELEMENTINFO_ENTERADDMATERIAL = "EnterAddMaterial";//增加
//    public static final String INFORMATIONMGR_ELEMENTINFO_ENTERDELETEMATERIAL = "EnterDeleteMaterial";//删除
//    public static final String INFORMATIONMGR_ELEMENTINFO_ENTERSEARCHMATERIAL = "EnterSearchMaterial";//查询
//    public static final String INFORMATIONMGR_ELEMENTINFO_PERSONADDMATERIAL = "PersonAddMaterial";//增加
//    public static final String INFORMATIONMGR_ELEMENTINFO_PERSONDELETEMATERIAL = "PersonDeleteMaterial";//删除
//    public static final String INFORMATIONMGR_ELEMENTINFO_PERSONSEARCHMATERIAL = "PersonSearchMaterial";//查询
//    public static final String INFORMATIONMGR_ELEMENTINFO_UPDATEENTERELEMENTCATE = "UpdateEnterElementCate";//修改
//    public static final String INFORMATIONMGR_ELEMENTINFO_UPDATEPERELEMENTCATE = "UpdatePerElementCate";//修改


    public static final String INFORMATIONMGR_TEMPLATE = "InformationMgr/Template";//模板管理
    public static final String INFORMATIONMGR_TEMPLATE_INDEX = "Index";
    public static final String INFORMATIONMGR_TEMPLATE_CREATE = "Create";//添加
    public static final String INFORMATIONMGR_TEMPLATE_DELETE = "Delete";//删除
    public static final String INFORMATIONMGR_TEMPLATE_EDIT = "Edit";//编辑
    public static final String INFORMATIONMGR_TEMPLATE_LIST = "List";//查询

    //region 彩信发送
    public static final String SMSMGR_SENDMMS = "SmsMgr/SendMms";//发送彩信
    public static final String UPLOADFILE = "UploadFile"; // 上传文件
    public static final String SMSMGR_SENDMMS_INDEX = "Index";  // 控制前台页面显示
    public static final String SMSMGR_SENDMMS_DOSENDMMS = "DoSendMms";//发送
    //endregion

    //region 审核彩信
    public static final String SMSMGR_SENDPENDINGMMS = "SmsMgr/SendPendingMms";// 彩信审核
    public static final String SMSMGR_SENDPENDINGMMS_LOADWAITBATCHS = "LoadWaitBatchs";// 彩信审核查询
    public static final String SMSMGR_SENDPENDINGMMS_CHECKBATCH = "CheckBatch";// 彩信审核查询
    //endregion

    //region 发送记录
    public static final String SMSMGR_SENDTRACKINGMMS = "SmsMgr/SendTrackingMms";//发送记录
    public static final String SMSMGR_SENDTRACKINGMMS_BATCHHISTORY = "BatchHistory";//查询
    public static final String SMSMGR_SENDTRACKINGMMS_LOADBATCHS = "LoadBatchs";//批次查询
    public static final String SMSMGR_SENDTRACKINGMMS_CANCLEBATCH = "CancleBatch";//取消发送
    public static final String SMSMGR_SENDTRACKINGMMS_EXPORTBATCH = "ExportBatchs";//导出
    //endregion


    public static final String SMSMGR_SENDTRACKINGMMS_LOADNUMBERS = "LoadNumbers";//号码查询


    public static final String SMSMGR_SENDTRACKINGMMS_CHECKBATCH = "CheckBatch";//批次审批
    public static final String SMSMGR_SENDTRACKINGMMS_INDEX = "Index";
    public static final String SMSMGR_SENDTRACKINGMMS_CLICKMMSEXPORTWEB = "ExportBatchs";//导出
    public static final String SMSMGR_SENDTRACKINGMMS_LOADWAITBATCHS = "LoadWaitBatchs";//查询
    // 导出彩信记录管理权限
    public static final String SEND_RECORD_MMS_CLICK = "SmsMgr/SendTrackingMms/clicksmsexportweb";
    // 取消彩信发送的管理权限
    public static final String SEND_MMS_CANCLE = "SmsMgr/SendTrackingMms/CancleBatch";

    //短信管理 普通模板
    public static final String INFO_COMMONTEMPLATE= "SmsMgr/SmsTemplate";//模板管理
    public static final String INFO_COMMONTEMPLATE_INDEX = "CommonTemplate";
    public static final String INFO_COMMONTEMPLATE_ADD = "CommonTemplateAdd";//增加
    public static final String INFO_COMMONTEMPLATE_DEL = "CommonTemplateDel";//删除
    public static final String INFO_COMMONTEMPLATE_LIST = "CommonTemplateList";//查询
    public static final String INFO_COMMONTEMPLATE_UPDATE = "CommonTemplateUpdate";//修改
    //短信管理 变量模板
    public static final String INFO_VARIANTTEMPLATE = "SmsMgr/SmsTemplate";//模板管理
    public static final String INFO_VARIANTTEMPLATE_INDEX = "VariantTemplate";
    public static final String INFO_VARIANTTEMPLATE_ADD = "VariantTemplateAdd";//增加
    public static final String INFO_VARIANTTEMPLATE_DEL = "VariantTemplateDel";//删除
    public static final String INFO_VARIANTTEMPLATE_LIST = "VariantTemplateList";//查询
    public static final String INFO_VARIANTTEMPLATE_UPDATE = "VariantTemplateUpdate";//修改





    public static final String SMSMGR_INBOX = "SmsMgr/Inbox";//接收短信
    public static final String SMSMGR_INBOX_INDEX = "Index";
    public static final String SMSMGR_INBOX_EXPORTINBOX = "ExportInbox";//导出
    public static final String SMSMGR_INBOX_GETSMSLIST = "GetSMSlist";//查询
    public static final String SMSMGR_INBOX_REPLYSMS = "ReplySMS";//回复
    public static final String SMSMGR_INBOX_UPDATEINBOX = "UpdateInbox";//设为未/已读

    public static final String SMSMGR_SENDSMS = "SmsMgr/SendSms";//发送短信
    public static final String SMSMGR_SENDSMS_INDEX = "Index";
    public static final String SMSMGR_SENDSMS_COMMONSMS_SEND = "CommonSmsSend";//发送普通短信
    public static final String SMSMGR_SENDSMS_VARIANTSMS_SEND = "VariantSmsSend";//发送变量短信

    public static final String SMSMGR_SENDTRACKING = "SmsMgr/SendTracking";//发送记录
    public static final String SMSMGR_SENDTRACKING_INDEX = "Index";
    public static final String SMSMGR_SENDTRACKING_BATCHHISTORY = "NumberHistory";//原【号码记录】，现【发送详情】
    public static final String SMSMGR_SENDTRACKING_CANCLEBATCH = "CancleBatch";//取消发送
    public static final String SMSMGR_SENDTRACKING_CHECKBATCH = "CheckBatch";//审批
    public static final String SMSMGR_SENDTRACKING_CLICKSMSEXPORTWEB = "ExportBatchs";//导出
    public static final String SMSMGR_SENDTRACKING_RESEND = "ReSend";//失败重发
    public static final String SMSMGR_SENDTRACKING_LOADBATCHS = "LoadBatchs";//查询
    public static final String SMSMGR_SENDTRACKING_LOADNUMBERS = "LoadNumbers";//查询
    public static final String SMSMGR_SENDTRACKING_LOADWAITBATCHS = "LoadWaitBatchs";//查询

    public static final String SMSMGR_SENDPENDING = "SmsMgr/SendPending";//短信管理——>审核短信 by jiangziyuan
    public static final String SMSMGR_SENDPENDING_INDEX = "Index";//短信管理——>审核短信——>显示 by jiangziyuan
    public static final String SMSMGR_SENDPENDING_LOADWAITBATCHS = "LoadWaitBatchs";//短信管理——>审核短信——>查询 by jiangziyuan
    public static final String SMSMGR_SENDPENDING_CHECKBATCH = "CheckBatch";//短信管理——>审核短信——>审批 by jiangziyuan

    public static final String SMSMGR_SENDINTERNATIONALSMS = "SmsMgr/SendSms";//国际短信发送
    public static final String SMSMGR_SENDINTERNATIONALSMS_INDEX = "InternationalSms";
    public static final String SMSMGR_SENDINTERNATIONALSMS_DOSENDSMSFORINTERNATIONAL = "DoSendSmsForInternational";//国际短信发送

    public static final String PUBLIC_SEND_IMPORT = "sendImport";//【发送短信】——导入文件
    //public static final String SMSMGR_SENDTRACKING_BATCHHISTORY = "SmsMgr/SendTracking/BatchHistory";//短信批次历史
    public static final String SMS_SEND_HIDE_CONTENT = "/SmsMgr/SendTracking/hideContent";// 短信内容隐藏
    public static final String SMS_SEND_HIDE_PHONE = "/smsmgmt/sendtracking/hidePhone";// 短信号码隐藏
    public static final String MMS_SEND_HIDE_PHONE = "/mmsmgmt/sendtracking/hidePhone";// 彩信号码隐藏



    //报表统计
    public static final String STATISTICS_BILLINGACCOUNTSTATISTICS = "Statistics/BillingAccountStatistics";//计费账户统计
    public static final String STATISTICS_BILLINGACCOUNTSTATISTICS_INDEX = "Index";
    public static final String STATISTICS_BILLINGACCOUNTSTATISTICS_GETACCOUNT = "Index/GetAccount";//
    public static final String STATISTICS_BILLINGACCOUNTSTATISTICS_EXPORTBILLINGACCOUNTSTATISTICS = "ExportBillingAccountStatistics";//导出
    public static final String STATISTICS_BILLINGACCOUNTSTATISTICS_GETBILLINGACCOUNTSTATISTICS = "GetBillingAccountStatistics";//查询
    public static final String STATISTICS_BILLINGACCOUNTSTATISTICS_GETDETAILSTAT = "detail";// 计费帐户详情查询
    public static final String STATISTICS_BILLINGACCOUNTSTATISTICS_EXPORTDETAIL = "Index/export";// 导出计费帐户详情
    public static final String STATISTICS_BILLINGACCOUNTSTATISTICS_USERDETAIL = "Index/userDetail";// 用户消费情况
    

    public static final String STATISTICS_SUMSTATISTICS = "Statistics/SumStatistics";//总量统计
    public static final String STATISTICS_SUMSTATISTICS_INDEX = "Index";
	public static final String STATISTICS_SUMSTATISTICS_DEPARTMENT_INDEX = "SumDepartment"; //部门统计
    public static final String STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT = "GetSumDepartment";//部门统计查询
    public static final String STATISTICS_SUMSTATISTICS_EXPORTDEPARTMENT = "ExportDepartment";//部门统计导出
    public static final String STATISTICS_SUMSTATISTICS_GETDEPTTREE = "SumDepartment/GetDeptTree";//部门统计树形菜单
    
    public static final String STATISTICS_SUMSTATISTICS_ACCOUNT_INDEX = "SumAccount"; //用户统计
    public static final String STATISTICS_SUMSTATISTICS_GETSUMACCOUNT = "GetSumAccount";//用户统计查询
    public static final String STATISTICS_SUMSTATISTICS_EXPORTACCOUNT = "ExportAccount";//用户统计导出
    public static final String STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSERLIST = "singleUserList";//用户详情查询
    public static final String STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSEREXPORT = "SumAccount/singleUserExport";//用户详情导出 
    public static final String STATISTICS_SUMSTATISTICS_USERSELECT = "SumAccount/UserSelect";//查询页面加载用户
    public static final String STATISTICS_SUMSTATISTICS_DEPTSELECT = "SumAccount/DeptSelect";//查询页面加载部门
   
    public static final String STATISTICS_SUMSTATISTICS_BUSINESS_INDEX = "SumBusinessType";//业务类型统计
    public static final String STATISTICS_SUMSTATISTICS_GETSUMBUSINESSTYPE = "GetSumBusinessType";//业务类型查询
    public static final String STATISTICS_SUMSTATISTICS_EXPORTBUSINESSTYPE = "ExportBusinessType";//业务类型导出
    public static final String STATISTICS_SUMSTATISTICS_BUSITYPE = "SumBusinessType/BusiSelect";//业务类型统计页面加载业务类型下拉数据
    
    public static final String STATISTICS_SUMSTATISTICS_CHANNEL_INDEX = "SumChannel";//通道统计
    public static final String STATISTICS_SUMSTATISTICS_GETSUMCHANNEL = "GetSumChannel";//通道统计查询
    public static final String STATISTICS_SUMSTATISTICS_EXPORTCHANNEL = "ExportChannel";//通道统计导出

    public static final String STATISTICS_TRENDSTATISTICS = "Statistics/TrendStatistics";//趋势统计
    public static final String STATISTICS_TRENDSTATISTICS_INDEX = "Index";
    public static final String STATISTICS_TRENDSTATISTICS_EXPORTACCOUNT = "ExportAccount";//导出
    public static final String STATISTICS_TRENDSTATISTICS_EXPORTBUSINESSTYPE = "ExportBusinessType";//导出
    public static final String STATISTICS_TRENDSTATISTICS_EXPORTCHANNEL = "ExportChannel";//导出
    public static final String STATISTICS_TRENDSTATISTICS_EXPORTDEPARTMENT = "ExportDepartment";//导出
    public static final String STATISTICS_TRENDSTATISTICS_GETTRENDACCOUNT = "GetTrendAccount";//查询
    public static final String STATISTICS_TRENDSTATISTICS_GETTRENDBUSINESSTYPE = "GetTrendBusinessType";//查询
    public static final String STATISTICS_TRENDSTATISTICS_GETTRENDCHANNEL = "GetTrendChannel";//查询
    public static final String STATISTICS_TRENDSTATISTICS_GETTRENDDEPARTMENT = "GetTrendDepartment";//查询

    //系统管理
    public static final String SYSTEMMGR_ACCOUNTMGR = "SystemMgr/AccountMgr";//用户管理
    public static final String SYSTEMMGR_ACCOUNTMGR_INDEX = "Index";
    public static final String SYSTEMMGR_ACCOUNTMGR_ACCOUNT_DETAIL = "Index/AccountDetail";
    public static final String SYSTEMMGR_ACCOUNTMGR_ADDACCOUNT = "AddAccount";//用户增加
    public static final String SYSTEMMGR_ACCOUNTMGR_ADDACCOUNT_PRE = "AddAccount/Pre";//增加用户预处理
    public static final String SYSTEMMGR_ACCOUNTMGR_DELETEUSER = "DeleteUser";//用户删除
    public static final String SYSTEMMGR_ACCOUNTMGR_GETUSERLIST = "GetUserList";//用户查询
    public static final String SYSTEMMGR_ACCOUNTMGR_UPDATEACCOUNT = "UpdateAccount";//用户修改
    public static final String SYSTEMMGR_ACCOUNTMGR_UPDATEACCOUNT_PRE = "UpdateAccount/Pre";//修改用户预处理
    public static final String SYSTEMMGR_ACCOUNTMGR_USEREXPORT = "UserExport";//用户导出
    public static final String SYSTEMMGR_ACCOUNTMGR_USERIMPORTING = "UserImporting";//用户导入
    public static final String SYSTEMMGR_ACCOUNTMGR_GETDEPTS = "DeptList"; //部门查询
    public static final String SYSTEMMGR_ACCOUNTMGR_GETDEPTS_DEPTTREE = "Index/DeptTree"; //部门树
    public static final String SYSTEMMGR_ACCOUNTMGR_ADDDEPARTMENT = "AddDepartment"; //部门新增
    public static final String SYSTEMMGR_ACCOUNTMGR_ADDDEPARTMENT_PRE = "AddDepartment/Pre";//部门增加预处理
    public static final String SYSTEMMGR_ACCOUNTMGR_DELDEPARTMENT = "DelDepartment"; //部门删除
    public static final String SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT = "UpdateDepartment"; //部门修改
    public static final String SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT_CONFIRM = "UpdateDepartment/Confirm";//修改
    public static final String SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT_PRE = "UpdateDepartment/Pre";//修改部门预处理

    public static final String SYSTEMMGR_ANNOUNCEMENTMGR = "SystemMgr/AnnouncementMgr";//公告管理
    public static final String SYSTEMMGR_ANNOUNCEMENTMGR_ADDANNOUNCEMENT = "AddAnnouncement";//新增
    public static final String SYSTEMMGR_ANNOUNCEMENTMGR_DELANNOUNCEMENT = "DelAnnouncement";//删除
    public static final String SYSTEMMGR_ANNOUNCEMENTMGR_GETALLANNOUNCEMENT = "GetAllAnnouncement";//查询
    public static final String SYSTEMMGR_ANNOUNCEMENTMGR_GETALLANNOUNCEMENT_DETAIL = "GetAllAnnouncement/Detail";//公告详情
    public static final String SYSTEMMGR_ANNOUNCEMENTMGR_MODIFYANNOUNCEMENT = "ModifyAnnouncement";//修改

    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR = "SystemMgr/ChargingAccountMgr";//计费账户
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_INDEX = "Index";
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_ACCOUNTLIST = "GetAccountList";
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_ADDACCOUNT = "AddAccount";//新增账户
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGING = "Charging";//计费充值
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGFORAUTOCHARGING = "ChargingForAutoCharging";//自动充值失败后转人工充值
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD = "ChargingRecord";//充值查询
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_DETAIL = "ChargingRecord/detail";//充值记录查看
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_USERS_DETAIL = "ChargingRecord/usersDetail";//充值记录查看
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_DELETEACCOUNT = "DeleteAccount";//删除账户
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL = "Detail";//查看账户
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL_USERS = "Detail/listUsers";//查看账户
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_EXPORTRECHARGE = "Exportrecharge";//充值记录导出
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_IMPORTINGCHARGING = "ImportingCharging";//导入充值
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_UPDATEACCOUNT = "UpdateAccount";//修改账户
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR = "UserSelector";//包含用户
    public static final String SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR_USERS = "UserSelector/listUsers";//包含用户列表

    public static final String SYSTEMMGR_LOGMGR = "SystemMgr/LogMgr";//日志管理
    public static final String SYSTEMMGR_LOGMGR_INDEX = "Index";
    public static final String SYSTEMMGR_LOGMGR_INDEX_OPERATION_TYPE = "Index/GetOperationType";
    public static final String SYSTEMMGR_LOGMGR_GETLOGLIST = "GetLogList";//查询

    public static final String SYSTEMMGR_PERSONALMGR = "SystemMgr/PersonalMgr";//个人管理
    public static final String SYSTEMMGR_PERSONALMGR_INDEX = "Index";
    public static final String SYSTEMMGR_PERSONALMGR_UPDATE = "Update";//修改个人信息

    public static final String SYSTEMMGR_ROLEMGR = "SystemMgr/RoleMgr";//角色管理
    public static final String SYSTEMMGR_ROLEMGR_INDEX = "Index";
    public static final String SYSTEMMGR_ROLEMGR_INDEX_PERMISSION = "Index/Permission"; //获取权限
    public static final String SYSTEMMGR_ROLEMGR_INDEX_DETAIL = "Index/Detail"; //获取角色详情
    public static final String SYSTEMMGR_ROLEMGR_ADDROLE = "AddRole";//增加
    public static final String SYSTEMMGR_ROLEMGR_DELETEROLE = "DeleteRole";//删除
    public static final String SYSTEMMGR_ROLEMGR_GETALLROLE = "GetAllRole";//查询
    public static final String SYSTEMMGR_ROLEMGR_UPDATEROLE = "UpdateRole";//修改

    public static final String SYSTEMMGR_SYSCONFIG = "SystemMgr/SysConfig";//系统配置
    public static final String SYSTEMMGR_SYSCONFIG_ENTERPRISE = "Enterprise";//企业信息
    public static final String SYSTEMMGR_SYSCONFIG_PARAMTER_CONFIG = "ParameterConfig";//参数设置
    public static final String SYSTEMMGR_SYSCONFIG_PARAMTER_CONFIG_UPDATE = "ParameterConfig/Update";//更新

    public static final String SYSTEMMGR_TASKMGR = "SystemMgr/TaskMgr"; //任务管理
    public static final String SYSTEMMGR_TASKMGR_IMPORT = "UploadTask"; //导入
    public static final String SYSTEMMGR_TASKMGR_EXPORT = "DownloadTask"; //导出
}
