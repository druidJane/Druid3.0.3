define(["app"], function(app) {
    var injectParams = ['$scope', '$modal', '$location', 'repoService', 'NgTableParams', 'utilService', '$q'];
    var deptIndexCtrl = function($scope, $modal, $location, repo, NgTableParams, util, $q) {
        var conf = {
            url: '/SystemMgr/AccountMgr',
            showName: '部门查询' //提示的名称，一般为模块名，必填
        };

        $scope.deptParams = {};

        $scope.deptTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_GETDEPTS, conf,
                    util.buildQueryParam(params, $scope.deptParams)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });

        $scope.search = function() {
            $scope.deptTable.reload();
        };

        $scope.detailDeptInfo = function(dept) {
            util.htmlModal($scope, "部门信息查看", "modal/detailDeptInfo.tpl.html",
                function(modal){
                    var mScope = modal.$scope;
                    mScope.dept = angular.copy(dept);
                    mScope.diagCls = "modal-md";
                    mScope.okBtn = {
                        hide: true
                    };
                });
        };

    };
    deptIndexCtrl.$inject = injectParams;
    app.register.controller('deptIndexCtrl', deptIndexCtrl);
});
