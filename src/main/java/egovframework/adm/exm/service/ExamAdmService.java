package egovframework.adm.exm.service;

import java.util.List;
import java.util.Map;

public interface ExamAdmService {
	
	
	/**
	 * 평가문제 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamQuestList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제 미리보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamQuestPreviewList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제 체크항목 모두 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteQuestCheckExamList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가마스터 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamMasterList(Map<String, Object> commandMap) throws Exception;
	
	
	
	/**
	 * 평가문제지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamPaperList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제지 정보 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectExamPaperView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 문제지가 출제가 되었는지를 검사한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectExamResultCount(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가결과조회 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamResultList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 과정별 평가문제지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int insertExamPaperSubject(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 기수별 평가문제지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int insertExamPaperGrSeq(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제지  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateExamPaper(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제지 기간연장  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateExamPaperDate(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제지  삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteExamPaper(Map<String, Object> commandMap) throws Exception;
	
	
	
	/**
	 * 평가결과 조회 전체평균정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectExamResultAvg(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 학습자 재응시 횟수 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int upddateExamUserRetry(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가자 평가 정보 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUserPaperResultList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 학습자별 평가 답안 정보 - 풀어서보여주기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectUserExamResultAnswerList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 학습자별 평가답안 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectUserExamResultList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제 Pool 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamQuestPoolList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제 Pool 문제등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertTzExamPoolSelect(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectQuestExamView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제 해당 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectQuestExamSelList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertTzExam(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제수정
	 * @param commandMap
	 * @return -1 : 사용중인 문항임
	 * @return -2 : 에러
	 * @return  0 : 정상종료
	 * @throws Exception
	 */
	int updateTzExam(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가마스터관리에서 사용할 수 있는 문제의 개수를 뽑아오는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamLevelsList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가마스터등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertTzExamMaster(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가마스터 정보 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectExamMasterView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가마스터가 문제지에서 사용되었는지를 확인한다
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectExamMasterPaperCount(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가마스터  삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteTzExamMaster(Map<String, Object> commandMap) throws Exception;
	
	

	/**
	 * 평가마스터  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateTzExamMaster(Map<String, Object> commandMap) throws Exception;

	/**
	 * 평가POOL 과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List selectExamPoolSubjList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 평가POOL 팝업 과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List examPoolSubjListPop(Map<String, Object> commandMap) throws Exception;

	/**
	 * 평가POOL 과정 문제 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List examQuestionDetailList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 평가 문제 복사
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> examQuestPoolCopy(Map<String, Object> commandMap) throws Exception;


	void insertExamFileToDB(List result, Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제은행 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List selectExamBankList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제은행 과정등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertTzExamSubj(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제은행 과정 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	Object selectTzExamSubj(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 문제은행 과정 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateTzExamSubj(Map<String, Object> commandMap) throws Exception;
	

	/**
	 * 평가문제은행 문제 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List examBankDetailList(Map<String, Object> commandMap) throws Exception;

		
	/**
	 * 평가마스터등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertExamBankTzExamMaster(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 기수별 평가문제지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int insertExamBankPaperGrSeq(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 문제지
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamBankLevelsList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectExamBankPaperView(Map<String, Object> commandMap) throws Exception;
	
	

	/**
	 * 평가문제지  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateExamBankPaper(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 콘텐츠 문제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectExamBankSubj(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 기수별 평가문제지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectExamBankPaperCount(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제지 기간연장  수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateExamBankPaperDate(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가 기본정보 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateExamBankPaperBasic(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제지  삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteExamBankPaper(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 출제된적이 있는가
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectExamBankResultCount(Map<String, Object> commandMap) throws Exception;
	
	//문제엑셀등록
	void insertExamBankFileToDB(List result, Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectExamBankView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제 해당 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamBankSelList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제수정
	 * @param commandMap
	 * @return -1 : 사용중인 문항임
	 * @return -2 : 에러
	 * @return  0 : 정상종료
	 * @throws Exception
	 */
	int updateTzExamBank(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제 체크항목 모두 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteExamBankCheckList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제지 문제 삭제 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteExamBankPaperSheetExam(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제지 문제 삭제 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteExamBankPaperSheetExamChange(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 문제지가 출제가 되었는지를 검사한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectExamBankNumResultSubjseqCount(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 교체할 문제가 있는가?
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectExamBankPaperSheetExamCount(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 평가문제은행 다운로드
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectExamBankExcelDownList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 평가문제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?> selectExamBankLevelsCnt(Map<String, Object> commandMap) throws Exception;
	
	//문제 미리보기
	public List selectExamBankPaperExamViewList(Map<String, Object> commandMap) throws Exception;
}
