package egovframework.usr.stu.std.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studySurveyDAO")
public class StudySurveyDAO extends EgovAbstractDAO{

	public List selectEducationSubjectList(Map<String, Object> commandMap) throws Exception{
		return list("studySurveyDAO.selectEducationSubjectList", commandMap);
	}
	
	public List selectUserList(Map<String, Object> commandMap) throws Exception{
		return list("studySurveyDAO.selectUserList", commandMap);
	}
	
	public String getIsSubjSul(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("studySurveyDAO.getIsSubjSul", commandMap);
	}
	
	public int getPapernumSeq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studySurveyDAO.getPapernumSeq", commandMap);
	}
	
	public List selectPaperQuestionExampleList(Map<String, Object> commandMap) throws Exception{
		return list("studySurveyDAO.selectPaperQuestionExampleList", commandMap);
	}
	
	public String getSulNums(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("studySurveyDAO.getSulNums", commandMap);
	}
	
	public void insertSulmunUserResult(Map<String, Object> commandMap) throws Exception{
		insert("studySurveyDAO.insertSulmunUserResult", commandMap);
	}
}
