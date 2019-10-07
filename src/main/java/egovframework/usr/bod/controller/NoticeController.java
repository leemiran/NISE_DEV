package egovframework.usr.bod.controller;

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

import egovframework.adm.common.CommonXSSFilterUtil;
import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class NoticeController {

	/** log */
	protected static final Log log = LogFactory.getLog(NoticeController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** noticeAdminService */
	@Resource(name = "noticeAdminService")
	NoticeAdminService noticeAdminService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
	
	
	/**
	 * 참여마당 > 공지사항 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/bod/noticeList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if( commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").toString().equals("") ){
			Map inputMap = new HashMap();
			inputMap.put("p_type", "HN");
			commandMap.put("p_tabseq", noticeAdminService.selectTableseq(inputMap));
		}
		
		List allList = noticeAdminService.selectNoticeListAll(commandMap);
		model.addAttribute("allList", allList);
		
		int totCnt = noticeAdminService.selectNoticeListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = noticeAdminService.selectNoticeList(commandMap);
		model.addAttribute("list", list);
		model.addAttribute("totCnt", totCnt);
		
		model.addAllAttributes(commandMap);
		return "/usr/bod/noticeList";
	}
	
	
	
	/**
	 * 참여마당 > 공지사항 보기 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/bod/noticeView.do")
	public String viewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if(commandMap.get("p_seq") != null)
		{
			//조회수 업데이트 
			noticeAdminService.updateNoticeReadCount(commandMap);
			
			//보기 정보
			model.addAttribute("view", noticeAdminService.selectNoticeView(commandMap));
			
			//첨부파일 리스트 정보
			List fileList = noticeAdminService.selectBoardFileList(commandMap);
			
			model.addAttribute("fileList", fileList);
			
			
			model.addAllAttributes(commandMap);
			return "/usr/bod/noticeView";
		}
		else
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.select"));
			model.addAllAttributes(commandMap);
			return "forward:/usr/bod/noticeList.do";
		}
	}
	
	

	/**
	 * 참여마당 > 연수소감 - 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/bod/opinionUserList.do")
	public String opinionUserList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//p_tabseq
		commandMap.put("p_tabseq", "0");
		
		int totCnt = noticeAdminService.selectQnaListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
		List list = noticeAdminService.selectQnaList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/bod/opinionUserList";
	}
	
	/**
	 * 참여마당 > 연수개선의견
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/bod/opinionUpgradeWrite.do")
	public String opinionUpgradeWrite(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
			
		model.addAllAttributes(commandMap);
		return "usr/bod/opinionUpgradeWrite";
	}
	@RequestMapping(value="/usr/bod/opinionUpgradePopup.do")
	public String opinionUpgradePopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "usr/bod/opinionUpgradePopup";
	}
	
	
	/**
	 * 참여마당 > 연수소감 - 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/bod/opinionUserView.do")
	public String opinionUserView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
//		조회수 업데이트
		noticeAdminService.updateQnaCount(commandMap);
		
		model.addAllAttributes(commandMap);
		return "usr/bod/opinionUserView";
	}
	
	/**
	 * 참여마당 > 연수소감 -  등록/수정/답변 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/bod/opinionUserEdit.do")
	public String opinionUserqnaEdit(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String p_process = (String)commandMap.get("p_process");
		
		model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "usr/bod/opinionUserEdit";
	}
	
	
	/**
	 * 참여마당 > 연수소감 등록/수정/답변/삭제 작업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/bod/opinionUserAction.do")
	public String opinionUserAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String p_process = (String)commandMap.get("p_process");
		String p_content = "";
		String xssContent = "";
		
		String url = "/usr/bod/opinionUserList.do";
		
		boolean isok = false;
		
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			p_content = (String)commandMap.get("p_content");
			
			xssContent = CommonXSSFilterUtil.removeXSS(p_content, true);
			commandMap.put("p_content", xssContent);
			Object o = noticeAdminService.insertQna(commandMap);
			if(o != null) isok = true;
		}
		else if(p_process.equalsIgnoreCase("reply"))
		{
			p_content = (String)commandMap.get("p_content");
			
			xssContent = CommonXSSFilterUtil.removeXSS(p_content, true);
			commandMap.put("p_content", xssContent);
			Object o = noticeAdminService.insertQnaReply(commandMap);
			if(o != null) isok = true;
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			p_content = (String)commandMap.get("p_content");
			
			xssContent = CommonXSSFilterUtil.removeXSS(p_content, true);
			commandMap.put("p_content", xssContent);
			//updateQna가 아닌 updateQnaUser  사용자용 업데이트 문...실행
			isok = noticeAdminService.updateQnaUser(commandMap);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = noticeAdminService.deleteQna(commandMap);
			
			//글의 값을 삭제하여 리스트로 보낸다.
			commandMap.remove("p_seq");
			
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
	 * 참여마당 > 연수개선의견 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/bod/opinionUpgradeAction.do")
	public String opinionUpgradeAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String p_content = "";
		String xssContent = "";
		
		String url = "/usr/bod/opinionUpgradePopup.do";
		
		boolean isok = false;
		
		p_content = (String)commandMap.get("p_content");
		
		xssContent = CommonXSSFilterUtil.removeXSS(p_content, true);
		commandMap.put("p_content", xssContent);
		Object o = noticeAdminService.insertQna(commandMap);
		if(o != null) isok = true;
		
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + "insert");
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + "insert");
			
			
			
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	
}
