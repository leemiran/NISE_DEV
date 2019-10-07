/* 
 * LcmsManiseqDAO.java		1.00	2011-09-14 
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
import egovframework.adm.lcms.cts.domain.LcmsManiseq;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsManiseqDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsManiseqDAO")
public class LcmsManiseqDAO extends EgovAbstractDAO{
    public List selectLcmsManiseqPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsManiseqDAO.selectLcmsManiseqPageList", commandMap);
    }

    public int  selectLcmsManiseqPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsManiseqDAO.selectLcmsManiseqPageListTotCnt", commandMap);
    }

    public List selectLcmsManiseqList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsManiseqDAO.selectLcmsManiseqPageList", commandMap);
    }

    public Object selectLcmsManiseq( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsManiseqDAO.selectLcmsManiseq", commandMap);
    }

    public Object insertLcmsManiseq( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsManiseqDAO.insertLcmsManiseq", commandMap);
    }

    public int updateLcmsManiseq( Map<String, Object> commandMap) throws Exception{
        return update("lcmsManiseqDAO.updateLcmsManiseq", commandMap);
    }

    public int updateFieldLcmsManiseq( Map<String, Object> commandMap) throws Exception{
        return update("lcmsManiseqDAO.updateFieldLcmsManiseq", commandMap);
    }

    public int deleteLcmsManiseq( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsManiseqDAO.deleteLcmsManiseq", commandMap);
    }

    public int deleteLcmsManiseqAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsManiseqDAO.deleteAllLcmsManiseq", commandMap);
    }
    
    public int selectLcmsManiSeq( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsManiseqDAO.selectLcmsManiSeq", commandMap);
    }

    public Object existLcmsManiseq( LcmsManiseq lcmsManiseq) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsManiseqDAO.existLcmsManiseq", lcmsManiseq);
    }

}
