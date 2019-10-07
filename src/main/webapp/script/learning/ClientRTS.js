var debugWindow = null;

function SCORMAPI()
{

   /**
    * This controls display of log messages to the java console.
    */
   this._Debug = true;
   
//   if(this._Debug == true) debugWindow=window.open("/debugWindow.jsp","debugWindow","width=1020,height=760,scrollbars=yes,resizable=yes,top=0,left=0");

   /**
    * Indicates if the SCO is in an 'initialized' state.
    */
   this.mInitializedState = false;

   /**
    * String value of FALSE for JavaScript returns.
    */
   this.mStringFalse = "false";

   /**
    * String value of TRUE for JavaScript returns.
    */
   this.mStringTrue = "true";
 
   /**
    * Indicates if the SCO is in a 'terminated' state.
    */
   this.mTerminatedState = false;
   /**
    * Indicates if the SCO is in a 'terminated' state.
    */
   this.mTerminateCalled = false;

   /**
    * Indicates if the current SCO is SCORM Version 1.2.
    */
   this.mSCO_VER_2 = false;

   /**
    * Indicates if the current SCO is SCORM 2004.
    */
   this.mSCO_VER_3 = false;

   this.init = function() {
      // We assume at this point that the user has successfully logged in
      // to the LMS.

      if ( this._Debug ){
    	  this.printWindows("In API::init()(the applet Init method)");
      }
      this.mTerminatedState = false;
      this.mInitializedState = false;
      this.mTerminateCalled = false;
      this.mSCO_VER_2 = false;
      this.mSCO_VER_3 = false;
   }
   
   this.isInitialized = function() {
      if ( ( !this.mInitializedState ) && ( this.mSCO_VER_2 ) ) {
            if ( this._Debug ) {
            	this.printWindows("isInitialized Error.GEN_GET_FAILURE");
           	}
      	   this.setLastError(301);
      }
      return this.mInitializedState;
   }
   
   this.Initialize = function(param) {  
      if(this.mInitializedState==true && this.mTerminatedState==false) this.Terminate(); 
     
		var result = ajaxCall({
			url: context +'/com/aja/lcm/InitializeAPIEM.do',
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : map,
		    validationForm : ""
		});

      // This function must be called by a SCO before any other
      // API calls are made.   It can not be called more than once
      // consecutively unless Terminate is called.
      if ( this._Debug ){
    	  this.printWindows("In API::Initialize " + map);
      }
      // Assume failure
      
      var result = this.mStringFalse;
      
      if ( this.mTerminatedState ){
           if ( this._Debug ){
        	   this.printWindows("Initialize Error.CONTENT_INSTANCE_TERMINATED");
           }
      	  this.setLastError(104);
          return result;
      }
      this.mTerminatedState = false;
      this.mTerminateCalled = false;
      this.mSCO_VER_2 = false;
      this.mSCO_VER_3 = true;

      // Make sure param is empty string "" - as per the API spec
      // Check for "null" is a workaround described in "Known Problems"
      // in the header.
      var tempParm = param;

      if ( typeof(tempParm) == 'undefined' || tempParm != "") {
          if ( this._Debug ){
        	  this.printWindows("Initialize Error MErrorCodes.GEN_ARGUMENT_ERROR");
          }
      	 this.setLastError(201);
      }
      // If the SCO is already initialized set the appropriate error code
      else if ( this.mInitializedState ) {
          if ( this._Debug ){
        	  this.printWindows("Initialize Error APIErrorCodes.ALREADY_INITIALIZED");
          }
      	 this.setLastError(103);
      } else {
// set Session
// DWR_SCORM_Proc.setSessionActivityTree();   
// DMHandler.Initialize(learner_name,	course_map,	org_seq, item_id, save, req_apply_seq,user_ip);
    	  

		var result1 = ajaxCall({
			url: context+'/com/aja/lcm/Initialize.do',
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : map,
		    validationForm : ""
		});

		if( !result1 ){
			alert("1일 학습진도 제한을 초과하였습니다.");
			self.close();
			return result1;
		}

         result = this.mStringTrue;
         
         this.mInitializedState = true;         
      }
      
      if ( this._Debug ){
    	  this.printWindows("Done Processing Initialize()");
      }

      return result;
   }
   
   this.GetValue = function(iDataModelElement) {
	  if(this.mInitializedState != true) return "";

      var result = "";

      // already terminated
      if ( this.mTerminatedState ) {
          if ( this._Debug ){
        	  this.printWindows("GetValue.GET_AFTER_TERMINATE");
          }
      	 this.setLastError(123);
         return result;
      }

      if ( typeof(iDataModelElement) == 'undefined' || iDataModelElement == ""){
          if ( this._Debug ){
        	  this.printWindows("GetValue.GEN_GET_FAILURE");
          }
      	 this.setLastError(301);
         return result;
      }
      if ( this.isInitialized() ){

// Clear current error codes
//       mLMSErrorManager.clearCurrentErrorCode();

		// Process 'GET'
         	var getValueMap = new JMap();
         
         	getValueMap.put("cmiCode",iDataModelElement);
         
		    result = ajaxCall({ 
			url: context +'/com/aja/lcm/getValue.do',
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : getValueMap,
		    validationForm : ""
		    });
		    
         if (result == "") {
             if ( this._Debug ){
            	 this.printWindows("Found the element, but the value was null");
             }
             result = "";
         }

      }
      // not initialized
      else {
           if ( this._Debug ) {
        	   this.printWindows("GetValue Error.GET_BEFORE_INIT");
           }
      	   this.setLastError(122);
      }
      if ( this._Debug ){
    	  this.printWindows("GetValue("+iDataModelElement+" : "+result+")");
      }

      return result;
   }
   
   
   this.SetValue = function(iDataModelElement, iValue) {
	  if(this.mInitializedState != true) return "";
      // Assume failure
      var result = this.mStringFalse;

      // already terminated
      if ( this.mTerminatedState ) {
          if ( this._Debug ){
        	  this.printWindows("SetValue Error.SET_AFTER_TERMINATE");
          }
      	 this.setLastError(133);
         return result;
      }
      // Clear any existing error codes
//    mLMSErrorManager.clearCurrentErrorCode();

     if ( ! this.isInitialized() ) {
        // not initialized
         if ( this._Debug ) {
        	 this.printWindows("SetValue  Error.SET_BEFORE_INIT");
        }
      	this.setLastError(132);
     } else {
         var setValue = null;

         // Check for "null" is a workaround described in "Known Problems"
         // in the header.
         var tempValue = iValue;
         if ( tempValue == null ){
            setValue = "";
         } else {
            setValue = tempValue;
         }
         // Construct the request

         
         /**
		 DMHandler.setValue(iDataModelElement, setValue, {
		 callback:function(dataFromServer) {
		 result = dataFromServer;
		 },async:false
		 });
         **/
         
      	 var  setValueMap = new JMap();
      
      	 setValueMap.put("cmiCode",iDataModelElement);
      	 setValueMap.put("value",setValue);
      
		    result = ajaxCall({
			url: context +'/com/aja/lcm/setValue.do',
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : setValueMap,
		    validationForm : ""
		    });

         //clear MessageCollection
	      if ( this._Debug ){
	    	  this.printWindows("SetValue("+iDataModelElement+" : "+ setValue +" )");
	      }
	      return result;
	   }
   }
   
   this.Terminate = function(iParam) {
	  if(this.mInitializedState != true) return ""; 


      this.mTerminateCalled = true;
      // Assume failure
      var result = this.mStringFalse;

      // already terminated
      if ( this.mTerminatedState )
      {
          if ( this._Debug ) {
        	  this.printWindows("Terminate ErrorCodes.TERMINATE_AFTER_TERMINATE");
          }
      	 this.setLastError(113);
         return result;
      }
      if ( !this.isInitialized() ) {

	      if ( this._Debug ){
	    	  this.printWindows("Terminate ErrorCodes.TERMINATE_BEFORE_INIT");
	      }
	      this.setLastError(112);
	      return result;
      }
      // Make sure param is empty string "" - as per the API spec
      // Check for "null" is a workaround described in "Known Problems"
      // in the header.


            this.mInitializedState = false;
            var tempEvent = "_none_";
            var isChoice = false;
  			this.mInitializedState = false;
  	      
  	      	var  terminateMap = new JMap();
  	      
  	            terminateMap.put("iParam",iParam);
  	      
  			var result = ajaxCall({
  				url: context +'/com/aja/lcm/Terminate.do',
  			    async : false,
  			    type: 'POST',
  			    ajaxId : "",
  			    method : "",
  			    data : terminateMap,
  			    validationForm : ""
  			    });

  			if(result != null){
	  			tempEvent = result.tempEvent;
	  			isChoice  = result.isChoice;
  			}
  	      if ( this._Debug ) {
  	    	  this.printWindows("::Terminate :: \n iParam :"+iParam +"\n tempEvent : "+ tempEvent +"\n isChoice : " + isChoice);
  	      }
  			
            // handle if sco set nav.request
            if ( !(tempEvent == "_none_") ) {
               if ( this._Debug )
               {
            	   this.printWindows("in finish - navRequest was set");
                  this.printWindows("request " + tempEvent );
               }
               if ( isChoice == true){
            	   ContentExecutor.doChoiceEvent(tempEvent);
               } else{
                 if (tempEvent == "previous") {
	                tempEvent = "prev";
	             }
                 ContentExecutor.buttonProc(tempEvent)
               }
            }
      this.mSCO_VER_3 = false;
      
      this.init();

      if ( this._Debug ){
    	  this.printWindows("Done Processing Terminate()");
      }
      return result;
   }
   this.Commit = function(iParam)
   {
	  if(this.mInitializedState != true) return ""; 
      if ( this._Debug )
      {
    	  this.printWindows("Processing API::Commit");
      }

      // Assume failure
      var result = this.mStringFalse;
 //   mNextAvailable = false;

      // already terminated
      if ( this.mTerminatedState )
      {
          if ( this._Debug ) {
        	  this.printWindows("APIErrorCodes.COMMIT_AFTER_TERMINATE");
          }
      	 this.setLastError(143);
         return result;
      }
      // Disable UI Controls
 	  // setUIState(false);
      // Make sure param is empty string "" - as per the API spec
      // Check for "null" is a workaround described in "Known Problems"
      // in the header.
      var tempParm = iParam;

      if ( typeof(tempParm) == 'undefined' || tempParm != "")
      {
          if ( this._Debug )
            {
        	  this.printWindows("DMErrorCodes.GEN_ARGUMENT_ERROR");
         }
      	 this.setLastError(201);
      }
      else
      {
         if ( !this.isInitialized() )
         {
            //LMS is not initialized
             if ( this._Debug )
            {
            	 this.printWindows("APIErrorCodes.COMMIT_BEFORE_INIT");
            }
      	    this.setLastError(142);
            return result;
         }
         else if ( this.mTerminatedState )
         {
            //LMS is terminated
             if ( this._Debug )
            {
            	 this.printWindows("APIErrorCodes.COMMIT_AFTER_TERMINATE");
            }
      	    this.setLastError(143);
            return result;
         } else {
        	 
        	/** 
			  Map 필수항목 save,req_apply_seq ,org_seq
		    **/
        	 
   			var result = ajaxCall({
   				url: context +'/com/aja/lcm/Commit.do',
   			    async : false,
   			    type: 'POST',
   			    ajaxId : "",
   			    method : "",
   			    data : map,
   			    validationForm : ""
   			    });
   			
         }
      }
// Enable UI Controls
//      setUIState(true);
      if ( this._Debug ){
    	  this.printWindows("Processing done for API::Commit");
      }
      if (closeEvent){
    	  this.printWindows("close Session & remove Session")
      	  ContentExecutor.clearSession();
      }
      return result;
   }

   
   this.setLastError = function(value){  
		var result = ajaxCall({
			url: context+'/com/aja/lcm/setLastError.do',
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : value,
		    validationForm : ""
		});
   }
   
   this.GetLastError = function()
   {
	  if(this.mInitializedState != true) return "";
      var result = '';
      if ( this._Debug > 1 ){
    	  this.printWindows("In API::GetLastError()");
      }
      var tempMap = new JMap();
      
     // getErrorStringMap.put("errorCode",errorCode);

	  result = ajaxCall({
			url: context+'/com/aja/lcm/getLastError.do',
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : tempMap,
		    validationForm : ""
		});

      if ( this._Debug > 1 ) {
    	  this.printWindows("GetLastError Code was: "+ result);
      }
      return result;
   }
   
   this.GetErrorString = function(errorCode) {
	  if(this.mInitializedState != true) return "";
	  
      var result = "";
      
      var getErrorStringMap = new JMap();
      
      getErrorStringMap.put("errorCode",errorCode);
      
	  result = ajaxCall({
			url: context+'/com/aja/lcm/getDiagnostic.do',
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : getErrorStringMap,
		    validationForm : ""
		});
      return result;
      
      if ( this._Debug ){
    	  this.printWindows("getErrorStringMap Code was: "+ result);
      }
   }
   
   this.GetDiagnostic = function(errorCode){
	  if(this.mInitializedState != true) return "";
      var result = "";

      var errorCodeMap = new JMap();
      errorCodeMap.put("errorCode",errorCode);
	  result = ajaxCall({
			url: context+'/com/aja/lcm/getDiagnostic.do',
		    async : false,
		    type: 'POST',
		    ajaxId : "",
		    method : "",
		    data : errorCodeMap,
		    validationForm : ""
		});
      return result;
      
      if ( this._Debug ){
    	  this.printWindows("getDiagnostic Code was: "+ result);
      }
   }
   
   this.ShowComment = function(){
	  if(this.mInitializedState != true) return "";
	  //window.open("/kt_lcms/study/studyComment.jsp?org_seq="+org_seq,"","width=800;height=600");		
   }
   this.prev_sco = function(){
   	  if(this.mInitializedState==false) return;
   	ContentExecutor.changeContent("prev");
   }
   this.next_sco = function(){
   	  if(this.mInitializedState==false) return;  
   	ContentExecutor.changeContent("next");
   }

   this.printWindows = function(value){
       debug(value);
       debug("");
   }
	   
}