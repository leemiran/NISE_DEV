/* 
 * LcmsResourceDependencyServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsResourceDependencyService;
import egovframework.adm.lcms.cts.dao.LcmsResourceDependencyDAO;
import egovframework.adm.lcms.cts.domain.LcmsResourceDependency;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsResourceDependencyServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsResourceDependencyService")
public class LcmsResourceDependencyServiceImpl extends EgovAbstractServiceImpl implements LcmsResourceDependencyService {
    @Resource(name="lcmsResourceDependencyDAO")
    private LcmsResourceDependencyDAO lcmsResourceDependencyDAO;

    public List selectLcmsResourceDependencyPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsResourceDependencyDAO.selectLcmsResourceDependencyPageList( commandMap);
    }

    public int selectLcmsResourceDependencyPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsResourceDependencyDAO.selectLcmsResourceDependencyPageListTotCnt( commandMap);
    }

    public List selectLcmsResourceDependencyList( Map<String, Object> commandMap) throws Exception {
        return lcmsResourceDependencyDAO.selectLcmsResourceDependencyList( commandMap);
    }

    public Object selectLcmsResourceDependency( Map<String, Object> commandMap) throws Exception {
        return lcmsResourceDependencyDAO.selectLcmsResourceDependency( commandMap);
    }

    public Object insertLcmsResourceDependency( Map<String, Object> commandMap) throws Exception {
        return lcmsResourceDependencyDAO.insertLcmsResourceDependency( commandMap);
    }

    public int updateLcmsResourceDependency( Map<String, Object> commandMap) throws Exception {
        return lcmsResourceDependencyDAO.updateLcmsResourceDependency( commandMap);
    }

    public int updateFieldLcmsResourceDependency( Map<String, Object> commandMap) throws Exception {
        return lcmsResourceDependencyDAO.updateLcmsResourceDependency( commandMap);
    }

    public int deleteLcmsResourceDependency( Map<String, Object> commandMap) throws Exception {
        return lcmsResourceDependencyDAO.deleteLcmsResourceDependency( commandMap);
    }

    public int deleteLcmsResourceDependencyAll( Map<String, Object> commandMap) throws Exception {
        return lcmsResourceDependencyDAO.deleteLcmsResourceDependencyAll( commandMap);
    }

    
    public Object existLcmsResourceDependency( LcmsResourceDependency lcmsResourceDependency) throws Exception {
        return lcmsResourceDependencyDAO.existLcmsResourceDependency( lcmsResourceDependency);
    }

    
}
