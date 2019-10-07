/* 
 * LcmsResourceDependency.java		1.00	2011-09-05 
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
 * source      : LcmsResourceDependency.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsResourceDependency {
    private long seq = 0;
    private String rsrcIdRef = "";
    private long rsrcSeq = 0;
    private String userId = "";
    private String updateDt = "";

    public long getSeq() {
        return seq;
    }

    public String getRsrcIdRef() {
        return rsrcIdRef;
    }

    public long getRsrcSeq() {
        return rsrcSeq;
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

    public void setRsrcIdRef( String rsrcIdRef) {
        this.rsrcIdRef = rsrcIdRef;
    }

    public void setRsrcSeq( long rsrcSeq) {
        this.rsrcSeq = rsrcSeq;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

}
