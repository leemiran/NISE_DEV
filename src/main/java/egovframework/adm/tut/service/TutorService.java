package egovframework.adm.tut.service;

import java.util.List;
import java.util.Map;

public interface TutorService {
	
	
	/**
	 * 강사 리스트 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectTutorTotCnt(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 강사 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectTutorList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 강사 강의 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectTutorSubjList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 강사 강의 이력 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectTutorSubjHistoryList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 강사 정보 보기 화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectTutorView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 강사 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int insertTutor(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 강사 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int updateTutor(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 강사 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int deleteTutor(Map<String, Object> commandMap) throws Exception;
	
}
