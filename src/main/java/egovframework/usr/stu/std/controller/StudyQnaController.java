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
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.usr.stu.std.service.StudyManageService;
import egovframework.usr.stu.std.service.StudyQnaService;

@Controller
public class StudyQnaController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudyQnaController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** studyQnaService */
	@Resource(name = "studyQnaService")
	StudyQnaService studyQnaService;
	
	/** studyManageService */
	@Resource(name = "studyManageService")
	StudyManageService studyManageService;
	
	/** boardManageService */
	@Resource(name = "boardManageService")
	BoardManageService boardManageService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	/**
	 * 리스트조회
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyQnaList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);
		
		int tabseq = 0;
		if( commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").toString().equals("") ){
            /*------- 게시판 분류에 대한 부분 세팅 -----*/
			commandMap.put("p_type", "SQ");
            /*-----------------------------------*/
            tabseq = studyQnaService.selectSQTableseq(commandMap);
		}else{
			tabseq = Integer.valueOf((String)commandMap.get("p_tabseq"));
		}
		commandMap.put("p_tabseq", tabseq);
		
		int totCnt = boardManageService.selectBoardListForAdminBySubjseqTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = boardManageService.selectBoardListForAdminBySubjseq(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyQnaList";
	}
	
	/**
	 * 상세화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyQnaView.do")
	public String viewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);
		
		int tabseq = 0;
		if( commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").toString().equals("") ){
            /*------- 게시판 분류에 대한 부분 세팅 -----*/
			commandMap.put("p_type", "SQ");
            /*-----------------------------------*/
            tabseq = studyQnaService.selectSQTableseq(commandMap);
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
		return "usr/stu/std/userStudyQnaView";
	}
	
	/**
	 * 등록페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyQnaInsertPage.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);
		
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyQnaInsertPage";
	}
	
	/**
	 * 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyQnaInsert.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		FileController.uploadMultiFile(request, commandMap);
		
		int isOk = boardManageService.insertBoardData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/usr/stu/std/userStudyQnaList.do";
	}
	
	/**
	 * 답변등록페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyQnaReplyInsertPage.do")
	public String replyInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);
		
		List list = boardManageService.selectBoard(commandMap);
		Map data = new HashMap();
		ArrayList fileList = new ArrayList();
		for( int i=0; i<list.size(); i++ ){
			data = (Map)list.get(i);
		}
		model.addAttribute("data", data);
		model.addAllAttributes(commandMap);
		return "usr/stu/std/userStudyQnaReplyInsertPage";
	}
	
	/**
	 * 답변등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyQnaReplyInsert.do")
	public String replyInsertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		FileController.uploadMultiFile(request, commandMap);
		
		int isOk = boardManageService.insertBoardData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/usr/stu/std/userStudyQnaList.do";
	}
	
	/**
	 * 수정페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyQnaUpdatePage.do")
	public String updatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		setSummaryInfo(commandMap, model);
		
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
		return "usr/stu/std/userStudyQnaUpdatePage";
	}
	
	/**
	 * 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyQnaUpdate.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		FileController.uploadMultiFile(request, commandMap);
		int isOk = boardManageService.updateBoardData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/usr/stu/std/userStudyQnaList.do";
	}
	
	
	/**
	 * 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/std/userStudyQnaDelete.do")
	public String deleteData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = boardManageService.deleteBoardData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/usr/stu/std/userStudyQnaList.do";
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
