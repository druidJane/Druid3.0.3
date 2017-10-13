define(["app"], function(app) {
    var injectParams = ['$scope', '$location', '$modal', 'repoService', 'NgTableParams', 'utilService', '$q', 'modalService'];
    var econtactAddCtrl = function($scope,$location, $modal, repo, NgTableParams, util, $q,modal) {
        var contactIndexConf = {
            locationUrl: $scope.urlPerMap.CONTACTMGR_ECONTACT_INDEX,
            //基本URL，必填
            showName: '共享通讯录' //提示的名称，一般为模块名，必填
        };
        var groupIndexConf = {
            url: 'ContactMgr/EContact/index',
            //基本URL，必填
            showName: '群组' //提示的名称，一般为模块名，必填
        };
        var reg = RegExp("^[a-zA-Z 0-9]{4,12}$");
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
        };
        $scope.econtact = {
            shareChildFlag:'true',
            showContactFlag:'true'
        }
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
        //树结构
        $scope.treeOptions = {
            //子节点字段名称
            nodeChildren: "children",
            dirSelectable: true
        };
        //加载左侧树形结构
        $scope.refreshGroup = function() {
            repo.post("/ContactMgr/PContact/Index/getPTree").then(function(data) {
                $scope.dataForTheTree = util.transDataToTree(data.data, 'id', 'parentId', 'children');
                $scope.expandedNodes = [$scope.dataForTheTree[0]];
                for (var i = $scope.dataForTheTree[0].children.length - 1; i >= 0; i--) {
                    $scope.expandedNodes.push($scope.dataForTheTree[0].children[i]);
                }
            });
        };

        $scope.refreshGroup();

        $scope.submitForm = function () {
            var parent = $scope.selectedNode;
            if (parent == undefined) {
                toastr.warning("请选择父群组！");
                return;
            }
            if(parent.id == 1){
                toastr.warning("根节点不能共享！");
                return;
            }
            $scope.econtact.groupId = parent.id;
            repo.post($scope.urlPerMap.CONTACTMGR_ECONTACT_ADDCONTACTGROUP,$scope.econtact).then(function (resp) {
                if (resp.status == 0) {
                    toastr.success("新增" + contactIndexConf.showName + "成功。");
                    $location.path(contactIndexConf.locationUrl);
                } else {
                    var msg = "新增失败: " + resp.errorMsg;
                    toastr.error(msg);
                }
            }).catch(function(resp) {
                toastr.error("新增" + contactIndexConf.showName + "失败: " + resp.errorMsg);
            });
        }
    };
    econtactAddCtrl.$inject = injectParams;
    app.register.controller('econtactAddCtrl', econtactAddCtrl);
});
