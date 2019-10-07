package egovframework.adm.hom.sch.controller;

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
public class SchoolRoomAdminController {

	/** log */
	protected static final Log log = LogFactory.getLog(SchoolRoomAdminController.class);
	
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
	 * 홈페이지 > 출석고사장 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/sch/selectSchoolRoomList.do")
	public String selectSchoolRoomList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		sch list
		List list = noticeAdminService.selectSchoolRoomList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/sch/selectSchoolRoomList";
	}
	
	

	/**
	 * 홈페이지 > 출석고사장 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/sch/selectSchoolRoomView.do")
	public String selectSchoolRoomView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		sch 정보
		model.addAttribute("view", noticeAdminService.selectSchoolRoomView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "adm/hom/sch/selectSchoolRoomView";
	}
	
	/**
	 * 홈페이지 > 출석고사장 등록/수정/삭제 작업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/sch/selectSchoolRoomAction.do")
	public String selectSchoolRoomAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/sch/selectSchoolRoomList.do";
		
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		

		if(p_process.equalsIgnoreCase("insert"))
		{
			Object o = noticeAdminService.insertSchoolRoom(commandMap);
			if(o != null) isok = true;
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = noticeAdminService.updateSchoolRoom(commandMap);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = noticeAdminService.deleteSchoolRoom(commandMap);
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
