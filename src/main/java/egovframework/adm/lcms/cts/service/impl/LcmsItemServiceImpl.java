/* 
 * LcmsItemServiceImpl.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.excel.EgovExcelService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import egovframework.adm.lcms.cts.service.LcmsItemService;
import egovframework.adm.lcms.cts.dao.LcmsItemDAO;
import egovframework.adm.lcms.cts.domain.LcmsItem;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsItemServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsItemService")
public class LcmsItemServiceImpl extends EgovAbstractServiceImpl implements LcmsItemService {
    @Resource(name="lcmsItemDAO")
    private LcmsItemDAO lcmsItemDAO;

    public List selectLcmsItemPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.selectLcmsItemPageList( commandMap);
    }

    public int selectLcmsItemPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.selectLcmsItemPageListTotCnt( commandMap);
    }

    public List selectLcmsItemList( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.selectLcmsItemList( commandMap);
    }

    public Object selectLcmsItem( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.selectLcmsItem( commandMap);
    }

    public Object insertLcmsItem( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.insertLcmsItem( commandMap);
    }

    public int updateLcmsItem( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.updateLcmsItem( commandMap);
    }

    public int updateFieldLcmsItem( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.updateLcmsItem( commandMap);
    }

    public int deleteLcmsItem( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.deleteLcmsItem( commandMap);
    }

    public int deleteLcmsItemAll( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.deleteLcmsItemAll( commandMap);
    }

    
    public Object existLcmsItem( LcmsItem lcmsItem) throws Exception {
        return lcmsItemDAO.existLcmsItem( lcmsItem);
    }

    public Object selectComLenLcmsItem( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.selectComLenLcmsItem( commandMap);
    }
    
    public Object selectComLenRsrcSeqLcmsItem( Map<String, Object> commandMap) throws Exception {
    	return lcmsItemDAO.selectComLenRsrcSeqLcmsItem( commandMap);
    }
    
    public Object selectLcmsItem01( Map<String, Object> commandMap) throws Exception {
        return lcmsItemDAO.selectLcmsItem01( commandMap);
    }
}
