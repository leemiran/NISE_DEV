/* 
 * LcmsResourceDependencyDAO.java		1.00	2011-09-05 
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
import egovframework.adm.lcms.cts.domain.LcmsResourceDependency;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsResourceDependencyDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsResourceDependencyDAO")
public class LcmsResourceDependencyDAO extends EgovAbstractDAO{
    public List selectLcmsResourceDependencyPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsResourceDependencyDAO.selectLcmsResourceDependencyPageList", commandMap);
    }

    public int  selectLcmsResourceDependencyPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsResourceDependencyDAO.selectLcmsResourceDependencyPageListTotCnt", commandMap);
    }

    public List selectLcmsResourceDependencyList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsResourceDependencyDAO.selectLcmsResourceDependencyPageList", commandMap);
    }

    public Object selectLcmsResourceDependency( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsResourceDependencyDAO.selectLcmsResourceDependency", commandMap);
    }

    public Object insertLcmsResourceDependency( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsResourceDependencyDAO.insertLcmsResourceDependency", commandMap);
    }

    public int updateLcmsResourceDependency( Map<String, Object> commandMap) throws Exception{
        return update("lcmsResourceDependencyDAO.updateLcmsResourceDependency", commandMap);
    }

    public int updateFieldLcmsResourceDependency( Map<String, Object> commandMap) throws Exception{
        return update("lcmsResourceDependencyDAO.updateFieldLcmsResourceDependency", commandMap);
    }

    public int deleteLcmsResourceDependency( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsResourceDependencyDAO.deleteLcmsResourceDependency", commandMap);
    }

    public int deleteLcmsResourceDependencyAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsResourceDependencyDAO.deleteAllLcmsResourceDependency", commandMap);
    }

    public Object existLcmsResourceDependency( LcmsResourceDependency lcmsResourceDependency) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsResourceDependencyDAO.existLcmsResourceDependency", lcmsResourceDependency);
    }

}
