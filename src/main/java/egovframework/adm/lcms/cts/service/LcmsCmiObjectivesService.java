/* 
 * LcmsCmiObjectivesService.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.domain.LcmsCmiObjectives;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiObjectivesService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsCmiObjectivesService {
    List selectLcmsCmiObjectivesPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsCmiObjectivesPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsCmiObjectivesList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsCmiObjectives( Map<String, Object> commandMap,List list) throws Exception;
    int updateLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiObjectivesAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsCmiObjectives( LcmsCmiObjectives lcmsCmiObjectives) throws Exception;
}
