/**
 * Created by gdy on 2017/7/10.
 */
define(["app"], function(app) {
    var injectParams = ['$rootScope','$routeParams','$scope','$location'];
    var smsDispatchCtrl = function($rootScope,$routeParams, $scope, $location ) {
        if ($scope.hasPermission($scope.urlPerMap.SMSMGR_SENDTRACKING_LOADBATCHS)) {
            // 必须要保证，如果有发送记录的权限的时候，必须跳转到发送记录的页面中去!!!
            $location.path($scope.urlPerMap.SMSMGR_SENDTRACKING_LOADBATCHS);
        } else if ($scope.hasPermission($scope.urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY)) {
            $location.path($scope.urlPerMap.SMSMGR_SENDTRACKING_BATCHHISTORY)
        } else if ($scope.hasPermission($scope.urlPerMap.SMSMGR_INBOX_INDEX)) {
            $location.path($scope.urlPerMap.SMSMGR_INBOX_INDEX);
        } else {
            // 如果没有权限，则让其跳转到发送记录，由于也没有发送记录的权限，因此一定会报没有权限的错误
            // 这样子才符合我们程序的设计
            $location.path($scope.urlPerMap.SMSMGR_SENDTRACKING_LOADBATCHS);
        }
    };
    smsDispatchCtrl.$inject = injectParams;
    app.register.controller('smsDispatchCtrl', smsDispatchCtrl);
});
