/* 
 * LcmsGlobalobjDAO.java		1.00	2011-09-16 
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
import egovframework.adm.lcms.cts.domain.LcmsGlobalobj;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsGlobalobjDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-16 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsGlobalobjDAO")
public class LcmsGlobalobjDAO extends EgovAbstractDAO{
    public List selectLcmsGlobalobjPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsGlobalobjDAO.selectLcmsGlobalobjPageList", commandMap);
    }

    public int  selectLcmsGlobalobjPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsGlobalobjDAO.selectLcmsGlobalobjPageListTotCnt", commandMap);
    }

    public List selectLcmsGlobalobjList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsGlobalobjDAO.selectLcmsGlobalobjList", commandMap);
    }

    public Object selectLcmsGlobalobj( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsGlobalobjDAO.selectLcmsGlobalobj", commandMap);
    }

    public Object insertLcmsGlobalobj( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsGlobalobjDAO.insertLcmsGlobalobj", commandMap);
    }

    public int updateLcmsGlobalobj( Map<String, Object> commandMap) throws Exception{
        return update("lcmsGlobalobjDAO.updateLcmsGlobalobj", commandMap);
    }

    public int updateFieldLcmsGlobalobj( Map<String, Object> commandMap) throws Exception{
        return update("lcmsGlobalobjDAO.updateFieldLcmsGlobalobj", commandMap);
    }

    public int deleteLcmsGlobalobj( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsGlobalobjDAO.deleteLcmsGlobalobj", commandMap);
    }

    public int deleteLcmsGlobalobjAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsGlobalobjDAO.deleteAllLcmsGlobalobj", commandMap);
    }

    public Object existLcmsGlobalobj( LcmsGlobalobj lcmsGlobalobj) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsGlobalobjDAO.existLcmsGlobalobj", lcmsGlobalobj);
    }

}
