/* 
 * LcmsSeqConditionType.java		1.00	2011-09-05 
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
 * source      : LcmsSeqConditionType.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsSeqConditionType {
    private long ctIdxNum = 0;
    private long seqIdNum = 0;
    private long ruleConditionId = 0;
    private String conditionRuleType = "";
    private String ruleAction = "";
    private String conditionCombination = "";
    private String userId = "";
    private String updateDt = "";
    private long seqIdxNum = 0;
    private long beforeCtIdxNum = 0;

    public long getCtIdxNum() {
        return ctIdxNum;
    }

    public long getSeqIdNum() {
        return seqIdNum;
    }

    public long getRuleConditionId() {
        return ruleConditionId;
    }

    public String getConditionRuleType() {
        return conditionRuleType;
    }

    public String getRuleAction() {
        return ruleAction;
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

    public long getBeforeCtIdxNum() {
        return beforeCtIdxNum;
    }

    public void setCtIdxNum( long ctIdxNum) {
        this.ctIdxNum = ctIdxNum;
    }

    public void setSeqIdNum( long seqIdNum) {
        this.seqIdNum = seqIdNum;
    }

    public void setRuleConditionId( long ruleConditionId) {
        this.ruleConditionId = ruleConditionId;
    }

    public void setConditionRuleType( String conditionRuleType) {
        this.conditionRuleType = conditionRuleType;
    }

    public void setRuleAction( String ruleAction) {
        this.ruleAction = ruleAction;
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

    public void setBeforeCtIdxNum( long beforeCtIdxNum) {
        this.beforeCtIdxNum = beforeCtIdxNum;
    }

}
