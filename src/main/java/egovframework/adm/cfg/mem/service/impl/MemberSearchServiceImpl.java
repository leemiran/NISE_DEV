package egovframework.adm.cfg.mem.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cfg.mem.dao.MemberSearchDAO;
import egovframework.adm.cfg.mem.service.MemberSearchService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.apache.log4j.Logger;

@Service("memberSearchService")
public class MemberSearchServiceImpl extends EgovAbstractServiceImpl implements MemberSearchService{
	
	@Resource(name="memberSearchDAO")
    private MemberSearchDAO memberSearchDAO;
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public List selectMemberList(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectMemberList(commandMap);
	}
	
	public int selectSearchMemberLogListTotCnt(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectSearchMemberLogListTotCnt(commandMap);
	}
	
	public List selectSearchMemberLogList(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectSearchMemberLogList(commandMap);
	}
	
	public int selectReissueMemberPwdListTotCnt(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectReissueMemberPwdListTotCnt(commandMap);
	}
	
	public List selectReissueMemberPwdList(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectReissueMemberPwdList(commandMap);
	}
	
	public Map getStatusCount(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.getStatusCount(commandMap);
	}
	
	public int selectMemberLoginLogListTotCnt(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectMemberLoginLogListTotCnt(commandMap);
	}
	
	public List selectMemberLoginLogList(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectMemberLoginLogList(commandMap);
	}
	
	public int selectExcelPrintLogListTotCnt(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectExcelPrintLogListTotCnt(commandMap);
	}
	
	public List selectExcelPrintLogList(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectExcelPrintLogList(commandMap);
	}
	
	public List memberSearchExcelList(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.memberSearchExcelList(commandMap);
	}
	
