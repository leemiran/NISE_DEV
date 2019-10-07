package egovframework.mbl.std.controller;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ziaan.lcms.EduStartBean;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.usr.stu.cou.service.CourseStudyService;
import egovframework.usr.stu.std.service.StudyManageService;
import egovframework.usr.stu.std.service.StudyNoticeService;
import egovframework.usr.subj.service.UserSubjectService;


@Controller
public class studyController {

	/** log */
	protected static final Log log = LogFactory.getLog(studyController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** courseStudyService */
	@Resource(name = "courseStudyService")
	CourseStudyService courseStudyService;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	StudyManageService studyManageService;
	
	/** userSubjectService */
	@Resource(name = "userSubjectService")
    private UserSubjectService userSubjectService;
	
	/** studyNoticeService */
	@Resource(name = "studyNoticeService")
	StudyNoticeService studyNoticeService;
	
	
	/**
	 * 모바일 > 나의학습방 > 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/std/studyList.do")
	public String noticeList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String url = "mbl/std/studyList";
		
		//모바일과정만 가져온다.
		commandMap.put("isMobile", "Y");
		
		
		//학습진행목록 리스트
		List list = courseStudyService.selectEducationSubjectList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	/**
	 * 학습목록 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/std/studyItemList.do")
	public String studyItemList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//과정정보
		Map view = userSubjectService.selectUserSubjectView(commandMap);
		model.addAttribute("view", view);
		log.info(view);
		
		
		setSummaryInfo(commandMap, model);					// 요약정보
		Map m = studyManageService.getContenttype(commandMap);
		commandMap.put("p_contenttype", m.get("contenttype"));
		commandMap.put("p_studyType", m.get("studyType"));
		
		List itemList = null;
		
		//모바일지원과정이라면
		if(view.get("mobile") != null && view.get("mobile").equals("Y"))
			itemList = studyManageService.selectOldItemMobileList(commandMap);
		else
			itemList = studyManageService.selectItemList(commandMap);
		
		
		model.addAttribute("itemList", itemList);
		
		commandMap.put("subj", commandMap.get("p_subj"));
		commandMap.put("year", commandMap.get("p_year"));
		commandMap.put("subjseq", commandMap.get("p_subjseq"));
		commandMap.put("firstIndex", 0);
		commandMap.put("recordCountPerPage", 100);
		// 공지
		model.addAttribute("noticeList", courseStudyService.selectListSubjGongPageList(commandMap));
		
		model.addAllAttributes(commandMap);
		return "mbl/std/studyItemList";
	}
	
	
	/**
	 * 모바일앱 xml파일로 정보 보내주기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/std/studyItemView.do")
	public String studyItemView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String mobileYn = studyManageService.selectMobileSubject(commandMap);
		
		if(mobileYn != null && mobileYn.equals("Y"))		//모바일지원과정일경우
		{
			
			//모바일 학습여부 정보
			Map oldItemMobileStatus = studyManageService.selectOldItemMobileStatus(commandMap);
			String mblLessonstatus = oldItemMobileStatus.get("lessonstatus").toString();
			commandMap.put("mblLessonstatus", mblLessonstatus);
			
			log.info("mblLessonstatus ----> "+mblLessonstatus);
			
			
			//과정 모듈정보
			model.addAttribute("view", studyManageService.selectMobileContentOldView(commandMap));
			//챕터리스트
			model.addAttribute("list", studyManageService.selectMobileChapterList(commandMap));
		}
		else	//모바일만 서비스하는 과정인 경우
		{
			model.addAttribute("view", studyManageService.selectMobileContentView(commandMap));
		}
		
		
		
		
		model.addAllAttributes(commandMap);
		return "mbl/std/studyResponseXml";
	}
	
	
	/**
	 * 모바일앱 진도정보 저장후 다시 목차 리스트로 보낸다.
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/std/studyItemProgress.do")
	public String studyItemProgress(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//동영상 진도 정보 저장 및 완료여부 체크
		boolean isOk = studyManageService.insertUpdateMobileProgressAction(commandMap);
		
		Map mm = studyManageService.getContenttype(commandMap);
		
		//차시목록을 보여주기 위하여 넣어준다. 없으면 에러남.
		commandMap.put("p_contenttype", mm.get("contenttype"));
		commandMap.put("p_lcmstype", mm.get("studyType"));
		
		log.info(commandMap);
		
		String resultMsg = "";
		
		if(isOk)
		{
			resultMsg = "100";		//정상메세지 - 응답결과코드
			
		}else{
			resultMsg = "200";		//에러메세지 - 응답결과코드
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		String url = "mbl/std/studyRequestXml";
		
		return url;
	}
	
	@RequestMapping(value="/mbl/std/subjNoticeListAjax.do")
	public String subjNoticeListAjax(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		// 공지
		model.addAttribute("noticeList", courseStudyService.selectListSubjGongPageList(commandMap));
		model.addAllAttributes(commandMap);
		return "mbl/std/subjNoticeListAjax";
	}
	
	@RequestMapping(value="/mbl/std/subjNoticeViewAjax.do")
	public String subjNoticeViewAjax(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		// 공지
		Map data = studyNoticeService.selectViewGong(commandMap);
		model.addAttribute("selectGong", data);
		
		model.addAllAttributes(commandMap);
		return "mbl/std/subjNoticeViewAjax";
	}
	
	
	
	
	public void setSummaryInfo(Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		// 나의 진도율, 권장 진도율
		double progress = Double.parseDouble(studyManageService.getProgress(commandMap));
		double promotion = Double.parseDouble(studyManageService.getPromotion(commandMap));
		
		model.addAttribute("progress", String.valueOf(progress));
		model.addAttribute("promotion", String.valueOf(promotion));
		
		// 학습정보
		EduStartBean bean = EduStartBean.getInstance();
		List dataTime = studyManageService.SelectEduTimeCountOBC(commandMap);          // 학습시간,최근학습일,강의접근횟수
		model.addAttribute("EduTime", dataTime);
		
		Map data2 = studyManageService.SelectEduScore(commandMap);
		model.addAttribute("EduScore", data2);
		
		// 강사정보
		Map tutorInfo = studyManageService.getTutorInfo(commandMap);
		model.addAttribute("tutorInfo", tutorInfo);
		
		commandMap.put("p_grcode","N000001");
		commandMap.put("p_class","1");
        List list = studyManageService.selectListOrderPerson(commandMap);
        model.addAttribute("ReportInfo", list);

		// 총차시, 학습한 차시, 진도율, 과정구분
		Map map = studyManageService.getStudyChasi(commandMap);
		
		model.addAttribute("datecnt",    map.get("datecnt")); // 총차시
		model.addAttribute("edudatecnt", map.get("edudatecnt")); // 학습한 차시
		model.addAttribute("wstep",      map.get("wstep"));		 // 진도율
		model.addAttribute("attendCnt",      map.get("attendCnt"));		 // 출석개수
	}
}
