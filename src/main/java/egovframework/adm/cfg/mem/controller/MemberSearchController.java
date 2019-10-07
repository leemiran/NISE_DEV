package egovframework.adm.cfg.mem.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ziaan.library.RequestBox;

import egovframework.adm.cfg.mem.service.MemberSearchService;
import egovframework.adm.com.cmp.service.SelectCompanyService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.utl.fcc.service.EgovStringUtil;

@Controller
public class MemberSearchController {

	/** log */
	protected static final Log log = LogFactory.getLog(MemberSearchController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
    @Resource(name = "pagingManageController")
    private PagingManageController pagingManageController;
	
	/** MemberSearchService */
	@Resource(name = "memberSearchService")
	MemberSearchService memberSearchService;
	
	/** SelectCompanyService */
	@Resource(name = "selectCompanyService")
	SelectCompanyService selectCompanyService;
	
	@RequestMapping(value="/adm/cfg/mem/memberSearchList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		List list = new ArrayList();
		if( commandMap.get("p_action") != null && ((String)commandMap.get("p_action")).equals("go") ){
			list = memberSearchService.selectMemberList(commandMap);
		}
		HttpSession session	= request.getSession();
		commandMap.put("logIp", request.getRemoteAddr());
		
		
		model.addAttribute("list", list);
		String gadmin = (String)commandMap.get("gadmin");
		commandMap.put("pp_gadmin", gadmin.substring(0, 1));
		List compList = selectCompanyService.getCompany(commandMap);
		model.addAttribute("compList", compList);
				
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/memberSearchList";
	}
	
	@RequestMapping(value="/adm/cfg/mem/subjMemberSearchList.do")
	public String subjMemberSearchList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		List list = new ArrayList();
		int totCnt = 0;
		if( commandMap.get("p_action") != null && ((String)commandMap.get("p_action")).equals("go") ){
			//20160728수정
			//list = memberSearchService.selectMemberList(commandMap);
			System.out.println("selectMemberSearchList ----> controller ");
			log.info("ses_search_grseq --->  "+commandMap.get("ses_search_grseq"));
			log.info("ses_search_gyear --->  "+commandMap.get("ses_search_gyear"));
			log.info("ses_search_att --->  "+commandMap.get("ses_search_att"));
			log.info("ses_search_subj --->  "+commandMap.get("ses_search_subj"));
			log.info("ses_search_subjseq --->  "+commandMap.get("ses_search_subjseq"));
			log.info("ses_search_year --->  "+commandMap.get("ses_search_year"));
			log.info("search_appstatus --->  "+commandMap.get("search_appstatus"));
			log.info("search_payType --->  "+commandMap.get("search_payType"));
			log.info("p_searchtext --->  "+commandMap.get("p_searchtext"));
			log.info("search_orderColumn --->  "+commandMap.get("search_orderColumn"));	
			
			
			
			
			totCnt = memberSearchService.selectMemberSearchListTotCnt(commandMap);
			pagingManageController.PagingManage(commandMap, model, totCnt);
			
			System.out.println("totCnt -----> "+ totCnt);
			
			
			list = memberSearchService.selectMemberSearchList(commandMap);
			
			
		}else{
			pagingManageController.PagingManage(commandMap, model, totCnt);
		}
		model.addAttribute("list", list);
		
		
		HttpSession session	= request.getSession();
		commandMap.put("logIp", request.getRemoteAddr());
		
		
		
		String gadmin = (String)commandMap.get("gadmin");
		commandMap.put("pp_gadmin", gadmin.substring(0, 1));
		List compList = selectCompanyService.getCompany(commandMap);
		model.addAttribute("compList", compList);
				
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/subjMemberSearchList";
	}
	
