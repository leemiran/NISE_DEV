/* 
 * LcmsSeqMapInfoServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsSeqMapInfoService;
import egovframework.adm.lcms.cts.dao.LcmsSeqMapInfoDAO;
import egovframework.adm.lcms.cts.domain.LcmsSeqMapInfo;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqMapInfoServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsSeqMapInfoService")
public class LcmsSeqMapInfoServiceImpl extends EgovAbstractServiceImpl implements LcmsSeqMapInfoService {
    @Resource(name="lcmsSeqMapInfoDAO")
    private LcmsSeqMapInfoDAO lcmsSeqMapInfoDAO;

    public List selectLcmsSeqMapInfoPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqMapInfoDAO.selectLcmsSeqMapInfoPageList( commandMap);
    }

    public int selectLcmsSeqMapInfoPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqMapInfoDAO.selectLcmsSeqMapInfoPageListTotCnt( commandMap);
    }

    public List selectLcmsSeqMapInfoList( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqMapInfoDAO.selectLcmsSeqMapInfoList( commandMap);
    }

    public Object selectLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqMapInfoDAO.selectLcmsSeqMapInfo( commandMap);
    }

    public Object insertLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqMapInfoDAO.insertLcmsSeqMapInfo( commandMap);
    }

    public int updateLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqMapInfoDAO.updateLcmsSeqMapInfo( commandMap);
    }

    public int updateFieldLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqMapInfoDAO.updateLcmsSeqMapInfo( commandMap);
    }

    public int deleteLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqMapInfoDAO.deleteLcmsSeqMapInfo( commandMap);
    }

    public int deleteLcmsSeqMapInfoAll( Map<String, Object> commandMap) throws Exception {
        return lcmsSeqMapInfoDAO.deleteLcmsSeqMapInfoAll( commandMap);
    }

    
    public Object existLcmsSeqMapInfo( LcmsSeqMapInfo lcmsSeqMapInfo) throws Exception {
        return lcmsSeqMapInfoDAO.existLcmsSeqMapInfo( lcmsSeqMapInfo);
    }

    
}
