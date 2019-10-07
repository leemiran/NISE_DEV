package egovframework.usr.myh.controller;

import java.util.ArrayList;
import java.util.Collection;
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

import egovframework.adm.fin.service.FinishManageService;
import egovframework.adm.stu.service.StuMemberService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.com.bod.service.BoardManageService;

import javax.crypto.*;
import org.apache.xerces.impl.dv.util.HexBin;

import com.ziaan.library.DesEncrypter;

import javax.crypto.spec.*;
import java.security.MessageDigest;


@Controller
public class MyClassController {

	/** log */
	protected static final Log log = LogFactory.getLog(MyClassController.class);
	
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	
    /** stuMemberService */
	@Resource(name = "stuMemberService")
    private StuMemberService stuMemberService;
	

	/** portalActionMainService */
	@Resource(name = "finishManageService")
	FinishManageService finishManageService;
	
	/** noticeAdminService */
	@Resource(name = "noticeAdminService")
	NoticeAdminService noticeAdminService;
	

/** boardManageService */
	@Resource(name = "boardManageService")
	BoardManageService boardManageService;
	

	/**
	 * 나의 교육이력 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/myStudyHisList.do")
	public String myStudyHisList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		//교육이력리스트
		List<?> list = stuMemberService.selectMyStudyHisList(commandMap);
		
		//과거이수내역리스트(2004-2007)
		List<?> oldList = finishManageService.selectUserFinishOldList(commandMap);
		
		
		model.addAttribute("list", list);
		model.addAttribute("oldList", oldList);
		
		model.addAllAttributes(commandMap);
		
		return "usr/myh/myStudyHisList";
	}
	
	
	/**
	 * 과거이수내역 수료증출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/oldSuRoyJeungPrint.do")
	public String oldSuRoyJeungPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", finishManageService.selectFinishOldView(commandMap));
		
		model.addAllAttributes(commandMap);
		return "adm/fin/oldSuRoyJeungPrint";
	}
	
	
	/**
	 * 나의 학습활동 
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/myActivityList.do")
	public String myActivityList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_subj = (String)commandMap.get("p_subj");
		String p_year = (String)commandMap.get("p_year");
		String p_subjseq = (String)commandMap.get("p_subjseq");
		String p_userid = (String)commandMap.get("userid");
		
		List<?> list = stuMemberService.selectMyActivityList(commandMap);
		model.addAttribute("list", list);	
		
		
		if(p_subj != null && p_year != null && p_subjseq != null && p_userid != null)
		{
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("p_subj", p_subj);
			hm.put("p_year", p_year);
			hm.put("p_subjseq", p_subjseq);
			hm.put("p_userid", p_userid);
			
			
			//학습별 통계
			model.addAttribute("lectview", stuMemberService.selectSatisLect(hm));

			//학습시간정보
			model.addAttribute("timeview", stuMemberService.selectSatisTime(hm));


			//최근학습일
			model.addAttribute("dateview", stuMemberService.selectSatisLastLecDate(hm));


			//강의실 접근정보
			model.addAttribute("roomview", stuMemberService.selectSatisLecRoom(hm));


			// 게시판 정보
			//model.addAttribute("boardview", stuMemberService.selectSatisBoard(hm));


			// 진도율
			model.addAttribute("progview", stuMemberService.selectSatisProgress(hm));

			// 출석기간
			model.addAttribute("termview", stuMemberService.selectSatisCheckTerm(hm));
			
			
			//출석부 날짜
			int levelDay = stuMemberService.selectLevelDay(hm);
			hm.put("levelDay", levelDay);
			
			// 출석부 [type : List]
			List<?> checklist = stuMemberService.selectSatisCheckList(hm);
			model.addAttribute("checklist", checklist);
			
			
		}
		
		
		model.addAllAttributes(commandMap);
		
		return "usr/myh/myActivityList";
	}
	
	
	/**
	 * 연수행정실 상세보기 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/celOfficeDetailList.do")
	public String celOfficeDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		//과정정보
		model.addAttribute("view", stuMemberService.selectMyStudyHisDetailView(commandMap));	
		//결제정보
		model.addAttribute("payView", stuMemberService.selectMyStudyHisPayCdView(commandMap));
		
		model.addAllAttributes(commandMap);
		
		return "usr/myh/celOfficeDetailList";
	}
	
	
	/**
	 * 연수행정실 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/celOfficeList.do")
	public String celOfficeList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectMyStudyHisList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "usr/myh/celOfficeList";
	}
	
	
	/**
	 * 연수수강 내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/celOfficeList01.do")
	public String celOfficeList01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectMyStudyHisList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "usr/myh/celOfficeList01";
	}
	
	
	/**
	 * 출석고사 내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/celOfficeList02.do")
	public String celOfficeList02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectMyStudyHisList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "usr/myh/celOfficeList02";
	}
	
	
	/**
	 * 연수결제 내역
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/celOfficeList03.do")
	public String celOfficeList3(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectMyStudyHisList(commandMap);
		
		List<EgovMap> list2 = new ArrayList<EgovMap>();
		
		
		for(int i = 0; i<list.size(); i++)
		{
				//결제정보
				EgovMap mm = (EgovMap)list.get(i);
		
				
				String v_transactionid = "";
				//out.print(" <br/> v_transactionid : " + v_transactionid);
				//v_transactionid = "belie2010042613202427493";
				
				if(mm != null)
				{
					v_transactionid = (String)mm.get("transactionId");
				}
				log.info("v_transactionid : " + v_transactionid);
				
				String mid = "believe2024";     // 상점아이디
				String tid = v_transactionid;     // 거래번호   PA_PAYMENT 테이블의 TRANSACTION_ID 값을 넣어주어야 함
				String mertKey = "a126743d464c0e9bdcfa3f8066054106";     //데이콤에서 상점아이디별 발급 키값
		
		
				byte[] key = null;
				DESKeySpec spec = null;
				SecretKeyFactory factory = null;
				SecretKey secret = null;
		
			////////////////////////////// 전송필드 암화화(DES) //////////////////////////////
				key = HexBin.decode(mertKey);
				spec = new DESKeySpec(key);
				factory = SecretKeyFactory.getInstance("DES");
				secret = factory.generateSecret(spec);
				
				DesEncrypter encrypter		= new DesEncrypter(secret);
			////////////////////////////// 전송필드 암화화(DES) //////////////////////////////
		
		
			//////////////////////////////md5///////////////////////////////////////////////
			    StringBuffer sb = new StringBuffer();
			    
			    sb.append(mid);
			    sb.append(tid);
				sb.append(mertKey);
				//sb.append("00");
		
			    byte[] bNoti = sb.toString().getBytes();
		
			    MessageDigest md = MessageDigest.getInstance("MD5");
			    byte[] digest = md.digest(bNoti);
		
			    StringBuffer strBuf = new StringBuffer();
		
			    for (int i5=0 ; i5 < digest.length ; i5++) {
			        int c = digest[i5] & 0xff;
			        if (c <= 15){
			            strBuf.append("0");
			        }
			        strBuf.append(Integer.toHexString(c));
			    }
		
			    String authdata = strBuf.toString();
			    
			    mm.put("authdata", authdata);
			    log.info("authdata : " + authdata);
				
			    list2.add(mm);
		}
	    
		
		model.addAttribute("list", list2);	
		model.addAllAttributes(commandMap);
		
		return "usr/myh/celOfficeList03";
	}
	
	/**
	 * 연수지명번호 입력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/userService01.do")
	public String userService01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectMyStudyHisList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "usr/svc/userService01";
	}
	
	
	/**
	 * 출석고사장 입력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/userService02.do")
	public String userService02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		commandMap.put("testPrint", "Y");
		List<?> list = stuMemberService.selectMyStudyHisList(commandMap);		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "usr/svc/userService02";
	}
	
	
	/**
	 * 이수증출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/userService03.do")
	public String userService03(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectMyStudyHisList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "usr/svc/userService03";
	}
	
	
	
	/**
	 * 영수증출력 입력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/userService04.do")
	public String userService04(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectMyStudyHisList(commandMap);
		
		List<EgovMap> list2 = new ArrayList<EgovMap>();
		
		
		for(int i = 0; i<list.size(); i++)
		{
				//결제정보
				EgovMap mm = (EgovMap)list.get(i);
		
				
				String v_transactionid = "";
				//out.print(" <br/> v_transactionid : " + v_transactionid);
				//v_transactionid = "belie2010042613202427493";
				
				if(mm != null)
				{
					v_transactionid = (String)mm.get("transactionId");
				}
				log.info("v_transactionid : " + v_transactionid);
				
				String mid = "believe2024";     // 상점아이디
				String tid = v_transactionid;     // 거래번호   PA_PAYMENT 테이블의 TRANSACTION_ID 값을 넣어주어야 함
				String mertKey = "a126743d464c0e9bdcfa3f8066054106";     //데이콤에서 상점아이디별 발급 키값
		
		
				byte[] key = null;
				DESKeySpec spec = null;
				SecretKeyFactory factory = null;
				SecretKey secret = null;
		
			////////////////////////////// 전송필드 암화화(DES) //////////////////////////////
				key = HexBin.decode(mertKey);
				spec = new DESKeySpec(key);
				factory = SecretKeyFactory.getInstance("DES");
				secret = factory.generateSecret(spec);
				
				DesEncrypter encrypter		= new DesEncrypter(secret);
			////////////////////////////// 전송필드 암화화(DES) //////////////////////////////
		
		
			//////////////////////////////md5///////////////////////////////////////////////
			    StringBuffer sb = new StringBuffer();
			    
			    sb.append(mid);
			    sb.append(tid);
				sb.append(mertKey);
				//sb.append("00");
		
			    byte[] bNoti = sb.toString().getBytes();
		
			    MessageDigest md = MessageDigest.getInstance("MD5");
			    byte[] digest = md.digest(bNoti);
		
			    StringBuffer strBuf = new StringBuffer();
		
			    for (int i5=0 ; i5 < digest.length ; i5++) {
			        int c = digest[i5] & 0xff;
			        if (c <= 15){
			            strBuf.append("0");
			        }
			        strBuf.append(Integer.toHexString(c));
			    }
		
			    String authdata = strBuf.toString();
			    
			    mm.put("authdata", authdata);
			    log.info("authdata : " + authdata);
				
			    list2.add(mm);
		}
	    
		
		model.addAttribute("list", list2);	
		model.addAllAttributes(commandMap);
		
		
		return "usr/svc/userService04";
	}
	
	
	
	
	
	
	
	
	/**
	 * 설문 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/sulmumUserList.do")
	public String sulmumUserList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectMySulmunList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "usr/myh/sulmunUserList";
	}
	
	
	/**
	 * 수료증프린트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/suRoyJeungPrint.do")
	public String suRoyJeungPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		HttpSession session = request.getSession();
		String p_emp_gubun = (String)session.getAttribute("emp_gubun");
		commandMap.put("p_emp_gubun", p_emp_gubun);
		
		//System.out.println("p_emp_gubun -------> "+p_emp_gubun);
		
		List list = finishManageService.suRoyJeungPrintList(commandMap);
		model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
		
		String return_page = "adm/fin/suRoyJeungPrint";
		if( Integer.valueOf((String)commandMap.get("p_year")) > 2013 ){
			//return_page = "adm/fin/suRoyJeungPrint_New";
			return_page = "adm/fin/suRoyJeungPrintNonActive";
		}
		
		return return_page;
	}
	
	
	/**
	 * 연수행정실 확인증 출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/selectUserReqPrint.do")
	public String selectUserReqPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", stuMemberService.selectUserReqPrint(commandMap));
		
		
		model.addAllAttributes(commandMap);
		//return "usr/myh/selectUserReqPrint";
		return "usr/myh/selectUserReqPrintNonActive";
	}
	
	/**
	 * 연수행정실 영수증 출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/selectUserCashPrint.do")
	public String selectUserCashPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAttribute("view", stuMemberService.selectUserCashPrint(commandMap));
		
		
		model.addAllAttributes(commandMap);
		//return "usr/myh/selectUserCashPrint";
		return "usr/myh/selectUserCashPrintNonActive";
	}
	
	
	/**
	 * 연수행정실 연수지명번호 및 출석고사장 업데이트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/celOfficeDetailAction.do")
	public String pageEtcAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/usr/myh/celOfficeDetailList.do";
		String p_redirect_url = commandMap.get("p_redirect_url") + "";
		
		boolean isok = stuMemberService.updateLecselAttendNumber(commandMap);
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common.save");
			
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.save");
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:" + p_redirect_url;
	}
	
	/**
	 * 나의 강의실 > 나의 질문방 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/myCursQnaList.do")
	public String myCursQnaList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//int totCnt = stuMemberService.selectMyCursQnaTotCnt(commandMap);
		int totCnt = stuMemberService.selectMyCursQnaQnaTotCnt(commandMap);
        pagingManageController.PagingManage(commandMap, model, totCnt);
        
        //나의 문의함
		//List list = stuMemberService.selectMyCursQnaList(commandMap);
        List list = stuMemberService.selectMyCursQnaQnaList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/myh/myCursQnaList";
	}
	
	
	
	/**
	 * 나의 강의실 > 나의 질문방 보기화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/myh/myCursQnaView.do")
	public String myCursQnaView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userid");
		
		String p_kind = commandMap.get("p_kind").toString();
		if(p_kind.equals("1")){
			Map view = stuMemberService.selectMyCursQnaView(commandMap);
			if(userId.equals(view.get("userid"))){
				model.addAttribute("view", view);
				boardManageService.updateBoardViewCount(commandMap);
			}else{
				model.addAllAttributes(commandMap);
				return "forward:/usr/hom/portalActionMainPage.do";
			}			
		}else{
			//model.addAttribute("view", stuMemberService.selectMyCursQnaQnaView(commandMap));
			Map view = noticeAdminService.selectQnaView(commandMap);
			if(userId.equals(view.get("userid"))){
				model.addAttribute("view", view);
				//조회수 업데이트
				noticeAdminService.updateQnaCount(commandMap);						
			}else{
				model.addAllAttributes(commandMap);
				return "forward:/usr/hom/portalActionMainPage.do";
			}			
		}
		
		List list = stuMemberService.selectMyCursQnaFileList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "usr/myh/myCursQnaView";
	}
	
	
	/**
	 * 수험표출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/testIdentificationPrint.do")
	public String testIdentificationPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		log.info("p_subj  ------> "+commandMap.get("p_subj"));
		log.info("p_year  ------> "+commandMap.get("p_year"));
		log.info("p_subjseq  ------> "+commandMap.get("p_subjseq"));
		log.info("p_userid  ------> "+commandMap.get("p_userid"));
			
		Map view = stuMemberService.selecttestIdentificationView(commandMap);
		
		model.addAttribute("view", view);
		model.addAllAttributes(commandMap);
		
		return "usr/svc/testIdentificationPrint";
	}
	
	/**
	 * 인가서출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/svc/userService05.do")
	public String userService05(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
			
		model.addAllAttributes(commandMap);		
		return "usr/svc/userService05";
	}
	
	
}
