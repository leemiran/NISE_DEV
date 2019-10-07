package egovframework.adm.snd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.cfg.mem.service.MemberSearchService;
import egovframework.adm.sms.SMSSenderDAO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.snd.service.SendSmsMailManageService;
import egovframework.svt.adm.snd.templete.AdminSndTempleteService;
import egovframework.adm.com.cmp.service.SelectCompanyService;

@Controller
public class SendMailManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(SendMailManageController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** sendMailManageService */
	@Resource(name = "sendSmsMailManageService")
	SendSmsMailManageService sendSmsMailManageService;
	

	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	/** SMSSender */
	@Resource(name = "SMSSenderDAO")
	SMSSenderDAO SMSSenderDAO;
	
	/** MemberSearchService */
	@Resource(name = "memberSearchService")
	MemberSearchService memberSearchService;
	
	/** SelectCompanyService */
	@Resource(name = "selectCompanyService")
	SelectCompanyService selectCompanyService;
	
	@Autowired
	AdminSndTempleteService adminSndTempleteService;

	/**
	 * 공통메일발송화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/snd/memberGubunMailForm.do")
	public String memberGubunMailForm(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//회원구분별 인원수
		Map<String, Object> memberGubunCount = sendSmsMailManageService.selectMemberGubunCount(commandMap);
		
		model.addAttribute("memberGubunCount", memberGubunCount);	
		
		
		List list = new ArrayList();
		
		System.out.println("1111111111");
		
		/*if( commandMap.get("p_action") != null && ((String)commandMap.get("p_action")).equals("go") ){
			list = memberSearchService.selectMemberList(commandMap);
		}
		HttpSession session	= request.getSession();
		commandMap.put("logIp", request.getRemoteAddr());
		
		model.addAttribute("list", list);*/
		
		int totCnt = 0;
		if( commandMap.get("p_action") != null && ((String)commandMap.get("p_action")).equals("go") ){
			//20160728수정
			//list = memberSearchService.selectMemberList(commandMap);
			System.out.println("selectMemberSearchList ----> controller ");
			log.info("ses_search_grseq --->  "+commandMap.get("ses_search_grseq"));
			log.info("ses_search_gyear --->  "+commandMap.get("ses_search_gyear"));
			log.info("ses_search_att --->  "+commandMap.get("ses_search_att"));
			log.info("ses_search_subj --->  "+commandMap.get("ses_search_subj"));
			log.info("ses_search_subjseq --->  "+commandMap.get("ses_search_subjseq"));
			log.info("ses_search_year --->  "+commandMap.get("ses_search_year"));
			log.info("search_appstatus --->  "+commandMap.get("search_appstatus"));
			log.info("search_payType --->  "+commandMap.get("search_payType"));
			log.info("p_searchtext --->  "+commandMap.get("p_searchtext"));
			log.info("search_orderColumn --->  "+commandMap.get("search_orderColumn"));	
			
			
			
			
			totCnt = memberSearchService.selectMemberSearchListTotCnt(commandMap);
			pagingManageController.PagingManage(commandMap, model, totCnt);
			
			System.out.println("totCnt -----> "+ totCnt);
			
			
			list = memberSearchService.selectMemberSearchList(commandMap);
			
			
		}else{
			pagingManageController.PagingManage(commandMap, model, totCnt);
		}
		model.addAttribute("list", list);
		
		
		HttpSession session	= request.getSession();
		commandMap.put("logIp", request.getRemoteAddr());
		
		
		
		String gadmin = (String)commandMap.get("gadmin");
		commandMap.put("pp_gadmin", gadmin.substring(0, 1));
		List compList = selectCompanyService.getCompany(commandMap);
		model.addAttribute("compList", compList);
				
		model.addAllAttributes(commandMap);
		
		commandMap.put("pp_gadmin", gadmin.substring(0, 1));
		
		//List compList = selectCompanyService.getCompany(commandMap);
		//model.addAttribute("compList", compList);

		List<?> templeteList = adminSndTempleteService.templeteList();
		model.addAttribute("templeteList", templeteList);
			
		model.addAllAttributes(commandMap);
		return "adm/snd/memberGubunMailForm";
	}
	
	/**
	 * 발송 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/snd/sendMailList.do")
	public String sendMailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		int totCnt = sendSmsMailManageService.selectSendMailListCnt(commandMap);
		
		System.out.println("totCnt ----> "+totCnt);
		
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
               
        
		List<?> list = sendSmsMailManageService.selectSendMailList(commandMap);		
		model.addAttribute("list", list);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/snd/sendMailList";
	}
	
	@RequestMapping(value="/adm/snd/sendMailContent.do")
	public String sendMailContent(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		Map<String, Object> sendMailContent = sendSmsMailManageService.selectSendMailContent(commandMap);		
		model.addAttribute("sendMailContent", sendMailContent);
		
		return "adm/snd/sendMailContent";
	}
	
	
}
