/* 
 * LcmsSeqRuleConditionService.java		1.00	2011-09-16 
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

import egovframework.adm.lcms.cts.domain.LcmsSeqRuleCondition;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRuleConditionService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-16 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsSeqRuleConditionService {
    List selectLcmsSeqRuleConditionPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsSeqRuleConditionPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsSeqRuleConditionList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception;
    int updateLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqRuleConditionAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsSeqRuleCondition( LcmsSeqRuleCondition lcmsSeqRuleCondition) throws Exception;
}
