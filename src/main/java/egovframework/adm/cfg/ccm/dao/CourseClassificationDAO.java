package egovframework.adm.cfg.ccm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("courseClassificationDAO")
public class CourseClassificationDAO extends EgovAbstractDAO{

	/**
	 * 과정분류 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectCourseClassificationList(Map<String, Object> commandMap) throws Exception{
		return list("courseClassificationDAO.selectCourseClassificationList", commandMap);
	}
	
	/**
	 * 과정분류 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateCourseClassification(Map<String, Object> commandMap) throws Exception{
		return update("courseClassificationDAO.updateCourseClassification", commandMap);
	}
	
	/**
	 * 과정분류 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteCourseClassification(Map<String, Object> commandMap) throws Exception{
		return update("courseClassificationDAO.deleteCourseClassification", commandMap);
	}
	
	public String getNewClassCode(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("courseClassificationDAO.getNewClassCode", commandMap);
	}
	
	public List selectClassList(Map<String, Object> commandMap) throws Exception{
		return list("courseClassificationDAO.selectClassList", commandMap);
	}
	
	public void insertCourseClassification(Map<String, Object> commandMap) throws Exception{
		insert("courseClassificationDAO.insertCourseClassification", commandMap);
	}
}
