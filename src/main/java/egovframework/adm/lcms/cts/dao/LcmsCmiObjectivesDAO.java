/* 
 * LcmsCmiObjectivesDAO.java		1.00	2011-09-26 
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
import egovframework.adm.lcms.cts.domain.LcmsCmiObjectives;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiObjectivesDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsCmiObjectivesDAO")
public class LcmsCmiObjectivesDAO extends EgovAbstractDAO{
    public List selectLcmsCmiObjectivesPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsCmiObjectivesDAO.selectLcmsCmiObjectivesPageList", commandMap);
    }

    public int  selectLcmsCmiObjectivesPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCmiObjectivesDAO.selectLcmsCmiObjectivesPageListTotCnt", commandMap);
    }

    public List selectLcmsCmiObjectivesList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsCmiObjectivesDAO.selectLcmsCmiObjectivesList", commandMap);
    }

    public Object selectLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiObjectivesDAO.selectLcmsCmiObjectives", commandMap);
    }

    public Object insertLcmsCmiObjectives(LcmsCmiObjectives lcmsCmiObjectives) throws Exception{
        return insert("lcmsCmiObjectivesDAO.insertLcmsCmiObjectives", lcmsCmiObjectives);
    }

    public int updateLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiObjectivesDAO.updateLcmsCmiObjectives", commandMap);
    }

    public int updateFieldLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiObjectivesDAO.updateFieldLcmsCmiObjectives", commandMap);
    }

    public int deleteLcmsCmiObjectives( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiObjectivesDAO.deleteLcmsCmiObjectives", commandMap);
    }

    public int deleteLcmsCmiObjectivesAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiObjectivesDAO.deleteAllLcmsCmiObjectives", commandMap);
    }

    public Object existLcmsCmiObjectives( LcmsCmiObjectives lcmsCmiObjectives) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiObjectivesDAO.existLcmsCmiObjectives", lcmsCmiObjectives);
    }

}
