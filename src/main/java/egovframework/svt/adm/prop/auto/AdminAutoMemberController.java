package egovframework.svt.adm.prop.auto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.file.controller.FileController;

@Controller
public class AdminAutoMemberController {

	protected static final Log log = LogFactory.getLog(AdminAutoMemberController.class);
	
	@Autowired
	AdminAutoMemberService adminAutoMemberService;
	
	@RequestMapping(value="/adm/prop/autoMemberList.do")
	public String autoMemberList(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		
		List<?> autoMemberList = adminAutoMemberService.autoMemberList(commandMap);
		model.addAttribute("autoMemberList", autoMemberList);
		return "svt/adm/prop/autoMemberList";
	}
	
	@RequestMapping(value="/adm/prop/insertAutoMember.do")
	public String insertAutoMember(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + "member/";
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		Map fileInfo = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				fileInfo = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
				log.info("/adm/prop/insertAutoMember.do");
				log.info("자동승인 회원등록");
			}
		}
		
		FileController file = new FileController();
		List<Map<String, String>> excelDataList = file.getExcelDataList(String.valueOf(fileInfo.get("filePath")) + String.valueOf(fileInfo.get("uploadFileName")));
		excelDataList.remove(0); // 타이틀 제거
		
		// list set
		// parameter0: empGubun, parameter1: name, parameter2: nicePersonalNum, parameter3: birthDate
		List<Map<String, String>> autoMemberList = new ArrayList<Map<String,String>>();
		for(Map<String, String> excelData: excelDataList) {
			Map<String, String> autoMember = new HashMap<String, String>();
			autoMember.put("empGubun", excelData.get("parameter0").trim());
			autoMember.put("name", excelData.get("parameter1").trim());
			
			if(null != excelData.get("parameter2")) {
				autoMember.put("nicePersonalNum", excelData.get("parameter2").trim());
			}
			if(null != excelData.get("parameter3")) {
				autoMember.put("birthDate", excelData.get("parameter3").trim());
			}
			
			autoMemberList.add(autoMember);
		}
		
		model.addAllAttributes(adminAutoMemberService.insertAutoMember(autoMemberList, commandMap));
		return "jsonView";
	}
}
