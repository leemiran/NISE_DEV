package egovframework.usr.stu.std.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StudyReportService {

	public Map selectViewOrderStu(Map<String, Object> commandMap) throws Exception;
	
	public List selectProfFiles(Map<String, Object> commandMap) throws Exception;
	
	public int insertProfData(Map<String, Object> commandMap) throws Exception;
	
	public Map selectProfData(Map<String, Object> commandMap) throws Exception;
}
