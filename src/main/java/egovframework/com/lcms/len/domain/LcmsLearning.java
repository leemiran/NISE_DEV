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

import java.util.List;

import egovframework.adm.lcms.cts.domain.LcmsProgress;
import egovframework.adm.lcms.nct.domain.LcmsLesson;


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


public class LcmsLearning{
    private LcmsProgress progress = null;
    private LcmsLesson   lesson = null;
    private List lessonList = null;				//레슨정보
    private List progressList = null;			//진도정보
    
    
	public LcmsProgress getProgress() {
		return progress;
	}
	public void setProgress(LcmsProgress progress) {
		this.progress = progress;
	}
	public LcmsLesson getLesson() {
		return lesson;
	}
	public void setLesson(LcmsLesson lesson) {
		this.lesson = lesson;
	}
	public List getLessonList() {
		return lessonList;
	}
	public void setLessonList(List lessonList) {
		this.lessonList = lessonList;
	}
	public List getProgressList() {
		return progressList;
	}
	public void setProgressList(List progressList) {
		this.progressList = progressList;
	}
    
    

}
