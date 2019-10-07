/* 
 * LcmsCmiObjectivesServiceImpl.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.service.LcmsCmiObjectivesService;
import egovframework.adm.lcms.cts.dao.LcmsCmiObjectivesDAO;
import egovframework.adm.lcms.cts.domain.LcmsCmiObjectives;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiObjectivesServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsCmiObjectivesService")
public class LcmsCmiObjectivesServiceImpl extends EgovAbstractServiceImpl implements LcmsCmiObjectivesService {
    @Resource(name="lcmsCmiObjectivesDAO")
    private LcmsCmiObjectivesDAO lcmsCmiObjectivesDAO;

    public List selectLcmsCmiObjectivesPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectivesDAO.selectLcmsCmiObjectivesPageList( commandMap);
    }

    public int selectLcmsCmiObjectivesPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectivesDAO.selectLcmsCmiObjectivesPageListTotCnt( commandMap);
    }

    public List selectLcmsCmiObjectivesList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectivesDAO.selectLcmsCmiObjectivesList( commandMap);
    }

    public Object selectLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectivesDAO.selectLcmsCmiObjectives( commandMap);
    }

    public Object insertLcmsCmiObjectives( Map<String, Object> commandMap,List list) throws Exception {
    	    Map<String,Object> map = (Map)commandMap.get("delMap");
    	    lcmsCmiObjectivesDAO.deleteLcmsCmiObjectives(map);
    	    
    	    Object result = null;
    	    
    	    for (Object o : list) {
    	    	  result = lcmsCmiObjectivesDAO.insertLcmsCmiObjectives((LcmsCmiObjectives)o);
    	    }

        return result;
    }

    public int updateLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectivesDAO.updateLcmsCmiObjectives( commandMap);
    }

    public int updateFieldLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectivesDAO.updateLcmsCmiObjectives( commandMap);
    }

    public int deleteLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectivesDAO.deleteLcmsCmiObjectives( commandMap);
    }

    public int deleteLcmsCmiObjectivesAll( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiObjectivesDAO.deleteLcmsCmiObjectivesAll( commandMap);
    }

    
    public Object existLcmsCmiObjectives( LcmsCmiObjectives lcmsCmiObjectives) throws Exception {
        return lcmsCmiObjectivesDAO.existLcmsCmiObjectives( lcmsCmiObjectives);
    }

    
}
