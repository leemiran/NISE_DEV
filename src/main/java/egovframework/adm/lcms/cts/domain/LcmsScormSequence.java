/* 
 * LcmsScormSequence.java		1.00	2011-09-05 
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
 * source      : LcmsScormSequence.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsScormSequence {
    private long   seqIdxNum = 0;
    private long   itemSeq = 0;
    private long   orgSeq = 0;
    private String seqId = "";
    private String seqIdref = "";
    private String choice = "";
    private String choiceExit = "";
    private String flow = "";
    private String forwardOnly = "";
    private String useAttemptObjInfo = "";
    private String useAttemptProgressInfo = "";
    private long attemptLimit = 0;
    private String attemptDurationLimit = "";
    private String randomTiming = "";
    private long selectCount = 0;
    private String reorderChildren = "";
    private String selectionTiming = "";
    private String tracked = "";
    private String completSetbyContent = "";
    private String objSetbyContent = "";
    private String preventActivation = "";
    private String constrainChoice = "";
    private String requiredSatisfied = "";
    private String requiredNotSatisfied = "";
    private String requiredCompleted = "";
    private String requiredIncompleted = "";
    private String measureSatisfiIfAction = "";
    private String rollupObjSatisfied = "";
    private String rollupProgressCompletion = "";
    private String objMeasureWeight = "";
    private String hideUiPre = "";
    private String hideUiCon = "";
    private String hideUiEx = "";
    private String hideUiAbd = "";
    private String userId = "";
    private String updateDt = "";
    private String courseCd = "";
    private long   beforeSeqIdxNum = 0;

    public long getSeqIdxNum() {
        return seqIdxNum;
    }

    public long getItemSeq() {
        return itemSeq;
    }

    public long getOrgSeq() {
        return orgSeq;
    }

    public String getSeqId() {
        return seqId;
    }

    public String getSeqIdref() {
        return seqIdref;
    }

    public String getChoice() {
        return choice;
    }

    public String getChoiceExit() {
        return choiceExit;
    }

    public String getFlow() {
        return flow;
    }

    public String getForwardOnly() {
        return forwardOnly;
    }

    public String getUseAttemptObjInfo() {
        return useAttemptObjInfo;
    }

    public String getUseAttemptProgressInfo() {
        return useAttemptProgressInfo;
    }

    public long getAttemptLimit() {
        return attemptLimit;
    }

    public String getAttemptDurationLimit() {
        return attemptDurationLimit;
    }

    public String getRandomTiming() {
        return randomTiming;
    }

    public long getSelectCount() {
        return selectCount;
    }

    public String getReorderChildren() {
        return reorderChildren;
    }

    public String getSelectionTiming() {
        return selectionTiming;
    }

    public String getTracked() {
        return tracked;
    }

    public String getCompletSetbyContent() {
        return completSetbyContent;
    }

    public String getObjSetbyContent() {
        return objSetbyContent;
    }

    public String getPreventActivation() {
        return preventActivation;
    }

    public String getConstrainChoice() {
        return constrainChoice;
    }

    public String getRequiredSatisfied() {
        return requiredSatisfied;
    }

    public String getRequiredNotSatisfied() {
        return requiredNotSatisfied;
    }

    public String getRequiredCompleted() {
        return requiredCompleted;
    }

    public String getRequiredIncompleted() {
        return requiredIncompleted;
    }

    public String getMeasureSatisfiIfAction() {
        return measureSatisfiIfAction;
    }

    public String getRollupObjSatisfied() {
        return rollupObjSatisfied;
    }

    public String getRollupProgressCompletion() {
        return rollupProgressCompletion;
    }

    public String getObjMeasureWeight() {
        return objMeasureWeight;
    }

    public String getHideUiPre() {
        return hideUiPre;
    }

    public String getHideUiCon() {
        return hideUiCon;
    }

    public String getHideUiEx() {
        return hideUiEx;
    }

    public String getHideUiAbd() {
        return hideUiAbd;
    }

    public String getUserId() {
        return userId;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public String getCourseCd() {
        return courseCd;
    }

    public long getBeforeSeqIdxNum() {
        return beforeSeqIdxNum;
    }

    public void setSeqIdxNum( long seqIdxNum) {
        this.seqIdxNum = seqIdxNum;
    }

    public void setItemSeq( long itemSeq) {
        this.itemSeq = itemSeq;
    }

    public void setOrgSeq( long orgSeq) {
        this.orgSeq = orgSeq;
    }

    public void setSeqId( String seqId) {
        this.seqId = seqId;
    }

    public void setSeqIdref( String seqIdref) {
        this.seqIdref = seqIdref;
    }

    public void setChoice( String choice) {
        this.choice = choice;
    }

    public void setChoiceExit( String choiceExit) {
        this.choiceExit = choiceExit;
    }

    public void setFlow( String flow) {
        this.flow = flow;
    }

    public void setForwardOnly( String forwardOnly) {
        this.forwardOnly = forwardOnly;
    }

    public void setUseAttemptObjInfo( String useAttemptObjInfo) {
        this.useAttemptObjInfo = useAttemptObjInfo;
    }

    public void setUseAttemptProgressInfo( String useAttemptProgressInfo) {
        this.useAttemptProgressInfo = useAttemptProgressInfo;
    }

    public void setAttemptLimit( long attemptLimit) {
        this.attemptLimit = attemptLimit;
    }

    public void setAttemptDurationLimit( String attemptDurationLimit) {
        this.attemptDurationLimit = attemptDurationLimit;
    }

    public void setRandomTiming( String randomTiming) {
        this.randomTiming = randomTiming;
    }

    public void setSelectCount( long selectCount) {
        this.selectCount = selectCount;
    }

    public void setReorderChildren( String reorderChildren) {
        this.reorderChildren = reorderChildren;
    }

    public void setSelectionTiming( String selectionTiming) {
        this.selectionTiming = selectionTiming;
    }

    public void setTracked( String tracked) {
        this.tracked = tracked;
    }

    public void setCompletSetbyContent( String completSetbyContent) {
        this.completSetbyContent = completSetbyContent;
    }

    public void setObjSetbyContent( String objSetbyContent) {
        this.objSetbyContent = objSetbyContent;
    }

    public void setPreventActivation( String preventActivation) {
        this.preventActivation = preventActivation;
    }

    public void setConstrainChoice( String constrainChoice) {
        this.constrainChoice = constrainChoice;
    }

    public void setRequiredSatisfied( String requiredSatisfied) {
        this.requiredSatisfied = requiredSatisfied;
    }

    public void setRequiredNotSatisfied( String requiredNotSatisfied) {
        this.requiredNotSatisfied = requiredNotSatisfied;
    }

    public void setRequiredCompleted( String requiredCompleted) {
        this.requiredCompleted = requiredCompleted;
    }

    public void setRequiredIncompleted( String requiredIncompleted) {
        this.requiredIncompleted = requiredIncompleted;
    }

    public void setMeasureSatisfiIfAction( String measureSatisfiIfAction) {
        this.measureSatisfiIfAction = measureSatisfiIfAction;
    }

    public void setRollupObjSatisfied( String rollupObjSatisfied) {
        this.rollupObjSatisfied = rollupObjSatisfied;
    }

    public void setRollupProgressCompletion( String rollupProgressCompletion) {
        this.rollupProgressCompletion = rollupProgressCompletion;
    }

    public void setObjMeasureWeight( String objMeasureWeight) {
        this.objMeasureWeight = objMeasureWeight;
    }

    public void setHideUiPre( String hideUiPre) {
        this.hideUiPre = hideUiPre;
    }

    public void setHideUiCon( String hideUiCon) {
        this.hideUiCon = hideUiCon;
    }

    public void setHideUiEx( String hideUiEx) {
        this.hideUiEx = hideUiEx;
    }

    public void setHideUiAbd( String hideUiAbd) {
        this.hideUiAbd = hideUiAbd;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

    public void setCourseCd( String courseCd) {
        this.courseCd = courseCd;
    }

    public void setBeforeSeqIdxNum( long beforeSeqIdxNum) {
        this.beforeSeqIdxNum = beforeSeqIdxNum;
    }

}
