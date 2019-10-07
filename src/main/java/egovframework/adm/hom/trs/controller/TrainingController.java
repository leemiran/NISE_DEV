package egovframework.adm.hom.trs.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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



import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.adm.hom.trs.service.TrainingService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.sch.service.SearchBarsService;

@Controller
public class TrainingController {

	/** log */
	protected static final Log log = LogFactory.getLog(TrainingController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** noticeAdminService */
	@Resource(name = "trainingService")
	TrainingService trainingService;
	
	/** portalActionMainService */
	@Resource(name = "searchBarsService")
	SearchBarsService searchBarsService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
	
	
	/**
	 * 홈페이지 > 연간연수 일정 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/trs/selectTrainingList.do")
	public String selectTrainingList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		int totCnt = trainingService.selectTrainingListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = trainingService.selectTrainingList(commandMap);
		model.addAttribute("list", list);
		model.addAttribute("totCnt", totCnt);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/trs/selectTrainingList";
	}
	
	@RequestMapping(value="/adm/hom/trs/trainingInsertPage.do")
	public String trainingInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//보기 정보
		model.addAttribute("view", trainingService.selectTrainingView(commandMap));
		
		//첨부파일 리스트 정보
		List fileList = trainingService.selectTrainingFileList(commandMap);
		
		model.addAttribute("fileList", fileList);
		
		List year_list = searchBarsService.selectSearchYearList(commandMap);
    	model.addAttribute("year_list", year_list);
    	
		model.addAllAttributes(commandMap);
		return "adm/hom/trs/trainingInsertPage";
	}
	
	
	//연간연수 일정 등록
	@RequestMapping(value="/adm/hom/trs/trainingActionPage.do")
	public String trainingActionPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/trs/selectTrainingList.do";
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		
		
		//업로드 처리를 한다.
		List<Object> fileList = this.uploadFiles(request, commandMap);
		
		
		String p_use = commandMap.get("p_use")+"";
		if("Y".equals(p_use)){
			//사용여부 N
			trainingService.updateTrainingUseN(commandMap);
		}
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			isok = trainingService.insertTraining(commandMap, fileList);
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = trainingService.updateTraining(commandMap, fileList);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = trainingService.deleteTraining(commandMap);
		}
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			
			
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	/**
	 * 업로드된 파일을 등록한다.
	 * 
	 * @param	contentPath	컨텐츠 경로
	 * @param	contentCode	컨텐츠 코드
	 * @return	Directory 생성 여부
	 * @throws Exception
	 */
	public List<Object> uploadFiles(HttpServletRequest request, Map<String, Object> commandMap) throws Exception {
		
		//기본 업로드 폴더
		String defaultDP = EgovProperties.getProperty("Globals.defaultDP");
		
		log.info("- 기본 업로드 폴더 : " + defaultDP);
		
		List<Object> list = new ArrayList<Object>();
		
		//저장경로 : dp\\training
		
		//파일업로드를 실행한다.
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		
		java.util.Iterator<?> fileIter = mptRequest.getFileNames();
		 
		while (fileIter.hasNext()) {
			 MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			 
			 if (mFile.getSize() > 0) {
				 Object fileHm = new HashMap();
				 fileHm = EgovFileMngUtil.uploadContentFile(mFile, defaultDP + File.separator + "training");
				 list.add(fileHm);
			 }
		}
		
		return list;
	}
	
	/**
	 * 홈페이지 > 연간연수 일정 보기 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/trs/selectTrainingViewPage.do")
	public String selectTrainingViewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if(commandMap.get("p_seq") != null)
		{
			
			//보기 정보
			model.addAttribute("view", trainingService.selectTrainingView(commandMap));
			
			//첨부파일 리스트 정보
			List fileList = trainingService.selectTrainingFileList(commandMap);
			
			model.addAttribute("fileList", fileList);
			
			
			model.addAllAttributes(commandMap);
			return "adm/hom/trs/trainingViewPage";
		}
		else
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.select"));
			model.addAllAttributes(commandMap);
			return "forward:/adm/hom/trs/selectTrainingList.do";
		}
	}
	
	
	@RequestMapping(value="/adm/hom/trs/selectTrainingScheduleList.do")
	public String selectTrainingScheduleList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = trainingService.selectTrainingScheduleListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = trainingService.selectTrainingScheduleList(commandMap);
		model.addAttribute("list", list);
		model.addAttribute("totCnt", totCnt);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/trs/selectTrainingScheduleList";
	}
	
	
	@RequestMapping(value="/adm/hom/trs/trainingScheduleInsertPage.do")
	public String trainingScheduleInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//보기 정보
		model.addAttribute("view", trainingService.selectTrainingScheduleView(commandMap));
    	
		model.addAllAttributes(commandMap);
		return "adm/hom/trs/trainingScheduleInsertPage";
	}
	
	
	//연간연수 일정 등록
	@RequestMapping(value="/adm/hom/trs/trainingScheduleActionPage.do")
	public String trainingScheduleActionPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/trs/selectTrainingScheduleList.do";
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			isok = trainingService.insertTrainingSchedule(commandMap);
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = trainingService.updateTrainingSchedule(commandMap);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = trainingService.deleteTrainingSchedule(commandMap);
		}
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			
			
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	//연간연수 일정 등록
	@RequestMapping(value="/adm/hom/trs/trainingScheduleMove.do")
	public String trainingScheduleMove(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/trs/selectTrainingScheduleList.do";
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		
		isok = trainingService.updateTrainingScheduleOrderNum(commandMap);
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
		
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	@RequestMapping(value="/adm/hom/trs/selectTrainingCourseList.do")
	public String selectTrainingCourseList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		int totCnt = trainingService.selectTrainingCourseListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = trainingService.selectTrainingCourseList(commandMap);
		model.addAttribute("list", list);
		model.addAttribute("totCnt", totCnt);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/trs/selectTrainingCourseList";
	}	
	
	
		
	@RequestMapping(value="/adm/hom/trs/trainingCourseInsertPage.do")
	public String trainingCourseInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//보기 정보
		model.addAttribute("view", trainingService.selectTrainingCourseView(commandMap));
    	
		model.addAllAttributes(commandMap);
		return "adm/hom/trs/trainingCourseInsertPage";
	}
	
	//연간연수 일정 등록
	@RequestMapping(value="/adm/hom/trs/trainingCourseActionPage.do")
	public String trainingCourseActionPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/trs/selectTrainingCourseList.do";
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			isok = trainingService.insertTrainingCourse(commandMap);
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = trainingService.updateTrainingCourse(commandMap);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = trainingService.deleteTrainingCourse(commandMap);
		}
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			
			
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	//연간연수 일정 등록
	@RequestMapping(value="/adm/hom/trs/trainingCourseMove.do")
	public String trainingCourseMove(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/trs/selectTrainingCourseList.do";
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		
		isok = trainingService.updateTrainingCourseOrderNum(commandMap);
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
		
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
}
