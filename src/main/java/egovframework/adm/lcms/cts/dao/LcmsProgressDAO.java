/* 
 * LcmsProgressDAO.java		1.00	2011-10-17 
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
import egovframework.adm.lcms.cts.domain.LcmsProgress;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsProgressDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-17 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsProgressDAO")
public class LcmsProgressDAO extends EgovAbstractDAO{
    public List selectLcmsProgressPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsProgressDAO.selectLcmsProgressPageList", commandMap);
    }

    public int  selectLcmsProgressPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsProgressDAO.selectLcmsProgressPageListTotCnt", commandMap);
    }

    public List selectLcmsProgressList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsProgressDAO.selectLcmsProgressList", commandMap);
    }
    
    public List selectLcmsOldProgressList( Map<String, Object> commandMap) throws Exception{
    	return list("lcmsProgressDAO.selectLcmsOldProgressList", commandMap);
    }

    public Object selectLcmsProgress( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsProgressDAO.selectLcmsProgress", commandMap);
    }
    
    public Object selectLcmsProgressOld( Map<String, Object> commandMap) throws Exception{
    	return (Object)getSqlMapClientTemplate().queryForObject("lcmsProgressDAO.selectLcmsProgressOld", commandMap);
    }

    public Object insertLcmsProgress( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsProgressDAO.insertLcmsProgress", commandMap);
    }
    
    public Object insertLcmsOldProgress( Map<String, Object> commandMap) throws Exception{
    	return insert("lcmsProgressDAO.insertLcmsOldProgress", commandMap);
    }

    public int updateLcmsProgress( Map<String, Object> commandMap) throws Exception{
        return update("lcmsProgressDAO.updateLcmsProgress", commandMap);
    }
    
    public int updateLcmsProgressOld( Map<String, Object> commandMap) throws Exception{
    	return update("lcmsProgressDAO.updateLcmsProgressOld", commandMap);
    }

    public int updateFieldLcmsProgress( Map<String, Object> commandMap) throws Exception{
        return update("lcmsProgressDAO.updateFieldLcmsProgress", commandMap);
    }

    public int deleteLcmsProgress( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsProgressDAO.deleteLcmsProgress", commandMap);
    }

    public int deleteLcmsProgressAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsProgressDAO.deleteAllLcmsProgress", commandMap);
    }

    public Object existLcmsProgress( LcmsProgress lcmsProgress) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsProgressDAO.existLcmsProgress", lcmsProgress);
    }

    public int  selectLcmsMobileBookMarkCount( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsProgressDAO.selectLcmsMobileBookMarkCount", commandMap);
    }
    
    public Object insertLcmsMobileBookMark( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsProgressDAO.insertLcmsMobileBookMark", commandMap);
    }
    
    public int updateLcmsMobileBookMark( Map<String, Object> commandMap) throws Exception{
    	return update("lcmsProgressDAO.updateLcmsMobileBookMark", commandMap);
    }

	public String selectEndCommitYn(Map<String, Object> commandMap)  throws Exception{
		return (String)getSqlMapClientTemplate().queryForObject("lcmsProgressDAO.selectEndCommitYn", commandMap);
	}

	public void insertLcmsProgressComplete(Map<String, Object> commandMap)  throws Exception {
		insert("lcmsProgressDAO.insertLcmsProgressComplete", commandMap);
	}

	public void updateLcmsProgressFinalStatus(Map<String, Object> commandMap)  throws Exception {
		update("lcmsProgressDAO.updateLcmsProgressFinalStatus", commandMap);
	}

	public void updateLcmsProgressComplete(Map<String, Object> commandMap)  throws Exception {
		update("lcmsProgressDAO.updateLcmsProgressComplete", commandMap);
	}

	public String selectMobileEndCommitYn(Map<String, Object> commandMap) {
		return (String)getSqlMapClientTemplate().queryForObject("lcmsProgressDAO.selectMobileEndCommitYn", commandMap);
	}

	public void insertMobileLcmsProgressComplete(Map<String, Object> commandMap) {
		insert("lcmsProgressDAO.insertMobileLcmsProgressComplete", commandMap);
	}
    
    
}
