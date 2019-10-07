
/*******************************************************************************
**
** Advanced Distributed Learning Co-Laboratory (ADL Co-Lab) grants you
** ("Licensee") a non-exclusive, royalty free, license to use, modify and
** redistribute this software in source and binary code form, provided that
** i) this copyright notice and license appear on all copies of the software;
** and ii) Licensee does not utilize the software in a manner which is
** disparaging to ADL Co-Lab.
**
** This software is provided "AS IS," without a warranty of any kind.  ALL
** EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
** ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
** OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.  ADL Co-Lab AND ITS LICENSORS
** SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
** USING, MODIFYING OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES.  IN NO
** EVENT WILL ADL Co-Lab OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE,
** PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
** INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE
** THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE
** SOFTWARE, EVEN IF ADL Co-Lab HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH
** DAMAGES.
**
*******************************************************************************/

package com.ziaan.scorm2004.runtime.client;

import javax.servlet.http.HttpSession;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import com.ziaan.library.Log;
import com.ziaan.scorm2004.datamodels.*;
import com.ziaan.scorm2004.api.ecmascript.*;
import com.ziaan.scorm2004.datamodels.nav.*;
import com.ziaan.scorm2004.runtime.server.LMSCMIPROC;
import com.ziaan.scorm2004.runtime.server.LMSCMIServletRequest;
import com.ziaan.scorm2004.runtime.server.LMSCMIServletResponse;
import com.ziaan.scorm2004.sequencer.*;
import com.ziaan.scorm2004.util.*;

/**
 * This class implements the ADL Sharable Content Object Reference Model (SCORM)
 * Version 2004 Sharable Content Object (SCO) to Learning Management System
 * (LMS) communication API defined by the Institute of Electrical and
 * Electronics Engineers (IEEE).  It is intended to be an example only and as
 * such several simplifications have been made.  It is not intended to be a
 * complete LMS implementation.  For example, robust error handling along with
 * performance optimizatons are left to a later development iteration.<br>
 *
 * <strong>Filename:</strong> ClientRTS<br><br>
 *
 * <strong>Description:</strong><br><br>
 *
 * This class is implemented as an applet running in a web-based client/server
 * LMS.  The applet runs within the context of the LMS provided client.  It
 * was developed and tested using IE5 or IE6 and the Sun Java Runtime
 * Environment Standard Edition Version 1.4.<br>
 *
 * The applet interacts with a server-side component.  The server component is
 * implemented as a Java Servlet and handles persistence of the data model.<br>
 *
 * Currently available web technologies provide many ways in which an LMS could
 * be implemented to be compliant with the communication mechanisms described
 * in the SCORM.  This is just one example.<br>
 *
 * Since the intended usage of this API is via LiveConnect from ECMAScript
 * (JavaScript), values are returned from the public LMS functions
 * to the caller as String objects.  The ECMAScript caller will see the
 * return values as JavaScript String objects.<br><br>
 *
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Version 2004.
 * Sample RTE 1.3. <br> <br>
 *
 * <strong>Implementation Issues:</strong><br>
 * This is a faceless Applet.  No user interface is provided<br><br>
 *
 * <strong>Known Problems:</strong><br>
 * 1. In several instances, the parameters to the API functions are checked
 * for a value of "null" because the Java Plug-in converts an empty string ("")
 * to <code>null</code>.  This is only a workaround.  The expected parameters
 * are "" where stated in SCORM and not <code>null</code>.<br><br>
 *
 * <strong>Side Effects:</strong><br><br>
 *
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS Simple Sequencing Specification</li>
 *     <li>SCORM Version 2004</li>
 *     <li>IEEE P1484.11.1 Draft 1/WD 13 Draft Standard for Learning Technology
 *            Data Model for Content Object Communication.
 *            Available at: http://ltsc.ieee.org/</li>
 *     <li>IEEE P1484.11.2 Draft 4 Standard for Learning Technology
 *            ECMAScript Application Programming Interface for Content to
 *            Runtime Services Communication.
 *            Available at: http://ltsc.ieee.org/</li>
 * </ul>
 *
 * @author ADL Technical Team
 */
public class ClientRTS implements SCORM13APIInterface
{
   /**
    * This controls display of log messages to the java console.
    */
   private static boolean _Debug = true;

   /**
    * Provides all LMS Error reporting.
    */
   private APIErrorManager mLMSErrorManager = null;

