/* 
 * lcmsCourseOrgDAO.java		1.00	2011-09-05 
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
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSerializerDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsSerializerDAO")
public class LcmsSerializerDAO extends EgovAbstractDAO{


	public int selectLcmsSerializerIdx( Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSerializerDAO.selectLcmsSerializerIdx", commandMap);
	}
	
    public Object insertLcmsSerializer( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsSerializerDAO.insertLcmsSerializer", commandMap);
    }
    
    /**
     * 학습창 
     * @param commandMap
     * @return
     * @throws Exception
     */
    public Object selectLcmsSerializer( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSerializerDAO.selectLcmsSerializer", commandMap);
    }
    
    public int deleteLcmsSerializer(Map<String, Object> commandMap) throws Exception{
    	return delete("lcmsSerializerDAO.deleteLcmsSerializer", commandMap);
    }

}
