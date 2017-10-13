/**
 * Created by gdy on 2017/7/10.
 */
define(["app"], function(app) {
    var injectParams = ['$rootScope','$routeParams','$scope','$location'];
    var mmsDispatchCtrl = function($rootScope,$routeParams, $scope, $location ) {
        if ($scope.hasPermission($scope.urlPerMap.MMS_SEND_RECORD_INDEX)) {
            // 必须要保证，如果有发送记录的权限的时候，必须跳转到发送记录的页面中去!!!
            $location.path($scope.urlPerMap.MMS_SEND_RECORD_INDEX);
        } else if ($scope.hasPermission($scope.urlPerMap.MMS_SEND_DETAIL_INDEX)) {
            $location.path($scope.urlPerMap.MMS_SEND_DETAIL_INDEX)
        } else {
            // 如果没有权限，则让其跳转到发送记录，由于也没有发送记录的权限，因此一定会报没有权限的错误
            // 这样子才符合我们程序的设计
            $location.path($scope.urlPerMap.MMS_SEND_RECORD_INDEX);
        }
    };
    mmsDispatchCtrl.$inject = injectParams;
    app.register.controller('mmsDispatchCtrl', mmsDispatchCtrl);
});