   /**
    * The current run-time data model values.
    */
   private SCODataManager mSCOData = null;

   /**
    * Indicates if the SCO is in an 'initialized' state.
    */
   private static boolean mInitializedState = false;

   /**
    * String value of FALSE for JavaScript returns.
    */
   private static final String mStringFalse = "false";

   /**
    * String value of TRUE for JavaScript returns.
    */
   private static final String mStringTrue = "true";

   /**
    * Indicates if the SCO is in a 'terminated' state.
    */
   private boolean mTerminatedState = false;
   /**
    * Indicates if the SCO is in a 'terminated' state.
    */
   private boolean mTerminateCalled = false;

   /**
    * URL to the location of the <code>LMSCMIServlet</code>.
    */
//   private URL mServletURL = null;

   /**
    * ID of the activity associated with the currently launched content.
    */
   private String mActivityID = null;

   /**
    * ID of the activity's associated run-time data.
    */
   private String mStateID = null;

   /**
    * ID of the student experiencing the currently launched content.
    */
   private String mUserID = null;

   /**
    * Name of the student experiencing the currently launched content.
    */
   private String mUserName = null;

   /**
    * ID of the LMS of which the currently experienced activity is a part.
    */
   private String mLMSID = null;

   /**
    * ID of the course of which the currently experienced activity is a part.
    */
   private String mCourseID = null;

   /**
    * ID of the org of which the currently experienced activity is a part.
    */
   private String mOrgID = null;

   /**
    * Indicates if another activity is available for delivery.
    */
   private boolean mNextAvailable = false;

   /**
    * Indicates number of the current attempt.
    */
   private long mNumAttempts = 0L;

   /**
    * Indicates nav.event if applicable.
    */
   private String mNavEvent = null;

   /**
    * Store current valid navigation requests.
    */
   private ADLValidRequests mValidRequests = null;


   /**
    * Indicates if the current SCO is SCORM Version 1.2.
    */
   private boolean mSCO_VER_2 = false;

   /**
    * Indicates if the current SCO is SCORM 2004.
    */
   private boolean mSCO_VER_3 = false;

   /**
    * The public version attribute of the SCORM API.
    */
   public static final String version = "1.0";

   private boolean mIsChoice = false;
   
   private String mTempEvent = "_none_";
   
   public ClientRTS()
   {
       init();
   }

   /**
    * Initializes the applet's state.
    */
   public void init()
   {
      // We assume at this point that the user has successfully logged in
      // to the LMS.

      if ( _Debug )
      {
         System.out.println("In API::init()(the applet Init method)");
      }

      mTerminatedState = false;
      mInitializedState = false;
      mTerminateCalled = false;

      mLMSErrorManager = new APIErrorManager(APIErrorManager.SCORM_2004_API);


      mSCO_VER_2 = false;
      mSCO_VER_3 = false;

      /*
      URL codebase = getCodeBase();
      String host = codebase.getHost();
      String protocol = codebase.getProtocol();
      int port = codebase.getPort();
      if ( _Debug )
      {
         System.out.println("codebase url is " + codebase.getPath().toString());
      }

      try
      {
         mServletURL = new URL(protocol + "://" + host + ":" + port + "/adl/lmscmi");

         if ( _Debug )
         {
            System.out.println("servlet url is " + mServletURL.toString());
         }
      }
      catch ( Exception e )
      {
         if ( _Debug )
         {
            System.out.println("ERROR in INIT");
         }
         e.printStackTrace();
      }
      */
   }


   /**
    * Provides a string describing the the API applet class.
    *
    * @return API Applet information string.
    */
   public String getAppletInfo()
   {
      return "Title: Sample RTE Client Component \nAuthor: ADL TT\n" +
      "This Applet contains an example implementation of the SCORM 2004 API.";
   }

   /**
    * Provides information about this applet's parameters.
    *
    * @return String containing information about the applet's parameters.
    */
   public String[][] getParameterInfo()
   {
      String[][] info =
      {{ "None", "", "This applet requires no parameters."}};

      return info;
   }

   /**
    * Confirms that the communication session has been initialized
    * (<code>LMSInitialize </code> or <code>Initialize</code> has been called).
    *
    * @return <code>true</code> if <code>LMSInitialize</code> or
    * <code>Initialize</code> has been called otherwise <code>false</code>.
    */
   private boolean isInitialized()
   {
       if ( ( !mInitializedState ) &&  ( mSCO_VER_2 ) )
      {

         mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_GET_FAILURE);
      }

