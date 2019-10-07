/* 
 * LcmsCourseMapService.java		1.00	2011-09-19 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.service;

import java.util.List;
import java.util.Map;

import egovframework.adm.lcms.cts.domain.LcmsCourseMap;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCourseMapService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-19 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsCourseMapService {
    List selectLcmsCourseMapPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsCourseMapPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsCourseMapList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsCourseMap( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsCourseMap( Map<String, Object> commandMap) throws Exception;
    int updateLcmsCourseMap( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsCourseMap( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCourseMap( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCourseMapAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsCourseMap( LcmsCourseMap lcmsCourseMap) throws Exception;
    
    int getCourseMapSeq(Map<String, Object> commandMap) throws Exception;
}
