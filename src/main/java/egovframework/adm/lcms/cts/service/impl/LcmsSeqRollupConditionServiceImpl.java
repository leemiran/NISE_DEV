/* 
 * LcmsSeqRollupConditionServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsSeqRollupConditionService;
import egovframework.adm.lcms.cts.dao.LcmsSeqRollupConditionDAO;
import egovframework.adm.lcms.cts.domain.LcmsSeqRollupCondition;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRollupConditionServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsSeqRollupConditionService")
public class LcmsSeqRollupConditionServiceImpl extends EgovAbstractServiceImpl implements LcmsSeqRollupConditionService {
    @Resource(name="lcmsSeqRollupConditionDAO")
    private LcmsSeqRollupConditionDAO lcmsSeqRollupConditionDAO;

    public List selectLcmsSeqRollupConditionPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupConditionDAO.selectLcmsSeqRollupConditionPageList( commandMap);
    }

    public int selectLcmsSeqRollupConditionPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupConditionDAO.selectLcmsSeqRollupConditionPageListTotCnt( commandMap);
    }

    public List selectLcmsSeqRollupConditionList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupConditionDAO.selectLcmsSeqRollupConditionList( commandMap);
    }

    public Object selectLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupConditionDAO.selectLcmsSeqRollupCondition( commandMap);
    }

    public Object insertLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupConditionDAO.insertLcmsSeqRollupCondition( commandMap);
    }

    public int updateLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupConditionDAO.updateLcmsSeqRollupCondition( commandMap);
    }

    public int updateFieldLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupConditionDAO.updateLcmsSeqRollupCondition( commandMap);
    }

    public int deleteLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupConditionDAO.deleteLcmsSeqRollupCondition( commandMap);
    }

    public int deleteLcmsSeqRollupConditionAll( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupConditionDAO.deleteLcmsSeqRollupConditionAll( commandMap);
    }

    
    public Object existLcmsSeqRollupCondition( LcmsSeqRollupCondition lcmsSeqRollupCondition) throws Exception {
        return lcmsSeqRollupConditionDAO.existLcmsSeqRollupCondition( lcmsSeqRollupCondition);
    }

    
}
