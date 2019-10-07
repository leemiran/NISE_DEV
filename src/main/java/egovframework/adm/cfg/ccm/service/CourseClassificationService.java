package egovframework.adm.cfg.ccm.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CourseClassificationService {

	public List selectCourseClassificationList(Map<String, Object> commandMap) throws Exception;
	
	public int updateCourseClassification(Map<String, Object> commandMap) throws Exception;
	
	public int deleteCourseClassification(Map<String, Object> commandMap) throws Exception;
	
	public String getNewClassCode(Map<String, Object> commandMap) throws Exception;
	
	public List selectClassList(Map<String, Object> commandMap) throws Exception;
	
	public int insertCourseClassification(Map<String, Object> commandMap) throws Exception;
	
}
