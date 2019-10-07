package com.ziaan.scorm2004.runtime.client;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.ziaan.library.ConfigSet;
import com.ziaan.library.DataBox;
import com.ziaan.library.Log;
import com.ziaan.scorm2004.ScormBlobBean;
import com.ziaan.scorm2004.sequencer.ADLLaunch;
import com.ziaan.scorm2004.sequencer.ADLSequencer;
import com.ziaan.scorm2004.sequencer.ADLTOC;
import com.ziaan.scorm2004.sequencer.ADLValidRequests;
import com.ziaan.scorm2004.sequencer.SeqActivity;
import com.ziaan.scorm2004.sequencer.SeqActivityTree;
import com.ziaan.scorm2004.sequencer.SeqNavRequests;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

public class SequencingEngine
{
    private static boolean _Debug = true;
    
    private static String ERROR_PAGE = "/learn/user/lcms/scorm2004/specialstate/undefinedError.htm";

    /**
    * Enumeration of possible results of of the sequencing process that do not
    * provide the 'next' activity to launch.
    * <br>ERROR
    * <br><b>"_ERROR_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
    public static String LAUNCH_ERROR = "/learn/user/lcms/scorm2004/specialstate/error.htm";

    /**
    *  Enumeration of possible results of of the sequencing process that do not
    * provide the 'next' activity to launch.
    * <br>Blocked
    * <br><b>"_Blocked_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
    public static String LAUNCH_BLOCKED = "/learn/user/lcms/scorm2004/specialstate/blocked.htm";
    //public static String LAUNCH_BLOCKED = "/learn/user/lcms/scorm2004/specialstate/returnPage.jsp";

    /**
    * Enumeration of possible results of of the sequencing process that do not
    * provide the 'next' activity to launch.
    * <br>Nothing
    * <br><b>"_NOTHING_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
    public static String LAUNCH_NOTHING = "/learn/user/lcms/scorm2004/specialstate/nothing.htm";
    //public static String LAUNCH_NOTHING = "/learn/user/lcms/scorm2004/specialstate/returnPage.jsp";

    /**
    * Enumeration of possible results of of the sequencing process that do not
    * provide the 'next' activity to launch.
    * <br>Course Complete
    * <br><b>"_COURSECOMPLETE_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
    public static String LAUNCH_COURSECOMPLETE = "/learn/user/lcms/scorm2004/specialstate/coursecomplete.htm";

    /**
    * Enumeration of possible results of of the sequencing process that do not
    * provide the 'next' activity to launch.
    * <br>No Available Activities
    * <br><b>"_INVALIDNAVEVENT_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
    public static String LAUNCH_INVALIDNAVEVENT = "/learn/user/lcms/scorm2004/specialstate/invalidevent.htm";
    //public static String LAUNCH_INVALIDNAVEVENT = "/learn/user/lcms/scorm2004/specialstate/returnPage.jsp";

     /**
    * Enumeration of possible results of of the sequencing process that do not
    * provide the 'next' activity to launch.
    * <br>No Available Activities
    * <br><b>"_DEADLOCK_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
    public static String LAUNCH_DEADLOCK = "/learn/user/lcms/scorm2004/specialstate/deadlock.htm";
    
     /**
    * Enumeration of possible results of of the sequencing process that do not
    * provide the 'next' activity to launch.
    * <br>No Available Activities
    * <br><b>"_ENDSESSION_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
    public static String LAUNCH_ENDSESSION = "/learn/user/lcms/scorm2004/specialstate/endsession.htm";

    private boolean isSetInfo = false;
    
    // lmsID : subj_year_subjseq 형태의 값을 갖는다.
    private String lmsID = "";
    private String courseID = "";
    private String orgID = "";
    private String courseTitle = "";
    
    // 복습창, 열린교육 여부 
    private String reviewType = "";
    
    private SequencingEngineBean sequencingEngineBean = new SequencingEngineBean();
    private boolean isProcessed = false;
    private String forwardPage = "";

    // The type of controls shown
    private boolean isNextAvailable = false;
    private boolean isPrevAvailable = false;
    private boolean isSuspendAvailable = false;
    private boolean displayQuit = true;
    private boolean isTOCAvailable = false;
    private boolean isCourseAvailable = true;
    
    public SequencingEngine()
    {
    }
    
    public void setInfo( String lmsID, String courseID, String orgID, String courseTitle, String reviewType )
    {
        this.lmsID = lmsID;
        this.courseID = courseID;
        this.orgID = orgID;
        this.courseTitle = courseTitle;
        this.reviewType = reviewType;
        
        isSetInfo = true;
    }
    
    public boolean process( String requestedSCO, String buttonType )
    {
        if ( !isSetInfo ) {
        
            Log.err.println( "초기화 오류 Cause: \r\n" );
            Log.err.println( "- lmsID, courseID, orgID, courseTitle, reviewType 등의 정보가 설정되지 않았습니다." );
            Log.err.println( "- Process() 메소드 호출전에 SequencingEngine 클래스의 setInfo() 메소드 호출 여부를 확인하세요." );
            
            return false;
        }
        
        // Get request and session.
        WebContext ctx = WebContextFactory.get();
        HttpSession session = ctx.getSession();
        
        //  Booleans for a completed course and request type
        boolean courseComplete = true;
        boolean wasAMenuRequest = false;
        boolean wasANextRequest = false;
        boolean wasAPrevRequest = false;
        boolean wasFirstSession = false;
        boolean wasANullRequest = false;
        boolean wasAnExitRequest = false;
        boolean wasAnExitAllRequest = false;
        boolean empty_block = false;
        boolean wasASuspendAllRequest = false;
        boolean wasSuspended = false;
        boolean endSession = false;

        String errDescr = new String();

        // Create sequencer, launch, UIState, Activity Tree and nav event objects
        ADLSequencer msequencer = new ADLSequencer();
        ADLLaunch mlaunch = new ADLLaunch();
        SeqNavRequests mnavRequest = new SeqNavRequests();
        ADLValidRequests mValidRequests = new ADLValidRequests();
        SeqActivityTree mactivityTree = null;
        SeqActivity mactivity = new SeqActivity();

        // Lists used during menu construction
        Vector TOCState = new Vector();
        Vector title = new Vector();
        Vector id = new Vector();
        Vector depth = new Vector();
        ADLTOC toc = new ADLTOC();
      
        String title_string = new String();
        String depth_string = new String();
        String id_string = new String();

        // The next item that will be launched
        String nextItemToLaunch = new String();

        // The type of button request if its a button request
//        String buttonType = new String();
        // Whether the launched unit is a sco or an asset
        String type = new String();
        // Is the item a block with no content
        String item_type = new String();
        // Is the identifier column 
        String identifier = new String();

        /*
        // The courseID and course title are passed as parameters on initial launch of a course
        String courseID = (String)request.getParameter( "p_course_code" );
        String courseTitle = (String)request.getParameter( "p_course_nm" );

        //  Get the requested sco if its a menu request
        String requestedSCO = (String)request.getParameter( "scoID" );
        //  Get the button that was pushed if its a button request
        buttonType = (String)request.getParameter( "button" );
        */
        
