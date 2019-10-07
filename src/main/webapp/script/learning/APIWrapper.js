/*******************************************************************************
**
** FileName: APIWrapper.js
**
*******************************************************************************/


var _Debug = false;  // set this to false to turn debugging off
                     // and get rid of those annoying alert boxes.

// Define exception/error codes
var _NoError = 0;
var _GeneralException = 101;
var _GeneralInitializationFailure = 102;
var _AlreadyInitialized = 103;
var _ContentInstanceTerminated = 104;
var _GeneralTerminationFailure = 111;
var _TerminationBeforeInitialization = 112;
var _TerminationAfterTermination = 113;
var _ReceivedDataBeforeInitialization = 122;
var _ReceivedDataAfterTermination = 123;
var _StoreDataBeforeInitialization = 132;
var _StoreDataAfterTermination = 133;
var _CommitBeforeInitialization = 142;
var _CommitAfterTermination = 143;
var _GeneralArgumentError = 201;
var _GeneralGetFailure = 301;
var _GeneralSetFailure = 351;
var _GeneralCommitFailure = 391;
var _UndefinedDataModelElement = 401;
var _UnimplementedDataModelElement = 402;
var _DataModelElementValueNotInitialized = 403;
var _DataModelElementIsReadOnly = 404;
var _DataModelElementIsWriteOnly = 405;
var _DataModelElementTypeMismatch = 406;
var _DataModelElementValueOutOfRange = 407;


// local variable definitions
var apiHandle = null;
var API = null;
var findAPITries = 0;


/*******************************************************************************
**
** Function: doInitialize()
** Inputs:  None
** Return:  CMIBoolean true if the initialization was successful, or
**          CMIBoolean false if the initialization failed.
**
** Description:
** Initialize communication with LMS by calling the Initialize
** function which will be implemented by the LMS.
**
*******************************************************************************/
function doInitialize()
{
   var result = "false";
   var api = getAPIHandle();
   if (api == null)
   {
      //alert("Unable to locate the LMS's API Implementation.\nInitialize was not successful.");
      return result;
   }
   
   result = "true";

   var result = api.Initialize("");
   
   if (result.toString() != "true")
   {
      var err = ErrorHandler();
   }

   return result.toString();
}

/*******************************************************************************
**
** Function doTerminate()
** Inputs:  None
** Return:  CMIBoolean true if successful
**          CMIBoolean false if failed.
**
** Description:
** Close communication with LMS by calling the Terminate
** function which will be implemented by the LMS
**
*******************************************************************************/
function doTerminate()
{  
   var api = getAPIHandle();
   if (api == null)
   {
//      alert("Unable to locate the LMS's API Implementation.\nTerminate was not successful.");
      return "false";
   }
   else
   {
      // call the Terminate function that should be implemented by the API

      var result = api.Terminate("");
      
      if (result.toString() != "true")
      {
         var err = ErrorHandler();
      }

   }

   return result.toString();
}

/*******************************************************************************
**
** Function doGetValue(name)
** Inputs:  name - string representing the cmi data model defined category or
**             element (e.g. cmi.core.student_id)
** Return:  The value presently assigned by the LMS to the cmi data model
**       element defined by the element or category identified by the name
**       input value.
**
** Description:
** Wraps the call to the GetValue method
**
*******************************************************************************/
function doGetValue(name)
{
   var api = getAPIHandle();
   if (api == null)
   {
//      alert("Unable to locate the LMS's API Implementation.\nGetValue was not successful.");
      return "";
   }
   else
   {
      return value = api.GetValue(name);
   }
}

/*******************************************************************************
**
** Function doSetValue(name, value)
** Inputs:  name -string representing the data model defined category or element
**          value -the value that the named element or category will be assigned
** Return:  CMIBoolean true if successful
**          CMIBoolean false if failed.
**
** Description:
** Wraps the call to the SetValue function
**
*******************************************************************************/
function doSetValue(name, value)
{
   var api = getAPIHandle();
   if (api == null)
   {
//      alert("Unable to locate the LMS's API Implementation.\nSetValue was not successful.");
      return;
   }
   else
   {
      var result = api.SetValue(name, value);
      if (result.toString() != "true")
      {
         var err = ErrorHandler();
      }
   }

   return;
}

/*******************************************************************************
**
** Function doLMSInterface(name)
** Inputs:  name - LMS와의 게시판류 연계 함수
**          value -the value that the named element or category will be assigned
** Return:  true : 성공
**          false : 실패
**
** Description:
** Wraps the call to the doLMSInterface function
**
*******************************************************************************/
function doLMSInterface(name) 
{
   var api = getAPIHandle();
   if (api == null)
   {
      return;
   }
   else
   {
      var result = api.setLMSInterface(name);
      if (result.toString() != "true")
      {
         var err = ErrorHandler();
      }
   }
   return;
}

/*******************************************************************************
**
** Function doNAVIInterface(name)
** Inputs:  name - LMS와의 아이템 분기  연계 함수
**          value -the value that the named element or category will be assigned
** Return:  true : 성공
**          false : 실패
**
** Description:
** Wraps the call to the doNAVIInterface function
**
*******************************************************************************/
function doNAVIInterface(item_id) 
{
   var api = getAPIHandle();
   if (api == null)
   {
      return;
   }
   else
   {
      var result = api.setNAVIInterface(item_id);
      if (result.toString() != "true")
      {
         var err = ErrorHandler();
      }
   }
   return;
}


/*******************************************************************************
**
** Function ErrorHandler()
** Inputs:  None
** Return:  The current value of the LMS Error Code
**
** Description:
** Determines if an error was encountered by the previous API call
** and if so, displays a message to the user.  If the error code
** has associated text it is also displayed.
**
*******************************************************************************/
function ErrorHandler()
{
   	return "";
}

/******************************************************************************
**
** Function getAPIHandle()
** Inputs:  None
** Return:  value contained by APIHandle
**
** Description:
** Returns the handle to API object if it was previously set,
** otherwise it returns null
**
*******************************************************************************/
function getAPIHandle()
{
   if (apiHandle == null)
   {
      apiHandle = getAPI();
   }

   return apiHandle;
}


/*******************************************************************************
**
** Function findAPI(win)
** Inputs:  win - a Window Object
** Return:  If an API object is found, it's returned, otherwise null is returned
**
** Description:
** This function looks for an object named API in parent and opener windows
**
*******************************************************************************/
function findAPI(win)
{
   while ((win.API_1484_11 == null) && (win.parent != null) && (win.parent != win))
   {
      findAPITries++;
      
      if (findAPITries > 500) 
      {
//         alert("Error finding API -- too deeply nested.");
         return null;
      }
      
      win = win.parent;

   }
   return win.API_1484_11;
}



/*******************************************************************************
**
** Function getAPI()
** Inputs:  none
** Return:  If an API object is found, it's returned, otherwise null is returned
**
** Description:
** This function looks for an object named API, first in the current window's 
** frame hierarchy and then, if necessary, in the current window's opener window
** hierarchy (if there is an opener window).
**
*******************************************************************************/
function getAPI()
{
   var theAPI = findAPI(window);
   if ((theAPI == null) && (window.opener != null) && (typeof(window.opener) != "undefined"))
   {
      theAPI = findAPI(window.opener);
   }
   if (theAPI == null)
   {
//      alert("Unable to find an API adapter");
   }
   return theAPI
}


