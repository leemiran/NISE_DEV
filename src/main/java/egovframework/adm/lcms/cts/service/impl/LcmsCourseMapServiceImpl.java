/* 
 * LcmsCourseMapServiceImpl.java		1.00	2011-09-19 
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

import egovframework.adm.lcms.cts.service.LcmsCourseMapService;
import egovframework.adm.lcms.cts.dao.LcmsCourseMapDAO;
import egovframework.adm.lcms.cts.domain.LcmsCourseMap;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCourseMapServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-19 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsCourseMapService")
public class LcmsCourseMapServiceImpl extends EgovAbstractServiceImpl implements LcmsCourseMapService {
    @Resource(name="lcmsCourseMapDAO")
    private LcmsCourseMapDAO lcmsCourseMapDAO;

    public List selectLcmsCourseMapPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseMapDAO.selectLcmsCourseMapPageList( commandMap);
    }

    public int selectLcmsCourseMapPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseMapDAO.selectLcmsCourseMapPageListTotCnt( commandMap);
    }

    public List selectLcmsCourseMapList( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseMapDAO.selectLcmsCourseMapList( commandMap);
    }

    public Object selectLcmsCourseMap( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseMapDAO.selectLcmsCourseMap( commandMap);
    }

    public Object insertLcmsCourseMap( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseMapDAO.insertLcmsCourseMap( commandMap);
    }

    public int updateLcmsCourseMap( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseMapDAO.updateLcmsCourseMap( commandMap);
    }

    public int updateFieldLcmsCourseMap( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseMapDAO.updateLcmsCourseMap( commandMap);
    }

    public int deleteLcmsCourseMap( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseMapDAO.deleteLcmsCourseMap( commandMap);
    }

    public int deleteLcmsCourseMapAll( Map<String, Object> commandMap) throws Exception {
        return lcmsCourseMapDAO.deleteLcmsCourseMapAll( commandMap);
    }

    
    public Object existLcmsCourseMap( LcmsCourseMap lcmsCourseMap) throws Exception {
        return lcmsCourseMapDAO.existLcmsCourseMap( lcmsCourseMap);
    }
    
    public int getCourseMapSeq(Map<String, Object> commandMap) throws Exception{
    	return lcmsCourseMapDAO.getCourseMapSeq(commandMap);
    }

    
}
