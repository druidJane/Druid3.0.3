define(["app"], function (app) {
    app.controller("loginCtrl", function ($scope, $http, $window, $cookies, utilService,$interval,$timeout,paramService) {
        $("#login_content").fadeIn("fast");
		$scope.webversion = webversion;
        $scope.loginParams = {
            username:  angular.isUndefined($cookies["$MosYs#"])?"":paramService.decode($cookies["$MosYs#"]),
            password: "",
            imgVerifyCode: ""
        };

		utilService.capitalTip("password");		

        $scope.codeUrl = xpath.service("login/autocode?ran=" + Math.random());

        var tokenUrl = xpath.service("login/token");

        //刷新图片验证码
        $scope.refreshImgCode = function () {
            $scope.codeUrl = xpath.service("login/autocode?ran=" + Math.random());
        };

        $scope.validate = function (phoneList) {
            $scope.phoneList = phoneList;
            utilService.htmlModal($scope, "手机验证登录", "modal/validateForm.tpl.html",
                function (modal) {
                    var mScope = modal.$scope;
                    mScope.validate = {msg:'获取验证码',valid:true};
                    mScope.getCode = function () {
                        if (angular.isUndefined(mScope.validate.phone) || !mScope.validate.phone.trim().length) {
                            toastr.error("请先输入企业号码");
                            return;
                        }
                        var exsitPhone = false;
                        angular.forEach(mScope.phoneList,function(o){
                            if (o == mScope.validate.phone) {
                                exsitPhone = true;
                                return;
                            }
                        });
                        if (exsitPhone == false) {
                            toastr.error("该号码不属于企业验证号码,请重新输入");
                            mScope.validate.phone = '';
                            return;
                        }
                        mScope.coldTime = 60;
                        mScope.validate.msg = mScope.coldTime+"s";
                        $interval(function(){
                            if (mScope.coldTime > 1) {
                                mScope.coldTime--;
                                mScope.validate.valid = false;
                                mScope.validate.msg = mScope.coldTime+"s";
                            } else {
                                mScope.coldTime = 60;
                                mScope.validate.valid = true;
                                mScope.validate.msg = "获取验证码";
                            }

                        },1000,60);
                        $http.post(xpath.service("login/sendPhoneVerifyCode"), mScope.validate.phone).success(function (data) {
                            if (data.status == 0) {
                                toastr.success("短信验证码发送成功，请注意查收");
                            } else if (data.status == 1){
                                toastr.warning(data.errorMsg);
                                $timeout(function(){$window.location.href = loginPage;},1000);
                            } else {
                                toastr.error(data.errorMsg);
                            }
                        })
                    };
                    mScope.okBtn = {
                        text: "登录",
                        click: function () {
                            if (angular.isUndefined(mScope.validate.code) || !mScope.validate.code.trim().length) {
                                toastr.error("请先输入验证码");
                                return;
                            }
                            $http.post(xpath.service("login/validatePhone"), mScope.validate).success(function (data) {
                                if (data.status == 0) {
                                    $window.location.href = indexPage;
                                } else if (data.status == 1) {
                                    toastr.warning(data.errorMsg);
                                    $timeout(function () {
                                        $window.location.href = loginPage;
                                    }, 1000);
                                } else {
                                    toastr.error(data.errorMsg);
                                }
                            })
                        }
                    };
                });
        };

        $scope.updatePasswd = function (tip) {				
            utilService.htmlModal($scope, "修改密码", "modal/changePasswd.tpl.html",
                function (modal) {
					utilService.capitalTip("oldPassword");	
					utilService.capitalTip("newPassword");	
					utilService.capitalTip("secondPassword");

                    var mScope = modal.$scope;
                    mScope.tip = tip;
                    mScope.password = {
                        newPassword: "",
                        secondPassword: ""
                    };

                    mScope.okBtn = {
                        text: "修改",
                        click: function () {
                            if (mScope.password.oldPassword != mScope.loginParams.password) {
                                toastr.error("旧密码输入不正确,请重新输入");
                                mScope.password.oldPassword = "";
                                return;
                            }
                            if (mScope.password.oldPassword == mScope.password.newPassword) {
                                toastr.error("新密码不能与原密码相同");
                                mScope.password.newPassword = "";
                                mScope.password.secondPassword = "";
                                return;
                            }
                            $http.post(tokenUrl).then(function (resp) {
                                var data = resp.data;
                                if (data.status == 0) {
                                    enc.init(data.data.exp, data.data.mod);
                                    //加屏障避免重复提交
                                    $(".fullscreen-posting").fadeIn("fast");
                                    $http.post(xpath.service("login/updatePasswd"), {
                                        userName: enc.encode(mScope.loginParams.username),
                                        newPasswd: enc.encode(mScope.password.newPassword)
                                    }).success(function (data) {
                                        if (data.status == 0) {
                                            toastr.success("密码修改成功，请重新登录");
                                            mScope.loginParams.password = "";
                                            utilService.hideModal(modal);
                                        } else {
                                            toastr.error(data.errorMsg);
                                        }
                                        $(".fullscreen-posting").fadeOut("fast");
                                    });
                                }
                            });
                        }
                    };
                });
        };

        $scope.doLogin = function () {
            if (!$scope.loginParams.username.trim().length || !$scope.loginParams.password.trim().length || !$scope.loginParams.imgVerifyCode.trim().length) {
                toastr.warning("请先输入用户名,密码和验证码");
                return;
            }

            //获取Rsa密钥
            $http.post(tokenUrl).then(function (resp) {
                var data = resp.data;
                if (data.status == 0) {
                    //Rsa密钥初始化
                    enc.init(data.data.exp, data.data.mod);
                    //Rsa加密
                    var rsaParam = {};
                    rsaParam.username = enc.encode($scope.loginParams.username);
                    rsaParam.password = enc.encode($scope.loginParams.password);
                    rsaParam.imgVerifyCode = $scope.loginParams.imgVerifyCode;
                    var loginUrl = xpath.service("login");
                    $http.post(loginUrl, rsaParam).then(function (resp) {
                        var data = resp.data;
                        //data.status = 1;
                        if (data.status == 0) {
                            //记住用户名
                            $cookies["$MosYs#"] = paramService.encode($scope.loginParams.username);
                            $window.location.href = indexPage;
                        } else {
                            if (data.status == 1) {
                                //需要手机验证码验证登录
                                $scope.validate(data.data);
                            } else if (data.status == 2) {
                                //需要修改密码
                                $scope.updatePasswd(data.errorMsg);
                            } else {
                                if (data.status == 3) {//用户名错误
                                    $scope.loginParams.username = '';
                                }
                                if (data.status == 4) {//密码错误
                                    $scope.loginParams.password = '';
                                }
                                if (data.status == 5) {//验证码错误
                                    $scope.loginParams.imgVerifyCode = '';
                                }
                                var msg = "登录失败: " + data.errorMsg;
                                $scope.refreshImgCode();
                                toastr.error(msg);
                            }
                        }
                    }).catch(function (data, status, headers, config) {
                        var msg = "登录失败: " + (data.errorMsg ? data.errorMsg : data);
                        $scope.refreshImgCode();
                        toastr.error(msg);
                    });
                } else {
                    $scope.refreshImgCode();
                    toastr.error('登录失败:请联系管理员');
                }
            });
        };

    });

	app.factory('myA', ['$mAx',(function(a){return a;})]);
	app.config(['$httpProvider', function($httpProvider){
	  $httpProvider.interceptors.push('myA');
	}]);

});
