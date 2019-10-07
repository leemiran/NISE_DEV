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
package com.ziaan.scorm2004.runtime.server;

//Native java imports
import java.io.*;
import java.sql.*;

//ADL imports
import com.ziaan.library.ConfigSet;
import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.ErrorManager;
import com.ziaan.scorm2004.ScormBlobBean;
import com.ziaan.scorm2004.datamodels.DMInterface;
import com.ziaan.scorm2004.datamodels.DMFactory;
import com.ziaan.scorm2004.datamodels.SCODataManager;

/**
 * <strong>Filename:</strong> RTEFilehandler.java<br>
 *
 * <strong>Description:</strong><br>
 * <code>RTEFilehandler</code> class is a utility class.  It contains methods 
 * to create a state file used to store datamodel information and to inititalize
 * datamodel elements based on elements in the imsmanifest.xml file.  It 
 * contains a method that queries teh database for initialized datamodel values 
 * and stores those values in String array.  In  addition, this class contains 
 * logic to delete any course files and temporary uploaded packages after a 
 * successful import.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM 2004 Sample 
 * Run-Time Environment Version 1.3.<br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br><br>
 * 
 * <strong>Known Problems:</strong><br><br>
 * 
 * <strong>Side Effects:</strong><br><br>
 * 
 *  
 * @author ADL Technical Team
 */
public class FileHandler
{
    /**
     * The Sample RTE flat file root directory
     */
    private String mSampleRTERoot;
    
    /**
     * Debug indicator boolean
     */
    private static boolean _Debug = false;    
    
    /**
     * Default Constructor for the <code>FileHandler</code>
     */
    public FileHandler()
    {
        mSampleRTERoot = File.separator + "SampleRTE133Files";
    }
    
    /**
     * <code>initializeStateFile</code>
     * This method is used to create a state file for an item.  It is called 
     * by <code>LMSCMIServlet</code>when a item is entered for the first time.
     * 
     * @param iNumAttempt  Number of the current attempt on the item
     * @param iUserID  The Sample RTE's unique user identifier for
     *        a learner 
     * @param iUserName  The name of the user 
     * @param iCourseID  The unique course identifier
     * @param iItemID  The item's identifier
     * @param dbID  The item's unique identifier (may be the same as the iItemID)
     *              
     */
    public Object initializeStateFile( String iLMSID, String iCourseID, String iOrgID, String iItemID, String iNumAttempt, String iUserID, String iUserName )
    {
       SCODataManager scoData = null;
       
       try
       {  
          if ( _Debug )
          {
             System.out.println("**** IN INITIALIZESTATEFILE****");
          }
          
          // Now create a SCODataManager object, initialize values, and serialize to DB for SCO
          scoData = new SCODataManager();
          
          // Add a SCORM 2004 Data Model
          scoData.addDM(DMFactory.DM_SCORM_2004);

          // Add a SCORM 2004 Nav Data Model
          scoData.addDM(DMFactory.DM_SCORM_NAV);

          initSCOData(scoData, iUserID, iUserName, iCourseID, iItemID );
          
          ScormBlobBean sbb = new ScormBlobBean();
          sbb.insertDataModelObject( iLMSID, iCourseID, iOrgID, iItemID, iNumAttempt, iUserID, scoData );
          
          if ( _Debug )
          {
              System.out.println( "## DM Object : FileHandler 쓰기 성공" );
          }
       }
       catch(Exception e)
       {
          e.printStackTrace();
       }
       
       return scoData;
    }
    
    /**
     * This method deletes the course files for a student/course when a 
     * registration is removed.
     * 
     * @param iDeleteCourseID  The courseId of course to delete course files
     * 
     * @param iUserID  The user identifier associated with the files to delete
     */                                                      
    public void deleteCourseFiles(String courseCode, String userid)
    {
        try
        {
            ConfigSet conf = new ConfigSet();
            String seqInfoDir = conf.getProperty("SCORM2004.SEQINFO.DIR");
            
            String userDir = seqInfoDir + File.separator + userid + File.separator + courseCode;

            if ( _Debug )
            {
                System.out.println("path  " + userDir);
            }
            
            File scoDataDir = new File(userDir);
            
            File scoFiles[] = scoDataDir.listFiles();
            
            for ( int i = 0; i < scoFiles.length; i++ )
            {
                scoFiles[i].delete();
            }
            
            scoDataDir.delete();
        }
        catch(Exception e)
        {            
            System.out.println("Error deleting files during un-registration");
            e.printStackTrace();
        }
    }
    
