define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'NgTableParams', 'repoService', 'utilService', 'modalService'];
    var whitelistIndexCtrl = function($scope, $location, NgTableParams, repo, util, modal) {
        var whitelistConf = {
            url: 'InformationMgr/WiteListweb', //基本URL，必填
            showName: '企业白名单', //提示的名称，一般为模块名，必填
            uploadUrl: $scope.urlPerMap.INFO_WITELISTWEB_IMPORT+'?encode=0', //文件上传url
            delUrl:$scope.urlPerMap.INFO_WITELISTWEB_DEL,
            importUrl:$scope.urlPerMap.INFO_WITELISTWEB_IMPORT+'?action=doImport'
        };

        $scope.params = {

        };
		$scope.datePicker.isAllowEmpty = true;
        $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
        $('input[name="_gt_begintime"]').daterangepicker($scope.datePicker);
        $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
        $('input[name="_lt_endtime"]').daterangepicker($scope.datePicker);
        $scope.params._gt_begintime='';
        $scope.params._lt_endtime='';

        $scope.whitelistsTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                var query = angular.copy($scope.params);
                if(query._gt_begintime !== '')query._gt_begintime += " 00:00:00";
                if(query._lt_endtime !== '')query._lt_endtime += " 23:59:59";;
                return repo.queryByUrl($scope.urlPerMap.INFO_WITELISTWEB_LIST,whitelistConf, util.buildQueryParam(params, query)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });
        $scope.selectAll = { checked: false };
        $scope.$watch('selectAll.checked', function(value) {
            util.selectAll($scope.whitelistsTable, value);
        });
        $scope.checkOne = function(whitelists) {
            util.checkOne($scope.selectAll, $scope.whitelistsTable, whitelists);
        };
        $scope.searchWhiteLists = function() {
            $scope.params.userName = $('#userName input:first-child').val();
            if ($scope.params.beginTime > $scope.params.endTime) {
                toastr.error("开始时间不能小于结束时间");
                return;
            }
            $scope.whitelistsTable.reload();
        };

        $scope.deleteWhiteList = function(whitelist) { //删除单个黑名单
            repo.removeOne(whitelistConf, whitelist.id, whitelist.phone).then(function(data) {
                $scope.whitelistsTable.reload();
            });
        };
        $scope.deleteWhiteLists = function() { //删除多个黑名单
            repo.remove(whitelistConf, $scope.whitelistsTable.data, "id").then(function(data) {
                $scope.whitelistsTable.reload();
            });
        };

        $scope.addWhiteList = function() { //新建企业白名单
            util.htmlModal($scope, "新增企业白名单", "modal/addWhiteListForm.tpl.html",
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.diagCls = 'modal-lg';
                    mScope.addParams = {};
                    mScope.okBtn = {
                        text: "保存",
                        click: function() {
                            whitelistConf.updateUrl = $scope.urlPerMap.INFO_WITELISTWEB_ADD;
                            var add = {
                                telphone:mScope.addParams.telphone
                            };
                            repo.updateByPath(whitelistConf, "", add, "新增").then(function(json) {
                                if (json.status == 0) {
                                    $scope.searchWhiteLists();
                                    util.hideModal(modal);
                                }
                            });
                        }
                    };
                });
            //$location.path($scope.urlPerMap.INFO_WITELISTWEB_ADD);
        };

        $scope.importWhiteList = function() {
            //导入文件——数据头映射，从后台获取
            repo.getByUrl(whitelistConf.uploadUrl+'?action=upload').then(function(data) {
                $scope.headDMapToFList = data.data;
                modal.importModal($scope, "导入企业白名单", whitelistConf);
            });
        };
    };

    whitelistIndexCtrl.$inject = injectParams;
    app.register.controller('whitelistIndexCtrl', whitelistIndexCtrl);
});
