package egovframework.usr.subj.controller;

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

import egovframework.adm.prop.service.ApprovalService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class PropCancelController {

	/** log */
	protected static final Log log = LogFactory.getLog(PropCancelController.class);
	
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
	 * 연수과정/안내 및 신청 > 수강신청/취소리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/propCancelList.do")
	public String propCancelList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
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
		return "usr/subj/propCancelList";
	}
	
	
	/**
	 * 연수과정/안내 및 신청 > 취소사유입력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/propCancelPopup.do")
	public String propCancelPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		
		model.addAllAttributes(commandMap);
		return "usr/subj/propCancelPopup";
	}
	
	
	
	/**
	 * 연수과정/안내 및 신청 > 취소사유입력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/propCancelAction.do")
	public String propCancelAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		String resultMsg = "";
		String url = "usr/subj/propCancelPopup";
		
		boolean isok = approvalService.propUserCancelAction(commandMap);
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.delete");
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		log.info(resultMsg);
		
		model.addAttribute("resultMsg", resultMsg);
		commandMap.put("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "" + url;
	}
	
}
