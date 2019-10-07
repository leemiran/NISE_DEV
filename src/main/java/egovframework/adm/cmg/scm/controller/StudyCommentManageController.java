package egovframework.adm.cmg.scm.controller;

import java.util.ArrayList;
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

import egovframework.adm.cmg.scm.service.StudyCommentManageService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class StudyCommentManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudyCommentManageController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** StudyCommentManage */
	@Resource(name = "studyCommentManageService")
	StudyCommentManageService studyCommentManageService;
	
	@RequestMapping(value="/adm/cmg/scm/studyCommentList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String url = "adm/cmg/scm/studyCommentDateList";
		List list = new ArrayList();
		if( commandMap.get("ses_search_subj") != null && !commandMap.get("ses_search_subj").toString().equals("") ){
			if( commandMap.get("pageType") == null || commandMap.get("pageType").toString().equals("") ){
				list = studyCommentManageService.selectCommentDateList(commandMap);
			}else{
				url = "adm/cmg/scm/studyCommentList";
				list = studyCommentManageService.selectCommentList(commandMap);
			}
		}else{
			if( commandMap.get("pageType") == null || commandMap.get("pageType").toString().equals("") ){
				url = "adm/cmg/scm/studyCommentDateList";
			}else{
				url = "adm/cmg/scm/studyCommentList";
			}
		}
		model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
		return url;
	}
	
	@RequestMapping(value="/adm/cmg/scm/studyCommentSubList.do")
	public String subListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		List list = studyCommentManageService.selectCommentSubList(commandMap);
		model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
		return "adm/cmg/scm/studyCommentSubList";
	}
	
	@RequestMapping(value="/adm/cmg/scm/studyCommentUpdate.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		String url = "adm/cmg/scm/studyCommentDateList";
		int isOk = studyCommentManageService.updateCommentDateList(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/scm/studyCommentList.do";
	}
	
	@RequestMapping(value="/adm/cmg/scm/studyCommentSubUpdate.do")
	public String updateSubData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		String url = "adm/cmg/scm/studyCommentDateList";
		int isOk = studyCommentManageService.updateCommentDateList(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/scm/studyCommentSubList.do";
	}
	
}
