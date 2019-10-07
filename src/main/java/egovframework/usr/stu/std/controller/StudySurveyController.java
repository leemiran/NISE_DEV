package egovframework.usr.stu.std.controller;

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

import com.ziaan.lcms.EduStartBean;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.usr.stu.std.service.StudyManageService;
import egovframework.usr.stu.std.service.StudySurveyService;

@Controller
public class StudySurveyController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudySurveyController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** studySurveyService */
	@Resource(name = "studySurveyService")
	StudySurveyService studySurveyService;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	StudyManageService studyManageService;
	
	/**
	 * 설문리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudySurveyList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String returnURL = "";
		if( commandMap.get("pageType") != null ){
			setSummaryInfo(commandMap, model);
		}
		//과목 설문 리스트 
		List EducationSubjectList = studySurveyService.selectEducationSubjectList(commandMap);
		log.info("EducationSubjectList : " + EducationSubjectList);
		
		model.addAttribute("EducationSubjectList", EducationSubjectList);
		
		//사용자 해당과목리스트
//		List commonSurverList = studySurveyService.selectUserList(commandMap);
//		model.addAttribute("commonSurverList", commonSurverList);
//		사용자페이지에서 숨김 사용안함
		if( commandMap.get("pageType") != null ){
				returnURL = "usr/stu/std/userStudySurveyList";
		}else{
				returnURL = "usr/stu/cou/userStudySurveyList";
		}
		model.addAllAttributes(commandMap);
		return returnURL;
	}
	
	/**
	 * 설문 작성페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudySurveyInsertPage.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String returnURL = "";
		if( commandMap.get("pageType") != null ){
			setSummaryInfo(commandMap, model);
		}
		
		List list = studySurveyService.selectPaperQuestionExampleList(commandMap);
		model.addAttribute("PaperQuestionExampleList", list);
		
		if( commandMap.get("pageType") != null ){
			returnURL = "usr/stu/std/userStudySurveyInsertPage";
		}else{
			returnURL = "usr/stu/cou/userStudySurveyInsertPage"; //"usr/stu/cou/userStudySurveyList";
		}
		model.addAllAttributes(commandMap);
		return returnURL;
	}
	
	@RequestMapping(value="/usr/stu/std/userStudySurveyInsertData.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String returnURL = "";
		String resultMsg = "";
		int isOk = studySurveyService.insertSulmunUserResult(commandMap);
		
		if( isOk > 0 ){
			resultMsg = "제출되었습니다.";
		}else{
			resultMsg = "제출에 실패하였습니다.";
		}
		if( commandMap.get("pageType") != null ){
			//System.out.println("pageType3" + commandMap.get("pageType"));
			
			returnURL = "forward:/usr/stu/std/userStudySurveyList.do";
		}else{
			//System.out.println("pageType4" + commandMap.get("pageType"));
			
			returnURL = "forward:/usr/stu/std/userStudySurveyList.do"; 
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return returnURL;
	}
	
	public void setSummaryInfo(Map<String, Object> commandMap, ModelMap model) throws Exception{
		if( commandMap.get("studyPopup") == null || commandMap.get("studyPopup").toString().equals("") ){
			
			// 나의 진도율, 권장 진도율
			double progress = Double.parseDouble(studyManageService.getProgress(commandMap));
			double promotion = Double.parseDouble(studyManageService.getPromotion(commandMap));
			
			model.addAttribute("progress", String.valueOf(progress));
			model.addAttribute("promotion", String.valueOf(promotion));
			
			// 학습정보
			EduStartBean bean = EduStartBean.getInstance();
			List dataTime = studyManageService.SelectEduTimeCountOBC(commandMap);          // 학습시간,최근학습일,강의접근횟수
			model.addAttribute("EduTime", dataTime);
			
			Map data2 = studyManageService.SelectEduScore(commandMap);
			model.addAttribute("EduScore", data2);
			
			// 강사정보
			Map tutorInfo = studyManageService.getTutorInfo(commandMap);
			model.addAttribute("tutorInfo", tutorInfo);
			
			commandMap.put("p_grcode","N000001");
			commandMap.put("p_class","1");
			List list = studyManageService.selectListOrderPerson(commandMap);
			model.addAttribute("ReportInfo", list);
			
			// 총차시, 학습한 차시, 진도율, 과정구분
			Map map = studyManageService.getStudyChasi(commandMap);
			
			model.addAttribute("datecnt",    map.get("datecnt")); // 총차시
			model.addAttribute("edudatecnt", map.get("edudatecnt")); // 학습한 차시
			model.addAttribute("wstep",      map.get("wstep"));		 // 진도율
			model.addAttribute("attendCnt",      map.get("attendCnt"));		 // 출석개수
		}
	}
}
