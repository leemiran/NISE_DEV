package egovframework.usr.subj.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.cfg.mem.service.MemberSearchService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.svt.lifetime.LifetimeService;
import egovframework.svt.usr.subj.UserSubjService;
import egovframework.svt.valid.ValidService;
import egovframework.usr.subj.dao.UserSubjectDAO;
import egovframework.usr.subj.service.UserSubjectService;

@Controller
public class SubjInfoController {

	/** log */
	protected static final Log log = LogFactory.getLog(SubjInfoController.class);
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
    /** userSubjectService */
	@Resource(name = "userSubjectService")
    private UserSubjectService userSubjectService;
	
	
	/** MemberSearchService */
	@Resource(name = "memberSearchService")
	MemberSearchService memberSearchService;
	
	
	@Autowired
	ValidService validService;
	
	@Autowired
	UserSubjService userSubjService;
	
	@Autowired
	LifetimeService lifetimeService;
	
	@Resource(name="userSubjectDAO")
    private UserSubjectDAO userSubjectDAO;
	
	/**
	 * 연수과정/안내 및 신청 > 신청 가능한 과정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/usr/subj/subjInfoList.do")
	public String subjInfoList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{

		//과정의 메뉴코드를 강제로 넣어 준다.
		commandMap.put("menu_main", "2");
		commandMap.put("menu_sub", "1");
		commandMap.put("menu_tab_title", "연수신청");
		commandMap.put("menu_sub_title", "신청 가능한 과정");

		//과정리스트 
		List<?> list = userSubjectService.selectUserSubjectList(commandMap);
		model.addAttribute("list", list);

		model.addAllAttributes(commandMap);
		return "usr/subj/subjInfoList";
	}*/

