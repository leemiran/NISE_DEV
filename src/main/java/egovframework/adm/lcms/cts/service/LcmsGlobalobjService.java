/* 
 * LcmsGlobalobjService.java		1.00	2011-09-16 
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

import egovframework.adm.lcms.cts.domain.LcmsGlobalobj;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsGlobalobjService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-16 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsGlobalobjService {
    List selectLcmsGlobalobjPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsGlobalobjPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsGlobalobjList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsGlobalobj( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsGlobalobj( Map<String, Object> commandMap) throws Exception;
    int updateLcmsGlobalobj( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsGlobalobj( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsGlobalobj( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsGlobalobjAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsGlobalobj( LcmsGlobalobj lcmsGlobalobj) throws Exception;
}
