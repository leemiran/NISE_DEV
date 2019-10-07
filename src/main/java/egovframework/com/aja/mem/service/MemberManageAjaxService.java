package  egovframework.com.aja.mem.service;

import java.util.List;
import java.util.Map;

/** 
 * 회원 관련 처리 인터페이스 클래스를 정의한다.
 */

public interface MemberManageAjaxService {

	/**
	 * 비밀번호 초기화
	 * @param commandMap Map<String, Object>
	 * @return int
	 * @exception Exception
	 */
	int updatePWInitialization(Map<String, Object> commandMap) throws Exception;

}