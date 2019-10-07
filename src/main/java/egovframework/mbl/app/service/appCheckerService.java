package egovframework.mbl.app.service;

import java.util.Map;

public interface appCheckerService {
	
	/**
	 * 버젼이 현재 버젼보다 높은개 있는지를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean selectAppCheckerResult(Map<String, Object> commandMap) throws Exception;
	
	
	
}
