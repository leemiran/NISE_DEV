/* 
 * LcmsItemResourceDAO.java		1.00	2011-09-05 
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
import egovframework.adm.lcms.cts.domain.LcmsItemResource;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsItemResourceDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsItemResourceDAO")
public class LcmsItemResourceDAO extends EgovAbstractDAO{
    public List selectLcmsItemResourcePageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsItemResourceDAO.selectLcmsItemResourcePageList", commandMap);
    }

    public int  selectLcmsItemResourcePageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsItemResourceDAO.selectLcmsItemResourcePageListTotCnt", commandMap);
    }
    

    /*
     * 학습자료일련번호 조회
    */
    public int  selectLcmsItemResourceSeq( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsItemResourceDAO.selectLcmsItemResourceSeq", commandMap);
    }

    public List selectLcmsItemResourceList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsItemResourceDAO.selectLcmsItemResourcePageList", commandMap);
    }

    public Object selectLcmsItemResource( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsItemResourceDAO.selectLcmsItemResource", commandMap);
    }

    public Object insertLcmsItemResource( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsItemResourceDAO.insertLcmsItemResource", commandMap);
    }

    public int updateLcmsItemResource( Map<String, Object> commandMap) throws Exception{
        return update("lcmsItemResourceDAO.updateLcmsItemResource", commandMap);
    }

    public int updateFieldLcmsItemResource( Map<String, Object> commandMap) throws Exception{
        return update("lcmsItemResourceDAO.updateFieldLcmsItemResource", commandMap);
    }

    public int deleteLcmsItemResource( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsItemResourceDAO.deleteLcmsItemResource", commandMap);
    }

    public int deleteLcmsItemResourceAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsItemResourceDAO.deleteAllLcmsItemResource", commandMap);
    }

    public Object existLcmsItemResource( LcmsItemResource lcmsItemResource) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsItemResourceDAO.existLcmsItemResource", lcmsItemResource);
    }
    
    public Object selectComLenLcmsItemResource( Map<String, Object> commandMap) throws Exception{;
    	return (Object)getSqlMapClientTemplate().queryForObject("lcmsItemResourceDAO.selectComLenLcmsItemResource", commandMap);
    }

}
