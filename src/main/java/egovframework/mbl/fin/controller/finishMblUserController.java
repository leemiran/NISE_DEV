package egovframework.mbl.fin.controller;

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

import egovframework.adm.fin.service.FinishManageService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class finishMblUserController {

	/** log */
	protected static final Log log = LogFactory.getLog(finishMblUserController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "finishManageService")
	FinishManageService finishManageService;
	
	@RequestMapping(value="/mbl/fin/finishList.do")
	public String finishList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = finishManageService.selectMblUserSuryuList(commandMap);
		model.addAttribute("list", list);
	
		model.addAllAttributes(commandMap);
		return "mbl/fin/finishList";
	}
	
	
}
