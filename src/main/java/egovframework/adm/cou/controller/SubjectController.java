package egovframework.adm.cou.controller;

import java.io.File;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.adm.cou.service.SubjectService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class SubjectController {
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
	
	
    /** subjectService */
	@Resource(name = "subjectService")
    private SubjectService subjectService;
	
	

	/**
	 * 과정 리스트 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/subjectList.do")
	public String pageList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		int totCnt = subjectService.selectSubjectListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List<?> list = subjectService.selectSubjectList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/cou/subjectList";
	}
	
	
	

	/**
	 * 과정 상세정보 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/subjectView.do")
	public String pageView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
        Map<?, ?> view = subjectService.selectSubjectView(commandMap);
		
		model.addAttribute("view", view);	
		model.addAllAttributes(commandMap);
		
		return "adm/cou/subjectView";
	}
	
	
	/**
	 * 과정 등록/수정/삭제 작업처리
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cou/subjectAction.do")
	public String pageAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		
		//System.out.println("commandMap --> "+commandMap);
		
		//업로드 처리를 한다.
		commandMap.putAll(this.uploadSubjectImage(request, commandMap));
		
		//업로드 처리를 한다.
		//List<Object> fileList = this.uploadSubjectImage(request, commandMap);
		
		
		
		/*String originalFileName = "";
		String uploadFileName = "";
		String explainfilereal = "";
		String explainfile = "";
		
		//파일
		if(fileList != null)
		{
			for(int i=0; i<fileList.size(); i++)
			{
				System.out.println("paraName ---->11111111111 ");
				HashMap fileh = (HashMap)fileList.get(i);
				
				System.out.println("paraName ----> "+fileh.get("paraName").toString());
				
				String paraName = fileh.get("paraName").toString();
				
				if("introducefile".equals(paraName)){ 
					originalFileName = fileh.get("originalFileName").toString(); 
					uploadFileName = fileh.get("uploadFileName").toString();
				}else{
					explainfilereal = fileh.get("originalFileName").toString();
					explainfile = fileh.get("uploadFileName").toString();
				}
				
			}
		}
		commandMap.put("originalFileName", originalFileName);
		commandMap.put("uploadFileName", uploadFileName);
		commandMap.put("explainfilereal", explainfilereal);
		commandMap.put("explainfile", explainfile);		
		*/
		if(commandMap.get("process").equals("insert"))
		{
			Object subj = subjectService.insertSubject(commandMap);
			
			if( subj != null){
				
//				컨텐츠 업로드 폴더
				String contentFileStore = EgovProperties.getProperty("Globals.contentFileStore");
				
				//컨텐츠 폴더 디렉토리를 생성한다.
				this.createDirectory(contentFileStore,  (String)subj);
				
				/*
				commandMap.put("subj", subj);
				
				int idx1 = subjectService.insertSubjContInfo(commandMap);
				
				int idx2 = subjectService.insertGrpSubj(commandMap);
				*/
				
				resultMsg = egovMessageSource.getMessage("success.common.insert");
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.insert");
			}
		}
		else if(commandMap.get("process").equals("update"))
		{
			int cnt = subjectService.updateSubject(commandMap);
			
			if( cnt > 0){
				
				resultMsg = egovMessageSource.getMessage("success.common.update");
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.update");
			}
		}
		else if(commandMap.get("process").equals("delete"))
		{
			int cnt = subjectService.deleteSubject(commandMap);
			
			if( cnt > 0){
				
				resultMsg = egovMessageSource.getMessage("success.common.delete");
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.delete");
			}
		}
		
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/adm/cou/subjectList.do";
	}
	
	/**
	 * Content Directory를 생성한다.
	 * 
	 * @param	contentPath	컨텐츠 경로
	 * @param	contentCode	컨텐츠 코드
	 * @return	Directory 생성 여부
	 * @throws Exception
	 */
	public boolean createDirectory(String contentPath, String contentCode) throws Exception {
		boolean result = false;
		
		File directory = new File( contentPath + contentCode );
		log.info("- 과정 코드 디렉토리 생성 URl : " + directory.getPath());
		
		if ( !directory.exists() ) {
			result = directory.mkdir();
			
			log.info("- 과정 코드 디렉토리 생성 여부 : " + result);
		}
		
		return result;
	}
	
	
	
	/**
	 * 업로드된 이미지를 등록한다.
	 * 
	 * @param	contentPath	컨텐츠 경로
	 * @param	contentCode	컨텐츠 코드
	 * @return	Directory 생성 여부
	 * @throws Exception
	 */
	public Map<String, Object> uploadSubjectImage(HttpServletRequest request, Map<String, Object> commandMap) throws Exception {
	//public  List<Object> uploadSubjectImage(HttpServletRequest request, Map<String, Object> commandMap) throws Exception {
		
		//기본 업로드 폴더
		String defaultDP = EgovProperties.getProperty("Globals.defaultDP");
		
		log.info("- 기본 업로드 폴더 : " + defaultDP);
		
		List<Object> list = new ArrayList<Object>();
		
		//파일업로드를 실행한다.
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		
		java.util.Iterator<?> fileIter = mptRequest.getFileNames();
		
		
		MultipartFile file = mptRequest.getFile("introducefile");
		
		System.out.println("getSize --> "+file.getSize());
		
		if (file.getSize() > 0) {
			commandMap.putAll( EgovFileMngUtil.uploadContentFile(file, defaultDP + File.separator + "subject", "introducefile") );

			/*Object fileHm = new HashMap();				
			fileHm = EgovFileMngUtil.uploadContentFile(file, defaultDP + File.separator + "subject", "introducefile");				
			list.add(file);	*/
		 }
		
		MultipartFile file1 = mptRequest.getFile("explainfile");
		System.out.println("getSize --> "+file1.getSize());
		if (file1.getSize() > 0) {
			commandMap.putAll( EgovFileMngUtil.uploadContentFile(file1, defaultDP + File.separator + "subject", "explainfile") );
			 
			/*Object fileHm = new HashMap();				
			fileHm = EgovFileMngUtil.uploadContentFile(file1, defaultDP + File.separator + "subject", "explainfile");				
			list.add(file1);	*/
		 }	
		
		
		/*
		System.out.println("file ----> "+file);
		System.out.println("file1 ----> "+file1);
		
		while (fileIter.hasNext()) {
			 MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			 
			 if (mFile.getSize() > 0) {
				//commandMap.putAll( EgovFileMngUtil.uploadContentFile(mFile, defaultDP + File.separator + "subject") );
				
				 String paraName= "";
				 System.out.println("introducefile ------> "+mptRequest.getFile("introducefile").getName());
				 System.out.println("explainfile ------> "+mptRequest.getFile("explainfile").getName());
				 
				 if("introducefile".equals(mptRequest.getFile("introducefile").getName())){
					 paraName = "introducefile"; 
				 }else if ("explainfile".equals(mptRequest.getFile("explainfile").getName())){
					 paraName = "explainfile"; 
				 }
				 
				 
				 
				 Object fileHm = new HashMap();				
				fileHm = EgovFileMngUtil.uploadContentFile(mFile, defaultDP + File.separator + "subject", paraName);				
				list.add(fileHm);	
			 }
		}*/
		
		return commandMap;
		//return list;
	}

}
