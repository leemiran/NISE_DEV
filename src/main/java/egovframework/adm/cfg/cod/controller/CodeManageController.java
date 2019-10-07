package egovframework.adm.cfg.cod.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.cfg.cod.service.CodeManageService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class CodeManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(CodeManageController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "codeManageService")
	CodeManageService codeManageService;
	
	/**
	 * 사이트코드관리 대분류코드 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = codeManageService.selectListCode(commandMap);
		
		model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
		return "adm/cfg/cod/codeManageList";
	}
	
	/**
	 * 사이트코드관리 대분류코드 상세화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageView.do")
	public String viewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map view = codeManageService.selectViewCode(commandMap);
		
		model.addAttribute("view", view);
		model.addAllAttributes(commandMap);
		return "adm/cfg/cod/codeManageView";
	}
	
	/**
	 * 사이트코드관리 대분류코드 수정화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageUpdatePage.do")
	public String updatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map view = codeManageService.selectViewCode(commandMap);
		model.addAttribute("view", view);
		
		List list = codeManageService.selectListCode(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/cod/codeManageUpdatePage";
	}
	
	/**
	 * 사이트코드관리 대분류코드 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageUpdate.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = codeManageService.updateCode(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/cod/codeManageList.do";
	}
	
	/**
	 * 사이트코드관리 대분류코드 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageDelete.do")
	public String deleteData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = codeManageService.deleteCode(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/cod/codeManageList.do";
	}
	
	/**
	 * 사이트코드관리 대분류코드 등록화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageInsertPage.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = codeManageService.selectListCode(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/cod/codeManageInsertPage";
	}

	/**
	 * 사이트코드관리 대분류코드 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageInsert.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = codeManageService.insertCode(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/cod/codeManageList.do";
	}

	/**
	 * 사이트코드관리 소분류코드 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageSubList.do")
	public String subListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		Map view = codeManageService.selectViewCode(commandMap);
		model.addAttribute("view", view);
		
		List list = codeManageService.selectSubListCode(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/cod/codeManageSubList";
	}
	
	/**
	 * 사이트코드관리 소분류코드 상세화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageSubView.do")
	public String subViewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		Map subView = codeManageService.selectSubViewCode(commandMap);
		model.addAttribute("subView", subView);
		
		Map view = codeManageService.selectViewCode(commandMap);
		model.addAttribute("view", view);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/cod/codeManageSubView";
	}
	
	/**
	 * 사이트코드관리 소분류코드 수정화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageSubUpdatePage.do")
	public String subUpdatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		Map subView = codeManageService.selectSubViewCode(commandMap);
		model.addAttribute("subView", subView);
		
		Map view = codeManageService.selectViewCode(commandMap);
		model.addAttribute("view", view);
		
		List list = codeManageService.selectSubListCode(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/cod/codeManageSubUpdatePage";
	}
	
	/**
	 * 사이트코드관리 소분류코드 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageSubUpdate.do")
	public String subUpdateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = codeManageService.subUpdateData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/cod/codeManageSubList.do";
	}
	
	/**
	 * 사이트코드관리 소분류코드 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageSubDelete.do")
	public String subDeleteData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = codeManageService.subDeleteData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/cod/codeManageSubList.do";
	}
	
	/**
	 * 사이트코드관리 소분류코드 등록화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageSubInsertPage.do")
	public String subInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map view = codeManageService.selectViewCode(commandMap);
		model.addAttribute("view", view);
		
		List list = codeManageService.selectSubListCode(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/cod/codeManageSubInsertPage";
	}
	
	/**
	 * 사이트코드관리 소분류코드 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/cod/codeManageSubInsert.do")
	public String subInsertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = codeManageService.subInsertData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/cod/codeManageSubList.do";
	}
}
