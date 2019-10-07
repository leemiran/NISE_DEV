/* 
 * LcmsCmiIa.java		1.00	2011-09-26 
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
 * source      : LcmsCmiIa.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsCmiIa {
    private long interactionsSeq = 0;
    private long interactionsCount = 0;
    private String interactionsId = "";
    private String interactionsType = "";
    private String interactionsWeighting = "";
    private String interactionsLearnerResponse = "";
    private String interactionsResult = "";
    private String interactionsLatency = "";
    private String interactionsDescription = "";
    private String interactionsTimestamp = "";
    private String userId = "";
    private String updateDt = "";
    private long courseMapSeq = 0;
    private long orgSeq = 0;
    private String itemId = "";
    private String learnerId = "";

    public long getInteractionsSeq() {
        return interactionsSeq;
    }

    public long getInteractionsCount() {
        return interactionsCount;
    }

    public String getInteractionsId() {
        return interactionsId;
    }

    public String getInteractionsType() {
        return interactionsType;
    }

    public String getInteractionsWeighting() {
        return interactionsWeighting;
    }

    public String getInteractionsLearnerResponse() {
        return interactionsLearnerResponse;
    }

    public String getInteractionsResult() {
        return interactionsResult;
    }

    public String getInteractionsLatency() {
        return interactionsLatency;
    }

    public String getInteractionsDescription() {
        return interactionsDescription;
    }

    public String getInteractionsTimestamp() {
        return interactionsTimestamp;
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

    public void setInteractionsSeq( long interactionsSeq) {
        this.interactionsSeq = interactionsSeq;
    }

    public void setInteractionsCount( long interactionsCount) {
        this.interactionsCount = interactionsCount;
    }

    public void setInteractionsId( String interactionsId) {
        this.interactionsId = interactionsId;
    }

    public void setInteractionsType( String interactionsType) {
        this.interactionsType = interactionsType;
    }

    public void setInteractionsWeighting( String interactionsWeighting) {
        this.interactionsWeighting = interactionsWeighting;
    }

    public void setInteractionsLearnerResponse( String interactionsLearnerResponse) {
        this.interactionsLearnerResponse = interactionsLearnerResponse;
    }

    public void setInteractionsResult( String interactionsResult) {
        this.interactionsResult = interactionsResult;
    }

    public void setInteractionsLatency( String interactionsLatency) {
        this.interactionsLatency = interactionsLatency;
    }

    public void setInteractionsDescription( String interactionsDescription) {
        this.interactionsDescription = interactionsDescription;
    }

    public void setInteractionsTimestamp( String interactionsTimestamp) {
        this.interactionsTimestamp = interactionsTimestamp;
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

}
