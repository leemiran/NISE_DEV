package com.ziaan.scorm2004.validator.contentpackage;

import java.util.List;

public class SeqRuleData
{
    private int ruleIdx;
    private int seqIdx;
    
    private String ruleType;
    private String conditionCombination;
    private String ruleAction;
    
    private List ruleConditionList;
    
    public SeqRuleData()
    {
        conditionCombination = "all";
    }
    
    public String getConditionCombination()
    {
        return conditionCombination;
    }
    
    public void setConditionCombination(String conditionCombination)
    {
        this.conditionCombination = conditionCombination;
    }
    
    public String getRuleAction()
    {
        return ruleAction;
    }
    
    public void setRuleAction(String ruleAction)
    {
        this.ruleAction = ruleAction;
    }
    
    public int getRuleIdx()
    {
        return ruleIdx;
    }
    
    public void setRuleIdx(int ruleIdx)
    {
        this.ruleIdx = ruleIdx;
    }
    
    public String getRuleType()
    {
        return ruleType;
    }
    
    public void setRuleType(String ruleType)
    {
        this.ruleType = ruleType;
    }
    
    public int getSeqIdx()
    {
        return seqIdx;
    }
    
    public void setSeqIdx(int seqIdx)
    {
        this.seqIdx = seqIdx;
    }

    public List getRuleConditionList()
    {
        return ruleConditionList;
    }

    public void setRuleConditionList(List ruleConditionList)
    {
        this.ruleConditionList = ruleConditionList;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "\n *SequencingRule -----------------------" );
        sb.append( "\n seqIdx               : " + seqIdx );
        sb.append( "\n ruleIdx              : " + ruleIdx );
        sb.append( "\n ruleType             : " + ruleType );
        sb.append( "\n conditionCombination : " + conditionCombination );
        sb.append( "\n ruleAction           : " + ruleAction );
        sb.append( "\n ---------------------------------------" );        
                
        return sb.toString();
    }
}
