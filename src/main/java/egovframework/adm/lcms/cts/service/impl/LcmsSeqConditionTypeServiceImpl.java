/* 
 * LcmsSeqConditionTypeServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsSeqConditionTypeService;
import egovframework.adm.lcms.cts.dao.LcmsSeqConditionTypeDAO;
import egovframework.adm.lcms.cts.domain.LcmsSeqConditionType;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqConditionTypeServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsSeqConditionTypeService")
public class LcmsSeqConditionTypeServiceImpl extends EgovAbstractServiceImpl implements LcmsSeqConditionTypeService {
    @Resource(name="lcmsSeqConditionTypeDAO")
    private LcmsSeqConditionTypeDAO lcmsSeqConditionTypeDAO;

    public List selectLcmsSeqConditionTypePageList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqConditionTypeDAO.selectLcmsSeqConditionTypePageList( commandMap);
    }

    public int selectLcmsSeqConditionTypePageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqConditionTypeDAO.selectLcmsSeqConditionTypePageListTotCnt( commandMap);
    }

    public List selectLcmsSeqConditionTypeList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqConditionTypeDAO.selectLcmsSeqConditionTypeList( commandMap);
    }

    public Object selectLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqConditionTypeDAO.selectLcmsSeqConditionType( commandMap);
    }

    public Object insertLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqConditionTypeDAO.insertLcmsSeqConditionType( commandMap);
    }

    public int updateLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqConditionTypeDAO.updateLcmsSeqConditionType( commandMap);
    }

    public int updateFieldLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqConditionTypeDAO.updateLcmsSeqConditionType( commandMap);
    }

    public int deleteLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqConditionTypeDAO.deleteLcmsSeqConditionType( commandMap);
    }

    public int deleteLcmsSeqConditionTypeAll( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqConditionTypeDAO.deleteLcmsSeqConditionTypeAll( commandMap);
    }

    
    public Object existLcmsSeqConditionType( LcmsSeqConditionType lcmsSeqConditionType) throws Exception {
        return lcmsSeqConditionTypeDAO.existLcmsSeqConditionType( lcmsSeqConditionType);
    }

    
}
