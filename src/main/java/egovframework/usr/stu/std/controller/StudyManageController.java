package egovframework.usr.stu.std.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ziaan.lcms.EduStartBean;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.svt.valid.ValidService;
import egovframework.usr.stu.std.service.StudyManageService;
import egovframework.usr.stu.std.service.StudyNoticeService;

@Controller
public class StudyManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudyManageController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	StudyManageService studyManageService;
	
	/** studyNoticeService */
	@Resource(name = "studyNoticeService")
	StudyNoticeService studyNoticeService;
	
	@Autowired
	ValidService validService;
	
	/**
	 * 학습창 홈
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyHome.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = validService.validUserArea((String.valueOf(request.getSession().getAttribute("userid"))));
		
		if(3 != resultMsg.length()) {
			commandMap.put("resultMsg", resultMsg);
			model.addAllAttributes(commandMap);
			return "usr/stu/std/userStudyHome";
		}
		
		setSummaryInfo(commandMap, model);					// 요약정보
		setBBSInfo(commandMap, model);						// 게시판정보
		setSulmunAndReportAndExamInfo(commandMap, model);	// 설문, 과제, 평가정보
				
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyHome";
	}
	
	/**
	 * 학습목록 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyItemList.do")
	public String studyListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);					// 요약정보
	//	System.out.println("============================================== 컨텐츠 타입 =======================================");
		Map m = studyManageService.getContenttype(commandMap);
	//	System.out.println("============================================== 컨텐츠 타입// =======================================");
		commandMap.put("p_contenttype", m.get("contenttype"));
		commandMap.put("p_studyType", m.get("studyType"));
		commandMap.put("p_mobileCheck", m.get("mobileCheck"));
		commandMap.put("p_contentLessonAllView", m.get("contentLessonAllView"));
		
	//	System.out.println("==============================================--------- 아이템리스트 -----------=======================================");
		List itemList = studyManageService.selectItemList(commandMap);
	//	System.out.println("==============================================---------- 아이템리스트// ----------=======================================");
		model.addAttribute("itemList", itemList);
		
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyItemList";
	}
	
	
	public void setSulmunAndReportAndExamInfo(Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//설문
		List sul_list = studyManageService.selectSulpaperList(commandMap);
		model.addAttribute("sul_list", sul_list);
		
		//과제
		List report_list = studyManageService.selectReportList(commandMap);
		model.addAttribute("report_list", report_list);
		
		//시험
		List exam_list = studyManageService.selectExamList(commandMap);
		model.addAttribute("exam_list", exam_list);
	}
	
	public void setBBSInfo(Map<String, Object> commandMap, ModelMap model) throws Exception{
		// 공지사항
		List selectGongList = studyManageService.selectGongList(commandMap);
		model.addAttribute("selectGongList", selectGongList);
		
		// 자료실
		commandMap.put("p_type", "SD");
		List sdList = studyManageService.selectBoardList(commandMap);
		model.addAttribute("sdList", sdList);
		// Q&A
		commandMap.put("p_type", "SQ");
		List sqList = studyManageService.selectQnaBoardList(commandMap);
		model.addAttribute("sqList", sqList);
	}
	
	public void setSummaryInfo(Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		// 나의 진도율, 권장 진도율
	//	System.out.println("=================================== 진도율 =======================================");
		double progress = Double.parseDouble(studyManageService.getProgress(commandMap));
	//	System.out.println("=================================== 진도율// =======================================");
	//	System.out.println("=================================== 권장 진도율 =======================================");
		double promotion = Double.parseDouble(studyManageService.getPromotion(commandMap));
	//	System.out.println("=================================== 권장 진도율// =======================================");
		
		model.addAttribute("progress", String.valueOf(progress));
		model.addAttribute("promotion", String.valueOf(promotion));
		
		// 학습정보
		EduStartBean bean = EduStartBean.getInstance();
	//	System.out.println("=================================== 학습시간, 최근학습일, 강의접근횟수 구하기 =======================================");
		List dataTime = studyManageService.SelectEduTimeCountOBC(commandMap);          // 학습시간,최근학습일,강의접근횟수
	//	System.out.println("=================================== 학습시간, 최근학습일, 강의접근횟수 구하기// =======================================");
		model.addAttribute("EduTime", dataTime);
		
	//	System.out.println("=================================== SelectEduScore =======================================");
		Map data2 = studyManageService.SelectEduScore(commandMap);
	//	System.out.println("=================================== SelectEduScore// =======================================");
		model.addAttribute("EduScore", data2);
		
		// 강사정보
	//	System.out.println("=================================== 강사정보 =======================================");
		Map tutorInfo = studyManageService.getTutorInfo(commandMap);
	//	System.out.println("=================================== 강사정보// =======================================");
		model.addAttribute("tutorInfo", tutorInfo);
		
		commandMap.put("p_grcode","N000001");
		commandMap.put("p_class","1");
	//	System.out.println("=================================== selectListOrderPerson =======================================");
        List list = studyManageService.selectListOrderPerson(commandMap);
     //   System.out.println("=================================== selectListOrderPerson// =======================================");
        model.addAttribute("ReportInfo", list);

		// 총차시, 학습한 차시, 진도율, 과정구분
     //   System.out.println("=================================== getStudyChasi(총차시, 학습한 차시, 진도율, 과정구분) =======================================");
		Map map = studyManageService.getStudyChasi(commandMap);
	//	System.out.println("=================================== getStudyChasi// =======================================");
		model.addAttribute("datecnt",    map.get("datecnt")); // 총차시
		model.addAttribute("edudatecnt", map.get("edudatecnt")); // 학습한 차시
		model.addAttribute("wstep",      map.get("wstep"));		 // 진도율
		model.addAttribute("attendCnt",      map.get("attendCnt"));		 // 출석개수
	}
	
	
}
