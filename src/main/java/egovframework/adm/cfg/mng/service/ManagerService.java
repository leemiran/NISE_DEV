package egovframework.adm.cfg.mng.service;

import java.util.List;
import java.util.Map;

public interface ManagerService {

	public List selectManagerList(Map<String, Object> commandMap) throws Exception;
	
	public List selectGadminList(Map<String, Object> commandMap) throws Exception;
	
	public Map selectManagerView(Map<String, Object> commandMap) throws Exception;
	
	public List selectManagerViewGrcode(Map<String, Object> commandMap) throws Exception;
	
	public List selectManagerViewSubj(Map<String, Object> commandMap) throws Exception;
	
	public List selectManagerViewComp(Map<String, Object> commandMap) throws Exception;
	
	public int updateManager(Map<String, Object> commandMap) throws Exception;
	
	public List selectBranchList(Map<String, Object> commandMap) throws Exception;
	
	public List getGadminSelectNop(Map<String, Object> commandMap) throws Exception;
	
	public int managerInsert(Map<String, Object> commandMap) throws Exception;
	
	public int managerDelete(Map<String, Object> commandMap) throws Exception;
		
	public int selectManagerLogTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectManagerLogList(Map<String, Object> commandMap) throws Exception;
	
}
