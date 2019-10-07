package egovframework.adm.prop.service;

import java.util.List;
import java.util.Map;

public interface ApprovalService {
	/**
	 * 신청승인 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectApprovalList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 신청승인 삭제자 목록 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectApprovalDeleteList(Map<String, Object> commandMap)	throws Exception;
	
	/**
	 * 신청승인 비고항목 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectApprovaEtcView(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 수강신청 비고 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateApprovalEtc(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 수강신청 처리상태(승인, 취소, 반려, 삭제) 이하 정보 수정 및 수강생으로 등록처리 프로세스
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean approvalProcess(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 수강생관리 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectStudentManagerList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 수강생관리 과정완료여부 및 과정상태확인 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectStudentManagerIsClosed(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 선정할 교육대상자 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectAcceptTargetMemberList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 선정된 교육대상자 입과처리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> acceptTargetMember(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 수강생관리 프로세스
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean studentManagerProcess(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 취소/반려자 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectPropCancelMemberList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 환불일자 업데이트 하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updatePropCancelMemberRePay(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 사용자 수강신청/반려 리스트 #1
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUserCancelPosibleList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 사용자 수강신청/반려 리스트 #3
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUserCourseCancelList(Map<String, Object> commandMap) throws Exception;
	
	
	
	/**
	 * 사용자 취소/반려 리스트 #3
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUserCancelList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 사용자 수강취소 프로세스 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean propUserCancelAction(Map<String, Object> commandMap) throws Exception;
	
	

	/**
	 * 수강신청 결제정보변경
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean studentPayTypeProcess(Map<String, Object> commandMap) throws Exception;

	/**
	 * 과정의 기수 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectSubjSeqList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 수강생 기수 정보 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> updateMemberSubjSeqInfo(Map<String, Object> commandMap) throws Exception;

	/**
	 * 기수 리스트 조회
	 * @param commandMap
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> studentManagerView(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 재수강 가능한 기수 리스트 조회
	 * @param commandMap
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	List<Map<String, Object>> studentSubjseqList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 재수강 정보 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> updateMemberSubjseqProc(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 최초 신청 기수
	 */
	public Map selectStudentSubjseqView(Map<String, Object> commandMap) throws Exception;
	
	
}
