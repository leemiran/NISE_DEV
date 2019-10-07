package egovframework.com.upl.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.property.EgovPropertyService;



/**
 * 업로드 공통
 */
@Controller
public class FileUploadController {
   
	/** log */
    protected static final Log log = LogFactory.getLog(FileUploadController.class);
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
	/** fileUtil */
    @Resource(name="EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;
    
    /** fileMngService */
    @Resource(name="EgovFileMngService")
    private EgovFileMngService fileMngService;
    
    /**
     * 공통 업로드를 구현한다.
     * @param final MultipartHttpServletRequest multiRequest,
    		Map<String, Object> commandMap, 
    		ModelMap model
     * @return 
     * @exception Exception
     */	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/com/upl/comFileUploadAction.do",  method=RequestMethod.POST)  
    public String handleRequestAction(
    		final MultipartHttpServletRequest multiRequest,
    		Map<String, Object> commandMap, 
    		ModelMap model) throws Exception {	
		
		String url = "/com/upl/comFileUpload_P";
		
		String keyFileId = commandMap.get("keyFileId") + "";	//파일아이디 첨자
		String _atchFileId = commandMap.get("atchFileId") + "";	//기존의 파일 아이디
		String posblAtchFileNumber = commandMap.get("posblAtchFileNumber") + "";	//첨부가능개수
		
		if(keyFileId == null || keyFileId.equals("")) {keyFileId = "CMM";}	//기본 키파일 아이디 지정.
		
		List<?> _result = null;
		
		try {
			final Map<String, MultipartFile> files = multiRequest.getFileMap();
			
			if(!files.isEmpty()){
				if(_atchFileId == null || "".equals(_atchFileId))
				{
					_result = fileUtil.parseFileInf(files, keyFileId.toUpperCase() + "_", 0, "", keyFileId.toLowerCase()); //마지막부분은 Globals.fileStorePath + 경로이다..
					log.info(_result);
					
					_atchFileId = fileMngService.insertFileInfs(_result);  //파일이 생성되고나면 생성된 첨부파일 ID를 리턴한다.
					log.info(_atchFileId);
					
				} else {
					FileVO fvo = new FileVO();
					fvo.setAtchFileId(_atchFileId); // 최종 파일 순번을 획득하기 위하여 VO에 현재 첨부파일 ID를 세팅한다.
					int _cnt = fileMngService.getMaxFileSN(fvo); // 해당 첨부파일 ID에 속하는 최종 파일 순번을 획득한다.
					_result = fileUtil.parseFileInf(files, keyFileId.toUpperCase() + "_", _cnt, _atchFileId, keyFileId.toLowerCase()); //마지막부분은 Globals.fileStorePath + 경로이다..
					fileMngService.updateFileInfs(_result);
				}
				
				url = "redirect:/com/upl/comFileUploadIframe.do?atchFileId=" + _atchFileId + "&posblAtchFileNumber=" + posblAtchFileNumber + "&keyFileId=" + keyFileId;
				
			}
		} catch(Exception e)
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.insert"));
		}
		
		
		log.info("############# return url : " + url);
		return url;
    }
	

    /**
     * 공통 업로드를 구현한다.
     * @param final MultipartHttpServletRequest multiRequest,
    		Map<String, Object> commandMap, 
    		ModelMap model
     * @return 
     * @exception Exception
     */	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/com/upl/comFileUploadActionCom.do")  
    public String handleRequestActionCom(
    		final MultipartHttpServletRequest multiRequest,
    		Map<String, Object> commandMap, 
    		ModelMap model) throws Exception {	
		
		String keyFileId           = commandMap.get("keyFileId") + "";	//파일아이디 첨자
		String _atchFileId         = commandMap.get("atchFileId") + "";	//기존의 파일 아이디
		String posblAtchFileNumber = commandMap.get("posblAtchFileNumber") + "";	//첨부가능개수
		String type                = commandMap.get("type") + "";	   //등록 타입
		
		if(keyFileId == null || keyFileId.equals("")) {keyFileId = "CMM";}	//기본 키파일 아이디 지정.
		
		List<?> _result = null;
		
		//String url = "/com/upl/comFileUploadIframeCom.do";
		String url = "/com/upl/comFileUpload_c";
		
		//String url = "com/upl/comFileUpload_c";
		
		try {
			final Map<String, MultipartFile> files = multiRequest.getFileMap();
			
			if(!files.isEmpty()){
				if(type.equals("insert")) // insert
				{
					_result = fileUtil.parseFileInf(files, keyFileId.toUpperCase() + "_", 0, _atchFileId, keyFileId.toLowerCase()); //마지막부분은 Globals.fileStorePath + 경로이다..
					log.info(_result);
					
					_atchFileId = fileMngService.insertFileInfs(_result);  //파일이 생성되고나면 생성된 첨부파일 ID를 리턴한다.
					log.info(_atchFileId);
				}	
			    if(type.equals("update")){ // update
					FileVO fvo = new FileVO();
					fvo.setAtchFileId(_atchFileId); // 최종 파일 순번을 획득하기 위하여 VO에 현재 첨부파일 ID를 세팅한다.
					int _cnt = fileMngService.getMaxFileSN(fvo); // 해당 첨부파일 ID에 속하는 최종 파일 순번을 획득한다.
					
					int fileCnt = fileMngService.getCountFileSN(fvo); // 해당 첨부파일 ID에 속하는 최종 파일 순번을 획득한다.
					
					
					_result = fileUtil.parseFileInf(files, keyFileId.toUpperCase() + "_", _cnt, _atchFileId, keyFileId.toLowerCase()); //마지막부분은 Globals.fileStorePath + 경로이다..
					
					if(fileCnt ==0){
						_atchFileId = fileMngService.insertFileInfs(_result);  //파일이 생성되고나면 생성된 첨부파일 ID를 리턴한다.
					} else {
						fileMngService.updateFileInfs(_result);
					}
				}
			}
		} catch(Exception e)
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.insert"));
		}
		
		
		//log.info("############# return url : " + url);  
		System.out.println("test"+ url);
		return url;
    }
	

	/**
     * 공통 업로드를 구현한다.
     * @param Map<String, Object> commandMap, ModelMap model
     * @return 출력페이지정보 "adm/scu/admScoUpload_P.jsp" 
     * @exception Exception
     */
	@RequestMapping(value="/com/upl/comFileUploadIframe.do")
    public String handleRequestInput(Map<String, Object> commandMap, ModelMap model) throws Exception {
        model.addAllAttributes(commandMap);
        return  "com/upl/comFileUpload_P"; 
    }
	/**
	 * 공통 업로드를 구현한다.
	 * @param Map<String, Object> commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/scu/admScoUpload_P.jsp" 
	 * @exception Exception
	 */
	@RequestMapping(value="/com/upl/comFileUploadIframeCom.do")
	public String handleRequestInputCom(Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAllAttributes(commandMap);
		return  "com/upl/comFileUpload_c"; 
	}
}