    /**
     * This function deletes the files in the SampleRTE133Files\tempUploads 
     * directory.
     * 
     */
    public void deleteTempFiles( String uploadDir )
    {
        try
        {
            File tempUploadDir = new File(uploadDir);
            
            File tempFiles[] = tempUploadDir.listFiles();
            
            for ( int j = 0; j < tempFiles.length; j++ )
            {
                tempFiles[j].delete();
            }
        }
        catch(Exception e)
        {
            System.out.println("Error deleting files in the " + "tempUploads directory");              
            e.printStackTrace();
        }
    }
    
    /**
     * initPersistedData
     * 
     * This method is used to get initialized data model element values
     * from the database.  The initialized values are returned in a String 
     * array in the following order: cmi.scaled_passing_score, cmi.launch_data,
     * cmi.max_time_allowed, cmi.time_limit_action, 
     * cmi.completion_threshold
     *    
     * 
     * @param iCourseID  The id of the course
     * @param iItemID    The item ID
     * @param iUserID   The user ID
     */
    public void initPersistedData(SCODataManager ioSCOData, String iCourseID, 
                                  String iItemID, String iUserID) throws Exception
    {
    	/*
       DBConnectionManager connMgr = null;
    	
       try
       {  
          
          // Get some information from the database
       	  connMgr = new DBConnectionManager();

          PreparedStatement stmtSelectItem;
          PreparedStatement stmtSelectComments;
          PreparedStatement stmtSelectUser;

          String sqlSelectItem = "SELECT * FROM TYS_ITEM WHERE COURSE_CODE " + 
                                 "= ? AND ITEM_ID = ?";
          String sqlSelectComments = "SELECT * FROM SCOComments WHERE " +
                                     "ActivityID = ?";
          //String sqlSelectUser = "SELECT * FROM UserInfo WHERE UserID = ?";


          stmtSelectItem = connMgr.prepareStatement(sqlSelectItem);
          //stmtSelectComments = connMgr.prepareStatement(sqlSelectComments);
          //stmtSelectUser = connMgr.prepareStatement(sqlSelectUser);
          
          ResultSet rsItem = null;
          //ResultSet rsUser = null;

          synchronized(stmtSelectItem)
          {
             stmtSelectItem.setString(1, iCourseID);
             stmtSelectItem.setString(2, iItemID);
             rsItem = stmtSelectItem.executeQuery();
          }

          //synchronized(stmtSelectUser)
          //{
             //stmtSelectUser.setString(1, iUserID);
             //rsUser = stmtSelectUser.executeQuery();
          //}

          String masteryScore = new String();
          String dataFromLMS = new String();
          String maxTime = new String();
          String timeLimitAction = new String();
          String completionThreshold = new String();
          String audLev = new String();
          String audCap = new String();
          String delSpd = new String();
          String lang = new String();
          int activityID;

          // Get the learner preference values from the database
          //if(rsUser.next())
          //{
             //audLev = rsUser.getString("AudioLevel");
             //audCap = rsUser.getString("AudioCaptioning");
             //delSpd = rsUser.getString("DeliverySpeed");
             //lang = rsUser.getString("Language");
          //}
          //rsUser.close();
          

          while( rsItem.next() )
          {
             String type = rsItem.getString("Type");

             if ( type.equals("sco") || type.equals("asset") )
             {
                masteryScore = rsItem.getString("MinNormalizedMeasure");
                dataFromLMS = rsItem.getString("DataFromLMS");
                maxTime = rsItem.getString("AttemptAbsoluteDurationLimit");
                timeLimitAction = rsItem.getString("TimeLimitAction");
                completionThreshold = rsItem.getString("CompletionThreshold");
                activityID = rsItem.getInt("ActivityID");

                
                ResultSet rsComments = null;

                // Get the comments associated with an activity if any
                synchronized(stmtSelectComments)
                {
                   stmtSelectComments.setInt(1, activityID);
                   rsComments = stmtSelectComments.executeQuery();
                }

                // Loop through the comments and initialize the SCO data\
                int idx = 0;
                while(rsComments.next())
                {
                   int error = 0;
                   String cmt = rsComments.getString("Comment");
                   String elem = "cmi.comments_from_lms." + idx + ".comment";
                   
                   error = DMInterface.processSetValue(elem, cmt, 
                                                       true, ioSCOData);


                   String cmtDT = rsComments.getString("CommentDateTime");
                   elem = "cmi.comments_from_lms." + idx + ".timestamp";
                   
                   error = DMInterface.processSetValue(elem, cmtDT, 
                                                       true, ioSCOData); 

                   String cmtLoc = rsComments.getString("CommentLocation");
                   elem = "cmi.comments_from_lms." + idx + ".location";
                   
                   error = DMInterface.processSetValue(elem, cmtLoc, 
                                                       true, ioSCOData);
                   
                   idx++;
                }
                rsComments.close();
                
             }
          }

          stmtSelectItem.close();
          //stmtSelectComments.close();
          rsItem.close();          


          String element = new String();
          int err;

          // Initialize the cmi.credit value
          element = "cmi.credit";
          err = DMInterface.processSetValue(element, "credit", true, 
                                            ioSCOData);

          // Initialize the mode 
          element = "cmi.mode";
          err = DMInterface.processSetValue(element, "normal", true, 
                                            ioSCOData);

          // Initialize any launch data 
          if ( dataFromLMS != null && ! dataFromLMS.equals("") )
          {
             element = "cmi.launch_data";
             err = DMInterface.processSetValue(element, dataFromLMS, true, 
                                               ioSCOData);
          }
          
          // Initialize the scaled passing score 
          if ( masteryScore != null && ! masteryScore.equals("") )
          {
             element = "cmi.scaled_passing_score";
             err = DMInterface.processSetValue(element, masteryScore, true, 
                                               ioSCOData);
          }

          // Initialize the time limit action 
          if ( timeLimitAction != null && ! timeLimitAction.equals("") )
          {
             element = "cmi.time_limit_action";
             err = DMInterface.processSetValue(element, timeLimitAction, 
                                               true, ioSCOData);
          }

          // Initialize the completion_threshold
          if ( completionThreshold != null && ! completionThreshold.equals("") )
          {
             element = "cmi.completion_threshold";
             err = DMInterface.processSetValue(element, completionThreshold, 
                                               true, ioSCOData);
          }

          // Initialize the max time allowed 
          if ( maxTime != null && !  maxTime.equals("") )
          {
             element = "cmi.max_time_allowed";
             err = DMInterface.processSetValue(element,  maxTime, 
                                               true, ioSCOData);
          }

          // Initialize the learner preferences based on the SRTE 
          // learner profile information

          // audio_level
          element = "cmi.learner_preference.audio_level";
          err = DMInterface.processSetValue(element, audLev, true, ioSCOData);

          // audio_captioning
          element = "cmi.learner_preference.audio_captioning";
          err = DMInterface.processSetValue(element, audCap, true, ioSCOData);

          // delivery_speed
          element = "cmi.learner_preference.delivery_speed";
          err = DMInterface.processSetValue(element, delSpd, true, ioSCOData);
          
          // language
          element = "cmi.learner_preference.language";
          err = DMInterface.processSetValue(element, lang, true, ioSCOData);


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
       */
    }
    
