package egovframework.adm.cash.controller;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.book.service.CpBookAdminService;
import egovframework.adm.cash.service.CashPrintService;
import egovframework.adm.cfg.amm.service.AdminMenuManageService;
import egovframework.adm.common.CommonXSSFilterUtil;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;

@Controller
public class CashPrintController {

	/** log */
	protected static final Log log = LogFactory.getLog(CashPrintController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "cashPrintService")
	CashPrintService cashPrintService;
	
	
	/**
	 * 연수생관리 > 영수증 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cash/selectCashPrintList.do")
	public String selectCashPrintList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = cashPrintService.selectCashPrintList(commandMap);
		
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cash/selectCashPrintList";
	}
	
	
	/**
	 * 연수생관리 영수증 출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cash/selectAdminCashPrint.do")
	public String selectAdminCashPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", cashPrintService.selectAdminCashPrint(commandMap));
		
		
		model.addAllAttributes(commandMap);
		return "adm/cash/selectAdminCashPrint";
	}
	
	/**
	 * 등록 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cash/cashPrintActionPage.do")
	public String cashPrintActionPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		model.addAllAttributes(commandMap);
		return "adm/cash/cashPrintInsertPage";
	}
	
	/**
	 * 등록 처리
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cash/cashPrintAction.do")
	public String cashPrintAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String p_process = (String)commandMap.get("p_process");
		
		String url = "/adm/cash/selectCashPrintList.do";
		
		boolean isok = false;
		
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			Object o = cashPrintService.insertCashPrint(commandMap);
			if(o != null) isok = true;
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = cashPrintService.updateCashPrint(commandMap);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = cashPrintService.deleteCashPrint(commandMap);
			
			//글의 값을 삭제하여 리스트로 보낸다.
			commandMap.remove("p_seq");
			
		}
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	/**
	 * 뷰/ 수정페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cash/cashPrintViewPage.do")
	public String cashPrintViewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", cashPrintService.selectAdminCashPrintView(commandMap));
		
		
		model.addAllAttributes(commandMap);
		return "adm/cash/cashPrintViewPage";
	}
	
}
