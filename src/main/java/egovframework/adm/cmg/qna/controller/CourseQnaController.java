package egovframework.adm.cmg.qna.controller;

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

import egovframework.adm.cmg.qna.service.CourseQnaService;
import egovframework.com.bod.service.BoardManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class CourseQnaController {

	/** log */
	protected static final Log log = LogFactory.getLog(CourseQnaController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** courseQnaService */
	@Resource(name = "courseQnaService")
	CourseQnaService courseQnaService;
	
	/** boardManageService */
	@Resource(name = "boardManageService")
	BoardManageService boardManageService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		commandMap.put("p_gadmin", (String)session.getAttribute("gadmin"));
		commandMap.put("userid", (String)session.getAttribute("userid"));

		List list = courseQnaService.selectAdminList(commandMap);
		model.addAttribute("list", list);
				
		model.addAllAttributes(commandMap);
		return "adm/cmg/qna/courseAdminList";
	}
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminQnaList.do")
	public String qnaListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = boardManageService.selectBoardListForAdminBySubjseqTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = boardManageService.selectBoardListForAdminBySubjseq(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/qna/courseAdminQnaList";
	}
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminQnaView.do")
	public String qnaViewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
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
		return "adm/cmg/qna/courseAdminQnaView";
	}
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminQnaUpdatePage.do")
	public String qnaUpdatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
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
		return "adm/cmg/qna/courseAdminQnaUpdatePage";
	}
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminQnaUpdate.do")
	public String qnaUpdateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
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
		return "forward:/adm/cmg/qna/courseAdminQnaList.do";
	}
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminQnaInsertPage.do")
	public String qnaInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/qna/courseAdminQnaInsertPage";
	}
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminQnaInsert.do")
	public String qnaInsertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
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
		return "forward:/adm/cmg/qna/courseAdminQnaList.do";
	}
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminQnaDelete.do")
	public String qnaDeleteData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = boardManageService.deleteBoardData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/qna/courseAdminQnaList.do";
	}
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminQnaReplyInsertPage.do")
	public String qnaReplyInsertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = boardManageService.selectBoard(commandMap);
		Map data = new HashMap();
		ArrayList fileList = new ArrayList();
		for( int i=0; i<list.size(); i++ ){
			data = (Map)list.get(i);
		}
		model.addAttribute("data", data);
		model.addAllAttributes(commandMap);
		return "adm/cmg/qna/courseAdminQnaReplyInsertPage";
	}
	
	@RequestMapping(value="/adm/cmg/qna/courseAdminQnaReplyInsert.do")
	public String qnaReplyInsertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
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
		return "forward:/adm/cmg/qna/courseAdminQnaList.do";
	}
	
}
