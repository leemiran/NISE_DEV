/* 
 * LcmsProgress.java		1.00	2011-10-17 
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
 * source      : LcmsProgress.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-17 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsProgress {
    private long seqCurm = 0;
    private long crsSqCode = 0;
    private String crsCode = "";
    private String year = "";
    private String module = "";
    private String lesson = "";
    private String userId = "";
    private String lessonstatus = "N";
    private String sessionTime = "";
    private String totalTime = "";
    private long lessonCount = 0;
    private String location = "";
    private String indate = "";
    private String inuserid = "";
    private String ldate = "";
    private String luserid = "";

    public long getSeqCurm() {
        return seqCurm;
    }

    public long getCrsSqCode() {
        return crsSqCode;
    }

    public String getCrsCode() {
        return crsCode;
    }

    public String getYear() {
        return year;
    }

    public String getModule() {
        return module;
    }

    public String getLesson() {
        return lesson;
    }

    public String getUserId() {
        return userId;
    }

    public String getLessonstatus() {
        return lessonstatus;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public long getLessonCount() {
        return lessonCount;
    }

    public String getLocation() {
        return location;
    }

    public String getIndate() {
        return indate;
    }

    public String getInuserid() {
        return inuserid;
    }

    public String getLdate() {
        return ldate;
    }

    public String getLuserid() {
        return luserid;
    }

    public void setSeqCurm( long seqCurm) {
        this.seqCurm = seqCurm;
    }

    public void setCrsSqCode( long crsSqCode) {
        this.crsSqCode = crsSqCode;
    }

    public void setCrsCode( String crsCode) {
        this.crsCode = crsCode;
    }

    public void setYear( String year) {
        this.year = year;
    }

    public void setModule( String module) {
        this.module = module;
    }

    public void setLesson( String lesson) {
        this.lesson = lesson;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setLessonstatus( String lessonstatus) {
        this.lessonstatus = lessonstatus;
    }

    public void setSessionTime( String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public void setTotalTime( String totalTime) {
        this.totalTime = totalTime;
    }

    public void setLessonCount( long lessonCount) {
        this.lessonCount = lessonCount;
    }

    public void setLocation( String location) {
        this.location = location;
    }

    public void setIndate( String indate) {
        this.indate = indate;
    }

    public void setInuserid( String inuserid) {
        this.inuserid = inuserid;
    }

    public void setLdate( String ldate) {
        this.ldate = ldate;
    }

    public void setLuserid( String luserid) {
        this.luserid = luserid;
    }

}
