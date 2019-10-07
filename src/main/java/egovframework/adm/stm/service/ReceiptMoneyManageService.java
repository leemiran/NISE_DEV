package egovframework.adm.stm.service;

import java.util.List;
import java.util.Map;

/**
 * 결제관리
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   수정일      		수정자           수정내용
 *  -------    		--------    ---------------------------
 *   2014.12.23  	유상도          	 최초 생성
 * </pre>
 */

public interface ReceiptMoneyManageService {
	
	
	/**
	 * 과정별 입금 내역 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	List<Map<String, Object>> selectSubjReceiptMoneyList(Map<String, Object> map) throws Exception;
	
	

	/**
	 * 교육청 수납일자 
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	int selectSubjseqDepositDateCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 교육청 수납일자 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	Object selectSubjseqDepositDate(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 교육청 수납일자 등록
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	int insertSubjseqDepositDate(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 교육청 수납일자 수정
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	int updateSubjseqDepositDate(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 과정별 입금 내역 total 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	List<Map<String, Object>> selectSubjReceiptMoneyTotalList(Map<String, Object> map) throws Exception;
	
	
	/**
	 * 교육청 수납일자 삭제
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public boolean deleteSubjseqDepositDate(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 과정별 입금 내역 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	List<Map<String, Object>> selectYearMoneyTotalizationList(Map<String, Object> map) throws Exception;
	
	/**
     *연도별입금내역 데이터 생성
     * @param commandMap
     * @return
     */
	int insertYearReceiptMoneyStatus(Map<String, Object> commandMap) throws Exception;
	
	/**
     * 연도별입금내역 데이터 수정
     * @param commandMap
     * @return
     */
	public int yearReceiptMoneyStatusUpdate(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 연도별입금내역 데이터 total 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	List<Map<String, Object>> selectYearMoneyTotalizationTotalList(Map<String, Object> map) throws Exception;
	
	
}
