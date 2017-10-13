define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'repoService', 'utilService'];
    var whitelistAddCtrl = function($scope, $location, repo, util) {
        var whitelistConf = {
            locationUrl:'InformationMgr/WiteListweb/Index',//页面URL
            url: 'InformationMgr/WiteListweb', //基本URL，必填
            showName: '企业白名单' //提示的名称，一般为模块名，必填
        };
        //新增提交按钮
        $scope.submitForm = function() {
            var url = whitelistConf.url + "/Addwhitelist";
            repo.post(url,$scope.whitelist).then(function (resp) {
                if (resp.status == 0) {
                    toastr.success("新增" + whitelistConf.showName + "成功。");
                    $location.path(whitelistConf.locationUrl);
                } else {
                    var msg = "新增失败: " + resp.errorMsg;
                    toastr.error(msg);
                }
            }).catch(function(resp) {
                toastr.error("新增" + $scope.whitelist.showName + "失败: " + resp.errorMsg);
            });
        };
    };
    whitelistAddCtrl.$inject = injectParams;
    app.register.controller('whitelistAddCtrl', whitelistAddCtrl);
});
