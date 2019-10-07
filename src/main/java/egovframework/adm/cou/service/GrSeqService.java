package egovframework.adm.cou.service;

import java.util.List;
import java.util.Map;

public interface GrSeqService {
	/**
	 * 지정된 기수/코스가 없는 교육차수Record Add
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectGrSeqEmptyList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 기수 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectGrSeqList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 기수상세화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectGrSeqView(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 기수 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertSubjSeq(Map<String, Object> commandMap) throws Exception;
	
	
	
	/**
	 * 교육 기수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int updateGrSeq(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 교육 기수 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int deleteGrSeq(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 해당교육기수의 수강생이 존재하는지를 체크한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectProposeCnt(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 교육기수에 수강, 신청, 승인, 취소, 수료자 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectGrSeqStudentList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 교육기수 상세 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectGrSeqDetailList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 기수 전체 등록을 위한 메쏘드
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean createAllSubjSeq(Map<String, Object> commandMap) throws Exception;
	
	
	
	/**
	 * 기수 전체 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteSubjSeq(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 기수 - 출석고사장 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectGrSeqSchoolList(Map<String, Object> commandMap) throws Exception;

	
	/**
	 * 기수 상세정보 - 수정화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectGrSeqDetailView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 현재새로운기수 수정시 기수가 존재하는지를 체크한다. 
	 * @param commandMap
	 * @return 기수의 개수를 반환한다.
	 * @throws Exception
	 */
	int selectCheckSubjseq(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 단일 기수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateSubjseq(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 교육기수명 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectGrSeqName(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 교육기수 일괄 수정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUpdateSubjCourseList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 교육기수 일괄 지정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectAssignSubjCourseList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 일일 학습제한 저장하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateEdulimit(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 일일 수강신청기간 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updatePropose(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 일일 학습기간 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateEdu(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 일일 학습제한 교육기수(GrSeq) 전체 수정하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateGrSeqEdulimit(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 기수 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean assignSave(Map<String, Object> commandMap) throws Exception;

	// 2017 추가
	/**
	 * 기수지역코드리스트
	 * @return
	 * @throws Exception 
	 */
	List<?> selectAreaCodeList() throws Exception;

	String selectSubjseqAreaCodeConcat(Map<String, Object> commandMap) throws Exception;
	// 2017 추가 끝
	
	
	/**
	 * 과정 복습 / 수료처리  여부 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int updateSubjseqReviewIsclosed(Map<String, Object> commandMap) throws Exception;
	
}
