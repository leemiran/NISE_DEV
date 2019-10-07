/* 
 * LcmsCmiIaService.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.domain.LcmsCmiIa;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiIaService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsCmiIaService {
    List selectLcmsCmiIaPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsCmiIaPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsCmiIaList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsCmiIa( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsCmiIa( Map<String, Object> commandMap) throws Exception;
    int updateLcmsCmiIa( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsCmiIa( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiIa( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiIaAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsCmiIa( LcmsCmiIa lcmsCmiIa) throws Exception;
}
