package egovframework.adm.cfg.mng.controller;

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

import egovframework.adm.cfg.mng.service.ManagerService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;


@Controller
public class ManagerController {

	/** log */
	protected static final Log log = LogFactory.getLog(ManagerController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "managerService")
	ManagerService managerService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	/**
	 * 운영자관리 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mng/managerList.do")
	public String managerList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = managerService.selectManagerList(commandMap);
		model.addAttribute("list", list);
		
		List authList = managerService.selectGadminList(commandMap);
		model.addAttribute("authList", authList);
		
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/mng/managerList";
	}
	
	/**
	 * 운영자관리 상세화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mng/managerView.do")
	public String managerView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map view = managerService.selectManagerView(commandMap);
		model.addAttribute("view", view);
		// 교육주관
		List grcodeList = managerService.selectManagerViewGrcode(commandMap);
		model.addAttribute("grcodeList", grcodeList);
		// 과목
		List subjList = managerService.selectManagerViewSubj(commandMap);
		model.addAttribute("subjList", subjList);
		// 회사
		List compList = managerService.selectManagerViewComp(commandMap);
		model.addAttribute("compList", compList);
		
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/mng/managerView";
	}
	
	/**
	 * 운영자관리 수정화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mng/managerUpdatePage.do")
	public String managerUpdatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map view = managerService.selectManagerView(commandMap);
		model.addAttribute("view", view);
		// 교육주관
		List grcodeList = managerService.selectManagerViewGrcode(commandMap);
		model.addAttribute("grcodeList", grcodeList);
		// 과목
		List subjList = managerService.selectManagerViewSubj(commandMap);
		model.addAttribute("subjList", subjList);
		// 회사
		List compList = managerService.selectManagerViewComp(commandMap);
		model.addAttribute("compList", compList);
		
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/mng/managerUpdatePage";
	}
	
	/**
	 * 운영자관리 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mng/managerUpdate.do")
	public String managerUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = managerService.updateManager(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/mng/managerList.do";
	}
	
	/**
	 * 운영자관리 등록화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mng/managerInsertPage.do")
	public String managerInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = managerService.selectBranchList(commandMap);
		model.addAttribute("list", list);
		
		List gadmin = managerService.getGadminSelectNop(commandMap);
		model.addAttribute("gadminList", gadmin);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mng/managerInsertPage";
	}
	
	
	/**
	 * 운영자관리 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mng/managerInsert.do")
	public String managerInsert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		int isOk = managerService.managerInsert(commandMap);
		if( isOk == -99 ){
			resultMsg = "이미 등록된 관리자입니다.";
		}else if( isOk == 1 ){
				resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/mng/managerList.do";
	}
	
	/**
	 * 운영자관리 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mng/managerDelete.do")
	public String managerDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		int isOk = managerService.managerDelete(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/mng/managerList.do";
	}
	
	@RequestMapping(value="/adm/cfg/mng/managerLogList.do")
	public String managerLogList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = managerService.selectManagerLogTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = managerService.selectManagerLogList(commandMap);
		model.addAttribute("list", list);
		
		List authList = managerService.selectGadminList(commandMap);
		model.addAttribute("authList", authList);
		
		model.addAllAttributes(commandMap);
		
		return "adm/cfg/mng/managerLogList";
	}
	
	@RequestMapping(value="/adm/cfg/mng/managerExcelDown.do")
	public ModelAndView managerExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		commandMap.put("excelDown", "Y");
		
		List list = managerService.selectManagerLogList(commandMap);
		model.addAttribute("list", list);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("ManagerExcelDown", "managerLogMap", map);
	}
	
	
	
	
	
}
