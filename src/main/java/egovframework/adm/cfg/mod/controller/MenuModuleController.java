package egovframework.adm.cfg.mod.controller;

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

import egovframework.adm.cfg.aut.service.MenuAuthorityService;
import egovframework.adm.cfg.mod.service.MenuModuleService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class MenuModuleController {

	/** log */
	protected static final Log log = LogFactory.getLog(MenuModuleController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** menuModuleService */
	@Resource(name = "menuModuleService")
	MenuModuleService menuModuleService;
	
	/** menuAuthorityService */
	@Resource(name = "menuAuthorityService")
	MenuAuthorityService menuAuthorityService;
	
	/**
	 * 메뉴모듈관리 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleList.do")
	public String menuModuleList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = menuModuleService.menuModuleList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mod/menuModuleList";
	}
	
	/**
	 * 모듈 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleSubList.do")
	public String menuModuleSubList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String menuNm = menuModuleService.getMenuName(commandMap);
		commandMap.put("menuName", menuNm);
		
		List list = menuModuleService.menuModuleSubList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mod/menuModuleSubList";
	}
	
	/**
	 * 모듈 상세페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleView.do")
	public String menuModuleView(HttpServletRequest requset, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String menuNm = menuModuleService.getMenuName(commandMap);
		commandMap.put("menuName", menuNm);
		
		Map view = menuModuleService.getModuleInfo(commandMap);
		model.addAttribute("view", view);
		
		List auth = menuModuleService.selectMenuAuthList(commandMap);
		model.addAttribute("authList", auth);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mod/menuModuleView";
	}
	
	/**
	 * 모듈 수정페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleUpdatePage.do")
	public String menuModuleUpdatePage(HttpServletRequest requset, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String menuNm = menuModuleService.getMenuName(commandMap);
		commandMap.put("menuName", menuNm);
		
		Map view = menuModuleService.getModuleInfo(commandMap);
		model.addAttribute("view", view);
		
		List auth = menuModuleService.selectMenuAuthList(commandMap);
		model.addAttribute("authList", auth);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mod/menuModuleUpdatePage";
	}
	
	/**
	 * 모듈 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleUpdate.do")
	public String menuModuleUpdate(HttpServletRequest requset, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = menuModuleService.updateMenuModule(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/mod/menuModuleView.do";
	}
	
	/**
	 * 모듈정보 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleDelete.do")
	public String menuModuleDelete(HttpServletRequest requset, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = menuModuleService.deleteMenuModule(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/mod/menuModuleSubList.do";
	}
	
	/**
	 * 모듈정보 등록페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleInsertPage.do")
	public String moduleInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String menuNm = menuModuleService.getMenuName(commandMap);
		commandMap.put("menuName", menuNm);
		
		List auth = menuAuthorityService.selectListGadmin(commandMap);
		model.addAttribute("authList", auth);
		
		List list = menuModuleService.menuModuleSubList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mod/menuModuleInsertPage";
	}
	
	/**
	 * 모듈정보 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleInsert.do")
	public String moduleInsert(HttpServletRequest requet, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = menuModuleService.insertModule(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/cfg/mod/menuModuleSubList.do";
	}
	
	
	
	/**
	 * 프로세스 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleProcessList.do")
	public String menuModuleProcessList(HttpServletRequest requset, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String menuNm = menuModuleService.getMenuName(commandMap);
		commandMap.put("menuName", menuNm);
		
		List list = menuModuleService.selectProcessList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mod/menuModuleProcessList";
	}
	
	/**
	 * 프로세스 상세화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleProcessView.do")
	public String processView(HttpServletRequest requset, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String menuNm = menuModuleService.getMenuName(commandMap);
		commandMap.put("menuName", menuNm);
		
		Map view = menuModuleService.selectProcessInfo(commandMap);
		model.addAttribute("view", view);
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/mod/menuModuleProcessView";
	}
	
	/**
	 * 프로세스 수정화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleProcessUpdatePage.do")
	public String processUpdatePage(HttpServletRequest requset, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String menuNm = menuModuleService.getMenuName(commandMap);
		commandMap.put("menuName", menuNm);
		
		Map view = menuModuleService.selectProcessInfo(commandMap);
		model.addAttribute("view", view);
		
		List list = menuModuleService.selectProcessList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/mod/menuModuleProcessUpdatePage";
	}
	
	/**
	 * 프로세스 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleProcessUpdate.do")
	public String processUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = menuModuleService.updateProcess(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
	
		return "forward:/adm/cfg/mod/menuModuleProcessView.do";
	}
	
	/**
	 * 프로세스 등록화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleProcessInsertPage.do")
	public String processInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String menuNm = menuModuleService.getMenuName(commandMap);
		commandMap.put("menuName", menuNm);
		
		List list = menuModuleService.selectProcessList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mod/menuModuleProcessInsertPage";
	}
	
	/**
	 * 프로세스 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mod/menuModuleProcessInsert.do")
	public String processInsert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = menuModuleService.insertProcess(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/cfg/mod/menuModuleProcessList.do";
	}
	
	
}
