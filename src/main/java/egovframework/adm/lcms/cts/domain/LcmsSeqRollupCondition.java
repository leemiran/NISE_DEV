/* 
 * LcmsSeqRollupCondition.java		1.00	2011-09-05 
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
 * source      : LcmsSeqRollupCondition.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsSeqRollupCondition {
    private long rlcIdxNum = 0;
    private long rollupConditionNum = 0;
    private String operator = "";
    private String condition = "";
    private String userId = "";
    private String updateDt = "";
    private long rrIdxNum = 0;

    public long getRlcIdxNum() {
        return rlcIdxNum;
    }

    public long getRollupConditionNum() {
        return rollupConditionNum;
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

    public long getRrIdxNum() {
        return rrIdxNum;
    }

    public void setRlcIdxNum( long rlcIdxNum) {
        this.rlcIdxNum = rlcIdxNum;
    }

    public void setRollupConditionNum( long rollupConditionNum) {
        this.rollupConditionNum = rollupConditionNum;
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

    public void setRrIdxNum( long rrIdxNum) {
        this.rrIdxNum = rrIdxNum;
    }

}
