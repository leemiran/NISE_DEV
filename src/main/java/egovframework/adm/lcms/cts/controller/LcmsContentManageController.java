package egovframework.adm.lcms.cts.controller;

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

import egovframework.adm.lcms.cts.service.LcmsContentManageService;
import egovframework.adm.lcms.sco.service.ScormContentService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class LcmsContentManageController {

	/** log */
    protected static final Log log = LogFactory.getLog(LcmsContentManageController.class);

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
	@Resource(name = "lcmsContentManageService")
    private LcmsContentManageService lcmsContentManageService;
	
	
	/**
	 * 형상관리 목록 페이지
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/cts/lcmsContentList.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/cts/lcmsContentList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		
		
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
			path = lcmsContentManageService.selectContentPath(commandMap);
		}
		commandMap.put("path", path);
		
		List fileList = this.getContentList(Globals.CONTNET_REAL_PATH + path);
		
		model.addAttribute("fileList", this.sortList(fileList));	
		
		model.addAllAttributes(commandMap);
		
	  	return "adm/lcms/cts/admLcmsContentList";
	}
	
	/**
	 * 형상관리 폴더생성팝업
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/cts/createContentFolder.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/cts/createContentFolderPopup.do")
	public String createContentFolderPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		model.addAllAttributes(commandMap);
		return "adm/lcms/cts/admLcmsContentFolder";
	}
	
	/**
	 * 형상관리 폴더생성팝업
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/cts/createContentFolder.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/cts/createContentFolder.do")
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
		return "adm/lcms/cts/admLcmsContentFolder";
	}
	
	/**
	 * 형상관리 파일삭제
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/cts/lcmsContentList.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/cts/deleteContentFile.do")
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
		
		return "forward:/adm/lcms/cts/lcmsContentList.do";
	}
	
	/**
	 * 형상관리 폴더생성팝업
	 * @param LcmsContentManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/cts/createContentFolder.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/adm/lcms/cts/changeContentFilePopup.do")
	public String changeFilePopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
		model.addAllAttributes(commandMap);
		return "adm/lcms/cts/admLcmsContentChangeFile";
	} 
	
	@RequestMapping(value="/adm/lcms/cts/uploadContentFile.do")
	public String uploadContentFile(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String strSavePath = EgovProperties.getProperty("Globals.contentRealPath")+commandMap.get("path");
		String resultMsg = "";
		try{
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			Iterator fileIter = mptRequest.getFileNames();
			
			HashMap map = new HashMap();
			while (fileIter.hasNext()) {
				MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
				
				if (mFile.getSize() > 0) {
					map = EgovFileMngUtil.uploadRealFile(mFile, strSavePath);
					
				}
			}
			
			resultMsg = "정상적으로 파일을 등록하였습니다.";
		}catch(Exception ex){
			resultMsg = "파일등록에 실패하였습니다.";
			ex.printStackTrace();
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "com/lcms/inn/innoDSFileUploadForm";
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
