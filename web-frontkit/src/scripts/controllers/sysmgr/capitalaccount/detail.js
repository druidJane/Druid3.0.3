define(["app"], function(app) {
    var injectParams = ['$scope', '$routeParams', 'paramService', '$location', 'NgTableParams', 'repoService', 'utilService'];
    var detailAccountIndexCtrl = function($scope, $routeParams, ma, $location, NgTableParams, repo, util) {
        var conf = {
            url: "/SystemMgr/ChargingAccountMgr",
            capitalAccountDetail: $scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL,
            listUserDetail: $scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL_USERS,
            showName: '计费账户'
        };

        //兼容IE浏览器
        if (typeof String.prototype.startsWith != 'function') {
            String.prototype.startsWith = function (prefix){
                return this.slice(0, prefix.length) === prefix;
            };
        }
        //由于计费账户中查看功能与充值记录中查看功能相同，所以这里使用一个js控制不同URL来处理
        if($location.path().startsWith($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD)) {
            conf.capitalAccountDetail = $scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_DETAIL;
            conf.listUserDetail = $scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_USERS_DETAIL;
        }

        $scope.detailParams = {'id': ma.decode($routeParams.id)};
        repo.post(conf.capitalAccountDetail, $scope.detailParams).then(function(data) {
            $scope.capitalAccount = data.data;
        });

        repo.post("/common/getAllRoles").then(function(data) {
            $scope.roleSelectModel = data.data;
        });

        $scope.userParams = {capitalAccountId: $scope.detailParams.id};
        $scope.usersTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl(conf.listUserDetail, conf,
                    util.buildQueryParam(params, $scope.userParams)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });

        $scope.searchDetails = function() {
            $scope.usersTable.reload();
        };

    };
    detailAccountIndexCtrl.$inject = injectParams;
    app.register.controller('detailAccountIndexCtrl', detailAccountIndexCtrl);
});
