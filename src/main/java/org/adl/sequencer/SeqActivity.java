// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 2006-05-29 오후 1:01:01
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SeqActivity.java

package org.adl.sequencer;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.adl.util.debug.DebugIndicator;

import org.lcms.api.com.AOFException;

// Referenced classes of package org.adl.sequencer:
//            SeqActivityTrackingAccess, SeqActivityStateAccess, SeqRollupRule, SeqRuleset, 
//            ADLDuration, SeqRollupRuleset, SeqObjective, ADLTracking, 
//            SeqObjectiveTracking, SeqObjectiveMap, ADLAuxiliaryResource, ADLObjStatus

public class SeqActivity extends SeqActivityTrackingAccess
    implements SeqActivityStateAccess, Serializable
{
    public static String TIMING_NEVER = "never";
    public static String TIMING_ONCE = "once";
    public static String TIMING_EACHNEW = "onEachNewAttempt";
    private static String TER_EXITALL = "_EXITALL_";
    private static boolean _Debug;
    private String mXML;
    private int mDepth;
    private int mCount;
    private String mLearnerID;
    private String mScopeID;
    private String mActivityID;
    private String mResourceID;
    private String mStateID;
    private String mTitle;
    private boolean mIsVisible;
    private int mOrder;
    private int mActiveOrder;
    private boolean mSelected;
    private SeqActivity mParent;
    private boolean mIsActive;
    private boolean mIsSuspended;
    private Vector mChildren;
    private Vector mActiveChildren;
    private String mDeliveryMode;
    private boolean mControl_choice;
    private boolean mControl_choiceExit;
    private boolean mControl_flow;
    private boolean mControl_forwardOnly;
    private boolean mConstrainChoice;
    private boolean mPreventActivation;
    private boolean mUseCurObj;
    private boolean mUseCurPro;
    SeqRuleset mPreConditionRules;
    SeqRuleset mPostConditionRules;
    SeqRuleset mExitActionRules;
    private boolean mMaxAttemptControl;
    private long mMaxAttempt;
    private boolean mAttemptAbDurControl;
    private ADLDuration mAttemptAbDur;
    private boolean mAttemptExDurControl;
    private ADLDuration mAttemptExDur;
    private boolean mActivityAbDurControl;
    private ADLDuration mActivityAbDur;
    private boolean mActivityExDurControl;
    private ADLDuration mActivityExDur;
    private boolean mBeginTimeControl;
    private String mBeginTime;
    private boolean mEndTimeControl;
    private String mEndTime;
    private Vector mAuxResources;
    private SeqRollupRuleset mRollupRules;
    private boolean mActiveMeasure;
    private String mRequiredForSatisfied;
    private String mRequiredForNotSatisfied;
    private String mRequiredForCompleted;
    private String mRequiredForIncomplete;
    private Vector mObjectives;
    private Hashtable mObjMaps;
    private boolean mIsObjectiveRolledUp;
    private double mObjMeasureWeight;
    private boolean mIsProgressRolledUp;
    private String mSelectTiming;
    private boolean mSelectStatus;
    private int mSelectCount;
    private boolean mSelection;
    private String mRandomTiming;
    private boolean mReorder;
    private boolean mRandomized;
    private boolean mIsTracked;
    private boolean mContentSetsCompletion;
    private boolean mContentSetsObj;
    private ADLTracking mCurTracking;
    private Vector mTracking;
    private long mNumAttempt;
    private ADLDuration mActivityAbDur_track;
    private ADLDuration mActivityExDur_track;
    private String studyTime;
    private long sysAttempt;
    private String successStatus;

    static 
    {
      _Debug = DebugIndicator.ON;
    }

    public SeqActivity()
    {
        mXML = null;
        mDepth = 0;
        mCount = -1;
        mLearnerID = "_NULL_";
        mScopeID = null;
        mActivityID = null;
        mResourceID = null;
        mStateID = null;
        mTitle = null;
        mIsVisible = true;
        mOrder = -1;
        mActiveOrder = -1;
        mSelected = true;
        mParent = null;
        mIsActive = false;
        mIsSuspended = false;
        mChildren = null;
        mActiveChildren = null;
        mDeliveryMode = "normal";
        mControl_choice = true;
        mControl_choiceExit = true;
        mControl_flow = false;
        mControl_forwardOnly = false;
        mConstrainChoice = false;
        mPreventActivation = false;
        mUseCurObj = true;
        mUseCurPro = true;
        mPreConditionRules = null;
        mPostConditionRules = null;
        mExitActionRules = null;
        mMaxAttemptControl = false;
        mMaxAttempt = 0L;
        mAttemptAbDurControl = false;
        mAttemptAbDur = null;
        mAttemptExDurControl = false;
        mAttemptExDur = null;
        mActivityAbDurControl = false;
        mActivityAbDur = null;
        mActivityExDurControl = false;
        mActivityExDur = null;
        mBeginTimeControl = false;
        mBeginTime = null;
        mEndTimeControl = false;
        mEndTime = null;
        mAuxResources = null;
        mRollupRules = null;
        mActiveMeasure = true;
        mRequiredForSatisfied = SeqRollupRule.ROLLUP_CONSIDER_ALWAYS;
        mRequiredForNotSatisfied = SeqRollupRule.ROLLUP_CONSIDER_ALWAYS;
        mRequiredForCompleted = SeqRollupRule.ROLLUP_CONSIDER_ALWAYS;
        mRequiredForIncomplete = SeqRollupRule.ROLLUP_CONSIDER_ALWAYS;
        mObjectives = null;
        mObjMaps = null;
        mIsObjectiveRolledUp = true;
        mObjMeasureWeight = 1.0D;
        mIsProgressRolledUp = true;
        mSelectTiming = "never";
        mSelectStatus = false;
        mSelectCount = 0;
        mSelection = false;
        mRandomTiming = "never";
        mReorder = false;
        mRandomized = false;
        mIsTracked = true;
        mContentSetsCompletion = false;
        mContentSetsObj = false;
        mCurTracking = null;
        mTracking = null;
        mNumAttempt = 0L;
        mActivityAbDur_track = null;
        mActivityExDur_track = null;
        studyTime = null;
        sysAttempt = 0L;
        successStatus = null;
    }

    public boolean getControlModeChoice()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getChoice");
            System.out.println("  ::-->  " + mControl_choice);
            System.out.println("  :: SeqActivity     --> END   - getChoice");
        }
        return mControl_choice;
    }

    public void setControlModeChoice(boolean iChoice)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setChoice");
            System.out.println("  ::-->  " + iChoice);
        }
        mControl_choice = iChoice;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setChoice");
    }

    public boolean getControlModeChoiceExit()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getChoiceExit");
            System.out.println("  ::-->  " + mControl_choiceExit);
            System.out.println("  :: SeqActivity     --> END   - getChoiceExit");
        }
        return mControl_choiceExit;
    }

    public void setControlModeChoiceExit(boolean iChoiceExit)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setChoiceExit");
            System.out.println("  ::-->  " + iChoiceExit);
        }
        mControl_choiceExit = iChoiceExit;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setChoiceExit");
    }

    public boolean getControlModeFlow()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getFlow");
            System.out.println("  ::-->  " + mControl_flow);
            System.out.println("  :: SeqActivity     --> END   - getFlow");
        }
        return mControl_flow;
    }

    public void setControlModeFlow(boolean iFlow)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setFlow");
            System.out.println("  ::-->  " + iFlow);
        }
        mControl_flow = iFlow;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setFlow");
    }

    public boolean getControlForwardOnly()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getForwardOnly");
            System.out.println("  ::-->  " + mControl_forwardOnly);
            System.out.println("  :: SeqActivity     --> END   - getForwardOnly");
        }
        return mControl_forwardOnly;
    }

    public void setControlForwardOnly(boolean iForwardOnly)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setForwardOnly");
            System.out.println("  ::-->  " + iForwardOnly);
        }
        mControl_forwardOnly = iForwardOnly;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setForwardOnly");
    }

    public boolean getConstrainChoice()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getConstrainChoice");
            System.out.println("  ::-->  " + mConstrainChoice);
            System.out.println("  :: SeqActivity     --> END   - getConstrainChoice");
        }
        return mConstrainChoice;
    }

    public void setConstrainChoice(boolean iConstrainChoice)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setConstrainChoice");
            System.out.println("  ::-->  " + iConstrainChoice);
        }
        mConstrainChoice = iConstrainChoice;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   -  setConstrainChoice");
    }

    public boolean getPreventActivation()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getPreventActivation");
            System.out.println("  ::-->  " + mPreventActivation);
            System.out.println("  :: SeqActivity     --> END   - getPreventActivation");
        }
        return mPreventActivation;
    }

    public void setPreventActivation(boolean iPreventActivation)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setPreventActivation");
            System.out.println("  ::-->  " + iPreventActivation);
        }
        mPreventActivation = iPreventActivation;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setPreventActivation");
    }

    public boolean getUseCurObjective()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getUseCurObjective");
            System.out.println("  ::-->  " + mUseCurObj);
            System.out.println("  :: SeqActivity     --> END   - getUseCurObjective");
        }
        return mUseCurObj;
    }

    public void setUseCurObjective(boolean iUseCur)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setUseCurObjective");
            System.out.println("  ::-->  " + iUseCur);
        }
        mUseCurObj = iUseCur;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END - setUseCurObjective");
    }

    public boolean getUseCurProgress()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getUseCurProgress");
            System.out.println("  ::-->  " + mUseCurPro);
            System.out.println("  :: SeqActivity     --> END   - getUseCurProgress");
        }
        return mUseCurPro;
    }

    public void setUseCurProgress(boolean iUseCur)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setUseCurProgress");
            System.out.println("  ::-->  " + iUseCur);
        }
        mUseCurPro = iUseCur;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END - setUseCurProgress");
    }

    public SeqRuleset getPreSeqRules()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getPreSeqRules");
            System.out.println("  :: SeqActivity     --> END   - getPreSeqRules");
        }
        return mPreConditionRules;
    }

    public void setPreSeqRules(SeqRuleset iRuleSet)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setPreSeqRules");
            if(iRuleSet != null)
                System.out.println("  ::-->  " + iRuleSet.size());
            else
                System.out.println("  ::-->  NULL");
        }
        mPreConditionRules = iRuleSet;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setPreSeqRules");
    }

    public SeqRuleset getExitSeqRules()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getExitSeqRules");
            System.out.println("  :: SeqActivity     --> END   - getExitSeqRules");
        }
        return mExitActionRules;
    }

    public void setExitSeqRules(SeqRuleset iRuleSet)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setExitSeqRules");
            if(iRuleSet != null)
                System.out.println("  ::-->  " + iRuleSet.size());
            else
                System.out.println("  ::-->  NULL");
        }
        mExitActionRules = iRuleSet;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setExitSeqRules");
    }

    public SeqRuleset getPostSeqRules()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getPostSeqRules");
            System.out.println("  :: SeqActivity     --> END   - getPostSeqRules");
        }
        return mPostConditionRules;
    }

    public void setPostSeqRules(SeqRuleset iRuleSet)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setPostSeqRules");
            if(iRuleSet != null)
                System.out.println("  ::-->  " + iRuleSet.size());
            else
                System.out.println("  ::-->  NULL");
        }
        mPostConditionRules = iRuleSet;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setPostSeqRules");
    }

    public boolean getAttemptLimitControl()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getAttemptLimitControl");
            System.out.println("  ::-->  " + mMaxAttemptControl);
            System.out.println("  :: SeqActivity    --> END   - getAttemptLimitControl");
        }
        return mMaxAttemptControl;
    }

    public long getAttemptLimit()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getAttemptLimit");
            System.out.println("  ::-->  " + mMaxAttempt);
            System.out.println("  :: SeqActivity    --> END   - getAttemptLimit");
        }
        return mMaxAttempt;
    }

    public void setAttemptLimit(Long iMaxAttempt)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - setAttemptLimit");
            if(iMaxAttempt != null)
                System.out.println("  ::-->  " + iMaxAttempt.toString());
            else
                System.out.println("  ::-->  NULL");
        }
        if(iMaxAttempt != null)
        {
            long value = iMaxAttempt.longValue();
            if(value > 0L)
            {
                mMaxAttemptControl = true;
                mMaxAttempt = value;
            } else
            {
                mMaxAttemptControl = false;
                mMaxAttempt = -1L;
            }
        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END  - setAttemptLimit");
    }

    public boolean getAttemptAbDurControl()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getAttemptAbDurControl");
            System.out.println("  ::-->  " + mAttemptAbDurControl);
            System.out.println("  :: SeqActivity    --> END   - getAttemptAbDurControl");
        }
        return mAttemptAbDurControl;
    }

    public String getAttemptAbDur()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity    --> BEGIN - getAttemptAbDur");
        String dur = null;
        if(mAttemptAbDur != null)
            dur = mAttemptAbDur.format(1);
        if(_Debug)
        {
            System.out.println("  ::-->  " + dur);
            System.out.println("  :: SeqActivity    --> END   - getAttemptAbDur");
        }
        return dur;
    }

    public void setAttemptAbDur(String iDur)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setAttemptAbDur");
            System.out.println("  ::-->  " + iDur);
        }
        if(iDur != null)
        {
            mAttemptAbDurControl = true;
            mAttemptAbDur = new ADLDuration(1, iDur);
        } else
        {
            mAttemptAbDurControl = false;
            mAttemptAbDur = null;
        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setAttemptAbDur");
    }

    public boolean getAttemptExDurControl()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getAttemptExDurControl");
            System.out.println("  ::-->  " + mAttemptExDurControl);
            System.out.println("  :: SeqActivity    --> END   - getAttemptExDurControl");
        }
        return mAttemptExDurControl;
    }

    public String getAttemptExDur()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity    --> BEGIN - getAttemptExDur");
        String dur = null;
        if(mAttemptExDur != null)
            dur = mAttemptExDur.format(1);
        if(_Debug)
        {
            System.out.println("  ::-->  " + dur);
            System.out.println("  :: SeqActivity    --> END   - getAttemptExDur");
        }
        return dur;
    }

    public void setAttemptExDur(String iDur)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setAttemptExDur");
            System.out.println("  ::-->  " + iDur);
        }
        if(iDur != null)
        {
            mAttemptExDurControl = true;
            mAttemptExDur = new ADLDuration(1, iDur);
        } else
        {
            mAttemptExDurControl = false;
        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setAttemptExDur");
    }

    public boolean getActivityAbDurControl()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getActivityAbDurControl");
            System.out.println("  ::-->  " + mActivityAbDurControl);
            System.out.println("  :: SeqActivity    --> END   - getActivityAbDurControl");
        }
        return mActivityAbDurControl;
    }

    public String getActivityAbDur()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity    --> BEGIN - getActivityAbDur");
        String dur = null;
        if(mActivityAbDur != null)
            dur = mActivityAbDur.format(1);
        if(_Debug)
        {
            System.out.println("  ::-->  " + dur);
            System.out.println("  :: SeqActivity    --> END   - getActivityAbDur");
        }
        return dur;
    }

    public void setActivityAbDur(String iDur)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setActivityAbDur");
            System.out.println("  ::-->  " + iDur);
        }
        if(iDur != null)
        {
            mActivityAbDurControl = true;
            mActivityAbDur = new ADLDuration(1, iDur);
        } else
        {
            mActivityAbDurControl = false;
        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setActivityAbDur");
    }

    public boolean getActivityExDurControl()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getActivityExDurControl");
            System.out.println("  ::-->  " + mActivityExDurControl);
            System.out.println("  :: SeqActivity    --> END   - getActivityExDurControl");
        }
        return mActivityExDurControl;
    }

    public String getActivityExDur()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity    --> BEGIN - getActivityExDur");
        String dur = null;
        if(mActivityExDur != null)
            dur = mActivityExDur.format(1);
        if(_Debug)
        {
            System.out.println("  ::-->  " + dur);
            System.out.println("  :: SeqActivity    --> END   - getActivityExDur");
        }
        return dur;
    }

    public void setActivityExDur(String iDur)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setActivityExDur");
            System.out.println("  ::-->  " + iDur);
        }
        if(iDur != null)
        {
            mActivityExDurControl = true;
            mActivityExDur = new ADLDuration(1, iDur);
        } else
        {
            mActivityExDurControl = false;
        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setActivityExDur");
    }

    public boolean getBeginTimeLimitControl()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getBeginTimeLimitControl");
            System.out.println("  ::-->  " + mBeginTimeControl);
            System.out.println("  :: SeqActivity    --> END   - getBeginTimeLimitControl");
        }
        return mBeginTimeControl;
    }

    public String getBeginTimeLimit()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getBeginTimeLimit");
            System.out.println("  ::-->  " + mBeginTime);
            System.out.println("  :: SeqActivity    --> END   - getBeginTimeLimit");
        }
        return mBeginTime;
    }

    public void setBeginTimeLimit(String iTime)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setBeginTimeLimit");
            System.out.println("  ::-->  " + iTime);
        }
        if(iTime != null)
        {
            mBeginTimeControl = true;
            mBeginTime = iTime;
        } else
        {
            mBeginTimeControl = false;
        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setBeginTimeLimit");
    }

    public boolean getEndTimeLimitControl()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getEndTimeLimitControl");
            System.out.println("  ::-->  " + mEndTimeControl);
            System.out.println("  :: SeqActivity    --> END   - getEndTimeLimitControl");
        }
        return mEndTimeControl;
    }

    public String getEndTimeLimit()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getEndTimeLimit");
            System.out.println("  ::-->  " + mEndTime);
            System.out.println("  :: SeqActivity    --> END   - getEndTimeLimit");
        }
        return mEndTime;
    }

    public void setEndTimeLimit(String iTime)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setEndTimeLimit");
            System.out.println("  ::-->  " + iTime);
        }
        if(iTime != null)
        {
            mEndTimeControl = true;
            mEndTime = iTime;
        } else
        {
            mEndTimeControl = false;
        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setEndTimeLimit");
    }

    public Vector getAuxResources()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getAuxResources");
            System.out.println("  :: SeqActivity     --> END   - getAuxResources");
        }
        return mAuxResources;
    }

    public void setAuxResources(Vector iRes)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setAuxResources");
            if(iRes != null)
                System.out.println("  ::-->  " + iRes.size());
            else
                System.out.println("  ::-->  NULL");
        }
        mAuxResources = iRes;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setAuxResources");
    }

    public SeqRollupRuleset getRollupRules()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getRollupRules");
            System.out.println("  :: SeqActivity     --> END   - getRollupRules");
        }
        return mRollupRules;
    }

    public void setRollupRules(SeqRollupRuleset iRuleSet)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setRollupRules");
            if(iRuleSet != null)
                System.out.println("  ::-->  " + iRuleSet.size());
            else
                System.out.println("  ::-->  NULL");
        }
        mRollupRules = iRuleSet;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setRollupRules");
    }

    public boolean getSatisfactionIfActive()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getSatisfactionIfActive");
            System.out.println("  :: SeqActivity     --> END   - getSatisfactionIfActive");
        }
        return mActiveMeasure;
    }

    public void setSatisfactionIfActive(boolean iActiveMeasure)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setSatisfactionIfActive");
            System.out.println("  ::-->  " + iActiveMeasure);
        }
        mActiveMeasure = iActiveMeasure;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setSatisfactionIfActive");
    }

    public String getRequiredForSatisfied()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getRequiredForSatisfied");
            System.out.println("  :: SeqActivity     --> END   - getRequiredForSatisfied");
        }
        return mRequiredForSatisfied;
    }

    public void setRequiredForSatisfied(String iConsider)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setRequiredForSatisfied");
            System.out.println("  ::-->  " + iConsider);
        }
        mRequiredForSatisfied = iConsider;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setRequiredForSatisfied");
    }

    public String getRequiredForNotSatisfied()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getRequiredForNotSatisfied");
            System.out.println("  :: SeqActivity     --> END   - getRequiredForNotSatisfied");
        }
        return mRequiredForNotSatisfied;
    }

    public void setRequiredForNotSatisfied(String iConsider)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setRequiredForNotSatisfied");
            System.out.println("  ::-->  " + iConsider);
        }
        mRequiredForNotSatisfied = iConsider;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setRequiredForNotSatisfied");
    }

    public String getRequiredForCompleted()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getRequiredForCompleted");
            System.out.println("  :: SeqActivity     --> END   - getRequiredForCompleted");
        }
        return mRequiredForCompleted;
    }

    public void setRequiredForCompleted(String iConsider)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setRequiredForCompleted");
            System.out.println("  ::-->  " + iConsider);
        }
        mRequiredForCompleted = iConsider;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setRequiredForCompleted");
    }

    public String getRequiredForIncomplete()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getRequiredForIncomplete");
            System.out.println("  :: SeqActivity     --> END   - getRequiredForIncomplete");
        }
        return mRequiredForIncomplete;
    }

    public void setRequiredForIncomplete(String iConsider)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setRequiredForIncomplete");
            System.out.println("  ::-->  " + iConsider);
        }
        mRequiredForIncomplete = iConsider;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setRequiredForIncomplete");
    }

    public Vector getObjectives()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getObjectives");
            System.out.println("  :: SeqActivity     --> END   - getObjectives");
        }
        return mObjectives;
    }

    public void setObjectives(Vector iObjs)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setObjectives");
            if(iObjs != null)
                System.out.println("  ::-->  " + iObjs.size());
            else
                System.out.println("  ::-->  NULL");
        }
        mObjectives = iObjs;
        if(iObjs != null)
        {
            for(int i = 0; i < iObjs.size(); i++)
            {
                SeqObjective obj = (SeqObjective)iObjs.elementAt(i);
                if(obj.mMaps != null)
                {
                    if(mObjMaps == null)
                        mObjMaps = new Hashtable();
                    mObjMaps.put(obj.mObjID, obj.mMaps);
                }
            }

        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setObjectives");
    }

    public boolean getIsObjRolledUp()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getIsObjRolledUp");
            System.out.println("  ::-->  " + mIsObjectiveRolledUp);
            System.out.println("  :: SeqActivity     --> END   - getIsObjRolledUp");
        }
        return mIsObjectiveRolledUp;
    }

    public void setIsObjRolledUp(boolean iRolledup)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setIsObjRolledUp");
            System.out.println("  ::-->  " + iRolledup);
        }
        mIsObjectiveRolledUp = iRolledup;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setIsObjRolledUp");
    }

    public double getObjMeasureWeight()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getObjMeasureWeight");
            System.out.println("  ::-->  " + mObjMeasureWeight);
            System.out.println("  :: SeqActivity     --> END   - getObjMeasureWeight");
        }
        return mObjMeasureWeight;
    }

    public void setObjMeasureWeight(double iWeight)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setObjMeasureWeight");
            System.out.println("  ::-->  " + iWeight);
        }
        mObjMeasureWeight = iWeight;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setObjMeasureWeight");
    }

    public boolean getIsProgressRolledUp()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getIsProgressRolledUp");
            System.out.println("  ::-->  " + mIsProgressRolledUp);
            System.out.println("  :: SeqActivity     --> END   - getIsProgressRolledUp");
        }
        return mIsProgressRolledUp;
    }

    public void setIsProgressRolledUp(boolean iRolledup)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setIsProgressRolledUp");
            System.out.println("  ::-->  " + iRolledup);
        }
        mIsProgressRolledUp = iRolledup;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setIsProgressRolledUp");
    }

    public void setSelectionTiming(String iTiming)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - setSelectionTiming");
            System.out.println("  ::-->  " + iTiming);
        }
        if(!iTiming.equals(TIMING_NEVER) && !iTiming.equals(TIMING_ONCE) && !iTiming.equals(TIMING_EACHNEW))
            mSelectTiming = TIMING_NEVER;
        else
            mSelectTiming = iTiming;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END  - setSelectionTiming");
    }

    public String getSelectionTiming()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getSelectionTiming");
            System.out.println("  ::-->  " + mSelectTiming);
            System.out.println("  :: SeqActivity    --> END   - getSelectionTiming");
        }
        return mSelectTiming;
    }

    public boolean getSelectStatus()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getSelectStatus");
            System.out.println("  ::-->  " + mSelectStatus);
            System.out.println("  :: SeqActivity     --> END   - getSelectStatus");
        }
        return mSelectStatus;
    }

    public int getSelectCount()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getSelectCount");
        if(mChildren != null)
        {
            if(mSelectCount >= mChildren.size())
            {
                mSelectTiming = "never";
                mSelectCount = mChildren.size();
            }
        } else
        {
            mSelectStatus = false;
            mSelectCount = 0;
        }
        if(_Debug)
        {
            System.out.println("  ::-->  " + mSelectCount);
            System.out.println("  :: SeqActivity     --> END   - getSelectCount");
        }
        return mSelectCount;
    }

    public void setSelectCount(int iCount)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - setSelectCount");
            System.out.println("  ::-->  " + iCount);
        }
        if(iCount >= 0)
        {
            mSelectStatus = true;
            mSelectCount = iCount;
        } else
        {
            mSelectStatus = false;
        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END  - setSelectCount");
    }

    public void setRandomTiming(String iTiming)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - setRandomTiming");
            System.out.println("  ::-->  " + iTiming);
        }
        if(!iTiming.equals(TIMING_NEVER) && !iTiming.equals(TIMING_ONCE) && !iTiming.equals(TIMING_EACHNEW))
            mSelectTiming = TIMING_NEVER;
        else
            mRandomTiming = iTiming;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END  - setRandomTiming");
    }

    public String getRandomTiming()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getRandomTiming");
            System.out.println("  ::-->  " + mRandomTiming);
            System.out.println("  :: SeqActivity    --> END   - getRandomTiming");
        }
        return mRandomTiming;
    }

    public boolean getReorderChildren()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getReorderChildren");
            System.out.println("  ::-->  " + mReorder);
            System.out.println("  :: SeqActivity    --> END   - getReorderChildren");
        }
        return mReorder;
    }

    public void setReorderChildren(boolean iReorder)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - setReorderChildren");
            System.out.println("  ::-->  " + iReorder);
        }
        mReorder = iReorder;
        if(_Debug)
            System.out.println("  :: SeqActivity    --> END   - setReorderChildren");
    }

    public boolean getIsTracked()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getIsTracked");
            System.out.println("  ::-->  " + mIsTracked);
            System.out.println("  :: SeqActivity    --> END   - getIsTracked");
        }
        return mIsTracked;
    }

    public void setIsTracked(boolean iTracked)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setIsTracked");
            System.out.println("  ::-->  " + iTracked);
        }
        mIsTracked = iTracked;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setIsTracked");
    }

    public boolean getSetCompletion()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getSetCompletion");
            System.out.println("  ::-->  " + mContentSetsCompletion);
            System.out.println("  :: SeqActivity    --> END   - getSetCompletion");
        }
        return mContentSetsCompletion;
    }

    public void setSetCompletion(boolean iSet)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setSetCompletion");
            System.out.println("  ::-->  " + iSet);
        }
        mContentSetsCompletion = iSet;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setSetCompletion");
    }

    public boolean getSetObjective()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getSetObjective");
            System.out.println("  ::-->  " + mContentSetsObj);
            System.out.println("  :: SeqActivity    --> END   - getSetObjective");
        }
        return mContentSetsObj;
    }

    public void setSetObjective(boolean iSet)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setSetObjective");
            System.out.println("  ::-->  " + iSet);
        }
        mContentSetsObj = iSet;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setSetObjective");
    }

    public String getDeliveryMode()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity    --> BEGIN - getDeliveryMode");
            System.out.println("  ::-->  " + mDeliveryMode);
            System.out.println("  :: SeqActivity    --> END   - getDeliveryMode");
        }
        return mDeliveryMode;
    }

    public void setDeliveryMode(String iDeliveryMode)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setDeliveryMode");
            System.out.println("  ::-->  " + iDeliveryMode);
        }
        if(iDeliveryMode.equals("browse") || iDeliveryMode.equals("review") || iDeliveryMode.equals("normal"))
            mDeliveryMode = iDeliveryMode;
        else
            mDeliveryMode = "normal";
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setDeliveryMode");
    }

    public String getResourceID()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getResourceID");
            System.out.println("  ::-->  " + mResourceID);
            System.out.println("  :: SeqActivity     --> END   - getResourceID");
        }
        return mResourceID;
    }

    public void setResourceID(String iResourceID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setResourceID");
            System.out.println("  ::--> Resource ID     : " + iResourceID);
        }
        mResourceID = iResourceID;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setResourceID");
    }

    public String getStateID()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getStateID");
        if(_Debug)
        {
            System.out.println("  ::-->  " + mStateID);
            System.out.println("  :: SeqActivity     --> END   - getStateID");
        }
        return mStateID;
    }

    public void setStateID(String iStateID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setStateID");
            System.out.println("  ::--> State ID     : " + iStateID);
        }
        mStateID = iStateID;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setStateID");
    }

    public String getID()
    {
        return mActivityID;
    }

    public void setID(String iID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setID");
            System.out.println("  ::-->  " + iID);
        }
        mActivityID = iID;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setID");
    }

    public String getTitle()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getTitle");
            System.out.println("  ::-->  " + mTitle);
            System.out.println("  :: SeqActivity     --> END   - getTitle");
        }
        return mTitle;
    }

    public void setTitle(String iTitle)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setTitle");
            System.out.println("  ::-->  " + iTitle);
        }
        mTitle = iTitle;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setTitle");
    }

    public void setXMLFragment(String iXML)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setXMLFragment");
            System.out.println("  ::-->  " + iXML);
        }
        mXML = iXML;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setXMLFragment");
    }

    public String getXMLFragment()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getXMLFragment");
            System.out.println("  ::-->  " + mXML);
            System.out.println("  :: SeqActivity     --> END   - getXMLFragment");
        }
        return mXML;
    }

    public void setLearnerID(String iLearnerID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setLearnerID");
            System.out.println("  ::-->  " + iLearnerID);
        }
        mLearnerID = iLearnerID;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setLearnerID");
    }

    public String getLearnerID()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getLearnerID");
            System.out.println("  ::-->  " + mLearnerID);
            System.out.println("  :: SeqActivity     --> END   - getLearnerID");
        }
        return mLearnerID;
    }

    public boolean getIsSelected()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getSelected");
        if(_Debug)
        {
            System.out.println("  ::-->  " + mSelected);
            System.out.println("  :: SeqActivity     --> END   - getSelected");
        }
        return mSelected;
    }

    public void setIsSelected(boolean iSelected)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setSelected");
            System.out.println("  ::--> State ID     : " + iSelected);
        }
        mSelected = iSelected;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setSelected");
    }

    public void setScopeID(String iScopeID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setScopeID");
            System.out.println("  ::-->  " + iScopeID);
        }
        mScopeID = iScopeID;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setScopeID");
    }

    public String getScopeID()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getScopeID");
            System.out.println("  ::-->  " + mScopeID);
            System.out.println("  :: SeqActivity     --> END   - getScopeID");
        }
        return mScopeID;
    }

    public boolean getIsVisible()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getIsVisible");
            System.out.println("  ::-->  " + mIsVisible);
            System.out.println("  :: SeqActivity     --> END   - getIsVisible");
        }
        return mIsVisible;
    }

    public void setIsVisible(boolean iIsVisible)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setIsVisible");
            System.out.println("  ::-->  " + iIsVisible);
        }
        mIsVisible = iIsVisible;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setIsVisible");
    }

    public boolean getIsActive()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getIsActive");
            System.out.println("  ::-->  " + mIsActive);
            System.out.println("  :: SeqActivity     --> END   - getIsActive");
        }
        return mIsActive;
    }

    public void setIsActive(boolean iActive)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setIsActive");
            System.out.println("  ::-->  " + iActive);
        }
        mIsActive = iActive;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setIsActive");
    }

    public boolean getIsSuspended()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getIsSuspended");
            System.out.println("  ::-->  " + mIsSuspended);
            System.out.println("  :: SeqActivity     --> END   - getIsSuspended");
        }
        return mIsSuspended;
    }

    public void setIsSuspended(boolean iSuspended)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setIsSuspended");
            System.out.println("  ::-->  " + iSuspended);
        }
        mIsSuspended = iSuspended;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setIsSuspended");
    }

    boolean getActivityAttempted()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getActivityAttempted");
            System.out.println("  ::-->  " + (mNumAttempt != 0L ? "Attempted" : "NotAttempted"));
            System.out.println("  :: SeqActivity     --> END   - getActivityAttempted");
        }
        return mNumAttempt != 0L;
    }

    boolean getAttemptCompleted(boolean iIsRetry)
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getAttemptCompleted");
        String progress = ADLTracking.TRACK_UNKNOWN;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(!mCurTracking.mDirtyPro && !iIsRetry)
            progress = mCurTracking.mProgress;
        if(_Debug)
        {
            System.out.println("  ::--> " + progress);
            System.out.println("  :: SeqActivity     --> END   - getAttemptCompleted");
        }
        return progress.equals(ADLTracking.TRACK_COMPLETED);
    }

    boolean setProgress(String iProgress)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setProgress");
            System.out.println("  ::-->  " + iProgress);
        }
        boolean statusChange = false;
        if((iProgress.equals(ADLTracking.TRACK_UNKNOWN) || iProgress.equals(ADLTracking.TRACK_COMPLETED) || iProgress.equals(ADLTracking.TRACK_INCOMPLETE)) && mCurTracking != null)
        {
            String prev = mCurTracking.mProgress;
            mCurTracking.mProgress = iProgress;
            statusChange = !prev.equals(iProgress);
        }
        if(_Debug)
        {
            System.out.println("  ::-->  " + statusChange);
            System.out.println("  :: SeqActivity     --> END   - setProgress");
        }
        return statusChange;
    }

    boolean getProgressStatus(boolean iIsRetry)
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getProgressStatus");
        boolean status = false;
        if(mCurTracking != null && !mCurTracking.mDirtyPro && !iIsRetry)
            status = !mCurTracking.mProgress.equals(ADLTracking.TRACK_UNKNOWN);
        if(_Debug)
        {
            System.out.println("  ::--> " + status);
            System.out.println("  :: SeqActivity     --> END   - getProgressStatus");
        }
        return status;
    }

    boolean getObjMeasureStatus(String iObjID, boolean iIsRetry)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getObjMeasureStatus");
            System.out.println("  ::-->  " + iObjID);
        }
        boolean status = false;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(iObjID == null)
            status = getObjMeasureStatus(iIsRetry);
        else
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(iObjID);
            if(obj != null)
            {
                String result = null;
                try {
					result = obj.getObjMeasure(iIsRetry);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if(!result.equals(ADLTracking.TRACK_UNKNOWN))
                    status = true;
            } else
            if(_Debug)
                System.out.println("  ::-->  Objective undefined");
        } else
        if(_Debug)
            System.out.println("  ::-->  ERROR : Bad Tracking");
        if(_Debug)
        {
            System.out.println("  ::-->  " + status);
            System.out.println("  :: SeqActivity     --> END   - getObjMeasureStatus");
        }
        return status;
    }

    boolean getObjMeasureStatus(boolean iIsRetry)
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getObjMeasureStatus");
        boolean status = false;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(mCurTracking.mPrimaryObj);
            if(obj != null)
            {
                String result = null;
                try {
					result = obj.getObjMeasure(iIsRetry);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if(!result.equals(ADLTracking.TRACK_UNKNOWN))
                    status = true;
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        } else
        if(_Debug)
            System.out.println("  ::-->  ERROR : Bad Tracking");
        if(_Debug)
        {
            System.out.println("  ::-->  " + status);
            System.out.println("  :: SeqActivity     --> END   - getObjMeasureStatus");
        }
        return status;
    }

    boolean clearObjMeasure()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - clearObjMeasure");
        boolean statusChange = false;
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(mCurTracking.mPrimaryObj);
            if(obj != null)
            {
                SeqObjective objD = obj.getObj();
                boolean affectSatisfaction = objD.mSatisfiedByMeasure;
                if(affectSatisfaction)
                    affectSatisfaction = !objD.mContributesToRollup || mActiveMeasure || !mIsActive;
                try {
					statusChange = obj.clearObjMeasure(affectSatisfaction);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        }
        if(_Debug)
        {
            System.out.println("  ::--> " + statusChange);
            System.out.println("  :: SeqActivity     --> END   - clearObjMeasure");
        }
        return statusChange;
    }

    boolean clearObjMeasure(String iObjID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - clearObjMeasure");
            System.out.println("  ::--> " + iObjID);
        }
        boolean statusChange = false;
        if(iObjID == null)
            statusChange = clearObjMeasure();
        else
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(iObjID);
            if(obj != null)
            {
                SeqObjective objD = obj.getObj();
                boolean affectSatisfaction = objD.mSatisfiedByMeasure;
                if(affectSatisfaction)
                    affectSatisfaction = !objD.mContributesToRollup || mActiveMeasure || !mIsActive;
                try {
					statusChange = obj.clearObjMeasure(affectSatisfaction);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else
            if(_Debug)
                System.out.println("  ::-->  Objective Undefined");
        }
        if(_Debug)
        {
            System.out.println("  ::--> " + statusChange);
            System.out.println("  :: SeqActivity     --> END   - clearObjMeasure");
        }
        return statusChange;
    }

    boolean setObjMeasure(double iMeasure)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setObjMeasure");
            System.out.println("  ::--> " + iMeasure);
        }
        boolean statusChange = false;
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(mCurTracking.mPrimaryObj);
            if(obj != null)
            {
                String prev = null;
				try {
					prev = obj.getObjStatus(false);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                SeqObjective objD = obj.getObj();
                boolean affectSatisfaction = objD.mSatisfiedByMeasure;
                if(affectSatisfaction)
                    affectSatisfaction = !objD.mContributesToRollup || mActiveMeasure || !mIsActive;
                try {
					obj.setObjMeasure(iMeasure, affectSatisfaction);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                try {
					statusChange = !prev.equals(obj.getObjStatus(false));
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        }
        if(_Debug)
        {
            System.out.println("  ::-->  " + statusChange);
            System.out.println("  :: SeqActivity     --> END   - setObjMeasure");
        }
        return statusChange;
    }

    boolean setObjMeasure(String iObjID, double iMeasure)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setObjMeasure");
            System.out.println("  ::--> " + iObjID);
            System.out.println("  ::--> " + iMeasure);
        }
        boolean statusChange = false;
        if(iObjID == null)
            statusChange = setObjMeasure(iMeasure);
        else
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(iObjID);
            if(obj != null)
            {
                String prev = null;
				try {
					prev = obj.getObjStatus(false);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                SeqObjective objD = obj.getObj();
                boolean affectSatisfaction = objD.mSatisfiedByMeasure;
                if(affectSatisfaction)
                    affectSatisfaction = !objD.mContributesToRollup || mActiveMeasure || !mIsActive;
                try {
					obj.setObjMeasure(iMeasure, affectSatisfaction);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                try {
					statusChange = !prev.equals(obj.getObjStatus(false));
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        }
        if(_Debug)
        {
            System.out.println("  ::-->  " + statusChange);
            System.out.println("  :: SeqActivity     --> END   - setObjMeasure");
        }
        return statusChange;
    }

    boolean getObjSatisfiedByMeasure()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getObjSatisfiedByMeasure");
        boolean byMeasure = false;
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(mCurTracking.mPrimaryObj);
            if(obj != null)
                byMeasure = obj.getByMeasure();
            else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        }
        if(_Debug)
        {
            System.out.println("  ::-->  " + byMeasure);
            System.out.println("  :: SeqActivity     --> END   - getObjSatisfiedByMeasure");
        }
        return byMeasure;
    }

    double getObjMinMeasure(String iObjID)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getObjMinMeasure");
            System.out.println("  ::--> " + iObjID);
        }
        double minMeasure = -1D;
        if(mObjectives != null)
        {
            for(int i = 0; i < mObjectives.size(); i++)
            {
                SeqObjective obj = (SeqObjective)mObjectives.elementAt(i);
                if(iObjID.equals(obj.mObjID))
                    minMeasure = obj.mMinMeasure;
            }

        }
        if(_Debug)
        {
            System.out.println("  ::-->  " + minMeasure);
            System.out.println("  :: SeqActivity     --> END   - getObjMinMeasure");
        }
        return minMeasure;
    }

    double getObjMinMeasure()
    {
        if(mCurTracking != null)
            return getObjMinMeasure(mCurTracking.mPrimaryObj);
        else
            return -1D;
    }

    double getObjMeasure(String iObjID, boolean iIsRetry)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getObjMeasure");
            System.out.println("  ::--> " + iObjID);
        }
        double measure = 0.0D;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(iObjID == null)
            measure = getObjMeasure(iIsRetry);
        else
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(iObjID);
            if(obj != null)
            {
                String result = null;
                try {
					result = obj.getObjMeasure(iIsRetry);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if(!result.equals(ADLTracking.TRACK_UNKNOWN))
                    measure = (new Double(result)).doubleValue();
            } else
            if(_Debug)
                System.out.println("  ::-->  Objective undefined");
        } else
        if(_Debug)
            System.out.println("  ::-->  ERROR : Bad Tracking");
        if(_Debug)
        {
            System.out.println("  ::-->  " + measure);
            System.out.println("  :: SeqActivity     --> END   - getObjMeasure");
        }
        return measure;
    }

    double getObjMeasure(boolean iIsRetry)
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getObjMeasure");
        double measure = 0.0D;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(mCurTracking.mPrimaryObj);
            if(obj != null)
            {
                String result = null;
                try {
					result = obj.getObjMeasure(iIsRetry);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if(!result.equals(ADLTracking.TRACK_UNKNOWN))
                    measure = (new Double(result)).doubleValue();
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        } else
        if(_Debug)
            System.out.println("  ::-->  ERROR : Bad Tracking");
        if(_Debug)
        {
            System.out.println("  ::-->  " + measure);
            System.out.println("  :: SeqActivity     --> END   - getObjMeasure");
        }
        return measure;
    }

    void triggerObjMeasure()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - triggerObjMeasure");
        double measure = 0.0D;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(mCurTracking.mPrimaryObj);
            if(obj != null)
            {
                if(obj.getObj().mSatisfiedByMeasure)
                {
                    String result = null;
                    try {
						result = obj.getObjMeasure(false);
					} catch (AOFException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    if(!result.equals(ADLTracking.TRACK_UNKNOWN))
                    {
                        measure = (new Double(result)).doubleValue();
                        try {
							obj.setObjMeasure(measure, true);
						} catch (AOFException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    } else
                    {
                        try {
							obj.clearObjMeasure(true);
						} catch (AOFException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                } else
                if(_Debug)
                    System.out.println("  ::--> Satisfaction not affected by measure");
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        } else
        if(_Debug)
            System.out.println("  ::-->  ERROR : Bad Tracking");
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - triggerObjMeasure");
    }

    boolean getObjStatus(String iObjID, boolean iIsRetry)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getObjStatus");
            System.out.println("  ::--> " + iObjID);
        }
        boolean status = false;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(iObjID == null)
            status = getObjStatus(iIsRetry);
        else
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(iObjID);
            if(obj != null)
            {
                String result = null;
                try {
					result = obj.getObjStatus(iIsRetry);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if(!result.equals(ADLTracking.TRACK_UNKNOWN))
                    status = true;
            } else
            if(_Debug)
                System.out.println("  ::-->  Objective not defined");
        } else
        if(_Debug)
            System.out.println("  ::-->  ERROR : Bad Tracking");
        if(_Debug)
        {
            System.out.println("  ::-->  " + status);
            System.out.println("  :: SeqActivity     --> END   - getObjStatus");
        }
        return status;
    }

    boolean getObjStatus(boolean iIsRetry)
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getObjStatus");
        boolean status = false;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(mCurTracking.mPrimaryObj);
            if(obj != null)
            {
                String result = null;
                try {
					result = obj.getObjStatus(iIsRetry);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if(!result.equals(ADLTracking.TRACK_UNKNOWN))
                    status = true;
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        } else
        if(_Debug)
            System.out.println("  ::-->  ERROR : Bad Tracking");
        if(_Debug)
        {
            System.out.println("  ::-->  " + status);
            System.out.println("  :: SeqActivity     --> END   - getObjStatus");
        }
        return status;
    }

    boolean setObjSatisfied(String iObjID, String iStatus)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setObjSatisfied");
            System.out.println("  ::--> " + iObjID);
            System.out.println("  ::--> " + iStatus);
        }
        boolean statusChange = false;
        if(iObjID == null)
            statusChange = setObjSatisfied(iStatus);
        else
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(iObjID);
            if(obj != null)
            {
                if(iStatus.equals(ADLTracking.TRACK_UNKNOWN) || iStatus.equals(ADLTracking.TRACK_SATISFIED) || iStatus.equals(ADLTracking.TRACK_NOTSATISFIED))
                {
                    String result = null;
					try {
						result = obj.getObjStatus(false);
						obj.setObjStatus(iStatus);
					} catch (AOFException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    statusChange = !result.equals(iStatus);
                } else
                if(_Debug)
                    System.out.println("  ::--> Invalid status value");
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        }
        if(_Debug)
        {
            System.out.println("  ::-->  " + statusChange);
            System.out.println("  :: SeqActivity     --> END   - setObjSatisfied");
        }
        return statusChange;
    }

    boolean setObjSatisfied(String iStatus)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setObjSatisfied");
            System.out.println("  ::--> " + iStatus);
        }
        boolean statusChange = false;
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(mCurTracking.mPrimaryObj);
            if(obj != null)
            {
                if(iStatus.equals(ADLTracking.TRACK_UNKNOWN) || iStatus.equals(ADLTracking.TRACK_SATISFIED) || iStatus.equals(ADLTracking.TRACK_NOTSATISFIED))
                {
                    String result = null;
					try {
						result = obj.getObjStatus(false);
	                    obj.setObjStatus(iStatus);
					} catch (AOFException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    statusChange = !result.equals(iStatus);
                } else
                if(_Debug)
                    System.out.println("  ::--> Invalid status value");
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        }
        if(_Debug)
        {
            System.out.println("  ::-->  " + statusChange);
            System.out.println("  :: SeqActivity     --> END   - setObjSatisfied");
        }
        return statusChange;
    }

    boolean getObjSatisfied(String iObjID, boolean iIsRetry)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getObjSatisfied");
            System.out.println("  ::--> " + iObjID);
        }
        boolean status = false;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(iObjID == null)
            status = getObjSatisfied(iIsRetry);
        else
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(iObjID);
            if(obj != null)
            {
                String result = null;
                try {
					result = obj.getObjStatus(iIsRetry);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if(result.equals(ADLTracking.TRACK_SATISFIED))
                    status = true;
            } else
            if(_Debug)
                System.out.println("  ::-->  Objective not defined");
        } else
        if(_Debug)
            System.out.println("  ::-->  ERROR : Bad Tracking");
        if(_Debug)
        {
            System.out.println("  ::-->  " + status);
            System.out.println("  :: SeqActivity     --> END   - getObjSatisfied");
        }
        return status;
    }

    boolean getObjSatisfied(boolean iIsRetry)
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getObjSatisfied");
        boolean status = false;
        if(mCurTracking == null)
        {
            ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
            track.mAttempt = mNumAttempt;
            mCurTracking = track;
        }
        if(mCurTracking != null)
        {
            SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(mCurTracking.mPrimaryObj);
            if(obj != null)
            {
                String result = null;
                try {
					result = obj.getObjStatus(iIsRetry);
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if(result.equals(ADLTracking.TRACK_SATISFIED))
                    status = true;
            } else
            if(_Debug)
                System.out.println("  ::-->  ERROR : No primary objective");
        } else
        if(_Debug)
            System.out.println("  ::-->  ERROR : Bad Tracking");
        if(_Debug)
        {
            System.out.println("  ::-->  " + status);
            System.out.println("  :: SeqActivity     --> END   - getObjSatisfied");
        }
        return status;
    }

    void setCurAttemptExDur(ADLDuration iDur)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity --> BEGIN - setCurAttemptExDur");
            if(iDur != null)
                System.out.println("  ::--> " + iDur.format(1));
            else
                System.out.println("  ::--> NULL");
        }
        if(mCurTracking != null)
            mCurTracking.mAttemptAbDur = iDur;
        if(_Debug)
            System.out.println("  :: SeqActivity --> END   - setCurAttemptExDur");
    }

    boolean evaluateLimitConditions()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity --> BEGIN - evaluateLimitConditions");
        boolean disabled = false;
        if(mCurTracking != null)
        {
            if(mMaxAttemptControl)
            {
                if(_Debug)
                    System.out.println("  ::--> Attempt Limit Check");
                if(mNumAttempt >= mMaxAttempt)
                    disabled = true;
            }
            if(mActivityAbDurControl && !disabled)
            {
                if(_Debug)
                    System.out.println("  ::--> Activity Ab Dur Check");
                if(mActivityAbDur.compare(mActivityAbDur_track) != -1)
                    disabled = true;
            }
            if(mActivityExDurControl && !disabled)
            {
                if(_Debug)
                    System.out.println("  ::--> Activity Ex Dur Check");
                if(mActivityExDur.compare(mActivityExDur_track) != -1)
                    disabled = true;
            }
            if(mAttemptAbDurControl && !disabled)
            {
                if(_Debug)
                    System.out.println("  ::--> Attempt Ab Dur Check");
                if(mActivityAbDur.compare(mCurTracking.mAttemptAbDur) != -1)
                    disabled = true;
            }
            if(mAttemptExDurControl && !disabled)
            {
                if(_Debug)
                    System.out.println("  ::--> Attempt Ex Dur Check");
                if(mActivityExDur.compare(mCurTracking.mAttemptExDur) != -1)
                    disabled = true;
            }
            if(mBeginTimeControl && !disabled && _Debug)
                System.out.println("  ::--> Begin Time Check");
            if(mEndTimeControl && !disabled && _Debug)
                System.out.println("  ::--> End Time Check");
        } else
        if(_Debug)
            System.out.println("  ::--> Nothing to check");
        if(_Debug)
            System.out.println("  :: SeqActivity --> END   - evaluateLimitConditions");
        return disabled;
    }

    void incrementAttempt()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - incrementAttempt");
            System.out.println("  ::-->  " + mActivityID);
        }
        if(mCurTracking != null)
        {
            if(mTracking == null)
                mTracking = new Vector();
            mTracking.add(mCurTracking);
        }
        ADLTracking track = new ADLTracking(mObjectives, mLearnerID, mScopeID);
        mNumAttempt++;
        track.mAttempt = mNumAttempt;
        mCurTracking = track;
        if(mActiveChildren != null)
        {
            for(int i = 0; i < mActiveChildren.size(); i++)
            {
                SeqActivity temp = (SeqActivity)mActiveChildren.elementAt(i);
                if(mUseCurObj)
                    temp.setDirtyObj();
                if(mUseCurPro)
                    temp.setDirtyPro();
            }

        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - incrementAttempt");
    }

    void setDirtyObj()
    {
        if(mCurTracking != null)
            mCurTracking.setDirtyObj();
    }

    void setDirtyPro()
    {
        if(mCurTracking != null)
            mCurTracking.mDirtyPro = true;
    }

    void resetNumAttempt()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - resetNumAttempt");
        mNumAttempt = 0L;
        mCurTracking = null;
        mTracking = null;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - resetNumAttempt");
    }

    long getNumAttempt()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getNumAttempt");
            System.out.println("  ::--> " + mNumAttempt);
            System.out.println("  :: SeqActivity     --> END   - getNumAttempt");
        }
        return mNumAttempt;
    }

    Vector getObjIDs(String iObjID, boolean iRead)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getObjIDs");
            if(iObjID != null)
                System.out.println("  ::--> " + iObjID);
            else
                System.out.println("  ::--> NULL");
        }
        if(iObjID == null)
            if(mCurTracking != null)
                iObjID = mCurTracking.mPrimaryObj;
            else
            if(_Debug)
                System.out.println("  :: ERROR :: Unknown Tracking");
        Vector objSet = null;
        Vector mapSet = null;
        if(mObjMaps != null)
        {
            mapSet = (Vector)mObjMaps.get(iObjID);
            if(mapSet != null)
            {
                for(int i = 0; i < mapSet.size(); i++)
                {
                    SeqObjectiveMap map = (SeqObjectiveMap)mapSet.elementAt(i);
                    if(!iRead && (map.mWriteStatus || map.mWriteMeasure))
                    {
                        if(objSet == null)
                            objSet = new Vector();
                        objSet.add(map.mGlobalObjID);
                    } else
                    if(iRead && (map.mReadStatus || map.mReadMeasure))
                    {
                        if(objSet == null)
                            objSet = new Vector();
                        objSet.add(map.mGlobalObjID);
                    }
                }

            } else
            if(_Debug)
                System.out.println("  ::--> No Maps defined for objective");
        } else
        if(_Debug)
            System.out.println("  ::--> No Maps defined for activity");
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - getObjIDs");
        return objSet;
    }

    public void dumpState()
    {
        if(_Debug)
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
            if(mCurTracking != null)
				try {
					mCurTracking.dumpState();
				} catch (AOFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
                System.out.println("\t  ::--> Cur Track   :  NULL ");
            if(mActivityAbDur_track != null)
                System.out.println("\t  ::--> Act Ab Dur  : " + mActivityAbDur_track.format(0));
            else
                System.out.println("\t  ::--> Act Ab Dur  :  NULL");
            if(mActivityExDur_track != null)
                System.out.println("\t  ::--> Act Ex Dur  : " + mActivityExDur_track.format(0));
            else
                System.out.println("\t  ::--> Act Ex Dur  :  NULL");
            if(mTracking != null)
            {
                for(int i = 0; i < mTracking.size(); i++)
                {
                    System.out.println("");
                    ADLTracking track = (ADLTracking)mTracking.elementAt(i);
                    try {
						track.dumpState();
					} catch (AOFException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }

            }
            System.out.println("\t  ::--> IsActive    :  " + mIsActive);
            System.out.println("\t  ::--> IsSupended  :  " + mIsSuspended);
            System.out.println("\t  ::--> IsVisible   :  " + mIsVisible);
            if(mParent == null)
                System.out.println("\t  ::--> Parent      :   NULL");
            else
                System.out.println("\t  ::--> Parent      :  " + mParent.getID());
            if(mChildren == null)
                System.out.println("\t  ::--> Children    :  NULL");
            else
                System.out.println("\t  ::--> Children    :  [" + mChildren.size() + "]");
            if(mActiveChildren == null)
                System.out.println("\t  ::--> ActChildren :  NULL");
            else
                System.out.println("\t  ::--> ActChildren :  [" + mActiveChildren.size() + "]");
            System.out.println("\t  ::--> Choice      :  " + mControl_choice);
            System.out.println("\t  ::--> Choice Exit :  " + mControl_choiceExit);
            System.out.println("\t  ::--> Flow        :  " + mControl_flow);
            System.out.println("\t  ::--> ForwardOnly :  " + mControl_forwardOnly);
            System.out.println("\t  ::--> Constrain   :  " + mConstrainChoice);
            System.out.println("\t  ::--> Prevent Act :  " + mPreventActivation);
            System.out.println("\t  ::--> Use Cur Obj :  " + mUseCurObj);
            System.out.println("\t  ::--> Use Cur Pro :  " + mUseCurPro);
            if(mPreConditionRules == null)
                System.out.println("\t  ::--> PRE SeqRules : NULL");
            else
                System.out.println("\t  ::--> PRE SeqRules : [" + mPreConditionRules.size() + "]");
            if(mExitActionRules == null)
                System.out.println("\t  ::--> EXIT SeqRules: NULL");
            else
                System.out.println("\t  ::--> EXIT SeqRules: [" + mExitActionRules.size() + "]");
            if(mPostConditionRules == null)
                System.out.println("\t  ::--> POST SeqRules: NULL");
            else
                System.out.println("\t  ::--> POST SeqRules: [" + mPostConditionRules.size() + "]");
            System.out.println("\tCONTROL MaxAttempts :  " + mMaxAttemptControl);
            if(mMaxAttemptControl)
                System.out.println("\t      ::-->         :  " + mMaxAttempt);
            System.out.println("\tCONTROL Att Ab Dur  :  " + mAttemptAbDurControl);
            if(mAttemptAbDurControl)
                System.out.println("\t      ::-->         :  " + mAttemptAbDur.format(0));
            System.out.println("\tCONTROL Att Ex Dur  :  " + mAttemptExDurControl);
            if(mAttemptExDurControl)
                System.out.println("\t      ::-->         :  " + mAttemptExDur.format(0));
            System.out.println("\tCONTROL Act Ab Dur  :  " + mActivityAbDurControl);
            if(mActivityAbDurControl)
                System.out.println("\t      ::-->         :  " + mActivityAbDur.format(0));
            System.out.println("\tCONTROL Act Ex Dur  :  " + mActivityExDurControl);
            if(mActivityExDurControl)
                System.out.println("\t      ::-->         :  " + mActivityExDur.format(0));
            System.out.println("\tCONTROL Begin Time  :  " + mBeginTimeControl);
            System.out.println("\t      ::-->         :  " + mBeginTime);
            System.out.println("\tCONTROL End Time    :  " + mEndTimeControl);
            System.out.println("\t      ::-->         :  " + mEndTime);
            if(mAuxResources != null)
            {
                System.out.println("\t  ::--> Services    :  [ " + mAuxResources.size() + "]");
                ADLAuxiliaryResource temp = null;
                for(int i = 0; i < mAuxResources.size(); i++)
                {
                    temp = (ADLAuxiliaryResource)mAuxResources.elementAt(i);
                    temp.dumpState();
                }

            } else
            {
                System.out.println("\t  ::--> Services    :  NULL");
            }
            if(mRollupRules == null)
                System.out.println("\t  ::--> RollupRules :  NULL");
            else
                System.out.println("\t  ::--> RollupRules :  [" + mRollupRules.size() + "]");
            System.out.println("\t  ::--> Rollup Satisfied      :  " + mRequiredForSatisfied);
            System.out.println("\t  ::--> Rollup Not Satisfied  :  " + mRequiredForNotSatisfied);
            System.out.println("\t  ::--> Rollup Completed      :  " + mRequiredForCompleted);
            System.out.println("\t  ::--> Rollup Incomplete     :  " + mRequiredForIncomplete);
            if(mObjectives == null)
            {
                System.out.println("\t  ::--> Objectives  :  NULL");
            } else
            {
                System.out.println("\t  ::--> Objectives  :  [" + mObjectives.size() + "]");
                for(int i = 0; i < mObjectives.size(); i++)
                {
                    SeqObjective obj = (SeqObjective)mObjectives.elementAt(i);
                    obj.dumpState();
                }

            }
            System.out.println("\t  ::--> Rollup Obj     :  " + mIsObjectiveRolledUp);
            System.out.println("\t  ::--> Rollup Weight  :  " + mObjMeasureWeight);
            System.out.println("\t  ::--> Rollup Pro     :  " + mIsProgressRolledUp);
            System.out.println("\t  ::--> Select Time    :  " + mSelectTiming);
            System.out.println("\t CONTROL Select Count  :  " + mSelectStatus);
            System.out.println("\t         ::-->         :  " + mSelectCount);
            System.out.println("\t  ::--> Random Time    :  " + mRandomTiming);
            System.out.println("\t  ::--> Reorder        :  " + mReorder);
            System.out.println("\t  ::--> Is Tracked     :  " + mIsTracked);
            System.out.println("\t  ::--> Cont Sets Obj  :  " + mContentSetsCompletion);
            System.out.println("\t  ::--> Cont Sets Pro  :  " + mContentSetsObj);
            System.out.println("  :: SeqActivity   --> END   - dumpState");
        }
    }

    void addChild(SeqActivity ioChild)
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - addChild");
        if(mChildren == null)
            mChildren = new Vector();
        mActiveChildren = mChildren;
        mChildren.add(ioChild);
        ioChild.setOrder(mChildren.size() - 1);
        ioChild.setActiveOrder(mChildren.size() - 1);
        ioChild.setParent(this);
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - addChild");
    }

    void setChildren(Vector ioChildren, boolean iAll)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setChildren");
            System.out.println("  ::-->  " + iAll);
        }
        SeqActivity walk = null;
        if(iAll)
        {
            mChildren = ioChildren;
            mActiveChildren = ioChildren;
            for(int i = 0; i < ioChildren.size(); i++)
            {
                walk = (SeqActivity)ioChildren.elementAt(i);
                walk.setOrder(i);
                walk.setActiveOrder(i);
                walk.setParent(this);
                walk.setIsSelected(true);
            }

        } else
        {
            for(int i = 0; i < mChildren.size(); i++)
            {
                walk = (SeqActivity)mChildren.elementAt(i);
                walk.setIsSelected(false);
            }

            mActiveChildren = ioChildren;
            for(int i = 0; i < ioChildren.size(); i++)
            {
                walk = (SeqActivity)ioChildren.elementAt(i);
                walk.setActiveOrder(i);
                walk.setIsSelected(true);
                walk.setParent(this);
            }

        }
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setChildren");
    }

    Vector getChildren(boolean iAll)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getChildren");
            System.out.println("  ::-->  " + iAll);
        }
        Vector result = null;
        if(iAll)
            result = mChildren;
        else
            result = mActiveChildren;
        if(_Debug)
        {
            if(result != null)
                System.out.println("  ::-->  [" + result.size() + "]");
            System.out.println("  :: SeqActivity     --> END   - getChildren");
        }
        return result;
    }

    boolean hasChildren(boolean iAll)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - hasChildren");
            System.out.println("  ::-->  " + iAll);
        }
        boolean result = false;
        if(iAll)
            result = mChildren != null;
        else
            result = mActiveChildren != null;
        if(_Debug)
        {
            System.out.println("  ::-->  " + result);
            System.out.println("  :: SeqActivity     --> END   - hasChildren");
        }
        return result;
    }

    SeqActivity getNextSibling(boolean iAll)
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getNextSibling");
        SeqActivity next = null;
        int target = -1;
        if(mParent != null)
        {
            if(iAll)
                target = mOrder + 1;
            else
                target = mActiveOrder + 1;
            if(target < mParent.getChildren(iAll).size())
                next = (SeqActivity)mParent.getChildren(iAll).elementAt(target);
        }
        if(_Debug)
        {
            if(next != null)
                System.out.println("  ::-->  " + next.getID());
            else
                System.out.println("  ::-->  NULL");
            System.out.println("  :: SeqActivity     --> END   - getNextSibling");
        }
        return next;
    }

    SeqActivity getPrevSibling(boolean iAll)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getPrevSibling");
            if(iAll)
                System.out.println("  ::-->  " + mOrder);
            else
                System.out.println("  ::-->  " + mActiveOrder);
        }
        SeqActivity prev = null;
        int target = -1;
        if(mParent != null)
        {
            if(iAll)
                target = mOrder - 1;
            else
                target = mActiveOrder - 1;
            if(target >= 0)
                prev = (SeqActivity)mParent.getChildren(iAll).elementAt(target);
        }
        if(_Debug)
        {
            if(prev != null)
                System.out.println("  ::-->  " + prev.getID());
            else
                System.out.println("  ::-->  NULL");
            System.out.println("  :: SeqActivity     --> END   - getPrevSibling");
        }
        return prev;
    }

    SeqActivity getParent()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getParent");
            if(mParent != null)
                System.out.println("  ::--> " + mParent.getID());
            else
                System.out.println("  ::-->  NULL");
            System.out.println("  :: SeqActivity     --> END   - getParent");
        }
        return mParent;
    }

    String getParentID()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getParentID");
            System.out.println("  :: SeqActivity     --> END   - getParentID");
        }
        if(mParent != null)
            return mParent.getID();
        else
            return null;
    }

    int getActiveOrder()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getActiveOrder");
            System.out.println("  ::-->  " + mActiveOrder);
            System.out.println("  :: SeqActivity     --> END   - getActiveOrder");
        }
        return mActiveOrder;
    }

    Vector getObjStatusSet()
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getObjStatusSet");
        Vector objSet = null;
        if(mCurTracking != null)
        {
            if(mCurTracking.mObjectives != null)
            {
                objSet = new Vector();
                for(Enumeration enume = mCurTracking.mObjectives.keys(); enume.hasMoreElements();)
                {
                    String key = (String)enume.nextElement();
                    if(!key.equals("_primary_"))
                    {
                        if(_Debug)
                            System.out.println("  ::--> Getting  -> " + key);
                        SeqObjectiveTracking obj = (SeqObjectiveTracking)mCurTracking.mObjectives.get(key);
                        ADLObjStatus objStatus = new ADLObjStatus();
                        objStatus.mObjID = obj.getObjID();
                        String measure = null;
						try {
							measure = obj.getObjMeasure(false);
						} catch (AOFException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        objStatus.mHasMeasure = !measure.equals(ADLTracking.TRACK_UNKNOWN);
                        if(objStatus.mHasMeasure)
                            objStatus.mMeasure = (new Double(measure)).doubleValue();
                        try {
							objStatus.mStatus = obj.getObjStatus(false);
						} catch (AOFException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        objSet.add(objStatus);
                    }
                }

            }
        } else
        if(_Debug)
            System.out.println("  --> Attempt has not begun -- no tracking");
        if(objSet != null && objSet.size() == 0)
            objSet = null;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - getObjStatusSet");
        return objSet;
    }

    int getDepth()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getDepth");
            System.out.println("  :: SeqActivity     --> END   - getDepth");
        }
        return mDepth;
    }

    void setDepth(int iDepth)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setDepth");
            System.out.println("  ::-->  " + iDepth);
        }
        mDepth = iDepth;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setDepth");
    }

    int getCount()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getCount");
            System.out.println("  :: SeqActivity     --> END   - getCount");
        }
        return mCount;
    }

    void setCount(int iCount)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setCount");
            System.out.println("  ::-->  " + iCount);
        }
        mCount = iCount;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setCount");
    }

    boolean getSelection()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getSelection");
            System.out.println("  :: SeqActivity     --> END   - getSelection");
        }
        return mSelection;
    }

    void setSelection(boolean iSelection)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setSelection");
            System.out.println("  ::-->  " + iSelection);
        }
        mSelection = iSelection;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setSelection");
    }

    boolean getRandomized()
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - getRandomized");
            System.out.println("  :: SeqActivity     --> END   - getRandomized");
        }
        return mRandomized;
    }

    void setRandomized(boolean iRandomized)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setRandomized");
            System.out.println("  ::-->  " + iRandomized);
        }
        mRandomized = iRandomized;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setRandomized");
    }

    private void setOrder(int iOrder)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setOrder");
            System.out.println("  ::-->  " + iOrder);
        }
        mOrder = iOrder;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setOrder");
    }

    private void setActiveOrder(int iOrder)
    {
        if(_Debug)
        {
            System.out.println("  :: SeqActivity     --> BEGIN - setActiveOrder");
            System.out.println("  ::-->  " + iOrder);
        }
        mActiveOrder = iOrder;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setActiveOrder");
    }

    private void setParent(SeqActivity iParent)
    {
        if(_Debug)
            System.out.println("  :: SeqActivity     --> BEGIN - setParent");
        mParent = iParent;
        if(_Debug)
            System.out.println("  :: SeqActivity     --> END   - setParent");
    }

    public ADLTracking getMCurTracking()
    {
        return mCurTracking;
    }

    public Vector getMActiveChildren()
    {
        return mActiveChildren;
    }

    public String getStudyTime()
    {
        return studyTime;
    }

    public void setStudyTime(String studyTime)
    {
        this.studyTime = studyTime;
    }

    public long getSysAttempt()
    {
        return sysAttempt;
    }

    public void setSysAttempt(long sysAttempt)
    {
        this.sysAttempt = sysAttempt;
    }

    public String getSuccessStatus()
    {
        return successStatus;
    }

    public void setSuccessStatus(String successStatus)
    {
        this.successStatus = successStatus;
    }

}