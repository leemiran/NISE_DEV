/* 
 * LcmsSeqRuleCondition.java		1.00	2011-09-16 
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
 * source      : LcmsSeqRuleCondition.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-16 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsSeqRuleCondition {
    private long rcIdxNum = 0;
    private long ctIdxNum = 0;
    private long ruleConditionId = 0;
    private String referenceObjective = "";
    private String measureThreshold = "";
    private String operator = "";
    private String condition = "";
    private String userId = "";
    private String updateDt = "";

    public long getRcIdxNum() {
        return rcIdxNum;
    }

    public long getCtIdxNum() {
        return ctIdxNum;
    }

    public long getRuleConditionId() {
        return ruleConditionId;
    }

    public String getReferenceObjective() {
        return referenceObjective;
    }

    public String getMeasureThreshold() {
        return measureThreshold;
    }

    public String getOperator() {
        return operator;
    }

    public String getCondition() {
        return condition;
    }

    public String getUserId() {
        return userId;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setRcIdxNum( long rcIdxNum) {
        this.rcIdxNum = rcIdxNum;
    }

    public void setCtIdxNum( long ctIdxNum) {
        this.ctIdxNum = ctIdxNum;
    }

    public void setRuleConditionId( long ruleConditionId) {
        this.ruleConditionId = ruleConditionId;
    }

    public void setReferenceObjective( String referenceObjective) {
        this.referenceObjective = referenceObjective;
    }

    public void setMeasureThreshold( String measureThreshold) {
        this.measureThreshold = measureThreshold;
    }

    public void setOperator( String operator) {
        this.operator = operator;
    }

    public void setCondition( String condition) {
        this.condition = condition;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

}
