/* 
 * LcmsOrganizationLogDAO.java		1.00	2011-09-05 
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
import egovframework.adm.lcms.cts.domain.LcmsOrganizationLog;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsOrganizationLogDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsOrganizationLogDAO")
public class LcmsOrganizationLogDAO extends EgovAbstractDAO{
    public List selectLcmsOrganizationLogPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsOrganizationLogDAO.selectLcmsOrganizationLogPageList", commandMap);
    }

    public int  selectLcmsOrganizationLogPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsOrganizationLogDAO.selectLcmsOrganizationLogPageListTotCnt", commandMap);
    }
    
    public int  selectLcmsOrganizationLogMaxNum( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsOrganizationLogDAO.selectLcmsOrganizationLogMaxNum", commandMap);
    }

    public List selectLcmsOrganizationLogList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsOrganizationLogDAO.selectLcmsOrganizationLogPageList", commandMap);
    }

    public Object selectLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsOrganizationLogDAO.selectLcmsOrganizationLog", commandMap);
    }

    public Object insertLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsOrganizationLogDAO.inserLcmsOrganizationLog", commandMap);
    }

    public int updateLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception{
        return update("lcmsOrganizationLogDAO.updateLcmsOrganizationLog", commandMap);
    }

    public int updateFieldLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception{
        return update("lcmsOrganizationLogDAO.updateFieldLcmsOrganizationLog", commandMap);
    }

    public int deleteLcmsOrganizationLog( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsOrganizationLogDAO.deleteLcmsOrganizationLog", commandMap);
    }

    public int deleteLcmsOrganizationLogAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsOrganizationLogDAO.deleteAllLcmsOrganizationLog", commandMap);
    }

    public Object existLcmsOrganizationLog( LcmsOrganizationLog lcmsOrganizationLog) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsOrganizationLogDAO.existLcmsOrganizationLog", lcmsOrganizationLog);
    }

}
