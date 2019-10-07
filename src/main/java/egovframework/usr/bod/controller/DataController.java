package egovframework.usr.bod.controller;

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
public class DataController {

	/** log */
	protected static final Log log = LogFactory.getLog(DataController.class);
	
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
	 * 참여마당 > DATA 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/bod/dataList.do")
	public String selectFaqList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
//		데이터 구분코드
		commandMap.put("p_faqgubun", "DTA");
		
		
//		카테고리리스트
		List categoryList = noticeAdminService.selectCategoryList(commandMap);
		model.addAttribute("categoryList", categoryList);
		
//		dataList
		List list = noticeAdminService.selectFaqList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/bod/dataList";
	}
	
	
}
