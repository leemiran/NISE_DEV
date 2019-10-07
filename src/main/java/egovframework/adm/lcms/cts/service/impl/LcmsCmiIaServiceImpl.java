/* 
 * LcmsCmiIaServiceImpl.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.service.LcmsCmiIaService;
import egovframework.adm.lcms.cts.dao.LcmsCmiIaDAO;
import egovframework.adm.lcms.cts.domain.LcmsCmiIa;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiIaServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsCmiIaService")
public class LcmsCmiIaServiceImpl extends EgovAbstractServiceImpl implements LcmsCmiIaService {
    @Resource(name="lcmsCmiIaDAO")
    private LcmsCmiIaDAO lcmsCmiIaDAO;

    public List selectLcmsCmiIaPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiIaDAO.selectLcmsCmiIaPageList( commandMap);
    }

    public int selectLcmsCmiIaPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiIaDAO.selectLcmsCmiIaPageListTotCnt( commandMap);
    }

    public List selectLcmsCmiIaList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiIaDAO.selectLcmsCmiIaList( commandMap);
    }

    public Object selectLcmsCmiIa( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiIaDAO.selectLcmsCmiIa( commandMap);
    }

    public Object insertLcmsCmiIa( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiIaDAO.insertLcmsCmiIa( commandMap);
    }

    public int updateLcmsCmiIa( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiIaDAO.updateLcmsCmiIa( commandMap);
    }

    public int updateFieldLcmsCmiIa( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiIaDAO.updateLcmsCmiIa( commandMap);
    }

    public int deleteLcmsCmiIa( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiIaDAO.deleteLcmsCmiIa( commandMap);
    }

    public int deleteLcmsCmiIaAll( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiIaDAO.deleteLcmsCmiIaAll( commandMap);
    }

    
    public Object existLcmsCmiIa( LcmsCmiIa lcmsCmiIa) throws Exception {
        return lcmsCmiIaDAO.existLcmsCmiIa( lcmsCmiIa);
    }

    
}
