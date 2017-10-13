define(["app"],function(app){
	var injectParams = ['$scope','$timeout', '$location', 'NgTableParams', 'repoService', 'utilService', 'graphService', 'modalService'];
	var bizStatIndexCtrl = function($scope,$timeout,$location, NgTableParams, repo, util,graphService, modal) {
		var bizStatConf = {
	            url: '/Statistics/SumStatistics', //基本URL，必填
	            showName: '业务类型统计', //提示的名称，一般为模块名，必填
	            exportUrl:$scope.urlPerMap.STATISTICS_SUMSTATISTICS_EXPORTBUSINESSTYPE, //导出url
                disPlayTaskNameInput: true
	        };
	    $scope.params = {};
	    $scope.params.operType=1;
		$scope.params.smsType=0;
		repo.getByUrl($scope.urlPerMap.STATISTICS_SUMSTATISTICS_BUSITYPE).then(function(data){
			
            $scope.bizType = data.data;		
			$scope.params.bizType=$scope.bizType[0].id;
			
        });		
		var firstDate = new Date();
       	firstDate.setDate(1);
        $scope.params.beginDate = firstDate.Format("yyyy-MM-dd");
        $scope.params.endDate = new Date().Format("yyyy-MM-dd");
        $(function() {		      
            $scope.datePicker4Stat.startDate = firstDate.Format("yyyy-MM-dd");
            $('input[name="beginDate"]').daterangepicker($scope.datePicker4Stat);
            $scope.datePicker4StatEnd.startDate = moment().format('YYYY-MM-DD');	    
            $('input[name="endDate"]').daterangepicker($scope.datePicker4StatEnd);
        });
		
       $scope.dayChange = function (){		
			$scope.params.operType=1;				
			$(function() {
			     
				$scope.datePicker4Stat.startDate = firstDate.Format("yyyy-MM-dd");
				$('input[name="beginDate"]').daterangepicker($scope.datePicker4Stat);
				$scope.datePicker4StatEnd.startDate = moment().format('YYYY-MM-DD');				
				$('input[name="endDate"]').daterangepicker($scope.datePicker4StatEnd);
			});			
		};
        $scope.monthChange = function (){
			var now = new Date(); //当前日期
			var nowYear = now.getFullYear(); //当前年
			var monthStartDate = new Date(nowYear, 0, 1);
			$scope.params.operType=2;	
			$(function() {			
				$scope.monthPicker4Stat.startDate = monthStartDate.Format("yyyy-MM");
				$('input[name="beginDate"]').daterangepicker($scope.monthPicker4Stat);
				$scope.monthPicker4StatEnd.startDate = moment().format('YYYY-MM');			
				$('input[name="endDate"]').daterangepicker($scope.monthPicker4StatEnd);
			});			
		};

		$scope.hasApp = false;
        $scope.toggleStat = function() {
         
            $timeout(function() {
                $scope.showStat("echart", $scope.stats);
            }, 10);
        }

        $scope.showStat = function(id, data) {
            var option = graphService.getSendStatOption(data.title, data.legend, data.datas, data.xAxis,true,data.yMax,data.interval);
            graphService.showEchart(id, option);
        }

        $scope.bizStatTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
				
                return repo.queryByUrl($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMBUSINESSTYPE,bizStatConf, util.buildQueryParam(params, $scope.params)).then(function(data) {
                    params.total(data.total);
					$scope.d1 = data.data.d1;
					$scope.d2 = data.data.d2;	
				    if (data.total > 0) {						
						$scope.stats =	data.data.d3;
                    	$scope.hasApp = true;
                    	$scope.toggleStat();
                    } else {
						
                        $scope.hasApp = false;
                       	$scope.stats = {};
                    }			
                    return data.data.d2;
                });
            }
        });
        //列表头选项
        $scope.cols = [
            { title: "业务类型", show: true },
            { title: "日期", show: true },
            { title: "总提交量", show: true },
            { title: "总发送量", show: true },
            { title: "总成功量", show: true },
            { title: "移动成功量", show: true },
            { title: "联通成功量", show: true },
            { title: "电信CMDA成功量", show: true },
            { title: "电信小灵通成功量", show: true }
        ];

        $scope.searchBusiStat = function(){
			 if($scope.params.beginDate > $scope.params.endDate){
        		 toastr.warning("开始日期不能大于结束日期");
        		 return;
        	 }
			$scope.params.newQuery =1;
        	$scope.bizStatTable.reload();
        };
        $scope.exportBusiStat = function(){

			$scope.params.sendReport = 'true'; 
			$scope.params.sendReportNoBill = 'true';
			var params = {
                count: 1,
                page: 1,
                params: $scope.params
            };
            repo.queryByUrl($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMBUSINESSTYPE, bizStatConf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                $scope.params.exportRecordSize = data.total;
				if($scope.params.operType == 1){
					modal.exportModal($scope, "业务类型日趋势统计", bizStatConf, $scope.params);
				}else{
					modal.exportModal($scope, "业务类型月趋势统计", bizStatConf, $scope.params);
				}
            });        
        
        }
	};
	bizStatIndexCtrl.$inject = injectParams;
    app.register.controller('bizStatIndexCtrl', bizStatIndexCtrl);
});
