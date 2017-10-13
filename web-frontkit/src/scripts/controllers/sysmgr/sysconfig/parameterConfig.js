define(["app"], function(app){
    var injectParams = ['$scope', '$filter', 'repoService', 'utilService', '$location'];
    var parameterConfigCtrl = function($scope, $filter, repo, util, $location){
        var sysIndexConf = {
            url: $scope.urlPerMap.SYSTEMMGR_SYSCONFIG,
            showName: '参数设置'
        };

        repo.post($scope.urlPerMap.SYSTEMMGR_SYSCONFIG_PARAMTER_CONFIG).then(function(data){
            putFormValue(data.data);
        });

        $scope.showEnterpriseInfo = function() {
            $location.path($scope.urlPerMap.SYSTEMMGR_SYSCONFIG_INDEX);
        };

        $scope.entConfig={};
        putFormValue = function(ent) {
            angular.element("#completeUserName_value").val(ent.moUserName);
            $scope.entConfig.defaultMoUserId = ent.defaultMoUserId;
            $scope.entConfig.deptNoPrefix = ent.deptNoPrefix;
            $scope.entConfig.signature = ent.signature;
            $scope.entConfig.sigLocation = ent.sigLocation;
            $scope.entConfig.auditingFlag = ent.auditingFlag;
            $scope.entConfig.auditingNum = ent.auditingNum;
            $scope.entConfig.auditingMmsFlag = ent.auditingMmsFlag;
            $scope.entConfig.auditingMmsNum = ent.auditingMmsNum;
            $scope.entConfig.balanceRemind = parseInt(ent.balanceRemind);
            $scope.entConfig.chargeDay = ent.chargeDay;
        };

        //自动补全用户账号
        $scope.acompleteUserNameUrl = xpath.service("common/complete/fetchUserData?userName=");

        $scope.watchUserName = function() {
            var userName = angular.element("#completeUserName_value").val();
            if(userName.length == '') {
                $scope.userNameErrorMsg = "默认接收上行短信账号不能为空";
                $scope.userNameError = true;
            } else {
                $scope.userNameError = false;
            }
        };

        //保存
        $scope.save = function() {
            //debugger
            if(angular.isDefined($scope.completeUser)) {
                $scope.entConfig.defaultMoUserId = $scope.completeUser.originalObject.id;
            }
            var userName = angular.element("#completeUserName_value").val().trim();
            if(userName.length == '') {
                return;
            }
            //校验“部门编号前缀”的长度在6以内  by jiangziyuan
            if ($scope.entConfig.deptNoPrefix.length > 6) {
                toastr.error("部门编号前缀长度不允许大于6！");
                return;
            }
            //校验“部门编号前缀”需由数字或字母组成  by jiangziyuan
            var Regx = /^[A-Za-z0-9]*$/;
            if (!Regx.test($scope.entConfig.deptNoPrefix)) {
                toastr.error("“部门编号前缀”需由数字或字母组成！");
                return;
            }

            $scope.entConfig.moUserName = userName;
            repo.updateByPath(sysIndexConf, '/ParameterConfig/Update', $scope.entConfig, '保存').then(function(data) {
                repo.post($scope.urlPerMap.SYSTEMMGR_SYSCONFIG_PARAMTER_CONFIG).then(function(data){
                    $scope.options = data.data;
                    $scope.diffAccount = $scope.options.diffCapitalAccount;
                    putFormValue($scope.options);
                });
            });
        }
    };
    parameterConfigCtrl.$inject = injectParams;
    app.register.controller('parameterConfigCtrl', parameterConfigCtrl);
});