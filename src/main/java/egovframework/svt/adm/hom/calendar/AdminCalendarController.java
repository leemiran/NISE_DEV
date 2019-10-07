package egovframework.svt.adm.hom.calendar;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.svt.util.HtmlUtil;

@Controller
public class AdminCalendarController {

	/** log */
	protected static final Log log = LogFactory.getLog(AdminCalendarController.class);
	
	@Autowired
	AdminCalendarService adminCalendarService;
	
	@Autowired
	HtmlUtil htmlUtil;
	
	@RequestMapping(value="/adm/hom/calendar/calendarList.do")
	public String calendarList(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		List<?> calendarList = adminCalendarService.calendarList();
		model.addAttribute("calendarList", calendarList);

		// 일정안내 제목
		Map<String, String> calendarTitle = adminCalendarService.calendarTitle();
		model.addAttribute("calendarTitle", calendarTitle);
		
		return "svt/adm/hom/calendar/calendarList";
	}
	
	@RequestMapping(value="/adm/hom/calendar/calendarReg.do")
	public String calendarReg(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "svt/adm/hom/calendar/calendarReg";
	}

	@RequestMapping(value="/adm/hom/calendar/insertCalendar.do")
	public String insertCalendar(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "categoryNm");
		
		model.addAllAttributes(adminCalendarService.insertCalendar(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/calendar/calendarDetail.do")
	public String calendarDetail(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		Map<String, String> calendar = adminCalendarService.calendarDetail(commandMap.get("calendarSeq").toString());
		model.addAttribute("calendar", calendar);
		
		return "svt/adm/hom/calendar/calendarReg";
	}

	@RequestMapping(value="/adm/hom/calendar/updateCalendar.do")
	public String updateCalendar(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "categoryNm");
		
		model.addAllAttributes(adminCalendarService.updateCalendar(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/calendar/deleteCalendar.do")
	public String deleteCalendar(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(adminCalendarService.deleteCalendar(commandMap));
		return "jsonView";
	}
	
	// 하위
	@RequestMapping(value="/adm/hom/calendar/calendarPeriodList.do")
	public String calendarPeriodList(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		List<?> calendarPeriodList = adminCalendarService.calendarPeriodList(commandMap);
		model.addAttribute("calendarPeriodList", calendarPeriodList);
		
		// 카테고리 셀렉트 박스
		List<?> calendarList = adminCalendarService.calendarList();
		model.addAttribute("calendarList", calendarList);
		
		return "svt/adm/hom/calendar/calendarPeriodList";
	}
	
	@RequestMapping(value="/adm/hom/calendar/calendarPeriodReg.do")
	public String calendarPeriodReg(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "svt/adm/hom/calendar/calendarPeriodReg";
	}
	
	@RequestMapping(value="/adm/hom/calendar/insertCalendarPeriod.do")
	public String insertCalendarPeriod(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "requestPeriod,trainPeriod");
		
		model.addAllAttributes(adminCalendarService.insertCalendarPeriod(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/calendar/calendarPeriodDetail.do")
	public String calendarPeriodDetail(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		Map<String, String> calendarPeriod = adminCalendarService.calendarPeriodDetail(commandMap.get("calendarPeriodSeq").toString());
		model.addAttribute("calendarPeriod", calendarPeriod);
		
		return "svt/adm/hom/calendar/calendarPeriodReg";
	}

	@RequestMapping(value="/adm/hom/calendar/updateCalendarPeriod.do")
	public String updateCalendarPeriod(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "requestPeriod,trainPeriod");
		
		model.addAllAttributes(adminCalendarService.updateCalendarPeriod(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/calendar/deleteCalendarPeriod.do")
	public String deleteCalendarPeriod(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(adminCalendarService.deleteCalendarPeriod(commandMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/hom/calendar/updateCalendarTitle.do")
	public String updateCalendarTitle(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		htmlUtil.escapeMap(commandMap, "titleNm");
		
		model.addAllAttributes(adminCalendarService.updateCalendarTitle(commandMap));
		return "jsonView";
	}
}
