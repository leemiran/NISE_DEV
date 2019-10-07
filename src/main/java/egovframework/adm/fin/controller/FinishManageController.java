package egovframework.adm.fin.controller;

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
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.fin.service.FinishManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class FinishManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(FinishManageController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "finishManageService")
	FinishManageService finishManageService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
	@RequestMapping(value="/adm/fin/finishCourseList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if( commandMap.get("ses_search_subj") != null && !commandMap.get("ses_search_subj").toString().equals("") ){
			List list = finishManageService.selectFinishCourseList(commandMap);
			model.addAttribute("list", list);
		}
		
		model.addAllAttributes(commandMap);
		return "adm/fin/finishCourseList";
	}
	
	/**
	 * 수료처리 취소
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/subjectCompleteCancel.do")
	public String subjectCompleteCancel(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		int isOk = finishManageService.subjectCompleteCancel(commandMap);
		if( isOk > 0 ){
			resultMsg = "수료처리가 취소되었습니다.";
		}else{
			resultMsg = "수료처리취소를 실패하였습니다.";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/fin/finishCourseList.do";
	}
	
	/**
	 * 수료처리 취소
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/finishStudentRerating.do")
	public String finishStudentRerating(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		int isOk = finishManageService.subjectCompleteCancel(commandMap);
		if( isOk > 0 ){
			resultMsg = "수료처리가 취소되었습니다.";
		}else{
			resultMsg = "수료처리취소를 실패하였습니다.";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/fin/finishCourseList.do";
	}
	
	@RequestMapping(value="/adm/fin/finishStudentList.do")
	public String studentListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int cnt = finishManageService.getCntBookMonth(commandMap);
		commandMap.put("cnt", cnt);
		
		List list = finishManageService.selectFinishStudentList(commandMap);
		model.addAttribute("list", list);
	
		Map subjseqInfo = finishManageService.SelectSubjseqInfoDbox(commandMap);
		model.addAttribute("subjseqInfo", subjseqInfo);
		
		List socreList = finishManageService.ScoreCntList(commandMap);
		model.addAttribute("ScoreCntList", socreList);
		
		model.addAllAttributes(commandMap);
		return "adm/fin/finishStudentList";
	}
	
	@RequestMapping(value="/adm/fin/finishGraduatedUpdate.do")
	public String graduatedUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = finishManageService.graduatedUpdate(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/fin/finishStudentList.do";
	}
	
	/**
	 * 수료처리
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/finishCompleteUpdate.do")
	public String completeUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = finishManageService.completeUpdate(commandMap);
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/fin/finishStudentList.do";
	}
	
	/**
	 * 수료처리 취소
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/completeCancelUpdate.do")
	public String completeCancel(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		int isOk = finishManageService.subjectCompleteCancel(commandMap);
		if( isOk > 0 ){
			resultMsg = "수료처리가 취소되었습니다.";
		}else{
			resultMsg = "수료처리취소를 실패하였습니다.";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/fin/finishStudentList.do";
	}
	
	/**
	 * 수료처리 취소
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/completeOutSubjReject.do")
	public String updateOutSubjReject(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		int isOk = finishManageService.updateOutSubjReject(commandMap);
		if( isOk > 0 ){
			resultMsg = "결과입력을 재요청하였습니다.";
		}else{
			resultMsg = "결과입력 재요청을 실패하였습니다.";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/fin/finishStudentList.do";
	}
	
	/**
	 * 온라인 과목 점수재계산
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/subjectCompleteRerating.do")
	public String subjectCompleteRerating(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String resultUrl = "forward:/adm/fin/finishStudentList.do";
		
		
//		점수계산 실행이 실패하여도 가야 한다.
		if( commandMap.get("p_stgubun") != null && commandMap.get("p_stgubun").toString().equals("1") ){
//			p_stgubun == 1 일때는 종합학습현황으로 이동한다.
			resultUrl = "forward:/adm/stu/totalScoreMemberList.do";
		}
		else
		{
			resultUrl = "forward:/adm/fin/finishStudentList.do";
		}
			
		int isOk = finishManageService.subjectCompleteRerating(commandMap);
		
		if( isOk > 0 ){
			resultMsg = "점수재계산 되었습니다.";
			if( commandMap.get("p_stgubun") != null && commandMap.get("p_stgubun").toString().equals("1") ){
//				p_stgubun == 1 일때는 종합학습현황으로 이동한다.
				resultUrl = "forward:/adm/stu/totalScoreMemberList.do";
			}else{
				if( commandMap.get("p_studentlist") != null && !commandMap.get("p_studentlist").toString().equals("") ){
					resultUrl = "forward:/adm/fin/finishStudentList.do";
				}else{
					resultUrl = "forward:/adm/fin/finishCourseList.do";
				}
			}
		}else if( isOk == -1 ){
			resultMsg = "이미 수료처리 되었습니다.";
		}else if( isOk == -2 ){
			resultMsg = "학습시작후 가능합니다.";
		}else{
			resultMsg = "실패하였습니다.";
		}
		
		
		log.info(" return : " + isOk + " / msg : " +  resultMsg);
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return resultUrl;
	}
	
	
	@RequestMapping(value="/adm/fin/suRoyJeungPrint.do")
	public String suRoyJeungPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		
		String return_page = "adm/fin/suRoyJeungPrint";
		
		if( Integer.valueOf((String)commandMap.get("p_year")) > 2013 ){
			//return_page = "adm/fin/suRoyJeungPrint_New";
			return_page = "adm/fin/suRoyJeungPrintNonActive";
		}
		else
		{
			List list = finishManageService.suRoyJeungPrintList(commandMap);
			model.addAttribute("list", list);
			log.info(list);
		}
		
		
		model.addAllAttributes(commandMap);
		
		return return_page;
	}
	
	
	@RequestMapping(value="/adm/fin/subjectComplete3.do")
	public String subjectComplete3(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = finishManageService.subjectComplete3(commandMap);
		if( isOk > 0 ){
			resultMsg = "조정점수가 산정되었습니다.";
		}else{
			resultMsg = "실패하였습니다.";
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/fin/finishStudentList.do";
	}
	
	
	/**
	 * 이수관리 > 과거이수내역리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/finishOldList.do")
	public String finishOldList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = finishManageService.selectfinishOldListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = finishManageService.selectfinishOldList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/fin/finishOldList";
	}
	
	/**
	 * 이수관리 > 과거이수내역 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/finishOldView.do")
	public String finishOldView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", finishManageService.selectFinishOldView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "adm/fin/finishOldView";
	}
	
	
	/**
	 * 이수관리 > 과거이수내역 수료증출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/oldSuRoyJeungPrint.do")
	public String oldSuRoyJeungPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", finishManageService.selectFinishOldView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "adm/fin/oldSuRoyJeungPrint";
	}

	
	/**
	 * 이수관리 > 과거이수내역 등록/수정/삭제 작업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/finishOldAction.do")
	public String finishOldAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/fin/finishOldList.do";
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			finishManageService.insertFinishOld(commandMap);
			isok = true;
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			int cnt = finishManageService.updateFinishOld(commandMap);
			if(cnt > 0) isok = true;
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			int cnt = finishManageService.deleteFinishOld(commandMap);
			if(cnt > 0) isok = true;
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
	 * 이수 관리 엑셀 출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/fin/finishStudentExcelList.do")
	public ModelAndView finishStudentExcelList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = finishManageService.finishStudentExcelList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("finishStudentExcelView", "fnMap", map);
	}
	
	//수료증 출력여부
	@RequestMapping(value="/adm/fin/suroyprintYnUpdate.do")
	public String suroyprintYnUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = finishManageService.suroyprintYnUpdate(commandMap);
		if( isOk > 0 ){
			resultMsg = "이수증출력이 수정되었습니다.";
		}else{
			resultMsg = "실패하였습니다.";
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/fin/finishCourseList.do";
	}
	
}
