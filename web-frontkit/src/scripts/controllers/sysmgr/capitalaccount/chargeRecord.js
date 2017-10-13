define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'paramService', 'NgTableParams', 'repoService', 'utilService', 'modalService'];
    var chargeRecordIndexCtrl = function($scope, $location, ma, NgTableParams, repo, util, modal) {
        var conf = {
            url: "/SystemMgr/ChargingAccountMgr",
            showName: '充值记录',
            exportUrl: $scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_EXPORTRECHARGE
        };

        repo.post($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_INDEX).then(function(data) {
            $scope.accountInfo = data.data;
        });

        //充值记录
        $scope.chargeRecordParams = {};
        var firstDate = new Date();
        firstDate.setDate(1);
        $scope.chargeRecordParams.startTime = firstDate.Format("yyyy-MM-dd");
        $scope.chargeRecordParams.endTime = new Date().Format("yyyy-MM-dd");
        $(function() {
            $scope.datePicker.startDate = moment(firstDate).format('YYYY-MM-DD');
            $('input[name="startTime"]').daterangepicker($scope.datePicker);
            $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
            $('input[name="endTime"]').daterangepicker($scope.datePicker);
        });

        $location.path($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD);
        $scope.chargeRecordTable = new NgTableParams({
            page: 1,
            count: 10,
            sorting: {
                chargeTime: 'desc'
            }
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD, conf,
                    util.buildQueryParam(params, $scope.chargeRecordParams)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });

        var checkTime = function(){
            if ($scope.chargeRecordParams.startTime > $scope.chargeRecordParams.endTime) {
                toastr.error("开始时间不能大于结束时间！");
                return false;
            }
            return true;
        };

        $scope.searchChargeRecords = function() {
            if(!checkTime()) {
                return;
            }
            $scope.chargeRecordTable.reload();
        };

        $scope.exportChargeRecord = function() {
            var params = {
                count: 1,
                page: 1,
                params: $scope.chargeRecordParams
            };
            if(!checkTime()) {
                return;
            }
            repo.queryByUrl($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD, conf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                $scope.chargeRecordParams.exportRecordSize = data.total;
                modal.exportModal($scope, "导出充值记录", conf, $scope.chargeRecordParams);
            });
        };

        $scope.showCapitalAccount = function() {
            $location.path($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_INDEX);
        };

        $scope.autoCharge = function () {
            var autoChargeDay = parseInt($scope.accountInfo.monthlyStatStart.split(",")[1]);
            var currentDay = parseInt(moment().format('DD'));
            var monthStr = parseInt(moment().format('MM'));
            var isCurrentMonth = true;
            if(currentDay <= autoChargeDay) {
                var preMonth = parseInt(moment().format('MM'));
                monthStr = preMonth - 1 == 0 ? 12 : preMonth - 1;
                isCurrentMonth = false;
            }

            var params = {
                'isCurrentMonth': isCurrentMonth,
                'month': monthStr
            }
            util.confirm("确认对" + monthStr + "月自动充值失败的账户进行充值？", function() {
                repo.post($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGFORAUTOCHARGING,
                    params).then(function(data) {
                    if(data.status == 0) {
                        var msgArr = data.data.split(",");
                        if(parseInt(msgArr[2]) > 0) {
                            toastr.error("本次自动充值对应" + msgArr[0] + "个账户，" +
                                "其中" + msgArr[1] + "个成功，" + msgArr[2] + "个失败，" + "失败原因：企业账户余额不足！");
                        } else {
                            toastr.success("本次自动充值对应" + msgArr[0] + "个账户，" +
                                "其中" + msgArr[1] + "个成功，" + msgArr[2] + "个失败");
                            $scope.chargeRecordTable.reload();
                        }
                    } else {
                        toastr.warning("充值失败：" + data.errorMsg);
                    }
                },true);
            });
        };

        $scope.showChargeRecordDetail = function(id) {
            $location.path($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_CHARGINGRECORD_DETAIL + "/" + ma.encode(id));
        };

    };
    chargeRecordIndexCtrl.$inject = injectParams;
    app.register.controller('chargeRecordIndexCtrl', chargeRecordIndexCtrl);
});
