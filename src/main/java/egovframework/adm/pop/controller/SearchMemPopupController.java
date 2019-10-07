package egovframework.adm.pop.controller;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.cfg.mem.service.MemberSearchService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class SearchMemPopupController {

	/** log */
	protected static final Log log = LogFactory.getLog(SearchMemPopupController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
    @Resource(name = "pagingManageController")
    private PagingManageController pagingManageController;
	
	/** MemberSearchService */
	@Resource(name = "memberSearchService")
	MemberSearchService memberSearchService;
	
	/**
	 * 개별회원정보/수강정보 >> 회원의 대한 모든 정보 팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/pop/searchPersonGeneralPopup.do")
	public String searchPersonGeneralPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		List subjectList = memberSearchService.selectSubjectList(commandMap);
		model.addAttribute("list", subjectList);
		
//		회원정보
		Map view = memberSearchService.selectMemberView(commandMap);
		model.addAttribute("view", view);
		
		
//		수강신청내역
		List propList = memberSearchService.selectProposeList(commandMap);
		model.addAttribute("propList", propList);
		
//		수강과목내역
		List eduList = memberSearchService.selectEducationList(commandMap);
		model.addAttribute("eduList", eduList);
		
//		수료과목내역
		List graduList = memberSearchService.selectGraduationList(commandMap);
		model.addAttribute("graduList", graduList);
		
//		상담신청내역
		List counselList = memberSearchService.selectCounselList(commandMap);
		model.addAttribute("counselList", counselList);
		
		commandMap.put("targetUrl", "/adm/pop/searchPersonGeneralPopup.do");
		commandMap.put("logAction", "조회");
//		정보조회 로그 남기기
		Object obj = memberSearchService.insertMemberSearchLog(commandMap);
		
		log.info(this.getClass().getName() + " 학습자 개인정보조회 : log seq : " +  obj);
		
		model.addAllAttributes(commandMap);
		return "adm/pop/searchPersonGeneralPopup";
	}	
	
	/**
	 * 1. 개별회원정보/수강정보 >> 회원의 대한 정보를 저장한다.
	 * 2. 비밀번호를 초기화한다.
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/pop/searchPersonGeneralAction.do")
	public String searchPersonGeneralAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/pop/searchPersonGeneralPopup.do";
		String p_process = (String)commandMap.get("p_process");
		
		
		boolean isok = false;
		
		if(p_process != null)
		{
			//개인정보저장
			if(p_process.equals("update"))
			{
				isok = memberSearchService.updateMemberInfo(commandMap);
			}
			//비밀번호 초기화
			else if(p_process.equals("Lgfsetting"))
			{
				isok = memberSearchService.updateLoginFail(commandMap);
			}
		}
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.update");
			
			commandMap.put("targetUrl", "/adm/pop/searchPersonGeneralAction.do");
			commandMap.put("logAction", "수정");
//			정보조회 로그 남기기
			Object obj = memberSearchService.insertMemberSearchLog(commandMap);
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}	
	
	/**
	 * 상담 등록/보기 화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/pop/searchCounselPopup.do")
	public String searchCounselPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		개인상담 보기
		model.addAttribute("view", memberSearchService.selectCounselView(commandMap));
		
		
		model.addAllAttributes(commandMap);
		return "adm/pop/searchCounselPopup";
	}	
	
	
	/**
	 * 상담 등록/수정/삭제 화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/pop/searchCounselAction.do")
	public String searchCounselAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/pop/searchCounselPopup.do";
		String p_process = (String)commandMap.get("p_process");
		
		
		boolean isok = false;
		
		
		String p_subj = (String)commandMap.get("p_subj");
		
		StringTokenizer st  = new StringTokenizer(p_subj, "/");
        String v_subj              = st.nextToken();
        String v_year              = st.nextToken();
        String v_subjseq           = st.nextToken();
        
        commandMap.put("v_subj", v_subj);
        commandMap.put("v_year", v_year);
        commandMap.put("v_subjseq", v_subjseq);
        
		if(p_process.equals("insert"))
		{
			Object o = memberSearchService.insertCounsel(commandMap);
			if(o != null) isok = true;
		}
		else if(p_process.equals("update"))
		{
			int cnt = memberSearchService.updateCounsel(commandMap);
			if(cnt > 0) isok = true;
		}
		else if(p_process.equals("delete"))
		{
			int cnt = memberSearchService.deleteCounsel(commandMap);
			if(cnt > 0) isok = true;
		}
		
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
		}
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}	
	
	
	
	
	
	

}