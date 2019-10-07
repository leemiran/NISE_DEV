/* 
 * LcmsSeqRollupRule.java		1.00	2011-09-05 
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
 * source      : LcmsSeqRollupRule.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsSeqRollupRule {
    private long rrIdxNum = 0;
    private String childActivitySet = "";
    private long minimumCount = 0;
    private String minimumPercent = "";
    private String rollupAction = "";
    private String conditionCombination = "";
    private String userId = "";
    private String updateDt = "";
    private long seqIdxNum = 0;
    private long beforeRrIdxNum = 0;

    public long getRrIdxNum() {
        return rrIdxNum;
    }

    public String getChildActivitySet() {
        return childActivitySet;
    }

    public long getMinimumCount() {
        return minimumCount;
    }

    public String getMinimumPercent() {
        return minimumPercent;
    }

    public String getRollupAction() {
        return rollupAction;
    }

    public String getConditionCombination() {
        return conditionCombination;
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

    public long getBeforeRrIdxNum() {
        return beforeRrIdxNum;
    }

    public void setRrIdxNum( long rrIdxNum) {
        this.rrIdxNum = rrIdxNum;
    }

    public void setChildActivitySet( String childActivitySet) {
        this.childActivitySet = childActivitySet;
    }

    public void setMinimumCount( long minimumCount) {
        this.minimumCount = minimumCount;
    }

    public void setMinimumPercent( String minimumPercent) {
        this.minimumPercent = minimumPercent;
    }

    public void setRollupAction( String rollupAction) {
        this.rollupAction = rollupAction;
    }

    public void setConditionCombination( String conditionCombination) {
        this.conditionCombination = conditionCombination;
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

    public void setBeforeRrIdxNum( long beforeRrIdxNum) {
        this.beforeRrIdxNum = beforeRrIdxNum;
    }

}
