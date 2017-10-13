define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'NgTableParams', 'repoService', 'utilService', 'modalService','paramService'];
    var biztypeIndexCtrl = function($scope, $location, NgTableParams, repo, util, modal, ps) {
        var biztypeConf = {
            url: 'InformationMgr/Biztype', //基本URL，必填
            showName: '业务类型', //提示的名称，一般为模块名，必填
            delUrl:$scope.urlPerMap.INFO_BIZTYPE_DELETE
        };

        $scope.params = {

        };

        $scope.bizTypesTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.INFO_BIZTYPE_LIST,biztypeConf, util.buildQueryParam(params, $scope.params)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });
        $scope.selectAll = { checked: false };
        $scope.$watch('selectAll.checked', function(value) {
            util.selectAll($scope.bizTypesTable, value);
        });
        $scope.checkOne = function(biztypes) {
            util.checkOne($scope.selectAll, $scope.bizTypesTable, biztypes);
            $scope.biztype = $scope.selectAll.lastSelected;
        };
        $scope.searchBiztype = function() {
            $scope.bizTypesTable.reload();
        };

        $scope.deleteBiztype = function(biztype) { //删除单个业务类型
            repo.removeOne(biztypeConf, biztype.id, biztype.phone).then(function(data) {
                $scope.bizTypesTable.reload();
            });
        };
        $scope.deleteBiztypes = function() { //删除多个业务类型
            repo.remove(biztypeConf, $scope.bizTypesTable.data, "id").then(function(data) {
                $scope.bizTypesTable.reload();
            });
        };

        $scope.addBiztype = function() { //新建业务类型
            $location.path($scope.urlPerMap.INFO_BIZTYPE_ADD);
        };

        $scope.updateBiztype = function (biztype) {
            var checked = util.selectedItems($scope.bizTypesTable.data);
            if (checked.length != 1) {
                toastr.warning("请选择要修改的业务类型！一次只能修改一个业务类型！");
                return ;
            }
            $location.path($scope.urlPerMap.INFO_BIZTYPE_UPDATE + "/"+ ps.encode(checked[0].id)+"/edit");
        }
        $scope.detail = function (biztype) {
            $location.path($scope.urlPerMap.INFO_BIZTYPE_DETAIL + "/"+ ps.encode(biztype.id)+"/detail");
        }
    };

    biztypeIndexCtrl.$inject = injectParams;
    app.register.controller('biztypeIndexCtrl', biztypeIndexCtrl);
});
