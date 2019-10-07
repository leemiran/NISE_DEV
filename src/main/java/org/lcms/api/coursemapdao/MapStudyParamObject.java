/**
* @(#)MapStudyParamObject.java
* description :  TB_LCMS_CORUSE_MAP Table에 대한 등록/수정/삭제/조회를 수행하는 Data Access Object.
* note : TB_LCMS_CORUSE_MAP
* date : 2008. 12. 31.
* @author : 4CSOFT-오일환.
* @version : 1.0.
* modified : 
* comment : 
...
*/
package org.lcms.api.coursemapdao;

import java.io.Serializable;

public class MapStudyParamObject implements MapStudyParamInf , Serializable{
/*
    LEC_APP_SERIAL,   -- 수강생일련번호
    LEVEL_CD,   -- 레벨코드
	CHAPTER_CD	-- 챕터코드
	GANG_ID		-- 강아이디
 */
	
	static final long serialVersionUID = -6548994212831326482L;
	String item_id;
	int item_seq;
	int org_seq;
	String progress_measure;
	long total_time;
	int study_count;
	
	String level_cd = null;
	String chapter_cd = null;
	int edu_day;
	String course_map_seq = null;
	String course_seq = null;
	String course_cd = null;
	String lms_year = null;
	String lms_term = null;
	String lms_week_cd = null;
	String lms_course_cd = null;
	String service_yn = null;
	String yn_lms = null;
	String lms_subject_cd = null;
	/**
	 * @return the item_id
	 */
	public String getItem_id() {
		return item_id;
	}
	/**
	 * @param item_id the item_id to set
	 */
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	/**
	 * @return the item_seq
	 */
	public int getItem_seq() {
		return item_seq;
	}
	/**
	 * @param item_seq the item_seq to set
	 */
	public void setItem_seq(int item_seq) {
		this.item_seq = item_seq;
	}
	/**
	 * @return the org_seq
	 */
	public int getOrg_seq() {
		return org_seq;
	}
	/**
	 * @param org_seq the org_seq to set
	 */
	public void setOrg_seq(int org_seq) {
		this.org_seq = org_seq;
	}
	/**
	 * @return the progress_measure
	 */
	public String getProgress_measure() {
		return progress_measure;
	}
	/**
	 * @param progress_measure the progress_measure to set
	 */
	public void setProgress_measure(String progress_measure) {
		this.progress_measure = progress_measure;
	}
	/**
	 * @return the total_time
	 */
	public long getTotal_time() {
		return total_time;
	}
	/**
	 * @param total_time the total_time to set
	 */
	public void setTotal_time(long total_time) {
		this.total_time = total_time;
	}
	/**
	 * @return the study_count
	 */
	public int getStudy_count() {
		return study_count;
	}
	/**
	 * @param study_count the study_count to set
	 */
	public void setStudy_count(int study_count) {
		this.study_count = study_count;
	}
	/**
	 * @return the level_cd
	 */
	public String getLevel_cd() {
		return level_cd;
	}
	/**
	 * @param level_cd the level_cd to set
	 */
	public void setLevel_cd(String level_cd) {
		this.level_cd = level_cd;
	}
	/**
	 * @return the chapter_cd
	 */
	public String getChapter_cd() {
		return chapter_cd;
	}
	/**
	 * @param chapter_cd the chapter_cd to set
	 */
	public void setChapter_cd(String chapter_cd) {
		this.chapter_cd = chapter_cd;
	}
	/**
	 * @return the edu_day
	 */
	public int getEdu_day() {
		return edu_day;
	}
	/**
	 * @param edu_day the edu_day to set
	 */
	public void setEdu_day(int edu_day) {
		this.edu_day = edu_day;
	}
	/**
	 * @return the course_map_seq
	 */
	public String getCourse_map_seq() {
		return course_map_seq;
	}
	/**
	 * @param course_map_seq the course_map_seq to set
	 */
	public void setCourse_map_seq(String course_map_seq) {
		this.course_map_seq = course_map_seq;
	}
	/**
	 * @return the course_seq
	 */
	public String getCourse_seq() {
		return course_seq;
	}
	/**
	 * @param course_seq the course_seq to set
	 */
	public void setCourse_seq(String course_seq) {
		this.course_seq = course_seq;
	}
	/**
	 * @return the lms_year
	 */
	public String getLms_year() {
		return lms_year;
	}
	/**
	 * @param lms_year the lms_year to set
	 */
	public void setLms_year(String lms_year) {
		this.lms_year = lms_year;
	}
	/**
	 * @return the lms_term
	 */
	public String getLms_term() {
		return lms_term;
	}
	/**
	 * @param lms_term the lms_term to set
	 */
	public void setLms_term(String lms_term) {
		this.lms_term = lms_term;
	}
	/**
	 * @return the lms_week_cd
	 */
	public String getLms_week_cd() {
		return lms_week_cd;
	}
	/**
	 * @param lms_week_cd the lms_week_cd to set
	 */
	public void setLms_week_cd(String lms_week_cd) {
		this.lms_week_cd = lms_week_cd;
	}
	/**
	 * @return the lms_course_cd
	 */
	public String getLms_course_cd() {
		return lms_course_cd;
	}
	/**
	 * @param lms_course_cd the lms_course_cd to set
	 */
	public void setLms_course_cd(String lms_course_cd) {
		this.lms_course_cd = lms_course_cd;
	}
	/**
	 * @return the service_yn
	 */
	public String getService_yn() {
		return service_yn;
	}
	/**
	 * @param service_yn the service_yn to set
	 */
	public void setService_yn(String service_yn) {
		this.service_yn = service_yn;
	}
	/**
	 * @return the yn_lms
	 */
	public String getYn_lms() {
		return yn_lms;
	}
	/**
	 * @param yn_lms the yn_lms to set
	 */
	public void setYn_lms(String yn_lms) {
		this.yn_lms = yn_lms;
	}
	/**
	 * @return the lms_subject_cd
	 */
	public String getLms_subject_cd() {
		return lms_subject_cd;
	}
	/**
	 * @param lms_subject_cd the lms_subject_cd to set
	 */
	public void setLms_subject_cd(String lms_subject_cd) {
		this.lms_subject_cd = lms_subject_cd;
	}
	/**
	 * @return the course_cd
	 */
	public String getCourse_cd() {
		return course_cd;
	}
	/**
	 * @param course_cd the course_cd to set
	 */
	public void setCourse_cd(String course_cd) {
		this.course_cd = course_cd;
	}
	
}
