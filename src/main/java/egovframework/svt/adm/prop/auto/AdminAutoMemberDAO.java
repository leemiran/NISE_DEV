package egovframework.svt.adm.prop.auto;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class AdminAutoMemberDAO extends EgovAbstractDAO {

	public void insertAutoMember(Map<String, Object> commandMap) {
		insert("adminAutoMember.insertAutoMember", commandMap);
	}

	public void deleteAutoMember(Map<String, Object> commandMap) {
		delete("adminAutoMember.deleteAutoMember", commandMap);
	}

	public List<?> getAutoMemberList(Map<String, Object> commandMap) {
		return list("adminAutoMember.getAutoMemberList", commandMap);
	}

}
