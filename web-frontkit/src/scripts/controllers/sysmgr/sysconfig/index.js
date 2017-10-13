define(["app"], function(app){
    var injectParams = ['$scope', '$filter', 'repoService', 'utilService', '$location'];
    var sysConfigIndexCtrl = function($scope, $filter, repo, util, $location){
        var sysIndexConf = {
            url: $scope.urlPerMap.SYSTEMMGR_SYSCONFIG,
            showName: '企业信息'
        };

        $scope.showParameterConfig = function() {
            $location.path($scope.urlPerMap.SYSTEMMGR_SYSCONFIG_PARAMTER_CONFIG);
        };

        if($scope.hasPermission($scope.urlPerMap.SYSTEMMGR_SYSCONFIG_ENTERPRISE)) {
            repo.post($scope.urlPerMap.SYSTEMMGR_SYSCONFIG_ENTERPRISE).then(function(data){
                $scope.options = data.data;
                $scope.diffAccount = $scope.options.diffCapitalAccount;
            });
        } else {
            $scope.showParameterConfig();
        }

    };
    sysConfigIndexCtrl.$inject = injectParams;
    app.register.controller('sysConfigIndexCtrl', sysConfigIndexCtrl);
});