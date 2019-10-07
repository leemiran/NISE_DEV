package egovframework.adm.cfg.per.controller;

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

import egovframework.adm.cfg.per.service.PermissionService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class PermissionController {

	/** log */
	protected static final Log log = LogFactory.getLog(PermissionController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "permissionService")
	PermissionService permissionService;
	
	@RequestMapping(value="/adm/cfg/per/permissionList.do")
	public String permissionList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = permissionService.selectPermissionList(commandMap);
		model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
		return "adm/cfg/per/permissionList";
	}
	
	@RequestMapping(value="/adm/cfg/per/permissionUpdatePage.do")
	public String updatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map view = permissionService.selectPermissionInfo(commandMap);
		model.addAttribute("view", view);
		
		List gadminList = permissionService.selectGadminList(commandMap);
		model.addAttribute("gadminList", gadminList);
		
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/per/permissionUpdatePage";
	}
	
	@RequestMapping(value="/adm/cfg/per/permissionUpdate.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = permissionService.updatePermission(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/cfg/per/permissionList.do";
	}
	
	@RequestMapping(value="/adm/cfg/per/permissionDelete.do")
	public String deleteData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = permissionService.deletePermission(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/cfg/per/permissionList.do";
	}
	
	@RequestMapping(value="/adm/cfg/per/permissionInsertPage.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List gadminList = permissionService.selectGadminList(commandMap);
		model.addAttribute("gadminList", gadminList);
		
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/per/permissionInsertPage";
	}
	
	@RequestMapping(value="/adm/cfg/per/permissionInsert.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = permissionService.insertPermission(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/cfg/per/permissionList.do";
	}
}
