/* 
 * LcmsProgressService.java		1.00	2011-10-17 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.adm.lcms.cts.domain.LcmsProgress;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsProgressService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-17 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsProgressService {
    List selectLcmsProgressPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsProgressPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsProgressList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsProgress( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsProgress( Map<String, Object> commandMap) throws Exception;
    int updateLcmsProgress( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsProgress( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsProgress( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsProgressAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsProgress( LcmsProgress lcmsProgress) throws Exception;
    int updateCotiLcmsProgress( Map<String, Object> commandMap) throws Exception;
    int updateNormalLcmsProgress( Map<String, Object> commandMap) throws Exception;
    int updateScormLcmsProgress( Map<String, Object> commandMap) throws Exception;
	Map selectSubjseq(HashMap<String, Object> hm) throws Exception;
}
