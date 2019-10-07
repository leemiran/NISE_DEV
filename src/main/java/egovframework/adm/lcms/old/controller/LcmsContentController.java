package egovframework.adm.lcms.old.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import org.lcms.api.com.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.cmg.prt.service.PracticeStudyService;
import egovframework.adm.lcms.com.service.CommonContentService;
import egovframework.adm.lcms.nct.service.LcmsModuleService;
import egovframework.adm.lcms.old.service.LcmsContentService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.utl.fcc.service.EgovStringUtil;




@Controller
public class LcmsContentController {

	/** log */
	protected static final Log log = LogFactory.getLog(LcmsContentController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	/** lcmsContentService */
	@Resource(name = "lcmsContentService")
	LcmsContentService lcmsContentService;
	
	/** commonContentService */
	@Resource(name = "commonContentService")
	CommonContentService commonContentService;
	
	/** lcmsModuleService */
	@Resource(name = "lcmsModuleService")
	LcmsModuleService lcmsModuleService;
	
	/** practiceStudyService */
	@Resource(name = "practiceStudyService")
	PracticeStudyService practiceStudyService;
	
	
	@RequestMapping(value="/adm/lcms/old/oldContentList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if( ((String)commandMap.get("gadmin")).startsWith("M") ){
			commandMap.put("type", true);
		}
		// 2017 추가
		if(null == commandMap.get("searchFinalContentYn"))
			commandMap.put("searchFinalContentYn", "Y");
		// 2017 추가 끝
		HttpSession session = request.getSession();
		commandMap.put("gadmin", (String)session.getAttribute("gadmin"));
		
		int totCnt = lcmsContentService.selectContentsListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
		List list = lcmsContentService.selectContentsList(commandMap);
		
		model.addAttribute("list", list);
				
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/oldContentList";
	}
	
	@RequestMapping(value="/adm/lcms/old/oldContentInfoView.do")
	public String infoView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map data = commonContentService.selectMasterformData(commandMap);
		Map subject = lcmsContentService.selectSubjectData(commandMap);
		
