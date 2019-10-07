package com.ziaan.scorm2004.validator.contentpackage;

import java.util.List;

public class RollupRuleData
{
    private int rollupRuleIdx;
    private int seqIdx;
    
    private String childActivitySet;
    private int minimumCount;
    private double minimumPercent;
    private String rollupAction;
    private String conditionCombination;
    
    private List rollupRuleConditionList;
    
    public RollupRuleData()
    {
        childActivitySet = "all";
        minimumCount = 0;
        minimumPercent = 0.0000;
        
        conditionCombination = "any";
    }
    
    public String getChildActivitySet()
    {
        return childActivitySet;
    }
    
    public void setChildActivitySet(String childActivitySet)
    {
        this.childActivitySet = childActivitySet;
    }
    
    public String getConditionCombination()
    {
        return conditionCombination;
    }
    
    public void setConditionCombination(String conditionCombination)
    {
        this.conditionCombination = conditionCombination;
    }
    
    public int getMinimumCount()
    {
        return minimumCount;
    }
    
    public void setMinimumCount(int minimumCount)
    {
        this.minimumCount = minimumCount;
    }
    
    public double getMinimumPercent()
    {
        return minimumPercent;
    }
    
    public void setMinimumPercent(double minimumPercent)
    {
        this.minimumPercent = minimumPercent;
    }
    
    public String getRollupAction()
    {
        return rollupAction;
    }
    
    public void setRollupAction(String rollupAction)
    {
        this.rollupAction = rollupAction;
    }
    
    public int getRollupRuleIdx()
    {
        return rollupRuleIdx;
    }
    
    public void setRollupRuleIdx(int rollupRuleIdx)
    {
        this.rollupRuleIdx = rollupRuleIdx;
    }
    
    public int getSeqIdx()
    {
        return seqIdx;
    }
    
    public void setSeqIdx(int seqIdx)
    {
        this.seqIdx = seqIdx;
    }

    public List getRollupRuleConditionList()
    {
        return rollupRuleConditionList;
    }

    public void setRollupRuleConditionList(List rollupRuleConditionList)
    {
        this.rollupRuleConditionList = rollupRuleConditionList;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "\n *RollupRule ---------------------------" );
        sb.append( "\n seqIdx              : " + seqIdx );
        sb.append( "\n rollupRuleIdx       : " + rollupRuleIdx );
        sb.append( "\n childAcivitySet     : " + childActivitySet  );
        sb.append( "\n minimumCount        : " + seqIdx );
        sb.append( "\n rollupAction        : " + rollupAction );
        sb.append( "\n conditionCombination: " + conditionCombination );
        sb.append( "\n ---------------------------------------" );
        
        return sb.toString();
    }
}
