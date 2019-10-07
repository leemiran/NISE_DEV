package egovframework.usr.subj.service;

import java.util.List;
import java.util.Map;

public interface UserSubjectService {
	
	/**
	 * 과정 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUserSubjectList(Map<String, Object> commandMap) throws Exception;
	

	/**
	 * 과정 기수 정보 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUserSubjectSeqList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 과정 교육후기 정보 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUserStoldCommentList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 과정 교육후기 정보 전체개수 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectUserStoldCommentTotCnt(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 과정상세정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectUserSubjectView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 
	 * 설  명 : 나이버 번호 체크 유무
	 * @modify  2016. 3. 17. 오후 9:09:12 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> niceNumCheck(Map<String, Object> commandMap) throws Exception;	

	
	
	/**
	 * 수강신청 > 수강제약사항 체크하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectUserSubjProposeCheck(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 수강신청 > 과정정보 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUserProposeSubjInfo(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 수강신청 > 결재아이디 생성하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectUserProposeGetOrderId(Map<String, Object> commandMap) throws Exception;
		
	/**
	 * 사용자 수강신청 등록(무통장/교육청일괄납부)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertUserProposeOB(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 사용자 수강신청 등록(PG사 계좌이체/신용카드)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertUserProposePG(Map<String, Object> commandMap) throws Exception;

	/**
	 * 사용자 수강신청 취소(승인처리 전 사용자 직접 삭제)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteUserPropose(Map<String, Object> commandMap) throws Exception;

	
	/**
	 * 모바일 과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List selectUserSubjectMobileList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 모바일 과정 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectUserSubjectMobileListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	
	
	
	
	
	
	
}
