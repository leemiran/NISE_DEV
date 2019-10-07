package egovframework.mbl.subj.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.usr.subj.service.UserSubjectService;


@Controller
public class subjController {

	/** log */
	protected static final Log log = LogFactory.getLog(subjController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	 /** userSubjectService */
	@Resource(name = "userSubjectService")
    private UserSubjectService userSubjectService;
	
	
	
	
	/**
	 * 모바일 > 과정목록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/subj/subjList.do")
	public String subjList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String url = "mbl/subj/subjList";
		
		//request가 ajx에서 온거라면
		if( commandMap.get("pageGubun") != null && commandMap.get("pageGubun").equals("ajax") ){
			url = "mbl/subj/subjListAjax";
		}
		
		if( commandMap.get("firstIndex") == null || commandMap.get("firstIndex").toString().equals("") ){
			commandMap.put("firstIndex", 0);
		}
		
		if( commandMap.get("recordCountPerPage") == null || commandMap.get("recordCountPerPage").toString().equals("") ){
			commandMap.put("recordCountPerPage", 15);
		}
		
		int totCnt = userSubjectService.selectUserSubjectMobileListTotCnt(commandMap);
		
		List list = userSubjectService.selectUserSubjectMobileList(commandMap);
		
		model.addAttribute("list", list);
		model.addAttribute("totCnt", totCnt);
		
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	/**
	 * 모바일 > 과정정보
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/subj/subjView.do")
	public String subjView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//과정정보
		model.addAttribute("view", userSubjectService.selectUserSubjectView(commandMap));
		
		//기수리스트 
		List<?> subjSeqList = userSubjectService.selectUserSubjectSeqList(commandMap);
		model.addAttribute("subjSeqList", subjSeqList);
		
		model.addAllAttributes(commandMap);
		return "/mbl/subj/subjView";
		
	}
	


	
}
