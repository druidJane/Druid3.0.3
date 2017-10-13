define(["app"], function(app) {
    var injectParams = ['$rootScope','$routeParams','$scope','$location', 'NgTableParams', 'repoService', 'utilService', 'modalService','paramService','StService','MsgService'];
    var sendTrackingCtrl = function($rootScope,$routeParams, $scope, $location, NgTableParams, repo, util,modal,ma,stService,msg ) {
        var sendTrackingConf = {
            url: 'SmsMgr/SendTracking', //基本URL，必填
            showName: '发送记录', //提示的名称，一般为模块名，必填
            sendRecordUrl : $scope.urlPerMap.SMSMGR_SENDTRACKING_INDEX,//发送记录
            sendDetailUrl : $scope.urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY,//发送详情
            receiveRecordUrl : $scope.urlPerMap.SMSMGR_INBOX_INDEX,//接收记录,
            locationUrl: $scope.urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY,
            failResend:'/smsmgr/sendTracking/sendRecord/failResend.html',
            reSendResult:'/smsmgr/sendTracking/sendRecord/reSendResult.html',
            reSendDetail:'/smsmgr/sendTracking/sendRecord/reSendDetail.html',
            resultImportContactTplUrl:'/smsmgr/sendsms/modal/resultImportContact.html',
            updateUrl:$scope.urlPerMap.SMSMGR_SENDTRACKING_CANCLEBATCH,
            exportUrl:$scope.urlPerMap.SMSMGR_SENDTRACKING_CLICKSMSEXPORTWEB,
            checkPackUrl:'/smsmgr/sendTracking/sendRecord/checkRecordDetail.html',
            allCheckUrl:$scope.urlPerMap.SMSMGR_SENDTRACKING_INDEX+'/checkAllPack',
            filterCheckUrl:$scope.urlPerMap.SMSMGR_SENDTRACKING_INDEX+'/checkFilterPack',
            showTplUrl:'smsmgr/sendsms/modal/smsTemplate.html',
            infoTplUrl:'_modal/infoAlert.html',
            messageExport:true
        };

        //region 初始化参数
        $scope.initCtrl = function () {
            $scope.stParams = {
                failSMSNum:-1,
                packState: -1,
                subDept: true,
            };
            $scope.selectedObj = {
                user:true,
                dept:true,
                data:"day",
                userUrl:'SmsMgr/SendTracking/LoadBatchs/fetchUserData?userName=',
                deptUrl:'SmsMgr/SendTracking/LoadBatchs/fetchDeptData?deptName='
            }
            stService.initOneDay($scope);
            stService.autoCompleteUser($scope);
            stService.autoCompleteDept($scope);
        }
        //endregion

        //region 切换tab
        $scope.showSendRecord = function () {
            $location.path(sendTrackingConf.sendRecordUrl);
        }

        $scope.showSendDetail = function () {
            $location.path(sendTrackingConf.sendDetailUrl);
        }

        $scope.showReceiveRecord = function () {
            $location.path(sendTrackingConf.receiveRecordUrl);
        }
        //endregion

        //region 从首页跳转而来
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

        //region 搜索栏
        $scope.smsRecordTable = new NgTableParams(
            {
                count: 10,
                page: 1
            }, {
                paginationMaxBlocks:4,
                paginationMinBlocks:2,
                total: 0,
                getData: function (params) {
                    if (!stService.checkStCondition($scope, $scope.selectedObj)) {
                        return;
                    }
                    stService.checkReloadFlag($scope,params);
                    return repo.queryByUrl($scope.urlPerMap.SMSMGR_SENDTRACKING_LOADBATCHS, sendTrackingConf, util.buildQueryParam(params, $scope.stParams)).then(function (data) {
                        params.total(data.total);
                        $scope.smsSendRecordList = data.data;
                        $scope.pageInfo = params;
                        return $scope.smsSendRecordList;
                    });
                }
            });

        $scope.searchBatchRecord = function () {
            stService.setReloadFlag($scope);
            $scope.smsRecordTable.reload();
        };
        // endregion

        //region 导出
        $scope.exportSmsPack = function () {
            var queryParams = {
                starttime:$scope.stParams.beginTime,
                enddate:$scope.stParams.endTime,
                senduser:$scope.completeUser.userName,
                batchname:$scope.stParams.batchName,
                batchstate:$scope.stParams.packState,
            };

            var params = {
                count: 1,
                page: 1,
                params: $scope.stParams
            };
            if (!stService.checkStCondition($scope, $scope.selectedObj)) {
                return;
            }
            repo.queryByUrl($scope.urlPerMap.SMSMGR_SENDTRACKING_LOADBATCHS, sendTrackingConf, params).then(function (data) {
                if(data.data.length == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                queryParams.exportRecordSize = data.total;
                modal.exportModal($scope, "导出发送记录", sendTrackingConf, queryParams);
            });

        };
        //endregion

        //region 发送详情 -- 跳转页面
        $scope.checkSmsDetail =function (pack) {
            $rootScope.packObject = pack;
            $location.path(sendTrackingConf.locationUrl);
        };
        //endregion

        //region 批次详情 -- 显示批次信息
        $scope.showRecordPack = function (msgPack) {
            msg.querySmsPack($scope,sendTrackingConf.sendRecordUrl,sendTrackingConf,msgPack);
        };
        $scope.selectAll = { checked: false };
        $scope.$watch('selectAll.checked', function(value) {
            util.selectAll($scope.smsRecordTable, value);
        });
        $scope.checkOne = function(msgPack) {
            util.checkOne($scope.selectAll, $scope.smsRecordTable, msgPack);
        };
        $scope.showTemplateContent = function (checkResult) {
            util.commonModal($scope, "短信内容", sendTrackingConf.showTplUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.content  = checkResult.smsContent;
                mScope.okBtn = {
                    hide:true
                }
            })
        }
        // region 检核详情
        $scope.checkPackDetail = function (pack) {
            // 获取当前安的批次
            util.commonModal($scope,"检核详情",sendTrackingConf.checkPackUrl,function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    selectedItem:1,
                    totalTickets:pack.totalTickets,
                    filterTickets:pack.filterTickets
                };
                mScope.filterCheckData = undefined;
                mScope.allCheckData = undefined;
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
                                return repo.queryByUrl(sendTrackingConf.allCheckUrl, sendTrackingConf, util.buildQueryParam(params, pack)).then(function (data) {
                                    params.total(data.total);
                                    return data.data;
                                })
                            } else {
                                // 过滤核检项目
                                return repo.queryByUrl(sendTrackingConf.filterCheckUrl, sendTrackingConf, util.buildQueryParam(params, pack)).then(function (data) {
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
        //endregion

        //region 失败重发点击事件
        $scope.failResend = function (index) {
            $scope.reSend = [];
            $scope.failSum = 0;
            var ids = [];
            angular.forEach($scope.smsRecordTable.data, function(o) {
                if (o.$checked == true && o.failedTickets > 0) {
                    var pack = {
                        packId:o.packId,
                        postTime:o.postTime
                    };
                    ids.push(o.packId);
                    $scope.failSum += o.failedTickets;
                    $scope.reSend.push(pack);
                }
            });
            if($scope.reSend.length<1){
                util.operateInfoModel('', "无失败号码可重发，请勾选需要重发的批次。");
                return;
            }
            $scope.reSendparams = angular.copy($scope.stParams);
            $scope.reSendparams.packIds = ids;
            util.commonModal($scope,"失败重发",sendTrackingConf.failResend,function (modal) {
                var mScope = modal.$scope;
                repo.queryByUrl($scope.urlPerMap.SMSMGR_SENDTRACKING_INDEX+"/reSendList", sendTrackingConf, util.buildQueryParam($scope.pageInfo, $scope.reSendparams)).then(function (data) {
                    mScope.$data = data.data;
                });
                mScope.changeBiztype = function (biztype) {
                    mScope.selectedBizType = biztype;
                }
                mScope.changeDistinct = function (value) {
                    mScope.isDistinct = value;
                }
                mScope.changeDistinct(true);
                // 渲染业务类型
                mScope.bizTypes = {};
                repo.getByUrl("/common/complete/fetchUserBindBizTypeData","业务类型").then(function(data) {
                    mScope.selectedBizType = data.data[0];
                    mScope.bizTypes = data.data;
                    mScope.distinct = 'true';
                });
                mScope.okBtn = {
                    text: "重发",
                    click: function() {
                        util.confirm('是否重发当前选择批次？',function () {
                            angular.forEach($scope.reSend,function (o) {
                                o.bizTypeId = mScope.selectedBizType.id;
                                o.distinct = mScope.isDistinct;
                            });
                            repo.post($scope.urlPerMap.SMSMGR_SENDTRACKING_RESEND, $scope.reSend).then(function(json) {
                                if (json.status == 0) {
                                    util.hideModal(modal);
                                    util.commonModal($scope,"短信失败重发",sendTrackingConf.reSendResult,function (resultModal) {
                                        var resultScope = resultModal.$scope;
                                        resultScope.okBtn={hide:true};
                                        resultScope.batchCount = json.data.batchCount;
                                        resultScope.time = json.data.time;
                                        resultScope.ticketCount = json.data.ticketCount;
                                        resultScope.resendDetail = function () {
                                            util.hideModal(resultModal);
                                            util.commonModal($scope,"重发结果详情",sendTrackingConf.reSendDetail,function (resultModal) {
                                                var detailScope = resultModal.$scope;
                                                detailScope.batchList = json.data.batchList;
                                                detailScope.user = json.data.user;
                                                detailScope.okBtn={hide:true};
                                            });
                                        }
                                    });
                                }
                            });
                        });
                    }
                };

            });

        };
        //endregion

        //region 取消选定批次发送
        $scope.cancelSend = function (pack) {
            util.commonModal($scope,"取消发送",sendTrackingConf.infoTplUrl,function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    message:'当前取消仅能取消该批次未发送的手机号，已发送的无法取消，确认是否取消发送？'
                }

                mScope.okBtn = {
                    text: '确认',
                    click: function () {
                        repo.updateByPath(sendTrackingConf, '', pack).then(function (data) {
                            $scope.smsRecordTable.reload();
                        })
                        util.hideModal(modal);
                    }
                }
                mScope.closeBtn = {
                    hide:true
                }
            });
        };
        //endregion
        $scope.showContent = function (fail) {
            msg.showContent(fail,1);
        }
        this.init = $scope.initCtrl();
    };

    sendTrackingCtrl.$inject = injectParams;
    app.register.controller('sendTrackingCtrl', sendTrackingCtrl);
});
