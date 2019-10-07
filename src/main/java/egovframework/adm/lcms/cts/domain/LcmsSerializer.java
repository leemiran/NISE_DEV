/* 
 * LcmsSerializer.java		1.00	2011-09-14 
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
 * source      : LcmsSerializer.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsSerializer {
    private long idx = 0;
    private String courseSeq = "";
    private long orgSeq = 0;
    private Object serializer = null;

    public long getIdx() {
        return idx;
    }

    public String getCourseSeq() {
        return courseSeq;
    }

    public long getOrgSeq() {
        return orgSeq;
    }

    public Object getSerializer() {
        return serializer;
    }

    public void setIdx( long idx) {
        this.idx = idx;
    }

    public void setCourseSeq( String courseSeq) {
        this.courseSeq = courseSeq;
    }

    public void setOrgSeq( long orgSeq) {
        this.orgSeq = orgSeq;
    }

    public void setSerializer( Object serializer) {
        this.serializer = serializer;
    }

}
