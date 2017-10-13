define(["app"], function (app) {
    var injectParams = ['$scope', '$location', 'MmsService'];
    var mmsTemplateAddCtrl = function ($scope, $location, mmsService) {
        var mmsTemplateAddConf = {
            uploadFileUrl: 'service/common/UploadFile?encode=0', // 上传文件的url
            url: 'InformationMgr/Template/Create', //基本URL，必填
            showName: '增加模板', //提示的名称，一般为模块名，必填
        };

        //region 初始化当前所在的控制器
        $scope.initMmsCtrl = function () {
            mmsService.initMmsCtrl($scope);
            mmsService.addFrame($scope);
        };
        //endregion

        //region 添加一帧
        $scope.addFrame = function () {
            mmsService.addFrame($scope);
        };
        //endregion

        //region 删除一帧
        $scope.deleteFrame = function (index) {
            mmsService.deleteFrame($scope, index);
        };
        //endregion

        //region 点击相应的帧，就会渲染相应的数据
        /**
         * @param index 当前的frame在frames中的下标的index值
         */
        $scope.editFrame = function (index) {
            mmsService.editFrame($scope, index);
        };
        //endregion

        //region 计算字符串的长度是多少
        /**
         * @param index frames的下标
         * @param string
         */
        $scope.calStringLength = function (index, string) {
            mmsService.calStringLength($scope, index, string);
        };
        //endregion

        //region 清空当前帧的文本内容
        $scope.clearFrameText = function (index) {
            mmsService.clearFrameText($scope,index);
        }
        //endregion

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

        //region 保存彩信模板
        $scope.saveMmsTemplate = function () {
            mmsService.saveMmsTemplate($scope, mmsTemplateAddConf, mmsTemplateAddConf.url,"add");
        };
        //endregion

        $scope.setAudioDuration = function (index) {
            mmsService.setAudioDuration($scope, index, $scope.audioDuration);
        }

        $scope.setVideoDuration = function (index) {
            mmsService.setVideoDuration($scope, index, $scope.videoDuration);
        }

        //region 取消返回到模板首页
        $scope.cancelAddTemplate = function () {
            mmsService.locationToUrl($scope.urlPerMap.INFO_TEMPLATE_INDEX);
        };
        //endregion

        //region 执行这个控制器的一些方法
        this.init =$scope.initMmsCtrl();
        //endregion

    };
    mmsTemplateAddCtrl.$inject = injectParams;
    app.register.controller('mmsTemplateAddCtrl', mmsTemplateAddCtrl);
});
