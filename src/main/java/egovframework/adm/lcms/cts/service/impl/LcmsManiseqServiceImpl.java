/* 
 * LcmsManiseqServiceImpl.java		1.00	2011-09-14 
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

import egovframework.adm.lcms.cts.service.LcmsManiseqService;
import egovframework.adm.lcms.cts.dao.LcmsManiseqDAO;
import egovframework.adm.lcms.cts.domain.LcmsManiseq;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsManiseqServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsManiseqService")
public class LcmsManiseqServiceImpl extends EgovAbstractServiceImpl implements LcmsManiseqService {
    @Resource(name="lcmsManiseqDAO")
    private LcmsManiseqDAO lcmsManiseqDAO;

    public List selectLcmsManiseqPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsManiseqDAO.selectLcmsManiseqPageList( commandMap);
    }

    public int selectLcmsManiseqPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsManiseqDAO.selectLcmsManiseqPageListTotCnt( commandMap);
    }

    public List selectLcmsManiseqList( Map<String, Object> commandMap) throws Exception {
        return lcmsManiseqDAO.selectLcmsManiseqList( commandMap);
    }

    public Object selectLcmsManiseq( Map<String, Object> commandMap) throws Exception {
        return lcmsManiseqDAO.selectLcmsManiseq( commandMap);
    }

    public Object insertLcmsManiseq( Map<String, Object> commandMap) throws Exception {
        return lcmsManiseqDAO.insertLcmsManiseq( commandMap);
    }

    public int updateLcmsManiseq( Map<String, Object> commandMap) throws Exception {
        return lcmsManiseqDAO.updateLcmsManiseq( commandMap);
    }

    public int updateFieldLcmsManiseq( Map<String, Object> commandMap) throws Exception {
        return lcmsManiseqDAO.updateLcmsManiseq( commandMap);
    }

    public int deleteLcmsManiseq( Map<String, Object> commandMap) throws Exception {
        return lcmsManiseqDAO.deleteLcmsManiseq( commandMap);
    }

    public int deleteLcmsManiseqAll( Map<String, Object> commandMap) throws Exception {
        return lcmsManiseqDAO.deleteLcmsManiseqAll( commandMap);
    }

    
    public Object existLcmsManiseq( LcmsManiseq lcmsManiseq) throws Exception {
        return lcmsManiseqDAO.existLcmsManiseq( lcmsManiseq);
    }

    
}
