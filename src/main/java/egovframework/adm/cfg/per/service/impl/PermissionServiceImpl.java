package egovframework.adm.cfg.per.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cfg.per.dao.PermissionDAO;
import egovframework.adm.cfg.per.service.PermissionService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("permissionService")
public class PermissionServiceImpl extends EgovAbstractServiceImpl implements PermissionService{

	@Resource(name="permissionDAO")
    private PermissionDAO permissionDAO;
	
	public List selectPermissionList(Map<String, Object> commandMap) throws Exception{
		return permissionDAO.selectPermissionList(commandMap);
	}
	
	public Map selectPermissionInfo(Map<String, Object> commandMap) throws Exception{
		return permissionDAO.selectPermissionInfo(commandMap);
	}
	
	public List selectGadminList(Map<String, Object> commandMap) throws Exception{
		return permissionDAO.selectGadminList(commandMap);
	}
	
	public int updatePermission(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			int cnt = permissionDAO.getGadminCount(commandMap);
			String i_gadmin = "";
			if( cnt > 0 ){
				i_gadmin = (String)commandMap.get("p_gadmin");
			}else{
				String key = permissionDAO.getGadminKey(commandMap);
				i_gadmin = (String)commandMap.get("p_gadminsel")+key;
			}
			commandMap.put("i_gadmin", i_gadmin);
			permissionDAO.updatePermission(commandMap);
			
			if( ((String)commandMap.get("p_isneedgrcode")).equals("Y") ){
				permissionDAO.deleteGrcodeMan(commandMap);
				permissionDAO.insertGrcodeMan(commandMap);
			}
			if( ((String)commandMap.get("p_isneedsubj")).equals("Y") ){
				permissionDAO.deleteSubjMan(commandMap);
				permissionDAO.insertSubjMan(commandMap);
			}
			if( ((String)commandMap.get("p_isneedcomp")).equals("Y") ){
				permissionDAO.deleteCompMan(commandMap);
				permissionDAO.insertCompMan(commandMap);
			}
			if( ((String)commandMap.get("p_isneeddept")).equals("Y") ){
				permissionDAO.deleteDeptMan(commandMap);
				permissionDAO.insertDeptMan(commandMap);
			}
			
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public int deletePermission(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			
			permissionDAO.deletePermission(commandMap);
			permissionDAO.deletePermissionAuth(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public int insertPermission(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			int cnt = permissionDAO.getGadminCheck(commandMap);
			if( cnt == 0 ){
				String key = permissionDAO.getGadminKey(commandMap);
				commandMap.put("p_key", key);
				permissionDAO.insertGadmin(commandMap);
				
				if( ((String)commandMap.get("p_isneedgrcode")).equals("Y") ){
					permissionDAO.insertGrcodeMan(commandMap);
				}
				if( ((String)commandMap.get("p_isneedsubj")).equals("Y") ){
					permissionDAO.insertSubjMan(commandMap);
				}
				if( ((String)commandMap.get("p_isneedcomp")).equals("Y") ){
					permissionDAO.insertCompMan(commandMap);
				}
				if( ((String)commandMap.get("p_isneeddept")).equals("Y") ){
					permissionDAO.insertDeptMan(commandMap);
				}
				
				permissionDAO.insertMenuAuth(commandMap);
			}
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		
		return isOk;
	}
}
