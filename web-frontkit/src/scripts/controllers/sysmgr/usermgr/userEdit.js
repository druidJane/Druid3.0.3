define(["app"], function(app) {
    var injectParams = ['$scope', 'paramService', '$routeParams', 'repoService', 'utilService'];
    var userEditIndexCtrl = function($scope, ma, $routeParams, repo, util) {
        var conf = {
            url: '/SystemMgr/AccountMgr',
            showName: '用户修改' //提示的名称，一般为模块名，必填,
        };
        var callBackAddressPattern = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9]):[1-9]{1}[0-9]{0,5}$/;

        var userId = ma.decode($routeParams.id);
        $scope.cusSignatureInit = true;
        $scope.user = {
            accountType: 1,
            id: userId,
            bizTypes: [],
            roles: []
        };
        $scope.isRightUserName = false;

        var watchObj = {
            unWatchAccountType: null,
            unWatchProtocolType: null,
            openWatch:  function() {
                watchObj.unWatchAccountType = $scope.$watch('user.accountType', function(value) {
                    if(value == 3) {
                        $scope.showSrcPortDom = true;
                        $scope.showSendSpeedDom = true;
                        $scope.showLinkNumDom = true;

                        $scope.userNameLength = 6;
                        $scope.showDomain = '';
                        $scope.user.customerSignature = 1;
                        $scope.customerSignatureDis = true;
                    } else {
                        $scope.showCallbackAddressDom = false;
                        $scope.showSrcPortDom = false;
                        $scope.showSendSpeedDom = false;
                        $scope.showLinkNumDom = false;

                        $scope.userNameLength = 30;
                        if(!angular.isUndefined($scope.enterprise)) {
                            $scope.showDomain = '@' + $scope.enterprise.domain;
                        }
                        if(!$scope.cusSignatureInit) {
                            $scope.user.customerSignature = 0;   
                        }
                        $scope.customerSignatureDis = false;
                    }
                });

                watchObj.unWatchProtocolType = $scope.$watch('user.protocolType', function(newVal, oldVal) {
                    if(newVal == 3) {
                        $scope.showCallbackAddressDom = true;
                    } else {
                        $scope.showCallbackAddressDom = false;
                    }
                });
            },
            stopWatch: function() {
                if(watchObj.unWatchAccountType != null) {
                    watchObj.unWatchAccountType();
                    watchObj.unWatchAccountType = null;
                }
                if(watchObj.unWatchProtocolType != null) {
                    watchObj.unWatchProtocolType();
                    watchObj.unWatchProtocolType = null;
                }
            }
        };

        //获取企业信息
        repo.post("/common/getEntInfo").then(function(data) {
            $scope.enterprise = data.data;
            $scope.showDomain = '@' + data.data.domain;
        });

        repo.queryByPath(conf, "/UpdateAccount/Pre", $scope.user).then(function(data) {
            $scope.deptName = data.data.pDept.enterpriseName;
            setInputUser(data.data.user);
            $scope.putCommonBizType(data.data.user);
            if(data.data.user.accountType.index == 3) {
                $scope.accountTypes = [{'id':3, 'name':'透传'}];
                $scope.accountTypeDis = true;
                $scope.pwdSendName = '重置透传密码';
            } else {
                $scope.accountTypes = [{'id':1, 'name':'web'},{'id':2, 'name':'接口'}];
                $scope.pwdSendName = '重置发送密码';
            }
        });

        var setInputUser = function(user) {
            angular.forEach(user.bizTypes, function(bizType){
                var tempType = {id: bizType.id, name: bizType.name, bound: bizType.bound,
                    numExtendSize: bizType.numExtendSize};
                $scope.user.bizTypes.push(tempType);
            });
            angular.forEach(user.roles, function(role){
                var tempRole = {id: role.id, name: role.name, checked: role.checked, default: role.default};
                $scope.user.roles.push(tempRole);
            });
            $scope.user.path = user.fullPath;
            $scope.user.userName = user.userName.split('@')[0];
            $scope.user.linkMan = user.linkMan;
            $scope.user.state = user.state.index;
            if(user.signature == '' && $scope.enterprise.signature == ''
                && user.state.index != 0) {
                $scope.userStateDisable = true;
            }
            $scope.user.phone = user.phone;
            $scope.user.identify = user.identify;
            $scope.user.signature = user.signature;
            $scope.user.sigLocation = user.sigLocation;
            $scope.user.commonBizTypeId = user.commonBizTypeId;
            $scope.user.accountType = user.accountType.index;
            $scope.user.protocolType = user.protocolType.index;
            $scope.user.srcPort = user.srcPort;
            $scope.user.callbackAddress = user.callbackAddress;
            $scope.user.customerSignature = user.customerSignature;
            $scope.user.sendSpeed = user.sendSpeed;
            $scope.user.linkNum = user.linkNum;
            $scope.user.remark = user.remark;

        };

        $scope.resetSendPwd = function() {
            repo.post("/common/resetSendOrMidPwd", $scope.user).then(function(data) {
                if(data.status == 0) {
                    util.operateInfoModel($scope, data.data);
                }
            });
        };

        $scope.resetLoginPwd = function() {
            repo.post("/common/resetLoginPwd", $scope.user).then(function(data) {
                if(data.status == 0) {
                    util.operateInfoModel($scope, data.data);
                }
            });
        };

        watchObj.openWatch();

        $scope.putCommonBizType = function(user) {
            var tempBizTypes = new Array();
            angular.forEach(user.bizTypes, function(item){
                if(item.bound) {
                    tempBizTypes.push(item);
                }
            });
            $scope.commonBizTypes = tempBizTypes;
        };

        $scope.save = function() {
            var isChecked = false;
            var minExtendSize = 1000;
            for (var i = 0; i < $scope.user.bizTypes.length; i++) {
                if ($scope.user.bizTypes[i].bound) {
                    if (minExtendSize > $scope.user.bizTypes[i].numExtendSize) {
                        minExtendSize = $scope.user.bizTypes[i].numExtendSize;
                    }
                    isChecked = true;
                }
            }
            if (!isChecked) {
                toastr.warning("请至少勾选一个业务类型！");
                return;
            }

            if($scope.user.userName.length > $scope.userNameLength) {
                $scope.userNameErrorMsg = "用户账号长度不能超过" + $scope.userNameLength + "个字符";
                $scope.userNameError = true;
                return;
            }

            var saveUser = angular.copy($scope.user);
            if ($scope.user.accountType != 3) {
                saveUser.userName = saveUser.userName + $scope.showDomain;
            }

            if($scope.user.protocolType != 3) {
                saveUser.callbackAddress = '';
            }

            //回调地址校验
            if($scope.user.protocolType == 3) {
                if(!callBackAddressPattern.test($scope.user.callbackAddress)){
                    $scope.callbackAddressError = true;
                    return;
                }
            }

            //用户扩展码校验
            if ($scope.user.identify != null && $scope.user.identify.length > minExtendSize) {
                util.confirm('用户扩展码长度超出了所勾选业务类型绑定端口的最小可扩展长度，超出部分将被截取和影响上行接收，确认是否保存？', function(){
                    commit(saveUser);
                });
            } else {
                commit(saveUser);
            }
        };

        var commit = function(saveUser) {
            repo.post($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_UPDATEACCOUNT, saveUser).then(function(data) {
                if (data.status == 0) {
                    toastr.success("修改用户成功！");
                    watchObj.stopWatch();
                    window.history.back();
                } else {
                    toastr.error("修改用户失败：" + data.errorMsg);
                }
            });
        };

        $scope.userNameBlur = function () {
            if(angular.isDefined($scope.user.userName)) {
                if($scope.user.userName.length <= $scope.userNameLength) {
                    $scope.userNameError = false;
                }
            }
            if(angular.isDefined($scope.userAddForm) && !$scope.userAddForm.$pristine) {
                $scope.userNameError = false;
            }
        };

        $scope.checkUserName = function () {
            $scope.userNameErrorMsg = "";
            if (angular.isUndefined($scope.user.userName) || $scope.user.userName == "") {
                $scope.userNameErrorMsg = "“用户账号”不能为空";
                $scope.isRightUserName = true;
                return false;
            }

            var Regx = /^[A-Za-z0-9]*$/;
            if (!Regx.test($scope.user.userName)) {
                $scope.userNameErrorMsg = "“用户账号”需由数字或字母组成";
                $scope.isRightUserName = true;
                return false;
            }

            if(angular.isDefined($scope.user.userName)) {
                if ($scope.user.userName.length > $scope.userNameLength) {
                    $scope.userNameErrorMsg = "用户账号长度不能超过" + $scope.userNameLength + "个字符";
                    $scope.isRightUserName = true;
                    return false;
                }
            }
            $scope.isRightUserName = false;

            /* if(angular.isDefined($scope.userAddForm) && !$scope.userAddForm.$pristine) {
             $scope.userNameError = false;
             }*/
        }

        $scope.callbackAddressBlur = function () {
            if($scope.user.protocolType == 3) {
                if(callBackAddressPattern.test($scope.user.callbackAddress)){
                    $scope.callbackAddressError = false;
                }
            }
        };

        $scope.notInitLoad = function() {
            $scope.cusSignatureInit = false;
        };

    };
    userEditIndexCtrl.$inject = injectParams;
    app.register.controller('userEditIndexCtrl', userEditIndexCtrl);
});
