/* 
 * LcmsItemService.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.domain.LcmsItem;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsItemService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsItemService {
    List selectLcmsItemPageList(Map<String, Object> commandMap) throws Exception;
    int  selectLcmsItemPageListTotCnt(Map<String, Object> commandMap) throws Exception;
    List selectLcmsItemList(Map<String, Object> commandMap) throws Exception;
    Object selectLcmsItem( Map<String, Object> commandMap) throws Exception;
    Object insertLcmsItem( Map<String, Object> commandMap) throws Exception;
    int updateLcmsItem( Map<String, Object> commandMap) throws Exception;
    int updateFieldLcmsItem( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsItem( Map<String, Object> commandMap) throws Exception;
    int deleteLcmsItemAll(Map<String, Object> commandMap) throws Exception;
    Object existLcmsItem( LcmsItem lcmsItem) throws Exception;
    Object selectComLenLcmsItem( Map<String, Object> commandMap) throws Exception;
    Object selectComLenRsrcSeqLcmsItem( Map<String, Object> commandMap) throws Exception;
    
    //학습창 
    Object selectLcmsItem01( Map<String, Object> commandMap) throws Exception;
}
