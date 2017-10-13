define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'NgTableParams', 'repoService', 'utilService', 'modalService','paramService','MmsService'];
    var mmsTemplateIndexCtrl = function($scope, $location, NgTableParams, repo, util, modal, ps,mmsService) {

        var mmsTemplateIndexConf = {
            editUrl:$scope.urlPerMap.INFO_TEMPLATE_MODIFY,
            delUrl:$scope.urlPerMap.INFO_TEMPLATE_DELETE,
            showTplUrl:'mmsmgmt/template/modal/template.html',
            url: 'InformationMgr/Template', //基本URL，必填
            showName: '彩信模板', //提示的名称，一般为模块名，必填
        };

        $scope.mmsTemplateListParams = {
            templateId:"",
            templateName:""
        };

        $scope.mmsTemplateTable = new NgTableParams(
            {
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function (params) {
                    return repo.queryByUrl($scope.urlPerMap.INFO_TEMPLATE_LIST, mmsTemplateIndexConf, util.buildQueryParam(params, $scope.mmsTemplateListParams)).then(function (data) {
                        params.total(data.total);
                        // params.total(data.total);
                        return data.data;
                    });
                }
            });

        $scope.addMmsTemplate = function () {
            $location.path($scope.urlPerMap.INFO_TEMPLATE_CREATE);
        };

        $scope.modifyMmsTemplate = function () {
            var templateId = util.selectedItems( $scope.mmsTemplateTable.data, "id");
            if (templateId.length>1) {
                toastr.error("进行修改时，仅能选择一条记录");
                return;
            }
            if (templateId.length<1) {
                toastr.error("请选择一条记录进行修改");
                return;
            }
            $location.path($scope.urlPerMap.INFO_TEMPLATE_MODIFY+'/'+ps.encode(String(templateId[0])));
        };

        $scope.searchMmsTemplate = function () {
            if ($scope.mmsTemplateListParams.templateName.length>20){
                toastr.error("查询字符不得超过20个字符")
                return;
            }
            $scope.mmsTemplateTable.reload();
        };

        //可以选择多个进行删除
        $scope.deleteMmsTemplate = function() {
            repo.remove(mmsTemplateIndexConf, $scope.mmsTemplateTable.data, "id").then(function(data) {
                $scope.mmsTemplateTable.reload();
            });
        };

        $scope.selectAll = { checked: false };
        $scope.$watch('selectAll.checked', function(value) {
            util.selectAll($scope.mmsTemplateTable, value);
        });

        $scope.findTemplateById = function (index) {
            mmsService.showMmsPack($scope, '/InformationMgr/Template/Edit', '', '', index);
        }

        $scope.checkOne = function(mmsTemplateList) {
            util.checkOne($scope.selectAll, $scope.mmsTemplateTable, mmsTemplateList);
        };
    };
    mmsTemplateIndexCtrl.$inject = injectParams;
    app.register.controller('mmsTemplateIndexCtrl', mmsTemplateIndexCtrl);
});