	@RequestMapping(value="/usr/subj/subjInfoListPopup.do")
	public String subjInfoListPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		return "usr/subj/subjInfoListPopup";
	}
	
	/**
	 * 연수과정/안내 및 신청 > 개설교육과정검색 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/usr/subj/subjInfoSearchList.do")
	public String subjInfoSearchList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//과정의 메뉴코드를 강제로 넣어 준다.
		commandMap.put("menu_main", "2");
		commandMap.put("menu_sub", "2");
		commandMap.put("menu_tab_title", "연수신청");
		commandMap.put("menu_sub_title", "개설교육과정검색");
		//전체 과정 개설 검색일 경우는 search_knise_subj_module 값을 넣어서 현재 년도의 데이터를 모두 가져온다.
		commandMap.put("search_knise_subj_module", "allSearch");
		
		
		//과정리스트 
		List<?> list = userSubjectService.selectUserSubjectList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/subj/subjInfoSearchList";
	}*/
	
	
	
	/**
	 * 연수과정/안내 및 신청 > 개설교육과정검색/신청 보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/subjInfoView.do")
	public String subjInfoView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String search_knise_subj_module = commandMap.get("search_knise_subj_module") + "";
		
		if(commandMap.get("p_target") == null){
			commandMap.put("p_target", "");
		}
		
		if(commandMap.get("p_target").equals("view")){
			commandMap.put("menu_main", "2");
			commandMap.put("menu_sub", "1");
			commandMap.put("menu_tab_title", "연수신청");
			commandMap.put("menu_sub_title", "신청 가능한 연수");
		}
		
		commandMap.remove("p_target");
		
		//개설과정 검색이라면
		if(search_knise_subj_module != null && search_knise_subj_module.equals("allSearch"))
		{
			//과정의 메뉴코드를 강제로 넣어 준다.
			commandMap.put("menu_main", "2");
			commandMap.put("menu_sub", "1");
			commandMap.put("menu_tab_title", "연수신청");
			commandMap.put("menu_sub_title", "신청 가능한 연수");
		}
		
		
		//과정정보
		model.addAttribute("view", userSubjectService.selectUserSubjectView(commandMap));
		
		//기수리스트 
		List<?> subjSeqList = userSubjectService.selectUserSubjectSeqList(commandMap);
		model.addAttribute("subjSeqList", subjSeqList);
		
		//교육후기개수
		int totCnt = userSubjectService.selectUserStoldCommentTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
        
		//교육후기리스트 
		List<?> cmmList = userSubjectService.selectUserStoldCommentList(commandMap);
		model.addAttribute("cmmList", cmmList);
		
		model.addAllAttributes(commandMap);
		return "usr/subj/subjInfoView";
	}
	
	/**
	 * 연수과정/안내 및 신청 > 개설교육과정검색/신청 보기 팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/subjInfoViewPopUp.do")
	public String subjInfoViewPopUp(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//과정정보
		model.addAttribute("view", userSubjectService.selectUserSubjectView(commandMap));
		
		//기수리스트 
		List<?> subjSeqList = userSubjectService.selectUserSubjectSeqList(commandMap);
		model.addAttribute("subjSeqList", subjSeqList);
		
		
		//교육후기개수
		int totCnt = userSubjectService.selectUserStoldCommentTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
        
		//교육후기리스트 
		List<?> cmmList = userSubjectService.selectUserStoldCommentList(commandMap);
		model.addAttribute("cmmList", cmmList);
		
		model.addAllAttributes(commandMap);
		return "usr/subj/subjInfoViewPopUp";
	}
	
	
	
	/**
	 * 수강신청 시작 화면 -- 수강제약체크하기 Step01
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/subjProposeStep01.do")
	public String subjProposeStep01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String validResult = validService.validSugangRequest(commandMap);
		model.addAttribute("validResult", validResult);
		
		String resultMsg = "";
		String returnUrl = "usr/subj/subjProposeStep01";
		//제약정보
		model.addAttribute("view", userSubjectService.selectUserSubjProposeCheck(commandMap));
		
		//과정-기수정보
		List subjinfo = (List) userSubjectService.selectUserProposeSubjInfo(commandMap);
		model.addAttribute("subjinfo", subjinfo);
		
		//회원정보
		HashMap userMap = new HashMap();
		userMap.put("p_userid", commandMap.get("userid"));
		
		Map userInfo = memberSearchService.selectMemberView(userMap);
		
		String p_empGubun = (String)userInfo.get("empGubun");
		String p_nicePersonalNum = (String)userInfo.get("nicePersonalNum");
		String p_niceAllowYn= (String)userInfo.get("niceNumAllowYn");
		String p_jobCd= userInfo.get("jobCd") !=null ? (String)userInfo.get("jobCd") : "";
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ p_empGubun  "+p_empGubun);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ p_nicePersonalNum  "+p_nicePersonalNum);
		if(p_nicePersonalNum == null){
			p_nicePersonalNum = "";
		}
		
		if(p_empGubun.equals("T") && p_nicePersonalNum.equals("") && p_niceAllowYn.equals("N") && !p_jobCd.equals("00039")){
			//returnUrl = "forward:/usr/mpg/memMyPage.do";
			
			//메뉴코드값 강제 등록
			commandMap.put("menu_main", "6");
			commandMap.put("menu_sub", "3");
			commandMap.put("menu_tab_title", "마이페이지");
			commandMap.put("menu_sub_title", "개인정보수정");
			
			resultMsg = "수강신청을 하시려면 나이스 개인번호를 입력하셔야 합니다.\\n개인정보 수정으로 이동합니다.";
			model.addAttribute("resultMsg", resultMsg);
		}
		
		model.addAttribute("meminfo", userInfo);
		
		model.addAllAttributes(commandMap);
		return returnUrl;
	}
	
	
	/**
	 * 수강신청 시작 화면 -- 수강제약체크하기 Step02
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/subjProposeStep02.do")
	public String subjProposeStep02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		//과정-기수정보
		model.addAttribute("subjinfo", userSubjectService.selectUserProposeSubjInfo(commandMap));
		
		String v_usebook_yn  = commandMap.get("v_usebook_yn")+"";
		//변경된 주소 업데이트
		if(v_usebook_yn.equals("Y")){
			memberSearchService.updateMemberAddress(commandMap);
		}

		//회원정보
		HashMap userMap = new HashMap();
		userMap.put("p_userid", commandMap.get("userid"));
		model.addAttribute("meminfo", memberSearchService.selectMemberView(userMap));
		
		model.addAllAttributes(commandMap);
		return "usr/subj/subjProposeStep02";
	}
	
	
	/**
	 * 수강신청 시작 화면 -- 수강제약체크하기 Step03
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/subjProposeStep03.do")
	public String subjProposeStep03(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		//결재 아이디 order_id를 생성하여 가져온다.
		model.addAttribute("orderinfo", userSubjectService.selectUserProposeGetOrderId(commandMap));
		
		//과정-기수정보
		model.addAttribute("subjinfo", userSubjectService.selectUserProposeSubjInfo(commandMap));		
		
		//회원정보
		HashMap userMap = new HashMap();
		userMap.put("p_userid", commandMap.get("userid"));
		model.addAttribute("meminfo", memberSearchService.selectMemberView(userMap));

		model.addAllAttributes(commandMap);
		return "usr/subj/subjProposeStep03";
	}
	
	
	/**
	 * (무통장/일괄납부)수강신청 완료
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/subjProposeStepAction.do")
	public String subjProposeStepAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		String resultMsg = "";
		
		// 수강신청시 수강인원 체크
		if(null == userSubjService.getSubjStudentLimit(commandMap)) {
			model.addAttribute("resultCode", "OVER");
			model.addAttribute("resultMsg", "정원이 초과되었습니다.");
			return "jsonView";
		};
		
		// 무료수강 변경으로 1단계에서 바로 수강신청. 수강신청 시 주문 아이디생성(원래 로직은 수강신청 3단계에서 생성)
		commandMap.put("p_order_id", userSubjectService.selectUserProposeGetOrderId(commandMap).get("orderId"));
		
		boolean isok = userSubjectService.insertUserProposeOB(commandMap);
		
		if (isok)
		{
			// 평생계좌제 연동 여부
			try {
				Map<String, Object> resultMap = lifetimeService.join(request);
				model.addAllAttributes(resultMap);
			} catch (Exception e) {
			}
			resultMsg = egovMessageSource.getMessage("success.common.save");
		}
		else
		{
			resultMsg = egovMessageSource.getMessage("fail.common.save");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "jsonView";
	}
	
	/**
	 * (무통장/일괄납부)수강신청 취소
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/subj/subjProposeCancelAction.do")
	public String subjProposeCancel(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		// 삭제에 필요한 파라메터를 받고
		// 
		String url = "/usr/subj/subjInfoList.do";
		if(null != commandMap.get("fromPage")) {
			url = "/usr/stu/cou/courseStudyList.do";
		}
		String resultMsg = "";
		
		// 패키지 확인
		HashMap<String, Object> subjMap = new HashMap<String, Object>();
		subjMap.put("p_subj", commandMap.get("p_subj"));
		subjMap.put("p_year", commandMap.get("p_year"));
		subjMap.put("p_subjseq", commandMap.get("p_subjseq"));
		List<Map<String, Object>> subjinfo = (List<Map<String, Object>>) userSubjectDAO.selectUserProposeSubjInfo(subjMap);
		
		int deleteCnt = 0;
		for(Map<String, Object> subj: subjinfo) {
			subjMap = new HashMap<String, Object>();
			subjMap.put("p_subj", subj.get("subj"));
			subjMap.put("p_year", subj.get("year"));
			subjMap.put("p_subjseq", subj.get("subjseq"));
			subjMap.put("userid", commandMap.get("userid"));
			boolean isok = userSubjectService.deleteUserPropose(subjMap);
			if(isok) deleteCnt ++;
		}
		boolean isok = subjinfo.size() == deleteCnt;
		if (isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.delete");
		}
		else
		{
			resultMsg = egovMessageSource.getMessage("fail.common.delete");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
}
