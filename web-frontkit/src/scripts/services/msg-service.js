/**
 * Created by gdy on 2017/6/2.
 * 该类主要编写在页面中多次进行调用的方法
 */
define(["app"], function(app) {

    var msgCfg = {
        infoTplUrl:'/_modal/infoAlert.html',
        remarkTplUrl:'/mmsmgmt/sendmms/modal/remark.html',
        addPhoneTplUrl:'/mmsmgmt/sendmms/modal/addPhone.html',
        contactTplUrl:'/mmsmgmt/sendmms/modal/contactPhone.html',
        invalidPhoneTplUrl:'/mmsmgmt/sendmms/modal/showInvalidPhones.html',
        clrContactTplUrl:'/mmsmgmt/sendmms/modal/resultImportContact.html',
        alertInvalidTplUrl:'/mmsmgmt/sendmms/modal/alertInvalidPhone.html',
        sendStatusTplUrl:'/_modal/msgResult.html', // 如果需要修改发送短彩信状态的模态框可以使用
        manualAddCount : 10000, // 在模态框中一次性手动添加号码的最大数目
        smsBatchDetail:'/smsmgr/sendTracking/sendRecord/recordDetail.html',
    }

    var MsgService = function(util, repo,NgTableParams,$interval,$location) {

        //region 使用于彩信/短信的通讯录
        /**
         * 注：该方法中改变parentScope中的值(parentScope.itemMainList)，因此传入需要
         * 该值的属性名为itemMainList，否则就你需要自己修改或增加代码来对你的parentScope进行适当的改变！！！！
         * @param parentScope 父级scope
         * @param description 要展示的模态框的头部显示字样
         * @param templateUrl 通讯录模板使用的templateUrl的位置
         * @param clearUrl 显示清空联系人使用的templateUrl的位置
         */
        var contactModal = function (parentScope, description,templateUrl,clearUrl) {
            util.commonModal(parentScope,description,msgCfg.contactTplUrl,function (modal) {
                var $mScope = modal.$scope;
                $mScope.diagCls = "modal-dialog-max";
                $mScope.showRightItemList = [];

                //region 公用方法
                //region 标识当前的选择是：个人通讯录为0，共享通讯录为1 -- 公用

                $mScope.checkState = {
                    type:-1
                }

                //endregion

                //region 显示最右侧的联系人 --公用
                $mScope.showRightItemListTable = new NgTableParams(
                    {
                        page:1,
                        count:10
                    },{
                        counts:[],
                        paginationMaxBlocks: 5,
                        paginationMinBlocks: 2,
                        total:0,
                        getData: function (params) {
                            //params.count = $mScope.contactPersonTable.count;
                            var page = params.page(); // 指的是我点过的页面
                            var data = $mScope.showRightItemList.slice(
                                ( page - 1) * params.count(), page * params.count());
                            if (data.length == 0 && $mScope.showRightItemList != 0) {
                                page = page - 1;
                                params.page(page);
                                data = $mScope.showRightItemList.slice(
                                    ( page - 1) * params.count(), page * params.count());
                            }
                            params.total($mScope.showRightItemList.length);
                            return data;
                        }
                    });
                //endregion

                //region 删除选中的联系人 -- 公用
                $mScope.deleteCurItem = function (index,params) {
                    var realIndex = ( params.page()-1)*(params.count())+index;
                    $mScope.showRightItemList.splice(realIndex,1);
                    $mScope.showRightItemListTable.reload();
                };
                //endregion

                //region 清空右侧联系人列表 -- 公用
                $mScope.clearNewListTree = function () {
                    util.commonModal($mScope,"清空联系人",msgCfg.clrContactTplUrl,function (modal) {
                        var $mmScope = modal.$scope;
                        $mmScope.diagCls = "modal-dialog-min";
                        $mmScope.params ={message:"确定要清空所有的联系人吗？"};
                        $mmScope.okBtn = {
                            text:'确定',
                            click:function () {
                                $mScope.showRightItemList = [];
                                $mScope.showRightItemListTable.reload();
                                util.hideModal(modal);
                            }
                        }

                    });
                };
                //endregion

                //region 添加全部按钮
                $mScope.addAllQueryResult = function () {
                    if (!angular.isDefined($mScope.selectedNode)) {
                        $mScope.selectedNode = personTreeOptions[0];
                    }
                    var path = $mScope.selectedNode.path;
                    //封装Path，q 查询条件，s 共享通讯录，g 个人通讯录
                    if ($mScope.selectedNode.type == 2) {
                        path = 's' + path;
                    } else {
                        path = 'g' + path;
                    }
                    if (!$mScope.personParams.showChild) {
                        path += "#";
                    }
                    if(!util.isEmpty($mScope.queryParams)){
                        $mScope.queryParams.path = $mScope.selectedNode.path;
                        path = 'q' + path.charAt(0) +  angular.toJson($mScope.queryParams);
                    }

                    if ($mScope.selectedNode.name.length > 7) {
                        var text = $mScope.selectedNode.name.substr(0, 7)+"......";
                    } else {
                        var text = $mScope.selectedNode.name;
                    }
                    var contact = {
                        value: path,
                        text: text + "(" + $mScope.contactPersonTable.total() + ")",
                        count: $mScope.contactPersonTable.total(),
                        contactData: $mScope.selectedNode.contactData,
                        varParams : ['姓名','手机号码', '性别', '出生日期',  '编号','VIP', '备注']
                    }

                    if ($mScope.checkState.type == 0) {
                        if (util.isEmpty($mScope.selectedNode.contactData)) {
                            util.operateInfoModel($mScope, "该群组无联系人数据，无法添加！");
                            return;
                        }
                    } else {
                        if (angular.isDefined($mScope.selectedNode.showContact)) {
                            if ($mScope.selectedNode.showContact == false) {
                                // 隐藏的通讯录群组
                                contact = {
                                    value: path, // 最重要的是要把path传到后台给后台解析
                                    text: $mScope.selectedNode.name,
                                    count: $mScope.selectedNode.childCount, // 不重要，甚至可以不传
                                    contactData: $mScope.selectedNode.contactData // // 不重要，甚至可以不传
                                }
                            } else {
                                // 非隐藏的通讯录群组
                                if (util.isEmpty($mScope.selectedNode.contactData)) {
                                    util.operateInfoModel($mScope, "该群组无联系人数据，无法添加！");
                                    return;
                                }
                            }
                        } else {
                            util.operateInfoModel('', '通讯录异常');
                        }
                    }

                    $mScope.showRightItemList.push(contact);
                    $mScope.showRightItemListTable.reload();
                };
                //endregion

                //region 将左侧的号码添加到右侧栏--没有去重 -- 公用
                $mScope.addQueryResult = function () {
                    var selectedAllItems = util.selectedItems($mScope.contactPersonTable.data);
                    if (selectedAllItems.length <= 0) {
                        util.operateInfoModel($mScope, "请先选择要添加的联系人");
                        return;
                    }
                    var selectAllItemLength = selectedAllItems.length;
                    for (var index = 0; index < selectAllItemLength; index++) {
                        var curItem = selectedAllItems[index];
                        var contact = {
                            value:curItem.id,
                            text : curItem.phone,
                            count : 1,
                            contactData : [selectedAllItems[index]],
                            varParams : ['姓名','手机号码', '性别', '出生日期',  '编号','VIP', '备注']
                        }
                        $mScope.showRightItemList.push(contact);
                    }
                    $mScope.showRightItemListTable.reload();
                };
                //endregion
                //region 个人通讯录的单选框 --公用
                $mScope.selectAll = {
                    checked: false
                };
                //endregion

                //region 监听$mScope.selectShareAll 节点的check事件 -共享
                $mScope.$watch('selectAll.checked', function (value) {
                    util.selectAll($mScope.contactPersonTable, value);
                });
                //endregion
                //endregion

                //region 个人通讯录
                //region 加载个人选项卡
                $mScope.loadPersonTable = function () {
                    $mScope.checkState.type = 0;
                    $mScope.refreshContact();
                    $mScope.refreshPersonGroup();;
                }
                //endregion

                //region 个人通讯录表头 -- 个人
                $mScope.personCols = [{
                    title: "姓名",
                    show: true
                }, {
                    title: "手机号码",
                    show: true
                }, {
                    title: "所属组",
                    show: true
                }, {
                    title: "性别",
                    show: true
                }, {
                    title: "出生日期",
                    show: true
                }, {
                    title: "编号",
                    show: true
                }, {
                    title: "VIP",
                    show: true
                }, {
                    title: "备注",
                    show: true
                }];
                //endregion

                //region 个人通讯录查询参数 -- 个人
                $mScope.personParams = {
                    showChild: true,
                    vip : '-1',
                    sex : '-1',
                    type : 0,
                    groupId : 1,
                    startTime : ''
                };
                //endregion

                //region 将点击的个人通讯录树中的数据进行加载 --个人
                $mScope.showPersonSelected = function(node) {
                    $mScope.personParams.type = node.type;
                    $mScope.personParams.groupId = node.id;
                    $mScope.personParams.path = node.path;
                    $mScope.selectedNode = node;
                    $mScope.refreshContact();
                    $mScope.queryParams = undefined;
                };
                //endregion

                //region 加载右侧通讯录列表
                $mScope.refreshContact = function() {
                    var tempurl = {url:'common'};
                    var path = "/getContactByGroup";
                    if($mScope.checkState.type == 1){
                        tempurl = {url: 'common'};
                        path = "/getContactByEGroup";
                        $mScope.personParams.source = "message"
                    }
                    $mScope.contactPersonTable = new NgTableParams(
                        {
                            page : 1,
                            count : 10
                        }, {
                            total : 0,
                            getData : function (params) {
                                if ($mScope.checkState.type == 1
                                    && angular.isDefined($mScope.selectedNode.showContact)
                                    && $mScope.selectedNode.showContact == false) {
                                    params.total(0);
                                    return 0;
                                }
                                var query = angular.copy($mScope.personParams);
                                if(angular.isDefined(query._gt_beginDate) && query._gt_beginDate !== '')query._gt_beginDate += " 00:00:00";
                                if(angular.isDefined(query._lt_endDate) && query._lt_endDate !== '')query._lt_endDate += " 23:59:59";
                                return repo.queryByPath(tempurl, path, util.buildQueryParam(params, query)).then(function (data) {
                                    if(params.page() === 1){
                                        $mScope.selectedNode.contactData = data.data;
                                    }
                                    $mScope.showRightItemListTable.count = params.count;
                                    $mScope.showRightItemListTable.reload();
                                    params.total(data.total);
                                    return data.data;
                                });
                            }
                        });
                };
                //endregion

                //region 个人通讯录树结构 -- 个人
                $mScope.personTreeOptions = {
                    //子节点字段名称
                    nodeChildren: "children",
                    dirSelectable: true
                };
                //endregion

                //region 将获取的数据渲染为个人通讯录树 -- 个人
                $mScope.refreshPersonGroup = function() {
                    var tempUrl = {url:'common/getPTree'}
                    repo.post(tempUrl.url).then(function(data) {
                        $mScope.dataForThePersonTree = util.transDataToTree(data.data, 'id', 'parentId', 'children');
                        $mScope.expandedPersonNodes = [$mScope.dataForThePersonTree[0]];
                        if (angular.isDefined($mScope.dataForThePersonTree[0].children)
                        && $mScope.dataForThePersonTree[0].children!=null) {
                            for (var i = $mScope.dataForThePersonTree[0].children.length - 1; i >= 0; i--) {
                                $mScope.expandedPersonNodes.push($mScope.dataForThePersonTree[0].children[i]);
                            }
                        }
                        $mScope.selectedNode = $mScope.dataForThePersonTree[0];
                    });
                };
                //endregion

                //region 个人通讯录的单选  -- 个人
                $mScope.checkPersonOne = function (item) {
                    util.checkOne($mScope.selectAll, $mScope.contactPersonTable, item);
                };
                //endregion
                //endregion

                //region 共享通讯录
                //region 加载共享选项卡
                $mScope.loadShareTable = function () {
                    $mScope.checkState.type = 1;
                    $mScope.refreshContact();
                    $mScope.refreshShareGroup();
                }
                //endregion

                //region 将获取的数据渲染为共享通讯录树 -- 共享
                $mScope.refreshShareGroup = function () {
                    var tempUrl = {url: 'common'}
                    repo.post(tempUrl.url + '/getETree',{source:"message"}).then(function (data) {
                        var rootChildren = data.data.children;
                        for (var i = rootChildren.length - 1; i >= 0; i--) {
                            var item = data.data.children[i].children;
                            if(item != null){
                                var convertNode = util.transDataToTree(item, 'id', 'parentId', 'children');
                                data.data.children[i].children = convertNode;
                            }
                        }
                        $mScope.dataForTheShareTree = [data.data];
                        $mScope.expandedShareNodes = [data.data];
                        for (var i = $mScope.dataForTheShareTree[0].children.length - 1; i >= 0; i--) {
                            if($mScope.dataForTheShareTree[0].children[i].children == null)continue;
                            var childArray = util.transDataToTree($mScope.dataForTheShareTree[0].children[i].children, 'id', 'parentId', 'children');
                            $mScope.expandedShareNodes.push(childArray);
                        }
                        $mScope.selectedNode = $mScope.dataForTheShareTree[0];
                    });
                };
                //endregion

                //region 检索按钮
                $mScope.search = function() {
                    if(!util.isEmpty($mScope.personParams._lt_endDate)&&!util.isEmpty($mScope.personParams._gt_beginDate)){
                        if (moment($mScope.personParams._lt_endDate).isBefore(moment($mScope.personParams._gt_beginDate))) {
                            toastr.error("开始时间不能大于结束时间");
                            return ;
                        }
                    }
                    $mScope.queryParams = angular.copy($mScope.personParams);
                    $mScope.refreshContact();
                };
                //endregion

                //region 通讯录确认键
                $mScope.okBtn = {
                    text:"确定",
                    click:function () {
                        if (parentScope.itemMainList && parentScope.showItemMainListTable) {
                            if($mScope.showRightItemList.length<1){
                                util.operateInfoModel('',"请添加联系人");
                                // util.hideModal(modal);
                                return;
                            }
                            //parentScope.varParams = ['姓名','手机号码', '性别', '出生日期',  '编号','VIP', '备注'];
                            // 如果parentScope上有itemMainList和showItemMainListTable 这两个属性
                            // 则进行修改parentScope上的itemMainList的值，并重新加载showItemMainListTable
                            var c = parentScope.itemMainList.concat($mScope.showRightItemList);

                            //var c = parentScope.itemMainList.concat(targetItemList);
                            parentScope.itemMainList = c;
                            c = [];
                            $mScope.showRightItemList = [];
                            parentScope.showItemMainListTable.reload();
                            if(!util.isEmpty(parentScope.convertVar)){
                                parentScope.convertVar(parentScope.itemMainList);
                            }
                            util.hideModal(modal);
                        }
                        // 如果需要调用该方法，但是parentScope上的属性的名字不是itemMainList和showItemMainListTable
                        // 那么你可以通过仿照上述的自己添加if分支来进行对你的parentScope上的属性进行修改和重新加载。
                    }
                }
                /*if(!parentScope.hasPermission(parentScope.urlPerMap.CONTACTMGR_PCONTACT_INDEX)){
                    $mScope.loadShareTable();
                }else {
                    $mScope.loadPersonTable();
                }*/
                $mScope.loadPersonTable();
                //endregion
            });
        }
        //endregion

        //region 获取通道详情
        var getChannelByBizType = function (parentScope, description, templateUrl, msgType) {
            util.commonModal(parentScope, description, templateUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.channelTable = new NgTableParams(
                    {
                        page: 0,
                        count: []
                    }, {
                        total: 0,
                        counts: [],
                        getData: function (params) {
                            return getChannelInfo(parentScope, msgType).then(function (data) {
                                params.total(data.total);
                                return data.data;
                            });
                        }
                    });
                mScope.okBtn = {
                    hide: true
                }
            })
        }

        //region 根据业务类型和信息类型，获取按通道价格为降序的通道列表
        var getChannelInfo = function (parentScope, msgType) {
            return repo.post('common/getChannelByBiztype', {
                bizTypeId: parentScope.selectedBizType.id,
                msgType: msgType
            }).then(function (data) {
                return data;
            });
        }
        //endregion
        //endregion

        //region 获取账户余额
        var getBalance = function (parentScope, url,bizTypeId,msgType) {
            var params = {
                bizTypeId:bizTypeId,
                msgType:msgType
            }
            repo.post(url,params).then(function (data) {
                parentScope.accountInfo = data.data;
            });
        };
        //endregion

        //region 检测关键字
        var checkKeyword = function (textContent, msgType) {
            repo.post("/common/checkKeyword", {content: textContent, type: msgType}).then(function (data) {
                var checkResultStr = "未检测到存在关键词";
                    if (data.status != 0) {
                        if (msgType == 2) {
                            checkResultStr = "标题或内容存在关键词：" + data.errorMsg.substring(0, data.errorMsg.length - 1);
                        } else {
                            checkResultStr = "内容存在关键词：" + data.errorMsg.substring(0, data.errorMsg.length - 1);
                        }
                    }
                util.operateInfoModel('', checkResultStr);
            });
        }
        //endregion

        //region 手动添加联系人
        //region 手动添加联系人弹窗
        var manualAddPhonesModal = function ($scope, sendSmsConf) {
            util.commonModal($scope, "手动添加", msgCfg.addPhoneTplUrl, function (modal) {
                var mScope = modal.$scope;
                initParams(mScope);
                mScope.okBtn = {
                    text: "确定",
                    click: function () {
                        reInitParams(mScope);
                        parsePhoneList($scope, mScope);
                    }
                };
            });
        };
        //endregion

        //region 初始化所有参数
        var initParams = function (parentScope) {
            parentScope.params = {
                // 输入的文本号码
                txtPhones:"",
                // 无效手机号码列表
                invalidPhones:[],
                invalidNum:0,
                // 有效手机号码个数
                validNum:0,
                // 有效号码列表
                validPhones:[],
                // 正则匹配号码
                phoneRegStr:new RegExp(/^(86|\+86)?((0((10[0-9]{7,8})|([2-9]{1}[0-9]{8,10})))|(0?1[3-8]{1}[0-9]{9}))$/),
                // phoneRegStr: new RegExp("(((^13[0-9]{1})|(^147)|(^15([0-3|5-9]{1}))|(^18[0|2|5|6|7|8|9]{1})|(^170))[0-9]{8}$)|(((^1718)|(^1719))[0-9]{7}$)", "g")
            };
        }

        var reInitParams = function (parentScope) {
            // 无效手机号码列表
            parentScope.params.invalidPhones=[];
            // 有效号码列表
            parentScope.params.validPhones=[];
            // 无效手机号码个数
            parentScope.params.invalidNum=0;
            // 有效手机号码个数
            parentScope.params.validNum=0;
        }
        //endregion

        //region 对号码进行排除86等
        var removeZhCnCode = function (phone){
            if(phone.length < 10) return phone;
            var sub = phone.substring(0, 2);
            if('86' == sub) return phone.substring(2);
            if('01' == sub && phone.charAt(2) != 0){
                return phone.substring(1);
            }
            sub = phone.substring(0, 3);
            if('+86' == sub) return phone.substring(3);
            return phone;
        };
        //endregion

        //region 过滤重复号码
        var filterRepeatPhone = function (parentScope, tempList) {
            var resultList = [];
            var tempLength = tempList.length;
            var temp = tempLength;
            for (var i = 0; i < temp; i++) {
                for (var j = i + 1; j < temp; j++) {
                    if (tempList[i].value === tempList[j].value) {
                        j = ++i;
                        parentScope.params.invalidNum++;
                        var invalidPhone = {
                            value: tempList[i].value,
                            reason: '号码重复'
                        };
                        parentScope.params.invalidPhones.push(invalidPhone);
                    }
                }
                parentScope.params.validNum++;
                resultList.push(tempList[i]);
            }
            return resultList;
        }
        //endregion

        //region 解析文本中的电话号码
        var parseText = function (parentScope, content) {
            // 使用正则，以免QQ粘贴过来的号码不能正确分割，因其是使用\r进行换行分割的，而不是常见的\r\n或\n
            var orgNums = content.replace(/[\r\n]/g, ",").replace(/ /g, ",").replace(/;/g, ",").replace(/\|/g, ",").split(",");

            // 根据空格、换行、逗号、 “|”分隔号码。换行与某一种分隔符可混用，其他分隔符不能同时混用。
            // 若存在多分隔符，系统仅识别第一个，其他当非法字符处理。

            // 临时list用于接收所有的通讯录--然后去重该数组
            var tempList = [];
            var phoneReg = parentScope.params.phoneRegStr;
            for (var i = 0; i < orgNums.length; i++) {
                if (orgNums[i].length > 0) {
                    phoneReg.lastIndex = 0;
                    var tempPhone = removeZhCnCode(orgNums[i]);
                    if (phoneReg.test(tempPhone)) {
                        parentScope.params.validPhones.push(tempPhone);
                        var contact = {
                            value: 'p' + removeZhCnCode(orgNums[i]),
                            text: removeZhCnCode(orgNums[i]),
                            count: 1
                        }
                        tempList.push(contact);
                    } else {
                        var reason = "";
                        if (tempPhone.length != 11) {
                            reason = '号码格式不正确';
                        } else {
                            reason = "非运营商号码";
                        }
                        var errorPhone = {
                            value: orgNums[i],
                            reason: reason
                        };
                        parentScope.params.invalidPhones.push(errorPhone);
                        parentScope.params.invalidNum++;
                    }
                }
            }
            return tempList;
        }
        // endregion

        //region 判断是否超过1万个手机号码
        /**
         * @param list
         * @returns {boolean} 超过1w返回true，否则返回false
         */
        var checkIsOverSize = function (list) {
            if (angular.isDefined(list) && list.length > msgCfg.manualAddCount) {
                util.operateInfoModel('',"不可以填写超过1万个手机号");
                return true;
            }
            return false;
        }
        //endregion

        //region 添加的所有号码都是成功的
        /**
         * @param tempList tempList中的数据都是通过校验的数据
         */
        var allSucModal = function (parentScope,childScope,tempList) {
            util.commonModal(
                childScope, "号码添加", msgCfg.infoTplUrl, function (modal) {
                    var mScope = modal.$scope;
                    mScope.params.message = "总共" + tempList.length + "个手机号码全部添加成功";
                    mScope.okBtn = {
                        text: "确定",
                        click: function () {
                            for(var i=0;i<tempList.length;i++) {
                                parentScope.itemMainList.push(tempList[i]);
                            }
                            parentScope.showItemMainListTable.reload();
                            util.hideModal(modal);
                            util.hideModal(mScope.$parent.modal);
                        }
                    };
                    mScope.closeBtn = {
                        hide:true
                    };
                    mScope.showIcon = {
                        hide:true
                    }
                });
        }
        //endregion

        //region 部分号码成功，部分失败
        /**
         * @param tempList tempList中的数据都是通过校验的数据
         */
        var partSucModal = function (parentScope, childScope,tempList) {
            util.commonModal(childScope, "号码提交提示", msgCfg.alertInvalidTplUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    invalidNum: childScope.params.invalidNum,
                    validNum: childScope.params.validNum,
                    invalidPhones: childScope.params.invalidPhones,
                };
                mScope.okBtn = {
                    text: "查看错误号码",
                    click: function () {
                        util.commonModal(mScope, "显示错误号码数", msgCfg.invalidPhoneTplUrl, function (modal) {
                            var mmScope = modal.$scope;
                            mmScope.invalidPhoneTable = new NgTableParams(
                                {
                                    page: 1,
                                    count: 10
                                },
                                {
                                    total: 0,
                                    counts: [],
                                    getData: function (params) {
                                        var page = params.page(); // 指的是我点过的页面
                                        var data = mScope.params.invalidPhones.slice(( page - 1) * params.count(), page * params.count());
                                        if (data.length == 0 && mScope.params.invalidPhones != 0) {
                                            page = page - 1;
                                            params.page(page);
                                            data = mScope.params.invalidPhones.slice(
                                                ( page - 1)* params.count(), page * params.count());
                                        }
                                        params.total(mScope.params.invalidPhones.length);
                                        return data;
                                    }
                                });
                            mmScope.okBtn = {
                                text: "确定",
                                click: function () {
                                    for(var i=0;i<tempList.length;i++) {
                                        parentScope.itemMainList.push(tempList[i]);
                                    }
                                    parentScope.showItemMainListTable.reload();
                                    util.hideModal(modal);
                                    util.hideModal(mScope.$parent.modal);
                                }
                            };
                        });
                    }
                };
            });
        }
        //endregion

        //region 解析输入框中的主要操作流程
        var parsePhoneList = function (parentScope, childScope) {
            var content = childScope.params.txtPhones;

            if (content == "") {
                util.operateInfoModel(childScope, "共处理0个号码，其中成功添加0个，失败0个");
                return;
            }
            var resultList = filterRepeatPhone(childScope, parseText(childScope, content));
            // if (angular.isUndefined(parentScope.sendTotal)){
            //     parentScope.sendTotal =0;
            // }
            // parentScope.sendTotal = parentScope.sendTotal + childScope.params.validNum;
            if (checkIsOverSize(resultList)) {
                return;
            }
            if (childScope.params.invalidNum == 0) {
                allSucModal(parentScope, childScope, resultList);
                return;
            } else {
                partSucModal(parentScope, childScope, resultList);
            }
        }
        //endregion
        //endregion

        //region 清空输入框中的文本内容
        /**
         * 清空输入框中的文本内容
         * @param scope 作用域
         * @param type 短彩信类型(mms : 彩信, 其他为短信)
         */
        var clearMsgText = function (scope, type) {
            util.commonModal(scope,"清空内容",msgCfg.infoTplUrl,function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    message:"确定清空"+(type=="mms"?"当前帧的彩信":"短信")+"内容"
                }
                mScope.okBtn = {
                    text:"清空",
                    click :function () {
                        if (type == "mms") {
                            scope.currentFrame.textContent = "";
                        } else {
                            scope.textContent = "";
                        }
                        util.hideModal(modal);
                    }
                }
            });
        }
        //endregion

        //region 导入收信人文件
        /**
         * 导入收信人文件
         * @param scope
         * @param sendSmsConf
         */
        var importContactModal = function (scope, conf) {
            util.commonModal(scope, "导入收信人", conf.importContactTemplateUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.diagCls = "modal-md";

                mScope.importParams = {
                    url: conf.uploadContactFileUrl,
                    delimiter: ",", //分隔符默认值
                    otherDelimiter: "",
                    fileType: "excel", //文件类型默认值
                };

                mScope.uploadData = {
                    file: null,
                    params: {}
                };
                mScope.progress = {
                    file: 0,
                };

                mScope.showParams = {
                    uploadFileButton: false,
                    selectFileButton: false,
                }

                mScope.fileParams = {
                    delimiter: null,
                    newName: null,
                    oldName: null
                }

                //上传按钮
                mScope.uploadFile = function () {
                    uploadFile(mScope, conf);
                }

                // 导入上传文件中的手机号码
                mScope.importFilePhone = function () {
                    importFileToPage(scope, mScope, conf);
                }

                mScope.okBtn = {
                    hide:true,
                    text: "导入",
                    click: function () {
                        if (mScope.progress.file != 100) {
                            toastr.warning("请先上传文件！");
                            return;
                        }
                        mScope.importFilePhone();
                    }
                };
            });
        }

        //region 将成功上传的文件中的电话联系人渲染到页面上
        var importFileToPage = function (parentScope, childScope, conf) {
            if(childScope.importParams.delimiter == "otherDelimiter"){
                if(childScope.importParams.otherDelimiter === ''){
                    util.operateInfoModel(childScope,"分隔符不可为空");
                    return;
                }
                var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;
                if(reg.test(childScope.importParams.otherDelimiter)){
                    util.operateInfoModel(childScope,"只能输入：英文、数字、符号");
                    return;
                }
                childScope.fileParams.delimiter = childScope.importParams.otherDelimiter;
            }else{
                childScope.fileParams.delimiter = childScope.importParams.delimiter;
            }
            repo.post(conf.doImportContactFileUrl, childScope.fileParams).then(function (resp) {
                if (resp.status == 0) {
                    if(resp.data.oldName.length > 7){
                        var text = resp.data.oldName.substr(0,7)+"......";
                    }else{
                        var text = resp.data.oldName;
                    }
                    var concact = {
                        value: "f" + resp.data.newName + "$" + resp.data.delimiter,
                        text: text + "（" + resp.data.sucCount + "）",
                        count: resp.data.sucCount,
                        viewRow:resp.data.viewRow,
                        //讲解析的文件头放入变量短信选项
                        varParams:angular.fromJson(resp.data.header)
                    }

                    // 全部导入成功
                    var sucInfo = "";
                    if (resp.data.sucCount == 0) {
                        util.operateInfoModel(childScope,"上传的文件中没有数据，请重新上传");
                        return;
                    }
                    if (resp.data.totalCount === resp.data.sucCount) {
                        sucInfo = "成功导入联系人" + resp.data.sucCount + "条记录"
                    } else {
                        // 部分导入成功
                        sucInfo = "总共有" + resp.data.totalCount + "条联系人记录，成功导入的有"
                                  + resp.data.sucCount + "条"
                    }
                    parentScope.itemMainList.push(concact);
                    if(!util.isEmpty(parentScope.convertVar)){
                        parentScope.convertVar(parentScope.itemMainList);
                    }
                    resultFile(parentScope, childScope,sucInfo)
                } else {
                    var msg = "新增失败: " + resp.errorMsg;
                    toastr.error(msg);
                }
            }).catch(function (resp) {
                toastr.error("导入发送短信联系人失败: " + resp.errorMsg);
            });
        };
        //endregion

        //region 文件上传之后，返回响应值
        /**
         * parentScope是childScope的父scope，用于在关闭子scope的
         * modal的时候同时，也关闭掉父scope的modal
         * @param parentScope
         * @param childScope
         * @param info 弹出modal(childScope的modal)要显示的数据，
         */
        var resultFile = function (parentScope, childScope, info) {
            util.commonModal(
                childScope, "导入联系人", msgCfg.infoTplUrl,
                function (modal) {
                    var mmScope = modal.$scope;
                    mmScope.params = {
                        message: info
                    };
                    mmScope.okBtn = {
                        text: '确定',
                        click: function () {
                            parentScope.showItemMainListTable.reload();
                            util.hideModal(modal);
                            util.hideModal(mmScope.$parent.modal);
                        }
                    };
                    mmScope.showIcon = {
                        hide: true
                    }
                    mmScope.closeBtn = {
                        hide: true
                    }
                });
        }
        //endregion

        //region 上传短彩信的通讯录文件
        /**
         * @param scope
         * @param conf
         */
        var uploadFile = function(scope, conf) {
            if (!scope.uploadData.file) {
                util.operateInfoModel(scope,"请选择上传文件");
                return;
            }
            // 判断文件分隔符
            scope.uploadData.params.delimiter =
                scope.importParams.delimiter != "otherDelimiter" ? scope.importParams.delimiter
                    : scope.importParams.otherDelimiter;

            if(scope.uploadData.params.delimiter == "") {
                scope.uploadData.params.delimiter = " ";
            }

            scope.uploadData.params.fileType = scope.importParams.fileType;
            if (checkFileType(scope)) {
                return;
            }

            repo.uploadFile(
                conf.uploadContactFileUrl, scope.uploadData.file, scope.uploadData.params,
                function (evt) {
                    scope.progress.file = parseInt(100.0 * evt.loaded / evt.total);
                },
                function (data) {
                    //读取返回数据中的文件头
                    if (data.status == 0) {
                        scope.okBtn.hide = false;
                        scope.showParams.selectFileButton = true;
                        //scope.fileParams.delimiter = scope.uploadData.params.delimiter;
                        scope.fileParams.newName = data.data.newName;
                        scope.fileParams.oldName = data.data.oldName;
                        toastr.success('文件上传成功！');
                    } else {
                        toastr.warning(data.errorMsg);
                        scope.okBtn.hide = true;
                    }
                }
            );
        };
        //endregion

        //region 检测用户在发送短彩信中导入的文件和选择的文件类型是否一致
        /**
         * @param scope
         * @returns {boolean} 类型一致返回true，类型不一致返回false
         */
        var checkFileType = function (scope) {
            var flag = false;
            var fileName = scope.uploadData.file.name;
            if ("excel" == scope.uploadData.params.fileType) {
                if (!util.endWith(fileName, "xlsx", true)&&!util.endWith(fileName, "xls", true)) {
                    util.operateInfoModel(scope, "上传类型不正确，请选择正确的excel类型的文件进行上传")
                    flag = true;
                }
            } else if ("txt" == scope.uploadData.params.fileType) {
                if (!util.endWith(fileName, "txt", true)) {
                    util.operateInfoModel(scope, "上传类型不正确，请选择正确的txt类型的文件进行上传")
                    flag = true;
                }
            } else if ("csv" == scope.uploadData.params.fileType) {
                if (!util.endWith(fileName, "csv", true)) {
                    util.operateInfoModel(scope, "上传类型不正确，请选择正确的csv类型的文件进行上传")
                    flag = true;
                }
            }
            return flag;
        }
        //endregion
        //endregion

        //region 发送短信或者是彩信的通用方法
        //region 判断发送参数是否有问题
        /**
         * @param scope
         * @param typeParams {}
         * typeParams.type ： "mms"是指彩信, "sms"是指短信
         * @returns {boolean} 发送参数符合校验返回true， 发送参数不符合校验返回false
         */
        var checkParams = function (scope, typeParams) {

            //region 判断短彩信中的共同拥有属性
            if (scope.itemMainList.length <= 0) {
                util.operateInfoModel(scope, "请添加"+typeParams.title+"发送用户");
                return false;
            }
            //endregion

            //region 判断短彩信中独有的属性
            if (typeParams.type == "mms") {
                //region 判断彩信中的独有属性
                if (scope.mmsTitle.length <= 0) {
                    util.operateInfoModel(scope, "请填写彩信标题");
                    return false;
                }

                if (scope.totalSize <= 0) {
                    util.operateInfoModel(scope, "请填写彩信内容，包括图片，音频，视频，或文字！")
                    return false;
                }
                //endregion
            } else {
                // 判断短信中的独有属性请
                if (scope.textContent.length <= 0) {
                    util.operateInfoModel(scope, "请填写短信内容");
                    return false;
                }
            }
            //endregion

            //region 判断短彩信中的定时参数是否是否有效
            if (!scope.isCheck) {
                // 如果没有勾选定时发送按钮，则将定时时间指定为空
                scope.scheduledTime = "";
            } else {
                // 定时的时间大于当前的时间
                if (moment().isAfter(scope.scheduledTime)) {
                    util.operateInfoModel(scope, "定时发送时间不得小于当前时间");
                    return false;
                }
            }
            //endregion
            return true;
        };
        //endregion

        //region 发送短信/彩信
        /**
         * @param scope 需要创建模态框的scope
         * @param conf 配置文件
         * conf.sendMsgUrl : 发送短信/彩信的请求url
         * @param params 发送参数
         * @param type 发送类型：mms为彩信 ，其余为短信
         */
        var sendMsg = function (scope, conf, params, type) {
            getChannelInfo(scope, conf.msgType).then(function (data) {
                if (data.status == 0 && data.total <= 0) {
                    // 判断用户选择的默认业务类型的通道是否存在
                    // 不存在则提醒他
                    util.operateInfoModel('', "请选择有通道的业务类型进行发送信息");
                    return;
                } else if (data.errorMsg != null && data.status != 0) {
                    util.operateInfoModel('', data.errorMsg);
                } else {
                    var showMsg = "发送成功";
                    var typeParams = {};
                    if (type == "mms") {
                        typeParams = {
                            type: "mms",
                            title: "彩信"
                        }
                        if(scope.hasPermission(scope.urlPerMap.SMSMGR_SENDTRACKING_LOADBATCHS)) {
                            showMsg = "发送成功，是否前往历史记录进行查看";
                        }
                    } else {
                        typeParams = {
                            type: "sms",
                            title: "短信"
                        }
                        if (scope.hasPermission(scope.urlPerMap.MMS_SEND_RECORD_INDEX)){
                            showMsg = "发送成功，是否前往历史记录进行查看";
                        }
                    }
                    if (!checkParams(scope, typeParams)) {
                        return;
                    }
                    // 只获取后台所需的联系人数据
                    var contactItem = [];
                    angular.forEach(params.contactItem, function (item) {
                        var contact = {
                            type: item.type,
                            value: item.value,
                            newName: item.newName
                        }
                        contactItem.push(contact);
                    });
                    params.contactItem = contactItem;

                    util.confirm(
                        "预计发送" + scope.sendTotal + "个号码，您确认要发送吗?", function () {

                            getSendState(scope, typeParams.title, conf,type);
                            repo.post(conf.sendMsgUrl, params).then(
                                function (data) {
                                    if (data.status == 0) {
                                        //region 如果发送成功
                                        scope.resultButton.hide = false;
                                        scope.sendResult.status = 1;
                                        scope.sendResult.msg = showMsg;
                                        if (scope.clearSent) {
                                            scope.textContent = '';
                                            scope.batchName = '';
                                            scope.itemMainList = [];
                                            scope.viewRow = [];
                                            scope.varParamsText = [];
                                            scope.showItemMainListTable.reload();
                                        }
                                        clearMsg(scope, type);
                                        //endregion
                                    } else {
                                        var errMsg = '其他错误';
                                        switch (data.status){
                                            case -1:errMsg = '非法账号';break;
                                            case -2:errMsg = '非法参数';break;
                                            case -6:errMsg = '账号不存在或密码错误';break;
                                            case -9:errMsg = '计费账户不存在';break;
                                            default:errMsg = data.errorMsg;
                                        }
                                        //region 发送异常
                                        util.commonModal(
                                            scope, "发送" + typeParams.title
                                                   + "返回信息", msgCfg.infoTplUrl, function (modal) {
                                                mScope = modal.$scope;
                                                mScope.params = {
                                                    message: errMsg
                                                }
                                                mScope.okBtn = {
                                                    text: "确定",
                                                    click: function () {
                                                        util.hideModal(modal);
                                                        util.hideModal(scope.$parent.modal);
                                                        return;
                                                    }
                                                }
                                                mScope.closeBtn = {
                                                    hide: true
                                                }
                                                mScope.showIcon = {
                                                    hide: true
                                                }
                                            })
                                        //endregion
                                        // util.operateInfoModel(scope, data.errorMsg, );
                                    }
                                    $interval.cancel(scope.timer);
                                }, true);
                        });
                }
            });
        }
        //endregion

        //region 短彩信获取发送短彩信状态
        /**
         * @param scope 创建模态框的scope
         * @param title 显示模态框的标题头
         * @param conf 配置参数：
         * 需要conf.sendResultUrl ： 要打开的模态框的模板的url
         * 需要conf.sendRecordUrl ：确定的时候，将要跳转的url
         * 需要conf.sendStatusUrl : 请求获取消息状态的url
         */
        var getSendState = function (scope,title, conf,type) {

            if(!angular.isDefined(title)) {
                title = "信息发送状态";
            } else {
                title = title+"发送状态";
            }

            scope.sendResult = {
                msg: "请求提交中...",
                status: 0
            };

            if ((type == "sms"
                 && scope.hasPermission(scope.urlPerMap.SMSMGR_SENDTRACKING_LOADBATCHS)) ||
                (type == "mms"
                 && scope.hasPermission(scope.urlPerMap.MMS_SEND_RECORD_INDEX))) {
                showSendMsgAfterModal(scope, title, conf, "前往历史记录查看", true);
            } else {
                showSendMsgAfterModal(scope, title, conf, "确定", false);
            }

            //region 定时2s获取发送状态
            scope.timer = $interval(function () {
                repo.post(conf.sendStatusUrl, {}).then(
                    function (data) {
                        scope.sendResult.msg = data.data;
                    });
            }, 2000);
            //endregion
        }
        //endregion


        //region 显示发送短彩信成功后的模态框
        var showSendMsgAfterModal = function (scope, title, conf,text,isLocation) {
            //region 显示发送记录模态框
            util.commonModal(scope, title, conf.sendResultUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    message: scope.sendResult.msg,
                    status: scope.sendResult.status
                };
                mScope.okBtn = {
                    hide: scope.sendResult.status != 1,
                    text: text,
                    click: function () {
                        util.hideModal(modal);
                        if (isLocation) {
                            $location.path(conf.sendRecordUrl);
                        }
                    }
                }
                mScope.closeBtn = {
                    hide:true
                }
                scope.resultButton = mScope.okBtn;
            });
            //endregion
        }
        //endregion

        //region 只有当彩信/短信发送成功之后，才会清空消息内容
        /**
         * @param scope 父scope
         * @param type 发送的消息类型：mms：彩信，其他：短信
         */
        var clearMsg = function (scope, type) {
            if (type == "mms" && scope.clearSent) {
                scope.initCtrl(scope);
            } else {
                // title = "短信";
            }
        }
        //endregion
        //endregion
        var showContent = function (normalInfo,msgType) {
            if(msgType == 1){
                var showTplUrl = 'smsmgr/sendsms/modal/smsTemplate.html';
            }else{
                var showTplUrl = 'mmsmgmt/template/modal/template.html';
            }
            util.commonModal('', "回复内容", showTplUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.content  = normalInfo.smsContent;
                mScope.templateTitle  = normalInfo.title;
                mScope.okBtn = {
                    hide:true
                }
            })
        }

        // 彩信备注
        var showRemark = function (scope) {
            var originalRemark = "";
            if (angular.isDefined(scope.remark) && scope.remark != "") {
                originalRemark = scope.remark;
            }
            util.commonModal(scope,"设置备注",msgCfg.remarkTplUrl,function (modal) {
                var $mScope = modal.$scope;
                $mScope.params = {
                    remark : originalRemark
                };
                $mScope.okBtn = {
                    text:"确定",
                    click: function () {
                        if($mScope.params.remark.length > 200) {
                            toastr.error("备注字符不得超过200个字符");
                            return;
                        }
                        scope.remark = $mScope.params.remark;
                        util.hideModal(modal);
                    }
                }
            });
        };
        //endregion

        //region 显示短信的批次详情
        var querySmsPack = function (parentScope, queryUrlPrefix, conf, pack) {
            debugger;
            repo.queryByUrl(
                queryUrlPrefix + '/showPack', conf, pack)
                .then(function (data) {
                    if (data.status == 0) {
                        util.commonModal(parentScope, "批次详情", msgCfg.smsBatchDetail, function (modal) {
                            var mScope = modal.$scope;
                            mScope.params = {};
                            mScope.params = data.data;
                            mScope.params.scheduleTime = pack.scheduleTime;
                            mScope.params.sendedTickets = pack.sendedTickets;
                            mScope.params.validTickets = pack.validTickets;

                            mScope.okBtn = {
                                hide: true
                            }

                        });
                    } else {
                        util.operateInfoModel('', "该批次显示异常");
                    }
                })
        }
        //endregion

        return {
            sendMsg: sendMsg,
            showRemark:showRemark,
            getBalance: getBalance,
            checkParams: checkParams,
            showContent : showContent,
            querySmsPack:querySmsPack,
            contactModal: contactModal,
            checkKeyword: checkKeyword,
            clearMsgText: clearMsgText,
            getChannelInfo: getChannelInfo,
            importContactModal: importContactModal,
            getChannelByBizType: getChannelByBizType,
            manualAddPhonesModal: manualAddPhonesModal,
        };
    };
    app.factory("MsgService", ["utilService", "repoService","NgTableParams",'$interval','$location', MsgService]);
});
