package egovframework.adm.stu.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.adm.stu.dao.StuMemberDAO;
import egovframework.adm.stu.service.StuMemberService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("stuMemberService")
public class StuMemberServiceImpl extends EgovAbstractServiceImpl implements StuMemberService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="stuMemberDAO")
    private StuMemberDAO stuMemberDAO;
	
	/**
	 * 입과명단 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectStuMemberList(Map<String, Object> commandMap) throws Exception {
		return stuMemberDAO.selectStuMemberList(commandMap);
	}
	
	
	/**
	 * 입과인원 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectStuMemberCountList(Map<String, Object> commandMap) throws Exception {
		return stuMemberDAO.selectStuMemberCountList(commandMap);
	}

	/**
	 * 과정별 기이수현황 -- 과정정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSubjectFinishSatisView(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSubjectFinishSatisView(commandMap);
	}
	
	/**
	 * 과정별 기이수현황 -- 학습자리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSubjectFinishSatisList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSubjectFinishSatisList(commandMap);
	}
	
	
	/**
	 * 수험번호 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateStuMemberExamNum(Map<String, Object> commandMap) throws Exception {
		boolean isok = false;
		try {
			
			List list = stuMemberDAO.selectStuMemberExamNumList(commandMap);
			
			if(list != null)
			{
				for(int i=0; i<list.size(); i++)
				{
					Map mm = (Map) list.get(i);
					
					commandMap.put("p_count", i);
					commandMap.put("p_userid", mm.get("userid"));
					
					int cnt = stuMemberDAO.updateStuMemberExamNum(commandMap);
				}
				
				isok = true;
			}
		
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	/**
	 * 온라인 출석부 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectLearningTimeList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectLearningTimeList(commandMap);
	}
	
	
	/**
	 * 개인별 학습로그 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectPersonalTimeList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectPersonalTimeList(commandMap);
	}
	
	
	/**
	 * 개인별 출석부 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectAttendList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectAttendList(commandMap);
	}
	
	/**
	 * 개인별 출석여부체크하기 - 학습후 출석여부 체크
	 * @param commandMap
	 * @return int
	 * @throws Exception
	 */
	public int selectAttendCount(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectAttendCount(commandMap);
	}
	
	/**
	 * 개인별 출석부 등록 - 학습후 출석여부 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertUserAttendance(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.insertUserAttendance(commandMap);
	}
	
	/**
	 * 개인별 출석부 등록 및 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateAttendList(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		String []  _Array_p_attendyn_chk = (String []) commandMap.get("_Array_p_attendyn_chk");
		String []  _Array_p_attendyn = (String []) commandMap.get("_Array_p_attendyn");
		String []  _Array_p_attdate = (String []) commandMap.get("_Array_p_attdate");
		String []  _Array_p_reason = (String []) commandMap.get("_Array_p_reason");
		
		String  p_subj = (String) commandMap.get("p_subj");
		String  p_year = (String) commandMap.get("p_year");
		String  p_subjseq = (String) commandMap.get("p_subjseq");
		String  p_userid = (String) commandMap.get("p_userid");
		String  p_luserid = (String) commandMap.get("userid");
		try {
			
			if(_Array_p_attendyn != null)
			{
				
				
				for ( int i = 0 ; i < _Array_p_attendyn.length; i++ ) { 
				
					
					HashMap<String, Object> mm = new HashMap<String, Object>();
					mm.put("p_attendyn_chk", _Array_p_attendyn_chk[i]);
					mm.put("p_attendyn", _Array_p_attendyn[i]);
					mm.put("p_attdate", _Array_p_attdate[i]);
					mm.put("p_reason", _Array_p_reason[i]);
					mm.put("p_subj", p_subj);
					mm.put("p_year", p_year);
					mm.put("p_subjseq", p_subjseq);
					mm.put("p_userid", p_userid);
					mm.put("p_luserid", p_luserid);
					
					
					if("X".equals(_Array_p_attendyn_chk[i]) && "O".equals(_Array_p_attendyn[i]))
						stuMemberDAO.insertAttendance(mm);
					else if("O".equals(_Array_p_attendyn_chk[i]) && "X".equals(_Array_p_attendyn[i]))
						stuMemberDAO.deleteAttendance(mm);
						
				}
				
				//학습자 참여도 점수 넣어주기
	    		 stuMemberDAO.updateUserAttendanceStudentScore(commandMap);
			}
			
			isok = true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	/**
	 * 종합학습현황 리스트 총개수
	 * @param commandMap
	 * @return int
	 * @throws Exception
	 */
	public int selectTotalScoreMemberTotCnt(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectTotalScoreMemberTotCnt(commandMap);
	}
	
	
	/**
	 * 종합학습현황 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTotalScoreMemberList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectTotalScoreMemberList(commandMap);
	}
	
	
	/**
	 * 나의 교육이력 리스트 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyStudyHisList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMyStudyHisList(commandMap);
	}
	
	/**
	 * 연수행정실 리스트 상세화면 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectMyStudyHisDetailView(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMyStudyHisDetailView(commandMap);
	}
	
	
	/**
	 * 연수행정실 리스트 상세화면 결재화면 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectMyStudyHisPayCdView(Map<String, Object> commandMap) throws Exception {
		return stuMemberDAO.selectMyStudyHisPayCdView(commandMap);
	}
	
	
	
	/**
	 * 나의 설문 리스트 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMySulmunList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMySulmunList(commandMap);
	}
	
	
	/**
	 * 연수행정실 확인증 출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserReqPrint(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectUserReqPrint(commandMap);
	}
	
	
	/**
	 * 연수행정실 영수증 출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserCashPrint(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectUserCashPrint(commandMap);
	}
	
	/**
	 * 연수행정실 연수지명번호 및 출석고사장 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateLecselAttendNumber(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		
		try {
			int cnt = stuMemberDAO.updateLecselAttendNumber(commandMap);
			if(cnt > 0) isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}

	
	/**
	 * 나의 학습활동 리스트 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyActivityList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMyActivityList(commandMap);
	}
	

	/**
	 * 나의 학습활동 > 학습별 통계
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisLect(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSatisLect(commandMap);
	}

	
	/**
	 * 나의 학습활동 > 학습시간정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisTime(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSatisTime(commandMap);
	}

	
	/**
	 * 나의 학습활동 > 최근학습일
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisLastLecDate(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSatisLastLecDate(commandMap);
	}

	/**
	 * 나의 학습활동 > 강의실 접근정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisLecRoom(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSatisLecRoom(commandMap);
	}

	/**
	 * 나의 학습활동 > 게시판 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisBoard(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSatisBoard(commandMap);
	}

	/**
	 * 나의 학습활동 > 진도율
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisProgress(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSatisProgress(commandMap);
	}

	
	/**
	 * 나의 학습활동 > 출석기간
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisCheckTerm(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSatisCheckTerm(commandMap);
	}


	/**
	 * 나의 학습활동 > 출석부
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSatisCheckList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectSatisCheckList(commandMap);
	}
	
	
	/**
	 * 나의강의실 - 나의 질문방 전체개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectMyCursQnaTotCnt(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMyCursQnaTotCnt(commandMap);
	}
	
	/**
	 * 나의강의실 - 나의 질문방 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyCursQnaList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMyCursQnaList(commandMap);
	}
	
	
	/**
	 * 나의강의실 - 나의 질문방 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectMyCursQnaView(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMyCursQnaView(commandMap);
	}

	
	/**
	 * 나의강의실 - 나의 질문방 파일 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyCursQnaFileList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMyCursQnaFileList(commandMap);
	}


	/**
	 * 수료상태 처리 프로세스
	 */
	public void progressAction(Map mm, String prog) throws Exception {
		
		if(prog.equals("insert")){
			//System.out.println("!!!!!!!!  insert  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			stuMemberDAO.insertProgressStatus(mm);
		}else if(prog.equals("update")){
			//System.out.println("!!!!!!!!!!!!!!!!!!  update  !!!!!!!!!!!!!!!!!!!!!!!!!!!");
			stuMemberDAO.updateProgressStatus(mm);
		}else if(prog.equals("delete")){
			//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!  delete  !!!!!!!!!!!!!!!!!");
			stuMemberDAO.deleteProgressStatus(mm);
		}
	}

	/**
	 * 진도율 저장
	 */
	public void updateProgress(HashMap inputMap) throws Exception {
		stuMemberDAO.updateProgress(inputMap);
	}
	
	/**
	 * 나의강의실 - 수험표출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selecttestIdentificationView(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selecttestIdentificationView(commandMap);
	}
	
	
	
	/**
	 * 나의강의실 - 나의 질문방 전체개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectMyCursQnaQnaTotCnt(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMyCursQnaQnaTotCnt(commandMap);
	}
	
	/**
	 * 나의강의실 - 나의 질문방 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyCursQnaQnaList(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectMyCursQnaQnaList(commandMap);
	}
	
	public int selectLevelDay(Map<String, Object> commandMap) throws Exception{
		return stuMemberDAO.selectLevelDay(commandMap);
	}
	
}
