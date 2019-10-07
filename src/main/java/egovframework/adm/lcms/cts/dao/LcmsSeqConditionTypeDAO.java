/* 
 * LcmsSeqConditionTypeDAO.java		1.00	2011-09-05 
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
import egovframework.adm.lcms.cts.domain.LcmsSeqConditionType;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqConditionTypeDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsSeqConditionTypeDAO")
public class LcmsSeqConditionTypeDAO extends EgovAbstractDAO{
    public List selectLcmsSeqConditionTypePageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsSeqConditionTypeDAO.selectLcmsSeqConditionTypePageList", commandMap);
    }

    public int  selectLcmsSeqConditionTypePageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqConditionTypeDAO.selectLcmsSeqConditionTypePageListTotCnt", commandMap);
    }
    
    public int  selectLcmsSeqConditionTypeMaxNum( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqConditionTypeDAO.selectLcmsSeqConditionTypeMaxNum", commandMap);
    }

    public List selectLcmsSeqConditionTypeList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsSeqConditionTypeDAO.selectLcmsSeqConditionTypePageList", commandMap);
    }

    public Object selectLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqConditionTypeDAO.selectLcmsSeqConditionType", commandMap);
    }

    public Object insertLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsSeqConditionTypeDAO.insertLcmsSeqConditionType", commandMap);
    }

    public int updateLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqConditionTypeDAO.updateLcmsSeqConditionType", commandMap);
    }

    public int updateFieldLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqConditionTypeDAO.updateFieldLcmsSeqConditionType", commandMap);
    }

    public int deleteLcmsSeqConditionType( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqConditionTypeDAO.deleteLcmsSeqConditionType", commandMap);
    }

    public int deleteLcmsSeqConditionTypeAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqConditionTypeDAO.deleteAllLcmsSeqConditionType", commandMap);
    }

    public Object existLcmsSeqConditionType( LcmsSeqConditionType lcmsSeqConditionType) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqConditionTypeDAO.existLcmsSeqConditionType", lcmsSeqConditionType);
    }

}
