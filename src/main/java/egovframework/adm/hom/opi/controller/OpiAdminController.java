package egovframework.adm.hom.opi.controller;

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
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class OpiAdminController {

	/** log */
	protected static final Log log = LogFactory.getLog(OpiAdminController.class);
	
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
	 * 홈페이지 > 연수개선의견 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/opi/selectOpiList.do")
	public String selectOpiList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//p_tabseq
		if(commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").equals(""))
		{
			commandMap.put("p_tabseq", "4");
		}	
		
//		보기일때 만.
		if(commandMap.get("p_seq") != null)
		{
//			qna 정보
			model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
//			조회수 업데이트
			noticeAdminService.updateQnaCount(commandMap);
		}
		
		
		int totCnt = noticeAdminService.selectQnaListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
//		qna list
		List list = noticeAdminService.selectQnaList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/opi/selectOpiList";
	}
	
	

	/**
	 * 홈페이지 > 연수개선의견 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/opi/selectOpiView.do")
	public String selectOpiView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		qna 정보
		model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "adm/hom/opi/selectOpiView";
	}
	
	/**
	 * 홈페이지 > 연수개선의견 삭제 작업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/opi/selectOpiAction.do")
	public String selectOpiAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/opi/selectOpiList.do";
		
		commandMap.put("p_tabseq", "4");
		boolean isok = false;
		
		isok = noticeAdminService.deleteQna(commandMap);
			
		//글의 값을 삭제하여 리스트로 보낸다.
		commandMap.remove("p_seq");
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + "delete");
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + "delete");
			
			
			
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
}
