package egovframework.com.log.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("logManageAspectLogDAO")
public class LogManageAspectLogDAO extends EgovAbstractDAO{
	
	/**
	 * 로그저장
	 * @param commandMap Map<String, Object> 
	 * @return Object 
	 * @exception Exception
	 */
    public String manageAspectLogActionLog(Map<String, Object> inputMap) {
        return (String)insert("logManageAspectLogDAO.manageAspectLogActionLog", inputMap);
    }
                 	
}