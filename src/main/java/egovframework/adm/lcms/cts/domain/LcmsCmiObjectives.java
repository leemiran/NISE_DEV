/* 
 * LcmsCmiObjectives.java		1.00	2011-09-26 
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
 * source      : LcmsCmiObjectives.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsCmiObjectives {
    private long objectivesSeq = 0;
    private long objectivesCount = 0;
    private String objectivesId = "";
    private String objectivesScoreScaled = "";
    private String objectivesScoreRaw = "";
    private String objectivesScoreMin = "";
    private String objectivesScoreMax = "";
    private String objectivesSuccessStatus = "";
    private String objectivesCompletionStatus = "";
    private String objectivesProgressMeasure = "";
    private String objectivesDescription = "";
    private String userId = "";
    private String updateDt = "";
    private long courseMapSeq = 0;
    private long orgSeq = 0;
    private String itemId = "";
    private String learnerId = "";
    private String nowYear   = "";

    public long getObjectivesSeq() {
        return objectivesSeq;
    }

    public long getObjectivesCount() {
        return objectivesCount;
    }

    public String getObjectivesId() {
        return objectivesId;
    }

    public String getObjectivesScoreScaled() {
        return objectivesScoreScaled;
    }

    public String getObjectivesScoreRaw() {
        return objectivesScoreRaw;
    }

    public String getObjectivesScoreMin() {
        return objectivesScoreMin;
    }

    public String getObjectivesScoreMax() {
        return objectivesScoreMax;
    }

    public String getObjectivesSuccessStatus() {
        return objectivesSuccessStatus;
    }

    public String getObjectivesCompletionStatus() {
        return objectivesCompletionStatus;
    }

    public String getObjectivesProgressMeasure() {
        return objectivesProgressMeasure;
    }

    public String getObjectivesDescription() {
        return objectivesDescription;
    }

    public String getUserId() {
        return userId;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public long getCourseMapSeq() {
        return courseMapSeq;
    }

    public long getOrgSeq() {
        return orgSeq;
    }

    public String getItemId() {
        return itemId;
    }

    public String getLearnerId() {
        return learnerId;
    }

    public void setObjectivesSeq( long objectivesSeq) {
        this.objectivesSeq = objectivesSeq;
    }

    public void setObjectivesCount( long objectivesCount) {
        this.objectivesCount = objectivesCount;
    }

    public void setObjectivesId( String objectivesId) {
        this.objectivesId = objectivesId;
    }

    public void setObjectivesScoreScaled( String objectivesScoreScaled) {
        this.objectivesScoreScaled = objectivesScoreScaled;
    }

    public void setObjectivesScoreRaw( String objectivesScoreRaw) {
        this.objectivesScoreRaw = objectivesScoreRaw;
    }

    public void setObjectivesScoreMin( String objectivesScoreMin) {
        this.objectivesScoreMin = objectivesScoreMin;
    }

    public void setObjectivesScoreMax( String objectivesScoreMax) {
        this.objectivesScoreMax = objectivesScoreMax;
    }

    public void setObjectivesSuccessStatus( String objectivesSuccessStatus) {
        this.objectivesSuccessStatus = objectivesSuccessStatus;
    }

    public void setObjectivesCompletionStatus( String objectivesCompletionStatus) {
        this.objectivesCompletionStatus = objectivesCompletionStatus;
    }

    public void setObjectivesProgressMeasure( String objectivesProgressMeasure) {
        this.objectivesProgressMeasure = objectivesProgressMeasure;
    }

    public void setObjectivesDescription( String objectivesDescription) {
        this.objectivesDescription = objectivesDescription;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

    public void setCourseMapSeq( long courseMapSeq) {
        this.courseMapSeq = courseMapSeq;
    }

    public void setOrgSeq( long orgSeq) {
        this.orgSeq = orgSeq;
    }

    public void setItemId( String itemId) {
        this.itemId = itemId;
    }

    public void setLearnerId( String learnerId) {
        this.learnerId = learnerId;
    }

	public String getNowYear() {
		return nowYear;
	}

	public void setNowYear(String nowYear) {
		this.nowYear = nowYear;
	}
    

}
