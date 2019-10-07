package egovframework.adm.rep.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.rep.service.ReportResultService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.usr.stu.std.service.StudyManageService;
import egovframework.usr.stu.std.service.StudyReportService;

@Controller
public class ReportResultController {

	/** log */
	protected static final Log log = LogFactory.getLog(ReportResultController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "reportResultService")
	ReportResultService reportResultService;
	
	/** studyReportService */
	@Resource(name = "studyReportService")
	StudyReportService studyReportService;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	StudyManageService studyManageService;
	
	
	/**
	 * 과제평가관리 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportResultList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if( commandMap.get("p_action") != null && commandMap.get("p_action").toString().equals("go") ){
			List list = reportResultService.selectReportResultList(commandMap);
			model.addAttribute("list", list);
		}
				
		model.addAllAttributes(commandMap);
		return "adm/rep/reportResultList";
	}
	
	
	/**
	 * 과제평가관리 상세리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportResultDetailList.do")
	public String detailListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		Map view = reportResultService.selectViewOrder(commandMap);
		model.addAttribute("view", view);
		
		List list = reportResultService.selectReportStudentList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/rep/reportResultDetailList";
	}
	
	
	@RequestMapping(value="/adm/rep/reportResultUpdateData.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = reportResultService.updateReportResultScore(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/rep/reportResultDetailList.do";
	}
	
	@RequestMapping(value="/adm/rep/reportResultExcelList.do")
	public ModelAndView excelList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = reportResultService.selectReportStudentList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		return new ModelAndView("reportResultExcelView", "reportMap", map);
	}
	
	@RequestMapping(value="/adm/rep/reportResultExcelInsertPage.do")
	public String excelInsertPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map view = reportResultService.selectViewOrder(commandMap);
		model.addAttribute("view", view);
		
		model.addAllAttributes(commandMap);
		return "adm/rep/reportResultExcelInsertPage";
	}
	
	@RequestMapping(value="/adm/rep/reportResultExcelInsertData.do")
	public String excelInsertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		String strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + "report/";
		String resultMsg = "";
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		Map fil_info = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				fil_info = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		
		FileController file = new FileController();
		List list = file.getExcelDataList(strSavePath);
		int isOk = reportResultService.insertExcelToDBNew(strSavePath+fil_info.get("uploadFileName"), list, commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/rep/reportResultExcelInsertPage.do";
	}
	
	
	
	/**
	 * 과제평가관리 학습자 과제파일 업로드 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportViewPage.do")
	public String reportViewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map<String, Object> commandMapCopy = new HashMap<String, Object>();
		
		
//		아이디 바꿔치기- 쿼리 재사용을 위하여..
		commandMapCopy.putAll(commandMap);
		commandMapCopy.put("userid", commandMapCopy.get("p_userid"));
		
		
		Map m = studyReportService.selectViewOrderStu(commandMapCopy);
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
        
        List list = studyReportService.selectProfFiles(commandMapCopy);
        request.setAttribute("selectProfFiles", list);
        
        Map profData = studyReportService.selectProfData(commandMapCopy);
        request.setAttribute("profData", profData);
				
		model.addAllAttributes(commandMap);
		return "adm/rep/reportViewPage";
	}
	

	
	/**
	 * 과제평가관리 학습자 과제파일 업로드 및 데이터 저장/업데이트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/userStudyReportInsertData.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		Map<String, Object> commandMapCopy = new HashMap<String, Object>();
		
//		아이디 바꿔치기- 쿼리 재사용을 위하여..
		commandMapCopy.putAll(commandMap);
		commandMapCopy.put("userid", commandMapCopy.get("p_userid"));
		
		
		FileController.uploadMultiFile(request, commandMapCopy);
		
		int isOk = studyReportService.insertProfData(commandMapCopy);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		model.addAttribute("resultMsg", resultMsg);
		
		
		model.addAllAttributes(commandMap);
		return "forward:/adm/rep/reportViewPage.do";
	}
	
}
