/* 
 * LcmsLessonServiceImpl.java		1.00	2011-10-11 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.nct.service.impl;

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

import egovframework.adm.lcms.nct.service.LcmsLessonService;
import egovframework.adm.lcms.nct.dao.LcmsLessonDAO;
import egovframework.adm.lcms.nct.domain.LcmsLesson;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsLessonServiceImpl.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


@Service("lcmsLessonService")
public class LcmsLessonServiceImpl extends EgovAbstractServiceImpl implements LcmsLessonService {
    @Resource(name="lcmsLessonDAO")
    private LcmsLessonDAO lcmsLessonDAO;

    public List selectLcmsLessonPageList( Map<String, Object> commandMap) throws Exception {
        return lcmsLessonDAO.selectLcmsLessonPageList( commandMap);
    }

    public int selectLcmsLessonPageListTotCnt( Map<String, Object> commandMap) throws Exception {
        return lcmsLessonDAO.selectLcmsLessonPageListTotCnt( commandMap);
    }

    public List selectLcmsLessonList( Map<String, Object> commandMap) throws Exception {
        return lcmsLessonDAO.selectLcmsLessonList( commandMap);
    }

    public Object selectLcmsLesson( Map<String, Object> commandMap) throws Exception {
        return lcmsLessonDAO.selectLcmsLesson( commandMap);
    }

    public Object insertLcmsLesson( Map<String, Object> commandMap) throws Exception {
        return lcmsLessonDAO.insertLcmsLesson( commandMap);
    }

    public int updateLcmsLesson( Map<String, Object> commandMap) throws Exception {
        return lcmsLessonDAO.updateLcmsLesson( commandMap);
    }

    public int updateFieldLcmsLesson( Map<String, Object> commandMap) throws Exception {
        return lcmsLessonDAO.updateLcmsLesson( commandMap);
    }

    public int deleteLcmsLesson( Map<String, Object> commandMap) throws Exception {
        return lcmsLessonDAO.deleteLcmsLesson( commandMap);
    }

    public int deleteLcmsLessonAll( Map<String, Object> commandMap) throws Exception {
        return lcmsLessonDAO.deleteLcmsLessonAll( commandMap);
    }

    
    public Object existLcmsLesson( LcmsLesson lcmsLesson) throws Exception {
        return lcmsLessonDAO.existLcmsLesson( lcmsLesson);
    }
    
    
    public Map selectLessonData(Map<String, Object> commandMap) throws Exception{
    	return lcmsLessonDAO.selectLessonData(commandMap);
    }

    
}
