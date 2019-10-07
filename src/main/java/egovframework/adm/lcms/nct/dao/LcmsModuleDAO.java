/* 
 * LcmsModuleDAO.java		1.00	2011-10-11 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.nct.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.adm.lcms.nct.domain.LcmsModule;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsModuleDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsModuleDAO")
public class LcmsModuleDAO extends EgovAbstractDAO{
    public List selectLcmsModulePageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsModuleDAO.selectLcmsModulePageList", commandMap);
    }

    public int  selectLcmsModulePageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsModuleDAO.selectLcmsModulePageListTotCnt", commandMap);
    }

    public List selectLcmsModuleList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsModuleDAO.selectLcmsModuleList", commandMap);
    }

    public Object selectLcmsModule( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsModuleDAO.selectLcmsModule", commandMap);
    }

    public Object insertLcmsModule( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsModuleDAO.insertLcmsModule", commandMap);
    }

    public int updateLcmsModule( Map<String, Object> commandMap) throws Exception{
        return update("lcmsModuleDAO.updateLcmsModule", commandMap);
    }

    public int updateFieldLcmsModule( Map<String, Object> commandMap) throws Exception{
        return update("lcmsModuleDAO.updateFieldLcmsModule", commandMap);
    }

    public int deleteLcmsModule( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsModuleDAO.deleteLcmsModule", commandMap);
    }

    public int deleteLcmsModuleAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsModuleDAO.deleteAllLcmsModule", commandMap);
    }

    public Object existLcmsModule( LcmsModule lcmsModule) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsModuleDAO.existLcmsModule", lcmsModule);
    }
    
    public String selectModuleNum(Map<String, Object> commandMap) throws Exception{
    	return (String)getSqlMapClientTemplate().queryForObject("lcmsModuleDAO.selectModuleNum", commandMap);
    }
    
    public List selectModulePath(Map<String, Object> commandMap) throws Exception{
    	return list("lcmsModuleDAO.selectModulePath", commandMap);
    }
    
    public Map selectLcmsModuleData(Map<String, Object> commandMap) throws Exception{
    	return (Map)selectByPk("lcmsModuleDAO.selectLcmsModuleData", commandMap);
    }
    
    public Map selectSaveInfoData(Map<String, Object> commandMap) throws Exception{
    	return(Map)selectByPk("lcmsModuleDAO.selectSaveInfoData", commandMap);
    }
    
    public int deleteModuleAll(Map<String, Object> commandMap) throws Exception{
    	return delete("lcmsModuleDAO.deleteModuleAll", commandMap);
    }
    
}
