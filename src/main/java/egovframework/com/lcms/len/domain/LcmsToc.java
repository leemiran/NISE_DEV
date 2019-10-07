/* 
 * LcmsToc.java		1.00	2011-09-14 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.com.lcms.len.domain;


/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsToc.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsToc {
    private long tocIdx = 0;
    private String userId = "";
    private long courseMapSeq = 0;
    private long idx = 0;
    private Object serializer = "";
    private int orgSeq  = 0;
    public long getTocIdx() {
        return tocIdx;
    }

    public String getUserId() {
        return userId;
    }

    public long getCourseMapSeq() {
        return courseMapSeq;
    }

    public long getIdx() {
        return idx;
    }

    public Object getSerializer() {
        return serializer;
    }

    public void setTocIdx( long tocIdx) {
        this.tocIdx = tocIdx;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setCourseMapSeq( long courseMapSeq) {
        this.courseMapSeq = courseMapSeq;
    }

    public void setIdx( long idx) {
        this.idx = idx;
    }

    public void setSerializer( Object serializer) {
        this.serializer = serializer;
    }

	public int getOrgSeq() {
		return orgSeq;
	}

	public void setOrgSeq(int orgSeq) {
		this.orgSeq = orgSeq;
	}

}
