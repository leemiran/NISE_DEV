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
import egovframework.adm.lcms.cts.domain.LcmsCourseOrg;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : lcmsCourseOrgDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsCourseOrgDAO")
public class LcmsCourseOrgDAO extends EgovAbstractDAO{
    public List selectLcmsCourseOrgPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsCourseOrgDAO.selectLcmsCourseOrgPageList", commandMap);
    }

    public int  selectLcmsCourseOrgPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCourseOrgDAO.selectLcmsCourseOrgPageListTotCnt", commandMap);
    }

    public List selectLcmsCourseOrgList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsCourseOrgDAO.selectLcmsCourseOrgPageList", commandMap);
    }

    public Object selectLcmsCourseOrg( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCourseOrgDAO.selectLcmsCourseOrg", commandMap);
    }

    public Object insertLcmsCourseOrg( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsCourseOrgDAO.insertLcmsCourseOrg", commandMap);
    }

    public int updateLcmsCourseOrg( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCourseOrgDAO.updateLcmsCourseOrg", commandMap);
    }

    public int updateFieldLcmsCourseOrg( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCourseOrgDAO.updateFieldLcmsCourseOrg", commandMap);
    }

    public int deleteLcmsCourseOrg( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCourseOrgDAO.deleteLcmsCourseOrg", commandMap);
    }

    public int deleteLcmsCourseOrgAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCourseOrgDAO.deleteAllLcmsCourseOrg", commandMap);
    }

    public Object existLcmsCourseOrg( LcmsCourseOrg LcmsCourseOrg) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCourseOrgDAO.existLcmsCourseOrg", LcmsCourseOrg);
    }
    
    public int selectCrsOrgNo(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCourseOrgDAO.selectCrsOrgNo", commandMap);
    }

}
