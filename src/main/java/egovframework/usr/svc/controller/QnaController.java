package egovframework.usr.svc.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.adm.common.CommonXSSFilterUtil;
import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.adm.stu.service.StuMemberService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class QnaController {

	/** log */
	protected static final Log log = LogFactory.getLog(QnaController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** noticeAdminService */
	@Resource(name = "noticeAdminService")
	NoticeAdminService noticeAdminService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	@Autowired
	private StuMemberService stuMemberService;
	
	/**
	 * 고객센터 > QNA 리스트 - 연수관련상담
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaList01.do")
	public String selectUserQnaList01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//p_tabseq
		commandMap.put("p_tabseq", "1");
		
		int totCnt = noticeAdminService.selectQnaListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
//		qna list
		List list = noticeAdminService.selectQnaList(commandMap);
		model.addAttribute("list", list);
		
		// 내문의
		if(null != request.getSession().getAttribute("userid") && !"".equals(request.getSession().getAttribute("userid"))) {
			commandMap.put("userid", request.getSession().getAttribute("userid"));
			int myQnaCnt = stuMemberService.selectMyCursQnaTotCnt(commandMap);
			model.addAttribute("myQnaCnt", myQnaCnt);
		}
		
		model.addAllAttributes(commandMap);
		return "usr/svc/qnaList";
	}
	
	
	/**
	 * 마이페이지 > 나의 상담내역 (전체)
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/memMyQnaList.do")
	public String memMyQnaList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//나의 상담내역 전체를 가져오기 위하여 맵에 넣는다.
		commandMap.put("p_search", "userid");
		commandMap.put("p_searchtext", commandMap.get("userid"));
		
		int totCnt = noticeAdminService.selectQnaListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
//		qna list
		List list = noticeAdminService.selectQnaList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/mpg/qnaList";
	}
	
	/**
	 * 고객센터 > QNA 리스트 - 환불요청
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaList02.do")
	public String selectUserQnaList02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//p_tabseq
		commandMap.put("p_tabseq", "2, 3");
		
		int totCnt = noticeAdminService.selectQnaListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
//		qna list
		List list = noticeAdminService.selectQnaList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/svc/qnaList";
	}
	
	/**
	 * 고객센터 > QNA 리스트 - 입금확인요청
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaList03.do")
	public String selectUserQnaList03(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//p_tabseq
		commandMap.put("p_tabseq", "3");
		
		int totCnt = noticeAdminService.selectQnaListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
//		qna list
		List list = noticeAdminService.selectQnaList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/svc/qnaList";
	}
	
	

	/**
	 * 고객센터 > QNA 보기 - 연수관련상담
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/memMyQnaView.do")
	public String memMyQnaView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		qna 정보
		model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
//		조회수 업데이트
		noticeAdminService.updateQnaCount(commandMap);
		
		model.addAllAttributes(commandMap);
		return "usr/mpg/qnaView";
	}
	
	/**
	 * 마이페이지 > 나의상담내역 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaView01.do")
	public String selectUserQnaView01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		String p_openChk = (String)commandMap.get("p_openchk");		
		String userId = (String)session.getAttribute("userid");
		
		
		String url = "";

		if(p_openChk.equals("N")){
			url = "forward:/usr/hom/portalActionMainPage.do";
		}else{
			url = "usr/svc/qnaView";
			
			Map  view = noticeAdminService.selectQnaView(commandMap);
//			qna 정보
			
			if(userId.equals(view.get("userid"))){
				model.addAttribute("view", view);
			}else{
				model.addAllAttributes(commandMap);
				return "forward:/usr/hom/portalActionMainPage.do";
			}
			
			
//			조회수 업데이트
			noticeAdminService.updateQnaCount(commandMap);
		}
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	/**
	 * 고객센터 > QNA 보기 - 환불요청
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaView02.do")
	public String selectUserQnaView02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String p_openChk = (String)commandMap.get("p_openchk");
		String url = "";
		String userId = (String)commandMap.get("userid");
		
		if(p_openChk.equals("N")){
			url = "forward:/usr/hom/portalActionMainPage.do";
		}else{
			url = "usr/svc/qnaView";
//			qna 정보
			Map view = noticeAdminService.selectQnaView(commandMap);
			
			if(userId.equals(view.get("userid"))){
				model.addAttribute("view", view);
			}else{
				model.addAllAttributes(commandMap);
				return "forward:/usr/hom/portalActionMainPage.do";
			}
//			조회수 업데이트
			noticeAdminService.updateQnaCount(commandMap);
		}

		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	/**
	 * 고객센터 > QNA 보기 - 입금확인요청
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaView03.do")
	public String selectUserQnaView03(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String p_openChk = (String)commandMap.get("p_openchk");
		String userId = (String)commandMap.get("userid");
		String url = "";
		
		if(p_openChk.equals("N")){
			url = "forward:/usr/hom/portalActionMainPage.do";
		}else{
			url = "usr/svc/qnaView";
//			qna 정보
			Map view = noticeAdminService.selectQnaView(commandMap);
			
			if(userId.equals(view.get("userid"))){
				model.addAttribute("view", view);
			}else{
				model.addAllAttributes(commandMap);
				return "forward:/usr/hom/portalActionMainPage.do";
			}
//			조회수 업데이트
			noticeAdminService.updateQnaCount(commandMap);
		}

		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	/**
	 * 고객센터 > QNA 등록/수정 - 연수관련상담
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaEdit01.do")
	public String selectUserQnaEdit01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userid");
		
		System.out.println("userId 11111 ====> "+ userId);
		
		if(commandMap.get("p_seq") != null && !commandMap.get("p_seq").equals("")){
			//qna 정보	
			Map  view = noticeAdminService.selectQnaView(commandMap);		
			System.out.println("userId 2222 ====> "+ view.get("userid"));
			if(view != null){
				if(userId.equals(view.get("userid"))){
					model.addAttribute("view", view);
				}else{
					model.addAllAttributes(commandMap);
					return "forward:/usr/hom/portalActionMainPage.do";
				}
			}		
			model.addAttribute("view", view);
		}
		
		model.addAllAttributes(commandMap);
		return "usr/svc/qnaEdit";
	}
	
	
	/**
	 * 고객센터 > QNA 등록/수정 - 환불요청
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaEdit02.do")
	public String selectUserQnaEdit02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		qna 정보
		model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "usr/svc/qnaEdit";
	}
	
	
	/**
	 * 고객센터 > QNA 등록/수정 - 입금확인요청
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaEdit03.do")
	public String selectUserQnaEdit03(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		qna 정보
		model.addAttribute("view", noticeAdminService.selectQnaView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "usr/svc/qnaEdit";
	}
	
	
	
	
	
	/**
	 * 고객센터 > QNA 등록/수정/삭제 작업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/qnaAction.do", method = RequestMethod.POST)
	public String selectUserQnaAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		
		String resultMsg = "";
		String p_tabseq = (String)commandMap.get("p_tabseq");
		String p_process = (String)commandMap.get("p_process");
		String p_content = "";
		String xssContent = "";
		String userId = (String)session.getAttribute("userid");
		
		if(userId == null){
			return "forward:/usr/hom/portalActionMainPage.do";
		}
		
		String url = "/usr/svc/qnaList0" + p_tabseq + ".do";
		
		log.info("url : " + url);
		log.info("p_content : " + (String)commandMap.get("p_content"));
		
		boolean isok = false;
		
		//업로드 처리를 한다.
		commandMap.putAll(this.uploadFiles(request, commandMap));
		
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			p_content = (String)commandMap.get("p_content");
			
			xssContent = CommonXSSFilterUtil.removeXSS(p_content, true);
			
			commandMap.put("p_content", xssContent);
			//스팸확인
			int  spamCnt = noticeAdminService.selectQnaSpamCnt(commandMap);
			if(spamCnt > 0){
				return "forward:/usr/hom/portalActionMainPage.do";
			}
			Object o = noticeAdminService.insertQna(commandMap);
			if(o != null) isok = true;
		}
		else{
			
			//qna 정보
			Map  view = noticeAdminService.selectQnaView(commandMap);

			//본인확인
			if(userId.equals(view.get("userid"))){
				if(p_process.equalsIgnoreCase("update"))		{
					p_content = (String)commandMap.get("p_content");
					
					xssContent = CommonXSSFilterUtil.removeXSS(p_content, true);
					
					commandMap.put("p_content", xssContent);
					//updateQna가 아닌 updateQnaUser  사용자용 업데이트 문...실행
					isok = noticeAdminService.updateQnaUser(commandMap);
				}
				else if(p_process.equalsIgnoreCase("delete"))
				{
					isok = noticeAdminService.deleteQna(commandMap);
					
					//글의 값을 삭제하여 리스트로 보낸다.
					commandMap.remove("p_seq");
					
				}
				
			}else{
				model.addAllAttributes(commandMap);
				return "forward:/usr/hom/portalActionMainPage.do";
			}
			
			
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
			 
			 
			 String orginFileName = mFile.getOriginalFilename();    	
		  	 int index = orginFileName.lastIndexOf(".");
		     //String fileName = orginFileName.substring(0, _index);
		     String fileExt = orginFileName.substring(index + 1);
		     log.info("fileExt 0000 ---> "+fileExt);
		     
		     fileExt = fileExt.toLowerCase();
		     
		     log.info("fileExt 1111 ---> "+fileExt);	
		     
			 if(fileExt.equals("gif") ||
				fileExt.equals("png") ||
				fileExt.equals("jpg") ||
				fileExt.equals("jpeg") ||
				fileExt.equals("hwp") ||
				fileExt.equals("doc") ||
				fileExt.equals("docx") ||
				fileExt.equals("ppt") ||
				fileExt.equals("ppt") ||
				fileExt.equals("pptx") ||
				fileExt.equals("pps") ||
				fileExt.equals("ppsx") ||
				fileExt.equals("xls") ||
				fileExt.equals("xlsx") ||
				fileExt.equals("pdf") ||
				fileExt.equals("zip")){
				 
				 log.info("fileExt 222 ---> "+fileExt);
				 if (mFile.getSize() > 0) {
					 commandMap.putAll( EgovFileMngUtil.uploadContentFile(mFile, defaultDP + File.separator + "bulletin") );
				 }
				 
			 }else{
				 log.info("fileExt not upload ---> "+fileExt);
			 }
			 
			 
		}
		
		return commandMap;
	}
	
}
