/* 
 * LcmsOrganizationService.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import egovframework.adm.lcms.cts.domain.LcmsOrganization;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsOrganizationService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsOrganizationService {
    List selectLcmsOrganizationPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsOrganizationPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsOrganizationList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsOrganization( Map<String, Object> commandMap) throws Exception;
    
    // manifest.xml file insert
    int insertLcmsOrganization( ArrayList dataList) throws Exception;
    
    int updateLcmsOrganization( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsOrganization( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsOrganization( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsOrganizationAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsOrganization( LcmsOrganization lcmsOrganization) throws Exception;
    
    List selectOrganizationPathList(Map<String, Object> commandMap) throws Exception;
    int checkCourseMapping(Map<String, Object> commandMap) throws Exception;
}
