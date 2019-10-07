package egovframework.adm.cmg.not.controller;

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

import com.ziaan.library.StringManager;

import egovframework.adm.cfg.cod.service.CodeManageService;
import egovframework.adm.cmg.not.service.CourseNoticeService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.file.controller.FileController;

@Controller
public class CourseNoticeController {

	/** log */
	protected static final Log log = LogFactory.getLog(CourseNoticeController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** courseNoticeService */
	@Resource(name = "courseNoticeService")
	CourseNoticeService courseNoticeService;
	
	/** codeManageService */
	@Resource(name = "codeManageService")
	CodeManageService codeManageService;
	
	/**
	 * 과정운영 > 공지사항 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cmg/not/courseNoticeList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		HttpSession session = request.getSession();
				
		String p_gadmin = session.getAttribute("gadmin") !=null ? (String)session.getAttribute("gadmin") : "" ;		
		//System.out.println("p_gadmin -----> "+p_gadmin);
		
		if(p_gadmin.equals("Q1")){
			return "forward:/adm/cmg/prt/practiceStudyList.do?s_menu=04000000&s_submenu=04260000";
		}
		
		Map inputMap = new HashMap();
		inputMap.put("p_gubun", "0008");
		List noticeList = codeManageService.selectSubListCode(inputMap);
		String sql = getNoticeQuery(noticeList);
		commandMap.put("sql", sql);
		commandMap.put("p_gadmin", p_gadmin);
		commandMap.put("userid", (String)session.getAttribute("userid"));
		
		List list = null;
		if(commandMap.get("ses_search_grseq") !=null){
			list = courseNoticeService.selectcourseNoticeList(commandMap);
		}
		
		model.addAttribute("list", list);
		model.addAttribute("noticeList", noticeList);
				
		model.addAllAttributes(commandMap);
		return "adm/cmg/not/courseNoticeList";
	}
	
	
	/**
	 * 과정운영 > 공지사항 상세 공지리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cmg/not/courseNoticeSubList.do")
	public String subListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List noticeList = courseNoticeService.selectNoticeSubList(commandMap);
		model.addAttribute("noticeList", noticeList);
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/not/courseNoticeSubList";
	}
	
	/**
	 * 과정운영 > 공지사항 공지사항 상세보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cmg/not/courseNoticeView.do")
	public String noticeView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map data = courseNoticeService.selectNoticeView(commandMap);
		model.addAttribute("data", data);
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/not/courseNoticeView";
	}
	
	/**
	 * 과정운영 > 공지사항 공지사항 수정화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cmg/not/courseNoticeUpdatePage.do")
	public String noticeUpdatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map data = courseNoticeService.selectNoticeView(commandMap);
		model.addAttribute("data", data);
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/not/courseNoticeUpdatePage";
	}
	
	/**
	 * 과정운영 > 공지사항 공지사항 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cmg/not/courseNoticeUpdate.do")
	public String noticeUpdateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		FileController.uploadFile(request, commandMap);
		
		int isOk = courseNoticeService.updateNoticeData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/not/courseNoticeView.do";
	}
	
	/**
	 * 과정운영 > 공지사항 공지사항 등록화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cmg/not/courseNoticeInsertPage.do")
	public String noticeInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/not/courseNoticeInsertPage";
	}
	
	/**
	 * 과정운영 > 공지사항 공지사항 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cmg/not/courseNoticeInsert.do")
	public String noticeInsertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		FileController.uploadFile(request, commandMap);
		int isOk = courseNoticeService.insertNoticeData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/not/courseNoticeSubList.do";
	}
	
	/**
	 * 과정운영 > 공지사항 공지사항 삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cmg/not/courseNoticeDelete.do")
	public String noticeDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = courseNoticeService.deleteNoticeData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/not/courseNoticeSubList.do";
	}
	
	
	
	
	/**
	 * 공지카테고리 종류 관련 쿼리
	 * @param ls
	 * @return
	 * @throws Exception
	 */
	public String getNoticeQuery(List ls) throws Exception{
        String sql1 = "";
        String sql2 = "";
        int listCount = ls.size();
        for( int i=0; i<listCount; i++ ){
        	Map m = (Map)ls.get(i);
        	String code = (String)m.get("code");
        	if( i == 0 ){// 처음 쿼리 연결을 위하여
        		sql1 += ", ";
        		sql2 += ", ";
        	}else{// 문자간 결합시 사이에 '/' 추가
        		sql1 += " || '/' || ";
        		sql2 += " || '/' || ";
        	}
        	sql1 += "'"+code+"'";
			sql2 += " max(decode(c.types, '"+code+"', '"+i+"' || (select count(0) from tz_gong where types='"+code+"' and subj = a.subj and subjseq = a.scsubjseq), '"+i+"'))";
        	if( (i+1) == listCount ){// 마지막 로우일 경우
        		sql1 += " as types ";
        		sql2 += " as typescnt ";
        	}
        }
		return sql1 + sql2;
	}
}