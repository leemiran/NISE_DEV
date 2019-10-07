/* 
 * LcmsModuleServiceImpl.java		1.00	2011-10-11 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.nct.service.impl;

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

import egovframework.adm.lcms.nct.service.LcmsModuleService;
import egovframework.adm.lcms.nct.dao.LcmsLessonDAO;
import egovframework.adm.lcms.nct.dao.LcmsModuleDAO;
import egovframework.adm.lcms.nct.domain.LcmsModule;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsModuleServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsModuleService")
public class LcmsModuleServiceImpl extends EgovAbstractServiceImpl implements LcmsModuleService {
    @Resource(name="lcmsModuleDAO")
    private LcmsModuleDAO lcmsModuleDAO;
    @Resource(name="lcmsLessonDAO")
    private LcmsLessonDAO lcmsLessonDAO;

    public List selectLcmsModulePageList( Map<String, Object> commandMap) throws Exception {
        return lcmsModuleDAO.selectLcmsModulePageList( commandMap);
    }

    public int selectLcmsModulePageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsModuleDAO.selectLcmsModulePageListTotCnt( commandMap);
    }

    public List selectLcmsModuleList( Map<String, Object> commandMap) throws Exception {
        return lcmsModuleDAO.selectLcmsModuleList( commandMap);
    }

    public Object selectLcmsModule( Map<String, Object> commandMap) throws Exception {
        return lcmsModuleDAO.selectLcmsModule( commandMap);
    }

    public Object insertLcmsModule( Map<String, Object> commandMap) throws Exception {
        return lcmsModuleDAO.insertLcmsModule( commandMap);
    }

    public int updateLcmsModule( Map<String, Object> commandMap) throws Exception {
        return lcmsModuleDAO.updateLcmsModule( commandMap);
    }

    public int updateFieldLcmsModule( Map<String, Object> commandMap) throws Exception {
        return lcmsModuleDAO.updateLcmsModule( commandMap);
    }

    public int deleteLcmsModule( Map<String, Object> commandMap) throws Exception {
    	int isOk = 1;
    	try{
    		lcmsModuleDAO.deleteLcmsModule( commandMap);
    		lcmsLessonDAO.deleteLcmsLessonAll( commandMap);
    	}catch(Exception ex){
    		isOk = 0;
    		ex.printStackTrace();
    	}
        return isOk;
    }

    public int deleteLcmsModuleAll( Map<String, Object> commandMap) throws Exception {
        return lcmsModuleDAO.deleteLcmsModuleAll( commandMap);
    }

    
    public Object existLcmsModule( LcmsModule lcmsModule) throws Exception {
        return lcmsModuleDAO.existLcmsModule( lcmsModule);
    }
    
    public List selectModulePath(Map<String, Object> commandMap) throws Exception{
    	return lcmsModuleDAO.selectModulePath(commandMap);
    }

    
    public Map selectLcmsModuleData(Map<String, Object> commandMap) throws Exception{
    	return lcmsModuleDAO.selectLcmsModuleData(commandMap);
    }
    
    public Map selectSaveInfoData(Map<String, Object> commandMap) throws Exception{
    	return lcmsModuleDAO.selectSaveInfoData(commandMap);
    }
    
}
