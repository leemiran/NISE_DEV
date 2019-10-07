package egovframework.adm.cfg.per.service;

import java.util.List;
import java.util.Map;

public interface PermissionService {

	public List selectPermissionList(Map<String, Object> commandMap) throws Exception;
	
	public Map selectPermissionInfo(Map<String, Object> commandMap) throws Exception;
	
	public List selectGadminList(Map<String, Object> commandMap) throws Exception;
	
	public int updatePermission(Map<String, Object> commandMap) throws Exception;
	
	public int deletePermission(Map<String, Object> commandMap) throws Exception;
	
	public int insertPermission(Map<String, Object> commandMap) throws Exception;
}
