package egovframework.adm.lcms.nct.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.HSSFColor.GOLD;
import org.lcms.api.com.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.lcms.cts.service.LcmsOrganizationService;
import egovframework.adm.lcms.nct.service.LcmsModuleService;
import egovframework.adm.lcms.nct.service.LcmsNonScormManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class LcmsNonScormManageController {

	/** log */
    protected Log log = LogFactory.getLog(this.getClass());

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
	/** CodeManageService */
	@Resource(name = "lcmsNonScormManageService")
	private LcmsNonScormManageService lcmsNonScormManageService;
	
    /** lcmsModuleService */
	@Resource(name = "lcmsModuleService")
    private LcmsModuleService lcmsModuleService;
	
	/** lcmsOrganizationService */
	@Resource(name = "lcmsOrganizationService")
	private LcmsOrganizationService lcmsOrganizationService;
	
	
	/**
	 * NonScorm 리스트페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/nct/lcmsNonScormList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = lcmsNonScormManageService.selectNonScormListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = lcmsNonScormManageService.selectNonScormList(commandMap);
		
		List codeList = lcmsNonScormManageService.selectContentCodeList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAttribute("codeList", codeList);	
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/nct/admLcmsNonScormList";
	}
	
	/**
	 * 업로드 엑셀리스트 미리보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/nct/excelNonScormList.do")
	public String excelNonScormList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String strSavePath = (String)commandMap.get("path");//Globals.CONTNET_REAL_PATH+commandMap.get("subj");
		
		/*
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		 
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		*/
		
		FileController file = new FileController();
		List result = file.getExcelDataList(strSavePath);
		
		model.addAttribute("list", result);
		model.addAllAttributes(commandMap);
		
	  	return "adm/lcms/nct/admNonScormExcelList";
	}
	
	
	@RequestMapping(value="/adm/lcms/nct/LcmsNonScormModuleDelete.do")
	public String nonScormModuleDelete(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		
		String resultMsg 	= ""; 
        String forwardUrl 	= ""; 
        
        int check = lcmsOrganizationService.checkCourseMapping(commandMap);

        if( check == 0){
        	String[] module = EgovStringUtil.getStringSequence(commandMap,"chk");
        	commandMap.put("module", module);
        	//List filePath = lcmsModuleService.selectModulePath(commandMap);
        	
        	int result = lcmsModuleService.deleteLcmsModule( commandMap);
        	
//        	FileController file = new FileController();
//        	for( int i=0; i<filePath.size(); i++ ){
//        		//선택차시 컨텐츠파일 삭제
//        		boolean deleteResult = file.deleteDirector(Globals.CONTNET_REAL_PATH+((Map)filePath.get(i)).get("modulePath"));
//        	}
        	if(result > 0){  
        		resultMsg = egovMessageSource.getMessage("success.common.delete"); 
        	}else{ 
        		resultMsg = egovMessageSource.getMessage("fail.common.delete"); 
        	} 
        	
        }else{
        	resultMsg = "이미 사용중인 교과는 수정할 수 없습니다.";
        }

 
        model.addAttribute("resultMsg", resultMsg); 
        model.addAllAttributes(commandMap);	
		
		return "forward:/adm/lcms/nct/nonScormOrgList.do";
	}
	
	
	
	/**
	 * 엑셀파일 데이터 등록( 중공교 )
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/nct/LcmsCotiExcelInsert.do")
	public String insertCotiExcel(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String strSavePath = Globals.CONTNET_REAL_PATH+commandMap.get("subj");
		
		FileController file = new FileController();
		List dataList = file.getExcelDataList(strSavePath);
		
//		// 압축파일 해제
//		String contents[] = (new File(strSavePath)).list();
//		for(int j=0; j<contents.length; j++){
//			String saveFile = contents[j];
//			if( !saveFile.endsWith(".xls") ){
//				String strSavePath2 = file.checkCotiFile(strSavePath, saveFile);
//				file.fileUnZip2(strSavePath, strSavePath2, saveFile);
//			}
//		}
		
		commandMap.put("strSavePath", strSavePath);
		
		
		int result = lcmsNonScormManageService.insertCotiExcel(dataList, commandMap);
		
		if( result > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
			// 폴더 삭제
			boolean deleteResult = file.deleteDirector(strSavePath);
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "adm/lcms/nct/admNonScormExcelList";
	}
	
	/**
	 * 엑셀파일 데이터 등록( nonscorm )
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/nct/LcmsExcelInsert.do")
	public String insertExcel(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		String strSavePath = Globals.CONTNET_REAL_PATH+commandMap.get("subj");
		
		
		FileController file = new FileController();
		List dataList = file.getExcelDataList(strSavePath);
		
		
		commandMap.put("strSavePath", strSavePath);
		
		int result = lcmsNonScormManageService.insertExcel(dataList, commandMap);
		if( result > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
			// 폴더 삭제
			boolean deleteResult = file.deleteDirector(strSavePath);
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/nct/admNonScormExcelList";
	}
	
	/**
	 * 컨텐츠 업로드 취소( 업로드파일 삭제 )
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/nct/LcmsNonScormCancel.do")
	public String deleteFolder(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
    	
    	FileController file = new FileController();
    	String strSavePath = Globals.CONTNET_REAL_PATH+commandMap.get("subj");
    	
    	// 폴더 삭제
		boolean deleteResult = file.deleteDirector(strSavePath);
    	
    	if( deleteResult ){
    		resultMsg = "취소 되었습니다.";
    	}else{
    		resultMsg = "취소처리중 오류로 인하여 파일이 정상적으로 삭제되지 않았습니다.";
    	}
    	
    	model.addAttribute("resultMsg", resultMsg);
    	model.addAllAttributes(commandMap);
		return "adm/lcms/nct/admNonScormExcelList";
	}
	
	@RequestMapping(value="/adm/lcms/nct/nonScormOrgList.do")
	public String moduleList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = lcmsNonScormManageService.selectNonScormOrgList(commandMap);
		
		Map data = lcmsModuleService.selectSaveInfoData(commandMap);
		
		model.addAttribute("list", list);	
		model.addAttribute("data", data);	
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/nct/admNonScormOrgList";
	}
	
	/**
	 * 형상관리 목록 페이지
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/lcms/nct/lcmsContentList.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/nct/lcmsNonScormContentList.do")
	public String fileList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		
		
		String folder = (String)commandMap.get("folder");
		folder = folder != null && !folder.equals("") ? "/"+folder : "";
		String path = "";
		if( commandMap.get("path") != null && !commandMap.get("path").toString().equals("") ){
			path = (String)commandMap.get("path");
			if( commandMap.get("eventType").toString().equals("IN") ){
				path += folder;
			}else{
				path = path.substring(0, path.lastIndexOf("/"));
			}
		}else{
			path = lcmsNonScormManageService.selectContentPath(commandMap);
		}
		commandMap.put("path", path);
		
		List fileList = this.getContentList(Globals.CONTNET_REAL_PATH + path);
		
		model.addAttribute("fileList", this.sortList(fileList));	
		
		model.addAllAttributes(commandMap);
		
	  	return "adm/lcms/nct/admNonScormContentList";
	}
	
	/**
	 * 형상관리 폴더생성팝업
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/lcms/nct/createContentFolder.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/nct/createContentFolderPopup.do")
	public String createContentFolderPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		model.addAllAttributes(commandMap);
		return "adm/lcms/nct/admLcmsContentFolder";
	}
	
	/**
	 * 형상관리 폴더생성팝업
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/lcms/nct/createContentFolder.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/nct/createContentFolder.do")
	public String createContentFolder(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String path = Globals.CONTNET_REAL_PATH + (String)commandMap.get("path");
		String fileName = (String)commandMap.get("folderName");
		String resultMsg = "";
		File file = new File(path +"/"+ fileName);
		
		if( file.isDirectory() ){
			resultMsg = "이미 존재하는 폴더입니다.";
		}else{
			file.mkdir();
			resultMsg = "폴더를 생성 하였습니다.";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "adm/lcms/nct/admLcmsContentFolder";
	}
	
	/**
	 * 형상관리 파일삭제
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/lcms/nct/lcmsContentList.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/nct/deleteContentFile.do")
	public String deleteContentFile(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		
		
		String[] fileName = EgovStringUtil.getStringSequence(commandMap,"chk");
		String path =  Globals.CONTNET_REAL_PATH+(String)commandMap.get("path");
		FileController file = new FileController();
		for( int i=0; i<fileName.length; i++ ){
			File tmp = new File(path+"/"+fileName[i]);
			if( tmp.isDirectory() ){
				file.deleteDirector(path+"/"+fileName[i]);
			}else{
				tmp.delete();
			}
		}
		
		model.addAttribute("resultMsg", "삭제 처리 되었습니다.");
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/lcms/nct/lcmsNonScormContentList.do";
	}
	
	/**
	 * 형상관리 폴더생성팝업
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/lcms/nct/createContentFolder.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/nct/changeContentFilePopup.do")
	public String changeFilePopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		model.addAllAttributes(commandMap);
		return "adm/lcms/nct/admLcmsContentChangeFile";
	} 
	
	/**
	 * CA정보 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/nct/lcmsNonScormExcelDown.do")
	public ModelAndView excelDownload(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		
		List list = new ArrayList();
		String contentType = (String)commandMap.get("contentType");
		if( commandMap.get("contentType") != null ){
			if( contentType.equals("C") ){
				list = lcmsNonScormManageService.selectCotiExcelList(commandMap);
			}else{
				list = lcmsNonScormManageService.selectExcelList(commandMap);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("contentType", contentType);
		return new ModelAndView("lcmsExcelView", "lcmsMap", map);
	}
	
	@RequestMapping(value="/adm/lcms/nct/deleteLcmsCA.do")
	public String deleteLcmsCA(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		String resultMsg = "";

		int result = lcmsNonScormManageService.deleteLcmsCA(commandMap);
		if( result > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/lcms/nct/lcmsNonScormList.do";
	}
	
	@RequestMapping(value="/adm/lcms/nct/progressLogPopup.do")
	public String progressLogPopup(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		
		String[] arrOrgSeq = EgovStringUtil.getStringSequence(commandMap, "chk");
		commandMap.put("arrOrgSeq", arrOrgSeq);
		
		List progressList = lcmsNonScormManageService.selectProgressLogList(commandMap);
		
		model.addAttribute("progressList", progressList);
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/nct/progressLogPopup";
	}
	
	@RequestMapping(value="/adm/lcms/nct/deleteLog.do")
	public String deletePorgressLog(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
		
		String[] arrOrgSeq = EgovStringUtil.getStringSequence(commandMap, "chk");
		commandMap.put("arrOrgSeq", arrOrgSeq);
		
		String resultMsg = "";
		
		int result = lcmsNonScormManageService.deletePorgressLog(commandMap);
		if( result > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/lcms/nct/progressLogPopup.do";
	}
	
	@RequestMapping(value="/adm/lcms/nct/sampleExcelPopup.do")
	public String sampleExcelPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "adm/lcms/nct/sampleExcelPopup";
	}
	
	
	public List getContentList(String path) throws Exception{
		ArrayList result = new ArrayList();
		try{
			File file = new File(path);
			String contents[] = file.list();
			Map<String, Object> inputMap;
			Map ext = this.getExtension();
			for( int i=0; i<contents.length; i++ ){
				inputMap = new HashMap<String, Object>();
				String fileName = contents[i];
				File child = new File(file, fileName);
				inputMap.put("fileName", fileName);
				inputMap.put("modified", Util.dateToString(new Date(child.lastModified()), "yyyy-MM-dd HH:MM:SS"));
				if( child.isDirectory() ){
					inputMap.put("byte", 0);
					inputMap.put("fileType", 1);
				}else{
					inputMap.put("byte", child.length());
					inputMap.put("fileType", 2);
				}
				inputMap.put("fileExt", ext.containsKey(fileName.substring(fileName.indexOf(".") + 1)));
				
				result.add(inputMap);
			}
			
		}catch(Exception ex){
			log.info("getContentList exception : "+ex);
		}
		
		
		return result;
	}
	
	public static Map getExtension(){
		Map map = new HashMap();
		
		map.put("zip","PACK");
		map.put("alz","PACK");
		map.put("jar","PACK");

		map.put("swf","FLASH");
		map.put("fla","FLASH");
		
		map.put("gif","IMAGE");
		map.put("jpg","IMAGE");
		map.put("bmp","IMAGE");
		map.put("png","IMAGE");
		map.put("doc","WORD");
		map.put("ppt","POWERPOINT");
		map.put("xls","EXCEL");
		
		map.put("hwp","DOCUMENT");
		map.put("pdf","DOCUMENT");		

		return map;
	}
	
	public List sortList(List list) throws Exception{
		ArrayList result = new ArrayList();
		Map temp = new HashMap();
		for( int i=1; i<=2; i++ ){
			for( int j=0; j<list.size(); j++ ){
				temp = (Map)list.get(j);
				if( (Integer)temp.get("fileType") == i ){
					result.add(temp);
				}
			}
		}
		return result;
	}
}
