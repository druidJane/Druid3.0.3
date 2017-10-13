define(["app"],function(app){
	var injectParams = ['$rootScope','$location','paramService','$timeout','$routeParams','$scope', '$location', 'NgTableParams', 'repoService','graphService', 'utilService', 'modalService'];
	var userDetailIndexCtrl = function($rootScope, $location,paramService, $timeout, $routeParams, $scope, $location, NgTableParams, repo, graphService,util,modal) {
		//var cache = $cacheFactory('myCache');
		
		//cache.put('hello','world');
		//cache.put('date1','2017-05-01');
		//var info = cache.info();
			//cache.put('beginDate', $scope.userStatParams.beginDate);
			//cache.put('endDate',$scope.userStatParams.endDate);
			//cache.put('userId',userStat.userId);
			//cache.put('smsType',$scope.userStatParams.smsType);
			//cache.put('userName',userStat.userName);
			//cache.put('deptName',userStat.deptName);


		var userDetailConf = {
	            url: 'Statistics/SumStatistics', //基本URL，必填
	            showName: '统计详情', //提示的名称，一般为模块名，必填     
	            exportUrl:'Statistics/SumStatistics/SumAccount/singleUserExport',//导出详情url
				disPlayTaskNameInput: true
	        };
	    $scope.userDetailParams = {

        };      
		var Params = {};
		Params = paramService.decode($routeParams.queryParam);
        $scope.userDetailParams.smsType = Params.smsType;
		$scope.userDetailParams.userId = Params.userId;
		$scope.userDetailParams.operType = 1;		
		$scope.userName= Params.userName;
		$scope.deptName= Params.deptName;
		$scope.userDetailParams.beginDate = Params.beginDate;
        $scope.userDetailParams.endDate = Params.endDate;
		$scope.userDetailParams.userName = Params.userName;
		$scope.userDetailParams.deptName= Params.deptName;
		
		

        $(function() {
           // $scope.datePicker.startDate = moment().format('YYYY-MM-DD');
		    $scope.datePicker4Stat.startDate =  Params.beginDate;
            $('input[name="beginDate"]').daterangepicker($scope.datePicker4Stat);
            $scope.datePicker4StatEnd.startDate = Params.endDate;
            $('input[name="endDate"]').daterangepicker($scope.datePicker4StatEnd);
        });
		$scope.dayChange = function (){
		
			$scope.userDetailParams.operType=1;

			$(function() {
			
				$scope.datePicker4Stat.startDate =  Params.beginDate;
				$('input[name="beginDate"]').daterangepicker($scope.datePicker4Stat);
				$scope.datePicker4StatEnd.startDate =  Params.endDate;
				
				$('input[name="endDate"]').daterangepicker($scope.datePicker4StatEnd);
			});			
		};
        $scope.monthChange = function (){
			var now = new Date(); //当前日期
			var nowYear = now.getFullYear(); //当前年
			var monthStartDate = new Date(nowYear, 0, 1);
			$scope.userDetailParams.operType=2;	

			$(function() {	
					
				$scope.monthPicker4Stat.startDate = monthStartDate.Format("yyyy-MM");
				$('input[name="beginDate"]').daterangepicker($scope.monthPicker4Stat);
				$scope.monthPicker4StatEnd.startDate = moment().format('YYYY-MM');			
				$('input[name="endDate"]').daterangepicker($scope.monthPicker4StatEnd);
			});			
		};
		$scope.hasApp = false;
        $scope.userDetailTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSERLIST,userDetailConf, util.buildQueryParam(params, $scope.userDetailParams)).then(function(data) {
                    params.total(data.total);   
                    $scope.d1 = data.data.d1;
				
					if(data.total > 0){
						
						$scope.stats =	data.data.d2;
						$scope.hasApp = true;
                    	$scope.toggleStat();
					}else{
						
						$scope.hasApp = false;
						$scope.stats = {};
					}				
                    return data.data.d1;
                });
            }
        });

		$scope.toggleStat = function() {
         
            $timeout(function() {
                $scope.showStat("echart", $scope.stats);
            }, 10);
        }

        $scope.showStat = function(id, data) {
            var option = graphService.getSendStatOption(data.title, data.legend, data.datas, data.xAxis,true,data.yMax,data.interval);
            graphService.showEchart(id, option);
        }

        //列表头选项
        //列表头选项
        $scope.cols = [
			{ title: "日期", show: true },
            { title: "用户名", show: true },
            { title: "部门", show: true },         
            { title: "总提交量", show: true },
            { title: "总发送量", show: true },
            { title: "总成功量", show: true },
            { title: "移动成功量", show: true },
            { title: "联通成功量", show: true },
            { title: "电信CMDA成功量", show: true },
            { title: "电信小灵通成功量", show: true }
        ];
        $scope.searchUserDetailStat = function(){
        	 if($scope.userDetailParams.beginDate > $scope.userDetailParams.endDate){
        		 toastr.warning("开始日期不能大于结束日期");
        		 return;
        	 }
        	 $scope.userDetailTable.reload();
        };

        $scope.exportuserDetailStat = function(){
			$scope.userDetailParams.sendReport = 'true'; 			
			var params = {
                count: 1,
                page: 1,
                params: $scope.userDetailParams
            };
            repo.queryByUrl($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSERLIST, userDetailConf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
				$scope.userDetailParams.exportRecordSize = data.total;
				if($scope.userDetailParams.operType == 1){
					 modal.exportModal($scope, "用户日趋势统计导出", userDetailConf, $scope.userDetailParams);
				}else{
					 modal.exportModal($scope, "用户月趋势统计导出", userDetailConf, $scope.userDetailParams);
				}
               
            });    
        }
	};
	userDetailIndexCtrl.$inject = injectParams;
    app.register.controller('userDetailIndexCtrl', userDetailIndexCtrl);
	
});