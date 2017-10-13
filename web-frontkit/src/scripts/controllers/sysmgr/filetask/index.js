define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'NgTableParams', 'repoService', 'utilService'];
    var fileTaskIndexCtrl = function($scope, $location, NgTableParams, repo, util) {
        var filetaskConf = {
            url: $scope.urlPerMap.SYSTEMMGR_TASKMGR_IMPORT,
            downloadUrl: $scope.urlPerMap.SYSTEMMGR_TASKMGR_EXPORT,
            dataServerDownloadUrl: '/common/download',
            showName: '导入导出'
        };

        $scope.filetaskParams = {'state': -1};
        $scope.downloadText = "失败文件";
        $scope.filetaskParams.beginTime = new Date().Format("yyyy-MM-dd") + " 00:00:00";
        $scope.filetaskParams.endTime = new Date().Format("yyyy-MM-dd") + " 23:59:59";
        $scope.filetaskParams.state = "-1";
        $scope.filetaskParams.type = 1; //默认为导入
        $scope.tab1 = "active";

        $scope.loadExport = function() {
            $scope.tab1 = "";
            $scope.tab2 = "active";
            $scope.downloadText = "下载";
            $scope.filetaskParams.type = 2;
            filetaskConf.url = $scope.urlPerMap.SYSTEMMGR_TASKMGR_EXPORT;
            $scope.filetasksTable.reload();
        };
        $scope.loadImport = function() {
            $scope.tab1 = "active";
            $scope.tab2 = "";
            $scope.downloadText = "失败文件";
            $scope.filetaskParams.type = 1;
            filetaskConf.url = $scope.urlPerMap.SYSTEMMGR_TASKMGR_IMPORT;
            $scope.filetasksTable.reload();
        };
        $scope.urlExport = function() {
            //同步更新导入导出红点数
            if ($scope.globalVar.upAndLoadCount > 0) {
                repo.post('common/updateUpAndLoadCount', 2, function (data) {
                    repo.post("common/upAndLoadCount").then(function (data) {
                        if (angular.isDefined(data.data) && data.data != null) {
                            $scope.globalVar.upAndLoadCount = data.data[0];
                        }
                    });
                });
            }

            $location.url($scope.urlPerMap.SYSTEMMGR_TASKMGR_INDEX + '?type=2');
        };
        $scope.urlImport = function() {
            //同步更新导入导出红点数
            if ($scope.globalVar.upAndLoadCount > 0) {
                repo.post('common/updateUpAndLoadCount', 1, function (data) {
                    repo.post("common/upAndLoadCount").then(function (data) {
                        if (angular.isDefined(data.data) && data.data != null) {
                            $scope.globalVar.upAndLoadCount = data.data[0];
                        }
                    });
                });
            }

            $location.url($scope.urlPerMap.SYSTEMMGR_TASKMGR_INDEX + '?type=1');
        };
        $scope.showReport = function(message) {
            if (message == null) {
                message = "无处理结果信息";
            }
            toastr.info(message);
        };

        $scope.downloadResult = function(filetask) {
            var path = null;
            if(filetask.taskType == 1) {
                var filePath = encodeURIComponent(filetask.fileName);
                path = filetaskConf.downloadUrl + "?fileName=" + filePath + "&type=" + filetask.type.index;
            } else {
                path = filetaskConf.dataServerDownloadUrl + "?path=/" + filetask.fileAddress + filetask.fileName;
            }
            repo.downloadFile(path);
        };

        $scope.filetasksTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl(filetaskConf.url, filetaskConf,
                    util.buildQueryParam(params, $scope.filetaskParams)).then(function(data) {
                    params.total(data.total);
                    angular.forEach(data.data, function(data) {
                        var width = data.percent + "%";
                        data.progressStyle = { "width": width };
                        if (data.percent < 10) {
                            data.progressStyle.color = "#333333";
                        }
                        if(data.percent == 100) {
                            data.canShowReport = true;
                            data.canShowDownload = true;
                        } else {
                            data.canShowReport = false;
                            data.canShowDownload = false;
                        }
                        var str = data.progress.split("/");
                        if (str[0] == str[1] && $scope.filetaskParams.type == 1) {
                            data.canShowDownload = false;
                        }
                        if (str[0] == 0 && str[1] == 0 && $scope.filetaskParams.type == 2) {
                            data.canShowDownload = false;
                        }
                    });
                    return data.data;
                });
            }
        });

        $scope.searchFiletasks = function() {
            if ($scope.filetaskParams.beginTime > $scope.filetaskParams.endTime) {
                toastr.error("开始时间不能大于结束时间!");
                return;
            }
            $scope.filetasksTable.reload();
        };
        $(function() {
            $scope.dateTimePicker.startDate = moment().format('YYYY-MM-DD') + ' 00:00:00';
			$scope.dateTimePicker.isAllowEmpty = true;
            $('input[name="startTime"]').daterangepicker($scope.dateTimePicker);
            $scope.dateTimePicker.startDate = moment().format('YYYY-MM-DD') + ' 23:59:59';
            $('input[name="endTime"]').daterangepicker($scope.dateTimePicker);
        });

        //页面业务逻辑
        var queryData = function(item) {
            var sValue = $location.absUrl().match(new RegExp("[\?\&]" + item + "=([^\&]*)(\&?)", "i"));
            return sValue ? sValue[1] : sValue;
        };
        if (queryData("type") == 2) {
            $scope.loadExport();
        } else if (queryData("type") == 1) {
            $scope.loadImport();
        }
    };
    fileTaskIndexCtrl.$inject = injectParams;
    app.register.controller('fileTaskIndexCtrl', fileTaskIndexCtrl);
});
