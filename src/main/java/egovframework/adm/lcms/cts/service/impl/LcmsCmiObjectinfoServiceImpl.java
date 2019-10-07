/* 
 * LcmsCmiObjectinfoServiceImpl.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.service.LcmsCmiObjectinfoService;
import egovframework.adm.lcms.cts.dao.LcmsCmiObjectinfoDAO;
import egovframework.adm.lcms.cts.domain.LcmsCmiObjectinfo;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiObjectinfoServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsCmiObjectinfoService")
public class LcmsCmiObjectinfoServiceImpl extends EgovAbstractServiceImpl implements LcmsCmiObjectinfoService {
    @Resource(name="lcmsCmiObjectinfoDAO")
    private LcmsCmiObjectinfoDAO lcmsCmiObjectinfoDAO;

    public List selectLcmsCmiObjectinfoPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectinfoDAO.selectLcmsCmiObjectinfoPageList( commandMap);
    }

    public int selectLcmsCmiObjectinfoPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectinfoDAO.selectLcmsCmiObjectinfoPageListTotCnt( commandMap);
    }

    public List selectLcmsCmiObjectinfoList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectinfoDAO.selectLcmsCmiObjectinfoList( commandMap);
    }

    public Map selectLcmsCmiObjectinfo( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectinfoDAO.selectLcmsCmiObjectinfo( commandMap);
    }

    public Object insertLcmsCmiObjectinfo( LcmsCmiObjectinfo lcmsCmiObjectinfo) throws Exception {
        return lcmsCmiObjectinfoDAO.insertLcmsCmiObjectinfo( lcmsCmiObjectinfo);
    }

    public int updateLcmsCmiObjectinfo( LcmsCmiObjectinfo lcmsCmiObjectinfo) throws Exception {
        return lcmsCmiObjectinfoDAO.updateLcmsCmiObjectinfo( lcmsCmiObjectinfo);
    }

    public int updateFieldLcmsCmiObjectinfo( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectinfoDAO.updateFieldLcmsCmiObjectinfo( commandMap);
    }

    public int deleteLcmsCmiObjectinfo( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectinfoDAO.deleteLcmsCmiObjectinfo( commandMap);
    }

    public int deleteLcmsCmiObjectinfoAll( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectinfoDAO.deleteLcmsCmiObjectinfoAll( commandMap);
    }

    
    public Object existLcmsCmiObjectinfo( LcmsCmiObjectinfo lcmsCmiObjectinfo) throws Exception {
        return lcmsCmiObjectinfoDAO.existLcmsCmiObjectinfo( lcmsCmiObjectinfo);
    }
    
    public String checkScormEduLimit(Map<String, Object> commandMap) throws Exception{
    	return lcmsCmiObjectinfoDAO.checkScormEduLimit(commandMap);
    }

    
}
