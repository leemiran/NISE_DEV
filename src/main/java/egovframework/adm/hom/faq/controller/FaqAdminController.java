package egovframework.adm.hom.faq.controller;

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

import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class FaqAdminController {

	/** log */
	protected static final Log log = LogFactory.getLog(FaqAdminController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** noticeAdminService */
	@Resource(name = "noticeAdminService")
	NoticeAdminService noticeAdminService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
	
	
	/**
	 * 홈페이지 > FAQ 카테고리 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/faq/selectCategoryList.do")
	public String selectCategoryList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = noticeAdminService.selectCategoryList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/faq/selectCategoryList";
	}
	
	
	/**
	 * 홈페이지 > FAQ 카테고리 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/faq/selectCategoryView.do")
	public String selectCategoryView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", noticeAdminService.selectCategoryView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "adm/hom/faq/selectCategoryView";
	}
	
	/**
	 * 홈페이지 > FAQ 카테고리 등록/수정/삭제 작업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/faq/selectCategoryAction.do")
	public String selectCategoryAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/faq/selectCategoryList.do";
		
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		

		if(p_process.equalsIgnoreCase("insert"))
		{
			isok = noticeAdminService.insertCategoryFaq(commandMap);
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = noticeAdminService.updateCategoryFaq(commandMap);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = noticeAdminService.deleteCategoryFaq(commandMap);
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
	 * 홈페이지 > FAQ 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/faq/selectFaqList.do")
	public String selectFaqList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		카테고리리스트
		List categoryList = noticeAdminService.selectCategoryList(commandMap);
		model.addAttribute("categoryList", categoryList);
		
//		faq list
		List list = noticeAdminService.selectFaqList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/faq/selectFaqList";
	}
	
	

	/**
	 * 홈페이지 > FAQ 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/faq/selectFaqView.do")
	public String selectFaqView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		카테고리리스트
		List categoryList = noticeAdminService.selectCategoryList(commandMap);
		model.addAttribute("categoryList", categoryList);
		
//		faq 정보
		model.addAttribute("view", noticeAdminService.selectFaqView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "adm/hom/faq/selectFaqView";
	}
	
	/**
	 * 홈페이지 > FAQ 등록/수정/삭제 작업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/faq/selectFaqAction.do")
	public String selectFaqAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/faq/selectFaqList.do";
		
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		

		if(p_process.equalsIgnoreCase("insert"))
		{
			Object o = noticeAdminService.insertFaq(commandMap);
			if(o != null) isok = true;
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = noticeAdminService.updateFaq(commandMap);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = noticeAdminService.deleteFaq(commandMap);
		}
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			
			
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	
	
}
