define(["app"], function(app) {
    var injectParams = ['$scope', '$location', '$routeParams', 'NgTableParams', 'repoService', 'utilService', 'modalService','paramService'];
    var biztypeAddCtrl = function($scope, $location, $routeParams,NgTableParams, repo, util, modal, ps) {
        $scope.priority = [
        {"id":4 ,name: "低"},
        {"id":3 ,name: "中"},
        {"id":2 ,name: "较高"},
        {"id":1 ,name: "最高"},
       ];
        $scope.mode = 'add';
        $scope.channelArray = [];
        $scope.carrierArray = [];
        $scope.bizType = {
            speedMode:1,
            priority:4,
            startTime : new Date().setHours(0,0,0),
            endTime : new Date().setHours(23,59,59)
        };
        if($routeParams.mode){
            $scope.mode = $routeParams.mode;
        }
        if($routeParams.id){
			$routeParams.id = ps.decode($routeParams.id);
            id = $routeParams.id;
        }
        if($scope.mode !== 'add'){
            repo.post($scope.urlPerMap.INFO_BIZTYPE_INDEX + "/" +
                $routeParams.id).then(function(data) {
                data.data.endTime = new moment('1970-01-01 ' + data.data.endTime, 'YYYY-MM-DD HH:mm:ss');
                data.data.startTime = new moment('1970-01-01 ' + data.data.startTime, 'YYYY-MM-DD HH:mm:ss');
                //data.data.startTime = "2000-1-1 " + data.data.startTime;
                $scope.bizType = data.data;
                angular.forEach($scope.bizType.carrierChannel,function (item) {
                    $scope.channelArray.push(item.id);
                    angular.forEach(item.canSendCarrier,function (carrier) {
                        $scope.carrierArray.push(item.id + "_" + carrier.id);
                    })
                })
            });
        }
        
        $scope.checkSpeed = function () {
            $scope.speedErrorMsg = "";
            if (util.isEmpty($scope.bizType.speed)) {
                $scope.speedErrorMsg = "值不能为空";
                return false;
            }
            if (/^([1-9][0-9]*|0{1,1})$/.test($scope.bizType.speed) == false) {
                $scope.speedErrorMsg = "输入格式不正确:必须为0或正整数";
                return false;
            }
            if ($scope.bizType.speed < 0 || $scope.bizType.speed > 2000) {
                $scope.speedErrorMsg = "值必须在0-2000之间";
                return false;
            }
            return true;
        };

        $scope.channelTable = new NgTableParams({
            page: 1,
            count: 1000
        }, {
            counts: [],
            getData: function(params) {
                return repo.queryByUrl($scope.urlPerMap.INFO_BIZTYPE_ADD+'/getChannel','', util.buildQueryParam(params, $scope.carrierDnSegParams)).then(function(data) {
                    params.total(data.total);
                    if($scope.mode == 'detail'){
                        $("#submit").hide();
                        var checkbox = $("#dataForm input[type='checkbox']");
                        angular.forEach(checkbox,function (o) {
                            o.disabled = true;
                        })
                    }
                    return data.data;
                });
            }
        });
        $scope.selectChannel = function (event,o) {
            var array = $("input[channelid="+o.channel.id+"]");
            angular.forEach(array,function (item) {
                item.checked = event.target.checked;
            })

        }
        $scope.selectAll = { checked: false };
        $scope.$watch('selectAll.checked', function(newValue,oldValue) {
            var data = $("#dataForm input[type='checkbox']");
            angular.forEach(data, function(o) {
                o.checked = newValue;
            });
            //util.selectAll($scope.channelTable, value);
        });
        $scope.isChannelSelected = function (o) {
            return $.inArray(o.channel.id,$scope.channelArray)!=-1;
        }
        $scope.isCarrierSelected = function (o) {
            return $.inArray(o.$parent.channel.id+"_"+o.carrier.id,$scope.carrierArray)!=-1;
        }
        var bizTypeConf = {
            locationUrl:'InformationMgr/BusinessType/Index',//页面URL
            url: 'InformationMgr/BusinessType', //基本URL，必填
            showName: '业务类型', //提示的名称，一般为模块名，必填
            updateUrl:$scope.urlPerMap.INFO_BIZTYPE_ADD
        };
        //$scope.timePicker.isAllowEmpty = true;
        //$('input[name="startTime"]').daterangepicker($scope.timePicker);
        //$scope.timePicker.startDate = new Date().setHours(23,59,59);
        //$('input[name="endTime"]').daterangepicker($scope.timePicker);
        //$('div[class="calendar-table"]').hide();
        $scope.submitBiztype = function(){
            var operator = '新增';
            if($scope.mode!=='add'){
                operator = '修改';
            }
            var submit = angular.copy($scope.bizType);
            submit.endTime = submit.endTime.Format("hh:mm:ss");
            submit.startTime = submit.startTime.Format("hh:mm:ss");
            repo.post(bizTypeConf.updateUrl,submit).then(function (resp) {
                if (resp.status == 0) {
                    toastr.success(operator + bizTypeConf.showName + "成功。");
                    $location.path(bizTypeConf.locationUrl);
                } else {
                    var msg = operator + "失败: " + resp.errorMsg;
                    toastr.error(msg);
                }
            }).catch(function(resp) {
                toastr.error(operator + $scope.bizType.showName + "失败: " + resp.errorMsg);
            });
        };
        //新增提交按钮
        $scope.submitForm = function() {
            var channelCarrier=[];
            var carrierId = $("input[name='carrierId']:checked");
            if(carrierId.length<1){
                toastr.error("请至少选择一个运营商!");
                return false;
            }
            if ($scope.checkSpeed($scope.bizType.speed) == false) {
                toastr.error("发送速度输入有误,请仔细核查!");
                return false;
            }
            var channelIds=[];
            var check = [[]];
            var valid = true;
            angular.forEach(carrierId,function (carrierItem,index) {
                if(carrierItem.checked){
                    var carrierId = carrierItem.value;
                    var price = carrierItem.attributes["price"].value;
                    var msgType = carrierItem.attributes["msgType"].value;
                    if(!util.isEmpty(check[carrierId])){
                        if(!util.isEmpty(check[carrierId][msgType])){
                            if(check[carrierId][msgType] != price){
                                valid =  false;
                            }
                        }
                    }else{
                        check[carrierId] = new Array();
                    }
                    check[carrierId][msgType] = price;
                    channelIds.push(carrierItem.attributes["channelid"].value);
                    /*var carrier = {id:carrierItem.value};
                    $scope.bizType.carrierChannel[index].canSendCarrier.push(carrier);*/
                    channelCarrier.push(carrierItem.attributes["spec_bind_id"].value+":"+carrierItem.value);
                }
            });
            if(!valid){
                toastr.error("单价不一致并且可发送运营商一致的端口号禁止同时分配给同一个业务类型!");
                return false;
            }
            $scope.bizType.extAttr1 = channelCarrier.join(",");
            if($scope.mode!=='add'){
                channelIds = $.unique(channelIds);
                bizTypeConf.updateUrl = $scope.urlPerMap.INFO_BIZTYPE_UPDATE +"?channelIds=" +channelIds.join(",");
                $scope.bizType.carrierChannel = [];
            }
            $scope.submitBiztype();
        };
    };
    biztypeAddCtrl.$inject = injectParams;
    app.register.controller('biztypeAddCtrl', biztypeAddCtrl);
});
