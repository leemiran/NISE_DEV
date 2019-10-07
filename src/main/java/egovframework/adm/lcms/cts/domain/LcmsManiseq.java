/* 
 * LcmsManiseq.java		1.00	2011-09-14 
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
 * source      : LcmsManiseq.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsManiseq {
    private long seq = 0;
    private String insertDate = "";

    public long getSeq() {
        return seq;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setSeq( long seq) {
        this.seq = seq;
    }

    public void setInsertDate( String insertDate) {
        this.insertDate = insertDate;
    }

}
