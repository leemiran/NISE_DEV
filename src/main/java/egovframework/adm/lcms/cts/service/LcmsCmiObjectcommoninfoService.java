/* 
 * LcmsCmiObjectcommoninfoService.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.domain.LcmsCmiObjectcommoninfo;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiObjectcommoninfoService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsCmiObjectcommoninfoService {
    List selectLcmsCmiObjectcommoninfoPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsCmiObjectcommoninfoPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsCmiObjectcommoninfoList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception;
    int updateLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiObjectcommoninfoAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsCmiObjectcommoninfo( LcmsCmiObjectcommoninfo lcmsCmiObjectcommoninfo) throws Exception;
}
