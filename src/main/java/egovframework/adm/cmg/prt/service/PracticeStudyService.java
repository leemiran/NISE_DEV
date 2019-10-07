package egovframework.adm.cmg.prt.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface PracticeStudyService {

	public List practiceStudyList(Map<String, Object> commandMap) throws Exception;
	
	public List practiceStudySubList(Map<String, Object> commandMap) throws Exception;
	
	public List subjmanPracticeStudyList(Map<String, Object> commandMap) throws Exception;
	
}
