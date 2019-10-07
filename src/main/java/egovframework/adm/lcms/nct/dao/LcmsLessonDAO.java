/* 
 * LcmsLessonDAO.java		1.00	2011-10-11 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.nct.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.adm.lcms.nct.domain.LcmsLesson;
/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsLessonDAO.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


@Repository("lcmsLessonDAO")
public class LcmsLessonDAO extends EgovAbstractDAO{
    public List selectLcmsLessonPageList( Map<String, Object> commandMap) throws Exception{
    return list("lcmsLessonDAO.selectLcmsLessonPageList", commandMap);
    }

    public int  selectLcmsLessonPageListTotCnt( Map<String, Object> commandMap) throws Exception{
        return (Integer)getSqlMapClientTemplate().queryForObject("lcmsLessonDAO.selectLcmsLessonPageListTotCnt", commandMap);
    }

    public List selectLcmsLessonList( Map<String, Object> commandMap) throws Exception{
        return list("lcmsLessonDAO.selectLcmsLessonList", commandMap);
    }

    public Object selectLcmsLesson( Map<String, Object> commandMap) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsLessonDAO.selectLcmsLesson", commandMap);
    }

    public Object insertLcmsLesson( Map<String, Object> commandMap) throws Exception{
        return insert("lcmsLessonDAO.insertLcmsLesson", commandMap);
    }
    
    public Object insertLcmsLesson2( Map<String, Object> commandMap) throws Exception{
    	return insert("lcmsLessonDAO.insertLcmsLesson2", commandMap);
    }

    public int updateLcmsLesson( Map<String, Object> commandMap) throws Exception{
        return update("lcmsLessonDAO.updateLcmsLesson", commandMap);
    }

    public int updateFieldLcmsLesson( Map<String, Object> commandMap) throws Exception{
        return update("lcmsLessonDAO.updateFieldLcmsLesson", commandMap);
    }

    public int deleteLcmsLesson( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsLessonDAO.deleteLcmsLesson", commandMap);
    }

    public int deleteLcmsLessonAll( Map<String, Object> commandMap) throws Exception{
        return delete("lcmsLessonDAO.deleteLcmsLessonAll", commandMap);
    }

    public Object existLcmsLesson( LcmsLesson lcmsLesson) throws Exception{
        return (Object)getSqlMapClientTemplate().queryForObject("lcmsLessonDAO.existLcmsLesson", lcmsLesson);
    }
    
    public Object insertLcmsLessonXinics(Map<String, Object> commandMap) throws Exception{
    	return insert("lcmsLessonDAO.insertLcmsLessonXinics", commandMap);
    }
    
    
    public Map selectLessonData(Map<String, Object> commandMap) throws Exception{
    	return (Map)selectByPk("lcmsLessonDAO.selectLessonData", commandMap);
    }
    
    public int deleteLessonAll(Map<String, Object> commandMap) throws Exception{
    	return delete("lcmsLessonDAO.deleteLessonAll", commandMap);
    }

}
