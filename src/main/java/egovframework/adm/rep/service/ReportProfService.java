package egovframework.adm.rep.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ReportProfService {

	public List selectReportProfList(Map<String, Object> commandMap) throws Exception;
	
	public int insertReportProfData(Map<String, Object> commandMap) throws Exception;
	
	public Map selectReportProfData(Map<String, Object> commandMap) throws Exception;
	
	public List selectProfFiles(Map<String, Object> commandMap) throws Exception;
	
	public int updateReportProfData(Map<String, Object> commandMap) throws Exception;
	
	public int reportProfWeightUpdateData(Map<String, Object> commandMap) throws Exception;
	
	public List selectReportQuestionList(Map<String, Object> commandMap) throws Exception;
	
	public Map selectReportQuestionView(Map<String, Object> commandMap) throws Exception;
		
	/**
	 * 과제 문항 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertTzReportQues(Map<String, Object> commandMap) throws Exception;
	
	//과제문항 과정의 문항 조회 
	public List selectReportQuestionSubjList(Map<String, Object> commandMap) throws Exception;
	
	//과제문항 과정의 문항 조회 
	public Map selectReportProfView(Map<String, Object> commandMap) throws Exception;
	
	
	//과제 문항 리스트
	public List selectReportQuesList(Map<String, Object> commandMap) throws Exception;
	
	//과제 등록
	public int insertReportProf(Map<String, Object> commandMap) throws Exception;	
	
	//과제 수정
	public int updateReportProf(Map<String, Object> commandMap) throws Exception;
	
	//과제 문항 수정
	public int updateTzReportQues(Map<String, Object> commandMap) throws Exception;
	
	//과제 문항 삭제
	public int deleteTzReportQues(Map<String, Object> commandMap) throws Exception;
	

	//과제 문항 리스트
	public List selectReportQuesSubjseqList(Map<String, Object> commandMap) throws Exception;
		
}
