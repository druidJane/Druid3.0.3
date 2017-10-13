define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'NgTableParams', 'repoService', 'utilService', 'modalService'];
    var announcementIndexCtrl = function($scope, $location, NgTableParams, repo, util, modal) {
        var announcementConf = {
            url: '/SystemMgr/AnnouncementMgr/Index', //基本URL，必填
            showName: '公告管理', //提示的名称，一般为模块名，必填
            showAddUrl: 'sysmgr/announcement/add.html',
            showUpdateUrl: 'sysmgr/announcement/update.html',
            showDetailUrl: 'sysmgr/announcement/detail.html',
            delUrl: $scope.urlPerMap.SYSTEMMGR_ANNOUNCEMENTMGR_DELANNOUNCEMENT
        };

        $scope.announcementParams = {'showState': -1};
        var firstDate = new Date();
        firstDate.setDate(1);
        $scope.announcementParams.startTime = firstDate.Format("yyyy-MM-dd");
        $scope.announcementParams.endTime = new Date().Format("yyyy-MM-dd");
        $(function() {
            $scope.datePicker.startDate = moment(firstDate).format('YYYY-MM-DD');
            $('input[name="startTime"]').daterangepicker($scope.datePicker);
            $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
            $('input[name="endTime"]').daterangepicker($scope.datePicker);
        });

        $scope.announcementTable = new NgTableParams({
            page: 1,
            count: 10,
            sorting: {
                postTime: 'desc'
            }
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.SYSTEMMGR_ANNOUNCEMENTMGR_GETALLANNOUNCEMENT,
                    announcementConf, util.buildQueryParam(params, $scope.announcementParams)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });

        $scope.selectAll = { checked: false };
        $scope.$watch('selectAll.checked', function(value) {
            util.selectAll($scope.announcementTable, value);
            if($scope.announcementTable.data.length == 1) {
                $scope.checkedAnnouncement = angular.copy($scope.announcementTable.data[0]);
            }
        });
        $scope.checkOne = function(announcement) {
            util.checkOne($scope.selectAll, $scope.announcementTable, announcement);
            $scope.checkedAnnouncement = $scope.selectAll.lastSelected;;
        };

        $scope.searchAnnouncement = function() {
            if ($scope.announcementParams.startTime > $scope.announcementParams.endTime) {
                toastr.error("开始时间不能大于结束时间！");
                return;
            }
            $scope.announcementTable.reload();
        };

        $scope.addAnnouncement = function(){
            util.commonModal($scope, "新增公告", announcementConf.showAddUrl,
                function(modal) {
                    var mScope = modal.$scope;
                    mScope.announcement = {showState:1};
                    mScope.doAdd = function(){
                        repo.post($scope.urlPerMap.SYSTEMMGR_ANNOUNCEMENTMGR_ADDANNOUNCEMENT, mScope.announcement).then(
                            function(data){
                                if (data.status == 0) {
                                    toastr.success("新增公告成功！");
                                    $scope.announcementTable.reload();
                                    util.hideModal(modal);
                                } else {
                                    toastr.error("新增公告失败：" + data.errorMsg);
                                }
                            },true
                        );
                    };

                    mScope.okBtn = {
                        text : '保存',
                        click : function() {
                            mScope.doAdd();
                        }
                    };
                }
            );
        };

        $scope.editAnnouncement = function() {
            var checked = util.selectedItems($scope.announcementTable.data, "id");
            if (checked.length != 1) {
                toastr.warning("请选择要修改的公告！一次只能修改一个公告！");
                return;
            }
            util.commonModal($scope, "修改公告信息", announcementConf.showUpdateUrl, function(modal) {
                var mScope = modal.$scope;
                mScope.announcement = angular.copy($scope.checkedAnnouncement);
                mScope.operate = 'update';
                mScope.doUpdate = function(){
                    repo.post($scope.urlPerMap.SYSTEMMGR_ANNOUNCEMENTMGR_MODIFYANNOUNCEMENT, mScope.announcement).then(
                        function(data){
                            if (data.status == 0) {
                                toastr.success("修改公告信息成功！");
                                $scope.announcementTable.reload();
                                util.hideModal(modal);
                            } else {
                                toastr.error("修改公告信息失败：" + data.errorMsg);
                            }
                        },true
                    );
                };

                mScope.okBtn = {
                    text : '保存',
                    click : function() {
                        mScope.doUpdate();
                    }
                };
            });
        };

        $scope.detail = function(announcement) {
            util.commonModal($scope, '查看公告信息', announcementConf.showDetailUrl, function(modal) {
                var mScope = modal.$scope;
                mScope.okBtn={hide:true};
                mScope.closeBtn={hide:true};
                mScope.announcement = angular.copy(announcement);
            });
        };

        $scope.delAccouncement = function() {
            announcementConf.url = $scope.urlPerMap.SYSTEMMGR_ANNOUNCEMENTMGR_DELANNOUNCEMENT;
            repo.remove(announcementConf, $scope.announcementTable.data, "id").then(function(data) {
                $scope.announcementTable.reload();
            });
        };

    };

    announcementIndexCtrl.$inject = injectParams;
    app.register.controller('announcementIndexCtrl', announcementIndexCtrl);
});
