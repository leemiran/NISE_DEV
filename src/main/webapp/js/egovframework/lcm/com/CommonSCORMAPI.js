// CommonSCORMAPI.jsp start
	var API_1484_11  = null;
	var API_KSS_1001 = null;			
	var gSCOID = "";
	var element = null;
	var select = false;
	var closeEvent = false;
	var course_map = "";
	var subject_cd = "";
	var term = "";
	var year = "";
	var item_seq = ""; 
	var org_seq="";
	var save = "";
	var year = "";
	var gang_id = "";
	var learner_name = "";
	var learner_id = "";
	var lms_code = "";
	var req_apply_seq = "";
	var default_req = "";
	var user_ip = "";
	var flag = false;
	var user_id = "";
	var lms_course_cd="";
	var lms_week_cd="";
	
	var class_seq = "";
	var class_course_seq = "";
	var lecture_seq = "";

// CommonSCORMAPI.jsp end
// CommonUtilAPI.jsp start
	var prevButton = true;
	var nextButton = true;
	var quitButton = true;
	var suspendButton = true;
	var classroomName = "";
	
	var item_id = null;
	
	function initAPI(message)
	{
		alert("난 initAPI 이다 ");
	    var result = null;	    
		DWR_SCORM_Proc.startClearSession();		
	    API_1484_11  = new DWRSCORMAPI();
	    API_KSS_1001 = new DWRKERISAPI();
	    API_KSS_1001 = new DWRKERISAPI();	    
	    LoadingMessage(message);
	    changeContent(gSCOID);		    
	}
	function onExit()
 	{
 		API_1484_11.Terminate();
		closeEvent = true;
		if (!API_1484_11.mInitializedState) {
			clearSession();
		}
		try{
			opener.reload();
		}catch(e){
		}
 	}
 	function clearSession()
 	{
 		if(flag){
	 		DWREngine.setAsync(false);
			DWR_SCORM_Proc.clearSession(user_id,course_map,{
				callback:function(dataFromServer) {
					result = dataFromServer;
				}
			});
			DWREngine.setAsync(true);
		}
 	}
 	
 	function changeContent(fSCOID)
 	{
        alert("난 changeContent 이다"); 
        
        //alert("DWR_SCORM_Proc.getContentsURL : " +user_id+ " " +org_seq + " " + gSCOID + " " + classroomName + " " + course_map + " " + learner_name + " " + lms_course_cd + " " +SettingContent  );
        //alert("SettingContent : "+SettingContent  );
 		DWREngine.setAsync(false);
 		var divObj = $("contentArea");
		var src = "<iframe id='cframenm' name='cframenm' frameborder=0 src='"+_contextname+"'/lcms/loadingMessage.jsp' width = '100%' height = '100%' scrolling='no'></iframe>";

		divObj.innerHTML = "";

		divObj.innerHTML = src;
		divObj = $("cframenm");
//		divObj.src = _contextname+"/lcms/loadingMessage.jsp";
		divObj.src = "about:blank";
	  	gSCOID = fSCOID;
	  	
		DWR_SCORM_Proc.getContentsURL(user_id, org_seq , gSCOID,classroomName, course_map, learner_name,lms_course_cd,SettingContent);
  		DWREngine.setAsync(true);		
 	} 	
 	
 	function SettingContent(result)
 	{
//        if(API_1484_11.mInitializedState==true && API_1484_11.mTerminatedState==false) API_1484_11.Terminate_force(); 	
 		
		var divObj = $("cframenm");
		if ( divObj != null && divObj != undefined)
		{
			if(result.itemURL != null && result.itemURL !='null' &&result.itemURL != 'NOT MATCHED'){
//				API_1484_11.Initialize_force("LCMS Process"); 				
			
		 		var divObj = $("cframenm");
		 		divObj.src = result.itemURL;
		 		item_id = result.item_id;
				flag = true;					
			}else{
			
				divObj.src = _contextname+"/lcms/deleteToc.jsp?course_map="+course_map+"&org_seq="+org_seq+"&learner_id="+learner_id+"&lms_course_cd="+lms_course_cd+"&course_seq="+course_seq+"&subject_cd="+subject_cd+"&term="+term+"&year="+year;
				flag = false;

				setTimeout('time_reload()',2000);
			}
		}
		SCOButton(result.prevButton, result.nextButton, result.quitButton, result.suspendAllButton);
		dTreeInit(result.treeBean,classroomName,'SCORMTree',1);			
		req_apply_seq=default_req+"|"+result.req_apply_seq+"|"+lms_code;
		
		divObj.onBeforeUnload=divObj.onUnload;
 	} 	
 	
	function doChoiceEvent(fSCOID)
	{
	    var result;
	    changeContent(fSCOID);
	    
	}
	function defalutURL(result)
	{
		DWRUtil.setValue("cframenm",result);
	}
	function quit()
	{
		if (confirm('학습창을 종료하시겠습니까?'))
		{
			onExit();
			
			self.close();
		}
	}
	function nextExit() {
		var divObj = $("contentArea");
		if ( divObj != null && divObj != undefined)
		{
			var src = "<iframe id='SCORMSco' name='SCORMSco' frameborder=0 src='"+_contextname+"'/lcms/study/commonAPI/CommonLectureSuccessful.jsp' width = '"+screen.width+"' height = '"+screen.height+"' scrolling=auto style='border:1px solid #AAB3E1'></iframe>";
			divObj.innerHTML = src;
		}

		if (confirm('study exit?')){
			onExit();
			self.close();
		}else {
			return;
		}
	}
	
	function ErrorHandler2(message){
		var divObj = $("cframenm");
		divObj.src = _contextname+"/lcms/deleteToc.jsp?course_map="+course_map+"&org_seq="+org_seq+"&learner_id="+learner_id+"&lms_course_cd="+lms_course_cd+"&course_seq="+course_seq+"&subject_cd="+subject_cd+"&term="+term+"&year="+year;
		flag = false;
		setTimeout('time_reload()',2000);
	}
	DWREngine.setErrorHandler(ErrorHandler2);  