package egovframework.adm.cmg.prt.controller;

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

import egovframework.adm.cmg.prt.service.PracticeStudyService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class PracticeStudyController {

	/** log */
	protected static final Log log = LogFactory.getLog(PracticeStudyController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** practiceStudyService */
	@Resource(name = "practiceStudyService")
	PracticeStudyService practiceStudyService;
	
	@RequestMapping(value="/adm/cmg/prt/practiceStudyList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
	
		HttpSession session = request.getSession();
		String p_gadmin = session.getAttribute("gadmin") !=null ? (String)session.getAttribute("gadmin") : "" ;		
		//System.out.println("p_gadmin -----> "+p_gadmin);
		
		List list = null;
		if(p_gadmin.equals("Q1")){
			list = practiceStudyService.subjmanPracticeStudyList(commandMap);
		}else{
			commandMap.put("p_gadmin", p_gadmin);
			commandMap.put("userid", (String)session.getAttribute("userid"));
			list = practiceStudyService.practiceStudyList(commandMap);
		}
		
		
		model.addAttribute("list", list);
				
		model.addAllAttributes(commandMap);
		return "adm/cmg/prt/practiceStudyList";
	}
	
	@RequestMapping(value="/adm/cmg/prt/practiceStudySubList.do")
	public String subListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = practiceStudyService.practiceStudySubList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/prt/practiceStudySubList";
	}
	
}