		model.addAttribute("data", data);
		model.addAttribute("subject", subject);
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/oldContentInfoView";
	}
	
	@RequestMapping(value="/adm/lcms/old/oldContentItemList.do")
	public String itemListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = lcmsContentService.selectItemList(commandMap);
		
		model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/oldContentItemList";
	}
	
	@RequestMapping(value="/adm/lcms/old/oldContentItemDelete.do")
	public String deleteCA(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";

		int isOk = lcmsContentService.deleteCA(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/lcms/old/oldContentItemList.do";
	}
	
	@RequestMapping(value="/adm/lcms/old/oldContentModulePopup.do")
	public String modulePopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map module_info = null;
		String module_key = "";
		if( !commandMap.get("module").toString().equals("") ){
			module_info = lcmsContentService.selectModuleInfo(commandMap);
		}else{
			module_key = lcmsContentService.selectModuleKey(commandMap); 
		}
		
		model.addAttribute("module_info", module_info);	
		model.addAttribute("module_key", module_key);
		
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/oldContentModulePopup";
	}
	
	@RequestMapping(value="/adm/lcms/old/lcmsModuleInsert.do")
	public String insertModule(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";

		int isOk = lcmsContentService.insertModule(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("finish", true);
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/lcms/old/oldContentModulePopup.do";
	}
	
	@RequestMapping(value="/adm/lcms/old/lcmsModuleUpdate.do")
	public String updateModule(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = lcmsContentService.updateModule(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("finish", true);
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/lcms/old/oldContentModulePopup.do";
	}
	
	
	@RequestMapping(value="/adm/lcms/old/oldContentLessonPopup.do")
	public String lessonPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Map lesson_info = null;
		String lesson_key = "";
		if( !commandMap.get("lesson").toString().equals("") ){
			lesson_info = lcmsContentService.selectLessonInfo(commandMap);
		}else{
			lesson_info = lcmsContentService.selectNewLessonInfo(commandMap); 
		}
		
		model.addAttribute("lesson_info", lesson_info);	
		
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/oldContentLessonPopup";
	}
	
	@RequestMapping(value="/adm/lcms/old/lcmsLessonInsert.do")
	public String insertLesson(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = lcmsContentService.insertLesson(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		model.addAttribute("finish", true);
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/lcms/old/oldContentLessonPopup.do";
	}
	
	@RequestMapping(value="/adm/lcms/old/lcmsLessonUpdate.do")
	public String updateLesson(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = lcmsContentService.updateLesson(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("finish", true);
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/lcms/old/oldContentLessonPopup.do";
	}
	
	@RequestMapping(value="/adm/lcms/old/oldContentUploadXlsPage.do")
	public String uploadPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/oldContentUploadXls";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/adm/lcms/old/oldContentUploadXls.do",  method=RequestMethod.POST)
	public String uploadXls(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//모바일여부
		String mobileYn = commandMap.get("mobile_yn") + "";
		
		String strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + "lcms/";
		
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		Map fil_info = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				fil_info = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		
		//데이터 등록
		String resultMsg = "";
		
		FileController file = new FileController();
		List list = new ArrayList();
		try{
			list = file.getExcelDataList(strSavePath);
		}catch(IndexOutOfBoundsException ie){
			resultMsg = "파일 양식을 확인해주세요.";
			File del = new File(strSavePath+fil_info.get("uploadFileName"));
			del.delete();
		}
		
		//모바일용업로드
		if(mobileYn != null && mobileYn.equals("Y"))
		{
			resultMsg = lcmsContentService.updateLcmsMobileExcel(strSavePath+fil_info.get("uploadFileName"), list, commandMap);
		}
		//웹컨텐츠용 업로드
		else
		{
			resultMsg = lcmsContentService.insertLcmsCAExcel(strSavePath+fil_info.get("uploadFileName"), list, commandMap);
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/oldContentUploadXls";
	}
	
	@RequestMapping(value="/adm/lcms/old/oldContentExcelDown.do")
	public ModelAndView excelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = lcmsContentService.selectItemList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		return new ModelAndView("LcmsOldExcelView", "lcmsMap", map);
	}
	
	@RequestMapping(value="/adm/lcms/old/oldContentBetaProgressDelete.do")
	public String deleteBetaProgress(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = lcmsContentService.deleteBetaProgress(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/lcms/old/oldContentList.do";
	}
	
	/**
	 * 콘텐츠관리 - 파일리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/old/oldContentListPop.do")
	public String selectOldContentListPop(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String path = "";
		String folder = "";
		if( commandMap.get("folder") != null && !"".equals((String)commandMap.get("folder")) ){
			folder = (String)commandMap.get("folder");
		}
		if( commandMap.get("path") != null && !commandMap.get("path").toString().equals("") ){
			path = (String)commandMap.get("path");
			if( commandMap.get("eventType").toString().equals("IN") ){
				if( !"".equals(folder) ){
					path += "/"+folder;
				}
			}else{
				path = path.substring(0, path.lastIndexOf("/"));
			}
		}else{
			path = "contents/" + (String)commandMap.get("subj");
		}
		commandMap.put("path", path);
		
		List fileList = this.getContentList(Globals.CONTNET_REAL_PATH + path);
		
		model.addAttribute("fileList", this.sortList(fileList));	
		
		model.addAllAttributes(commandMap);
		
	  	return "adm/lcms/old/oldContentListPop";
	}
	
	/**
	 * 콘텐츠관리 - 폴더생성팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/old/createContentFolderPopup.do")
	public String createContentFolderPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/oldContentFolder";
	}
	
	/**
	 * 콘텐츠관리 - 파일생성
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/old/createContentFolder.do")
	public String createContentFolder(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String path = Globals.CONTNET_REAL_PATH + (String)commandMap.get("path");
		String fileName = (String)commandMap.get("folderName");
		String resultMsg = "";
		File file = new File(path +"/"+ fileName);
		
		if( file.isDirectory() ){
			resultMsg = "이미 존재하는 폴더입니다.";
		}else{
			file.mkdirs();
			resultMsg = "폴더를 생성 하였습니다.";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/oldContentFolder";
	}
	
	/**
	 * 콘텐츠관리 - 파일삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/lcms/old/deleteContentFile.do")
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
		
		return "forward:/adm/lcms/old/oldContentListPop.do";
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
				inputMap.put("modified", Util.dateToString(new Date(child.lastModified()), "yyyy-MM-dd HH:mm"));
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
	
	@RequestMapping(value="/adm/lcms/old/moduleUploadPage.do")
	public String moduleUploadPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/moduleUploadPage";
	}
	
	@RequestMapping(value="/adm/lcms/old/lessonUploadPage.do")
	public String lessonUploadPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/lessonUploadPage";
	}
	
	@RequestMapping(value="/adm/lcms/old/moduleUpload.do")
	public String moduleUpload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + "lcms/";
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		Map fil_info = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				fil_info = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		
		String resultMsg = "";
		
		FileController file = new FileController();
		List list = new ArrayList();
		try{
			list = file.getExcelDataList(strSavePath+fil_info.get("uploadFileName"));
		}catch(IndexOutOfBoundsException ie){
			resultMsg = "파일 양식을 확인해주세요.";
			File del = new File(strSavePath+fil_info.get("uploadFileName"));
			del.delete();
		}
		
		resultMsg = lcmsContentService.insertModuleExcelUpload(strSavePath+fil_info.get("uploadFileName"), list, commandMap);
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/moduleUploadPage";
	}
	
	@RequestMapping(value="/adm/lcms/old/lessonUpload.do")
	public String lessonUpload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String strSavePath = EgovProperties.getProperty("Globals.fileStorePath") + "lcms/";
		
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		Map fil_info = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				fil_info = EgovFileMngUtil.uploadContentFile(mFile, strSavePath);
			}
		}
		
		String resultMsg = "";
		
		FileController file = new FileController();
		List list = new ArrayList();
		try{
			list = file.getExcelDataList(strSavePath+fil_info.get("uploadFileName"));
		}catch(IndexOutOfBoundsException ie){
			resultMsg = "파일 양식을 확인해주세요.";
			File del = new File(strSavePath+fil_info.get("uploadFileName"));
			del.delete();
		}
		
		resultMsg = lcmsContentService.insertLessonExcelUpload(strSavePath+fil_info.get("uploadFileName"), list, commandMap);
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/lessonUploadPage";
	}
	
	@RequestMapping(value="/adm/lcms/old/moduleRecoveryPage.do")
	public String moduleRecoveryPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		commandMap.put("pageUnit", "2");
		if(null == commandMap.get("pageIndex")) {
			commandMap.put("pageIndex", "1");
		}
		
		int totCnt = 0;
		List<Map<String, Object>> moduleList = lcmsContentService.getModuleBackupGroupList(commandMap);
		
		if(moduleList.size() > 0) {
			totCnt = Integer.parseInt(String.valueOf(moduleList.get(0).get("totalCnt")));
		}
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		model.addAttribute("moduleList", moduleList);
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/moduleRecoveryPage";
	}
	
	@RequestMapping(value="/adm/lcms/old/lessonRecoveryPage.do")
	public String lessonRecoveryPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		commandMap.put("recordCountPerPage", "10");
		if(null == commandMap.get("pageIndex")) {
			commandMap.put("pageIndex", "1");
		}
		
		int totCnt = 0;
		List<Map<String, Object>> lessonList = lcmsContentService.getLessonBackupGroupList(commandMap);
		
		if(lessonList.size() > 0) {
			totCnt = Integer.parseInt(String.valueOf(lessonList.get(0).get("totalCnt")));
		}
		pagingManageController.PagingManage(commandMap, model, totCnt);
		
		model.addAttribute("lessonList", lessonList);
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/lessonRecoveryPage";
	}
	
	@RequestMapping(value="/adm/lcms/old/recoveryModule.do")
	public String recoveryModule(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = null;
		
		int isOk = lcmsContentService.recoveryModule(commandMap);
		if(isOk > 0) {
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		} else {
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/lcms/old/recoveryLesson.do")
	public String recoveryLesson(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = null;
		
		int isOk = lcmsContentService.recoveryLesson(commandMap);
		if(isOk > 0) {
			resultMsg = egovMessageSource.getMessage("success.common.insert");
		} else {
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		return "jsonView";
	}

	@RequestMapping(value="/adm/lcms/old/deleteModuleBackup.do")
	public String deleteModuleBackup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = null;
		
		int isOk = lcmsContentService.deleteModuleBackup(commandMap);
		if(isOk > 0) {
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		} else {
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/lcms/old/deleteLessonBackup.do")
	public String deleteLessonBackup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = null;
		
		int isOk = lcmsContentService.deleteLessonBackup(commandMap);
		if(isOk > 0) {
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		} else {
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		return "jsonView";
	}
	
	@RequestMapping(value="/adm/lcms/old/moduleExcelDown.do")
	public ModelAndView moduleExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = lcmsContentService.getSubjModuleList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("subj", commandMap.get("subj"));
		return new ModelAndView("LcmsModuleExcelView", "lcmsMap", map);
	}
	
	@RequestMapping(value="/adm/lcms/old/lessonExcelDown.do")
	public ModelAndView lessonExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = lcmsContentService.getSubjLessonList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("subj", commandMap.get("subj"));
		return new ModelAndView("LcmsLessonExcelView", "lcmsMap", map);
	}
	
	@RequestMapping(value="/adm/lcms/old/moduleBackupExcelDown.do")
	public ModelAndView moduleBackupExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = lcmsContentService.getSubjModuleBackupList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("subj", commandMap.get("subj"));
		return new ModelAndView("LcmsModuleExcelView", "lcmsMap", map);
	}
	
	@RequestMapping(value="/adm/lcms/old/lessonBackupExcelDown.do")
	public ModelAndView lessonBackupExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = lcmsContentService.getSubjLessonBackupList(commandMap);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("subj", commandMap.get("subj"));
		return new ModelAndView("LcmsLessonExcelView", "lcmsMap", map);
	}
	
	@RequestMapping(value="/adm/lcms/old/practiceStudyList.do")
	public String practiceStudyList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
	
		HttpSession session = request.getSession();
		String p_gadmin = session.getAttribute("gadmin") !=null ? (String)session.getAttribute("gadmin") : "" ;		
		//System.out.println("p_gadmin -----> "+p_gadmin);
		
		List list = null;
		if(p_gadmin.equals("Q1")){
			list = practiceStudyService.subjmanPracticeStudyList(commandMap);
		}else{
			commandMap.put("p_gadmin", p_gadmin);
			commandMap.put("userid", (String)session.getAttribute("userid"));
			list = practiceStudyService.practiceStudyList(commandMap);
		}
		
		
		model.addAttribute("list", list);
				
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/practiceStudyList";
	}
	
	@RequestMapping(value="/adm/lcms/old/practiceStudySubList.do")
	public String practiceStudySubList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = practiceStudyService.practiceStudySubList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/lcms/old/practiceStudySubList";
	}
		
	@RequestMapping(value="/adm/lcms/old/oriContentsSubjList.do")
	public String oriContentsSubjList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
				
		List list = lcmsContentService.selectExamSubjList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/old/oriContentsSubjList";
	}
	
	@RequestMapping(value="/adm/lcms/old/oriContentsFile.do")
	public String oriContentsFileDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
				
		Map view = lcmsContentService.selectOriContents(commandMap);
		model.addAttribute("view", view);
		
		List list = lcmsContentService.selectContentsFileList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		
		return "adm/lcms/old/oriContentsFile";
	}
	
	@RequestMapping(value="/adm/lcms/old/oriContentsFileInsert.do")
	public String oriContentsFileInsert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String url = "forward:/adm/lcms/old/oriContentsFile.do";
		
		String[] origfilename = request.getParameterValues("_innorix_origfilename"); 	// 원본 파일명
		String[] savefilename = request.getParameterValues("_innorix_savefilename"); 	// 저장 파일명
		String[] savepath = request.getParameterValues("_innorix_savepath"); 			// 파일 저장경로
		String[] filesize = request.getParameterValues("_innorix_filesize"); 			// 파일 사이즈
		String[] foldername = request.getParameterValues("_innorix_folder"); 			// 폴더정보(폴더 업로드시만)
		String[] customvalue = request.getParameterValues("_innorix_customvalue"); 		// 개발자 정의값
		String[] componentname = request.getParameterValues("_innorix_componentname"); 	// 컴포넌트 이름
		
		
		commandMap.put("origfilename", request.getParameterValues("_innorix_origfilename"));	// 원본 파일명
		commandMap.put("savefilename", request.getParameterValues("_innorix_savefilename"));					// 저장 파일명
		commandMap.put("savepath", request.getParameterValues("_innorix_savepath"));								// 파일 저장경로
		commandMap.put("filesize", request.getParameterValues("_innorix_filesize"));										// 파일 사이즈
		commandMap.put("foldername", request.getParameterValues("_innorix_folder"));									// 폴더정보(폴더 업로드시만)
		commandMap.put("customvalue", request.getParameterValues("_innorix_customvalue"));					// 개발자 정의값
		commandMap.put("componentname", request.getParameterValues("_innorix_componentname"));	// 컴포넌트 이름
		
		
		String p_process = commandMap.get("p_process").toString();
		if("insert".equals(p_process)){
			boolean isOk = lcmsContentService.insertOriContents(commandMap);
		}else if("update".equals(p_process)){
			boolean isOk = lcmsContentService.updateOriContents(commandMap);
			commandMap.remove("p_seq");
		}
		
		if(origfilename != null){
			boolean isOk = lcmsContentService.insertOriContentsFileInsert(commandMap);
		}
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	@RequestMapping(value="/adm/lcms/old/oriContentsFileUpload.do")
	public String oriContentsFileUpload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String url = "/adm/lcms/old/oriContentsFileUpload";
		model.addAllAttributes(commandMap);				
		return url;
	}
	
	
		
}