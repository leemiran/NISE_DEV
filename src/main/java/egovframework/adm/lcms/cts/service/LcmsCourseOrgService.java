/* 
 * LcmsCourseOrgService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsCourseOrg;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCourseOrgService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsCourseOrgService {
    List selectLcmsCourseOrgPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsCourseOrgPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsCourseOrgList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsCourseOrg( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsCourseOrg( Map<String, Object> commandMap) throws Exception;
    int updateLcmsCourseOrg( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsCourseOrg( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCourseOrg( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCourseOrgAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsCourseOrg( LcmsCourseOrg LcmsCourseOrg) throws Exception;
}
