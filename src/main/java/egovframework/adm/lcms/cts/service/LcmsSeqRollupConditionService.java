/* 
 * LcmsSeqRollupConditionService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsSeqRollupCondition;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRollupConditionService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsSeqRollupConditionService {
    List selectLcmsSeqRollupConditionPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsSeqRollupConditionPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsSeqRollupConditionList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception;
    int updateLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqRollupConditionAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsSeqRollupCondition( LcmsSeqRollupCondition lcmsSeqRollupCondition) throws Exception;
}
