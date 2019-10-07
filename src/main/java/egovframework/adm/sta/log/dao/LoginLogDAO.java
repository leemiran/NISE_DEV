package egovframework.adm.sta.log.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("loginLogDAO")
public class LoginLogDAO  extends EgovAbstractDAO{

	/**
	 * 접속정보 카운트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectLoginCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("loginLogDAO.selectLoginCount", commandMap);
	}
	
	/**
	 * 접속정보 등록
	 * @param commandMap
	 * @throws Exception
	 */
	public void insertLogCount(Map<String, Object> commandMap) throws Exception{
		insert("loginLogDAO.insertLogCount", commandMap);
	}
	
	/**
	 * 접속정보 카운트 업데이트
	 * @param commandMap
	 * @throws Exception
	 */
	public void updateLogCount(Map<String, Object> commandMap) throws Exception{
		update("loginLogDAO.updateLogCount", commandMap);
	}
}
