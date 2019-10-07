/* 
 * LcmsOrganizationLogServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsOrganizationLogService;
import egovframework.adm.lcms.cts.dao.LcmsOrganizationLogDAO;
import egovframework.adm.lcms.cts.domain.LcmsOrganizationLog;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsOrganizationLogServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsOrganizationLogService")
public class LcmsOrganizationLogServiceImpl extends EgovAbstractServiceImpl implements LcmsOrganizationLogService {
    @Resource(name="lcmsOrganizationLogDAO")
    private LcmsOrganizationLogDAO lcmsOrganizationLogDAO;

    public List selectLcmsOrganizationLogPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationLogDAO.selectLcmsOrganizationLogPageList( commandMap);
    }

    public int selectLcmsOrganizationLogPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationLogDAO.selectLcmsOrganizationLogPageListTotCnt( commandMap);
    }

    public List selectLcmsOrganizationLogList( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationLogDAO.selectLcmsOrganizationLogList( commandMap);
    }

    public Object selectLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationLogDAO.selectLcmsOrganizationLog( commandMap);
    }

    public Object insertLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationLogDAO.insertLcmsOrganizationLog( commandMap);
    }

    public int updateLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationLogDAO.updateLcmsOrganizationLog( commandMap);
    }

    public int updateFieldLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationLogDAO.updateLcmsOrganizationLog( commandMap);
    }

    public int deleteLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationLogDAO.deleteLcmsOrganizationLog( commandMap);
    }

    public int deleteLcmsOrganizationLogAll( Map<String, Object> commandMap) throws Exception {
        return lcmsOrganizationLogDAO.deleteLcmsOrganizationLogAll( commandMap);
    }

    
    public Object existLcmsOrganizationLog( LcmsOrganizationLog lcmsOrganizationLog) throws Exception {
        return lcmsOrganizationLogDAO.existLcmsOrganizationLog( lcmsOrganizationLog);
    }

    
}
