/* 
 * LcmsCmiCommentlearnerService.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.domain.LcmsCmiCommentlearner;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiCommentlearnerService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsCmiCommentlearnerService {
    List selectLcmsCmiCommentlearnerPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsCmiCommentlearnerPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsCmiCommentlearnerList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception;
    int updateLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiCommentlearnerAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsCmiCommentlearner( LcmsCmiCommentlearner lcmsCmiCommentlearner) throws Exception;
}
