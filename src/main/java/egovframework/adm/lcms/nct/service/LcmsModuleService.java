/* 
 * LcmsModuleService.java		1.00	2011-10-11 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.nct.service;

import java.util.List;
import java.util.Map;

import egovframework.adm.lcms.nct.domain.LcmsModule;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsModuleService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsModuleService {
    List selectLcmsModulePageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsModulePageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsModuleList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsModule( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsModule( Map<String, Object> commandMap) throws Exception;
    int updateLcmsModule( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsModule( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsModule( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsModuleAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsModule( LcmsModule lcmsModule) throws Exception;
    List selectModulePath(Map<String, Object> commandMap) throws Exception;
    
    Map selectLcmsModuleData(Map<String, Object> commandMap) throws Exception;
    Map selectSaveInfoData(Map<String, Object> commandMap) throws Exception;
}
