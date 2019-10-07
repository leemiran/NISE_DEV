/* 
 * LcmsSeqMapInfo.java		1.00	2011-09-05 
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
 * source      : LcmsSeqMapInfo.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsSeqMapInfo {
    private long rlcIdxNum = 0;
    private long mapInfoId = 0;
    private String targetObjId = "";
    private String readStatus = "";
    private String readMeasure = "";
    private String writeStatus = "";
    private String writeMeasure = "";
    private String userId = "";
    private long objectiveIdxNum = 0;
    private String updateDt = "";

    public long getRlcIdxNum() {
        return rlcIdxNum;
    }

    public long getMapInfoId() {
        return mapInfoId;
    }

    public String getTargetObjId() {
        return targetObjId;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public String getReadMeasure() {
        return readMeasure;
    }

    public String getWriteStatus() {
        return writeStatus;
    }

    public String getWriteMeasure() {
        return writeMeasure;
    }

    public String getUserId() {
        return userId;
    }

    public long getObjectiveIdxNum() {
        return objectiveIdxNum;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setRlcIdxNum( long rlcIdxNum) {
        this.rlcIdxNum = rlcIdxNum;
    }

    public void setMapInfoId( long mapInfoId) {
        this.mapInfoId = mapInfoId;
    }

    public void setTargetObjId( String targetObjId) {
        this.targetObjId = targetObjId;
    }

    public void setReadStatus( String readStatus) {
        this.readStatus = readStatus;
    }

    public void setReadMeasure( String readMeasure) {
        this.readMeasure = readMeasure;
    }

    public void setWriteStatus( String writeStatus) {
        this.writeStatus = writeStatus;
    }

    public void setWriteMeasure( String writeMeasure) {
        this.writeMeasure = writeMeasure;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setObjectiveIdxNum( long objectiveIdxNum) {
        this.objectiveIdxNum = objectiveIdxNum;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

}
