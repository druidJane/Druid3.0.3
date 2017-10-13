define(["app"], function(app) {
    var injectParams = ['$scope', 'paramService', '$routeParams', 'repoService', 'utilService'];
    var deptEditIndexCtrl = function($scope, ma, $routeParams, repo, util) {
        var conf = {
            url: '/SystemMgr/AccountMgr',
            showName: '修改部门' //提示的名称，一般为模块名，必填,
        };

        var deptId = ma.decode($routeParams.id);
        $scope.dept = {
            id: deptId,
            bizTypes: [],
            roles: []
        };

        repo.queryByPath(conf, "/UpdateDepartment/Pre", $scope.dept).then(function(data) {
            $scope.parentDeptName = data.data.pDept.enterpriseName;
            $scope.dept.deptName = data.data.user.deptName;
            $scope.dept.identify = data.data.user.identify;
            angular.forEach(data.data.user.bizTypes, function(bizType){
                var tempType = {id: bizType.id, name: bizType.name, bound: bizType.bound};
                $scope.dept.bizTypes.push(tempType);
            });
            angular.forEach(data.data.user.roles, function(role){
                var tempRole = {id: role.id, name: role.name, checked: role.checked, default: role.default};
                $scope.dept.roles.push(tempRole);
            });
            $scope.dept.remark = data.data.user.remark;
            $scope.dept.path = data.data.pDept.path + data.data.pDept.id + '.';
            $scope.dept.parentId = data.data.user.parentId;
        });

        $scope.save = function() {
            var isChecked = false;
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
            repo.post($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT, $scope.dept).then(function(data) {
                if (data.status == 0) {
                    toastr.success("修改部门成功！");
                    window.history.back();
                } else {
                    toastr.error("修改部门失败：" + data.errorMsg);
                }
            });
        };

        $scope.checkConfirm = function(dept, bizType, role, tip) {
            var checked = bizType == null ? role.checked : bizType.bound;
            if(!checked) {
                var bizTypeId = bizType == null ? '' : bizType.id;
                var roleId = role == null ? '' : role.id;
                var params = {
                    'path': dept.path,
                    'bizTypeId': bizTypeId,
                    'roleId': roleId
                }
                repo.post($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT_CONFIRM, params).then(function(data){
                    if (data.status == 0 && data.data) {
                        util.confirmHasCancel("取消本部门原" + tip + "，将同时取消其下子部门和用户的该项" + tip + "。是否确认修改？",
                            function() {},
                            function() {
                                //点击取消，重新勾选
                                bizType == null ? (role.checked = true) : (bizType.bound = true);
                            });
                    }
                });
            }
        }

    };
    deptEditIndexCtrl.$inject = injectParams;
    app.register.controller('deptEditIndexCtrl', deptEditIndexCtrl);
});
