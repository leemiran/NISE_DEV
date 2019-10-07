package egovframework.usr.stu.cou.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usr.stu.cou.dao.CourseStudyDAO;
import egovframework.usr.stu.cou.service.CourseStudyService;

@Service("courseStudyService")
public class CourseStudyServiceImpl extends EgovAbstractServiceImpl implements CourseStudyService{
	
	@Resource(name="courseStudyDAO")
    private CourseStudyDAO courseStudyDAO;

	public List selectListSubjGong(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectListSubjGong(commandMap);
	}
	
	public int selectListSubjGongTotCnt(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectListSubjGongTotCnt(commandMap);
	}
	
	public List selectListSubjGongPageList(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectListSubjGongPageList(commandMap);
	}
	
	
	public List selectEducationSubjectList(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectEducationSubjectList(commandMap);
	}
	
	public List selectStudingSubjList(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectStudingSubjList(commandMap);
	}
	
	public List selectGraduationSubjectList(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectGraduationSubjectList(commandMap);
	}
	
	public List selectGraduationYearList(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectGraduationYearList(commandMap);
	}
	
	public List selectProposeSubjectList(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectProposeSubjectList(commandMap);
	}

	public List selectstoldCommentList2(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectstoldCommentList2(commandMap);
	}
	 
	public int selectstoldCommentList2TotCnt(Map<String, Object> commandMap) throws Exception{
		return courseStudyDAO.selectstoldCommentList2TotCnt(commandMap);
	}
	
	public boolean whenSubjCommentsInsert(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try{
			isok = true;
			courseStudyDAO.whenSubjCommentsInsert(commandMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	return isok;
	}
	
	public boolean whenSubjCommentsDelete(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try{
			isok = true;
			courseStudyDAO.whenSubjCommentsDelete(commandMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	return isok;
	}

	public String selectnicePersonalNum(Map<String, Object> commandMap)
			throws Exception {
		
		return courseStudyDAO.selectnicePersonalNum(commandMap);
	}

	public List selectEducationSubjectDList(Map<String, Object> commandMap)
			throws Exception {
		return courseStudyDAO.selectEducationSubjectDList(commandMap);
	}
}
