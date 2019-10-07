package egovframework.adm.cfg.ccm.controller;


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

import egovframework.adm.cfg.ccm.service.CourseClassificationService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class CourseClassificationController {

	/** log */
	protected static final Log log = LogFactory.getLog(CourseClassificationController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "courseClassificationService")
	CourseClassificationService courseClassificationService;
	
	/**
	 * 과정분류 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/ccm/courseClassificationList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = courseClassificationService.selectCourseClassificationList(commandMap);
		
		model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
		return "adm/cfg/ccm/courseClassificationList";
	}
	
	/**
	 * 과정분류 수정화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/ccm/courseClassificationUpdatePage.do")
	public String updatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/ccm/courseClassificationUpdatePage";
	}
	
	/**
	 * 과정분류 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/ccm/courseClassificationUpdate.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = courseClassificationService.updateCourseClassification(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/ccm/courseClassificationUpdatePage.do";
	}
	
	/**
	 * 과정분류 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/ccm/courseClassificationDelete.do")
	public String deleteData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = courseClassificationService.deleteCourseClassification(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/ccm/courseClassificationUpdatePage.do";
	}
	
	/**
	 * 과정분류 등록화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/ccm/courseClassificationInsertPage.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if( commandMap.get("p_classtype") == null ){
			commandMap.put("p_classtype", "upper");
			commandMap.put("gubun", "upper");
		}
		List upperList = new ArrayList();
		List middleList = new ArrayList();
		if( !((String)commandMap.get("p_classtype")).equals("upper") ){
			commandMap.put("gubun", "middle");
			upperList = courseClassificationService.selectClassList(commandMap);
			if( ((String)commandMap.get("p_classtype")).equals("lower") ){
				commandMap.put("gubun", "lower");
				middleList = courseClassificationService.selectClassList(commandMap);
			}
			if( (commandMap.get("p_upperclass") == null || ((String)commandMap.get("p_upperclass")).equals("")) && upperList.size() > 0 ){
				log.info(upperList);
				log.info((Map)upperList.get(0));
				commandMap.put("p_upperclass", ((Map)upperList.get(0)).get("code"));
			}
			if( (commandMap.get("p_middleclass") == null || ((String)commandMap.get("p_middleclass")).equals("")) && middleList.size() > 0 ){
				commandMap.put("p_middleclass", ((Map)middleList.get(0)).get("code"));
			}
		}
		model.addAttribute("upperList", upperList);
		model.addAttribute("middleList", middleList);
		
		String code = courseClassificationService.getNewClassCode(commandMap);
		commandMap.put("p_classcode", code);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/ccm/courseClassificationInsertPage";
	}
	
	/**
	 * 과정분류 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/ccm/courseClassificationInsert.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = courseClassificationService.insertCourseClassification(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/ccm/courseClassificationInsertPage.do";
	}
	
}
