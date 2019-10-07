/* 
 * LcmsCmiCommentlearner.java		1.00	2011-09-26 
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
 * source      : LcmsCmiCommentlearner.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */

public class LcmsCmiCommentlearner {
    private long commentsLearnerCmtSeq = 0;
    private long cmtsLearnerCount = 0;
    private String cmtsLearnerCmt = "";
    private String cmtsLearnerLocation = "";
    private String cmtsLearnerData = "";
    private String userId = "";
    private String updateDt = "";
    private long cmtsLearnerNum = 0;
    private long progresscode = 0;
    private long courseMapSeq = 0;
    private long orgSeq = 0;
    private String itemId = "";
    private String learnerId = "";
    private String cmtsLearnerTimestamp = "";

    public long getCommentsLearnerCmtSeq() {
        return commentsLearnerCmtSeq;
    }

    public long getCmtsLearnerCount() {
        return cmtsLearnerCount;
    }

    public String getCmtsLearnerCmt() {
        return cmtsLearnerCmt;
    }

    public String getCmtsLearnerLocation() {
        return cmtsLearnerLocation;
    }

    public String getCmtsLearnerData() {
        return cmtsLearnerData;
    }

    public String getUserId() {
        return userId;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public long getCmtsLearnerNum() {
        return cmtsLearnerNum;
    }

    public long getProgresscode() {
        return progresscode;
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

    public void setCommentsLearnerCmtSeq( long commentsLearnerCmtSeq) {
        this.commentsLearnerCmtSeq = commentsLearnerCmtSeq;
    }

    public void setCmtsLearnerCount( long cmtsLearnerCount) {
        this.cmtsLearnerCount = cmtsLearnerCount;
    }

    public void setCmtsLearnerCmt( String cmtsLearnerCmt) {
        this.cmtsLearnerCmt = cmtsLearnerCmt;
    }

    public void setCmtsLearnerLocation( String cmtsLearnerLocation) {
        this.cmtsLearnerLocation = cmtsLearnerLocation;
    }

    public void setCmtsLearnerData( String cmtsLearnerData) {
        this.cmtsLearnerData = cmtsLearnerData;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

    public void setCmtsLearnerNum( long cmtsLearnerNum) {
        this.cmtsLearnerNum = cmtsLearnerNum;
    }

    public void setProgresscode( long progresscode) {
        this.progresscode = progresscode;
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

	public String getCmtsLearnerTimestamp() {
		return cmtsLearnerTimestamp;
	}

	public void setCmtsLearnerTimestamp(String cmtsLearnerTimestamp) {
		this.cmtsLearnerTimestamp = cmtsLearnerTimestamp;
	}

}
