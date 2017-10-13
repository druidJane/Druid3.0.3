define(["app"], function(app) {
    var injectParams = ['$scope','$location', 'NgTableParams', 'repoService', 'utilService', 'modalService','StService','MsgService'];
    var receiveRecordCtrl = function($scope, $location, NgTableParams, repo, util,modal,stService,msg) {
        var receiveRecordConf = {
            url: 'SmsMgr/SendTracking', //基本URL，必填
            showName: '接收记录', //提示的名称，一般为模块名，必填
            sendRecordUrl : $scope.urlPerMap.SMSMGR_SENDTRACKING_INDEX,//发送记录
            sendDetailUrl : $scope.urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY,//发送详情
            receiveRecordUrl : $scope.urlPerMap.SMSMGR_INBOX_INDEX,//接收记录
            showMakeCallUrl: 'smsmgr/sendTracking/receiveRecord/makeCall.html',
            exportUrl:$scope.urlPerMap.SMSMGR_INBOX_EXPORTINBOX
        };

        //region 切换tab
        $scope.showSendRecord = function () {
            $location.path(receiveRecordConf.sendRecordUrl);
        }

        $scope.showSendDetail = function () {
            $location.path(receiveRecordConf.sendDetailUrl);
        }

        $scope.showReceiveRecord = function () {
            $location.path(receiveRecordConf.receiveRecordUrl);
        }
        //endregion
        //region 初始化
        $scope.initCtrl = function () {
            $scope.stParams = {
                userId: -1,
                hasReply: -1,
                subDept: true,
                monthFirstDate: true
            };
            $scope.selectedObj = {
                user: true,
                dept: true,
                data: "year",
                userUrl:$scope.urlPerMap.SMSMGR_INBOX_GETSMSLIST + '/fetchUserData?userName=',
                deptUrl:$scope.urlPerMap.SMSMGR_INBOX_GETSMSLIST + '/fetchDeptData?deptName='
            }
            stService.initOneMonth($scope);
            stService.autoCompleteUser($scope);
            stService.autoCompleteDept($scope);
        }
        //endregion

        //region 查询
        $scope.receiveTable = new NgTableParams(
            {
                count: 10,
                page: 1
            }, {
                total: 0,
                getData: function (params) {
                    if (!stService.checkStCondition($scope, $scope.selectedObj)) {
                        return;
                    }
                    return repo.queryByUrl($scope.urlPerMap.SMSMGR_INBOX_GETSMSLIST, receiveRecordConf, util.buildQueryParam(params, $scope.stParams)).then(function (data) {
                        params.total(data.total);
                        return data.data;
                    });
                }
            });

        $scope.searchReceiveRecord = function () {
            $scope.receiveTable.reload();
        }
        //endregion

        //region 导出
        $scope.exportReceiveRecord = function () {
            if (!stService.checkStCondition($scope,$scope.selectedObj)) {
                return;
            }
            var queryParams = {
                subDept:$scope.stParams.subDept,
                beginTime:$scope.stParams.beginTime,
                hasReply:$scope.stParams.hasReply,
                endTime:$scope.stParams.endTime,
                phone:$scope.stParams.phone,
                deptId:$scope.stParams.deptId,
                path:$scope.stParams.path,
                userId:$scope.stParams.userId,
                specNumber:$scope.stParams.specNumber,
                _lk_specNumber:$scope.stParams._lk_specNumber
            };

            var params = {
                count: 1,
                page: 1,
                params: queryParams
            };
            if (!stService.checkStCondition($scope, $scope.selectedObj)) {
                return;
            }
            repo.queryByUrl($scope.urlPerMap.SMSMGR_INBOX_GETSMSLIST, receiveRecordConf, params).then(function (data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                queryParams.exportRecordSize = data.total;
                modal.exportModal($scope, "导出接收记录", receiveRecordConf, queryParams);
            });
        };
        // endregion
        msg.getBalance($scope,"common/getUserSignature",2,1);
        //region 回复
        $scope.makeCall = function (info) {
            util.commonModal($scope, "回复信息", receiveRecordConf.showMakeCallUrl, function(modal) {
                var mScope = modal.$scope;
                mScope.replyParams = {
                    moTicketId:info.id,
                    phone:info.phone
                };

                //加载回复明细表
                mScope.replyTableParams = {
                    moTicketId : info.id
                }
                mScope.replyTable = new NgTableParams(
                    {
                        page: 1,
                        count: 10
                    }, {
                        total: 0,
                        getData: function (params) {
                            return repo.queryByUrl($scope.urlPerMap.SMSMGR_INBOX_REPLYSMS + "/list", receiveRecordConf, util.buildQueryParam(params, mScope.replyTableParams)).then(function (data) {
                                params.total(data.total);
                                // params.total(data.total);
                                return data.data;
                            });
                        }
                    });
                mScope.$watch('replyParams.replyContent',function () {
                    var size = util.isEmpty(mScope.replyParams.replyContent)?0:mScope.replyParams.replyContent.length
                    size += $scope.accountInfo.entSignature.length + $scope.accountInfo.userSignature.length;
                    mScope.totalSize = size;
                    mScope.frameNum = parseInt((mScope.totalSize -1) /70) + 1;
                });
                //回复信息
                mScope.reply = function () {
                    if(util.isEmpty(mScope.replyParams.replyContent)){
                        util.operateInfoModel('',"回复内容不能为空！");
                        return;
                    }
                    if(mScope.totalSize > 1000){
                        util.operateInfoModel('',"回复内容不能大于1000！");
                        return;
                    }
                    repo.post($scope.urlPerMap.SMSMGR_INBOX_REPLYSMS, mScope.replyParams).then(
                        function(data){
                            if (data.status == 0) {
                                toastr.success("回复信息成功!");
                                $scope.receiveTable.reload();
                                util.hideModal(modal);
                            } else {
                                toastr.error("回复信息失败!");
                            }
                        },true
                    );
                }
                mScope.okBtn = {
                    text : '发送',
                    click : function() {
                        mScope.makeCall.id = info.id;
                        mScope.reply();
                    }
                };
            });
        }
        $scope.showContent = function (replyInfo) {
            util.commonModal($scope, "回复内容", 'smsmgr/sendsms/modal/sendResult.html', function (modal) {
                var mScope = modal.$scope;
                mScope.sendResult = {msg:replyInfo.replyContent};
                mScope.okBtn = {
                    hide:true
                }
            })
        }
        // endregion
        this.init = $scope.initCtrl();
    };

    receiveRecordCtrl.$inject = injectParams;
    app.register.controller('receiveRecordCtrl', receiveRecordCtrl);
});
