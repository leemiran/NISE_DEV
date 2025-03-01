/*******************************************************************************
**
** Concurrent Technologies Corporation (CTC) grants you ("Licensee") a non-
** exclusive, royalty free, license to use, modify and redistribute this
** software in source and binary code form, provided that i) this copyright
** notice and license appear on all copies of the software; and ii) Licensee
** does not utilize the software in a manner which is disparaging to CTC.
**
** This software is provided "AS IS," without a warranty of any kind.  ALL
** EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
** IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-
** INFRINGEMENT, ARE HEREBY EXCLUDED.  CTC AND ITS LICENSORS SHALL NOT BE LIABLE
** FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
** DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES.  IN NO EVENT WILL CTC  OR ITS
** LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
** INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
** CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
** OR INABILITY TO USE SOFTWARE, EVEN IF CTC HAS BEEN ADVISED OF THE POSSIBILITY
** OF SUCH DAMAGES.
**
*******************************************************************************/

package com.ziaan.scorm2004.sequencer;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.ziaan.scorm2004.util.debug.DebugIndicator;

/**
 * Implementation of one node of an activity tree.<br><br>
 * 
 * <strong>Filename:</strong> SeqActivity.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * This implementation includes both the activity's status tracking
 * information and all sequencing information described in the IMS SS
 * Specification.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE 1.3. <br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * This implementation has not been optimized.<br><br>
 * 
 * This implementation is not intended to scale well or provide
 * the 'best' solution.  The goal of this implementation is to
 * provide a proof-of-concept implementation of the IMS SS
 * Specification.<br><br>
 * 
 * Future versions of this sequencing implementation may utilize
 * other data structures/sources and/or make performance and
 * scalablity optimizations.<br><br>
 * 
 * <strong>Known Problems:</strong><br><br>
 * 
 * <strong>Side Effects:</strong><br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS 1.0
 *     <li>SCORM 1.3
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class SeqActivity extends SeqActivityTrackingAccess implements 
SeqActivityStateAccess, Serializable
{

   /**
    * Enumeration of the possible times for application of the Selection and
    * Randomization Processes.
    * <br>never
    * <br><b>"_NEVER_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String TIMING_NEVER             = "never";

   /**
    * Enumeration of the possible times for application of the Selection and
    * Randomization Processes.
    * <br>once
    * <br><b>"_ONCE_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String TIMING_ONCE              = "once";

   /**
    * Enumeration of the possible times for application of the Selection and
    * Randomization Processes.
    * <br>on each new attempt
    * <br><b>"_EACHNEW_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String TIMING_EACHNEW           = "onEachNewAttempt";

   /**
    * Enumeration of the possible termination requests -- described in section
    * TB of the IMS SS Specification.
    * <br>Exit All
    * <br><b>"_EXITALL_"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   private static String TER_EXITALL           = "_EXITALL_";
   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * This contains the XML fragment describing this activity's sequencing 
    * definition information.
    */
   private String mXML = null;

   /**
    * This describes the count (order) of the activity in its activity tree
    */
   private int mDepth = 0;

   /**
   * This describes the depth of the activity in its activity tree
   */
   private int mCount = -1;

   /**
    * This describes the ID associated with the root aggregation's user.
    */
   private String mLearnerID = "_NULL_";

   /**
    * This describes the ID associated with the activity's objective's scope
    */
   private String mScopeID = null;

   /**
    * This describes the activity's unique ID
    */
   private String mActivityID = null;

   /**
    * This describes the ID of the activity's associated resources
    */
   private String mResourceID = null;

   /**
    * This describes the ID of the activity's associated persistent state
    */
   private String mStateID = null;

   /**
    * This describes a human-readable description of the activity
    */
   private String mTitle = null;

   /**
    * This describes if this activity is visible in a TOC
    */
   private boolean mIsVisible = true;

   /**
    * This describes the relative order of this activity in context of all of
    * its siblings.
    */
   private int mOrder = -1;

   /**
    * This describes the relative order of this activity in context of its
    * selected siblings.
    */
   private int mActiveOrder = -1;

   /**
    * This describes if the activity is in the 'selected' set
    */
   private boolean mSelected = true;

   /**
    * This describes the parent activity of this activity
    */
   private SeqActivity mParent = null;


   // Activity State information
   /**
    * This describes if this activity is considered to be 'active'
    */
   private boolean mIsActive = false;

   /**
    * This describes if this activity is suspended
    */
   private boolean mIsSuspended = false;

   /**
    * This describes the set of children of this activity
    */
   private Vector mChildren = null;

   /**
    * This describes the set of 'active' children of this activity -- these
    * are children that will be considered during sequencing.
    */
   private Vector mActiveChildren = null;

   /**
    * This describes the delivery mode of the activity
    */
   private String mDeliveryMode = "normal";


   // Sequencing Definintions described in IMS SS SD 
   /**
    * This describes the sequencing definition model element 1.1
    */
   private boolean mControl_choice = true;

   /**
    * This describes the sequencing definition model element 1.2
    */
   private boolean mControl_choiceExit = true;

   /**
    * This describes the sequencing definition model element 1.3
    */
   private boolean mControl_flow = false;

   /**
    * This describes the sequencing definition model element 1.4
    */
   private boolean mControl_forwardOnly = false;


   /**
    * This describes the Constrained Choice SCORM sequencing extension
    */
   private boolean mConstrainChoice= false;

   /**
    * This describes the Prevent Activation SCORM sequencing extension
    */
   private boolean mPreventActivation = false;


   /**
    * This describes the sequencing definition model element 1.5
    */
   private boolean mUseCurObj = true;

   /**
    * This describes the sequencing definition model element 1.6
    */
   private boolean mUseCurPro = true;


   /**
    * This describes the sequencing definition model element 2
    */
   SeqRuleset mPreConditionRules = null;

   /**
    * This describes the sequencing definition model element 2
    */
   SeqRuleset mPostConditionRules = null;

   /**
    * This describes the sequencing definition model element 2
    */
   SeqRuleset mExitActionRules = null;


   /**
    * This describes the sequencing definition model element 3.1
    */
   private boolean mMaxAttemptControl = false;

   /**
    * This describes the sequencing definition model element 3.2
    */
   private long mMaxAttempt = 0;

   /**
    * This describes the sequencing definition model element 3.3
    */
   private boolean mAttemptAbDurControl = false;

   /**
    * This describes the sequencing definition model element 3.4
    */
   private ADLDuration mAttemptAbDur = null;

   /**
    * This describes the sequencing definition model element 3.5
    */
   private boolean mAttemptExDurControl = false;

   /**
    * This describes the sequencing definition model element 3.6
    */
   private ADLDuration mAttemptExDur = null;

   /**
    * This describes the sequencing definition model element 3.7
    */
   private boolean mActivityAbDurControl = false;

   /**
    * This describes the sequencing definition model element 3.8
    */
   private ADLDuration mActivityAbDur = null;

   /**
    * This describes the sequencing definition model element 3.9
    */
   private boolean mActivityExDurControl = false;

   /**
    * This describes the sequencing definition model element 3.10
    */
   private ADLDuration mActivityExDur = null;

   /**
    * This describes the sequencing definition model element 3.11
    */
   private boolean mBeginTimeControl = false;

   /**
    * This describes the sequencing definition model element 3.12
    */
   private String mBeginTime = null;

   /**
    * This describes the sequencing definition model element 3.13
    */
   private boolean mEndTimeControl = false;

   /**
    * This describes the sequencing definition model element 3.14
    */
   private String mEndTime = null;

   /**
    * This describes the sequencing definition model elements 4
    */
   private Vector mAuxResources = null;

   /**
    * This describes the sequencing definition model elements 5
    */
   private SeqRollupRuleset mRollupRules = null;

   /** 
    * This indicates if satisfaction of the activity can be evaluated from
    * measure when the activity is active.
    */
   private boolean mActiveMeasure = true;

   /** 
    * This describes when the activity contributes to its parent's rollup
    * during a Satisfied rule evaluation
    */
   private String mRequiredForSatisfied = 
   SeqRollupRule.ROLLUP_CONSIDER_ALWAYS;

   /** 
    * This describes when the activity contributes to its parent's rollup
    * during a Not Satisfied rule evaluation
    */
   private String mRequiredForNotSatisfied =
   SeqRollupRule.ROLLUP_CONSIDER_ALWAYS;

   /** 
    * This describes when the activity contributes to its parent's rollup
    * during a Completed rule evaluation
    */
   private String mRequiredForCompleted = 
   SeqRollupRule.ROLLUP_CONSIDER_ALWAYS;

   /** 
    * This describes when the activity contributes to its parent's rollup
    * during a Not Satisfied rule evaluation
    */
   private String mRequiredForIncomplete = 
   SeqRollupRule.ROLLUP_CONSIDER_ALWAYS;

   /**
    * This describes the sequencing definition model elements 6 and 7
    */
   private Vector mObjectives = null;

   /**
    * This describes the set of objective mappings defined for the activity's
    * objectives.
    */
   private Hashtable mObjMaps = null;

   /**
    * This describes the sequencing definition model element 8.1
    */
   private boolean mIsObjectiveRolledUp = true;

   /**
    * This describes the sequencing definition model element 8.2
    */
   private double mObjMeasureWeight = 1.0;

   /**
    * This describes the sequencing definition model element 8.3
    */
   private boolean mIsProgressRolledUp = true;

   /**
    * This describes the sequencing definition model element 9.1
    */
   private String mSelectTiming = "never";

   /**
    * This describes the sequencing definition model element 9.2
    */
   private boolean mSelectStatus = false;

   /**
    * This describes the sequencing definition model element 9.3
    */
   private int mSelectCount = 0;

   /**
    * This describes if the selection process has already been applied to
    * the activity.
    */
   private boolean mSelection = false;

   /**
    * This describes the sequencing definition model element 10.1
    */
   private String mRandomTiming = "never";

   /**
    * This describes the sequencing definition model element 10.2
    */
   private boolean mReorder = false;

   /**
    * This describes if the randomized process has already been applied to
    * the activity.
    */
   private boolean mRandomized = false;

   /**
    * This describes the sequencing definition model element 11.1
    */
   private boolean mIsTracked = true;

   /**
    * This describes the sequencing definition model element 11.2
    */
   private boolean mContentSetsCompletion = false;

   /**
    * This describes the sequencing definition model element 11.3
    */
   private boolean mContentSetsObj = false;

   /**
    * This describes the 'current' tracking information
    */
   private ADLTracking mCurTracking = null;

   /**
    * This records the tracking history
    */
   private Vector mTracking = null;

   /**
    * This describes the tracking status model element 1.2.1 Element 4
    */
   private long mNumAttempt = 0;

   /** 
    * This describes the activity's absolute duration.<br>
    * Tracking element 1.2.1 Element 2
    */
   private ADLDuration mActivityAbDur_track = null;

   /** 
    * This describes the activity's experienced duration.<br>
    * Tracking element 1.2.1 Element 3
    */
   private ADLDuration mActivityExDur_track = null;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Constructors 
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
   public SeqActivity()
   {
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
   Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Implementation of SeqActivityStateAccess Interface
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   // The following accessors provide information included in the Sequencing
   // Definition Model
   // ----------------------------------------------------------------------

   /**
    * Retrieves the value of the ControlMode.Choice Sequencing Definition
    * Model Element (<b>element 1.1</b>) for this activity.
    * 
    * @return <code>true</code> if the 'Choice' is enabled for this cluster,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getControlModeChoice()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getChoice");
         System.out.println("  ::-->  " + mControl_choice);
         System.out.println("  :: SeqActivity     --> END   - getChoice");
      }
      return mControl_choice;
   }

   /**
    * Sets the value of the ControlMode.Choice Sequencing Definition Model
    * Element (<b>element 1.1</b>) for this activity.
    * 
    * @param iChoice <code>true</code> to enable 'Choice' for this cluster or
    *                <code>false</code> to disable.
    */
   public void setControlModeChoice(boolean iChoice)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setChoice");
         System.out.println("  ::-->  " + iChoice);
      }

      mControl_choice = iChoice;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setChoice");
      }
   }

   /**
    * Retrieves the value of the ControlMode.ChoiceExit Sequencing Definition
    * Model Element (<b>element 1.2</b>) for this activity.
    * 
    * @return <code>true</code> if the 'ChoiceExit' is enabled for this cluster,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getControlModeChoiceExit()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getChoiceExit");
         System.out.println("  ::-->  " + mControl_choiceExit);
         System.out.println("  :: SeqActivity     --> END   - getChoiceExit");
      }

      return mControl_choiceExit;
   }

   /**
    * Sets the value of the ControlMode.ChoiceExit Sequencing Definition Model
    * Element (<b>element 1.2</b>) for this activity.
    * 
    * @param iChoiceExit <code>true</code> to enable 'ChoiceExit' for this
    *                    or <code>false</code> to disable.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setControlModeChoiceExit(boolean iChoiceExit)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setChoiceExit");
         System.out.println("  ::-->  " + iChoiceExit);
      }

      mControl_choiceExit = iChoiceExit;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setChoiceExit");
      }
   }

   /**
    * Retrieves the value of the ControlMode.Flow Sequencing Definition Model
    * Element (<b>element 1.3</b>) for this activity.
    * 
    * @return <code>true</code> if the 'Flow' is enabled for this cluster,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getControlModeFlow()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getFlow");
         System.out.println("  ::-->  " + mControl_flow);
         System.out.println("  :: SeqActivity     --> END   - getFlow");
      }

      return mControl_flow;
   }

   /**
    * Sets the value of the ControlMode.Flow Sequencing Definition Model
    * Element (<b>element 1.3</b>) for this activity.
    * 
    * @param iFlow  <code>true</code> to enable 'Flow' for this cluster or
    *               <code>false</code> to disable.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setControlModeFlow(boolean iFlow)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setFlow");
         System.out.println("  ::-->  " + iFlow);
      }

      mControl_flow = iFlow;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setFlow");
      }
   }

   /**
    * Retrieves the value of the ControlMode.ForwardOnly Sequencing Definition
    * Model Element (<b>element 1.4</b>) for this activity.
    * 
    * @return <code>true</code> if the 'ForwardOnly' is defined for this
    *         cluster,  otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getControlForwardOnly()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getForwardOnly");
         System.out.println("  ::-->  " + mControl_forwardOnly);
         System.out.println("  :: SeqActivity     --> END   - getForwardOnly");
      }

      return mControl_forwardOnly;
   }

   /**
    * Sets the value of the ControlMode.ForwardOnly Sequencing Definition Model 
    * Element (<b>element 1.4</b>) for this activity.
    * 
    * @param iForwardOnly <code>true</code> to enforce 'ForwardOnly' for this
    *                     cluster or <code>false</code> to disable.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setControlForwardOnly(boolean iForwardOnly)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setForwardOnly");
         System.out.println("  ::-->  " + iForwardOnly);
      }

      mControl_forwardOnly = iForwardOnly;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setForwardOnly");
      }
   }


   /**
     * Retrieves the value of the Choice Constraint Constrain Choice Sequencing
     * Definition Model Element for this activity.
     * 
     * @return <code>true</code> if the 'constrainChoice' is defined for this
     *         activity,  otherwise <code>false</code>.
     * 
     * @see <code>SeqActivityStateAccess</code>
     */
   public boolean getConstrainChoice()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "getConstrainChoice");
         System.out.println("  ::-->  " + mConstrainChoice);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getConstrainChoice");
      }

      return mConstrainChoice;
   }

   /**
    * Sets the value of the Choice Constraint Constrain Choice Sequencing
    * Definition Model Element for this activity.
    * 
    * @param iForwardOnly <code>true</code> to enforce 'constrainChoice' for 
    *                     this activity or <code>false</code> to prevent.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setConstrainChoice(boolean iConstrainChoice)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setConstrainChoice");
         System.out.println("  ::-->  " + iConstrainChoice);
      }

      mConstrainChoice = iConstrainChoice;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   -  " +
                            "setConstrainChoice");
      }
   }

   /**
    * Retrieves the value of the Choice Constraint Prevent Activation Sequencing
    * Definition Model Element for this activity.
    * 
    * @return <code>true</code> if the 'preventActivation' is defined for this
    *         activity,  otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getPreventActivation()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "getPreventActivation");
         System.out.println("  ::-->  " + mPreventActivation);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getPreventActivation");
      }

      return mPreventActivation;
   }

   /**
    * Sets the value of the Choice Constraint Prevent Activation Sequencing
    * Definition Model Element for this activity.
    * 
    * @param iForwardOnly <code>true</code> to enforce 'preventActivation' for 
    *                     this activity or <code>false</code> to allow.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setPreventActivation(boolean iPreventActivation)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setPreventActivation");
         System.out.println("  ::-->  " + iPreventActivation);
      }

      mPreventActivation = iPreventActivation;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setPreventActivation");
      }
   }

   /**
    * Retrieves the value of the ControlMode.useCurrentAttemptObjectiveInfo
    * Sequencing Definition Model Element (<b>element 1.5</b>)
    * for this activity.
    * 
    * @return <code>true</code> if the 'useCurrentAttemptObjectiveInfo'
    *         is defined for this cluster, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getUseCurObjective()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "getUseCurObjective");
         System.out.println("  ::-->  " + mUseCurObj);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getUseCurObjective");
      }

      return mUseCurObj;
   }

   /**
    * Sets the value of the ControlMode.useCurrentAttemptObjectiveInfo
    * Sequencing Definition Model Element (<b>element 1.5</b>) for this
    * activity.
    * 
    * @param iUseCur <code>true</code> to enforce that objective
    *                information for children will only be used during
    *                sequencing if that information was obtained withn the
    *                context of the current attempt on the activity, or
    *                <code>false</code> to disable.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setUseCurObjective(boolean iUseCur)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setUseCurObjective");
         System.out.println("  ::-->  " + iUseCur);
      }

      mUseCurObj = iUseCur;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END - " +
                            "setUseCurObjective");
      }
   }

   /**
    * Retrieves the value of the ControlMode.useCurrentAttemptProgressInfo
    * Sequencing Definition Model Element (<b>element 1.6</b>)
    * for this activity.
    * 
    * @return <code>true</code> if the 'useCurrentAttemptProgressInfo'
    *         is defined for this cluster, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getUseCurProgress()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "getUseCurProgress");
         System.out.println("  ::-->  " + mUseCurPro);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getUseCurProgress");
      }

      return mUseCurPro;
   }

   /**
    * Sets the value of the ControlMode.useCurrentAttemptProgressInfo
    * Sequencing Definition Model Element (<b>element 1.6</b>) for this
    * activity.
    * 
    * @param iUseCur <code>true</code> to enforce that progress
    *                information for children will only be used during
    *                sequencing if that information was obtained withn the
    *                context of the current attempt on the activity, or
    *                <code>false</code> to disable.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setUseCurProgress(boolean iUseCur)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setUseCurProgress");
         System.out.println("  ::-->  " + iUseCur);
      }

      mUseCurPro = iUseCur;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END - " +
                            "setUseCurProgress");
      }
   }

   /**
    * Retrieves the Precondition Action Rules, Sequencing Definition Model
    * Element (<b>element 2</b>, precondition action subset) defined for
    * this activity.
    * 
    * @return The Precondition Action Sequencing Rules (<code>SeqRuleset</code>)
    *         defined for this activity, or <code>null</code> if no precondition
    *         rules have been defined.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public SeqRuleset getPreSeqRules()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getPreSeqRules");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getPreSeqRules");
      }

      return mPreConditionRules;
   }

   /**
    * Sets the Precondition Action Rules, Sequencing Definition Model Element
    * (<b>element 2</b>, precondition action subset) defined for this
    *  activity.
    * 
    * @param iRuleSet The set of Precondition Action Sequencing Rules
    *                 (<code>SeqRuleset</code>) defined for this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setPreSeqRules(SeqRuleset iRuleSet)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setPreSeqRules");

         if ( iRuleSet != null )
         {
            System.out.println("  ::-->  " + iRuleSet.size());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }
      }

      mPreConditionRules = iRuleSet;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setPreSeqRules");
      }
   }

   /**
    * Retrieves the Exit Action Rules, Sequencing Definition Model Element
    * (<b>element 2</b>, exit action subset) defined for this activity.
    * 
    * @return The Exit Action Sequencing Rules (<code>SeqRuleset</code>)
    *         defined for this activity, or <code>null</code> if no exit action
    *         rules have been defined.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public SeqRuleset getExitSeqRules()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getExitSeqRules");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getExitSeqRules");
      }

      return mExitActionRules;
   }

   /**
    * Sets the Exit Action Rules, Sequencing Definition Model Element
    * (<b>element 2</b>, exit action subset) defined for this activity.
    * 
    * @param iRuleSet The set of Exit Action Sequencing Rules
    *                 (<code>SeqRuleset</code>) defined for this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setExitSeqRules(SeqRuleset iRuleSet)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setExitSeqRules");

         if ( iRuleSet != null )
         {
            System.out.println("  ::-->  " + iRuleSet.size());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }
      }

      mExitActionRules = iRuleSet;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setExitSeqRules");
      }
   }

   /**
    * Retrieves the Post Condition Action Rules, Sequencing Definition Model
    * Element (<b>element 2</b>, post condition action subset) defined for
    * this activity.
    * 
    * @return The Post Condition Action Sequencing Rules 
    *         (<code>SeqRuleset</code>) defined for this activity, or
    *         <code>null</code> if no post condition rules have been defined.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public SeqRuleset getPostSeqRules()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getPostSeqRules");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getPostSeqRules");
      }

      return mPostConditionRules;
   }

   /**
    * Sets the Post Condition Action Rules, Sequencing Definition Model Element
    * (<b>element 2</b>, post condition action subset) defined for this
    * activity.
    * 
    * @param iRuleSet The set of Post Condition Action Sequencing Rules
    *                 (<code>SeqRuleset</code>) defined for this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setPostSeqRules(SeqRuleset iRuleSet)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setPostSeqRules");

         if ( iRuleSet != null )
         {
            System.out.println("  ::-->  " + iRuleSet.size());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }
      }

      mPostConditionRules = iRuleSet;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setPostSeqRules");
      }
   }

   /**
    * Retrieves the value of the limitCondition.attemptLimitControl Sequencing
    * Definition Model Element (<b>element 3.1</b> for this activity.
    * 
    * @return <code>true</code> if limitCondition.attemptLimit is defined for
    *         this activity, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getAttemptLimitControl()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getAttemptLimitControl");
         System.out.println("  ::-->  " + mMaxAttemptControl);
         System.out.println("  :: SeqActivity    --> END   - " + 
                            "getAttemptLimitControl");
      }

      return mMaxAttemptControl;
   } 

   /**
    * Retrieves the value of the limitCondition.attemptLimit Sequencing
    * Definition Model Element (<b>element 3.2</b> for this activity.
    * 
    * @return The maximum attempts (<code>long</code>) that has been defined
    *         for this activity, or <code>-1</code> if none have been
    *         defined.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public long getAttemptLimit()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getAttemptLimit");
         System.out.println("  ::-->  " + mMaxAttempt);
         System.out.println("  :: SeqActivity    --> END   - getAttemptLimit");
      }

      return mMaxAttempt;
   } 

   /**
    * Sets the value of the LimitCondtitions.attemptLimit Sequencing
    * Definition Model Element (<b>element 3.2</b>) for this activity.
    * 
    * @param iMaxAttempt The maximum number attempts allowed for this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setAttemptLimit(Long iMaxAttempt)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - setAttemptLimit");

         if ( iMaxAttempt != null )
         {
            System.out.println("  ::-->  " + iMaxAttempt.toString());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }

      }

      if ( iMaxAttempt != null )
      {
         long value = iMaxAttempt.longValue();

         if ( value > 0 )
         {
            mMaxAttemptControl = true;
            mMaxAttempt = value;
         }
         else
         {
            mMaxAttemptControl = false;
            mMaxAttempt = -1;
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END  - setAttemptLimit");
      }
   }

   /**
    * Retrieves the value of the attemptAbsoluteDurationLimitControl
    * Sequencing Definition Model Element (<b>element 3.3</b>) for this 
    * activity.
    * 
    * @return <code>true</code> if limitCondition.attemptAbsoluteDurationLimit
    *         is defined for this activity, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getAttemptAbDurControl()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getAttemptAbDurControl");
         System.out.println("  ::-->  " + mAttemptAbDurControl);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getAttemptAbDurControl");
      }

      return mAttemptAbDurControl;
   } 

   /**
    * Retrieves the value of the limitCondition.attemptAbsoluteDurationLimit
    * Sequencing Definition Model Element (<b>element 3.4</b>)
    * for this activity.
    * 
    * @return The absolute duration limit for an attempt on the activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getAttemptAbDur()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getAttemptAbDur");
      }

      String dur = null;

      if ( mAttemptAbDur != null )
      {
         dur = mAttemptAbDur.format(ADLDuration.FORMAT_SCHEMA);
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + dur);
         System.out.println("  :: SeqActivity    --> END   - getAttemptAbDur");
      }

      return dur;
   } 

   /**
    * Sets the value of the LimitCondition.attemptAbsoluteDurationLimit
    * Sequencing Definition Model Element (<b>element 3.4</b>) for this
    * activity.
    * 
    * @param iDur   The absolute duration (<code>String</code>) for an attempt
    *               on the activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setAttemptAbDur(String iDur)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setAttemptAbDur");
         System.out.println("  ::-->  " + iDur);
      }

      if ( iDur != null )
      {
         mAttemptAbDurControl = true;
         mAttemptAbDur = new ADLDuration(ADLDuration.FORMAT_SCHEMA, iDur);
      }
      else
      {
         mAttemptAbDurControl = false;
         mAttemptAbDur = null;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setAttemptAbDur");
      }
   }

   /**
    * Retrieves the value of the attemptExperiencedDurationLimit
    * Sequencing Definition Model Element (<b>element 3.5</b>) for this
    * activity.
    * 
    * @return <code>true</code> if 
    *         limitCondition.attemptExperiencedDurationLimit is defined for
    *         this activity, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getAttemptExDurControl()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getAttemptExDurControl");
         System.out.println("  ::-->  " + mAttemptExDurControl);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getAttemptExDurControl");
      }

      return mAttemptExDurControl;
   } 

   /**
    * Retrieves the value of the limitCondition.attemptExperiencedDurationLimit
    * Sequencing Definition Model Element (<b>element 3.6</b>)
    * for this activity.
    * 
    * @return The experienced duration limit for an attempt on the activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getAttemptExDur()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getAttemptExDur");
      }

      String dur = null;

      if ( mAttemptExDur != null )
      {
         dur = mAttemptExDur.format(ADLDuration.FORMAT_SCHEMA);
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + dur);
         System.out.println("  :: SeqActivity    --> END   - getAttemptExDur");
      }

      return dur;
   } 

   /**
    * Sets the value of the LimitCondition.attemptExperiencedDurationLimit
    * Sequencing Definition Model Element (<b>element 3.6</b>) for this
    * activity.
    * 
    * @param iDur   The experienced duration (<code>String</code>) for an 
    *               attempt on the activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setAttemptExDur(String iDur)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setAttemptExDur");
         System.out.println("  ::-->  " + iDur);
      }

      if ( iDur != null )
      {
         mAttemptExDurControl = true;
         mAttemptExDur = new ADLDuration(ADLDuration.FORMAT_SCHEMA, iDur);
      }
      else
      {
         mAttemptExDurControl = false;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setAttemptExDur");
      }
   }

   /**
    * Retrieves the value of the activiyAbsoluteDurationLimitControl
    * Sequencing Definition Model Element (<b>element 3.7</b>) for this
    * activity.
    * 
    * @return <code>true</code> if limitCondition.activiyAbsoluteDurationLimit
    *         is defined for this activity, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getActivityAbDurControl()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getActivityAbDurControl");
         System.out.println("  ::-->  " + mActivityAbDurControl);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getActivityAbDurControl");
      }

      return mActivityAbDurControl;
   }

   /**
    * Retrieves the value of the limitCondition.activiyAbsoluteDurationLimit
    * Sequencing Definition Model Element (<b>element 3.8</b>) for this
    * activity.
    * 
    * @return The absolute duration limit for an activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getActivityAbDur()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getActivityAbDur");
      }

      String dur = null;

      if ( mActivityAbDur != null )
      {
         dur = mActivityAbDur.format(ADLDuration.FORMAT_SCHEMA);
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + dur);
         System.out.println("  :: SeqActivity    --> END   - getActivityAbDur");
      }

      return dur;
   } 

   /**
    * Sets the value of the LimitCondition.activityAbsoluteDurationLimit
    * Sequencing Definition Model Element (<b>element 3.8</b>) for this
    * activity.
    * 
    * @param iDur   The absolute duration (<code>String</code>) for an activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setActivityAbDur(String iDur)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setActivityAbDur");
         System.out.println("  ::-->  " + iDur);
      }

      if ( iDur != null )
      {
         mActivityAbDurControl = true;
         mActivityAbDur = new ADLDuration(ADLDuration.FORMAT_SCHEMA, iDur);
      }
      else
      {
         mActivityAbDurControl = false;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setActivityAbDur");
      }
   }

   /**
    * Retrieves the value of the activityExperiencedDurationLimitControl
    * Sequencing Definition Model Element (<b>element 3.9</b>) for this
    * activity.
    * 
    * @return <code>true</code> if
    *         limitCondition.activityExperiencedDurationLimit is defined for
    *         this activity, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getActivityExDurControl()

   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getActivityExDurControl");
         System.out.println("  ::-->  " + mActivityExDurControl);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getActivityExDurControl");
      }

      return mActivityExDurControl;
   }

   /**
    * Retrieves the value of the limitCondition.activityExperiencedDurationLimit
    * Sequencing Definition Model Element (<b>element 3.10</b>)
    * for this activity.
    * 
    * @return The experienced duration limit for an activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getActivityExDur()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getActivityExDur");
      }

      String dur = null;

      if ( mActivityExDur != null )
      {
         dur = mActivityExDur.format(ADLDuration.FORMAT_SCHEMA);
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + dur);
         System.out.println("  :: SeqActivity    --> END   - getActivityExDur");
      }

      return dur;
   }

   /**
    * Sets the value of the LimitCondition.activityExperiencedDurationLimit
    * Sequencing Definition Model Element (<b>element 3.10</b>) for this
    * activity.
    * 
    * @param iDur   The experienced duration (<code>String</code>) for an 
    *               activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setActivityExDur(String iDur)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setActivityExDur");
         System.out.println("  ::-->  " + iDur);
      }

      if ( iDur != null )
      {
         mActivityExDurControl = true;
         mActivityExDur = new ADLDuration(ADLDuration.FORMAT_SCHEMA, iDur);
      }
      else
      {
         mActivityExDurControl = false;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setActivityExDur");
      }
   }

   /**
    * Retrieves the value of the limitCondition.beginTimeLimitControl
    * Sequencing Definition Model Element (<b>element 3.11</b>) for this
    * activity.
    * 
    * @return <code>true</code> if limitCondition.beginTimeLimit
    *         is defined for this activity, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getBeginTimeLimitControl()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getBeginTimeLimitControl");
         System.out.println("  ::-->  " + mBeginTimeControl);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getBeginTimeLimitControl");
      }

      return mBeginTimeControl;
   }

   /**
    * Retrieves the value of the limitCondition.beginTimeLimit Sequencing
    * Definition Model Element (<b>element 3.12</b>) for this activity.
    * 
    * @return The time limit when an activity may begin.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getBeginTimeLimit()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getBeginTimeLimit");
         System.out.println("  ::-->  " + mBeginTime);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getBeginTimeLimit");
      }

      return mBeginTime;
   }

   /**
    * Sets the value of the LimitCondition.beginTimeLimit Sequencing Definition
    * Model Element (<b>element 3.12</b>) for this activity.
    * 
    * @param iTime  The time (<code>String</code>), before which, an attempt
    *               on the activity cannot begin.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setBeginTimeLimit(String iTime)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setBeginTimeLimit");
         System.out.println("  ::-->  " + iTime);
      }

      if ( iTime != null )
      {
         mBeginTimeControl = true;
         mBeginTime = iTime;
      }
      else
      {
         mBeginTimeControl = false;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setBeginTimeLimit");
      }
   }

   /**
    * Retrieves the value of the limitCondition.endTimeLimitControl
    * Sequencing Definition Model Element (<b>element 3.13</b>) for this
    * activity.
    * 
    * @return <code>true</code> if limitCondition.endTimeLimit
    *         is defined for this activity, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getEndTimeLimitControl()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getEndTimeLimitControl");
         System.out.println("  ::-->  " + mEndTimeControl);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getEndTimeLimitControl");
      }

      return mEndTimeControl;
   }

   /**
    * Retrieves the value of the limitCondition.endTimeLimit Sequencing
    * Definition Model Element (<b>element 3.14</b>) for this activity.
    * 
    * @return The time limit by which an activity must end.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getEndTimeLimit()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getEndTimeLimit");
         System.out.println("  ::-->  " + mEndTime);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getEndTimeLimit");
      }

      return mEndTime;
   }

   /**
     * Sets the value of the LimitCondition.endTimeLimit Sequencing Definition
     * Model Element (<b>element 3.14</b>) for this activity.
     * 
     * @param iTime  The time (<code>String</code>), after which, an attempt
     *               on the activity is invalid.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setEndTimeLimit(String iTime)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setEndTimeLimit");
         System.out.println("  ::-->  " + iTime);
      }

      if ( iTime != null )
      {
         mEndTimeControl = true;
         mEndTime = iTime;
      }
      else
      {
         mEndTimeControl = false;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setEndTimeLimit");
      }
   }

   /**
    * Retrieves the value of the set of Auxiliary Resource Sequencing
    * Definition Model Elements (<b>element 4</b>) for this activity.
    * 
    * @return The set (<code>Vector</code> of <code>ADLAuxiliaryResource</code>
    *         objects) of auxiliary resource assoiciated with the activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public Vector getAuxResources()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getAuxResources");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getAuxResources");
      }

      return mAuxResources;
   }

   /**
    * Sets the value of the Auxiliary Resource Sequencing Definition
    * Model Element (<b>element 4</b>) for this activity.
    * 
    * @param iRes   The set (<code>Vector</code>) of auxiliary resources
    *               (<code>ADLAuxiliaryResource</code>) associated with this
    *               activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setAuxResources(Vector iRes)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setAuxResources");

         if ( iRes != null )
         {
            System.out.println("  ::-->  " + iRes.size());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }
      }

      mAuxResources = iRes;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setAuxResources");
      }
   }

   /**
    * Retrieves the Rollup Rules, Sequencing Definiition Model Element
    * (<b>element 5</b>) defined for this activity.
    * 
    * @return The Rollup Rules (<code>SeqRollupRuleset</code) defined for this
    *         activity, or <code>null</code> if no rollup rules have been
    *         defined.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public SeqRollupRuleset getRollupRules()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getRollupRules");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getRollupRules");
      }

      return mRollupRules;
   }

   /**
    * Sets the Rollup Rules Sequencing Definition Model Elements
    * (<b>element 5</b) defined for this activity.
    * 
    * @param iRuleSet The set (<code>SeqRollupRuleset</code>) of Rollup Rules
    *                 defined for this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setRollupRules(SeqRollupRuleset iRuleSet)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setRollupRules");
         if ( iRuleSet != null )
         {
            System.out.println("  ::-->  " + iRuleSet.size());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }
      }

      mRollupRules = iRuleSet;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setRollupRules");
      }
   }


   /**
    * Describes if measure should be used to evaluate satisfaction if the
    * activity is active.
    * 
    * @return Indicates if measure should be used to evaluate satisfaction if 
    *         the activity is active.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getSatisfactionIfActive()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getSatisfactionIfActive");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getSatisfactionIfActive");
      }

      return mActiveMeasure;
   }

   /**
    * Indicates if measure should be used to evaluate satisfaction if the
    * activity is active.
    * 
    * @param iActiveMeasure Indicates if measure should be used to evaluate 
    *                       satisfaction if the activity is active.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setSatisfactionIfActive(boolean iActiveMeasure)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setSatisfactionIfActive");
         System.out.println("  ::-->  " + iActiveMeasure);
      }

      // Assume the token is OK due to previous validation.
      mActiveMeasure = iActiveMeasure;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setSatisfactionIfActive");
      }
   }

   /**
    * Retrieves Satisfied Rollup Rule Consideration Sequencing Model Element
    * defined for this activity.
    * 
    * @return Indication of when the activity should be included in its
    *         parents Satisfaction rollup rule evaluation.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getRequiredForSatisfied()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getRequiredForSatisfied");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getRequiredForSatisfied");
      }

      return mRequiredForSatisfied;
   }

   /**
    * Sets the Satisfied Rollup Rule Consideration Sequencing Definition
    * Model Element defined for this activity.
    * 
    * @param iConsider Indication of when the activity should be included in
    *                  its parent's Satisfaction rollup rule evaluation.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setRequiredForSatisfied(String iConsider)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setRequiredForSatisfied");
         System.out.println("  ::-->  " + iConsider);
      }

      // Assume the token is OK due to previous validation.
      mRequiredForSatisfied = iConsider;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setRequiredForSatisfied");
      }
   }

   /**
    * Retrieves Not Satisfied Rollup Rule Consideration Sequencing Model Element
    * defined for this activity.
    * 
    * @return Indication of when the activity should be included in its
    *         parents Satisfaction rollup rule evaluation.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getRequiredForNotSatisfied()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getRequiredForNotSatisfied");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getRequiredForNotSatisfied");
      }

      return mRequiredForNotSatisfied;
   }

   /**
    * Sets the Not Satisfied Rollup Rule Consideration Sequencing Definition
    * Model Element defined for this activity.
    * 
    * @param iConsider Indication of when the activity should be included in
    *                  its parent's Satisfaction rollup rule evaluation.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setRequiredForNotSatisfied(String iConsider)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setRequiredForNotSatisfied");
         System.out.println("  ::-->  " + iConsider);
      }

      // Assume the token is OK due to previous validation.
      mRequiredForNotSatisfied = iConsider;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setRequiredForNotSatisfied");
      }
   }

   /**
    * Retrieves Complete Rollup Rule Consideration Sequencing Model Element
    * defined for this activity.
    * 
    * @return Indication of when the activity should be included in its
    *         parents Satisfaction rollup rule evaluation.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getRequiredForCompleted()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getRequiredForCompleted");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getRequiredForCompleted");
      }

      return mRequiredForCompleted;
   }

   /**
    * Sets the Complete Rollup Rule Consideration Sequencing Definition
    * Model Element defined for this activity.
    * 
    * @param iConsider Indication of when the activity should be included in
    *                  its parent's Satisfaction rollup rule evaluation.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setRequiredForCompleted(String iConsider)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setRequiredForCompleted");
         System.out.println("  ::-->  " + iConsider);
      }

      // Assume the token is OK due to previous validation.
      mRequiredForCompleted = iConsider;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setRequiredForCompleted");
      }
   }

   /**
    * Retrieves Incomplete Rollup Rule Consideration Sequencing Model Element
    * defined for this activity.
    * 
    * @return Indication of when the activity should be included in its
    *         parents Satisfaction rollup rule evaluation.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getRequiredForIncomplete()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getRequiredForIncomplete");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getRequiredForIncomplete");
      }

      return mRequiredForIncomplete;
   }

   /**
    * Sets the Incomplete Rollup Rule Consideration Sequencing Definition
    * Model Element defined for this activity.
    * 
    * @param iConsider Indication of when the activity should be included in
    *                  its parent's Satisfaction rollup rule evaluation.
    *
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setRequiredForIncomplete(String iConsider)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setRequiredForIncomplete");
         System.out.println("  ::-->  " + iConsider);
      }

      // Assume the token is OK due to previous validation.
      mRequiredForIncomplete = iConsider;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setRequiredForIncomplete");
      }
   }

   /**
    * Retrieves the value of the set of Objective Definition Sequencing
    * Definition Model Elements (<b>elements 6 and 7</b>) for this activity.
    * 
    * @return The set of objectives assoiciated with the activity.  These
    *         objectivees are returned as a <code>Vector</code> of
    *         <code>SeqObjective</code> objects.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public Vector getObjectives()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjectives");
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjectives");
      }

      return mObjectives;
   }

   /**
    * Sets the value of the Objectives Resource Sequencing Definition
    * Model Elements (<b>elements 6 and 7</b>) for this activity.
    * 
    * @param iObjs  The set (<code>Vector</code> of <code>SeqObjective</code>)
    *               of objectives(s) associated with this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setObjectives(Vector iObjs)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setObjectives");
         if ( iObjs != null )
         {
            System.out.println("  ::-->  " + iObjs.size());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }
      }

      mObjectives = iObjs;

      if ( iObjs != null )
      {
         for ( int i = 0; i < iObjs.size(); i++ )
         {
            SeqObjective obj = (SeqObjective)iObjs.elementAt(i);

            if ( obj.mMaps != null )
            {
               if ( mObjMaps == null )
               {
                  mObjMaps = new Hashtable();
               }

               mObjMaps.put(obj.mObjID, obj.mMaps);
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setObjectives");
      }
   }

   /**
    * Retrieves the value of the Rollup Objective Satisfied Sequencing
    * Definition Model Element (<b>element 8.1</b>) for this activity.
    * 
    * @return <code>true</code> if objective status for this activity should
    *         be considered during rollup for its parent, or
    *         <code>false</code> if it should not be considered.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getIsObjRolledUp()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getIsObjRolledUp");
         System.out.println("  ::-->  " + mIsObjectiveRolledUp);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getIsObjRolledUp");
      }

      return mIsObjectiveRolledUp;
   }

   /**
    * Sets the value of the Rollup Objective Satisfied Sequencing
    * Definition Model Element (<b>element 8.1</b>) for this activity.
    * 
    * @param iRolledup <code>true</code> if this activity should contribute
    *                  mastery status to its parent during rollup; otherwise
    *                  <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setIsObjRolledUp(boolean iRolledup)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setIsObjRolledUp");
         System.out.println("  ::-->  " + iRolledup);
      }

      mIsObjectiveRolledUp = iRolledup;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setIsObjRolledUp");
      }
   }

   /**
    * Retrieves the value of the Rollup Objective Measure Weight Sequencing
    * Definition Model Element (<b>element 8.2</b>) for this activity.
    * 
    * @return A <code>double</code> value from 0.0 to 1.0, describing the
    *         weight this activity's score will have during rollup.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public double getObjMeasureWeight()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjMeasureWeight");
         System.out.println("  ::-->  " + mObjMeasureWeight);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjMeasureWeight");
      }

      return mObjMeasureWeight;
   }

   /**
    * Sets the value of the Rollup Objective Measure Weight Sequencing
    * Definition Model Element (<b>element 8.2</b>) for this activity.
    * 
    * @param iWeight A value from 0.0 to 1.0 describing the
    *                weight this activity's score will have during rollup.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setObjMeasureWeight(double iWeight)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setObjMeasureWeight");
         System.out.println("  ::-->  " + iWeight);
      }

      mObjMeasureWeight = iWeight;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setObjMeasureWeight");
      }
   }

   /**
    * Retrieves the value of the Rollup Progress Completion Sequencing
    * Definition Model Element (<b>element 8.3</b>) for this activity.
    * 
    * @return <code>true</code> if completion status for this activity should
    *         be considered during rollup for its parent, or
    *         <code>false</code> if it should not be considered.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getIsProgressRolledUp()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getIsProgressRolledUp");
         System.out.println("  ::-->  " + mIsProgressRolledUp);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getIsProgressRolledUp");
      }

      return mIsProgressRolledUp;
   }

   /**
    * Sets the value of the Rollup Progress Completion Sequencing
    * Definition Model Element (<b>element 8.3</b>) for this activity.
    * 
    * @param iRolledup <code>true</code> if this activity should contribute
    *                  completion status to its parent during rollup; otherwise
    *                  <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setIsProgressRolledUp(boolean iRolledup)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setIsProgressRolledUp");
         System.out.println("  ::-->  " + iRolledup);
      }

      mIsProgressRolledUp = iRolledup;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setIsProgressRolledUp");
      }
   }

   /**
    * Sets the value of the SelectionControl.SelectionTiming Sequencing
    * Definition Model element (<b>element 9.1</b>) for this activity.
    * 
    * @param iTiming Indicates when the selection process should be applied
    *                to this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setSelectionTiming(String iTiming)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "setSelectionTiming");
         System.out.println("  ::-->  " + iTiming);
      }

      // Validate vocabulary
      if ( !(iTiming.equals(SeqActivity.TIMING_NEVER) || 
             iTiming.equals(SeqActivity.TIMING_ONCE) ||
             iTiming.equals(SeqActivity.TIMING_EACHNEW) ) )
      {
         mSelectTiming = SeqActivity.TIMING_NEVER;
      }
      else
      {
         mSelectTiming = iTiming;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END  - " +
                            "setSelectionTiming");
      }
   }

   /**
    * Retrieves the value of the SelectionControl.SelectionTiming
    * Sequencing Definition Model Element (<b>element 9.1</b>) for
    * this activity.
    * 
    * @return When the selectiion process should be applied to this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getSelectionTiming()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getSelectionTiming");
         System.out.println("  ::-->  " + mSelectTiming);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getSelectionTiming");
      }

      return mSelectTiming;
   }

   /**
    * Retrieves the value of the SelectCountStatus Sequencing Definition Model
    * Element (<b>element 9.2</b>) for this activity.
    * 
    * @return <code>true</code> if the value of Selection Count is valid, 
    * otherwise <code>false</code>
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getSelectStatus()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getSelectStatus");
         System.out.println("  ::-->  " + mSelectStatus);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getSelectStatus");
      }

      return mSelectStatus;
   }

   /**
    * Retrieves the value of the SelectCount Sequencing Definition Model
    * Element (<b>element 9.3</b>) for this activity.
    * 
    * @return The size of the random set of children to be selected from
    *         from this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public int getSelectCount()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getSelectCount");
      }

      // If the number to be randomized is greater than the number of children
      // available, no  selection is required
      if ( mChildren != null )
      {
         if ( mSelectCount >= mChildren.size() )
         {
            mSelectTiming = "never";
            mSelectCount = mChildren.size();
         }
      }
      else
      {
         // No children to select from; can't select
         mSelectStatus = false;
         mSelectCount = 0;
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + mSelectCount);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getSelectCount");
      }

      return mSelectCount;
   }

   /**
    * Sets the value of the SelectCount Sequencing Definition Model
    * Element (<b>element 9.3</b>) for this activity.
    * 
    * @param iCount Indicates the number of children to be selected when
    *               the selection process is applied to this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setSelectCount(int iCount)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - setSelectCount");
         System.out.println("  ::-->  " + iCount);
      }

      if ( iCount >= 0 )
      {
         mSelectStatus = true;
         mSelectCount = iCount;
      }
      else
      {
         mSelectStatus = false;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END  - setSelectCount");
      }
   }

   /**
    * Sets the value of the RandomizationControls.RandomizationTiming Sequencing
    * Definition Model element (<b>element 10.1</b>) for this activity.
    * 
    * @param iTiming Indicates when the randomization process should be applied
    *                to this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setRandomTiming(String iTiming)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - setRandomTiming");
         System.out.println("  ::-->  " + iTiming);
      }

      // Validate vocabulary
      if ( !(iTiming.equals(SeqActivity.TIMING_NEVER) || 
             iTiming.equals(SeqActivity.TIMING_ONCE) ||
             iTiming.equals(SeqActivity.TIMING_EACHNEW) ) )
      {
         mSelectTiming = SeqActivity.TIMING_NEVER;
      }
      else
      {
         mRandomTiming = iTiming;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END  - setRandomTiming");
      }
   }

   /**
    * Retrieves the value of the RandomizationControl.RandomizationTiming
    * Sequencing Definition Model Element (<b>element 10.1</b>) for
    * this activity.
    * 
    * @return When the randomization process should be applied to this
    *         activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getRandomTiming()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getRandomTiming");
         System.out.println("  ::-->  " + mRandomTiming);
         System.out.println("  :: SeqActivity    --> END   - getRandomTiming");
      }

      return mRandomTiming;
   } 

   /**
    * Retrieves the value of the RandomizationControl.RandomizeChildren
    * Sequencing Definition Model Element (<b>element 10.2</b>) for
    * this activity.
    * 
    * @return If the children of this activty should be reordered when the
    *         randomization process is applied (<code>boolean</code>).
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getReorderChildren()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "getReorderChildren");
         System.out.println("  ::-->  " + mReorder);
         System.out.println("  :: SeqActivity    --> END   - " +
                            "getReorderChildren");
      }

      return mReorder;
   }

   /**
    * Sets the value of the RandomizationControl.RandomizeChildren 
    * Sequencing Definition Model Element (<b>element 10.2</b>) for
    * this activity.
    * 
    * @param iReorder Indicates if children of this activity should be
    *                 reordered when the randomization process is applied to
    *                 this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setReorderChildren(boolean iReorder)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - " +
                            "setReorderChildren");         
         System.out.println("  ::-->  " + iReorder);
      }

      mReorder = iReorder;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> END   - " +
                            "setReorderChildren"); 
      }
   }

   /**
    * Retrieves the value of the DeliveryControl.isTracked Sequencing Definition
    * Model Element (<b>element 11.1</b>) for this activity.
    * 
    * @return <code>true</code> if the activity is tracked, otherwise
    *         <code>false</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getIsTracked()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getIsTracked");
         System.out.println("  ::-->  " + mIsTracked);
         System.out.println("  :: SeqActivity    --> END   - getIsTracked");
      }

      return mIsTracked;
   }

   /**
    * Sets the value of the DeliveryControl.isTracked Sequencing Definition
    * Model Element (<b>element 11.1</b>)  for this activity.
    * 
    * @param iTracked <code>true</code> if Tracking Status informatin should be
    *                 maintained for this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setIsTracked(boolean iTracked)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setIsTracked");
         System.out.println("  ::-->  " + iTracked);
      }

      mIsTracked = iTracked;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setIsTracked");
      }
   }

   /**
    * Retrieves the value of the DeliveryControl.CompletionSetbyContent
    * Sequencing Definition Model Element (<b>element 11.2</b>) for this
    * activity.
    * 
    * @return <code>true</code> if the activity communicates its progress
    *         status information; <code>false</code> if the LMS should set the
    *         activity's progress status.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getSetCompletion()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getSetCompletion");
         System.out.println("  ::-->  " + mContentSetsCompletion);
         System.out.println("  :: SeqActivity    --> END   - getSetCompletion");
      }

      return mContentSetsCompletion;
   }

   /**
    * Sets the value of the DeliveryControl.CompletionSetbyContent
    * Sequencing Definition Model Element (<b>element 11.2</b>) for this
    * activity.
    * 
    * @param iSet   <code>true</code> if the activity communicates its progress
    *               status information.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setSetCompletion(boolean iSet)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setSetCompletion");
         System.out.println("  ::-->  " + iSet);
      }

      mContentSetsCompletion = iSet;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setSetCompletion");
      }
   }

   /**
    * Retrieves the value of the DeliveryControl.ObjectiveSetbyContent
    * Sequencing Definition Model Element (<b>element 11.3</b>) for this
    * activity.
    * 
    * @return <code>true</code> if the activity communicates its objective
    *         status information; <code>false</code> if the LMS should set the
    *         activity's objective status.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getSetObjective()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getSetObjective");
         System.out.println("  ::-->  " + mContentSetsObj);
         System.out.println("  :: SeqActivity    --> END   - getSetObjective");
      }

      return mContentSetsObj;
   }

   /**
    * Sets the value of the DeliveryControl.ObjectiveSetbyContent
    * Sequencing Definition Model Element (<b>element 11.3</b>) for this
    * activity.
    * 
    * @param iSet   <code>true</code> if the activity communicates its objective
    *               status information.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setSetObjective(boolean iSet)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " +
                            "setSetObjective");
         System.out.println("  ::-->  " + iSet);
      }

      mContentSetsObj = iSet;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setSetObjective");
      }
   }


   // The following accessors provide additional activity state information 
   // that is not included in the Sequencing Definition Model
   // ----------------------------------------------------------------------

   /**
    * Retrieves the value of the Delivery Mode.
    * 
    * @return The DeliveryMode (<code>String</code>) for this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getDeliveryMode()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity    --> BEGIN - getDeliveryMode");
         System.out.println("  ::-->  " + mDeliveryMode);
         System.out.println("  :: SeqActivity    --> END   - getDeliveryMode");
      }

      return mDeliveryMode;
   }

   /**
    * Sets the value of the Delivery Mode for this activity.
    * 
    * @param iDeliveryMode The Delivery Mode for this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setDeliveryMode(String iDeliveryMode)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setDeliveryMode");
         System.out.println("  ::-->  " + iDeliveryMode);
      }

      // Test vocabulary
      if ( iDeliveryMode.equals("browse") || iDeliveryMode.equals("review") ||
           iDeliveryMode.equals("normal") )
      {
         mDeliveryMode = iDeliveryMode;
      }
      else
      {
         mDeliveryMode = "normal";
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setDeliveryMode");
      }
   } 

   /**
    * Retrieves the ID of the resource associated with this activity.
    * 
    * @return The ID (<code>String</code>) of the resource associated with this
    *         activity, or <code>null</code> if the activity does not have a
    *         resource.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getResourceID()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getResourceID");
         System.out.println("  ::-->  " + mResourceID);
         System.out.println("  :: SeqActivity     --> END   - getResourceID");
      }

      return mResourceID;
   }

   /**
    * Sets the ID of the resource associated with this activity.
    * 
    * @param iResourceID The ID (<code>String</code>) of the resource.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setResourceID(String iResourceID)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setResourceID");
         System.out.println("  ::--> Resource ID     : " + iResourceID);
      }

      mResourceID = iResourceID;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setResourceID");
      }
   }

   /**
    * Retrieves the ID of the activity's associated persistent state.
    * 
    * @return The ID (<code>String</code>) of the persistent state
    *         associated with this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */   
   public String getStateID()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getStateID");
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + mStateID);
         System.out.println("  :: SeqActivity     --> END   - getStateID");
      }

      return mStateID;
   }

   /**
    * Sets the ID of the activity's associated persisent state.
    * 
    * @param iStateID The ID (<code>String</code>) of the activity's persistent
    *                 state information.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setStateID(String iStateID)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setStateID");
         System.out.println("  ::--> State ID     : " + iStateID);
      }

      mStateID = iStateID;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setStateID");
      }
   }

   /**
    * Retrieves the activity ID of this activity.
    * 
    * @return The unique ID (<code>String</code>) of this resource.<br>
    *         NOTE: This will not be <code>null</code>.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getID()
   {
      return mActivityID;
   }

   /**
    * Sets the ID of this activity.
    * 
    * @param iID   The activity's ID.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setID(String iID)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setID");
         System.out.println("  ::-->  " + iID);
      }

      mActivityID = iID;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setID");
      }
   }

   /**
    * Retrieves the user-readable title of this activity.
    * 
    * @return The user-readable title (<code>String</code>) of this activity, or
    *         <code>null</code> if the activity does not have a title.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getTitle()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getTitle");
         System.out.println("  ::-->  " + mTitle);
         System.out.println("  :: SeqActivity     --> END   - getTitle");
      }

      return mTitle;
   }

   /**
    * Sets the user-readable title for this activity.
    * 
    * @param iTitle The user-readable title for this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setTitle(String iTitle)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setTitle");
         System.out.println("  ::-->  " + iTitle);
      }

      mTitle = iTitle;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setTitle");
      }
   }

   /**
    * Set this activity's XML fragment of sequencing information.
    * 
    * @param iXML   Contains the XML fragment.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setXMLFragment(String iXML)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setXMLFragment");
         System.out.println("  ::-->  " + iXML);
      }

      mXML = iXML;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setXMLFragment");
      }
   }

   /**
    * Retrieves this activity's XML fragment of sequencing information.
    * 
    * @return The XML fragment.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getXMLFragment()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getXMLFragment");
         System.out.println("  ::-->  " + mXML);
         System.out.println("  :: SeqActivity     --> END   - getXMLFragment");
      }

      return mXML;
   }

   /**
    * Associates an ID with learner of this activity
    * 
    * @param iLearnerID The ID of the learner associated with this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setLearnerID(String iLearnerID)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setLearnerID");
         System.out.println("  ::-->  " + iLearnerID);
      }

      mLearnerID = iLearnerID;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setLearnerID");
      }
   }

   /**
    * Retrives the a learner ID associated with this activity
    * 
    * @return The ID (<code>String</code>> of the learner associated with
    *         this activity.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getLearnerID()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getLearnerID");
         System.out.println("  ::-->  " + mLearnerID);
         System.out.println("  :: SeqActivity     --> END   - getLearnerID");
      }

      return mLearnerID;
   }

   /**
    * Retrieves the 'selected' state of the activity.  Activities become 
    * 'selected' through the Selection and Randomization Process.
    * 
    * @return Indication if the activity is currently selected
    */   
   public boolean getIsSelected()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getSelected");
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + mSelected);
         System.out.println("  :: SeqActivity     --> END   - getSelected");
      }

      return mSelected;
   }

   /**
    * Sets the 'selected' state of the activity
    * 
    * @param iSelected Indicates if the activity is 'selected.
    */
   public void setIsSelected(boolean iSelected)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setSelected");
         System.out.println("  ::--> State ID     : " + iSelected);
      }

      mSelected = iSelected;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setSelected");
      }
   }

   /**
    * Associates an ID with scope of this activity's objectives
    * 
    * @param iScopeID The ID of the scope associated with the objectives
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setScopeID(String iScopeID)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setScopeID");
         System.out.println("  ::-->  " + iScopeID);
      }

      mScopeID = iScopeID;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setScopeID");
      }
   }

   /**
    * Retrives the a scope ID associated with this activity.
    * 
    * @return The ID (<code>String</code>> of the scope associated with
    *         this activity's objectives.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public String getScopeID()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getScopeID");
         System.out.println("  ::-->  " + mScopeID);
         System.out.println("  :: SeqActivity     --> END   - getScopeID");
      }

      return mScopeID;
   }

   // The following package accessors provide acess to the activity's state.   
   // These methods are only called by the sequencing subprocesses.
   // ----------------------------------------------------------------------

   /**
    * Retrieve this activity's IsVisible status.
    * 
    * @return <code>true</code> if this activity is 'visible', otherwise
    *         <code>false</code>
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getIsVisible()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getIsVisible");
         System.out.println("  ::-->  " + mIsVisible);
         System.out.println("  :: SeqActivity     --> END   - getIsVisible");
      }

      return mIsVisible;
   }

   /**
    * Set this activity's 'IsVisible' status.
    * 
    * @param iIsVisible
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setIsVisible(boolean iIsVisible)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setIsVisible");
         System.out.println("  ::-->  " + iIsVisible);
      }

      mIsVisible = iIsVisible;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setIsVisible");
      }
   }

   /**
    * Retrieve this activity's 'IsActive' status.
    * 
    * @return <code>true</code> if this activity is assumed to be 'active',
    *         otherwise <code>false</code>
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getIsActive()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getIsActive");
         System.out.println("  ::-->  " + mIsActive);
         System.out.println("  :: SeqActivity     --> END   - getIsActive");
      }

      return mIsActive;
   }

   /**
    * Set this activity's 'IsActive' status.
    * 
    * @param iActive Indicates that the activity is assumed to be 'Active'.
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setIsActive(boolean iActive)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setIsActive");
         System.out.println("  ::-->  " + iActive);
      }

      mIsActive = iActive;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setIsActive");
      }
   }

   /**
    * Retrieve this activity's 'IsSuspended' status.
    * 
    * @return <code>true</code> if this activity is assumed to be 'suspended',
    *         otherwise <code>false</code>
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public boolean getIsSuspended()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getIsSuspended");
         System.out.println("  ::-->  " + mIsSuspended);
         System.out.println("  :: SeqActivity     --> END   - getIsSuspended");
      }

      return mIsSuspended;
   }

   /**
    * Set this activity's 'IsSuspended' status.
    * 
    * @param iSuspended Is the activity assumed to be 'suspended'?
    * 
    * @see <code>SeqActivityStateAccess</code>
    */
   public void setIsSuspended(boolean iSuspended)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setIsSuspended");
         System.out.println("  ::-->  " + iSuspended);
      }

      mIsSuspended = iSuspended;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setIsSuspended");
      }
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Implementation of SeqActivityTrackingAccess Interface
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Retrieves the attempt status of this activity.
    * 
    * @return <code>true</code> if the activity has been attempted, otherwise,
    *         <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getActivityAttempted()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getActivityAttempted");
         System.out.println("  ::-->  " + 
                            ((mNumAttempt == 0) ?
                             "NotAttempted" : "Attempted"));
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getActivityAttempted");
      }

      return(mNumAttempt != 0);
   }


   /**
    * Retrieves the current attempt's progress status.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the current attempt on the activity is
    *         completed, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getAttemptCompleted(boolean iIsRetry)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getAttemptCompleted");
      }

      String progress = ADLTracking.TRACK_UNKNOWN;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives, 
                                                mLearnerID,
                                                mScopeID);
   
            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         // make sure the current state is valid
         if ( !mCurTracking.mDirtyPro && !iIsRetry )
         {
            progress = mCurTracking.mProgress;
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + progress);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getAttemptCompleted");
      }

      return(progress.equals(ADLTracking.TRACK_COMPLETED));
   }

   /**
    * Set the current attempt's progress status to the desired value.<br><br>
    * Valid values are: <code>unknown</code>, <code>completed</code>, and
    * <code>incomplete</code>.
    * 
    * @param iProgress New value for the attempt's progress status.
    * 
    * @return <code>true</code> if the progress status of the activty changed,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean setProgress(String iProgress)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setProgress");
         System.out.println("  ::-->  " + iProgress);
      }

      boolean statusChange = false;

      if ( mIsTracked )
      {
         // Validate state data
         if ( iProgress.equals(ADLTracking.TRACK_UNKNOWN) ||
              iProgress.equals(ADLTracking.TRACK_COMPLETED) ||
              iProgress.equals(ADLTracking.TRACK_INCOMPLETE) )
         {
            if ( mCurTracking != null )
            {
               String prev = mCurTracking.mProgress;
   
               mCurTracking.mProgress = iProgress;
   
               statusChange = !prev.equals(iProgress);
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + statusChange);
         System.out.println("  :: SeqActivity     --> END   - setProgress");
      }

      return statusChange;
   }

   /**
    * Determines if the current attempt's progress status is valid.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the progress status of the current attempt
    *         is valid, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess`</code>
    */
   /* package */
   boolean getProgressStatus(boolean iIsRetry)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getProgressStatus");
      }

      boolean status = false;


      if ( mIsTracked )
      {
         if ( mCurTracking != null )
         {
   
            if ( !mCurTracking.mDirtyPro && !iIsRetry )
            {
               status = !mCurTracking.mProgress.
                  equals(ADLTracking.TRACK_UNKNOWN);
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + status);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getProgressStatus");
      }

      return status;
   }

   /**
    * Indicates if the designated objective's measure value is valid.
    * 
    * @param iObjID   ID of the objective whose measure is desired.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the designated objective's measure is valid,
    *         otherwise <code>false</code>.
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getObjMeasureStatus(String iObjID, boolean iIsRetry)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjMeasureStatus");
         System.out.println("  ::-->  " + iObjID);
      }

      boolean status = false;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives,
                                                mLearnerID,
                                                mScopeID);
   
            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         // A null objective indicates the primary objective
         if ( iObjID == null )
         {
            status = getObjMeasureStatus(iIsRetry);
         }
         else if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(iObjID)));
   
            if ( obj != null )
            {
               String result = null;
   
               result = obj.getObjMeasure(iIsRetry);
   
               if ( !result.equals(ADLTracking.TRACK_UNKNOWN) )
               {
                  status = true;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  Objective undefined");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : Bad Tracking");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }


      if ( _Debug )
      {
         System.out.println("  ::-->  " + status);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjMeasureStatus");
      }

      return status;
   }

   /**
    * Indicates if the primary objective's measure value is valid.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the designated objective's measure is valid,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getObjMeasureStatus(boolean iIsRetry)
   {
      return getObjMeasureStatus(iIsRetry, false);
   }


   /**
    * Indicates if the primary objective's measure value is valid.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @param iUseLocal Indicates if only the local status information should
    *                  be considered.
    * 
    * @return <code>true</code> if the designated objective's measure is valid,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getObjMeasureStatus(boolean iIsRetry, boolean iUseLocal)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjMeasureStatus");
      }

      boolean status = false;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives,
                                                mLearnerID,
                                                mScopeID);
   
            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(
                                       mCurTracking.mPrimaryObj)));
   
            if ( obj != null )
            {
               String result = null;
   
               result = obj.getObjMeasure(iIsRetry, iUseLocal);
   
               if ( !result.equals(ADLTracking.TRACK_UNKNOWN) )
               {
                  status = true;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  ERROR : No primary objective");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : Bad Tracking");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }


      if ( _Debug )
      {
         System.out.println("  ::-->  " + status);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjMeasureStatus");
      }

      return status;

   }


   /**
    * Clears the value of the primary objective's measure.
    * 
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean clearObjMeasure()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "clearObjMeasure");
      }

      boolean statusChange = false;

      if ( mCurTracking != null )
      {
         SeqObjectiveTracking obj = 
         ((SeqObjectiveTracking)(mCurTracking.
                                 mObjectives.get(mCurTracking.mPrimaryObj)));

         if ( obj != null )
         {
            SeqObjective objD = obj.getObj();
            boolean affectSatisfaction = objD.mSatisfiedByMeasure;

            if ( affectSatisfaction )
            {
               affectSatisfaction = !objD.mContributesToRollup ||
                                    ( mActiveMeasure || !mIsActive );
            }

            statusChange = obj.clearObjMeasure(affectSatisfaction);
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : No primary objective");
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + statusChange);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "clearObjMeasure");
      }

      return statusChange;
   }

   /**
    * Clears the value of the designated objective's measure.
    * 
    * @param iObjID ID of the objective whose measure has changed.
    * 
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean clearObjMeasure(String iObjID)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "clearObjMeasure");
         System.out.println("  ::--> " + iObjID);
      }

      boolean statusChange = false;

      // A null objective indicates the primary objective
      if ( iObjID == null )
      {
         statusChange = clearObjMeasure();
      }
      else if ( mCurTracking != null )
      {
         SeqObjectiveTracking obj = 
         ((SeqObjectiveTracking)(mCurTracking.
                                 mObjectives.get(iObjID)));

         if ( obj != null )
         {
            SeqObjective objD = obj.getObj();
            boolean affectSatisfaction = objD.mSatisfiedByMeasure;

            if ( affectSatisfaction )
            {
               affectSatisfaction = !objD.mContributesToRollup ||
                                    ( mActiveMeasure || !mIsActive );
            }

            statusChange = obj.clearObjMeasure(affectSatisfaction);
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  Objective Undefined");
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + statusChange);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "clearObjMeasure");
      }

      return statusChange;
   }

   /**
    * Sets the primary objective's measure to the desired value.
    * 
    * @param iMeasure The value of the objective's measure.
    * 
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean setObjMeasure(double iMeasure)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "setObjMeasure");
         System.out.println("  ::--> " + iMeasure);
      }

      boolean statusChange = false;

      if ( mIsTracked )
      {
         if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.
                                       get(mCurTracking.mPrimaryObj)));
    
            if ( obj != null )
            {
               String prev = obj.getObjStatus(false);
   
               SeqObjective objD = obj.getObj();
               boolean affectSatisfaction = objD.mSatisfiedByMeasure;
   
               if ( affectSatisfaction )
               {
                  affectSatisfaction = !objD.mContributesToRollup ||
                                       ( mActiveMeasure || !mIsActive );
               }
   
               obj.setObjMeasure(iMeasure, affectSatisfaction);
   
               statusChange = !prev.equals(obj.getObjStatus(false));
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  ERROR : No primary objective");
               }
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + statusChange);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setObjMeasure");
      }

      return statusChange;

   }

   /**
    * Sets the designated objective's measure to the desired value.
    * 
    * @param iObjID   ID of the objective whose measure has changed.
    * 
    * @param iMeasure The value of the objective's measure.
    * 
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean setObjMeasure(String iObjID, double iMeasure)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "setObjMeasure");
         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iMeasure);
      }

      boolean statusChange = false;

      if ( mIsTracked )
      {
         // A null objective indicates the primary objective
         if ( iObjID == null )
         {
            statusChange = setObjMeasure(iMeasure);
         }
         else if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(iObjID)));
   
            if ( obj != null )
            {
               String prev = obj.getObjStatus(false);
   
               SeqObjective objD = obj.getObj();
               boolean affectSatisfaction = objD.mSatisfiedByMeasure;
   
               if ( affectSatisfaction )
               {
                  affectSatisfaction = !objD.mContributesToRollup ||
                                       ( mActiveMeasure || !mIsActive );
               }
   
               obj.setObjMeasure(iMeasure, affectSatisfaction);
   
               statusChange = !prev.equals(obj.getObjStatus(false));
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  ERROR : No primary objective");
               }
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + statusChange);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setObjMeasure");
      }

      return statusChange;
   }

   /**
    * Determines if the activity's primary objective is satisfied by measure.
    * 
    * @return <code>true</code> if the primary objective is satisfied by
    *         measure, otherwise <code>false</code>
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getObjSatisfiedByMeasure()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjSatisfiedByMeasure");
      }

      boolean byMeasure = false;

      if ( mCurTracking != null )
      {
         SeqObjectiveTracking obj = 
         ((SeqObjectiveTracking)(mCurTracking.
                                 mObjectives.get(mCurTracking.mPrimaryObj)));

         if ( obj != null )
         {
            byMeasure = obj.getByMeasure();
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : No primary objective");
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + byMeasure);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjSatisfiedByMeasure");
      }

      return byMeasure;
   }

   /**
    * Retreives the designated objective's minimum measure value.<br><br>
    * 
    * @param iObjID ID of the objective whose minimum measure is desired.
    * 
    * @return The measure of the designated objective, or <code>-1.0</code>
    *         if no minimum measure is defined.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   double getObjMinMeasure(String iObjID)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjMinMeasure");
         System.out.println("  ::--> " + iObjID);
      }

      double minMeasure = -1.0;

      if ( mObjectives != null )
      {
         for ( int i = 0; i < mObjectives.size(); i++ )
         {
            SeqObjective obj = (SeqObjective)mObjectives.elementAt(i);

            if ( iObjID.equals(obj.mObjID) )
            {
               minMeasure = obj.mMinMeasure;
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + minMeasure);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjMinMeasure");
      }

      return minMeasure;

   }

   /**
    * Retreives the primary objective's minimum measure value.<br><br>
    * 
    * @return The measure of the designated objective, or <code>-1.0</code>
    *         if no minimum measure is defined.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   double getObjMinMeasure()
   {

      if ( mCurTracking != null )
      {
         return getObjMinMeasure(mCurTracking.mPrimaryObj);
      }
      else
      {
         return -1.0;
      }
   }

   /**
    * Retreives the designated objective's measure value.<br><br>
    * <b>NOTE:</b> the value returned has no signifigance unless the
    * objective's measure status is <code>true</code>.
    * 
    * @param iObjID   ID of the objective whose measure is desired.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return The measure of the designated objective.
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   double getObjMeasure(String iObjID, boolean iIsRetry)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjMeasure");
         System.out.println("  ::--> " + iObjID);
      }

      double measure = 0.0;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives, 
                                                mLearnerID,
                                                mScopeID);
   
            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         // A null objective indicates the primary objective
         if ( iObjID == null )
         {
            measure = getObjMeasure(iIsRetry);
         }
         else if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(iObjID)));
   
            if ( obj != null )
            {
               String result = null;
   
               result = obj.getObjMeasure(iIsRetry);
   
               if ( !result.equals(ADLTracking.TRACK_UNKNOWN) )
               {
                  measure = (new Double(result)).doubleValue();
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  Objective undefined");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : Bad Tracking");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + measure);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjMeasure");
      }

      return measure;
   }

   /**
    * Retreives the primary objective's measure value.<br><br>
    * <b>NOTE:</b> the value returned has no signifigance unless the
    * objective's measure status is <code>true</code>.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    *
    * @return The measure of the primary objective.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   double getObjMeasure(boolean iIsRetry)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjMeasure");
      }

      double measure = 0.0;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives, 
                                                mLearnerID,
                                                mScopeID);
   
            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(
                                       mCurTracking.mPrimaryObj)));
   
            if ( obj != null )
            {
               String result = null;
   
               result = obj.getObjMeasure(iIsRetry);
   
               if ( !result.equals(ADLTracking.TRACK_UNKNOWN) )
               {
                  measure = (new Double(result)).doubleValue();
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  ERROR : No primary objective");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : Bad Tracking");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + measure);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjMeasure");
      }

      return measure;

   }

   /**
    * Triggers the deterministic rollup of measure.
    * 
    */
   /* package */
   void triggerObjMeasure()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "triggerObjMeasure");
      }

      double measure = 0.0;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives, 
                                                mLearnerID, 
                                                mScopeID);
   
            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.
                                       get(mCurTracking.mPrimaryObj)));
   
            if ( obj != null )
            {
   
               if ( obj.getObj().mSatisfiedByMeasure )
               {
   
                  String result = null;
   
                  result = obj.getObjMeasure(false);
   
                  if ( !result.equals(ADLTracking.TRACK_UNKNOWN) )
                  {
                     measure = (new Double(result)).doubleValue();
   
                     obj.setObjMeasure(measure, true);
                  }
                  else
                  {
                     obj.clearObjMeasure(true);
                  }
               }
               else
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> Satisfaction not affected " +
                                        "by measure");
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  ERROR : No primary objective");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : Bad Tracking");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }


      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "triggerObjMeasure");
      }
   }

   /**
    * Determines if the designated objective's progress status is valid.
    * 
    * @param iObjID   ID of the objective.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the designated objective's progress status is
    *         valid, otherwise <code>false</code>.
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getObjStatus(String iObjID, boolean iIsRetry)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjStatus");
         System.out.println("  ::--> " + iObjID);
      }

      boolean status = false;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives,
                                                mLearnerID,
                                                mScopeID);
   
            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         // A null objective indicates the primary objective
         if ( iObjID == null )
         {
            status = getObjStatus(iIsRetry);
         }
         else if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(iObjID)));
   
            if ( obj != null )
            {
               String result = null;
   
               result = obj.getObjStatus(iIsRetry);
   
               if ( !result.equals(ADLTracking.TRACK_UNKNOWN) )
               {
                  status = true;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  Objective not defined");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : Bad Tracking");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + status);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjStatus");
      }

      return status;
   }


   /**
    * Determines if the primary objective's progress status is valid.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the primary objective's progress status is
    *         valid, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getObjStatus(boolean iIsRetry)
   {
      return getObjStatus(iIsRetry, false);
   }

   /**
    * Determines if the primary objective's progress status is valid.
    * 
    * @param iIsRetry  Indicates if this evaluation is occuring during the
    *                  processing of a 'retry' sequencing request.
    * 
    * @param iUseLocal Indicates if only the local status information should
    *                  be considered.
    * 
    * @return <code>true</code> if the primary objective's progress status is
    *         valid, otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getObjStatus(boolean iIsRetry, boolean iUseLocal)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjStatus");
      }

      boolean status = false;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives,
                                                mLearnerID,
                                                mScopeID);

            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(
                                       mCurTracking.mPrimaryObj)));
   
            if ( obj != null )
            {
               String result = null;
   
               result = obj.getObjStatus(iIsRetry, iUseLocal);
   
               if ( !result.equals(ADLTracking.TRACK_UNKNOWN) )
               {
                  status = true;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  ERROR : No primary objective");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : Bad Tracking");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + status);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjStatus");
      }

      return status;
   }


   /**
    * Set the designated objective's status to the desired value.
    * 
    * @param iObjID  ID of the objective whose satisfaction has changed.
    * 
    * @param iStatus New value for the objective's satisfaction status.<br><br>
    *                Valid values are: <code>unknown</code>,
    *                <code>satisfied</code>, or <code>notsatisfied</code>.
    * 
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean setObjSatisfied(String iObjID, String iStatus)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "setObjSatisfied");
         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iStatus);
      }

      boolean statusChange = false;

      if ( mIsTracked )
      {         
         // A null objective indicates the primary objective
         if ( iObjID == null )
         {
            statusChange = setObjSatisfied(iStatus);
         }
         else if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(iObjID)));
   
            if ( obj != null )
            {
               // Validate desired value
               if ( iStatus.equals(ADLTracking.TRACK_UNKNOWN) ||
                    iStatus.equals(ADLTracking.TRACK_SATISFIED) ||
                    iStatus.equals(ADLTracking.TRACK_NOTSATISFIED) )
               {
   
                  String result = obj.getObjStatus(false);
   
                  obj.setObjStatus(iStatus);
   
                  statusChange = !result.equals(iStatus);
               }
               else
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> Invalid status value");
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  ERROR : No primary objective");
               }
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }


      if ( _Debug )
      {
         System.out.println("  ::-->  " + statusChange);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setObjSatisfied");
      }

      return statusChange;
   }

   /**
    * Set the primary objective's status to the desired value.
    * 
    * @param iStatus New value for the objective's satisfaction status.<br><br>
    *                Valid values are: <code>unknown</code>,
    *                <code>satisfied</code>, or <code>notsatisfied</code>.
    * 
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean setObjSatisfied(String iStatus)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "setObjSatisfied");
         System.out.println("  ::--> " + iStatus);
      }

      boolean statusChange = false;

      if ( mIsTracked )
      {
         if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.
                                       get(mCurTracking.mPrimaryObj)));
   
            if ( obj != null )
            {
               // Validate desired value
               if ( iStatus.equals(ADLTracking.TRACK_UNKNOWN) ||
                    iStatus.equals(ADLTracking.TRACK_SATISFIED) ||
                    iStatus.equals(ADLTracking.TRACK_NOTSATISFIED) )
               {
   
                  String result = obj.getObjStatus(false);
   
                  obj.setObjStatus(iStatus);
   
                  statusChange = !result.equals(iStatus);
               }
               else
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> Invalid status value");
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  ERROR : No primary objective");
               }
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + statusChange);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "setObjSatisfied");
      }

      return statusChange;
   }

   /**
    * Retrieves the designated objective's status.
    * 
    * @param iObjID ID of the objective.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the designated objective is satisfied
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getObjSatisfied(String iObjID, boolean iIsRetry)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjSatisfied");
         System.out.println("  ::--> " + iObjID);
      }

      boolean status = false;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives,
                                                mLearnerID,
                                                mScopeID);
   
            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         // A null objective indicates the primary objective
         if ( iObjID == null )
         {
            status = getObjSatisfied(iIsRetry);
         }
         else if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(iObjID)));
   
            if ( obj != null )
            {
               String result = null;
   
               result = obj.getObjStatus(iIsRetry);
   
               if ( result.equals(ADLTracking.TRACK_SATISFIED) )
               {
                  status = true;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  Objective not defined");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : Bad Tracking");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + status);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjSatisfied");
      }

      return status;
   }


   /**
    * Retrieves the primary objective's status.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the primary objective is satisfied
    *         otherwise <code>false</code>.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */
   boolean getObjSatisfied(boolean iIsRetry)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjSatisfied");
      }

      boolean status = false;

      if ( mIsTracked )
      {
         if ( mCurTracking == null )
         {
            ADLTracking track = new ADLTracking(mObjectives,
                                                mLearnerID,
                                                mScopeID);
   
            track.mAttempt = mNumAttempt;
   
            mCurTracking = track;
         }
   
         if ( mCurTracking != null )
         {
            SeqObjectiveTracking obj = 
            ((SeqObjectiveTracking)(mCurTracking.
                                    mObjectives.get(
                                       mCurTracking.mPrimaryObj)));
   
            if ( obj != null )
            {
               String result = null;
   
               result = obj.getObjStatus(iIsRetry);
   
               if ( result.equals(ADLTracking.TRACK_SATISFIED) )
               {
                  status = true;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->  ERROR : No primary objective");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  ERROR : Bad Tracking");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + status);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjSatisfied");
      }

      return status;
   }

   /**
    * Sets the designated objective's experienced duration for the current
    * attempt.
    * 
    * @param iDur   The experienced duration of the current attempt on the
    *               activity.
    */
   /* package */
   void setCurAttemptExDur(ADLDuration iDur)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity --> BEGIN - " +
                            "setCurAttemptExDur");

         if ( iDur != null )
         {
            System.out.println("  ::--> " + 
                               iDur.format(ADLDuration.FORMAT_SCHEMA));
         }
         else
         {
            System.out.println("  ::--> NULL");
         }
      }

      if ( mCurTracking != null )
      {
         mCurTracking.mAttemptAbDur = iDur;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity --> END   - " +
                            "setCurAttemptExDur");
      }
   }

   /**
    * Evaluate all limit conditions defined for the activity.
    *
    * @return <code>true</code> if the evaulation of limit condtions for the
    *         target activity result in that activity becoming disabled,
    *         otherwise <code>false</code>.
    */
   /* package */
   boolean evaluateLimitConditions()
   {

      // This is an implementation of UP.1

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity --> BEGIN - " +
                            "evaluateLimitConditions");
      }

      boolean disabled = false;

      if ( mCurTracking != null )
      {

         // Test max attempts
         if ( mMaxAttemptControl )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Attempt Limit Check");
            }

            if ( mNumAttempt >= mMaxAttempt )
            {
               disabled = true;
            }
         }

         if ( mActivityAbDurControl && !disabled )
         {

            if ( _Debug )
            {
               System.out.println("  ::--> Activity Ab Dur Check");
            }

            if ( mActivityAbDur.compare(mActivityAbDur_track) 
                 != ADLDuration.LT )
            {
               disabled = true;
            }
         }

         if ( mActivityExDurControl && !disabled )
         {

            if ( _Debug )
            {
               System.out.println("  ::--> Activity Ex Dur Check");
            }

            if ( mActivityExDur.compare(mActivityExDur_track) != 
                 ADLDuration.LT )
            {
               disabled = true;
            }
         }

         if ( mAttemptAbDurControl && !disabled )
         {

            if ( _Debug )
            {
               System.out.println("  ::--> Attempt Ab Dur Check");
            }

            if ( mActivityAbDur.compare(mCurTracking.mAttemptAbDur)
                 != ADLDuration.LT )
            {
               disabled = true;
            }
         }

         if ( mAttemptExDurControl && !disabled )
         {

            if ( _Debug )
            {
               System.out.println("  ::--> Attempt Ex Dur Check");
            }

            if ( mActivityExDur.compare(mCurTracking.mAttemptExDur)
                 != ADLDuration.LT )
            {
               disabled = true;
            }
         }

         if ( mBeginTimeControl && !disabled )
         {

            if ( _Debug )
            {
               System.out.println("  ::--> Begin Time Check");
            }

            // -+- TODO -+-

            if ( false )
            {
               disabled = true;
            }
         }

         if ( mEndTimeControl && !disabled )
         {

            if ( _Debug )
            {
               System.out.println("  ::--> End Time Check");
            }

            // -+- TODO -+-

            if ( false )
            {
               disabled = true;
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> Nothing to check");
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity --> END   - " +
                            "evaluateLimitConditions");
      }

      return disabled;

   }

   /**
    * Increment the attempt count for this activity by one.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */ 
   void incrementAttempt()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "incrementAttempt");
         System.out.println("  ::-->  " + mActivityID);
      }

      // Store existing tracking information for historical purposes
      if ( mCurTracking != null )
      {
         if ( mTracking == null )
         {
            mTracking = new Vector();
         }

         mTracking.add(mCurTracking);
      }

      // Create a set of tracking information for the new attempt
      ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);

      mNumAttempt++;
      track.mAttempt = mNumAttempt;

      mCurTracking = track;

      // If this is a cluster, check useCurrent flags
      if ( mActiveChildren != null )
      {
         for ( int i = 0; i < mActiveChildren.size(); i++ )
         {
            SeqActivity temp = (SeqActivity)mActiveChildren.elementAt(i);

            // Flag 'dirty' data if we are supposed to only use 'current attempt
            // status -- Set existing data to 'dirty'.  When a new attempt on a
            // a child activity begins, the new tracking information will be 
            // 'clean'.
            if ( mUseCurObj )
            {
               temp.setDirtyObj();
            }

            if ( mUseCurPro )
            {
               temp.setDirtyPro();
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "incrementAttempt");
      }
   }

   /**
    * Indicates that the current Objective state is invalid due to a new
    * attempt on the activity's parent.
    */
   /* package */
   void setDirtyObj()
   {

      if ( mCurTracking != null )
      {
         mCurTracking.setDirtyObj();
      }

   }

   /**
    * Indicates that the current Progress state is invalid due to a new
    * attempt on the activity's parent.
    */
   /* package */
   void setDirtyPro()
   {
      if ( mCurTracking != null )
      {
         mCurTracking.mDirtyPro = true;
      }
   }

   /**
    * Reset this activity's attempt count.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */ 
   void resetNumAttempt()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "resetNumAttempt");
      }

      // Clear all current and historical tracking information.
      mNumAttempt = 0;
      mCurTracking = null;
      mTracking = null;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "resetNumAttempt");
      }
   }

   /**
    * Retrieve this activity's attempt count.
    * 
    * @return A <code>long</code> value indicating the number attempts on this
    *         activity.
    * 
    * @see <code>SeqActivityTrackingAccess</code>
    */
   /* package */ 
   long getNumAttempt()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getNumAttempt");
      }

      long attempt = 0;

      if ( mIsTracked )
      {
         attempt = mNumAttempt;
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + attempt);
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getNumAttempt");
      }

      return attempt;
   }

   /**
    * Retrieve the set of read or write objective maps for a specifed objective
    * associated with this activity.
    * 
    * @param iObjID Specifies a local objective ID
    * 
    * @param iRead  Specifies if the request is for read or write maps
    * 
    * @return A set of global shared objective IDs that are written to by
    *         the specified objective associated with this activity,
    *         or <code>null</code> if none exisit.
    */
   /* package */ 
   Vector getObjIDs(String iObjID, boolean iRead)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjIDs");
         if ( iObjID != null )
         {
            System.out.println("  ::--> " + iObjID);
         }
         else
         {
            System.out.println("  ::--> NULL");
         }
      }

      // Attempt to find the ID associated with the rolledup objective
      if ( iObjID == null )
      {

         if ( mCurTracking != null )
         {
            iObjID = mCurTracking.mPrimaryObj;
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  :: ERROR :: Unknown Tracking");
            }
         }
      }

      Vector objSet = null;
      Vector mapSet = null;

      if ( mIsTracked )
      {
         if ( mObjMaps != null )
         {
   
            mapSet = (Vector)mObjMaps.get(iObjID);
            if ( mapSet != null )
            {
   
               for ( int i = 0; i < mapSet.size(); i++ )
               {
                  SeqObjectiveMap map = (SeqObjectiveMap)mapSet.elementAt(i);
   
                  if ( !iRead && (map.mWriteStatus || map.mWriteMeasure) )
                  {
                     if ( objSet == null )
                     {
                        objSet = new Vector();
                     }
   
                     objSet.add(map.mGlobalObjID);
                  }
                  else if ( iRead && (map.mReadStatus || map.mReadMeasure) )
                  {
                     if ( objSet == null )
                     {
                        objSet = new Vector();
                     }
   
                     objSet.add(map.mGlobalObjID);
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> No Maps defined for objective");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> No Maps defined for activity");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> NOT TRACKED");
         }
      }


      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - " +
                            "getObjIDs");
      }

      return objSet;
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * This method provides the state this <code>SeqActivity</code> object for
    * diagnostic purposes.
    *
    */
   public void dumpState()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivty   --> BEGIN - dumpState");

         System.out.println("\t  ::--> Depth:         " + mDepth);
         System.out.println("\t  ::--> Count:         " + mCount);
         System.out.println("\t  ::--> Order:         " + mOrder);
         System.out.println("\t  ::--> Selected:      " + mSelected);
         System.out.println("\t  ::--> Active Order:  " + mActiveOrder);
         System.out.println("\t  ::--> Learner:       " + mLearnerID);
         System.out.println("\t  ::--> Activity ID:   " + mActivityID);
         System.out.println("\t  ::--> Resource ID:   " + mResourceID);
         System.out.println("\t  ::--> State ID:      " + mStateID);
         System.out.println("\t  ::--> Title:         " + mTitle);
         System.out.println("\t  ::--> Delivery Mode: " + mDeliveryMode);

         System.out.println("");
         System.out.println("\t  ::--> XML:           " + mXML);
         System.out.println("");

         System.out.println("\t  ::--> Num Attempt :  " + mNumAttempt);

         if ( mCurTracking != null )
         {
            mCurTracking.dumpState();
         }
         else
         {
            System.out.println("\t  ::--> Cur Track   :  NULL ");
         }

         if ( mActivityAbDur_track != null )
         {
            System.out.println("\t  ::--> Act Ab Dur  : " + 
                               mActivityAbDur_track.
                               format(ADLDuration.FORMAT_SECONDS));
         }
         else
         {
            System.out.println("\t  ::--> Act Ab Dur  :  NULL");
         }

         if ( mActivityExDur_track != null )
         {
            System.out.println("\t  ::--> Act Ex Dur  : " + 
                               mActivityExDur_track.
                               format(ADLDuration.FORMAT_SECONDS));
         }
         else
         {
            System.out.println("\t  ::--> Act Ex Dur  :  NULL");
         }


         if ( mTracking != null )
         {
            for ( int i = 0; i < mTracking.size(); i++ )
            {
               System.out.println("");
               ADLTracking track = (ADLTracking)mTracking.elementAt(i);
               track.dumpState();
            }
         }

         System.out.println("\t  ::--> IsActive    :  " + mIsActive);
         System.out.println("\t  ::--> IsSupended  :  " + mIsSuspended);
         System.out.println("\t  ::--> IsVisible   :  " + mIsVisible);

         if ( mParent == null )
         {
            System.out.println("\t  ::--> Parent      :   NULL");
         }
         else
         {
            System.out.println("\t  ::--> Parent      :  " +
                               mParent.getID());
         }

         if ( mChildren == null )
         {
            System.out.println("\t  ::--> Children    :  NULL");
         }
         else
         {
            System.out.println("\t  ::--> Children    :  [" +
                               mChildren.size() + "]");
         }

         if ( mActiveChildren == null )
         {
            System.out.println("\t  ::--> ActChildren :  NULL");
         }
         else
         {
            System.out.println("\t  ::--> ActChildren :  [" +
                               mActiveChildren.size() + "]");
         }

         System.out.println("\t  ::--> Choice      :  " + mControl_choice);
         System.out.println("\t  ::--> Choice Exit :  " + mControl_choiceExit);
         System.out.println("\t  ::--> Flow        :  " + mControl_flow);
         System.out.println("\t  ::--> ForwardOnly :  " + mControl_forwardOnly);
         System.out.println("\t  ::--> Constrain   :  " + 
                            mConstrainChoice);
         System.out.println("\t  ::--> Prevent Act :  " +                       
                            mPreventActivation);
         System.out.println("\t  ::--> Use Cur Obj :  " + mUseCurObj);
         System.out.println("\t  ::--> Use Cur Pro :  " + mUseCurPro);

         if ( mPreConditionRules == null )
         {
            System.out.println("\t  ::--> PRE SeqRules : NULL");
         }
         else
         {
            System.out.println("\t  ::--> PRE SeqRules : [" +
                               mPreConditionRules.size() + "]");
         }


         if ( mExitActionRules == null )
         {
            System.out.println("\t  ::--> EXIT SeqRules: NULL");
         }
         else
         {
            System.out.println("\t  ::--> EXIT SeqRules: [" +
                               mExitActionRules.size() + "]");
         }


         if ( mPostConditionRules == null )
         {
            System.out.println("\t  ::--> POST SeqRules: NULL");
         }
         else
         {
            System.out.println("\t  ::--> POST SeqRules: [" +
                               mPostConditionRules.size() + "]");
         }

         System.out.println("\tCONTROL MaxAttempts :  " + mMaxAttemptControl);
         if ( mMaxAttemptControl )
         {
            System.out.println("\t      ::-->         :  " + mMaxAttempt);
         }

         System.out.println("\tCONTROL Att Ab Dur  :  " + mAttemptAbDurControl);
         if ( mAttemptAbDurControl )
         {
            System.out.println("\t      ::-->         :  " +
                               mAttemptAbDur.
                               format(ADLDuration.FORMAT_SECONDS));
         }

         System.out.println("\tCONTROL Att Ex Dur  :  " + mAttemptExDurControl);
         if ( mAttemptExDurControl )
         {
            System.out.println("\t      ::-->         :  " +
                               mAttemptExDur.
                               format(ADLDuration.FORMAT_SECONDS));
         }

         System.out.println("\tCONTROL Act Ab Dur  :  " 
                            + mActivityAbDurControl);
         if ( mActivityAbDurControl )
         {
            System.out.println("\t      ::-->         :  " +
                               mActivityAbDur.
                               format(ADLDuration.FORMAT_SECONDS));

         }

         System.out.println("\tCONTROL Act Ex Dur  :  "
                            + mActivityExDurControl);
         if ( mActivityExDurControl )
         {
            System.out.println("\t      ::-->         :  " +
                               mActivityExDur.
                               format(ADLDuration.FORMAT_SECONDS));
         }

         System.out.println("\tCONTROL Begin Time  :  " + mBeginTimeControl);
         System.out.println("\t      ::-->         :  " + mBeginTime);
         System.out.println("\tCONTROL End Time    :  " + mEndTimeControl);
         System.out.println("\t      ::-->         :  " + mEndTime);

         if ( mAuxResources != null )
         {
            System.out.println("\t  ::--> Services    :  [ " 
                               + mAuxResources.size() + "]");

            ADLAuxiliaryResource temp = null;

            for ( int i = 0; i < mAuxResources.size(); i++ )
            {
               temp = (ADLAuxiliaryResource)mAuxResources.elementAt(i);

               temp.dumpState();
            }
         }
         else
         {
            System.out.println("\t  ::--> Services    :  NULL");
         }


         if ( mRollupRules == null )
         {
            System.out.println("\t  ::--> RollupRules :  NULL");
         }
         else
         {
            System.out.println("\t  ::--> RollupRules :  [" +
                               mRollupRules.size() + "]");
         }

         System.out.println("\t  ::--> Rollup Satisfied      :  " + 
                            mRequiredForSatisfied);
         System.out.println("\t  ::--> Rollup Not Satisfied  :  " +             
                            mRequiredForNotSatisfied);
         System.out.println("\t  ::--> Rollup Completed      :  " + 
                            mRequiredForCompleted);
         System.out.println("\t  ::--> Rollup Incomplete     :  " +             
                            mRequiredForIncomplete);

         if ( mObjectives == null )
         {
            System.out.println("\t  ::--> Objectives  :  NULL");
         }
         else
         {
            System.out.println("\t  ::--> Objectives  :  [" +
                               mObjectives.size() + "]");

            for ( int i = 0; i < mObjectives.size(); i++ )
            {
               SeqObjective obj = (SeqObjective)mObjectives.elementAt(i);

               obj.dumpState();
            }
         }

         System.out.println("\t  ::--> Rollup Obj     :  " +
                            mIsObjectiveRolledUp);
         System.out.println("\t  ::--> Rollup Weight  :  "
                            + mObjMeasureWeight);
         System.out.println("\t  ::--> Rollup Pro     :  "
                            + mIsProgressRolledUp);


         System.out.println("\t  ::--> Select Time    :  " + mSelectTiming);
         System.out.println("\t CONTROL Select Count  :  " + mSelectStatus);
         System.out.println("\t         ::-->         :  " + mSelectCount);


         System.out.println("\t  ::--> Random Time    :  " + mRandomTiming);
         System.out.println("\t  ::--> Reorder        :  " + mReorder);

         System.out.println("\t  ::--> Is Tracked     :  " + mIsTracked);
         System.out.println("\t  ::--> Cont Sets Obj  :  " + 
                            mContentSetsCompletion);
         System.out.println("\t  ::--> Cont Sets Pro  :  " + mContentSetsObj);

         System.out.println("  :: SeqActivity   --> END   - dumpState");
      }
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
   Package Methods for Managing Tree State
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
   /**
     * Appends one activity to the list of all existing child activities for thi
     * activity.
     * 
     * @param iChild The child activity to add.
     */
   /* package */ 
   void addChild(SeqActivity ioChild)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - addChild");
      }

      if ( mChildren == null )
      {
         mChildren = new Vector();
      }

      // To maintain consistency, adding a child activity will set the active
      // children to the set of all children.
      mActiveChildren = mChildren;

      mChildren.add(ioChild);

      // Tell the child who its parent is and its order in relation to its
      // siblings.
      ioChild.setOrder(mChildren.size() - 1);
      ioChild.setActiveOrder(mChildren.size() - 1);

      ioChild.setParent(this);

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - addChild");
      }
   }

   /**
    * Assigns a set of activites (<code>Vector</code>) to become the child
    * activities for this activity.
    * 
    * @param iChildren The set of activites to become the children.
    * 
    * @param iAll      Indicates if the set of activies is intended to be 'all'
    *                  of this activity's children, or just the 'active' set.
    */
   /* package */ 
   void setChildren(Vector ioChildren, boolean iAll)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setChildren");
         System.out.println("  ::-->  " + iAll);
      }

      SeqActivity walk = null; 

      if ( iAll )
      {
         mChildren = ioChildren;
         mActiveChildren = ioChildren;

         for ( int i = 0; i < ioChildren.size(); i++ )
         {
            walk = (SeqActivity)ioChildren.elementAt(i);

            walk.setOrder(i);
            walk.setActiveOrder(i);
            walk.setParent(this);
            walk.setIsSelected(true);
         }
      }
      else
      {
         for ( int i = 0; i < mChildren.size(); i++ )
         {
            walk = (SeqActivity)mChildren.elementAt(i);

            walk.setIsSelected(false);
         }

         mActiveChildren = ioChildren;

         for ( int i = 0; i < ioChildren.size(); i++ )
         {
            walk = (SeqActivity)ioChildren.elementAt(i);

            walk.setActiveOrder(i);
            walk.setIsSelected(true);
            walk.setParent(this);
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setChildren");
      }
   }

   /**
    * Retrieves the set of child activites for this activity.
    * 
    * @param iAll  Indicates if the set of activies requested is intended to
    *              be 'all' of this activity's children, or just the 'active'
    *              set.
    * 
    * @return The set of child activites (<code>Vector</code> of <code>
    *         SeqActivity</code>), or <code>null</code> if the activity has no
    *         children.
    */
   /* package */ 
   Vector getChildren(boolean iAll)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getChildren");
         System.out.println("  ::-->  " + iAll);
      }

      Vector result = null;

      if ( iAll )
      {
         result = mChildren;
      }
      else
      {
         result = mActiveChildren;
      }

      if ( _Debug )
      {
         if ( result != null )
         {
            System.out.println("  ::-->  [" + result.size() + "]");
         }

         System.out.println("  :: SeqActivity     --> END   - getChildren");
      }

      return result;
   }

   /**
    * Indicates if this activity has children.
    * 
    * @param iAll  Indicates if the set of activies requested is intended to
    *              be 'all' of this activity's children, or just the 'active'
    *              set.
    * 
    * @return <code>true</code> if this activity has children, otherwise
    *         <code>false</code>.
    */
   /* package */ 
   boolean hasChildren(boolean iAll)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - hasChildren"); 
         System.out.println("  ::-->  " + iAll);
      }

      boolean result = false;

      if ( iAll )
      {
         result = mChildren != null;
      }
      else
      {
         result = mActiveChildren != null;
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + result);
         System.out.println("  :: SeqActivity     --> END   - hasChildren");
      }

      return result;
   }

   /**
    * Retreives the next sibling (forward traversal) to this activity.
    * 
    * @param iAll  Indicates if the set of activies requested is intended to
    *              be 'all' of this activity's children, or just the 'active'
    *              set.
    *
    * @return The next sibling (<code>SeqActivity</code>), or <code>null
    *         </code> if none exists.
    */
   /* package */ 
   SeqActivity getNextSibling(boolean iAll)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getNextSibling");
      }

      SeqActivity next = null;
      int target = -1;

      // Make sure this activity has a parent
      if ( mParent != null )
      {
         if ( iAll )
         {
            target = mOrder + 1; 
         }
         else
         {
            target = mActiveOrder + 1;
         }

         // Make sure there is a 'next' sibling
         if ( target < mParent.getChildren(iAll).size() )
         {
            next = (SeqActivity)mParent.getChildren(iAll).elementAt(target);
         }
      }

      if ( _Debug )
      {
         if ( next != null )
         {
            System.out.println("  ::-->  " + next.getID());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }

         System.out.println("  :: SeqActivity     --> END   - getNextSibling");
      }

      return next;
   }

   /**
    * Retreives the previous sibling (reverse traversal) to this activity.
    * 
    * @param iAll  Indicates if the set of activies requested is intended to
    *              be 'all' of this activity's children, or just the 'active'
    *              set.
    *
    * @return The next sibling (<code>SeqActivity</code>), or <code>null
    *         </code> if none exists.
    */
   /* package */ 
   SeqActivity getPrevSibling(boolean iAll)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getPrevSibling");
         if ( iAll )
         {
            System.out.println("  ::-->  " + mOrder);
         }
         else
         {
            System.out.println("  ::-->  " + mActiveOrder);
         }
      }

      SeqActivity prev = null;
      int target = -1;

      // Make sure this activity has a parent
      if ( mParent != null )
      {
         if ( iAll )
         {
            target = mOrder - 1;
         }
         else
         {
            target = mActiveOrder - 1;
         }

         // Make sure there is a 'next' sibling
         if ( target >= 0 )
         {
            prev = (SeqActivity)mParent.getChildren(iAll).elementAt(target);
         }
      }

      if ( _Debug )
      {
         if ( prev != null )
         {
            System.out.println("  ::-->  " + prev.getID());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }

         System.out.println("  :: SeqActivity     --> END   - getPrevSibling");
      }

      return prev;
   }

   /**
    * Retreives the parent activity of this activity.
    * 
    * @return The parent of this activity (<code>SeqActivity</code>),
    *         or <code>null</code> if it is the 'Root'.
    */
   /* package */ 
   SeqActivity getParent()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getParent");

         if ( mParent != null )
         {
            System.out.println("  ::--> " + mParent.getID());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }

         System.out.println("  :: SeqActivity     --> END   - getParent");
      }
      return mParent;
   }

   /**
    * Retreives the ID of the parent activity of this activity.
    * 
    * @return The ID (<code>String</code>) parent activity of this activity,
    *         or <code>null</code> if it is the 'Root'.
    */
   /* package */ 
   String getParentID()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getParentID");
         System.out.println("  :: SeqActivity     --> END   - getParentID");
      }

      if ( mParent != null )
      {
         return mParent.getID();
      }
      else
      {
         return null;
      }
   }

   /**
    * Retrieves the order of this activity in relation to its active siblings.
    * 
    * @return The order of this activity relative to its active siblings.
    */
   /* package */
   int getActiveOrder()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getActiveOrder");

         System.out.println("  ::-->  " + mActiveOrder);
         System.out.println("  :: SeqActivity     --> END   - getActiveOrder");
      }

      return mActiveOrder;
   }

   /**
    * Retrieves the objective status records for the activity.
    * 
    * @return A list (<code>Vector</code>) of <code>ADLObjStatus</code> records.
    */
   /* package */
   Vector getObjStatusSet()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjStatusSet");
      }

      Vector objSet = null;

      if ( mCurTracking != null )
      {
         if ( mCurTracking.mObjectives != null )
         {
            objSet = new Vector();

            Enumeration enum1 = mCurTracking.mObjectives.keys();
            while ( enum1.hasMoreElements() )
            {
               String key = (String)enum1.nextElement();

               // Only include objectives with IDs
               if ( !key.equals("_primary_") )
               {
               
                  if ( _Debug )
                  {
                     System.out.println("  ::--> Getting  -> " + key);
                  }
   
                  SeqObjectiveTracking obj =
                  (SeqObjectiveTracking)mCurTracking.mObjectives.get(key);

                  ADLObjStatus objStatus = new ADLObjStatus();

                  objStatus.mObjID = obj.getObjID();
                  String measure = obj.getObjMeasure(false);

                  objStatus.mHasMeasure =
                  !measure.equals(ADLTracking.TRACK_UNKNOWN);

                  if ( objStatus.mHasMeasure )
                  {
                     objStatus.mMeasure = (new Double(measure)).doubleValue();
                  }

                  objStatus.mStatus = obj.getObjStatus(false);

                  objSet.add(objStatus);
               }
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  --> Attempt has not begun -- no tracking");
         }
      }

      if ( objSet != null )
      {
         if ( objSet.size() == 0 )
         {
            objSet = null;
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - " + 
                            "getObjStatusSet");
      }

      return objSet;

   }

   /**
    * Retrieves the depth of this activity in the activity tree
    * 
    * @return The depth of this activity in the activity tree
    */
   /* package */
   int getDepth()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getDepth");
         System.out.println("  :: SeqActivity     --> END   - getDepth");
      }

      return mDepth;
   }

   /**
    * Sets the depth this activity in the activity tree.
    * 
    * @param iDepth The depth of the activity.
    */
   /* package */
   void setDepth(int iDepth)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setDepth");
         System.out.println("  ::-->  " + iDepth);
      }

      mDepth = iDepth;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setDepth");
      }
   }

   /**
    * Retrieves the count (order) of this activity in the activity tree
    * 
    * @return The count of this activity in the activity tree
    */
   /* package */
   int getCount()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getCount");
         System.out.println("  :: SeqActivity     --> END   - getCount");
      }

      return mCount;
   }

   /**
    * Sets the count (order) of this activity in the activity tree.
    * 
    * @param iCount The depth of the activity.
    */
   /* package */
   void setCount(int iCount)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setCount");
         System.out.println("  ::-->  " + iCount);
      }

      mCount = iCount;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setCount");
      }
   }


   /**
    * Retreives if the activity has already had the Selection Process applied.
    * 
    * @return Has the activity already had the Selection Process applied?
    */
   /* package */
   boolean getSelection()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getSelection");
         System.out.println("  :: SeqActivity     --> END   - getSelection");
      }

      return mSelection;
   }

   /**
    * Indictes if the activity has already had the Selection Process applied.
    * 
    * @param iSelection Has the activity already had the Selection Process
    *                   applied?
    */
   /* package */
   void setSelection(boolean iSelection)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setSelection");
         System.out.println("  ::-->  " + iSelection);
      }

      mSelection = iSelection;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setSelection");
      }
   }


   /**
    * Retreives if the activity has already had the Randomization Process 
    * applied.
    * 
    * @return Has the activity already had the Randomization Process applied?
    */
   /* package */
   boolean getRandomized()
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - getRandomized");
         System.out.println("  :: SeqActivity     --> END   - getRandomized");
      }

      return mRandomized;
   }

   /**
    * Indictes if the activity has already had the Randomization Process
    * applied.
    * 
    * @param iRandomized Has the activity already had the Randomized
    *                    Process applied?
    */
   /* package */
   void setRandomized(boolean iRandomized)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setRandomized");
         System.out.println("  ::-->  " + iRandomized);
      }

      mRandomized = iRandomized;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setRandomized");
      }
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Private Methods 
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Sets the order of this activity in relation to all of its siblings.
    * 
    * @param iOrder The order of this activity relative to its siblings.
    */
   private void setOrder(int iOrder)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setOrder");
         System.out.println("  ::-->  " + iOrder);
      }

      mOrder = iOrder;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setOrder");
      }
   }

   /**
    * Sets the order of this activity in relation to its active siblings.
    * 
    * @param iOrder The order of this activity relative to its active siblings.
    */
   private void setActiveOrder(int iOrder)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setActiveOrder");
         System.out.println("  ::-->  " + iOrder);
      }

      mActiveOrder = iOrder;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setActiveOrder");
      }
   }

   /**
    * Sets the designated activity as the parent of this activity
    * 
    * @param iParent The parent activity of this activity.
    */
   private void setParent(SeqActivity iParent)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> BEGIN - setParent");
      }

      mParent = iParent;

      if ( _Debug )
      {
         System.out.println("  :: SeqActivity     --> END   - setParent");
      }
   }

}  // end SeqActivity
