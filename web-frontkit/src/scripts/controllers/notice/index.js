define(["app"], function(app) {
    var injectParams =  ['$rootScope','$scope', '$location', 'NgTableParams', 'repoService', 'utilService'];
    var noticeIndexCtrl = function ($rootScope, $scope, $location, NgTableParams, repo, util) {
        var itemConf = {
            url: 'common/notice', //基本URL，必填
            showName: '消息通知' //提示的名称，一般为模块名，必填,
        };

        $scope.params = {
            beginTime:new Date().Format("yyyy-MM-") + "01 00:00:00",
            endTime:new Date().Format("yyyy-MM-dd") + " 23:59:59"
        }

        $scope.table = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function (params) {
                return repo.query(itemConf, util.buildQueryParam(params, $scope.params)).then(function (data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });

        $scope.selectAll = {checked: false};
        $scope.$watch('selectAll.checked', function (value) {
            util.selectAll($scope.table, value);
        });

        $scope.checkOne = function (item) {
            util.checkOne($scope.selectAll, $scope.table, item);
        };

        $scope.search = function () {
            if ($scope.params.beginTime > $scope.params.endTime) {
                toastr.warning("结束时间不能小于起始时间");
                return
            }
            $scope.table.reload();
        };
        
        $scope.batchFlagRead = function () {
            var rets = [];
            angular.forEach($scope.table.data, function(o) {
                if (o.$checked == true) {
                    rets.push(o['id']);
                }
            });
            if (rets.length == 0) {
                toastr.warning('请先选择消息');
                return;
            }
            repo.post('common/notice/updateStateByIds',rets,function(data){
                if (data.data == 0) {
                    toastr.warning('您好，当前所选消息全部为已读。');
                } else {
                    repo.post("common/notice/unReadNoticeCount").then($scope.unReadNoticeCallBack);
                    $scope.table.reload();
                }
            });
        };
        

        $scope.allFlagRead = function () {
            repo.post('common/notice/updateStateByParams',{params:$scope.params},function(data){
                if (data.data == 0) {
                    toastr.warning('您好，当前已无未读消息。');
                } else {
                    repo.post("common/notice/unReadNoticeCount").then($scope.unReadNoticeCallBack);
                    $scope.table.reload();
                }
            });
        };

        

        $scope.batchRmv = function () {
            var newTip = false;
            angular.forEach($scope.table.data,function(item,index){
                if (item.$checked == true && item.state.name == 'UNREAD') {
                    newTip = true;
                    return;
                }
            });

            if (newTip == true) {
                itemConf.deleteTip = '当前所勾选信息含未读信息，是否确定删除 ?';
            } else {
                delete itemConf.deleteTip;
            }

            repo.remove(itemConf, $scope.table.data, "id").then(function (data) {
                repo.post("common/notice/unReadNoticeCount").then($scope.unReadNoticeCallBack);
                $scope.table.reload();
            });
        };
        
    };

    noticeIndexCtrl.$inject = injectParams;
    app.register.controller('noticeIndexCtrl', noticeIndexCtrl);
});
