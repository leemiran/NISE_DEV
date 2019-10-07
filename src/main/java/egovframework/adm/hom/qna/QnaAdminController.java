package egovframework.adm.hom.qna;

import java.io.File;
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

import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class QnaAdminController {

	/** log */
	protected static final Log log = LogFactory.getLog(QnaAdminController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** noticeAdminService */
	@Resource(name = "noticeAdminService")
	NoticeAdminService noticeAdminService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
	
	/**
	 * 홈페이지 > QNA 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/qna/selectQnaList.do")
	public String selectQnaList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		commandMap.put("s_menu", "15000000");
		commandMap.put("s_submenu", "15180000");
		
		//p_tabseq
		if(commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").equals(""))
		{
			commandMap.put("p_tabseq", "1");
		}	
		
//		보기일때 만.
		if(commandMap.get("p_seq") != null)
		{
//			qna 정보
			model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
//			조회수 업데이트
			noticeAdminService.updateQnaCount(commandMap);
		}
		
		
		int totCnt = noticeAdminService.selectQnaListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
//		qna list
		List list = noticeAdminService.selectQnaList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/qna/selectQnaList";
	}
	
	/**
	 * 입금게시판
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/qna/selectQnaList2.do")
	public String selectQnaList1(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		commandMap.put("s_menu", "30020000");
		commandMap.put("s_submenu", "30030000");
		
		//p_tabseq
		commandMap.put("p_tabseq", "2");
		
//		보기일때 만.
		if(commandMap.get("p_seq") != null)
		{
//			qna 정보
			model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
//			조회수 업데이트
			noticeAdminService.updateQnaCount(commandMap);
		}
		
		
		int totCnt = noticeAdminService.selectQnaListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
//		qna list
		List list = noticeAdminService.selectQnaList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/qna/selectQnaList";
	}
	
	/**
	 * 환불게시판
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/qna/selectQnaList3.do")
	public String selectQnaList3(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		commandMap.put("s_menu", "30020000");
		commandMap.put("s_submenu", "30040000");
		
		//p_tabseq
		commandMap.put("p_tabseq", "3");
		
//		보기일때 만.
		if(commandMap.get("p_seq") != null)
		{
//			qna 정보
			model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
//			조회수 업데이트
			noticeAdminService.updateQnaCount(commandMap);
		}
		
		
		int totCnt = noticeAdminService.selectQnaListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
//		qna list
		List list = noticeAdminService.selectQnaList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/qna/selectQnaList";
	}
	
	

	/**
	 * 홈페이지 > QNA 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/qna/selectQnaView.do")
	public String selectQnaView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		qna 정보
		model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "adm/hom/qna/selectQnaView";
	}
	
	/**
	 * 홈페이지 > QNA 등록/수정/삭제 작업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/qna/selectQnaAction.do")
	public String selectQnaAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		String p_tabseq = commandMap.get("p_tabseq") !=null ? (String)commandMap.get("p_tabseq") : "";
		System.out.println("p_tabseq -----> "+ p_tabseq);
		String url = "/adm/hom/qna/selectQnaList.do";
		if("2".equals(p_tabseq)){
			url = "/adm/hom/qna/selectQnaList2.do";
		}else if("3".equals(p_tabseq)){
			url = "/adm/hom/qna/selectQnaList3.do";
		}
		
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		
		//업로드 처리를 한다.
		commandMap.putAll(this.uploadFiles(request, commandMap));
		
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			Object o = noticeAdminService.insertQna(commandMap);
			if(o != null) isok = true;
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = noticeAdminService.updateQna(commandMap);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = noticeAdminService.deleteQna(commandMap);
			
			//글의 값을 삭제하여 리스트로 보낸다.
			commandMap.remove("p_seq");
			
		}
		
		if(isok)
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
		else
			resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			
			
			
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	/**
	 * 업로드된 파일을 등록한다.
	 * 
	 * @param	contentPath	컨텐츠 경로
	 * @param	contentCode	컨텐츠 코드
	 * @return	Directory 생성 여부
	 * @throws Exception
	 */
	public Map<String, Object> uploadFiles(HttpServletRequest request, Map<String, Object> commandMap) throws Exception {
		
		//기본 업로드 폴더
		String defaultDP = EgovProperties.getProperty("Globals.defaultDP");
		
		log.info("- 기본 업로드 폴더 : " + defaultDP);
		
		
		//파일업로드를 실행한다.
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		
		java.util.Iterator<?> fileIter = mptRequest.getFileNames();
		 
		while (fileIter.hasNext()) {
			 MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			 
			 if (mFile.getSize() > 0) {
				 commandMap.putAll( EgovFileMngUtil.uploadContentFile(mFile, defaultDP + File.separator + "bulletin") );
			 }
		}
		
		return commandMap;
	}
	
}
