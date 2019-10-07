/* 
 * LcmsScormSequenceServiceImpl.java		1.00	2011-09-05 
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

import egovframework.adm.lcms.cts.service.LcmsScormSequenceService;
import egovframework.adm.lcms.cts.dao.LcmsScormSequenceDAO;
import egovframework.adm.lcms.cts.domain.LcmsScormSequence;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsScormSequenceServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsScormSequenceService")
public class LcmsScormSequenceServiceImpl extends EgovAbstractServiceImpl implements LcmsScormSequenceService {
    @Resource(name="lcmsScormSequenceDAO")
    private LcmsScormSequenceDAO lcmsScormSequenceDAO;

    public List selectLcmsScormSequencePageList( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.selectLcmsScormSequencePageList( commandMap);
    }

    public int selectLcmsScormSequencePageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.selectLcmsScormSequencePageListTotCnt( commandMap);
    }

    public List selectLcmsScormSequenceList( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.selectLcmsScormSequenceList( commandMap);
    }

    public Object selectLcmsScormSequence( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.selectLcmsScormSequence( commandMap);
    }

    public Object insertLcmsScormSequence( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.insertLcmsScormSequence( commandMap);
    }

    public int updateLcmsScormSequence( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.updateLcmsScormSequence( commandMap);
    }

    public int updateFieldLcmsScormSequence( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.updateLcmsScormSequence( commandMap);
    }

    public int deleteLcmsScormSequence( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.deleteLcmsScormSequence( commandMap);
    }

    public int deleteLcmsScormSequenceAll( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.deleteLcmsScormSequenceAll( commandMap);
    }

    
    public Object existLcmsScormSequence( LcmsScormSequence lcmsScormSequence) throws Exception {
        return lcmsScormSequenceDAO.existLcmsScormSequence( lcmsScormSequence);
    }
    
    public Map selectLcmsScormSequenceSeqIdxNum( Map<String, Object> commandMap) throws Exception {
        return lcmsScormSequenceDAO.selectLcmsScormSequenceSeqIdxNum( commandMap);
    }
}
