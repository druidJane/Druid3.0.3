'use strict';
define([], function() {

    /**
     * 路由设置函数，所以这里定义setRoute
     *
     * @param {[type]}　url [模块相对路径]
     * @param {[type]}　ctrl [Controller名称]
     * @param {[type]}　reqJs [模块对应的控制器JS文件]
     * @param {[type]}　label [标签，对应于导航条]
     */
    function setRoute(url, ctrl, reqJs, label) {
        var routeDef = {};
		routeDef.templateUrl = xpath.res("views" + url + ".html?ran=" + Math.random());
        routeDef.controller = ctrl;
        routeDef.resolve = {
            load: ['$q', '$rootScope',
                function($q, $rootScope) {
                    var defer = $q.defer();
                    require([xpath.res("scripts/controllers") + reqJs],
                        function() {
                            defer.resolve();
                            $rootScope.$apply();
                        });
                    return defer.promise;
                }
            ]
        };
        routeDef.label = label;
        return routeDef;
    }

    var app = angular.module('mosApp', ['ngRoute','ngCookies', 'ui.router','angular.filter', 'mgcrea.ngStrap', "treeControl", 'ng-breadcrumbs', 'ngTable', "w5c.validator", "ngFileUpload", "angucomplete", 'ngAnimate', "angucomplete-alt",'ui.sortable','sy.bootstrap.timepicker','mA','ngAudio','com.2fdevs.videogular','com.2fdevs.videogular.plugins.controls','ngSanitize']);
    app.config(["w5cValidatorProvider", function(w5cValidatorProvider) {
        // 全局配置
        w5cValidatorProvider.config({
            blurTrig: true,
            showError: true,
            removeError: true
        });

        w5cValidatorProvider.setRules({
            email: {
                required: "输入的邮箱地址不能为空",
                email: "输入邮箱地址格式不正确"
            },
            username: {
                required: "输入的用户名不能为空",
                pattern: "用户名必须输入字母、数字、下划线,以字母开头",
                w5cuniquecheck: "输入用户名已经存在，请重新输入"
            },
            password: {
                required: "密码不能为空",
                pattern: "密码需为由数字、大小写字母组成的长度为8~20位的字符"
            },
            newPassword: {
                required: "密码不能为空",
                pattern: "密码需为由数字、大小写字母组成的长度为8~20位的字符"
            },
            repeatPassword: {
                required: "重复密码不能为空",
                repeat: "两次密码输入不一致"
            },
            number: {
                required: "数字不能为空"
            },
            accountName: {
                required: "开户名称不能为空",
                repeat: "公司名称与开户名称不一致"
            },
            amount: {
                required: "请输入金额",
                pattern: "请输入有效金额",
                min: "金额不能小于100",
                max: "不能超过可索取发票金额"
            },
            mobile: {
                pattern: "请正确填写手机号码"
            },
            userMgr_userName: {
                pattern: "仅支持输入字符为：英文（不区分大小写）、数字、下划线"
            },
            balanceRemind: {
                pattern: "仅允许输入0-99999999之间的整数值"
            }
        });
    }]);

    app.factory('myA', ['$mAx',(function(a){return a;})]);

	app.factory('myB', ['$mAx',(function(a){return a.gethead();})]);

    app.filter('menuIconFilter',function () {
        return function(text) {
            if (text.indexOf("首页") != -1) {
                return 1;
            } else if (text.indexOf("短信管理") != -1) {
                return 2;
            } else if (text.indexOf("彩信管理") != -1) {
                return 3;
            } else if (text.indexOf("通讯录管理") != -1) {
                return 4;
            } else if (text.indexOf("报表统计") != -1) {
                return 5;
            } else if (text.indexOf("资料管理") != -1) {
                return 6;
            } else if (text.indexOf("系统管理") != -1) {
                return 7;
            } 
        }
    });

    app.config(['$locationProvider', function($locationProvider) {
        $locationProvider.hashPrefix('');
    }]);
    app.config(function($routeProvider, $controllerProvider, $provide, $httpProvider, $compileProvider) {
        $provide.decorator('ngClickDirective',['$delegate','$timeout', function ($delegate,$timeout) {
            var original = $delegate[0].compile;
            var delay = 1000;//设置间隔时间
            $delegate[0].compile = function (element, attrs, transclude) {
                var disabled = false;
                function onClick(evt) {
                    if (disabled) {
                        evt.preventDefault();
                        evt.stopImmediatePropagation();
                    } else {
                        disabled = true;
                        $timeout(function () { disabled = false; }, delay, false);
                    }
                }
                //   scope.$on('$destroy', function () { iElement.off('click', onClick); });
                element.on('click', onClick);

                return original(element, attrs, transclude);
            };
            return $delegate;
        }]);
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|javascript):/);

        var urlPerMap = {
            /**首页管理**/
            HOME_STATISTICS:'/Home/Index/Statistics',
			HOME_PERSONINFO:'/Home/Index/PersonInfo',
            //发送记录

            /**短信管理**/
            //短信管理->短信发送
            SMSMGR_SENDSMS_INDEX :'/SmsMgr/SendSms/DoSendSms',
            SMSMGR_SENDSMS_COMMONSMS_SEND :'/SmsMgr/SendSms/CommonSmsSend',//发送普通短信
            SMSMGR_SENDSMS_VARIANTSMS_SEND :'/SmsMgr/SendSms/VariantSmsSend',//发送变量短信

            //短信管理->发送记录
            SMSMGR_SENDTRACKING : '/SmsMgr/SendTracking',
            SMSMGR_SENDTRACKING_INDEX : '/SmsMgr/SendTracking/Index', //【发送记录】
            SMSMGR_SENDTRACKING_LOADBATCHS : '/SmsMgr/SendTracking/LoadBatchs', //【发送记录】
            SMSMGR_SENDTRACKING_BATCHHISTORY : '/SmsMgr/SendTracking/NumberHistory',//发送详情
            SMSMGR_SENDTRACKING_CLICKSMSEXPORTWEB : '/SmsMgr/SendTracking/ExportBatchs',//发送记录导出
            SMSMGR_SENDTRACKING_RESEND : '/SmsMgr/SendTracking/ReSend',//发送记录失败重发
            SMSMGR_SENDTRACKING_CANCLEBATCH : '/SmsMgr/SendTracking/CancleBatch',//发送记录 取消发送
            SMSMGR_INBOX_INDEX : '/SmsMgr/Inbox/Index',//接收短信
            SMSMGR_INBOX_GETSMSLIST : '/SmsMgr/Inbox/GetSMSlist',//查询回复
            SMSMGR_INBOX_REPLYSMS : '/SmsMgr/Inbox/ReplySMS',//回复信息
            SMSMGR_INBOX_EXPORTINBOX : '/SmsMgr/Inbox/ExportInbox',//导出接收短信

            //短信管理->审核短信
            SMSMGR_SENDPENDING : '/SmsMgr/SendPending',
            SMSMGR_SENDPENDING_INDEX : '/SmsMgr/SendPending/Index', //【审核短信】对应原mos【发送记录】的【待发送】页面
            SMSMGR_SENDPENDING_CHECKBATCH : '/SmsMgr/SendPending/CheckBatch',  //审核
            SMSMGR_SENDPENDING_LOADWAITBATCHS : '/SmsMgr/SendPending/LoadWaitBatchs',  //查询

            //短信管理->模板管理 普通模板
            INFO_SMSTEMPLATE_INDEX : '/SmsMgr/SmsTemplate/Index',   //短信模板
            INFO_COMMONTEMPLATE_INDEX : '/SmsMgr/SmsTemplate/CommonTemplate',   //短信模板
            INFO_COMMONTEMPLATE_ADD : '/SmsMgr/SmsTemplate/CommonTemplateAdd',  //新增模板
            INFO_COMMONTEMPLATE_DEL : '/SmsMgr/SmsTemplate/CommonTemplateDel', //删除模板
            INFO_COMMONTEMPLATE_LIST : '/SmsMgr/SmsTemplate/CommonTemplateList',   //查询模板
            INFO_COMMONTEMPLATE_UPDATE : '/SmsMgr/SmsTemplate/CommonTemplateUpdate',   //修改模板
            //短信管理->模板管理 变量模板
            INFO_VARIANTTEMPLATE_INDEX : '/SmsMgr/SmsTemplate/VariantTemplate',   //短信模板
            INFO_VARIANTTEMPLATE_ADD : '/SmsMgr/SmsTemplate/VariantTemplateAdd',  //新增模板
            INFO_VARIANTTEMPLATE_DEL : '/SmsMgr/SmsTemplate/VariantTemplateDel', //删除模板
            INFO_VARIANTTEMPLATE_LIST : '/SmsMgr/SmsTemplate/VariantTemplateList',   //查询模板
            INFO_VARIANTTEMPLATE_UPDATE : '/SmsMgr/SmsTemplate/VariantTemplateUpdate',   //修改模板
            //彩信管理->发送彩信
            SMSMGR_SENDMMS_INDEX: '/SmsMgr/SendMms/Index',
            SEND_MMS_DO_SEND:'/SmsMgr/SendMms/DoSendMms',

            //彩信管理->审核记录
            SEND_MMS_AUDIT_INDEX:'/SmsMgr/SendPendingMms/Index',
            SEND_MMS_AUDIT_QUERY:'/SmsMgr/SendPendingMms/LoadWaitBatchs',
            SEND_MMS_AUDIT_BUTTON:'/SmsMgr/SendPendingMms/CheckBatch',

            //彩信管理->发送记录
            MMS_SEND_TRACE_INDEX:'/SmsMgr/SendTrackingMms/Index',
            MMS_SEND_RECORD_INDEX:'/SmsMgr/SendTrackingMms/BatchHistory',
            MMS_SEND_RECORD_QUERY:'/SmsMgr/SendTrackingMms/LoadBatchs',
            MMS_SEND_RECORD_CANCEL:'/SmsMgr/SendTrackingMms/CancleBatch',
            MMS_SEND_RECORD_EXPORT:'/SmsMgr/SendTrackingMms/ExportBatchs',

            //彩信管理->发送详情
            MMS_SEND_DETAIL_INDEX:'/SmsMgr/SendTrackingMms/NumberHistory',
            MMS_SEND_DETAIL_QUERY:'/SmsMgr/SendTrackingMms/LoadNumbers',
            MMS_SEND_DETAIL_EXPORT:'/SmsMgr/SendTrackingMms/ExportNumbers',


            // 彩信管理->模板管理
            INFO_TEMPLATE_INDEX: '/InformationMgr/Template/Index',
            INFO_TEMPLATE_LIST: '/InformationMgr/Template/List',
            INFO_TEMPLATE_CREATE: '/InformationMgr/Template/Create',     // 新增
            INFO_TEMPLATE_MODIFY: '/InformationMgr/Template/Edit',         //修改
            INFO_TEMPLATE_DELETE: '/InformationMgr/Template/Delete',

            SEND_MMS_TRACE_PAGE:'/SmsMgr/SendTrackingMms/ExportBatchs',
            SEND_MMS_TRACE_CLICK:'/SmsMgr/SendTrackingMms/clickmmsexportweb',



            /**系统管理**/
            //日志管理
            SYSTEMMGR_LOGMGR_INDEX_OPERATION_TYPE : '/SystemMgr/LogMgr/Index/GetOperationType',
            SYSTEMMGR_LOGMGR_INDEX : '/SystemMgr/LogMgr/Index',
            SYSTEMMGR_LOGMGR_LIST : '/SystemMgr/LogMgr/GetLogList',
            //系统配置
            SYSTEMMGR_SYSCONFIG : '/SystemMgr/SysConfig',
            SYSTEMMGR_SYSCONFIG_INDEX : '/SystemMgr/SysConfig/Index',
            SYSTEMMGR_SYSCONFIG_ENTERPRISE : '/SystemMgr/SysConfig/Enterprise',
            SYSTEMMGR_SYSCONFIG_PARAMTER_CONFIG : '/SystemMgr/SysConfig/ParameterConfig',
            //任务管理
            SYSTEMMGR_TASKMGR : "/SystemMgr/TaskMgr",
            SYSTEMMGR_TASKMGR_INDEX : "/SystemMgr/TaskMgr/Index",
            SYSTEMMGR_TASKMGR_IMPORT : "/SystemMgr/TaskMgr/UploadTask",
            SYSTEMMGR_TASKMGR_EXPORT : "/SystemMgr/TaskMgr/DownloadTask",
            //公告管理
            SYSTEMMGR_ANNOUNCEMENTMGR_INDEX : '/SystemMgr/AnnouncementMgr/Index',
            SYSTEMMGR_ANNOUNCEMENTMGR_GETALLANNOUNCEMENT_DETAIL : '/SystemMgr/AnnouncementMgr/GetAllAnnouncement/Detail',
            SYSTEMMGR_ANNOUNCEMENTMGR_GETALLANNOUNCEMENT : '/SystemMgr/AnnouncementMgr/GetAllAnnouncement',
            SYSTEMMGR_ANNOUNCEMENTMGR_ADDANNOUNCEMENT : '/SystemMgr/AnnouncementMgr/AddAnnouncement',
            SYSTEMMGR_ANNOUNCEMENTMGR_MODIFYANNOUNCEMENT : '/SystemMgr/AnnouncementMgr/ModifyAnnouncement',
            SYSTEMMGR_ANNOUNCEMENTMGR_DELANNOUNCEMENT : '/SystemMgr/AnnouncementMgr/DelAnnouncement',
            //角色管理
            SYSTEMMGR_ROLEMGR_INDEX: '/SystemMgr/RoleMgr/Index',
            SYSTEMMGR_ROLEMGR_INDEX_PERMISSION: '/SystemMgr/RoleMgr/Index/Permission',
            SYSTEMMGR_ROLEMGR_GETALLROLE: '/SystemMgr/RoleMgr/GetAllRole',
            SYSTEMMGR_ROLEMGR_ADDROLE: '/SystemMgr/RoleMgr/AddRole',
            SYSTEMMGR_ROLEMGR_UPDATEROLE: '/SystemMgr/RoleMgr/UpdateRole',
            SYSTEMMGR_ROLEMGR_DELETEROLE: '/SystemMgr/RoleMgr/DeleteRole',
            //计费账户
            SYSTEMMGR_CHARGINGACCOUNTMGR_INDEX: '/SystemMgr/ChargingAccountMgr/Index',
            SYSTEMMGR_CHARGINGACCOUNTMGR_ACCOUNTLIST: '/SystemMgr/ChargingAccountMgr/GetAccountList',
            SYSTEMMGR_CHARGINGACCOUNTMGR_EXPORTRECHARGE: '/SystemMgr/ChargingAccountMgr/Exportrecharge',
            SYSTEMMGR_CHARGINGACCOUNTMGR_ADDACCOUNT: '/SystemMgr/ChargingAccountMgr/AddAccount',
            SYSTEMMGR_CHARGINGACCOUNTMGR_AUTOCHARGING: '/SystemMgr/ChargingAccountMgr/AutoCharging',
            SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGING: '/SystemMgr/ChargingAccountMgr/Charging',
            SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD: '/SystemMgr/ChargingAccountMgr/ChargingRecord',
            SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_DETAIL: '/SystemMgr/ChargingAccountMgr/ChargingRecord/detail',
            SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_USERS_DETAIL: '/SystemMgr/ChargingAccountMgr/ChargingRecord/usersDetail',
            SYSTEMMGR_CHARGINGACCOUNTMGR_DELETEACCOUNT: '/SystemMgr/ChargingAccountMgr/DeleteAccount',
            SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL: '/SystemMgr/ChargingAccountMgr/Detail',
            SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL_USERS: '/SystemMgr/ChargingAccountMgr/Detail/listUsers',
            SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL_ROLES: '/SystemMgr/ChargingAccountMgr/Detail/getRoles',
            SYSTEMMGR_CHARGINGACCOUNTMGR_IMPORTINGCHARGING: '/SystemMgr/ChargingAccountMgr/ImportingCharging',
            SYSTEMMGR_CHARGINGACCOUNTMGR_UPDATEACCOUNT: '/SystemMgr/ChargingAccountMgr/UpdateAccount',
            SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR: '/SystemMgr/ChargingAccountMgr/UserSelector',
            SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR_USERS: '/SystemMgr/ChargingAccountMgr/UserSelector/listUsers',
            SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR_TREE: '/SystemMgr/ChargingAccountMgr/UserSelector/deptTree',
            SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGFORAUTOCHARGING: '/SystemMgr/ChargingAccountMgr/ChargingForAutoCharging',
            //用户管理
            SYSTEMMGR_ACCOUNTMGR_INDEX: '/SystemMgr/AccountMgr/Index',
            SYSTEMMGR_ACCOUNTMGR_ADDACCOUNT: '/SystemMgr/AccountMgr/AddAccount',
            SYSTEMMGR_ACCOUNTMGR_UPDATEACCOUNT: '/SystemMgr/AccountMgr/UpdateAccount',
            SYSTEMMGR_ACCOUNTMGR_DELETEUSER: '/SystemMgr/AccountMgr/DeleteUser',
            SYSTEMMGR_ACCOUNTMGR_USERIMPORTING: '/SystemMgr/AccountMgr/UserImporting',
            SYSTEMMGR_ACCOUNTMGR_USEREXPORT: '/SystemMgr/AccountMgr/UserExport',
            SYSTEMMGR_ACCOUNTMGR_GETUSERLIST: '/SystemMgr/AccountMgr/GetUserList',
            SYSTEMMGR_ACCOUNTMGR_GETDEPTS: '/SystemMgr/AccountMgr/DeptList',
            SYSTEMMGR_ACCOUNTMGR_GETDEPTS_DEPTTREE: '/SystemMgr/AccountMgr/Index/DeptTree',
            SYSTEMMGR_ACCOUNTMGR_DEPT_DETAIL: '/SystemMgr/AccountMgr/DeptDetail',
            SYSTEMMGR_ACCOUNTMGR_ADDDEPARTMENT: '/SystemMgr/AccountMgr/AddDepartment',
            SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT: '/SystemMgr/AccountMgr/UpdateDepartment',
            SYSTEMMGR_ACCOUNTMGR_DELDEPARTMENT: '/SystemMgr/AccountMgr/DelDepartment',
            SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT_CONFIRM: '/SystemMgr/AccountMgr/UpdateDepartment/Confirm',


            /**资料管理**/
            //黑名单
            INFO_BLACKLIST_INDEX:'/InformationMgr/BlackList/Index',
            INFO_BLACKLIST_LIST:'/InformationMgr/BlackList/GetAllBlackPhone',
            INFO_BLACKLIST_ADD:'/InformationMgr/BlackList/AddBlackList',
            INFO_BLACKLIST_DEL:'/InformationMgr/BlackList/DeleteBlackPhone',
            INFO_BLACKLIST_IMPORT:'/InformationMgr/BlackList/ImportingBlackPhone',
            INFO_BLACKLIST_EXPORT:'/InformationMgr/BlackList/ExportBlackPhone',
            //运营商号码段
            INFOR_CARRIERDNSEG_INDEX:'/InformationMgr/CarrierDnSeg/Index',
            INFOR_CARRIERDNSEG_LIST:'/InformationMgr/CarrierDnSeg/GetAllCarrierDnSegPhone',
            //企业白名单
            INFO_WITELISTWEB_INDEX:'/InformationMgr/WiteListweb/Index',
            INFO_WITELISTWEB_LIST:'/InformationMgr/WiteListweb/Getwhitelistweb',
            INFO_WITELISTWEB_ADD:'/InformationMgr/WiteListweb/Addwhitelist',
            INFO_WITELISTWEB_DEL:'/InformationMgr/WiteListweb/Deletewhitephone',
            INFO_WITELISTWEB_IMPORT:'/InformationMgr/WiteListweb/Importwhitephoneweblist',
            //关键字
            INFO_KEYWORD_INDEX:'/InformationMgr/Keyword/Index',
            INFO_KEYWORD_ADD:'/InformationMgr/Keyword/AddKeyword',
            INFO_KEYWORD_DEL:'/InformationMgr/Keyword/DeleteKeyword',
            INFO_KEYWORD_EXPORT:'/InformationMgr/Keyword/ExportKeyword',
            INFO_KEYWORD_LIST:'/InformationMgr/Keyword/GetAllKeyword',
            INFO_KEYWORD_IMPORT:'/InformationMgr/Keyword/KeywordImporting',
            INFO_KEYWORD_UPDATE:'/InformationMgr/Keyword/UpdateKeyword',
            //业务类型
            INFO_BIZTYPE_INDEX:'/InformationMgr/BusinessType/Index',
            INFO_BIZTYPE_ADD:'/InformationMgr/BusinessType/AddBusinessType',
            INFO_BIZTYPE_DELETE:'/InformationMgr/BusinessType/DeleteBusinessType',
            INFO_BIZTYPE_LIST:'/InformationMgr/BusinessType/GetBusinessTypeList',
            INFO_BIZTYPE_UPDATE:'/InformationMgr/BusinessType/UpdateBusinessType',
            INFO_BIZTYPE_DETAIL:'/InformationMgr/BusinessType/UpdateBusinessType/Detail',

            /**通讯录管理**/
            //个人通讯录 PCONTACT
            CONTACTMGR_PCONTACT_INDEX:'/ContactMgr/PContact/Index',
            CONTACTMGR_PCONTACT_ADDCONTACT:'/ContactMgr/PContact/AddContact',
            CONTACTMGR_PCONTACT_UPDATECONTACT:'/ContactMgr/PContact/UpdateContact',
            CONTACTMGR_PCONTACT_DELETECONTACT:'/ContactMgr/PContact/DeleteContact',
            CONTACTMGR_PCONTACT_ADDCONTACTGROUP:'/ContactMgr/PContact/AddContactGroup',
            CONTACTMGR_PCONTACT_UPDATECONTACTGROUP:'/ContactMgr/PContact/UpdateContactGroup',
            CONTACTMGR_PCONTACT_DELETECONTACTGROUP:'/ContactMgr/PContact/DeleteContactGroup',
            CONTACTMGR_PCONTACT_EXPORTCONTACTS:'/ContactMgr/PContact/ExportContacts',
            CONTACTMGR_PCONTACT_IMPORTCONTACTS:'/ContactMgr/PContact/ImportContacts',
			CONTACTMGR_PCONTACT_IMPORTCONTACTS_TREE:'/ContactMgr/PContact/Index/getPTree',
            //企业通讯录 ECONTACT
            CONTACTMGR_ECONTACT_INDEX:'/ContactMgr/EContact/Index',
            CONTACTMGR_ECONTACT_ADD:'/ContactMgr/EContact/Add',
            CONTACTMGR_ECONTACT_ADDCONTACTGROUP:'/ContactMgr/EContact/AddContactGroup',
            CONTACTMGR_ECONTACT_DELETECONTACTGROUP:'/ContactMgr/EContact/DeleteContactGroup',

            /**报表统计**/
            STATISTICS_SUMSTATISTICS:'/Statistics/SumStatistics',
            STATISTICS_SUMSTATISTICS_INDEX:'/Statistics/SumStatistics/Index',
            //用户统计
            STATISTICS_SUMSTATISTICS_ACCOUNT_INDEX:'/Statistics/SumStatistics/SumAccount',
            STATISTICS_SUMSTATISTICS_GETSUMACCOUNT:'/Statistics/SumStatistics/GetSumAccount',
            STATISTICS_SUMSTATISTICS_EXPORTACCOUNT:'/Statistics/SumStatistics/ExportAccount',
            STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSERLIST:'/Statistics/SumStatistics/singleUserList',
            STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSEREXPORT:'/Statistics/SumStatistics/SumAccount/singleUserExport',
			STATISTICS_SUMSTATISTICS_USERSELECT: '/Statistics/SumStatistics/SumAccount/UserSelect',
			STATISTICS_SUMSTATISTICS_DEPTSELECT: '/Statistics/SumStatistics/SumAccount/DeptSelect',
            //业务类型统计
            STATISTICS_SUMSTATISTICS_BUSINESS_INDEX:'/Statistics/SumStatistics/SumBusinessType',
            STATISTICS_SUMSTATISTICS_GETSUMBUSINESSTYPE:'/Statistics/SumStatistics/GetSumBusinessType',
            STATISTICS_SUMSTATISTICS_EXPORTBUSINESSTYPE:'/Statistics/SumStatistics/ExportBusinessType',
            STATISTICS_SUMSTATISTICS_BUSITYPE:'/Statistics/SumStatistics/SumBusinessType/BusiSelect',
            //计费帐户统计
            STATISTICS_BILLINGACCOUNTSTATISTICS_INDEX:'/Statistics/BillingAccountStatistics/Index',
            STATISTICS_BILLINGACCOUNTSTATISTICS_GETBILLINGACCOUNTSTATISTICS:'/Statistics/BillingAccountStatistics/GetBillingAccountStatistics',
            STATISTICS_BILLINGACCOUNTSTATISTICS_EXPORTBILLINGACCOUNTSTATISTICS:'/Statistics/BillingAccountStatistics/ExportBillingAccountStatistics',
            STATISTICS_BILLINGACCOUNTSTATISTICS_GETACCOUNT:'/Statistics/BillingAccountStatistics/Index/GetAccount',
            STATISTICS_BILLINGACCOUNTSTATISTICS_GETDETAILSTAT:'/Statistics/BillingAccountStatistics/detail',
            STATISTICS_BILLINGACCOUNTSTATISTICS_EXPORTDETAIL:'/Statistics/BillingAccountStatistics/Index/export',
			STATISTICS_BILLINGACCOUNTSTATISTICS_USERCONSUME:'/Statistics/BillingAccountStatistics/userDetail',
			STATISTICS_BILLINGACCOUNTSTATISTICS_USERDETAIL:'/Statistics/BillingAccountStatistics/Index/userDetail',
			
            // 部门统计
            STATISTICS_SUMSTATISTICS_DEPARTMENT_INDEX:'/Statistics/SumStatistics/SumDepartment',
            STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT:'/Statistics/SumStatistics/GetSumDepartment',
            STATISTICS_SUMSTATISTICS_EXPORTDEPARTMENT:'/Statistics/SumStatistics/ExportDepartment',
            STATISTICS_SUMSTATISTICS_GETDEPTTREE:'/Statistics/SumStatistics/SumDepartment/GetDeptTree',
        };

        app.register = {
            controller: $controllerProvider.register,
            factory: $provide.factory,
            service: $provide.service,
            urlPerMapConstant:urlPerMap
        };





        var checkAccessState = function(resp) {
            var headers = resp.headers();
            var accessState = headers["access-state"];
            if (angular.isDefined(accessState) && accessState == "unlogin") {
                location.href = loginPage;
            } else if (angular.isDefined(accessState) && accessState == "unauthorized") {
                toastr.error("对不起，你没有权限进行此项操作。请联系系统管理员！");
                location.href = indexPage;
            } else {
                return resp;
            }
            return resp;
        }

        $httpProvider.interceptors.push(['$q','myB',function($q,myB) {
            return {
                "request": function(config) {
                    if (global_variable.formPosting) {
                        $(".fullscreen-posting").fadeIn("fast");
                    }
					config.headers[myB.name] = myB.value;
                    config.headers['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
                    return config;
                },
                "response": function(resp) {
                    if (global_variable.formPosting) {
                        global_variable.formPosting = false;
                        $(".fullscreen-posting").fadeOut("fast");
                    }
                    return checkAccessState(resp);
                },
                "responseError": function(rejection) {
                    if (global_variable.formPosting) {
                        global_variable.formPosting = false;
                        $(".fullscreen-posting").fadeOut("fast");
                    }
                    var ret = checkAccessState(rejection);
                    if (angular.isDefined(ret)) {
                        //过滤自动填充第一次初始化错误信息，IE11不兼容
                        if(!rejection.config.url.toString().contains('/common/complete') 
							&& !rejection.config.url.toString().contains('?userName=')
							&& !rejection.config.url.toString().contains('?deptName=')){
                             toastr.error("请求处理失败！");
                        }
                    }
                    return $q.reject(rejection);
                }
            };
        }]);

        $httpProvider.interceptors.push('myA');

        /** 设置路由 */
        $routeProvider
        /** 公共管理 */
            .when('/', setRoute('/console/index', 'consoleIndexCtrl', '/console/index.js', '平台首页'))
            .when('/home', setRoute('/console/index', 'consoleIndexCtrl', '/console/index.js', '平台首页'))
            .when('/notice',setRoute('/notice/index', 'noticeIndexCtrl', '/notice/index.js', '消息通知'))
            .when('/notice/detail',setRoute('/notice/detail', 'noticeDetailCtrl', '/notice/detail.js', '消息通知详情'))
            .when('/document',setRoute('/document/index', 'documentIndexCtrl', '/document/index.js', '帮助与文档'))
            .when('/accountinfo',setRoute('/accountinfo/index', 'accountInfoIndexCtrl', '/accountinfo/index.js', '账号信息'))

            /**短信管理**/
            //短信发送
            .when(urlPerMap.SMSMGR_SENDSMS_INDEX, setRoute('/smsmgr/sendsms/normalSMS/index', 'sendSmsIndexCtrl', '/smsmgr/sendsms/normalSMS/index.js', '短信管理 -> 发送短信 -> 发送普通短信'))
            .when(urlPerMap.SMSMGR_SENDSMS_COMMONSMS_SEND, setRoute('/smsmgr/sendsms/normalSMS/index', 'sendSmsIndexCtrl', '/smsmgr/sendsms/normalSMS/index.js', '短信管理 -> 发送短信 -> 发送普通短信'))
            .when(urlPerMap.SMSMGR_SENDSMS_VARIANTSMS_SEND, setRoute('/smsmgr/sendsms/normalSMS/index', 'sendSmsIndexCtrl', '/smsmgr/sendsms/normalSMS/index.js', '短信管理 -> 发送短信 -> 发送变量短信'))
             //发送记录
            .when(urlPerMap.SMSMGR_SENDTRACKING_INDEX, setRoute('/smsmgr/sendTracking/dispatch', 'smsDispatchCtrl', '/smsmgr/sendTracking/dispatch.js', '彩信记录的总路由'))
            .when(urlPerMap.SMSMGR_SENDTRACKING_LOADBATCHS, setRoute('/smsmgr/sendTracking/sendRecord/index', 'sendTrackingCtrl', '/smsmgr/sendTracking/sendRecord/index.js', '短信管理 -> 短信记录 -> 发送记录'))
            .when(urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY, setRoute('/smsmgr/sendTracking/sendDetail/index', 'sendDetailCtrl', '/smsmgr/sendTracking/sendDetail/index.js', '短信管理 -> 短信记录 -> 发送详情'))
            .when(urlPerMap.SMSMGR_INBOX_INDEX, setRoute('/smsmgr/sendTracking/receiveRecord/index', 'receiveRecordCtrl', '/smsmgr/sendTracking/receiveRecord/index.js', '短信管理 -> 短信记录 -> 接收记录'))
            //短信管理—>审核短信
            .when(urlPerMap.SMSMGR_SENDPENDING_INDEX, setRoute('/smsmgr/sendPending/index', 'sendPendingCtrl', '/smsmgr/sendPending/index.js', '短信管理 -> 审核短信'))
            //短信管理—>模板管理
            .when(urlPerMap.INFO_SMSTEMPLATE_INDEX, setRoute('/smsmgr/smsTemplate/index', 'infoIndexCtrl', '/smsmgr/smsTemplate/index.js', '短信管理 -> 模板管理 -> 普通模板'))
            .when(urlPerMap.INFO_VARIANTTEMPLATE_INDEX, setRoute('/smsmgr/smsTemplate/index', 'infoIndexCtrl', '/smsmgr/smsTemplate/index.js', '短信管理 -> 模板管理 -> 变量模板'))
            .when(urlPerMap.INFO_COMMONTEMPLATE_ADD +'/:phraseType', setRoute('/smsmgr/smsTemplate/add', 'phraseAddCtrl', '/smsmgr/smsTemplate/add.js', '短信管理 -> 模板管理 -> 新增模板'))
            .when(urlPerMap.INFO_COMMONTEMPLATE_UPDATE +'/:phraseType' +'/:id', setRoute('/smsmgr/smsTemplate/edit', 'phraseEditCtrl', '/smsmgr/smsTemplate/edit.js', '短信管理 -> 模板管理 -> 修改模板'))
            .when(urlPerMap.INFO_VARIANTTEMPLATE_ADD +'/:phraseType', setRoute('/smsmgr/smsTemplate/add', 'phraseAddCtrl', '/smsmgr/smsTemplate/add.js', '短信管理 -> 模板管理 -> 新增模板'))
            .when(urlPerMap.INFO_VARIANTTEMPLATE_UPDATE +'/:phraseType' +'/:id', setRoute('/smsmgr/smsTemplate/edit', 'phraseEditCtrl', '/smsmgr/smsTemplate/edit.js', '短信管理 -> 模板管理 -> 修改模板'))

            /**彩信管理**/
            // 彩信管理 - > 发送彩信
            .when(urlPerMap.SMSMGR_SENDMMS_INDEX, setRoute('/mmsmgmt/sendmms/index','sendMmsIndexCtrl','/mmsmgmt/sendmms/index.js','彩信管理 -> 发送彩信'))
            // 彩信管理 - > 发送记录
            .when(urlPerMap.MMS_SEND_TRACE_INDEX, setRoute('/mmsmgmt/sendtracking/dispatch', 'mmsDispatchCtrl', '/mmsmgmt/sendtracking/dispatch.js', '彩信记录的总路由'))
            .when(urlPerMap.MMS_SEND_RECORD_INDEX, setRoute('/mmsmgmt/sendtracking/record/index','mmsSendRecordIndexCtrl','/mmsmgmt/sendtracking/record/index.js','彩信管理 -> 彩信记录 -> 发送记录'))
            .when(urlPerMap.MMS_SEND_DETAIL_INDEX, setRoute('/mmsmgmt/sendtracking/detail/index','mmsSendDetailIndexCtrl','/mmsmgmt/sendtracking/detail/index.js','彩信管理 -> 彩信记录 -> 发送详情'))
            // 彩信管理 - > 彩信审核
            .when(urlPerMap.SEND_MMS_AUDIT_INDEX, setRoute('/mmsmgmt/audit/index','mmsAuditIndexCtrl','/mmsmgmt/audit/index.js','彩信管理 -> 审核彩信'))

            // 彩信管理 - > 模板管理
            .when(urlPerMap.INFO_TEMPLATE_INDEX, setRoute('/mmsmgmt/template/index','mmsTemplateIndexCtrl','/mmsmgmt/template/index.js','彩信管理 -> 模板管理'))
            .when(urlPerMap.INFO_TEMPLATE_CREATE, setRoute('/mmsmgmt/template/add','mmsTemplateAddCtrl','/mmsmgmt/template/add.js','彩信管理 -> 模板管理'))
            .when(urlPerMap.INFO_TEMPLATE_MODIFY+'/:templateId', setRoute('/mmsmgmt/template/modify','mmsTemplateModifyCtrl','/mmsmgmt/template/modify.js','彩信管理 -> 模板管理'))


            /**系统管理**/
            .when(urlPerMap.SYSTEMMGR_LOGMGR_INDEX, setRoute('/sysmgr/syslog/index', 'sysLogIndexCtrl', '/sysmgr/syslog/index.js', '系统管理 -> 日志记录'))
            .when(urlPerMap.SYSTEMMGR_SYSCONFIG_INDEX, setRoute('/sysmgr/sysconfig/index', 'sysConfigIndexCtrl', '/sysmgr/sysconfig/index.js', '系统管理 -> 系统配置'))
            .when(urlPerMap.SYSTEMMGR_SYSCONFIG_PARAMTER_CONFIG, setRoute('/sysmgr/sysconfig/parameterConfig', 'parameterConfigCtrl', '/sysmgr/sysconfig/parameterConfig.js', '系统管理 -> 参数设置'))
            .when(urlPerMap.SYSTEMMGR_TASKMGR_INDEX, setRoute('/sysmgr/filetask/index', 'fileTaskIndexCtrl', '/sysmgr/filetask/index.js', '系统管理 -> 导入导出'))
            .when(urlPerMap.SYSTEMMGR_ANNOUNCEMENTMGR_INDEX, setRoute('/sysmgr/announcement/index', 'announcementIndexCtrl', '/sysmgr/announcement/index.js', '系统管理 -> 公告管理'))
            .when(urlPerMap.SYSTEMMGR_ROLEMGR_INDEX, setRoute('/sysmgr/role/index', 'roleIndexCtrl', '/sysmgr/role/index.js', '系统管理 -> 角色管理'))
            .when(urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_INDEX, setRoute('/sysmgr/capitalaccount/index', 'capitalAccountIndexCtrl', '/sysmgr/capitalaccount/index.js', '系统管理 -> 计费账户'))
            .when(urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL + "/:id", setRoute('/sysmgr/capitalaccount/detail', 'detailAccountIndexCtrl', '/sysmgr/capitalaccount/detail.js', '系统管理 -> 计费账户'))
            .when(urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD, setRoute('/sysmgr/capitalaccount/chargeRecord', 'chargeRecordIndexCtrl', '/sysmgr/capitalaccount/chargeRecord.js', '系统管理 -> 充值记录'))
            .when(urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_DETAIL + "/:id", setRoute('/sysmgr/capitalaccount/detail', 'detailAccountIndexCtrl', '/sysmgr/capitalaccount/detail.js', '系统管理 -> 充值记录'))
            .when(urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR + "/:id", setRoute('/sysmgr/capitalaccount/userSelector', 'userSelectorIndexCtrl', '/sysmgr/capitalaccount/userSelector.js', '系统管理 -> 计费账户'))
            .when(urlPerMap.SYSTEMMGR_ACCOUNTMGR_INDEX, setRoute('/sysmgr/usermgr/index', 'userMgrIndexCtrl', '/sysmgr/usermgr/index.js', '系统管理 -> 用户管理'))
            .when(urlPerMap.SYSTEMMGR_ACCOUNTMGR_GETDEPTS, setRoute('/sysmgr/usermgr/dept', 'deptIndexCtrl', '/sysmgr/usermgr/dept.js', '系统管理 -> 用户管理'))
            .when(urlPerMap.SYSTEMMGR_ACCOUNTMGR_ADDDEPARTMENT + "/:id", setRoute('/sysmgr/usermgr/deptAdd', 'deptAddIndexCtrl', '/sysmgr/usermgr/deptAdd.js', '系统管理 -> 用户管理'))
            .when(urlPerMap.SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT + "/:id", setRoute('/sysmgr/usermgr/deptEdit', 'deptEditIndexCtrl', '/sysmgr/usermgr/deptEdit.js', '系统管理 -> 用户管理'))
            .when(urlPerMap.SYSTEMMGR_ACCOUNTMGR_ADDACCOUNT + "/:id", setRoute('/sysmgr/usermgr/userAdd', 'userAddIndexCtrl', '/sysmgr/usermgr/userAdd.js', '系统管理 -> 用户管理'))
            .when(urlPerMap.SYSTEMMGR_ACCOUNTMGR_UPDATEACCOUNT + "/:id", setRoute('/sysmgr/usermgr/userEdit', 'userEditIndexCtrl', '/sysmgr/usermgr/userEdit.js', '系统管理 -> 用户管理'))

            /**资料管理**/
            .when(urlPerMap.INFO_BLACKLIST_INDEX, setRoute('/InformationMgr/BlackList/index', 'blacklistIndexCtrl', '/InformationMgr/BlackList/index.js', '资料管理 -> 黑名单'))
            .when(urlPerMap.INFO_BLACKLIST_ADD, setRoute('/InformationMgr/BlackList/add', 'blacklistAddCtrl', '/InformationMgr/BlackList/add.js', '资料管理 -> 新增黑名单'))
            .when(urlPerMap.INFOR_CARRIERDNSEG_INDEX, setRoute('/InformationMgr/CarrierDnSeg/index', 'carrierDnSegIndexCtrl', '/InformationMgr/CarrierDnSeg/index.js', '资料管理 -> 运营商号段'))
            .when(urlPerMap.INFO_WITELISTWEB_INDEX, setRoute('/InformationMgr/WiteListweb/index', 'whitelistIndexCtrl', '/InformationMgr/WiteListweb/index.js', '资料管理 -> 企业白名单'))
            .when(urlPerMap.INFO_KEYWORD_INDEX, setRoute('/InformationMgr/Keyword/index', 'keywordIndexCtrl', '/InformationMgr/Keyword/index.js', '资料管理 -> 非法关键字'))
            .when(urlPerMap.INFO_BIZTYPE_INDEX, setRoute('/InformationMgr/BusinessType/index', 'biztypeIndexCtrl', '/InformationMgr/BusinessType/index.js', '资料管理 -> 业务类型 '))
            .when(urlPerMap.INFO_BIZTYPE_ADD, setRoute('/InformationMgr/BusinessType/add', 'biztypeAddCtrl', '/InformationMgr/BusinessType/add.js', '资料管理 -> 业务类型 -> 新增业务类型'))
            .when(urlPerMap.INFO_BIZTYPE_UPDATE + "/:id/:mode", setRoute('/InformationMgr/BusinessType/add', 'biztypeAddCtrl', '/InformationMgr/BusinessType/add.js', '资料管理 -> 业务类型 -> 更新业务类型'))
            .when(urlPerMap.INFO_BIZTYPE_DETAIL + "/:id/:mode", setRoute('/InformationMgr/BusinessType/add', 'biztypeAddCtrl', '/InformationMgr/BusinessType/add.js', '资料管理 -> 业务类型 -> 查看业务类型'))

            /**通讯录管理**/
            .when(urlPerMap.CONTACTMGR_PCONTACT_INDEX, setRoute('/ContactMgr/PContact/index', 'pcontactIndexCtrl', '/ContactMgr/PContact/index.js', '通讯录管理 -> 个人通讯录'))
            .when(urlPerMap.CONTACTMGR_ECONTACT_INDEX, setRoute('/ContactMgr/EContact/index', 'econtactIndexCtrl', '/ContactMgr/EContact/index.js', '通讯录管理 -> 共享通讯录'))
            .when(urlPerMap.CONTACTMGR_ECONTACT_ADD, setRoute('/ContactMgr/EContact/add', 'econtactAddCtrl', '/ContactMgr/EContact/add.js', '通讯录管理 -> 共享通讯录 -> 新增共享通讯录'))

            /**报表统计**/
            .when(urlPerMap.STATISTICS_SUMSTATISTICS_DEPARTMENT_INDEX, setRoute('/Statistics/SumStatistics/SumDepartment','deptStatIndexCtrl','/Statistics/SumStatistics/SumDepartment.js','报表统计 -> 部门统计'))
            .when(urlPerMap.STATISTICS_SUMSTATISTICS_ACCOUNT_INDEX, setRoute('/Statistics/SumStatistics/SumAccount','userStatIndexCtrl','/Statistics/SumStatistics/SumAccount.js','报表统计 -> 用户统计'))
            .when(urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSERLIST + "/:queryParam", setRoute('/Statistics/SumStatistics/SumAccount/singleUserList','userDetailIndexCtrl','/Statistics/SumStatistics/SumAccount/singleUserList.js','报表统计 -> 用户统计 -> 用户详情统计'))
            .when(urlPerMap.STATISTICS_SUMSTATISTICS_BUSINESS_INDEX, setRoute('/Statistics/SumStatistics/SumBusinessType','bizStatIndexCtrl','/Statistics/SumStatistics/SumBusinessType.js','报表统计 -> 业务类型统计'))
            .when(urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_INDEX, setRoute('/Statistics/BillingAccountStatistics/index','billAccountStatIndexCtrl','/Statistics/BillingAccountStatistics/index.js','报表统计 -> 计费账户统计'))
            .when(urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_GETDETAILSTAT + "/:queryParam",setRoute('/Statistics/BillingAccountStatistics/detail','billDetailIndexCtrl','/Statistics/BillingAccountStatistics/detail.js','报表统计 -> 计费账户统计 -> 计费账户详情'))
			.when(urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_USERCONSUME+"/:queryParam", setRoute('/Statistics/BillingAccountStatistics/userDetail','userIndexCtrl','/Statistics/BillingAccountStatistics/userDetail.js','报表统计 -> 计费账户统计 -> 计费账户详情 -> 用户消费情况'))
        ;
    });

    app.directive('compileHtml', function($compile) {
        return {
            restrict: 'A',
            replace: true,
            link: function(scope, ele, attrs) {
                scope.$watch(function() {
                        return scope.$eval(attrs.ngBindHtml);
                    },
                    function(html) {
                        $compile(ele.contents())(scope);
                    });
            }
        };
    });

	app.directive('ngPageInfo',['ngTableEventsChannel',function (ngTableEventsChannel) {
    return {
        restrict: 'C',
        link: function (scope) {
				var pageLoad = function(pages){
						var totalpage = 1;
						var currpage = 1;
						angular.forEach(pages, function(data,index,array){
							if(data.type=="last"){
								totalpage = data.number;
							}
							if(eval(data.current)){
								currpage = data.number;
							}
						});
						scope.pageinfo = {"totalpage":totalpage,"currpage":currpage};
				};
			
				ngTableEventsChannel.onAfterReloadData(function(pubParams) {
						var pages = pubParams.generatePagesArray();
                       pageLoad(pages);
                   }, scope, function(pubParams){
                        return pubParams === scope.params;
                    });

				pageLoad(scope.pages);				
			}
		};
	}]);
    function isEmpty(value) {
        return angular.isUndefined(value) || value === '' || value === null || value !== value;
    }
	app.directive('ngMin', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attr, ctrl) {
            scope.$watch(attr.ngMin, function () {
                ctrl.$setViewValue(ctrl.$viewValue);
            });
            var minValidator = function (value) {				
                var min = scope.$eval(attr.ngMin) || 0;
				
                if (!isEmpty(value) && value < min) {
                    ctrl.$setValidity('ngMin', false);
                    return min;
                } else {
                    ctrl.$setValidity('ngMin', true);
                    return value;
                }
            };
 
            ctrl.$parsers.push(minValidator);
            ctrl.$formatters.push(minValidator);
        }
    };
});

	app.directive('ngJumpClick', function() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var selector = attrs.selector;
                element.on('click', selector, function(e) {
                    var callback = function() {
						var pageI = scope.pageInput;
						if(pageI > attrs.ngJumpClick.totalpage){
							pageI = attrs.ngJumpClick.totalpage;
						}
						scope.params.page(pageI);
						$(attrs.jumptarget).val(pageI);
						scope.pageInput = pageI;
                    };
                    scope.$apply(callback);
                });
            }
        };
    });
 
app.directive('ngMax', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attr, ctrl) {
            scope.$watch(attr.ngMax, function () {
                ctrl.$setViewValue(ctrl.$viewValue);
            });
            var maxValidator = function (value) {
				var max = scope.$eval(attr.ngMax) || Infinity;
                if (!isEmpty(value) && value > max) {
                    ctrl.$setValidity('ngMax', false);
                    return max;
                } else {
                    ctrl.$setValidity('ngMax', true);
                    return value;
                }
            };
 
            ctrl.$parsers.push(maxValidator);
            ctrl.$formatters.push(maxValidator);
        }
    };
});

    //新增过滤器，用于控制文本显示长度，超出省略号过滤器，比如短信中“模板内容”只显示前30个字符，其余用省略号代替 by jiangziyuan
    app.filter ('cutContent' , function () {
        return function (value, wordwise, max, tail) {
            if (!value) return '';
            max = parseInt(max, 10);
            if (!max) return value;
            if (value.length <= max) return value;

            value = value.substr(0, max);
            if (wordwise) {
                var lastspace = value.lastIndexOf(' ');
                if (lastspace != -1) {
                    value = value.substr(0, lastspace);
                }
            }
            return value + (tail || ' …');
        };
    });

    return app;
});
