package egovframework.adm.cmg.ter.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface TermDictionaryService {

	public List selectTermDictionaryList(Map<String, Object> commandMap) throws Exception;
	
	public List selectDicGroup(Map<String, Object> commandMap) throws Exception;
	
	public int insertTermDictionary(Map<String, Object> commandMap) throws Exception;
	
	public Map selectDictionaryData(Map<String, Object> commandMap) throws Exception;
	
	public int updateTermDictionary(Map<String, Object> commandMap) throws Exception;
	
	public int deleteTermDictionary(Map<String, Object> commandMap) throws Exception;
	
	public int insertExcelUploadData(List list, Map<String, Object> commandMap) throws Exception;
}
