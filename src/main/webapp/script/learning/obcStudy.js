var ContentExecutor = {
	contentObjectTableList : null,
	contentTitle : null,
	defaultPath : '',
	current : '0',
	exit : false,
	learningType : null,
	contentType : null,
	otype : null,
	init : function(result, learningType, otype) {
		this.learningType = learningType;
		
		this.otype = otype;
		
		// 콘텐츠 타입 셋팅
		this.contentObjectTableList = result.contentObjectTableList;
		// 콘텐츠타이틀 세팅
		this.contentTitle = result.contentTitle;
		// 마지막 콘텐츠 캐팅
		var lastSeqValue = 0;
		this.defaultPath = result.defaultPath;
		
	   	if (result.lastSeq != 0) {
	   		for (var i = 0 ; i < this.contentObjectTableList.length ; i++) {
	   			if (result.lastSeq == this.contentObjectTableList[i].seq) {
	   				lastSeqValue = this.contentObjectTableList[i].orders - 1;
	   				break;
	   			}
	   		}
	   	} 
		this.changeContent(lastSeqValue, true);
	   	myinit();
	},
	
	changeContent : function(index) {
		var startFile =  this.defaultPath + this.contentObjectTableList[index].startfile;
		this.makeItemTree(index);
		this.current = index;
		//$('content').src = startFile;
		$('#content').attr('src', startFile);
		clearTime(this.contentObjectTableList[index].totalTime);
	},
	
	makeItemTree : function(index){
		var HTML = "";
		for (var i = 0 ; i < this.contentObjectTableList.length ; i++) {
			if (i == 0) {
				if (index == this.contentObjectTableList[i].orders - 1) {
					HTML += "<dt class='dtStart select'>";
				} else {
					HTML += "<dt class='dtStart'>";
				}
			} else {
				if (index == this.contentObjectTableList[i].orders - 1) {
					HTML += "<dt class='dtMiddle select'>";
				} else {
					HTML += "<dt class='dtMiddle'>";
				}
			}
			HTML += "<a href=\"javascript:ContentExecutor.changeContent('" + i + "')\">" + this.contentObjectTableList[i].title + "</a>";
			HTML += "</dt>";
			
			
			var studyCount 			= (this.contentObjectTableList[i].studycount == undefined ? 0 : this.contentObjectTableList[i].studycount);
			var totalTime 			= (this.contentObjectTableList[i].totalTime == '0' ? '0' : this.contentObjectTableList[i].totalTime);
			var lessonstatustext 	= (this.contentObjectTableList[i].lessonstatustext == '' ? '미완료' : this.contentObjectTableList[i].lessonstatustext);
			
			
			HTML += "<dd id='studyCount_" + i + "'>학습횟수 : " + studyCount + "</dd>";
//			if (i +1  ==  this.contentObjectTableList.length) {
//				HTML += "<dd id='lessonstatustext_" + i + "' class='ddEnd'> &nbsp;&nbsp;학습상태 : " + lessonstatustext + "</dd>";
//			} else {
//				HTML += "<dd id='lessonstatustext_" + i + "'> &nbsp;&nbsp;학습상태 : " + lessonstatustext + "</dd>";
//			}
			if (i +1  ==  this.contentObjectTableList.length) {
				HTML += "<dd id='totalTime_" + i + "' class='ddEnd'>학습시간 : " + calStudyTime(totalTime) + "</dd>";
			} else {
				HTML += "<dd id='totalTime_" + i + "'>학습시간 : " + calStudyTime(totalTime) + "</dd>";
			}
			
		}
		//Element.update($('contentItemList'), HTML);
		$('#contentItemList').html(HTML);
	}
}
