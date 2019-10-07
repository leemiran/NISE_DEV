package egovframework.mbl.prop.controller;

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
public class propMblCancelController {

	/** log */
	protected static final Log log = LogFactory.getLog(propMblCancelController.class);
	
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
	 * 모바일 > 과정신청내역 > 수강신청/취소리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/prop/propList.do")
	public String propList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		수강신청/취소리스트 #1
		List cancPosList = approvalService.selectUserCancelPosibleList(commandMap);
		model.addAttribute("cancPosList", cancPosList);
		
//		수강신청/취소리스트 #2
		List couCancList = approvalService.selectUserCourseCancelList(commandMap);
		model.addAttribute("couCancList", couCancList);
		
//		취소/반려리스트
		List cancelList = approvalService.selectUserCancelList(commandMap);
		model.addAttribute("cancelList", cancelList);
		
		
		model.addAllAttributes(commandMap);
		return "mbl/prop/propList";
	}
	
	
	/**
	 * 모바일 > 과정신청내역 > 취소사유입력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/prop/propListAction.do")
	public ModelAndView propListAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		boolean isok = approvalService.propUserCancelAction(commandMap);
		
		Map resultMap = new HashMap();
    	resultMap.put("result", isok);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
	}
	
}
