package egovframework.adm.cou.service;

import java.util.List;
import java.util.Map;

public interface SubjectService {
	/**
	 * 과정목록총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectSubjectListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 과정 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectSubjectList(Map<String, Object> commandMap) throws Exception;
	
	public List selectAreaList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 과정상세화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectSubjectView(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 과정 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Object insertSubject(Map<String, Object> commandMap) throws Exception;
	
	
	
	/**
	 * 과정 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int updateSubject(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 과정 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int deleteSubject(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 이어보기 관련정보테이블
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int insertSubjContInfo(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 교육그룹 연결
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int insertGrpSubj(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 나의관심과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectConcernInfoList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 관심과정 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteConcernInfo(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 관심과정 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertConcernInfo(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 직무관련 -기타
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectSubjEtcCount(Map<String, Object> commandMap) throws Exception;
	
}
