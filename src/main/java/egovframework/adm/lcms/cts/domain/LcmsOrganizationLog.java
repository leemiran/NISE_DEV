/* 
 * LcmsOrganizationLog.java		1.00	2011-09-05 
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
 * source      : LcmsOrganizationLog.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsOrganizationLog {
    private long logSeq = 0;
    private String updateDt = "";
    private String updateId = "";
    private String insertId = "";
    private long orgSeq = 0;

    public long getLogSeq() {
        return logSeq;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public String getUpdateId() {
        return updateId;
    }

    public String getInsertId() {
        return insertId;
    }

    public long getOrgSeq() {
        return orgSeq;
    }

    public void setLogSeq( long logSeq) {
        this.logSeq = logSeq;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

    public void setUpdateId( String updateId) {
        this.updateId = updateId;
    }

    public void setInsertId( String insertId) {
        this.insertId = insertId;
    }

    public void setOrgSeq( long orgSeq) {
        this.orgSeq = orgSeq;
    }

}
