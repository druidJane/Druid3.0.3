define(["app"], function(app) {
    var injectParams = ['$scope', '$location', 'repoService', 'utilService','$routeParams'];
    var phraseAddCtrl = function($scope, $location, repo, util, $routeParams) {
        var phraseAddConf = {
            locationUrl:$scope.urlPerMap.INFO_SMSTEMPLATE_INDEX,//页面URL
            url: 'SmsMgr/SmsTemplate', //基本URL，必填
            showName: '新增模版' //提示的名称，一般为模块名，必填
        };
        $scope.phraseAdd = {
            templateType : 0  //用于显示初始化下拉框
        };

        var phraseType = $routeParams.phraseType;//normal：普通模板，var：变量模板
        
        $scope.isNormal = true;
        if(phraseType == "normal"){
            $scope.isNormal = false;
        }

        var numberVariable = '10';
        var chineseVariable = '3';
        $scope.addVariable = function (){
            var str = "[$数字变量$]";
            $("#phraseContent").focus();
            var posTextArea = document.getElementById("phraseContent");
            var textArea = $("#phraseContent").val();
            var reg = new RegExp("\\[\\$数字变量\\$\\]", "g");
            var arr = textArea.match(reg);
            var num = 0;
            if (arr) {
                num = arr.length;
            }
            //校验“数字变量”标签的个数
            if (num >= numberVariable) {
                alert("模板数字变量不能超过"+numberVariable+"个！", function () {
                    $("#phraseContent").focus();
                });
                return;
            }

            //textArea += str;
            if(textArea.length + str.length>1000){
                alert("模板内容不能够超过1000个字符");
                return;
            }
            //在textArea对应的光标下插入“数字变量”标签
            var pos = getCursorPos(posTextArea);
            var tempText = textArea.substring(0, pos) + str + textArea.substring(pos);

            //$scope.phraseAdd.phraseSMSContent = textArea;
            $scope.phraseAdd.phraseSMSContent = tempText;
            $("#phraseContent").focus();
        }

        $scope.addChineseVariable = function (){
            var str = "[$中文变量$]";
            $("#phraseContent").focus();
            var posTextArea = document.getElementById("phraseContent");
            var textArea = $("#phraseContent").val();
            var reg = new RegExp("\\[\\$中文变量\\$\\]", "g");
            var arr = textArea.match(reg);
            var num = 0;
            if (arr) {
                num = arr.length;
            }
            //校验“中文变量”标签的个数
            if (num >= chineseVariable) {
                alert("模板中文变量不能超过"+chineseVariable+"个！", function () {
                    $("#phraseContent").focus();
                });
                return;
            }
            //textArea += str;
            if(textArea.length + str.length>1000){
                alert("模板内容不能够超过1000个字符");
                return;
            }

            //在textArea对应的光标下插入“中文变量”标签
            var pos = getCursorPos(posTextArea);
            var tempText = textArea.substring(0, pos) + str + textArea.substring(pos);
            //$scope.phraseAdd.phraseSMSContent = textArea;
            $scope.phraseAdd.phraseSMSContent = tempText;
            $("#phraseContent").focus();
        }

        $scope.reatextchange = function (){
            var textArea = $("#phraseContent").val();
            if(textArea) {
                var reg = /\[\$([^\[\$])*\$\]/g;
                var arr = textArea.match(reg);
                if (arr) {
                    for (var i = 0; i < arr.length; i++) {
                        if (arr[i] != '[$中文变量$]' && arr[i] != '[$数字变量$]')
                            textArea = textArea.replace(arr[i], '');
                    }
                }
                //$("#contentstr").val(content);
                $scope.phraseAdd.phraseSMSContent = textArea;
                $("#phraseContent").focus();
            }
        }
        
        /**
         * 获取光标在短连接输入框中的位置
         * @param inputId 框Id
         * @return {*}
         */
        function getCursorPos(elem) {
            var index = 0;
            if (document.selection) {// IE Support
                elem.focus();
                var Sel = document.selection.createRange();
                if (elem.nodeName === 'TEXTAREA') {//textarea
                    var Sel2 = Sel.duplicate();
                    Sel2.moveToElementText(elem);
                    var index = -1;
                    while (Sel2.inRange(Sel)) {
                        Sel2.moveStart('character');
                        index++;
                    };
                }else if (elem.nodeName === 'INPUT') {// input
                    Sel.moveStart('character', -elem.value.length);
                    index = Sel.text.length;
                }
            }else if (elem.selectionStart || elem.selectionStart == '0') { // Firefox support
                index = elem.selectionStart;
            }
            return (index);
        }

        //校验表单数据
        function checkParameters(parameters) {
            /**
             * 1、点击【新增】时，校验“模板内容”栏是否含变量配对符[$$]，
               识别到变量符号“[$”后再识别第一个匹配的变量符“$]”，识别成对的变量符后，
               中间字符均视为变量字段名，且变量配对符号中间不允许为空或空格，
               如果为空或空格则限制新增并提示：变量名称不能为空；
                2、至少存在一个变量，否则限制新增成功并提示：至少添加一个变量；
             */

            var content = parameters.phraseSMSContent;
            //var reg = new RegExp("\\[\\$.*\\$\\]", "g");//new RegExp("\\[\\$^s\\$\\]", "g");
            //var arr = content.match(reg);//只要arr不是null，证明模板内容带了变量
            var reg = /\[\$([^\[\$])*\$\]/g;
            var arr = content.match(reg);
            
            if($scope.isNormal && arr == null){
                toastr.warning("模板内容需要带变量");
                return false;
            }

            //模板名称  字符长度限制为50
            if(parameters.title.length > 50){
                toastr.warning("模板名称”的长度不能大于50！");
                return false;
            }

            //模板内容  字符长度限制为1000
            if(parameters.phraseSMSContent.length > 1000){
                toastr.warning("模板内容”的长度不能大于1000！");
                return false;
            }

            //return false;
            return true;
        }

        //新增提交按钮
        $scope.submitForm = function() {
            var url = phraseAddConf.url + "/AddNonAudit";
            //校验表单数据
            if(!checkParameters($scope.phraseAdd)){
                return;
            }
            
            if(phraseType == 'normal'){
                $scope.phraseAdd.phraseType = 0;//用于区分“普通模板（0）”与“变量模板”（1）
                url = $scope.urlPerMap.INFO_COMMONTEMPLATE_ADD;
            }else if(phraseType == 'var'){
                $scope.phraseAdd.phraseType = 1;//用于区分“普通模板（0）”与“变量模板”（1）
                url = $scope.urlPerMap.INFO_VARIANTTEMPLATE_ADD;
            }

            //提交数据
            repo.post(url, $scope.phraseAdd).then(function(data) {
                if(data.status == 0){
                    $location.path(phraseAddConf.locationUrl);
                }else{
                    toastr.error('新增模板失败!');
                }
            });
        };
    };

    phraseAddCtrl.$inject = injectParams;
    app.register.controller('phraseAddCtrl', phraseAddCtrl);
});
