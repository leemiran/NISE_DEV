/* 
 * LcmsSeqRollupRuleServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsSeqRollupRuleService;
import egovframework.adm.lcms.cts.dao.LcmsSeqRollupRuleDAO;
import egovframework.adm.lcms.cts.domain.LcmsSeqRollupRule;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRollupRuleServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsSeqRollupRuleService")
public class LcmsSeqRollupRuleServiceImpl extends EgovAbstractServiceImpl implements LcmsSeqRollupRuleService {
    @Resource(name="lcmsSeqRollupRuleDAO")
    private LcmsSeqRollupRuleDAO lcmsSeqRollupRuleDAO;

    public List selectLcmsSeqRollupRulePageList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRulePageList( commandMap);
    }

    public int selectLcmsSeqRollupRulePageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRulePageListTotCnt( commandMap);
    }

    public List selectLcmsSeqRollupRuleList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRuleList( commandMap);
    }

    public Object selectLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRule( commandMap);
    }

    public Object insertLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupRuleDAO.insertLcmsSeqRollupRule( commandMap);
    }

    public int updateLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupRuleDAO.updateLcmsSeqRollupRule( commandMap);
    }

    public int updateFieldLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupRuleDAO.updateLcmsSeqRollupRule( commandMap);
    }

    public int deleteLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupRuleDAO.deleteLcmsSeqRollupRule( commandMap);
    }

    public int deleteLcmsSeqRollupRuleAll( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqRollupRuleDAO.deleteLcmsSeqRollupRuleAll( commandMap);
    }

    
    public Object existLcmsSeqRollupRule( LcmsSeqRollupRule lcmsSeqRollupRule) throws Exception {
        return lcmsSeqRollupRuleDAO.existLcmsSeqRollupRule( lcmsSeqRollupRule);
    }

    
}
