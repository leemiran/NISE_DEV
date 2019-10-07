/* 
 * LcmsSeqObjectivesService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsSeqObjectives;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqObjectivesService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsSeqObjectivesService {
    List selectLcmsSeqObjectivesPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsSeqObjectivesPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsSeqObjectivesList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception;
    int updateLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqObjectivesAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsSeqObjectives( LcmsSeqObjectives lcmsSeqObjectives) throws Exception;
    Map selectLcmsSeqObjectives01( Map<String, Object> commandMap) throws Exception;
}
