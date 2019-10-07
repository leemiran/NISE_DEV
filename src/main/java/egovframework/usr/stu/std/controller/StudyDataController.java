package egovframework.usr.stu.std.controller;

import java.util.ArrayList;
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

import egovframework.com.bod.service.BoardManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.usr.stu.std.service.StudyDataService;
import egovframework.usr.stu.std.service.StudyManageService;

@Controller
public class StudyDataController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudyDataController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** studyDataService */
	@Resource(name = "studyDataService")
	StudyDataService studyDataService;
	
	/** boardManageService */
	@Resource(name = "boardManageService")
	BoardManageService boardManageService;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	private StudyManageService studyManageService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	@RequestMapping(value="/usr/stu/std/userStudyDataList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);
		
		int tabseq = 0;
		if( commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").toString().equals("") ){
            /*------- 게시판 분류에 대한 부분 세팅 -----*/
			commandMap.put("p_type", "SD");
            /*-----------------------------------*/
            tabseq = studyDataService.selectSDTableseq(commandMap);
		}else{
			tabseq = Integer.valueOf((String)commandMap.get("p_tabseq"));
		}
		commandMap.put("p_tabseq", tabseq);
		
		int totCnt = boardManageService.selectBoardListForAdminBySubjseqTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = boardManageService.selectBoardListForAdminBySubjseq(commandMap);
		model.addAttribute("list", list);
				
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyDataList";
	}
	
	@RequestMapping(value="/usr/stu/std/userStudyDataView.do")
	public String viewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);
		
		int tabseq = 0;
		if( commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").toString().equals("") ){
            /*------- 게시판 분류에 대한 부분 세팅 -----*/
			commandMap.put("p_type", "SD");
            /*-----------------------------------*/
            tabseq = studyDataService.selectSDTableseq(commandMap);
		}else{
			tabseq = Integer.valueOf((String)commandMap.get("p_tabseq"));
		}
		commandMap.put("p_tabseq", tabseq);
		
		boardManageService.updateBoardViewCount(commandMap);
		
		List list = boardManageService.selectBoard(commandMap);
		
		Map data = new HashMap();
		ArrayList fileList = new ArrayList();
		for( int i=0; i<list.size(); i++ ){
			data = (Map)list.get(i);
			Map fileMap = new HashMap();
			fileMap.put("realfile", data.get("realfile"));
			fileMap.put("savefile", data.get("savefile"));
			fileMap.put("fileseq", data.get("fileseq"));
			fileList.add(fileMap);
		}
		model.addAttribute("data", data);
		model.addAttribute("fileList", fileList);
		
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyDataView";
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
