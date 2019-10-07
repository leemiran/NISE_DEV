var ContentExecutor = {
	lessonProgress : null,
	contentTitle : null,
	defaultPath : tempPath,
	current : '0',
	exit : false,
	learningType : null,
	startfile : null,
	contentType : null,
	location: "",
	otype : null,
	currentindex:0,
	contentStudyList:null,
	contentObjectTableList:null,
	lesson : "",
	oldLesson : "",
	playTime  : 0,
	totalTime : 0,
	lessonstatus : 'N',
	limit : 'PLAY',
 	limitLessonStatus : 'N',
 	_Debug : false,
 	
	init : function(result, learningType, otype) {
		this.learningType = learningType;
		this.otype = otype;
		
		// 콘텐츠 타입 셋팅
		this.contentObjectTableList = result.lessonList;
		
		// 콘텐츠 타입 셋팅
		//this.lessonProgress = result.lessonProgress;
		// 콘텐츠타이틀 세팅
		
	    this.location = "0";
		var lastSeqValue = 0;
		
		//레슨 클릭시 
		if(result.lesson.lesson)
		{
			//레슨
			try {
				this.lesson = result.lesson.lesson;
				//lastSeqValue = eval(this.lesson)-1;
			} catch(e)
			{
				//lastSeqValue = 0;
			}
		}
		//alert("this.lesson : " + this.lesson + " / lastSeqValue : " + lastSeqValue);
		
		//this.defaultPath    = result.defaultPath;
		this.startfile      = result.starting;
		this.contentType    = result.contentType;
		
		var breakCheck = false;
		
		if(lastSeqValue == 0)		//학습시작을 누르고 이어보기나 첫번째 로우를 볼때.
		{
	   		for (var i = 0 ; i < this.contentObjectTableList.length ; i++) {
	   			this.contentTitle =  this.contentObjectTableList[i].moduleName;
	   			
	   			if(this.lesson)
	   			{
	   				if(this.lesson == this.contentObjectTableList[i].lesson) {
	   					lastSeqValue = this.contentObjectTableList[i].ordering;
						this.location  = "0";
						this.lesson = this.contentObjectTableList[i].lesson;
						breakCheck = true;
	   				}
	   			}
	   			else
	   			{
					if( result.progressList != null && result.progressList.length > 0 ){
						for (var j = 0 ; j < result.progressList.length ; j++) {
							//alert(result.progressList[j].rn + "/" +result.progressList[j].lesson + "/" +this.contentObjectTableList[i].lesson);
							if (result.progressList[j].rn == 1 && result.progressList[j].lesson == this.contentObjectTableList[i].lesson) {
								lastSeqValue = this.contentObjectTableList[i].ordering;
								this.location  = result.progressList[j].location;
								this.lesson = this.contentObjectTableList[i].lesson;
								breakCheck = true;
								break;
							}
						}
					}else{
						lastSeqValue = this.contentObjectTableList[i].ordering;
						this.location  = "0";
						this.lesson = this.contentObjectTableList[i].lesson;
						breakCheck = true;
						break;
					}
	   			}
				if(breakCheck){
					break;
				}
	   		}
		}
		
		
   		//alert("lastSeqValue : " + lastSeqValue + " / this.location : " + this.location + " / this.lesson : " + this.lesson);
   		
		this.changeContent(lastSeqValue, this.location, this.lesson);
   		//myinit();

	},
	init2 : function(lesson){
		map.put("lesson", lesson);
		var progresResult = ajaxCall({
			url: context +'/com/lcms/len/selectNonScormProgressData.do',  
			async : false,
			type: 'POST',
			ajaxId : "",
			method : "",
			data : map,
			validationForm : ""
		})
		
		return progresResult;
	},
	checkEduLimit : function(){
		var eduLimit = ajaxCall({
			url : context + '/com/lcms/len/checkNonScormEduLimit.do',
			async : false,
			type : 'POST',
			ajaxId : "",
			method : "",
			data : map,
			validationForm : ""
		})
		return eduLimit;
	},
	changeContent : function(index, _location, lesson) {
		if(!isLogin(map)) {
			self.close();
			return;
		};
		
		index = eval(index);
		//alert("index : " + index + "/lesson : " + lesson);
		this.oldLesson = this.lesson;
		var commit_flag = true;
		map.put("lesson", this.contentObjectTableList[this.currentindex].lesson); 
		this.lesson = lesson;
		var progressData = this.init2(this.lesson);
		if ( progressData && progressData.lessonstatus ){ 
			this.lessonstatus = progressData.lessonstatus;
			lessonstatus = progressData.lessonstatus;
		}
		if (this.learningType == "P"){
			commit_flag = false;
			if( this.playTime > 0 && this.totalTime > 0) {
				commit_flag = false;
			}else{
				commit_flag = true;
			}
		}
		this.limitLessonStatus = 'N';
		if( progressData ){
			this.limitLessonStatus = progressData.lessonstatus;
		}
		if( this.lessonstatus == null || this.lessonstatus == 'N' ){
			var limit = this.checkEduLimit();
			if( limit ){
				this.limit = limit.eduLimit;
			}
		}
		
		this.commit(commit_flag, 'N');
		
		
		this.currentindex = index;
		//alert("index " + this.currentindex + " / lesson : " + this.contentObjectTableList[this.currentindex].starting);
		
		
		var startFile =  this.defaultPath + this.contentObjectTableList[this.currentindex].starting;
		
		
		
		
		
		var tempTitle = this.contentTitle;
		
		var eduTime = this.contentObjectTableList[this.currentindex].eduTime;
		//alert("eduTime : " + this.currentindex + "/" + eduTime);
		this.totalTime = eduTime;
		
		map.put("lesson",this.contentObjectTableList[this.currentindex].lesson); 
		//alert("index " + this.currentindex + " / lesson : " + this.contentObjectTableList[this.currentindex].lesson);
		var progresResult = ajaxCall({
			url: context +'/com/lcms/len/selectNormalProgressCheck.do',  
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : map,
		    validationForm : ""
		 })
		 this.contentStudyList = progresResult.progressList;
		 this.location = "0";

		
		 for (var i = 0 ; i < this.contentStudyList.length ; i++) {	 
			 if(this.contentObjectTableList[this.currentindex].module == this.contentStudyList[i].module && this.contentObjectTableList[this.currentindex].lesson== this.contentStudyList[i].lesson){
				 this.lessonstatus = this.contentStudyList[i].lessonstatus;			 
				 this.location = this.contentStudyList[i].location;			 
				 break;
			 }
		 }
		
		if (this.learningType == "P") {

			if(eduTime != null && eduTime != undefined){
			     eduTime = eduTime * 60;
			}else{
				eduTime = 0;
			}
			
			this.totalTime = eduTime;
			var url = context+'/player.do?starting=' + startFile + '&location='+this.location+'&eduTime='+eduTime+'&contentTitle=';
			$('#content').attr('src', url);
		} else {
			$('#content').attr('src', startFile);
		}

	
		
		var prev = null;
		var prevLesson = this.lesson;
		var nextLesson = this.lesson;
		var idx = index;
		
		
		if(this.currentindex == 0){
			
			prev = new Array(0,0);
		}else{
			  var  tempPrevIndex = this.currentindex;
			  while( 0 < tempPrevIndex){
				  tempPrevIndex--;
				  
				  if( this.contentObjectTableList[tempPrevIndex].progressYn == 'Y' && this.contentObjectTableList[tempPrevIndex].starting != null ){
					  prev = new Array(tempPrevIndex,1);		
					  break;
				  }				
			  }
			  while( true ){
				if( this.contentObjectTableList[--idx] && this.contentObjectTableList[idx].starting != null ){
					prevLesson = this.contentObjectTableList[idx].lesson;
					break;
				}
			}
		}
		
		var  tempNextIndex = this.currentindex; 

	    var next = null;
		if(tempNextIndex == this.contentObjectTableList.length -1 ){  
			next = new Array(0,0);
		}else{
			idx = index;
			  while( this.contentObjectTableList.length  > tempNextIndex){
				 
				  tempNextIndex++;
				  
				  if( this.contentObjectTableList[tempNextIndex].progressYn == 'Y' && this.contentObjectTableList[tempNextIndex].starting != null ){
					  next = new Array(tempNextIndex,1);		
					  break;
				  }
				
			  } 
			  while( true ){
				if( this.contentObjectTableList[++idx] && this.contentObjectTableList[idx].starting != null ){
					nextLesson = this.contentObjectTableList[idx].lesson;
					break;
				}
			}
		}
		this.oldLesson = this.lesson;
		dTreeInit(this.contentObjectTableList,this.contentTitle,'contentItemList',1, this.currentindex,this.contentStudyList);
		//clearTime(this.lessonProgress.totalTime);
		this.SCOButton(prev, next, "", "", prevLesson, nextLesson);
	},
	
	changeContent2 : function (index, nowPageCout, lesson){
		if(!isLogin(map)) {
			self.close();
			return;
		};
		//alert(lesson);
		
		 map.put("gubun", true);
		 map.put("lesson", this.oldLesson);
		 map.put("p_end", 'Y');
		 //map.put("lessonstatus","Y");
		var progResult = ajaxCall({
			url: context +'/com/lcms/len/insertNormalProgress.do',  
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : map,
		    validationForm : ""
			})
			
			
		var checkResult = ajaxCall({
			url: context +'/com/lcms/len/selectNormalProgressCheck.do',  
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : map,
		    validationForm : ""
		 })
		 
		this.contentStudyList = checkResult.progressList;
		map.put("sessionTime", this.playTime);
		map.put("totalTime", this.totalTime);
		
		if( this.limit == 'STOP' && this.limitLessonStatus != "Y" ){
		alert("1일 학습진도 제한을 초과하였습니다.");
		self.close();
		return;
		}
		
		dTreeInit(this.contentObjectTableList,this.contentTitle,'contentItemList',1, this.currentindex,this.contentStudyList);
		this.changeContent(index, nowPageCout, lesson);
		document.titletempTitle;
	},
	
 	buttonProc :function (index, nowPageCout, lesson)
	{
		this.oldLesson = this.lesson;
		this.lesson = lesson;
		this.changeContent2(index, nowPageCout, lesson);
	},
	SCOButton : function (prev, next, now, quit, prevLesson, nextLesson) {
		
		var pageNumView ="<font color=\"FFFFFF\" size=\"3\">"+ now[0] +" / " +now[1]+"</font>&nbsp;&nbsp;";

		var prevButton = "<a href='#'><img src='"+ context+"/images/learn/btn_back.gif'  border='0' alt='"+prev[0]+","+prev[1]+"'></a>"; //alt='\uB4A4\uB85C'>";
		if(prev[0] ==0 &&  prev[1]==0 ){
			prevButton = "<a href='#'><img src='"+ context+"/images/learn/btn_back.gif'  border='0' alt='"+prev[0]+","+prev[1]+"'></a>"; //alt='\uB4A4\uB85C'>";
		}else{
			prevButton = "<a href=javascript:ContentExecutor.buttonProc("+prev[0]+","+prev[1]+",'"+prevLesson+"');><img src='"+ context+"/images/learn/btn_back.gif'  border='0' alt='"+prev[0]+","+prev[1]+"'></a>"; //alt='\uB4A4\uB85C'>";
		}
		
		
		var nextButton = "<a href='javascript:ContentExecutor.quit()';><img src='"+ context+"/images/learn/btn_end.gif'  border='0' alt='\uC885\uB8CC'>";
		
		if(next[0] ==0 &&  next[1] ==0 ){
			nextButton = "<a href='javascript:ContentExecutor.quit()';><img src='"+ context+"/images/learn/btn_end.gif'  border='0' alt='\uC885\uB8CC'>";
		}else{
			nextButton = "<a href=javascript:ContentExecutor.buttonProc("+next[0]+","+next[1]+",'"+nextLesson+"');><img src='"+ context+"/images/learn/btn_next.gif'  border='0' alt='"+next[0]+","+next[1]+"'></a>"; // alt='\uC55E\uC73C\uB85C'>";
		}
		
		var quitButton = "<a href='javascript:ContentExecutor.quit()';><img src='"+ context+"/images/learn/btn_end.gif'  border='0' alt='\uC885\uB8CC'>";

		
		//$("#pageNum").html(pageNumView);
		$("#next").html(nextButton);
		$("#prev").html(prevButton);	
	},
	quit : function ()
	{
		if (confirm('학습창을 종료하시겠습니까?'))
		{
			self.close();
		}
	},
	onExit : function ()
 	{
	    if ( this._Debug )
		alert("onExit");

	    this.commit(true, 'Y');

		try{
			opener.doPageReload();
		}catch(e){
		}
 	},
 	commit :function (gubun, endType)
 	{
 		if(!isLogin(map)) {
			self.close();
			return;
		};
		 

		 map.put("gubun", gubun);
		 map.put("p_end", endType);
		 
		 //alert("commit - lesson :"+this.lesson + " / gubun : " + gubun + " / endType : " + endType);
		 
		 
		 //alert(this.lessonstatus +":"+ gubun);
		 if(this.lessonstatus != "Y" || gubun){
			//alert(this.contentObjectTableList[this.currentindex].progressYn +":"+  this.contentObjectTableList[this.currentindex].starting + ":" + this.contentObjectTableList[this.currentindex].progressYn );
			//if( this.contentObjectTableList[this.currentindex].progressYn == 'Y' && this.contentObjectTableList[this.currentindex].starting != null ){
				
				if(this.learningType == "P"){// 동영상일경우 
					if(this.playTime > 0){
						if(this.playTime == this.totalTime){
							$("#"+this.contentObjectTableList[this.currentindex].lesson).html("학습완료");
							map.put("lessonstatus","Y");
							map.put("location",0);
							if( this.limit == 'STOP'){
								map.put("lessonstatus","N");
							}
							
						}else{
							map.put("lessonstatus","N");
							map.put("location",this.playTime);
							
						}
						
						 this.playTime = 0;
						 this.totalTime = 0;
					}else{
						map.put("lessonstatus","N");
					}
				}else{
					map.put("lesson", this.lesson);
					//alert("html : "+$(("#"+this.lesson)).html()+ " :" + this.lesson);
					if( endType == 'Y'){
						$(("#"+this.lesson)).html("학습완료");
						map.put("lessonstatus","Y");
						
					}else{
						$(("#"+this.lesson)).html("학습중");
						map.put("lessonstatus","N");
						
					}
					if( this.limit == 'STOP'){
						$(("#"+this.lesson)).html("학습중");
						map.put("lessonstatus","N");
					}
				}
				//alert("map : " + map);
				
					var progResult = ajaxCall({
						url: context +'/com/lcms/len/insertNormalProgress.do',  
					    async : false,
					    type: 'POST',
					    ajaxId : "",
					    method : "",
					    data : map,
					    validationForm : ""
		   			})

			//}
			
		}
		 var checkResult = ajaxCall({
				url: context +'/com/lcms/len/selectNormalProgressCheck.do',  
			    async : false,
			    type: 'POST',
			    ajaxId : "",
			    method : "",
			    data : map,
			    validationForm : ""
			 })
			 
		 this.contentStudyList = checkResult.progressList;
		 map.put("sessionTime", this.playTime);
		 map.put("totalTime", this.totalTime);
		 
		 if( this.limit == 'STOP' && this.limitLessonStatus != "Y" ){
			alert("1일 학습진도 제한을 초과하였습니다.");
			self.close();
			return;
		 }
		 
		 dTreeInit(this.contentObjectTableList,this.contentTitle,'contentItemList',1, this.currentindex,this.contentStudyList);
		 
 	}
	
}
