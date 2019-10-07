/* 
 * LcmsSeqRollupRuleService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsSeqRollupRule;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRollupRuleService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsSeqRollupRuleService {
    List selectLcmsSeqRollupRulePageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsSeqRollupRulePageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsSeqRollupRuleList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception;
    int updateLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqRollupRuleAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsSeqRollupRule( LcmsSeqRollupRule lcmsSeqRollupRule) throws Exception;
}
