define(["app"], function(app) {
    var injectParams = ['$scope', 'paramService', '$routeParams', 'repoService', 'utilService', '$location'];
    var userAddIndexCtrl = function($scope, ma, $routeParams, repo, util, $location) {
        var conf = {
            url: '/SystemMgr/AccountMgr',
            showName: '用户新增' //提示的名称，一般为模块名，必填,
        };

        var parentId = ma.decode($routeParams.id);

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
                        $scope.user.customerSignature = 0;
                        $scope.customerSignatureDis = false;
                    }
                });

                watchObj.unWatchProtocolType = $scope.$watch('user.protocolType', function(value) {
                    if(value == 3) {
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

        $scope.initUserData = function() {
            $scope.user = {
                bizTypes: [],
                roles: []
            };
            $scope.user.accountType = 1;
            $scope.user.sigLocation = 0;
            $scope.user.parentId = parentId;
            $scope.user.linkNum = 1;
            $scope.user.sendSpeed = 100;
            $scope.isRightUserName = true;

            //获取企业信息
            repo.post("/common/getEntInfo").then(function(data) {
                $scope.enterprise = data.data;
                $scope.showDomain = '@' + data.data.domain;
                $scope.accountTypes = [
                    {id: 1, name: 'web'},
                    {id: 2, name: '接口'}
                ];
                if($scope.enterprise.transparentSend) {
                    $scope.accountTypes.push({id: 3, name: '透传'});
                }
            });

            repo.queryByPath(conf, "/AddAccount/Pre", $scope.user).then(function(data) {
                angular.forEach(data.data.bizTypes, function(bizType){
                    var tempType = {id: bizType.id, name: bizType.name, bound: bizType.bound,
                        numExtendSize: bizType.numExtendSize};
                    $scope.user.bizTypes.push(tempType);
                });
                angular.forEach(data.data.roles, function(role){
                    var tempRole = {id: role.id, name: role.name, checked: role.checked, default: role.default};
                    $scope.user.roles.push(tempRole);
                });
                $scope.deptName = data.data.deptName;
                $scope.user.path = data.data.fullPath;
                $scope.putCommonBizType(data.data);
            });

            watchObj.openWatch();
        };
        $scope.initUserData();

        $scope.putCommonBizType = function(user) {
            var tempBizTypes = new Array();
            angular.forEach(user.bizTypes, function(bizType){
                if(bizType.bound) {
                    tempBizTypes.push(bizType);
                }
            });
            $scope.commonBizTypes = tempBizTypes;
        };

        $scope.save = function(typeIndex) {
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

            //用户扩展码校验
            if (!angular.isUndefined($scope.user.identify) && $scope.user.identify.length > minExtendSize) {
                util.confirm('用户扩展码长度超出了所勾选业务类型绑定端口的最小可扩展长度，超出部分将被截取和影响上行接收，确认是否保存？', function(){
                    commit(saveUser, typeIndex);
                });
            } else {
                commit(saveUser, typeIndex);
            }
        };

        var commit = function(saveUser, typeIndex){
            repo.post($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_ADDACCOUNT, saveUser).then(function(data) {
                if (data.status == 0) {
                    toastr.success('新增用户成功！');
                    watchObj.stopWatch();
                    util.operateInfoCustomerModel($scope, data.data, function(modal) {
                        var mScope = modal.$scope;
                        mScope.okBtn = {
                            text : '确定',
                            click : function() {
                                util.hideModal(modal);
                                if(typeIndex == 1) {
                                    window.history.back();
                                } else {
                                    $scope.user = {};
                                    $scope.initUserData();
                                }
                            }
                        };
                        mScope.closeBtn = {
                            hide: true
                        };
                    });
                } else {
                    toastr.error("新增用户失败：" + data.errorMsg);
                }
            });
        }

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

    };
    userAddIndexCtrl.$inject = injectParams;
    app.register.controller('userAddIndexCtrl', userAddIndexCtrl);
});
