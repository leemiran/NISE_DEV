package egovframework.adm.cmg.stu.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StudyAdminDataService {

	public List selectStudyAdminDataList(Map<String, Object> commandMap) throws Exception;
	
	public List selectBoardListForAdmin(Map<String, Object> commandMap) throws Exception;
	
	public List selectBoardViewData(Map<String, Object> commandMap) throws Exception;
	
	public int updateBoardData(Map<String, Object> commandMap) throws Exception;
	
	public int insertBoardData(Map<String, Object> commandMap) throws Exception;
	
	public int deleteBoardData(Map<String, Object> commandMap) throws Exception;
}
