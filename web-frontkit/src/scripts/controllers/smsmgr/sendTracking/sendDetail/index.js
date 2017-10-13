define(["app"], function(app) {
    var injectParams = ['$rootScope','$scope','$location', 'NgTableParams', 'repoService', 'utilService','StService'];
    var sendDetailCtrl = function($rootScope,$scope, $location, NgTableParams, repo, util,stService) {
        var sendDetailConf = {
            url: 'SmsMgr/SendTracking', //基本URL，必填
            showName: '发送详情', //提示的名称，一般为模块名，必填
            sendRecordUrl : $scope.urlPerMap.SMSMGR_SENDTRACKING_INDEX,//发送记录
            sendDetailUrl : $scope.urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY,//发送详情
            receiveRecordUrl : $scope.urlPerMap.SMSMGR_INBOX_INDEX,//接收记录,
            showTplUrl:'smsmgr/sendsms/modal/smsTemplate.html',
            showTicketUrl : $scope.urlPerMap.SMSMGR_SENDTRACKING_INDEX + "/showTicket",//短信内容

        };

        //region 初始化
        $scope.initCtrl = function () {
            $scope.stParams = {
                fBizForm: -1,
                reportState: -1,
                state: -1,
            };
            $scope.selectedObj = {
                user:true,
                dept:false,
                data:"day",
                userUrl:$scope.urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY + '/fetchUserData?userName=',
                deptUrl:$scope.urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY + '/fetchDeptData?deptName='
            }
            stService.initOneDay($scope);
            stService.autoCompleteUser($scope);
            stService.getChannelByMsgId($scope, 1,'common/getAllChannelByMsgType');
            if(angular.isDefined($rootScope.packObject)){
                $scope.queryTerm.postTime = new Date($rootScope.packObject.postTime).Format("yyyy-MM-dd");
                var pack = $rootScope.packObject;
                $scope.stParams._lk_batchName = pack.batchName;
                delete $rootScope.packObject;
            }
        }
        //endregion
        $scope.showTemplateContent = function (numberRecord) {
            util.commonModal($scope, "短信内容", sendDetailConf.showTplUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.content  = numberRecord.smsContent;
                mScope.okBtn = {
                    hide:true
                }
            })
        }
        //region 切换tab
        $scope.showSendRecord = function () {
            $location.path(sendDetailConf.sendRecordUrl);
        }

        $scope.showSendDetail = function () {
            $location.path(sendDetailConf.sendDetailUrl);
        }

        $scope.showReceiveRecord = function () {
            $location.path(sendDetailConf.receiveRecordUrl);
        }
        //endregion

        //region 如果从发送记录跳转过来

        //endregion

        //region 搜索
        $scope.smsNumberRecordTable = new NgTableParams(
            {
                count : 10,
                page : 1
            }, {
                paginationMaxBlocks:4,
                paginationMinBlocks:2,
                total : 0,
                getData : function (params) {
                    if (!stService.checkStCondition($scope,$scope.selectedObj)) {
                        return;
                    }
                    stService.checkReloadFlag($scope,params);
                    return repo.queryByUrl($scope.urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY, sendDetailConf, util.buildQueryParam(params, $scope.stParams)).then(function (data) {
                        params.total(data.total);
                        return data.data;
                    });
                }
            });

        $scope.searchSMSNumberRecord = function () {
            stService.setReloadFlag($scope);
            $scope.smsNumberRecordTable.reload();
        };
        //endregion

        this.init = $scope.initCtrl();
    };

    sendDetailCtrl.$inject = injectParams;
    app.register.controller('sendDetailCtrl', sendDetailCtrl);
});
