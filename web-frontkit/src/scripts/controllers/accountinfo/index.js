define(["app"], function(app) {
    var injectParams = ['$scope', '$timeout', 'repoService', 'utilService',  '$interval'];
    var accountInfoIndexCtrl = function($scope, $timeout, repo, util,  $interval) {
        $scope.updateFlag = false;
        $scope.updatePsswdFlag = false;
        $scope.password = {};
        repo.post('common/acc/getAccountInfo').then(function(data){
            $scope.user = data.data;
            $scope.preUser = angular.copy($scope.user);
        });
        $scope.cancel = function () {
            $scope.user = angular.copy($scope.preUser);
            $scope.updateFlag = false;
        };
        $scope.save = function () {
            if ($scope.user.phone == $scope.preUser.phone && $scope.user.linkMan == $scope.preUser.linkMan) {
                toastr.warning("请先做修改！！！");
                return;
            }
            repo.post('common/acc/updateAccInfo',{phone:$scope.user.phone,linkMan:$scope.user.linkMan}).then(function(data){
                if (data.data == 1) {
                    toastr.success("账户信息更新成功");
                    $scope.updateFlag = false;
                    $scope.preUser = angular.copy($scope.user);
                }
            });
        };

        $scope.reset = function () {
            $scope.updatePsswdFlag = false;
            $scope.password = {};
        };

		util.capitalTip("oldPassword");		
		util.capitalTip("newPassword");
		util.capitalTip("secondPassword");

        $scope.updatePasswd = function () {
            if ($scope.password.oldPassword == $scope.password.newPassword) {
                toastr.error("新密码不能与旧密码相同");
                return;
            }
            repo.post("login/token",{},function (data) {
                if (data.status == 0) {
                    enc.init(data.data.exp, data.data.mod);
                    repo.post("common/acc/updateAccPasswd", {
                        oldPasswd: enc.encode($scope.password.oldPassword),
                        newPasswd: enc.encode($scope.password.newPassword)
                    },function (data) {
                        if (data.status == 0) {
                            toastr.success("密码修改成功");
                            $scope.updatePsswdFlag = false;
                        } else if (data.status == 1) {
                            $scope.password.oldPassword = '';
                            toastr.error(data.errorMsg);
                        } else {
                            $scope.password.newPassword = '';
                            $scope.password.secondPassword = '';
                            toastr.error(data.errorMsg);
                        }
                    });
                }
            },true);
        };
    };

    accountInfoIndexCtrl.$inject = injectParams;
    app.register.controller('accountInfoIndexCtrl', accountInfoIndexCtrl);
});
