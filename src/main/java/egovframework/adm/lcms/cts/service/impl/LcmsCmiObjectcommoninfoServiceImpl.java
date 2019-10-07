/* 
 * LcmsCmiObjectcommoninfoServiceImpl.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.service.LcmsCmiObjectcommoninfoService;
import egovframework.adm.lcms.cts.dao.LcmsCmiObjectcommoninfoDAO;
import egovframework.adm.lcms.cts.domain.LcmsCmiObjectcommoninfo;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiObjectcommoninfoServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsCmiObjectcommoninfoService")
public class LcmsCmiObjectcommoninfoServiceImpl extends EgovAbstractServiceImpl implements LcmsCmiObjectcommoninfoService {
    @Resource(name="lcmsCmiObjectcommoninfoDAO")
    private LcmsCmiObjectcommoninfoDAO lcmsCmiObjectcommoninfoDAO;

    public List selectLcmsCmiObjectcommoninfoPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.selectLcmsCmiObjectcommoninfoPageList( commandMap);
    }

    public int selectLcmsCmiObjectcommoninfoPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.selectLcmsCmiObjectcommoninfoPageListTotCnt( commandMap);
    }

    public List selectLcmsCmiObjectcommoninfoList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.selectLcmsCmiObjectcommoninfoList( commandMap);
    }

    public Object selectLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.selectLcmsCmiObjectcommoninfo( commandMap);
    }

    public Object insertLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.insertLcmsCmiObjectcommoninfo( commandMap);
    }

    public int updateLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.updateLcmsCmiObjectcommoninfo( commandMap);
    }

    public int updateFieldLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.updateLcmsCmiObjectcommoninfo( commandMap);
    }

    public int deleteLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.deleteLcmsCmiObjectcommoninfo( commandMap);
    }

    public int deleteLcmsCmiObjectcommoninfoAll( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.deleteLcmsCmiObjectcommoninfoAll( commandMap);
    }

    
    public Object existLcmsCmiObjectcommoninfo( LcmsCmiObjectcommoninfo lcmsCmiObjectcommoninfo) throws Exception {
        return lcmsCmiObjectcommoninfoDAO.existLcmsCmiObjectcommoninfo( lcmsCmiObjectcommoninfo);
    }

    
}
