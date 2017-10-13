define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'NgTableParams', 'repoService', 'utilService', 'modalService'];
    var blacklistIndexCtrl = function($scope, $location, NgTableParams, repo, util, modal) {
        var blacklistConf = {
            url: 'InformationMgr/BlackList', //基本URL，必填
            showName: '黑名单', //提示的名称，一般为模块名，必填
            uploadUrl: $scope.urlPerMap.INFO_BLACKLIST_IMPORT+'?encode=0', //文件上传url
            delUrl:$scope.urlPerMap.INFO_BLACKLIST_DEL,
            importUrl:$scope.urlPerMap.INFO_BLACKLIST_IMPORT+'?encode=0',
            exportUrl:$scope.urlPerMap.INFO_BLACKLIST_EXPORT
        };
        $scope.blacklistParams = {

        };
        repo.queryByUrl('/common/complete/fetchEnterprise',blacklistConf, '').then(function(data) {
            $scope.enterprise = {name : data.data.enterpriseName,id:data.data.enterpriseId};
        });
        $scope.blacklistParams.type = '-1';
        $scope.blacklistParams.entDomain = "";
        $scope.domain = {};
        $scope.handleFromDesc = {};
        $scope.handleFromDesc[1] = "后台";
        $scope.handleFromDesc[2] = "前台";
        $scope.EntDomains = xpath.service("common/complete/domain?domain=");

        var watch = $scope.$watch('blacklistParams.type', function(newValue, oldValue, scope) {
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

        $scope.blacklistsTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.INFO_BLACKLIST_LIST,blacklistConf, util.buildQueryParam(params, $scope.blacklistParams)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });
        $scope.selectAll = { checked: false };
        $scope.$watch('selectAll.checked', function(value) {
            util.selectAll($scope.blacklistsTable, value);
        });
        $scope.checkOne = function(blacklists) {
            util.checkOne($scope.selectAll, $scope.blacklistsTable, blacklists);
        };
        $scope.searchBlacklists = function() {
            if(!angular.isDefined($scope.domain.item)&&!util.isEmpty($scope.blacklistParams.targetName)){
                util.operateInfoModel('', "请选择正确的所属对象！");
                return;
            }
            if($scope.blacklistParams.type == '0' || $scope.blacklistParams.type == '5'){
                if($scope.domain.item){
                    $scope.blacklistParams.target = $scope.domain.item.originalObject.id;
                }else{
                    $scope.blacklistParams.target = null;
                }

            }else if($scope.blacklistParams.type == '2'){
                $scope.blacklistParams.target = $scope.enterprise.id;
            }
            $scope.blacklistsTable.reload();
        };

        $scope.deleteBlacklist = function(blacklist) { //删除单个黑名单
            repo.removeOne(blacklistConf, blacklist.id, blacklist.phone).then(function(data) {
                $scope.blacklistsTable.reload();
            });
        };
        $scope.deleteBlacklists = function() { //删除多个黑名单
            repo.remove(blacklistConf, $scope.blacklistsTable.data, "id").then(function(data) {
                $scope.blacklistsTable.reload();
            });
        };
        $scope.watchDomain = function (inputValue) {
            $scope.domain.item = undefined;
            $scope.blacklistParams.targetName = inputValue;
        }
        $scope.addBlacklist = function() { //新建运营商号段
            $location.path($scope.urlPerMap.INFO_BLACKLIST_ADD);
        };

        $scope.importBlacklists = function() {
            //导入文件——数据头映射，从后台获取
            repo.getByUrl(blacklistConf.uploadUrl+'?encode=0').then(function(data) {
                $scope.headDMapToFList = data.data;
                modal.importModal($scope, "导入黑名单", blacklistConf);
            });
        };

        $scope.changeBlacklistType = function() {
            $scope.domain.item = undefined;
            $scope.blacklistParams.entDomain = "";
            $scope.blacklistParams.entName = "";
            $scope.blacklistParams.targetName = "";
            $scope.blacklistParams.target = null;
            if($scope.blacklistParams.type==2){
                $scope.blacklistParams.target = $scope.enterprise.id;
            }
        }

        $scope.exportBlacklists = function() {
            var queryParams = {};
            queryParams.type = $scope.blacklistParams.type;
            queryParams.phone = $scope.blacklistParams._lk_phone;
            queryParams.user_name = $("#domain_value").val();

            var params = {
                count: 1,
                page: 1,
                params: queryParams
            };
            repo.queryByUrl($scope.urlPerMap.INFO_BLACKLIST_LIST,blacklistConf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                queryParams.exportRecordSize = data.total;
                modal.exportModal($scope, "导出黑名单", blacklistConf, queryParams);
            });
        };
    };

    blacklistIndexCtrl.$inject = injectParams;
    app.register.controller('blacklistIndexCtrl', blacklistIndexCtrl);
});
