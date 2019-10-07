package egovframework.usr.stu.cou.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("courseStudyDAO")
public class CourseStudyDAO extends EgovAbstractDAO{

	public List selectListSubjGong(Map<String, Object> commandMap) throws Exception{
		return list("courseStudyDAO.selectListSubjGong", commandMap);
	}
	
	public int selectListSubjGongTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("courseStudyDAO.selectListSubjGongTotCnt", commandMap);
	}
	
	public List selectListSubjGongPageList(Map<String, Object> commandMap) throws Exception{
		return list("courseStudyDAO.selectListSubjGongPageList", commandMap);
	}
	
	
	public List selectEducationSubjectList(Map<String, Object> commandMap) throws Exception{
		return list("courseStudyDAO.selectEducationSubjectList", commandMap);
	}
	
	public List selectStudingSubjList(Map<String, Object> commandMap) throws Exception{
		return list("courseStudyDAO.selectStudingSubjList", commandMap);
	}
	
	public List selectGraduationSubjectList(Map<String, Object> commandMap) throws Exception{
		return list("courseStudyDAO.selectGraduationSubjectList", commandMap);
	}
	
	public List selectGraduationYearList(Map<String, Object> commandMap) throws Exception{
		return list("courseStudyDAO.selectGraduationYearList", commandMap);
	}
	
	public List selectProposeSubjectList(Map<String, Object> commandMap) throws Exception{
		return list("courseStudyDAO.selectProposeSubjectList", commandMap);
	}
	
	public List selectstoldCommentList2(Map<String, Object> commandMap) throws Exception{
		return list("courseStudyDAO.selectstoldCommentList2", commandMap);
	}
	
	public int selectstoldCommentList2TotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("courseStudyDAO.selectstoldCommentList2TotCnt", commandMap);
	}
	
	public Object whenSubjCommentsInsert(Map<String, Object> commandMap) throws Exception{
		return insert("courseStudyDAO.whenSubjCommentsInsert", commandMap);
	}
	
	public Object whenSubjCommentsDelete(Map<String, Object> commandMap) throws Exception{
		return delete("courseStudyDAO.whenSubjCommentsDelete", commandMap);
	}

	public String selectnicePersonalNum(Map<String, Object> commandMap) throws Exception{
		
		return (String)getSqlMapClientTemplate().queryForObject("courseStudyDAO.selectnicePersonalNum", commandMap);
	}

	public List selectEducationSubjectDList(Map<String, Object> commandMap) {
		return list("courseStudyDAO.selectEducationSubjectDList", commandMap);
	}
	

	
}
