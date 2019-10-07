/* 
 * LcmsTocDAO.java		1.00	2011-09-14 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.com.lcms.len.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsTocDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


@Repository("learningDAO")
public class LearningDAO extends EgovAbstractDAO{
    public Object selectLearningCrsInfo( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("LearningDAO.selectLearningCrsInfo", commandMap);
    }
    
    public Map selectMasterform( Map<String, Object> commandMap) throws Exception{
    	return (Map)selectByPk("LearningDAO.selectMasterform", commandMap);
    }
    
    public int selectStudyCount(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("LearningDAO.selectStudyCount", commandMap);
    }
    
    public int updateStudyCount(Map<String, Object> commandMap) throws Exception{
    	return update("LearningDAO.updateStudyCount", commandMap);
    }
    
    public Object insertStudyCount(Map<String, Object> commandMap) throws Exception{
    	return insert("LearningDAO.insertStudyCount", commandMap);
    }
    
    public List selectEduScore(Map<String, Object> commandMap) throws Exception{
    	return list("LearningDAO.selectEduScore", commandMap);
    }
    
    public List selectEduScore2(Map<String, Object> commandMap) throws Exception{
    	return list("LearningDAO.selectEduScore2", commandMap);
    }
    
    public int selectUserPage(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("LearningDAO.selectUserPage", commandMap);
    }
    
    public List SelectEduList(Map<String, Object> commandMap) throws Exception{
    	return list("LearningDAO.SelectEduList", commandMap);
    }
    
    public List selectEduTimeCountOBC(Map<String, Object> commandMap) throws Exception{
    	return list("LearningDAO.selectEduTimeCountOBC", commandMap);
    }
    
    public int isReportAssign(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("LearningDAO.isReportAssign", commandMap);
    }
    
    public List selectProjseq(Map<String, Object> commandMap) throws Exception{
    	return list("LearningDAO.selectProjseq", commandMap);
    }
    
    public Object insertProjassign(Map<String, Object> commandMap) throws Exception{
    	return insert("LearningDAO.insertProjassign", commandMap);
    }
    
    public Map selectSubjseq(Map<String, Object> commandMap) throws Exception{
    	return (Map)selectByPk("LearningDAO.selectSubjseq", commandMap);
    }
}
