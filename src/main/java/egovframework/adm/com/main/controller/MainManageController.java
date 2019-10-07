package egovframework.adm.com.main.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.adm.com.main.service.MainManageService;
import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class MainManageController {

	protected Log log = LogFactory.getLog(this.getClass());
	/* Validator */
	@Autowired
	private DefaultBeanValidator beanValidator;
	
	/** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** MenuManageService */
	@Resource(name = "mainManageService")
    private MainManageService mainManageService;

	/** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;	
    
    /** noticeAdminService */
	@Resource(name = "noticeAdminService")
	NoticeAdminService noticeAdminService;

   

	/**
	 * 관리자 페이지 메인 - 기본현황
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/com/main/admActionMainPage.do")
	public String adminMainPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		String p_gadmin = session.getAttribute("gadmin") !=null ? (String)session.getAttribute("gadmin") : "" ;
		
		//System.out.println("p_gadmin -----> "+p_gadmin);
		
		if(p_gadmin.equals("Q1") || p_gadmin.equals("P1")){
			return "forward:/adm/cmg/prt/practiceStudyList.do?s_menu=04000000&s_submenu=04260000";
		}
		//과정 리스트
		
		commandMap.put("p_gadmin", p_gadmin);
		commandMap.put("userid", (String)session.getAttribute("userid"));
		List list1 = mainManageService.selectTotalSubSeqList(commandMap);
		
		model.addAttribute("subjList", list1);
		
		
		//공지사항 리스트
		//List list2 = mainManageService.selectNoticeBoardList(commandMap);
		
		//model.addAttribute("noticeList", list2);
		
		//자료실 질문방리스트 
		List list3 = mainManageService.selectPdsBoardList(commandMap);
		
		model.addAttribute("pdsList", list3);
		
		//연수문의 리스트
		List list4 = mainManageService.selectQnaBoardList(commandMap);
		
		model.addAttribute("qnaList", list4);
		
		//Top 메뉴
		commandMap.put("s_menu", "31040000");
		//Left 메뉴
		commandMap.put("s_submenu", "31050000");
		
		
		model.addAllAttributes(commandMap);
		return "adm/com/main/admActionMainPage";
	}
	
	/**
	 * 메인페이지 - qna 관리 답변등록 폼
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/com/main/qnaInsertForm.do")
	public String qnaInsertForm(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
//		조회수 업데이트
		noticeAdminService.updateQnaCount(commandMap);
		
		model.addAllAttributes(commandMap);
		return "adm/com/main/admQnaInsertForm";
	}
	
	/**
	 * 답변등록처리
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/com/main/qnaInsert.do")
	public String selectQnaAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/com/main/qnaInsertForm.do";
		
		boolean isok = false;
		
		//업로드 처리를 한다.
		commandMap.putAll(this.uploadFiles(request, commandMap));
		String p_process = (String)commandMap.get("p_process");
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			Object o = noticeAdminService.insertQna(commandMap);
			if(o != null) isok = true;
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = noticeAdminService.updateQna(commandMap);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = noticeAdminService.deleteQna(commandMap);
			
			//글의 값을 삭제하여 리스트로 보낸다.
			commandMap.remove("p_seq");
			
		}
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		else
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
			
			
			
		model.addAttribute("resultMsg1", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	/**
	 * 당해연도운영현황 데이터 생성
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/com/main/admYearEduStatus.do")
	public String admYearEduStatus(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		List<Map<String, Object>> resultList = mainManageService.admYearEduStatus(commandMap);		
		
		
		model.addAttribute("resultMsg", "데이터생성이 정상적으로 처리 되었습니다.");
		model.addAllAttributes(commandMap);
		return "forward:/adm/com/main/admYearEduStatusList.do";
	}
	
	/**
	 * 당해연도운영현황 내역 조회
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/com/main/admYearEduStatusList.do")
	public String admYearEduStatusList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		List<Map<String, Object>> resultList = mainManageService.admYearEduStatusList(commandMap);		
		model.addAttribute("resultList", resultList);
		model.addAllAttributes(commandMap);
		if(resultList.size() > 0){
			model.addAttribute("totalCnt", resultList.get(0).get("allTotalCnt"));
		}else{
			model.addAttribute("totalCnt", "0");
		}
		return "adm/com/main/admYearEduStatus";
	}
	
	/**
	 * 당해연도운영현황 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/com/main/admYearEduStatusUpdate.do")
	public String admYearEduStatusUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		int result = mainManageService.admYearEduStatusUpdate(commandMap);		
		
		model.addAttribute("resultMsg", egovMessageSource.getMessage("success.common.update"));
		model.addAllAttributes(commandMap);
		return "forward:/adm/com/main/admYearEduStatusList.do";
	}
	
	/**
	 * 당해연도운영현황 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/com/main/admYearEduStatusDelete.do")
	public String admYearEduStatusDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		int result = mainManageService.admYearEduStatusDelete(commandMap);		
		
		model.addAttribute("resultMsg", egovMessageSource.getMessage("success.common.delete"));
		model.addAllAttributes(commandMap);
		return "forward:/adm/com/main/admYearEduStatusList.do";
	}
	
	/**
	 * 당해연도운영현황 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/com/main/admYearEduStatusExcelDown.do")
	public String admYearEduStatusExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		log.error("당해연도운영현황 엑셀 다운로드");
		List<Map<String, Object>> resultList = mainManageService.admYearEduStatusList(commandMap);		
		model.addAttribute("resultList", resultList);
		if(resultList.size() > 0){
			model.addAttribute("totalCnt", resultList.get(0).get("allTotalCnt"));
		}else{
			model.addAttribute("totalCnt", "0");
		}
		model.addAllAttributes(commandMap);
		return "adm/com/main/admYearEduStatusxls";
	}
	
	/**
	 * 업로드된 파일을 등록한다.
	 * 
	 * @param	contentPath	컨텐츠 경로
	 * @param	contentCode	컨텐츠 코드
	 * @return	Directory 생성 여부
	 * @throws Exception
	 */
	public Map<String, Object> uploadFiles(HttpServletRequest request, Map<String, Object> commandMap) throws Exception {
		
		//기본 업로드 폴더
		String defaultDP = EgovProperties.getProperty("Globals.defaultDP");
		
		log.info("- 기본 업로드 폴더 : " + defaultDP);
		
		
		//파일업로드를 실행한다.
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		
		java.util.Iterator<?> fileIter = mptRequest.getFileNames();
		 
		while (fileIter.hasNext()) {
			 MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			 
			 if (mFile.getSize() > 0) {
				 commandMap.putAll( EgovFileMngUtil.uploadContentFile(mFile, defaultDP + File.separator + "bulletin") );
			 }
		}
		
		return commandMap;
	}
}
