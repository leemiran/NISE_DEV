/* 
 * LcmsCmiCommentfromlmsService.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.domain.LcmsCmiCommentfromlms;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiCommentfromlmsService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsCmiCommentfromlmsService {
    List selectLcmsCmiCommentfromlmsPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsCmiCommentfromlmsPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsCmiCommentfromlmsList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception;
    int updateLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsCmiCommentfromlmsAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsCmiCommentfromlms( LcmsCmiCommentfromlms lcmsCmiCommentfromlms) throws Exception;
}