        if ( _Debug )
        {
            System.out.println( "lmsID          : " + lmsID );
            System.out.println( "courseID       : " + courseID );
            System.out.println( "courseTitle    : " + courseTitle );
            System.out.println( "requestedSCO   : " + requestedSCO );
            System.out.println( "buttonType     : " + buttonType );
        }
        
        // if first time for a course, set the course title session variable
        if ( (! (courseTitle == null)) && (! courseTitle.equals("") ) )  
        {
           session.setAttribute( "COURSETITLE", courseTitle );
        }
      
        String mIsExit = (String)session.getAttribute( "EXIT" );
        
        if ( ( mIsExit == null ) || mIsExit.equals("false") )
        {  
           mIsExit = "false";
        }
        session.setAttribute( "EXIT", "false" );
        
        // Set boolean for the type of navigation request
        if ( (! (requestedSCO == null)) && (! requestedSCO.equals("") ))
        {
           wasAMenuRequest = true;
        }
        else if ( (! (buttonType == null) ) && ( buttonType.equals("exitAll") ) )
        {
           wasAnExitAllRequest = true;
        }
        else if ( (! (buttonType == null) ) && ( buttonType.equals("suspendAll") ) )
        {
           wasASuspendAllRequest = true;
        }
        else if ( (! (buttonType == null) ) && ( buttonType.equals("prev") ) )
        {
           wasAPrevRequest = true;
        }
        else if ( mIsExit.equals("true") )
        {  
           wasAnExitRequest = true;
        }
        else if ( (! (buttonType == null) ) && ( buttonType.equals("next") ) ) 
        {
           wasANextRequest = true;
        }
        else if ( (! (buttonType == null) ) && ( buttonType.equals("nul") ) )
        {
           wasANullRequest = true;
        }
        else if ( (! (buttonType == null) ) && ( buttonType.equals("exit") ) )
        {
           wasAnExitRequest = true;
        }
        else
        {
           // First launch of the course in this session.
           wasFirstSession = true;
        }

