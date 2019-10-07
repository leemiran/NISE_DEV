package egovframework.usr.info.controller;

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

import egovframework.com.cmm.EgovMessageSource;
import egovframework.usr.info.service.LeariningGuidService;



@Controller
public class LeariningGuidController {

	/** log */
	protected static final Log log = LogFactory.getLog(LeariningGuidController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** LeariningGuidService */
	@Resource(name = "leariningGuidService")
	LeariningGuidService leariningGuidService;
	
	
	
	
	/**
	 * 연수안내 > 모집요강
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/info/eduSubjectList.do")
	public String eduSubjectList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{		
		model.addAllAttributes(commandMap);
		return "usr/info/eduSubjectList";
	}
	
	/**
	 * 연수안내 > 등록절차
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/info/registerProcess.do")
	public String registerProcess(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{		
		model.addAllAttributes(commandMap);
		return "usr/info/registerProcess";
	}
	/**
	 * 연수안내 > 학습방법
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/info/studyMethod.do")
	public String studyMethod(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{		
		model.addAllAttributes(commandMap);
		return "usr/info/studyMethod";
	}
	/**
	 * 연수안내 > 평가방법
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/info/evaluationMethod.do")
	public String evaluationMethod(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{		
		model.addAllAttributes(commandMap);
		return "usr/info/evaluationMethod";
	}
	/**
	 * 연수안내 >  문의안내
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/info/inquiryGuide.do")
	public String inquiryGuide(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{		
		model.addAllAttributes(commandMap);
		return "usr/info/inquiryGuide";
	}
	
	/**
	 * 연수안내 > 연수일정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/info/courseSchedule.do")
	public String courseSchedule(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{		
		model.addAllAttributes(commandMap);
		return "usr/info/courseSchedule";
	}
	
	/**
	 * 연수안내 > 모바일연수안내
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/info/apguideView.do")
	public String apguideView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{		
		model.addAllAttributes(commandMap);
		return "usr/info/apguideView";
	}
	
	//연수안내 > 연수일정 
	@RequestMapping(value="/usr/info/eduTrainingSchedule.do")
	public String eduTrainingSchedule(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{		
		model.addAllAttributes(commandMap);
		
		//보기 정보
		model.addAttribute("view", leariningGuidService.selectEduTrainingView(commandMap));
		
		//연간연수 일정
		List list = leariningGuidService.selectEduTrainingScheduleList(commandMap);
		
		//연수과정
		List courseList = leariningGuidService.selectEduTrainingCourseList(commandMap);
		
		//첨부파일 리스트 정보
		List fileList = leariningGuidService.selectEduTrainingFileList(commandMap);
		
		model.addAttribute("fileList", fileList);
		
		model.addAttribute("list", list);
		model.addAttribute("courseList", courseList);	
		
		model.addAllAttributes(commandMap);	
		
		return "usr/info/eduTrainingSchedule";
	}
	
}
