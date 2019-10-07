package com.ziaan.scorm2004.validator.contentpackage;

import java.util.List;

public class ObjectivesData
{
    private int seqIdx;
    private int objIdx;
    
    private String objType;
    private boolean satisfiedByMeasure;
    private String objectiveID;
    
    private double minNormalizedMeasure;

    private List mapInfoList;
    
    public ObjectivesData()
    {
        satisfiedByMeasure = false;
        minNormalizedMeasure = 1.0;
    }


    public double getMinNormalizedMeasure()
    {
        return minNormalizedMeasure;
    }


    public void setMinNormalizedMeasure(double minNormalizedMeasure)
    {
        this.minNormalizedMeasure = minNormalizedMeasure;
    }


    public String getObjectiveID()
    {
        return objectiveID;
    }


    public void setObjectiveID(String objectiveID)
    {
        this.objectiveID = objectiveID;
    }


    public int getObjIdx()
    {
        return objIdx;
    }


    public void setObjIdx(int objIdx)
    {
        this.objIdx = objIdx;
    }


    public String getObjType()
    {
        return objType;
    }


    public void setObjType(String objType)
    {
        this.objType = objType;
    }


    public boolean isSatisfiedByMeasure()
    {
        return satisfiedByMeasure;
    }


    public void setSatisfiedByMeasure(boolean satisfiedByMeasure)
    {
        this.satisfiedByMeasure = satisfiedByMeasure;
    }


    public int getSeqIdx()
    {
        return seqIdx;
    }


    public void setSeqIdx(int seqIdx)
    {
        this.seqIdx = seqIdx;
    }

    public List getMapInfoList()
    {
        return mapInfoList;
    }
    
    public void setMapInfoList(List mapInfoList)
    {
        this.mapInfoList = mapInfoList;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "\n *Objectives ---------------------------" );        
        sb.append( "\n seqIdx              : " + seqIdx );
        sb.append( "\n objIdx              : " + objIdx );
        sb.append( "\n objType             : " + objType );
        sb.append( "\n satisfiedByMeasure  : " + satisfiedByMeasure );
        sb.append( "\n objectiveID         : " + objectiveID );
        sb.append( "\n minNormalizedMeasure: " + minNormalizedMeasure );
        sb.append( "\n ---------------------------------------" );
        
        return sb.toString();
    }
}
