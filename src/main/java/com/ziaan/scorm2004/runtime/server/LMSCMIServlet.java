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
**
*******************************************************************************/

package com.ziaan.scorm2004.runtime.server;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.EduEtc1Bean;
import com.ziaan.scorm2004.ScormBlobBean;
import com.ziaan.scorm2004.datamodels.*;
import com.ziaan.scorm2004.sequencer.*;

import java.util.logging.*;

/**
 * The LMSCMIServlet class handles the server side data model communication 
 * of the Sample RTE.<br><br>
 * 
 * <strong>Filename:</strong> LMSCMIServletjava<br><br>
 *
 * <strong>Description:</strong><br>
 * This servlet handles persistence of the AICC Data Model elements.
 * Persistence is being handled via flat files and java object serialization
 * rather than through a database.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE 1.3. <br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * This servlet works in conjunction with the LMS RTEClient applet in the
 * org.adl.lms.client package.<br><br>
 * 
 * <strong>Known Problems:</strong><br><br>
 * 
 * <strong>Side Effects:</strong><br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS Specification
 *     <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class LMSCMIServlet extends HttpServlet
{

   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = false;

   /**
    * This is the value used for the primary objective ID    
    */
   public final String mPRIMARY_OBJ_ID =  null;   

   /**
    * This sets up Java Logging
    */
   private Logger mLogger = Logger.getLogger("org.adl.util.debug.samplerte");

   /**
    * The name of the target persisted run-time data model file.
    */
   private String mSCOFile = null;

   /**
    * The ID of the learner associated with the persisted run-time data.
    */
   private String mUserID = null;

   /**
    * The ID of the LMS associated with the persisted run-time data.
    */
   private String mLMSID = null;
   
   /**
    * The Name of the learner associated with the persisted run-time data.
    */
   private String mUserName = null;

   /**
    * The ID of the course associated with the persisted run-time data.
    */
   private String mCourseID = null;

   /**
    * The ID of the ORG associated with the persisted run-time data.
    */
   private String mOrgID = null;

   /**
    * The ID of the SCO associated with the persisted run-time data.
    */
   private String mSCOID = null;


   /**
    * The attempt number associated with the persisted run-time data.
    */
   private String mNumAttempt = null;

   /**
    * The ID of the activity associated with the persisted run-time data.
    */
   private String mActivityID = null;

   /**
    * The request issued by the LMS Client.
    */
   private LMSCMIServletRequest mRequest = null;

   /**
    * The response of this servlet.
    */
   private LMSCMIServletResponse mResponse = null;

   /**
    * The active set of run-time data.
    */
   private SCODataManager mSCOData = null;

   private String mSeqInfoDir = null;

   /**
    * The ID of the LMS associated with the persisted run-time data.
    */
   private String mSubj = "";
   
   private String mYear = "";
   
   private String mSubjseq = "";
   
   /**
    * This method handles the 'POST' message sent to the servlet.  This servlet
    * will handle <code>LMSServletRequest</code> objects and respond with
    * a <code>LMSServletResponse</code> object.
    * 
    * @param iRequest  The request 'POST'ed to the servlet.
    * 
    * @param oResponse The response returned by the servlet.
    * 
    * @exception ServletException
    * @exception IOException
    * @see <code>LMSServletRequest</code>
    * @see <code>LMSServletResponse</code>
    */
   public void doPost(HttpServletRequest iRequest,
        HttpServletResponse oResponse)
   		throws ServletException, IOException
   {
      ScormBlobBean sbb = new ScormBlobBean();
      
      mLogger.entering("---LMSCMIServlet", "doPost()");
      mLogger.info("POST received by LMSCMIServlet");

      try
      {
         /*
         System.out.println("Requested session: " + iRequest.getRequestedSessionId());
         System.out.println("query string: " + iRequest.getQueryString());
         System.out.println("header string: " + iRequest.getContextPath());

         for ( Enumeration e = iRequest.getHeaderNames(); e.hasMoreElements() ; )
         {
            System.out.println(e.nextElement().toString());
         }                               

         // Retrieve the current session ID
         HttpSession session = iRequest.getSession(false);
         if ( session == null )
         {
            mLogger.severe("  ERROR - No session ID in LMSCMIServlet.");
         }
         else
         {
            System.out.println("Session ID is: " + session.getId());
         }

         System.out.println("Checking attributes");
         */

         ObjectInputStream in = 
         new ObjectInputStream(iRequest.getInputStream());

         mLogger.info("Created REQUEST object INPUT stream successfully");

         ObjectOutputStream out = 
         new ObjectOutputStream(oResponse.getOutputStream());

         mLogger.info("Created RESPONSE object OUTPUT stream successfully");
         
         // Read the LMSCMIServletRequest object
         mRequest = (LMSCMIServletRequest)in.readObject();
         
         // Set servlet state
         mSCOID = mRequest.mStateID;
         mActivityID = mRequest.mActivityID;
         mCourseID = mRequest.mCourseID;
         mLMSID = mRequest.mLMSID;
         mOrgID = mRequest.mOrgID;
         mUserID = mRequest.mStudentID;
         mNumAttempt = mRequest.mNumAttempt;
         mUserName = mRequest.mUserName;
         //mSCOData = mRequest.mActivityData;

         String[] tempLMSID = mLMSID.split( "_" );
         if ( tempLMSID.length == 3 )
         {
             mSubj = tempLMSID[0];
             mYear = tempLMSID[1];
             mSubjseq = tempLMSID[2];
         }
         
         // TODO 테스트용 삭제
         if ( false )
         {
             System.out.println( "mLMSID" + mLMSID );
             System.out.println( "mSCOID" + mSCOID );
             System.out.println( "mActivityID" + mActivityID );
             System.out.println( "mCourseID" + mCourseID );
             System.out.println( "mOrgID" + mOrgID );
             System.out.println( "mUserID" + mUserID );
             System.out.println( "mNumAttempt" + mNumAttempt );
             System.out.println( "mUserName" + mUserName );
         }
         
         /*
         ConfigSet conf = new ConfigSet();
         mSeqInfoDir = conf.getProperty( "SCORM2004.SEQINFO.DIR" );
         
         // Set the run-time data model path
         if ( mNumAttempt != null )
         {
             mSCOFile = mSeqInfoDir + File.separator + mUserID + File.separator +
                 mLMSID + "_" + mCourseID + File.separator + mSCOID + "__" + mNumAttempt;
         }
         else
         {
            mLogger.fine("  ERROR: NULL # attempt");

            mSCOFile = mSeqInfoDir + File.separator + mUserID + File.separator +
                mLMSID + "_" + mCourseID + File.separator + mSCOID;
         }

         FileInputStream fi = null;
         ObjectInputStream file_in = null;
         */
         // Handle the request
         switch ( mRequest.mRequestType )
         {
            
            case LMSCMIServletRequest.TYPE_INIT :

               mLogger.info("CMI Servlet - doPost() - entering case INIT ");

               mLogger.info("Processing 'init' request");

               // create response object to return
               mResponse = new LMSCMIServletResponse();

               //Serialize the users activity tree for the selected course
               SeqActivityTree mSeqActivityTree = new SeqActivityTree();

               //String mTreePath = mSeqInfoDir + File.separator + mUserID + File.separator + mLMSID + "_" + mCourseID + File.separator + mOrgID;
               
               /*
               FileInputStream mFileIn = new FileInputStream(mTreePath);
               ObjectInputStream mObjectIn = new ObjectInputStream(mFileIn);
               mSeqActivityTree = (SeqActivityTree)mObjectIn.readObject();
               mObjectIn.close();
               */
               
               mSeqActivityTree = (SeqActivityTree) sbb.selectTreeObject( mLMSID, mCourseID, mOrgID, mUserID, "" );

               boolean new_file = true;
               FileHandler fileHandler = new FileHandler();
                
               /*
               // Try to open the state file
               try
               {
                  fi = new FileInputStream(mSCOFile);
                  new_file = false; 
               }
               catch ( FileNotFoundException fnfe )
               {
                  mLogger.info("State file does not exist...");

                  // data model file does not exist so initialize values

                  mLogger.info("Created file handler");
                  mLogger.info("About to create file");

                  fileHandler.initializeStateFile(mNumAttempt, mUserID, 
                                                  mUserName, mLMSID, mCourseID, 
                                                  mSCOID, mSCOID, mSeqInfoDir );

                  mLogger.info("after initialize state file");
                  mLogger.info("State File Created");

                  fi = new FileInputStream( mSCOFile );
               }

               mLogger.info("Created LMSSCODataFile File input stream " +
                            "successfully");

               file_in = new ObjectInputStream(fi);

               mLogger.info("Created OBJECT input stream successfully");

               // Initialize the new attempt
               mSCOData = (SCODataManager)file_in.readObject();
               file_in.close();
               */
               
               mSCOData = (SCODataManager) sbb.selectDataModelObject( mLMSID, mCourseID, mOrgID, mSCOID, mNumAttempt, mUserID );
               
               if ( mSCOData == null )
               {
                   // 새로 생성
                   new_file = true;

                   mSCOData = (SCODataManager) fileHandler.initializeStateFile( mLMSID, mCourseID, mOrgID, mSCOID, mNumAttempt, mUserID, mUserName );
                   
                   if ( _Debug )
                   {
                       System.out.println( "## DM Object : LMSCMIPROC 새로 생성" );
                   }
               }
                       
               
               // Create the sequencer and set the tree
               ADLSequencer mSequencer = new ADLSequencer();
               ADLValidRequests mState = new ADLValidRequests();
               SeqActivity mSeqActivity = mSeqActivityTree.getActivity( mSCOID );
               mSequencer.setActivityTree(mSeqActivityTree);

               // get UIState
               mSequencer.getValidRequests(mState);
               mResponse.mValidRequests = mState;
               mLogger.info( "continue  " + mResponse.mValidRequests.mContinue);
               mLogger.info( "previous  " + mResponse.mValidRequests.mPrevious);

               Vector mStatusVector = new Vector();

               mStatusVector = mSequencer.getObjStatusSet(mSCOID);

               ADLObjStatus mObjStatus = new ADLObjStatus();

               
               // Temporary variables for obj initialization
               int err = 0;
               String obj = new String();
               
               //  Initialize Objectives based on global objectives               
               if ( mStatusVector != null )
               {
                  if ( new_file )
                  {
                     for ( int i = 0; i< mStatusVector.size(); i++ )
                     {
                        // initialize objective status from sequencer
                        mObjStatus = (ADLObjStatus)mStatusVector.get(i);

                        // Set the objectives id
                        obj = "cmi.objectives." + i + ".id";

                        err = DMInterface.processSetValue(obj, mObjStatus.mObjID, true, mSCOData);

                        //  Set the objectives success status
                        obj = "cmi.objectives." + i + ".success_status";

                        if ( ((String)mObjStatus.mStatus).equalsIgnoreCase("satisfied") )
                        {
                           err = DMInterface.processSetValue(obj, "passed", true, mSCOData);
                        }
                        else if ( ((String)mObjStatus.mStatus).equalsIgnoreCase("notSatisfied") )
                        {
                           err = DMInterface.processSetValue(obj, "failed", true, mSCOData);
                        }

                        // Set the objectives scaled score
                        obj = "cmi.objectives." + i + ".score.scaled";

                        if ( mObjStatus.mHasMeasure )
                        {
                           Double norm = new Double(mObjStatus.mMeasure);
                           err = DMInterface.processSetValue(obj, norm.toString(), true, mSCOData);
                        }
                     }
                  } 
               }

               mResponse.mActivityData = mSCOData;

               // Need to return time tracking information
               // -+- TODO -+-
               initializeDB();

               out.writeObject(mResponse);
               mLogger.info("LMSCMIServlet processed init");

               break;

            case LMSCMIServletRequest.TYPE_GET :

               mLogger.info("Processing 'get' request");

               mResponse = new LMSCMIServletResponse();

               mResponse.mActivityData = (SCODataManager) sbb.selectDataModelObject( mLMSID, mCourseID, mOrgID, mSCOID, mNumAttempt, mUserID );
               
               if ( mResponse.mActivityData == null )
               {
                   mLogger.fine("ERROR == State data not created");
                   mResponse.mError = "NO DATA";
                   System.out.println( "## DM Object : LMSCMIPROC NO DATA" );
               }
               
               /*
               //  Try to open the state file
               try
               {
                  fi = new FileInputStream(mSCOFile);

                  mLogger.info("Created SCO data file input stream " +
                               "successfully");

                  file_in = new ObjectInputStream(fi);

                  mLogger.info("Created OBJECT input stream successfully");

                  mResponse.mActivityData = 
                     (SCODataManager)file_in.readObject();

               }
               catch ( FileNotFoundException fnfe )
               {
                  mLogger.fine("ERROR == State data not created");

                  mResponse.mError = "NO DATA";
               }

               file_in.close();
               */
               out.writeObject(mResponse);

               mLogger.info("LMSCMIServlet processed get for SCO Data\n");

               break;

            case LMSCMIServletRequest.TYPE_SET :

                mLogger.info("Processing 'set' request");

               //mResponse = new LMSCMIServletResponse();
               handleData(mRequest.mActivityData);

               out.writeObject(mResponse);

               mLogger.info("LMSCMIServlet processed set.");

               break;

            case LMSCMIServletRequest.TYPE_TIMEOUT :

                mLogger.info("Processing 'timeout' request");

               // -+- TODO -+- 

                mLogger.info("LMSCMIServlet processed 'timeout'");

               break;

            default:

               mLogger.severe("ERROR:  Bad Request Type.");

               break;
         }
         
         mResponse.mError = "OK";
         
         // Close the input and output streams
         
         in.close();
         out.close();
         
      }
      catch ( Exception e )
      {
         mLogger.severe(" :: doPost :: EXCEPTION");
         mLogger.severe(e.toString());
         //e.printStackTrace();
         mResponse.mError = "FAILED";
         System.out.println("error"+e);
      }
      
      //return mResponse;
   }


   /**
    * This method handles processing of the core data being sent from the client
    * to the LMS.  The data needs to be processed and made persistent.
    * 
    * @param iSCOData The run-time data to be processed.
    */
   private void handleData(SCODataManager iSCOData)
   {
      ScormBlobBean sbb = new ScormBlobBean();
      
      mLogger.info("LMSCMIServlet - Entering handleData()");      
      mResponse = new LMSCMIServletResponse();

      //String userDir = mSeqInfoDir + File.separator + mUserID + File.separator + mLMSID + "_" + mCourseID;

      boolean suspended = false;

      try
      {
         String completionStatus = null;
         String SCOEntry = null;
         double normalScore = -1.0;
         String masteryStatus = null;
         String sessionTime = null;
         String score = null;

         SCODataManager scoData = mRequest.mActivityData;

         // call terminate on the sco data
         // TODO 실행시 Navigation 문제 생김. 이유는 모르겠음.
         scoData.terminate();         

         int err = 0;
         DMProcessingInfo dmInfo = new DMProcessingInfo();

         // Get the current completion_status
         err = DMInterface.processGetValue("cmi.completion_status", true, 
                                           scoData, dmInfo);
         completionStatus = dmInfo.mValue;

         // Get the current success_status
         err = DMInterface.processGetValue("cmi.success_status", true, scoData, 
                                           dmInfo);
         masteryStatus = dmInfo.mValue;
         
         // Get the current entry
         err = DMInterface.processGetValue("cmi.entry", true, true, scoData, 
                                           dmInfo);
         SCOEntry = dmInfo.mValue;

         // Get the current scaled score
         err = DMInterface.processGetValue("cmi.score.scaled", true, scoData, 
                                           dmInfo);

         if ( err == DMErrorCodes.NO_ERROR )
         {
            mLogger.info("Got score, with no error");
            score = dmInfo.mValue;
         }
         else
         {
            mLogger.info("Failed getting score, got err: " + err);
            score = "";
         }
         
         // Get the current session time
         err = DMInterface.processGetValue("cmi.session_time",  true, scoData, dmInfo);
         
         if ( err == DMErrorCodes.NO_ERROR )
         {
            sessionTime = dmInfo.mValue;
         }

         mLogger.info("Saving Data to the File ...  PRIOR TO SAVE");
         mLogger.info("The SCO Data Manager for the current SCO contains the " +
                      "following:");

         // Open the Activity tree flat file associated with the
         // logged in user
//         String theWebPath = getServletConfig().getServletContext().getRealPath("/");

         //String actFile = userDir + File.separator + mOrgID;
         
         // Only perform data mapping on LMSFinish
         if ( mRequest.mIsFinished )
         {
            mLogger.info("About to get and update activity tree");

            ScormBlobBean ssb = new ScormBlobBean();
            SeqActivityTree theTree = (SeqActivityTree) ssb.selectTreeObject( mLMSID, mCourseID, mOrgID, mUserID, "" );            
            /*
            FileInputStream fi;
            try
            {
               fi = new FileInputStream(actFile);
            }
            catch ( FileNotFoundException fnfe )
            {
               mLogger.severe("Can not open Activity tree file");

               fi = new FileInputStream(actFile);
            }

            mLogger.info("Created Activity FILE input stream successfully");


            ObjectInputStream file_in = new ObjectInputStream(fi);

            mLogger.info("Created Activity Tree OBJECT input stream " +
                         "successfully");


            SeqActivityTree theTree = (SeqActivityTree)file_in.readObject();
            file_in.close();
            */
            mLogger.info("(*********DUMPING ActivityTree***********)");
            if ( theTree == null )
            {

                mLogger.info("The activity tree is NULL");
            }
            else
            {
               theTree.dumpState();
            }
            

            if ( theTree != null )
            {
               // Create the sequencer and set the tree
               ADLSequencer theSequencer = new ADLSequencer();
               theSequencer.setActivityTree( theTree );

               SeqActivity act = theTree.getActivity(mActivityID);

               // Update the activity's status
               mLogger.info("Performing default mapping to TM");


               // Get the activities objective list
               // Map the DM to the TM
               err = DMInterface.processGetValue("cmi.objectives._count", true, 
                                                 scoData, dmInfo);
               Integer size = new Integer(dmInfo.mValue);
               int numObjs = size.intValue();
               

               //  Loop through objectives updating TM
               for ( int i=0; i < numObjs; i++ )
               {
                  mLogger.info("CMISerlet - IN MAP OBJ LOOP");
                  String objID = new String("");
                  String objMS = new String("");
                  String objScore = new String("");
                  String obj = new String("");

                  // Get this objectives id
                  obj = "cmi.objectives." + i + ".id";
                  err = DMInterface.processGetValue(obj, true, scoData,
                                                    dmInfo);

                  objID = dmInfo.mValue;


                  // Get this objectives mastery
                  obj = "cmi.objectives." + i + ".success_status";
                  err = DMInterface.processGetValue(obj, true, scoData,
                                                    dmInfo);
                  objMS = dmInfo.mValue;
                  
                  //  Report the success status
                  if ( objMS.equals( "passed" ) )
                  {
                     theSequencer.setAttemptObjSatisfied(mActivityID,
                                                         objID,
                                                         "satisfied");
                  }
                  else if ( objMS.equals( "failed" ) )
                  {
                     theSequencer.setAttemptObjSatisfied(mActivityID,
                                                         objID,
                                                         "notSatisfied");
                  }
                  else
                  {
                     theSequencer.setAttemptObjSatisfied(mActivityID,
                                                         objID,
                                                         "unknown");
                  }

                  // Get this objectives measure
                  obj = "cmi.objectives." + i + ".score.scaled";
                  err = DMInterface.processGetValue(obj, true, scoData,
                                                    dmInfo);
                  if ( err == DMErrorCodes.NO_ERROR )
                  {
                     objScore = dmInfo.mValue;
                  }

                  // Report the measure
                  if ( !objScore.equals("") && 
                       !objScore.equals("unknown") )
                  {
                     try
                     {
                        normalScore = (new Double(objScore)).doubleValue();
                        theSequencer.setAttemptObjMeasure(mActivityID,
                                                          objID,
                                                          normalScore);
                     }
                     catch ( Exception e )
                     {
                        mLogger.severe("  ::--> ERROR: Invalid score");
                        mLogger.severe("  ::  " + normalScore);

                        mLogger.severe(e.toString());
                        e.printStackTrace();
                     }
                  }
                  else
                  {
                     theSequencer.clearAttemptObjMeasure(mActivityID,
                                                         objID);
                  }
               }

	            //  Report the completion status
               theSequencer.setAttemptProgressStatus(mActivityID,
                                                       completionStatus);


               if ( SCOEntry.equals("resume") )
               {
                  theSequencer.reportSuspension(mActivityID, true);
               }
               else
               {
                  theSequencer.reportSuspension(mActivityID, false);
               }
               
               //  Report the success status
               if ( masteryStatus.equals("passed") )
               {
                  theSequencer.setAttemptObjSatisfied(mActivityID,
                                                      mPRIMARY_OBJ_ID,
                                                      "satisfied");
               }
               else if ( masteryStatus.equals("failed") )
               {
                  theSequencer.setAttemptObjSatisfied(mActivityID,
                                                      mPRIMARY_OBJ_ID,
                                                      "notSatisfied");
               }
               else
               {
                  theSequencer.setAttemptObjSatisfied(mActivityID,
                                                      mPRIMARY_OBJ_ID,
                                                      "unknown");
               }
               

               // Report the measure
               if ( ! score.equals("") && !score.equals("unknown") )
               {
                  try
                  {
                     normalScore = (new Double(score)).doubleValue();
                     theSequencer.setAttemptObjMeasure(mActivityID,
                                                       mPRIMARY_OBJ_ID,
                                                       normalScore);
                  }
                  catch ( Exception e )
                  {
                     mLogger.severe("  ::--> ERROR: Invalid score");
                     mLogger.severe("  ::  " + normalScore);

                     mLogger.severe(e.toString());
                     e.printStackTrace();
                  }
               }
               else
               {
                  theSequencer.clearAttemptObjMeasure(mActivityID, 
                                                      mPRIMARY_OBJ_ID);
               }


               // May need to get the current valid requests
               mResponse.mValidRequests = new ADLValidRequests();
               theSequencer.getValidRequests(mResponse.mValidRequests);

               mLogger.info("Sequencer is initialized and statuses have been " +
                            "set");
               mLogger.info("Now re-serialize the file");
               
               sbb.insertTreeObject( mLMSID, mCourseID, mOrgID, mUserID, theSequencer.getActivityTree(), true );
               /*
               FileOutputStream fo = new FileOutputStream(actFile);

               ObjectOutputStream out_file = new ObjectOutputStream(fo);

               SeqActivityTree theTempTree = theSequencer.getActivityTree();

               theTempTree.clearSessionState();

               out_file.writeObject(theTempTree);
               out_file.close();
               */           
            }
         }

         sbb.insertDataModelObject( mLMSID, mCourseID, mOrgID, mSCOID, mNumAttempt, mUserID, mRequest.mActivityData );
         terminateDB(scoData);
         /*
         // Persist the run-time data model
         FileOutputStream fo = new FileOutputStream(mSCOFile);
         ObjectOutputStream out_file = new ObjectOutputStream(fo);
         out_file.writeObject(mRequest.mActivityData);
         out_file.close();
         */
         
         //terminateDB(scoData);
      }
      catch ( Exception e )
      {

         mLogger.severe(e.toString());
         e.printStackTrace(); 

      }
   }
   
   private void initializeDB() throws Exception
   {
       if ( _Debug )
       {
           System.out.println( "InitialzeDB Enter..... ");
       }
       
       DBConnectionManager connMgr = null;
       
       try
       {
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);

           //int isOk1 = setStartTime( connMgr );
           int isOk2 = setFirstEdu( connMgr );
           
           if ( isOk2 == 1 )
           {
               connMgr.commit();
               if ( _Debug )
               {
                   System.out.println( "  - InitialzeDB Commited..... ");
               }
           }
           else
           {
               connMgr.rollback();
           }
       }
       catch ( Exception e )
       {
           e.printStackTrace();
           
           connMgr.rollback();
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(connMgr != null) { try { connMgr.setAutoCommit(true); } catch (Exception e2) {} }            
           if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
       }       
   }
   
   private int setFirstEdu( DBConnectionManager connMgr ) throws Exception
   {
       int isOk = 0;
       
       PreparedStatement stmtSelectItem = null;
       PreparedStatement stmtInsertItem = null;
       PreparedStatement stmtUpdateItem = null;
       
       ResultSet rsItem = null;
       
       try
       {  
    
           // Get some information from the database
           String totalTime = "";
           
           String sqlSelectItem = 
               "\n  SELECT                                         " +
               "\n      subj                                       " +
               "\n  FROM                                           " +
               "\n      tz_progress                                " +
               "\n  WHERE 1 = 1                                    " +
               "\n      AND subj = ? AND year = ? AND subjseq = ?  " +
               "\n      AND lesson = ? AND oid = ? AND userid = ?  ";
           
           //System.out.println("sqlSelectItem:" + sqlSelectItem);
           
           stmtSelectItem = connMgr.prepareStatement(sqlSelectItem);
           
           stmtSelectItem.setString(1, mSubj );
           stmtSelectItem.setString(2, mYear );
           stmtSelectItem.setString(3, mSubjseq );
           stmtSelectItem.setString(4, mCourseID + "_" + mOrgID );
           stmtSelectItem.setString(5, mSCOID );
           stmtSelectItem.setString(6, mUserID );
           
           rsItem = stmtSelectItem.executeQuery();
           
           if (rsItem.next()) 
           {
               String sqlUpdateItem = 
                   "\n  UPDATE                                             " +
                   "\n      tz_progress                                    " +
                   "\n  SET                                                " +
                   "\n      ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')  " +
                   "\n  WHERE 1 = 1                                        " +
                   "\n      AND subj = ? AND year = ? AND subjseq = ?      " +
                   "\n      AND lesson = ? AND oid = ? AND userid = ?      ";
               
               stmtUpdateItem = connMgr.prepareStatement( sqlUpdateItem );
               
               stmtUpdateItem.setString( 1, mSubj );
               stmtUpdateItem.setString( 2, mYear );
               stmtUpdateItem.setString( 3, mSubjseq );
               stmtUpdateItem.setString( 4, mCourseID + "_" + mOrgID );
               stmtUpdateItem.setString( 5, mSCOID );
               stmtUpdateItem.setString( 6, mUserID );
               
               isOk = stmtUpdateItem.executeUpdate();
           } 
           else 
           {
               String sqlInsertItem = 
                   "\n  INSERT INTO tz_progress                                                            " +
                   "\n      ( subj, year, subjseq, lesson, oid, userid,                                    " +
                   "\n      lessonstatus, session_time, total_time,                                        " +
                   "\n      first_edu, lesson_count, ldate )                                               " +
                   "\n  VALUES                                                                             " +
                   "\n      ( ?, ?, ?, ?, ?, ?,                                                            " +
                   "\n      'incomplete', '00:00:00.00', '00:00:00.00',                                               " +
                   "\n      to_char(sysdate,'YYYYMMDDHH24MISS'), 0, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') ) ";
               
               stmtInsertItem = connMgr.prepareStatement( sqlInsertItem );
               
               stmtInsertItem.setString( 1, mSubj );
               stmtInsertItem.setString( 2, mYear );
               stmtInsertItem.setString( 3, mSubjseq );
               stmtInsertItem.setString( 4, mCourseID + "_" + mOrgID );
               stmtInsertItem.setString( 5, mSCOID );
               stmtInsertItem.setString( 6, mUserID );
               
               isOk = stmtInsertItem.executeUpdate();
           }
       }
       catch ( Exception e )
       {
           e.printStackTrace();
           
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(rsItem != null) { try { rsItem.close(); } catch (Exception e1 ) {} }
           if(stmtSelectItem != null) { try { stmtSelectItem.close(); } catch (Exception e1 ) {} }
           if(stmtInsertItem != null) { try { stmtInsertItem.close(); } catch (Exception e2 ) {} }
           if(stmtUpdateItem != null) { try { stmtUpdateItem.close(); } catch (Exception e3 ) {} }
       }
       
       return isOk;
   }


   private void terminateDB( SCODataManager scoData ) throws Exception
   {
       if ( _Debug )
       {
           System.out.println( "TerminateDB Enter..... ");
       }
       
       DBConnectionManager connMgr = null;
       
       try
       {
           connMgr = new DBConnectionManager();
           connMgr.setAutoCommit(false);

           //int isOk1 = setCMIData( connMgr, scoData );
           int isOk2 = setProgressData( connMgr, scoData );
           
           if ( isOk2 == 1 )
           {
               connMgr.commit();
               if ( _Debug )
               {
                   System.out.println( "  - TerminateDB Commited..... ");
               }               
           }
           else
           {
               connMgr.rollback();
           }
       }
       catch ( Exception e )
       {
           e.printStackTrace();
           
           connMgr.rollback();
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(connMgr != null) { try { connMgr.setAutoCommit(true); } catch (Exception e2) {} }            
           if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
       }
   }
   
   private int setProgressData( DBConnectionManager connMgr, SCODataManager scoData ) throws Exception 
   {
       int isOk = 0;
       
       PreparedStatement stmtSelectItem = null;
       PreparedStatement stmtUpdateItem = null;

       ResultSet rsItem = null;
       
       try
       {
           String sqlSelectItem = 
               "\n  SELECT                                                                 " +
               "\n      lessonstatus, total_time, ldate, TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') nowtime,  " +
               "\n      first_end                                                          " +
               "\n  FROM                                                                   " +
               "\n      tz_progress                                                        " +
               "\n  WHERE 1=1                                                              " +
               "\n      AND subj = ? AND year = ? AND subjseq = ?                          " +
               "\n      AND lesson = ? AND oid = ? AND userid = ?                          ";
           
           String sqlUpdateItem = "";
           
           stmtSelectItem = connMgr.prepareStatement(sqlSelectItem);
           
           stmtSelectItem.setString( 1, mSubj );
           stmtSelectItem.setString( 2, mYear );
           stmtSelectItem.setString( 3, mSubjseq );
           stmtSelectItem.setString( 4, mCourseID + "_" + mOrgID );
           stmtSelectItem.setString( 5, mSCOID );
           stmtSelectItem.setString( 6, mUserID );
           
           rsItem = stmtSelectItem.executeQuery();
           
           if (rsItem.next()) 
           {
               
               String completionStatus = "";

               String v_lessonstatus  = "";
               String v_total_time    = "";
               String v_ldate         = "";
               String v_sysdate       = "";
               String v_session_time  = "";
               
               boolean isFirstEnd = false;
               
               int err = 0;
               DMProcessingInfo dmInfo = new DMProcessingInfo();
               
               err = DMInterface.processGetValue("cmi.completion_status", true, scoData, dmInfo);
               if (err == DMErrorCodes.NO_ERROR) completionStatus = dmInfo.mValue;
               

               v_lessonstatus = rsItem.getString("LESSONSTATUS");

               // 기존 진도체크 상태 유지를 위함.
               if ( completionStatus.equals("completed"))
               {
                   v_lessonstatus = "complete";
               }
               
               if ( v_lessonstatus == null || v_lessonstatus.equals("") )
               {
                   v_lessonstatus = "incomplete";
               }
               
               // 최초 학습 종료시간이 있으면, 
               String v_first_end = rsItem.getString("FIRST_END");
               if ( v_lessonstatus.equals("complete") && (v_first_end == null || v_first_end.equals("")) )
               {
                   isFirstEnd = false;
               }
               else
               {
                   isFirstEnd = true;
               }
               
               v_total_time    = rsItem.getString("TOTAL_TIME");
               if (v_total_time == null || ("").equals(v_total_time))
               {
                   v_total_time = "00:00:00.00";
               }

               v_ldate = rsItem.getString("LDATE");
               v_sysdate = rsItem.getString("NOWTIME");

               v_session_time = EduEtc1Bean.get_duringtime(v_ldate,v_sysdate);
               v_total_time = EduEtc1Bean.add_duringtime(v_total_time,v_session_time);
               
               sqlUpdateItem = 
                   "\n  UPDATE tz_progress                                      " +
                   "\n     SET lessonstatus = ?,                                " +
                   "\n         session_time = ?,                                " +
                   "\n         total_time = ?,                                  " +
                   "\n         :firstCondition                                  " +
                   "\n         lesson_count = ?,                                " +
                   "\n         ldate = to_char(sysdate,'YYYYMMDDHH24MISS')      " +
                   "\n   WHERE 1 = 1                                            " +
                   "\n     AND subj = ?                                         " +
                   "\n     AND year = ?                                         " +
                   "\n     AND subjseq = ?                                      " +
                   "\n     AND lesson = ?                                       " +
                   "\n     AND oid = ?                                          " +
                   "\n     AND userid = ?                                       ";
    
               if ( !isFirstEnd )
               {
                   sqlUpdateItem = sqlUpdateItem.replaceAll( ":firstCondition", "first_end = to_char(sysdate,'YYYYMMDDHH24MISS')," );
               }
               else
               {
                   sqlUpdateItem = sqlUpdateItem.replaceAll( ":firstCondition", "" );
               }
                   
               stmtUpdateItem = connMgr.prepareStatement( sqlUpdateItem );
    
               stmtUpdateItem.setString( 1, v_lessonstatus );
               stmtUpdateItem.setString( 2, v_session_time );
               stmtUpdateItem.setString( 3, v_total_time ); //total_time
               stmtUpdateItem.setString( 4, mNumAttempt );
               stmtUpdateItem.setString( 5, mSubj );
               stmtUpdateItem.setString( 6, mYear );
               stmtUpdateItem.setString( 7, mSubjseq );
               stmtUpdateItem.setString( 8, mCourseID + "_" + mOrgID );
               stmtUpdateItem.setString( 9, mSCOID );
               stmtUpdateItem.setString( 10, mUserID );

               isOk = stmtUpdateItem.executeUpdate();
           }
                      
           stmtUpdateItem.close();
       }
       catch ( Exception e )
       {
           e.printStackTrace();
           
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(rsItem != null) { try { rsItem.close(); } catch (Exception e1 ) {} }
           if(stmtSelectItem != null) { try { stmtSelectItem.close(); } catch (Exception e1 ) {} }
           if(stmtUpdateItem != null) { try { stmtUpdateItem.close(); } catch (Exception e3 ) {} }
       }
       
       return isOk;
   }


