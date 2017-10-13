define(["app"], function(app) {
    var injectParams = ['$scope', '$location', '$modal', 'repoService', 'NgTableParams', 'utilService', '$q', 'modalService'];
    var econtactIndexCtrl = function($scope,$location, $modal, repo, NgTableParams, util, $q,modal) {
        var contactIndexConf = {
            url: 'ContactMgr/EContact/Index',
            //基本URL，必填
            showName: '共享通讯录' //提示的名称，一般为模块名，必填
        };
        var groupIndexConf = {
            url: 'ContactMgr/EContact/Index',
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
        //加载右侧共享通讯录列表
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
                    return repo.queryByPath(contactIndexConf, "/getContactByEGroup", util.buildQueryParam(params, query)).then(function(data) {
                        params.total(data.total);
                        return data.data;
                    });
                }
            });
        };
       

        $scope.expandFlag = 0;
        //折叠群组
        $scope.compress = function() {
            $scope.expandedNodes = [$scope.dataForTheTree];
            $scope.expandFlag = 0;
        };
        //展开所有群组
        $scope.expand = function() {
            $scope.expandedNodes = [];
            expandAll($scope.expandedNodes, $scope.dataForTheTree);
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
            $('input[name="beginDate"]').daterangepicker($scope.datePicker);
            $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
            $('input[name="endDate"]').daterangepicker($scope.datePicker);
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
            repo.queryByPath(contactIndexConf, "/getETree").then(function(data) {

                var rootChildren = data.data.children;
                for (var i = rootChildren.length - 1; i >= 0; i--) {
                    var item = data.data.children[i].children;
                    if(item != null){
                        var convertNode = util.transDataToTree(item, 'id', 'parentId', 'children');
                        data.data.children[i].children = convertNode;
                    }
                }
                $scope.dataForTheTree = [data.data];
                //$scope.dataForTheTree = util.transDataToTree(data.data, 'id', 'parentId', 'children');
                $scope.expandedNodes = [data.data];//[$scope.dataForTheTree[0]];
                for (var i = $scope.dataForTheTree[0].children.length - 1; i >= 0; i--) {
                    var childArray = util.transDataToTree($scope.dataForTheTree[0].children[i].children, 'id', 'parentId', 'children');
                    $scope.expandedNodes.push(childArray);
                }
				 $scope.refreshContact();
            });
        };

        $scope.refreshGroup();
        //新增群组
        $scope.addGroup = function() {
            $location.path($scope.urlPerMap.CONTACTMGR_ECONTACT_ADD);
        };
        //删除群组
        $scope.deleteGroup = function() {
            var share = $scope.selectedNode;
            if (share == undefined) {
                toastr.warning("请选择要删除的群组！");
                return;
            }
            if (share.parentId == 0) {
                toastr.warning("根节点不允许删除！");
                return;
            }
            util.confirm("确定删除群组" + share.name + " 吗？",
                function() {
                    groupIndexConf.updateUrl = $scope.urlPerMap.CONTACTMGR_ECONTACT_DELETECONTACTGROUP;
                    repo.updateByPath(groupIndexConf, "", {"groupId":share.id}, "删除").then(function(data) {
                        if (data.status == 0) {
                            $scope.refreshGroup();
                            $scope.selectedNode = null;
                        }else{

                        }

                    });
                });
        };
        //新增共享通讯录
        function addContactAsync(parent) {
            var defer = $q.defer();
            util.htmlModal($scope, "新增共享通讯录", "modal/addContactForm.tpl.html",
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.disab = false;
                    mScope.contact = {};
                    mScope.contact.groupId = parent.id;

                    mScope.userNameValidateFlag = true;
                    mScope.diagCls = "modal-md";
                    defer.resolve(mScope);
                    contactIndexConf.updateUrl = $scope.urlPerMap.CONTACTMGR_PCONTACT_ADDCONTACT;
                    mScope.okBtn = {
                        text: "新增",
                        click: function() {
                            repo.updateByPath(contactIndexConf, "", mScope.contact).then(function(json) {
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


        $scope.checkOne = function(item) {
            $scope.checkedContact = item;
            util.checkOne($scope.selectAll, $scope.contactTable, item);
        };
        $scope.selectAll = {
            checked: false
        };
        $scope.$watch('selectAll.checked', function(value) {
            util.selectAll($scope.contactTable, value);
        });
        //检索按钮
        $scope.search = function() {
            $scope.params.contactName = $('#contactName input:first-child').val();
            $scope.params.linkMan = $('#linkMan input:first-child').val();
            $scope.refreshContact();
        };
    };
    econtactIndexCtrl.$inject = injectParams;
    app.register.controller('econtactIndexCtrl', econtactIndexCtrl);
});
