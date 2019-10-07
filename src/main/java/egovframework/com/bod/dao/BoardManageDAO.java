package egovframework.com.bod.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("boardManageDAO")
public class BoardManageDAO extends EgovAbstractDAO{

	public List selectBoardListForAdmin(Map<String, Object> commandMap) throws Exception{
		return list("boardManageDAO.selectBoardListForAdmin", commandMap);
	}
	
	public int selectBoardListForAdminBySubjseqTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("boardManageDAO.selectBoardListForAdminBySubjseqTotCnt", commandMap);
	}
	
	public List selectBoardListForAdminBySubjseq(Map<String, Object> commandMap) throws Exception{
		return list("boardManageDAO.selectBoardListForAdminBySubjseq", commandMap);
	}
	
	public List selectBoard(Map<String, Object> commandMap) throws Exception{
		return list("boardManageDAO.selectBoard", commandMap);
	}
	
	public int selectMaxFileseq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("boardManageDAO.selectMaxFileseq", commandMap);
	}
	
	public Object updateBoardData(Map<String, Object> commandMap) throws Exception{
		return update("boardManageDAO.updateBoardData", commandMap);
	}
	
	public Object deleteBoardFile(Map<String, Object> commandMap) throws Exception{
		return delete("boardManageDAO.deleteBoardFile", commandMap);
	}
	
	public Object insertBoardFile(Map<String, Object> commandMap) throws Exception{
		return insert("boardManageDAO.insertBoardFile", commandMap);
	}
	
	public int selectMaxBoardseq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("boardManageDAO.selectMaxBoardseq", commandMap);
	}
	
	public Object insertBoardData(Map<String, Object> commandMap) throws Exception{
		return insert("boardManageDAO.insertBoardData", commandMap);
	}
	
	public Object deleteBoardData(Map<String, Object> commandMap) throws Exception{
		return insert("boardManageDAO.deleteBoardData", commandMap);
	}
	
	public Object deleteBoardAllFile(Map<String, Object> commandMap) throws Exception{
		return insert("boardManageDAO.deleteBoardAllFile", commandMap);
	}
	
	public Object updateReplyPosition(Map<String, Object> commandMap) throws Exception{
		return update("boardManageDAO.updateReplyPosition", commandMap);
	}
	
	public int updateBoardViewCount(Map<String, Object> commandMap) throws Exception{
		return update("boardManageDAO.updateBoardViewCount", commandMap);
	}
}
