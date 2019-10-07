package  egovframework.adm.lcms.sco.service;

import java.util.List;
import java.util.Map;

/** 
 * 코드관리에 관한 서비스 인터페이스 클래스를 정의한다.
 */

public interface ScormContentService {

	
	/**
	 * 과목 목록을 조회
	 * @param Map commandMap
	 * @return List
	 * @exception Exception
	 */
	List selectScormContentPageList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * list 총건수를 조회한다.
	 * @param commandMap Map<String, Object>
	 * @return int
	 * @exception Exception
	 */
	int selectScormContentListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 차시 목록을 조회
	 * @param Map commandMap
	 * @return List
	 * @exception Exception
	 */
	List selectScormNewScoPageList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 차시 등록정보 조회
	 * @param Map commandMap
	 * @return List
	 * @exception Exception
	 */
	Map selectScormCreateInfo(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 복사 대상파일의 경로 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List selectFileBaseDirList(Map<String, Object> commandMap) throws Exception;
	
	List selectProgressLogList(Map<String, Object> commandMap) throws Exception;
	
	int deleteProgressLog(Map<String, Object> commandMap) throws Exception;

}