private int setStartTime( DBConnectionManager connMgr ) throws Exception
   {
       int isOk = 0;
       
       PreparedStatement stmtSelectItem = null;
       PreparedStatement stmtInsertItem = null;
       PreparedStatement stmtUpdateItem = null;
       
       ResultSet rsItem = null;
       
       try
       {  
           // Get some information from the database
           String totalTime = "";
           
           String sqlSelectItem = 
               "\n  SELECT                                         " +
               "\n      course_code                                " +
               "\n  FROM                                           " +
               "\n      tys_cmi_objectinfo                         " +
               "\n  WHERE 1 = 1                                    " +
               "\n      AND subj = ? AND year = ? AND subjseq = ?  " +
               "\n      AND course_code = ? AND org_id = ?         " +
               "\n      AND sco_id = ? AND userid = ?              ";
           
           String sqlInsertItem = "";
           String sqlUpdateItem = "";
           
           //System.out.println("sqlSelectItem:" + sqlSelectItem);
           
           stmtSelectItem = connMgr.prepareStatement(sqlSelectItem);
           //ResultSet rsUser = null;
           
           stmtSelectItem.setString(1, mSubj);
           stmtSelectItem.setString(2, mYear);
           stmtSelectItem.setString(3, mSubjseq);
           stmtSelectItem.setString(4, mCourseID);
           stmtSelectItem.setString(5, mOrgID);
           stmtSelectItem.setString(6, mSCOID);
           stmtSelectItem.setString(7, mUserID);
           rsItem = stmtSelectItem.executeQuery();
           
           if (rsItem.next()) 
           {
               sqlUpdateItem = 
                   "\n  UPDATE                                                                 " +
                   "\n      tys_cmi_objectinfo                                                 " +
                   "\n  SET luserid = ?,                                                       " +
                   "\n      ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS')                      " +
                   "\n  WHERE 1 = 1                                                            " +
                   "\n      AND subj = ? AND year = ? AND subjseq = ?                          " +
                   "\n      AND course_code = ? AND org_id = ? AND sco_id = ? AND userid = ?   ";
               
               stmtUpdateItem = connMgr.prepareStatement( sqlUpdateItem );
               
               stmtUpdateItem.setString( 1, mUserID );
               stmtUpdateItem.setString( 2, mSubj );
               stmtUpdateItem.setString( 3, mYear );
               stmtUpdateItem.setString( 4, mSubjseq );
               stmtUpdateItem.setString( 5, mCourseID );
               stmtUpdateItem.setString( 6, mOrgID );
               stmtUpdateItem.setString( 7, mSCOID );
               stmtUpdateItem.setString( 8, mUserID );
               
               isOk = stmtUpdateItem.executeUpdate();
           } 
           else 
           {
               sqlInsertItem =
                   "\n  INSERT INTO tys_cmi_objectinfo                     " +
                   "\n      ( subj, year, subjseq, course_code, org_id,    " +
                   "\n      sco_id, userid, name, luserid, ldate )                 " +
                   "\n  VALUES                                             " +
                   "\n      ( ?, ?, ?, ?, ?,                               " +
                   "\n      ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') )";
               
               stmtInsertItem = connMgr.prepareStatement( sqlInsertItem );
               
               stmtInsertItem.setString( 1, mSubj );
               stmtInsertItem.setString( 2, mYear );
               stmtInsertItem.setString( 3, mSubjseq );
               stmtInsertItem.setString( 4, mCourseID );
               stmtInsertItem.setString( 5, mOrgID );
               stmtInsertItem.setString( 6, mSCOID );
               stmtInsertItem.setString( 7, mUserID );
               stmtInsertItem.setString( 8, mUserName );
               stmtInsertItem.setString( 9, mUserID );
               
               isOk = stmtInsertItem.executeUpdate();
           }
       }
       catch ( Exception e )
       {
           e.printStackTrace();
           
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(rsItem != null) { try { rsItem.close(); } catch (Exception e1 ) {} }
           if(stmtSelectItem != null) { try { stmtSelectItem.close(); } catch (Exception e1 ) {} }
           if(stmtInsertItem != null) { try { stmtInsertItem.close(); } catch (Exception e2 ) {} }
           if(stmtUpdateItem != null) { try { stmtUpdateItem.close(); } catch (Exception e3 ) {} }
       }
       
       return isOk;
   }
   
   private int setCMIData( DBConnectionManager connMgr, SCODataManager scoData ) throws Exception
   {
       int isOk = 0;
       
       PreparedStatement stmtSelectItem = null;
       PreparedStatement stmtUpdateItem = null;

       ResultSet rsItem = null;
       
       try
       {
           // Get some information from the database
           String completionStatus = "";
           String masteryStatus = "";
           String SCOEntry = "";
           String score = "";
           String completionThreshold = "";
           String credit = "";
           String exitStatus = "";
           String location = "";
           String maxTimeAllowed = "";
           String modeInfo = "";
           String progressMeasure = "";
           String scaledPassingScore = "";
           String scoreScaled = "";
           String scoreRaw = "";
           String scoreMin = "";
           String scoreMax = "";
           String successStatus = "";
           String timeLimitAction = "";
           String suspendData = "";
           String totalTime = "";
           String sessionTime = "";           
           String v_total_time    = "";
           String v_ldate         = "";
           String v_sysdate       = "";
           String v_session_time  = "";
           boolean sessionFlag    = false;           
           
           int err = 0;
           DMProcessingInfo dmInfo = new DMProcessingInfo();
           err = DMInterface.processSetValue("cmi.entry", "resume", true, scoData);
           
           err = DMInterface.processGetValue("cmi.total_time", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) totalTime = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.completion_status", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) completionStatus = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.success_status", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) masteryStatus = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.entry", true, true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) SCOEntry = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.score.scaled", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR)    score = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.completion_threshold", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) completionThreshold = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.credit", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) credit = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.exit", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) exitStatus = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.location", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) location = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.max_time_allowed", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) maxTimeAllowed = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.mode", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) modeInfo = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.progress_measure", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) progressMeasure = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.scaled_passing_score", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) scaledPassingScore = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.score.scaled", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) scoreScaled = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.score.raw", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) scoreRaw = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.score.max", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) scoreMax = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.score.min", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) scoreMin = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.success_status", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) successStatus = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.time_limit_action", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) timeLimitAction = dmInfo.mValue;
           
           err = DMInterface.processGetValue("cmi.suspend_data", true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) suspendData = dmInfo.mValue;

           err = DMInterface.processGetValue("cmi.session_time", true, true, scoData, dmInfo);
           if (err == DMErrorCodes.NO_ERROR) sessionTime = dmInfo.mValue;
           
           if ((sessionTime != null && !(("").equals(sessionTime))) || (sessionTime != null && !(("PT0H0M0S").equals(sessionTime))) ) 
           {
               System.out.println("sessionTime:" + sessionTime);
               sessionFlag = true;
           }
           
           String sqlSelectItem = 
               "\n  SELECT                                                                 " +
               "\n      total_time, ldate, TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS') nowtime   " +
               "\n  FROM                                                                   " +
               "\n      tys_cmi_objectinfo                                                 " +
               "\n  WHERE 1=1                                                              " +
               "\n      AND subj = ? AND year = ? AND subjseq = ?                          " +
               "\n      AND course_code = ? AND org_id = ? AND sco_id = ? AND userid = ?   ";
           
           //String sqlInsertItem = "";
           String sqlUpdateItem = "";
           
           
           //System.out.println("sqlSelectItem:" + sqlSelectItem);
           
           stmtSelectItem = connMgr.prepareStatement(sqlSelectItem);
           //ResultSet rsUser = null;
           
           stmtSelectItem.setString(1, mSubj);
           stmtSelectItem.setString(2, mYear);
           stmtSelectItem.setString(3, mSubjseq);
           stmtSelectItem.setString(4, mCourseID);
           stmtSelectItem.setString(5, mOrgID);
           stmtSelectItem.setString(6, mSCOID);
           stmtSelectItem.setString(7, mUserID);
           
           rsItem = stmtSelectItem.executeQuery();
           
           if (rsItem.next()) 
           {
               v_total_time    = rsItem.getString("TOTAL_TIME");
               if (v_total_time == null || ("").equals(v_total_time)) v_total_time = "00:00:00.00";
               v_ldate         = rsItem.getString("LDATE");
               v_sysdate       = rsItem.getString("NOWTIME");
               
               v_session_time  = EduEtc1Bean.get_duringtime(v_ldate,v_sysdate);
               v_total_time    = EduEtc1Bean.add_duringtime(v_total_time,v_session_time);
           }
           else 
           {
               v_session_time  = "00:00:00.00";
               v_total_time = "00:00:00.00";
           }
           
           sqlUpdateItem = 
               "\n  UPDATE                                                                 " +
               "\n      tys_cmi_objectinfo                                                 " +
               "\n  SET total_time = ?,                                                    " +
               "\n      score_scaled = ?,                                                  " +
               "\n      completion_status = ?,                                             " +
               "\n      completion_threshold = ?,                                          " +
               "\n      entry_info = ?,                                                    " +
               "\n      credit = ?,                                                        " +
               "\n      exit_status = ?,                                                   " +
               "\n      launch_data = ?,                                                   " +
               "\n      LOCATION = ?,                                                      " +
               "\n      max_time_allowed = ?,                                              " +
               "\n      mode_info = ?,                                                     " +
               "\n      progress_measure = ?,                                              " +
               "\n      scaled_passing_score = ?,                                          " +
               "\n      success_status = ?,                                                " +
               "\n      suspend_data = ?,                                                  " +
               "\n      time_limit_action = ?,                                             " +
               "\n      score_raw = ?,                                                     " +
               "\n      score_max = ?,                                                     " +
               "\n      score_min = ?,                                                     " +
               "\n      learner_preference_audio_level = ?,                                " +
               "\n      learner_preference_language = ?,                                   " +
               "\n      learner_preference_delivery_sp = ?,                                " +
               "\n      learner_preference_audio_capti = ?,                                " +
               "\n      luserid = ?,                                                       " +
               "\n      ldate = TO_CHAR (SYSDATE, 'YYYYMMDDHH24MISS'),                     " +
               "\n      attempt = ?,                                                       " +
               "\n      session_time = ?                                                   " +
               "\n   WHERE 1 = 1                                                           " +
               "\n      AND subj = ? AND year = ? AND subjseq = ?                          " +
               "\n      AND course_code = ? AND org_id = ? AND sco_id = ? AND userid = ?   ";
           
           stmtUpdateItem = connMgr.prepareStatement( sqlUpdateItem );
           
           stmtUpdateItem.setString( 1, v_total_time ); //total_time
           stmtUpdateItem.setString( 2, scoreScaled );
           stmtUpdateItem.setString( 3, completionStatus );
           stmtUpdateItem.setString( 4, completionThreshold );
           stmtUpdateItem.setString( 5, SCOEntry );
           stmtUpdateItem.setString( 6, credit );
           stmtUpdateItem.setString( 7, exitStatus );
           stmtUpdateItem.setString( 8, "" ); //launch_data
           stmtUpdateItem.setString( 9, location );
           stmtUpdateItem.setString( 10, maxTimeAllowed );
           stmtUpdateItem.setString( 11, modeInfo );
           stmtUpdateItem.setString( 12, progressMeasure );
           stmtUpdateItem.setString( 13, scaledPassingScore );
           stmtUpdateItem.setString( 14, successStatus );
           stmtUpdateItem.setString( 15, suspendData );
           stmtUpdateItem.setString( 16, timeLimitAction );
           stmtUpdateItem.setString( 17, scoreRaw );
           stmtUpdateItem.setString( 18, scoreMax );
           stmtUpdateItem.setString( 19, scoreMin );
           stmtUpdateItem.setString( 20, "" );
           stmtUpdateItem.setString( 21, "" );
           stmtUpdateItem.setString( 22, "" );
           stmtUpdateItem.setString( 23, "" );
           stmtUpdateItem.setString( 24, mUserID );
           stmtUpdateItem.setString( 25, mNumAttempt );
           stmtUpdateItem.setString( 26, v_session_time );
           stmtUpdateItem.setString( 27, mSubj );
           stmtUpdateItem.setString( 28, mYear );
           stmtUpdateItem.setString( 29, mSubjseq );
           stmtUpdateItem.setString( 30, mCourseID );
           stmtUpdateItem.setString( 31, mOrgID );
           stmtUpdateItem.setString( 32, mSCOID );
           stmtUpdateItem.setString( 33, mUserID );
           
           isOk = stmtUpdateItem.executeUpdate();

           stmtUpdateItem.close();
           
           /*   
            } else {
            sqlInsertItem = "INSERT INTO TYS_CMI_OBJECTINFO ( COURSE_CODE, SCO_ID, LEARNER_ID, LEARNER_NAME, TOTAL_TIME," +
            "SCORE_SCALED, COMPLETION_STATUS, COMPLETION_THRESHOLD, ENTRY_INFO, CREDIT, EXIT_STATUS, LAUNCH_DATA," +
            "LOCATION, MAX_TIME_ALLOWED, MODE_INFO, PROGRESS_MEASURE, SCALED_PASSING_SCORE, SUCCESS_STATUS," +
            "SUSPEND_DATA, TIME_LIMIT_ACTION, SCORE_RAW, SCORE_MAX, SCORE_MIN, LEARNER_PREFERENCE_AUDIO_LEVEL," +
            "LEARNER_PREFERENCE_LANGUAGE, LEARNER_PREFERENCE_DELIVERY_SP, LEARNER_PREFERENCE_AUDIO_CAPTI," +
            "INUSERID, INDATE, ATTEMPT ) VALUES (" +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_CHAR( sysdate, 'YYYYMMDDHH24MISS'), ?)";            
            stmtInsertItem = connMgr.prepareStatement( sqlInsertItem );
            
            synchronized( stmtInsertItem )
            {
            stmtInsertItem.setString( 1, mCourseID );
            stmtInsertItem.setString( 2, mSCOID );
            stmtInsertItem.setString( 3, mUserID );
            stmtInsertItem.setString( 4, mUserName );
            stmtInsertItem.setString( 5, totalTime ); //total_time
            stmtInsertItem.setString( 6, scoreScaled );
            stmtInsertItem.setString( 7, completionStatus );
            stmtInsertItem.setString( 8, completionThreshold );
            stmtInsertItem.setString( 9, SCOEntry );
            stmtInsertItem.setString( 10, credit );
            stmtInsertItem.setString( 11, exitStatus );
            stmtInsertItem.setString( 12, "" ); //launch_data
            stmtInsertItem.setString( 13, location ); 
            stmtInsertItem.setString( 14, maxTimeAllowed );
            stmtInsertItem.setString( 15, modeInfo );
            stmtInsertItem.setString( 16, progressMeasure );
            stmtInsertItem.setString( 17, scaledPassingScore );
            stmtInsertItem.setString( 18, successStatus );
            stmtInsertItem.setString( 19, suspendData );
            stmtInsertItem.setString( 20, timeLimitAction );
            stmtInsertItem.setString( 21, scoreRaw );
            stmtInsertItem.setString( 22, scoreMax );
            stmtInsertItem.setString( 23, scoreMin );
            stmtInsertItem.setString( 24, "" );
            stmtInsertItem.setString( 25, "" );
            stmtInsertItem.setString( 26, "" );
            stmtInsertItem.setString( 27, "" );
            stmtInsertItem.setString( 28, mUserID );
            stmtInsertItem.setString( 29, mNumAttempt );
            
            stmtInsertItem.executeUpdate();
            }
            
            }
            */
           
            //session Time 처리
            //totalTime = this.convertTime(totalTime, sessionTime, false);
