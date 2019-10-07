function DWRKERISAPI()
{

	this.init = function() {

	}

   	this.isInitialized = function() {
      	return true;
   	}
   	
   	this.KSSInitialize = function(param) {
   		return true;
   	}
   	
   	this.KSSGetValue = function(iDataModelElement, page)
   	{
   	var result = '';
      	return result;
   	}

   	this.KSSSetValue = function(iDataModelElement,page, iValue)
   	{
      		// Assume failure
      	var result = this.mStringFalse;
		return result;
   	}
   	
   	this.KSSGetLastError = function()
   	{
      	var result = '';
      	return result;
   	}
   	
   	this.GetErrorString = function(errorCode)
   	{
      	var result = '';
      	return result;
   	}
   	
   	this.GetDiagnostic = function(errorCode)
   	{
   		var result = '';
      	return result;
   	}

	this.KSSLaunchService = function(iDataModel, page, style)
	{
		if(iDataModel=="keris.kss.board"){
			//goArticle();
			if(class_seq == 'null' || class_course_seq == 'null'){
				alert('게시판 기능을 사용할 수 없습니다');
				return true;
			}			
			cooperationWork.getLcmsCourseBbsSeq(class_seq , class_course_seq , bbsCallBack);					
		}else if(iDataModel=="keris.kss.board[QNA]"){
			//goArticle();
			if(class_seq == 'null' || class_course_seq == 'null'){
				alert('게시판 기능을 사용할 수 없습니다');
				return true;
			}
			cooperationWork.getLcmsCourseBbsSeq(class_seq , class_course_seq , bbsCallBack);		
		}else if(iDataModel=="keris.kss.memo"){
			var gourls=_contextname+"/lcmsMemo/mainListLcms.action?decorator=popup&confirm=true";		
			window.open(gourls,'parentIdfind_pop','top=10,left=10,status=yes, scrollbars=yes,resizable=no,width=830, height=400');
		}else if(iDataModel=="keris.kss.bookmark"){
			if(class_seq == 'null' || class_course_seq == 'null' || lecture_seq == 'null'){
				alert('북마크 기능을 사용할 수 없습니다');
				return true;
			}
			var action_p = "courseGetgroupname.action?decorator=ajax&confirm=true";
			sendRequest(getgroupname,'&org_seq='+org_seq,'GET',''+ action_p +'',true,true);	
			//registBookMark();	
		}else if(iDataModel=="keris.kss.comment"){
			var gourls=_contextname+"/opinionMain.action?decorator=popup&confirm=true" ;		
			window.open(gourls,'parentIdfind_pop','top=10,left=10,status=yes, scrollbars=yes, resizable=yes ,width=830, height=700');		
		}
		return true;
	}
	function getgroupname(group_name){
		courseStudyWork.customCourseLectureSave(class_seq, class_course_seq, lecture_seq, group_name.responseText.replace(/(^\s*)|(\s*$)/g, ""), registBookMarkCallback);
	}
	function bbsCallBack(data){
		if(data != null){
			var gourls=_contextname+"/course/bbsArticleMain.action?decorator=popup&confirm=true&bbs_seq="+data+"&class_seq="+class_seq+"&class_course_seq="+class_course_seq+"&lecture_seq="+lecture_seq;		
			window.open(gourls,'parentIdfind_pop','top=10,left=10,status=yes, scrollbars=yes, resizable=yes ,width=830, height=700');			  				
					
		}else{
			alert("게시판 로딩에 실패 했습니다.");
		}
	}
	
	//수정/삭제(결과)
	function registBookMarkCallback(data){
		var result = data;
	
		if(result == "input") {
			alert("이미 맞춤 수강으로 등록되어 있어서 중복 등록 하실수 없습니다.");
			return; 
		}
		
		if(result == "success"){
			alert("맞춤 수강 등록에 성공 하였습니다.");
			return; 
			
	  	}else{
	  		alert("열린 강좌에서는 맞춤 수강 등록을 하실 수 없습니다.");
	  		return;
	  	}		
	}	
}