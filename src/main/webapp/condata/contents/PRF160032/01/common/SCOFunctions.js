/*******************************************************************************
** 
** Filename: SCOFunctions.js
**
** File Description: This file contains several JavaScript functions that are 
**                   used by the Sample SCOs contained in the Sample Course.
**                   These functions encapsulate actions that are taken when the
**                   user navigates between SCOs, or exits the Lesson.
**
** Author: ADL Technical Team
**
** Contract Number:
** Company Name: CTC
**
** Design Issues:
**
** Implementation Issues:
** Known Problems:
** Side Effects:
**
** References: ADL SCORM
**
*******************************************************************************/

var startDate;
var exitPageStatus;

function loadPage()
{
   var result = doLMSInitialize();

  
   var status = doLMSGetValue( "cmi.core.lesson_status" );
   if (status == "not attempted")
   {	 
	  doLMSSetValue( "cmi.core.lesson_status", "incomplete" );
   }

   exitPageStatus = false;
   startTimer();


}


function startTimer()
{
   startDate = new Date().getTime();
}

function computeTime()
{
   if ( startDate != 0 )
   {
      var currentDate = new Date().getTime();
      var elapsedSeconds = ( (currentDate - startDate) / 1000 );
      var formattedTime = convertTotalSeconds( elapsedSeconds );
   }
   else
   {
      formattedTime = "0000:00:00.00";
   }

   doLMSSetValue( "cmi.core.session_time", formattedTime );
}

function doBack()
{
   doLMSSetValue( "cmi.core.exit", "suspend" );

   computeTime();
   exitPageStatus = true;
   
   var result;

   result = doLMSCommit();

	// NOTE: LMSFinish will unload the current SCO.  All processing
	//       relative to the current page must be performed prior
	//		 to calling LMSFinish.   
   
   result = doLMSFinish();

}

function doContinue( status )
{
   // Reinitialize Exit to blank
   doLMSSetValue( "cmi.core.exit", "" );
	
   var mode = doLMSGetValue( "cmi.core.lesson_mode" );
	
   if ( mode != "review"  &&  mode != "browse" )
   {
      doLMSSetValue( "cmi.core.lesson_status", status );
   }
 
   computeTime();
   exitPageStatus = true;
   
   var result;
   result = doLMSCommit();
	// NOTE: LMSFinish will unload the current SCO.  All processing
	//       relative to the current page must be performed prior
	//		 to calling LMSFinish.   

   result = doLMSFinish();

}

function doQuit()
{
   doLMSSetValue( "cmi.core.exit", "logout" );
	
   computeTime();
   exitPageStatus = true;
   
   var result;

   result = doLMSCommit();

	// NOTE: LMSFinish will unload the current SCO.  All processing
	//       relative to the current page must be performed prior
	//		 to calling LMSFinish.   

   result = doLMSFinish();
}

/*******************************************************************************
** The purpose of this function is to handle cases where the current SCO may be 
** unloaded via some user action other than using the navigation controls 
** embedded in the content.   This function will be called every time an SCO
** is unloaded.  If the user has caused the page to be unloaded through the
** preferred SCO control mechanisms, the value of the "exitPageStatus" var
** will be true so we'll just allow the page to be unloaded.   If the value
** of "exitPageStatus" is false, we know the user caused to the page to be
** unloaded through use of some other mechanism... most likely the back
** button on the browser.  We'll handle this situation the same way we 
** would handle a "quit" - as in the user pressing the SCO's quit button.
*******************************************************************************/
function unloadPage()
{

	if (exitPageStatus != true)
	{
		doQuit();
	}

	// NOTE:  don't return anything that resembles a javascript
	//		  string from this function or IE will take the
	//		  liberty of displaying a confirm message box.

}





/*******************************************************************************
** this function will convert seconds into hours, minutes, and seconds in
** CMITimespan type format - HHHH:MM:SS.SS (Hours has a max of 4 digits &
** Min of 2 digits
*******************************************************************************/
function convertTotalSeconds(ts)
{
   var sec = (ts % 60);

   ts -= sec;
   var tmp = (ts % 3600);  //# of seconds in the total # of minutes
   ts -= tmp;              //# of seconds in the total # of hours

   // convert seconds to conform to CMITimespan type (e.g. SS.00)
   sec = Math.round(sec*100)/100;
   
   var strSec = new String(sec);
   var strWholeSec = strSec;
   var strFractionSec = "";

   if (strSec.indexOf(".") != -1)
   {
      strWholeSec =  strSec.substring(0, strSec.indexOf("."));
      strFractionSec = strSec.substring(strSec.indexOf(".")+1, strSec.length);
   }
   
   if (strWholeSec.length < 2)
   {
      strWholeSec = "0" + strWholeSec;
   }
   strSec = strWholeSec;
   
   if (strFractionSec.length)
   {
      strSec = strSec+ "." + strFractionSec;
   }


   if ((ts % 3600) != 0 )
      var hour = 0;
   else var hour = (ts / 3600);
   if ( (tmp % 60) != 0 )
      var min = 0;
   else var min = (tmp / 60);

   if ((new String(hour)).length < 2)
      hour = "0"+hour;
   if ((new String(min)).length < 2)
      min = "0"+min;

   var rtnVal = hour+":"+min+":"+strSec;

   return rtnVal;
}



function setProgress(val) {
	
	var complete_rate = top.studyMenuFrame.menuForm.complete_rate.value;

	var arrGetVal = new Array(2);
	var arrSetVal = new Array(2);
	
	var getVal1, getVal2, setVal1, setVal2

	//저장된 값에서 진도페이지수를 가져옴
	getVal1   = doLMSGetValue("cmi.suspend_data");
	if (getVal1==null || getVal1=="") {getVal1="0/0"}
	arrGetVal = getVal1.split("/");
	getVal1   = arrGetVal[0];
	getVal2   = arrGetVal[1];  
	

	//넘겨받은 값에서 진도페이지수를 가져옴
	setVal1   = val;
	if (setVal1==null || setVal1=="") {setVal1="0/0"}
	arrSetVal = setVal1.split("/");
	setVal1   = arrSetVal[0];
	setVal2   = arrSetVal[1];

	
	//넘겨받은값이 더 클경우만 저장	
	if (eval(getVal1)<eval(setVal1)) {
		doLMSSetValue("cmi.suspend_data",val);
	}

	var user_rate = (setVal1/setVal2)*100;
	
	if (eval(user_rate)>=eval(complete_rate)){
		doLMSSetValue ("cmi.core.lesson_status","completed");
	}

}


