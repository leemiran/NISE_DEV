package egovframework.usr.stu.std.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyExamDAO")
public class StudyExamDAO extends EgovAbstractDAO{ 
	
	
	public Map getExamResult(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyExamDAO.getExamResult", commandMap);
	}
	
	public int getExamResultChk(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyExamDAO.getExamResultChk", commandMap);
	}
	
	
	

	public List selectPaperQuestionExampleList(Map<String, Object> commandMap) throws Exception{
		return list("studyExamDAO.selectPaperQuestionExampleList", commandMap);
	}
	
	public List getExampleData(Map<String, Object> commandMap) throws Exception{
		return list("studyExamDAO.getExampleData", commandMap);
	}
	
	public Map getExamPaperData(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyExamDAO.getExamPaperData", commandMap);
	}
	
	public List getQuestionList(Map<String, Object> commandMap) throws Exception{
		return list("studyExamDAO.getQuestionList", commandMap);
	}
	
	public int updateExam(Map<String, Object> commandMap) throws Exception{
		return update("studyExamDAO.updateExam", commandMap);
	}
	
	public Map getPaperData(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyExamDAO.getPaperData", commandMap);
	}
	
	public Map selectStartTime(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyExamDAO.selectStartTime", commandMap);
	}
	
	public String getExamType(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("studyExamDAO.getExamType", commandMap);
	}
	
	public int selectStudentCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyExamDAO.selectStudentCount", commandMap);
	}
	
	public int checkPaper(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyExamDAO.checkPaper", commandMap);
	}
	
	public int chkResultExist(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyExamDAO.chkResultExist", commandMap);
	}
	
	public int MakeExamResult(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyExamDAO.MakeExamResult", commandMap);
	}
	
	public void insertTZ_examresult(Map<String, Object> commandMap) throws Exception{
		insert("studyExamDAO.insertTZ_examresult", commandMap);
	}
	
	public void updateTZ_examresult(Map<String, Object> commandMap) throws Exception{
		update("studyExamDAO.updateTZ_examresult", commandMap);
	}
	
	/**
	 * 재채점용 업데이트 쿼리
	 * @param commandMap
	 * @throws Exception
	 */
	public void updateTZ_examresultReRating(Map<String, Object> commandMap) throws Exception{
		update("studyExamDAO.updateTZ_examresultReRating", commandMap);
	}
	
	
	
	public int IsResultScore(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyExamDAO.IsResultScore", commandMap);
	}
	
	public int SelectEndedTime(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyExamDAO.SelectEndedTime", commandMap);
	}
	
	public List selectPaperQuestionExampleBankList(Map<String, Object> commandMap) throws Exception{
		return list("studyExamDAO.selectPaperQuestionExampleBankList", commandMap);
	}
	
	
	public List selectExamBankPaperModifyList(Map<String, Object> commandMap) throws Exception{
		return list("studyExamDAO.selectExamBankPaperModifyList", commandMap);
	}
	public int updateExamResultExam(Map<String, Object> commandMap) throws Exception{
		return update("studyExamDAO.updateExamResultExam", commandMap);
	}
	
	//제출
	public int updateTZ_examresultEnded(Map<String, Object> commandMap) throws Exception{
		return update("studyExamDAO.updateTZ_examresultEnded", commandMap);
	}

	public Map<String, Object> getExamResultInfo(Map<String, Object> commandMap) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectByPk("studyExamDAO.getExamResultInfo", commandMap);
	}

	public List getExamInProgress(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return list("studyExamDAO.getExamInProgress", paramMap);
	}

	public void updateExamResultSubmit(Map<String, Object> commandMap) {
		update("studyExamDAO.updateExamResultSubmit", commandMap);
	}

	public String getRetryExamChangeYn(Map<String, Object> commandMap) {
		// TODO Auto-generated method stub
		return (String) selectByPk("studyExamDAO.getRetryExamChangeYn", commandMap);
	}

	public void updateRetryExamChange(Map<String, Object> commandMap) {
		update("studyExamDAO.updateRetryExamChange", commandMap);
	}
	
	public int selectRetrycnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyExamDAO.selectRetrycnt", commandMap);
	}
	
	public String selectExamResultSubmit(Map<String, Object> commandMap) {
		return (String) selectByPk("studyExamDAO.selectExamResultSubmit", commandMap);
	}
		
}
