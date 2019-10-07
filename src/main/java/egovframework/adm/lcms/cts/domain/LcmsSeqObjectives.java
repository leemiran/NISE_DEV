/* 
 * LcmsSeqObjectives.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.domain;


/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqObjectives.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsSeqObjectives {
    private long objectiveIdxNum = 0;
    private String objId = "";
    private String satisfiedMeasure = "";
    private String minnormalMeasure = "";
    private String objType = "";
    private String userId = "";
    private String updateDt = "";
    private long seqIdxNum = 0;
    private long beforeObjectiveIdxNum = 0;

    public long getObjectiveIdxNum() {
        return objectiveIdxNum;
    }

    public String getObjId() {
        return objId;
    }

    public String getSatisfiedMeasure() {
        return satisfiedMeasure;
    }

    public String getMinnormalMeasure() {
        return minnormalMeasure;
    }

    public String getObjType() {
        return objType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public long getSeqIdxNum() {
        return seqIdxNum;
    }

    public long getBeforeObjectiveIdxNum() {
        return beforeObjectiveIdxNum;
    }

    public void setObjectiveIdxNum( long objectiveIdxNum) {
        this.objectiveIdxNum = objectiveIdxNum;
    }

    public void setObjId( String objId) {
        this.objId = objId;
    }

    public void setSatisfiedMeasure( String satisfiedMeasure) {
        this.satisfiedMeasure = satisfiedMeasure;
    }

    public void setMinnormalMeasure( String minnormalMeasure) {
        this.minnormalMeasure = minnormalMeasure;
    }

    public void setObjType( String objType) {
        this.objType = objType;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

    public void setSeqIdxNum( long seqIdxNum) {
        this.seqIdxNum = seqIdxNum;
    }

    public void setBeforeObjectiveIdxNum( long beforeObjectiveIdxNum) {
        this.beforeObjectiveIdxNum = beforeObjectiveIdxNum;
    }

}
