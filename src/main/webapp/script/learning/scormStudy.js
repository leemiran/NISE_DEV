var ContentExecutor = {
		contentTitle : null,
		exit : false,
		learningType : null,
		contentType : null,
		otype : null,
		inputMap : null,
		default_req:"",
		lms_code:"",
		flag : false,
		scoId: "",
		_Debug: false,

		prevButton:true,
		nextButton:true,
		quitButton:true,
		suspendButton:true,
		
		init : function(map, learningType, otype) {
       if ( this._Debug )
	         alert("init");

			this.learningType = learningType;
			this.otype        = otype;
			this.inputMap     =  map;

			this.startClearSession()
		    API_1484_11  = new SCORMAPI();
		    this.changeContent(this.scoId);		  
		},
		checkEduLimit : function(){
			var eduLimit = ajaxCall({
				url : context + '/com/aja/lcm/checkScormEduLimit.do',
				async : false,
				type : 'POST',
				ajaxId : "",
				method : "",
				data : this.inputMap,
				validationForm : ""
			})
			return eduLimit;
		},
		changeContent : function(scoId) {
			if(!isLogin(map)) {
				self.close();
				return;
			};
			
		    if ( this._Debug )
			 alert("changeContent");
		    

			this.inputMap.put("scoId",scoId);
			//alert("changeContent : " +this.inputMap);
   			var result = ajaxCall({
					url: context+'/com/aja/lcm/getContentsURL.do',
				    async : false,
				    type: 'POST',
				    ajaxId : "",
				    method : "",
				    data : this.inputMap,
				    validationForm : ""
   			});

			var classroomName = 'test';

			this.SettingContent(result);
 
		},
		
		SettingContent :function(result) {
			if ( this._Debug )
			    alert("SettingContent");
			 
			if ( result != null && result != undefined){
				if(result.itemURL != null && result.itemURL !='null' &&result.itemURL != 'NOT MATCHED'){
					var url = result.itemURL;
					$('#content').attr('src', url);
					$('#test1').val(url);
				}else{
					alert("itemURL No");
				}
			}
			this.inputMap.put("itemId",result.itemId);
			this.flag = true;	
			var classroomName = 'test';
			dTreeInit(result.treeBean,classroomName,'contentItemList',1);
			
			req_apply_seq=this.default_req+"|"+result.req_apply_seq+"|"+this.lms_code;
			this.SCOButton(result.prevButton, result.nextButton, result.quitButton, result.suspendAllButton);
			var limit = this.checkEduLimit();
			if( limit && limit.eduLimit == 'STOP' ){
				this.limit = limit.eduLimit;
				alert("1일 학습진도 제한을 초과하였습니다.");
				self.close();
				return;
			}
			
			//divObj.onBeforeUnload=divObj.onUnload;	
		},
		 doChoiceEvent :  function(fSCOID) {
		    //var result;
		    this.changeContent(fSCOID);
		},
		quit : function ()
		{
			if (confirm('학습창을 종료하시겠습니까?'))
			{
				//this.onExit();
				self.close();
			}
		},
		onExit : function ()
	 	{
		    if ( this._Debug )
			alert("onExit");
		    
   			var result = ajaxCall({
				url: context+'/com/lcms/len/insertScormProgress.do',
			    async : false,
			    type: 'POST',
			    ajaxId : "",
			    method : "",
			    data : this.inputMap,
			    validationForm : ""
			});
		    
		    
	 		API_1484_11.Terminate();
			closeEvent = true;
			if (!API_1484_11.mInitializedState) {
				this.clearSession();
			}
			try{
				opener.doPageReload();
			}catch(e){
			}
	 	},
	 	clearSession :function ()
	 	{
	 		if ( this._Debug )
	 		alert("clearSession");
	 		
	 		if(this.flag){
		   		var result = ajaxCall({
					url: context+'/com/aja/lcm/clearSession.do',
				    async : false,
				    type: 'POST',
				    ajaxId : "",
				    method : "",
				    data : this.inputMap,
				    validationForm : ""
	   			});	
		   	
			}
	 	},
	 	startClearSession :function ()
	 	{
	 		if ( this._Debug )
	 		alert("startClearSession");
	 		
	   		var result = ajaxCall({
				url: context+'/com/aja/lcm/startClearSession.do',
			    async : false,
			    type: 'POST',
			    ajaxId : "",
			    method : "",
			    data : this.inputMap,
			    validationForm : ""
   			});	
	 	},
	 	buttonProc :function (buttonType)
		{
			var prev_contextname = context+'/images/learn/btn_pre.gif';
			var next_contextname = context+'/images/learn/btn_next.gif';
			if (prevButton) {
				var prev = "<img src='"+prev_contextname+"' border='0' alt='이전'>";
			}
			if (nextButton) {
				var next = "<img src='"+next_contextname+"' border='0' alt='다음'>";
			}
			
			$("#next").html(next);
			$("#prev").html(prev);
			//DWRUtil.setValue("next",next);
			//DWRUtil.setValue("prev",prev);
			this.changeContent(buttonType);
		},
		SCOButton : function (prev, next, quit, suspend) {
			var studyprevbbtn = "<img src='../images/learn/btn_prev.gif' name='Image22' width='23' height='78' border='0'>";
			var studynexttbtn = "<img src='../images/learn/btn_next.gif' name='Image21' width='23' height='78' border='0'>";
			if (prev == "")	{
				prevButton = false;
			}else {
				prevButton = true;
				studyprevbbtn = "<a href=\"javascript:buttonProc('prev');\" onMouseOut='MM_swapImgRestore()' onMouseOver=\"MM_swapImage('Image22','','../images/lcms/images/btn_next_over.gif',1)\">"+
								"<img src='../images/learn/btn_prev.gif' name='Image21' width='23' height='78' border='0'>"+
								"</a>";
			}
			if (next == "")	{
				nextButton = false;
			}else {
				studynexttbtn = "<a href=\"javascript:buttonProc('next');\" onMouseOut='MM_swapImgRestore()' onMouseOver=\"MM_swapImage('Image22','','../images/lcms/images/btn_next_over.gif',1)\">"+
								"<img src='../images/learn/btn_next.gif' name='Image22' width='23' height='78' border='0'>"+
								"</a>";
				nextButton = true;
			}
			if (suspend == "") {
				suspendButton = false;
			}else {
				suspendButton = true;
			}
			if (quit == "") {
				quitButton = false;
			}else {
				quitButton = true;
			}
			$("#prev").html(prev);
			$("#next").html(next);
			$("#quit").html(quit);
		}
		
		
	}








