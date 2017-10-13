/**
 * Created by gdy on 2017/6/5.
 * 命名原因 ： 存放和帧相关的代码---短信没有这个功能，
 */
/**
 * Created by gdy on 2017/6/2.
 * 该类主要编写在页面中多次进行调用的方法
 */
define(["app"], function (app) {

    var MmsService = function (sizeFormatterFilter,util, repo, $location, ngFileUpload, ngAudio, audioConfig, vgSec,$filter,msgService) {

        var mmsConf = {
            infoUrl: '/_modal/infoAlert.html',
            videoUrl: '/mmsmgmt/_modal/video.html',
            uploadUrl: xpath.service('common/UploadFile?encode=0'),
            templateUrl: '/mmsmgmt/template/modal/template.html',
            staticResourceUrl: '/mos/styles/default/images',
            batchDetail:'/mmsmgmt/sendtracking/modal/recordDetail.html',
            showTplUrl:'mmsmgmt/template/modal/template.html',
            VIDEO_3GP:'video/3gpp',
            VIDEO_MP4:'video/mp4',
            DEFAULT_DURATION:5,
        }

        //region 初始化变量
        var initMmsCtrl = function (parentScope) {
            // 批次名称
            parentScope.batchName = "";
            // 彩信标题
            parentScope.mmsTitle = "";
            // 彩信模板的编号
            parentScope.templateIndex = "";
            // 彩信模板的名称
            parentScope.templateName = "";
            // 最后要进行序列化的数据
            parentScope.frames = [];
            // 用于控制生成frame是frame的id的值
            parentScope.frameIdValue = 1;
            //当前选中的帧要进行渲染的数据
            parentScope.currentFrame = {
                // frames中的索引值
                index: -1,
                // 当前frame的id属性的值,控制激活的帧
                idValue: -1,
                // 每帧图片下面的显示
                // 图片地址
                imgAdder: null,
                // 音频地址
                audioAdder: null,
                // 音频文件--供播放
                audioFile:null,
                // 视频地址
                videoAdder: null,
                // 视频文件--供播放
                videoFile:null,
                // 文字内容
                textContent: null,
                // 上一次输入字符串的字节数目
                textSize:0,
                // 右侧预览图片
                // 预览图片
                previewImg: null,
                // 预览文字
                previewText: null
            };
            // 当前短信所有帧的总的字节数
            parentScope.totalSize = 0;
            // 每帧显示的图片
            parentScope.fileType = {
                textPng: mmsConf.staticResourceUrl + '/text.png',
                audioPng: mmsConf.staticResourceUrl + '/audio.png',
                videoPng: mmsConf.staticResourceUrl + '/video.png'
            };
            // 定义播放时长  [1,2,3,4,5,6,7,8,9,10,15,20,25,30];
            //region 这里注意：audioDuration 中的数据必须是在audioDurations中存在的，否则不进行显示
            parentScope.audioDurations = [1,2,3,4,5,6,7,8,9,10,15,20,25,30];
            parentScope.audioDuration = 5;
            //endregion
            parentScope.videoDurations = [1,2,3,4,5,6,7,8,9,10,15,20,25,30];
            parentScope.videoDuration = 5;
            // 对于sortable插件的配置在此处进行
            parentScope.sortableOptions = {};
            //region 控制能够上传的文件的配置
            parentScope.uploadConf = {
                video:{
                    type:".mp4,.3gp",
                    size:"50KB"
                },
                audio:{
                    type:".amr,.mid",
                    size:"50KB"
                },
                img:{
                    type:".jpg,.gif,.bmp",
                    size:"50KB"
                }
            }
            //endregion
            parentScope.maxSize = 300*1024; // 总共可以发送的彩信大小300KB
            parentScope.systemNeedSize = 2*1204; // 系统占用2KB
            parentScope.mediaType = ["img","audio","video"];
        };
        //endregion

        // region 文件的公共方法
        //region 上传文件的主要流程方法
        var uploadMediaFile = function (parentScope, index, file, invalidFile, type) {
            if (checkFileValid(parentScope, file, invalidFile, type,index)) {
                if (!checkOverSize(parentScope, "file", file)) {
                    uploadFileToServer(parentScope, index, type, file);
                }
            }
        };
        //endregion
        //region 检测上传文件类型是否匹配
        var checkFileValid = function (parentScope, file, invalidFile,type,index) {
            if ((invalidFile === null) && (file === null)) {
                return  false;
            }

            if (parentScope.totalSize > parentScope.maxSize) {
                util.operateInfoModel(parentScope,"上传的文件容量已经超过最大值");
                return false;
            }

            var params = {};
            //region 选择显示数据
            if (type == "audio") {
                params = {
                    name : "音频",
                    type:parentScope.uploadConf.audio.type,
                    size:parentScope.uploadConf.audio.size,
                }
            } else if (type == "video" ) {
                params = {
                    name : "视频",
                    type:parentScope.uploadConf.video.type,
                    size:parentScope.uploadConf.video.size,
                }
            } else if (type == "img") {
                params = {
                    name : "图片",
                    type:parentScope.uploadConf.img.type,
                    size:parentScope.uploadConf.img.size,
                }
            }
            //endregion

            //region 显示报错信息
            if (invalidFile && util.containerStr(invalidFile.$error, "pattern")) {
                util.operateInfoModel(parentScope, "请选择后缀为"+params.type+"的"+params.name+"文件");
                return false;
            }
            if (invalidFile && util.containerStr(invalidFile.$error, "maxTotalSize")) {
                util.operateInfoModel(parentScope, "请选择"+params.name+"文件小于"+params.size);
                return false;
            }
            //endregion

            //region 判断文件是否能够共存
            var curFrame = parentScope.frames[index];
            if (type == "audio" && (curFrame.video.name!="" && curFrame.video.size > 0)) {
                // 上传的音频文件和已经存在的视频文件冲突了
                util.operateInfoModel('',"视频和声音不能存在同一帧中");
                return false;
            } else if (type == "img" && (curFrame.video.name!="" && curFrame.video.size > 0)) {
                // 上传的图片频文件和已经存在的视频文件冲突了
                util.operateInfoModel('',"视频和图片不能存在同一帧中");
                return false;
            } else if (type == "video") {
                if (curFrame.img.name != "" && curFrame.img.size > 0) {
                    util.operateInfoModel('',"视频和图片不能存在同一帧中");
                    return false;
                } else if (curFrame.audio.name != "" && curFrame.audio.size > 0) {
                    util.operateInfoModel('',"视频和声音不能存在同一帧中");
                    return false;
                }
            }
            //endregion
            return true;
        }
        //endregion

        // region 封装一个上传文件到后台服务器的方法
        var uploadFileToServer = function (parentScope, index, type ,file) {
            if (file) {
                file.upload = ngFileUpload.upload(
                    {
                        url: mmsConf.uploadUrl,
                        data: {file: file}
                    });
                file.upload.then(function (response) {

                    if(0 == response.data.status ){
                        // 上传成功
                        //region 检测图片位置应当显示什么图片
                        if (type == "audio" && !util.isEmptyFile(parentScope.frames[index].img)) {
                            // 上传的文件是音频，且不存在图片文件
                            parentScope.frames[index].file = parentScope.fileType.audioPng;
                        } else if (type == "video"){
                            parentScope.frames[index].file = parentScope.fileType.videoPng;
                            parentScope.frames[index].videoType = file.type;
                        } else if (type == "img"){
                            parentScope.frames[index].file = file;
                        }
                        //endregion

                        // region 使用.来访问或者是进行设置属性的key
                        // 使用[]来进行访问对象中的key是变量的情况
                        parentScope.frames[index][type]['name']=response.data.data.name;
                        parentScope.frames[index][type]['size']=response.data.data.size;
                        // 当文件上传成功后，先将当前帧的时长设置为默认值
                        // --防止用户先进行时间选择，在上传文件，此时控件会显示之前选择的时长，测试会误认为是bug
                        parentScope.frames[index].duration = mmsConf.DEFAULT_DURATION;
                        // endregion

                        renderCurFrame(parentScope, index);
                    } else {
                        showUploadFailed(parentScope,type,response)
                    }
                });
            }
        };
        // endregion

        // region 如果上传文件失败，显示的消息弹窗
        var showUploadFailed = function (parentScope,type,response) {
            var typeName = "";
            if (type == "video") {
                typeName = "视频"
            } else if (type == "audio") {
                typeName = "音频"
            } else if (type == "img") {
                typeName = "图片"
            }
            util.operateInfoModel(parentScope, typeName + "上传失败 : "+response.data.errorMsg);
        };
        // endregion

        //region 删除文件
        var deleteMediaFile = function (parentScope, index, type) {
            var typeName = ""
            if (type == "img") {
                typeName = "图片"
            } else if (type =="audio") {
                typeName = "音频"
                parentScope.audioFile = null;
            } else if (type =="video") {
                typeName = "视频"
            }
            var curFrame = parentScope.frames[index];
            if (curFrame[type].size <= 0) {
                util.operateInfoModel(parentScope, "该帧不存在"+typeName);
                return;
            }
            // 清空数据
            parentScope.frames[index].duration = mmsConf.DEFAULT_DURATION;
            parentScope.frames[index][type].name = "";
            parentScope.frames[index][type].size = 0;
            renderCurFrame(parentScope, index);
        }
        //endregion
        // endregion

        //region 音频相关操作
        //region 加载音频，渲染指定帧的时候进行该操作
        var downloadAudioFile = function (parentScope, index) {
            if ((angular.isDefined(parentScope.bizTypes))&&(parentScope.bizTypes.length == 0)){
                toastr.warning("请选择业务类型");
                return;
            }
            var curFrame = parentScope.frames[index];
            // 此处必须要提交要进行该配置，否则就会出错
            if (curFrame.audio && curFrame.audio.name) {
                audioConfig.unlock = false;
                parentScope.audioFile = ngAudio.load(util.imgUrl + curFrame.audio.name);
            } else {
                parentScope.audioFile = null;
            }
        }
        //endregion

        //region 播放音频
        var playAudio = function (parentScope) {
            var playFlag = false;
            if (parentScope.audioFile) {
                // 如果语音文件的url为mid、amr的时候，必然为true，除这两种文件外其他的必然为false
                playFlag =
                    util.endWith(parentScope.audioFile.src, ".mid")
                    || util.endWith(parentScope.audioFile.src, ".amr");

                if (playFlag == false) {
                    parentScope.audioFile.play();
                } else {
                    util.operateInfoModel(parentScope, "暂时不支持试听mid，amr的音频格式")
                }
            }

        };
        //endregion

        //region 暂停播放音频
        var pauseAudio = function (parentScope) {
            parentScope.audioFile.pause();
        }
        //endregion
        //endregion

        //region 视频相关操作
        //region 播放视频
        var playVideo = function (parentScope,index) {

            var curFrame = parentScope.frames[index];
            var videoType ="";
            var videoUrl = "";
            if(curFrame.video && curFrame.video.name!="") {
               videoType = curFrame.videoType;
               videoUrl = curFrame.video.name;
                parentScope.videoFile = true;
            } else {
                util.operateInfoModel('', "没有可播放文件，请上传相关文件")
                return;
            }

            if (videoType != "" && videoType == mmsConf.VIDEO_3GP) {
                util.operateInfoModel('', "暂时不支持播放3gp格式的视频")
                return;
            }
            util.commonModal(parentScope, "视频预览", mmsConf.videoUrl, function (modal) {
                var mScope = modal.$scope;
                mScope.params = {}
                // 只有mp4文件才可以进行播放
                if (videoType != mmsConf.VIDEO_3GP) {
                    // region播放文件的配置
                    mScope.params = {
                        config: {
                            preload: 'none',
                            sources: [{
                                src: vgSec.trustAsResourceUrl(util.imgUrl + videoUrl),
                                type: videoType
                            }]
                        }
                    }
                    // endregion
                }
                //region 按钮隐藏
                mScope.okBtn = {
                    hide: true
                }
                mScope.closeBtn = {
                    hide: true
                }
                //endregion
            })
        };
        //endregion
        //endregion

        //region 帧相关操作
        //region 添加一个帧
        var addFrame = function (parentScope) {
            if (parentScope.frames.length >= 20) {
                util.operateInfoModel(parentScope, "不得添加超过20帧的彩信");
                return;
            }
            var frame = createFrame(parentScope);
            parentScope.frames.push(frame);
            // 添加完一帧之后，当前的将自动定位到新添加的帧
            renderCurFrame(parentScope, parentScope.frames.length - 1);
        };
        //endregion

        //region 组装一个frame,也可以叫做新创建一个帧
        var createFrame = function (parentScope) {
            // 注释 -- duration的作用是这个彩信会自动的播放的时长
            var frame = {
                id: "mms_" + parentScope.frameIdValue,
                text: {
                    name: "",
                    size: 0,
                    content: "",
                },
                img: {
                    name: "",
                    size: 0,
                },
                audio: {
                    name: "",
                    size: 0
                },
                video: {
                    name: "",
                    size: 0
                },
                duration: mmsConf.DEFAULT_DURATION,
                file: null,
                // 视频的类型
                videoType: null,
            };
            parentScope.frameIdValue++;
            return frame;
        };
        //endregion

        //region 删除一帧，删除该帧后，自动定位到删除帧的前一帧
        /**
         * @param index 点击的帧的在frames中的帧索引
         */
        var deleteFrame = function (parentScope, index) {
            if (index == 0) {
                util.operateInfoModel(parentScope, "第一帧不可删除");
                return;
            }
            parentScope.frames.splice(index, 1);
            // 删除后将光标定位到被删除的帧的位置上
            renderCurFrame(parentScope, index - 1);
        };
        //endregion

        //region 点击相应的帧，就会渲染相应的数据
        /**
         * @param parentScope
         * @param index 当前的frame在frames中的下标的index值
         */
        var editFrame = function (parentScope, index) {
            renderCurFrame(parentScope, index);
        };
        //endregion

        //region 选择帧上要显示的图片是什么?显示优先级:img>audioPng>videoPng>textPng
        /**
         * @param index frames的索引值
         */
        var checkFileType = function (parentScope, index) {
            if (parentScope.frames[index].img.size > 0) {
                return;
            }
            if (parentScope.frames[index].audio.size > 0) {
                parentScope.frames[index].file = parentScope.fileType.audioPng;
                return;
            }
            if (parentScope.frames[index].video.size > 0) {
                parentScope.frames[index].file = parentScope.fileType.videoPng;
                return;
            }
            if (parentScope.frames[index].text.size > 0) {
                parentScope.frames[index].file = parentScope.fileType.textPng;
                return;
            }
            parentScope.frames[index].file = null;
        };
        //endregion

        //region 将数据渲染到该帧
        /**
         * @param index frames数组的索引值
         */
        var locationCurFrame = function (parentScope, index) {
            var curFrame = parentScope.frames[index];
            parentScope.currentFrame.index = index;
            parentScope.currentFrame.idValue = curFrame.id;
            parentScope.currentFrame.textContent = curFrame.text.content;
            parentScope.currentFrame.textSize = calculateContentBytes(curFrame.text.content);
            parentScope.currentFrame.imgAdder = curFrame.img.name;
            parentScope.currentFrame.audioAdder = curFrame.audio.name;
            parentScope.currentFrame.videoAdder = curFrame.video.name;
            parentScope.currentFrame.previewText = curFrame.text.content;
            //region 判断当前要显示的图片
            if ((curFrame.img.size > 0) || (curFrame.audio.size > 0) || (curFrame.video.size > 0)) {
                parentScope.currentFrame.previewImg = curFrame.file;
            } else {
                parentScope.currentFrame.previewImg = null;
            }
            //endregion
            //region 判断video是否存在，以判断页面中是否显示播放二字
            if (curFrame.video.size>0) {
                parentScope.currentFrame.videoFile = true;
            } else {
                parentScope.currentFrame.videoFile = false;
            }
            //endregion
        };
        //endregion

        //region 改变之后渲染当前所在的帧
        /**
         * @param parentScope parentScope
         * @param index 当前帧的frame的索引
         */
        var renderCurFrame = function (parentScope, index) {
            if (index < 0) {
                util.operateInfoModel(parentScope, "已经是第一帧");
                return;
            }
            if (index > (parentScope.frames.length - 1)) {
                util.operateInfoModel(parentScope, "已经是最后一帧了");
                return;
            }
            checkFileType(parentScope, index);
            calTotalSize(parentScope);
            downloadAudioFile(parentScope, index);
            resetCurFrameDuration(parentScope,index);
            locationCurFrame(parentScope, index);
        };
        //endregion

        //region 计算帧大小操作
        //region 计算当前彩信的大小彩信
        var calTotalSize = function (parentScope) {
            var frameSize = parentScope.frames.length;
            var total = 0;
            for (var index = 0; index < frameSize; index++) {
                var curFrame = parentScope.frames[index];
                total += curFrame.text.size;
                total += curFrame.img.size;
                total += curFrame.audio.size;
                total += curFrame.video.size;
            }
            parentScope.totalSize = total;
        };
        //endregion

        //region 计算字符串+音频+视频+图片的字节是多少
        /**
         * @param index frames的下标
         * @param string
         */
        var calStringLength = function (parentScope, index, textContent) {
            // 判断该帧最多可以输入的字节长度
            var remainIndex = parentScope.maxSize + parentScope.currentFrame.textSize - parentScope.totalSize ;
            var bytesCount= calculateContentBytes(textContent);
            if(remainIndex < bytesCount) {
                util.commonModal(parentScope, "输入字符提示", mmsConf.infoUrl, function (modal) {
                    var childScope = modal.$scope;

                    childScope.params = {
                        message:"输入内容过多，已经超过彩信最大容量: "+sizeFormatterFilter(parentScope.maxSize, 0)+"， 系统将自动为你截取到最大可输入文字长度"
                    }

                    childScope.okBtn = {
                        text:'确定',
                        click: function () {
                            var tempTextSize = 0;
                            var tempContent = '';
                            if (remainIndex == 0) {
                                textContent = tempContent;
                                bytesCount = tempTextSize;
                            } else if (remainIndex == 1) {
                                if (textContent.charCodeAt(0) > 255) {
                                    textContent = tempContent;
                                    bytesCount = tempTextSize;
                                } else {
                                    textContent = textContent[0];
                                    bytesCount = 1;
                                }
                            } else {
                                for (var i = 0; i < textContent.length; i++) {

                                    // 计算该字符的字节长度
                                    if (textContent.charCodeAt(i) > 255) {
                                        tempTextSize += 2;
                                    } else {
                                        tempTextSize += 1;
                                    }
                                    // 拼装需要的字符串
                                    tempContent += textContent[i];

                                    if ((tempTextSize == remainIndex - 1)
                                        && (textContent.charCodeAt(i + 1) > 255)) {
                                        // 如果只剩下一个字节的话，下一个字符是ACSII码的话，就继续进行循环，
                                        // 否则就跳过该字节
                                        textContent = tempContent;
                                        bytesCount = tempTextSize;
                                        break;
                                    }

                                    if (tempTextSize == remainIndex) {
                                        textContent = tempContent;
                                        bytesCount = tempTextSize;
                                        break;
                                    }
                                }
                            }

                            // textContent = textContent.substring(0, remainIndex);
                            showTextSize(parentScope,index,textContent,bytesCount);
                            util.hideModal(modal);
                        }
                    }
                    childScope.closeBtn = {
                        hide:true
                    }
                    childScope.showIcon = {
                        hide:true
                    }
                });
            } else {
                showTextSize(parentScope,index,textContent,bytesCount);
            }
        };

        var showTextSize = function (parentScope,index,textContent,bytesCount) {
            parentScope.frames[index].text.content = textContent;
            parentScope.frames[index].text.size = bytesCount;
            parentScope.totalSize = parentScope.totalSize + bytesCount;
            parentScope.currentFrame.textSize = bytesCount;
            parentScope.currentFrame.previewText = textContent;
            renderCurFrame(parentScope, index);
        }

        // 计算字符串的总字节数
        var calculateContentBytes = function (textContent) {
            var bytesCount = 0;
            for (var i = 0; i < textContent.length; i++) {
                if(textContent.charCodeAt(i)>255){
                    bytesCount+=2;
                } else {
                    bytesCount+=1;
                }
            }
            return bytesCount;
        }
        //endregion

        var resetCurFrameDuration = function (scope, index) {
            // 按照旧mos的来做，只能放音频+图片+文件，
            // 或者是视频+文字，因此同一帧中只有一个duration有效
            var curF = scope.frames[index];
            if (curF.audio.size > 0 && curF.audio.name != "") {
                scope.audioDuration = curF.duration;
                scope.videoDuration = mmsConf.DEFAULT_DURATION;
            } else if (curF.video.size > 0 && curF.video.name != "") {
                scope.videoDuration = curF.duration;
                scope.audioDuration = mmsConf.DEFAULT_DURATION;
            } else {
                // 当判断当前帧中既不存在音频文件，也不存在视频文件
                // 那么不管当前帧被用户设置为多少，都会在切换帧的时候，被重置为默认值
                scope.videoDuration = mmsConf.DEFAULT_DURATION;
                scope.audioDuration = mmsConf.DEFAULT_DURATION;
                scope.frames[index].duration = mmsConf.DEFAULT_DURATION;
            }
        }
        //endregion

        //region 清空当前帧彩信输入的内容
        var clearMmsCurFrameText = function (scope,index) {
            util.commonModal(scope,"清空内容",mmsConf.infoUrl,function (modal) {
                var mScope = modal.$scope;
                mScope.params = {
                    message:"确定清空当前帧的彩信内容"
                }
                mScope.okBtn = {
                    text:"清空",
                    click :function () {
                        scope.frames[index].text.content = "";
                        scope.frames[index].text.size = 0;
                        util.hideModal(modal);
                        renderCurFrame(scope,index);
                    }
                }
            });
        }
        //endregion

        //region 检测当前帧彩信输入的内容
        var checkMmsKeyWord = function (scope) {
            // 获取当前所有帧中的文字
            var framesLength = scope.frames.length;
            var content = [];
            content.push(scope.mmsTitle);
            for (var index = 0; index < framesLength; index++) {
                content.push(scope.frames[index].text.content);
            }
            msgService.checkKeyword(content, 2);
        }
        //endregion

        //region 判断当前短信的大小是否已经超过了系统的最大值。
        /**
         * @param scope
         * @param type 检测类型：file 检测类型为上传文件时调用，word 检测类型为输入字符完毕后调用， 其他 检测类型为发送彩信时调用
         * @param file 如果检测类型为 file，此时file为上传的文件
         * @returns {boolean} 超过系统大小返回true，没有超过大小返回false
         */
        var checkOverSize = function (scope, type, file) {
            var flag = false;
            var size = file ? file.size : 0;
            size = size + scope.totalSize;
            if (size > scope.maxSize) {
                var info = $filter('sizeFormatter')(scope.maxSize, 0);
                if(type == "file") {
                    info = "上传文件容量过大，会使当前彩信超过" + info + "请重新选择上传文件";
                } else {
                    info = "系统检测到你提交的彩信已经超过最大容量: "+info+"请减少彩信内容";
                }
                util.operateInfoModel(scope, info)
                flag = true;
            }
            return flag;
        }
        //endregion

        //endregion

        //region 保存彩信模板
        /**
         * @param scope
         * @param conf  需要conf.showName
         * @param addUrl  增加或更新模板的url
         * @param isLocation 是否跳转
         */
        var saveMmsTemplate = function (scope, conf, addUrl, type) {
            var templateWarnStr = "请填写模板名称";
            var templateLengthStr = "模板长度不得大于20个字符";
            if (type=="sendMms") {
                templateWarnStr = "请填写彩信标题";
                templateLengthStr = "彩信标题长度不得大于20个字符";
            }

            if (!angular.isDefined(scope.templateName)||scope.templateName.length<=0) {
                util.operateInfoModel(scope, templateWarnStr);
                return;
            }
            if (angular.isDefined(scope.templateName) && scope.templateName.length>20) {
                util.operateInfoModel(scope, templateLengthStr );
                return;
            }
            if (scope.totalSize === 0) {
                util.operateInfoModel(scope, "请至少添加一个非空彩信帧");
                return;
            }
            if (scope.totalSize >= (300 * 1024)) {
                util.operateInfoModel(scope, "彩信模板大小不得大于300KB");
                return;
            }

            var showWarnStr = "";
            if (type == "add"){
                showWarnStr = "新增"
            } else if (type == "update"){
                showWarnStr = "更新"
            } else if (type == "sendMms"){
                if (scope.templateId == 0) {
                    showWarnStr = "新增"
                    addUrl = scope.urlPerMap.INFO_TEMPLATE_CREATE;
                } else {
                    showWarnStr = "更新"
                    addUrl = scope.urlPerMap.INFO_TEMPLATE_MODIFY+'/update';
                }

            } else {
                showWarnStr = "其他"
            }

            var totalFrame = scope.frames.length;

            for (var index = 0; index < totalFrame; index++) {
                var frame = scope.frames[index];
                delete frame.file;
                delete frame.videoType;
            }
            var json = {
                id: scope.templateId?scope.templateId:0,
                title: scope.templateName,
                mms: {
                    subject: scope.templateName,
                    frames: scope.frames
                },
                bizId: 0
            };

            repo.post(addUrl, json).then(function (resp) {
                if (resp.status == 0) {
                    toastr.success(conf.showName + "成功。");
                    if (type != "sendMms") {
                        $location.path(scope.urlPerMap.INFO_TEMPLATE_INDEX);
                    }
                } else {
                    var msg = showWarnStr + "失败: " + resp.errorMsg;
                    toastr.error(msg);
                }
            }).catch(function (resp) {
                toastr.error(showWarnStr + conf.showName + "失败: " + resp.errorMsg);
            });

        };
        //endregion

        //region 根据当前的模板id/或者是其他参数，加载该模板到页面中/或者是预览彩信的页面中
        // 调用该方法后会定位到最后一帧
        var loadMmsTemplate = function (parentScope, url, index, type) {
            if (parentScope.frames) {
                parentScope.frames = [];
            }
            repo.post(url, index).then(
                function (data) {
                    // 获取数据值
                    var jsonResult = util.fromJson(data.data);

                    var frameData = {};
                    var totalFrame = 0;

                    if (angular.isDefined(jsonResult.content)
                        && angular.isDefined(jsonResult.content.frames)) {
                        // 如果是加载彩信模板走该分支
                        frameData = jsonResult.content.frames;
                        parentScope.templateIndex = jsonResult.id;
                        parentScope.templateName = jsonResult.title;
                        parentScope.mmsTitle = jsonResult.title;
                    } else if (angular.isDefined(jsonResult.frames)) {
                        // 如果是预览彩信走该分支
                        frameData = jsonResult.frames;
                        parentScope.templateName = jsonResult.subject;
                    }
                    totalFrame = frameData.length;
                    createFrameWithData(parentScope, frameData, totalFrame);

                    if (parentScope.frames) {
                        renderCurFrame(parentScope, parentScope.frames.length - 1);
                        parentScope.frameIdValue = parentScope.frames.length;
                    } else {
                        parentScope.frameIdValue = 1;
                    }

                    // 如果是预览的话，同时该彩信帧至少一帧，则定位到最开始的一帧
                    if (angular.isDefined(type) && parentScope.frames.length > 0) {
                        renderCurFrame(parentScope, 0);
                    }
                }
            )
        };

        //region 将下载的所有frames的数据加载到同样多的帧中
        /**
         * @param parentScope
         * @param framesData 一条彩信中所有的数据
         * @param totalFrams 一条彩信中的帧的总数
         */
        var createFrameWithData = function (parentScope, framesData, totalFrames) {
            for (var index = 0; index < totalFrames; index++) {
                var frame = {
                    id: "",
                    text: {
                        name: "",
                        size: 0,
                        content: "",
                    },
                    img: {
                        name: "",
                        size: 0,
                    },
                    audio: {
                        name: "",
                        size: 0
                    },
                    video: {
                        name: "",
                        size: 0
                    },
                    duration: mmsConf.DEFAULT_DURATION,
                    file: null
                };
                frame.id = "mms_" + index;
                //region 设置文本相关
                if (framesData[index].text && framesData[index].text.size > 0) {
                    frame.text.name = framesData[index].text.name;
                    frame.text.size = calculateContentBytes(framesData[index].text.content);
                    frame.text.content = framesData[index].text.content;
                }
                //endregion

                //region 设置图像
                if (framesData[index].img && framesData[index].img.size > 0) {
                    frame.img.name = framesData[index].img.name;
                    frame.img.size = framesData[index].img.size;
                }
                //endregion

                //region 设置音频
                if (framesData[index].audio && framesData[index].audio.size > 0) {
                    frame.audio.name = framesData[index].audio.name;
                    frame.audio.size = framesData[index].audio.size;
                }
                //endregion

                //region 设置视频
                if (framesData[index].video && framesData[index].video.size > 0) {
                    frame.video.name = framesData[index].video.name;
                    frame.video.size = framesData[index].video.size;
                }
                //endregion

                //region 设置播放时长
                if (framesData[index].duration) {
                    frame.duration = framesData[index].duration;
                }
                //endregion

                //region 设置每一帧的上图片的显示
                if (framesData[index].img && framesData[index].img.size > 0) {
                    // 如果有图片的话，将获取图片并添加给file
                    // 可以在生成文件的位置建立一个web服务器，通过nginx来
                    frame.file = util.imgUrl + frame.img.name;
                }
                //endregion

                parentScope.frames.push(frame);
                checkFileType(parentScope, index);
            }
        }
        //endregion
        //endregion

        //region 根据url，跳转到某个页面
        var locationToUrl = function (url) {
            $location.path(url)
        };
        //endregion

        //region 预览彩信内容
        /**
         * 预览彩信
         * @param parentScope 父scope作用域
         * @param url 要查询的url
         * @param templateUrl 显示彩信使用的模板路径
         * @param conf 配置，需要使用到conf中的showName属性
         * @param params 参数
         */
        var showMmsPack = function (parentScope, url, templateUrl, conf, params) {
            templateUrl = mmsConf.templateUrl;
            util.commonModal(parentScope, "彩信预览", templateUrl, function (modal) {
                var scope = modal.$scope;
                initMmsCtrl(scope);
                loadMmsTemplate(scope, url, params,"preShow");
                scope.editFrame = function (index) {
                    editFrame(scope, index);
                }
                scope.okBtn = {
                    hide:true
                }
            })
        }
        //endregion

        //region 设置视频的播放时间长度
        var setVideoDuration = function (scope,index,time) {
            scope.frames[index].duration = time;
        }
        //endregion

        //region 设置音频播放的时间
        var setAudioDuration = function (scope,index,time) {
            scope.frames[index].duration = time;
        }
        //endregion

        var queryMmsPack = function (parentScope, urlPrefix, conf, pack) {
            repo.queryByUrl(urlPrefix + '/showPack', conf, pack).then(function (data) {
                if (data.status == 0) {
                    util.commonModal(parentScope, "批次详情", mmsConf.batchDetail, function (modal) {
                        var mScope = modal.$scope;
                        mScope.params = {};
                        mScope.params = data.data;
                        mScope.params.sendedTickets = pack.sendedTickets;
                        mScope.params.scheduleTime = pack.scheduleTime;
                        mScope.params.validTickets = pack.validTickets;
                        //region 预览彩信内容
                        mScope.mmsPreviewByPackId = function () {
                            showMmsPack(parentScope, urlPrefix + '/showMms', mmsConf.showTplUrl, conf,
                                        mScope.params);
                        }

                        //endregion
                        mScope.okBtn = {
                            hide: true
                        }
                    });
                } else {
                    util.operateInfoModel('', "该批次显示异常");
                }
            })
        }

        return {
            addFrame: addFrame,
            playAudio:playAudio,
            playVideo:playVideo,
            editFrame: editFrame,
            pauseAudio:pauseAudio,
            initMmsCtrl:initMmsCtrl,
            showMmsPack:showMmsPack,
            createFrame: createFrame,
            deleteFrame: deleteFrame,
            queryMmsPack:queryMmsPack,
            checkOverSize:checkOverSize,
            locationToUrl:locationToUrl,
            checkMmsKeyWord:checkMmsKeyWord,
            deleteMediaFile:deleteMediaFile,
            uploadMediaFile:uploadMediaFile,
            loadMmsTemplate:loadMmsTemplate,
            saveMmsTemplate: saveMmsTemplate,
            calStringLength: calStringLength,
            setVideoDuration:setVideoDuration,
            setAudioDuration:setAudioDuration,
            clearFrameText:clearMmsCurFrameText,
        };
    };
    app.factory("MmsService", ["sizeFormatterFilter","utilService", "repoService", '$location', 'Upload','ngAudio','ngAudioGlobals', '$sce','$filter','MsgService', MmsService]);
});
