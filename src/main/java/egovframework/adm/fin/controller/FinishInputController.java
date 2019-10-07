package egovframework.adm.fin.controller;

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

import egovframework.adm.fin.service.FinishInputService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.file.controller.FileController;

@Controller
public class FinishInputController {

	/** log */
	protected static final Log log = LogFactory.getLog(FinishInputController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "finishInputService")
	FinishInputService finishInputService;
	
	@RequestMapping(value="/adm/fin/finishInputPage.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "adm/fin/finishInputPage";
	}
	
	@RequestMapping(value="/adm/fin/finishExcelInsert.do")
	public String insertData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + "finish/"+commandMap.get("userid")+"/";
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		Map fil_info = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				fil_info = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		
		FileController file = new FileController();
		List list = file.getExcelDataList(strSavePath+fil_info.get("uploadFileName"));
		
		String resultMsg = "";
		commandMap.put("path", strSavePath+fil_info.get("uploadFileName"));
		int isOk = finishInputService.excelMemberInsert(list, commandMap);
		
		log.info("list size : " + list.size());
		
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else if( isOk == -1){
			resultMsg = "수험번호를 확인하세요.";
		}else if( isOk == -2){
			resultMsg = "점수를 입력해 주세요.";
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		try{
			new File(strSavePath).delete();
		}catch(Exception ex){}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/fin/finishInputPage.do";
	}
	
}
