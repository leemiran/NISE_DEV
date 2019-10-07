package egovframework.adm.cfg.cod.service;

import java.util.List;
import java.util.Map;

public interface CodeManageService {

	public List selectListCode(Map<String, Object> commandMap) throws Exception;
	
	public Map selectViewCode(Map<String, Object> commandMap) throws Exception;
	
	public int updateCode(Map<String, Object> commandMap) throws Exception;
	
	public int deleteCode(Map<String, Object> commandMap) throws Exception;
	
	public int insertCode(Map<String, Object> commandMap) throws Exception;
	
	public List selectSubListCode(Map<String, Object> commandMap) throws Exception;
	
	public Map selectSubViewCode(Map<String, Object> commandMap) throws Exception;
	
	public int subUpdateData(Map<String, Object> commandMap) throws Exception;
	
	public int subDeleteData(Map<String, Object> commandMap) throws Exception;
	
	public int subInsertData(Map<String, Object> commandMap) throws Exception;
	
	public List selectCursBunryuList(Map<String, Object> commandMap) throws Exception;
	
	public List selectEduListCode(Map<String, Object> commandMap) throws Exception;
	
	public List selectSulPaperList(Map<String, Object> commandMap) throws Exception;
	
	public List selectSchoolRoomList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 기관코드 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectEduOrgList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 설문척도 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSulScaleList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 검색에서 평가의 대한 종류를 가져오는 쿼리
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectExamResultPaperNum(Map<String, Object> commandMap) throws Exception;
}
