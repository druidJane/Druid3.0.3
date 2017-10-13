define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'repoService', 'utilService'];
    var blacklistAddCtrl = function($scope, $location, repo, util) {
        var blacklistConf = {
            locationUrl:'InformationMgr/BlackList/Index',//页面URL
            url: 'InformationMgr/BlackList', //基本URL，必填
            showName: '黑名单' //提示的名称，一般为模块名，必填
        };
        $scope.blacklist = {};
        $scope.domain = {};
        $scope.blacklist.type = '2';
        $scope.EntDomains = {};
        $scope.correctPhone = false;
        repo.queryByUrl('/common/complete/fetchEnterprise',blacklistConf, '').then(function(data) {
            $scope.enterprise = {name : data.data.enterpriseName,id:data.data.enterpriseId};
        });
        var watch = $scope.$watch('blacklist.type', function(newValue, oldValue, scope) {
            if (newValue != null) {
                switch(newValue){
                    case "0":
                        $scope.EntDomains = xpath.service("common/complete/fetchUserData?userName=");
                        break;
                    case "5":
                        $scope.EntDomains = xpath.service("common/complete/fetchBizTypeData?bizType=");
                        break;
                }
            }
        });

        //判断所输入的电话号码是否附近需求——小灵通为10至12位，其余为11位  by jiangziyuan
        $scope.checkPhone = function (phone) {
            if (phone != null) {
                //1、判断所输入的号码是否全为数字
                if (isNaN(phone)) {
                    toastr.warning("输入的手机号码必须是数字！");
                    $scope.correctPhone = false;
                }
                //2、判断所输入的电话号码是否附近需求,开头为0的号码为小灵通
                var tempPhoneTop = phone.substring(0,1);
                if (tempPhoneTop == 0 && phone.length >= 10 && phone.length <= 12) {
                    $scope.correctPhone = true;
                } else if(phone.length == 11) {
                    $scope.correctPhone = true;
                } else {
                    $scope.correctPhone = false;
                }
                //console.log("correctPhone : " + $scope.correctPhone);
            } else if (phone == null) {
                $scope.correctPhone = false;
            }
        }

        //新增提交按钮
        $scope.submitForm = function() {
            if($scope.blacklist.type != '2'){
                if($("#domain_value").val() ==''){
                    toastr.error("所属对象不可为空！");
                    return;
                }
                $scope.blacklist.target = $scope.domain.item.originalObject.id;
            }else{
                $scope.blacklist.target = $scope.enterprise.id;
            }

            var url = blacklistConf.url + "/AddBlackList";
            repo.post(url,$scope.blacklist).then(function (resp) {
                if (resp.status == 0) {
                    toastr.success("新增" + blacklistConf.showName + "成功。");
                    $location.path(blacklistConf.locationUrl);
                } else {
                    var msg = "新增失败: " + resp.errorMsg;
                    toastr.error(msg);
                }
            }).catch(function(resp) {
                toastr.error("新增" + $scope.blacklist.showName + "失败: " + resp.errorMsg);
            });
        };
    };

    blacklistAddCtrl.$inject = injectParams;
    app.register.controller('blacklistAddCtrl', blacklistAddCtrl);
});
