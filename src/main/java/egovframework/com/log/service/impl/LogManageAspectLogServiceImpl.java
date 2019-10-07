package egovframework.com.log.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import egovframework.com.log.service.LogManageAspectLogService;
import egovframework.com.log.dao.LogManageAspectLogDAO;


@Service("logManageAspectLogService")
public class LogManageAspectLogServiceImpl extends EgovAbstractServiceImpl implements LogManageAspectLogService{	
	
	@Resource(name="logManageAspectLogDAO")
    private LogManageAspectLogDAO logManageAspectLogDAO;
	
	/**
     * 로그저장
     * @param 
     * @throws Exception
     */
	public String manageAspectLogActionLog(Map<String, Object> inputMap) throws Exception{
		return logManageAspectLogDAO.manageAspectLogActionLog(inputMap);
	}
}