        //  If the LMS has not been launched
        if ( lmsID != null )
        {
            //  set the LMS ID
            session.setAttribute( "LMSID", lmsID );
            
        }
        else // Not the initial launch of LMS, use session data
        {
            lmsID = (String)session.getAttribute( "LMSID" );
        }
        
        //  If the course has not been launched
        if ( courseID != null )
        {
           //  set the course ID
           session.setAttribute( "COURSEID", courseID );
        
        }
        else // Not the initial launch of course, use session data
        {
           courseID = (String)session.getAttribute( "COURSEID" );
        }

        //  If the orgID has not been launched
        if ( orgID != null )
        {
            //  set the course ID
            session.setAttribute( "ORGID", orgID );
            
        }
        else // Not the initial launch of orgID, use session data
        {
            orgID = (String)session.getAttribute( "ORGID" );
        }
        
        //  Get the user's id
        String userID = (String)session.getAttribute( "userid" );
        String exitFlag = (String)session.getAttribute( "EXITFLAG" );
                
        
        try
        { 
            String specialState = new String();

            if ( ( userID == null ) || ( courseID == null ) )
            {
                 specialState = LAUNCH_ENDSESSION;
                 endSession = true;
            }
            else
            {
                ScormBlobBean sbb = new ScormBlobBean();
                mactivityTree = (SeqActivityTree) sbb.selectTreeObject( lmsID, courseID, orgID, userID, reviewType );
                
                if ( _Debug )
                {
                    System.out.println( "## Tree Object : SeqEngine  읽기 성공" );
                }
                
                // Set the student id in the activity tree if it has not been set yet
                String studentID = new String();
                studentID = mactivityTree.getLearnerID();
                
                if (studentID == null)
                {
                   mactivityTree.setLearnerID(userID);
                }
                
                mactivity = mactivityTree.getSuspendAll();
                if ( mactivity != null ) 
                {
                   wasSuspended = true;
                   wasFirstSession = false;
                }

                // Set the Activity Tree
                msequencer.setActivityTree(mactivityTree);
             
                // Initialize variables that help with sequencing
                String scoID = new String();
                String lessonStatus = new String();
                boolean filePersisted = false;
                       
                // Open the connection for the sequencer
                // LMSDBHandler.getConnection();
                
                //  If the user selected a menu option, handle appropriately
                if ( wasAMenuRequest )
                {
                   mlaunch = msequencer.navigate( requestedSCO );
                }
                else // It was a next request, previous request, or first launch of session (or auto) or resume
                {
                   //  If its first session
                   if ( wasFirstSession  )
                   {
                      mlaunch = msequencer.navigate( mnavRequest.NAV_START );
                               
                   }  //  Ends if it was the first time in for the session
                   else if ( wasSuspended )// Its a resume request
                   {
                      mlaunch = msequencer.navigate( mnavRequest.NAV_RESUMEALL );
                      
                   }  //  Ends if its a resume request
                   else if ( wasANextRequest )// Its a next request
                   {  
                      mlaunch = msequencer.navigate( mnavRequest.NAV_CONTINUE );
                      
                   }  //  Ends if its a next request
                   else if ( wasAPrevRequest )// Its a previous request
                   {
                      // Handle the previous request
                      mlaunch = msequencer.navigate( mnavRequest.NAV_PREVIOUS );
                   }//end previous
                   else if ( wasAnExitRequest )// Its an exit request
                   {  
                      // Handle an exit request
                      mlaunch = msequencer.navigate( mnavRequest.NAV_EXIT );
                   }//end exit
                   else if ( wasAnExitAllRequest )// Its an exitAll request
                   { 
                      // Handle an exitAll request
                      mlaunch = msequencer.navigate( mnavRequest.NAV_EXITALL );
                   }//end exitAll
                   else if ( wasASuspendAllRequest )// Its a suspendAll request
                   {  
                      // Handle an suspendAll request
                      mlaunch = msequencer.navigate( mnavRequest.NAV_SUSPENDALL );
                   }//end suspendAll
                      
                }  // Ends if it was a button request
                
                // Close the static connection
                // LMSDBHandler.closeConnection();
                
                // Set the session variables returned by the sequencer
                // that are used by the RTE during launch and execution
                session.setAttribute( "SCOID", mlaunch.mStateID );
                
                Long longObj = new Long(mlaunch.mNumAttempt);
                session.setAttribute( "NUMATTEMPTS", longObj.toString() );
                session.setAttribute( "ACTIVITYID", mlaunch.mActivityID );

                // If its an END_SESSION, clear the active activity
                if ( (mlaunch.mSeqNonContent != null) && 
                    ((mlaunch.mSeqNonContent).equals("_ENDSESSION_") ||
                     (mlaunch.mSeqNonContent).equals("_COURSECOMPLETE_")) )
                {
                   msequencer.clearSeqState();
                   endSession = true;
                }
                
                // Save the activity tree
                filePersisted = persistActivityTree( msequencer.getActivityTree(), userID );
                              
                // Get the RTE's User Interface state
                mValidRequests = mlaunch.mNavState;
                if ( mValidRequests == null )
                {
                   isNextAvailable = false;
                   isPrevAvailable = false;
                   TOCState = null;  
                   isSuspendAvailable = false;
                }
                else
                {  
                   if (mValidRequests.mContinue && mValidRequests.mContinueExit)
                   {
                      //alert("continue and continueExit are both true");
                   }
                   if ( mValidRequests.mContinueExit )
                   {
                      session.setAttribute( "EXIT", "true" );
                   }
                   else 
                   {
                      session.setAttribute( "EXIT", "false" );
                   }
                   if (mValidRequests.mContinue || mValidRequests.mContinueExit)
                   {   
                      isNextAvailable = true;
                   }
                   isPrevAvailable = mValidRequests.mPrevious;
                   TOCState = mValidRequests.mTOC;
                   isSuspendAvailable = mValidRequests.mSuspend;
                }  
             
                // Look for a special state and redirect if appropriate
                if (mlaunch.mSeqNonContent != null)
                {
                   specialState = getSpecialState( mlaunch.mSeqNonContent );
                   isSuspendAvailable = false;
                }
                
                // Set up the RTE Database connection
                String uploadCode = sequencingEngineBean.selectUploadPath( courseID );
                String webPathKey = "SCORM2004.WEBPATH." + uploadCode;
                
                ConfigSet conf = new ConfigSet();
                String webPath = conf.getProperty( webPathKey );

                if ( _Debug )
                {
                    System.out.println( "ACT ID : " + mlaunch.mActivityID );
    
                    System.out.println("  ::--> Non Content:   " + mlaunch.mSeqNonContent);        
                    System.out.println("  ::--> End Session:   " + mlaunch.mEndSession);
                    System.out.println("  ::--> Activity ID:   " + mlaunch.mActivityID);
                    System.out.println("  ::--> Resource ID:   " + mlaunch.mResourceID);
                    System.out.println("  ::--> State ID:      " + mlaunch.mStateID);
                    System.out.println("  ::--> Attempt #:     " + mlaunch.mNumAttempt);
                    System.out.println("  ::--> Delivery Mode: " + mlaunch.mDeliveryMode);
                    System.out.println("  ::--> Max Time:      " + mlaunch.mMaxTime);
                }

                
                List launchInfo = sequencingEngineBean.selectLaunchLocation( courseID, mlaunch.mActivityID );
                
                String itemID;
                boolean matched = false;
                DataBox dbox = null;
                
                for ( int j=0; j<launchInfo.size() && (!matched); j++ )
                {
                    dbox = (DataBox) launchInfo.get(j);
                    itemID = dbox.getString("d_item_id");
                    
                    if ( (itemID.compareTo(mlaunch.mActivityID) == 0) )
                    {
                       matched = true;
                       nextItemToLaunch = webPath + dbox.getString("d_res_href") + dbox.getString("d_item_parameters");
                       
                       if ( _Debug )
                       {
                           System.out.println( "Launch URL : " + nextItemToLaunch );
                       }

                       if ( dbox.getString( "d_next" ).equals("Y") ) 
                       {
                          isNextAvailable = false;
                       }
                       if ( dbox.getString( "d_previous" ).equals("Y") ) 
                       {
                          isPrevAvailable = false;
                       }
                       if ( dbox.getString( "d_exit" ).equals("Y") ) 
                       {  
                          displayQuit = false;
                       }
                    }
                 }
                 
                 if ( !matched )
                 {
                     nextItemToLaunch = ERROR_PAGE;
                 }
                
                 // set up the table of contents information if choice = true
                 if (TOCState != null)
                 {    
                     isTOCAvailable = true;
                     session.setAttribute( "TOC", "true" );
                 }
                 else
                 {
                    session.setAttribute( "TOC", "false" );
                 }
            }
            
           //  If the course is complete redirect to the course complete page
           if ( specialState.equals( LAUNCH_ENDSESSION )  )
           {
               session.removeAttribute( "COURSETITLE" );
               session.removeAttribute( "COURSEID" );
               session.removeAttribute( "ORGID" );
               session.removeAttribute( "SCOID" );
               session.removeAttribute( "LMSID" );
               session.removeAttribute( "TOC" );
               session.removeAttribute( "TOCList" );
               session.removeAttribute( "progressDataMap" );               
           }
           
           if ( specialState.equals( LAUNCH_COURSECOMPLETE )  )
           {
               session.removeAttribute( "COURSETITLE" );
               session.removeAttribute( "COURSEID" );
               session.removeAttribute( "ORGID" );
               session.removeAttribute( "SCOID" );
               session.removeAttribute( "LMSID" );
               session.removeAttribute( "TOC" );
               session.removeAttribute( "TOCList" );
               session.removeAttribute( "progressDataMap" );

               forwardPage = specialState;
           }
           else
           {
               if ( ( mlaunch.mSeqNonContent != null) || ( endSession ) )
               {
                   forwardPage = specialState;
               }
               else
               {
                   forwardPage = nextItemToLaunch;
               }
           }
           
           ADLValidRequests validRequests = new ADLValidRequests();
           Vector TOCList = new Vector();
           Map progressDataMap = new HashMap();
           
           // Tree를 구성하기위한 TOC List를 가져온다.
           if ( isTOCAvailable && courseID != null &&  !courseID.equals("") )
           {
               msequencer.getValidRequests(validRequests);
               TOCList = validRequests.mTOC;
               session.setAttribute( "TOCList", TOCList );

               // 이미 학습한 SCO들을 가져온다. (Tree Icon으로 학습여부를 나타내기 위함)
               if ( reviewType.equals("review") || reviewType.equals("openedu") || reviewType.equals("preview") )
               {
                   // 진도 체크가 안되므로 가져올 필요 없음.
               }
               else
               {
                   progressDataMap = sequencingEngineBean.selectProgressDataMap( lmsID, courseID, orgID, userID );
               }
               session.setAttribute( "progressDataMap", progressDataMap );
           }
           else
           {
               session.removeAttribute( "TOCList" );
           }
           
           isProcessed = true;
        }
        catch ( Exception e )
        { 
            session.removeAttribute( "COURSETITLE" );
            session.removeAttribute( "COURSEID" );
            session.removeAttribute( "ORGID" );
            session.removeAttribute( "SCOID" );
            session.removeAttribute( "LMSID" );
            session.removeAttribute( "TOC" );
            session.removeAttribute( "TOCList" );
            session.removeAttribute( "progressDataMap" );

//            session.removeAttribute( "EXIT" );
//            session.removeAttribute( "SCOID" );
//            session.removeAttribute( "NUMATTEMPTS" );
//            session.removeAttribute( "ACTIVITYID" );
            e.printStackTrace();

            Log.err.println( "초기화 오류 Cause: \r\n" );
            Log.err.println( e.getMessage() );
        }         
        