//            DMTimeUtility dmu = new DMTimeUtility();
//            totalTime = dmu.add(totalTime, sessionTime);
//            err = DMInterface.processSetValue("cmi.total_time", totalTime, true, scoData);
       }
       catch ( Exception e )
       {
           e.printStackTrace();
           
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(rsItem != null) { try { rsItem.close(); } catch (Exception e1 ) {} }
           if(stmtSelectItem != null) { try { stmtSelectItem.close(); } catch (Exception e1 ) {} }
           if(stmtUpdateItem != null) { try { stmtUpdateItem.close(); } catch (Exception e3 ) {} }
       }
       
       return isOk;
   }
   
   private String convertTime(String scormTime, String sessionTime, boolean flag)
   {
       String returnTotalStudyTime = null;
       String hh = "00";
       String mm = "00";
       String ss = "00";
       
       if (flag) {
           if (scormTime.indexOf("PT") > -1) {

               if(scormTime.indexOf("H") > -1)
               {
                   hh = scormTime.substring(scormTime.indexOf("T") + 1, scormTime.indexOf("H"));
                   if (hh.length() == 1) hh = "0" + hh;
               }
               if(scormTime.indexOf("M") > -1)
               {
                   if (scormTime.indexOf("H") > -1) mm = scormTime.substring(scormTime.indexOf("H") + 1, scormTime.indexOf("M"));
                   else mm = scormTime.substring(scormTime.indexOf("T") + 1, scormTime.indexOf("M"));
                   if (mm.length() == 1) mm = "0" + mm;
               }
               if(scormTime.indexOf("S") > -1)
               {
                   if (scormTime.indexOf("M") > -1 ) ss = scormTime.substring(scormTime.indexOf("M") + 1, scormTime.indexOf("S"));
                   else if (scormTime.indexOf("M") < 0 && scormTime.indexOf("H") > -1) ss = scormTime.substring(scormTime.indexOf("H") + 1, scormTime.indexOf("S"));
                   else if (scormTime.indexOf("M") < 0 && scormTime.indexOf("H") < 0) ss = scormTime.substring(scormTime.indexOf("T") + 1, scormTime.indexOf("S"));
                   if (ss.length() == 1) ss = "0" + ss;
               }
           }
           
           returnTotalStudyTime = hh + ":" + mm + ":" + ss + ".00";

       } else {
        
            long hhL = 0;
            long mmL = 0;
            long ssL = 0;
            long shhL = 0;
            long smmL = 0;
            long sssL = 0;
            
            if (scormTime == null) scormTime = "PT0H0M0S";
            
            if (scormTime.indexOf("H") > -1) {
                hhL = Long.parseLong(scormTime.substring(scormTime.indexOf("T")+1, scormTime.indexOf("H")));
            }
            if (scormTime.indexOf("M") > -1) { 
                if (scormTime.indexOf("H") > -1) mmL = Long.parseLong(scormTime.substring(scormTime.indexOf("H") + 1, scormTime.indexOf("M")));
                else mmL = Long.parseLong(scormTime.substring(scormTime.indexOf("T") + 1, scormTime.indexOf("M")));
            }

            if (scormTime.indexOf("S") > -1) {
                if (scormTime.indexOf("M") > -1 ) 
                    ssL = Long.parseLong(scormTime.substring(scormTime.indexOf("M")+1, scormTime.indexOf("S")));
                else if (scormTime.indexOf("M") < 0 && scormTime.indexOf("H") > -1) 
                    ssL = Long.parseLong(scormTime.substring(scormTime.indexOf("H") + 1, scormTime.indexOf("S")));
                else if (scormTime.indexOf("M") < 0 && scormTime.indexOf("H") < 0) 
                    ssL = Long.parseLong(scormTime.substring(scormTime.indexOf("T") + 1, scormTime.indexOf("S")));
            }

            if (sessionTime == null) sessionTime = "PT0H0M0S";
            
            if (sessionTime.indexOf("H") > -1) {
                shhL = Long.parseLong(sessionTime.substring(sessionTime.indexOf("T")+1, sessionTime.indexOf("H")));
            }
            if (sessionTime.indexOf("M") > -1) {
                if (sessionTime.indexOf("H") > -1) smmL = Long.parseLong(sessionTime.substring(sessionTime.indexOf("H")+1, sessionTime.indexOf("M")));
                else smmL = Long.parseLong(sessionTime.substring(sessionTime.indexOf("T") + 1, sessionTime.indexOf("M")));
            }

            if (sessionTime.indexOf("S") > 0) {
                if (sessionTime.indexOf("M") > 0 )
                    sssL = Long.parseLong(sessionTime.substring(sessionTime.indexOf("M")+1, sessionTime.indexOf("S")));
                else if (sessionTime.indexOf("M") < 0 && sessionTime.indexOf("H") > -1) 
                    sssL = Long.parseLong(sessionTime.substring(sessionTime.indexOf("H") + 1, sessionTime.indexOf("S")));
                else if (sessionTime.indexOf("M") < 0 && sessionTime.indexOf("H") < 0) 
                    sssL = Long.parseLong(sessionTime.substring(sessionTime.indexOf("T") + 1, sessionTime.indexOf("S")));
            }
            
            long tTime = hhL*3600 + mmL*60 + ssL + shhL*3600 + smmL*60 + sssL;
            
            int h = (int)(tTime / 3600);
            int m = (int)((tTime % 3600)/60);
            int s = (int)(((tTime % 3600)%60));
            
            returnTotalStudyTime = "PT"+h+"H"+m+"M"+s+"S";
            
       }

       return returnTotalStudyTime;
   }   
} // LMSCMIServlet

