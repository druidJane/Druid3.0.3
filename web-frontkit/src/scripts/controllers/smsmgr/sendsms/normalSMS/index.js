define(["app"], function(app) {
    var injectParams = ['$rootScope','$scope', '$location', 'NgTableParams','repoService', 'utilService', '$timeout', 'Upload','MsgService','$interval'];
    var sendSmsIndexCtrl = function($rootScope,$scope, $location,NgTableParams, repo, util, $timeout, Upload,msgService,$interval) {
        if(!$scope.hasPermission($scope.urlPerMap.SMSMGR_SENDSMS_COMMONSMS_SEND)&&!$scope.hasPermission($scope.urlPerMap.SMSMGR_SENDSMS_VARIANTSMS_SEND)){
            toastr.error("对不起，你没有权限进行此项操作。请联系系统管理员！");
            location.href = indexPage;
            return;
        }
        var sendSmsConf = {
            locationUrl:'/SmsMgr/SendSms/DoSendSms',
            addPhoneTemplateUrl:'/smsmgr/sendsms/modal/addPhone.html',
            addSuccessTemplateUrl:'/_modal/infoAlert.html',
            invalidTemplateUrl:'/smsmgr/sendsms/modal/alertInvalidPhone.html',
            showInvalidTemplateUrl:'/smsmgr/sendsms/modal/showInvalidPhones.html',
            importContactTemplateUrl:'/mmsmgmt/sendmms/modal/importContact.html',
            saveSmsNormalTemplateUrl:'/smsmgr/sendsms/modal/saveSmsNormal.html',
            saveSmsTemplateTemplateUrl:'/smsmgr/sendsms/modal/saveSmsTemplate.html',
            quoteTemplateUrl:'/smsmgr/sendsms/modal/quoteTemplate.html',
            channelDetailTemplateUrl:'/smsmgr/sendsms/modal/channelDetail.html',
            resultImportContactTplUrl:'/mmsmgmt/sendmms/modal/resultImportContact.html',
            sendResultUrl:'/smsmgr/sendsms/modal/sendResult.html',
            contactPhoneTemplateUrl:'/mmsmgmt/sendmms/modal/contactPhone.html',
            getRestNum:$scope.urlPerMap.SMSMGR_SENDMMS_INDEX + '/getBalance',
            showTplUrl:'smsmgr/sendsms/modal/smsTemplate.html',
            sendRecordUrl :  $scope.urlPerMap.SMSMGR_SENDTRACKING_INDEX,
            msgType:1,
        }

        if(util.isEmpty($rootScope.smsType)){
            if($scope.hasPermission($scope.urlPerMap.SMSMGR_SENDSMS_COMMONSMS_SEND)){
                $rootScope.smsType = 'normal';
            }else if($scope.hasPermission($scope.urlPerMap.SMSMGR_SENDSMS_VARIANTSMS_SEND)){
                $rootScope.smsType = 'var';
            }
            $location.path($scope.urlPerMap.SMSMGR_SENDSMS_INDEX);
        }

        $scope.smsType = $rootScope.smsType;
        $scope.changeUrl = function () {
            if($scope.smsType == 'normal'){
                sendSmsConf.normalSMSUrl = $scope.urlPerMap.SMSMGR_SENDSMS_INDEX;
                sendSmsConf.sendStatusUrl = $scope.urlPerMap.SMSMGR_SENDSMS_COMMONSMS_SEND + '/getSendingState';
                sendSmsConf.sendMsgUrl = $scope.urlPerMap.SMSMGR_SENDSMS_COMMONSMS_SEND;
                sendSmsConf.uploadContactFileUrl = $scope.urlPerMap.SMSMGR_SENDSMS_COMMONSMS_SEND+'/upload?encode=0';
                sendSmsConf.doImportContactFileUrl = $scope.urlPerMap.SMSMGR_SENDSMS_COMMONSMS_SEND+'/import';
                sendSmsConf.getUserBalanceUrl = $scope.urlPerMap.SMSMGR_SENDSMS_COMMONSMS_SEND + '/getUserBalance';

                $scope.hasQueryTemplate = $scope.hasPermission($scope.urlPerMap.INFO_COMMONTEMPLATE_LIST);
                $scope.hasAddTemplate = $scope.hasPermission($scope.urlPerMap.INFO_COMMONTEMPLATE_ADD);
            }
            if($scope.smsType == 'var'){
                sendSmsConf.normalSMSUrl = $scope.urlPerMap.SMSMGR_SENDSMS_INDEX;
                sendSmsConf.sendStatusUrl = $scope.urlPerMap.SMSMGR_SENDSMS_VARIANTSMS_SEND + '/getSendingState';
                sendSmsConf.sendMsgUrl = $scope.urlPerMap.SMSMGR_SENDSMS_VARIANTSMS_SEND;
                sendSmsConf.uploadContactFileUrl = $scope.urlPerMap.SMSMGR_SENDSMS_VARIANTSMS_SEND+'/upload?encode=0';
                sendSmsConf.doImportContactFileUrl = $scope.urlPerMap.SMSMGR_SENDSMS_VARIANTSMS_SEND+'/import';
                sendSmsConf.getUserBalanceUrl = $scope.urlPerMap.SMSMGR_SENDSMS_VARIANTSMS_SEND + '/getUserBalance';

                $scope.hasQueryTemplate = $scope.hasPermission($scope.urlPerMap.INFO_VARIANTTEMPLATE_LIST);
                $scope.hasAddTemplate = $scope.hasPermission($scope.urlPerMap.INFO_VARIANTTEMPLATE_ADD);
            }
        }

        $scope.showNormalSMS = function () {
            $rootScope.smsType = 'normal';
            $scope.changeUrl();
        }

        $scope.showVarSMS = function () {
            $rootScope.smsType = 'var';
            $scope.changeUrl();
        }

        if(!angular.isDefined($rootScope.smsType)){
            $scope.showNormalSMS();
        }
        $scope.initCtrl = function (scope) {

        }

        $scope.distinct = true; // 默认去除重复
        $scope.clearSent =true;  // 默认清空内容
        $scope.isCheck = false; // 默认没有定时时间

        $scope.selectedBizType = null;
        // 号码项目：包含号码、导入文件、通讯录
        $scope.itemMainList = [];
        // 在前台显示的项目的list
        $scope.showItemList = [];
        $scope.sendTotal = 0;

        $scope.tempFrames = [];
        $scope.viewRow = [];
        // 彩信模板的编号
        $scope.templateIndex = "";
        // 彩信模板的名称
        $scope.templateName = "";
        // 最后要进行序列化的数据
        $scope.frames = [];
        $scope.currentFrame = {index:0};
        // 用于控制生成frame是frame的id的值
        $scope.frameIdValue = 1;
        $scope.changeUrl();
        //左侧收信人栏表格数据
        $scope.showItemMainListTable = new NgTableParams(
            {
                page:1,
                count:10
            },{
                counts:[],
                paginationMaxBlocks: 5,
                paginationMinBlocks: 2,
                total:0,
                getData: function(params) {
                    var page = params.page();
                    var data = $scope.itemMainList.slice(
                        ( page - 1) * params.count(), page * params.count());
                    if (data.length == 0 && $scope.itemMainList.length != 0) {
                        page = page - 1;
                        params.page(page); // 手动设置
                        data = $scope.itemMainList.slice(
                            ( page - 1) * params.count(), page * params.count());
                    }
                    params.total($scope.itemMainList.length);
                    $scope.currentFrame.index = 0;
                    $scope.sendTotal = 0;
                    // 计算当前的发送人的总个数
                    angular.forEach($scope.itemMainList, function (item) {
                        $scope.sendTotal += item.count;
                    });

                    //刷新内容
                    $scope.refreshContent($scope.textContent,$scope.textContent);

                    return data;
                }
            });
        // 删除一个号码项目
        $scope.deleteCurItem = function (index,params) {
            var realIndex = ( params.page()-1)*(params.count())+index;
            $scope.itemMainList.splice(realIndex, 1);
            if($scope.itemMainList.length==0){
                $scope.varParamsText = undefined;
                $scope.viewRow = [];
            }
            $scope.convertVar($scope.itemMainList);
            $scope.showItemMainListTable.reload();
        };
        $scope.clearItemMainList = function () {
            util.commonModal($scope,"清空发送人列表",sendSmsConf.resultImportContactTplUrl,function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    message:"确定要清空收信人列表吗？"
                }
                mScope.okBtn = {
                    text:"确定",
                    click:function () {
                        $scope.itemMainList = [];
                        $scope.varParamsText = undefined;
                        $scope.viewRow = [];
                        $scope.showItemMainListTable.reload();
                        util.hideModal(modal);
                    }
                }
            })
        };

        /**
         * 手动添加号码功能
         */
        $scope.manualAddPhones = function () {
            msgService.manualAddPhonesModal($scope,sendSmsConf);
        };
        //变量显示值，当varParams改变，会比较新旧值，用并集赋值varParamsText
        $scope.varParamsText = undefined;
        /**
         * 打开通讯录
         */
        $scope.openContact =function () {
            msgService.contactModal($scope,"选择通讯录",sendSmsConf.contactPhoneTemplateUrl,sendSmsConf.resultImportContactTplUrl);
        };
        /**
         * 修改业务类型，根据通道价格再次计算数量
         */
        $scope.toggleSendInfo = function () {
            repo.post(sendSmsConf.getUserBalanceUrl, {
                bizTypeId: $scope.selectedBizType.id,
                msgType: 1
            }).then(function (data) {
                $scope.accountInfo = {
                    balance: data.data.balance,
                    restSendNum: data.data.restSendNum,
                    enableKeywordFilter:data.data.enableKeywordFilter
                }
            });
        }
        /**
         * 导入收件人
         */
        $scope.importContact = function () {
            msgService.importContactModal($scope,sendSmsConf);
        };

        // 渲染业务类型
        $scope.bizTypes = {};

        $scope.getAllBizType =function () {
            var bizParams = {};
            bizParams.msgType = 1;
            repo.post("common/fetchBizTypeForMsg",bizParams).then(function(data) {
                if (data.status == 0) {
                    $scope.selectedBizType = data.data[0];
                    //显示账户余额信息
                    $scope.accountInfo = {};
                    $scope.bizTypes = data.data;
                    msgService.getBalance($scope,sendSmsConf.getUserBalanceUrl,$scope.selectedBizType.id,1);
                } else {
                    util.operateInfoModel('','由于通道未配置，未能加载到业务类型，请先配置通道');
                }
            });
        };
        //引用模板
        $scope.quoteSmsTemplate =function () {
            util.commonModal($scope,"引用模板",sendSmsConf.quoteTemplateUrl,function (modal) {
                var mScope = modal.$scope;
                var queryUrl = $scope.urlPerMap.INFO_COMMONTEMPLATE_LIST;
                mScope.smsTemplateListParams = {
                    result : 1, // 仅加载“审核结果”为已通过的普通模板
                    msgType:1,   //信息类型：1，短信；2，彩信；3，WAP_PUSH
                    templateType : '-1', //0为普通1为免审
                    phraseType : 0 //模板类型0为普通1为变量
                };
                if(mScope.smsType=='var'){
                    mScope.smsTemplateListParams.phraseType = 1;
                    queryUrl = $scope.urlPerMap.INFO_VARIANTTEMPLATE_LIST;
                }
                mScope.normalPhraseTable = new NgTableParams(
                    {
                        page: 1,
                        count: 10
                    }, {
                        total: 0,
                        getData: function (params) {
                            return repo.queryByUrl(queryUrl, sendSmsConf, util.buildQueryParam(params, mScope.smsTemplateListParams)).then(function (data) {
                                params.total(data.total);
                                // params.total(data.total);
                                return data.data;
                            });
                        }
                    });


                mScope.quoteTemplateById = function (info) {
                    $scope.loadSmsTemplate(info);
                    util.hideModal(modal);
                };
                mScope.okBtn = {
                    hide:true
                }
                //查询
                mScope.searchNormalPhrase = function() {
                    mScope.normalPhraseTable.reload();
                };
            })
        };
        $scope.seletedTemplate = undefined;
        /**
         * 使用模板
         * @param index 模板的id
         */
        $scope.loadSmsTemplate = function (info) {
            $scope.seletedTemplate = info;
            //变量模板，直接替换内容
            /*if(info.phraseType == 1){
                $scope.textContent = info.contentStr;
            }else{
                var elem = $("#textContent")[0];
                var pos = $scope.getCursorPos(elem);
                var tempText = $scope.textContent.substring(0, pos) + info.contentStr + $scope.textContent.substring(pos);
                elem.focus();
                $scope.textContent = tempText;
            }*/
            $scope.textContent = info.contentStr;
        };
        /**
         * 检测非法关键字
         */
        $scope.checkKeyword = function () {
            msgService.checkKeyword($scope.textContent, 1);
        }
        /**
         * 通道详情
         */
        $scope.channelDetail = function () {
            msgService.getChannelByBizType($scope,"通道详情",sendSmsConf.channelDetailTemplateUrl,1);
        }
        $scope.totalSize = 0;
        /**
         * 存为模板
         */
        $scope.saveSmsTemplate = function () {
            if($scope.textContent.length == 0){
                util.operateInfoModel('', "短信内容为空！");
                return;
            };
            util.commonModal($scope, "存为模板", sendSmsConf.saveSmsTemplateTemplateUrl,
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.phraseAdd = {phraseSMSContent:$scope.textContent};
                    if(mScope.smsType=='var'){
                        var url = $scope.urlPerMap.INFO_VARIANTTEMPLATE_ADD;
                        mScope.phraseAdd.phraseType= 1;
                        var numberVariable = '10';
                        var chineseVariable = '3';
                        $scope.addVariable = function (){
                            var str = "[$数字变量$]";
                            $("#phraseContent").focus();
                            var textArea = $("#phraseContent").val();
                            //var textArea = document.getElementById("phraseContent");
                            var reg = new RegExp("\\[\\$数字变量\\$\\]", "g");
                            var arr = textArea.match(reg);
                            var num = 0;
                            if (arr) {
                                num = arr.length;
                            }
                            if (num >= numberVariable) {
                                alert("模板数字变量不能超过"+numberVariable+"个！", function () {
                                    $("#phraseContent").focus();
                                });
                                return;
                            }
                            textArea += str;
                            if(textArea.length + str.length>1000){
                                util.operateInfoModel('',"模板内容不能够超过1000个字符");
                                return;
                            }
                            mScope.phraseAdd.phraseSMSContent = textArea;
                            $("#phraseContent").focus();
                        }

                        $scope.addChineseVariable = function (){
                            var str = "[$中文变量$]";
                            $("#phraseContent").focus();
                            var textArea = $("#phraseContent").val();
                            //var textArea = document.getElementById("phraseContent");
                            var reg = new RegExp("\\[\\$\\中文变量\\$\\]", "g");
                            var arr = textArea.match(reg);
                            var num = 0;
                            if (arr) {
                                num = arr.length;
                            }
                            if (num >= chineseVariable) {
                                alert("模板中文变量不能超过"+chineseVariable+"个！", function () {
                                    $("#phraseContent").focus();
                                });
                                return;
                            }
                            textArea += str;
                            if(textArea.length + str.length>1000){
                                alert("模板内容不能够超过1000个字符");
                                return;
                            }
                            mScope.phraseAdd.phraseSMSContent = textArea;
                            $("#phraseContent").focus();
                        }
                    }
                    var url = $scope.urlPerMap.INFO_COMMONTEMPLATE_ADD;
                    mScope.diagCls = 'modal-lg';
                    mScope.okBtn = {
                        text: "保存",
                        click: function() {
                            if(mScope.smsType=='var'){
                                var content = modal.$scope.phraseAdd.phraseSMSContent;
                                var reg = /\[\$([^\[\$])+\$\]/g;
                                var arr = content.match(reg);
                                if(arr == null){
                                    util.operateInfoModel('',"至少含一个变量，且模板中至少含一个非变量符的字符");
                                    return false;
                                }
                                content = content.replaceAll("[$中文变量$]",'').replaceAll("[$数字变量$]",'');
                                var strs = content.match(/\[\$[^\[\$]*\$\]/g);
                                if (strs && strs.length > 0) {
                                    for (var i = 0; i < strs.length; i++) {
                                        content = content.replaceAll(strs[i], "");
                                    }
                                    mScope.phraseAdd.phraseSMSContent = content;
                                    util.operateInfoModel('', "以下变量失效，已从内容中去除：\n"+strs);
                                    return false;
                                }
                            }

                            //提交数据
                            repo.post(url, modal.$scope.phraseAdd).then(function(data) {
                                if(data.status == 0){
                                    toastr.success('新增模板成功!');
                                    util.hideModal(modal);
                                }else{
                                    toastr.error('新增模板失败!');
                                }
                            });

                        }
                    };
                });
        }

        //region 增加备注
        $scope.showRemark = function () {
            msgService.showRemark($scope);
        };
        //发送短信——doSendSms
        $scope.doSendSms = function () {
            if($scope.smsType=='var'){
                var reg = /\[\$([^\[\$]+)\$\]/g;
                var arr = $scope.textContent.match(reg);
                if(arr == null){
                    util.operateInfoModel('',"至少含一个变量，且变量中至少含一个非变量符的字符");
                    return false;
                }
            }
            var sendSmsParams = {
                mms : $scope.textContent,
                batchName: $scope.batchName,
                scheduledTime: $scope.scheduledTime,
                remark: $scope.remark,
                clearSent: $scope.clearSent,
                distinct: $scope.distinct,
                sendCount: $scope.sendTotal,
                bizTypeId: $scope.selectedBizType.id,
                contactItem:$scope.itemMainList
            };
            if(!$scope.isCheck){
                sendSmsParams.scheduledTime = null;
            }
            if(angular.isDefined($scope.seletedTemplate)){
                sendSmsParams.templateId = $scope.seletedTemplate.id;
            }
            //发送短信
            msgService.sendMsg($scope,sendSmsConf,sendSmsParams,"sms");
        };

        $scope.scheduledTime = new Date().Format("yyyy-MM-dd HH:mm:ss");
        $(function() {
            var timePicker = angular.copy($scope.dateTimePicker);
            timePicker.isAllowEmpty = true;
            timePicker.startDate = moment().format('YYYY-MM-DD HH:mm:ss');
            timePicker.minDate = new Date();
            $('input[name="scheduledTime"]').daterangepicker(timePicker);
        });
        //签名为异步获取
        $scope.$watch('accountInfo', function(value) {
            if(angular.isDefined(value) && !util.isEmpty(value.userSigLocation)){
                $scope.convertSignature();
                $scope.totalSize = $scope.previewText.length;
            }
        });
        //遍历收信人栏，获取可选变量viewRow，相应变量值varParams
        $scope.convertVar = function (dataList) {

            $scope.varParamsText = undefined;
            if(util.isEmpty(dataList))return;
            $scope.viewRow = [];
            angular.forEach(dataList, function (item) {
                angular.forEach(item.contactData,function (o) {
                    if($scope.viewRow.length >= 10){
                        return;
                    };
                    //['姓名','手机号码','所属组', '性别', '出生日期',  '编号','VIP', '备注'];
                    var sex = o.sex==0?'先生':'女士';
                    var vip = o.vip==0?'否':'是';
                    var birthday = o.birthday==null?'':new Date(o.birthday).Format('yyyy-mm-dd');
                    //var contact = [];contact.push(o.name,o.phone,o.group.name,sex,birthday,o.identifier,vip,o.remark);
                    var contact = [];contact.push(o.name,o.phone,sex,birthday,o.identifier,vip,o.remark);
                    $scope.viewRow.push(contact);

                });
                angular.forEach(item.viewRow,function (o) {
                    if($scope.viewRow.length >= 10){
                        return;
                    };
                    $scope.viewRow.push(angular.fromJson(o));
                });
                $scope.convertVarParams(item.varParams);

            });
            //刷新内容
            $scope.refreshContent($scope.textContent,$scope.textContent);
        }
        //获取可选变量交集
        $scope.convertVarParams = function (newValue) {
            if($scope.varParamsText == undefined){
                $scope.varParamsText = newValue;
                return ;
            }
            var array = [];
            //取变量交集
            angular.forEach(newValue,function (o) {
                if($.inArray(o,$scope.varParamsText) != -1){
                    array.push(o);
                }
            })
            $scope.varParamsText = array;
        }
        //效果预览加签名
        $scope.convertSignature = function () {
            if(!util.isEmpty($scope.accountInfo)){
                if(!util.isEmpty($scope.accountInfo.userSigLocation) && $scope.accountInfo.userSigLocation===0){
                    $scope.previewText = $scope.previewText + $scope.accountInfo.userSignature;
                }else{
                    $scope.previewText = $scope.accountInfo.userSignature + $scope.previewText;
                }
                if(!util.isEmpty($scope.accountInfo.entSigLocation) && $scope.accountInfo.entSigLocation===0){
                    $scope.previewText = $scope.previewText + $scope.accountInfo.entSignature;
                }else{
                    $scope.previewText = $scope.accountInfo.entSignature + $scope.previewText;
                }
            }
        }
        $scope.refreshContent = function (newValue,oldValue) {
            if($rootScope.smsType == 'var'){
                $scope.convertPreContent();
            }else{
                $scope.previewText = newValue;
            }
            $scope.convertSignature();
            $scope.totalSize = $scope.previewText.length;
            if($scope.previewText.length>1000)$scope.textContent=oldValue;
            $scope.frameNum = parseInt(($scope.totalSize -1) /70) + 1;
        }
        $scope.$watch('textContent', function(newValue,oldValue) {
            $scope.refreshContent(newValue,oldValue);
        });
        //根据短信内容中的变量，替换相应数据，生成效果预览内容
        $scope.convertPreContent = function () {
            $scope.previewText = $scope.textContent;
            if(util.isEmpty($scope.viewRow))return;
            var str = $scope.textContent;
            //未引用模板，则按照正则匹配变量，引用模板则按顺序匹配变量
            if(util.isEmpty($scope.seletedTemplate)){
                angular.forEach($scope.varParamsText,function (o,index) {
                    //if(util.isEmpty($scope.varParams)||util.isEmpty($scope.viewRow))return;
                    var varValue = $scope.viewRow[$scope.currentFrame.index][index]==null?'':$scope.viewRow[$scope.currentFrame.index][index];
                    $scope.previewText = $scope.previewText.replaceAll("[$"+o+"$]",varValue);
                    str = str.replaceAll("[$"+o+"$]",'');
                });
                var strs = str.match(/\[\$[^\[\$]*\$\]/g);
                if (!util.isEmpty(strs)) {
                    for (var i = 0; i < strs.length; i++) {
                        $scope.textContent = $scope.textContent.replaceAll(strs[i], "");
                    }
                    util.operateInfoModel('', "以下变量失效，已从内容中去除：\n"+strs);
                }
            }else{
                var strs = str.match(/\[\$[^\[\$]*\$\]/g);
                angular.forEach(strs,function (item,index) {
                    var varValue = $scope.viewRow[$scope.currentFrame.index][index]==null?'':$scope.viewRow[$scope.currentFrame.index][index];
                    $scope.previewText = $scope.previewText.replace(item,varValue);
                });
            }

        }
        $scope.showTemplateContent = function (normalInfo) {
            util.commonModal($scope, "模板内容", sendSmsConf.showTplUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.content  = normalInfo.contentStr;
                mScope.templateTitle  = normalInfo.title;
                mScope.okBtn = {
                    hide:true
                }
            })
        }
        $scope.clearTextContent = function () {
            $scope.textContent = '';
            $scope.seletedTemplate = undefined;
        }
        //效果预览，切换帧
        $scope.editFrame = function (index) {
            if (index < 0) {
                return;
            }
            if (index > ($scope.viewRow.length - 1)) {
                return;
            }
            $scope.currentFrame.index = index;
            $scope.convertPreContent();
            $scope.convertSignature();
            $scope.totalSize = $scope.previewText.length;
        }
        this.init = $scope.getAllBizType();
        //添加变量到短信内容光标处
        $scope.pasteVar = function (item) {
            if(!util.isEmpty($scope.seletedTemplate)){
                return ;
            }
            var tempVar = "[$"+item+"$]";
            var elem = $("#textContent")[0];
            var pos = $scope.getCursorPos(elem);
            var tempText = $scope.textContent.substring(0, pos) + tempVar + $scope.textContent.substring(pos);
            elem.focus();
            $scope.textContent = tempText;
        }
        /**
         * 获取光标在短连接输入框中的位置
         * @param inputId 框Id
         * @return {*}
         */
        $scope.getCursorPos = function getCursorPos(elem) {
            var index = 0;
            if (document.selection) {// IE Support
                elem.focus();
                var Sel = document.selection.createRange();
                if (elem.nodeName === 'TEXTAREA') {//textarea
                    var Sel2 = Sel.duplicate();
                    Sel2.moveToElementText(elem);
                    var index = -1;
                    while (Sel2.inRange(Sel)) {
                        Sel2.moveStart('character');
                        index++;
                    };
                }else if (elem.nodeName === 'INPUT') {// input
                    Sel.moveStart('character', -elem.value.length);
                    index = Sel.text.length;
                }
            }else if (elem.selectionStart || elem.selectionStart == '0') { // Firefox support
                index = elem.selectionStart;
            }
            return index;
        }
        $scope.clear = function () {
            if($location.$$path !== "/SmsMgr/SendSms/VariantSmsSend" && $location.$$path !== "/SmsMgr/SendSms/CommonSmsSend"){
                $rootScope.smsType = 'normal';
            }
        }
        $scope.$on("$destroy", function(event,data) {
            $scope.clear();
        })
    };
        
    sendSmsIndexCtrl.$inject = injectParams;
    app.register.controller('sendSmsIndexCtrl', sendSmsIndexCtrl);
});
