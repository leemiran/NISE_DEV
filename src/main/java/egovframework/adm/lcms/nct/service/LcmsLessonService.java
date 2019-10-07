/* 
 * LcmsLessonService.java		1.00	2011-10-11 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.nct.service;

import java.util.List;
import java.util.Map;

import egovframework.adm.lcms.nct.domain.LcmsLesson;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsLessonService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsLessonService {
    List selectLcmsLessonPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsLessonPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsLessonList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsLesson( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsLesson( Map<String, Object> commandMap) throws Exception;
    int updateLcmsLesson( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsLesson( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsLesson( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsLessonAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsLesson( LcmsLesson lcmsLesson) throws Exception;
    
    Map selectLessonData(Map<String, Object> commandMap) throws Exception;
}
