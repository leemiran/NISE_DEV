/* 
 * LcmsCmiIaDAO.java		1.00	2011-09-26 
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
import egovframework.adm.lcms.cts.domain.LcmsCmiIa;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiIaDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsCmiIaDAO")
public class LcmsCmiIaDAO extends EgovAbstractDAO{
    public List selectLcmsCmiIaPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsCmiIaDAO.selectLcmsCmiIaPageList", commandMap);
    }

    public int  selectLcmsCmiIaPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCmiIaDAO.selectLcmsCmiIaPageListTotCnt", commandMap);
    }

    public List selectLcmsCmiIaList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsCmiIaDAO.selectLcmsCmiIaList", commandMap);
    }

    public Object selectLcmsCmiIa( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiIaDAO.selectLcmsCmiIa", commandMap);
    }

    public Object insertLcmsCmiIa( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsCmiIaDAO.insertLcmsCmiIa", commandMap);
    }

    public int updateLcmsCmiIa( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiIaDAO.updateLcmsCmiIa", commandMap);
    }

    public int updateFieldLcmsCmiIa( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiIaDAO.updateFieldLcmsCmiIa", commandMap);
    }

    public int deleteLcmsCmiIa( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiIaDAO.deleteLcmsCmiIa", commandMap);
    }

    public int deleteLcmsCmiIaAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiIaDAO.deleteAllLcmsCmiIa", commandMap);
    }

    public Object existLcmsCmiIa( LcmsCmiIa lcmsCmiIa) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiIaDAO.existLcmsCmiIa", lcmsCmiIa);
    }

}
