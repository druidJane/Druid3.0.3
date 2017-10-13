define(["app"], function (app) {
    /**
     * 写给后续维护的人：
     * 在这一份js中，变量phraseType和smsTemplateType的含义是一致的，都是表示模板类型的意思,后续代码中，将会使用smsTemplateType变量进行维护
     * phraseType是用于到数据库中查询的，因此如果没有需要的话，尽量不要修改该变量名
     * 另外:smsTemplateType : 0表示是普通短信模板，1表示是变量短信模板
     * 在添加和修改中会有两个方法有var num =10 表示的是最多有10个数字变量，var num = 3 表示最多有三个中文变量
     */
    var injectParams = ['$scope', '$location', 'NgTableParams', 'repoService', 'utilService',
                        '$rootScope',];
    var infoIndexCtrl = function ($scope, $location, NgTableParams, repo, util, $rootScope) {
        $scope.infoIndex = "短信模板——/smsmgr/smsTemplate/index";
        var smsTemplateConf = {
            url: $scope.urlPerMap.INFO_SMSTEMPLATE_INDEX, //基本URL，必填
            queryUrl:$scope.urlPerMap.INFO_COMMONTEMPLATE_LIST,
            addTpl: "smsmgr/smsTemplate/add.html",
            showName: '模板管理', //提示的名称，一般为模块名，必填
            showDetailUrl: 'smsmgr/smsTemplate/detail.html',
            showUpdateUrl: 'smsmgr/smsTemplate/edit.html'
        };

        //region 初始化页面
        $scope.initCtrl = function () {
            if (!angular.isDefined($rootScope.smsTemplateType)) {
                if($scope.hasPermission($scope.urlPerMap.INFO_COMMONTEMPLATE_INDEX)){
                    $rootScope.smsTemplateType = 0;
                }else if($scope.hasPermission($scope.urlPerMap.INFO_VARIANTTEMPLATE_INDEX)){
                    $rootScope.smsTemplateType = 1;
                }
            }
            if($rootScope.smsTemplateType == 0){
                smsTemplateConf.queryUrl = $scope.urlPerMap.INFO_COMMONTEMPLATE_LIST;
                $scope.hasAddTemplate = $scope.hasPermission($scope.urlPerMap.INFO_COMMONTEMPLATE_ADD);
                $scope.hasDelTemplate = $scope.hasPermission($scope.urlPerMap.INFO_COMMONTEMPLATE_DEL);
                $scope.hasQueryTemplate = $scope.hasPermission($scope.urlPerMap.INFO_COMMONTEMPLATE_LIST);
                $scope.hasUpdateTemplate = $scope.hasPermission($scope.urlPerMap.INFO_COMMONTEMPLATE_UPDATE);
            }else if($rootScope.smsTemplateType == 1){
                smsTemplateConf.queryUrl = $scope.urlPerMap.INFO_VARIANTTEMPLATE_LIST;
                $scope.hasAddTemplate = $scope.hasPermission($scope.urlPerMap.INFO_VARIANTTEMPLATE_ADD);
                $scope.hasDelTemplate = $scope.hasPermission($scope.urlPerMap.INFO_VARIANTTEMPLATE_DEL);
                $scope.hasQueryTemplate = $scope.hasPermission($scope.urlPerMap.INFO_VARIANTTEMPLATE_LIST);
                $scope.hasUpdateTemplate = $scope.hasPermission($scope.urlPerMap.INFO_VARIANTTEMPLATE_UPDATE);
            }
            $scope.smsTemplateParams = {
                result: -1,
                msgType: 1,   //信息类型：1，短信；2，彩信；3，WAP_PUSH
                templateType: -1, //0为普通1为免审
                phraseType: $rootScope.smsTemplateType // phraseType和smsTemplateType都是表示模板的意思
            };
        }
        //endregion

        //region tab间的跳转
        $scope.showNormalTemplate = function () {
            $rootScope.smsTemplateType = 0;
        }

        $scope.showVarTemplate = function () {
            $rootScope.smsTemplateType = 1;
        }
        //endregion

        //region 查询列表
        $scope.smsTemplateTable = new NgTableParams(
            {
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function (params) {
                    return repo.queryByUrl(smsTemplateConf.queryUrl, smsTemplateConf, util.buildQueryParam(params, $scope.smsTemplateParams)).then(function (data) {
                        $scope.selectAll = {checked: false};
                        params.total(data.total);
                        return data.data;
                    });
                }
            });

        $scope.searchSmsTemplate = function () {
            $scope.smsTemplateTable.reload();
        }
        //endregion

        //region 查看详情
        $scope.detail = function (detailPhraseInfo) {
            util.commonModal($scope, '查看模板信息', smsTemplateConf.showDetailUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.okBtn = {hide: true};
                mScope.closeBtn = {hide: true};
                mScope.info = angular.copy(detailPhraseInfo);

                //1、仅当“发送免审”为是时，显示该项
                //2、且仅显示最近一次的审核结果
                if (detailPhraseInfo.templateType == 1) {
                    mScope.info.isShowAuditState = true;
                } else if (detailPhraseInfo.templateType == 0) {
                    mScope.info.isShowAuditState = false;
                }

                //1、仅当“发送免审”为是时，且审核结果为“不通过”时显示该项
                //2、且仅显示最近一次的审核意见;
                if (detailPhraseInfo.templateType == 1 && detailPhraseInfo.auditState == 2) {
                    mScope.info.isShowRemark = true;
                } else if (detailPhraseInfo.templateType == 0 && detailPhraseInfo.auditState != 2) {
                    mScope.info.isShowRemark = false;
                }
            });
        };
        //endregion

        //region 选择框是单选还是多选
        $scope.selectAll = {checked: false};
        $scope.$watch('selectAll.checked', function (value) {
            util.selectAll($scope.smsTemplateTable, value);
        });
        $scope.checkOne = function (info) {
            util.checkOne($scope.selectAll, $scope.smsTemplateTable, info);
        };
        //endregion

        //region 获取光标在输入框中的位置--js --可以使用angular替换(caretaware.min.js)
        /** 留给项目的时间不多了.....
         * @param inputId 框Id
         * @return {*}
         */
        var getCursorPos = function (elem) {
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
                    }
                    ;
                } else if (elem.nodeName === 'INPUT') {// input
                    Sel.moveStart('character', -elem.value.length);
                    index = Sel.text.length;
                }
            } else if (elem.selectionStart || elem.selectionStart == '0') { // Firefox support
                index = elem.selectionStart;
            }
            return (index);
        }
        //endregion

        //region 增加普通模板或是变量模板
        $scope.addSmsTemplate = function (smsTemplateType) {
            var title = (smsTemplateType == 0) ? "普通" : "变量";

            util.commonModal($scope, title + "短信模板", smsTemplateConf.addTpl, function (modal) {
                var mScope = modal.$scope;
                //region 设置显示页面
                mScope.showParams = {
                    smsTemplateType: smsTemplateType
                }
                mScope.addTplParams = {};
                mScope.addTplParams.templateType = 0;
                //endregion

                //region 变量短信模板相关
                //region 增加短信变量
                // varType : 0为数字变量 1为中文变量
                mScope.addVarCheck = function (varType) {
                    //region 定义变量
                    var num = 0;
                    var varStr = "";
                    var varReg = new RegExp("");
                    var titleWarnStr = "";
                    if (varType == 0) {
                        num = 10;
                        varStr = "[$数字变量$]";
                        titleWarnStr = "数字变量";
                        varReg = new RegExp("\\[\\$数字变量\\$\\]", "g");
                    } else {
                        num = 3;
                        varStr = "[$中文变量$]";
                        titleWarnStr = "中文变量";
                        varReg = new RegExp("\\[\\$中文变量\\$\\]", "g");
                    }
                    //endregion

                    //region 获取当前输入框中的文本数据,并进行校验是否字数超过1000字，
                    // 数字变量是否超过三个或者是中文变量是否超过10个
                    var textArea = mScope.addTplParams.phraseSMSContent;
                    if (!angular.isDefined(textArea)) {
                        textArea = '';
                    }

                    // 获取匹配的的字符串的数组
                    var matchArray = textArea.match(varReg);
                    if (matchArray != null && matchArray.length >= num) {
                         toastr.warning('', titleWarnStr + "不能超过" + num + "个");
                        return;
                    }
                    if (textArea.length + varStr.length > 1000) {
                         toastr.warning('', "模板内容不能够超过1000个字符");
                        return;
                    }
                    //endregion

                    //region 在textArea对应的光标下插入“数字变量”标签
                    var posTextArea = document.getElementById("phraseContent");
                    var pos = getCursorPos(posTextArea);
                    var tempText = textArea.substring(0, pos) + varStr + textArea.substring(pos);
                    mScope.addTplParams.phraseSMSContent = tempText;
                    //endregion
                }
                //endregion

                //region 如果往中文变量或者是数字变量中插入数字，则将该变量删除
                mScope.renderSmsContent = function () {
                    var textArea = $("#phraseContent").val();
                    if (textArea) {
                        var reg = /\[\$([^\[\$])*\$\]/g;
                        var arr = textArea.match(reg);
                        if (arr) {
                            for (var i = 0; i < arr.length; i++) {
                                if (arr[i] != '[$中文变量$]' && arr[i] != '[$数字变量$]') {
                                    textArea = textArea.replace(arr[i], '');
                                }
                            }
                        }
                        mScope.addTplParams.phraseSMSContent = textArea;
                        $("#phraseContent").focus();
                    }
                }
                //endregion

                //endregion

                //region 校验表单数据
                function checkParameters(parameters) {
                    if(util.isEmpty(parameters.title)){
                        toastr.warning("模板名称不得为空");
                        return false;
                    }
                    if(util.isEmpty(parameters.phraseSMSContent)){
                        toastr.warning("模板内容不得为空");
                        return false;
                    }
                    // 匹配至少一个变量
                    var content = parameters.phraseSMSContent;
                    var reg = /\[\$([^\[\$])*\$\]/g;
                    var arr = content.match(reg);

                    if (smsTemplateType == 1 && arr == null) {
                         toastr.warning('', "模板内容需要带变量");
                        return false;
                    }

                    //模板名称  字符长度限制为50
                    if (parameters.title.length > 50) {
                         toastr.warning('', "模板名称”的长度不能大于50！");
                        return false;
                    }

                    //模板内容  字符长度限制为1000
                    if (parameters.phraseSMSContent.length > 1000) {
                         toastr.warning('', "模板内容”的长度不能大于1000！");
                        return false;
                    }

                    return true;
                }

                //endregion

                //region 提交数据
                mScope.submitForm = function () {
                    var url = "InformationMgr/SmsTemplate/AddNonAudit";
                    //校验表单数据
                    if (!checkParameters(mScope.addTplParams)) {
                        return;
                    }

                    if (smsTemplateType == 0) {
                        mScope.addTplParams.phraseType = 0;//用于区分“普通模板（0）”与“变量模板”（1）
                        url = $scope.urlPerMap.INFO_COMMONTEMPLATE_ADD;
                    } else if (smsTemplateType == 1) {
                        mScope.addTplParams.phraseType = 1;//用于区分“普通模板（0）”与“变量模板”（1）
                        url = $scope.urlPerMap.INFO_VARIANTTEMPLATE_ADD;
                    }

                    //提交数据
                    repo.post(url, mScope.addTplParams).then(function (data) {
                        util.hideModal(modal);
                        $scope.smsTemplateTable.reload();
                    });
                };
                //endregion

                //region 确定按钮事件
                mScope.okBtn = {
                    text: "保存",
                    click: function () {
                        mScope.submitForm();
                    }
                }
                //endregion
            })
        }
        //endregion

        //修改模板
        $scope.editSmsTemplateInfo = function (info) {
            util.commonModal($scope, "修改模板", smsTemplateConf.showUpdateUrl, function (modal) {
                var mScope = modal.$scope;

                //region 设置显示页面
                mScope.showParams = {
                    smsTemplateType: info.phraseType
                }
                mScope.modifyTplParams = angular.copy(info);
                mScope.modifyTplParams.phraseSMSContent = info.contentStr
                //endregion

                if (info.phraseType == 0) {
                    var url = $scope.urlPerMap.INFO_COMMONTEMPLATE_UPDATE;
                } else if (info.phraseType == 1) {
                    var url = $scope.urlPerMap.INFO_VARIANTTEMPLATE_UPDATE;
                }
                mScope.doUpdate = function () {
                    repo.post(url, mScope.modifyTplParams).then(
                        function (data) {
                            if (data.status == 0) {
                                toastr.success("修改模板成功!");
                                $scope.smsTemplateTable.reload();
                                util.hideModal(modal);
                            } else {
                                toastr.error("修改模板失败!");
                            }
                        }, true
                    );
                };

                mScope.checkEditParameters = function checkEditParameters(parameters, templateType) {
                    if(util.isEmpty(parameters.title)){
                        toastr.warning("模板名称不得为空");
                        return false;
                    }
                    if(util.isEmpty(parameters.phraseSMSContent)){
                        toastr.warning("模板内容不得为空");
                        return false;
                    }

                    var content = parameters.phraseSMSContent;
                    var reg = /\[\$([^\[\$])*\$\]/g;
                    var arr = content.match(reg);

                    if (templateType == 1 && arr == null) {
                        toastr.warning("模板内容需要带变量");
                        return false;
                    }

                    //模板名称  字符长度限制为50
                    if (parameters.title.length > 50) {
                        toastr.warning("“模板名称”的长度不能大于50！");
                        return false;
                    }

                    //模板内容  字符长度限制为1000
                    if (parameters.contentStr.length > 1000) {
                        toastr.warning("“模板内容”的长度不能大于1000！");
                        return false;
                    }
                    return true;
                }

                mScope.renderSmsContent = function () {
                    var textArea = $("#contentStr").val();
                    if (textArea) {
                        mScope.modifyTplParams.phraseSMSContent  = mScope.checkString(textArea);
                        $("#contentStr").focus();
                    }
                }

                mScope.checkString = function (content) {
                    var reg = /\[\$([^\[\$])*\$\]/g;
                    var arr = content.match(reg);
                    if (arr) {
                        for (var i = 0; i < arr.length; i++) {
                            if (arr[i] != '[$中文变量$]' && arr[i] != '[$数字变量$]') {
                                content = content.replace(arr[i], '');
                            }
                        }
                    }
                    return content;
                }

                mScope.editVarCheck = function (phraseEdit, varType) {
                    //region 定义变量
                    var num = 0;
                    var varStr = "";
                    var varReg = new RegExp("");
                    if (varType == 0) {
                        num = 10;
                        varStr = "[$数字变量$]";
                        varReg = new RegExp("\\[\\$数字变量\\$\\]", "g");
                    } else {
                        num = 3;
                        varStr = "[$中文变量$]";
                        varReg = new RegExp("\\[\\$中文变量\\$\\]", "g");
                    }
                    //endregion

                    //region 获取输入栏中的文件
                    var textArea = mScope.modifyTplParams.phraseSMSContent;
                    if (!angular.isDefined(textArea)) {
                        textArea = '';
                    }
                    //endregion

                    // region 获取匹配的的字符串的数组
                    var matchArray = textArea.match(varReg);
                    if (matchArray != null && matchArray.length >= num) {
                         toastr.warning('', "模板变量不能超过" + num + "个");
                        return;
                    }
                    if (textArea.length + varStr.length > 1000) {
                         toastr.warning('', "模板内容不能够超过1000个字符");
                        return;
                    }
                    //endregion

                    var posTextArea = document.getElementById("contentStr");
                    var pos = getCursorPos(posTextArea);
                    var tempText = textArea.substring(0, pos) + varStr + textArea.substring(pos);
                    mScope.modifyTplParams.phraseSMSContent = mScope.checkString(tempText);
                    $("#contentStr").focus();
                }

                mScope.okBtn = {
                    text: '保存',
                    click: function () {
                        mScope.modifyTplParams.id = info.id;
                        if (!mScope.checkEditParameters(mScope.modifyTplParams,mScope.showParams.smsTemplateType)) {
                            return;
                        }
                        mScope.doUpdate();
                    }
                };
            });
        };

        //region 删除模板
        $scope.delSmsTemplate = function () {
            if($scope.smsTemplateParams.phraseType == 0){
                smsTemplateConf.showName = "普通模板";
                smsTemplateConf.delUrl = $scope.urlPerMap.INFO_COMMONTEMPLATE_DEL;
            }else if($scope.smsTemplateParams.phraseType == 1){
                smsTemplateConf.showName = "变量模板";
                smsTemplateConf.delUrl = $scope.urlPerMap.INFO_VARIANTTEMPLATE_DEL;
            }

            repo.remove(smsTemplateConf, $scope.smsTemplateTable.data, "id").then(function (data) {
                $scope.smsTemplateTable.reload();
            });
        };
        //endregion

        this.init = $scope.initCtrl();
    };

    infoIndexCtrl.$inject = injectParams;
    app.register.controller('infoIndexCtrl', infoIndexCtrl);
});
