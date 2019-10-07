package egovframework.adm.com.cmp.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.com.cmp.service.SelectCompanyService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class SelectCompanyController {

	/** log */
	protected static final Log log = LogFactory.getLog(SelectCompanyController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "selectCompanyService")
	SelectCompanyService selectCompanyService;
	
	
}
