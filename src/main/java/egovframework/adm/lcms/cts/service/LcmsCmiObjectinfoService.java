/* 
 * LcmsCmiObjectinfoService.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.domain.LcmsCmiObjectinfo;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiObjectinfoService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsCmiObjectinfoService {
    List selectLcmsCmiObjectinfoPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsCmiObjectinfoPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsCmiObjectinfoList(Map<String, Object> commandMap) throws Exception;
    Map  selectLcmsCmiObjectinfo( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsCmiObjectinfo( LcmsCmiObjectinfo lcmsCmiObjectinfo) throws Exception;
    int updateLcmsCmiObjectinfo( LcmsCmiObjectinfo lcmsCmiObjectinfo) throws Exception;
    int updateFieldLcmsCmiObjectinfo( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiObjectinfo( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiObjectinfoAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsCmiObjectinfo( LcmsCmiObjectinfo lcmsCmiObjectinfo) throws Exception;
    String checkScormEduLimit(Map<String, Object> commandMap) throws Exception;
}
