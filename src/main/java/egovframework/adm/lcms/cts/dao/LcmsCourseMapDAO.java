/* 
 * LcmsCourseMapDAO.java		1.00	2011-09-19 
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
import egovframework.adm.lcms.cts.domain.LcmsCourseMap;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCourseMapDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-19 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsCourseMapDAO")
public class LcmsCourseMapDAO extends EgovAbstractDAO{
    public List selectLcmsCourseMapPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsCourseMapDAO.selectLcmsCourseMapPageList", commandMap);
    }

    public int  selectLcmsCourseMapPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCourseMapDAO.selectLcmsCourseMapPageListTotCnt", commandMap);
    }

    public List selectLcmsCourseMapList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsCourseMapDAO.selectLcmsCourseMapList", commandMap);
    }

    public Object selectLcmsCourseMap( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCourseMapDAO.selectLcmsCourseMap", commandMap);
    }

    public Object insertLcmsCourseMap( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsCourseMapDAO.insertLcmsCourseMap", commandMap);
    }

    public int updateLcmsCourseMap( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCourseMapDAO.updateLcmsCourseMap", commandMap);
    }

    public int updateFieldLcmsCourseMap( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCourseMapDAO.updateFieldLcmsCourseMap", commandMap);
    }

    public int deleteLcmsCourseMap( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCourseMapDAO.deleteLcmsCourseMap", commandMap);
    }

    public int deleteLcmsCourseMapAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCourseMapDAO.deleteAllLcmsCourseMap", commandMap);
    }

    public Object existLcmsCourseMap( LcmsCourseMap lcmsCourseMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCourseMapDAO.existLcmsCourseMap", lcmsCourseMap);
    }
    
    public int getCourseMapSeq(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCourseMapDAO.getCourseMapSeq", commandMap);
    }

}
