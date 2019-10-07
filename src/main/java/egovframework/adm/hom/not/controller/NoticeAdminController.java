package egovframework.adm.hom.not.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.pag.controller.PagingManageController;

@Controller
public class NoticeAdminController {

	/** log */
	protected static final Log log = LogFactory.getLog(NoticeAdminController.class);
	
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
 * 홈페이지 > 공지사항 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/not/selectNoticeList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		System.out.println(">>>>>>>selectNoticeList commandMap>>>>>>>>>>>>>> : " + commandMap);
		
		if( commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").toString().equals("") ){
			Map inputMap = new HashMap();
			inputMap.put("p_type", "HN");
			commandMap.put("p_tabseq", noticeAdminService.selectTableseq(inputMap));
		}
		
		List allList = noticeAdminService.selectNoticeListAll(commandMap);
		model.addAttribute("allList", allList);
		
		int totCnt = noticeAdminService.selectNoticeListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
		List list = noticeAdminService.selectNoticeList(commandMap);
		model.addAttribute("list", list);
		model.addAttribute("totCnt", totCnt);
		
		model.addAllAttributes(commandMap);
		return "adm/hom/not/selectNoticeList";
	}
	
	
	/**
	 * 홈페이지 > 공지사항 등록/수정 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/not/noticeInsertPage.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//보기 정보
		model.addAttribute("view", noticeAdminService.selectNoticeView(commandMap));
		
		//첨부파일 리스트 정보
		List fileList = noticeAdminService.selectBoardFileList(commandMap);
		
		model.addAttribute("fileList", fileList);
		
		
		
		
		model.addAllAttributes(commandMap);
		return "adm/hom/not/noticeInsertPage";
	}
	
	
	/**
	 * 홈페이지 > 공지사항 보기 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/not/noticeViewPage.do")
	public String viewPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		if(commandMap.get("p_seq") != null)
		{
			//조회수 업데이트 
			noticeAdminService.updateNoticeReadCount(commandMap);
			
			//보기 정보
			model.addAttribute("view", noticeAdminService.selectNoticeView(commandMap));
			
			//첨부파일 리스트 정보
			List fileList = noticeAdminService.selectBoardFileList(commandMap);
			
			model.addAttribute("fileList", fileList);
			
			
			model.addAllAttributes(commandMap);
			return "adm/hom/not/noticeViewPage";
		}
		else
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.select"));
			model.addAllAttributes(commandMap);
			return "forward:/adm/hom/not/selectNoticeList.do";
		}
	}
	
	
	/**
	 * 홈페이지 > 공지사항 등록/수정/삭제 작업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/not/noticeActionPage.do")
	public String actionPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/not/selectNoticeList.do";
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		

		//업로드 처리를 한다.
		List<Object> fileList = this.uploadFiles(request, commandMap);
		
		
		
		if(p_process.equalsIgnoreCase("insert"))
		{
			isok = noticeAdminService.insertNotice(commandMap, fileList);
		}
		else if(p_process.equalsIgnoreCase("update"))
		{
			isok = noticeAdminService.updateNotice(commandMap, fileList);
		}
		else if(p_process.equalsIgnoreCase("delete"))
		{
			isok = noticeAdminService.deleteNotice(commandMap);
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
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 18. 오후 5:15:44 - 최성민
	 * This Area Change Content Write
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/srs/srSystemActionPage.do")
	public String srSystemActionPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		String url = "/adm/hom/srs/srSystemSpecList.do";
		String p_process = (String)commandMap.get("p_process");
		
		boolean isok = false;
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");
		String empgubun = (String) session.getAttribute("emp_gubun");		
//		System.out.println(">>>>>>>userid : " + userid);

		commandMap.put("userid", userid);
		
		//System.out.println(">>>>>>>>>>>>commandMap : " + commandMap);

		//업로드 처리를 한다.
		List<Object> fileList = this.uploadFiles(request, commandMap);
		
		int replyCnt = 0;
		
		if(p_process.equalsIgnoreCase("insert")){
		
			isok = noticeAdminService.insertSrSystem(commandMap, fileList);			
		
		} else if(p_process.equalsIgnoreCase("update")) {
			
			isok = noticeAdminService.updateSrSystem(commandMap, fileList);			
		
		} else if(p_process.equalsIgnoreCase("delete")) {
			
			replyCnt = noticeAdminService.selectReplyCount(commandMap);
			
			//System.out.println(">>>>>>>replyCnt<<<<<<<<<<<<<< : " + replyCnt);
			
			if(replyCnt>0){
				isok = false;
			} else { 
				isok = noticeAdminService.deleteSrSystem(commandMap);				
			}
		} else if(p_process.equalsIgnoreCase("reinsert")) { //본글에 대한 답변

			//System.out.println(">>>>>>>reinsert commandMap<<<<<<< : " + commandMap);
			
			//System.out.println(">>>>>>>reinsert<<<<<<< : " + (String)commandMap.get("p_seq"));
			//System.out.println(">>>>>>>reinsert<<<<<<< : " + (String)commandMap.get("p_tabseq"));			
			
			int reSeq = 0;
			reSeq = noticeAdminService.selectSrSystemReSeq(commandMap);
			reSeq = reSeq+1;

			int srLevel = 1;
//			srLevel = noticeAdminService.selectSrSystemSrLevel(commandMap);
//			srLevel = srLevel+1;
			
			float srListSeqMin = 0;
			srListSeqMin = noticeAdminService.selectSrSystemSrListSeqMin(commandMap);
			
			//System.out.println(">>>>>>>srListSeqMin<<<<<<< : " + srListSeqMin);
			
			
			commandMap.put("reseq", reSeq);
			commandMap.put("srlevel", srLevel);			
			commandMap.put("ListSeqMin", srListSeqMin);			
			
			isok = noticeAdminService.insertSrSystemReply(commandMap, fileList);
		} else if(p_process.equalsIgnoreCase("reinsert2")) {  //답글에 답글시
			//System.out.println(">>>>>>>reinsert commandMap<<<<<<< : " + commandMap);
			
			//System.out.println(">>>>>>>reinsert<<<<<<< : " + (String)commandMap.get("p_seq"));
			//System.out.println(">>>>>>>reinsert<<<<<<< : " + (String)commandMap.get("p_tabseq"));			
			
			int reSeq = 0;
			reSeq = noticeAdminService.selectSrSystemReSeq(commandMap);
			commandMap.put("reSeq1",reSeq);
			reSeq = reSeq+1;

			int iPsrlevel = Integer.parseInt((String)commandMap.get("p_srlevel"));
			
			int srLevel = 0;
//			srLevel = noticeAdminService.selectSrSystemSrLevel(commandMap);
			srLevel = iPsrlevel+1;
			
			double srListSeqMinRe = 0;
			float srListSeqMinRe1 = 0;
			float srListSeqMinRe2 = 0;			
			
			commandMap.put("reSeq",reSeq);
			
			srListSeqMinRe = noticeAdminService.selectSrSystemSrListSeqMinRe1(commandMap);	//현재 List_seq와 이전List_seq의 합의 나누기 2
			//System.out.println("srListSeqMinRe : " + srListSeqMinRe);
//			srListSeqMinRe2 = noticeAdminService.selectSrSystemSrListSeqMinRe2(commandMap);			
			//srListSeqMinRe = srListSeqMinRe1 - 0.001;
			
			commandMap.put("reseq", reSeq);
			commandMap.put("srlevel", srLevel);			
			commandMap.put("ListSeqMin", srListSeqMinRe);			
			
			isok = noticeAdminService.insertSrSystemReply(commandMap, fileList);			
		} else if(p_process.equalsIgnoreCase("reupdate")) {
			isok = noticeAdminService.updateSrSystemReply(commandMap, fileList);
		} else if(p_process.equalsIgnoreCase("reupdate1")) {
			isok = noticeAdminService.updateSrSystemReply(commandMap, fileList);
		} else if(p_process.equalsIgnoreCase("redelete")) {

			isok = noticeAdminService.deleteSrSystemReply(commandMap);
			
		}
		
		int replyCnt1= 0;
		
		
		
		//System.out.println(">>>>>>>isok<<<<<<< : " + isok);
		
		if(isok){
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
		} else {
			replyCnt1 = noticeAdminService.selectReplyCount(commandMap);
			if(replyCnt1 > 0){
				resultMsg = egovMessageSource.getMessage("fail.common.replydelete");
			} else {
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
			}
		}
			
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 14. 오전 11:10:18 - 최성민
	 * This Area Change Content Write
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/srs/srSystemSpecList.do")
	public String srSystemSpecList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
//		System.out.println(">>>>>>>srSystemSpecList commandMap>>>>>>>>>>>>>> : " + commandMap);
		
		if( commandMap.get("p_tabseq") == null || commandMap.get("p_tabseq").toString().equals("") ){
			Map inputMap = new HashMap();
			inputMap.put("p_type", "SR");
			commandMap.put("p_tabseq", noticeAdminService.selectTableseq(inputMap));
		}
		
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");
		String empgubun = (String) session.getAttribute("emp_gubun");		
		
		commandMap.put("userid", userid);
		commandMap.put("pageUnit", "20");

		// 왜 이짓을 하셨나요? 1
		//List allList = noticeAdminService.srSystemSpecListAll(commandMap);
		//model.addAttribute("allList", allList);

		// srSystemSpecList
		// srSystemSpecListTotCnt 두 메소드에서 사용하는 쿼리에 답변여부를 확인할 수 있는 조건을 집어넣어야한다.
		int totCnt = noticeAdminService.srSystemSpecListTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);

		List list = noticeAdminService.srSystemSpecList(commandMap);

//		System.out.println(">>>>>>>>>>list : " + list.size());
		
		model.addAttribute("totCnt", totCnt);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "/adm/hom/srs/srSystemSpecList";
	}
	/**
	 * 홈페이지 > 공지사항 등록/수정 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/srs/srSystemSpecInsert.do")
	public String srSystemSpecInsert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{

		HttpSession session = request.getSession();
		
		
		String userid = (String) session.getAttribute("userid");
		String empgubun = (String) session.getAttribute("emp_gubun");		
//		System.out.println(">>>>>>>userid : " + userid);

		commandMap.put("userid", userid);
		
		//보기 정보
		model.addAttribute("view", noticeAdminService.srSystemSpecView(commandMap));

		
		//첨부파일 리스트 정보
		List fileList = noticeAdminService.selectBoardFileList(commandMap);
		
		model.addAttribute("fileList", fileList);
		
		model.addAllAttributes(commandMap);
		return "/adm/hom/srs/srSystemSpecInsert";
	}		

	/**
	 * 홈페이지 > 공지사항 보기 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/srs/srSystemSpecView.do")
	public String srSystemSpecView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		int cntCount = 0;
		int cntCount1 = 0;
		String userid = (String) session.getAttribute("userid");
		String empgubun = (String) session.getAttribute("emp_gubun");		
		
		boolean isok = false;
		
		commandMap.put("userid", userid);
		
		if(commandMap.get("p_seq") != null)
		{
			//조회수 업데이트 
			//noticeAdminService.updateNoticeReadCount(commandMap);
			
			//확인일자 있는지 여부 체크
			cntCount = noticeAdminService.selectConfirmDateCnt(commandMap);			

			if(cntCount==0){
				isok = noticeAdminService.confirmDateUpdate(commandMap);
			}
			
			//보기 정보
			//model.addAttribute("view", noticeAdminService.selectNoticeView(commandMap));
			model.addAttribute("view", noticeAdminService.srSystemSpecView(commandMap));			

			//System.out.println("commandMap : " + commandMap);
			
			model.addAttribute("viewlist", noticeAdminService.srSystemSpecViewList(commandMap));

			//System.out.println("aaa : " + noticeAdminService.srSystemSpecViewList(commandMap));
			
			//첨부파일 리스트 정보
			List fileList = noticeAdminService.selectBoardFileList(commandMap);
			
			model.addAttribute("fileList", fileList);
			
			
			model.addAllAttributes(commandMap);
			return "/adm/hom/srs/srSystemSpecView";
		}
		else
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.select"));
			model.addAllAttributes(commandMap);
			return "forward:/adm/hom/srs/srSystemSpecList.do";
		}
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:00:40 - 최성민
	 * This Area Change Content Write
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/srs/srSystemSpecViewReply.do")
	public String srSystemSpecViewReply(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		
		String userid = (String) session.getAttribute("userid");
		String empgubun = (String) session.getAttribute("emp_gubun");		

		commandMap.put("userid", userid);
		
		//System.out.println(">>>>>>>>>>>srSystemSpecViewReply commandMap<<<<<<<<<<<<<<<<  : " + commandMap);
		
		if(commandMap.get("p_seq") != null)
		{
			//조회수 업데이트 
//			noticeAdminService.updateNoticeReadCount(commandMap);
			
			//보기 정보
			//model.addAttribute("view", noticeAdminService.selectNoticeView(commandMap));
			model.addAttribute("view", noticeAdminService.srSystemSpecView(commandMap));			

			model.addAttribute("viewlist", noticeAdminService.srSystemSpecViewList(commandMap));
			
			
			//re_seq
			
			//첨부파일 리스트 정보
			List fileList = noticeAdminService.selectBoardFileList(commandMap);
			
			model.addAttribute("fileList", fileList);
			
			
			model.addAllAttributes(commandMap);
			return "/adm/hom/srs/srSystemSpecViewReply";
		}
		else
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.select"));
			model.addAllAttributes(commandMap);
			return "forward:/adm/hom/srs/srSystemSpecList.do";
		}
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 6. 27. 오전 11:39:30 - 최성민
	 * This Area Change Content Write
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/srs/srSystemSpecViewReplyRe.do")
	public String srSystemSpecViewReplyRe(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		
		String userid = (String) session.getAttribute("userid");
		String empgubun = (String) session.getAttribute("emp_gubun");		
		String replyinsert = "";		
		String p_reseq = "";		
		
		replyinsert = (String)commandMap.get("replyinsert");
		
		commandMap.put("userid", userid);
		
//		System.out.println(">>>>>>>>>>>srSystemSpecViewReply commandMap<<<<<<<<<<<<<<<<  : " + commandMap);
		
		if(commandMap.get("p_seq") != null)
		{
			//조회수 업데이트 
//			noticeAdminService.updateNoticeReadCount(commandMap);
			
			//보기 정보
			//model.addAttribute("view", noticeAdminService.selectNoticeView(commandMap));
			model.addAttribute("view", noticeAdminService.srSystemSpecView(commandMap));			

			model.addAttribute("viewlist", noticeAdminService.srSystemSpecViewList(commandMap));
			//System.out.println(">>>>>>>>>>>replyinsert1<<<<<<<<<<<<<<<<  : " + replyinsert);
			if("insert".equals(replyinsert)){
				//System.out.println(">>>>>>>>>>>replyinsert2<<<<<<<<<<<<<<<<  : " + replyinsert);
				commandMap.put("p_reseq", "99999999999");
			}
			model.addAttribute("viewlistRe", noticeAdminService.srSystemSpecViewListRe(commandMap));

			model.addAttribute("list_seq", (String)commandMap.get("list_seq"));
			
			//re_seq
			
			//첨부파일 리스트 정보
			List fileList = noticeAdminService.selectBoardFileList(commandMap);
			
			model.addAttribute("fileList", fileList);
			
			
			model.addAllAttributes(commandMap);
			return "/adm/hom/srs/srSystemSpecViewReplyRe";
		}
		else
		{
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.select"));
			model.addAllAttributes(commandMap);
			return "forward:/adm/hom/srs/srSystemSpecList.do";
		}
	}
	
	
	/**
	 * 업로드된 파일을 등록한다.
	 * 
	 * @param	contentPath	컨텐츠 경로
	 * @param	contentCode	컨텐츠 코드
	 * @return	Directory 생성 여부
	 * @throws Exception
	 */
	public List<Object> uploadFiles(HttpServletRequest request, Map<String, Object> commandMap) throws Exception {
		
		//기본 업로드 폴더
		String defaultDP = EgovProperties.getProperty("Globals.defaultDP");
		
		log.info("- 기본 업로드 폴더 : " + defaultDP);
		
		List<Object> list = new ArrayList<Object>();
		
		//저장경로 : dp\\bulletin
		
		//파일업로드를 실행한다.
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		
		java.util.Iterator<?> fileIter = mptRequest.getFileNames();
		 
		while (fileIter.hasNext()) {
			 MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
			 
			 if (mFile.getSize() > 0) {
				 Object fileHm = new HashMap();
				 fileHm = EgovFileMngUtil.uploadContentFile(mFile, defaultDP + File.separator + "bulletin");
				 list.add(fileHm);
			 }
		}
		
		return list;
	}

	/**
	 * 
	 * 설  명 : 시스템문의관리 엑셀다운로드
	 * @modify  2016. 8. 1. 오전 11:10:33 - 최성민
	 * This Area Change Content Write
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/hom/srs/srSystemSpecListExcelDown.do")	
	public String srSystemSpecListExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = noticeAdminService.srSystemSpecListExcelDown(commandMap);

		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);

		return "/adm/hom/srs/srSystemSpecListExcelDown";
	}
}
