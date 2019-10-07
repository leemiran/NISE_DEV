/* 
 * LcmsSeqRollupConditionDAO.java		1.00	2011-09-05 
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
import egovframework.adm.lcms.cts.domain.LcmsSeqRollupCondition;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRollupConditionDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsSeqRollupConditionDAO")
public class LcmsSeqRollupConditionDAO extends EgovAbstractDAO{
    public List selectLcmsSeqRollupConditionPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsSeqRollupConditionDAO.selectLcmsSeqRollupConditionPageList", commandMap);
    }

    public int  selectLcmsSeqRollupConditionPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqRollupConditionDAO.selectLcmsSeqRollupConditionPageListTotCnt", commandMap);
    }
    
    public int  selectLcmsSeqRollupConditionMaxNum( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqRollupConditionDAO.selectLcmsSeqRollupConditionMaxNum", commandMap);
    }

    public List selectLcmsSeqRollupConditionList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsSeqRollupConditionDAO.selectLcmsSeqRollupConditionPageList", commandMap);
    }

    public Object selectLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqRollupConditionDAO.selectLcmsSeqRollupCondition", commandMap);
    }

    public Object insertLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsSeqRollupConditionDAO.insertLcmsSeqRollupCondition", commandMap);
    }

    public int updateLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqRollupConditionDAO.updateLcmsSeqRollupCondition", commandMap);
    }

    public int updateFieldLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqRollupConditionDAO.updateFieldLcmsSeqRollupCondition", commandMap);
    }

    public int deleteLcmsSeqRollupCondition( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqRollupConditionDAO.deleteLcmsSeqRollupCondition", commandMap);
    }

    public int deleteLcmsSeqRollupConditionAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqRollupConditionDAO.deleteAllLcmsSeqRollupCondition", commandMap);
    }

    public Object existLcmsSeqRollupCondition( LcmsSeqRollupCondition lcmsSeqRollupCondition) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqRollupConditionDAO.existLcmsSeqRollupCondition", lcmsSeqRollupCondition);
    }

}
