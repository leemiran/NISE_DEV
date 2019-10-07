package egovframework.usr.subj.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("userSubjectDAO")
public class UserSubjectDAO extends EgovAbstractDAO {

	
	/**
	 * 과정 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserSubjectList(Map<String, Object> commandMap) throws Exception{
		return list("userSubjectDAO.selectUserSubjectList", commandMap);
	}
	
	
	/**
	 * 과정 기수 정보 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserSubjectSeqList(Map<String, Object> commandMap) throws Exception{
		return list("userSubjectDAO.selectUserSubjectSeqList", commandMap);
	}
	
	
	/**
	 * 과정 교육후기 정보 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserStoldCommentList(Map<String, Object> commandMap) throws Exception{
		return list("userSubjectDAO.selectUserStoldCommentList", commandMap);
	}
	
	/**
	 * 과정 교육후기 정보 전체개수 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectUserStoldCommentTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("userSubjectDAO.selectUserStoldCommentTotCnt", commandMap);
	}
	
	
	/**
	 * 과정상세정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserSubjectView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("userSubjectDAO.selectUserSubjectView", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 17. 오후 9:10:04 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> niceNumCheck(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("userSubjectDAO.niceNumCheck", commandMap);
	}

	/**
	 * 수강신청 > 수강제약사항 체크하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserSubjProposeCheck(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("userSubjectDAO.selectUserSubjProposeCheck", commandMap);
	}

	/**
	 * 수강신청 > 과정정보 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectUserProposeSubjInfo(Map<String, Object> commandMap) throws Exception {
		return list("userSubjectDAO.selectUserProposeSubjInfo", commandMap);
	}

	/**
	 * 수강신청 > 결재아이디 생성하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserProposeGetOrderId(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("userSubjectDAO.selectUserProposeGetOrderId", commandMap);
	}
	
	
	/**
	 * tz_propose에 수강신청 등록 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertUserPropose(Map<String, Object> commandMap) throws Exception{
		return insert("userSubjectDAO.insertUserPropose", commandMap);
	}
	
	
	/**
	 * tz_payment에 결제정보 등록 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertUserPayment(Map<String, Object> commandMap) throws Exception{
		return insert("userSubjectDAO.insertUserPayment", commandMap);
	}
	
	/**
	 * tz_payment에서 UseYn 상태값 변경(수강취소)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateApprovalPaPaymentUseYn(Map<String, Object> commandMap) throws Exception {
		return update("userSubjectDAO.updateApprovalPaPaymentUseYn", commandMap);
	}

	/**
	 * 수강신청에서 등록한 사용자 주소만 업데이트 하기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateUserMemberAddress(Map<String, Object> commandMap) throws Exception{
		return update("userSubjectDAO.updateUserMemberAddress", commandMap);
	}

	/**
	 * 모바일 과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectUserSubjectMobileList(Map<String, Object> commandMap) throws Exception{
		return list("userSubjectDAO.selectUserSubjectMobileList", commandMap);
	}
	
	/**
	 * 모바일 과정 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectUserSubjectMobileListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("userSubjectDAO.selectUserSubjectMobileListTotCnt", commandMap);
	}
	

	//수강등록 확인
	public int selectUserProposeCnt(Map<String, Object> commandMap)   throws Exception {
	    return ((Integer)getSqlMapClientTemplate().queryForObject("userSubjectDAO.selectUserProposeCnt", commandMap)).intValue();
	}

}
