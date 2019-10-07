package egovframework.adm.cfg.mng.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("managerDAO")
public class ManagerDAO extends EgovAbstractDAO{

	public List selectManagerList(Map<String, Object> commandMap) throws Exception{
		return list("managerDAO.selectManagerList", commandMap);
	}
	
	public List selectGadminList(Map<String, Object> commandMap) throws Exception{
		return list("managerDAO.selectGadminList", commandMap);
	}
	
	public Map selectManagerView(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("managerDAO.selectManagerView", commandMap);
	}
	
	public List selectManagerViewGrcode(Map<String, Object> commandMap) throws Exception{
		return list("managerDAO.selectManagerViewGrcode", commandMap);
	}
	
	public List selectManagerViewSubj(Map<String, Object> commandMap) throws Exception{
		return list("managerDAO.selectManagerViewSubj", commandMap);
	}
	
	public List selectManagerViewComp(Map<String, Object> commandMap) throws Exception{
		return list("managerDAO.selectManagerViewComp", commandMap);
	}
	
	public int updateManagerInfo(Map<String, Object> commandMap) throws Exception{
		return update("managerDAO.updateManagerInfo", commandMap);
	}
	
	public List selectBranchList(Map<String, Object> commandMap) throws Exception{
		return list("managerDAO.selectBranchList", commandMap);
	}
	
	public List getGadminSelectNop(Map<String, Object> commandMap) throws Exception{
		return list("managerDAO.getGadminSelectNop", commandMap);
	}
	
	public int checkManagerCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("managerDAO.checkManagerCount", commandMap);
	}
	
	public void managerInsert(Map<String, Object> commandMap) throws Exception{
		insert("managerDAO.managerInsert", commandMap);
	}
	
	public void insertGrcode(Map<String, Object> commandMap) throws Exception{
		insert("managerDAO.insertGrcode", commandMap);
	}
	
	public void insertSubj(Map<String, Object> commandMap) throws Exception{
		insert("managerDAO.insertSubj", commandMap);
	}
	
	public void insertComp(Map<String, Object> commandMap) throws Exception{
		insert("managerDAO.insertComp", commandMap);
	}
	
	public void deleteMenuAuth(Map<String, Object> commandMap) throws Exception{
		delete("managerDAO.deleteMenuAuth", commandMap);
	}
	
	public void insertMenuAuth(Map<String, Object> commandMap) throws Exception{
		insert("managerDAO.insertMenuAuth", commandMap);
	}
	
	public int deleteManager(Map<String, Object> commandMap) throws Exception{
		return delete("managerDAO.deleteManager", commandMap);
	}
	
	public int deleteGrcode(Map<String, Object> commandMap) throws Exception{
		return delete("managerDAO.deleteGrcode", commandMap);
	}
	
	public int deleteSubj(Map<String, Object> commandMap) throws Exception{
		return delete("managerDAO.deleteSubj", commandMap);
	}
	
	public int deleteComp(Map<String, Object> commandMap) throws Exception{
		return delete("managerDAO.deleteComp", commandMap);
	}
	
	public void managerInsertLog(Map<String, Object> commandMap) throws Exception{
		insert("managerDAO.managerInsertLog", commandMap);
	}
	
	public int selectManagerLogTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("managerDAO.selectManagerLogTotCnt", commandMap);
	}
	
	public List selectManagerLogList(Map<String, Object> commandMap) throws Exception{
		return list("managerDAO.selectManagerLogList", commandMap);
	}
	
	
}
