package egovframework.usr.stu.cou.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CourseStudyService {

	public List selectListSubjGong(Map<String, Object> commandMap) throws Exception;
	
	public List selectEducationSubjectList(Map<String, Object> commandMap) throws Exception;
	
	public List selectStudingSubjList(Map<String, Object> commandMap) throws Exception;
	
	public List selectGraduationSubjectList(Map<String, Object> commandMap) throws Exception;
	
	public List selectGraduationYearList(Map<String, Object> commandMap) throws Exception;
	
	public List selectProposeSubjectList(Map<String, Object> commandMap) throws Exception;
	
	public List selectstoldCommentList2(Map<String, Object> commandMap) throws Exception;
	
	public int selectstoldCommentList2TotCnt(Map<String, Object> commandMap) throws Exception;
	
	public boolean whenSubjCommentsInsert(Map<String, Object> commandMap) throws Exception;
	
	public boolean whenSubjCommentsDelete(Map<String, Object> commandMap) throws Exception;
	
	public int selectListSubjGongTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectListSubjGongPageList(Map<String, Object> commandMap) throws Exception;

	public String selectnicePersonalNum(Map<String, Object> commandMap) throws Exception;

	public List selectEducationSubjectDList(Map<String, Object> commandMap) throws Exception;
	
}
