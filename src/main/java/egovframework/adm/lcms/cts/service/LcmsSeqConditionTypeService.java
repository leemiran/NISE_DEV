/* 
 * LcmsSeqConditionTypeService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsSeqConditionType;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqConditionTypeService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsSeqConditionTypeService {
    List selectLcmsSeqConditionTypePageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsSeqConditionTypePageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsSeqConditionTypeList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception;
    int updateLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSeqConditionTypeAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsSeqConditionType( LcmsSeqConditionType lcmsSeqConditionType) throws Exception;
}
