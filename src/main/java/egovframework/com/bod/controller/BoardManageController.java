package egovframework.com.bod.controller;

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

import egovframework.com.bod.service.BoardManageService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class BoardManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(BoardManageController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "boardManageService")
	BoardManageService boardManageService;
	
	@RequestMapping(value="")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
				
		model.addAllAttributes(commandMap);
		return "";
	}
	
}
