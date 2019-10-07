package egovframework.com.pop.controller;

import java.util.HashMap;
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

import egovframework.adm.stu.service.StuMemberService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pop.service.StudyScoreService;
import egovframework.usr.stu.std.service.StudyExamService;
import egovframework.usr.stu.std.service.StudyManageService;

@Controller
public class StudyScoreController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudyScoreController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** studyScoreService */
	@Resource(name = "studyScoreService")
	StudyScoreService studyScoreService;
	
	/** stuMemberService */
	@Resource(name = "stuMemberService")
	StuMemberService stuMemberService;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	StudyManageService studyManageService;
	
	/** studyExamService */
	@Resource(name = "studyExamService")
    private StudyExamService studyExamService;
	
	
	@RequestMapping(value="/com/pop/courseScoreListPopup.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map<String, Object> scoreMap = new HashMap<String, Object>();
		
		scoreMap.putAll(commandMap);
		
		if(commandMap.get("p_isAdmin") != null && commandMap.get("p_isAdmin").equals("Y"))
		{
			/**
			 * 관리자에서 넘어온것인지를 파악하기 위하여 넣어준다. 
			 */
			
			scoreMap.put("userid", commandMap.get("p_userid"));
		}
		
		
		//전체점수저장
        studyExamService.calc_score(scoreMap);
		
		setSummaryInfo(scoreMap, model);
		
		//학습점수정보
		Map scoreData = studyScoreService.selectEduScore(scoreMap);
		model.addAttribute("scoreData", scoreData);
		
		//평가정보
		List examdata = studyScoreService.selectExamData(scoreMap);
		model.addAttribute("examdata", examdata);
		
		//과제정보
		Map projdata = studyScoreService.selectProjData(scoreMap);
		model.addAttribute("projdata", projdata);
		
		//설문정보
		Map suldata = studyScoreService.selectSulData(scoreMap);
		model.addAttribute("suldata", suldata);
		
		//출석부 날짜
		int levelDay = stuMemberService.selectLevelDay(scoreMap);
		scoreMap.put("levelDay", levelDay);
		
		// 출석부 [type : List]
		List<?> checklist = stuMemberService.selectSatisCheckList(scoreMap);
		model.addAttribute("checklist", checklist);
		
		List itemList = studyManageService.selectItemList(scoreMap);
		model.addAttribute("itemList", itemList);
		
		if(commandMap.get("p_isAdmin") != null && commandMap.get("p_isAdmin").equals("Y"))
		{
			/**
			 * 관리자에서 넘어온것인지를 파악하기 위하여 넣어준다. 
			 */
			scoreMap.remove("userid");
		}
		
		model.addAllAttributes(scoreMap);
		return "com/pop/courseScoreListPopup";
	}
	
	public void setSummaryInfo(Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		// 나의 진도율, 권장 진도율
		double progress = Double.parseDouble(studyManageService.getProgress(commandMap));
		double promotion = Double.parseDouble(studyManageService.getPromotion(commandMap));
		
		model.addAttribute("progress", String.valueOf(progress));
		model.addAttribute("promotion", String.valueOf(promotion));
		
		Map data2 = studyManageService.SelectEduScore(commandMap);
		model.addAttribute("EduScore", data2);
		
		// 학습정보
		EduStartBean bean = EduStartBean.getInstance();
		List dataTime = studyManageService.SelectEduTimeCountOBC(commandMap);          // 학습시간,최근학습일,강의접근횟수
		model.addAttribute("EduTime", dataTime);
		
		
		// 총차시, 학습한 차시, 진도율, 과정구분
		Map map = studyManageService.getStudyChasi(commandMap);
		
		model.addAttribute("datecnt",    map.get("datecnt")); 		// 총차시
		model.addAttribute("edudatecnt", map.get("edudatecnt")); 	// 학습한 차시
		model.addAttribute("wstep",      map.get("wstep"));		 	// 진도율
		model.addAttribute("attendCnt",      map.get("attendCnt"));		 // 출석개수
	}
	
}
