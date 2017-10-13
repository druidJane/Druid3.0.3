define(["app"],function(app){
	var injectParams = ['$scope','$location','$cookieStore','paramService','$routeParams','NgTableParams', 'repoService', 'utilService', 'modalService','StService'];
	var userStatIndexCtrl = function($scope, $location,$cookieStore,paramService,$routeParams, NgTableParams, repo, util, modal,stService) {
		
		
		var userStatConf = {
	            url: 'Statistics/SumStatistics', //基本URL，必填
	            showName: '用户统计', //提示的名称，一般为模块名，必填
	            detailUrl: $scope.urlPerMap.INFO_BLACKLIST_IMPORT+'?action=upload', //统计详情url
	            userSelectUrl:'/Statistics/SumStatistics/Index/UserSelect', //用户查询条件数据url
	            exportUrl:$scope.urlPerMap.STATISTICS_SUMSTATISTICS_EXPORTACCOUNT, //部门查询条件数据url
                disPlayTaskNameInput: true
	        };

		
		 $scope.initCtrl = function () {
				$scope.stParams = {
					smsType:'0',
				};
			  var firstDate = new Date();
				firstDate.setDate(1);
				$scope.stParams.beginDate = firstDate.Format("yyyy-MM-dd");
				$scope.stParams.endDate = new Date().Format("yyyy-MM-dd");
				$(function() {		
					 $scope.datePicker4Stat.startDate = firstDate.Format("yyyy-MM-dd");
					$('input[name="beginDate"]').daterangepicker($scope.datePicker4Stat);
					$scope.datePicker4StatEnd.startDate = moment().format('YYYY-MM-DD');	    
					$('input[name="endDate"]').daterangepicker($scope.datePicker4StatEnd);         
				});
	
	  
  
            $scope.selectedObj = {
                user:true,
                dept:true,
				userUrl:$scope.urlPerMap.STATISTICS_SUMSTATISTICS_USERSELECT+'?userName=',
				deptUrl:$scope.urlPerMap.STATISTICS_SUMSTATISTICS_DEPTSELECT+'?deptName='
            }
             stService.autoCompleteUser($scope);
             stService.autoCompleteDept($scope);
        }
	   
        
        $scope.userStatTable = new NgTableParams({
            page: 1,
            count: 10
        }, {
            total: 0,
            getData: function(params) {
				if (!stService.checkStCondition4UserStat($scope, $scope.selectedObj)) {
					return;
				}
                return repo.queryByUrl($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT,userStatConf, util.buildQueryParam(params, $scope.stParams)).then(function(data) {
                    params.total(data.total);
                    return data.data;
                });
            }
        });
		
        //列表头选项
        $scope.cols = [
            { title: "用户名", show: true },
            { title: "部门", show: true },
            { title: "总提交量", show: true },
            { title: "总发送量", show: true },
            { title: "总成功量", show: true },
            { title: "移动成功量", show: true },
            { title: "联通成功量", show: true },
            { title: "电信CMDA成功量", show: true },
            { title: "电信小灵通成功量", show: true },
            { title: "操作", show: true }
        ];
        $scope.searchUserStat = function(){
			 if($scope.stParams.beginDate > $scope.stParams.endDate){
        		 toastr.warning("开始日期不能大于结束日期");
        		 return;
        	 }
			 
        	 $scope.userStatTable.reload();
        };
        $scope.showDetail = function(userStat){
			var paramObj = {};
			paramObj.beginDate = $scope.stParams.beginDate;
			paramObj.endDate = $scope.stParams.endDate;
			paramObj.userId = userStat.userId;
			paramObj.smsType = $scope.stParams.smsType;
			paramObj.userName = userStat.userName;
			paramObj.deptName = userStat.deptName;
			var queryParam = paramService.encode(paramObj);
			//$cookieStore.put('bizTypeStatisticsParams',{beginDate:$scope.stParams.beginDate,endDate:$scope.stParams.endDate,userId:userStat.userId,smsType:		$scope.stParams.smsType,userName:userStat.userName,deptName:userStat.deptName});
			 $location.path($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT_SINGLEUSERLIST + "/" + queryParam);
        }
		
        $scope.exportUserStat = function(){
			$scope.stParams.sendReport = 'true'; 
			$scope.stParams.sendReportNoBill = 'true';			
			var params = {
                count: 1,
                page: 1,
                params: $scope.stParams
            };
            repo.queryByUrl($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT, userStatConf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
                $scope.stParams.exportRecordSize = data.total;
                modal.exportModal($scope, "用户总量统计导出", userStatConf, $scope.stParams);
            });        
			
        }
		//region 执行这个控制器的一些方法
        this.init =$scope.initCtrl();
	};
	userStatIndexCtrl.$inject = injectParams;
    app.register.controller('userStatIndexCtrl', userStatIndexCtrl);
	
});
