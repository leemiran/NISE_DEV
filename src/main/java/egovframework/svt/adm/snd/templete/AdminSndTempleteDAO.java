package egovframework.svt.adm.snd.templete;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class AdminSndTempleteDAO extends EgovAbstractDAO {

	public List<?> getTempleteList() {
		return list("adminTemplete.getTempleteList", null);
	}

	public void insertTemplete(Map<String, Object> commandMap) {
		insert("adminTemplete.insertTemplete", commandMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getTempleteDetail(String trainSeq) {
		return (Map<String, String>) selectByPk("adminTemplete.getTempleteDetail", trainSeq);
	}

	public int updateTemplete(Map<String, Object> commandMap) {
		return update("adminTemplete.updateTemplete", commandMap);
	}
	
	public int deleteTemplete(Map<String, Object> commandMap) {
		return delete("adminTemplete.deleteTemplete", commandMap);
	}
	
}
