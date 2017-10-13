define(["app"], function(app) {
    var injectParams = ['$scope','$rootScope', '$routeParams','$location', 'NgTableParams', 'repoService', 'utilService', 'modalService','$filter','paramService','MmsService','StService'];
    var mmsAuditIndexCtrl = function($scope,$rootScope,$routeParams, $location, NgTableParams, repo, util, modal,$filter,ma,mmsService,stService) {

        var mmsAuditIndexConf = {
            showName: '审核管理', //提示的名称，一般为模块名，必填
            url: $scope.urlPerMap.SEND_MMS_AUDIT_INDEX, //基本URL，必填
            updateUrl:$scope.urlPerMap.SEND_MMS_AUDIT_BUTTON,
            auditUrl:'SmsMgr/SendPendingMms/LoadWaitBatchs',
            preAuditUrl:'SmsMgr/SendPendingMms/CheckBatch',
            preAuditMmsTemplateUrl:'/mmsmgmt/audit/modal/preAuditMmsModal.html',
            resultImportContactTplUrl:'/mmsmgmt/sendmms/modal/resultImportContact.html',
            locationUrl:$scope.urlPerMap.MMS_SEND_TRACE_INDEX,
            allCheckUrl:$scope.urlPerMap.SEND_MMS_AUDIT_QUERY+'/checkAllPack',
            filterCheckUrl:$scope.urlPerMap.SEND_MMS_AUDIT_QUERY+'/checkFilterPack',
            checkPackUrl:'/mmsmgmt/sendtracking/modal/checkRecordDetail.html',
        };

        $scope.initCtrl = function () {
            $scope.stParams = {
                userId:-1,
                deptId:-1,
                subDept:true
            };
            $scope.selectedObj = {
                user:true,
                dept:true,
                data:"",
                userUrl:'SmsMgr/SendPendingMms/LoadWaitBatchs/fetchUserData?userName=',
                deptUrl:'SmsMgr/SendPendingMms/LoadWaitBatchs/fetchDeptData?deptName='
            }
            stService.initOneMonth($scope);
            stService.autoCompleteUser($scope);
            stService.autoCompleteDept($scope);
            var firstDate = new Date();
            firstDate.setDate(1);
            $scope.stParams.beginTime = firstDate.Format("yyyy-MM-dd 00:00:00");;
            //region 从首先中跳转过来
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
        }

        //region 查询
        $scope.searchAuditRecord = function () {
            $scope.auditTable.reload();
        };
        //endregion

        // region 审核大表
        $scope.auditTable = new NgTableParams(
            {
                count:10,
                page:1
            },{
                total:0,
                getData: function (params) {
                    if (!stService.checkStCondition($scope,$scope.selectedObj)) {
                        return;
                    }
                    return repo.queryByUrl(mmsAuditIndexConf.auditUrl, mmsAuditIndexConf, util.buildQueryParam(params, $scope.stParams)).then(function (data) {
                        params.total(data.total);
                        $scope.auditTableList = data.data;
                        return $scope.auditTableList;
                    });
                }
            });
        // endregion

        // region 审核按钮
        $scope.preAuditMms = function (index) {
            repo.queryByUrl($scope.urlPerMap.SEND_MMS_AUDIT_BUTTON+ '/preAudit', mmsAuditIndexConf, $scope.auditTableList[index]).then(function (data) {
                if (data.status == 0) {
                    util.commonModal(
                        $scope, "彩信审核", mmsAuditIndexConf.preAuditMmsTemplateUrl,
                        function (modal) {
                            var mScope = modal.$scope;
                            mScope.params = {};
                            mScope.params = data.data;
                            mScope.params.checkState=true;

                            //region 预览彩信
                            mScope.showCurPackMms = function () {
                                mmsService.showMmsPack($scope, $scope.urlPerMap.MMS_SEND_RECORD_QUERY
                                                               + '/showMms','', mmsAuditIndexConf, mScope.params);
                            }
                            //endregion

                            //region 审核确定键
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

                                    repo.updateByPath(mmsAuditIndexConf,'',mScope.params).then(function (data) {
                                        if (data.status == 0) {
                                            // 审核通过
                                            if (mScope.params.checkState){
                                                util.commonModal(mScope,"审核",mmsAuditIndexConf.resultImportContactTplUrl,function (modal) {
                                                    var mmScope = modal.$scope;
                                                    mmScope.params = {
                                                        message:"审核信息成功提交"
                                                    };
                                                    mmScope.okBtn = {
                                                        text:"确定",
                                                        click:function () {
                                                            util.hideModal(modal);
                                                            util.hideModal(mmScope.$parent.modal);
                                                        }
                                                    }
                                                })
                                            } else {
                                                // 审核不通过
                                                util.commonModal(mScope,"审核",mmsAuditIndexConf.resultImportContactTplUrl,function (modal) {
                                                    var mmScope = modal.$scope;
                                                    mmScope.params = {
                                                        message:"审核信息成功提交,已发送号码数：0"+", 失败号码数:"+mScope.params.totalTickets
                                                    };
                                                    mmScope.okBtn = {
                                                        text:"确定",
                                                        click:function () {
                                                            util.hideModal(modal);
                                                            util.hideModal(mmScope.$parent.modal);
                                                        }
                                                    }
                                                })
                                            }
                                        } else {
                                            util.commonModal(mScope,"审核",mmsAuditIndexConf.resultImportContactTplUrl,function (modal) {
                                                var mmScope = modal.$scope;
                                                mmScope.params = {
                                                    message:"审核信息提交失败"
                                                };
                                                mmScope.okBtn = {
                                                    text:"确定",
                                                    click:function () {
                                                        util.hideModal(modal);
                                                    }
                                                }
                                            })
                                        }
                                        $scope.auditTable.reload();
                                    });
                                }
                            };
                            //endregion
                        });
                } else {
                    util.operateInfoModel($scope, "审核异常");
                }
            });
        };
        // endregion

        //region 跳转到批次记录中查看当前审核批次
        $scope.goToMmsSendRecord = function (index) {
            var pack = $scope.auditTableList[index];
            mmsService.queryMmsPack($scope,$scope.urlPerMap.SEND_MMS_AUDIT_QUERY,mmsAuditIndexConf,pack);
        };
        //endregion

        //检核详情

        //region 检核详情
        $scope.checkAuditDetail = function (index) {
            var pack = $scope.auditTableList[index];
            // 获取当前安的批次
            var url = $scope.urlPerMap.SEND_MMS_AUDIT_QUERY + '/showMms';
            stService.checkDetail($scope, mmsAuditIndexConf, pack,url);
        }
        //endregion


        //region 执行这个控制器的一些方法
        this.init =$scope.initCtrl();
        //endregion

    };
    mmsAuditIndexCtrl.$inject = injectParams;
    app.register.controller('mmsAuditIndexCtrl', mmsAuditIndexCtrl);
});
