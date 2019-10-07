package egovframework.adm.cfg.amm.service;

import java.util.List;
import java.util.Map;

public interface AdminMenuManageService {

	public List adminMenuMngList(Map<String, Object> commandMap) throws Exception;
	
	public Map adminMenuMngView(Map<String, Object> commandMap) throws Exception;
	
	public int adminMenuMngUpdate(Map<String, Object> commandMap) throws Exception;
	
	public Map getUpperInfo(Map<String, Object> commandMap) throws Exception;
	
	public int adminMenuMngInsert(Map<String, Object> commandMap) throws Exception;
}
