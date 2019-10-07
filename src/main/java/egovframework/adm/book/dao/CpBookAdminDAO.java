package egovframework.adm.book.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("cpBookAdminDAO")
public class CpBookAdminDAO extends EgovAbstractDAO{

	public List selectCPList(Map<String, Object> commandMap) {
		return list("cpBookAdminDAO.selectCPList", commandMap);
	}

	public List selectList(Map<String, Object> commandMap) {
		return list("cpBookAdminDAO.selectList", commandMap);
	}

	public List selectSubj(Map<String, Object> commandMap) {
		return list("cpBookAdminDAO.selectSubj", commandMap);
	}

	public int updateCpbookStatus(HashMap<String, Object> mm) {
		return update("cpBookAdminDAO.updateCpbookStatus", mm);
	}

	public Map selectSubjInfo(Map<String, Object> commandMap) {
		return (Map)selectByPk("cpBookAdminDAO.selectSubjInfo", commandMap);
	}

	public List selectDeliveryCompExcelList(Map<String, Object> commandMap) {
		return list("cpBookAdminDAO.selectDeliveryCompExcelList", commandMap);
	}

	public Map selectDeliveryMemberInfo(Map<String, Object> mm) {
		return (Map)selectByPk("cpBookAdminDAO.selectDeliveryMemberInfo", mm);
	}

	public void insertDeliveryMeber(HashMap<String, Object> mm) {
		insert("cpBookAdminDAO.insertDeliveryMeber", mm);
	}

	public void updateDeliveryMeber(HashMap<String, Object> mm) {
		insert("cpBookAdminDAO.updateDeliveryMeber", mm);
	}

	public int deleteCpBook(HashMap<String, Object> mm) {
		return delete("cpBookAdminDAO.deleteCpBook", mm);
	}

	
}
