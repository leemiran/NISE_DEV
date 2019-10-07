package egovframework.adm.stu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StuMemberService {
	/**
	 * 입과명단 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectStuMemberList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 입과인원 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectStuMemberCountList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 과정별 기이수현황 -- 과정정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectSubjectFinishSatisView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 과정별 기이수현황 -- 학습자리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectSubjectFinishSatisList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 온라인 출석부 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectLearningTimeList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 수험번호 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateStuMemberExamNum(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 학습로그 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectPersonalTimeList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 출석부 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectAttendList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 출석부 등록 및 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateAttendList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 출석여부체크하기 - 학습후 출석여부 체크
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectAttendCount(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 개인별 출석부 등록 - 학습후 출석여부 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Object insertUserAttendance(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 종합학습현황 리스트 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectTotalScoreMemberTotCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 종합학습현황 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectTotalScoreMemberList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 나의 교육이력 리스트 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectMyStudyHisList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 연수행정실 리스트 상세화면 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectMyStudyHisDetailView(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 연수행정실 리스트 상세화면 결재화면 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectMyStudyHisPayCdView(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 나의 설문 리스트 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectMySulmunList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 연수행정실 확인증 출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectUserReqPrint(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 연수행정실 영수증 출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectUserCashPrint(Map<String, Object> commandMap) throws Exception;

	/**
	 * 연수행정실 연수지명번호 및 출석고사장 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateLecselAttendNumber(Map<String, Object> commandMap) throws Exception;

	/**
	 * 나의 학습활동 리스트 - 사용자
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectMyActivityList(Map<String, Object> commandMap) throws Exception;

	

	/**
	 * 나의 학습활동 > 학습별 통계
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectSatisLect(Map<String, Object> commandMap) throws Exception;

	
	/**
	 * 나의 학습활동 > 학습시간정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectSatisTime(Map<String, Object> commandMap) throws Exception;

	
	/**
	 * 나의 학습활동 > 최근학습일
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectSatisLastLecDate(Map<String, Object> commandMap) throws Exception;

	/**
	 * 나의 학습활동 > 강의실 접근정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectSatisLecRoom(Map<String, Object> commandMap) throws Exception;

	/**
	 * 나의 학습활동 > 게시판 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectSatisBoard(Map<String, Object> commandMap) throws Exception;

	/**
	 * 나의 학습활동 > 진도율
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectSatisProgress(Map<String, Object> commandMap) throws Exception;

	
	/**
	 * 나의 학습활동 > 출석기간
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectSatisCheckTerm(Map<String, Object> commandMap) throws Exception;


	/**
	 * 나의 학습활동 > 출석부
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectSatisCheckList(Map<String, Object> commandMap) throws Exception;



	/**
	 * 나의강의실 - 나의 질문방 전체개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectMyCursQnaTotCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 나의강의실 - 나의 질문방 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectMyCursQnaList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 나의강의실 - 나의 질문방 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectMyCursQnaView(Map<String, Object> commandMap) throws Exception;

	
	/**
	 * 나의강의실 - 나의 질문방 파일 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectMyCursQnaFileList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 수료상태 처리 프로세스
	 * @param mm
	 * @param string
	 * @throws Exception
	 */
	void progressAction(Map mm, String prog) throws Exception;

	void updateProgress(HashMap inputMap) throws Exception;

	/**
	 * 나의강의실 - 수험표출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selecttestIdentificationView(Map<String, Object> commandMap) throws Exception;

	/**
	 * 나의강의실 - 나의 질문방 전체개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectMyCursQnaQnaTotCnt(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 나의강의실 - 나의 질문방 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectMyCursQnaQnaList(Map<String, Object> commandMap) throws Exception;
	
	
	int selectLevelDay(Map<String, Object> commandMap) throws Exception;

}
