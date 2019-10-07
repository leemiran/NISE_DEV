/* 
 * LcmsItemResourceServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsItemResourceService;
import egovframework.adm.lcms.cts.dao.LcmsItemResourceDAO;
import egovframework.adm.lcms.cts.domain.LcmsItemResource;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsItemResourceServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsItemResourceService")
public class LcmsItemResourceServiceImpl extends EgovAbstractServiceImpl implements LcmsItemResourceService {
    @Resource(name="lcmsItemResourceDAO")
    private LcmsItemResourceDAO lcmsItemResourceDAO;

    public List selectLcmsItemResourcePageList( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.selectLcmsItemResourcePageList( commandMap);
    }

    public int selectLcmsItemResourcePageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.selectLcmsItemResourcePageListTotCnt( commandMap);
    }

    public List selectLcmsItemResourceList( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.selectLcmsItemResourceList( commandMap);
    }

    public Object selectLcmsItemResource( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.selectLcmsItemResource( commandMap);
    }

    public Object insertLcmsItemResource( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.insertLcmsItemResource( commandMap);
    }

    public int updateLcmsItemResource( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.updateLcmsItemResource( commandMap);
    }

    public int updateFieldLcmsItemResource( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.updateLcmsItemResource( commandMap);
    }

    public int deleteLcmsItemResource( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.deleteLcmsItemResource( commandMap);
    }

    public int deleteLcmsItemResourceAll( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.deleteLcmsItemResourceAll( commandMap);
    }

    
    public Object existLcmsItemResource( LcmsItemResource lcmsItemResource) throws Exception {
        return lcmsItemResourceDAO.existLcmsItemResource( lcmsItemResource);
    }
    
    public Object selectComLenLcmsItemResource( Map<String, Object> commandMap) throws Exception {
        return lcmsItemResourceDAO.selectComLenLcmsItemResource( commandMap);
    }

}
