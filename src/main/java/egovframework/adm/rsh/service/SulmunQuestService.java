package egovframework.adm.rsh.service;

import java.util.List;
import java.util.Map;

public interface SulmunQuestService {
	/**
	 * 설문 문제 관리 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> sulmunAllQuestList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문문제 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectTzSulmunView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문문제답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectTzSulmunSelectList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문문제 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertTzSulmun(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문문제 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateTzSulmun(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문문제 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteTzSulmun(Map<String, Object> commandMap) throws Exception;
	
	
	
	
	
	
	/**
	 * 설문 척도 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectScaleList(Map<String, Object> commandMap) throws Exception;
	

	/**
	 * 설문척도보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectScaleView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문척도 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertScale(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문척도 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateScale(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문척도 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteScale(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectSulPaperList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문지 전체 문제 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectSulPaperAllQuestList(Map<String, Object> commandMap) throws Exception;
	
	
	
	/**
	 * 설문지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean insertSulPaper(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문지 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean updateSulPaper(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문지 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	boolean deleteSulPaper(Map<String, Object> commandMap) throws Exception;

	
	/**
	 * 설문지 미리보기 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectSulPaperPreviewList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 과정의 대한 설문 문제 정보 모두 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectSulResultSulNumsList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문 학습자 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectSulResultAnswersList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 설문의 대한 수강생 전체인원수를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectSulStudentMemberCount(Map<String, Object> commandMap) throws Exception;

	/**
	 * 교육후기 리스트
	 * @param commandMap
	 * @return
	 */
	List<?> selectHukiList(Map<String, Object> commandMap)  throws Exception;
	
	
	/**
	 * 표준편차
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	Map<?, ?>  selectSulResultStddev(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 퍼센트 과정의 대한 설문 문제 정보 모두 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectPerSulResultSulNumsList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 퍼센트 설문 학습자 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> selectPerSulResultAnswersList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 퍼센트 설문의 대한 수강생 전체인원수를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	int selectPerSulStudentMemberCount(Map<String, Object> commandMap) throws Exception;
	
}
