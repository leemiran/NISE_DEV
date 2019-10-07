package egovframework.adm.book.service;

import java.util.List;
import java.util.Map;

public interface CpBookAdminService {

	/**
	 * 교재배송정보리스트
	 * @param commandMap
	 * @return
	 */
	public List selectList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 교재배송업체리스트
	 * @param commandMap
	 * @return
	 */
	public List selectCPList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 과목 select
	 * @param commandMap
	 * @return
	 */
	public List selectSubj(Map<String, Object> commandMap) throws Exception;

	/**
	 * 배송상태변경
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateCpbookStatus(Map<String, Object> commandMap) throws Exception;

	/**
	 * 과정정보
	 * @param commandMap
	 * @return
	 */
	public Map selectSubjInfo(Map<String, Object> commandMap) throws Exception;

	/**
	 * 택배사 코드 리스트
	 * @param commandMap
	 * @return
	 */
	public List selectDeliveryCompExcelList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 엑셀 업로드
	 * @param commandMap
	 * @param fileList 
	 * @return
	 */
	public String excelDownBookDelivery(Map<String, Object> commandMap, List<Object> fileList) throws Exception;

	public boolean deleteCpBook(Map<String, Object> commandMap) throws Exception;

	
}
