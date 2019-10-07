/* 
 * LcmsSeqRuleConditionDAO.java		1.00	2011-09-16 
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
import egovframework.adm.lcms.cts.domain.LcmsSeqRuleCondition;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRuleConditionDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-16 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsSeqRuleConditionDAO")
public class LcmsSeqRuleConditionDAO extends EgovAbstractDAO{
    public List selectLcmsSeqRuleConditionPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsSeqRuleConditionDAO.selectLcmsSeqRuleConditionPageList", commandMap);
    }

    public int  selectLcmsSeqRuleConditionPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqRuleConditionDAO.selectLcmsSeqRuleConditionPageListTotCnt", commandMap);
    }
    
    public int  selectLcmsSeqRuleConditionMaxNum( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqRuleConditionDAO.selectLcmsSeqRuleConditionMaxNum", commandMap);
    }

    public List selectLcmsSeqRuleConditionList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsSeqRuleConditionDAO.selectLcmsSeqRuleConditionList", commandMap);
    }

    public Object selectLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqRuleConditionDAO.selectLcmsSeqRuleCondition", commandMap);
    }

    public Object insertLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsSeqRuleConditionDAO.insertLcmsSeqRuleCondition", commandMap);
    }

    public int updateLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqRuleConditionDAO.updateLcmsSeqRuleCondition", commandMap);
    }

    public int updateFieldLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqRuleConditionDAO.updateFieldLcmsSeqRuleCondition", commandMap);
    }

    public int deleteLcmsSeqRuleCondition( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqRuleConditionDAO.deleteLcmsSeqRuleCondition", commandMap);
    }

    public int deleteLcmsSeqRuleConditionAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqRuleConditionDAO.deleteAllLcmsSeqRuleCondition", commandMap);
    }

    public Object existLcmsSeqRuleCondition( LcmsSeqRuleCondition lcmsSeqRuleCondition) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqRuleConditionDAO.existLcmsSeqRuleCondition", lcmsSeqRuleCondition);
    }

}
