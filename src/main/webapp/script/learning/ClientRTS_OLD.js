function ObcAPI() {
   // 전역함수 정의 시작
	this._Debug = false; 
	this.mInitializedState = true;
	this.mTerminatedState = false;
	this.progress = null;
	this.lessonProgress = null;
   
   // 전역함수 전역 종료
   
   
   // 콘텐츠 시작시 호출 
	this.Initialize = function (param) {
		var result = "false";
		if (this._Debug) {
			alert("*********************\nIn API::Initialize\n*********************");
			alert("param : " + param);
		}
		if (!this.mTerminatedState) {
			if (ContentExecutor.otype != "N") {
				var progress = map;
				progress.put("sequence", ContentExecutor.contentObjectTableList[ContentExecutor.current].seq);
				var result = null;
				if (ContentExecutor.learningType == "1") {
					result = dwrUtil.invokeMap(progress.toMap(), {beanId:"usr.obcWrapperDWR", method:"initialize"});
				} else {
					result = dwrUtil.invokeMap(progress.toMap(), {beanId:"usr.obcPreviewWrapperDWR", method:"initialize"});
				}
				if (result != null) {
					this.progress = result;
				}
				var index = ContentExecutor.contentObjectTableList[ContentExecutor.current].orders - 1;
				ContentExecutor.contentObjectTableList[index].studycount++;

				$('#studyCount_' + index).html("학습횟수 : " + ContentExecutor.contentObjectTableList[index].studycount);
				
				//Element.update($('studyCount_' + index), "학습횟수 : " + ContentExecutor.contentObjectTableList[index].studycount);
			} else {
				this.lessonProgress = ContentExecutor.lessonProgress;
			}
			
			this.mInitializedState = true;
			
			result = "true";
		}
		return result;
	}
	
	this.Terminate = function (param) {
		if (this._Debug) {
			alert("*********************\nIn API::terminate\n*********************");
			alert("param : " + param);
		}
		
		var result = "false";  
		
		if (!this.mTerminatedState) {
			this.mTerminatedState = true;
			if (ContentExecutor.otype != "N") {
				var progress = new JMap();
				progress.put("grcode",			this.progress.grcode);
				progress.put("subj",			this.progress.subj);
				progress.put("year",			this.progress.year);
				progress.put("subjseq",			this.progress.subjseq);
				progress.put("vclass",			this.progress.vclass);
				progress.put("module",			this.progress.module);
				progress.put("lesson",			this.progress.lesson);
				progress.put("oid",				this.progress.oid);
				progress.put("sequence",		this.progress.sequence);
				progress.put("studycount",		this.progress.studycount);
				progress.put("history",			this.progress.history);
				progress.put("itemCount",		ContentExecutor.contentObjectTableList.length);
				progress.put("totalTime",		this.progress.totalTime);
				progress.put("sessionTime",		this.progress.sessionTime);
				progress.put("otype",			ContentExecutor.otype);
				
				var returnTime = null;
				
				if (ContentExecutor.learningType == "1") {
					returnTime = dwrUtil.invokeMap(progress.toMap(), {beanId:"usr.obcWrapperDWR", method:"terminate"});
				} else {
					returnTime = dwrUtil.invokeMap(progress.toMap(), {beanId:"usr.obcPreviewWrapperDWR", method:"terminate"});
				}
				if (returnTime != 0 && !ContentExecutor.exit) {
					var contentObjectTable = ContentExecutor.contentObjectTableList;
					if (contentObjectTable != null) {
						for (var i = 0; i < contentObjectTable.length; i++) {
							if (contentObjectTable[i].seq == this.progress.sequence) {
								ContentExecutor.contentObjectTableList[i].totalTime = returnTime;
								//Element.update($('totalTime_' + i), "학습시간 : " + calStudyTime(returnTime));
								$('#totalTime_' + i).html("학습횟수 : " + calStudyTime(returnTime));
								break;
							}
						}
					}
				}
			} else {
				var lessonProgress = new JMap();
				lessonProgress.put("grcode",			this.lessonProgress.grcode);
				lessonProgress.put("subj",				this.lessonProgress.subj);
				lessonProgress.put("year",				this.lessonProgress.year);
				lessonProgress.put("subjseq",			this.lessonProgress.subjseq);
				lessonProgress.put("vclass",			this.lessonProgress.vclass);
				lessonProgress.put("module",			this.lessonProgress.module);
				lessonProgress.put("lesson",			this.lessonProgress.lesson);
				lessonProgress.put("oid",				this.lessonProgress.oid);
				lessonProgress.put("studycount",		this.lessonProgress.studycount);
				lessonProgress.put("history",			this.lessonProgress.history);
				lessonProgress.put("totalTime",			ContentExecutor.lessonProgress.totalTime);
				lessonProgress.put("otype",				ContentExecutor.otype);
				
				dwrUtil.invokeMap(lessonProgress.toMap(), {beanId:"usr.obcWrapperDWR", method:"terminate"});
			}
			
			result = true;
			
			this.mInitializedState = false;
			this.mTerminatedState = false;;
			this.progress = null;
			this.lessonProgress = null;
			
			if (ContentExecutor.exit) {
				opener.CtlExecutor.doStudyroomLessonReload();
			}
		}
		
		return result;
	}
	
	
	this.GetValue = function (iDataModelElement) {
		if (this._Debug) {
			alert("*******************\n" + "In API::GetValue\n" + "*******************\n" + "GetValue Parameter:" + iDataModelElement);
		}
		var result = "";
		
		if (this.mInitializedState) {
			if (iDataModelElement == "location" || iDataModelElement == "history") {
				if (ContentExecutor.otype != "N") {
					result = this.progress.history;
				} else {
					result = ContentExecutor.lessonProgress.history;
				}
			}
		}
		
		return result;
	};
	
	this.SetValue = function (iDataModelElement, iValue) {
		if (this._Debug) {
			alert("iDataModelElement::: " + iDataModelElement + " iValue::: " + iValue);
		}
		var result = "false";
		if (iDataModelElement == "location" || iDataModelElement == "history") {
			if (ContentExecutor.otype != "N") {
				this.progress.history = iValue;
			} else {
				this.lessonProgress.history = iValue;
			}
			result = "true";
		}
		
		return result;
	};
	
	this.setNAVIInterface = function (item_id) {
		if (this._Debug) {
			alert("*******************\n" + "In API::setNAVIInterface\n" + "*******************\n" + "setNAVIInterface item_id:" + item_id);
		}
		var result = "false";
		var contentObjectTable = ContentExecutor.contentObjectTableList;
		if (contentObjectTable == null) {
			return result;
		}
		for (var i = 0; i < contentObjectTable.length; i++) {
			if (contentObjectTable[i].itemid == item_id) {
				ContentExecutor.changeContent(i);
				result = "true";
				break;
			}
		}
		
		return result;
	}
	
	this.setLMSInterface = function (name) {
		var result = "false";
		if (this._Debug) {
			alert("*******************\n" + "In API::setLMSInterface\n" + "*******************\n" + "setLMSInterface name:" + name);
		}
		return PopExecutor.doPopupStart(name);
	}
}

