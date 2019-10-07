package egovframework.com.tmp.controller;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.tmp.service.TempletManageService;
import egovframework.usr.lgn.controller.LoginController;

@Controller
public class TempletManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(LoginController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "templetManageService")
	TempletManageService templetManageService;
}
