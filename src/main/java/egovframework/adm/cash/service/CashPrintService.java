package egovframework.adm.cash.service;

import java.util.List;
import java.util.Map;

public interface CashPrintService {

	/**
	 * 영수증리스트
	 * @param commandMap
	 * @return
	 */
	public List selectCashPrintList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 
	 * 영수증 출력
	 * @param commandMap
	 * @return
	 */
	public Map<?, ?> selectAdminCashPrint(Map<String, Object> commandMap) throws Exception;

	public boolean insertCashPrint(Map<String, Object> commandMap) throws Exception;

	public boolean updateCashPrint(Map<String, Object> commandMap) throws Exception;

	public boolean deleteCashPrint(Map<String, Object> commandMap) throws Exception;

	public Map<?, ?> selectAdminCashPrintView(Map<String, Object> commandMap) throws Exception;

	
}
