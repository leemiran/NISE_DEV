/* 
 * LcmsGlobalobj.java		1.00	2011-09-16 
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
 * source      : LcmsGlobalobj.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-16 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsGlobalobj {
    private String objid = "";
    private String userId = "";
    private String scopeid = "";
    private String satisfied = "";
    private String measure = "";

    public String getObjid() {
        return objid;
    }

    public String getUserId() {
        return userId;
    }

    public String getScopeid() {
        return scopeid;
    }

    public String getSatisfied() {
        return satisfied;
    }

    public String getMeasure() {
        return measure;
    }

    public void setObjid( String objid) {
        this.objid = objid;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setScopeid( String scopeid) {
        this.scopeid = scopeid;
    }

    public void setSatisfied( String satisfied) {
        this.satisfied = satisfied;
    }

    public void setMeasure( String measure) {
        this.measure = measure;
    }

}
