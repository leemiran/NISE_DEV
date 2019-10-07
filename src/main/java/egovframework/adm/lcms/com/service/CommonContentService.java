package egovframework.adm.lcms.com.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CommonContentService {

	public Map selectMasterformData(Map<String, Object> commandMap) throws Exception;
	
	public List selectMFMenuList(Map<String, Object> commandMap) throws Exception;
	
	public List selectMFSubjList(Map<String, Object> commandMap) throws Exception;
	
	public int updateMasterform(Map<String, Object> commandMap) throws Exception;
}