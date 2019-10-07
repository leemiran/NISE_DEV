package egovframework.svt.main;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmax.tibero.jdbc.TbClob;

import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.svt.adm.hom.calendar.AdminCalendarService;
import egovframework.usr.hom.service.PortalActionMainService;
import oracle.sql.CLOB;

@Controller
public class MainController {

	protected static final Log log = LogFactory.getLog(MainController.class);
	
	@Autowired
	PortalActionMainService portalActionMainService;
	
	@Autowired
	NoticeAdminService noticeAdminService;
	
	@Autowired
	MainService mainService;
	
	@Autowired
	AdminCalendarService adminCalendarService;
	
	@RequestMapping(value="/usr/hom/portalActionMainPage.do")
	public String portalMainPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		// 호환성보기시 ie7 문서모드로 기본적용으로 인한 메인 화면깨짐,
		// html 메타태그에 설정해도 안됨,
		// 기본 문서버전을 최신으로 하기위해 헤더에 설정 (ie 브라우저 기본문서모드 우선순위 확인)
		response.setHeader("X-UA-Compatible", "IE=Edge;chrome=1");
		
		String p_d_type = commandMap.get("p_d_type") !=null ? commandMap.get("p_d_type").toString() : "T";
		
		if(commandMap.get("p_auth") != null ){
			HttpSession session = request.getSession();
			session.setAttribute("gadmin", commandMap.get("p_auth"));
		}
		
		//권한 셀렉트 박스
		List authList = portalActionMainService.selectAuthList(commandMap);
		model.addAttribute("authList", authList);
		
//		팝업리스트
		List popupList = portalActionMainService.selectNoticePopUpList(commandMap);
		//popupList.addAll(portalActionMainService.selectNoticePopUpList(commandMap));
		model.addAttribute("popupList", popupList);

//		최근공지리스트
		List topNoticeList = portalActionMainService.selectNoticeTopList(commandMap);
		model.addAttribute("topNoticeList", topNoticeList);
		
		

//		과정전체 리스트
		List trainingList = portalActionMainService.selectTrainingList(commandMap);
		model.addAttribute("trainingList", trainingList);
		
//      레이어팝업 리스트		
		commandMap.put("p_tabseq", "12");
		commandMap.put("popupListAttr", popupList);
		List view	=	noticeAdminService.selectNoticeViewAll(commandMap);
		//view.addAll(noticeAdminService.selectNoticeViewAll(commandMap));
		
		/*
		Iterator it = view.iterator();
		while(it.hasNext()){
			Map<String, Object> ListDataMap = (Map<String, Object>) it.next(); 
			//System.out.println(ListDataMap.get("adcontent"));
			Reader reader = null;
			if("O".equals(p_d_type)){
				CLOB clob = (CLOB)ListDataMap.get("adcontent");
				reader = clob.getCharacterStream();
			}else{
				TbClob clob = (TbClob)ListDataMap.get("adcontent");
				reader = clob.getCharacterStream();
			}
			
			StringBuffer out = new StringBuffer();
			
			char[] buff = new char[1024];
			
			int nchars=0;
			
			while((nchars=reader.read(buff)) > 0){
				out.append(buff,0,nchars);
			}
			ListDataMap.put("adcontent", out.toString());
			
		}
		*/
		model.addAttribute("view",view);
		
		model.addAllAttributes(commandMap);
		
		// 연수안내
		List<?> trainSubjList = mainService.getTrainSubjList();
		model.addAttribute("trainSubjList",trainSubjList);
		// 일정안내
		List<?> calendarPeriodList = mainService.getCalendarPeriodList();
		model.addAttribute("calendarPeriodList",calendarPeriodList);
		// 일정안내 타이틀
		Map<String, String> calendarTitle = adminCalendarService.calendarTitle();
		model.addAttribute("calendarTitle", calendarTitle);
		return "svt/main/main";
	}
	
}
