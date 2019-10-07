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

import com.ziaan.lcms.EduStartBean;
import com.ziaan.library.DataBox;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.file.controller.FileController;
import egovframework.usr.stu.std.service.StudyManageService;
import egovframework.usr.stu.std.service.StudyReportService;

@Controller
public class StudyReportController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudyReportController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** studyReportService */
	@Resource(name = "studyReportService")
	StudyReportService studyReportService;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	StudyManageService studyManageService;
	
	@RequestMapping(value="/usr/stu/std/userStudyReportInsertPage.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);
		
		Map m = studyReportService.selectViewOrderStu(commandMap);
        request.setAttribute("selectViewOrderStu", m);
        
        String text_filetype = "";
        String fileext = "";
        if( m.get("submitfiletype") != null ){
        	String[] filetype = ((String)m.get("submitfiletype")).split(",");
        	if( filetype == null || filetype.length == 0 ){
        		filetype = new String[1];
        		filetype[0] = (String)m.get("submitfiletype");
        	}
        	
        	for( int i=0; i<filetype.length; i++ ){
        		log.info(" filetype[i] :  "+filetype[i]);
        		if( filetype[i].equals("1") ){
        			text_filetype += "제한없음";
        		}else if(filetype[i].equals("2")){
        			text_filetype += ("zip"+"&nbsp;");
        			fileext += "*.zip;";
        		}else if(filetype[i].equals("3")){
        			text_filetype += ("hwp,doc"+"&nbsp;");
        			fileext += "*.hwp;*.doc;";
        		}else if(filetype[i].equals("4")){
        			text_filetype += ("txt"+"&nbsp;");
        			fileext += "*.txt;";
        		}else if(filetype[i].equals("5")){
        			text_filetype += ("gif,jpg,bmp"+"&nbsp;");
        			fileext += "*.gif;*.jpg;*.bmp;";
        		}else if(filetype[i].equals("6")){
        			text_filetype += ("html,htm"+"&nbsp;");
        			fileext += "*.html;*.htm;";
        		}else if(filetype[i].equals("7")){
        			text_filetype += ("wav,asf"+"&nbsp;");
        			fileext += "*.wav;*.asf;";
        		}else if(filetype[i].equals("8")){
        			text_filetype += ("ppt"+"&nbsp;");
        			fileext += "*.ppt;";
        		}else if(filetype[i].equals("9")){
        			text_filetype += ("pdf"+"&nbsp;");
        			fileext += "*.pdf;";
        		}else if(filetype[i].equals("10")){
        			text_filetype += ("기타"+"&nbsp;");			//기타
        		}
        	}
        }
        
        commandMap.put("text_filetype", text_filetype);
        commandMap.put("fileext", fileext);
        
        List list = studyReportService.selectProfFiles(commandMap);
        request.setAttribute("selectProfFiles", list);
        
        Map profData = studyReportService.selectProfData(commandMap);
        request.setAttribute("profData", profData);
				
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyReportInsertPage";
	}
	
	
	@RequestMapping(value="/usr/stu/std/userStudyReportInsertData.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		FileController.uploadMultiFile(request, commandMap);
		
		int isOk = studyReportService.insertProfData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/usr/stu/std/userStudyReportList.do";
	}
	
	@RequestMapping(value="/usr/stu/std/userStudyReportList.do")
	public String listPag(HttpServletRequest request, HttpServletResponse reponse, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);
		
		List report_list = studyManageService.selectReportList(commandMap);
		model.addAttribute("report_list", report_list);
		
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyReportList";
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
