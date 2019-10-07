package egovframework.adm.prop.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("approvalDAO")
public class ApprovalDAO extends EgovAbstractDAO {

	
	/**
	 * 신청승인 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectApprovalList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.selectApprovalList", commandMap);
	}
	
	
	/**
	 * 신청승인 삭제자 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectApprovalDeleteList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.selectApprovalDeleteList", commandMap);
	}
	
	/**
	 * 신청승인 비고항목 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectApprovaEtcView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("approvalDAO.selectApprovaEtcView", commandMap);
	}
	
	/**
	 * 수강신청 비고 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateApprovalEtc(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updateApprovalEtc", commandMap);
	}
	
	
	/**
	 * 수강신청자 처리상태 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateApprovalPropose(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updateApprovalPropose", commandMap);
	}
	
	/**
	 * 수강신청자 독서통신 상태 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateApprovalProposeBook(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updateApprovalProposeBook", commandMap);
	}
	
	/**
	 * tz_student 테이블에 등록이 되어 있는지를 검사한다.
	 * @param commandMap
	 * @return .
	 * @throws Exception
	 */
	public int selectApprovalStudentCount(Map<String, Object> commandMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("approvalDAO.selectApprovalStudentCount", commandMap);
	}
	
	/**
	 * 수강신청자 tz_student 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertApprovalStudent(Map<String, Object> commandMap) throws Exception{
		return insert("approvalDAO.insertApprovalStudent", commandMap);
	}
	
	/**
	 * 수강신청자 tz_student 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteApprovalStudent(Map<String, Object> commandMap) throws Exception{
		return delete("approvalDAO.deleteApprovalStudent", commandMap);
	}
	
	/**
	 * 수강신청자 tz_cancel 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertApprovalCancel(Map<String, Object> commandMap) throws Exception{
		return insert("approvalDAO.insertApprovalCancel", commandMap);
	}
	
	/**
	 * 수강신청자 tz_propose 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteApprovalPropose(Map<String, Object> commandMap) throws Exception{
		return delete("approvalDAO.deleteApprovalPropose", commandMap);
	}
	
	
	/**
	 * 수강신청자 결제정보 사용안함으로 업데이트한다
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateApprovalPaPaymentUseYn(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updateApprovalPaPaymentUseYn", commandMap);
	}
	
	/**
	 * 수강신청자 결제정보 입금일자, 결재수단정보를 업데이트한다
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateApprovalPaPaymentEnterDtType(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updateApprovalPaPaymentEnterDtType", commandMap);
	}
	
	
	/**
	 * 수강생관리 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectStudentManagerList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.selectStudentManagerList", commandMap);
	}
	
	
	/**
	 * 수강생관리 과정완료여부 및 과정상태확인 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectStudentManagerIsClosed(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("approvalDAO.selectStudentManagerIsClosed", commandMap);
	}
	
	/**
	 * 선정할 교육대상자 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectAcceptTargetMemberList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.selectAcceptTargetMemberList", commandMap);
	}
	
	
	/**
	 * tz_propose 테이블에 등록이 되어 있는지를 검사한다.
	 * @param commandMap
	 * @return .
	 * @throws Exception
	 */
	public int selectProposeCount(Map<String, Object> commandMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("approvalDAO.selectProposeCount", commandMap);
	}
	
	
	/**
	 * tz_propose 수강자 업데이트 하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updatePropose(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updatePropose", commandMap);
	}
	
	/**
	 * tz_propose 주문정보 업데이트 하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateProposeOrdering(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updateProposeOrdering", commandMap);
	}
	
	
	/**
	 * 수강신청자 tz_propose 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertPropose(Map<String, Object> commandMap) throws Exception{
		return insert("approvalDAO.insertPropose", commandMap);
	}
	
	/**
	 * 수강신청자 결재 정보등록 pa_payment
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertPaPayment(Map<String, Object> commandMap) throws Exception{
		return insert("approvalDAO.insertPaPayment", commandMap);
	}
	
	/**
	 * 취소/반려자 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectPropCancelMemberList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.selectPropCancelMemberList", commandMap);
	}
	
	/**
	 * 환불일자 업데이트 하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updatePropCancelMemberRePay(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updatePropCancelMemberRePay", commandMap);
	}
	
	/**
	 * 사용자 수강신청/반려 리스트 #1
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserCancelPosibleList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.selectUserCancelPosibleList", commandMap);
	}
	
	
	/**
	 * 사용자 수강신청/반려 리스트 #3
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserCourseCancelList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.selectUserCourseCancelList", commandMap);
	}
	
	
	
	/**
	 * 사용자 취소/반려 리스트 #3
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserCancelList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.selectUserCancelList", commandMap);
	}
	
	

	/**
	 * 수강신청 결제정보 업데이트 하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateProposePaytype(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updateProposePaytype", commandMap);
	}
	
	
	/**
	 * PA_Payment 결제정보 업데이트 하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updatePaymentPaytype(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updatePaymentPaytype", commandMap);
	}

	/**
	 * 과정의 기수 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSubjSeqList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.selectSubjSeqList", commandMap);
	}

	/**
	 * 수강생 기수 정보 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateMemberSubjSeqInfo(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updateMemberSubjSeqInfo", commandMap);
	}

	/**
	 * 새로운 기수에 수강생 등록여부 확인
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectMemberSubjSeqInfo(Map<String, Object> commandMap)throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("approvalDAO.selectMemberSubjSeqInfo", commandMap);
	}

	/**
	 * 기수 리스트 조회
	 * @param commandMap
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> studentManagerView(	Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.studentManagerView", commandMap);
	}
	
	
	/**
	 * pa_payment 테이블에 등록이 되어 있는지를 검사한다.
	 * @param commandMap
	 * @return .
	 * @throws Exception
	 */
	public int selectPayMentCount(Map<String, Object> commandMap) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("approvalDAO.selectPayMentCount", commandMap);
	}
	
	
	/**
	 * 재수강 가능한 기수 리스트 조회
	 * @param commandMap
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> studentSubjseqList(Map<String, Object> commandMap) throws Exception{
		return list("approvalDAO.studentSubjseqList", commandMap);
	}
	
	
	/**
	 * 수강생 기수 정보 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateMemberSubjseqProc(Map<String, Object> commandMap) throws Exception{
		return update("approvalDAO.updateMemberSubjseqProc", commandMap);
	}
	
	//최초 신청 기수
	public Map selectStudentSubjseqView(Map<String, Object> commandMap) throws Exception {
		return (Map)selectByPk("approvalDAO.selectStudentSubjseqView", commandMap);
	}
	
	public Map selectSubject(Map<String, Object> commandMap) throws Exception {
		return (Map)selectByPk("approvalDAO.selectSubject", commandMap);
	}
	
	
}
