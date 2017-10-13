define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'NgTableParams', 'repoService', 'utilService'];
    var roleIndexCtrl = function($scope, $location, NgTableParams, repo, util) {
        var conf = {
            url: '/SystemMgr/RoleMgr', //基本URL，必填
            showName: '角色管理', //提示的名称，一般为模块名，必填
            addView: 'sysmgr/role/add.html',
            doAdd: '/AddRole',
            doUpdate: '/UpdateRole',
            delUrl: '/SystemMgr/RoleMgr/DeleteRole',
            detail: '/Index/Detail'
        };

        $scope.params = {'industryType': 0};

        $scope.rolesTable = new NgTableParams({
            page: 1,
            count: 10,
            sorting: {
                lastModifyDate: 'desc'
            }
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.SYSTEMMGR_ROLEMGR_GETALLROLE, conf,
                    util.buildQueryParam(params, $scope.params)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });

        //检索按钮
        $scope.search = function() {
            $scope.rolesTable.reload();
        };

        //重置查询条件
        $scope.reset = function() {
            $scope.params={};
            $scope.rolesTable.reload();
        };

        $scope.showDetail = function(role){
            repo.queryByPath(conf, conf.detail, role).then(
                function(data){
                    util.htmlModal($scope, "角色详情", "modal/tree.tpl.html",
                        function(modal) {
                            var mScope = modal.$scope;
                            mScope.diagCls = "modal-lg";
                            mScope.okBtn = {hide:true};
                            var root = {name: role.name};
                            $scope.role = data.data;
                            root.children = $scope.toVerticalItems(data.data.permissions);
                            var vgLength = data.data.permissions.length * 6;
                            verticalTree("verticalTree", root,vgLength,vgLength);
                        }
                    );
                }
            );
        }

        $scope.toVerticalItems = function(items){
            if(!items){
                return ;
            }
            var list = [];
            var map = {};
            angular.forEach(items, function(item, key) {
                if(item.parentId == 0){
                    list.push(item);
                }else{
                    if(!map[item.parentId]){
                        map[item.parentId] = [];
                    }
                    map[item.parentId].push(item);

                }
            });
            return $scope.setSubItem(list, map);
        }

        $scope.setSubItem = function(list, map){
            if(!list){
                return ;
            }
            var array = [];
            angular.forEach(list, function(item, key) {
                item.name = item.displayName;
                var subs = map[item.id];
                //item[subKey] = subs;
                var dataScope = item.dataScope.name=='GLOBAL'||item.dataScope.name=='NONE' ? '' : '('+item.dataScope.label+')';
                var tmp = {name: item.name+dataScope, children: $scope.setSubItem(subs, map)};
                array.push(tmp);
            });
            return array;
        }

        $scope.toTreeItems = function(items){
            if(!items){
                return ;
            }
            var tree = [];
            angular.forEach(items, function(item) {
                var treeItem = {name: item.displayName, id: item.id, checked:'no', parentId: item.parentId, dataScope: item.dataScope,
                    dataScopes: item.dataScopes, dependIds: item.dependIds, base: item.base, isDisplay: item.display};
                treeItem.subs = $scope.toTreeItems(item.sub);
                tree.push(treeItem);
            });
            return tree;
        }

        $scope.toMapItems = function (items,map) {
            if(!items || items.length == 0){
                return;
            }
            for(idx in items){
                map[items[idx].id] = items[idx];
                $scope.toMapItems(items[idx].subs,map);
            }
        }

        $scope.showAdd = function(){
            util.commonModal($scope, "新增角色", conf.addView,
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.diagCls = "modal-lg";
                    mScope.perms = [];
                    mScope.permMap = {};
                    mScope.role = {industryType:{index:0}};
                    mScope.selectPerms = {};
                    mScope.operate = 'add';
                    //获取权限列表
                    repo.post($scope.urlPerMap.SYSTEMMGR_ROLEMGR_INDEX_PERMISSION).then(
                        function(data){
                            mScope.selectPerms = {};
                            mScope.permMap = {};
                            if(data.data){
                                mScope.perms = $scope.toTreeItems(data.data);
                                $scope.toMapItems(mScope.perms,mScope.permMap);
                            }
                        }
                    );

                    mScope.doAdd = function(){
                        var permissions = [];
                        angular.forEach(mScope.selectPerms , function(perm, key) {
                            permissions.push({id: key, dataScope: perm.dataScope});
                        });
                        if(permissions.length == 0) {
                            toastr.warning("至少勾选一个功能权限！");
                            return;
                        }
                        mScope.role.permissions = permissions;
                        repo.post(conf.url + conf.doAdd, mScope.role).then(
                            function(data){
                                if (data.status == 0) {
                                    toastr.success("新增角色成功!");
                                    $scope.rolesTable.reload();
                                    util.hideModal(modal);
                                } else {
                                    toastr.error("新增角色失败：" + data.errorMsg);
                                }
                            },true);
                    }

                    mScope.okBtn = {
                        text : '保存',
                        click : function() {
                            mScope.doAdd();
                        }
                    };
                });
        }

        $scope.editRole = function (role) {
            util.commonModal($scope, "修改角色", conf.addView,
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.diagCls = "modal-lg";
                    mScope.perms = [];
                    mScope.permMap = {};
                    mScope.role = {};
                    mScope.selectPerms = {};
                    mScope.operate = 'update';

                    repo.queryByPath(conf, conf.detail, role).then(function (data) {
                        mScope.role = data.data;
                        angular.forEach(mScope.role.permissions,function (item,index) {
                            var treeItem = {name: item.displayName, id: item.id, checked: 'yes', parentId: item.parentId, dataScope: item.dataScope};
                            mScope.selectPerms[treeItem.id] = treeItem;
                        });
                        repo.post($scope.urlPerMap.SYSTEMMGR_ROLEMGR_INDEX_PERMISSION).then(
                            function(data){
                                if(data.data && data.data.length > 0){
                                    mScope.perms = $scope.toTreeItems(data.data);
                                    $scope.toMapItems(mScope.perms,mScope.permMap);
                                }
                            }
                        );
                    });

                    mScope.doSave = function(){
                        // get select permissions
                        var permissions = [];
                        angular.forEach(mScope.selectPerms , function(perm, key) {
                            permissions.push({id: key, dataScope: perm.dataScope});
                        });
                        mScope.role.permissions = permissions;
                        if(permissions.length == 0) {
                            toastr.warning("至少勾选一个功能权限！");
                            return;
                        }
                        repo.post(conf.url+conf.doUpdate, mScope.role).then(
                            function(data){
                                if (data.status == 0) {
                                    toastr.success("修改角色成功!");
                                    $scope.rolesTable.reload();
                                    util.hideModal(modal);
                                } else {
                                    toastr.error("修改角色失败：" + data.errorMsg);
                                }
                            },true);
                    }

                    mScope.okBtn = {
                        text : '保存',
                        click : function() {
                            mScope.doSave();
                        }
                    };
                });
        }

        //删除角色
        $scope.delete = function(role) {
            var infoMsg = "";
            if(role.userBindFlag) {
                infoMsg = "该角色已被分配，将自动删除用户与该角色的关联关系，确认要删除吗？";
            } else {
                infoMsg = "确认要删除该角色吗？";
            }
            var ids = [role.id];
            util.confirm(infoMsg, function () {
                repo.post(conf.delUrl, ids, function (data, status, headers, config) {
                    if (util.isRespOK(data)) {
                        toastr.success("删除成功。");
                        $scope.rolesTable.reload();
                    } else {
                        toastr.error("删除失败: " + data.errorMsg);
                    }
                });
            });
        };
    };
    roleIndexCtrl.$inject = injectParams;
    app.register.controller('roleIndexCtrl', roleIndexCtrl);
});
