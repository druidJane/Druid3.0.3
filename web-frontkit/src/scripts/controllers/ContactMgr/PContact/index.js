define(["app"], function(app) {
    var injectParams = ['$scope', '$modal', 'repoService', 'NgTableParams', 'utilService', '$q', 'modalService'];
    var pcontactIndexCtrl = function($scope, $modal, repo, NgTableParams, util, $q,modal) {
        var contactIndexConf = {
            url: 'ContactMgr/PContact/Index',
            updateUrl:$scope.urlPerMap.CONTACTMGR_PCONTACT_UPDATECONTACTGROUP,
            uploadUrl: $scope.urlPerMap.CONTACTMGR_PCONTACT_IMPORTCONTACTS+'?encode=0', //文件上传url
            importUrl:$scope.urlPerMap.CONTACTMGR_PCONTACT_IMPORTCONTACTS+'?action=doImport',
            //基本URL，必填
            showName: '联系人', //提示的名称，一般为模块名，必填
            exportUrl:$scope.urlPerMap.CONTACTMGR_PCONTACT_EXPORTCONTACTS
        };
        var groupIndexConf = {
            url: 'ContactMgr/PContact/index',
            updateUrl:$scope.urlPerMap.CONTACTMGR_PCONTACT_UPDATECONTACTGROUP,
            //基本URL，必填
            showName: '群组' //提示的名称，一般为模块名，必填
        };
        var reg = RegExp("^[a-zA-Z 0-9]{4,12}$");
        //列表头选项
        $scope.cols = [{
            title: "姓名",
            show: true
        }, {
            title: "手机号码",
            show: true
        }, {
            title: "所属组",
            show: true
        }, {
            title: "性别",
            show: true
        }, {
            title: "出生日期",
            show: true
        }, {
            title: "编号",
            show: true
        }, {
            title: "VIP",
            show: true
        }, {
            title: "备注",
            show: true
        }];
        $scope.params = {
            showChild: true,
            vip : -1,
            sex : -1,
            type : 0,
            groupId : 1,
            startTime : ''
        };
        $scope.showSelected = function(node) {
            $scope.params.type = node.type;
            $scope.params.groupId = node.id;
            $scope.params.path = node.path;
            $scope.selectedNode = node;
            $scope.params.contactName = $('#contactName input:first-child').val();
            $scope.params.linkMan = $('#linkMan input:first-child').val();
            $scope.refreshContact();
        };
        //加载右侧个人通讯录列表
        $scope.refreshContact = function() {
            $scope.contactTable = new NgTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function(params) {
                    var query = angular.copy($scope.params);
                    if(angular.isDefined(query._gt_beginDate) && query._gt_beginDate !== '')query._gt_beginDate += " 00:00:00";
                    if(angular.isDefined(query._lt_endDate) && query._lt_endDate !== '')query._lt_endDate += " 23:59:59";
                    return repo.queryByPath(contactIndexConf, "/getContactByGroup", util.buildQueryParam(params, query)).then(function(data) {
                        params.total(data.total);
                        return data.data;
                    });
                }
            });
        };
        

        $scope.expandFlag = 0;
        //折叠群组
        $scope.compress = function() {
            $scope.expandedNodes = [$scope.dataForTheTree[0]];
            $scope.expandFlag = 0;
        };
        //展开所有群组
        $scope.expand = function() {
            $scope.expandedNodes = [];
            expandAll($scope.expandedNodes, $scope.dataForTheTree[0]);
            $scope.expandFlag = 1;
        };
        // 迭代展开群组
        var expandAll = function(expandedNodes, node) {
            expandedNodes.push(node);
            if (node.children != null) {
                for (var i = node.children.length - 1; i >= 0; i--) {
                    expandAll(expandedNodes, node.children[i]);
                }
            }
        };
        $(function() {
			$scope.datePicker.isAllowEmpty = true;
            $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
            $('input[name="_gt_beginDate"]').daterangepicker($scope.datePicker);
            $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
            $('input[name="_lt_endDate"]').daterangepicker($scope.datePicker);
            $scope.params._lt_endDate='';
            $scope.params._gt_beginDate='';
        });
        //树结构
        $scope.treeOptions = {
            //子节点字段名称
            nodeChildren: "children",
            dirSelectable: true
        };
        //加载左侧树形结构
        $scope.refreshGroup = function() {
            repo.post(contactIndexConf.url+"/getPTree").then(function(data) {
                $scope.dataForTheTree = util.transDataToTree(data.data, 'id', 'parentId', 'children');
                $scope.expandedNodes = [$scope.dataForTheTree[0]];
                for (var i = $scope.dataForTheTree[0].children.length - 1; i >= 0; i--) {
                    $scope.expandedNodes.push($scope.dataForTheTree[0].children[i]);
                }
				$scope.refreshContact();
            });
        };

        $scope.refreshGroup();
        //新增群组
        $scope.addGroup = function() {
            var parent = $scope.selectedNode;
            if (parent == undefined) {
                toastr.warning("请选择父群组！");
                return;
            }
            util.htmlModal($scope, "新增群组", "modal/addGroupForm.tpl.html",
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.group = {};
                    mScope.group.parentId = parent.id;
                    //新增群组的path为父群组的path+父群组的id
                    mScope.group.path = parent.path;
                    mScope.diagCls = "modal-md";
                    mScope.okBtn = {
                        text: "保存",
                        click: function() {
                            groupIndexConf.updateUrl = $scope.urlPerMap.CONTACTMGR_PCONTACT_ADDCONTACTGROUP;
                            repo.updateByPath(groupIndexConf, "", mScope.group, "新增").then(function(json) {
                                if (json.status == 0) {
                                    $scope.refreshGroup();
                                    util.hideModal(modal);
                                }
                            });
                        }
                    };
                });
        };
        //删除群组
        $scope.deleteGroup = function() {
            var parent = $scope.selectedNode;
            if (parent == undefined) {
                toastr.warning("请选择要删除的群组！");
                return;
            }
            if (parent.parentId == 0) {
                toastr.warning("根节点不允许删除！");
                return;
            }
            util.confirm("将删除群组" + parent.name + "及该群组下的子分组及所有联系人,确定删除吗？",
                function() {
                    groupIndexConf.updateUrl = $scope.urlPerMap.CONTACTMGR_PCONTACT_DELETECONTACTGROUP;
                    repo.updateByPath(groupIndexConf, "", {"id":parent.id}, "删除").then(function(data) {
                        $scope.refreshGroup();
                        $scope.selectedNode = null;
                    });
                });
        };
        //编辑群组
        $scope.editGroup = function() {
            var checked = $scope.selectedNode;
            if (checked == undefined) {
                toastr.warning("请选择要修改的群组！");
                return;
            }
            if (checked.parentId == 0) {
                toastr.warning("根节点不允许修改！");
                return;
            }
            util.htmlModal($scope, "修改群组", "modal/addGroupForm.tpl.html",
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.group = {};
                    mScope.group.name = checked.name;
                    mScope.group.id = checked.id;
                    mScope.diagCls = "modal-md";
                    mScope.okBtn = {
                        text: "保存",
                        click: function() {
                            groupIndexConf.updateUrl = $scope.urlPerMap.CONTACTMGR_PCONTACT_UPDATECONTACTGROUP;
                            repo.updateByPath(groupIndexConf, "", mScope.group).then(function(json) {
                                if (json.status == 0) {
                                    $scope.refreshGroup();
                                    $scope.refreshContact();
                                    $scope.selectedNode = undefined;
                                    util.hideModal(modal);
                                }
                            });
                        }
                    };
                });
        };
        //新增个人通讯录
        function addContactAsync(parent) {
            var defer = $q.defer();
            util.htmlModal($scope, "新增联系人", "modal/addContactForm.tpl.html",
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.addContact = {};
                    mScope.addContact.groupId = parent.id;
                    mScope.addContact.birthday = "";
                    mScope.addContact.vip = false;
                    mScope.addContact.sex = 1;
                    mScope.userNameValidateFlag = true;
                    mScope.addContact.group = {name : parent.name};
                    mScope.diagCls = "modal-md";
                    defer.resolve(mScope);
                    contactIndexConf.updateUrl = $scope.urlPerMap.CONTACTMGR_PCONTACT_ADDCONTACT;
                    mScope.okBtn = {
                        text: "保存",
                        click: function() {
                            repo.updateByPath(contactIndexConf, "", mScope.addContact,"新增").then(function(json) {
                                if (json.status == 0) {
                                    $scope.refreshContact();
                                    util.hideModal(modal);
                                }
                            });
                        }
                    };
                });
            return defer.promise;
        };

        //新增个人通讯录  因为要在完成摸态框渲染后初始化时间框，需异步处理
        $scope.addContact = function() {
            var parent = $scope.selectedNode;
            if (!angular.isDefined(parent)){
                toastr.warning("请先选择通讯录群组！");
                return;
            }
            if (parent.parentId == 0) {
                toastr.warning("根结点下不能新增联系人！请先添加通讯录群组！");
                return;
            }
            addContactAsync(parent).then(function(mScope) {

                $('#birthday').daterangepicker({
                    isAllowEmpty : true,
                    maxDate:new Date(),
                    showDropdowns: true,
                    singleDatePicker: true,
                    locale: {
                        format: 'YYYY-MM-DD',
                        applyLabel: '确定',
                        cancelLabel: '取消',
                        fromLabel: '起始时间',
                        toLabel: '结束时间',
                        customRangeLabel: '自定义',
                        daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                        firstDay: 1
                    }
                });
                mScope.addContact.birthday = "";
            });
        };

        //删除个人通讯录
        $scope.deleteContact = function() {
            var selected = util.selectedItems($scope.contactTable.data, "id");
            if (selected.length == 0) {
                toastr.warning("请选择要删除的联系人！");
                return;
            }
            util.confirm("确定删除联系人吗？",
                function() {
                    contactIndexConf.updateUrl = $scope.urlPerMap.CONTACTMGR_PCONTACT_DELETECONTACT;
                    repo.updateByPath(contactIndexConf, "", selected, "删除").then(function(data) {
                        $scope.refreshContact();
                    });
                });
        };

        $scope.checkOne = function(item) {
            $scope.checkedContact = item;
            util.checkOne($scope.selectAll, $scope.contactTable, item);
        };
        $scope.selectAll = {
            checked: false
        };
        $scope.$watch('selectAll.checked', function(value) {
            if(angular.isDefined($scope.contactTable)){
                util.selectAll($scope.contactTable, value);
            }
        });

        //编辑个人通讯录
        function editcontactAsync() {
            var defer = $q.defer();
            var parent = $scope.selectedNode;
            var checked = util.selectedItems($scope.contactTable.data);
            if (checked.length != 1) {
                toastr.warning("请选择要修改的联系人！一次只能修改一个联系人！");
                defer.reject();
                return defer.promise;
            }
            util.htmlModal($scope, "修改联系人", "modal/addContactForm.tpl.html",
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.addContact = angular.copy(checked[0]);
                    mScope.diagCls = "modal-md";
                    defer.resolve(mScope.addContact.birthday);
                    mScope.okBtn = {
                        text: "保存",
                        click: function() {
                            contactIndexConf.updateUrl = $scope.urlPerMap.CONTACTMGR_PCONTACT_UPDATECONTACT;
                            mScope.addContact.birthday = $('#birthday').val();
                            repo.updateByPath(contactIndexConf, "", mScope.addContact, "修改").then(function(json) {
                                if (json.status == 0) {
                                    $scope.refreshContact();
                                    util.hideModal(modal);
                                }
                            });
                        }
                    };
                });
            return defer.promise;
        };

        $scope.editContact = function() {
            editcontactAsync().then(function(data) {
                    $('#birthday').daterangepicker({
                        isAllowEmpty : true,
                        maxDate:new Date(),
                        showDropdowns: true,
                        singleDatePicker: true,
                        startDate: new Date(data),
                        locale: {
                            format: 'YYYY-MM-DD',
                            applyLabel: '确定',
                            cancelLabel: '取消',
                            startDate: data,
                            fromLabel: '起始时间',
                            toLabel: '结束时间',
                            customRangeLabel: '自定义',
                            daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                            firstDay: 1
                        }
                    });
                    if(util.isEmpty(data)){
                        $('#birthday').val('');
                    }
                },
                function() {});
        };



        //检索按钮
        $scope.search = function() {
            $scope.params.contactName = $('#contactName input:first-child').val();
            $scope.params.linkMan = $('#linkMan input:first-child').val();
            if ($scope.params.beginTime > $scope.params.endTime) {
                toastr.error("开始时间不能小于结束时间");
                return;
            }
            $scope.refreshContact();
        };
        $scope.exportContact = function() {
            var parent = $scope.selectedNode;
            if (!angular.isDefined(parent)){
                toastr.warning("请先选择通讯录群组！");
                return;
            }
            var queryParams = {};
            queryParams._lk_name = $scope.params._lk_name;
            queryParams.groupId = $scope.selectedNode.id;
            queryParams._lk_phone = $scope.params._lk_phone;
            queryParams.sex = $scope.params.sex;
            queryParams.showChild = $scope.params.showChild;
            queryParams._gt_beginDate = $scope.params._gt_beginDate;
            queryParams._lt_endDate = $scope.params._lt_endDate;
            queryParams.vip = $scope.params.vip;
            queryParams.path = $scope.selectedNode.path;
            queryParams.type = $scope.params.type;
			queryParams._lk_identifier = $scope.params._lk_identifier
            var params = {
                count: 1,
                page: 1,
                params: queryParams
            };
            repo.queryByPath(contactIndexConf, "/getContactByGroup", params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                queryParams.exportRecordSize = data.total;
                modal.exportModal($scope, "导出通讯录", contactIndexConf, queryParams);
            });
        };

        //导入个人通讯录
        $scope.importContact = function() {
            var parent = $scope.selectedNode;
            if (!angular.isDefined(parent)){
                toastr.warning("请先选择通讯录群组！");
                return;
            }
            var parent = $scope.selectedNode;
            if (parent.parentId == 0) {
                toastr.warning("根结点下不能导入联系人！请先添加通讯录群组！");
                return;
            }
            contactIndexConf.importParams = {"params":{"groupId":parent.id}};
            //导入文件——数据头映射，从后台获取
            repo.getByUrl(contactIndexConf.uploadUrl+'?encode=0').then(function(data) {
                $scope.headDMapToFList = data.data;
                modal.importModal($scope, "导入通讯录", contactIndexConf);
            });
        };
    };
    pcontactIndexCtrl.$inject = injectParams;
    app.register.controller('pcontactIndexCtrl', pcontactIndexCtrl);
});
