package egovframework.adm.cfg.mod.service;

import java.util.List;
import java.util.Map;

public interface MenuModuleService {

	public List menuModuleList(Map<String, Object> commandMap) throws Exception;
	
	public List menuModuleSubList(Map<String, Object> commandMap) throws Exception;
	
	public String getMenuName(Map<String, Object> commandMap) throws Exception;
	
	public Map getModuleInfo(Map<String, Object> commandMap) throws Exception;
	
	public List selectMenuAuthList(Map<String, Object> commandMap) throws Exception;
	
	public int deleteMenuModule(Map<String, Object> commandMap) throws Exception;
	
	public int updateMenuModule(Map<String, Object> commandMap) throws Exception;
	
	public List selectProcessList(Map<String, Object> commandMap) throws Exception;
	
	public Map selectProcessInfo(Map<String, Object> commandMap) throws Exception;
	
	public int updateProcess(Map<String, Object> commandMap) throws Exception;
	
	public int insertModule(Map<String, Object> commandMap) throws Exception;
	
	public int insertProcess(Map<String, Object> commandMap) throws Exception;
}
