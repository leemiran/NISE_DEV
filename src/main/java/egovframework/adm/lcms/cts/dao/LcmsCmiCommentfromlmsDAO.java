/* 
 * LcmsCmiCommentfromlmsDAO.java		1.00	2011-09-26 
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
import egovframework.adm.lcms.cts.domain.LcmsCmiCommentfromlms;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiCommentfromlmsDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsCmiCommentfromlmsDAO")
public class LcmsCmiCommentfromlmsDAO extends EgovAbstractDAO{
    public List selectLcmsCmiCommentfromlmsPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsCmiCommentfromlmsDAO.selectLcmsCmiCommentfromlmsPageList", commandMap);
    }

    public int  selectLcmsCmiCommentfromlmsPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCmiCommentfromlmsDAO.selectLcmsCmiCommentfromlmsPageListTotCnt", commandMap);
    }

    public List selectLcmsCmiCommentfromlmsList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsCmiCommentfromlmsDAO.selectLcmsCmiCommentfromlmsList", commandMap);
    }

    public Object selectLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiCommentfromlmsDAO.selectLcmsCmiCommentfromlms", commandMap);
    }

    public Object insertLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsCmiCommentfromlmsDAO.insertLcmsCmiCommentfromlms", commandMap);
    }

    public int updateLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiCommentfromlmsDAO.updateLcmsCmiCommentfromlms", commandMap);
    }

    public int updateFieldLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiCommentfromlmsDAO.updateFieldLcmsCmiCommentfromlms", commandMap);
    }

    public int deleteLcmsCmiCommentfromlms( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiCommentfromlmsDAO.deleteLcmsCmiCommentfromlms", commandMap);
    }

    public int deleteLcmsCmiCommentfromlmsAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiCommentfromlmsDAO.deleteAllLcmsCmiCommentfromlms", commandMap);
    }

    public Object existLcmsCmiCommentfromlms( LcmsCmiCommentfromlms lcmsCmiCommentfromlms) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiCommentfromlmsDAO.existLcmsCmiCommentfromlms", lcmsCmiCommentfromlms);
    }

}
