define(["app"],function(app){
	var injectParams = ['$scope', '$location','$cookieStore','paramService', 'NgTableParams', 'repoService', 'utilService', 'modalService'];
	var billAccountStatIndexCtrl = function($scope, $location,$cookieStore,paramService, NgTableParams, repo, util, modal) {
		var billAccountStatConf = {
	            url: 'Statistics/BillingAccountStatistics', //基本URL，必填
	            showName: '计费账户统计', //提示的名称，一般为模块名，必填
	            exportUrl:$scope.urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_EXPORTBILLINGACCOUNTSTATISTICS,
				disPlayTaskNameInput: true
	        };
		$scope.initCtrl = function () {
			$scope.stParams = {

				smsType:'0',
				operType:1,
			};
			var firstDate = new Date();
			firstDate.setDate(1);
			$scope.stParams.beginDate = firstDate.Format("yyyy-MM-dd");
			$scope.stParams.endDate = new Date().Format("yyyy-MM-dd");
			$(function() {		      
				$scope.datePicker4StatBill.startDate = firstDate.Format("yyyy-MM-dd");
				$('input[name="beginDate"]').daterangepicker($scope.datePicker4StatBill);
				$scope.datePicker4StatBillEnd.startDate = moment().format('YYYY-MM-DD');	    
				$('input[name="endDate"]').daterangepicker($scope.datePicker4StatBillEnd);
			});		
			//自动补全用户账号
			$scope.acompleteBillNameUrl = xpath.service("common/complete/fetchAccountData?accountName=");

			$scope.watchBillName = function() {};

        }
        
        $scope.billAccountStatTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
				
                return repo.queryByUrl($scope.urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_GETBILLINGACCOUNTSTATISTICS,billAccountStatConf, util.buildQueryParam(params, $scope.stParams)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });
        //列表头选项
        $scope.cols = [
            { title: "计费账户", show: true },
            { title: "短信消费(元)", show: true },
            { title: "彩信消费(元)", show: true },
            { title: "总消费(元)", show: true },
            { title: "操作", show: true }
        ];
        $scope.searchBillAccountStat = function(){
			 $scope.stParams.accountName = angular.element("#completeBillName_value").val();
        	 if($scope.stParams.beginDate > $scope.stParams.endDate){
        		 toastr.warning("开始日期不能大于结束日期");
        		 return;
        	 }
        	 $scope.billAccountStatTable.reload();
        };

        $scope.exportBillStat = function(){
			$scope.stParams.sendReport = 'true'; 
			$scope.stParams.accountName = angular.element("#completeBillName_value").val();
			var params = {
                count: 1,
                page: 1,
                params: $scope.stParams
            };
            repo.queryByUrl($scope.urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_GETBILLINGACCOUNTSTATISTICS, billAccountStatConf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
				$scope.stParams.exportRecordSize = data.total;
                modal.exportModal($scope, "计费账户导出", billAccountStatConf, $scope.stParams);
            });        
		
        };
 
	    $scope.showSelectedUserData = function(billCountStat){
			//$cookieStore.put('stParams',{beginDate:$scope.stParams.beginDate,endDate:$scope.stParams.endDate,accountName:billCountStat.accountName,accuntId:billCountStat.captialAccuntId});
			var queryParams = {};
			queryParams.beginDate=$scope.stParams.beginDate;
			queryParams.endDate=$scope.stParams.endDate;
			queryParams.accountName=billCountStat.accountName;
			queryParams.accuntId=billCountStat.captialAccuntId;
			var queryParam = paramService.encode(queryParams);
        	 $location.path($scope.urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_GETDETAILSTAT + "/" + queryParam);
        }
		//region 执行这个控制器的一些方法
        this.init =$scope.initCtrl();
	};
	billAccountStatIndexCtrl.$inject = injectParams;
    app.register.controller('billAccountStatIndexCtrl', billAccountStatIndexCtrl);
});
