define(["app"],function(app){
	var injectParams = ['$scope','$timeout', '$location', 'NgTableParams', 'repoService', 'utilService','graphService', 'modalService'];
	var deptStatIndexCtrl = function($scope,$timeout, $location, NgTableParams, repo, util,graphService, modal) {
		var deptStatConf = {
	            url: '/Statistics/SumStatistics', //基本URL，必填
	            showName: '报表统计->部门统计', //提示的名称，一般为模块名，必填
	            exportUrl:$scope.urlPerMap.STATISTICS_SUMSTATISTICS_EXPORTDEPARTMENT, //数据导出url
				disPlayTaskNameInput: true
	        };
	    $scope.params = {};
	    $scope.params.operType=1;
		$scope.params.smsType=0;
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
		$scope.x =1;		
       $scope.dayChange = function (){		
			$scope.params.operType=1;				
			$scope.params.beginDate = firstDate.Format("yyyy-MM-dd");
			$scope.params.endDate = new Date().Format("yyyy-MM-dd");
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
			$scope.params.beginDate = monthStartDate.Format("yyyy-MM");
			$scope.params.endDate = new Date().Format("yyyy-MM");
			$(function() {			

				$scope.monthPicker4Stat.startDate = monthStartDate.Format("yyyy-MM");
				$('input[name="beginDate"]').daterangepicker($scope.monthPicker4Stat);
				$scope.monthPicker4Stat.startDate = moment().format('YYYY-MM');			
				$('input[name="endDate"]').daterangepicker($scope.monthPicker4Stat);
			});			
		};
        //树结构
        $scope.treeOptions = {
            //子节点字段名称
            nodeChildren: "children",
            dirSelectable: true
        };
		$scope.predicate='';

        //加载左侧树形结构
        repo.post($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETDEPTTREE).then(function(data) {
            $scope.dataForTheTree = util.transDataToTree(data.data.tree, 'id', 'parentId', 'children');
            $scope.expandedNodes = [$scope.dataForTheTree[0]];
			$scope.predicate = data.data.deptName;
			if(data.data.level ==1){
				for (var i = $scope.dataForTheTree[0].children.length - 1; i >= 0; i--) {
					$scope.expandedNodes.push($scope.dataForTheTree[0].children[i]);
				}
			}
				   
		    $scope.params.deptName = data.data.deptName;
			$scope.params.deptId = data.data.id;
		

			$scope.refreshDeptStatList();
        });
			
	 $scope.comparator = false;		
		
      $scope.changeLevel =function(x){
		   
		   if(x==1){	    
		   $scope.predicate='';
			    $scope.expandedNodes = [$scope.dataForTheTree[0]];
		   }else if(x==2){
			   $scope.predicate='';
		   		$scope.expandedNodes = [$scope.dataForTheTree[0]];
				for (var i = $scope.dataForTheTree[0].children.length - 1; i >= 0; i--) {
                	$scope.expandedNodes.push($scope.dataForTheTree[0].children[i]);
           		}								   				
		   }else if(x==3){
			   $scope.predicate='';
			   $scope.expandedNodes = [$scope.dataForTheTree[0]];
				for (var i = $scope.dataForTheTree[0].children.length - 1; i >= 0; i--) {
					$scope.expandedNodes.push($scope.dataForTheTree[0].children[i]);
					if(typeof($scope.dataForTheTree[0].children[i].children) == 'undefined'){
						continue;
					}
					for(var j = $scope.dataForTheTree[0].children[i].children.length - 1; j >= 0; j--){
					
						$scope.expandedNodes.push($scope.dataForTheTree[0].children[i].children[j]);
					}
                	
           		}					
		   }else if(x==4){
			   $scope.predicate='';
			   $scope.expandedNodes = [$scope.dataForTheTree[0]];
				for (var i = $scope.dataForTheTree[0].children.length - 1; i >= 0; i--) {
					$scope.expandedNodes.push($scope.dataForTheTree[0].children[i]);
					if(typeof($scope.dataForTheTree[0].children[i].children) == 'undefined'){
						continue;
					}
					for(var j = $scope.dataForTheTree[0].children[i].children.length - 1; j >= 0; j--){					
						$scope.expandedNodes.push($scope.dataForTheTree[0].children[i].children[j]);
						if(typeof($scope.dataForTheTree[0].children[i].children[j].children) == 'undefined'){
							continue;
						}
						for(var f = $scope.dataForTheTree[0].children[i].children[j].children.length - 1; f >= 0; f--){
				
							$scope.expandedNodes.push($scope.dataForTheTree[0].children[i].children[j].children[f]);
						}
					}
                	
           		}					
		   }
		   
	  }
		
		//树形菜单过滤函数
        $scope.filterDeptName = function(node){
            return $scope.nodeTraversal(node);
        }

        $scope.nodeTraversal= function(node){
            if(node.deptName != null && node.deptName.toLowerCase().indexOf($scope.predicate.toLowerCase()) >= 0){
                return true;
            }
            if(node.children != null){
            	var results=false;
                for(var i = 0; i<node.children.length; i++){
                    results = results||$scope.nodeTraversal(node.children[i]);
                }
                return results;
            }
            return false;
        }
		
		
		$scope.toggleStat = function() {
         
            $timeout(function() {
                $scope.showStat("echart", $scope.stats);
            }, 10);
        }

        $scope.showStat = function(id, data) {
            var option = graphService.getSendStatOption(data.title, data.legend, data.datas, data.xAxis,true,data.yMax,data.interval);
            graphService.showEchart(id, option);
        }




        $scope.showSelected = function(node) {
			$scope.predicate ='';
            $scope.params.deptId = node.id;
			$scope.params.deptName = node.deptName;
			//$scope.predicate = node.deptName;
            $scope.refreshDeptStatList();
        };
        //加载右侧个人通讯录列表
		$scope.hasApp = false;
        $scope.refreshDeptStatList = function() {

			$scope.deptStatTable = new NgTableParams({
				page: 1,
				count: 10
			}, {
				total: 0,
				getData: function(params) {
					return repo.queryByUrl($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT,deptStatConf, util.buildQueryParam(params, $scope.params)).then(function(data) {
						params.total(data.total);
						$scope.d1 = data.data.d1;
						$scope.d2 = data.data.d2;
						
						if(data.total > 0){
							if(data.data.d3 != null){
								$scope.stats =	data.data.d3;
								$scope.hasApp = true;
								$scope.toggleStat();
							}
							
						}else{
							$scope.hasApp = false;
							$scope.stats = {};
						}
						
						return data.data.d2;
					});
				}
			});
        };
      

        //列表头选项
        $scope.cols = [
            { title: "日期", show: true },
            { title: "部门", show: true },
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
        	$scope.refreshDeptStatList();
        };
        $scope.exportDeptStat = function(){
			$scope.params.sendReport = 'true'; 
			$scope.params.sendReportNoBill = 'true';
			var params = {
                count: 1,
                page: 1,
                params: $scope.params
            };
            repo.queryByUrl($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT, deptStatConf, params).then(function(data) {
                if(data.total == 0) {
                    toastr.warning("没有数据可供导出！");
                    return;
                }
				$scope.params.exportRecordSize = data.total;
				if($scope.params.operType == 1){
					modal.exportModal($scope, "部门日趋势统计导出", deptStatConf, $scope.params);
				}else{
					modal.exportModal($scope, "部门月趋势统计导出", deptStatConf, $scope.params);
				}
                
            });              
        
        }
	};
	deptStatIndexCtrl.$inject = injectParams;
    app.register.controller('deptStatIndexCtrl', deptStatIndexCtrl);
});
