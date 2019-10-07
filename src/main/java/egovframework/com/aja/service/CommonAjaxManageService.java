package  egovframework.com.aja.service;

import java.util.List;
import java.util.Map;

/** 
 * AJAX 호출에 관한 공통 서비스 인터페이스 클래스를 정의한다.
 */

public interface CommonAjaxManageService {

	
	/**
	 * 공통 조회 List
	 * @param Map commandMap
	 * @return List
	 * @exception Exception
	 */
	List selectCommonAjaxManageList(Map<String, Object> commandMap, String sqlStr) throws Exception;
	
	/**
	 * 공통 조회 int
	 * @param commandMap Map<String, Object>
	 * @return int
	 * @exception Exception
	 */
	int selectCommonAjaxManageInt(Map<String, Object> commandMap, String sqlStr) throws Exception;
	
	/**
	 * 공통 Insert int
	 * @param commandMap Map<String, Object>
	 * @return int
	 * @exception Exception
	 */
	int insertCommonAjaxManageInt(Map<String, Object> commandMap, String sqlStr) throws Exception;
	
	/**
	 * 공통 Update int
	 * @param commandMap Map<String, Object>
	 * @return int
	 * @exception Exception
	 */
	int updateCommonAjaxManageInt(Map<String, Object> commandMap, String sqlStr) throws Exception;
	
	/**
	 * 공통 조회 Object
	 * @param commandMap Map<String, Object>
	 * @return int
	 * @exception Exception
	 */
	Object selectCommonAjaxManageObject(Map<String, Object> commandMap, String sqlStr) throws Exception;
	
	/**
	 * 공통 조회 map
	 * @param commandMap Map<String, Object>
	 * @return int
	 * @exception Exception
	 */
	Map selectCommonAjaxManageMap (Map<String, Object> commandMap, String sqlStr) throws Exception;
	
	/**
	 * 나이스 개인번호 중복확인
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean nicePersonalNumOverlap(Map<String, Object> commandMap, String sqlStr) throws Exception;
	public Map nicePersonalChkValue(Map<String, Object> commandMap) throws Exception;
	
}