var debugWindow = null;

function SCORMAPI()
{
 
   /**
    * This controls display of log messages to the java console.
    */
   this._Debug = false;
   
   if(this._Debug == true) debugWindow=window.open("/ngedu/lcms/debugWindow.jsp","debugWindow","width=1020,height=760,scrollbars=yes,resizable=yes,top=0,left=0");

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

   this.init = function()
   {
      // We assume at this point that the user has successfully logged in
      // to the LMS.

      if ( this._Debug )
      {
         //debugWindow.debug("In API::init()(the applet Init method)");
      }
      this.mTerminatedState = false;
      this.mInitializedState = false;
      this.mTerminateCalled = false;
      this.mSCO_VER_2 = false;
      this.mSCO_VER_3 = false;

   }
   this.isInitialized = function()
   {
      if ( ( !this.mInitializedState ) &&  ( this.mSCO_VER_2 ) )
      {
            if ( this._Debug )
            {
           		//debugWindow.error("DMErrorCodes.GEN_GET_FAILURE");
           	}

      	   DMHandler.setLastError(301);
      }
      return this.mInitializedState;
   }
   
   this.Initialize = function(param)
   {  
      if(this.mInitializedState==true && this.mTerminatedState==false) this.Terminate(); 
   
     
      DMHandler.InitializeAPIEM();

      // This function must be called by a SCO before any other
      // API calls are made.   It can not be called more than once
      // consecutively unless Terminate is called.
      if ( this._Debug )
      {
         //debugWindow.debug("*********************");
         //debugWindow.debug("In API::Initialize");
         //debugWindow.debug("*********************");
         //debugWindow.debug("");
      }
      // Assume failure
      var result = this.mStringFalse;
      if ( this.mTerminatedState )
      {
           if ( this._Debug )
           {
          //debugWindow.error("APIErrorCodes.CONTENT_INSTANCE_TERMINATED");
          }
         
      	  DMHandler.setLastError(104);
          return result;
      }
      this.mTerminatedState = false;
      this.mTerminateCalled = false;
      this.mSCO_VER_2 = false;
      this.mSCO_VER_3 = true;

       // Make sure param is empty string "" - as per the API spec
      // Check for "null" is a workaround described in "Known Problems"
      // in the header.
      var tempParm = param

      if ( typeof(tempParm) == 'undefined' || tempParm != "")
      {
          if ( this._Debug )
            {
         //debugWindow.error("MErrorCodes.GEN_ARGUMENT_ERROR");
         }
        
      	 DMHandler.setLastError(201);
      }
      // If the SCO is already initialized set the appropriate error code
      else if ( this.mInitializedState )
      {
          if ( this._Debug )
            {
         //debugWindow.error("APIErrorCodes.ALREADY_INITIALIZED");
         }
        
      	 DMHandler.setLastError(103);
      }
      else
      {
 		
         // set Session
//DWR_SCORM_Proc.setSessionActivityTree();   
         DMHandler.Initialize(learner_name,	course_map,	org_seq, item_id, save, req_apply_seq,user_ip);
         
         // session clear
 //        DWR_SCORM_Proc.clearSessionActivityTree();
         
         // No errors were detected
 //       mLMSErrorManager.clearCurrentErrorCode();
         result = this.mStringTrue;
         
         this.mInitializedState = true;         
      }
      if ( this._Debug )
      {
         //debugWindow.debug("*******************************");
         //debugWindow.debug("Done Processing Initialize()");
         //debugWindow.debug("*******************************");
      }

      return result;
   }
   this.GetValue = function(iDataModelElement)
   {
	  if(this.mInitializedState != true) return "";
      if ( this._Debug )
      {
         //debugWindow.debug("*******************");
         //debugWindow.debug("In API::GetValue");
         //debugWindow.debug("*******************");
         //debugWindow.debug("");
      }
      var result = "";

      // already terminated
      if ( this.mTerminatedState )
      {
          if ( this._Debug )
            {
         //debugWindow.error("APIErrorCodes.GET_AFTER_TERMINATE");
         }
        
      	 DMHandler.setLastError(123);
         return result;
      }

      if ( typeof(iDataModelElement) == 'undefined' || iDataModelElement == "")
      {
          if ( this._Debug )
         {
         	//debugWindow.error("DMErrorCodes.GEN_GET_FAILURE");
         }
        
      	 DMHandler.setLastError(301);
         return result;
      }
      if ( this.isInitialized() )
      {
         if ( this._Debug )
         {
            //debugWindow.debug("Request being processed: GetValue(" + iDataModelElement + ")");
         }
         // Clear current error codes
//       mLMSErrorManager.clearCurrentErrorCode();

         // Process 'GET'
        
		 DMHandler.getValue(iDataModelElement, {
		 callback:function(dataFromServer) {
		 result = dataFromServer;
		 },async:false
		 });
         if (result == "")
         {
             if ( this._Debug )
             {
                //debugWindow.debug("Found the element, but the value was null");
             }
             result = "";
         }






//         //debugWindow.debug("getValue("+iDataModelElement+" : "+result+")");
      }
      // not initialized
      else
      {
           if ( this._Debug )
            {
          //debugWindow.error("APIErrorCodes.GET_BEFORE_INIT");
          }
         
      	  DMHandler.setLastError(122);
      }
      if ( this._Debug )
      {
         //debugWindow.debug("");
         //debugWindow.debug("************************************");
         //debugWindow.debug("Processing done for API::LMSGetValue");
         //debugWindow.debug("************************************");
      }
      return result;
   }

   this.SetValue = function(iDataModelElement, iValue)
   {
	  if(this.mInitializedState != true) return "";
      // Assume failure
      var result = this.mStringFalse;

      if ( this._Debug )
      {
         //debugWindow.debug("*******************");
         //debugWindow.debug("In API::SetValue");
         //debugWindow.debug("*******************");
         //debugWindow.debug("");
      }

      // already terminated
      if ( this.mTerminatedState )
      {
          if ( this._Debug )
            {
         //debugWindow.error("APIErrorCodes.SET_AFTER_TERMINATE");
         }
        
      	 DMHandler.setLastError(133);
         return result;
      }
      // Clear any existing error codes
//    mLMSErrorManager.clearCurrentErrorCode();

     if ( ! this.isInitialized() )
     {
        // not initialized
         if ( this._Debug )
            {
        //debugWindow.error("APIErrorCodes.SET_BEFORE_INIT");
        }
       
      	DMHandler.setLastError(132);
        return result;
     }
     else
     {
         var setValue = null;

         // Check for "null" is a workaround described in "Known Problems"
         // in the header.
         var tempValue = iValue;
         if ( tempValue == null )
         {
            setValue = "";
         }
         else
         {
            setValue = tempValue;
         }
         // Construct the request
         var theRequest = iDataModelElement + "," + setValue;
         if ( this._Debug )
         {
            //debugWindow.debug("Request being processed: SetValue(" + theRequest + ")");
            //debugWindow.debug( "Looking for the element " + iValue );
         }
        
		 DMHandler.setValue(iDataModelElement, setValue, {
		 callback:function(dataFromServer) {
		 result = dataFromServer;
		 },async:false
		 });




//		//debugWindow.debug("setValue("+theRequest+" : "+iValue+") result="+result);

         //clear MessageCollection
//       MessageCollection MC = MessageCollection.getInstance();
//       MC.clear();


	      if ( this._Debug )
	      {
	         //debugWindow.debug("");
	         //debugWindow.debug("************************************");
	         //debugWindow.debug("Processing done for API::SetValue");
	         //debugWindow.debug("************************************");
	      }
	      return result;
	   }
   }
   
   this.Terminate = function(iParam)
   {
	  if(this.mInitializedState != true) return ""; 
	  
      if ( this._Debug )
      {
         //debugWindow.debug("*****************");
         //debugWindow.debug("In API::Terminate");
         //debugWindow.debug("*****************");
         //debugWindow.debug("");
      }

      this.mTerminateCalled = true;
      // Assume failure
      var result = this.mStringFalse;

      // already terminated
      if ( this.mTerminatedState )
      {
          if ( this._Debug )
            {
         //debugWindow.error("APIErrorCodes.TERMINATE_AFTER_TERMINATE");
         }
        
      	 DMHandler.setLastError(113);
      	 
         return result;
      }
      if ( !this.isInitialized() )
      {

          if ( this._Debug )
            {
        // debugWindow.error("APIErrorCodes.TERMINATE_BEFORE_INIT");
         }
        
      	 DMHandler.setLastError(112);
      	 
         return result;
      }
      // Make sure param is empty string "" - as per the API spec
      // Check for "null" is a workaround described in "Known Problems"
      // in the header.
      /**
      var tempParm = iParam;
      if ( typeof(tempParm) == 'undefined' || tempParm != "")
      {
          if ( this._Debug )
            {
         debugWindow.error("DMErrorCodes.GEN_ARGUMENT_ERROR in terminate");
         }
        
      	 DMHandler.setLastError(201);
      }
      else
      **/
      {
//         result = this.Commit("");
//         this.mTerminatedState = true;
//        if ( !(result == this.mStringTrue) )
//         {
//            if ( this._Debug )
//            {
//               debugWindow.debug("Commit failed causing " + "Terminate to fail.");
//           }
            // General Commit Failure
//             if ( this._Debug )
//            {
//            debugWindow.debug("APIErrorCodes.GENERAL_COMMIT_FAILURE");
//            }
//           
//      	    DMHandler.setLastError(391);
//         }
//         else
//         {
            this.mInitializedState = false;

            var tempEvent = "_none_";
            var isChoice = false;
  			this.mInitializedState = false;
  			
//			DMHandler.Terminate(iParam, {

			DMHandler.Terminate(save,req_apply_seq ,org_seq,iParam, {
				callback:function(dataFromServer) {
				tempEvent = dataFromServer[0];
				isChoice = dataFromServer[1];
				},async:false
			});
		
            // handle if sco set nav.request
            if ( !(tempEvent == "_none_") )
            {
               if ( this._Debug )
               {
                  //debugWindow.debug("in finish - navRequest was set");
                  //debugWindow.debug("request " + tempEvent );
               }
               if ( isChoice == true)
               {
           		  doChoiceEvent(tempEvent);
               }
               else
               {
                 if (tempEvent == "previous")
	             {
	                tempEvent = "prev";
	             }
 //
 				  
                   buttonProc(tempEvent)
               }

            }
//         }
      }
      this.mSCO_VER_3 = false;
      
      this.init();

      if ( this._Debug )
      {
         //debugWindow.debug("");
         //debugWindow.debug("***************************");
         //debugWindow.debug("Done Processing Terminate()");
         //debugWindow.debug("***************************");
      }
      
       return result;
   }
   this.Commit = function(iParam)
   {
	  if(this.mInitializedState != true) return ""; 
      if ( this._Debug )
      {
         //debugWindow.debug("*************************");
         //debugWindow.debug("Processing API::Commit");
         //debugWindow.debug("*************************");
         //debugWindow.debug("");
      }

      // Assume failure
      var result = this.mStringFalse;
 //   mNextAvailable = false;

      // already terminated
      if ( this.mTerminatedState )
      {
          if ( this._Debug )
            {
         //debugWindow.error("APIErrorCodes.COMMIT_AFTER_TERMINATE");
         }
        
      	 DMHandler.setLastError(143);
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
        // debugWindow.error("DMErrorCodes.GEN_ARGUMENT_ERROR");
         }
        
      	 DMHandler.setLastError(201);
      }
      else
      {
         if ( !this.isInitialized() )
         {
            //LMS is not initialized
             if ( this._Debug )
            {
           // debugWindow.error("APIErrorCodes.COMMIT_BEFORE_INIT");
            }
           
      	    DMHandler.setLastError(142);
            return result;
         }
         else if ( this.mTerminatedState )
         {
            //LMS is terminated
             if ( this._Debug )
            {
            //debugWindow.error("APIErrorCodes.COMMIT_AFTER_TERMINATE");
            }
           
      	    DMHandler.setLastError(143);
            return result;
         }
         else
         {
//         mLMSErrorManager.clearCurrentErrorCode();
//           result = this.mStringTrue;
            
		    DMHandler.Commit(save,req_apply_seq ,org_seq,
		    {
		   		callback:function(dataFromServer) {
			  	result = dataFromServer;
				// get session & clear;
					if (result == "true")
				   	{
				   		DWR_SCORM_Proc.getSessionActivityTree();
				   	}
				},async:false
		   });
         }
      }
      // Enable UI Controls
//      setUIState(true);
      if ( this._Debug )
      {
         debugWindow.debug("");
         debugWindow.debug("**********************************");
         debugWindow.debug("Processing done for API::Commit");
         debugWindow.debug("**********************************");
      }
      if (closeEvent)
      {
      	  //debugWindow.debug("close Session & remove Session")
      	  clearSession();
      }
      return result;
   }

   this.GetLastError = function()
   {
	  if(this.mInitializedState != true) return "";
      var result = '';
      if ( this._Debug >1)
      {
         debugWindow.debug("In API::GetLastError()");
      }

	 
	  DMHandler.getLastError({
			callback:function(dataFromServer) {
		  	result = dataFromServer;
			},async:false
			});
      if ( this._Debug >1)
      {
         debugWindow.debug("Error Code was: "+ result);
      }
      return result;
   }
   this.GetErrorString = function(errorCode)
   {
	  if(this.mInitializedState != true) return "";
      var result = '';
     
	  DMHandler.getErrorString(errorCode, {
			callback:function(dataFromServer) {
		  	result = dataFromServer;
			},async:false
			});
      return result;
   }
   this.GetDiagnostic = function(errorCode)
   {
	  if(this.mInitializedState != true) return "";
      var result = '';
	       DMHandler.getDiagnostic(errorCode, {
			callback:function(dataFromServer) {
		  	result = dataFromServer;
			},async:false
			});
      return result;
   }
   
   this.ShowComment = function(){
	  if(this.mInitializedState != true) return "";
	  window.open("/kt_lcms/study/studyComment.jsp?org_seq="+org_seq,"","width=800;height=600");		
   }
   this.prev_sco = function(){
   	  if(mInitializedState==false) return;
   	  changeContent("prev");
   }
   this.next_sco = function(){
   	  if(mInitializedState==false) return;  
      changeContent("next");
   }
}