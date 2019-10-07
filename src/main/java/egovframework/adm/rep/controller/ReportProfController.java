package egovframework.adm.rep.controller;

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

import egovframework.adm.exm.service.ExamAdmService;
import egovframework.adm.rep.service.ReportProfService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.file.controller.FileController;

@Controller
public class ReportProfController {

	/** log */
	protected static final Log log = LogFactory.getLog(ReportProfController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** reportProfService */
	@Resource(name = "reportProfService")
	ReportProfService reportProfService;
	
	/** examAdmService */
	@Resource(name = "examAdmService")
    private ExamAdmService examAdmService;
	
	/**
	 * 과제출제관리 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportProfList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if( commandMap.get("p_action") != null && !commandMap.get("p_action").toString().equals("") ){
			List list = reportProfService.selectReportProfList(commandMap);
			model.addAttribute("list", list);
		}
				
		model.addAllAttributes(commandMap);
		return "adm/rep/reportProfList";
	}
	
	/**
	 * 과제출제관리 등록페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportProfInsertPage.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "adm/rep/reportProfInsertPage";
	}
	
	/**
	 * 과제출제관리 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportProfInsertData.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		FileController.uploadMultiFile(request, commandMap);
		
		int isOk = reportProfService.insertReportProfData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/rep/reportProfList.do";
	}
	
	/**
	 * 과제출제관리 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportProfUpdateData.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		FileController.uploadMultiFile(request, commandMap);
		
		int isOk = reportProfService.updateReportProfData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/rep/reportProfList.do";
	}
	
	
	/**
	 * 과제출제관리 수정페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportProfUpdatePage.do")
	public String updatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		Map view = reportProfService.selectReportProfData(commandMap);
		model.addAttribute("view", view);
		
		List list = reportProfService.selectProfFiles(commandMap);
        model.addAttribute("selectProfFiles", list);
        
        String submitfiletype = (String)view.get("submitfiletype");
        String [] arryformat = submitfiletype.split(",");
        for (int j=0; j < arryformat.length; j++)
        {
        	if(arryformat[j].equals("1"))
        	{
        		commandMap.put("format1", "checked");
        	}
        	else if(arryformat[j].equals("2"))
        	{
        		commandMap.put("format2", "checked");
        	}
        	else if(arryformat[j].equals("3"))
        	{
        		commandMap.put("format3", "checked");
        	}
        	else if(arryformat[j].equals("4"))
        	{
        		commandMap.put("format4", "checked");
        	}
        	else if(arryformat[j].equals("5"))
        	{
        		commandMap.put("format5", "checked");
        	}
        	else if(arryformat[j].equals("6"))
        	{
        		commandMap.put("format6", "checked");
        	}
        	else if(arryformat[j].equals("7"))
        	{
        		commandMap.put("format7", "checked");
        	}
        	else if(arryformat[j].equals("8"))
        	{
        		commandMap.put("format8", "checked");
        	}
        	else if(arryformat[j].equals("9"))
        	{
        		commandMap.put("format9", "checked");
        	}
        	else if(arryformat[j].equals("10"))
        	{
        		commandMap.put("format10", "checked");
        	}
        }
		
        model.addAllAttributes(commandMap);
		return "adm/rep/reportProfUpdatePage";
	}
	
	@RequestMapping(value="/adm/rep/reportProfWeightUpdateData.do")
	public String weightUpdateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		int isOk = reportProfService.reportProfWeightUpdateData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/rep/reportProfList.do";
	}
	
	/**
	 * 과제문항 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportQuestionList.do")
	public String reportQuestionList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = reportProfService.selectReportQuestionList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/rep/reportQuestionList";
	}
	
	/**
	 * 과제문항 보기(등록/수정) 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportQuestionView.do")
	public String reportQuestionView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		//과제문항 보기
		model.addAttribute("view", reportProfService.selectReportQuestionView(commandMap));
		
		model.addAllAttributes(commandMap);
		
		return "adm/rep/reportQuestionView";
	}
	
	
	/**
	 * 과제문항 액션
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportQuestionAction.do")
	public String reportQuestionAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = (String)commandMap.get("p_process");
		String resultMsg = "";
		String url = "forward:/adm/rep/reportQuestionView.do";
		
		//문항 등록
		if(p_process.equals("insert"))
		{
			boolean isok = reportProfService.insertTzReportQues(commandMap);
			
			if(isok)
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			//에러
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
			
		}		
		//문항 수정
		else if(p_process.equals("update"))
		{
			int isok = reportProfService.updateTzReportQues(commandMap);
			
			if(isok == -1)		//사용중인 문항
				resultMsg = egovMessageSource.getMessage("fail.common.status.count.save");
			else if(isok == -2)		//사용중인 문항//에러
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			else				//정상처리
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
		}		
		//문항 삭제
		else if(p_process.equals("delete"))
		{
		
			int isok = reportProfService.deleteTzReportQues(commandMap);
			
			if(isok == -1)		//사용중인 문항
				resultMsg = egovMessageSource.getMessage("fail.common.status.count.save");
			else if(isok == 0)		//사용중인 문항//에러
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			else				//정상처리
				resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			
			model.addAttribute("isOpenerReload", "OK");
			model.addAttribute("isClose", "OK");
		}
	
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	/**
	 * 과제문항 과정의 문항 조회 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportQuestionSubjList.do")
	public String examBankDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		log.error("과제문항 과정의 문항 조회 ");
		
		//과제 문항 리스트		
		List reportQuesList = reportProfService.selectReportQuesList(commandMap);
		model.addAttribute("reportQuesList", reportQuesList);
		
		model.addAllAttributes(commandMap);
		
		return "adm/rep/reportQuestionSubjList";
	}
	
		
	/**
	 * 과제출제관리 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportProf.do")
	public String reportProf(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		String p_exam_subj = commandMap.get("p_exam_subj") !=null ? commandMap.get("p_exam_subj").toString() : "";

		
		Map view = reportProfService.selectReportProfView(commandMap);
		model.addAttribute("view", view);
		
		List list = reportProfService.selectProfFiles(commandMap);
        model.addAttribute("selectProfFiles", list);
        
        //콘텐츠		
		List reportQuestionList = reportProfService.selectReportQuestionList(commandMap);		
		model.addAttribute("reportQuestionList", reportQuestionList);
		
		String isuse = "Y";
		commandMap.put("isuse", isuse);
		
	
		if("".equals(p_exam_subj) && view != null){
			p_exam_subj = (String)view.get("examsubj");
			commandMap.put("p_exam_subj", p_exam_subj);
		}
		
		if(!"".equals(p_exam_subj)){	
			//과제 문항 리스트		
			List reportQuesList = reportProfService.selectReportQuesSubjseqList(commandMap);
			model.addAttribute("reportQuesList", reportQuesList);			
		}
		
        if(view != null){
        	
        	
        	
        	
	        String submitfiletype = (String)view.get("submitfiletype");
	        String [] arryformat = submitfiletype.split(",");
	        for (int j=0; j < arryformat.length; j++)
	        {
	        	if(arryformat[j].equals("1"))
	        	{
	        		commandMap.put("format1", "checked");
	        	}
	        	else if(arryformat[j].equals("2"))
	        	{
	        		commandMap.put("format2", "checked");
	        	}
	        	else if(arryformat[j].equals("3"))
	        	{
	        		commandMap.put("format3", "checked");
	        	}
	        	else if(arryformat[j].equals("4"))
	        	{
	        		commandMap.put("format4", "checked");
	        	}
	        	else if(arryformat[j].equals("5"))
	        	{
	        		commandMap.put("format5", "checked");
	        	}
	        	else if(arryformat[j].equals("6"))
	        	{
	        		commandMap.put("format6", "checked");
	        	}
	        	else if(arryformat[j].equals("7"))
	        	{
	        		commandMap.put("format7", "checked");
	        	}
	        	else if(arryformat[j].equals("8"))
	        	{
	        		commandMap.put("format8", "checked");
	        	}
	        	else if(arryformat[j].equals("9"))
	        	{
	        		commandMap.put("format9", "checked");
	        	}
	        	else if(arryformat[j].equals("10"))
	        	{
	        		commandMap.put("format10", "checked");
	        	}
	        }
        }
		
        model.addAllAttributes(commandMap);
		return "adm/rep/reportProf";
	}
	
	
	/**
	 * 과제출제관리 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportProfInsert.do")
	public String reportProfInsert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		FileController.uploadMultiFile(request, commandMap);
		
		int isOk = reportProfService.insertReportProf(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/rep/reportProf.do";
	}
	
	/**
	 * 과제출제관리 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rep/reportProfUpdate.do")
	public String reportProfUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		FileController.uploadMultiFile(request, commandMap);
		
		int isOk = reportProfService.updateReportProf(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/rep/reportProf.do";
	}
	
	
}
