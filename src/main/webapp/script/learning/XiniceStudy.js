var ContentExecutor = {
	_Debug: false,	
	lessonProgress : null,
	contentTitle : null,
	defaultPath : tempPath,
	current : '0',
	exit : false,
	learningType : null,
	startfile : null,
	contentType : null,
	otype : null,
	totalTime: 0,
	playTime:0,
	sessionTime: 0,
	setXinice:'N',
	lessonstatus: 'N',
	lessonCnt:0,
	limit : 'PLAY',
	init : function(result, learningType, otype) {
		this.learningType = learningType;
		this.otype = otype;
		
		// 콘텐츠 타입 셋팅
		this.lessonProgress = result.lesson.eduTime;
		
		// 콘텐츠타이틀 세팅
		this.contentTitle   = result.lesson.contentTitle;
		
		//this.defaultPath    = result.defaultPath;
		this.startfile      = result.lesson.starting;
		this.contentType    = result.lesson.contentType;
		

		if(result.lesson.lessonCnt != 0  && result.lesson.sessionTime != "undefined")
			this.lessonCnt = result.lesson.lessonCnt;
		
		if(result.progress != null && result.progress != "undefined"){
			if(result.progress.totalTime != "")
				this.totalTime = result.progress.totalTime;
			if(result.progress.sessionTime != "")
				this.sessionTime = result.progress.sessionTime;
			if(result.progress.lessonstatus != "" )
				this.lessonstatus = result.progress.lessonstatus;
		}
		
		this.changeContent();
	   //	myinit();
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
	changeContent : function() {
		if(!isLogin(map)) {
			self.close();
			return;
		};
		
		//var startFile =  this.defaultPath + this.startfile ;
		if( this.lessonstatus == null || this.lessonstatus == 'N' ){
			var limit = this.checkEduLimit();
			if( limit && limit.eduLimit == 'STOP' ){
				this.limit = limit.eduLimit;
				alert("1일 학습진도 제한을 초과하였습니다.");
				self.close();
				return;
			}
		}
		
		var repley ="?startat="+this.sessionTime;
		var startFile =  this.defaultPath + this.startfile + repley;
		
		if (this.contentType == "VIDEO") {
			var url = '/player.do?starting=' + this.lessonProgress.history + '&pop=true';
			$('#content').attr('src', url);
		} else {
			$('#content').attr('src', startFile);
		}
		
		//this.makeItemTree();
	},
	
	makeItemTree : function(){
		var HTML = "";
			HTML += "<dt class='dtStart select'>" + this.contentTitle + "</dt>"
			
		var studyCount 			= (this.lessonProgress.studycount == undefined ? 0 : this.lessonProgress.studycount);
		var totalTime 			= (this.lessonProgress.totalTime == '0' ? '0' : this.lessonProgress.totalTime);
		var lessonstatustext 	= (this.lessonProgress.lessonstatustext == '' ? '미완료' : this.lessonProgress.lessonstatustext);	
			
		HTML += "<dd id='studyCount'>학습횟수 : " + studyCount + "</dd>";

		HTML += "<dd id='totalTime' class='ddEnd'>학습시간 : " + calStudyTime(totalTime) + "</dd>";
		$('#contentItemList').html(HTML);
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
		if ( this._Debug ){
			alert("onExit");
		    alert("OLD_totalTime   : "+this.totalTime);
		    alert("OlD_sessionTime : "+this.sessionTime); 
		    alert("New_sessionTime : "+document.content.getWMPPosition()); 
		    alert("New_totalTime   : "+document.content.getWMPDuration()); 
		}
		var tempPlayTime =  this.playTime;
		var temptotalTime = this.totalTime;
		if( this.setXinice == 'N' ){
			tempPlayTime =  document.content.getWMPPosition();
			temptotalTime = document.content.getWMPDuration();
			//alert(tempPlayTime);
		}
		//alert( tempPlayTime + " : " + temptotalTime );
	   if(this.lessonstatus != "Y") {
		   if(tempPlayTime >= temptotalTime){
			   map.put("lessonstatus","Y");
		   }else{
			   map.put("lessonstatus","N");   
		   }
		   map.put("oldTime",     this.sessionTime);
	       map.put("sessionTime", tempPlayTime);
	       map.put("totalTime",   temptotalTime);
	       map.put("location",this.startfile);
	       map.put("lessonCnt",this.lessonCnt);
	       
		    
			var result = ajaxCall({
				url: context+'/com/lcms/len/updateXiniceProgress.do',
			    async : false,
			    type: 'POST',
			    ajaxId : "",
			    method : "",
			    data   : map,
			    validationForm : ""
			});
	   }
	   
	   try{
		   opener.doPageReload();
	   }catch(e){}
 	}
}
