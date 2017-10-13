define(["app"], function(app) {
    var modalService = function(util, repo) {
        var importModal = function(parentScope, title, conf) {

            //头数据信息对象
            function HeadInfo(index, name) {
                var obj = new Object();
                obj.index = index;
                obj.name = name;
                return obj;
            };
            util.commonModal(parentScope, title, "_modal/import.html", function(modal) {
                var mScope = modal.$scope;
                mScope.diagCls = "modal-md";
                //导入参数实体
                //文件类型
                mScope.importParams = {
                    url:conf.uploadUrl,
                    delimiter: ",", //分隔符默认值
                    otherDelimiter: "",
                    fileType: "excel", //文件类型默认值
                    headDMapToFList: parentScope.headDMapToFList
                };
                //文件头
                mScope.fileHeads = [];
                mScope.progress = {
                    file: 0,
                };
                mScope.uploadData = {
                    file: null,
                    uploadSuccess: false,
                    params: {}
                };

                var uploadFileWatchStop = mScope.$watch('uploadData.file', function(value) {
                    if(!value) {
                        mScope.uploadData.uploadSuccess = false;
                    }
                })

                //上传按钮
                mScope.uploadFile = function() {
                    if (!mScope.uploadData.file) {
                        return;
                    }
                    var uploadFileName = mScope.uploadData.file.name;
                    var selectFileType = mScope.importParams.fileType;
                    var canUpload = true;
                    switch (selectFileType) {
                        case 'excel':
                            canUpload = util.endWith(uploadFileName, '.xls', true) ||
                                util.endWith(uploadFileName, '.xlsx', true);
                            break;
                        case 'txt':
                            canUpload = util.endWith(uploadFileName, '.txt', true);
                            break;
                        case 'csv':
                            canUpload = util.endWith(uploadFileName, '.csv', true);
                            break;
                        default:
                            canUpload = false;
                    }
                    if(!canUpload) {
                        toastr.error("请选择正确的文件类型！");
                        return;
                    }
                    mScope.uploadData.params.delimiter = mScope.importParams.delimiter != "otherDelimiter" ? mScope.importParams.delimiter : mScope.importParams.otherDelimiter;
                    if(mScope.uploadData.params.delimiter == "") {
                        mScope.uploadData.params.delimiter = " ";
                    }
                    mScope.uploadData.params.fileType = mScope.importParams.fileType;
                    repo.uploadFile(
                        conf.uploadUrl,
                        mScope.uploadData.file, mScope.uploadData.params,
                        function(evt) {
                            mScope.progress.file = parseInt(100.0 * evt.loaded / evt.total);
                        },
                        function(data) {
                            //读取返回数据中的文件头
                            if (data.status == 0) {
                                toastr.success('文件上传成功，请选择文件列 ！');
                                mScope.fileHeads = [];
                                for (var index in data.data.fileHead.headMap) {
                                    mScope.fileHeads.push(new HeadInfo(index, data.data.fileHead.headMap[index]));
                                }
                                //初始化系统列中的文件头
                                for (var hIndex in mScope.headDMapToFList) {
                                    for (var fIndex in mScope.fileHeads) {
                                        if (mScope.headDMapToFList[hIndex].dataHeadInfo.name == mScope.fileHeads[fIndex].name) {
                                            mScope.headDMapToFList[hIndex].fileHeadInfo = mScope.fileHeads[fIndex];
                                        }
                                    }
                                }
                                mScope.uploadData.fileSize = data.data.fileSize / 1024 / 1024;
                                mScope.importParams.fileName = data.data.fileName;
                                mScope.importParams.fileSize = data.data.fileSize;
                                mScope.importParams.path = data.data.path;
                                mScope.uploadData.uploadSuccess = true;
                            } else {
                                toastr.warning(data.errorMsg);
                            }
                        }
                    );
                };


                mScope.okBtn = {
                    text: "导入",
                    click: function() {
                        if (mScope.progress.file != 100) {
                            toastr.warning("请先上传文件！");
                            return;
                        }
                        var importRequest = {};
                        importRequest.fileName = mScope.importParams.fileName;
                        importRequest.fileSize = mScope.importParams.fileSize;
                        importRequest.headDMapToFList = mScope.importParams.headDMapToFList;
                        importRequest.delimiter = mScope.uploadData.params.delimiter;
                        importRequest.path = mScope.importParams.path;
                        repo.doImport(conf, importRequest).then(function(data) {
                            uploadFileWatchStop();
                            util.hideModal(modal);
                        });
                    }
                };
            });

        };

        var exportModal = function(parentScope, title, conf, queryParams) {
            repo.post('/common/checkEnabledExport','').then(function(data) {
                if(!angular.isDefined(conf.checkExport) && data.status == -1){
                    util.confirm("导出功能暂时关闭,有需要请联系客服,敬请谅解！", function () {
                    });
                }else{
                    if (data.data == null) {
                        queryParams.maxExportRecord = data.errorMsg;
                    } else {
                        queryParams.maxExportRecord = data.data
                    }
                    //queryParams.maxExportRecord = data.data;
                    openExport(parentScope, title, conf, queryParams);
                }
            });
        };
        var openExport = function (parentScope, title, conf, queryParams){
		
            util.commonModal(parentScope, title, "_modal/export.html", function(modal) {
                var mScope = modal.$scope;
                var maxExportRecord = queryParams.maxExportRecord/10000;
                mScope.diagCls = "modal-md";
                //导入参数实体
                //文件类型
				if(queryParams.sendReport!=null && queryParams.sendReport == 'true'){
					if(queryParams.sendReportNoBill == 'true'){
						mScope.exportParams = {	
							sendReport:"true",	
							sendReportNoBill:"true",							
							messageExport:conf.messageExport,
                            disPlayTaskNameInput:conf.disPlayTaskNameInput,
							maxExportRecord:maxExportRecord,
							delimiter: ",", //分隔符默认值
							otherDelimiter: "",
							fileType: "excel", //文件类型默认值
						};
					}else{
						mScope.exportParams = {	
							sendReport:"true",				
							messageExport:conf.messageExport,
                            disPlayTaskNameInput:conf.disPlayTaskNameInput,
							maxExportRecord:maxExportRecord,
							delimiter: ",", //分隔符默认值
							otherDelimiter: "",
							fileType: "excel", //文件类型默认值
						};
					}
					
				}else{
					mScope.exportParams = {				
						messageExport:conf.messageExport,
                        disPlayTaskNameInput:conf.disPlayTaskNameInput,
						maxExportRecord:maxExportRecord,
						delimiter: ",", //分隔符默认值
						otherDelimiter: "",
						fileType: "excel", //文件类型默认值
                	};
				}
    
                mScope.okBtn = {
                    text: "导出",
                    click: function() {
                        if (mScope.exportParams.delimiter == "otherDelimiter") {
                            mScope.exportParams.delimiter = mScope.exportParams.otherDelimiter;
                        }
                        if(mScope.exportParams.delimiter == "") {
                            mScope.exportParams.delimiter = " ";
                        }
                        mScope.exportParams.fileName = util.getExportFileName(mScope.exportParams.fileType);
                        angular.extend(mScope.exportParams, queryParams);
                        mScope.exportParams.maxExportRecord = queryParams.maxExportRecord/10000;
                        if(mScope.exportParams.fileType == 'excel' && mScope.exportParams.exportRecordSize > 65000) {
                            toastr.warning("导出数据量较大，仅可导出为文本文件或Csv文件！");
                            return;
                        }
                        if(mScope.exportParams.exportRecordSize > queryParams.maxExportRecord) {
                            toastr.warning("导出数据量超过了最大限制条数！");
                            return;
                        }
                        var taskName = mScope.exportParams.name;
                        var taskNamePattern = /[//\\\?]/;
                        if(angular.isDefined(taskName) && taskNamePattern.test(taskName)){
                            toastr.warning("任务名称中不能包含(/, \\, ？)等特殊字符！");
                            return;
                        }

                        repo.doExport(conf, util.buildCommonReq(mScope.exportParams)).then(function(data) {
                            util.hideModal(modal);
                        });
                    }
                };
            });
        }
        return {
            importModal: importModal,
            exportModal: exportModal

        };
    };
    app.factory("modalService", ["utilService", "repoService", modalService]);
});
