/* 
 * LcmsLesson.java		1.00	2011-10-11 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.nct.domain;


/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsLesson.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsLesson {
    private String crsCode = "";
    private String module = "";
    private String lesson = "";
    private String lessonName = "";
    private String starting = "";
    private long eduTime = 0;
    private String eduTimeYn = "";
    private long pageCount = 0;
    private String inuserid = "";
    private String indate = "";
    private String luserid = "";
    private String ldate = "";
    private int   lessonCnt = 0;

    public String getCrsCode() {
        return crsCode;
    }

    public String getModule() {
        return module;
    }

    public String getLesson() {
        return lesson;
    }

    public String getLessonName() {
        return lessonName;
    }

    public String getStarting() {
        return starting;
    }

    public long getEduTime() {
        return eduTime;
    }

    public String getEduTimeYn() {
        return eduTimeYn;
    }

    public long getPageCount() {
        return pageCount;
    }

    public String getInuserid() {
        return inuserid;
    }

    public String getIndate() {
        return indate;
    }

    public String getLuserid() {
        return luserid;
    }

    public String getLdate() {
        return ldate;
    }

    public void setCrsCode( String crsCode) {
        this.crsCode = crsCode;
    }

    public void setModule( String module) {
        this.module = module;
    }

    public void setLesson( String lesson) {
        this.lesson = lesson;
    }

    public void setLessonName( String lessonName) {
        this.lessonName = lessonName;
    }

    public void setStarting( String starting) {
        this.starting = starting;
    }

    public void setEduTime( long eduTime) {
        this.eduTime = eduTime;
    }

    public void setEduTimeYn( String eduTimeYn) {
        this.eduTimeYn = eduTimeYn;
    }

    public void setPageCount( long pageCount) {
        this.pageCount = pageCount;
    }

    public void setInuserid( String inuserid) {
        this.inuserid = inuserid;
    }

    public void setIndate( String indate) {
        this.indate = indate;
    }

    public void setLuserid( String luserid) {
        this.luserid = luserid;
    }

    public void setLdate( String ldate) {
        this.ldate = ldate;
    }

	public int getLessonCnt() {
		return lessonCnt;
	}

	public void setLessonCnt(int lessonCnt) {
		this.lessonCnt = lessonCnt;
	}

   
}
