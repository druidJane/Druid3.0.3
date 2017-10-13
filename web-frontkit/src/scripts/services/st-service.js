/**
 * Created by gdy on 2017/6/14.
 * 该st-service主要是使用于短彩信中发送记录、发送详情时
 * 有相同的逻辑是，进行相同的处理的位置,
 * st是sendtracking的缩写，中文名称：发送跟踪
 * 在这里我们进行规范，对于table中的参数都称为stParams
 */
define(["app"], function (app) {

    var StService = function (util, repo, NgTableParams, mmsService) {

        var stConfig = {
            showTplUrl:'mmsmgmt/template/modal/template.html',
        }

        //region 通过信息类型获取所有的通道
        var getChannelData = function (scope, msgType) {
            return repo.post('common/getChannelByBiztype', {msgType: msgType}).then(function (data) {
                scope.channel = data.data;
            })
        }
        //endregion

        //region 时间初始化和时间转换相关
        //region 初始化显示单天控件数据(起始：00:00:00 截止 ：23:59:59)
        var initOneDay = function (scope) {
            var todayStr = new Date();
            scope.queryTerm = {
                postTime: todayStr.Format("yyyy-MM-dd"),
                begin: new Date(todayStr.setHours(0,0,0)),
                end: new Date(todayStr.setHours(23,59,59))
            };
            scope.stParams.beginTime = scope.queryTerm.begin;
            scope.stParams.endTime = scope.queryTerm.end;
            $(function () {
                $('input[name="postTime"]').daterangepicker(scope.datePicker);
            });
        }
        //endregion

        //region 初始化跨月时间控件
        var initOneMonth = function (scope) {
            if(angular.isDefined(scope.stParams.monthFirstDate)&&scope.stParams.monthFirstDate){
                var first = new Date();
                first.setDate(1);
                scope.stParams.beginTime = first.Format("yyyy-MM-dd ") + "00:00:00";
            }else{
                scope.stParams.beginTime = new Date().Format("yyyy-MM-dd ") + "00:00:00";
            }
            scope.stParams.endTime = new Date().Format("yyyy-MM-dd ") + "23:59:59";
        }
        //endregion

        //region 将cst时间转换为字符串传到服务端
        var formatSingleDay = function (scope) {
            var begin = scope.queryTerm.begin.Format("hh:mm:ss");
            var end = scope.queryTerm.end.Format("hh:mm:ss");
            scope.stParams.beginTime = scope.queryTerm.postTime + " " + begin;
            scope.stParams.endTime = scope.queryTerm.postTime + " " + end;
        };
        //endregion
        //endregion

        //region 自动补全相关
        //region 完成部门自动填充
        var autoCompleteDept = function (scope) {
            var deptUrl = "common/complete/fetchDepartmentInfo?deptName=";
            if (angular.isDefined(scope.selectedObj.deptUrl)) {
                deptUrl = scope.selectedObj.deptUrl;
            }
            scope.completeDept = {
                deptName: '',
                sendDept: undefined,
                deptPath: xpath.service(deptUrl)
            }
        }
        //endregion

        //region 完成用户自动填充
        var autoCompleteUser = function (scope) {
            var userUrl = "common/complete/fetchUserData?userName=";
            if (angular.isDefined(scope.selectedObj.userUrl)) {
                userUrl = scope.selectedObj.userUrl;
            }
            scope.completeUser = {
                userName : '',
                sendUser : undefined,
                userPath:xpath.service(userUrl)
            }
        }
        //endregion
		
		//region 完成计费账户自动填充
        var autoCompleteBill = function (scope) {
            var billUrl = "common/complete/fetchUserData?userName=";
            if (angular.isDefined(scope.selectedObj.billUrl)) {
                billUrl = scope.selectedObj.billUrl;
            }
            scope.completeBill = {
                accountName : '',
                billAccount : undefined,
                billPath:xpath.service(billUrl)
            }
        }
        //endregion

        //region 检测是否选择了用户
        var checkSelectedUser = function (scope) {
            if (angular.isDefined(scope.completeUser.sendUser)) {
                scope.stParams.userId = scope.completeUser.sendUser.originalObject.id;
            } else {
                scope.stParams.userId = null;
                // 如果是手动填写了用户，
                if (scope.completeUser.userName !== '') {
                    util.operateInfoModel('', "请选择正确的发送用户！");
                    return false;
                }
            }
            return true;
        }
        //endregion
		
		//region 检测是否选择了计费账户
        var checkSelectedBill = function (scope) {
            if (angular.isDefined(scope.completeBill.billAccount)) {
                scope.stParams.accountId = scope.completeBill.billAccount.originalObject.id;
            } else {
                scope.stParams.accountId = null;
                // 如果是手动填写了用户，
                if (scope.completeBill.accountName !== '') {
                    util.operateInfoModel('', "请选择正确的计费账户！");
                    return false;
                }
            }
            return true;
        }
        //endregion

        //region 检测是否选择了部门
        var checkSelectedDept = function (scope) {
            if (angular.isDefined(scope.completeDept.sendDept)) {
                scope.stParams.deptId = scope.completeDept.sendDept.originalObject.id;
                scope.stParams.path = scope.completeDept.sendDept.originalObject.path;
            } else {
                scope.stParams.deptId = null;
                scope.stParams.path = null;
                if (scope.completeDept.deptName !== '') {
                    util.operateInfoModel('', "请选择正确的发送部门！");
                    return false;
                }
            }
            return true;
        }
        //endregion
        //endregion

        //region 检查st中的参数校验是否正确
        /**
         * @param scope
         * @param params {user:true,dept:true}]
         * user:true 表示有自动补全用户功能
         * dept:true 表示有自动补全部门功能
         * @param dataType 检验日期类型，day 表示检验是否在一天之内，month表示是否在一个月之内
         * @returns {boolean}
         */

        var checkStCondition = function (scope, params) {
            // 当前的页面中是否有自动补全用户功能
            // && 是否选择了列表中的用户
            if (params.user && !checkSelectedUser(scope)) {
                return false;
            }

            // 当前的页面中是否有自动补全部门功能
            // && 是否选择了列表中的部门
            if (params.dept && !checkSelectedDept(scope)) {
                return false;
            }

            if (params.data == "day") {
                formatSingleDay(scope);
            }
            // 是否超过限制时间
			
			
			if (!util.checkTimeRange(scope.stParams.beginTime, scope.stParams.endTime, params.data)) {
				return false;
			}
			
            
            return true;
        }
		 //region 检查用户统计页面st中的参数校验是否正确
        /**
         * @param scope
         * @param params {user:true,dept:true}]
         * user:true 表示有自动补全用户功能
         * dept:true 表示有自动补全部门功能
         * @returns {boolean}
         */

		var checkStCondition4UserStat = function (scope, params) {
            // 当前的页面中是否有自动补全用户功能
            // && 是否选择了列表中的用户
            if (params.user && !checkSelectedUser(scope)) {
                return false;
            }

            // 当前的页面中是否有自动补全部门功能
            // && 是否选择了列表中的部门
            if (params.dept && !checkSelectedDept(scope)) {
                return false;
            }
            
            return true;
        }
		//region 检查计费账户统计页面st中的参数校验是否正确
        /**
         * @param scope
         * @param params {user:true,dept:true}]
         * user:true 表示有自动补全用户功能
         * dept:true 表示有自动补全部门功能
         * @returns {boolean}
         */

		var checkStCondition4Bill = function (scope, params) {
            // 当前的页面中是否有自动补全用户功能
            // && 是否选择了列表中的用户
            if (params.bill && !checkSelectedBill(scope)) {
                return false;
            }

            
            return true;
        }
        //endregion

        //region 彩信检核详情
        var checkDetail = function (scope, conf, pack, url) {
            util.commonModal(scope, "检核详情", conf.checkPackUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    selectedItem: 1,
                    totalTickets:pack.totalTickets,
                    filterTickets:pack.filterTickets,
                };

                //region 检核table
                mScope.checkPackTable = new NgTableParams(
                    {
                        count: 10,
                        page: 1
                    },
                    {
                        total: 0,
                        getData: function (params) {
                            if (mScope.params.selectedItem == 0) {
                                // 全部核检项目
                                return repo.queryByUrl(conf.allCheckUrl, conf, util.buildQueryParam(params, pack)).then(function (data) {
                                    params.total(data.total);
                                    return data.data;
                                })
                            } else {
                                // 过滤核检项目
                                return repo.queryByUrl(conf.filterCheckUrl, conf, util.buildQueryParam(params, pack)).then(function (data) {
                                    params.total(data.total);
                                    return data.data;
                                })
                            }
                        }
                    }
                );
                //endregion

                //region 显示全部核检项目
                mScope.checkAllPhones = function () {
                    mScope.params.selectedItem = 0;
                    mScope.checkPackTable.reload();
                };
                //endregion

                //region 显示过滤核检项目
                mScope.checkFilterPhones = function () {
                    mScope.params.selectedItem = 1;
                    mScope.checkPackTable.reload();
                };
                //endregion

                mScope.mmsPreviewByPackId = function () {
                    var tempParams = {
                        postTime: pack.postTime,
                        packId: pack.packId
                    }
                    mmsService.showMmsPack(mScope, url, stConfig.showTplUrl, conf, tempParams);
                }

                //region 检核详情确定键
                mScope.okBtn = {
                    hide: true
                }
                //endregion
            });
        }
        //endregion

        //region 根据信息类型获取所有的通道
        var getChannelByMsgId = function (scope, msgType,channelUrl) {
		    var url = 'common/getChannelByBiztype';
		    if (angular.isDefined(channelUrl)) {
		        url = channelUrl;
            }
            repo.post(url, {msgType: msgType}).then(function (data) {
                scope.channel = data.data;
            });
        }
        //endregion

        var checkReloadFlag = function (scope,params) {
            if (angular.isDefined(scope.reloadFlag)&& scope.reloadFlag == 1) {
                scope.reloadFlag = 0
                params.page(1);
            }
        }

        var setReloadFlag = function (scope) {
            scope.reloadFlag = 1;
        }

        return {
            initOneDay: initOneDay,
            checkDetail: checkDetail,
            initOneMonth: initOneMonth,
            setReloadFlag:setReloadFlag,
            getChannelData: getChannelData,
            checkReloadFlag:checkReloadFlag,
            formatSingleDay: formatSingleDay,
            checkStCondition: checkStCondition,
            autoCompleteDept: autoCompleteDept,
            autoCompleteUser: autoCompleteUser,
            autoCompleteBill: autoCompleteBill,
            getChannelByMsgId: getChannelByMsgId,
            checkSelectedUser: checkSelectedUser,
            checkSelectedDept: checkSelectedDept,
            checkStCondition4Bill: checkStCondition4Bill,
            checkStCondition4UserStat: checkStCondition4UserStat,
        };
    };
    app.factory("StService", ["utilService", "repoService", "NgTableParams","MmsService", StService]);
});
