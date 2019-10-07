// **********************************************************
//  1. 제      목: 매월정기안전교육 data
//  2. 파  일  명: SubjectMmData
//  3. 개      요:
//  4. 환      경: JDK 1.4
//  5. 버      젼: 1.0
//  6. 작      성: 김미향 2008. 11. 07
//  7. 수      정:
// **********************************************************
package com.ziaan.course;

public class SubjectMmData { 
    private String subj    = "";
    private String year    = "";
    private String mm      = "";
    private String start_date = "";
    private String end_date = "";
    private int    study_count = 0;
    private int    study_time  = 0;
    private int    study_chasi = 0;
    private String luserid = "";
    private String ldate   = "";
    
    
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getSubj() {
		return subj;
	}
	public void setSubj(String subj) {
		this.subj = subj;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public int getStudy_count() {
		return study_count;
	}
	public void setStudy_count(int study_count) {
		this.study_count = study_count;
	}
	public int getStudy_time() {
		return study_time;
	}
	public void setStudy_time(int study_time) {
		this.study_time = study_time;
	}
	public int getStudy_chasi() {
		return study_chasi;
	}
	public void setStudy_chasi(int study_chasi) {
		this.study_chasi = study_chasi;
	}
	public String getLuserid() {
		return luserid;
	}
	public void setLuserid(String luserid) {
		this.luserid = luserid;
	}
	public String getLdate() {
		return ldate;
	}
	public void setLdate(String ldate) {
		this.ldate = ldate;
	}

}
