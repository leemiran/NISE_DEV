package com.ziaan.scorm2004.validator.contentpackage;

public class ObjectivesMapInfoData
{
    private int seqIdx;
    private int objIdx;
    private int objMapInfoIdx;
    
    private String targetObjectiveID;
    private boolean readSatisfiedStatus;
    private boolean readNormalizedMeasure;
    private boolean writeSatisfiedStatus;
    private boolean writeNormalizedMeasure;
    
    public ObjectivesMapInfoData()
    {
        readSatisfiedStatus = true;
        readNormalizedMeasure = true;
        writeSatisfiedStatus = false;
        writeNormalizedMeasure = false;
    }

    public int getObjIdx()
    {
        return objIdx;
    }

    public void setObjIdx(int objIdx)
    {
        this.objIdx = objIdx;
    }

    public int getObjMapInfoIdx()
    {
        return objMapInfoIdx;
    }

    public void setObjMapInfoIdx(int objMapInfoIdx)
    {
        this.objMapInfoIdx = objMapInfoIdx;
    }

    public boolean isReadNormalizedMeasure()
    {
        return readNormalizedMeasure;
    }

    public void setReadNormalizedMeasure(boolean readNormalizedMeasure)
    {
        this.readNormalizedMeasure = readNormalizedMeasure;
    }

    public boolean isReadSatisfiedStatus()
    {
        return readSatisfiedStatus;
    }

    public void setReadSatisfiedStatus(boolean readSatisfiedStatus)
    {
        this.readSatisfiedStatus = readSatisfiedStatus;
    }

    public int getSeqIdx()
    {
        return seqIdx;
    }

    public void setSeqIdx(int seqIdx)
    {
        this.seqIdx = seqIdx;
    }

    public String getTargetObjectiveID()
    {
        return targetObjectiveID;
    }

    public void setTargetObjectiveID(String targetObjectiveID)
    {
        this.targetObjectiveID = targetObjectiveID;
    }

    public boolean isWriteNormalizedMeasure()
    {
        return writeNormalizedMeasure;
    }

    public void setWriteNormalizedMeasure(boolean writeNormalizedMeasure)
    {
        this.writeNormalizedMeasure = writeNormalizedMeasure;
    }

    public boolean isWriteSatisfiedStatus()
    {
        return writeSatisfiedStatus;
    }

    public void setWriteSatisfiedStatus(boolean writeSatisfiedStatus)
    {
        this.writeSatisfiedStatus = writeSatisfiedStatus;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "\n *Objectives MapInfo -------------------" );
        sb.append( "\n seqIdx                   : " + seqIdx );
        sb.append( "\n objIdx                   : " + objIdx );
        sb.append( "\n objMapInfoIdx            : " + objMapInfoIdx );
        sb.append( "\n targetObjectiveID        : " + targetObjectiveID );
        sb.append( "\n readSatisfiedStatus      : " + readSatisfiedStatus );
        sb.append( "\n readNormalizedMeasure    : " + readNormalizedMeasure );
        sb.append( "\n writeSatisfiedStatus     : " + writeSatisfiedStatus );
        sb.append( "\n writeNormalizedMeasure   : " + writeNormalizedMeasure );        
        sb.append( "\n ---------------------------------------" );
        
        return sb.toString();
    }    
}
