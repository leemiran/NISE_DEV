/* 
 * LcmsCmiCommentfromlms.java		1.00	2011-09-26 
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
 * source      : LcmsCmiCommentfromlms.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsCmiCommentfromlms {
    private long commentsLmsCommentSeq = 0;
    private long commentsLmsCount = 0;
    private String commentsLmsComment = "";
    private String commentsLmsLocation = "";
    private String commentsLmsTimestamp = "";
    private String userId = "";
    private String updateDt = "";
    private long courseMapSeq = 0;
    private long orgSeq = 0;
    private String itemId = "";
    private String learnerId = "";

    public long getCommentsLmsCommentSeq() {
        return commentsLmsCommentSeq;
    }

    public long getCommentsLmsCount() {
        return commentsLmsCount;
    }

    public String getCommentsLmsComment() {
        return commentsLmsComment;
    }

    public String getCommentsLmsLocation() {
        return commentsLmsLocation;
    }

    public String getCommentsLmsTimestamp() {
        return commentsLmsTimestamp;
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

    public void setCommentsLmsCommentSeq( long commentsLmsCommentSeq) {
        this.commentsLmsCommentSeq = commentsLmsCommentSeq;
    }

    public void setCommentsLmsCount( long commentsLmsCount) {
        this.commentsLmsCount = commentsLmsCount;
    }

    public void setCommentsLmsComment( String commentsLmsComment) {
        this.commentsLmsComment = commentsLmsComment;
    }

    public void setCommentsLmsLocation( String commentsLmsLocation) {
        this.commentsLmsLocation = commentsLmsLocation;
    }

    public void setCommentsLmsTimestamp( String commentsLmsTimestamp) {
        this.commentsLmsTimestamp = commentsLmsTimestamp;
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
