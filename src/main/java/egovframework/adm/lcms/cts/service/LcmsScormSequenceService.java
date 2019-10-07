/* 
 * LcmsScormSequenceService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsScormSequence;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsScormSequenceService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsScormSequenceService {
    List selectLcmsScormSequencePageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsScormSequencePageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsScormSequenceList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsScormSequence( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsScormSequence( Map<String, Object> commandMap) throws Exception;
    int updateLcmsScormSequence( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsScormSequence( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsScormSequence( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsScormSequenceAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsScormSequence( LcmsScormSequence lcmsScormSequence) throws Exception;
    Map selectLcmsScormSequenceSeqIdxNum( Map<String, Object> commandMap) throws Exception;
}
