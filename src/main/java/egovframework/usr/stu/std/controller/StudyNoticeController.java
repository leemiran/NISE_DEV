package egovframework.usr.stu.std.controller;

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

import com.ziaan.course.SubjGongData;
import com.ziaan.lcms.EduStartBean;
import com.ziaan.system.StudyCountBean;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.lcms.len.service.LearningService;
import egovframework.usr.stu.std.service.StudyManageService;
import egovframework.usr.stu.std.service.StudyNoticeService;

@Controller
public class StudyNoticeController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudyNoticeController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** studyNoticeService */
	@Resource(name = "studyNoticeService")
	StudyNoticeService studyNoticeService;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	StudyManageService studyManageService;
	
	/**
	 * 학습창 공지사항 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyNoticeList.do")
	public String noticePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);					// 요약정보
		
		//공지
        List list = studyNoticeService.selectListGong(commandMap);       // 공지
        model.addAttribute("selectList", list);
        //전체공지
        List listall = studyNoticeService.selectListGongAll_H(commandMap); // 전체공지
        model.addAttribute("selectListAll", listall); 
		
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyNoticeList";
	}
	
	/**
	 * 학습창 공지사항 상세화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyNoticeView.do")
	public String noticeViewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);					// 요약정보
		
		Map data = studyNoticeService.selectViewGong(commandMap);
		model.addAttribute("selectGong", data);
		
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyNoticeView";
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
		}
	}
	
}