      return mInitializedState;
   }


   /**
    * Initiates the communication session.
    * It is used by SCORM 2004 SCOs.  The LMSCMIServlet is contacted
    * via LMSCMIServletRequest, ServletProxy and ServletWriter objects.
    * The LMSCMIServlet opens or initializes data model files and returns a
    * copy of any initialized data model elements to the applet.
    *
    * @param iParam  ("") - empty characterstring.
    * An empty characterstring shall be passed as a parameter.
    *
    * @return The function can return one of two values.  The return value
    * shall be represented as a characterstring.
    *    "true" - The characterstring "true" shall be returned if communication
    *    session initialization, as determined by the LMS, was successful.
    *    "false" - The characterstring "false" shall be returned if
    *    communication session initialization, as determined by the LMS, was
    *    unsuccessful.
    */
   public String Initialize(String iParam)
   {
      // This function must be called by a SCO before any other
      // API calls are made.   It can not be called more than once
      // consecutively unless Terminate is called.

      if ( _Debug )
      {
          Log.info.println( 
                  "[Init] :" + " CourseCode:" + mCourseID  + " ScoID:" + mStateID 
                  + " ID:" + mUserID + " Name:" + mUserName
          );       
          
         System.out.println("*********************");
         System.out.println("In API::Initialize");
         System.out.println("*********************");
         System.out.println("");
      }

      // Assume failure
      String result = mStringFalse;

      if ( mTerminatedState )
      {
          mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.CONTENT_INSTANCE_TERMINATED);
          return result;
      }

      mTerminatedState = false;
      mTerminateCalled = false;

      mSCO_VER_2 = false;
      mSCO_VER_3 = true;

       // Make sure param is empty string "" - as per the API spec
      // Check for "null" is a workaround described in "Known Problems"
      // in the header.
      String tempParm = String.valueOf(iParam);

      if ( (tempParm == null || tempParm.equals("")) != true )
      {
         mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_ARGUMENT_ERROR);
      }

      // If the SCO is already initialized set the appropriate error code
      else if ( mInitializedState )
      {
          System.out.println( "############################################################" );
          System.out.println( "ALREADY_INITIALIZED" );
          System.out.println( "############################################################" );
         mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.ALREADY_INITIALIZED);
      }
      else
      {
         LMSCMIServletRequest request = new LMSCMIServletRequest();

         // Build the local LMSCMIServlet Request Object to serialize
         // across the socket
         request.mActivityID = mActivityID;
         request.mStateID = mStateID;
         request.mStudentID = mUserID;
         request.mUserName = mUserName;
         request.mLMSID = mLMSID;
         request.mCourseID = mCourseID;
         request.mOrgID = mOrgID;
         request.mRequestType = LMSCMIServletRequest.TYPE_INIT;

         Long longObj = new Long(mNumAttempts);
         request.mNumAttempt = longObj.toString();

         if ( _Debug )
         {
            System.out.println("Trying to get SCO Data from servlet...");
            System.out.println("LMSCMIServlet Request contains: ");
            System.out.println("Activity ID: " + request.mActivityID);
            System.out.println("State ID: " + request.mStateID);
            System.out.println("User ID: " + request.mStudentID);
            System.out.println("Course ID: " + request.mCourseID);
            System.out.println("Org ID: " + request.mOrgID);
         }

//         ServletProxy proxy = new ServletProxy(mServletURL);
//         LMSCMIServletResponse response = proxy.postLMSRequest(request);

         LMSCMIPROC server = new LMSCMIPROC();
         LMSCMIServletResponse response = server.doPost(request);

         
         // Get the SCODataManager from the servlet response object
         mSCOData = response.mActivityData;

         SCORM_2004_NAV_DM navDM = (SCORM_2004_NAV_DM)mSCOData.getDataModel("adl");

         // Get the ADLValidRequests object from the servlet response object.
         mValidRequests = response.mValidRequests;
         navDM.setValidRequests(response.mValidRequests);

         mInitializedState = true;

         // No errors were detected
         mLMSErrorManager.clearCurrentErrorCode();

         result = mStringTrue;
      }

      if ( _Debug )
      {
         System.out.println("");
         System.out.println("*******************************");
         System.out.println("Done Processing Initialize()");
         System.out.println("*******************************");
      }
      
      return result;
   }

   /**
    * Terminates the communication session.  It is used
    * by a SCORM Version 2004 SCO when the SCO has determined that it no longer
    * needs to communicate with the LMS.  The Terminiate() function also shall
    * cause the persistence of any data (i.e., an implicit Commit("") call) set
    * by the SCO since the last successful call to Initialize("") or Commit(""),
    * whichever occurred most recently.  This guarantees to the SCO that all
    * data set by the SCO has been persisted by the LMS.
    * If the SCO has set a nav.event, the navigation event is communicated
    * to the Web browser through LiveConnect.
    *
    * @param iParam ("") - empty characterstring.  An empty characterstring
    * shall be passed as a parameter.
    *
    * @return The method can return one of two values.  The return value shall
    * be represented as a characterstring.
    *    "true" - The characterstring "true" shall be returned if termination
    *    of the communication session, as determined by the LMS, was successful.
    *    "false" - The characterstring "false" shall be returned if
    *    termination of the communication session, as determined by the LMS,
    *    was unsuccessful.
    */
   public String Terminate(String iParam)
   {
       
      if ( _Debug )
      {
         System.out.println("*****************");
         System.out.println("In API::Terminate");
         System.out.println("*****************");
         System.out.println("");
      }

      mTerminateCalled = true;
      // Assume failure
      String result = mStringFalse;

      // already terminated
      if ( mTerminatedState )
      {
         mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.TERMINATE_AFTER_TERMINATE);
         return result;
      }
      if ( !isInitialized() )
      {
         mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.TERMINATE_BEFORE_INIT);
         return result;
      }

      // Make sure param is empty string "" - as per the API spec
      // Check for "null" is a workaround described in "Known Problems"
      // in the header.
      String tempParm = String.valueOf(iParam);
      if ( (tempParm == null || tempParm.equals("")) != true )
      {
         mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_ARGUMENT_ERROR);
      }
      else
      {
         result = Commit("");
         mTerminatedState = true;

         if ( !result.equals(mStringTrue) )
         {
            if ( _Debug )
            {
               System.out.println("Commit failed causing " + "Terminate to fail.");
            }
            // General Commit Failure
            mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.GENERAL_COMMIT_FAILURE);
         }
         else
         {
            mInitializedState = false;

            // get value of "exit"
            DMProcessingInfo dmInfo = new DMProcessingInfo();
            int dmErrorCode = 0;
            dmErrorCode = DMInterface.processGetValue("cmi.exit", true, true, mSCOData, dmInfo);
            String exitValue = dmInfo.mValue;
//            String tempEvent = "_none_";
//            boolean isChoice = false;
//            String evalValue = null;
            
            if ( dmErrorCode == APIErrorCodes.NO_ERROR )
            {
               exitValue = dmInfo.mValue;
            }
            else
            {
              exitValue = new String("");
            }

            if ( exitValue.equals("logout") )
            {
               mTempEvent = "suspendAll";
            }
            else
            {
               // get navigation request
               SCORM_2004_NAV_DM navDM = (SCORM_2004_NAV_DM)mSCOData.getDataModel("adl");
               String event = navDM.getNavRequest();

               if  ( event != null )
               {  
                  // SeqNavRequests.NAV_CONTINUE
                  if ( event.equals("3") )
                  {
                     mTempEvent = "next";
                  }
                  // SeqNavRequests.NAV_PREVIOUS
                  else if ( event.equals("4") )
                  {
                      mTempEvent = "prev";
                  }
                  // SeqNavRequests.NAV_EXIT
                  else if ( event.equals("8") )
                  {
                      mTempEvent = "exit";
                  }
                  // SeqNavRequests.NAV_EXITALL
                  else if ( event.equals("9") )
                  {
                      mTempEvent = "exitAll";
                  }
                  // SeqNavRequests.NAV_ABANDON
                  else if ( event.equals("5") )
                  {
                      mTempEvent = "abandon";
                  }
                  // SeqNavRequests.NAV_ABANDONALL
                  else if ( event.equals("6") )
                  {
                      mTempEvent = "abandonAll";
                  }
                  // SeqNavRequests.NAV_NONE
                  else if ( event.equals("0") )
                  {
                      mTempEvent = "_none_";
                  }
                  else
                  {
                     // This must be a target for choice
                      mTempEvent = event;
                      mIsChoice = true;
                  }
               }
             }
            
            /*
            // handle if sco set nav.request
            if ( ! tempEvent.equals("_none_") )
            {
               if ( _Debug )
               {
                  System.out.println("in finish - navRequest was set");
                  System.out.println("request " + tempEvent );

               }
              if ( isChoice )
               {
                  evalValue = "doChoiceEvent(\"" + tempEvent + "\");";
                  if ( _Debug )
                  {
                     System.out.println("choice nav event  "+ evalValue);
                  }
               }
              else
              {
                 evalValue = "doNavEvent(\"" + tempEvent + "\");";
              }
              JSObject.getWindow( this ).eval( evalValue );
            }
            */
         }
      }


      mSCO_VER_3 = false;

      if ( _Debug )
      {
         System.out.println("");
         System.out.println("***************************");
         System.out.println("Done Processing Terminate()");
         System.out.println("***************************");

         Log.info.println( 
                 "[Term] :" + " CourseCode:" + mCourseID  + " ScoID:" + mStateID 
                 + " ID:" + mUserID + " Name:" + mUserName
         );       
      }

      
      return result;
   }


    /**
    * The function requests information from an LMS.  It permits the SCO to
    * request information from the LMS to determine among other things:
    *    Values for data model elements supported by the LMS.
    *    Version of the data model supported by the LMS.
    *    Whether or not specific data model elements are supported.
    * Retrieves the current value of the specified data model element
    * for a SCORM Version 1.3 SCO.  The  values are locally cached except for
    * nav.event_permitted, which requires a call to LMSCMIServlet to get the
    * current value.
    *
    * @param iDataModelElement The parameter represents the complete
    * identification of a data model element within a data model.
    *
    * @return The method can return one of two values.  If there is not error,
    * the return value shall be represented as a characterstring containing
    * the value associated with the parameter.  If an error occurs, then the
    * API Instance shall set an error code to a value specific to the error and
    * return an empty characterstring ("").
    */
   public String GetValue(String iDataModelElement)
   {
      if ( _Debug )
      {
         System.out.println("*******************");
         System.out.println("In API::GetValue");
         System.out.println("*******************");
         System.out.println("");
      }

      String result = "";

      // already terminated
      if ( mTerminatedState )
      {
         mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.GET_AFTER_TERMINATE);
         return result;
      }
      if ( iDataModelElement.length() == 0 )
      {
         mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_GET_FAILURE);
         return result;
      }


      if ( isInitialized() )
      {

         if ( _Debug )
         {
            System.out.println("Request being processed: GetValue(" +
                               iDataModelElement + ")");
         }

            // Clear current error codes
            mLMSErrorManager.clearCurrentErrorCode();

            // Process 'GET'
            DMProcessingInfo dmInfo = new DMProcessingInfo();
            int dmErrorCode = 0;
            dmErrorCode = DMInterface.processGetValue(iDataModelElement, false, mSCOData, dmInfo);

            // Set the LMS Error Manager from the Data Model Error Manager
            mLMSErrorManager.setCurrentErrorCode(dmErrorCode);

            if ( dmErrorCode == APIErrorCodes.NO_ERROR )
            {
               result = dmInfo.mValue;

               if ( _Debug )
               {
                  System.out.println("GetValue() found!");
                  System.out.println("Returning: "+ dmInfo.mValue);
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("Found the element, but the value was null");
               }
               result = new String("");
            }
      }
       // not initialized
      else
      {
          mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.GET_BEFORE_INIT);
      }

      if ( _Debug )
      {
         System.out.println("");
         System.out.println("************************************");
         System.out.println("Processing done for API::LMSGetValue");
         System.out.println("************************************");
      }

      return result;
   }


   /**
    * Request the transfer to the LMS of the value of
    * iValue for the data element specified as iDataModelElement.  This method
    * allows the SCO to send information to the LMS for storage.
    * Used by SCORM Version 2004 SCOs.  The values are locally cached
    * until <code>LMSCommit</code> or <code>LMSFinish</code> is called.
    *
    *
    * @param iDataModelElement - The complete identification of a data model
    *        element within a data model to be set.
    *        iValue  - The intended value of the CMI or Navigation
    *        datamodel element.  The value shall be a characterstring that shall
    *        be convertible to the data type defined for the data model element
    *        identified in iDataModelElement.
    *
    * @return The method can return one of two values.  The return value shall
    *         be represented as a characterstring.
    *        "true" - The characterstring "true" shall be returned if the LMS
    *        accepts the content of iValue to set the value of iDataModelElement
    *        "false" - The characterstring "false" shall be returned if the LMS
    *        encounters an error in setting the contents of iDataModelElement
    *        with the value of iValue.
    */
   public String SetValue(String iDataModelElement, String iValue)
   {
      // Assume failure
      String result = mStringFalse;

      if ( _Debug )
      {
         System.out.println("*******************");
         System.out.println("In API::SetValue");
         System.out.println("*******************");
         System.out.println("");
      }

      // already terminated
      if ( mTerminatedState )
      {
         mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.SET_AFTER_TERMINATE);
         return result;
      }

      // Clear any existing error codes
      mLMSErrorManager.clearCurrentErrorCode();

     if ( ! isInitialized() )
     {
        System.out.println( "########################################################" );         
        System.out.println( "SET BEFORE INIT" );         
        System.out.println( "########################################################" );         
        // not initialized
        mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.SET_BEFORE_INIT);
        return result;
     }
     else
     {
         String setValue = null;

         // Check for "null" is a workaround described in "Known Problems"
         // in the header.
         String tempValue = String.valueOf(iValue);
         if ( tempValue == null )
         {
            setValue = new String("");
         }
         else
         {
            setValue = tempValue;
         }

            // Construct the request
            String theRequest = iDataModelElement + "," + setValue;

            if ( _Debug )
            {
               System.out.println("Request being processed: SetValue(" + theRequest + ")");
               System.out.println( "Looking for the element " + iValue );
            }

            // Process 'SET'
            int dmErrorCode = 0;
            
            dmErrorCode = DMInterface.processSetValue(iDataModelElement, iValue, false, mSCOData);


            // Set the LMS Error Manager from the DataModel Manager
            mLMSErrorManager.setCurrentErrorCode(dmErrorCode);
     }

     if ( mLMSErrorManager.getCurrentErrorCode().equals("0") )
     {
      // Successful Set
        result = mStringTrue;
     }

     //clear MessageCollection
     MessageCollection MC = MessageCollection.getInstance();
     MC.clear();


      if ( _Debug )
      {
         System.out.println("");
         System.out.println("************************************");
         System.out.println("Processing done for API::SetValue");
         System.out.println("************************************");
      }

      return result;
   }



   /**
    * Toggles the state of the LMS provided UI controls.
    *
    * @param iState <code>true</code> if the controls should be enabled, or
    *               <code>false</code> if the controls should be disabled.
   private void setUIState(boolean iState)
   {
      if ( _Debug )
      {
         System.out.println(" ::Toggling UI State::-> " + iState);
      }

      String evalCmd = "setUIState(" + iState + ");";

      JSObject jsroot = JSObject.getWindow(this);
      jsroot.eval(evalCmd);
   }
   */

   /**
    * Requests forwarding to the persistent data store any data
    * from the SCO that may have been cached by the API Implementation since
    * the last call to Initialize("") or Commit(""), whichever occurred most
    * recently.  Used by SCORM Version 2004 SCOs.
    *
    * @param iParam ("") - empty characterstring.  An empty characterstring
    * shall be passed as a parameter.
    *
    * @return The method can return one of two values.  The return value shall
    *         be represented as a characterstring.
    *         "true" - The characterstring "true" shall be returned if the
    *         data was successfully persisted to a long-term data store.
    *         "false" - The characterstring "false" shall be returned if
    *         the data was unsuccessfully persisted to a long-term data store.
    *         The API Instance shall set the error code to a value specific to
    *         the error encountered.
    */
   public String Commit(String iParam)
   {
      if ( _Debug )
      {
         System.out.println("*************************");
         System.out.println("Processing API::Commit");
         System.out.println("*************************");
         System.out.println("");

         Log.info.println( 
                 "[Comm] :" + " CourseCode:" + mCourseID  + " ScoID:" + mStateID 
                 + " ID:" + mUserID + " Name:" + mUserName
         );       
      }

      // Assume failure
      String result = mStringFalse;
      mNextAvailable = false;

      // already terminated
      if ( mTerminatedState )
      {
         mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.COMMIT_AFTER_TERMINATE);
         return result;
      }

      // Disable UI Controls
      // TODO JavaScript Client side에서 추가할 것.
      // setUIState(false);



      // Make sure param is empty string "" - as per the API spec
      // Check for "null" is a workaround described in "Known Problems"
      // in the header.
      String tempParm = String.valueOf(iParam);

      if ( (tempParm == null || tempParm.equals("")) != true )
      {
         mLMSErrorManager.setCurrentErrorCode(DMErrorCodes.GEN_ARGUMENT_ERROR);
      }
      else
      {
         if ( !isInitialized() )
         {
            //LMS is not initialized
            mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.COMMIT_BEFORE_INIT);
            return result;
         }
         else if ( mTerminatedState )
         {
            //LMS is terminated
            mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.COMMIT_AFTER_TERMINATE);
            return result;
         }
         else
         {
         // Prepare the request before it goes across the socket
            LMSCMIServletRequest request = new LMSCMIServletRequest();
            request.mActivityData = mSCOData;
            request.mIsFinished = mTerminateCalled;
            request.mRequestType = LMSCMIServletRequest.TYPE_SET;
            request.mLMSID = mLMSID;
            request.mCourseID = mCourseID;
            request.mOrgID = mOrgID;
            request.mStudentID = mUserID;
            request.mUserName = mUserName;
            request.mStateID = mStateID;
            request.mActivityID = mActivityID;
            Long longObj = new Long(mNumAttempts);
            request.mNumAttempt = longObj.toString();

            LMSCMIPROC server = new LMSCMIPROC();
            LMSCMIServletResponse response = server.doPost(request);

            if ( !response.mError.equals("OK") )
            {
               mLMSErrorManager.setCurrentErrorCode(APIErrorCodes.GENERAL_EXCEPTION);

               if ( _Debug )
               {
                  System.out.println("'SET' to server was NOT successful!");
               }
            }
            else
            {
               mLMSErrorManager.clearCurrentErrorCode();

               result = mStringTrue;
               mNextAvailable = response.mAvailableActivity;

               SCORM_2004_NAV_DM navDM = (SCORM_2004_NAV_DM)mSCOData.getDataModel("adl");

               // Update the ADLValidRequests object from the servlet
               // response object.
              mValidRequests = response.mValidRequests;
              navDM.setValidRequests(response.mValidRequests);

              if ( _Debug )
               {
                  System.out.println("'SET' to server succeeded!");
               }
            }
            
         }
      }
      // Enable UI Controls
      // TODO  JavaScript Client side에서 추가할 것.
      // setUIState(true);

      // Refresh the Menu frame
      // jsroot.eval("refreshMenu()");

      if ( _Debug )
      {
         System.out.println("");
         System.out.println("**********************************");
         System.out.println("Processing done for API::Commit");
         System.out.println("**********************************");
      }

      return result;
   }


  /**
    * This method requests the error code for the current error state of the
    * API Instance.  Used by SCORM Version 2004 SCOs.
    *
    * <br><br>NOTE: Session and Data-Transfer API functions set or
    * clear the error code.<br><br>
    *
    * @param  The API method shall not accept any parameters.
    *
    * @return The API Instance shall return the error code reflecting the
    *         current error state of the API Instance.  The return value
    *         shall be a characterstring (convertible to an integer in the
    *         range from 0 to 65536 inclusive) representing the error code
    *         of the last error encountered.
    */
   public String GetLastError()
   {
      if ( _Debug )
      {
         System.out.println("In API::GetLastError()");
      }

      return mLMSErrorManager.getCurrentErrorCode();
   }



   /**
    * The GetErrorString() function can be used to retrieve a textual
    * description of the current error state.  The function is used by a
    * SCO to request the textual description for the error code specified
    * by the value of the parameter.  The API Instance shall be responsible
    * for supporting the error codes identified in Section 3.1.7 API
    * Implementation Error Codes.  This call has no effect on the current
    * error state; it simply returns the requested information.  Used by
    * SCORM Version 2004 SCOs.
    *
    * @param iErrorCode Represents the characterstring of the error code
    *        (integer value) corresponding to an error message.
    *
    * @return The method shall return a textual message containing a
    *         description of the error code specified by the value of the
    *         parameter.  The following requirements shall be adhered to for
    *         all return values:
    *         The return value shall be a characterstring that has a maximum
    *         length of 256 bytes (including null terminator).
    *         The SCORM makes no requirement on what the text of the
    *         characterstring shall contain.  The error codes themselves are
    *         explicitly and exclusively defined.  The textual description
    *         for the error code is LMS specific.
    *         If the requested error code is unknown by the LMS, an empty
    *         characterstring ("") shall be returned  This is the only time
    *         that an empty characterstring shall be returned.
    */
   public String GetErrorString(String iErrorCode)
   {
      if ( _Debug )
      {
         System.out.println("In API::GetErrorString()");
      }
      return mLMSErrorManager.getErrorDescription(iErrorCode);
   }



    /**
    * The GetDiagnostic() function exists for LMS specific use.  It  allows
    * the LMS to define additional diagnostic information through the API
    * Instance.  This call has no effect on the current error state; it
    * simply returns the requested information.  Used by SCORM Version
    * 2004 SCOs.
    *
    * @param iErrorCode  An implementer-specific value for diagnostics.  The
    *        maximum length of the parameter value shall be 256 bytes
    *        (including null terminator).  The value of the parameter may be
    *        an error code, but is not limited to just error codes.
    *
    * @return The API Instance shall return a characterstring representing
    *         the diagnostic information.  The maximum length of the
    *         characterstring returned shall be 256 bytes
    *         (including null terminator).
    */
   public String GetDiagnostic(String iErrorCode)
   {
      if ( _Debug )
      {
         System.out.println("In API::GetDiagnostic()");
      }
      return mLMSErrorManager.getErrorDiagnostic(iErrorCode);
   }



   /**
    * Sets the ID of the activity associated with the currently delivered content.
    *
    * @param iActivityID  The activity ID.
    */
   public void setActivityID( String iActivityID )
   {
      mActivityID = iActivityID;
   }

   /**
    * Sets the ID of the LMS with which the currently launched content is
    * associated.
    *
    * @param iLMSID  The LMS ID.
    */
   public void setLMSID( String iLMSID )
   {
       mLMSID = iLMSID;
   }
   
   /**
    * Sets the ID of the course with which the currently launched content is
    * associated.
    *
    * @param iCourseID  The course ID.
    */
   public void setCourseID( String iCourseID )
   {
      mCourseID = iCourseID;
   }
   
   /**
    * Sets the ID of the course with which the currently launched content is
    * associated.
    *
    * @param iCourseID  The course ID.
    */
   public void setOrgID( String iOrgID )
   {
       mOrgID = iOrgID;
   }

   /**
    * Sets the ID of the run-time data state of the currently launched content.
    *
    * @param iStateID  The run-time data state ID.
    */
   public void setStateID( String iStateID )
   {
      mStateID = iStateID;
   }

   /**
    * Sets the ID of the student experiencing the currently launched content.
    *
    * @param iUserID  The student ID.
    */
   public void setUserID( String iUserID )
   {
      mUserID = iUserID;
   }
   /**
    * Sets the name of the student experiencing the currently launched content.
    *
    * @param iUserName  The student Name.
    */
   public void setUserName( String iUserName )
   {
      mUserName = iUserName;
   }

   /**
    * Sets the number of the current attempt.
    *
    * @param iNumAttempts  The number of the current attempt.
    */
   public void setNumAttempts( long iNumAttempts )
   {
      mNumAttempts = iNumAttempts;
   }


    /**
    * Sets the number of the current attemptfrom a String parameter.
    *
    * @param iNumAttempts  The number of the current attempt.
    */
   public void setNumAttempts( String iNumAttempts )
   {
      Long tempLong = new Long( iNumAttempts );
      mNumAttempts = tempLong.longValue();
   }


   /**
    * Clears error codes and sets mInitialedState and mTerminated State to
    * default values.
    *
    * @param none
    */
   public void clearState()
   {
      mInitializedState = false;
      mTerminatedState = false;
      mTerminateCalled = false;
      mNavEvent = null;
      mLMSErrorManager.clearCurrentErrorCode();
   }

   public void setInfo()
   {
       WebContext ctx = WebContextFactory.get();
       HttpSession session = ctx.getSession();

       setLMSID( (String) session.getAttribute( "LMSID" ) );
       setCourseID( (String) session.getAttribute( "COURSEID" ) );
       setOrgID( (String) session.getAttribute( "ORGID" ) );
       setStateID( (String) session.getAttribute( "SCOID" ) );
       setActivityID( (String) session.getAttribute( "ACTIVITYID" ) );
       setUserID( (String) session.getAttribute( "userid" ) );
       setNumAttempts( (String) session.getAttribute( "NUMATTEMPTS" ) );
       setUserName( (String) session.getAttribute( "name" ) );
       
       mIsChoice = false;
       mTempEvent = "_none_";
   }
   
   public String getTempEvent()
   {
       return mTempEvent;
   }
   
   public boolean isChoice()
   {
       return mIsChoice;
   }
} // ClientRTS