	public String insertMemberExcel(String path, List list, Map<String, Object> commandMap) throws Exception{
		String resultMsg = "";
		
		try{
			if(list != null && list.size() > 0 ){
				int p_resncnt = 0;
				int lengthCnt = 0;
				int idCnt = 0;
				for(int i=1; i<list.size(); i++){
					Map map = (Map)list.get(i);
					String userid 	= (String)map.get("parameter1");
					String name		= (String)map.get("parameter2");
					String birth_date	= (String)map.get("parameter3");
					for( int j=1; j<list.size(); j++ ){
						Map map2 = (Map)list.get(j);
						if( birth_date.equals((String)map2.get("parameter3"))){
							p_resncnt++;
						}
						if( userid.equals((String)map2.get("parameter1"))){
							idCnt++;
						}
					}
					if( birth_date.length() != 6 ) lengthCnt++;
				}
				if( p_resncnt > 0 ){
					resultMsg = "파일에 중복된 생년월일가 있습니다.";
				}else if( idCnt > 0 ){
					resultMsg = "동일 생년월일로 등록된 아이디가 있습니다.";
					
				}else if( lengthCnt > 0 ){
					resultMsg = "생년월일 입력양식이 옳바르지 않습니다.";
					
				}else{
					for( int i=1; i<list.size(); i++ ){
						Map map = (Map)list.get(i);
						memberSearchDAO.insertMember(map);
					}
					resultMsg = egovMessageSource.getMessage("success.common.insert");
				}
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.file.isempty");
			}
			
			File del = new File(path);
			del.delete();
		}catch(Exception e){
			logger.info(e);
			File del = new File(path);
			del.delete();
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		return resultMsg;
	}
	
	public int selectExistId(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectExistId(commandMap);
	}
	
	public int insertMemberData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			int checkCnt = memberSearchDAO.selectResnoCheck(commandMap);
			if( checkCnt > 0 ){
				return -99;
			}
			memberSearchDAO.insertMemberData(commandMap);
		}catch(Exception ex ){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
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
		return memberSearchDAO.selectMemberView(commandMap);
	}
	
	
	/**
	 * 개인회원정보 업데이트 - 강사가 존재하면 강사의 정보도 같이 업데이트 한다.
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return boolean
	 * @throws Exception
	 */
	public boolean updateMemberInfo(Map<String, Object> commandMap) throws Exception{
		
		boolean isOk = false;
		try{
			
			//회원정보 업데이트
			int checkCnt = memberSearchDAO.updateMemberInfo(commandMap);
			//강사정보 업데이트 
			checkCnt += memberSearchDAO.updateMemberInTutorInfo(commandMap);
			
			if( checkCnt > 0 ){
				return true;
			}
			
		}catch(Exception ex ){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
		
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
	public boolean updateLoginFail(Map<String, Object> commandMap) throws Exception{
		
		int cnt = memberSearchDAO.updateLoginFail(commandMap);
		if(cnt > 0)
			return true;
		else
			return false;
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
		return memberSearchDAO.insertMemberSearchLog(commandMap);
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
		return memberSearchDAO.selectProposeList(commandMap);
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
		return memberSearchDAO.selectEducationList(commandMap);
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
		return memberSearchDAO.selectGraduationList(commandMap);
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
		return memberSearchDAO.selectCounselList(commandMap);
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
		return memberSearchDAO.selectCounselView(commandMap);
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
		return memberSearchDAO.insertCounsel(commandMap);
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
		return memberSearchDAO.updateCounsel(commandMap);
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
		return memberSearchDAO.deleteCounsel(commandMap);
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
		return memberSearchDAO.selectResnoCheck(commandMap);
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
	public boolean insertMemberUser(Map<String, Object> commandMap) throws Exception{
		
		boolean isOk = false;
		try{
			memberSearchDAO.insertMemberUser(commandMap);
			
			isOk = true;
			
		}catch(Exception ex ){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
		
		
	}
	
	/**
	 * 아이디/비밀번호 찾기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Map selectIdPwdSearch(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectIdPwdSearch(commandMap);
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
	public boolean updatePwdReset(Map<String, Object> commandMap) throws Exception{
		boolean isOk = false;
		try{
			int cnt = memberSearchDAO.updatePwdReset(commandMap);
			if(cnt > 0)	isOk = true;
			
		}catch(Exception ex ){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
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
	public boolean updateMemberUser(Map<String, Object> commandMap) throws Exception{
		boolean isOk = false;
		try{
			int cnt = memberSearchDAO.updateMemberUser(commandMap);
			if(cnt > 0)	isOk = true;
			
		}catch(Exception ex ){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
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
	public boolean updateMemberOutAndIn(Map<String, Object> commandMap) throws Exception {
		boolean isOk = false;
		try{
			int cnt = memberSearchDAO.updateMemberOutAndIn(commandMap);
			if(cnt > 0)	isOk = true;
			
		}catch(Exception ex ){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
	}

	
	/**
	 * 학교검색
	 */
	public List selectSearchSchool(Map<String, Object> commandMap)
			throws Exception {
		
		return memberSearchDAO.selectSearchSchool(commandMap);
	}

	public List selectSubjectList(Map<String, Object> commandMap)
			throws Exception {
		return memberSearchDAO.selectSubjectList(commandMap);
	}

	/**
	 * 아이디 통합 내역 조회
	 */
	public List idIntergrationIdSearch(Map<String, Object> commandMap)	throws Exception {
		return memberSearchDAO.idIntergrationIdSearch(commandMap);
	}

	/**
	 * 아이디 통합
	 */
	public Map<String, Object> idIntergrationIdAction(Map<String, Object> commandMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String [] userIds = null;
		int successCnt=0;
		int failCnt=0;
		if(commandMap.get("t_userIds") != null && !"".equals(commandMap.get("t_userIds"))){			
			userIds = (commandMap.get("t_userIds")+"").split(",");		
			
			for(String param : userIds){
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("t_userId", param);
				paramMap.put("p_userId", commandMap.get("p_userId"));
				paramMap.put("result", "");
				
				System.out.println("t_userId param ----> "+param);
				System.out.println("p_userId param ----> "+commandMap.get("p_userId"));
				
				//사용자가 통합 못하게 주석처리
				Map<String, Object> result = memberSearchDAO.idIntergrationIdAction(paramMap);
				
				//System.out.println("result ----> "+result.get("t_userId"));
				
				successCnt++;
				
			}
		}
		
		//logger.error("failCnt["+failCnt+"]");
		resultMap.put("successCnt", successCnt);
		//resultMap.put("failCnt", failCnt);*/
		return resultMap;
	}

	public void updateMemberAddress(Map<String, Object> commandMap)
			throws Exception {
		memberSearchDAO.updateMemberAddress(commandMap);
	}
	
	/**
	 * 교육청검색
	 */
	public List selectSearchEducationOfficePop(Map<String, Object> commandMap)
			throws Exception {
		
		return memberSearchDAO.selectSearchEducationOfficePop(commandMap);
	}
	
	//회원관리
	public List selectMemberSearchList(Map<String, Object> commandMap) throws Exception{
		System.out.println("selectMemberSearchList ---> Impl");
		return memberSearchDAO.selectMemberSearchList(commandMap);
	}
	
	//회원관리
	public int selectMemberSearchListTotCnt(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectMemberSearchListTotCnt(commandMap);
	}

	//휴면 회원 리스트
	public int selectDormantCnt(Map<String, Object> commandMap) throws Exception {
		return memberSearchDAO.selectDormantCnt(commandMap);
	}

	public void updateDormantYn(Map<String, Object> commandMap) throws Exception {
		memberSearchDAO.updateDormantYn(commandMap);
	}

	public void updateDormantYnE(Map<String, Object> commandMap)
			throws Exception {
		memberSearchDAO.updateDormantYnE(commandMap);
	}

	public void updateUserDelYn(Map<String, Object> commandMap)
			throws Exception {
		memberSearchDAO.updateUserDelYn(commandMap);
	}

	public Map selectIdPwdSearchOk(Map<String, Object> commandMap) throws Exception {
		return memberSearchDAO.selectIdPwdSearchOk(commandMap);
	}

	public boolean selectDormantOk(Map<String, Object> commandMap)
			throws Exception {
		boolean isOk = false;
		try{
			int checkCnt = memberSearchDAO.selectDormantOk(commandMap);
			
			if( checkCnt > 0 ){
				return true;
			}
			
		}catch(Exception ex ){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
	}

	public void updateDormantReset(Map<String, Object> commandMap) throws Exception {
		memberSearchDAO.updateDormantReset(commandMap);
		
	}
	
	//아이디 통합
	public List selectMemberMergeList(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectMemberMergeList(commandMap);
	}
	
	//아이디통합 Detail
	public List selectMemberMergeDetailList(Map<String, Object> commandMap) throws Exception{
		return memberSearchDAO.selectMemberMergeDetailList(commandMap);
	}
	
	/**
	 * 관리자 아이디 통합
	 */
	public Map<String, Object> idMergeUpdate(Map<String, Object> commandMap) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		
		String mergeId = commandMap.get("mergeId") != null ?  commandMap.get("mergeId").toString() : "";
		
		String []  v_t_userIds = (String []) commandMap.get("_Array_t_userIds");
		
		int successCnt=0;
		int failCnt=0;
		
		System.out.println("mergeId ----> "+mergeId);		
		System.out.println("v_t_userIds.length ----> "+ v_t_userIds.length);
		
		if(v_t_userIds.length > 0 && !"".equals(mergeId)){
			
			for ( int i = 0 ; i < v_t_userIds.length; i++ ) {
				
				if(!mergeId.equals(v_t_userIds[i])){
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					
					paramMap.put("t_userId", v_t_userIds[i]);	//asis
					paramMap.put("p_userId", mergeId);			//tobe
					paramMap.put("result", "");
					
					//System.out.println("t_userId param ----> "+param);
					System.out.println("v_t_userIds ----> "+ "["+i+"]" + v_t_userIds[i]);
					
					Map<String, Object> result = memberSearchDAO.idIntergrationIdAction(paramMap);
					
					//System.out.println("result ----> "+result.get("t_userId"));					
					successCnt++;
				}
			}
		}
		
		//logger.error("failCnt["+failCnt+"]");
		resultMap.put("successCnt", successCnt);
		//resultMap.put("failCnt", failCnt);*/
		return resultMap;
	}
}
