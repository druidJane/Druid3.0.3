define(["app"], function(app) {
    var injectParams = ['$scope', '$modal', 'repoService', 'NgTableParams', 'utilService'];
    var sysLogIndexCtrl = function($scope, $modal, repo, NgTableParams, util) {
        var sysLogIndexConf = {
            url: $scope.urlPerMap.SYSTEMMGR_LOGMGR_LIST, //基本URL，必填
            showName: '日志管理' //提示的名称，一般为模块名，必填
        };

        $scope.params = {};
        $scope.params.startTime = new Date().Format("yyyy-MM-dd");
        $scope.params.endTime = new Date().Format("yyyy-MM-dd");
        $(function() {
            $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
            $('input[name="startTime"]').daterangepicker($scope.datePicker);
            $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
            $('input[name="endTime"]').daterangepicker($scope.datePicker);
        });

        //自动补全用户账号
        $scope.acompleteUserNameUrl = xpath.service("common/complete/fetchUserData?userName=");

        $scope.watchUserName = function() {};

        repo.getByUrl($scope.urlPerMap.SYSTEMMGR_LOGMGR_INDEX_OPERATION_TYPE).then(function(data){
            $scope.operateTypes = data.data;
        });

        $scope.sysLogTable = new NgTableParams({
            page: 1,
            count: 10,
            sorting: {
                operateTime: 'desc'
            }
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl(sysLogIndexConf.url, sysLogIndexConf,
                    util.buildQueryParam(params, $scope.params)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });

        //列表头选项
        $scope.cols = [
            { title: "操作人", show: true },
            { title: "操作类型", show: true },
            { title: "操作对象", show: true },
            { title: "对象标识", show: true },
            { title: "操作时间", show: true }
        ];

        //检索按钮
        $scope.search = function() {
            $scope.params.userName = angular.element("#completeUserName_value").val();
            if ($scope.params.startTime > $scope.params.endTime) {
                toastr.error("开始时间不能大于结束时间！");
                return;
            }
            $scope.sysLogTable.reload();
        }
    };
    sysLogIndexCtrl.$inject = injectParams;
    app.register.controller('sysLogIndexCtrl', sysLogIndexCtrl);
});
