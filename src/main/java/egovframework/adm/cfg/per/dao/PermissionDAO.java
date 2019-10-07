package egovframework.adm.cfg.per.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("permissionDAO")
public class PermissionDAO extends EgovAbstractDAO{

	public List selectPermissionList(Map<String, Object> commandMap) throws Exception{
		return list("permissionDAO.selectPermissionList", commandMap);
	}
	
	public Map selectPermissionInfo(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("permissionDAO.selectPermissionInfo", commandMap);
	}
	
	public List selectGadminList(Map<String, Object> commandMap) throws Exception{
		return list("permissionDAO.selectGadminList", commandMap);
	}
	
	public int updatePermission(Map<String, Object> commandMap) throws Exception{
		return update("permissionDAO.updatePermission", commandMap);
	}
	
	public int getGadminCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("permissionDAO.getGadminCount", commandMap);
	}
	
	public String getGadminKey(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("permissionDAO.getGadminKey", commandMap);
	}
	
	public int deleteGrcodeMan(Map<String, Object> commandMap) throws Exception{
		return delete("permissionDAO.deleteGrcodeMan", commandMap);
	}
	
	public void insertGrcodeMan(Map<String, Object> commandMap) throws Exception{
		insert("permissionDAO.insertGrcodeMan", commandMap);
	}
	
	public int deleteSubjMan(Map<String, Object> commandMap) throws Exception{
		return delete("permissionDAO.deleteSubjMan", commandMap);
	}
	
	public void insertSubjMan(Map<String, Object> commandMap) throws Exception{
		insert("permissionDAO.insertSubjMan", commandMap);
	}
	
	public int deleteCompMan(Map<String, Object> commandMap) throws Exception{
		return delete("permissionDAO.deleteCompMan", commandMap);
	}
	
	public void insertCompMan(Map<String, Object> commandMap) throws Exception{
		insert("permissionDAO.insertCompMan", commandMap);
	}
	
	public int deleteDeptMan(Map<String, Object> commandMap) throws Exception{
		return delete("permissionDAO.deleteDeptMan", commandMap);
	}
	
	public void insertDeptMan(Map<String, Object> commandMap) throws Exception{
		insert("permissionDAO.insertDeptMan", commandMap);
	}
	
	public int deletePermission(Map<String, Object> commandMap) throws Exception{
		return delete("permissionDAO.deletePermission", commandMap);
	}
	
	public int deletePermissionAuth(Map<String, Object> commandMap) throws Exception{
		return delete("permissionDAO.deletePermissionAuth", commandMap);
	}
	
	public int getGadminCheck(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("permissionDAO.getGadminCheck", commandMap);
	}
	
	public void insertGadmin(Map<String, Object> commandMap) throws Exception{
		insert("permissionDAO.insertGadmin", commandMap);
	}
	
	public void insertMenuAuth(Map<String, Object> commandMap) throws Exception{
		insert("permissionDAO.insertMenuAuth", commandMap);
	}
}
