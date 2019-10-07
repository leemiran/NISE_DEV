package egovframework.mbl.app.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.mbl.app.dao.appCheckerDAO;
import egovframework.mbl.app.service.appCheckerService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("appCheckerService")
public class appCheckerServiceImpl extends EgovAbstractServiceImpl implements appCheckerService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="appCheckerDAO")
    private appCheckerDAO appCheckerDAO;
	
	/**
	 * 버젼이 현재 버젼보다 높은개 있는지를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean selectAppCheckerResult(Map<String, Object> commandMap) throws Exception {
		
		boolean isOk = false;
		
		int cnt = appCheckerDAO.selectAppCheckerCnt(commandMap);
		
		//cnt가 0보다 크면 업데이트가 있는 것이다.
		if(cnt > 0)	isOk = true;
		
		return isOk;
	}
	
}
