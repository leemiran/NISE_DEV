package egovframework.svt.adm.snm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.adm.sms.SMSSenderDAO;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
public class AdminSmsController {
	
	@Autowired
	SMSSenderDAO smsSenderDAO;
	
	@RequestMapping(value="/adm/snm/sendSms.do")
	public String sendSms(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAllAttributes(commandMap);
		return "svt/adm/snm/sendSms";
	}
	
	@RequestMapping(value="/adm/snm/uploadFile.do")
	public String uploadFile(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + "sms/";
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		Map fileInfo = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				fileInfo = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		
		FileController file = new FileController();
		List<Map<String, String>> excelDataList = file.getExcelDataList(String.valueOf(fileInfo.get("filePath")) + String.valueOf(fileInfo.get("uploadFileName")));
		excelDataList.remove(0); // 타이틀 제거
		
		// list set
		// parameter0: phoneNumer, parameter1: name
		List<Map<String, String>> smsToList = new ArrayList<Map<String,String>>();
		List<Map<String, String>> errorList = new ArrayList<Map<String,String>>();
		for(Map<String, String> excelData: excelDataList) {
			Map<String, String> smsTo = new HashMap<String, String>();
			
			if(excelData.get("parameter0").trim().matches("^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$")) {
				smsTo.put("phoneNumber", excelData.get("parameter0").trim());
				smsTo.put("name", excelData.get("parameter1").trim());
				
				smsToList.add(smsTo);
			} else {
				errorList.add(excelData);
				continue;
			}
		}
		// error 엑셀파일 생성
		if(errorList.size() > 0) {
			WritableWorkbook workbook = null;
	        WritableSheet sheet = null;
	        Label label = null;
	        
	        String errorFileName = "error_" + fileInfo.get(Globals.UPLOAD_FILE_NM);
	        
	        String errorDir = EgovProperties.getProperty("Globals.defaultDP") + "sms/";
	        File cFile = new File(errorDir);
	        if (!cFile.isDirectory())
	        	cFile.mkdir();
	        
	        File errorFile = new File(errorDir + errorFileName);
	        
	        try{
	            // 파일 생성
	            workbook = Workbook.createWorkbook(errorFile);
	             
	            // 시트 생성
	            workbook.createSheet("sheet1", 0);
	            sheet = workbook.getSheet(0);
	             
	            // 셀에 쓰기
	            label = new Label(0, 0, "No");
	            sheet.addCell(label);
	            label = new Label(1, 0, "휴대폰번호");
	            sheet.addCell(label);
	            label = new Label(2, 0, "이름");
	            sheet.addCell(label);
	            label = new Label(3, 0, "오류내역");
	            sheet.addCell(label);
	            
	            // 데이터 삽입
	            for(int i=0; i < errorList.size(); i++){
	                HashMap rs = (HashMap)errorList.get(i) ;
	                 
	                label = new Label(0, (i+1), String.valueOf(i + 1));
	                sheet.addCell(label);
	                
	                label = new Label(1, (i+1), (String)rs.get("parameter0"));
	                sheet.addCell(label);
	                 
	                label = new Label(2, (i+1), (String)rs.get("parameter1"));
	                sheet.addCell(label);
	                
	                label = new Label(3, (i+1), "유효하지 않은 휴대폰번호");
	                sheet.addCell(label);
	            }
	            workbook.write();
	            workbook.close();
	            
	            model.addAttribute("errorFileName", errorFileName);
	            model.addAttribute("errorCnt", errorList.size());
	        }catch(Exception e){
	            e.printStackTrace();
	        }
		}
		
		//model.addAllAttributes(adminAutoMemberService.insertAutoMember(autoMemberList, commandMap));
		model.addAttribute("smsToList", smsToList);
		return "svt/adm/snm/smsToList";
	}
	
	@RequestMapping(value="/adm/snm/sendSmsAction.do")
	public String sendSmsAction(HttpServletRequest request, Map<String, Object> commandMap, ModelMap model
			, @RequestParam(value = "phoneNumber", required = false) String[] arrPhoneNumber) throws Exception {
		String resultMsg = "";
		
		if("direct".equals(commandMap.get("sendType").toString())) {
			arrPhoneNumber = commandMap.get("tonumberzone").toString().split(System.getProperty("line.separator"));
		}
		commandMap.put("p_handphone", arrPhoneNumber);
		
		boolean isOk = smsSenderDAO.dacomSmsSender(commandMap);
		if( isOk ){
			resultMsg = "정상적으로 발송되었습니다.";
		}else{
			resultMsg = "문자발송에 실패하였습니다.";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "svt/adm/snm/sendSms";
	}
}
