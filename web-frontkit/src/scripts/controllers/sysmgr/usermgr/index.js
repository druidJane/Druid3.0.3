define(["app"], function(app) {
    var injectParams = ['$scope', 'modalService', '$location', 'repoService', 'NgTableParams', 'paramService', 'utilService', '$q'];
    var userMgrIndexCtrl = function($scope, modal, $location, repo, NgTableParams, ma, util, $q) {
        var userIndexConf = {
            url: '/SystemMgr/AccountMgr',
            userDetailUrl: '/sysmgr/usermgr/userDetail.html',
            delUrl: $scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_DELETEUSER,
            uploadUrl: $scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_USERIMPORTING+'?encode=0&action=upload',
            importUrl: $scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_USERIMPORTING+'?encode=0&action=doImport',
            exportUrl: $scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_USEREXPORT,
            showName: '用户', //提示的名称，一般为模块名，必填,
            checkExport:false
        };

        var deptIndexConf = {
            url: '/SystemMgr/AccountMgr',
            updateUrl: $scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_DELDEPARTMENT,
            showName: '部门'
        };

        $scope.userParams = {
            showAllChild: true,
            parentId: 0,
            userName: null
        };

        //角色下拉列表选项值
        repo.post("/common/getAllRoles").then(function(data) {
            $scope.roleSelectModel = data.data;
        });

        //业务类型下拉列表选项值
        repo.post("/common/getAllBusinessType").then(function(data) {
            $scope.bizTypeSelectModel = data.data;
        });

        $scope.expandFlag = 1;
        //折叠部门
        $scope.compress = function() {
            $scope.expandedNodes = [$scope.dataForTheTree[0]];
            $scope.expandFlag = 0;
        };
        //展开所有部门
        $scope.expand = function() {
            $scope.expandedNodes = [];
            expandAll($scope.expandedNodes, $scope.dataForTheTree[0]);
            $scope.expandFlag = 1;
        };
        // 迭代展开部门
        var expandAll = function(expandedNodes, node) {
            expandedNodes.push(node);
            if (node.children != null) {
                for (var i = node.children.length - 1; i >= 0; i--) {
                    expandAll(expandedNodes, node.children[i]);
                }
            }
        };

        //树结构
        $scope.treeOptions = {
            //子节点字段名称
            nodeChildren: "children",
            dirSelectable: true
        };

        //加载左侧树形结构
        $scope.refreshDept = function() {
            repo.post($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_GETDEPTS_DEPTTREE).then(function(data) {
                $scope.dataForTheTree = util.transDataToTree(data.data, 'id', 'parentId', 'children');
                $scope.expandedNodes = [$scope.dataForTheTree[0]];
                if(!angular.isUndefined($scope.dataForTheTree[0].children)) {
                    for (var i = $scope.dataForTheTree[0].children.length - 1; i >= 0; i--) {
                        $scope.expandedNodes.push($scope.dataForTheTree[0].children[i]);
                    }
                }
            });
        };
        $scope.refreshDept();

        $scope.searchDept = function() {
            $location.path($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_GETDEPTS);
        };

        $scope.showSelected = function(node) {
            $scope.userParams.path = node.fullPath;
            $scope.userParams.parentId = node.id;
            $scope.selectedNode = node;
            if($scope.hasPermission($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST)) {
                $scope.refreshUser();
            }
        };

        //加载右侧用户列表
        $scope.refreshUser = function() {
            //自动补全用户账号
            $scope.acompleteUserNameUrl = xpath.service("common/complete/fetchUserData?userName=");

            $scope.watchUserName = function() {};

            $scope.userTable = new NgTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function(params) {
                    return repo.queryByUrl($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST, userIndexConf,
                        util.buildQueryParam(params, $scope.userParams)).then(function(data) {
                        params.total(data.total);
                        return data.data;
                    });
                }
            });

            $scope.checkOne = function(item) {
                util.checkOne($scope.selectAll, $scope.userTable, item);
                $scope.checkedUser = $scope.selectAll.lastSelected;
            };
            $scope.selectAll = {
                checked: false
            };
            $scope.$watch('selectAll.checked', function(value) {
                util.selectAll($scope.userTable, value);
                if($scope.userTable.data.length == 1) {
                    $scope.checkedUser = angular.copy($scope.userTable.data[0]);
                }
            });
        };

        if($scope.hasPermission($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST)) {
            $scope.refreshUser();
        }

        //检索按钮
        $scope.searchUser = function() {
            $scope.userParams.userName = angular.element("#completeUserName_value").val();
            $scope.refreshUser();
        };

        //新增部门
        $scope.addDept = function() {
            var parent = $scope.selectedNode;
            if (parent == undefined) {
                toastr.warning("请选择父部门！");
                return;
            }
            $location.path($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_ADDDEPARTMENT + "/" + ma.encode(parent.id));
        };

        //删除部门
        $scope.deleteDept = function() {
            var parent = $scope.selectedNode;
            if (parent == undefined) {
                toastr.warning("请选择要删除的部门！");
                return;
            }
            if (parent.baseDept) {
                toastr.warning("企业根节点不允许删除！");
                return;
            }
            util.confirm("确定删除该部门吗？",
                function() {
                    repo.updateByPath(deptIndexConf, "/del", parent.id, "删除").then(function(data) {
                        $scope.refreshDept();
                        $scope.selectedNode = null;
                    });
                });
        };

        //编辑部门
        $scope.editDept = function() {
            var checked = $scope.selectedNode;
            if (checked == undefined) {
                toastr.warning("请选择要修改的部门！");
                return;
            }
            if (checked.baseDept) {
                toastr.warning("企业根节点不允许修改！");
                return;
            }
            $location.path($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_UPDATEDEPARTMENT + "/" + ma.encode(checked.id));
        };

        //用户详情查看
        $scope.showUserDetail = function(user) {
            util.commonModal($scope, '用户详情查看', userIndexConf.userDetailUrl, function(modal) {
                var mScope = modal.$scope;
                setUser(mScope, angular.copy(user));
                mScope.okBtn={hide:true};
                mScope.closeBtn={hide:true};
            });
        };

        var setUser = function(mScope, user) {
            var reg = new RegExp("^admin@");
            if(reg.test(user.userName)) {
                if(util.isEmpty(user.linkMan)) {
                    //user.linkMan = '超级管理员';
                }
                var bizNames = '';
                for(var i = 0; i < mScope.bizTypeSelectModel.length; i++) {
                    bizNames += mScope.bizTypeSelectModel[i].name + ',';
                }
                user.bizNames = bizNames.substring(0, bizNames.length - 1);

                var roleNames = '';
                for(var i = 0; i < mScope.roleSelectModel.length; i++) {
                    roleNames += mScope.roleSelectModel[i].name + ',';
                }
                user.roleNames = roleNames.substring(0, roleNames.length - 1);
            }
            mScope.user = user;
        };

        $scope.addUser = function() {
            var parent = $scope.selectedNode;
            if (parent == undefined) {
                toastr.warning("请选择一部门进行用户新增！");
                return;
            }
            if (parent.baseDept) {
                toastr.warning("企业根节点不允许创建用户！");
                return;
            }
            $location.path($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_ADDACCOUNT + "/" + ma.encode(parent.id));
        };

        $scope.editUser = function() {
            var checked = util.selectedItems($scope.userTable.data, "id");
            if (checked.length != 1) {
                toastr.warning("请选择要修改的用户！一次只能修改一个用户！");
                return;
            }

            var reg = new RegExp("^admin@");
            if(reg.test($scope.checkedUser.userName)) {
                toastr.warning("admin账号限制修改！");
                return;
            }
            $location.path($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_UPDATEACCOUNT + "/" + ma.encode($scope.checkedUser.id));
        };

        $scope.delUser = function() {
            var checked = util.selectedItems($scope.userTable.data, "id");
            if (checked.length == 0) {
                toastr.warning("请选择要删除的用户！");
                return;
            }
            var reg = new RegExp("^admin@");
            var canDelUser = true;
            angular.forEach($scope.userTable.data, function(item){
                if(item.$checked == true && reg.test(item.userName)) {
                    toastr.warning("“admin@企业域”账号限制删除！");
                    canDelUser = false;
                    return;
                }
            });
            if(canDelUser) {
                repo.remove(userIndexConf, $scope.userTable.data, "id").then(function(data) {
                    $scope.userTable.reload();
                });
            }
        };

        $scope.importUser = function() {
            //导入文件——数据头映射，从后台获取
            repo.getByUrl(userIndexConf.url + "/UserImporting?encode=0").then(function(data) {
                $scope.headDMapToFList = data.data;
                modal.importModal($scope, "导入用户", userIndexConf);
            });
        };

        $scope.exportUser = function() {
            var params = {
                count: 1,
                page: 1,
                params: $scope.userParams
            };
            repo.queryByUrl($scope.urlPerMap.SYSTEMMGR_ACCOUNTMGR_GETUSERLIST, userIndexConf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                $scope.userParams.exportRecordSize = data.total;
                modal.exportModal($scope, "导出用户", userIndexConf, $scope.userParams);
            });
        };

    };
    userMgrIndexCtrl.$inject = injectParams;
    app.register.controller('userMgrIndexCtrl', userMgrIndexCtrl);
});
