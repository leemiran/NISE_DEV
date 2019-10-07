package egovframework.usr.stu.cou.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.stu.service.StuMemberService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.usr.stu.cou.service.CourseStudyService;

@Controller
public class CourseStudyController {

	/** log */
	protected static final Log log = LogFactory.getLog(CourseStudyController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "courseStudyService")
	CourseStudyService courseStudyService;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	@Autowired
	private StuMemberService stuMemberService;
	
	@RequestMapping(value="/usr/stu/cou/courseStudyList.do")
	public String listPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//학습공지사항 리스트
//		int totCnt = courseStudyService.selectListSubjGongTotCnt(commandMap);
//		pagingManageController.PagingManage(commandMap, model, totCnt);
        
        //String returnUrl = "usr/stu/cou/courseStudyList";
		String returnUrl = "svt/usr/myStudy/studyInfo";
        String resultMsg = "";
        HttpSession session = request.getSession();
        
        if(null == session.getAttribute("userid")) {
			model.addAllAttributes(commandMap);
			return returnUrl;
		}
        	
        	//나이스 개인번호 가져오기
        	String nicePersonalNum = courseStudyService.selectnicePersonalNum(commandMap);
        	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    "+nicePersonalNum);
        	
        	if(commandMap.get("emp_gubun") == null){
        		commandMap.put("emp_gubun", "");
        	}
	       
//	        if(commandMap.get("emp_gubun").equals("T") && nicePersonalNum == null && commandMap.get("s_niceNumAllowYn").equals("N")){
//	        	
//	        	returnUrl = "forward:/usr/mpg/memMyPage.do";
//				
//				//메뉴코드값 강제 등록
//				commandMap.put("menu_main", "6");
//				commandMap.put("menu_sub", "3");
//				commandMap.put("menu_tab_title", "마이페이지");
//				commandMap.put("menu_sub_title", "개인정보수정");
//				
//				resultMsg = "학습을 하시려면 나이스 개인번호를 입력하셔야 합니다.\\n개인정보 수정으로 이동합니다.";
//				model.addAttribute("resultMsg", resultMsg);
//	        }else{
	        	//학습진행목록 리스트
	        	List ingList = courseStudyService.selectEducationSubjectList(commandMap);
	        	model.addAttribute("ingList", ingList);
	        	
	        	//학습대기목록
	        	List waitList = courseStudyService.selectEducationSubjectDList(commandMap);
	        	model.addAttribute("waitList", waitList);
	        	
	        	List<Object> noticeSubjList = new ArrayList<Object>();
	        	Map<String, Object> noticeMap = new HashMap<String, Object>();
	        	
	        	// 공지사항 과목 제한 수
	        	int noticeLimit = 5;
	        	// 진행목록 공지사항 갯수
	        	
	        	int ingLimit = 0;
	        	if(ingList.size() > noticeLimit) {
	        		ingLimit = noticeLimit;
	        	} else {
	        		ingLimit = ingList.size();
	        	}
	        	
	        	// 대기목록 공지사항 갯수
	        	int waitLimit = 0;
	        	if(waitList.size() > noticeLimit - ingList.size()) {
	        		waitLimit = noticeLimit - ingList.size();
	        	} else {
	        		waitLimit = waitList.size();
	        	}
	        			
	        	for(int i = 0; i < ingLimit; i++) {
	        		Map<String, Object> subject = (Map<String, Object>) ingList.get(i);
	        		commandMap.put("subj", subject.get("subj"));
	        		commandMap.put("year", subject.get("year"));
	        		commandMap.put("subjseq", subject.get("subjseq"));
	        		commandMap.put("firstIndex", 0);
	        		commandMap.put("recordCountPerPage", 7);
	        		
	        		noticeSubjList.add(subject.get("subjnm"));
	        		noticeMap.put(String.valueOf(i), courseStudyService.selectListSubjGongPageList(commandMap));
	        	}
	        	
	        	for(int i = 0; i < waitLimit; i++) {
	        		Map<String, Object> subject = (Map<String, Object>) waitList.get(i);
	        		commandMap.put("subj", subject.get("subj"));
	        		commandMap.put("year", subject.get("year"));
	        		commandMap.put("subjseq", subject.get("subjseq"));
	        		commandMap.put("firstIndex", 0);
	        		commandMap.put("recordCountPerPage", 7);
	        		
	        		noticeSubjList.add(subject.get("subjnm"));
	        		noticeMap.put(String.valueOf(ingLimit + i), courseStudyService.selectListSubjGongPageList(commandMap));
	        	}
	        	model.addAttribute("noticeSubjList", noticeSubjList);
	        	model.addAttribute("noticeMap", noticeMap);
				
				//완료,복습
				List reviewList = courseStudyService.selectGraduationSubjectList(commandMap);
				model.addAttribute("reviewList", reviewList);
				
				int qnaCnt = stuMemberService.selectMyCursQnaTotCnt(commandMap);
				model.addAttribute("qnaCnt", qnaCnt);
//	        }
	        
		model.addAllAttributes(commandMap);
		return returnUrl;
	}
	
	/**
	 * 수강신청한과정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/cou/courseProposeList.do")
	public String ProposeListPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		List list = courseStudyService.selectProposeSubjectList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/stu/cou/courseProposeList";
	}
	
	/**
	 * 복습/교육후기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/cou/courseStoldList.do")
	public String StoldLisPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//수강완료한 과정 목록
		List list = courseStudyService.selectGraduationSubjectList(commandMap);
		model.addAttribute("list", list);
		
		//수강완료한교육년도조회
		List yearList = courseStudyService.selectGraduationYearList(commandMap);
		model.addAttribute("yearList", yearList);
		
		model.addAllAttributes(commandMap);
		return "usr/stu/cou/courseStoldList";
	}
	
	
	/**
	 * 교육후기Pop
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/whenSubjComments.do")
	public String whenSubjComments(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		int totCnt = courseStudyService.selectstoldCommentList2TotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
        //교육후기 목록조회
        List list = courseStudyService.selectstoldCommentList2(commandMap);
        model.addAttribute("list", list);
        
		model.addAllAttributes(commandMap);
		return "usr/stu/cou/whenSubjCommentsPop";
	}
	
	/**
	 * 교육후기 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/stu/whenSubjCommentsInsert.do")
	public String whenSubjCommentsInsert(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		boolean isok = courseStudyService.whenSubjCommentsInsert(commandMap);
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.insert");
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		
		return "forward:/usr/stu/whenSubjComments.do";
	}
	
	
		/**
		 * 교육후기 삭제
		 * @param request
		 * @param response
		 * @param commandMap
		 * @param model
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value="/usr/stu/whenSubjCommentsDelete.do")
		public String whenSubjCommentsDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
			
			String resultMsg = "";
			
			boolean isok = courseStudyService.whenSubjCommentsDelete(commandMap);
			
			if(isok)
			{
				resultMsg = egovMessageSource.getMessage("success.common.delete");
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.delete");
			}
			
			model.addAttribute("resultMsg", resultMsg);
			model.addAllAttributes(commandMap);
			
			return "forward:/usr/stu/whenSubjComments.do";
		}
	
}
