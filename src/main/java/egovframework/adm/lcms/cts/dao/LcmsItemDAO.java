/* 
 * LcmsItemDAO.java		1.00	2011-09-05 
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
import egovframework.adm.lcms.cts.domain.LcmsItem;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsItemDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsItemDAO")
public class LcmsItemDAO extends EgovAbstractDAO{
    public List selectLcmsItemPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsItemDAO.selectLcmsItemPageList", commandMap);
    }

    public int  selectLcmsItemPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsItemDAO.selectLcmsItemPageListTotCnt", commandMap);
    }
    
    public int  selectLcmsItemMaxSeq( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsItemDAO.selectLcmsItemMaxSeq", commandMap);
    }
    
    public int  selectLcmsItemIdCnt( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsItemDAO.selectLcmsItemIdCnt", commandMap);
    }
    
    public int  selectLcmsItemHighItem( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsItemDAO.selectLcmsItemHighItem", commandMap);
    }

    public List selectLcmsItemList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsItemDAO.selectLcmsItemList", commandMap);
    }

    public Object selectLcmsItem( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsItemDAO.selectLcmsItem", commandMap);
    }

    public Object insertLcmsItem( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsItemDAO.insertLcmsItem", commandMap);
    }

    public int updateLcmsItem( Map<String, Object> commandMap) throws Exception{
        return update("lcmsItemDAO.updateLcmsItem", commandMap);
    }
    
    public int updateLcmsItemRsrcId( Map<String, Object> commandMap) throws Exception{
    	return update("lcmsItemDAO.updateLcmsItemRsrcId", commandMap);
    }

    public int updateFieldLcmsItem( Map<String, Object> commandMap) throws Exception{
        return update("lcmsItemDAO.updateFieldLcmsItem", commandMap);
    }

    public int deleteLcmsItem( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsItemDAO.deleteLcmsItem", commandMap);
    }

    public int deleteLcmsItemAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsItemDAO.deleteAllLcmsItem", commandMap);
    }

    public Object existLcmsItem( LcmsItem lcmsItem) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsItemDAO.existLcmsItem", lcmsItem);
    }
    
    public Object selectComLenLcmsItem( Map<String, Object> commandMap) throws Exception{;
    	return (Object)getSqlMapClientTemplate().queryForObject("lcmsItemDAO.selectComLenLcmsItem", commandMap);
    }
    public Object selectComLenRsrcSeqLcmsItem( Map<String, Object> commandMap) throws Exception{;
    	return (Object)getSqlMapClientTemplate().queryForObject("lcmsItemDAO.selectComLenRsrcSeqLcmsItem", commandMap);
    }
    
    // 학습창 
    public Object selectLcmsItem01( Map<String, Object> commandMap) throws Exception{;
    	return (Object)getSqlMapClientTemplate().queryForObject("lcmsItemDAO.selectLcmsItem01", commandMap);
    }

}
