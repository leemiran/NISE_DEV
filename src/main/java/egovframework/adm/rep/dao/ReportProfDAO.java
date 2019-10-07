package egovframework.adm.rep.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("reportProfDAO")
public class ReportProfDAO extends EgovAbstractDAO{

	public List selectReportProfList(Map<String, Object> commandMap) throws Exception{
		return list("reportProfDAO.selectReportProfList", commandMap);
	}
	
	public void insertReportProfData(Map<String, Object> commandMap) throws Exception{
		insert("reportProfDAO.insertReportProfData", commandMap);
	}
	
	public void insertFilesData(Map<String, Object> commandMap) throws Exception{
		insert("reportProfDAO.insertFilesData", commandMap);
	}
	
	public Map selectReportProfData(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("reportProfDAO.selectReportProfData", commandMap);
	}
	
	public List selectProfFiles(Map<String, Object> commandMap) throws Exception{
		return list("reportProfDAO.selectProfFiles", commandMap);
	}
	
	public int updateReportProfData(Map<String, Object> commandMap) throws Exception{
		return update("reportProfDAO.updateReportProfData", commandMap);
	}
	
	public int reportProfWeightUpdateData(Map<String, Object> commandMap) throws Exception{
		return update("reportProfDAO.reportProfWeightUpdateData", commandMap);
	}
	
	public List selectReportQuestionList(Map<String, Object> commandMap) throws Exception{
		return list("reportProfDAO.selectReportQuestionList", commandMap);
	}
	
	public Map selectReportQuestionView(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("reportProfDAO.selectReportQuestionView", commandMap);
	}
	
	/**
	 * 과제 문항 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertTzReportQues(Map<String, Object> commandMap) throws Exception{
		return insert("reportProfDAO.insertTzReportQues", commandMap);
	}
	
	//과제문항 과정의 문항 조회 
	public List selectReportQuestionSubjList(Map<String, Object> commandMap) throws Exception{
		return list("reportProfDAO.selectReportQuestionSubjList",commandMap);
	}
	
	
	public Map selectReportProfView(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("reportProfDAO.selectReportProfView", commandMap);
	}
	
	//과제 문항 리스트
	public List selectReportQuesList(Map<String, Object> commandMap) throws Exception{
		return list("reportProfDAO.selectReportQuesList", commandMap);
	}
	
	//과제 등록
	public void insertReportProf(Map<String, Object> commandMap) throws Exception{
		insert("reportProfDAO.insertReportProf", commandMap);
	}
	
	//평가과제문항 등록
	public void insertProjordSheet(Map<String, Object> commandMap) throws Exception{
		insert("reportProfDAO.insertProjordSheet", commandMap);
	}
	
	//과제 수정
	public int updateReportProf(Map<String, Object> commandMap) throws Exception{
		return update("reportProfDAO.updateReportProf", commandMap);
	}
	
	//과제 출제문항 삭제
	public int deleteProjordSheet(Map<String, Object> commandMap) throws Exception{
		return delete("reportProfDAO.deleteProjordSheet", commandMap);
	}
	
	//과제 첨부파일 삭제
	public int deleteReportProfFiles(Map<String, Object> commandMap) throws Exception{
		return delete("reportProfDAO.deleteReportProfFiles", commandMap);
	}
	
	//과제 문항 수정
	public int updateTzReportQues(Map<String, Object> commandMap) throws Exception{
		return update("reportProfDAO.updateTzReportQues", commandMap);
	}
	
	//과제 문항 삭제
	public int deleteTzReportQues(Map<String, Object> commandMap) throws Exception{
		return delete("reportProfDAO.deleteTzReportQues", commandMap);
	}
	
	//과제 출제 문항 리스트
	public List selectReportQuesSubjseqList(Map<String, Object> commandMap) throws Exception{
		return list("reportProfDAO.selectReportQuesSubjseqList", commandMap);
	}
	
}
