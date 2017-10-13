define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'repoService', 'utilService','$routeParams'];
    var phraseEditCtrl = function($scope, $location, repo, util, $routeParams) {
        var phraseDetailConf = {
            locationUrl:'InformationMgr/SmsTemplate/NonAuditIndex',//页面URL
            url: 'InformationMgr/SmsTemplate', //基本URL，必填
            showName: '模版详情' //提示的名称，一般为模块名，必填
        };
        $scope.phraseEdit= {};
        
    };

    phraseEditCtrl.$inject = injectParams;
    app.register.controller('phraseEditCtrl', phraseEditCtrl);
});