	/**
	 * 회원조회 로그기록 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mem/searchMemberLogList.do")
	public String viewLogPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = memberSearchService.selectSearchMemberLogListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = memberSearchService.selectSearchMemberLogList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/searchMemberLogList";
	}
	
	/**
	 * 비밀번호 재발급자 명단보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mem/reissueMemberPwdList.do")
	public String viewPwdPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = memberSearchService.selectReissueMemberPwdListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = memberSearchService.selectReissueMemberPwdList(commandMap);
		model.addAttribute("list", list);
		
		Map status = memberSearchService.getStatusCount(commandMap);
		model.addAttribute("status", status);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/reissueMemberPwdList";
	}
	
	/**
	 * 회원로그인 이력보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mem/selectMemberLoginLogList.do")
	public String viewLoginLogPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String[] arrUserId = EgovStringUtil.getStringSequence(commandMap, "p_key1");
		commandMap.put("arrUserid", arrUserId);
		
		int totCnt = memberSearchService.selectMemberLoginLogListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = memberSearchService.selectMemberLoginLogList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/memberLoginLogList";
	}
	
	/**
	 * 회원로그인 이력보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mem/selectExcelPrintLogPopup.do")
	public String selectExcelPrintLogPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = memberSearchService.selectExcelPrintLogListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = memberSearchService.selectExcelPrintLogList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/excelPrintLogPopup";
	}
	
	/**
	 * 엑셀출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/mem/memberSearchExcelList.do")
	public ModelAndView memberSearchExcelList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = memberSearchService.memberSearchExcelList(commandMap);
		
//		정보조회 로그 남기기
		commandMap.put("targetUrl", "/adm/cfg/mem/memberSearchExcelList.do");
		commandMap.put("logAction", "엑셀출력");
		Object obj = memberSearchService.insertMemberSearchLog(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		return new ModelAndView("memberSearchExcelView", "memberMap", map);
	}
	
	@RequestMapping(value="/adm/cfg/mem/excelUploadPopup.do")
	public String excelUploadPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/excelUploadPopup";
	}
	
	@RequestMapping(value="/adm/cfg/mem/excelPreview.do")
	public String excelPreview(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/excelPreview";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/adm/cfg/mem/excelUploadInsert.do",  method=RequestMethod.POST)
	public String excelUploadInsert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + "member/";
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		Map fil_info = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				fil_info = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
				//			정보조회 로그 남기기
				commandMap.put("targetUrl", "/adm/cfg/mem/excelUploadInsert.do");
				commandMap.put("logAction", "회원엑셀등록");
				Object obj = memberSearchService.insertMemberSearchLog(commandMap);

			}
		}
		
		FileController file = new FileController();
		List list = file.getExcelDataList(strSavePath);
		String resultMsg = memberSearchService.insertMemberExcel(strSavePath+fil_info.get("uploadFileName"), list, commandMap);
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/excelUploadPopup";
	}
	
	@RequestMapping(value="/adm/cfg/mem/memberInsertPopup.do")
	public String memberInsertPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		List subjectList = memberSearchService.selectSubjectList(commandMap);
		model.addAttribute("list", subjectList);
		return "adm/cfg/mem/memberInsertPopup";
	}
	
	@RequestMapping(value="/adm/cfg/mem/memberInsertData.do")
	public String memberInsertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		int isOk = memberSearchService.insertMemberData(commandMap);
		if( isOk == -99 ){
			resultMsg = "중복된 주민등록번호가 있습니다.";
		}else if( isOk == 1 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
			commandMap.put("targetUrl", "/adm/cfg/mem/memberInsertData.do");
			commandMap.put("logAction", "등록");
//			정보조회 로그 남기기
			Object obj = memberSearchService.insertMemberSearchLog(commandMap);
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/memberInsertPopup";
	}
	
	@RequestMapping(value="/adm/cfg/mem/existIdPopup.do")
	public String existIdPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int existCount = memberSearchService.selectExistId(commandMap);
		if( existCount == 0 ){
			commandMap.put("isOk", true);
		}
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/existIdPopup";
	}
	
	//아이디 톰합
	@RequestMapping(value="/adm/cfg/mem/memberMergeList.do")
	public String memberMergeList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = new ArrayList();
		if( commandMap.get("p_action") != null && ((String)commandMap.get("p_action")).equals("go") ){
			list = memberSearchService.selectMemberMergeList(commandMap);
		}
		
		model.addAttribute("list", list);
		
				
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/memberMergeList";
	}
	
	//아이디 톰합상세
	@RequestMapping(value="/adm/cfg/mem/memberMergeDetailList.do")
	public String memberMergeDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = memberSearchService.selectMemberMergeDetailList(commandMap);
				
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/mem/memberMergeDetailList";
	}
	
	
	
	@RequestMapping(value="/adm/cfg/mem/memberMergeUpdate.do")
	public String memberMergeUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map<String, Object> result = memberSearchService.idMergeUpdate(commandMap);
		
		System.out.println("successCnt ---> "+result.get("successCnt"));
		
		
		String resultMsg = "";
		String successCnt = result.get("successCnt") !=null ? result.get("successCnt").toString() : "";
		if(!"".equals(successCnt)){
			resultMsg = egovMessageSource.getMessage("success.request.msg");
		}
		
		model.addAllAttributes(commandMap);
		model.addAttribute("resultMsg", resultMsg);
		return "forward:/adm/cfg/mem/memberMergeDetailList.do";
	}
	
	
}
