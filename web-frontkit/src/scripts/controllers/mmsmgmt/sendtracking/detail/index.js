define(["app"], function (app) {
    var injectParams = ['$rootScope', '$scope', '$location', 'NgTableParams', 'repoService',
                        'utilService', 'StService', 'MmsService'];
    var mmsSendDetailIndexCtrl = function ($rootScope, $scope, $location, NgTableParams, repo, util, stService, mmsService) {
        var mmsSendDetailConf = {
            url: $scope.urlPerMap.MMS_SEND_DETAIL_INDEX,
            locationUrl: $scope.urlPerMap.MMS_SEND_RECORD_INDEX,
            showName: '彩信号码记录', //提示的名称，一般为模块名，必填
        };

        //region 初始化
        $scope.initCtrl = function () {
            $scope.stParams = {
                fBizForm: -1,
                reportState: -1,
                state: -1,
            };
            $scope.selectedObj = {
                user: true,
                dept: false,
                data: "day",
                userUrl:'SmsMgr/SendTrackingMms/LoadNumbers/fetchUserData?userName=',
            }
            stService.initOneDay($scope);
            stService.autoCompleteUser($scope);
            stService.getChannelByMsgId($scope, 2,'common/getAllChannelByMsgType');
            //region 从发送记录进行跳转
            if (angular.isDefined($rootScope.packObject)) {
                $scope.queryTerm.postTime =
                    new Date($rootScope.packObject.postTime).Format("yyyy-MM-dd")
                var pack = $rootScope.packObject;
                $scope.stParams._lk_batchName = pack.batchName;
                delete $rootScope.packObject;
            }
            //endregion
        }
        //endregion

        //region 页面列表跳转
        $scope.goToMmsSendRecord = function () {
            $location.path(mmsSendDetailConf.locationUrl);
        };

        $scope.goToMmsSendDetail = function () {
            $location.path(mmsSendDetailConf.url);
        };
        //endregion

        //region 展示号码记录列表
        $scope.mmsNumberRecordTable = new NgTableParams(
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
                    return repo.queryByUrl($scope.urlPerMap.MMS_SEND_DETAIL_QUERY, mmsSendDetailConf, util.buildQueryParam(params, $scope.stParams)).then(function (data) {
                        params.total(data.total);
                        $scope.mmsDetailRecordList = data.data;
                        return data.data;
                    });
                }
            });
        //endregion

        //region 查询号码记录按钮
        $scope.searchNumberRecord = function () {
            stService.setReloadFlag($scope);
            $scope.mmsNumberRecordTable.reload();
        };
        //endregion

        $scope.mmsPreviewByPackId = function (index) {
            var pack = $scope.mmsDetailRecordList[index];
            var tempParams = {
                packId: pack.packId,
                postTime: pack.sendTime
            }
            mmsService.showMmsPack($scope, $scope.urlPerMap.MMS_SEND_DETAIL_QUERY
                                           + '/showMms', '', mmsSendDetailConf, tempParams);
        }

        $scope.initCtrl();
    };
    mmsSendDetailIndexCtrl.$inject = injectParams;
    app.register.controller('mmsSendDetailIndexCtrl', mmsSendDetailIndexCtrl);
});
