/* 
 * LcmsOrganizationLogService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsOrganizationLog;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsOrganizationLogService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsOrganizationLogService {
    List selectLcmsOrganizationLogPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsOrganizationLogPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsOrganizationLogList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception;
    int updateLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsOrganizationLogAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsOrganizationLog( LcmsOrganizationLog lcmsOrganizationLog) throws Exception;
}
