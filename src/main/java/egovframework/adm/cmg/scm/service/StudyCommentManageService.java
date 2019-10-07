package egovframework.adm.cmg.scm.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StudyCommentManageService {

	public List selectCommentDateList(Map<String, Object> commandMap) throws Exception;
	
	public List selectCommentList(Map<String, Object> commandMap) throws Exception;
	
	public List selectCommentSubList(Map<String, Object> commandMap) throws Exception;
	
	public int updateCommentDateList(Map<String, Object> commandMap) throws Exception;
}
