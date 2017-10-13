define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'paramService', 'NgTableParams', 'repoService', 'utilService', 'modalService'];
    var capitalAccountIndexCtrl = function($scope, $location, ma, NgTableParams, repo, util, modal) {
        var conf = {
            url: "/SystemMgr/ChargingAccountMgr",
            showName: '计费账户',
            showAddUrl: 'sysmgr/capitalaccount/add.html',
            showEditUrl: 'sysmgr/capitalaccount/edit.html',
            showChargingUrl: 'sysmgr/capitalaccount/charging.html',
            delUrl: '/SystemMgr/ChargingAccountMgr/DeleteAccount',
            uploadUrl: $scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_IMPORTINGCHARGING+'?encode=0&action=upload',
            importUrl: $scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_IMPORTINGCHARGING+'?encode=0&action=doImport',
            exportUrl: $scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_EXPORTRECHARGE
        };

        //加载企业总账户信息
        $scope.loadAccountInfo = function() {
            repo.post($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_INDEX).then(function(data) {
                $scope.accountInfo = data.data;
            });
        };

        $scope.loadCapitalAccountData = function() {
            $scope.loadAccountInfo();
            $scope.accountParams = {};
            $scope.capitalAccountTable = new NgTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function(params) {
                    return repo.queryByUrl($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_ACCOUNTLIST, conf,
                        util.buildQueryParam(params, $scope.accountParams)).then(function(data) {
                        params.total(data.total);
                        return data.data;
                    });
                }
            });

            $scope.searchCapitalAccounts = function() {
                $scope.capitalAccountTable.reload();
            };

            $scope.selectAll = { checked: false };
            $scope.$watch('selectAll.checked', function(value) {
                util.selectAll($scope.capitalAccountTable, value);
                if($scope.capitalAccountTable.data.length == 1) {
                    $scope.checkedAccount = angular.copy($scope.capitalAccountTable.data[0]);
                }
            });
            $scope.checkOne = function(capitalAccount) {
                util.checkOne($scope.selectAll, $scope.capitalAccountTable, capitalAccount);
                $scope.checkedAccount = $scope.selectAll.lastSelected;
            };

            var watchObj = {
                unWatchChargeWay: null,
                unWatchDeductType: null,
                openWatch:  function(watchScope) {
                    watchObj.unWatchChargeWay = watchScope.$watch('capitalAccount.chargeWay', function(value) {
                        if(value == 1) {
                            watchScope.showAutoChargeMoneyDom = true;
                        } else {
                            watchScope.showAutoChargeMoneyDom = false;
                        }
                    });

                    watchObj.unWatchDeductType = watchScope.$watch('capitalAccount.deductType', function(value) {
                        if(value == 0) {
                            watchScope.showDeductTypeDom = true;
                        } else {
                            watchScope.showDeductTypeDom = false;
                        }
                    });
                },
                stopWatch: function() {
                    if(watchObj.unWatchChargeWay != null) {
                        watchObj.unWatchChargeWay();
                        watchObj.unWatchChargeWay = null;
                    }
                    if(watchObj.unWatchDeductType != null) {
                        watchObj.unWatchDeductType();
                        watchObj.unWatchDeductType = null;
                    }
                }
            };

            $scope.showAdd = function() {
                util.commonModal($scope, '新增计费账户', conf.showAddUrl, function(modal) {
                    $scope.loadAccountInfo();
                    var mScope = modal.$scope;
                    mScope.capitalAccount = {};
                    //此处使用watch来监视模型的变化主要是解决w5c无法校验后续新增DOM的问题
                    watchObj.openWatch(mScope);
                    mScope.doAdd = function() {
                        repo.post($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_ADDACCOUNT,
                            mScope.capitalAccount).then(function(data) {
                            if(data.status == 0) {
                                toastr.success("新增计费账户成功！");
                                $scope.capitalAccountTable.reload();
                                util.hideModal(modal);
                            } else {
                                toastr.error("新增计费账户失败：" + data.errorMsg);
                            }
                            //销毁watch
                            watchObj.stopWatch();
                        });
                    };
                    mScope.okBtn = {
                        text: '保存',
                        click: function() {
                            if($scope.validCapitalAccount(mScope)) {
                                mScope.doAdd();
                            }
                        }
                    }
                });
            };

            $scope.validCapitalAccount = function(mScope) {
                var autoChargeMoney = mScope.capitalAccount.autoChargeMoney;
                var smsPrice = mScope.capitalAccount.smsPrice;
                var mmsPrice = mScope.capitalAccount.mmsPrice;
                var autoChargeMoneyPattern = /^\d{1,8}(\.\d{1,4})?$/;
                var msgPricePattern = /^\d{1}(\.\d{1,4})?$/;
                var chargeWay = mScope.capitalAccount.chargeWay;
                var deductType = mScope.capitalAccount.deductType;
                if(chargeWay == 1) {
                    if(!(autoChargeMoney >= 1 && autoChargeMoney <= 99999999 && autoChargeMoneyPattern.test(autoChargeMoney))){
                        toastr.warning("自动充值金额仅允许输入1-99999999之间的保留小数点后四位的数值！");
                        return false;
                    }
                }
                if(deductType == 0) {
                    if(!(smsPrice > 0 && smsPrice < 10 && msgPricePattern.test(smsPrice))){
                        toastr.warning("短信单价仅允许输入0-10之间的保留小数点后四位的数值！");
                        return false;
                    }
                    if(!(mmsPrice > 0 && mmsPrice < 10 && msgPricePattern.test(mmsPrice))){
                        toastr.warning("彩信单价仅允许输入0-10之间的保留小数点后四位的数值！");
                        return false;
                    }
                }
                return true;
            };

            $scope.showEdit = function() {
                var checked = util.selectedItems($scope.capitalAccountTable.data, "id");
                if (checked.length != 1) {
                    toastr.warning("请选择要修改的计费账户！一次只能修改一个计费账户！");
                    return;
                }
                util.commonModal($scope, '修改计费账户', conf.showEditUrl, function(modal) {
                    $scope.loadAccountInfo();
                    var mScope = modal.$scope;
                    mScope.capitalAccount = angular.copy($scope.checkedAccount);
                    mScope.payingWay = mScope.capitalAccount.payingWay = mScope.capitalAccount.payingWay.index;
                    mScope.chargeWay = mScope.capitalAccount.chargeWay = mScope.capitalAccount.chargeWay.index;
                    mScope.deductType = mScope.capitalAccount.deductType = mScope.capitalAccount.deductType.index;
                    if(mScope.capitalAccount.deductType == 1) {
                        mScope.capitalAccount.smsPrice = 0.0500;
                        mScope.capitalAccount.mmsPrice = 0.2000;
                    }
                    watchObj.openWatch(mScope);
                    mScope.doAdd = function() {
                        repo.post($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_UPDATEACCOUNT,
                            mScope.capitalAccount).then(function(data) {
                            if(data.status == 0) {
                                toastr.success("修改计费账户成功!");
                                $scope.capitalAccountTable.reload();
                                util.hideModal(modal);
                            } else {
                                toastr.error("修改计费账户失败：" + data.errorMsg);
                            }
                            watchObj.stopWatch();
                        });
                    };
                    mScope.okBtn = {
                        text: '保存',
                        click: function() {
                            if($scope.validCapitalAccount(mScope)) {
                                mScope.doAdd();
                            }
                        }
                    }
                });
            };

            $scope.delAccount = function() {
                repo.remove(conf, $scope.capitalAccountTable.data, "id").then(function(data) {
                    $scope.capitalAccountTable.reload();
                });
            };

            $scope.showDetail = function(id) {
                $location.path($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_DETAIL + "/" + ma.encode(id));
            };

            $scope.charging = function(account) {
                util.commonModal($scope, "计费账户充值", conf.showChargingUrl,
                    function(modal) {
                        var mScope = modal.$scope;
                        mScope.chargeAccount = {
                            id: account.id,
                            accountName: account.accountName,
                            balance: account.balance
                        };
                        mScope.doAdd = function(){
                            if(validateChargeMoney(mScope, account)){
                                repo.post($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGING, mScope.chargeAccount).then(
                                    function(data){
                                        if (data.status == 0) {
                                            toastr.success("充值成功！");
                                            $scope.capitalAccountTable.reload();
                                            util.hideModal(modal);
                                        } else {
                                            toastr.error("充值失败：" + data.errorMsg);
                                        }
                                    },true
                                );
                            }
                        };

                        mScope.okBtn = {
                            text : '保存',
                            click : function() {
                                mScope.doAdd();
                            }
                        };
                    }
                );
            };

            var validateChargeMoney = function(mScope, account) {
                if(mScope.chargeAccount.chargeMoney == 0) {
                    toastr.warning("本次充值金额不能等于0元！");
                    return false;
                }
                if(account.chargeRatio != 0 &&
                    mScope.chargeAccount.chargeMoney > $scope.accountInfo.differenceBalance) {
                    toastr.warning("本次充值金额不能大于剩余信用额度！");
                    return false;
                }
                if(mScope.chargeAccount.chargeMoney > 99999999) {
                    toastr.warning("本次充值金额不能大于99999999元！");
                    return false;
                }
                return true;
            };

            $scope.importCharging = function() {
                //导入文件——数据头映射，从后台获取
                repo.getByUrl(conf.url + "/ImportingCharging").then(function(data) {
                    $scope.headDMapToFList = data.data;
                    modal.importModal($scope, "导入充值", conf);
                });
            };

            $scope.userSelector = function(id) {
                $location.path($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR + "/" + ma.encode(id));
            };
        };

        $scope.showChargeRecord = function() {
            $location.path($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD);
        };

        if($scope.hasPermission($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_ACCOUNTLIST)) {
            $scope.loadCapitalAccountData();
        } else {
            $scope.showChargeRecord();
        }
    };
    capitalAccountIndexCtrl.$inject = injectParams;
    app.register.controller('capitalAccountIndexCtrl', capitalAccountIndexCtrl);
});
