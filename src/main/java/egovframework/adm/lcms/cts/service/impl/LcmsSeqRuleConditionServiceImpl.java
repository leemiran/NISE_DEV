/* 
 * LcmsSeqRuleConditionServiceImpl.java		1.00	2011-09-16 
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

import egovframework.adm.lcms.cts.service.LcmsSeqRuleConditionService;
import egovframework.adm.lcms.cts.dao.LcmsSeqRuleConditionDAO;
import egovframework.adm.lcms.cts.domain.LcmsSeqRuleCondition;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRuleConditionServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-16 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsSeqRuleConditionService")
public class LcmsSeqRuleConditionServiceImpl extends EgovAbstractServiceImpl implements LcmsSeqRuleConditionService {
    @Resource(name="lcmsSeqRuleConditionDAO")
    private LcmsSeqRuleConditionDAO lcmsSeqRuleConditionDAO;

    public List selectLcmsSeqRuleConditionPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRuleConditionDAO.selectLcmsSeqRuleConditionPageList( commandMap);
    }

    public int selectLcmsSeqRuleConditionPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRuleConditionDAO.selectLcmsSeqRuleConditionPageListTotCnt( commandMap);
    }

    public List selectLcmsSeqRuleConditionList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRuleConditionDAO.selectLcmsSeqRuleConditionList( commandMap);
    }

    public Object selectLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRuleConditionDAO.selectLcmsSeqRuleCondition( commandMap);
    }

    public Object insertLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRuleConditionDAO.insertLcmsSeqRuleCondition( commandMap);
    }

    public int updateLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRuleConditionDAO.updateLcmsSeqRuleCondition( commandMap);
    }

    public int updateFieldLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRuleConditionDAO.updateLcmsSeqRuleCondition( commandMap);
    }

    public int deleteLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRuleConditionDAO.deleteLcmsSeqRuleCondition( commandMap);
    }

    public int deleteLcmsSeqRuleConditionAll( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRuleConditionDAO.deleteLcmsSeqRuleConditionAll( commandMap);
    }

    
    public Object existLcmsSeqRuleCondition( LcmsSeqRuleCondition lcmsSeqRuleCondition) throws Exception {
        return lcmsSeqRuleConditionDAO.existLcmsSeqRuleCondition( lcmsSeqRuleCondition);
    }

    
}
