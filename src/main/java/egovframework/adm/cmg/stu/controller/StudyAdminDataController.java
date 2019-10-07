package egovframework.adm.cmg.stu.controller;

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

import egovframework.adm.cmg.stu.service.StudyAdminDataService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.file.controller.FileController;

@Controller
public class StudyAdminDataController {

	/** log */
	protected static final Log log = LogFactory.getLog(StudyAdminDataController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "studyAdminDataService")
	StudyAdminDataService studyAdminDataService;
	
	@RequestMapping(value="/adm/cmg/stu/studyAdminDataList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = null;
		if(commandMap.get("ses_search_grseq") !=null){
			list = studyAdminDataService.selectStudyAdminDataList(commandMap);
		}
		model.addAttribute("list", list);
				
		model.addAllAttributes(commandMap);
		return "adm/cmg/stu/studyAdminDataList";
	}
	
	@RequestMapping(value="/adm/cmg/stu/studyAdminDataBoardList.do")
	public String subListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = studyAdminDataService.selectBoardListForAdmin(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/stu/studyAdminDataBoardList";
	}
	
	@RequestMapping(value="/adm/cmg/stu/studyAdminDataBoardView.do")
	public String viewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = studyAdminDataService.selectBoardViewData(commandMap);
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
		return "adm/cmg/stu/studyAdminDataBoardView";
	}
	
	@RequestMapping(value="/adm/cmg/stu/studyAdminDataUpdatePage.do")
	public String updatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = studyAdminDataService.selectBoardViewData(commandMap);
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
		return "adm/cmg/stu/studyAdminDataUpdatePage";
	}
	
	@RequestMapping(value="/adm/cmg/stu/studyAdminDataUpdate.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		FileController.uploadMultiFile(request, commandMap);
		
		int isOk = studyAdminDataService.updateBoardData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/stu/studyAdminDataBoardList.do";
	}
	
	@RequestMapping(value="/adm/cmg/stu/studyAdminDataInsertPage.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/stu/studyAdminDataInsertPage";
	}
	
	@RequestMapping(value="/adm/cmg/stu/studyAdminDataInsert.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		FileController.uploadMultiFile(request, commandMap);
		
		int isOk = studyAdminDataService.insertBoardData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/stu/studyAdminDataBoardList.do";
	}
	
	@RequestMapping(value="/adm/cmg/stu/studyAdminDataDelete.do")
	public String deleteData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = studyAdminDataService.deleteBoardData(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/stu/studyAdminDataBoardList.do";
	}
	
}
