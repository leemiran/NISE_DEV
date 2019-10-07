/* 
 * LcmsSeqObjectivesDAO.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.adm.lcms.cts.domain.LcmsSeqObjectives;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqObjectivesDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsSeqObjectivesDAO")
public class LcmsSeqObjectivesDAO extends EgovAbstractDAO{
    public List selectLcmsSeqObjectivesPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsSeqObjectivesDAO.selectLcmsSeqObjectivesPageList", commandMap);
    }

    public int  selectLcmsSeqObjectivesPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqObjectivesDAO.selectLcmsSeqObjectivesPageListTotCnt", commandMap);
    }
    
    public int  selectLcmsSeqObjectivesMaxNum( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqObjectivesDAO.selectLcmsSeqObjectivesMaxNum", commandMap);
    }

    public List selectLcmsSeqObjectivesList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsSeqObjectivesDAO.selectLcmsSeqObjectivesPageList", commandMap);
    }

    public Object selectLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqObjectivesDAO.selectLcmsSeqObjectives", commandMap);
    }

    public Object insertLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsSeqObjectivesDAO.insertLcmsSeqObjectives", commandMap);
    }

    public int updateLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqObjectivesDAO.updateLcmsSeqObjectives", commandMap);
    }

    public int updateFieldLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqObjectivesDAO.updateFieldLcmsSeqObjectives", commandMap);
    }

    public int deleteLcmsSeqObjectives( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqObjectivesDAO.deleteLcmsSeqObjectives", commandMap);
    }

    public int deleteLcmsSeqObjectivesAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqObjectivesDAO.deleteAllLcmsSeqObjectives", commandMap);
    }

    public Object existLcmsSeqObjectives( LcmsSeqObjectives lcmsSeqObjectives) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqObjectivesDAO.existLcmsSeqObjectives", lcmsSeqObjectives);
    }
    /**
     * 학습창 
     * @param commandMap
     * @return
     * @throws Exception
     */
    public Map selectLcmsSeqObjectives01( Map<String, Object> commandMap) throws Exception{;
    	//return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqObjectivesDAO.selectLcmsSeqObjectives01", commandMap);
    	return (Map)selectByPk("lcmsSeqObjectivesDAO.selectLcmsSeqObjectives01", commandMap);
    }

}
