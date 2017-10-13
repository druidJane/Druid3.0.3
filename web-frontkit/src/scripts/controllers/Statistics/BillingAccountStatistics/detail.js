define(["app"],function(app){
	var injectParams = ['$rootScope','$timeout','$routeParams','paramService','$scope', '$location', 'NgTableParams', 'repoService','graphService', 'utilService','modalService'];
	var billDetailIndexCtrl = function($rootScope,$timeout,$routeParams,paramService, $scope, $location, NgTableParams, repo, graphService,util,modal) {
		var billDetailConf = {
	            url: 'Statistics/BillingAccountStatistics', //基本URL，必填
	            showName: '计费账户详情统计', //提示的名称，一般为模块名，必填
				exportUrl:'Statistics/BillingAccountStatistics/Index/export',
				disPlayTaskNameInput: true
	        };
	    $scope.billDetailParams = {

        };      
		$scope.billDetailParams.operType = 1;
		var Params = paramService.decode($routeParams.queryParam);

		$scope.billDetailParams.accuntId = Params.accuntId;
		$scope.accountName = Params.accountName;

		$scope.billDetailParams.beginDate = Params.beginDate;
        $scope.billDetailParams.endDate = Params.endDate;
        $(function() {	
             $scope.datePicker4StatBill.startDate =  Params.beginDate;
            $('input[name="beginDate"]').daterangepicker($scope.datePicker4StatBill);
            $scope.datePicker4StatBillEnd.startDate = Params.endDate;
            $('input[name="endDate"]').daterangepicker($scope.datePicker4StatBillEnd);
        });
		
       $scope.dayChange = function (){		

			$scope.billDetailParams.operType=1;				
			$(function() {
			
				$scope.datePicker4StatBill.startDate = Params.beginDate;
				$('input[name="beginDate"]').daterangepicker($scope.datePicker4StatBill);
				$scope.datePicker4StatBillEnd.startDate = Params.endDate;			
				$('input[name="endDate"]').daterangepicker($scope.datePicker4StatBillEnd);
			});			
		};
        $scope.monthChange = function (){
			var now = new Date(); //当前日期
			var nowYear = now.getFullYear(); //当前年
			var monthStartDate = new Date(nowYear, 0, 1);
			$scope.billDetailParams.operType=2;	
			$(function() {			
				$scope.monthPicker4StatBill.startDate = monthStartDate.Format("yyyy-MM");
				$('input[name="beginDate"]').daterangepicker($scope.monthPicker4StatBill);
				$scope.monthPicker4StatBillEnd.startDate = moment().format('YYYY-MM');			
				$('input[name="endDate"]').daterangepicker($scope.monthPicker4StatBillEnd);
			});			
		};
	
		
        $scope.billDetailTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_GETDETAILSTAT,billDetailConf, util.buildQueryParam(params, $scope.billDetailParams)).then(function(data) {
                    params.total(data.total);
                    $scope.d1 = data.data.d1;
					if(data.total > 0){
						$scope.stats =	data.data.d3;
						$scope.hasApp = true;
                    	$scope.toggleStat();
					}else{
						$scope.hasApp = false;
						$scope.stats ={};
					}
					
                    return data.data.d2;
                });
            }
        });
		
		
		$scope.toggleStat = function() {
         
            $timeout(function() {
                $scope.showStat("echart", $scope.stats);
            }, 10);
        };

        $scope.showStat = function(id, data) {
            var option = graphService.getSendStatOption4Multi(data.title, data.legend, data.datas, data.xAxis, data.types,data.yMax,data.interval);
            graphService.showEchart(id, option);
        };
        //列表头选项
        $scope.cols = [
            { title: "计费账户", show: true },
            { title: "计费时间", show: true },
            { title: "短信消费(元)", show: true },
            { title: "彩信消费(元)", show: true },
            { title: "总消费(元)", show: true },
			{ title: "操作", show: true },
        ];
        $scope.searchBillAccountStat = function(){
        	 if($scope.billDetailParams.beginDate > $scope.billDetailParams.endDate){
        		 toastr.warning("开始日期不能大于结束日期");
        		 return;
        	 }
        	 $scope.billDetailTable.reload();
        };
        $scope.showConsume = function(billDetailStat){
        	//$cookieStore.put('userConsumeParams',{deductTime:billDetailStat.deductTime,accuntId:$scope.billDetailParams.accuntId,accountName:billDetailStat.accountName});
			var queryParams = {};
			queryParams.deductTime=billDetailStat.deductTime;
			queryParams.accuntId=$scope.billDetailParams.accuntId;
			queryParams.accountName=billDetailStat.accountName;
			var queryParam = paramService.encode(queryParams);
        	 $location.path($scope.urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_USERCONSUME + "/" + queryParam);
        }

        $scope.exportuserDetailStat = function(){
			$scope.billDetailParams.sendReport = 'true'; 
			
			var params = {
                count: 1,
                page: 1,
                params: $scope.billDetailParams
            };
            repo.queryByUrl($scope.urlPerMap.STATISTICS_BILLINGACCOUNTSTATISTICS_GETDETAILSTAT, billDetailConf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
				$scope.billDetailParams.exportRecordSize = data.total;
				if($scope.billDetailParams.operType == 1){
					 modal.exportModal($scope, "计费账户日趋势统计导出", billDetailConf, $scope.billDetailParams);
				}else{
					 modal.exportModal($scope, "计费账户月趋势统计导出", billDetailConf, $scope.billDetailParams);
				}
            });        

        };
		$scope.go_back = function(){
			window.history.back();
		};
	};
	billDetailIndexCtrl.$inject = injectParams;
    app.register.controller('billDetailIndexCtrl', billDetailIndexCtrl);
	
});