package egovframework.usr.info.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface LeariningGuidService {

	
	List<?> selectEduTrainingScheduleList(Map<String, Object> commandMap) throws Exception;
	
	public List selectEduTrainingFileList(Map<String, Object> commandMap) throws Exception;
	
	List<?> selectEduTrainingCourseList(Map<String, Object> commandMap) throws Exception;
	
	public Map<?, ?> selectEduTrainingView(Map<String, Object> commandMap) throws Exception ;
	
}
