define(["app"], function(app) {
    var injectParams = ['$scope','$routeParams','$location', 'NgTableParams', 'repoService', 'utilService','paramService','StService','MsgService'];
    var sendPendingCtrl = function($scope, $routeParams, $location, NgTableParams, repo, util,ma,stService,msgService) {
        var sendPendingConf = {
            url: $scope.urlPerMap.SMSMGR_SENDPENDING, //基本URL，必填
            showName: '审核短信', //提示的名称，一般为模块名，必填
            auditUrl: 'smsmgr/sendPending/audit.html',
            checkPackUrl:'/smsmgr/sendTracking/sendRecord/checkRecordDetail.html',
            preAuditMmsTemplateUrl:'/smsmgr/sendPending/modal/preAuditSmsModal.html',
            resultImportContactTplUrl:'/mmsmgmt/sendmms/modal/resultImportContact.html',
            allCheckUrl:$scope.urlPerMap.SMSMGR_SENDPENDING_INDEX+'/checkAllPack',
            filterCheckUrl:$scope.urlPerMap.SMSMGR_SENDPENDING_INDEX+'/checkFilterPack',
            showTplUrl:'smsmgr/sendsms/modal/smsTemplate.html',
    };

        /** 检索栏参数*/
        $scope.initCtrl = function () {
            $scope.stParams = {
                subDept : true
            };
            $scope.selectedObj = {
                user:true,
                dept:true,
                data:"",
                userUrl:$scope.urlPerMap.SMSMGR_SENDPENDING_LOADWAITBATCHS + '/fetchUserData?userName=',
                deptUrl:$scope.urlPerMap.SMSMGR_SENDPENDING_LOADWAITBATCHS + '/fetchDeptData?deptName='
            }
            stService.initOneMonth($scope);
            stService.autoCompleteUser($scope);
            stService.autoCompleteDept($scope);
            var firstDate = new Date();
            firstDate.setDate(1);
            $scope.stParams.beginTime = firstDate.Format("yyyy-MM-dd 00:00:00");;
        }

        //region 从首页进入该地方
        var decodeParam = ma.decode($routeParams.encodeParam);
        if (angular.isDefined(decodeParam.startTime)) {
            $scope.stParams.beginTime = decodeParam.startTime;
        }

        if (angular.isDefined(decodeParam.endTime)) {
            $scope.stParams.endTime = decodeParam.endTime;
        }

        if (angular.isDefined(decodeParam.packId)) {
            $scope.stParams.packId = decodeParam.packId;
        }
        //endregion

        //region 查询
        $scope.sendTrackingTable = new NgTableParams(
            {
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function (params) {
                    if (!stService.checkStCondition($scope, $scope.selectedObj)) {
                        return;
                    }
                    return repo.queryByUrl($scope.urlPerMap.SMSMGR_SENDPENDING_LOADWAITBATCHS, sendPendingConf, util.buildQueryParam(params, $scope.stParams)).then(function (data) {
                        params.total(data.total);
                        $scope.auditTableList = data.data;
                        return $scope.auditTableList;
                    });
                }
            });
        //endregion
        
        //查询
        $scope.searchSendTracking = function() {
            $scope.sendTrackingTable.reload();
        };

        //重置查询
        $scope.resetSearch = function() {
            $scope.stParams = {};
            $scope.searchSendTracking();
        };
        
        //审核
        $scope.preAuditSms = function (pack) {
            repo.queryByUrl($scope.urlPerMap.SMSMGR_SENDPENDING_CHECKBATCH + "/preAudit", sendPendingConf, pack).then(function (data) {
                if (data.status == 0) {
                    util.commonModal(
                        $scope, "短信审核", sendPendingConf.preAuditMmsTemplateUrl,
                        function (modal) {
                            var mScope = modal.$scope;
                            mScope.showParams = {};
                            mScope.params = {};
                            mScope.params = data.data;
                            mScope.params.checkState=true;
                            mScope.showParams.showAllContent = true;
                            mScope.clickToShowAllSmsContent = function () {
                                mScope.showParams.showAllContent = !mScope.showParams.showAllContent;
                            }
                            mScope.okBtn = {
                                text: "审核",
                                click: function () {
                                    // 如果审核不通过，必须要填写通过审核的理由
                                    if (!mScope.params.checkState
                                        && util.isEmpty(mScope.params.auditRemark)) {
                                        util.operateInfoModel('', "请填写不通过审核的理由");
                                        return;
                                    }
                                    if (!util.isEmpty(mScope.params.auditRemark) && (mScope.params.auditRemark.length>200)) {
                                        toastr.warning("审核字数限制在200个字符以内");
                                        return;
                                    }
                                    repo.post($scope.urlPerMap.SMSMGR_SENDPENDING_CHECKBATCH,mScope.params).then(function (data) {
                                        if (data.status == 0) {
                                            if (mScope.params.checkState){
                                                util.hideModal(modal);
                                                modal.$scope.sendTrackingTable.reload();
                                                util.operateInfoModel('', "审核信息成功提交!");
                                            } else {
                                                util.hideModal(modal);
                                                modal.$scope.sendTrackingTable.reload();
                                                util.operateInfoModel('', "审核信息成功提交,已发送号码数：0"+", 失败号码数:"+mScope.params.totalTickets);
                                            }
                                        } else {
                                            util.commonModal(mScope,"审核",sendPendingConf.resultImportContactTplUrl,function (modal) {
                                                util.hideModal(modal);
                                                util.operateInfoModel('', "审核信息提交失败:"+data.errorMsg);
                                            })
                                        }

                                    });
                                }
                            };
                        });
                } else {
                    util.operateInfoModel($scope, "审核异常");
                }
            });
        };
        //检核详情
        $scope.checkAuditDetail = function (pack) {
            // 获取当前安的批次
            util.commonModal($scope,"检核详情",sendPendingConf.checkPackUrl,function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    selectedItem:1,
                    totalTickets:pack.totalTickets,
                    filterTickets:pack.filterTickets
                };
                mScope.filterCheckData = undefined;
                mScope.allCheckData = undefined;

                mScope.showTemplateContent = function (checkResult) {
                    util.commonModal(mScope, "短信内容", sendPendingConf.showTplUrl, function (modal) {
                        var mmScope = modal.$scope;
                        mmScope.content  = checkResult.smsContent;
                        mmScope.okBtn = {
                            hide:true
                        }
                    })
                }

                //region 检核table
                mScope.checkPackTable = new NgTableParams(
                    {
                        count: 10,
                        page: 1
                    },
                    {
                        total: 0,
                        getData: function (params) {
                            if (mScope.params.selectedItem == 0){
                                // 全部核检项目
                                return repo.queryByUrl(sendPendingConf.allCheckUrl, sendPendingConf, util.buildQueryParam(params, pack)).then(function (data) {
                                    params.total(data.total);
                                    return data.data;
                                })
                            } else {
                                // 过滤核检项目
                                return repo.queryByUrl(sendPendingConf.filterCheckUrl, sendPendingConf, util.buildQueryParam(params, pack)).then(function (data) {
                                    params.total(data.total);
                                    return data.data;
                                })
                            }
                        }
                    }
                );
                //endregion

                //region 显示全部核检项目
                mScope.checkAllPhones = function () {
                    mScope.params.selectedItem = 0;
                    mScope.checkPackTable.reload();
                };
                //endregion

                //region 显示过滤核检项目
                mScope.checkFilterPhones = function () {
                    mScope.params.selectedItem = 1;
                    mScope.checkPackTable.reload();
                };
                //endregion

                //region 检核详情确定键
                mScope.okBtn = {
                    hide:true
                }
                //endregion
            });
        }

        // 批次详情
        $scope.querySmsPack = function (pack) {
            msgService.querySmsPack($scope,$scope.urlPerMap.SMSMGR_SENDPENDING_INDEX,sendPendingConf,pack);
        }

        //region 执行这个控制器的一些方法
        this.init =$scope.initCtrl();
        //endregion
    };

    sendPendingCtrl.$inject = injectParams;
    app.register.controller('sendPendingCtrl', sendPendingCtrl);
});
