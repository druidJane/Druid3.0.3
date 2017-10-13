define(["app"], function(app) {
    var injectParams = ['$scope', '$timeout', 'repoService', 'utilService', 'graphService', '$interval','modalService'];
    var consoleIndexCtrl = function($scope, $timeout, repo, util, graphService, $interval,modal) {
        var appConf = {
            url: 'home', //基本URL，必填
            showName: '首页', //提示的名称，一般为模块名，必填
            showDetailUrl: 'console/detail.html'
        };
		$scope.operType=2;

        repo.post("common/notice/unAuditNoticeCount").then(function (data) {
            $scope.unAudit = data.data;


        });
		$scope.hasApp = false;
	    $scope.permissionFlag = 0;

		// 判断是否有部门统计报表权限
		if($scope.hasPermission($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMDEPARTMENT)){
			$scope.permissionFlag = 1;
		}else if($scope.hasPermission($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMACCOUNT)){
			$scope.permissionFlag = 2;
		}else if($scope.hasPermission($scope.urlPerMap.STATISTICS_SUMSTATISTICS_GETSUMBUSINESSTYPE)){
			$scope.permissionFlag = 3;
		}else{
			$scope.permissionFlag = 4;
		}

		if($scope.hasPermission($scope.urlPerMap.HOME_STATISTICS)){

			repo.post($scope.urlPerMap.HOME_STATISTICS, $scope.permissionFlag).then(function(data) {
				$scope.sendStat = data.data.sendStat;
				$scope.stats = data.data.chartDate;
				if ($scope.stats!=null) {				
					$scope.hasApp = true;
					$scope.toggleStat();
				}else{
					$scope.hasApp = false;
					
				}			
			});	
		}
		
		repo.post("common/notice/getAnnouncementList").then(function (data) {
              $scope.unReadNoticeList = data.data
              $scope.unReadNoticeCount = data.total;
        });
		$scope.detail = function(notice) {
			var rets = [];
			rets.push(notice.id);
			repo.post('common/notice/updateStateByIds',rets,function(data){
                if (data.data == 0) {
                   
                } else {
                    repo.post("common/notice/unReadNoticeCount").then($scope.unReadNoticeCallBack);
                    $scope.table.reload();
                }
            });
            util.commonModal($scope, '查看公告信息', appConf.showDetailUrl, function(modal) {
                var mScope = modal.$scope;
           			repo.post("common/notice/getNoticeContent",notice.objectId).then(function (data) {
					  mScope.announcement.content = data.data.content
					  mScope.announcement.updateTime = data.data.updateTime;
				});				
				mScope.okBtn={hide:true};
                mScope.closeBtn={hide:true};
                mScope.announcement = angular.copy(notice);
            });
        };
		$scope.toggleStat = function() {
         
            $timeout(function() {
                $scope.showStat("echart", $scope.stats);
            }, 10);
        }

        $scope.showStat = function(id, data) {
            var option = graphService.getSendStatOption(data.title, data.legend, data.datas, data.xAxis,true,data.yMax,data.interval);
            graphService.showEchart(id, option);
        };
		$scope.weekstat = function(){
			$scope.operType=2;	
			repo.post('Home/Index/Statistics', $scope.operType).then(function(data) {	
				$scope.sendStat = data.data.sendStat;
				$scope.stats = data.data.chartDate;
				if (angular.isArray($scope.stats)) {
					$scope.hasApp = true;
					$scope.toggleStat();
				}else{
					$scope.hasApp = false;
				}			
			});		
		};
		
		$("#noticeScroll").noticeScroll();
	};
    consoleIndexCtrl.$inject = injectParams;
    app.register.controller('consoleIndexCtrl', consoleIndexCtrl);
});
$.fn.extend({
    'noticeScroll':function(opt){
        var opts = $.extend({},{
            speed : 500,
            timer : 5000
        },opt)
        var _this = this.find('ul:first');
        var _h = _this.find('li:first').height();
        var _tot = _this.find('li').length;
        var ind = 1;
        _this.find('li:last').prependTo(_this);
        var scrollDown = function(){
            if(_this.find('li').length > 1){
                _this.stop().animate({
                    marginTop : -_h
                },opts.speed,function(){
                    _this.find('li:first').appendTo(_this);
                    _this.css({marginTop:0});
                })
            }
        };
        _this.hover(function(){
            clearInterval(timer);
        },function(){
            timer = setInterval(scrollDown,opts.timer);
        });
        this.find('a.up').click(function(){
            clearInterval(timer);
            _this.find('li:last').prependTo(_this);
            _this.css({marginTop:-_h});
            timer = setInterval(scrollDown,opts.timer);
        });
        this.find('a.down').click(function(){
            clearInterval(timer);
            _this.find('li:first').appendTo(_this);
            _this.css({marginTop:-_h});
            timer = setInterval(scrollDown,opts.timer);
        });
        timer = setInterval(scrollDown,opts.timer);
    }
})