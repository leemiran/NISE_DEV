package egovframework.adm.prop.controller;

import java.util.ArrayList;
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

import egovframework.adm.prop.service.ApprovalService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class ApprovalController {
	/** log */
	protected Log log = LogFactory.getLog(this.getClass());

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
    /** approvalService */
	@Resource(name = "approvalService")
    private ApprovalService approvalService;
	
	

	/**
	 * 신청승인 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/approvalList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		//System.out.println(">>>>>>>>>>>commandMap : " + commandMap);
		
		commandMap.put("ccomp", ""); 
				
		List<?> list = null; 
		if(commandMap.get("ses_search_subjseq") !=null){
			list = approvalService.selectApprovalList(commandMap);
		}
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/prop/approvalList";
	}
	@RequestMapping(value="/adm/prop/approvalList1.do")
	public String pageList1(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		System.out.println(">>>>>>>>>>>commandMap : " + commandMap);
		commandMap.put("ccomp", "1001"); 

		List<?> list = approvalService.selectApprovalList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/prop/approvalList";
	}

	/**
	 * 신청승인 비고 보기 팝업 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/approvalEtcPopup.do")
	public String pageEtcView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		Map<?, ?> view = approvalService.selectApprovaEtcView(commandMap);
		
		model.addAttribute("view", view);	
		model.addAllAttributes(commandMap);
		
		return "adm/prop/approvalEtcPopup";
	}
	
	

	/**
	 * 신청승인 비고 보기 팝업 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/approvalEtcAction.do")
	public String pageEtcAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "forward:/adm/prop/approvalEtcPopup.do";
		url ="adm/prop/approvalEtcPopup";
		
		boolean isok = approvalService.updateApprovalEtc(commandMap);
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.save");
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.save");
		}
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	
	/**
	 * 수강신청 처리상태(승인, 취소, 반려, 삭제) 이하 정보 수정 및 수강생으로 등록처리 프로세스
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/approvalAction.do")
	public String pageapprovalAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/adm/prop/approvalList1.do";
		
		boolean isok = approvalService.approvalProcess(commandMap);
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.save");
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.save");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	/**
	 * 신청승인 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/approvalExcelDown.do")
	public ModelAndView excelDownload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List<?> list = new ArrayList();
		
		String p_process = (String)commandMap.get("p_process");
		
		commandMap.put("ccomp", "1001"); 
		
		if(p_process != null && p_process.equals("delete"))	//삭제자 리스트
		{
			list = approvalService.selectApprovalDeleteList(commandMap);
		}
		else 	//신청자리스트
		{
			list = approvalService.selectApprovalList(commandMap);
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("p_process", p_process);
		
		return new ModelAndView("approvalExcelView", "approvalMap", map);
	}
	

}
