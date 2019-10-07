/* 
 * LcmsCmiCommentlearnerServiceImpl.java		1.00	2011-09-26 
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

import egovframework.adm.lcms.cts.service.LcmsCmiCommentlearnerService;
import egovframework.adm.lcms.cts.dao.LcmsCmiCommentlearnerDAO;
import egovframework.adm.lcms.cts.domain.LcmsCmiCommentlearner;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiCommentlearnerServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsCmiCommentlearnerService")
public class LcmsCmiCommentlearnerServiceImpl extends EgovAbstractServiceImpl implements LcmsCmiCommentlearnerService {
    @Resource(name="lcmsCmiCommentlearnerDAO")
    private LcmsCmiCommentlearnerDAO lcmsCmiCommentlearnerDAO;

    public List selectLcmsCmiCommentlearnerPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentlearnerDAO.selectLcmsCmiCommentlearnerPageList( commandMap);
    }

    public int selectLcmsCmiCommentlearnerPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentlearnerDAO.selectLcmsCmiCommentlearnerPageListTotCnt( commandMap);
    }

    public List selectLcmsCmiCommentlearnerList( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentlearnerDAO.selectLcmsCmiCommentlearnerList( commandMap);
    }

    public Object selectLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentlearnerDAO.selectLcmsCmiCommentlearner( commandMap);
    }

    public Object insertLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentlearnerDAO.insertLcmsCmiCommentlearner( commandMap);
    }

    public int updateLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentlearnerDAO.updateLcmsCmiCommentlearner( commandMap);
    }

    public int updateFieldLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentlearnerDAO.updateLcmsCmiCommentlearner( commandMap);
    }

    public int deleteLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentlearnerDAO.deleteLcmsCmiCommentlearner( commandMap);
    }

    public int deleteLcmsCmiCommentlearnerAll( Map<String, Object> commandMap) throws Exception {
        return lcmsCmiCommentlearnerDAO.deleteLcmsCmiCommentlearnerAll( commandMap);
    }

    
    public Object existLcmsCmiCommentlearner( LcmsCmiCommentlearner lcmsCmiCommentlearner) throws Exception {
        return lcmsCmiCommentlearnerDAO.existLcmsCmiCommentlearner( lcmsCmiCommentlearner);
    }

    
}
