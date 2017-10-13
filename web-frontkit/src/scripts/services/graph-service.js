define(["app"], function(app) {
    var graphService = function($rootScope, $location, $q) {

        var getBaseOption = function (name, legend,tooltipflag) {
			var tooplipvalue='';
			if(tooltipflag==1){
				tooplipvalue ='条'	
			}else{
				tooplipvalue ='元'	
			}
            return {
                title: {text: name, right: '5%', top:'3%'},
				
                tooltip: {
					trigger: 'axis',
				   
                    formatter:function(params){
						var result = '';
                        result += params[0].name +'<br/>'
						params.forEach(function (item) {
					
							result += '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + item.color + '"></span>';							
                            result += item.seriesName +' : '+item.data+ tooplipvalue +'<br/>'
						});

						return result;
					}
				},
                legend: {
                    left: '2%',
                    top:'3%' ,
                    data: legend
                },
                grid: {left:'3%',right:'4%',bottom:'3%',containLabel: true,x:'50%'},
                color:['#fc9c6f','#35a9e5', '#2ecc71', '#f8d56b'],
                xAxis: [],
                yAxis: [],
                series: []
            }
        }

        var getSendStatOption = function(title, legend, data, xAxis,yFormatter,yMax,interval){
            var option = getBaseOption(title, legend,1);
            option.title.right = "50%";
            option.tooltip.axisPointer = {
                type : 'line'        // 默认为直线，可选为：'line' | 'shadow'
            };
            option.xAxis = [
                {
                    type : 'category',
                    boundaryGap : true,
					
                    data : ['1','2','3','4','5','6','7','8','9','10','11','12']
                }
            ];
            if(xAxis){
                option.xAxis[0].data = xAxis;
            }
            if(yFormatter){
				 option.yAxis = [{
					type : 'value',	
                   	max:yMax,
					min:0,
					interval:interval,
					axisLabel : {
						formatter: '{value} 条'
					}			
			        
				}];
            }else{
				 option.yAxis = [{
					type : 'value',	
	
				}];
			}
            for(var i in data){
                var tmpData = data[i];
                var serie = getSendSeries(legend[i], tmpData);
                option.series.push(serie);
            }
            return option;
        }
        var getSendStatOption4Multi = function(title, legend, data, xAxis,types,yMax,interval){
            var option = getBaseOption(title, legend,2);

            option.title.right = "50%";
            option.tooltip.axisPointer = {
                type : 'line'        // 默认为直线，可选为：'line' | 'shadow'
            };

            option.xAxis = [
                {
                    type : 'category',
                    boundaryGap : true,
					xAxisWidth : 80,
                    data : ['1','2','3','4','5','6','7','8','9','10','11','12']
                }
            ];
            if(xAxis){
                option.xAxis[0].data = xAxis;
            }
            option.yAxis = [{
					type : 'value',
					max:yMax,
					min:0,
					interval:interval,
					axisLabel : {
						formatter: '{value} 元'
					}			
			
			}];
            for(var i in data){
                var tmpData = data[i];
                var serie = getSendSeries4Multi(legend[i], tmpData,types[i]);
                option.series.push(serie);
            }
            return option;
        }
        var getSendSeries = function(legend, data){
            return {
                name: legend,
                type: 'line',
                smooth: false,
                data: data
            };
        }
        var getSendSeries4Multi = function(legend, data,type){
			if(type==1){
				return {
					name: legend,
					type: 'bar',
					barWidth : 50,
					stack: '总量',
					data: data
				};
			}else{
				
				return {
					name: legend,
					type: 'line',		
					data: data
				};
			}
           
        }
        var showEchart = function(id, option, width, height){
            if(!option){
                return;
            }
            var echartDiv = document.getElementById(id);
            if(!echartDiv){
                return;
            }
            if(width) {
                echartDiv.style.width = width + "px";
            }
            if(height){
                echartDiv.style.height = height+"px";
            }
            var chart = echarts.init(echartDiv);
            chart.setOption(option);
        }

        return {
            showEchart: showEchart,
            getSendSeries: getSendSeries,
            getBaseOption: getBaseOption,
            getSendStatOption: getSendStatOption,
			getSendStatOption4Multi:getSendStatOption4Multi
        };
    };
    app.factory("graphService", ["$rootScope", '$location', '$q', graphService]);
});
