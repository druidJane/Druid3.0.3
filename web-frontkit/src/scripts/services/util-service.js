define(["app"], function(app) {
    var utilService = function($rootScope, $modal, $location, $q,$filter) {

        var confirmScope = $rootScope.$new();

		var initdrop = function(a){

			$(".modal").draggable();
			$(".modal-dialog").css("overflow", "hidden");

			if(typeof a!="undefined"){
				if(eval(a)){
					$(".modal").draggable("enable");
					$(".modal-dialog").css("overflow", "hidden");
				}
				else{
					$(".modal").draggable("disable");
					$(".modal-dialog").css("overflow", "hidden");
				}
			}
		};

        var selectedItems = function(items, idKey) {
            var rets = [];
            var hasKey = angular.isDefined(idKey);
            if (angular.isDefined(items)) {
                angular.forEach(items, function(o) {
                    if (o.$checked == true) {
                        if (hasKey) {
                            rets.push(o[idKey]);
                        } else {
                            rets.push(o);
                        }
                    }
                });
            }
            return rets;
        };

        var buildQueryParam = function(tblParam, otherParam) {
            return {
                count: tblParam.count(),
                page: tblParam.page(),
                sorts: tblParam.sorting(),
                params: otherParam
            }
        };

        var handleMulti = function(obj) {
            // console.log(obj);
            for (e in obj) {
                var attr = obj[e];
                if (attr.ref && attr.checks) { // handle ref-1-n
                    for (c in attr.checks) {
                        if (attr.checks[c]) {
                            attr[c] = attr.ref[c]
                        }
                    }
                    delete attr.ref;
                    delete attr.checks;
                }
            }
            // console.log(obj);
            return obj;
        };

        var hideModal = function(modal) {
            if (modal == undefined) {
                if ($(".modal").length > 0) {
                    $(".modal").remove();
                    $(".modal-backdrop").remove();
                    $("body").removeClass("modal-open modal-with-am-fade");
                }
            } else {
                modal.$promise.then(modal.hide);
            }
        };

        var commonModal = function(parentScope, title, contentUrl, init) {
            var modal = $modal({
                scope: parentScope,
                title: title,
                templateUrl: "modal/common.tpl.html",
                contentTemplate: "views/" + contentUrl,
                show: false,
                backdrop: "static"
            });
			var scope = modal.$scope;
            modal.$promise.then(function() {
                init(modal);
                modal.show();				
            }); 
            scope.$on("modal.hide", function() {
                scope.$destroy();						
            });
			scope.toggle = function(obj) {
					obj.draggable = !obj.draggable;
					initdrop(obj.draggable);
			}
        };
        var isEmpty = function (value) {
            return angular.isUndefined(value) || value === '' || value === null || value !== value || typeof value == 'string' && value.trim().length == 0||value.length == 0;
        }
        var commonModalWithCtrl = function (parentScope, title, contentUrl,init) {
            var modal = $modal(
                {
                    scope: parentScope,
                    title: title,
                    templateUrl: "modal/common.tpl.html",
                    contentTemplate: "views/" + contentUrl,
                    show: false,
                    backdrop: "static"
                });
	
			var scope = modal.$scope;
            modal.$promise.then(function () {
                init(modal);
                modal.show();				
            });           
            scope.$on("modal.hide", function () {
                scope.$destroy();
            });
			scope.toggle = function(obj) {
					obj.draggable = !obj.draggable;
					initdrop(obj.draggable);
			}
        };

        /**
         * 操作提示框
         * @param parentScope
         * @param info 要显示的消息
         */
        var operateInfoModel = function (parentScope, info, title) {
            var realTitle = "操作提示";
            if (angular.isDefined(title)) {
                realTitle = title;
            }
            var modal = $modal(
                {
                    scope: parentScope,
                    title: realTitle,
                    templateUrl: "modal/common.tpl.html",
                    contentTemplate: "views/_modal/infoAlert.html",
                    show: false,
                    backdrop: "static"
                });

            var scope = modal.$scope;
            modal.$promise.then(function () {
                scope.closeBtn = {
                    hide: true
                };
                scope.okBtn = {
                    text: "确定",
                    click: function () {
                        hideModal(modal);
                    }
                };
				scope.toggle = function(obj) {
					obj.draggable = !obj.draggable;
					initdrop(obj.draggable);
				}
                scope.params = {
                    message: info
                };
                // init(modal);				
                modal.show();
            });
            scope.$on("modal.hide", function () {
                scope.$destroy();
            });
        };

        /**
         * 操作提示框，可以自定义确定和关闭按钮后的动作处理
         * @param parentScope
         * @param info 要显示的消息
         */
        var operateInfoCustomerModel = function (parentScope, info, init) {
            var modal = $modal(
                {
                    scope: parentScope,
                    title: "操作提示",
                    templateUrl: "modal/common.tpl.html",
                    contentTemplate: "views/_modal/infoAlert.html",
                    show: false,
                    backdrop: "static"
                });
            var scope = modal.$scope;
            modal.$promise.then(function () {
                init(modal);				
                scope.params = {};
                scope.params.message = info;
                // init(modal);
                modal.show();
				scope.toggle = function(obj) {
					obj.draggable = !obj.draggable;
					initdrop(obj.draggable);
				}
            });
            scope.$on("modal.hide", function () {
                scope.$destroy();
            });
        };

        var commonOptinal = function(parentScope, title, contentUrl, init) {
            var modal = $modal({
                scope: parentScope,
                title: title,
                templateUrl: "modal/commonOptinal.tpl.html",
                contentTemplate: contentUrl,
                show: false,
                backdrop: "static"
            });
			var scope = modal.$scope;
            modal.$promise.then(function() {
                init(modal);				
                modal.show();
				scope.toggle = function(obj) {
					obj.draggable = !obj.draggable;
					initdrop(obj.draggable);
				}
            });
            
            scope.$on("modal.hide", function() {
                scope.$destroy();
            });
        };

        var htmlModal = function(parentScope, title, tempId, init) {
            var modal = $modal({
                scope: parentScope,
                title: title,
                templateUrl: "modal/common.tpl.html",
                contentTemplate: tempId,
                html: true,
                show: false,
                backdrop: "static"
            });
            modal.$promise.then(function() {
                modal.show();
                init(modal);
            });
            var scope = modal.$scope;
            scope.$on("modal.hide", function() {
                scope.$destroy();				
            });
			scope.toggle = function(obj) {
					obj.draggable = !obj.draggable;
					initdrop(obj.draggable);
			}
        };

        var originalModal = function(parentScope, title, tempId, init) {
            var modal = $modal(
                {
                    scope: parentScope,
                    title: title,
                    templateUrl: "modal/original.tpl.html",
                    contentTemplate: tempId,
                    html: true,
                    show: false,
                    backdrop: "static"
                });
            modal.$promise.then(function () {
                init(modal);				
                modal.show();
            });
            var scope = modal.$scope;
            scope.$on("modal.hide", function () {
                scope.$destroy();
            });
			scope.toggle = function(obj) {
					obj.draggable = !obj.draggable;
					initdrop(obj.draggable);
				}
        };

        var procModal = function(parentScope, title) {
            var modal = $modal({
                scope: parentScope,
                title: title,
                templateUrl: "modal/process.tpl.html",
                show: false,
                backdrop: "static"
            });
            var scope = modal.$scope;
            modal.$promise.then(function() {
                scope.okBtn = {
                    click: function() {
                        hideModal(modal)
                    }
                }				
                modal.show();
            });
            scope.$on("modal.hide", function() {
                scope.$destroy();
            });
			scope.toggle = function(obj) {
					obj.draggable = !obj.draggable;
					initdrop(obj.draggable);
				}
            return modal;
        };

        var confirmModal = function(content, okFn) {
            var modal = $modal({
                scope: confirmScope,
                content: content,
                templateUrl: "modal/confirm.tpl.html",
                show: true,
                backdrop: "static"
            });
            var scope = modal.$scope;
            scope.$on("modal.hide", function() {
                scope.$destroy();
            });
            scope.okBtn = {
                click: function() {
                    okFn();
                    hideModal(modal);
                }
            }
        };

        var confirmHashCancelModal = function(content, okFn, cancelBtn) {
            var modal = $modal({
                scope: confirmScope,
                content: content,
                templateUrl: "modal/confirm_has_cancel.tpl.html",
                show: true,
                backdrop: "static"
            });
            var scope = modal.$scope;
            scope.$on("modal.hide", function() {
                scope.$destroy();
            });
            scope.okBtn = {
                click: function() {
                    okFn();
                    hideModal(modal);
                }
            }
            scope.cancelBtn = {
                click: function() {
                    if (cancelBtn != undefined) {
                        cancelBtn();
                    }
                    hideModal(modal);
                }
            }
        };

        var toDate = function toDate(dateStr) {
            if (dateStr == undefined) {
                return null;
            }
            var arr = dateStr.substring(0, 10).split("-");
            var time = arr[1] + "/" + arr[2] + "/" + arr[0] + dateStr.substring(10, 19);
            return Date.parse(time);
        };

        var showProc = function showProc($scope, title) {
            var modal = procModal($scope, title + '进度');
            var mScope = modal.$scope;
            var proc = mScope.proc = {
                msgs: ['正在' + title + '，请稍候...']
            };
            //get base URL
            var url = $location.absUrl();
            var idx = url.indexOf('#', 0);
            if (idx > 0) {
                url = url.substring(0, idx);
            }
            var defer = $q.defer();
            var sock = new SockJS(addContext(url + 'sock')); //close by server
            sock.onopen = function() {
                defer.resolve(modal);
            };
            sock.onmessage = function(e) {
                proc.msgs.push(e.data);
                mScope.$apply();
            };
            return defer.promise;
        };

        var isRespOK = function(resp) {
            if (angular.isDefined(resp) && resp.status == 0) {
                return true;
            }
            return false;
        };

        var clearObjAttr = function(obj) {
            if (obj) {
                angular.forEach(obj, function(value, key) {
                    delete obj[key];
                });
            }
        };

        var clearArray = function(array) {
            if (array && array.length > 0) {
                var len = array.length;
                for (var i = 0; i < len; i++) {
                    array.pop();
                }
            }
        };

        var getFromArray = function(arrays, key, value) {
            if (arrays && key && value) {
                var len = arrays.length;
                for (var i = 0; i < len; i++) {
                    if (arrays[i][key] == value) {
                        return arrays[i];
                    }
                }
            }
        };
        var getUUID = function() {
            return UUID.prototype.createUUID();
        };

        //region 判断彩信中的附件是否为空
        var isEmptyFile = function (object) {
            if (angular.isDefined(object)) {
                if (object.name != "" && object.size >0){
                    return true;
                }
            }
            return false;
        }
        //endregion

        function UUID() {
            this.id = this.createUUID();
        };

        UUID.prototype.valueOf = function() {
            return this.id;
        };
        UUID.prototype.toString = function() {
            return this.id;
        };
        UUID.prototype.createUUID = function() {
            var dg = new Date(1582, 10, 15, 0, 0, 0, 0);
            var dc = new Date();
            var t = dc.getTime() - dg.getTime();
            var tl = UUID.getIntegerBits(t, 0, 31);
            var tm = UUID.getIntegerBits(t, 32, 47);
            var thv = UUID.getIntegerBits(t, 48, 59) + '1'; // version 1, security version is 2
            var csar = UUID.getIntegerBits(UUID.rand(4095), 0, 7);
            var csl = UUID.getIntegerBits(UUID.rand(4095), 0, 7);
            var n = UUID.getIntegerBits(UUID.rand(8191), 0, 7) +
                UUID.getIntegerBits(UUID.rand(8191), 8, 15) +
                UUID.getIntegerBits(UUID.rand(8191), 0, 7) +
                UUID.getIntegerBits(UUID.rand(8191), 8, 15) +
                UUID.getIntegerBits(UUID.rand(8191), 0, 15); // this last number is two octets long
            return tl + tm + thv + csar + csl + n;
        };
        UUID.getIntegerBits = function(val, start, end) {
            var base16 = UUID.returnBase(val, 16);
            var quadArray = new Array();
            var quadString = '';
            var i = 0;
            for (i = 0; i < base16.length; i++) {
                quadArray.push(base16.substring(i, i + 1));
            }
            for (i = Math.floor(start / 4); i <= Math.floor(end / 4); i++) {
                if (!quadArray[i] || quadArray[i] == '') quadString += '0';
                else quadString += quadArray[i];
            }
            return quadString;
        };

        UUID.returnBase = function(number, base) {
            return (number).toString(base).toUpperCase();
        };

        UUID.rand = function(max) {
            return Math.floor(Math.random() * (max + 1));
        };

        var selectAll = function(table, value) {
            angular.forEach(table.data, function(o) {
                o.$checked = value;
            });
        };
        var checkOne = function(s, table, o) {
            o.$checked = o.$checked ? false : true;
            var selected = 0;
            angular.forEach(table.data, function(o) {
                if (o.$checked) {
                    selected++;
                    s.lastSelected = angular.copy(o);
                };
            });
            if (selected == 0) {
                s.checked = false;
            } else if (selected == table.data.length) {
                s.checked = true;
            }
        };

        var isIpAddress = function(str) {
            var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
            return reg.test(str);
        };


        /**
         * 判断targetStr中是否包含了testStr
         * @param targetStr
         * @param testStr
         * @returns {boolean} 返回true表示包含，false表示不包含
         */
        var containerStr = function (targetStr, testStr) {
            var result = targetStr.search(testStr);
            var isContainer = false;
            if (result > -1) {
                isContainer = true;
            }
            return isContainer;
        };
        /**
         * json格式转树状结构
         * @param   {json}      json数据
         * @param   {String}    id的字符串
         * @param   {String}    父id的字符串
         * @param   {String}    children的字符串
         * @return  {Array}     数组
         */
        var transDataToTree = function(data, idStr, pidStr, chindrenStr) {
            var r = [],
                hash = {},
                id = idStr,
                pid = pidStr,
                children = chindrenStr,
                i = 0,
                j = 0,
                len = data.length;
            for (; i < len; i++) {
                hash[data[i][id]] = data[i];
            }
            for (; j < len; j++) {
                var aVal = data[j],
                    hashVP = hash[aVal[pid]];
                if (hashVP) {
                    !hashVP[children] && (hashVP[children] = []);
                    hashVP[children].push(aVal);
                } else {
                    r.push(aVal);
                }
            }
            return r;
        };
        /**
         * n叉树中某个节点的查找
         * @param   {stack}  到该节点的路径
         * @param   {id}    id的字符串
         * @param   {root}  根节点
         * @return  {selected}   返回查找到的节点
         */
        var findChild = function(stack, id, root) {
            stack.push(root);
            if (root.id == id) {
                stack.pop();
                return root;
            } else if (root.children == undefined) {
                stack.pop();
            } else {
                for (var i = root.children.length - 1; i >= 0; i--) {
                    var selected = findChild(stack, id, root.children[i]);
                    if (selected != undefined) {
                        return selected;
                    }
                }
                stack.pop();
            }
        };
        var getExportFileName = function(fileType) {
            if (fileType == 'excel') {
                return "export.xls";
            }

            if (fileType == 'txt') {
                return "export.txt";
            }

            if (fileType == "csv") {
                return "export.csv"
            }
        };
        var buildCommonReq = function(param) {
            return {
                params: param
            }
        };

        // json 2 obj
        var fromJson = function (param) {
            return angular.fromJson(param);
        };
    
        // obj 2 json
        var toJson = function (param) {
            return angular.toJson(param);
        };
    
        var deepCopy = function (sourceObj, destinationObj) {
            return angular.copy(sourceObj,destinationObj);
        };
        
        var extendObj = function (destObject, sourceObj1, sourceObj2) {
            return angular.extend(destObject,sourceObj1,sourceObj2);
        };

        /**
         * 获取格式化的当年的某个时间节点
         * @param month 月份(一月份为0，二月份为1，以此类推)
         * @param day 月份中的天数，从1开始
         * @param hour 小时
         * @param minute 分钟
         * @param second 秒
         * @param format 格式化的形式
         * @returns {*}
         */
        var getTime = function (month,day,hour,minute,second,format) {
            var date = new Date();
            // 设置月
            if (month != -1) {
                date.setMonth(month);
            }
            // 设置天
            if ( day != -1){
                date.setDate(day);
            }
            // 设置小时
            if (hour != -1 && minute != -1 && second != -1) {
                date.setHours(hour,minute,second);
            }
            format = format||'yyyy-MM-dd HH:mm:ss';
            return $filter('date')(date,format );
        };

        //region 测试sourceStr是不是以targetStr开头的
        /**
         * @param sourceStr 待检测的字符串
         * @param targetStr 目标字符串
         * @returns {boolean} 如果是返回true,否则返回false
         */
        var startWith = function (sourceStr, targetStr) {
            var flag = new RegExp('^' + targetStr).test(sourceStr)
            return flag;
        }
        //endregion

        //region 测试sourceStr是不是以targetStr结尾的
        /**
         * @param sourceStr 待检测的字符串
         * @param targetStr 目标字符串
         * @param noCaseSensitive 传入该值，正则表达式匹配是不区分大小
         * @returns {boolean} 如果是返回true,否则返回false
         */
        var endWith = function (sourceStr, targetStr, noCaseSensitive) {
            var regex;
            if (angular.isDefined(noCaseSensitive) && (noCaseSensitive == true)) {
                regex = new RegExp(targetStr + '$', "i");
            } else {
                regex = new RegExp(targetStr + '$');
            }
            var flag = regex.test(sourceStr);
            return flag;
        }
        //endregion

        //region 页面上时间控件的时间检验
        /**
         * @param beginTime 起始时间--字符串
         * @param endTime   结束时间--字符串
         * @param type  查询时间跨度类型：day ：时间跨度为天，month ：时间跨度为月 --字符串
         * 其他的只检查开始时间到结束时间
         * @returns {boolean} 符合验证返回true，不符合验证返回false
         */
        var checkTimeRange = function (beginTime, endTime, type) {
            //region 保证开始时间必须要小于结束之间
            if (moment(endTime).isBefore(moment(beginTime))) {
                operateInfoModel('', "开始时间必须要小于结束时间");
                return false;
            }
            //endregion

            // region 保证时间是否在一个判定的时间跨度之内 -- 1天，或是一个月等
            var durType = "";
            var typeName = "";
            if (type == "day") {
                // 保证时间跨度在一天内
                // 保证小于一个月计算方式： |endTime - beginTime| <= 1 (days)
                durType = "days";
                typeName = "天";
            } else if (type == "month") {
                // 保证时间跨度在一个月内
                // 保证小于一个月计算方式： |endTime - beginTime| <= 1 (month)
                durType = "months";
                typeName = "个月";
            } else {
                return true;
            }

            var defineDur = moment.duration(1, durType);
            var endDur = moment(endTime);
            var beginDur = moment(beginTime);
            var flag = endDur.isAfter(beginDur) && (endDur.subtract(defineDur)).isBefore(beginDur);
            flag = flag || endDur.isSame(beginDur);
            if (!flag) {
                operateInfoModel('', "时间跨度不能超过一" + typeName);
                return false;
            }
            //endregion
            return true;
        }
        //endregion

        // 彩信管理中需要在路径中读取到tmp目录下，单独部署一个服务器，用于图片显示
        // 这是因为springboot的使用了内置的tomcat服务器，因此无法类似传统模式在
        // 同级目录下，生成文件，供外界访问
        var imgUrl = xpath.imgUrl;

		var capitalTip = function(id){
			 
			var capital = false;
			var capitalTip = {  
				$elem: {hide:function(){},show:function(){},is:function(){}}, 
				init: function(){
					if($('#capital_'+id).length==0){
						$('#' + id).after('<div id="capital_'+id+'" class="capslock">大小写锁定已打开</div>'); 
						this.$elem = $('#capital_'+id);
					}
				},
				toggle: function (s) {  
					if(s === 'none'){  
						this.$elem.hide();  
					}else if(s === 'block'){  
						this.init();
						this.$elem.show();  
					}else if(this.$elem.is(':hidden')){  
						this.init();
						this.$elem.show();  
					}else{  
						this.$elem.hide();  
					   }  
				}  
			}  
			$('#' + id).on('keydown.caps',function(e){  
				if (e.keyCode === 20 && capital) {
					capitalTip.toggle();  
				}  
			}).on('focus.caps',function(){capital = false}).on('keypress.caps',function(e){capsLock(e)}).on('blur.caps',function(e){  				  
				capitalTip.toggle('none');  
			});  
			function capsLock(e){  
				var keyCode = e.keyCode || e.which; 
				var isShift = e.shiftKey || keyCode === 16 || false;
				if(keyCode === 9){  
					capitalTip.toggle('none');  
				}else{  
				  if (((keyCode >= 65 && keyCode <= 90) && !isShift) || ((keyCode >= 97 && keyCode <= 122) && isShift)) {  
					  capitalTip.toggle('block');  
					  capital = true;  
				  } else {  
					  capitalTip.toggle('none');  
					  capital = true;  
				  }  
				}  
			}  
		}

        return {
            imgUrl:imgUrl,
            toJson:toJson,
            toDate: toDate,
            endWith:endWith,
            getTime:getTime,
            getUUID: getUUID,
            deepCopy:deepCopy,
            fromJson:fromJson,
            checkOne: checkOne,
            showProc: showProc,
            isRespOK: isRespOK,
            extendObj:extendObj,
            startWith:startWith,
            selectAll: selectAll,
            hideModal: hideModal,
            htmlModal: htmlModal,
            procModal: procModal,
            findChild: findChild,
            confirm: confirmModal,
            clearArray: clearArray,
            isEmptyFile:isEmptyFile,
            handleMulti: handleMulti,
            isIpAddress: isIpAddress,
            containerStr:containerStr,
            commonModal: commonModal,
            clearObjAttr: clearObjAttr,
            getFromArray: getFromArray,
            originalModal:originalModal,
            selectedItems: selectedItems,
            commonOptinal: commonOptinal,
            buildCommonReq:buildCommonReq,
            checkTimeRange:checkTimeRange,
            transDataToTree: transDataToTree,
            buildQueryParam: buildQueryParam,
            operateInfoModel:operateInfoModel,
            getExportFileName:getExportFileName,
            commonModalWithCtrl:commonModalWithCtrl,
            confirmHasCancel: confirmHashCancelModal,
            operateInfoCustomerModel:operateInfoCustomerModel,
            isEmpty:isEmpty,
			capitalTip:capitalTip
        };
    };
    app.factory("utilService", ["$rootScope", "$modal", '$location', '$q', '$filter',utilService]);
});
