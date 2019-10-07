Event.observe(window, 'load', function(){
  	var result = dwrUtil.invokeMap( map.toMap(), { beanId: "adm.contentObjectobjDWR", method: "getContentObjectTableList"});
  	if (result.contentType == 'N') 
  		NonObjectContentExecutor.init(result);
  	else if (result.contentType == 'O') 
  		ObjectContentExecutor.init(result);
  	else if (result.contentType == 'S') 
  		ScormObjectContentExecutor.init(result);
});

// OBC 콘텐츠 사용
var ObjectContentExecutor = {
	current : 0,
	contentObjectTableList : null,
	progressList : null,
	subjmoduleList : null,
	defaultPath : null,
	progressRate : 0,
	onunload : false,
	contentTitle : null,
	init : function(result) {
	  	// 기본 정보 세팅 시작
	  	// 콘텐츠 목차목록 셋팅
	  	for (var i = 0 ; i < result.contentObjectTableList.length ; i++) {
	  		result.contentObjectTableList[i].totalStudyCount = 0;
	  		result.contentObjectTableList[i].totalStudyTime = 0;
	  		result.contentObjectTableList[i].status = '';
	  	}
	  	
	  	this.contentObjectTableList = result.contentObjectTableList;
	  	// 진행률 세팅
	  	this.progressList = result.progressList;
	  	// 목차 세팅
	  	this.subjmoduleList = result.subjmoduleList;
	  	// 콘텐츠타이틀 세팅
	   	this.contentTitle = result.contentTitle;
	  	// 콘텐츠 기본 경로 세팅
	  	this.defaultPath = result.defaultPath;
	  	
	  	// 마지막 콘텐츠 캐팅
	  	var lastSeqValue = 0;
	   	if (result.lastSeq != null) {
	   		for (var i = 0 ; i < this.contentObjectTableList.length ; i++) {
	   			if (result.lastSeq == this.contentObjectTableList[i].seq) {
	   				lastSeqValue = this.contentObjectTableList[i].orders - 1;
	   				this.current = lastSeqValue - 1;
	   				break;
	   			}
	   		}
	   	}
	  	// 콘텐츠 변경 세팅
	  	this.changeContent(lastSeqValue, true);
		// 기본 정보 셋팅 종료
		// 레이어 보이기
		Element.show($('Layer1'));
		// 레이어 슬라이드
		StudyrooomMenuExecutor.doLeftMenuHide($('Layer1'), $('Layer2'), '-210', '2.0');
		// Time Object 설정
		myinit();
		// 시간 초기화
		clearTime(0);
	},
	changeContent : function(index, iniStatus) {
		// 버튼
		if (index == 'prev')
			index = this.current - 1;
		else if (index == 'next')
			index = this.current + 1;
		
		var startFile =  this.defaultPath + this.contentObjectTableList[index].startfile;
		
		this.contentObjectTableList[index].totalStudyCount++;
		
		if (iniStatus == undefined) {
			this.contentObjectTableList[this.current].totalStudyTime += getSeeionTime();
		}
		
		this.makeItemTree();
		
//		$(map.get("oid") + '_' + this.contentObjectTableList[index].orders).innerHTML = this.contentObjectTableList[index].title;
		$(map.get("oid") + '_' + this.contentObjectTableList[index].orders).innerHTML = '<strong><font color="F87432">' + $(map.get("oid") + '_' + this.contentObjectTableList[index].orders).innerHTML + '&nbsp;&#8592;진행중</font></strong>';
		$('content').src = startFile;
		
		this.current = index;
		
		this.doNavigation(index);
		
		clearTime(this.contentObjectTableList[this.current].totalStudyTime);
	},
	
	doNavigation : function(index) {
		if (index == 0) {
			Element.hide($('prev'));
			Element.show($('next'));
			$('next').onclick = function() {ObjectContentExecutor.changeContent('next');}
		} 
		if (index == this.contentObjectTableList.length - 1) {
			Element.hide($('next'));
			Element.show($('prev'));
			$('prev').onclick = function() {ObjectContentExecutor.changeContent('prev');}
		}
		if  (index != 0 && index != this.contentObjectTableList.length - 1) {
			Element.show($('prev'));
			Element.show($('next'));
			$('next').onclick = function() {ObjectContentExecutor.changeContent('next');}
			$('prev').onclick = function() {ObjectContentExecutor.changeContent('prev');}
		}
	},
	
	makeItemTree : function(){
		var HTML = '<table border="0" cellspacing="0" cellpadding="0">';
       	    HTML += '<tr>';
		    HTML += '<td id="contentTitle" name="contentTitle" class="listLevel01">' + this.contentTitle + '</td>';
		    HTML += '</tr>';
       	for (var i = 0 ; i < this.contentObjectTableList.length ; i++) {
       		var URL = "'" + this.defaultPath + this.contentObjectTableList[i].startfile + "'";
			HTML += '<tr>';
			HTML += '	<td class="listLevel02"><a id="' + map.get("oid") + '_' + this.contentObjectTableList[i].orders + '" name="' + map.get("oid") + '_' + this.contentObjectTableList[i].orders + '" ';
			HTML += '		href="javascript:ObjectContentExecutor.changeContent(' + i + ')">' + this.contentObjectTableList[i].title + '</a>';
			HTML += '		<br>학습횟수 : ' + this.contentObjectTableList[i].totalStudyCount;
			HTML +=	'  		<br>학습시간 : ' + calStudyTime(this.contentObjectTableList[i].totalStudyTime);
//			HTML += '		<br>학습상태 : ' + this.contentObjectTableList[i].status;
			HTML += '	</td>';
			HTML += '</tr>';
       	}
       	HTML += '</table>';
       	
       	Element.update($('contentItemList'), HTML);
	}
}

