package egovframework.svt.adm.sat;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ziaan.scorm.DateUtil;

@Controller
public class AdminSatController {
	
	@Autowired
	AdminSatService adminSatService;
	
	@RequestMapping(value="/adm/sat/popupYearContents.do")
	public String popupYearContents(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAttribute("yearContentsList", adminSatService.popupYearContents(commandMap));
		model.addAttribute("year", commandMap.get("year"));
		return "svt/adm/sat/popupYearContents";
	}
	
	@RequestMapping(value="/adm/sat/yearMemberList.do")
	public String yearMemberList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		if(null == commandMap.get("search_att")) {
			commandMap.put("search_att", "address");
		}
		if(null == commandMap.get("ses_search_gyear")) {
			commandMap.put("ses_search_gyear", DateUtil.toString("yyyy"));
		}
		List<?> list = null;
		
		String returnPage = null;
		if("address".equals(commandMap.get("search_att"))) {
			list = adminSatService.yearMemberListByAddr(commandMap);
			returnPage = "svt/adm/sat/yearMemberListByAddr";
		} else if("upperclass".equals(commandMap.get("search_att"))) {
			list = adminSatService.yearMemberListByUpper(commandMap);
			returnPage = "svt/adm/sat/yearMemberListByUpper";
		} else if("history".equals(commandMap.get("search_att"))) {
			list = adminSatService.yearMemberListByHistory(commandMap);
			returnPage = "svt/adm/sat/yearMemberListByHistory";
		} else if("job".equals(commandMap.get("search_att"))) {
			list = adminSatService.yearMemberListByJob(commandMap);
			returnPage = "svt/adm/sat/yearMemberListByJob";
		} else if("total".equals(commandMap.get("search_att"))) {
			list = adminSatService.yearMemberList(commandMap);
			returnPage = "svt/adm/sat/yearMemberList";
		} else {
			list = adminSatService.yearMemberListByStat(commandMap);
			returnPage = "svt/adm/sat/yearMemberListByStat";
		}
		
		model.addAllAttributes(commandMap);
		model.addAttribute("resultList", list);	
		
		// 총회원수
		if(!"total".equals(commandMap.get("search_att"))) {
			String totalMemberCnt = adminSatService.getTotalMemberCnt();
			model.addAttribute("memberCnt", totalMemberCnt);
		}
		
		return returnPage;
	}
	
	@RequestMapping(value="/adm/sat/eduOffice.do")
	public String eduOffice(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		if(null == commandMap.get("ses_search_gyear")) {
			commandMap.put("ses_search_gyear", DateUtil.toString("yyyy"));
		}
		
		if(null == commandMap.get("ses_search_gmonth")) {
			commandMap.put("ses_search_gmonth", DateUtil.toString("MM"));
		}
		
		//System.out.println("eduOffice ses_search_gyear ---> "+ commandMap.get("ses_search_gyear"));
		//System.out.println("eduOffice ses_search_gmonth ---> "+ commandMap.get("ses_search_gmonth"));			
		model.addAttribute("cursBunryuJob", adminSatService.cursBunryuJob(commandMap));
		model.addAttribute("eduOffice", adminSatService.eduOffice(commandMap));
		
		model.addAllAttributes(commandMap);
		return "svt/adm/sat/eduOffice";
	}

	@RequestMapping(value="/adm/sat/eduOfficeDetail.do")
	public String eduOfficeDetail(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception {
		if(null == commandMap.get("search_area")) {
			commandMap.put("search_area", "B10");
		}
		if(null == commandMap.get("ses_search_gyear")) {
			commandMap.put("ses_search_gyear", DateUtil.toString("yyyy"));
		}
		
		model.addAttribute("eduOfficeDetail", adminSatService.eduOfficeDetail(commandMap));
		model.addAllAttributes(commandMap);
		return "svt/adm/sat/eduOfficeDetail";
	}
	
	@RequestMapping(value="/adm/sat/popupEduOfficeIsu.do")
	public String popupEduOfficeIsu(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAttribute("eduOfficeName", adminSatService.getAreaCodeName(commandMap));
		model.addAttribute("eduOfficeIsu", adminSatService.popupEduOfficeIsu(commandMap));
		model.addAttribute("year", commandMap.get("year"));
		return "svt/adm/sat/popupEduOfficeIsu";
	}
	
	@RequestMapping(value="/adm/sat/score.do")
	public String score(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception {
		if(null != commandMap.get("p_search_from") && null != commandMap.get("p_search_to")) {
			model.addAttribute("score", adminSatService.score(commandMap));
		}
		
		model.addAllAttributes(commandMap);
		return "svt/adm/sat/score";
	}
	
	@RequestMapping(value="/adm/sat/point.do")
	public String point(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception {
		if(null == commandMap.get("ses_search_gyear")) {
			commandMap.put("ses_search_gyear", DateUtil.toString("yyyy"));
		}
		
		model.addAttribute("point", adminSatService.point(commandMap));
		model.addAllAttributes(commandMap);
		return "svt/adm/sat/point";
	}
	
	/*엑셀*/
	@RequestMapping(value="/adm/sat/eduOfficeExcel.do")
	public ModelAndView eduOfficeExcel(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception {
		if(null == commandMap.get("ses_search_gyear")) {
			commandMap.put("ses_search_gyear", DateUtil.toString("yyyy"));
		}
		
		if(null == commandMap.get("ses_search_gmonth")) {
			commandMap.put("ses_search_gmonth", DateUtil.toString("MM"));
		}
		
		commandMap.put("eduOffice", adminSatService.eduOffice(commandMap));
		return new ModelAndView("EduOfficeExcelView", "map", commandMap);
	}
}
