/* 
 * LcmsTocService.java		1.00	2011-09-14 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.com.lcms.len.service;

import java.util.List;
import java.util.Map;

import egovframework.com.lcms.len.domain.LcmsToc;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsTocService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


public interface LearningService {
    Object selectLearningCrsInfo( Map<String, Object> commandMap) throws Exception;
    
    Map selectMasterform( Map<String, Object> commandMap) throws Exception;
    
    int writeLog( Map<String, Object> commandMap) throws Exception;
    
    List selectEduScore( Map<String, Object> commandMap) throws Exception;
    
    List selectEduScore2( Map<String, Object> commandMap) throws Exception;
    
    int selectUserPage( Map<String, Object> commandMap) throws Exception;
    
    List SelectEduList( Map<String, Object> commandMap) throws Exception;
    
    List selectEduTimeCountOBC( Map<String, Object> commandMap) throws Exception;
    
    int isReportAssign( Map<String, Object> commandMap) throws Exception;
    
    int updateReportAssign( Map<String, Object> commandMap) throws Exception;
    
    String getPromotion( Map<String, Object> commandMap) throws Exception;
    
    String getProgress( Map<String, Object> commandMap) throws Exception;
}
