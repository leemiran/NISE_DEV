package egovframework.adm.fin.service;

import java.util.List;
import java.util.Map;

public interface FinishInputService {
	
	public int excelMemberInsert(List list, Map<String, Object> commandMap) throws Exception;
}
