/* 
 * LcmsCourseOrgServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsCourseOrgService;
import egovframework.adm.lcms.cts.dao.LcmsCourseOrgDAO;
import egovframework.adm.lcms.cts.domain.LcmsCourseOrg;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCourseOrgServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsCourseOrgService")
public class LcmsCourseOrgServiceImpl extends EgovAbstractServiceImpl implements LcmsCourseOrgService {
    @Resource(name="lcmsCourseOrgDAO")
    private LcmsCourseOrgDAO lcmsCourseOrgDAO;

    public List selectLcmsCourseOrgPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseOrgDAO.selectLcmsCourseOrgPageList( commandMap);
    }

    public int selectLcmsCourseOrgPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseOrgDAO.selectLcmsCourseOrgPageListTotCnt( commandMap);
    }

    public List selectLcmsCourseOrgList( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseOrgDAO.selectLcmsCourseOrgList( commandMap);
    }

    public Object selectLcmsCourseOrg( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseOrgDAO.selectLcmsCourseOrg( commandMap);
    }

    public Object insertLcmsCourseOrg( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseOrgDAO.insertLcmsCourseOrg( commandMap);
    }

    public int updateLcmsCourseOrg( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseOrgDAO.updateLcmsCourseOrg( commandMap);
    }

    public int updateFieldLcmsCourseOrg( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseOrgDAO.updateLcmsCourseOrg( commandMap);
    }

    public int deleteLcmsCourseOrg( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseOrgDAO.deleteLcmsCourseOrg( commandMap);
    }

    public int deleteLcmsCourseOrgAll( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseOrgDAO.deleteLcmsCourseOrgAll( commandMap);
    }

    
    public Object existLcmsCourseOrg( LcmsCourseOrg LcmsCourseOrg) throws Exception {
        return lcmsCourseOrgDAO.existLcmsCourseOrg( LcmsCourseOrg);
    }

    
}