    /**
     * initSCOData
     * 
     * This method is used to initialize certain data model elements.
     * This method is called by <code>initializeStateFiles<code> to initialize 
     * data model information based on Sample RTE specific information.
     * 
     * @param iSCOData  The SCODataManager whose values are initialized.
     */
    private void initSCOData(SCODataManager ioSCOData, String iUserID,
                             String iUserName, String iCourseID, String iItemID) throws Exception
							    {
        DBConnectionManager connMgr = null;
     	
        try
        {  
           
           // Get some information from the database
       	  connMgr = new DBConnectionManager();

          PreparedStatement stmtSelectItem;
          PreparedStatement stmtSelectComments;
          PreparedStatement stmtSelectUser;

          // TODO and obj_idx=1 추가함. - 수정 필요 
          String sqlSelectItem = "SELECT (select min_normal_measure from TYS_OBJECTIVES where seq_idx = (select seq_idx " +
          		"from tys_sequence where COURSE_CODE = ? AND ITEM_ID = ? and SATISFIED_MEASURE='Y') and obj_idx=1) ITEM_SCORE,  " +
          		"ITEM_DATA_FROM_LMS, ITEM_MAX_TIME_ALLOW, ITEM_TIME_LIMIT, ITEM_THRESHOLD, " +
          		"(select RES_SCORM_TYPE from TYS_RESOURCE where course_code = ? and item_id = ?) TYPE " +
          		"FROM TYS_ITEM WHERE COURSE_CODE = ? AND ITEM_ID = ?";
          
          //String sqlSelectComments = "SELECT * FROM SCOComments WHERE " +
          //                           "ActivityID = ?";
          //String sqlSelectUser = "SELECT * FROM UserInfo WHERE UserID = ?";
          
          stmtSelectItem = connMgr.prepareStatement(sqlSelectItem);
          //stmtSelectComments = connMgr.prepareStatement(sqlSelectComments);
          //stmtSelectUser = connMgr.prepareStatement(sqlSelectUser);

          if (_Debug)
          {
             System.out.println("about to call item in RTEFile");
             System.out.println("userID: " + iUserID);
             System.out.println("courseID: " + iCourseID);
             System.out.println("scoID: " + iItemID);
          }

          ResultSet rsItem = null;
          //ResultSet rsUser = null;

          synchronized(stmtSelectItem)
          {
             stmtSelectItem.setString(1, iCourseID);
             stmtSelectItem.setString(2, iItemID);
             stmtSelectItem.setString(3, iCourseID);
             stmtSelectItem.setString(4, iItemID);
             stmtSelectItem.setString(5, iCourseID);
             stmtSelectItem.setString(6, iItemID);
             rsItem = stmtSelectItem.executeQuery();
          }

          //synchronized(stmtSelectUser)
          //{
          //    stmtSelectUser.setString(1, iUserID);
          //    rsUser = stmtSelectUser.executeQuery();
          //}


          if (_Debug)
          {
             System.out.println("call to itemRS is complete");
          }

          String masteryScore = new String();
          String dataFromLMS = new String();
          String maxTime = new String();
          String timeLimitAction = new String();
          String completionThreshold = new String();
          String audLev = new String();
          String audCap = new String();
          String delSpd = new String();
          String lang = new String();
          int activityID;

          // Get the learner preference values from the database
          //if(rsUser.next())
          //{
          //   audLev = rsUser.getString("AudioLevel");
          //   audCap = rsUser.getString("AudioCaptioning");
          //   delSpd = rsUser.getString("DeliverySpeed");
          //   lang = rsUser.getString("Language");
          //}
          //rsUser.close();

          while( rsItem.next() )
          {
             String type = rsItem.getString("TYPE");

             if ( type.equals("sco") || type.equals("asset") )
             {
                masteryScore = rsItem.getString("ITEM_SCORE");
                dataFromLMS = rsItem.getString("ITEM_DATA_FROM_LMS");
                maxTime = rsItem.getString("ITEM_MAX_TIME_ALLOW");
                timeLimitAction = rsItem.getString("ITEM_TIME_LIMIT");
                completionThreshold = rsItem.getString("ITEM_THRESHOLD");
                //activityID = rsItem.getInt("ActivityID");

                ResultSet rsComments = null;

                // Get the comments associated with an activity if any
                //synchronized(stmtSelectComments)
                //{
                //   stmtSelectComments.setInt(1, activityID);
                //   rsComments = stmtSelectComments.executeQuery();
                //}

                // Loop through the comments and initialize the SCO data\
                /*
                int idx = 0;
                while(rsComments.next())
                {
                   int error = 0;
                   String cmt = rsComments.getString("Comment");
                   String elem = "cmi.comments_from_lms." + idx + ".comment";
                   
                   error = DMInterface.processSetValue(elem, cmt, 
                                                       true, ioSCOData); 
                   String cmtDT = rsComments.getString("CommentDateTime");
                   elem = "cmi.comments_from_lms." + idx + ".timestamp";
                   
                   error = DMInterface.processSetValue(elem, cmtDT, 
                                                       true, ioSCOData);

                   String cmtLoc = rsComments.getString("CommentLocation");
                   elem = "cmi.comments_from_lms." + idx + ".location";
                   
                   error = DMInterface.processSetValue(elem, cmtLoc, 
                                                       true, ioSCOData);                                    

                   idx++;
                }
                rsComments.close();
                */
             }
          }
          stmtSelectItem.close();
          rsItem.close();

          String element = new String();
          int err;

          // Initialize the learner id
          element = "cmi.learner_id";
          err = DMInterface.processSetValue(element, iUserID, true, ioSCOData);

          // Initialize the learner name
          element = "cmi.learner_name";
          err = DMInterface.processSetValue(element, iUserName, true, ioSCOData);
          
          //Initialize the Total Time
          element = "cmi.total_time";
          err = DMInterface.processSetValue(element, "PT0H0M0S", true, ioSCOData);

          // Initialize the cmi.credit value
          element = "cmi.credit";
          err = DMInterface.processSetValue(element, "credit", true, ioSCOData);

          // Initialize the mode 
          element = "cmi.mode";
          err = DMInterface.processSetValue(element, "normal", true, ioSCOData);

          // Initialize any launch data 
          if ( dataFromLMS != null && ! dataFromLMS.equals("") )
          {
             element = "cmi.launch_data";
             err = DMInterface.processSetValue(element, dataFromLMS, true, ioSCOData);
          }
          
          // Initialize the scaled passing score 
          if ( masteryScore != null && ! masteryScore.equals("") )
          {
             element = "cmi.scaled_passing_score";
             err = DMInterface.processSetValue(element, masteryScore, true, ioSCOData);
          }

          // Initialize the time limit action 
          if ( timeLimitAction != null && ! timeLimitAction.equals("") )
          {
             element = "cmi.time_limit_action";
             err = DMInterface.processSetValue(element, timeLimitAction, true, ioSCOData);
          }

          // Initialize the completion_threshold
          if ( completionThreshold != null && ! completionThreshold.equals("") )
          {
             element = "cmi.completion_threshold";
             err = DMInterface.processSetValue(element, completionThreshold, true, ioSCOData);
          }

          // Initialize the max time allowed 
          if ( maxTime != null && !  maxTime.equals("") )
          {
             element = "cmi.max_time_allowed";
             err = DMInterface.processSetValue(element,  maxTime, true, ioSCOData);
          }

          // Initialize the learner preferences based on the SRTE 
          // learner profile information

          // audio_level
          element = "cmi.learner_preference.audio_level";
          err = DMInterface.processSetValue(element, "1", true, ioSCOData);

          // audio_captioning
          element = "cmi.learner_preference.audio_captioning";
          err = DMInterface.processSetValue(element, "0", true, ioSCOData);

          // delivery_speed
          element = "cmi.learner_preference.delivery_speed";
          err = DMInterface.processSetValue(element, "1", true, ioSCOData);
          
          // language
          element = "cmi.learner_preference.language";
          err = DMInterface.processSetValue(element, lang, true, ioSCOData);

       }
       catch ( Exception e )
       {
           e.printStackTrace();
           
           ErrorManager.getErrorStackTrace(e);
           throw new Exception( e.getMessage());
       }
       finally 
       {
           if(connMgr != null) { try { connMgr.freeConnection(); }catch (Exception e10) {} }
       }  
    }    
}
