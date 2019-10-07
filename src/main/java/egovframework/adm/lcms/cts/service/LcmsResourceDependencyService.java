/* 
 * LcmsResourceDependencyService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsResourceDependency;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsResourceDependencyService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsResourceDependencyService {
    List selectLcmsResourceDependencyPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsResourceDependencyPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsResourceDependencyList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsResourceDependency( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsResourceDependency( Map<String, Object> commandMap) throws Exception;
    int updateLcmsResourceDependency( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsResourceDependency( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsResourceDependency( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsResourceDependencyAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsResourceDependency( LcmsResourceDependency lcmsResourceDependency) throws Exception;
}
