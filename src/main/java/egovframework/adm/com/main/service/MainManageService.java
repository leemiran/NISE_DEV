package egovframework.adm.com.main.service;


import java.util.List;
import java.util.Map;



public interface MainManageService {
    
	/**
	 * 관리자 메인 과정리스트 
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectTotalSubSeqList(Map<String, Object> commandMap) throws Exception;


    
    /**
	 * 관리자 메인 공지사항 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectNoticeBoardList(Map<String, Object> commandMap) throws Exception;
    
    
    /**
	 * 관리자 메인 질문방, 자료실 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectPdsBoardList(Map<String, Object> commandMap) throws Exception;


    /**
     * 관리자 메인 연수문의 리스트
     * @param commandMap
     * @return
     */
	public List selectQnaBoardList(Map<String, Object> commandMap)  throws Exception;


	 /**
     * 당해 연도 운영 현황 데이터 생성
     * @param commandMap
     * @return
     */
	public List<Map<String, Object>> admYearEduStatus(Map<String, Object> commandMap) throws Exception;


	 /**
     * 당해 연도 운영 현황 내역 조회
     * @param commandMap
     * @return
     */
	public List<Map<String, Object>> admYearEduStatusList(Map<String, Object> commandMap) throws Exception;


	/**
     * 당해 연도 운영 현황 수정
     * @param commandMap
     * @return
     */
	public int admYearEduStatusUpdate(Map<String, Object> commandMap) throws Exception;


	/**
     * 당해 연도 운영 현황 삭제
     * @param commandMap
     * @return
     */
	public int admYearEduStatusDelete(Map<String, Object> commandMap) throws Exception;
    
    
    
    
}
