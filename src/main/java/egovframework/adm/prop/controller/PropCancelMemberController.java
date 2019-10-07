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
public class PropCancelMemberController {
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
	 * 수강생관리 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/propCancelMemberList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		List<?> list = approvalService.selectPropCancelMemberList(commandMap);
		model.addAttribute("list", list);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/prop/propCancelMemberList";
	}
	
	
	/**
	 * 수강생관리 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/propCancelMemberExcelDown.do")
	public ModelAndView excelDownload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		

		List<?> list = approvalService.selectPropCancelMemberList(commandMap);
		model.addAttribute("list", list);	
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("propCancelMemberExcelView", "propCancelMemberMap", map);
	}
	
	
	
	/**
	 * 환불일자 저장
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/prop/propCancelMemberAction.do")
	public String PropCancelMemberAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/adm/prop/propCancelMemberList.do";
		
		boolean isok = approvalService.updatePropCancelMemberRePay(commandMap);
		
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
	
}
