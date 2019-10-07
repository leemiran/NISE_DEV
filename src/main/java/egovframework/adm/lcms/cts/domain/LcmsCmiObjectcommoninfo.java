/* 
 * LcmsCmiObjectcommoninfo.java		1.00	2011-09-26 
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
 * source      : LcmsCmiObjectcommoninfo.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsCmiObjectcommoninfo {
    private long seq = 0;
    private String version = "";
    private String commentsFromLearnerChildren = "";
    private String commentsFromLmsChildren = "";
    private String interactionsChildren = "";
    private String learnerPreferenceChildren = "";
    private String objectivesChildren = "";
    private String objectivesScoreChildren = "";
    private String scoreChildren = "";
    private String userId = "";
    private String updateDt = "";

    public long getSeq() {
        return seq;
    }

    public String getVersion() {
        return version;
    }

    public String getCommentsFromLearnerChildren() {
        return commentsFromLearnerChildren;
    }

    public String getCommentsFromLmsChildren() {
        return commentsFromLmsChildren;
    }

    public String getInteractionsChildren() {
        return interactionsChildren;
    }

    public String getLearnerPreferenceChildren() {
        return learnerPreferenceChildren;
    }

    public String getObjectivesChildren() {
        return objectivesChildren;
    }

    public String getObjectivesScoreChildren() {
        return objectivesScoreChildren;
    }

    public String getScoreChildren() {
        return scoreChildren;
    }

    public String getUserId() {
        return userId;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setSeq( long seq) {
        this.seq = seq;
    }

    public void setVersion( String version) {
        this.version = version;
    }

    public void setCommentsFromLearnerChildren( String commentsFromLearnerChildren) {
        this.commentsFromLearnerChildren = commentsFromLearnerChildren;
    }

    public void setCommentsFromLmsChildren( String commentsFromLmsChildren) {
        this.commentsFromLmsChildren = commentsFromLmsChildren;
    }

    public void setInteractionsChildren( String interactionsChildren) {
        this.interactionsChildren = interactionsChildren;
    }

    public void setLearnerPreferenceChildren( String learnerPreferenceChildren) {
        this.learnerPreferenceChildren = learnerPreferenceChildren;
    }

    public void setObjectivesChildren( String objectivesChildren) {
        this.objectivesChildren = objectivesChildren;
    }

    public void setObjectivesScoreChildren( String objectivesScoreChildren) {
        this.objectivesScoreChildren = objectivesScoreChildren;
    }

    public void setScoreChildren( String scoreChildren) {
        this.scoreChildren = scoreChildren;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

}
