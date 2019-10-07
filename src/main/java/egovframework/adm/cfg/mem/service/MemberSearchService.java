package egovframework.adm.cfg.mem.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface MemberSearchService {

	public List selectMemberList(Map<String, Object> commandMap) throws Exception;
	
	public int selectSearchMemberLogListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectSearchMemberLogList(Map<String, Object> commandMap) throws Exception;
	
	public int selectReissueMemberPwdListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectReissueMemberPwdList(Map<String, Object> commandMap) throws Exception;
	
	public Map getStatusCount(Map<String, Object> commandMap) throws Exception;
	
	public int selectMemberLoginLogListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectMemberLoginLogList(Map<String, Object> commandMap) throws Exception;
	
	public int selectExcelPrintLogListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectExcelPrintLogList(Map<String, Object> commandMap) throws Exception;
	
	public List memberSearchExcelList(Map<String, Object> commandMap) throws Exception;
	
	public String insertMemberExcel(String path, List dataList, Map<String, Object> commandMap) throws Exception;
	
	public int selectExistId(Map<String, Object> commandMap) throws Exception;
	
	public int insertMemberData(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인회원정보 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectMemberView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인회원정보 비밀번호 오류 횟수 초기화 (0)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean updateLoginFail(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인회원정보 업데이트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean updateMemberInfo(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 회원 탈퇴상태정보 변경
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean updateMemberOutAndIn(Map<String, Object> commandMap) throws Exception;
	
	
	
	
	/**
	 * 개인회원정보 조회시 로그 남기기.
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertMemberSearchLog(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 개인별 수강신청 내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectProposeList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 수강과목내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectEducationList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 수료과목 내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectGraduationList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 개인별 상담신청내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List selectCounselList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 개인별 상담신청 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectCounselView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 상담신청 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertCounsel(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 상담신청 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int updateCounsel(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 상담신청 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int deleteCounsel(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 가입여부 확인 (주민번호 체크)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public int selectResnoCheck(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 회원가입 - 사용자쪽
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean insertMemberUser(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 아이디/비밀번호 찾기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectIdPwdSearch(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 비밀번호초기화 -  학습자
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean updatePwdReset(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 개인정보수정 -  학습자
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean updateMemberUser(Map<String, Object> commandMap) throws Exception;

	/**
	 * 학교검색
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSearchSchool(Map<String, Object> commandMap) throws Exception;

	/**
	 * 교사등급 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSubjectList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 아이디통합 내역 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List idIntergrationIdSearch(Map<String, Object> commandMap) throws Exception;

	/**
	 * 아이디통합
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> idIntergrationIdAction(Map<String, Object> commandMap) throws Exception;

	public void updateMemberAddress(Map<String, Object> commandMap) throws Exception;

	
	/**
	 * 교육청검색
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSearchEducationOfficePop(Map<String, Object> commandMap) throws Exception;
	
	//회원관리
	public List selectMemberSearchList(Map<String, Object> commandMap) throws Exception;
	
	//회원관리
	public int selectMemberSearchListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	
	public int selectDormantCnt(Map<String, Object> commandMap) throws Exception;
	
	//휴면 계정 1년 업데이트
	public void updateDormantYn(Map<String, Object> commandMap) throws Exception;
	
	//휴면 계정 1년 3개월 업데이트
	public void updateDormantYnE(Map<String, Object> commandMap) throws Exception;

	//휴면 계정 2년된 유저 삭제 로직
	public void updateUserDelYn(Map<String, Object> commandMap) throws Exception;

	//안심본인 인증 비밀번호 찾기
	public Map selectIdPwdSearchOk(Map<String, Object> commandMap) throws Exception;
	
	public boolean selectDormantOk(Map<String, Object> commandMap) throws Exception;
	
	public void updateDormantReset(Map<String, Object> commandMap) throws Exception;
	
	//아이디통합 
	public List selectMemberMergeList(Map<String, Object> commandMap) throws Exception;
	
	//아이디통합 Detail
	public List selectMemberMergeDetailList(Map<String, Object> commandMap) throws Exception;
	
	//아이디 통합
	public Map<String, Object> idMergeUpdate(Map<String, Object> commandMap) throws Exception;
		
}
