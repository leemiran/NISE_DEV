package egovframework.adm.cfg.aut.service;

import java.util.List;
import java.util.Map;

public interface MenuAuthorityService {

	public List selectListGadmin(Map<String, Object> commandMap) throws Exception;
	
	public List selectMenuAuthList(Map<String, Object> commandMap) throws Exception;
	
	public int menuAuthUpdate(Map<String, Object> commandMap) throws Exception;
}
