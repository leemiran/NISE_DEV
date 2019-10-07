package com.ziaan.scorm2004.validator.contentpackage;

import java.util.ArrayList;
import java.util.List;

public class SequencingData
{
    private String organizationIdentifier;
    private String itemIdentifier;
    private String seqType;
    private String seqIdRef;
    
    private boolean choice;
    private boolean choiceExit;
    private boolean flow;
    private boolean forwardOnly;
    private boolean useAttemptObjInfo;
    private boolean useAttemptProgressInfo;
    
    private int attemptLimit;
    private double attemptDurationLimit;
    
    private String randomTiming;
    private int selectCount;
    private boolean reorderChildren;
    private String selectionTiming;
    
    private boolean tracked;
    private boolean completSetbyContent;
    private boolean objSetbyContent;
    
    private boolean preventActivation;
    private boolean constrainChoice;
    
    private String requiredSatisfied;
    private String requiredNotSatisfied;
    private String requiredComplete;
    private String requiredIncomplete;
    private boolean measureSatisfyIfAction;
    
    private boolean rollupObjSatisfied;
    private boolean rollupProgressComplete;
    private double objMeasureWeight;
    
    private List rollupRuleList;
    private List seqRuleList;
    private List objectivesList;
    
    public SequencingData()
    {
        // SCORM2004 Sequencing Spec에 따른 기본값 Setting
        choice = true;
        choiceExit = true;
        flow = false;
        forwardOnly = false;
        useAttemptObjInfo = true;
        useAttemptProgressInfo = true;

        attemptLimit = 0;
        attemptDurationLimit = 0.0;

        randomTiming = "never";
        selectCount = 0;
        reorderChildren = false;
        selectionTiming  = "never";

        tracked = true;
        completSetbyContent = false;
        objSetbyContent = false;

        preventActivation = false;
        constrainChoice = false;

        requiredSatisfied = "always";
        requiredNotSatisfied = "always";
        requiredComplete = "always";
        requiredIncomplete = "always";
        measureSatisfyIfAction = false;

        rollupObjSatisfied = true;
        rollupProgressComplete = true;
        objMeasureWeight = 1.0000;

        rollupRuleList = new ArrayList();
        seqRuleList = new ArrayList();;
        objectivesList = new ArrayList();;        
    }

    public double getAttemptDurationLimit()
    {
        return attemptDurationLimit;
    }

    public void setAttemptDurationLimit(double attemptDurationLimit)
    {
        this.attemptDurationLimit = attemptDurationLimit;
    }

    public int getAttemptLimit()
    {
        return attemptLimit;
    }

    public void setAttemptLimit(int attemptLimit)
    {
        this.attemptLimit = attemptLimit;
    }

    public boolean isChoice()
    {
        return choice;
    }

    public void setChoice(boolean choice)
    {
        this.choice = choice;
    }

    public boolean isChoiceExit()
    {
        return choiceExit;
    }

    public void setChoiceExit(boolean choiceExit)
    {
        this.choiceExit = choiceExit;
    }

    public boolean isCompletSetbyContent()
    {
        return completSetbyContent;
    }

    public void setCompletSetbyContent(boolean completSetbyContent)
    {
        this.completSetbyContent = completSetbyContent;
    }

    public boolean isConstrainChoice()
    {
        return constrainChoice;
    }

    public void setConstrainChoice(boolean constrainChoice)
    {
        this.constrainChoice = constrainChoice;
    }

    public boolean isFlow()
    {
        return flow;
    }

    public void setFlow(boolean flow)
    {
        this.flow = flow;
    }

    public boolean isForwardOnly()
    {
        return forwardOnly;
    }

    public void setForwardOnly(boolean forwardOnly)
    {
        this.forwardOnly = forwardOnly;
    }

    public String getItemIdentifier()
    {
        return itemIdentifier;
    }

    public void setItemIdentifier(String itemIdentifier)
    {
        this.itemIdentifier = itemIdentifier;
    }

    public boolean isMeasureSatisfyIfAction()
    {
        return measureSatisfyIfAction;
    }

    public void setMeasureSatisfyIfAction(boolean measureSatisfyIfAction)
    {
        this.measureSatisfyIfAction = measureSatisfyIfAction;
    }

    public double getObjMeasureWeight()
    {
        return objMeasureWeight;
    }

    public void setObjMeasureWeight(double objMeasureWeight)
    {
        this.objMeasureWeight = objMeasureWeight;
    }

    public boolean isObjSetbyContent()
    {
        return objSetbyContent;
    }

    public void setObjSetbyContent(boolean objSetbyContent)
    {
        this.objSetbyContent = objSetbyContent;
    }

    public String getOrganizationIdentifier()
    {
        return organizationIdentifier;
    }

    public void setOrganizationIdentifier(String organizationIdentifier)
    {
        this.organizationIdentifier = organizationIdentifier;
    }

    public boolean isPreventActivation()
    {
        return preventActivation;
    }

    public void setPreventActivation(boolean preventActivation)
    {
        this.preventActivation = preventActivation;
    }

    public String getRandomTiming()
    {
        return randomTiming;
    }

    public void setRandomTiming(String randomTiming)
    {
        this.randomTiming = randomTiming;
    }

    public boolean isReorderChildren()
    {
        return reorderChildren;
    }

    public void setReorderChildren(boolean reorderChildren)
    {
        this.reorderChildren = reorderChildren;
    }

