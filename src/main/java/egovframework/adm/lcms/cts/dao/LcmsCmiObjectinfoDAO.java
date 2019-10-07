/* 
 * LcmsCmiObjectinfoDAO.java		1.00	2011-09-26 
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
import egovframework.adm.lcms.cts.domain.LcmsCmiObjectinfo;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiObjectinfoDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsCmiObjectinfoDAO")
public class LcmsCmiObjectinfoDAO extends EgovAbstractDAO{
    public List selectLcmsCmiObjectinfoPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsCmiObjectinfoDAO.selectLcmsCmiObjectinfoPageList", commandMap);
    }

    public int  selectLcmsCmiObjectinfoPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCmiObjectinfoDAO.selectLcmsCmiObjectinfoPageListTotCnt", commandMap);
    }

    public List selectLcmsCmiObjectinfoList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsCmiObjectinfoDAO.selectLcmsCmiObjectinfoList", commandMap);
    }

    public Map selectLcmsCmiObjectinfo( Map<String, Object> commandMap) throws Exception{
        return (Map)selectByPk("lcmsCmiObjectinfoDAO.selectLcmsCmiObjectinfo", commandMap);
    }

    public Object insertLcmsCmiObjectinfo(LcmsCmiObjectinfo lcmsCmiObjectinfo) throws Exception{
        return insert("lcmsCmiObjectinfoDAO.insertLcmsCmiObjectinfo", lcmsCmiObjectinfo);
    }

    public int updateLcmsCmiObjectinfo( LcmsCmiObjectinfo lcmsCmiObjectinfo) throws Exception{
        return update("lcmsCmiObjectinfoDAO.updateLcmsCmiObjectinfo", lcmsCmiObjectinfo);
    }

    public int updateFieldLcmsCmiObjectinfo( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiObjectinfoDAO.updateFieldLcmsCmiObjectinfo", commandMap);
    }

    public int deleteLcmsCmiObjectinfo( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiObjectinfoDAO.deleteLcmsCmiObjectinfo", commandMap);
    }

    public int deleteLcmsCmiObjectinfoAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiObjectinfoDAO.deleteAllLcmsCmiObjectinfo", commandMap);
    }

    public Object existLcmsCmiObjectinfo( LcmsCmiObjectinfo lcmsCmiObjectinfo) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiObjectinfoDAO.existLcmsCmiObjectinfo", lcmsCmiObjectinfo);
    }
    
    public String checkScormEduLimit(Map<String, Object> commandMap) throws Exception{
    	return (String)selectByPk("lcmsCmiObjectinfoDAO.checkScormEduLimit", commandMap);
    }

}
