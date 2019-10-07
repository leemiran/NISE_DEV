package com.ziaan.scorm2004.validator.contentpackage;

public class RollupRuleConditionData
{
    private int rollupConditionIdx;
    private int rollupRullIdx;
    private int seqIDx;
    
    private String operator;
    private String condition;
    
    public RollupRuleConditionData()
    {
        operator = "";
        condition = "";
    }
    
    public String getCondition()
    {
        return condition;
    }
    
    public void setCondition(String condition)
    {
        this.condition = condition;
    }
    
    public String getOperator()
    {
        return operator;
    }
    
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
    
    public int getRollupConditionIdx()
    {
        return rollupConditionIdx;
    }
    
    public void setRollupConditionIdx(int rollupConditionIdx)
    {
        this.rollupConditionIdx = rollupConditionIdx;
    }
    
    public int getRollupRullIdx()
    {
        return rollupRullIdx;
    }
    
    public void setRollupRullIdx(int rollupRullIdx)
    {
        this.rollupRullIdx = rollupRullIdx;
    }
    
    public int getSeqIDx()
    {
        return seqIDx;
    }
    
    public void setSeqIDx(int seqIDx)
    {
        this.seqIDx = seqIDx;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "\n RollupRuleCondition---------------------" );
        sb.append( "\n seqIDx                   : " + seqIDx  );
        sb.append( "\n rollupRullIdx            : " + rollupRullIdx );
        sb.append( "\n rollupConditionIdx       : " + rollupConditionIdx );
        sb.append( "\n operator                 : " + operator );
        sb.append( "\n condition                : " + condition );
        sb.append( "\n ---------------------------------------" );
        
        return sb.toString();
    }
}
