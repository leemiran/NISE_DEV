package egovframework.com.bod.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface BoardManageService {

	public List selectBoardListForAdmin(Map<String, Object> commandMap) throws Exception;
	
	public int selectBoardListForAdminBySubjseqTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectBoardListForAdminBySubjseq(Map<String, Object> commandMap) throws Exception;
	
	public List selectBoard(Map<String, Object> commandMap) throws Exception;
	
	public int updateBoardData(Map<String, Object> commandMap) throws Exception;
	
	public int insertBoardData(Map<String, Object> commandMap) throws Exception;
	
	public int deleteBoardData(Map<String, Object> commandMap) throws Exception;
	
	public int updateBoardViewCount(Map<String, Object> commandMap) throws Exception;
}
