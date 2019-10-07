/* 
 * LcmsSerializerService.java		1.00	2011-09-14 
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

import egovframework.adm.lcms.cts.domain.LcmsSerializer;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSerializerService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsSerializerService {
	/**
    List selectLcmsSerializerPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsSerializerPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsSerializerList(Map<String, Object> commandMap) throws Exception;
    **/
	
    Object selectLcmsSerializer( Map<String, Object> commandMap) throws Exception;
    /**
    Object insertLcmsSerializer( Map<String, Object> commandMap) throws Exception;
    int updateLcmsSerializer( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsSerializer( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSerializer( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsSerializerAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsSerializer( LcmsSerializer lcmsSerializer) throws Exception;
    **/
}
