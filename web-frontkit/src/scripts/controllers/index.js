define(["app"], function (app) {
    app.controller("indexCtrl", function ($http, $scope, $rootScope, $location, $window, $filter, breadcrumbs, repoService, $interval, utilService,paramService) { // 主模块
        //region 初始化是判断是否配置图片路径已经完成，若(xpath.isImgFlag=0)表示尚未配置
        // 则进行配置
        if (xpath.isImgFlag == 0) {
            utilService.imgUrl = $location.protocol()+"://"+$location.host()+":"+$location.port()+context+tmp;
            xpath.isImgFlag = 1;
        }
        //endregion
        $scope.urlPerMap = app.register.urlPerMapConstant;
        $scope.breadcrumbs = breadcrumbs;
        //功能菜单配置
        $scope.menulists = [];
        $scope.btnLists = [];
        //初始化数据
        $scope.globalVar = {
            unReadNoticeCount: 0,
            unReadNoticeList: [],
            upAndLoadCount : 0
        };
        $scope.listshownMenu = null;
        $scope.shownMenu = null;

		$scope.webversion = webversion;

        //触发登录验证
        repoService.post("common/checkLogin").then(function (data) {
            if (data.status == 0) {
                $scope.onLoginSuccess();
                $scope.loginUser = data.data;
            } else {
                toastr.error(data.errorMsg);
                $window.location.href = loginPage;
            }
        });

        $scope.initMenuClass = function () {
            $scope.shownMenu = $scope.menulists[0];
            $("#menuShow2 .item").hover(function () {
                $(this).find('.i_item').attr('src', $(this).find('.i_item').attr('active'));
            }, function () {
                $(this).find('.i_item').attr('src', $(this).find('.i_item').attr('base'));
            })
        };

        //菜单切换
        $scope.menuShow1 = function () {
            $("#menuShow1").hide();
            $("#menuShow2").show();
            $(".main_right").css('paddingLeft', '44px');
            $("#menuShowBtn1").hide();
        };

        $scope.menuShow2 = function () {
            $("#menuShow2").hide();
            $("#menuShow1").show();
            $(".main_right").css('paddingLeft', '180px');
            $("#menuShowBtn1").show();
        };


        $scope.onLoginSuccess = function () {
            repoService.queryByUrl('common/menu', {showName: '菜单'}, {}).then(
                function (data) {
                    $scope.menulists = data.data.menuList;
                    $scope.btnLists = data.data.btnList;
                    $scope.initMenuClass();
                    $scope.getUnReadNoticeCount();
                    $scope.getUpAndLoadCount();
                    $scope.getAccBasicInfo();
                    $interval($scope.getUnReadNoticeCount, 5000);
                    $interval($scope.getUpAndLoadCount, 5000);
                }
            );
        };
        
        $scope.getAccBasicInfo = function(){
            repoService.post("common/acc/getAccountInfo").then(function (data) {
                $scope.globalVar.accInfo = data.data;
            });
        };
        
        $scope.unReadNoticeCallBack = function(data) {
            //数据更新才更新
            if (data.total != -1) {
                $scope.globalVar.unReadNoticeList = data.data
                $scope.globalVar.unReadNoticeCount = data.total;
            }
        }

        $scope.getUnReadNoticeCount = function () {
            repoService.post("common/notice/unReadNoticeCount").then($scope.unReadNoticeCallBack);
        };

      
        $scope.readNotice = function (notice) {
            var rets = [];
            rets.push(notice.id);
            repoService.post('common/notice/updateStateByIds', rets, function (data) {
                repoService.post("common/notice/unReadNoticeCount").then($scope.unReadNoticeCallBack);
            });
            //保存查询参数
            $rootScope.noticeParam = notice;
        };

        $scope.showNoticeDetail = function (type, id, pushTime) {
            var pushTime = new Date(pushTime);
            var startTime = pushTime.Format("yyyy-MM-dd")+" 00:00:00";
            var endTime = pushTime.Format("yyyy-MM-dd")+" 59:59:59";
            var paramObj = {"packId":id,"startTime":startTime,"endTime":endTime};
            var encodeParam = paramService.encode(paramObj);
            if (type == 1) { //短信发送通知
                $location.url($scope.urlPerMap.SMSMGR_SENDTRACKING_INDEX + '?encodeParam='+encodeParam);
            } else if (type == 2) { //彩信发送通知
                $location.url($scope.urlPerMap.MMS_SEND_RECORD_INDEX + '?encodeParam='+encodeParam);
            } else if (type == 3) { //一级短信审核
                $location.url($scope.urlPerMap.SMSMGR_SENDPENDING_INDEX + '?encodeParam='+encodeParam);
            } else if (type == 4) { //一级彩信审核
                $location.url($scope.urlPerMap.SEND_MMS_AUDIT_INDEX + '?encodeParam='+encodeParam);
            }
        };

        $scope.getUpAndLoadCount = function () {
            repoService.post("common/upAndLoadCount").then(function (data) {
                if (angular.isDefined(data.data) && data.data != null) {
                    $scope.globalVar.upAndLoadCount = data.data[0];
                    //数据不存在的时候会出现null
                    if (angular.isDefined(data.data[1]) && data.data[1] != null) {
                        $scope.fileType = data.data[1];
                    }
                }
            });
        };

        $scope.showFileTask = function () {
            repoService.post('common/updateUpAndLoadCount', $scope.fileType, function (data) {
                repoService.post("common/upAndLoadCount").then(function (data) {
                    if (angular.isDefined(data.data) && data.data != null) {
                        $scope.globalVar.upAndLoadCount = data.data[0];
                    }
                    
                    if ($scope.fileType == 1) { //导入
                        $location.url($scope.urlPerMap.SYSTEMMGR_TASKMGR_INDEX + '?type=1');
                    } else {//导出
                        $location.url($scope.urlPerMap.SYSTEMMGR_TASKMGR_INDEX + '?type=2');
                    }
                    //先进入页面后更新
                    if (angular.isDefined(data.data[1]) && data.data[1] != null) {
                        $scope.fileType = data.data[1];
                    }
                });
            });
        };

        $scope.contactUs = function () {
            utilService.htmlModal($scope, "联系我们", "modal/contactUs.tpl.html",
                function (modal) {
                    var mScope = modal.$scope;
                    mScope.okBtn = {
                        hide: true
                    }
                });
        };

        $scope.redirect = function (path,refresh) {
            if (refresh == true) {
                path += (path.indexOf("?") == -1 ? "?" : "&") + "_ran=" + Math.random();
            }
            $location.url(path);
        };


        //功能菜单事件
        $scope.toggleMenu = function (group) {
            if (!$scope.isMenuShown(group)) {
                $scope.shownMenu = group;
            } else {
                $scope.shownMenu = null;
            }
            //平台首页
            if (group.menuName.indexOf("首页") != -1) {
                $scope.redirect("/",true);
            }
        };
        $scope.isMenuShown = function (group) {
            return $scope.shownMenu === group;
        };
        $scope.toggleList = function (group) {
            $scope.listshownMenu = group;
        };
        $scope.isListShown = function (group) {
            return $scope.listshownMenu === group;
        };

        $scope.logout = function () {
            utilService.confirm('确定退出当前系统？',function () {
                var url = xpath.service("login/logout");
                $http.post(url).success(function (data) {
                    if (data.status == 0) {
                        $window.location.href = loginPage;
                    }
                }).error(function (data, status, headers, config) {
                    var msg = "退出: " + (data.errorMsg ? data.errorMsg : data);
                    toastr.error(msg);
                });
            });
        };


        // 判断是否有权限
        $scope.hasPermission = function (operation) {
            var permUrls = operation.split(",");
            for (var i = 0; i < permUrls.length; i++) {
                if ($.inArray(permUrls[i], $scope.btnLists) != -1) {
                    return true;
                }
            }
            return false;
        };

        $scope.fileTypeSet = {};
        $scope.fileTypeSet["txt"] = ["text/plain"];
        $scope.fileTypeSet["excel"] = ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel"];
        $scope.fileTypeSet["csv"] = ["application/csv"];

        $scope.dateTimePicker = {
            timePicker: true,
            timePicker24Hour: true,
            singleDatePicker: true,
            timePickerSeconds: true,

            locale: {
                format: 'YYYY-MM-DD HH:mm:ss',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        };
        $scope.timePicker = {
            timePicker: true,
            timePicker24Hour: true,
            singleDatePicker: true,
            timePickerSeconds: true,
            locale: {
                format: 'HH:mm:ss',
                applyLabel: '确定',
                cancelLabel: '取消'
            }
        };
        $scope.datePicker = {
            singleDatePicker: true,
            locale: {
                format: 'YYYY-MM-DD',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        };
		$scope.datePicker4Stat = {
            singleDatePicker: true,
			maxDate:new Date(),
            locale: {
                format: 'YYYY-MM-DD',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        };
		$scope.datePicker4StatEnd = {
            singleDatePicker: true,
			maxDate:new Date(),
            locale: {
                format: 'YYYY-MM-DD',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        };
		$scope.datePicker4StatBill = {
            singleDatePicker: true,
			maxDate:new Date(),
			minDate:moment().add('year', -2),
            locale: {
                format: 'YYYY-MM-DD',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        };
		$scope.datePicker4StatBillEnd = {
            singleDatePicker: true,
			maxDate:new Date(),
			minDate:moment().add('year', -2),
            locale: {
                format: 'YYYY-MM-DD',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        };
		$scope.monthPicker4Stat = {
			singleDatePicker: true,
			maxDate:new Date(),
			isYearViews:true,
            locale: {
                format: 'YYYY-MM',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            }
        };
		$scope.monthPicker4StatBill = {
            singleDatePicker: true,
			maxDate:new Date(),
			minDate:moment().add('year', -2), 
			isYearViews:true,
            locale: {
                format: 'YYYY-MM',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
               firstDay: 1
				
            }
        };

		$scope.monthPicker4StatEnd = {
            singleDatePicker: true,
			maxDate:new Date(),
			isYearViews:true,
            locale: {
                format: 'YYYY-MM',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        };
		$scope.monthPicker4StatBillEnd = {
            singleDatePicker: true,
			maxDate:new Date(),
			minDate:moment().add('year', -2),
			isYearViews:true,
            locale: {
                format: 'YYYY-MM',
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '起始时间',
                toLabel: '结束时间',
                customRangeLabel: '自定义',
                daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                firstDay: 1
            }
        };
        String.prototype.replaceAll = function(token, newToken){
            var str, i = -1;
            if((str = this.toString()) && typeof(token) === "string") {
                while((i = str.indexOf(token, i >= 0 ? i + newToken.length : 0)) !== -1 ) {
                    str = str.substring(0, i).concat(newToken).concat(str.substring(i + token.length));
                }
            }
            return str;
        };
        Date.prototype.Format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份 
                "d+": this.getDate(), //日 
                "h+": this.getHours(), //小时 
                "m+": this.getMinutes(), //分 
                "s+": this.getSeconds(), //秒 
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
                "S": this.getMilliseconds() //毫秒 
            };
            if (/(y+)/.test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            }
            return fmt;
        };
    });

});
