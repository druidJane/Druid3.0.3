define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'NgTableParams', 'repoService', 'utilService', 'modalService'];
    var carrierDnSegIndexCtrl = function($scope, $location, NgTableParams, repo, util, modal) {
        var carrierDnSegConf = {
            url: 'InformationMgr/CarrierDnSeg', //基本URL，必填
            showName: '运营商号码段' //提示的名称，一般为模块名，必填
        };
        $scope.carrierDnSegParams = {
        };
        repo.getByUrl('/common/getAllCarrier',carrierDnSegConf, '').then(function(data) {
            $scope.carrier= data.data;
        });
        $scope.carrierDnSegTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                if(angular.isDefined($scope.carrierDnSegParams.carrier)){
                    if($scope.carrierDnSegParams.carrier.id==null){
                        $scope.carrierDnSegParams.carrier.id='';
                    }
                }
                return repo.queryByUrl($scope.urlPerMap.INFOR_CARRIERDNSEG_LIST,carrierDnSegConf, util.buildQueryParam(params, $scope.carrierDnSegParams)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });
        $scope.searchCarrierDnSeg = function() {
            $scope.carrierDnSegTable.reload();
        };
    };
    carrierDnSegIndexCtrl.$inject = injectParams;
    app.register.controller('carrierDnSegIndexCtrl', carrierDnSegIndexCtrl);
});
