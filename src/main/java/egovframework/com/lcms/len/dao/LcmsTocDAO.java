/* 
 * LcmsTocDAO.java		1.00	2011-09-14 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.com.lcms.len.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.com.lcms.len.domain.LcmsToc;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsTocDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-14 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsTocDAO")
public class LcmsTocDAO extends EgovAbstractDAO{
    public List selectLcmsTocPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsTocDAO.selectLcmsTocPageList", commandMap);
    }

    public int  selectLcmsTocPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsTocDAO.selectLcmsTocPageListTotCnt", commandMap);
    }

    public List selectLcmsTocList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsTocDAO.selectLcmsTocPageList", commandMap);
    }

    public Object selectLcmsToc( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsTocDAO.selectLcmsToc", commandMap);
    }

    public Object insertLcmsToc( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsTocDAO.insertLcmsToc", commandMap);
    }

    public int updateLcmsToc( Map<String, Object> commandMap) throws Exception{
        return update("lcmsTocDAO.updateLcmsToc", commandMap);
    }

    public int updateFieldLcmsToc( Map<String, Object> commandMap) throws Exception{
        return update("lcmsTocDAO.updateFieldLcmsToc", commandMap);
    }

    public int deleteLcmsToc( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsTocDAO.deleteLcmsToc", commandMap);
    }

    public int deleteLcmsTocAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsTocDAO.deleteAllLcmsToc", commandMap);
    }

    public Object existLcmsToc( LcmsToc lcmsToc) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsTocDAO.existLcmsToc", lcmsToc);
    }

}
