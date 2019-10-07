package egovframework.adm.cfg.mem.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("memberSearchDAO")
public class MemberSearchDAO extends EgovAbstractDAO{

	public List selectMemberList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectMemberList", commandMap);
	}
	
	public int selectSearchMemberLogListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("memberSearchDAO.selectSearchMemberLogListTotCnt", commandMap);
	}
	
	public List selectSearchMemberLogList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectSearchMemberLogList", commandMap);
	}
	
	public int selectReissueMemberPwdListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("memberSearchDAO.selectReissueMemberPwdListTotCnt", commandMap);
	}
	
	public List selectReissueMemberPwdList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectReissueMemberPwdList", commandMap);
	}
	
	public Map getStatusCount(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("memberSearchDAO.getStatusCount", commandMap);
	}
	
	public int selectMemberLoginLogListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("memberSearchDAO.selectMemberLoginLogListTotCnt", commandMap);
	}
	
	public List selectMemberLoginLogList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectMemberLoginLogList", commandMap);
	}
	
	public int selectExcelPrintLogListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("memberSearchDAO.selectExcelPrintLogListTotCnt", commandMap);
	}
	
	public List selectExcelPrintLogList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectExcelPrintLogList", commandMap);
	}
	
	public List memberSearchExcelList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.memberSearchExcelList", commandMap);
	}
	
	public void insertMember(Map<String, Object> commandMap) throws Exception{
		insert("memberSearchDAO.insertMember", commandMap);
	}
	
	public int selectExistId(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("memberSearchDAO.selectExistId", commandMap);
	}
	
	/**
	 * 가입여부 확인 (주민번호 체크)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int selectResnoCheck(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("memberSearchDAO.selectResnoCheck", commandMap);
	}
	
	public void insertMemberData(Map<String, Object> commandMap) throws Exception{
		insert("memberSearchDAO.insertMemberData", commandMap);
	}
	
	/**
	 * 개인회원정보 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectMemberView(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("memberSearchDAO.selectMemberView", commandMap);
	}
	
	/**
	 * 개인회원정보 업데이트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int updateMemberInfo(Map<String, Object> commandMap) throws Exception{
		return update("memberSearchDAO.updateMemberInfo", commandMap);
	}
	
	/**
	 * 개인회원정보 비밀번호 오류 횟수 초기화 (0)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int updateLoginFail(Map<String, Object> commandMap) throws Exception{
		return update("memberSearchDAO.updateLoginFail", commandMap);
	}
	
	/**
	 * 개인회원정보 수정시 해당하는 아이디의 강사의 정보도 업데이트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int updateMemberInTutorInfo(Map<String, Object> commandMap) throws Exception{
		return update("memberSearchDAO.updateMemberInTutorInfo", commandMap);
	}
	
	/**
	 * 개인회원정보 조회시 로그 남기기.
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertMemberSearchLog(Map<String, Object> commandMap) throws Exception{
		return insert("memberSearchDAO.insertMemberSearchLog", commandMap);
	}
	
	/**
	 * 개인별 수강신청 내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectProposeList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectProposeList", commandMap);
	}
	
	/**
	 * 개인별 수강과목내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectEducationList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectEducationList", commandMap);
	}
	
	/**
	 * 개인별 수료과목 내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectGraduationList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectGraduationList", commandMap);
	}
	
	
	/**
	 * 개인별 상담신청내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectCounselList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectCounselList", commandMap);
	}
	

	/**
	 * 개인별 상담신청 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectCounselView(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("memberSearchDAO.selectCounselView", commandMap);
	}
	
	/**
	 * 개인별 상담신청 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertCounsel(Map<String, Object> commandMap) throws Exception{
		return insert("memberSearchDAO.insertCounsel", commandMap);
	}
	
	/**
	 * 개인별 상담신청 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int updateCounsel(Map<String, Object> commandMap) throws Exception{
		return update("memberSearchDAO.updateCounsel", commandMap);
	}
	
	/**
	 * 개인별 상담신청 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int deleteCounsel(Map<String, Object> commandMap) throws Exception{
		return delete("memberSearchDAO.deleteCounsel", commandMap);
	}
	
	
	/**
	 * 강사등록시 회원으로도 같이 등록하기 위하여..
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertTutorInMember(Map<String, Object> commandMap) throws Exception{
		return insert("memberSearchDAO.insertTutorInMember", commandMap);
	}
	
	/**
	 * 회원가입 - 사용자쪽
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertMemberUser(Map<String, Object> commandMap) throws Exception{
		return insert("memberSearchDAO.insertMemberUser", commandMap);
	}
	
	
	/**
	 * 아이디/비밀번호 찾기 - 학습자
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectIdPwdSearch(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("memberSearchDAO.selectIdPwdSearch", commandMap);
	}
	
	/**
	 * 비밀번호초기화 -  학습자
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int updatePwdReset(Map<String, Object> commandMap) throws Exception{
		return update("memberSearchDAO.updatePwdReset", commandMap);
	}
	
	
	/**
	 * 개인정보수정 -  학습자
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int updateMemberUser(Map<String, Object> commandMap) throws Exception{
		return update("memberSearchDAO.updateMemberUser", commandMap);
	}
	
	
	/**
	 * 회원 탈퇴상태정보 변경
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int updateMemberOutAndIn(Map<String, Object> commandMap) throws Exception{
		return update("memberSearchDAO.updateMemberOutAndIn", commandMap);
	}

	public List selectSearchSchool(Map<String, Object> commandMap) {
		return list("memberSearchDAO.selectSearchSchool", commandMap);
	}

	public List selectSubjectList(Map<String, Object> commandMap) {
		return list("memberSearchDAO.selectSubjectList", commandMap);
	}

	/**
	 * 아이디 통합 내역 조회
	 */
	public List idIntergrationIdSearch(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.idIntergrationIdSearch", commandMap);
	}

	/**
	 * 아이디 통합
	 */
	public Map<String, Object>  idIntergrationIdAction(Map<String, Object> map) throws Exception {
			selectByPk("memberSearchDAO.idIntergrationIdAction", map);
		return map;
	}

	public void updateMemberAddress(Map<String, Object> commandMap) throws Exception {
		update("memberSearchDAO.updateMemberAddress",commandMap);
	}
	
	//교육청검색 
	public List selectSearchEducationOfficePop(Map<String, Object> commandMap) {
		return list("memberSearchDAO.selectSearchEducationOfficePop", commandMap);
	}
	
	//회원관리
	public List selectMemberSearchList(Map<String, Object> commandMap) throws Exception{
		System.out.println("selectMemberSearchList -----> DAO");
		return list("memberSearchDAO.selectMemberSearchList", commandMap);
		//return list("memberSearchDAO.selectMemberList", commandMap);
	}
	
	//회원관리
	public int selectMemberSearchListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("memberSearchDAO.selectMemberSearchListTotCnt", commandMap);
	}
	
	
	//휴면 회원 리스트
	public int selectDormantCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("memberSearchDAO.selectDormantCnt", commandMap);
	}
	
	public void updateDormantYn(Map<String, Object> commandMap) throws Exception{
		update("memberSearchDAO.updateDormantYn", commandMap);
	}
	
	public void updateDormantYnE(Map<String, Object> commandMap) throws Exception{
		update("memberSearchDAO.updateDormantYnE", commandMap);
	}
	
	public void updateUserDelYn(Map<String, Object> commandMap) throws Exception{
		update("memberSearchDAO.updateUserDelYn", commandMap);
	}
	
	public Map selectIdPwdSearchOk(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("memberSearchDAO.selectIdPwdSearchOk", commandMap);
	}
	
	public int selectDormantOk(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("memberSearchDAO.selectDormantOk", commandMap);
	}
	
	public void updateDormantReset(Map<String, Object> commandMap) throws Exception{
		update("memberSearchDAO.updateDormantReset", commandMap);
	}
	
	public List selectMemberMergeList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectMemberMergeList", commandMap);
	}
	
	//아이디통합 Detail
	public List selectMemberMergeDetailList(Map<String, Object> commandMap) throws Exception{
		return list("memberSearchDAO.selectMemberMergeDetailList", commandMap);
	}
	
	
}
