define(["app"], function(app) {
    var injectParams = ['$scope', '$location','$timeout', 'NgTableParams', 'repoService', 'utilService', 'modalService'];
    var keywordIndexCtrl = function($scope, $location,$timeout, NgTableParams, repo, util, modal) {
        var keywordConf = {
            url: 'InformationMgr/Keyword', //基本URL，必填
            showName: '非法关键字', //提示的名称，一般为模块名，必填
            uploadUrl: $scope.urlPerMap.INFO_KEYWORD_IMPORT+'?encode=0', //文件上传url
            delUrl:$scope.urlPerMap.INFO_KEYWORD_DEL,
            importUrl:$scope.urlPerMap.INFO_KEYWORD_IMPORT+'?action=doImport',
            exportUrl:$scope.urlPerMap.INFO_KEYWORD_EXPORT
        };

        $scope.params = {

        };

        $scope.keywordsTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.INFO_KEYWORD_LIST,keywordConf, util.buildQueryParam(params, $scope.params)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });
        $scope.selectAll = { checked: false };
        $scope.$watch('selectAll.checked', function(value) {
            util.selectAll($scope.keywordsTable, value);
        });
        $scope.checkOne = function(keyword) {
            util.checkOne($scope.selectAll, $scope.keywordsTable, keyword);
            $scope.checkedKeyword = $scope.selectAll.lastSelected;
        };
        $scope.searchKeywords = function() {
            $scope.keywordsTable.reload();
        };

        $scope.deleteKeywords = function(keyword) { //删除单个非法关键字
            repo.removeOne(keywordConf, keyword.id, keyword.phone).then(function(data) {
                $scope.keywordsTable.reload();
            });
        };
        $scope.deleteKeywords = function() { //删除多个非法关键字
            repo.remove(keywordConf, $scope.keywordsTable.data, "id").then(function(data) {
                $scope.keywordsTable.reload();
            });
        };

        $scope.addKeyword = function() { //新建非法关键字
            util.htmlModal($scope, "新增非法关键字", "modal/addKeywordForm.tpl.html",
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.diagCls = 'modal-lg';
                    mScope.keyword = {mode:'add'};
                    var disabled = false;
                    mScope.okBtn = {
                        text: "保存",
                        click: function() {
                            if(disabled){
                                return;
                            }
                            $timeout(function() {
                                disabled = false;
                            }, 1000 , false);
                            keywordConf.updateUrl = $scope.urlPerMap.INFO_KEYWORD_ADD;
                            var add = {
                                keywordName:mScope.keyword.keywordName
                            };
                            repo.updateByPath(keywordConf, "", add, "新增").then(function(json) {
                                if (json.status == 0) {
                                    $scope.searchKeywords();
                                    util.hideModal(modal);
                                }
                            });
                            disabled = true;
                        }
                    };
                });
        };

        $scope.importKeyword = function() {
            //导入文件——数据头映射，从后台获取
            repo.getByUrl(keywordConf.uploadUrl+'?action=upload').then(function(data) {
                $scope.headDMapToFList = data.data;
                modal.importModal($scope, "导入非法关键字", keywordConf);
            });
        };
        $scope.updateKeywords = function () {
            var checked = util.selectedItems($scope.keywordsTable.data);
            if (checked.length != 1) {
                toastr.warning("请选择要修改的非法关键字！一次只能修改一个非法关键字！");
                return ;
            }
            util.htmlModal($scope, "修改非法关键字", "modal/addKeywordForm.tpl.html",
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.diagCls = 'modal-lg';
                    mScope.keyword = checked[0];
                    mScope.keyword.mode = "edit";
                    mScope.diagCls = "modal-md";
                    mScope.okBtn = {
                        text: "保存",
                        click: function() {
                            keywordConf.updateUrl = $scope.urlPerMap.INFO_KEYWORD_UPDATE;
                            var update = {
                                keywordName:mScope.keyword.keywordName,
                                id:mScope.keyword.id
                            };
                            repo.updateByPath(keywordConf, "", update, "修改").then(function(json) {
                                if (json.status == 0) {
                                    $scope.searchKeywords();
                                    util.hideModal(modal);
                                }
                            });
                        }
                    };
                });

        }
        $scope.exportKeywords = function () {
            var queryParams = {};
            queryParams._lk_keywordName = $scope.params._lk_keywordName;

            var params = {
                count: 1,
                page: 1,
                params: queryParams
            };
            repo.queryByUrl($scope.urlPerMap.INFO_KEYWORD_LIST,keywordConf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                queryParams.exportRecordSize = data.total;
                modal.exportModal($scope, "导出非法关键字", keywordConf, queryParams);
            });
        }
    };

    keywordIndexCtrl.$inject = injectParams;
    app.register.controller('keywordIndexCtrl', keywordIndexCtrl);
});
