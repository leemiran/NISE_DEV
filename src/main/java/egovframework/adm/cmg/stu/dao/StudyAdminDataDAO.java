package egovframework.adm.cmg.stu.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyAdminDataDAO")
public class StudyAdminDataDAO extends EgovAbstractDAO{

	public List selectStudyAdminDataList(Map<String, Object> commandMap) throws Exception{
		return list("studyAdminDataDAO.selectStudyAdminDataList", commandMap);
	}
	
	public List selectBoardListForAdmin(Map<String, Object> commandMap) throws Exception{
		return list("studyAdminDataDAO.selectBoardListForAdmin", commandMap);
	}
	
	public List selectBoardViewData(Map<String, Object> commandMap) throws Exception{
		return list("studyAdminDataDAO.selectBoardViewData", commandMap);
	}
	
	public Object updateBoardData(Map<String, Object> commandMap) throws Exception{
		return update("studyAdminDataDAO.updateBoardData", commandMap);
	}
	
	public int selectMaxBoardseq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyAdminDataDAO.selectMaxBoardseq", commandMap);
	}
	
	public Object insertBoardData(Map<String, Object> commandMap) throws Exception{
		return insert("studyAdminDataDAO.insertBoardData", commandMap);
	}
	
	public Object deleteBoardFile(Map<String, Object> commandMap) throws Exception{
		return delete("studyAdminDataDAO.deleteBoardFile", commandMap);
	}
	
	public Object insertBoardFile(Map<String, Object> commandMap) throws Exception{
		return insert("studyAdminDataDAO.insertBoardFile", commandMap);
	}
	
	public int selectMaxFileseq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyAdminDataDAO.selectMaxFileseq", commandMap);
	}
	
	public int deleteBoardData(Map<String, Object> commandMap) throws Exception{
		return delete("studyAdminDataDAO.deleteBoardData", commandMap);
	}
	
	public Object deleteBoardAllFile(Map<String, Object> commandMap) throws Exception{
		return delete("studyAdminDataDAO.deleteBoardAllFile", commandMap);
	}
	
}
