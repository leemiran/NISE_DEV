package egovframework.svt.usr.subj;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class UserSubjDAO extends EgovAbstractDAO {

	public Map<String, String> getSubjStudentLimit(Map<String, Object> commandMap) {
		return (Map<String, String>) selectByPk("userSubj.getSubjStudentLimit", commandMap);
	}

}
