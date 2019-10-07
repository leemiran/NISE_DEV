package egovframework.com.lcms.inn.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.pag.controller.PagingManageController;

import egovframework.rte.fdl.property.EgovPropertyService;



/** 
 * 
 * @author 
 * @since 
 * @version 1.0
 * @see
 *
 * <pre>
 *
 * </pre>
 */
@Controller
public class innoDSFileUploadController {
	
	/** log */
    protected static final Log log = LogFactory.getLog(innoDSFileUploadController.class);

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/**
	 * innoDS 대용량 파일업로드팝업
	 * 
	 * @param curmQustManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "com/inn/innoDSFileUploadForm.jsp"
	 * @exception Exception
	 */	
	@RequestMapping(value="/com/lcms/inn/innoDSFileUploadForm.do")
	public String uploadForm(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 
	
		Map inputMap = getUploadParameter(commandMap);
		
		String path = EgovProperties.getProperty("Globals.contentFileStore")+commandMap.get("userid")+"/"+commandMap.get("subj");
		
		model.addAttribute("path", path);
		
		model.addAllAttributes(inputMap);		
	  	return "com/lcms/inn/innoDSFileUploadForm";
	}
	
	
	public Map getUploadParameter(Map<String, Object> commandMap) throws Exception{
		
		// 대용량파일 업로드타입 ( 업로드타입에따라 업로드 제한 밑 파라미터 전달 )
		String uploadType = (String)commandMap.get("uploadType");
		
		/**
		 * innodsPageTitle       : 팝업창 타이틀
		 * innodsSubDir          : 업로드 서브경로
		 * innodsRootDir         : 업로드 기본경로
		 * innodsCompression     : 압축해제여부
		 * innodsLimitExt        : 확장자명
		 * innodsExtPermission   : 확장자제한설정
		 */
		if( uploadType.equals("CONTENT") ){
			commandMap.put("innodsPageTitle", 		"컨텐츠업로드");
			commandMap.put("innodsSubDir", 			commandMap.get("subj"));
			commandMap.put("innodsRootDir", 		Globals.CONTNET_REAL_PATH);
			commandMap.put("innodsCompression", 	"Y");
			commandMap.put("innodsLimitExt", 		"zip;jar");
			commandMap.put("innodsExtPermission", 	"true");
			commandMap.put("innodsViewType", 		"0");
		}else if( uploadType.equals("FILE") ){
			commandMap.put("innodsPageTitle", 		"파일등록");
			commandMap.put("innodsSubDir", 			commandMap.get("path"));
			commandMap.put("innodsRootDir", 		Globals.CONTNET_REAL_PATH);
			commandMap.put("innodsCompression", 	"N");
			commandMap.put("innodsLimitExt", 		"");
			commandMap.put("innodsExtPermission", 	"false");
			commandMap.put("innodsViewType", 		"0");
		}else if( uploadType.equals("FOLDER") ){
			commandMap.put("innodsPageTitle", 		"폴더등록");
			commandMap.put("innodsSubDir", 			commandMap.get("path"));
			commandMap.put("innodsRootDir", 		Globals.CONTNET_REAL_PATH);
			commandMap.put("innodsCompression", 	"N");
			commandMap.put("innodsLimitExt", 		"");
			commandMap.put("innodsExtPermission", 	"false");
			commandMap.put("innodsViewType", 		"2");
		}else if( uploadType.equals("NONSCORM") ){
			commandMap.put("innodsPageTitle", 		"컨텐츠업로드");
			commandMap.put("innodsSubDir", 			commandMap.get("subj"));
			commandMap.put("innodsRootDir", 		Globals.CONTNET_REAL_PATH);
			commandMap.put("innodsCompression", 	"Y");
			commandMap.put("innodsLimitExt", 		"zip;jar,xls");
			commandMap.put("innodsExtPermission", 	"true");
			commandMap.put("innodsViewType", 		"0");
		}
		
		return commandMap;
	}
	
}
