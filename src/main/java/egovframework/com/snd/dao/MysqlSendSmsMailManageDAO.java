package egovframework.com.snd.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("mysqlSendSmsMailManageDAO")
public class MysqlSendSmsMailManageDAO extends MysqlMapClientDAO{
	

	public int selectSendMailListCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("mysqlSendSmsMailManageDAO.selectSendMailListCnt", commandMap);
	}
	
	public List selectSendMailList(Map<String, Object> commandMap) throws Exception{
		return list("mysqlSendSmsMailManageDAO.selectSendMailList", commandMap);
	}

	public Map<String, Object> selectSendMailContent(Map<String, Object> commandMap) {
		return (Map<String, Object>) selectByPk("mysqlSendSmsMailManageDAO.selectSendMailContent", commandMap);
	}
	
	
}
