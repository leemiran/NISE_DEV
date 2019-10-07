package egovframework.adm.stu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("stuMemberDAO")
public class StuMemberDAO extends EgovAbstractDAO {

	
	/**
	 * 입과명단 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectStuMemberList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectStuMemberList", commandMap);
	}
	
	/**
	 * 입과인원 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectStuMemberCountList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectStuMemberCountList", commandMap);
	}
	
	
	/**
	 * 과정별 기이수현황 -- 과정정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSubjectFinishSatisView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>) selectByPk("stuMemberDAO.selectSubjectFinishSatisView", commandMap);
	}
	
	/**
	 * 과정별 기이수현황 -- 학습자리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSubjectFinishSatisList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectSubjectFinishSatisList", commandMap);
	}
	
	
	
	/**
	 * 온라인 출석부 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectLearningTimeList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectLearningTimeList", commandMap);
	}
	
	
	
	/**
	 * 수험번호 생성용 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectStuMemberExamNumList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectStuMemberExamNumList", commandMap);
	}
	
	/**
	 * 수험번호 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateStuMemberExamNum(Map<String, Object> commandMap) throws Exception{
		return update("stuMemberDAO.updateStuMemberExamNum", commandMap);
	}
	
	/**
	 * 개인별 학습로그 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectPersonalTimeList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectPersonalTimeList", commandMap);
	}
	
	
	/**
	 * 개인별 출석부 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectAttendList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectAttendList", commandMap);
	}
	
	
	/**
	 * 개인별 출석여부체크하기 -- 학습자용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectAttendCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("stuMemberDAO.selectAttendCount", commandMap);
	}
	
	
	
	/**
	 * 개인별 출석부 등록-- 학습자용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertUserAttendance(Map<String, Object> commandMap) throws Exception{
		return insert("stuMemberDAO.insertUserAttendance", commandMap);
	}
	
	/**
	 * 개인별 참여도 점수 넣어주기-- 학습자용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object updateUserAttendanceStudentScore(Map<String, Object> commandMap) throws Exception{
		return insert("stuMemberDAO.updateUserAttendanceStudentScore", commandMap);
	}
	
	/**
	 * 개인별 출석부 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertAttendance(Map<String, Object> commandMap) throws Exception{
		return insert("stuMemberDAO.insertAttendance", commandMap);
	}
	
	/**
	 * 개인별 출석부 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteAttendance(Map<String, Object> commandMap) throws Exception{
		return delete("stuMemberDAO.deleteAttendance", commandMap);
	}
	
	
	/**
	 * 종합학습현황 리스트 총개수
	 * @param commandMap
	 * @return int
	 * @throws Exception
	 */
	public int selectTotalScoreMemberTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("stuMemberDAO.selectTotalScoreMemberTotCnt", commandMap);
	}
	
	/**
	 * 종합학습현황 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTotalScoreMemberList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectTotalScoreMemberList", commandMap);
	}
	
	/**
	 * 나의 교육이력 리스트 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyStudyHisList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectMyStudyHisList", commandMap);
	}
	
	/**
	 * 연수행정실 리스트 상세화면 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectMyStudyHisDetailView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectMyStudyHisDetailView", commandMap);
	}
	
	/**
	 * 연수행정실 리스트 상세화면 결재화면 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectMyStudyHisPayCdView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectMyStudyHisPayCdView", commandMap);
	}
	
	
	/**
	 * 나의 설문 리스트 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMySulmunList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectMySulmunList", commandMap);
	}
	
	
	/**
	 * 연수행정실 확인증 출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserReqPrint(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectUserReqPrint", commandMap);
	}
	
	
	/**
	 * 연수행정실 영수증 출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectUserCashPrint(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectUserCashPrint", commandMap);
	}
	
	
	/**
	 * 연수행정실 연수지명번호 및 출석고사장 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateLecselAttendNumber(Map<String, Object> commandMap) throws Exception{
		return update("stuMemberDAO.updateLecselAttendNumber", commandMap);
	}

	
	
	
	
	

	/**
	 * 나의 학습활동 리스트 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyActivityList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectMyActivityList", commandMap);
	}


	/**
	 * 나의 학습활동 > 학습별 통계
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisLect(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectSatisLect", commandMap);
	}

	
	/**
	 * 나의 학습활동 > 학습시간정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisTime(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectSatisTime", commandMap);
	}

	
	/**
	 * 나의 학습활동 > 최근학습일
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisLastLecDate(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectSatisLastLecDate", commandMap);
	}

	/**
	 * 나의 학습활동 > 강의실 접근정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisLecRoom(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectSatisLecRoom", commandMap);
	}

	/**
	 * 나의 학습활동 > 게시판 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisBoard(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectSatisBoard", commandMap);
	}

	/**
	 * 나의 학습활동 > 진도율
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisProgress(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectSatisProgress", commandMap);
	}

	
	/**
	 * 나의 학습활동 > 출석기간
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSatisCheckTerm(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectSatisCheckTerm", commandMap);
	}


	/**
	 * 나의 학습활동 > 출석부
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSatisCheckList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectSatisCheckList", commandMap);
	}


	/**
	 * 나의강의실 - 나의 질문방 전체개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectMyCursQnaTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("stuMemberDAO.selectMyCursQnaTotCnt", commandMap);
	}
	
	/**
	 * 나의강의실 - 나의 질문방 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyCursQnaList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectMyCursQnaList", commandMap);
	}
	
	
	/**
	 * 나의강의실 - 나의 질문방 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectMyCursQnaView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selectMyCursQnaView", commandMap);
	}

	
	/**
	 * 나의강의실 - 나의 질문방 파일 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyCursQnaFileList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectMyCursQnaFileList", commandMap);
	}

	public void insertProgressStatus(Map mm) throws Exception{
		insert("stuMemberDAO.insertProgressStatus", mm);
	}

	public void updateProgressStatus(Map mm) throws Exception{
		update("stuMemberDAO.updateProgressStatus", mm);
	}

	public void deleteProgressStatus(Map mm) throws Exception{
		delete("stuMemberDAO.deleteProgressStatus", mm);
	}

	public void updateProgress(HashMap inputMap) throws Exception{
		update("lcmsProgressDAO.updateNormalOldProgress", inputMap);
	}
	
	/**
	 * 나의강의실 - 수험표출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selecttestIdentificationView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("stuMemberDAO.selecttestIdentificationView", commandMap);
	}
	
	
	/**
	 * 나의강의실 - 나의 질문방 전체개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectMyCursQnaQnaTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("stuMemberDAO.selectMyCursQnaQnaTotCnt", commandMap);
	}
	
	/**
	 * 나의강의실 - 나의 질문방 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectMyCursQnaQnaList(Map<String, Object> commandMap) throws Exception{
		return list("stuMemberDAO.selectMyCursQnaQnaList", commandMap);
	}
	
	public int selectLevelDay(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("stuMemberDAO.selectLevelDay", commandMap);
	}

	
	
}
