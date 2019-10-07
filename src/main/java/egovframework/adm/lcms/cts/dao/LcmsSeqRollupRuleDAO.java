/* 
 * LcmsSeqRollupRuleDAO.java		1.00	2011-09-05 
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
import egovframework.adm.lcms.cts.domain.LcmsSeqRollupRule;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqRollupRuleDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsSeqRollupRuleDAO")
public class LcmsSeqRollupRuleDAO extends EgovAbstractDAO{
    public List selectLcmsSeqRollupRulePageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRulePageList", commandMap);
    }

    public int  selectLcmsSeqRollupRulePageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRulePageListTotCnt", commandMap);
    }
    
    public int  selectLcmsSeqRollupRuleMaxNum( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRuleMaxNum", commandMap);
    }

    public List selectLcmsSeqRollupRuleList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRulePageList", commandMap);
    }

    public Object selectLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqRollupRuleDAO.selectLcmsSeqRollupRule", commandMap);
    }

    public Object insertLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsSeqRollupRuleDAO.insertLcmsSeqRollupRule", commandMap);
    }

    public int updateLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqRollupRuleDAO.updateLcmsSeqRollupRule", commandMap);
    }

    public int updateFieldLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqRollupRuleDAO.updateFieldLcmsSeqRollupRule", commandMap);
    }

    public int deleteLcmsSeqRollupRule( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqRollupRuleDAO.deleteLcmsSeqRollupRule", commandMap);
    }

    public int deleteLcmsSeqRollupRuleAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqRollupRuleDAO.deleteAllLcmsSeqRollupRule", commandMap);
    }

    public Object existLcmsSeqRollupRule( LcmsSeqRollupRule lcmsSeqRollupRule) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqRollupRuleDAO.existLcmsSeqRollupRule", lcmsSeqRollupRule);
    }

}
