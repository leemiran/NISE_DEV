package egovframework.adm.cash.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("cashPrintDAO")
public class CashPrintDAO extends EgovAbstractDAO{

	public List selectCashPrintList(Map<String, Object> commandMap) throws Exception {
		return list("cashPrintDAO.selectCashPrintList", commandMap);
	}

	public Map<?, ?> selectAdminCashPrint(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("cashPrintDAO.selectAdminCashPrint", commandMap);
	}

	public void deleteCashPrint(Map<String, Object> commandMap) throws Exception {
		delete("cashPrintDAO.deleteCashPrint", commandMap);
	}

	public void insertCashPrint(Map<String, Object> commandMap) throws Exception {
		insert("cashPrintDAO.insertCashPrint", commandMap);
	}

	public void updateCashPrint(Map<String, Object> commandMap) throws Exception {
		update("cashPrintDAO.updateCashPrint", commandMap);
	}

	public Map<?, ?> selectAdminCashPrintView(Map<String, Object> commandMap) {
		return (Map<?, ?>)selectByPk("cashPrintDAO.selectAdminCashPrintView", commandMap);
	}
}
