/* 
 * LcmsManiseqService.java		1.00	2011-09-14 
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

import egovframework.adm.lcms.cts.domain.LcmsManiseq;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsManiseqService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsManiseqService {
    List selectLcmsManiseqPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsManiseqPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsManiseqList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsManiseq( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsManiseq( Map<String, Object> commandMap) throws Exception;
    int updateLcmsManiseq( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsManiseq( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsManiseq( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsManiseqAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsManiseq( LcmsManiseq lcmsManiseq) throws Exception;
}
