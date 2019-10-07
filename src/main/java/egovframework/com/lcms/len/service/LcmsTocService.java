/* 
 * LcmsTocService.java		1.00	2011-09-14 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.com.lcms.len.service;

import java.util.List;
import java.util.Map;

import egovframework.com.lcms.len.domain.LcmsToc;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsTocService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsTocService {
    List selectLcmsTocPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsTocPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsTocList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsToc( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsToc( Map<String, Object> commandMap) throws Exception;
    int updateLcmsToc( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsToc( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsToc( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsTocAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsToc( LcmsToc lcmsToc) throws Exception;
}
