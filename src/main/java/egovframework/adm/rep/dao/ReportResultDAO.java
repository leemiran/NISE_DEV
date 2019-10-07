package egovframework.adm.rep.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("reportResultDAO")
public class ReportResultDAO extends EgovAbstractDAO{

	public List selectReportResultList(Map<String, Object> commandMap) throws Exception{
		return list("reportResultDAO.selectReportResultList", commandMap);
	}
	
	public Map selectViewOrder(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("reportResultDAO.selectViewOrder", commandMap);
	}
	
	public List selectReportStudentList(Map<String, Object> commandMap) throws Exception{
		return list("reportResultDAO.selectReportStudentList", commandMap);
	}
	
	public void updateReportResultData(Map<String, Object> commandMap) throws Exception{
		update("reportResultDAO.updateReportResultData", commandMap);
	}
	
	public void insertReportResultData(Map<String, Object> commandMap) throws Exception{
		insert("reportResultDAO.insertReportResultData", commandMap);
	}
	
	public void updateReportResult(Map<String, Object> commandMap) throws Exception{
		update("reportResultDAO.updateReportResult", commandMap);
	}
	
	
}
