/* 
 * LcmsSeqObjectivesServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsSeqObjectivesService;
import egovframework.adm.lcms.cts.dao.LcmsSeqObjectivesDAO;
import egovframework.adm.lcms.cts.domain.LcmsSeqObjectives;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqObjectivesServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsSeqObjectivesService")
public class LcmsSeqObjectivesServiceImpl extends EgovAbstractServiceImpl implements LcmsSeqObjectivesService {
    @Resource(name="lcmsSeqObjectivesDAO")
    private LcmsSeqObjectivesDAO lcmsSeqObjectivesDAO;

    public List selectLcmsSeqObjectivesPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.selectLcmsSeqObjectivesPageList( commandMap);
    }

    public int selectLcmsSeqObjectivesPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.selectLcmsSeqObjectivesPageListTotCnt( commandMap);
    }

    public List selectLcmsSeqObjectivesList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.selectLcmsSeqObjectivesList( commandMap);
    }

    public Object selectLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.selectLcmsSeqObjectives( commandMap);
    }

    public Object insertLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.insertLcmsSeqObjectives( commandMap);
    }

    public int updateLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.updateLcmsSeqObjectives( commandMap);
    }

    public int updateFieldLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.updateLcmsSeqObjectives( commandMap);
    }

    public int deleteLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.deleteLcmsSeqObjectives( commandMap);
    }

    public int deleteLcmsSeqObjectivesAll( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.deleteLcmsSeqObjectivesAll( commandMap);
    }

    
    public Object existLcmsSeqObjectives( LcmsSeqObjectives lcmsSeqObjectives) throws Exception {
        return lcmsSeqObjectivesDAO.existLcmsSeqObjectives( lcmsSeqObjectives);
    }

    public Map selectLcmsSeqObjectives01( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqObjectivesDAO.selectLcmsSeqObjectives01( commandMap);
    }
}
