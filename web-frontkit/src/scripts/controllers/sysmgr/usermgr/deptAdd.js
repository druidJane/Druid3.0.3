define(["app"], function(app) {
    var injectParams = ['$scope', '$routeParams', 'repoService', 'paramService'];
    var deptAddIndexCtrl = function($scope, $routeParams, repo, ma) {
        var conf = {
            url: '/SystemMgr/AccountMgr',
            showName: '部门新增' //提示的名称，一般为模块名，必填,
        };
        var parentId = ma.decode($routeParams.id);
        $scope.dept = {
            'parentId': parentId,
            bizTypes: [],
            roles: []
        };

        repo.queryByPath(conf, "/AddDepartment/Pre", $scope.dept).then(function(data) {
            $scope.parentDeptName = data.data.enterpriseName;
            angular.forEach(data.data.bizTypes, function(bizType){
                var tempType = {id: bizType.id, name: bizType.name, bound: bizType.bound};
                $scope.dept.bizTypes.push(tempType);
            });
            angular.forEach(data.data.roles, function(role){
                var tempRole = {id: role.id, name: role.name, checked: role.checked, default: role.default};
                $scope.dept.roles.push(tempRole);
            });
            $scope.dept.path = data.data.path;
        });

        $scope.save = function() {
            var isChecked = false;
            if(!angular.isUndefined($scope.dept.identify)) {
                if($scope.dept.identify.indexOf("dept") == 0) {
                    toastr.error("部门编号不允许以dept为前缀！");
                    return;
                }

                /*if ($scope.dept.identify.length > 12) {
                    toastr.error("部门编号长度大于12！");
                    return;
                }*/
            }

            for (var i = 0; i < $scope.dept.bizTypes.length; i++) {
                if ($scope.dept.bizTypes[i].bound) {
                    isChecked = true;
                    break;
                }
            }
            if (!isChecked) {
                toastr.warning("请至少勾选一个业务类型！");
                return;
            }
            repo.post($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_ADDDEPARTMENT, $scope.dept).then(function(data) {
                if (data.status == 0) {
                    toastr.success("新增部门成功！");
                    window.history.back();
                } else {
                    toastr.error("新增部门失败：" + data.errorMsg);
                }
            });
        };
    };
    deptAddIndexCtrl.$inject = injectParams;
    app.register.controller('deptAddIndexCtrl', deptAddIndexCtrl);
});
