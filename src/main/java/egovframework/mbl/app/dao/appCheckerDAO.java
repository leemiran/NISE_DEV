package egovframework.mbl.app.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("appCheckerDAO")
public class appCheckerDAO extends EgovAbstractDAO {

	/**
	 * 버젼이 현재 버젼보다 높은개 있는지를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectAppCheckerCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("appCheckerDAO.selectAppCheckerCnt", commandMap);
	}



}
