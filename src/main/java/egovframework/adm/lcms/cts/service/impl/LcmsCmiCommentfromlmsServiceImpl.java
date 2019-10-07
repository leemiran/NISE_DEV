/* 
 * LcmsCmiCommentfromlmsServiceImpl.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.service.LcmsCmiCommentfromlmsService;
import egovframework.adm.lcms.cts.dao.LcmsCmiCommentfromlmsDAO;
import egovframework.adm.lcms.cts.domain.LcmsCmiCommentfromlms;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiCommentfromlmsServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsCmiCommentfromlmsService")
public class LcmsCmiCommentfromlmsServiceImpl extends EgovAbstractServiceImpl implements LcmsCmiCommentfromlmsService {
    @Resource(name="lcmsCmiCommentfromlmsDAO")
    private LcmsCmiCommentfromlmsDAO lcmsCmiCommentfromlmsDAO;

    public List selectLcmsCmiCommentfromlmsPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentfromlmsDAO.selectLcmsCmiCommentfromlmsPageList( commandMap);
    }

    public int selectLcmsCmiCommentfromlmsPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentfromlmsDAO.selectLcmsCmiCommentfromlmsPageListTotCnt( commandMap);
    }

    public List selectLcmsCmiCommentfromlmsList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentfromlmsDAO.selectLcmsCmiCommentfromlmsList( commandMap);
    }

    public Object selectLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentfromlmsDAO.selectLcmsCmiCommentfromlms( commandMap);
    }

    public Object insertLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentfromlmsDAO.insertLcmsCmiCommentfromlms( commandMap);
    }

    public int updateLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentfromlmsDAO.updateLcmsCmiCommentfromlms( commandMap);
    }

    public int updateFieldLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentfromlmsDAO.updateLcmsCmiCommentfromlms( commandMap);
    }

    public int deleteLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentfromlmsDAO.deleteLcmsCmiCommentfromlms( commandMap);
    }

    public int deleteLcmsCmiCommentfromlmsAll( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentfromlmsDAO.deleteLcmsCmiCommentfromlmsAll( commandMap);
    }

    
    public Object existLcmsCmiCommentfromlms( LcmsCmiCommentfromlms lcmsCmiCommentfromlms) throws Exception {
        return lcmsCmiCommentfromlmsDAO.existLcmsCmiCommentfromlms( lcmsCmiCommentfromlms);
    }

    
}
