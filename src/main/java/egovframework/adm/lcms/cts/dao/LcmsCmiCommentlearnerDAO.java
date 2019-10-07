/* 
 * LcmsCmiCommentlearnerDAO.java		1.00	2011-09-26 
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
import egovframework.adm.lcms.cts.domain.LcmsCmiCommentlearner;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsCmiCommentlearnerDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-26 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsCmiCommentlearnerDAO")
public class LcmsCmiCommentlearnerDAO extends EgovAbstractDAO{
    public List selectLcmsCmiCommentlearnerPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsCmiCommentlearnerDAO.selectLcmsCmiCommentlearnerPageList", commandMap);
    }

    public int  selectLcmsCmiCommentlearnerPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsCmiCommentlearnerDAO.selectLcmsCmiCommentlearnerPageListTotCnt", commandMap);
    }

    public List selectLcmsCmiCommentlearnerList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsCmiCommentlearnerDAO.selectLcmsCmiCommentlearnerList", commandMap);
    }

    public Object selectLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiCommentlearnerDAO.selectLcmsCmiCommentlearner", commandMap);
    }

    public Object insertLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsCmiCommentlearnerDAO.insertLcmsCmiCommentlearner", commandMap);
    }

    public int updateLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiCommentlearnerDAO.updateLcmsCmiCommentlearner", commandMap);
    }

    public int updateFieldLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception{
        return update("lcmsCmiCommentlearnerDAO.updateFieldLcmsCmiCommentlearner", commandMap);
    }

    public int deleteLcmsCmiCommentlearner( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiCommentlearnerDAO.deleteLcmsCmiCommentlearner", commandMap);
    }

    public int deleteLcmsCmiCommentlearnerAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsCmiCommentlearnerDAO.deleteAllLcmsCmiCommentlearner", commandMap);
    }

    public Object existLcmsCmiCommentlearner( LcmsCmiCommentlearner lcmsCmiCommentlearner) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsCmiCommentlearnerDAO.existLcmsCmiCommentlearner", lcmsCmiCommentlearner);
    }

}