        return isProcessed;
    }
    
    private boolean persistActivityTree(SeqActivityTree iTree, String userID )
    {
        boolean result = true;
        
        // Clear the tree session state
        iTree.clearSessionState();
        
        try
        {
            ScormBlobBean sbb = new ScormBlobBean();

            if ( reviewType.equals("betatest") || reviewType.equals("review") || reviewType.equals("openedu") || reviewType.equals("preview") )
            {
                sbb.insertTreeObjectReview( lmsID, courseID, orgID, userID, iTree );
            }
            else
            {
                sbb.insertTreeObject( lmsID, courseID, orgID, userID, iTree, true );
            }
            
            if ( _Debug )
            {
                System.out.println( "## Tree Object : SeqEngine  쓰기 성공" );
            }
        }
        catch ( Exception e )
        {
            result = false;
        }
        
        return result;
    }

    public boolean isNextAvailable()
    {
        return isNextAvailable;
    }

    public boolean isPrevAvailable()
    {
        return isPrevAvailable;
    }

    public boolean isSuspendAvailable()
    {
        return isSuspendAvailable;
    }
    
    public boolean isTOCAvailable()
    {
        return isTOCAvailable;
    }
    
    public boolean isCourseAvailable()
    {
        return isCourseAvailable;
    }
    
    public boolean isDisplayQuit()
    {
        return displayQuit;
    }
    
    public String getForwardPage() 
    {
        String page = "";
        
        if ( isProcessed )
        {
            page =  forwardPage;
        }
        else
        {
            page = ERROR_PAGE;
        }
        
//        return WebContextFactory.get().forwardToString( page );
        return page;
    }
    
    /*
    public Vector getTOCList()
    {
        return TOCList;
    }    
    */
    
    public String getTreePage()
    {
        //return WebContextFactory.get().forwardToString("/forward.jsp");
        return "/learn/user/lcms/scorm2004/activityTree.jsp";
    }
    
    /****************************************************************************
     **
     ** Function:  getSpecialState()
     ** Input:   
     ** Output:  
     **
     ** Description:  This method looks at the mActivityID member of the current
     **               ADLLaunch object.  If it is in a special sequencing state
     **               then the appropriate String is returned.  If its not in
     **               a special state, null is returned.
     **
     ***************************************************************************/
    private String getSpecialState( String iActivity )
    {
        String theState = new String();
        
        if ( iActivity != null )
        {
            if ( iActivity.equals("_ERROR_") )
            {
                theState = LAUNCH_ERROR;
            }
            else if ( iActivity.equals("_SEQBLOCKED_") )
            {
                theState = LAUNCH_BLOCKED;
            }
            else if ( iActivity.equals("_NOTHING_") )
            {
                theState = LAUNCH_NOTHING;
            }
            else if ( iActivity.equals("_COURSECOMPLETE_") )
            {
                theState = LAUNCH_COURSECOMPLETE;
            }
            else if ( iActivity.equals("_INVALIDNAVREQ_") )
            {
                theState = LAUNCH_INVALIDNAVEVENT;
            }
            else if ( iActivity.equals("_DEADLOCK_") )
            {
                theState = LAUNCH_DEADLOCK;
            }
            else if ( iActivity.equals("_ENDSESSION_") )
            {
                theState = LAUNCH_ENDSESSION;
            }
            else
            {
                theState = ERROR_PAGE;
            }
        }
        else
        {
            theState = ERROR_PAGE;
        }
        return theState;
    }    
}
