define(["app"], function (app) {
    var injectParams = ['$scope', '$location', 'NgTableParams','repoService', 'utilService', '$timeout', 'Upload','MsgService','MmsService'];
    var sendMmsIndexCtrl = function ($scope, $location,NgTableParams, repo, util, $timeout, Upload,msgService,mmsService) {
        var sendMmsConf = {
            initUrl:$scope.urlPerMap.INFO_TEMPLATE_MODIFY,
            uploadFileUrl : 'service/common/UploadFile?encode=0', // 上传文件的url
            bizTypeUrl:$scope.urlPerMap.INFO_BIZTYPE_LIST,
            url : 'InformationMgr/Template/Create', //基本URL，必填
            showName : '发送彩信', //提示的名称，一般为模块名，必填,
            msgType:2,
            //region 发送彩信中使用
            sendMsgUrl: $scope.urlPerMap.SEND_MMS_DO_SEND,
            sendResultUrl:'/smsmgr/sendsms/modal/sendResult.html',
            sendRecordUrl : $scope.urlPerMap.MMS_SEND_RECORD_INDEX,
            sendStatusUrl:$scope.urlPerMap.SMSMGR_SENDMMS_INDEX + '/getSendingState',
            getRestNum:$scope.urlPerMap.SMSMGR_SENDMMS_INDEX + '/getBalance',
            //endregion

            addSuccessTemplateUrl:'/_modal/infoAlert.html',
            remarkTemplateUrl:'/mmsmgmt/sendmms/modal/remark.html',
            addPhoneTemplateUrl:'/mmsmgmt/sendmms/modal/addPhone.html',
            quoteTemplateUrl:'/mmsmgmt/sendmms/modal/quoteTemplate.html',
            invalidTemplateUrl:'/mmsmgmt/sendmms/modal/alertInvalidPhone.html',
            channelDetailTemplateUrl:'/smsmgr/sendsms/modal/channelDetail.html',
            importContactTemplateUrl:'/mmsmgmt/sendmms/modal/importContact.html',
            resultImportContactTplUrl:'/mmsmgmt/sendmms/modal/resultImportContact.html',
            showInvalidTemplateUrl:'/mmsmgmt/sendmms/modal/showInvalidPhones.html',
            uploadContactFileUrl:$scope.urlPerMap.SMSMGR_SENDMMS_INDEX+'/upload?encode=0',
            doImportContactFileUrl:$scope.urlPerMap.SMSMGR_SENDMMS_INDEX+'/import',
        };

        $scope.selectedBizType = null;
        // 号码项目：包含号码、导入文件、通讯录
        $scope.itemMainList = [];
        // 在前台显示的项目的list
        $scope.showItemList = [];
        $scope.tempFrames = [];
        $scope.sortableOptions = {};
        $scope.bizTypes = {};
        $scope.tempFrames =[];
        $scope.params = {};
        $scope.viewRow =[];

        $scope.showItemMainListTable = new NgTableParams(
            {
                page:1,
                count:10
            },{
                counts:[],
                paginationMaxBlocks: 5,
                paginationMinBlocks: 2,
                total:0,
                getData: function (params) {
                    var page = params.page(); // 指的是我点过的页面
                    var data = $scope.itemMainList.slice(
                        ( page - 1) * params.count(), page * params.count());
                    if (data.length == 0 && $scope.itemMainList.length != 0) {
                        page = page - 1;
                        params.page(page); // 还要手动设置，王八蛋
                        data = $scope.itemMainList.slice(
                            ( page - 1) * params.count(), page * params.count());
                    }
                    params.total($scope.itemMainList.length);
                    $scope.sendTotal = 0;
                    angular.forEach($scope.itemMainList, function (item) {
                        $scope.sendTotal += item.count;
                    });
                    return data;
                }
            });

        // 删除一个号码项目
        $scope.deleteCurItem = function (index,params) {
            var realIndex = ( params.page()-1)*(params.count())+index;
            $scope.itemMainList.splice(realIndex, 1);
            $scope.showItemMainListTable.reload();
        };

        //region 初始化
        $scope.initCtrl = function (scope) {
            if(!$scope.hasPermission($scope.urlPerMap.SMSMGR_SENDMMS_INDEX)) {
                toastr.error("对不起，你没有权限进行此项操作。请联系系统管理员！");
                location.href = indexPage;
                return;
            }

            mmsService.initMmsCtrl(scope);
            scope.addFrame();
            scope.getAllBizType();
            $scope.distinct = true; // 默认去除重复
            $scope.clearSent =true;  // 默认清空内容
            $scope.isCheck = false; // 默认没有定时时间
            $scope.checkKeyword = function (index) {
                mmsService.checkMmsKeyWord($scope,index);
            }
            $scope.scheduledTime = new Date().Format("yyyy-MM-dd") + " 00:00:00";
            $(function() {
                var timePicker = angular.copy($scope.dateTimePicker);
                timePicker.isAllowEmpty = true;
                timePicker.startDate = moment().format('YYYY-MM-DD HH:mm:ss');
                timePicker.minDate = new Date();
                $('input[name="scheduledTime"]').daterangepicker(timePicker);
            });
        }
        //endregion

        /**
         * 添加一帧
         */
        $scope.addFrame = function () {
            mmsService.addFrame($scope);
        };

        /**
         * 删除一帧，删除该帧后，自动定位到删除帧的前一帧
         * @param index
         */
        $scope.deleteFrame = function (index) {
            mmsService.deleteFrame($scope,index);
        };

        /**
         * 点击相应的帧，就会渲染相应的数据
         * @param index 当前的frame在frames中的下标的index值
         * @param itemId 当前frame的id的值，
         */
        $scope.editFrame = function (index) {
            mmsService.editFrame($scope,index);
        };

        /**
         * 计算字符串的长度是多少
         * @param index frames的下标
         * @param string
         */
        $scope.calStringLength = function (index, string) {
            mmsService.calStringLength($scope, index, string);
        };

        //region 上传文件
        /**
         * 上传文件的通用方法
         * @param index 当前帧的索引($scope.frames[index])
         * @param file 通过ng-file-upload验证的文件
         * @param invalidFile 没有通过nf-file-upload验证的文件
         * @param type 文件类型，取值为 $scope.mediaType的值
         */
        $scope.uploadMediaFile = function (index, file, invalidFile, type) {
            mmsService.uploadMediaFile($scope, index, file, invalidFile, type);
        };
        //endregion

        //region 删除文件
        /**
         * 删除文件：图片，音频，视频
         * @param index 当前帧的索引($scope.frames[index])
         * @param type 文件类型，取值为 $scope.mediaType的值("img","audio","video")
         */
        $scope.deleteMediaFile = function (index, type) {
            mmsService.deleteMediaFile($scope, index, type);
        }
        //endregion

        //region 清空当前帧的文本内容
        $scope.clearFrameText = function (index) {
            mmsService.clearFrameText($scope, index);
        }
        //endregion

        //region 保存彩信模板
        $scope.saveMmsTemplate = function () {
            $scope.templateName = $scope.mmsTitle;
            $scope.templateId = $scope.templateIndex;
            var tempConf = {
                showName:'彩信模板'
            }
            mmsService.saveMmsTemplate($scope,tempConf,'',"sendMms");
        };
        //endregion

        //region 渲染业务类型
        $scope.getAllBizType = function () {
            var bizParams = {};
            bizParams.msgType = 2;
            repo.post("common/fetchBizTypeForMsg", bizParams).then(function (data) {
                if (data.status == 0) {
                    $scope.selectedBizType = data.data[0];
                    $scope.bizTypes = data.data;
                    $scope.toggleSendInfo();
                } else {
                    util.operateInfoModel('', '由于通道未配置，未能加载到业务类型，请先配置通道');
                }
            });
        }
        //endregion

        //region 使用模板
        /**
         * @param index 模板的id
         */
        $scope.loadMmsTemplate = function (index) {
            mmsService.loadMmsTemplate($scope, sendMmsConf.initUrl, index)
        };
        //endregion

        //region 播放音频
        $scope.playAudio = function (index) {
            mmsService.playAudio($scope, index);
        }
        //endregion

        //region 暂停播放音频
        $scope.pauseAudio = function (index) {
            mmsService.pauseAudio($scope,index);
        }
        //endregion

        //region 播放视频
        $scope.playVideo = function (index) {
            mmsService.playVideo($scope,index);
        }
        //endregion

        $scope.setAudioDuration = function (index) {
            mmsService.setAudioDuration($scope, index, $scope.audioDuration);
        }

        $scope.setVideoDuration = function (index) {
            mmsService.setVideoDuration($scope, index, $scope.videoDuration);
        }

        //region 手动添加号码功能
        $scope.manualAddPhones = function () {
            msgService.manualAddPhonesModal($scope,sendMmsConf);
        };
        //endregion

        /**
         * 导入收件人
         */
        $scope.importContact = function () {
            msgService.importContactModal($scope,sendMmsConf);
        }

        /**
         * 打开通讯录
         */
        $scope.openContact =function () {
            msgService.contactModal($scope,"通讯录");
        };

        /**
         * 引用模板
         */
        $scope.quoteMmsTemplate = function () {
            util.commonModal($scope, "引用模板", sendMmsConf.quoteTemplateUrl, function (modal) {
                var $mScope = modal.$scope;
                $mScope.mmsTemplateListParams = {
                    templateId: "",
                    templateName: ""
                };
                $mScope.mmsTemplateTable = new NgTableParams(
                    {
                        page: 1,
                        count: 10
                    }, {
                        total: 0,
                        getData: function (params) {
                            return repo.queryByUrl($scope.urlPerMap.INFO_TEMPLATE_LIST, sendMmsConf, util.buildQueryParam(params, $mScope.mmsTemplateListParams)).then(function (data) {
                                params.total(data.total);
                                return data.data;
                            });
                        }
                    });

                // 引用模板的操作
                $mScope.quoteTemplateById = function (index) {
                    util.commonModal($mScope, "引用模板", sendMmsConf.resultImportContactTplUrl, function (childModal) {
                        var $mmScope = childModal.$scope;
                        $mmScope.params = {
                            message: "引用彩信模板，将会覆盖原有编辑的彩信内容，是否继续？"
                        }
                        $mmScope.okBtn = {
                            text: '确定',
                            click: function () {
                                $scope.loadMmsTemplate(index);
                                util.hideModal(modal);
                                util.hideModal(childModal);
                            }
                        }
                    })
                };

                // 查詢模板的操作
                $mScope.searchMmsTemplate = function () {
                    $mScope.mmsTemplateTable.reload();
                }

                $mScope.okBtn = {
                    hide: true
                }
            })
        };

        //region 当业务类型进行切换的时候，自动根据刷新页面上用户的可发送彩信数目
        $scope.toggleSendInfo = function () {
            repo.post(sendMmsConf.getRestNum, {
                bizTypeId: $scope.selectedBizType.id,
                msgType: 2
            }).then(function (data) {
                $scope.accountInfo = {
                    balance: data.data.balance,
                    restSendNum: data.data.restSendNum,
                    enableKeywordFilter:data.data.enableKeywordFilter
                }
                // 重新为总的大小进行赋值
                $scope.maxSize = data.data.mmsMaxLength;
            });
        }
        //endregion

        //region 通道详情
        $scope.channelDetail = function () {
            msgService.getChannelByBizType($scope,"通道详情",sendMmsConf.channelDetailTemplateUrl,2);
        }
        //endregion

        //region 增加备注
        $scope.showRemark = function () {
            msgService.showRemark($scope);
        };
        //endregion

        /**
         * 发送彩信
         */
        $scope.doSendMms = function () {
            util.deepCopy($scope.frames,$scope.tempFrames);
            var totalFrame =$scope.tempFrames.length;
            for (var index = 0; index < totalFrame; index++) {
                var frame = $scope.tempFrames[index];
                delete frame.file;
                delete frame.videoType;
            }
            var framesInfo = {
                id: 0,
                title: $scope.mmsTitle,
                mms: {
                    subject: $scope.mmsTitle,
                    frames: $scope.tempFrames
                },
                bizId:$scope.selectedBizType.id
            };
            if (!$scope.isCheck) {
                // 如果没有勾选定时发送按钮，则将该值指定为空
                $scope.scheduledTime = "";
            }
            //endregion

            //region 检查是否超过系统大小
            if (mmsService.checkOverSize($scope)) {
                return;
            }
            //endregion

            var sendMmsParams = {
                mms: util.toJson(framesInfo),
                batchName: $scope.batchName,
                scheduledTime: $scope.scheduledTime,
                remark: $scope.remark,
                clearSent: $scope.clearSent,
                distinct: $scope.distinct,
                sendCount: $scope.itemMainList.length,
                bizTypeId: $scope.selectedBizType.id,
                contactItem:$scope.itemMainList
            };

            //region 发送彩信
            msgService.sendMsg($scope,sendMmsConf,sendMmsParams,"mms");
            //endregion
        };

        $scope.clearItemMainList = function () {
          util.commonModal($scope,"清空发送人列表",sendMmsConf.resultImportContactTplUrl,function (modal) {
              var $mScope = modal.$scope;
              $mScope.params = {
                  message:"确定要清空发送人列表吗"
              }
              $mScope.okBtn = {
                  text:"确定",
                  click:function () {
                      $scope.itemMainList = [];
                      $scope.showItemMainListTable.reload();
                      util.hideModal(modal);
                  }
              }
          })
        };

        //region 加载页面初始化
        this.init = $scope.initCtrl($scope);
        //endregion
    };



    sendMmsIndexCtrl.$inject = injectParams;
    app.register.controller('sendMmsIndexCtrl', sendMmsIndexCtrl);
});
