
var ContentExecutor = {
	contentObjectTableList : null,
	contentStudyList: null,
	lessonProgress : null,
	contentTitle : null,
	defaultPath : tempPath,
	currentindex : 0,
	exit : false,
	learningType : null,
	startfile : null,
	contentType : null,
	otype : null,
	lessonstatus:'N',
	pageCount:0,
	nowPageCout:1,
	oldLesson:'',
	limit : 'PLAY',
	limitLessonStatus : 'N',
	init : function(result, learningType, otype) {
	
		this.learningType = learningType;
		this.otype = otype;
		
		// 콘텐츠 타입 셋팅
		this.contentObjectTableList = result.lessonList;
		
		var lastSeq = "";
		this.nowPageCout = 1;  
		var lastSeqValue = 0;
		var lesson = "";
		
		var breakCheck = false;
		
		for (var i = 0 ; i < this.contentObjectTableList.length ; i++) {
			this.contentTitle =  this.contentObjectTableList[i].moduleName;
			if( result.progressList != null && result.progressList.length > 0 ){
				for (var j = 0 ; j < result.progressList.length ; j++) {
					if (result.progressList[j].rn == 1 && result.progressList[j].ordering == this.contentObjectTableList[i].ordering) {
						/*
						if(result.progressList[j].lessonstatus =="N"){
						lastSeqValue = this.contentObjectTableList[i].ordering;
						this.nowPageCout  = result.progressList[j].location;
						lesson = this.contentObjectTableList[i].lesson;
						breakCheck = true;
						break;
					}else{
						if( this.contentObjectTableList[i].progressYn == 'Y' && this.contentObjectTableList[i].starting != null ){
							lastSeqValue = this.contentObjectTableList[i].ordering;
							lesson = this.contentObjectTableList[i].lesson;
							this.nowPageCout  = 1;  
							breakCheck = true;
							break;
						}else{
							break;
						}
					}
						 */
						lastSeqValue = this.contentObjectTableList[i].ordering;
						this.nowPageCout  = result.progressList[j].location == null ? 1 : result.progressList[j].location;
						lesson = this.contentObjectTableList[i].lesson;
						breakCheck = true;
						break;
					}
				}
				if(breakCheck){
					break;
				}
			}else{
				lastSeqValue = this.contentObjectTableList[i].ordering;
				this.nowPageCout  = 1;
				lesson = this.contentObjectTableList[i].lesson;
				break;
			}
			
		}
		
		this.changeContent(lastSeqValue, this.nowPageCout, lesson);
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
	changeContent : function(index, _nowPageCout, lesson) {
		if(!isLogin(map)) {
			self.close();
			return;
		};
		
		var progressData = this.init2(lesson);
		
		this.currentindex = index;
		
		this.pageCount =  this.contentObjectTableList[this.currentindex].pageCount;
		
		this.nowPageCout = _nowPageCout;
		if ( progressData && progressData.location && this.oldLesson != lesson ){
				this.lessonstatus = progressData.lessonstatus;
				this.nowPageCout = progressData.location;
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
		
		
		if( this.oldLesson != lesson ){
			this.oldLesson = lesson;
		}
		
		var startFile =  this.defaultPath + this.contentObjectTableList[this.currentindex].starting;
		
		var tempNowPageCout_1 = this.nowPageCout;
		if( this.lessonstatus == "Y" ){
			this.nowPageCout = 1;
		}
		
		if(this.nowPageCout != 0 ){
			if(this.pageCount > 0 && this.nowPageCout != 1){
				
				var tempStartFile = startFile.substring(0,startFile.lastIndexOf("_")+1);
				var tempExtension = startFile.substring(startFile.lastIndexOf("."),startFile.length);
				
				var tempStartFileNum = parseInt( startFile.substring(startFile.lastIndexOf("_")+1,startFile.lastIndexOf(".")),10) + parseInt(tempNowPageCout_1-1,10);
				
				tempStartFile = tempStartFile + padZero(tempStartFileNum)+ tempExtension;	
				
				startFile =	tempStartFile;
				
			}
		}
		
		
		map.put("lesson",this.contentObjectTableList[this.currentindex].lesson); 
		map.put("location",this.nowPageCout); 
		
		//alert(this.nowPageCout);
		
		this.commit(false);
		
		if(this.lessonstatus != "Y"){
			var progresResult = ajaxCall({
				url: context +'/com/lcms/len/selectCotiProgressCheck.do',  
				async : false,
				type: 'POST',
				ajaxId : "",
				method : "",
				data : map,
				validationForm : ""
			})
			
			this.contentStudyList = progresResult.progressList;
		}
		
		//this.lessonstatus = 'N';
		
		var prev = null;
		var prevLesson = lesson;
		var nextLesson = lesson;
		var now  = new Array(this.nowPageCout,this.pageCount);
		var idx = index;
		if(this.currentindex == 0 && this.nowPageCout == 1){
			prev = new Array(0,0);
		}else{
			if(this.nowPageCout == 1){
				var  tempPrevIndex = this.currentindex;
				while( 0 < tempPrevIndex){
					tempPrevIndex--;
					
					if( this.contentObjectTableList[tempPrevIndex].progressYn == 'Y' && this.contentObjectTableList[tempPrevIndex].starting != null ){
						prev = new Array(tempPrevIndex,this.contentObjectTableList[tempPrevIndex].pageCount);		
						break;
					}
					
				}
				while( true ){
					if( this.contentObjectTableList[--idx] && this.contentObjectTableList[idx].starting != null ){
						prevLesson = this.contentObjectTableList[idx].lesson;
						break;
					}
				}
				
			} else{
				var prevNowPageCout = this.nowPageCout;
				prev = new Array(this.currentindex,prevNowPageCout-1);
			}
		}
		var  tempNextIndex = this.currentindex; 
		var  nextNowPageCout = this.nowPageCout;
		var next = null;
		if(tempNextIndex == this.contentObjectTableList.length -1 && this.pageCount == nextNowPageCout){  
			next = new Array(0,0);
		}else{
			idx = index;
			if(this.pageCount > nextNowPageCout){
				next = new Array(tempNextIndex, ++nextNowPageCout);
			} else{
				
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
		}
		
		
		$('#content').attr('src', startFile);
		
		this.SCOButton(prev, next, now, "", prevLesson, nextLesson);
		this.lessonstatus = "N";
	},
	
	buttonProc :function (index, nowPageCout, lesson)
	{
		this.changeContent(index, nowPageCout, lesson);
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
		
		$("#pageNum").html(pageNumView);
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
		
		this.commit(true);
		
		try{
			opener.doPageReload();
		}catch(e){
		}
	},
	commit :function (gubun)
	{
		if(!isLogin(map)) {
			self.close();
			return;
		};
		
		map.put("gubun", gubun);
		var progResult = 0;
		if(this.lessonstatus != "Y" || gubun){
			if( this.contentObjectTableList[this.currentindex].progressYn == 'Y' && this.contentObjectTableList[this.currentindex].starting != null ){
				if(this.pageCount == this.nowPageCout){
					this.lessonstatus = "Y";
					if( this.limit == 'STOP' && this.lessonstatus == "Y" ){
						this.lessonstatus = "N";
					}
				}
				map.put("lessonstatus",this.lessonstatus);
				progResult = ajaxCall({
					url: context +'/com/lcms/len/insertCotiProgress.do',  
					async : false,
					type: 'POST',
					ajaxId : "",
					method : "", 
					data : map,
					validationForm : ""
				})
				
			}
		}
		
		var checkResult = ajaxCall({
			url: context +'/com/lcms/len/selectCotiProgressCheck.do',  
			async : false,
			type: 'POST',
			ajaxId : "",
			method : "",
			data : map,
			validationForm : ""
		})
		
		this.contentStudyList = checkResult.progressList;
			
		if( this.limit == 'STOP' && this.limitLessonStatus != "Y" ){
			alert("1일 학습진도 제한을 초과하였습니다.");
			self.close();
			return;
		}
		
		dTreeInit(this.contentObjectTableList,this.contentTitle,'contentItemList',1, this.currentindex,this.contentStudyList);
	}
}