package egovframework.adm.fin.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface FinishManageService {

	public List selectFinishCourseList(Map<String, Object> commandMap) throws Exception;
	
	public List selectFinishStudentList(Map<String, Object> commandMap) throws Exception;
	
	public Map SelectSubjseqInfoDbox(Map<String, Object> commandMap) throws Exception;
	
	public List ScoreCntList(Map<String, Object> commandMap) throws Exception;
	
	public int getCntBookMonth(Map<String, Object> commandMap) throws Exception;
	
	public int graduatedUpdate(Map<String, Object> commandMap) throws Exception;
	
	public String completeUpdate(Map<String, Object> commandMap) throws Exception;
	
	public int subjectCompleteCancel(Map<String, Object> commandMap) throws Exception;
	
	public int updateOutSubjReject(Map<String, Object> commandMap) throws Exception;
	
	public int subjectCompleteRerating(Map<String, Object> commandMap) throws Exception;
	
	public List suRoyJeungPrintList(Map<String, Object> commandMap) throws Exception;
	
	public int subjectComplete3(Map<String, Object> commandMap) throws Exception;
	
	//수료증 출력여부
	public int suroyprintYnUpdate(Map<String, Object> commandMap) throws Exception;
	
	/**
     * 개인별 수료리스트 - 모바일용
     * @return
     * @exception Exception
     */
	public List selectMblUserSuryuList(Map<String, Object> commandMap) throws Exception;
	
	/**
     * 이수관리 > 과거이수내역리스트
     * @return
     * @exception Exception
     */
	public List selectfinishOldList(Map<String, Object> commandMap) throws Exception;
	
	/**
     * 이수관리 > 과거이수내역리스트 전체개수
     * @return
     * @exception Exception
     */
	public int selectfinishOldListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
     * 나의학습방 > 나의 교육이력 >  과거이수내역리스트(사용자)
     * @return
     * @exception Exception
     */
	public List selectUserFinishOldList(Map<String, Object> commandMap) throws Exception;

	
	
	/**
     * 이수관리 > 과거이수내역 보기
     * @return
     * @exception Exception
     */
	public Map selectFinishOldView(Map<String, Object> commandMap) throws Exception;
	
	/**
     * 이수관리 > 과거이수내역 삭제
     * @return
     * @exception Exception
     */
	public int deleteFinishOld(Map<String, Object> commandMap) throws Exception;
	
	
	/**
     * 이수관리 > 과거이수내역 등록
     * @return
     * @exception Exception
     */
	public void insertFinishOld(Map<String, Object> commandMap) throws Exception;
	
	
	/**
     * 이수관리 > 과거이수내역 수정
     * @return
     * @exception Exception
     */
	public int updateFinishOld(Map<String, Object> commandMap) throws Exception;

	/**
	 * 이수관리 엑셀출력
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List finishStudentExcelList(Map<String, Object> commandMap) throws Exception;
	
	
}
