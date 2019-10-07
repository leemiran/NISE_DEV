package com.ziaan.scorm2004.validator.contentpackage;

public class SeqRuleConditionData
{
    private int ruleConditionIdx;
    private int ruleIdx;
    private int seqIdx;

    private String referencedObjective;
    private double measureThreshold;
    private String operator;
    private String condition;
    
    public SeqRuleConditionData()
    {
        referencedObjective = "";
        measureThreshold = 0.0;
        operator = "noOp";
        condition = "always";
    }
    
    public String getCondition()
    {
        return condition;
    }

    public void setCondition(String condition)
    {
        this.condition = condition;
    }
    
    public double getMeasureThreshold()
    {
        return measureThreshold;
    }
    
    public void setMeasureThreshold(double measureThreshold)
    {
        this.measureThreshold = measureThreshold;
    }
    
    public String getOperator()
    {
        return operator;
    }
    
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
    
    public String getReferencedObjective()
    {
        return referencedObjective;
    }
    
    public void setReferencedObjective(String referencedObjective)
    {
        this.referencedObjective = referencedObjective;
    }
    
    public int getRuleConditionIdx()
    {
        return ruleConditionIdx;
    }
    
    public void setRuleConditionIdx(int ruleConditionIdx)
    {
        this.ruleConditionIdx = ruleConditionIdx;
    }
    
    public int getRuleIdx()
    {
        return ruleIdx;
    }
    
    public void setRuleIdx(int ruleIdx)
    {
        this.ruleIdx = ruleIdx;
    }

    public int getSeqIdx()
    {
        return seqIdx;
    }
    
    public void setSeqIdx(int seqIdx)
    {
        this.seqIdx = seqIdx;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "\n *SequencingRuleCondition----------------" );
        sb.append( "\n seqIdx               : " + seqIdx );
        sb.append( "\n ruleIdx              : " + ruleIdx );
        sb.append( "\n ruleConditionIdx     : " + ruleConditionIdx );
        sb.append( "\n referencedObjective  : " + referencedObjective );
        sb.append( "\n measureThreshold     : " + measureThreshold );
        sb.append( "\n operator             : " + operator );
        sb.append( "\n condition            : " + condition );
        sb.append( "\n ---------------------------------------" );
        
        return sb.toString();
    }
}
