/* 
 * LcmsItemResourceService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsItemResource;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsItemResourceService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsItemResourceService {
    List selectLcmsItemResourcePageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsItemResourcePageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsItemResourceList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsItemResource( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsItemResource( Map<String, Object> commandMap) throws Exception;
    int updateLcmsItemResource( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsItemResource( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsItemResource( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsItemResourceAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsItemResource( LcmsItemResource lcmsItemResource) throws Exception;
    Object selectComLenLcmsItemResource( Map<String, Object> commandMap) throws Exception;
}
