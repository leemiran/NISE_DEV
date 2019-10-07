package egovframework.adm.cfg.amm.controller;

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

import egovframework.adm.cfg.amm.service.AdminMenuManageService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class AdminMenuManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(AdminMenuManageController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "adminMenuManageService")
	AdminMenuManageService adminMenuManageService;
	
	/**
	 * 운영메뉴관리 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/amm/adminMenuMngList.do")
	public String adminMenuMngList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String menu = (String)commandMap.get("p_menu");
		if( commandMap.get("p_view") != null ){
			commandMap.put("p_menu", commandMap.get("p_upper"));
		}
		Map upperInfo = adminMenuManageService.getUpperInfo(commandMap); 
		model.addAttribute("upperInfo", upperInfo);
		
		commandMap.put("p_menu", menu);
		List list = adminMenuManageService.adminMenuMngList(commandMap);
		model.addAttribute("list", list);
		
		
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/amm/adminMenuMngList";
	}
	
	/**
	 * 운영메뉴관리 메뉴 상세정보
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/amm/adminMenuMngView.do")
	public String adminMenuMngView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//상세정보
		Map view = adminMenuManageService.adminMenuMngView(commandMap);
		model.addAttribute("view", view);
		//하단 리스트
		List list = adminMenuManageService.adminMenuMngList(commandMap);
		model.addAttribute("list", list);
		
		Map upperInfo = adminMenuManageService.getUpperInfo(commandMap); 
		model.addAttribute("upperInfo", upperInfo);
		
		
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/amm/adminMenuMngView";
	}
	
	/**
	 * 운영메뉴정보 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/amm/adminMenuMngUpdate.do")
	public String adminMenuMngUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		int isOk = adminMenuManageService.adminMenuMngUpdate(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/cfg/amm/adminMenuMngList.do";
	}
	
	/**
	 * 운영메뉴 등록페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/amm/adminMenuMngInsertPage.do")
	public String adminMenuMngInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//하단 리스트
		List list = adminMenuManageService.adminMenuMngList(commandMap);
		model.addAttribute("list", list);
		
		Map upperInfo = adminMenuManageService.getUpperInfo(commandMap); 
		model.addAttribute("upperInfo", upperInfo);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/amm/adminMenuMngInsert";
	}
	
	@RequestMapping(value="/adm/cfg/amm/adminMenuMngInsert.do")
	public String adminMenuMngInsert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = adminMenuManageService.adminMenuMngInsert(commandMap);
		
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/amm/adminMenuMngList.do";
	}
}
