package egovframework.svt.find;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class IdPwdFindDAO extends EgovAbstractDAO {

	@SuppressWarnings("unchecked")
	public Map<String, String> getIdPwdFind(Map<String, String> paramMap) {
		return (Map<String, String>) selectByPk("idPwdFind.getIdPwdFind", paramMap);
	}

}