    public String getRequiredComplete()
    {
        return requiredComplete;
    }

    public void setRequiredComplete(String requiredComplete)
    {
        this.requiredComplete = requiredComplete;
    }

    public String getRequiredIncomplete()
    {
        return requiredIncomplete;
    }

    public void setRequiredIncomplete(String requiredIncomplete)
    {
        this.requiredIncomplete = requiredIncomplete;
    }

    public String getRequiredNotSatisfied()
    {
        return requiredNotSatisfied;
    }

    public void setRequiredNotSatisfied(String requiredNotSatisfied)
    {
        this.requiredNotSatisfied = requiredNotSatisfied;
    }

    public String getRequiredSatisfied()
    {
        return requiredSatisfied;
    }

    public void setRequiredSatisfied(String requiredSatisfied)
    {
        this.requiredSatisfied = requiredSatisfied;
    }

    public boolean isRollupObjSatisfied()
    {
        return rollupObjSatisfied;
    }

    public void setRollupObjSatisfied(boolean rollupObjSatisfied)
    {
        this.rollupObjSatisfied = rollupObjSatisfied;
    }

    public boolean isRollupProgressComplete()
    {
        return rollupProgressComplete;
    }

    public void setRollupProgressComplete(boolean rollupProgressComplete)
    {
        this.rollupProgressComplete = rollupProgressComplete;
    }

    public int getSelectCount()
    {
        return selectCount;
    }

    public void setSelectCount(int selectCount)
    {
        this.selectCount = selectCount;
    }

    public String getSelectionTiming()
    {
        return selectionTiming;
    }

    public void setSelectionTiming(String selectionTiming)
    {
        this.selectionTiming = selectionTiming;
    }

    public String getSeqIdRef()
    {
        return seqIdRef;
    }

    public void setSeqIdRef(String seqIdRef)
    {
        this.seqIdRef = seqIdRef;
    }

    public String getSeqType()
    {
        return seqType;
    }

    public void setSeqType(String seqType)
    {
        this.seqType = seqType;
    }

    public boolean isTracked()
    {
        return tracked;
    }

    public void setTracked(boolean tracked)
    {
        this.tracked = tracked;
    }

    public boolean isUseAttemptObjInfo()
    {
        return useAttemptObjInfo;
    }

    public void setUseAttemptObjInfo(boolean useAttemptObjInfo)
    {
        this.useAttemptObjInfo = useAttemptObjInfo;
    }

    public boolean isUseAttemptProgressInfo()
    {
        return useAttemptProgressInfo;
    }

    public void setUseAttemptProgressInfo(boolean useAttemptProgressInfo)
    {
        this.useAttemptProgressInfo = useAttemptProgressInfo;
    }

    public List getRollupRuleList()
    {
        return rollupRuleList;
    }

    public void setRollupRuleList(List rollupRuleList)
    {
        this.rollupRuleList = rollupRuleList;
    }

    public void setSeqRuleList(List seqRuleList)
    {
        this.seqRuleList = seqRuleList;
    }
    
    public List getSeqRuleList()
    {
        return seqRuleList;
    }

    public List getObjectivesList()
    {
        return objectivesList;
    }
    
    public void setObjectivesList(List objectivesList)
    {
        this.objectivesList = objectivesList;
    }

    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "\n Sequencing---------------------------------" );
        
        sb.append( "\n organizationIdentifier  : " + organizationIdentifier );
        sb.append( "\n itemIdentifier          : " + itemIdentifier );
        sb.append( "\n seqType                 : " + seqType );
        sb.append( "\n seqIdRef                : " + seqIdRef );

        sb.append( "\n choice                  : " + choice );
        sb.append( "\n choiceExit              : " + choiceExit );
        sb.append( "\n flow                    : " + flow );
        sb.append( "\n forwardOnly             : " + forwardOnly );
        sb.append( "\n useAttemptObjInfo       : " + useAttemptObjInfo );
        sb.append( "\n useAttemptProgressInfo  : " + useAttemptProgressInfo );

        sb.append( "\n attemptLimit            : " + attemptLimit );
        sb.append( "\n attemptDurationLimit    : " + attemptDurationLimit );

        sb.append( "\n randomTiming            : " + randomTiming );
        sb.append( "\n selectCount             : " + selectCount );
        sb.append( "\n reorderChildren         : " + reorderChildren );
        sb.append( "\n selectionTiming         : " + selectionTiming );

        sb.append( "\n tracked                 : " + tracked );
        sb.append( "\n completSetbyContent     : " + completSetbyContent );
        sb.append( "\n objSetbyContent         : " + objSetbyContent );

        sb.append( "\n preventActivation       : " + preventActivation );
        sb.append( "\n constrainChoice         : " + constrainChoice );

        sb.append( "\n requiredSatisfied       : " + requiredSatisfied );
        sb.append( "\n requiredNotSatisfied    : " + requiredNotSatisfied );
        sb.append( "\n requiredComplete        : " + requiredComplete );
        sb.append( "\n requiredIncomplete      : " + requiredIncomplete );
        sb.append( "\n measureSatisfyIfAction  : " + measureSatisfyIfAction );

        sb.append( "\n rollupObjSatisfied      : " + rollupObjSatisfied );
        sb.append( "\n rollupProgressComplete  : " + rollupProgressComplete );
        sb.append( "\n objMeasureWeight        : " + objMeasureWeight );

        sb.append( "\n ---------------------------------------" );
        
        return sb.toString();
    }
}

