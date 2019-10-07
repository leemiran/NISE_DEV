package egovframework.usr.stu.std.service;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public interface StudyExamService {

	/**
	 * 응시여부체크 1,2차
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int getExamResultChk(Map<String, Object> commandMap) throws Exception;
	
	public List selectPaperQuestionExampleList(Map<String, Object> commandMap) throws Exception;
	
	public Map getPaperData(Map<String, Object> commandMap) throws Exception;
	
	public Map selectStartTime(Map<String, Object> commandMap) throws Exception;
	
	public int InsertResult(Map<String, Object> commandMap) throws Exception;
	
	public int insertExamAnswerFinish(Map<String, Object> commandMap) throws Exception;
	
	public Vector getExamnums(Map<String, Object> commandMap) throws Exception;
	
	public Vector getScore(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 재채점용 업데이트 쿼리
	 * @param commandMap
	 * @throws Exception
	 */
	public void updateTZ_examresultReRating(Map<String, Object> commandMap) throws Exception;
	/**
	 * 관리자용 - 점수저장
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int calc_score_admin(Map<String, Object> commandMap) throws Exception;
	/**
	 * 사용자용 - 점수저장
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int calc_score(Map<String, Object> commandMap) throws Exception;
	
	
	public List selectPaperQuestionExampleBankList(Map<String, Object> commandMap) throws Exception;
	
	public List selectExamBankPaperModifyList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 제출
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */	
	int updateTZ_examresultEnded(Map<String, Object> commandMap) throws Exception;

	public Map<String, Object> getExamResultInfo(Map<String, Object> commandMap);

	public List getExamInProgress(Map<String, Object> paramMap);

	public String getRetryExamChangeYn(Map<String, Object> commandMap);

	public void updateRetryExamChange(Map<String, Object> commandMap);
	
	public String selectExamResultSubmit(Map<String, Object> commandMap);

}
