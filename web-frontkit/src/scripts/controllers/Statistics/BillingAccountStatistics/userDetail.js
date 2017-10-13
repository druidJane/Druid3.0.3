define(["app"],function(app){
	var injectParams = ['$rootScope','$timeout','paramService','$routeParams','$scope', '$location', 'NgTableParams', 'repoService','graphService', 'utilService','modalService'];
	var userIndexCtrl = function($rootScope,$timeout,paramService,$routeParams,$scope, $location, NgTableParams, repo, graphService,util,modal) {
		var userDetailConf = {
	            url: 'Statistics/BillingAccountStatistics', //基本URL，必填
	            showName: '计费账户详情统计' //提示的名称，一般为模块名，必填
				
	        };
	    $scope.params = {

        };   
		$scope.go_back = function(){
			window.history.back();
		};   
	
		var Params1 = paramService.decode($routeParams.queryParam);
		 $scope.params.deductTime = Params1.deductTime;
		 $scope.params.accountId = Params1.accuntId;
		 $scope.params.accountName = Params1.accountName;
		$scope.accountName = Params1.accountName;
        $scope.userDetailTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_USERDETAIL,userDetailConf, util.buildQueryParam(params, $scope.params)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });		
	
        //列表头选项
        $scope.cols = [
            { title: "用户姓名", show: true },
            { title: "用户账户", show: true },
            { title: "所属部门", show: true },
            { title: "总消费(元)", show: true },
            { title: "短信消费(元)", show: true },
			{ title: "彩信消费(元)", show: true },
        ];
	};
	userIndexCtrl.$inject = injectParams;
    app.register.controller('userIndexCtrl', userIndexCtrl);
	
});