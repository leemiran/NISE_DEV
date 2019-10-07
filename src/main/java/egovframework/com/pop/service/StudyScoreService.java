package egovframework.com.pop.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StudyScoreService {

	public Map selectEduScore(Map<String, Object> commandMap) throws Exception;
	
	public List selectExamData(Map<String, Object> commandMap) throws Exception;
	
	public Map selectProjData(Map<String, Object> commandMap) throws Exception;
	
	public Map selectSulData(Map<String, Object> commandMap) throws Exception;
}
