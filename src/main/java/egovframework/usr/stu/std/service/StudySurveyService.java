package egovframework.usr.stu.std.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StudySurveyService {

	public List selectEducationSubjectList(Map<String, Object> commandMap) throws Exception;
	
	public List selectUserList(Map<String, Object> commandMap) throws Exception;
	
	public List selectPaperQuestionExampleList(Map<String, Object> commandMap) throws Exception;
	
	public int insertSulmunUserResult(Map<String, Object> commandMap) throws Exception;
}
