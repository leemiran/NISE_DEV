package egovframework.adm.cmg.ter.controller;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.adm.cmg.ter.service.TermDictionaryService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;

@Controller
public class TermDictionaryController {

	/** log */
	protected static final Log log = LogFactory.getLog(TermDictionaryController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "termDictionaryService")
	TermDictionaryService termDictionaryService;
	
	@RequestMapping(value="/adm/cmg/ter/termDictionaryList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if( commandMap.get("ses_search_subj") != null && !((String)commandMap.get("ses_search_subj")).equals("")){
			List list = termDictionaryService.selectTermDictionaryList(commandMap);
			model.addAttribute("list", list);
		}
				
		model.addAllAttributes(commandMap);
		return "adm/cmg/ter/termDictionaryList";
	}
	
	@RequestMapping(value="/adm/cmg/ter/termDictionaryInsertPage.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List dicgroup = termDictionaryService.selectDicGroup(commandMap);
		model.addAttribute("dicgroup", dicgroup);
		model.addAllAttributes(commandMap);
		return "adm/cmg/ter/termDictionaryInsertPage"; 
	}
	
	@RequestMapping(value="/adm/cmg/ter/termDictionaryInsert.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = termDictionaryService.insertTermDictionary(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/ter/termDictionaryList.do"; 
	}
	
	@RequestMapping(value="/adm/cmg/ter/termDictionaryUpdatePage.do")
	public String updatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map data = termDictionaryService.selectDictionaryData(commandMap);
		model.addAttribute("data", data);
		
		List dicgroup = termDictionaryService.selectDicGroup(commandMap);
		model.addAttribute("dicgroup", dicgroup);
		
		model.addAllAttributes(commandMap);
		return "adm/cmg/ter/termDictionaryUpdatePage"; 
	}
	
	@RequestMapping(value="/adm/cmg/ter/termDictionaryUpdate.do")
	public String updateData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = termDictionaryService.updateTermDictionary(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/ter/termDictionaryList.do"; 
	}
	
	@RequestMapping(value="/adm/cmg/ter/termDictionaryDelete.do")
	public String deleteData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		int isOk = termDictionaryService.deleteTermDictionary(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cmg/ter/termDictionaryList.do"; 
	}
	
	@RequestMapping(value="/adm/cmg/ter/excelUploadPopup.do")
	public String excelUploadPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "adm/cmg/ter/excelUploadPopup"; 
	}
	
	@RequestMapping(value="/adm/cmg/ter/excelUploadInsert.do")
	public String excelUpload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		String strSavePath = EgovProperties.getProperty("Globals.fileStorePath")+"dic/"+commandMap.get("p_subj");
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		
		Map tmp = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				tmp = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		
		FileController file = new FileController();
		List result = file.getExcelDataList(strSavePath);
		(new File(strSavePath+"/"+tmp.get("uploadFileName"))).delete();
		
		int isOk = termDictionaryService.insertExcelUploadData(result, commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "adm/cmg/ter/excelUploadPopup"; 
	}
	
}
