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
 * source      : LcmsMetadataDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsMetadataDAO")
public class LcmsMetadataDAO extends EgovAbstractDAO{

    public int  selectLcmsMetadataSeq( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsMetadataDAO.selectLcmsMetadataSeq", commandMap);
    }


    public Object insertLcmsMetadata( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsMetadataDAO.insertLcmsMetadata", commandMap);
    }

    public int selectElementSeq( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsMetadataDAO.selectElementSeq", commandMap);
    }
    
    public Object insertMetadataElement( Map<String, Object> commandmap) throws Exception{
    	return insert("lcmsMetadataDAO.insertMetadataElement", commandmap);
    }
    
    public int deleteLcmsMetadataElement(Map<String, Object> commandMap) throws Exception{
    	return delete("lcmsMetadataDAO.deleteLcmsMetadataElement", commandMap);
    }
    
    public int deleteLcmsMetadata(Map<String, Object> commandMap) throws Exception{
    	return delete("lcmsMetadataDAO.deleteLcmsMetadata", commandMap);
    }
    
    public List selectLcmsMetadataElementList(Map<String, Object> commandMap) throws Exception{
    	return list("lcmsMetadataDAO.selectLcmsMetadataElementList", commandMap);
    }
    
    public int updateLcmsMetadataElement(Map<String, Object> commandMap) throws Exception{
    	return update("lcmsMetadataDAO.updateLcmsMetadataElement", commandMap);
    }
    
    public int selectLcmsMetadataCount(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsMetadataDAO.selectLcmsMetadataCount", commandMap);
    }
    
    public int selectLcmsMetadataElementCount(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsMetadataDAO.selectLcmsMetadataElementCount", commandMap);
    }
    

}
