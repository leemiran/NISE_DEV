package egovframework.mbl.svc.controller;

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

import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.com.cmm.EgovMessageSource;


@Controller
public class bbsController {

	/** log */
	protected static final Log log = LogFactory.getLog(bbsController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** noticeAdminService */
	@Resource(name = "noticeAdminService")
	NoticeAdminService noticeAdminService;
	
	
	
	/**
	 * 모바일 > 고객센터 > 공지사항
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/svc/noticeList.do")
	public String noticeList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String url = "mbl/svc/noticeList";
		
		//request가 ajx에서 온거라면
		if( commandMap.get("pageGubun") != null && commandMap.get("pageGubun").equals("ajax") ){
			url = "mbl/svc/noticeListAjax";
		}
		
		if( commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").toString().equals("") ){
			Map inputMap = new HashMap();
			inputMap.put("p_type", "HN");
			commandMap.put("p_tabseq", noticeAdminService.selectTableseq(inputMap));
		}
		
		
		if( commandMap.get("firstIndex") == null || commandMap.get("firstIndex").toString().equals("") ){
			commandMap.put("firstIndex", 0);
		}
		
		if( commandMap.get("recordCountPerPage") == null || commandMap.get("recordCountPerPage").toString().equals("") ){
			commandMap.put("recordCountPerPage", 15);
		}
		
		//List allList = noticeAdminService.selectNoticeListAll(commandMap);
		//model.addAttribute("allList", allList);
		
		int totCnt = noticeAdminService.selectNoticeListTotCnt(commandMap);
		
		List list = noticeAdminService.selectNoticeList(commandMap);
		model.addAttribute("list", list);
		model.addAttribute("totCnt", totCnt);
		
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	/**
	 * 모바일 > 고객센터 > 공지사항 - 보기페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/svc/noticeView.do")
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
			return "/mbl/svc/noticeView";
		}
		else
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.select"));
			model.addAllAttributes(commandMap);
			return "forward:/mbl/svc/noticeList.do";
		}
	}
	
	
	/**
	 * 모바일 > 고객센터 >  FAQ 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/svc/faqList.do")
	public String faqList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String url = "mbl/svc/faqList";
		
		//request가 ajx에서 온거라면
		if( commandMap.get("pageGubun") != null && commandMap.get("pageGubun").equals("ajax") ){
			url = "mbl/svc/faqListAjax";
		}
		
		if( commandMap.get("firstIndex") == null || commandMap.get("firstIndex").toString().equals("") ){
			commandMap.put("firstIndex", 0);
		}
		
		if( commandMap.get("recordCountPerPage") == null || commandMap.get("recordCountPerPage").toString().equals("") ){
			commandMap.put("recordCountPerPage", 15);
		}
		
		int totCnt = noticeAdminService.selectFaqListTotCnt(commandMap);
		model.addAttribute("totCnt", totCnt);
		
//		faq list
		List list = noticeAdminService.selectFaqListPage(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	/**
	 * 모바일 > 고객센터 >  FAQ 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/svc/faqView.do")
	public String faqView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if(commandMap.get("p_fnum") != null)
		{
			//보기 정보
			model.addAttribute("view", noticeAdminService.selectFaqView(commandMap));
			model.addAllAttributes(commandMap);
			return "/mbl/svc/faqView";
		}
		else
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.select"));
			model.addAllAttributes(commandMap);
			return "forward:/mbl/svc/faqList.do";
		}
	}
	
	
	/**
	 * 모바일 > 고객센터 > DATA 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/svc/dataList.do")
	public String selectFaqList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
//		데이터 구분코드
		commandMap.put("p_faqgubun", "DTA");
		
		String url = "mbl/svc/dataList";
		
		//request가 ajx에서 온거라면
		if( commandMap.get("pageGubun") != null && commandMap.get("pageGubun").equals("ajax") ){
			url = "mbl/svc/dataListAjax";
		}
		
		if( commandMap.get("firstIndex") == null || commandMap.get("firstIndex").toString().equals("") ){
			commandMap.put("firstIndex", 0);
		}
		
		if( commandMap.get("recordCountPerPage") == null || commandMap.get("recordCountPerPage").toString().equals("") ){
			commandMap.put("recordCountPerPage", 15);
		}
		
		int totCnt = noticeAdminService.selectFaqListTotCnt(commandMap);
		model.addAttribute("totCnt", totCnt);
		
//		faq list
		List list = noticeAdminService.selectFaqListPage(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	/**
	 * 모바일 > 고객센터 >  DATA 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/svc/dataView.do")
	public String dataView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
//		데이터 구분코드
		commandMap.put("p_faqgubun", "DTA");
		
		
		if(commandMap.get("p_fnum") != null)
		{
			//보기 정보
			model.addAttribute("view", noticeAdminService.selectFaqView(commandMap));
			model.addAllAttributes(commandMap);
			return "/mbl/svc/dataView";
		}
		else
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.select"));
			model.addAllAttributes(commandMap);
			return "forward:/mbl/svc/dataList.do";
		}
	}
}
