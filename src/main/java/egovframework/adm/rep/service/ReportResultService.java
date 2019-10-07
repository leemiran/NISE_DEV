package egovframework.adm.rep.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ReportResultService {

	public List selectReportResultList(Map<String, Object> commandMap) throws Exception;
	
	public Map selectViewOrder(Map<String, Object> commandMap) throws Exception;
	
	public List selectReportStudentList(Map<String, Object> commandMap) throws Exception;
	
	public int updateReportResultScore(Map<String, Object> commandMap) throws Exception;
	
	public int insertExcelToDBNew(String path, List list, Map<String, Object> commandMap) throws Exception;
}
