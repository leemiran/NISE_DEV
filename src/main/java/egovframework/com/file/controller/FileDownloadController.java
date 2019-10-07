package egovframework.com.file.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.service.FileDownloadService;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 관리자 코드 처리
 */
@Controller
public class FileDownloadController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(FileDownloadController.class);

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    @Resource(name = "fileDownloadService")
    protected FileDownloadService fileDownloadService;
    
	/** 
	 * <pre>
	 * 파일 다운로드
	 * 다운로드 요청된 파일의 정보를 서비스 객체를 통해 얻어오고,
	 * 얻어온 파일정보 객체를 ModelAndView에 셋팅해서 리턴한다.
	 * </pre>
	 * @param request HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @return 다운로드 요청된 파일과 다운로드View 정보를 가진 ModelAndView객체
	 * @throws Exception 
	 */
	@RequestMapping("/com/fileDownload.do")
	public ModelAndView fileDown(HttpServletRequest request, HttpServletResponse response,
    		Map<String, Object> commandMap) throws Exception{

		String fileName = (String)request.getParameter("fileName");
		String fileId = (String)request.getParameter("fileId");
		String subj = (String)request.getParameter("subj");
		
		FileVO fileVO = new FileVO();
		
		String filePath = null;
		Map result = new HashMap();
		//파일의 경로 지정
		if (fileId != null){
			filePath = EgovProperties.getProperty("Globals.defaultDP") + subj + "/";
			
			result = (Map)fileDownloadService.selectFileInfo(commandMap);
			if (result != null){
				fileName = (String)result.get("orignlFileNm");
			}
			
		}else{
			filePath = EgovProperties.getProperty("Globals.defaultDP") + "/sample/"; 
		}
		
		ModelAndView mav =new ModelAndView();
		if(!"".equals(fileName)){	
			//파일 객체에 다운받을 파일의 경로와 파일의 이름을 넣어서 생성
			File downFile = new File(filePath,fileName);	
			
			mav.setViewName("fileDownload");	
			mav.addObject("downloadFile", downFile);
			mav.addObject("fileName", fileName);	
			
			if(LOG.isDebugEnabled()){
				LOG.debug(">>>File DownLoad :  " + fileName + " : " + downFile.getPath() );
			}	
		}
		
		return mav;
	}
	
	/** 
	 * <pre>
	 * 파일 다운로드
	 * 다운로드 요청된 파일의 정보를 서비스 객체를 통해 얻어오고,
	 * 얻어온 파일정보 객체를 ModelAndView에 셋팅해서 리턴한다.
	 * </pre>
	 * @param request HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @return 다운로드 요청된 파일과 다운로드View 정보를 가진 ModelAndView객체
	 * @throws Exception 
	 */
	@RequestMapping("/com/commonFileDownload.do")
	public ModelAndView commonFileDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception{
		
		String fileName = (String)request.getParameter("realfile");
		String fileId = (String)request.getParameter("savefile");
		String dir = (String)request.getParameter("dir");
		
		fileName = fileName.replaceAll("\\/", "");		
		fileId = fileId.replaceAll("\\/", "");
		// dir =dir.replaceAll("\\/", ""); // nayhun
		
		System.out.println("fileName ----> "+fileName);
		System.out.println("fileId ----> "+fileId);
		System.out.println("dir ----> "+dir);
		
		
		
		String filePath = EgovProperties.getProperty("Globals.defaultDP") + dir + "/" + fileId;
		
		System.out.println("filePath ----> "+filePath);
		LOG.info("filePath ::: "+filePath);
		
		ModelAndView mav =new ModelAndView();
		if(!"".equals(fileName)){	
			//파일 객체에 다운받을 파일의 경로와 파일의 이름을 넣어서 생성
			File downFile = new File(filePath);	
			
			mav.setViewName("fileDownload");	
			mav.addObject("downloadFile", downFile);
			mav.addObject("fileName", fileName);	
			
			if(LOG.isDebugEnabled()){
				LOG.debug(">>>File DownLoad :  " + fileName + " : " + downFile.getPath() );
			}	
		}
		
		return mav;
	}
	
	
	@RequestMapping("/com/oriContentsFileDownload.do")
	public ModelAndView oriContentsFileDownload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception{
		
		String fileName = (String)request.getParameter("realfile");
		String filePath = (String)request.getParameter("savefile");

		
		
		//String filePath = EgovProperties.getProperty("Globals.defaultDP") + dir + "/" + fileId;
		
		LOG.info("filePath ::: "+filePath);
		
		ModelAndView mav =new ModelAndView();
		if(!"".equals(fileName)){	
			//파일 객체에 다운받을 파일의 경로와 파일의 이름을 넣어서 생성
			File downFile = new File(filePath);	
			
			mav.setViewName("fileDownload");	
			mav.addObject("downloadFile", downFile);
			mav.addObject("fileName", fileName);	
			
			if(LOG.isDebugEnabled()){
				LOG.debug(">>>File DownLoad :  " + fileName + " : " + downFile.getPath() );
			}	
		}
		
		return mav;
	}
	
	
}