package egovframework.svt.usr.subj;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.svt.valid.ValidService;
import egovframework.usr.subj.service.UserSubjectService;

@Controller
public class UserSubjController {

	/** log */
	protected static final Log log = LogFactory.getLog(UserSubjController.class);
	
    /** userSubjectService */
	@Resource(name = "userSubjectService")
    private UserSubjectService userSubjectService;
	
	@Autowired
	ValidService validService;
	
	/**
	 * 연수과정/안내 및 신청 > 신청 가능한 과정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/subjInfoList.do")
	public String subjInfoList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{

		//과정의 메뉴코드를 강제로 넣어 준다.
		commandMap.put("menu_main", "2");
		commandMap.put("menu_sub", "1");
		commandMap.put("menu_tab_title", "연수신청");
		commandMap.put("menu_sub_title", "신청 가능한 과정");
		
		if(null == request.getSession().getAttribute("userid")) {
			model.addAllAttributes(commandMap);
			return "svt/usr/subj/subjInfoList";
		}
		
		String userAreaCode = validService.validUserArea(String.valueOf(request.getSession().getAttribute("userid")));
		if(3 != userAreaCode.length()) {
			model.addAttribute("resultMsg", userAreaCode);
			model.addAttribute("resultCode", "NOT_MATCH_AREA");
			model.addAllAttributes(commandMap);
			return "svt/usr/subj/subjInfoList";
		}
		
		if(null == commandMap.get("areaCode") || "".equals(commandMap.get("areaCode"))) {
			commandMap.put("areaCode", userAreaCode);
		}
		
		
		//과정리스트 
		List<?> list = userSubjectService.selectUserSubjectList(commandMap);
		model.addAttribute("list", list);

		model.addAllAttributes(commandMap);
		return "svt/usr/subj/subjInfoList";
	}
	
	@RequestMapping(value="/usr/subj/subjInfoSearchList.do")
	public String subjInfoSearchList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//과정의 메뉴코드를 강제로 넣어 준다.
		commandMap.put("menu_main", "2");
		commandMap.put("menu_sub", "1");
		commandMap.put("menu_tab_title", "연수신청");
		commandMap.put("menu_sub_title", "신청 가능한 연수");
		//전체 과정 개설 검색일 경우는 search_knise_subj_module 값을 넣어서 현재 년도의 데이터를 모두 가져온다.
		//commandMap.put("search_knise_subj_module", "allSearch");
		
		if(null == request.getSession().getAttribute("userid")) {
			model.addAllAttributes(commandMap);
			return "svt/usr/subj/subjInfoSearchList";
		}
		
		
		//commandMap.put("userid", "kbhg12");
		
		String userAreaCode = validService.validUserArea(String.valueOf(request.getSession().getAttribute("userid")));
		if(3 != userAreaCode.length()) {
			model.addAttribute("resultMsg", userAreaCode);
			model.addAttribute("resultCode", "NOT_MATCH_AREA");
			model.addAllAttributes(commandMap);
			return "svt/usr/subj/subjInfoSearchList";
		}
		
		if(null == commandMap.get("areaCode") || "".equals(commandMap.get("areaCode"))) {
			commandMap.put("areaCode", userAreaCode);
		}
		
		//과정리스트 
		List<?> list = userSubjectService.selectUserSubjectList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "svt/usr/subj/subjInfoSearchList";
	}
}
