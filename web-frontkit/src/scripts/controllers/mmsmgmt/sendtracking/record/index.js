define(["app"], function (app) {
    var injectParams = ['$rootScope','$routeParams','$scope', '$location', 'NgTableParams', 'repoService', 'utilService', 'modalService','MmsService','paramService','StService'];
    var mmsSendRecordIndexCtrl = function ($rootScope,$routeParams,$scope, $location, NgTableParams, repo, util, modal,mmsService,ma,stService) {
        var mmsSendRecordConf = {
            url: $scope.urlPerMap.MMS_SEND_RECORD_INDEX,
            locationUrl: $scope.urlPerMap.MMS_SEND_DETAIL_INDEX,
            updateUrl:$scope.urlPerMap.MMS_SEND_RECORD_CANCEL,
            allCheckUrl:$scope.urlPerMap.MMS_SEND_RECORD_QUERY+'/checkAllPack',
            filterCheckUrl:$scope.urlPerMap.MMS_SEND_RECORD_QUERY+'/checkFilterPack',
            exportRecordUrl:'/mmsmgmt/sendtracking/modal/exportRecord.html',
            checkPackUrl:'/mmsmgmt/sendtracking/modal/checkRecordDetail.html',
            showTplUrl:'mmsmgmt/template/modal/template.html',
            showName: '彩信发送记录', //提示的名称，一般为模块名，必填
            exportUrl:$scope.urlPerMap.SEND_MMS_TRACE_PAGE,
            infoTplUrl:'_modal/infoAlert.html',
            messageExport:true
        };

        //region 初始化
        $scope.initCtrl = function () {
            $scope.enum = {
                waitAudit:'待审核',
                waitBackAudit:'待后台审核',
                wait:"待发送"
            };
            $scope.stParams = {
                packState: -1,
                subDept: true,
            };
            $scope.selectedObj = {
                user:true,
                dept:true,
                data:"day",
                userUrl:'SmsMgr/SendTrackingMms/LoadBatchs/fetchUserData?userName=',
                deptUrl:'SmsMgr/SendTrackingMms/LoadBatchs/fetchDeptData?deptName='
            }
            stService.initOneDay($scope);
            stService.autoCompleteUser($scope);
            stService.autoCompleteDept($scope);
            //region 如果是从首页跳转过来
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
            //region 如果是从审核记录跳转过来的 --> 应该是再也不会跳到这里了
            // if (angular.isDefined($rootScope.packObject)) {
            //     $scope.stParams.beginTime = $rootScope.packObject.postTime +" 00:00:00";
            //     $scope.stParams.endTime = $rootScope.packObject.postTime+" 23:59:59";
            //     $scope.stParams.packId = $rootScope.packObject.packId;
            //     delete $rootScope.packObject;
            // }
            //endregion
        }
        //endregion

        //region 切换tab
        $scope.goToMmsSendRecord = function () {
            $location.path(mmsSendRecordConf.url);
        };

        $scope.goToMmsSendDetail = function () {
            $location.path(mmsSendRecordConf.locationUrl);
        };
        //endregion

        //region 搜索栏
        $scope.mmsRecordTable = new NgTableParams(
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
                    return repo.queryByUrl($scope.urlPerMap.MMS_SEND_RECORD_QUERY, mmsSendRecordConf, util.buildQueryParam(params, $scope.stParams)).then(function (data) {
                        params.total(data.total);
                        $scope.mmsSendRecordList = data.data;
                        return $scope.mmsSendRecordList;
                    });
                }
            });

        //region 查询批次记录按钮
        $scope.searchBatchRecord = function () {
            stService.setReloadFlag($scope);
            $scope.mmsRecordTable.reload();
        };
        //endregion

        //endregion

        //region 发送详情 -- 跳转页面
        $scope.checkMmsDetail = function (index) {
            var pack = $scope.mmsSendRecordList[index];
            var packInfo = {
                packId: pack.packId,
                postTime: pack.postTime,
                batchName: pack.batchName
            };
            $rootScope.packObject = packInfo;
            $location.path(mmsSendRecordConf.locationUrl);
        };
        //endregion

        //region 批次详情 -- 显示批次信息
        $scope.showRecordPack = function (index) {
            var pack = $scope.mmsSendRecordList[index];
            mmsService.queryMmsPack($scope,$scope.urlPerMap.MMS_SEND_RECORD_QUERY,mmsSendRecordConf,pack);
        };
        //endregion

        //region 取消选定批次发送
        $scope.cancelThisPack = function (index) {
            var pack = $scope.mmsSendRecordList[index];
            util.commonModal($scope, "取消发送", mmsSendRecordConf.infoTplUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    message: '当前取消仅能取消该批次未发送的手机号，已发送的无法取消，确认是否取消发送？'
                }

                mScope.okBtn = {
                    text: '确认',
                    click: function () {
                        repo.updateByPath(mmsSendRecordConf, '', pack).then(function (data) {
                            $scope.mmsRecordTable.reload();
                        })
                        util.hideModal(modal);
                    }
                }
                mScope.closeBtn = {
                    hide: true
                }
            });
        };
        //endregion

        //region 检核详情
        $scope.checkPackDetail = function (index) {
            var pack = $scope.mmsSendRecordList[index];
            // 获取当前安的批次
            var url = $scope.urlPerMap.MMS_SEND_RECORD_QUERY + '/showMms';
            stService.checkDetail($scope, mmsSendRecordConf, pack,url);
        }
        //endregion

        //region 导出
        $scope.exportSmsPack = function () {
            var queryParams = {
                starttime:$scope.stParams.beginTime,
                enddate:$scope.stParams.endTime,
                senduser:$scope.stParams.loadSendUser,
                batchname:$scope.stParams.batchName,
                batchstate:$scope.stParams.packState
            };

            var params = {
                count: 1,
                page: 1,
                params: $scope.stParams
            };
            if (!stService.checkStCondition($scope, $scope.selectedObj)) {
                return;
            }
            repo.queryByUrl($scope.urlPerMap.MMS_SEND_RECORD_QUERY, mmsSendRecordConf, params).then(function (data) {
                if(data.data.length == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                queryParams.exportRecordSize = data.total;
                modal.exportModal($scope, "导出发送记录", mmsSendRecordConf, queryParams);
            });
        };
        //endregion

    $scope.initCtrl();

    };
    mmsSendRecordIndexCtrl.$inject = injectParams;
    app.register.controller('mmsSendRecordIndexCtrl', mmsSendRecordIndexCtrl);
});