// 일반 콘텐츠 사용
var NonObjectContentExecutor = {
	current : 0,
	contentObjectTableList : null,
	progressList : null,
	subjmoduleList : null,
	defaultPath : null,
	progressRate : 0,
	contentTitle : null,
	startfile : null,
	init : function(result) {
		// 콘텐츠타이틀 세팅
	   	this.contentTitle = result.contentTitle;
	  	// 콘텐츠 기본 경로 세팅
	  	this.defaultPath = result.defaultPath;
		// 레이어 보이기
		Element.show($('Layer1'));
		// 레이어 슬라이드
		StudyrooomMenuExecutor.doLeftMenuHide($('Layer1'), $('Layer2'), '-210', '2.0');
		this.startfile = result.contentObject.starting;
		this.changeContent();
		myinit();
		// 시간 초기화
		clearTime(0);
	},
	changeContent : function() {
		$('content').src = this.defaultPath +  this.startfile;
		this.doNavigation();
		this.makeItemTree();
	},
	doNavigation : function(index) {
		Element.hide($('prev'));
		Element.hide($('next'));
	},
	makeItemTree : function(){
		var HTML = '<table border="0" cellspacing="0" cellpadding="0">';
       	    HTML += '<tr>';
		    HTML += '<td id="contentTitle" name="contentTitle" class="listLevel01"><strong><font color="F87432">' + this.contentTitle + '&nbsp;&#8592;진행중</font></strong></td>';
		    HTML += '</tr>';
       		HTML += '</table>';
       	
       	Element.update($('contentItemList'), HTML);
	}
}

// SCORM 콘텐츠 사용
var ScormObjectContentExecutor = {
	current : 0,
	contentObjectTableList : null,
	progressList : null,
	subjmoduleList : null,
	defaultPath : null,
	progressRate : 0,
	contentTitle : null,
	startfile : null,
	init : function(result) {
		// 콘텐츠타이틀 세팅
	   	this.contentTitle = result.contentTitle;
	  	// 콘텐츠 기본 경로 세팅
	  	this.defaultPath = result.defaultPath;
		// 레이어 보이기
		Element.show($('Layer1'));
		// 레이어 슬라이드
		StudyrooomMenuExecutor.doLeftMenuHide($('Layer1'), $('Layer2'), '-210', '2.0');
		this.startfile = result.contentObject.starting;
		this.changeContent();
		myinit();
		// 시간 초기화
		clearTime(0);
	},
	changeContent : function() {
		$('content').src = this.defaultPath +  this.startfile;
		this.doNavigation();
		this.makeItemTree();
	},
	doNavigation : function(index) {
		Element.hide($('prev'));
		Element.hide($('next'));
	},
	makeItemTree : function(){
		var HTML = '<table border="0" cellspacing="0" cellpadding="0">';
       	    HTML += '<tr>';
		    HTML += '<td id="contentTitle" name="contentTitle" class="listLevel01"><strong><font color="F87432">' + this.contentTitle + '&nbsp;&#8592;진행중</font></strong></td>';
		    HTML += '</tr>';
       		HTML += '</table>';
       	
       	Element.update($('contentItemList'), HTML);
	}
}



