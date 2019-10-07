/* 
 * LcmsSeqMapInfoDAO.java		1.00	2011-09-05 
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
import egovframework.adm.lcms.cts.domain.LcmsSeqMapInfo;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsSeqMapInfoDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsSeqMapInfoDAO")
public class LcmsSeqMapInfoDAO extends EgovAbstractDAO{
    public List selectLcmsSeqMapInfoPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsSeqMapInfoDAO.selectLcmsSeqMapInfoPageList", commandMap);
    }

    public int  selectLcmsSeqMapInfoPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqMapInfoDAO.selectLcmsSeqMapInfoPageListTotCnt", commandMap);
    }
    
    public int  selectLcmsSeqMapInfoMaxNum( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsSeqMapInfoDAO.selectLcmsSeqMapInfoMaxNum", commandMap);
    }

    public List selectLcmsSeqMapInfoList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsSeqMapInfoDAO.selectLcmsSeqMapInfoPageList", commandMap);
    }

    public Object selectLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqMapInfoDAO.selectLcmsSeqMapInfo", commandMap);
    }

    public Object insertLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsSeqMapInfoDAO.insertLcmsSeqMapInfo", commandMap);
    }

    public int updateLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqMapInfoDAO.updateLcmsSeqMapInfo", commandMap);
    }

    public int updateFieldLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception{
        return update("lcmsSeqMapInfoDAO.updateFieldLcmsSeqMapInfo", commandMap);
    }

    public int deleteLcmsSeqMapInfo( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqMapInfoDAO.deleteLcmsSeqMapInfo", commandMap);
    }

    public int deleteLcmsSeqMapInfoAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsSeqMapInfoDAO.deleteAllLcmsSeqMapInfo", commandMap);
    }

    public Object existLcmsSeqMapInfo( LcmsSeqMapInfo lcmsSeqMapInfo) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsSeqMapInfoDAO.existLcmsSeqMapInfo", lcmsSeqMapInfo);
    }

}
