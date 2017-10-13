define(["app"], function(app) {
    var injectParams = ['$scope', '$routeParams', '$location', 'NgTableParams', 'repoService', 'utilService','paramService'];
    var userSelectorIndexCtrl = function($scope, $routeParams, $location, NgTableParams, repo, util, ps) {
        var conf = {
            url: "/SystemMgr/ChargingAccountMgr",
            showName: '计费账户'
        };

		$routeParams.id = ps.decode($routeParams.id);

        $scope.userParams = {
            capitalAccountId: $routeParams.id,
            parentId: 0,
            showAllChild: true,
            includedType: 1
        };

        $scope.doSubmitStr = '取消包含';

        $scope.included = function() {
            if($scope.userParams.includedType != 1) {
                $scope.includedClass = 'btn btn-info';
                $scope.unIncludedClass = 'btn btn-default';
                $scope.userParams.includedType = 1;
                $scope.doSubmitStr = '取消包含';
                if(angular.isUndefined($scope.selectedNode)) {
                    $scope.userParams.parentId = $scope.dataForTheTree[0].parentId;
                } else {
                    $scope.userParams.parentId = $scope.selectedNode.parentId;
                }
                $scope.refreshUser();
            }
        };

        $scope.unIncluded = function() {
            if($scope.userParams.includedType != 0) {
                $scope.includedClass = 'btn btn-default';
                $scope.unIncludedClass = 'btn btn-info';
                $scope.userParams.includedType = 0;
                $scope.doSubmitStr = '包含所选';
                if(angular.isUndefined($scope.selectedNode)) {
                    $scope.userParams.parentId = $scope.dataForTheTree[0].parentId;
                } else {
                    $scope.userParams.parentId = $scope.selectedNode.parentId;
                }
                $scope.refreshUser();
            }
        };

        $scope.showSelected = function(node) {
            $scope.selectedNode = node;
            $scope.userParams.path = node.fullPath;
            $scope.userParams.parentId = node.id;
            $scope.refreshUser();
        };

        //树结构
        $scope.treeOptions = {
            //子节点字段名称
            nodeChildren: "children",
            dirSelectable: true
        };

        //加载左侧树形结构
        repo.post("/common/getDeptTree").then(function(data) {
            $scope.dataForTheTree = util.transDataToTree(data.data, 'id', 'parentId', 'children');
            $scope.expandedNodes = [$scope.dataForTheTree[0]];
            for (var i = $scope.dataForTheTree[0].children.length - 1; i >= 0; i--) {
                $scope.expandedNodes.push($scope.dataForTheTree[0].children[i]);
            }
        });

        repo.post("/common/getAllRoles").then(function(data) {
            $scope.roleSelectModel = data.data;
        });

        $scope.refreshUser = function() {
            $scope.usersTable = new NgTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function(params) {
                    return repo.queryByUrl($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR_USERS, conf,
                        util.buildQueryParam(params, $scope.userParams)).then(function(data) {
                        params.total(data.total);
                        return data.data;
                    });
                }
            });
        };
        $scope.refreshUser();

        $scope.selectAll = { checked: false };
        $scope.$watch('selectAll.checked', function(value) {
            if(!angular.isUndefined($scope.usersTable)) {
                util.selectAll($scope.usersTable, value);
            }
        });
        $scope.checkOne = function(user) {
            util.checkOne($scope.selectAll, $scope.usersTable, user);
            $scope.checkedUser = $scope.selectAll.lastSelected;
        };

        $scope.search = function() {
            $scope.refreshUser();
        };

        $scope.doSelector = function() {
            var ids = util.selectedItems($scope.usersTable.data, "id");
            if(ids.length == 0) {
                toastr.error("请至少勾选一条数据！");
                return;
            }
            var params = {
                'capitalAccountId': $routeParams.id,
                'includedType': $scope.userParams.includedType,
                'userIds': ids.toString()
            };
            repo.post($scope.urlPerMap.SYSTEMMGR_CHARGINGACCOUNTMGR_USERSELECTOR, params).then(function(data) {
                if (data.status == 0) {
                    toastr.success($scope.doSubmitStr + "成功！");
                    $scope.usersTable.reload();
                } else {
                    toastr.error($scope.doSubmitStr + "失败！");
                }
            });
        };

    };
    userSelectorIndexCtrl.$inject = injectParams;
    app.register.controller('userSelectorIndexCtrl', userSelectorIndexCtrl);
});
