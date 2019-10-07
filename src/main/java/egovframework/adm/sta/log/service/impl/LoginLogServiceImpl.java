package egovframework.adm.sta.log.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.sta.log.dao.LoginLogDAO;
import egovframework.adm.sta.log.service.LoginLogService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("loginLogService")
public class LoginLogServiceImpl  extends EgovAbstractServiceImpl implements LoginLogService{

	@Resource(name="loginLogDAO")
    private LoginLogDAO loginLogDAO;
}
