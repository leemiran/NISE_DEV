/* 
 * LcmsOrganizationDAO.java		1.00	2011-09-05 
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
import egovframework.adm.lcms.cts.domain.LcmsOrganization;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsOrganizationDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsOrganizationDAO")
public class LcmsOrganizationDAO extends EgovAbstractDAO{
    public List selectLcmsOrganizationPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsOrganizationDAO.selectLcmsOrganizationPageList", commandMap);
    }

    public int  selectLcmsOrganizationPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsOrganizationDAO.selectLcmsOrganizationPageListTotCnt", commandMap);
    }

    public List selectLcmsOrganizationList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsOrganizationDAO.selectLcmsOrganizationPageList", commandMap);
    }

    public Object selectLcmsOrganization( Map<String, Object> commandMap) throws Exception{;
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsOrganizationDAO.selectLcmsOrganization", commandMap);
    }

    public Object insertLcmsOrganization( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsOrganizationDAO.insertLcmsOrganization", commandMap);
    }

    public int updateLcmsOrganization( Map<String, Object> commandMap) throws Exception{
        return update("lcmsOrganizationDAO.updateLcmsOrganization", commandMap);
    }

    public int updateFieldLcmsOrganization( Map<String, Object> commandMap) throws Exception{
        return update("lcmsOrganizationDAO.updateFieldLcmsOrganization", commandMap);
    }

    public int deleteLcmsOrganization( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsOrganizationDAO.deleteLcmsOrganization", commandMap);
    }

    public int deleteLcmsOrganizationAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsOrganizationDAO.deleteAllLcmsOrganization", commandMap);
    }

    public Object existLcmsOrganization( LcmsOrganization lcmsOrganization) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsOrganizationDAO.existLcmsOrganization", lcmsOrganization);
    }
    
    /**
     * org_seq가져오기
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int  getOrgSeqNum( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsOrganizationDAO.getOrgSeqNum", commandMap);
    }
    /**
     * organization id의 중복여부 확인
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int  getOrgaidCnt( Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsOrganizationDAO.getOrgaidCnt", commandMap);
    }
    
    /**
     * 선택차시 실제경로 조회
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectOrganizationPathList(Map<String, Object> commandMap) throws Exception{
    	return list("lcmsOrganizationDAO.selectOrganizationPathList", commandMap);
    }
    
    public int checkCourseMapping(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("lcmsOrganizationDAO.checkCourseMapping", commandMap);
    }

}
