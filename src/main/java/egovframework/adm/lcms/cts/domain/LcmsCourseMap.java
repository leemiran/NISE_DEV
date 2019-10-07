/* 
 * LcmsCourseMap.java		1.00	2011-09-19 
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
 * source      : LcmsCourseMap.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-19 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsCourseMap {
    private long courseMapSeq = 0;
    private String courseCd = "";
    private String lmsYear = "";
    private String lmsTerm = "";
    private String lmsWeekCd = "";
    private String lmsCourseCd = "";
    private String serviceYn = "";
    private String ynLms = "";
    private String lmsSubjectCd = "";
    private long orgSeq = 0;

    public long getCourseMapSeq() {
        return courseMapSeq;
    }

    public String getCourseCd() {
        return courseCd;
    }

    public String getLmsYear() {
        return lmsYear;
    }

    public String getLmsTerm() {
        return lmsTerm;
    }

    public String getLmsWeekCd() {
        return lmsWeekCd;
    }

    public String getLmsCourseCd() {
        return lmsCourseCd;
    }

    public String getServiceYn() {
        return serviceYn;
    }

    public String getYnLms() {
        return ynLms;
    }

    public String getLmsSubjectCd() {
        return lmsSubjectCd;
    }

    public long getOrgSeq() {
        return orgSeq;
    }

    public void setCourseMapSeq( long courseMapSeq) {
        this.courseMapSeq = courseMapSeq;
    }

    public void setCourseCd( String courseCd) {
        this.courseCd = courseCd;
    }

    public void setLmsYear( String lmsYear) {
        this.lmsYear = lmsYear;
    }

    public void setLmsTerm( String lmsTerm) {
        this.lmsTerm = lmsTerm;
    }

    public void setLmsWeekCd( String lmsWeekCd) {
        this.lmsWeekCd = lmsWeekCd;
    }

    public void setLmsCourseCd( String lmsCourseCd) {
        this.lmsCourseCd = lmsCourseCd;
    }

    public void setServiceYn( String serviceYn) {
        this.serviceYn = serviceYn;
    }

    public void setYnLms( String ynLms) {
        this.ynLms = ynLms;
    }

    public void setLmsSubjectCd( String lmsSubjectCd) {
        this.lmsSubjectCd = lmsSubjectCd;
    }

    public void setOrgSeq( long orgSeq) {
        this.orgSeq = orgSeq;
    }

}
