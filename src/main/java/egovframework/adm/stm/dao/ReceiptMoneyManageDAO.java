package egovframework.adm.stm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

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

@Repository("receiptMoneyManageDAO")
public class ReceiptMoneyManageDAO extends EgovAbstractDAO {

	
	
	/**
	 * 과정별 입금 내역 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSubjReceiptMoneyList(Map<String, Object> map) throws Exception{
		return list("ReceiptMoneyManageDAO.selectSubjReceiptMoneyList", map);
	}
	
	
	/**
	 * 교육청 수납일자
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public int selectSubjseqDepositDateCnt( Map<String, Object> commandMap) throws Exception{;
    	return (Integer)getSqlMapClientTemplate().queryForObject("ReceiptMoneyManageDAO.selectSubjseqDepositDateCnt", commandMap);    	
	}
	
	/**
	 * 교육청 수납일자
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public Object selectSubjseqDepositDate( Map<String, Object> commandMap) throws Exception{;
    	return (Object)getSqlMapClientTemplate().queryForObject("ReceiptMoneyManageDAO.selectSubjseqDepositDate", commandMap);    	
	}
	
	
	/**
	 * 교육청 수납일자 등록
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public Object insertSubjseqDepositDate(Map<String, Object> commandMap) throws Exception{
		return insert("ReceiptMoneyManageDAO.insertSubjseqDepositDate", commandMap);
	}
	
	/**
	 * 교육청 수납일자 수정
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public Object updateSubjseqDepositDate(Map<String, Object> commandMap) throws Exception{
		return update("ReceiptMoneyManageDAO.updateSubjseqDepositDate", commandMap);
	}
	
	
	/**
	 * 과정별 입금 내역 total 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSubjReceiptMoneyTotalList(Map<String, Object> map) throws Exception{
		return list("ReceiptMoneyManageDAO.selectSubjReceiptMoneyTotalList", map);
	}
	
	/**
	 * 교육청 수납일자 삭제
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public void deleteSubjseqDepositDate(Map<String, Object> commandMap) throws Exception {
		delete("ReceiptMoneyManageDAO.deleteSubjseqDepositDate", commandMap);
	}
	
	/**
	 * 과정별 입금 내역 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectYearMoneyTotalizationList(Map<String, Object> map) throws Exception{
		return list("ReceiptMoneyManageDAO.selectYearMoneyTotalizationList", map);
	}
	
	/**
     * 연도별입금내역 데이터 생성
     * @param commandMap
     * @return
     */
	public Object insertYearReceiptMoneyStatus(Map<String, Object> commandMap) throws Exception{
		return insert("ReceiptMoneyManageDAO.insertYearReceiptMoneyStatus", commandMap);
	}
	
	/**
	 * 연도별입금내역 데이터 삭제
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public void deleteYearReceiptMoneyStatus(Map<String, Object> commandMap) throws Exception {
		delete("ReceiptMoneyManageDAO.deleteYearReceiptMoneyStatus", commandMap);
	}
	
	/**
	 * 연도별입금내역 데이터 수정
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public int updateYearReceiptMoneyStatus(Map<String, Object> commandMap) throws Exception{
		return update("ReceiptMoneyManageDAO.updateYearReceiptMoneyStatus", commandMap);
	}
	
	/**
	 * 연도별입금내역 데이터 수정2
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */	
	public int updateYearReceiptMoneyStatus_2(Map<String, Object> commandMap) throws Exception{
		return update("ReceiptMoneyManageDAO.updateYearReceiptMoneyStatus_2", commandMap);
	}
	
	/**
	 * 연도별입금내역 데이터 total 조회
	 * @param commandMap
	 * @return  List<?>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectYearMoneyTotalizationTotalList(Map<String, Object> map) throws Exception{
		return list("ReceiptMoneyManageDAO.selectYearMoneyTotalizationTotalList", map);
	}
	
}
