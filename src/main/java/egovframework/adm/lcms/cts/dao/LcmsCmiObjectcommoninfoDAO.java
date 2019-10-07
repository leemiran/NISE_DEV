/* 
 * LcmsCmiObjectcommoninfoDAO.java		1.00	2011-09-26 
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
import egovframework.adm.lcms.cts.domain.LcmsCmiObjectcommoninfo;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiObjectcommoninfoDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsCmiObjectcommoninfoDAO")
public class LcmsCmiObjectcommoninfoDAO extends EgovAbstractDAO{
    public List selectLcmsCmiObjectcommoninfoPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsCmiObjectcommoninfoDAO.selectLcmsCmiObjectcommoninfoPageList", commandMap);
    }

    public int  selectLcmsCmiObjectcommoninfoPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCmiObjectcommoninfoDAO.selectLcmsCmiObjectcommoninfoPageListTotCnt", commandMap);
    }

    public List selectLcmsCmiObjectcommoninfoList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsCmiObjectcommoninfoDAO.selectLcmsCmiObjectcommoninfoList", commandMap);
    }

    public Object selectLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiObjectcommoninfoDAO.selectLcmsCmiObjectcommoninfo", commandMap);
    }

    public Object insertLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsCmiObjectcommoninfoDAO.insertLcmsCmiObjectcommoninfo", commandMap);
    }

    public int updateLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiObjectcommoninfoDAO.updateLcmsCmiObjectcommoninfo", commandMap);
    }

    public int updateFieldLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiObjectcommoninfoDAO.updateFieldLcmsCmiObjectcommoninfo", commandMap);
    }

    public int deleteLcmsCmiObjectcommoninfo( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiObjectcommoninfoDAO.deleteLcmsCmiObjectcommoninfo", commandMap);
    }

    public int deleteLcmsCmiObjectcommoninfoAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiObjectcommoninfoDAO.deleteAllLcmsCmiObjectcommoninfo", commandMap);
    }

    public Object existLcmsCmiObjectcommoninfo( LcmsCmiObjectcommoninfo lcmsCmiObjectcommoninfo) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiObjectcommoninfoDAO.existLcmsCmiObjectcommoninfo", lcmsCmiObjectcommoninfo);
    }

}
