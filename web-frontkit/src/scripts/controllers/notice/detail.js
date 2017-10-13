define(["app"], function(app) {
    var injectParams =  ['$rootScope','$routeParams','$scope', '$location', 'NgTableParams', 'repoService', 'utilService'];
    var noticeDetailCtrl = function ($rootScope, $routeParams, $scope, $location, NgTableParams, repo, util) {
        var itemConf = {
            url: 'common/notice', //基本URL，必填
            showName: '消息通知' //提示的名称，一般为模块名，必填
        };

        var noticeParam = $rootScope.noticeParam;
        if (angular.isDefined(noticeParam) && !angular.equals(noticeParam,{})) {
            repo.post("common/notice/getMessageContent",{messageType:noticeParam.messageType.index,pushTime:noticeParam.pushTime,objectId:noticeParam.objectId},function(data){
                $scope.notice = noticeParam;
                $scope.notice.content = data.data;
            });
        }
       
    };

    noticeDetailCtrl.$inject = injectParams;
    app.register.controller('noticeDetailCtrl', noticeDetailCtrl);
});
