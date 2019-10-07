package egovframework.adm.sta.log.controller;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import egovframework.adm.sta.log.service.LoginLogService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class LoginLogController {

	/** log */
	protected static final Log log = LogFactory.getLog(LoginLogController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "loginLogService")
	LoginLogService loginLogService;
}
