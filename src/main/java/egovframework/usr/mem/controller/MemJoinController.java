package egovframework.usr.mem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.epki.api.EpkiApi;
import com.epki.cert.X509Certificate;
import com.epki.cms.EnvelopedData;
import com.epki.conf.ServerConf;
import com.epki.crypto.PrivateKey;
import com.epki.exception.EpkiException;
import com.epki.util.Base64;

import egovframework.adm.cfg.mem.service.MemberSearchService;
import egovframework.adm.com.cmp.service.SelectCompanyService;
import egovframework.adm.sms.SMSSenderDAO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.svt.cert.EpkiService;
import egovframework.usr.lgn.service.LoginService;
import egovframework.svt.cert.EpkiHtml5Service;


@Controller
public class MemJoinController {

	/** log */
	protected static final Log log = LogFactory.getLog(MemJoinController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
    @Resource(name = "pagingManageController")
    private PagingManageController pagingManageController;
	
	/** MemberSearchService */
	@Resource(name = "memberSearchService")
	MemberSearchService memberSearchService;
	
	/** SelectCompanyService */
	@Resource(name = "selectCompanyService")
	SelectCompanyService selectCompanyService;
	
	/** loginService */
	@Resource(name = "loginService")
	LoginService loginService;
	
	
	/** SMSSender */
	@Resource(name = "SMSSenderDAO")
	SMSSenderDAO SMSSenderDAO;
	
	@Resource(name = "propertiesService")
    protected EgovPropertyService  propertyService;
	
	@Autowired
	EpkiService epkiService;
	
	@Autowired
	EpkiHtml5Service epkiHtml5Service;
	
	
	/**
	 * 회원가입 > 약관동의
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/memJoinStep01.do")
	public String memJoinStep01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{

		
		model.addAllAttributes(commandMap);
		return "usr/mem/memJoinStep01";
	}
	
	

	/**
	 * 회원가입 > 성명, 주민번호 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/usr/mem/memJoinStep02.do")
	public String memJoinStep02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String url =  "usr/mem/memJoinStep02";
		
		commandMap.put("menu_main", "7");
		commandMap.put("menu_sub", "1");
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "회원가입");
		
		
		model.addAllAttributes(commandMap);
		
		// epki 인증 세팅
		model.addAttribute("strServerCert", epkiService.getServerCert());
		model.addAttribute("sessionID", request.getSession().getId());
		return url;
	}*/
	@RequestMapping(value="/usr/mem/memJoinStep02_1.do")
	public String memJoinStep02_1(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
	
		System.out.println(">>>>>>>>>>>strRequestData<<<<<<<<<<< : " + (String)commandMap.get("strRequestData"));
		
		String url =  "usr/mem/memJoinStep02_1";
		
		commandMap.put("menu_main", "7");
		commandMap.put("menu_sub", "1");
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "회원가입");
		
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	/**
	 * 회원가입 > 가입여부 확인 및 가입프로세스
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/memJoinStep03.do", method=RequestMethod.POST)
	public String memJoinStep03(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		commandMap.put("menu_main", "7");
		commandMap.put("menu_sub", "1");
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "회원가입");
		
		String p_name = "";
		String birthDate = "";
		String sex = "";
		String virtualNo = "";
		
		HttpSession session = request.getSession();
		
		// 교사자격등급 셀렉트박스 추가
		model.addAttribute("list", memberSearchService.selectSubjectList(commandMap));
		
		// 인증수단 3가지
		// S: 본인인증
		// G: IPIN 인증
		// E: EPKI 인증서
		String certgubun = (String)commandMap.get("certgubun");
		System.out.println("certgubun ----> "+certgubun);
						
		if("E".equals(certgubun)) {
			if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {		
			    
				//예) cn=850홍길동001,ou=people,ou= 교육부 ,o=Government of Korea,c=KR
				//예) cn=851김철수001,ou=people,ou= 서울특별시교육청 ,o=Government of Korea,c=KR
				//예) cn=김순이()000123948324123123,ou=IBK,ou=personal4IB,o=yessign,c=kr
				//예) cn=박철수(sadfLfdpsaf)000401239324233,ou=KMB,ou=personal4IB,o=yessign,c=kr
				//예) cn=최철수(HONG SOON BOON)00023454345435435,ou=KMB,ou=personal4IB,o=yessign,c=kr				
				//String dn ="cn=최철수(HONG SOON BOON)00023454345435435,ou=KMB,ou=personal4IB,o=yessign,c=kr";
				
				//EPKI DN
				epkiHtml5Service.getHtml5Dn(request, model, commandMap);
				
				String dn = commandMap.get("dn") !=null ? commandMap.get("dn").toString() : ",";
		        String[] dnArr_4 = dn.split(",") ;
		        String p_dn = dnArr_4[0];
		        System.out.println("p_dn 111 ----> "+p_dn);
		        p_dn = p_dn.replaceAll("[0-9]", ""); 
		        System.out.println("p_dn 222 ----> "+p_dn);
		        p_dn = p_dn.replaceAll("cn=", ""); 
		        System.out.println("p_dn 333 ----> "+p_dn);
		        if(p_dn.contains("(")){
		        	String[] dnArr2_4 = p_dn.split("\\(") ;
			        p_dn = dnArr2_4[0];
			        System.out.println("p_dn \\( ----> "+p_dn);
		        }
		        commandMap.put("realName", p_dn);	// 이름
		        session.setAttribute("p_o_realName",p_dn);	// 이름
			}
			model.addAllAttributes(commandMap);
			return "usr/mem/memJoinStep03_1"; // epki
		} else {
						
			if("S".equals(certgubun) || "C".equals(certgubun)) {
				p_name = (String)commandMap.get("realName");
				birthDate = (String)commandMap.get("birthDate");
				sex = (String)commandMap.get("sex");
				
			} else if("G".equals(certgubun)) {
				p_name = (String)commandMap.get("realName");
				birthDate = (String)commandMap.get("birthDate");
				sex = (String)commandMap.get("sex");
				virtualNo = (String)commandMap.get("virtualNo");
				
				session.setAttribute("p_o_realName",p_name);	// 이름
				
				if(sex.equals("1")){
					commandMap.remove("sex");
					commandMap.put("sex", "M");
				}else{
					commandMap.remove("sex");
					commandMap.put("sex", "F");
				}
			} else {
				model.addAllAttributes(commandMap);
				model.addAttribute("resultMsg", egovMessageSource.getMessage("user.member.step02.fail"));
				return "forward:/usr/mem/memJoinStep01.do";
			}
			
			log.info("======================================================================");
			log.info("======================== p_name    : "+p_name+          "=============");
			log.info("======================== virtualNo : "+virtualNo+       "=============");
			log.info("======================== birthDate : "+birthDate+       "=============");
			log.info("======================== certgubun : "+certgubun+		  "=============");
			log.info("======================== sex       : "+sex+             "=============");
			log.info("======================================================================");
			
			//정상등록 인지를 체크한다.
			if(("S".equals(certgubun) && StringUtils.isBlank(p_name)) || ("G".equals(certgubun) && (StringUtils.isBlank(p_name) || StringUtils.isBlank(virtualNo))))
			{
				model.addAllAttributes(commandMap);
				model.addAttribute("resultMsg", egovMessageSource.getMessage("user.member.step02.fail"));
				return "forward:/usr/mem/memJoinStep01.do";
			}
			
			
			//기존의 가입자인지를 체크한다.
			int cnt = memberSearchService.selectResnoCheck(commandMap);
			if(cnt > 0)
			{
				model.addAttribute("resultMsg", egovMessageSource.getMessage("user.member.resno.same.fail"));
				return "forward:/usr/mem/userLogin.do";
			}
			
			model.addAllAttributes(commandMap);
		}
		
		return "usr/mem/memJoinStep03_1";
	}
	
	@RequestMapping(value="/usr/mem/memJoinStep03_1.do")
	public String memJoinStep03_1(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String url =  "usr/mem/memJoinStep03_1";
		
		commandMap.put("menu_main", "7");
		commandMap.put("menu_sub", "1");
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "회원가입");
		
		// 교사자격등급 셀렉트박스 추가
		model.addAttribute("list", memberSearchService.selectSubjectList(commandMap));
		
		return url;
	}
		
	
	/**
	 * 마이페이지 > 개인정보수정페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/memMyPage.do")
	public String memMyPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String url = "usr/mpg/memMyPage";
		
		HashMap userMap = new HashMap();
		userMap.put("p_userid", commandMap.get("userid"));
		
		Map view = memberSearchService.selectMemberView(userMap);
		String strServerCert = "";
		String sessionID 		= request.getSession().getId();
		List subjectList = memberSearchService.selectSubjectList(commandMap);
		
		//OLD EPKI
		/*
		if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {
			
			EpkiApi.initApp();
				
			try
			{
				byte[] bsserverCert = null;
				
				// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
				ServerConf conf = new ServerConf();
						
				// 복호화를 위한 키관리용 인증서 설정
				X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
				
				bsserverCert = cert.getCert();
				
				// 서버인증서 Base64 인코딩
				Base64 encoder = new Base64();
				strServerCert = encoder.encode(bsserverCert);
			}
			catch (EpkiException e) 
			{
				log.error("Session Request Error!<BR>");
				log.error(e.toString());
			}
			catch (Exception e) 
			{
				log.error("Session Request Error!<BR>");
				log.error(e.toString());
			}
		}*/
		
		
		if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {
			//html5 epki 인증 세팅
			epkiHtml5Service.getHtml5ServerCert(request, model);
		}		
		model.addAttribute("isDevelopmentMode", propertyService.getBoolean("Globals.isDevelopmentMode"));				
		
		model.addAttribute("view", view);
		model.addAttribute("list", subjectList);
		
		//model.addAttribute("serverCert", strServerCert);
		//model.addAttribute("sessionId", sessionID);
		model.addAllAttributes(commandMap);
		return url;
	}
	
	@RequestMapping(value="/usr/mpg/personalnumber.do")
	public String personalnumber(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String url =  "usr/mpg/personalnumber";
		return url;
	}	
	@RequestMapping(value="/usr/mpg/personalnumber1.do")
	public String personalnumber1(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String url =  "usr/mpg/personalnumber1";
		return url;
	}	
	
	/**
	 * 회원가입 > 회원등록 프로세스
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/memJoinAction.do")
	public String memJoinAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String p_process = (String)commandMap.get("p_process");
	
		String resultMsg = "";
		
		String url =  "forward:/usr/mem/memJoinStep01.do";
		HttpSession session = request.getSession();
		
		//회원가입
		if(p_process.equals("insert"))
		{
			
			//System.out.println("memJoinAction ----> ");
			
			String p_name = (String)commandMap.get("p_name");
			
			//System.out.println("p_name ----> "+p_name);
			
			String s_o_realName = session.getAttribute("p_o_realName") !=null ?  (String)session.getAttribute("p_o_realName") : "" ;			
			
			//System.out.println("s_o_realName ----> "+s_o_realName);
			
			if(!p_name.equals(s_o_realName)) {			
				resultMsg = "정상적인 입력이 아닙니다.";
				url =  "forward:/usr/mem/memJoinStep01.do";				
				
				commandMap.put("menu_main", "7");
				commandMap.put("menu_sub", "1");
				commandMap.put("menu_tab_title", "회원가입");
				commandMap.put("menu_sub_title", "회원가입");
				model.addAttribute("resultMsg", resultMsg);
				model.addAllAttributes(commandMap);
				return url;
			}
			//세션 삭제
			session.removeAttribute("p_o_realName");
			//System.out.println("p_o_realName ---> "+session.getAttribute("p_o_realName"));
			
			//String p_resno = (String)commandMap.get("p_resno");
			//p_resno = Seed128Cipher.decrypt(p_resno, Seed128Cipher.getStringToBytes(), "utf-8");
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@       "+p_resno);
			//commandMap.put("p_resno", p_resno);
			//System.out.println("#################################        "+commandMap.get("p_resno").toString());
			
			//기존의 가입자인지를 체크한다.
			int cnt = memberSearchService.selectResnoCheck(commandMap);
			
			if(cnt > 0)
			{
				resultMsg = egovMessageSource.getMessage("user.member.resno.same.fail");
				//url =  "forward:http://iedu.nise.go.kr/usr/mem/userLogin.do";
				url =  "forward:/usr/mem/userLogin.do";
			}
			else
			{
				boolean isok = memberSearchService.insertMemberUser(commandMap);		//회원가입
				
				if(isok) {
					resultMsg = egovMessageSource.getMessage("success.common.insert");
					url =  "forward:/usr/mem/memJoinStep04.do";
				}else{
					resultMsg = egovMessageSource.getMessage("fail.common.insert");
					url =  "forward:/usr/mem/memJoinStep01.do";
				}
			}
		}
		//개인정보수정
		else if(p_process.equals("update"))
		{
			System.out.println("p_nicePersonalNum 2222  ---> "+(String)commandMap.get("p_nicePersonalNum"));
			System.out.println("commandMap : " + commandMap);
			
			boolean isok = memberSearchService.updateMemberUser(commandMap);
			
			if(isok) {
				resultMsg = egovMessageSource.getMessage("success.common.update");
				url =  "forward:/usr/mpg/memMyPage.do";
			}else{
				resultMsg = egovMessageSource.getMessage("fail.common.update");
				url =  "forward:/usr/mpg/memMyPage.do";
			}
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	
	
	/**
	 * 회원가입 > 가입완료..
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/memJoinStep04.do")
	public String memJoinStep04(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		String url =  "usr/mem/memJoinStep04";
		
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	/**
	 * 모바일 > 아이디/비밀번호 찾기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/main/idpwMobile.do")
	public String idpwMobile(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		String url =  "mbl/main/idpwMobile";
		
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	/**
	 * 모바일 > 아이디찾기 완료
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/usr/userIdSearchAction.do")
	public ModelAndView propListAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		ModelAndView modelAndView = new ModelAndView();
		
		String p_hp1 = commandMap.get("p_hp1") + "";
		String p_hp2 = commandMap.get("p_hp2") + "";
		String p_hp3 = commandMap.get("p_hp3") + "";
		
		String p_hp = p_hp1 + "-" + p_hp2 + "-" + p_hp3;
		commandMap.put("p_hp", p_hp);
		
		Map view = memberSearchService.selectIdPwdSearch(commandMap);
		
		String p_mode = commandMap.get("p_mode") + "";
		
		if(view != null)
		{
			if(p_mode != null && p_mode.equals("pwdcheck"))		//비번찾기 일때는 비밀번호를 초기화 하여 보낸다.
			{
				//비밀번호 초기화
				boolean ok = memberSearchService.updatePwdReset(commandMap);
				
				String p_handphone = view.get("handphone") + "";
				
				
				//핸드폰 번호 변경
				if(p_hp != null && !p_hp.equals("")) 
				{
					
					p_hp = p_hp1 + "-" + p_hp2 + "-" + p_hp3;
					
					p_handphone = p_hp;
					
					view.put("handphone", p_handphone);
				}
				
				
				String pwd  = view.get("pwd") + "";
				String content = "회원님의 비밀번호는 [" + pwd + "]입니다. 로그인하신 후 꼭 비밀번호 변경부탁드립니다.";
				
				//문자발송
				if(ok == true && p_handphone != null && !p_handphone.equals(""))
				{
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("p_handphone", p_handphone);
					map.put("content", content);
					
					boolean isOk = SMSSenderDAO.dacomSmsSender(map);				
					
					
				}
				
			}
			
		}
		
		
		
		Map resultMap = new HashMap();
    	resultMap.put("result", view);
    	
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	
    	return modelAndView;
	}
	
	
	/**
	 * 모바일 > 비밀번호 찾기 완료
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/main/idpwMobileAction.do")
	public String idpwMobileAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		String url =  "mbl/main/idpwMobileAction";
		
		//임시비밀번호 생성 후 문자발송 및 비밀번호 변경을 한다.
		
		
		
		Map view = memberSearchService.selectIdPwdSearch(commandMap);
		model.addAttribute("view", view);
		
		
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	
	
	
	/**
	 * 회원가입 > 아이디/비밀번호 찾기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping(value="/usr/mem/userIdPwdSearch01.do")
	public String userIdPwdSearch01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		String url =  "usr/mem/userIdPwdSearch01";
		
		String strServerCert = "";
		String sessionID 		= request.getSession().getId();
		System.out.println("@@@@@@@@@  "+propertyService.getBoolean("Globals.isDevelopmentMode"));
		System.out.println("@@@@@@@@@  "+sessionID);
		if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {
		  
			//  epki-java 초기화
			EpkiApi.initApp();
				
			try
			{
				byte[] bsserverCert = null;
				
				// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
				ServerConf conf = new ServerConf();
						
				// 복호화를 위한 키관리용 인증서 설정
				X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
				
				bsserverCert = cert.getCert();
				
				// 서버인증서 Base64 인코딩
				Base64 encoder = new Base64();
				strServerCert = encoder.encode(bsserverCert);
			}
			catch (EpkiException e) 
			{
				log.info("getServerCert Error!<BR>");
				log.info(e.toString());
			} 
		}
		model.addAttribute("serverCert", strServerCert);
		model.addAttribute("sessionId", sessionID);
		model.addAttribute("isDevelopmentMode", propertyService.getBoolean("Globals.isDevelopmentMode"));
		model.addAllAttributes(commandMap);
		return url;
	}*/
	
	
	/**
	 * 회원가입 > 아이디/비밀번호 찾기 완료
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/userIdPwdSearch02.do")
	public String userIdPwdSearch02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		String p_mode = commandMap.get("p_mode") + "";
		
		String url =  "usr/mem/userIdPwdSearch02";
		
		String strAlgID = (String)commandMap.get("SelectedAlgID");
		String strEnvelopedResNo = (String)commandMap.get("p_resno");
		String searchGubun = (String)commandMap.get("p_searchGubun");
		
		
		
		String strEnvelopedUserId = "";
		if(p_mode.equals("pwdcheck")){
			strEnvelopedUserId = (String)commandMap.get("p_userid_1");
		}
		
		String strDevelopedResNo = "";
		String strDevelopedUserId = "";
		
		System.out.println("strEnvelopedResNo1================> " +strEnvelopedResNo);
		System.out.println("strEnvelopedUserId1================> " +strEnvelopedUserId);
		
		
		if(searchGubun == null) {
			if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {
				//  epki-java 초기화
				EpkiApi.initApp();
					
				try
				{	
					byte[] bEnvelopedData = null;
					byte[] bEnvelopedData1 = null;
					byte[] bDevelopData = null;
					byte[] bDevelopData1 = null;
					
					// 전송 데이터 Base64 디코딩
					Base64 decoder = new Base64();
					bEnvelopedData = decoder.decode(strEnvelopedResNo);
					if(p_mode.equals("pwdcheck")){
						bEnvelopedData1 = decoder.decode(strEnvelopedUserId);
					}
					
					EnvelopedData envelopedData = new EnvelopedData(strAlgID);
					
					// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
					ServerConf conf = new ServerConf();
					
					// 복호화를 위한 키관리용 인증서 설정
					X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
					PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_KM, "");
					
					bDevelopData = envelopedData.develop(bEnvelopedData, cert, priKey);
					strDevelopedResNo = new String(bDevelopData);
					
					if(p_mode.equals("pwdcheck")){
						bDevelopData1 = envelopedData.develop(bEnvelopedData1, cert, priKey);
						strDevelopedUserId = new String(bDevelopData1);
						
						commandMap.put("p_userid_1", strDevelopedUserId);
					}
					
					System.out.println("strDevelopedResNo===========> "+strDevelopedResNo);
					System.out.println("strDevelopedUserId===========> "+strDevelopedUserId);
				}
				catch (EpkiException e) 
				{
					System.out.println(strEnvelopedResNo);
					System.out.println("Develop Error!<BR>");
					System.out.println(e.toString());
				}
				catch (Exception e)
				{
					System.out.println("Develop Error!<BR>");
					System.out.println(e.toString());
				}
				
				commandMap.put("p_resno", strDevelopedResNo);
			}
		}
		
		String p_hp1 = commandMap.get("p_hp1") + "";
		String p_hp2 = commandMap.get("p_hp2") + "";
		String p_hp3 = commandMap.get("p_hp3") + "";
		
		String p_hp = p_hp1 + "-" + p_hp2 + "-" + p_hp3;
		
		commandMap.put("p_hp", p_hp);
		
		Map view = memberSearchService.selectIdPwdSearch(commandMap);
		
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "아아디/비밀번호찾기");
		commandMap.put("menu_sub", "2");
		commandMap.put("menu_main", "7");
		
		
		if(view != null)
		{
			if(p_mode != null && p_mode.equals("pwdcheck"))		//비번찾기 일때는 비밀번호를 초기화 하여 보낸다.
			{
				//비밀번호 초기화
				boolean ok = memberSearchService.updatePwdReset(commandMap);
				
				String p_handphone = view.get("handphone") + "";
				
				
				//핸드폰 번호 변경
				if(p_hp != null && !p_hp.equals("")) 
				{
					
					p_hp = p_hp1 + "-" + p_hp2 + "-" + p_hp3;
					
					p_handphone = p_hp;
					
					view.put("handphone", p_handphone);
				}
				
				
				String pwd  = view.get("pwd") + "";
				String content = "회원님의 비밀번호는 [" + pwd + "]입니다. 로그인하신 후 꼭 비밀번호 변경부탁드립니다.";
				
				//문자발송
				if(ok == true && p_handphone != null && !p_handphone.equals(""))
				{
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("p_handphone", p_handphone);
					map.put("content", content);
					
					boolean isOk = SMSSenderDAO.dacomSmsSender(map);
					
					if(isOk)
					{
						model.addAttribute("view", view);
						
						url =  "usr/mem/userIdPwdSearch02";
					}
					else
					{
						url =  "forward:/usr/mem/userIdPwdSearch01.do";
						
						resultMsg = egovMessageSource.getMessage("fail.common.idpwd.search");
						
						model.addAttribute("resultMsg", resultMsg);
					}
				}
				else
				{
					url =  "forward:/usr/mem/userIdPwdSearch01.do";
					
					resultMsg = egovMessageSource.getMessage("fail.common.idpwd.search");
					
					model.addAttribute("resultMsg", resultMsg);
				}
			}
			else
			{
				model.addAttribute("view", view);
				
				url =  "usr/mem/userIdPwdSearch02";
			}
			
		}
		else
		{
			url =  "forward:/usr/mem/userIdPwdSearch01.do";
			
			resultMsg = egovMessageSource.getMessage("fail.common.idpwd.search");
			
			model.addAttribute("resultMsg", resultMsg);
		}
		
		commandMap.remove("p_userid_1");
		commandMap.remove("p_resno");
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	/**
	 * 회원가입 > 아이디/비밀번호 찾기 완료
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/userIdPwdSearch0201.do")
	public String userIdPwdSearch0201(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		String url =  "usr/mem/userIdPwdSearch02";
		
		commandMap.put("p_userid_1", commandMap.get("p_userid_ok"));
		
		Map view = memberSearchService.selectIdPwdSearchOk(commandMap);
		
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "아아디/비밀번호찾기");
		commandMap.put("menu_sub", "2");
		commandMap.put("menu_main", "7");
		
		
		if(view != null)
		{
			//비밀번호 초기화
			boolean ok = memberSearchService.updatePwdReset(commandMap);
			
			String p_handphone = view.get("handphone") + "";
			
			String pwd  = view.get("pwd") + "";
			String content = "회원님의 비밀번호는 [" + pwd + "]입니다. 로그인하신 후 꼭 비밀번호 변경부탁드립니다.";
			
			//문자발송
			if(ok == true && p_handphone != null && !p_handphone.equals(""))
			{
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("p_handphone", p_handphone);
				map.put("content", content);
				
				boolean isOk = SMSSenderDAO.dacomSmsSender(map);
				
				if(isOk)
				{
					model.addAttribute("view", view);
					
					url =  "usr/mem/userIdPwdSearch02";
				}
				else
				{
					url =  "forward:/usr/mem/userIdPwdSearch01.do";
					
					resultMsg = egovMessageSource.getMessage("fail.common.idpwd.search");
					
					model.addAttribute("resultMsg", resultMsg);
				}
			}
			else
			{
				url =  "forward:/usr/mem/userIdPwdSearch01.do";
				
				resultMsg = egovMessageSource.getMessage("fail.common.idpwd.search");
				
				model.addAttribute("resultMsg", resultMsg);
			}
		}
		else
		{
			url =  "forward:/usr/mem/userIdPwdSearch01.do";
			
			resultMsg = egovMessageSource.getMessage("fail.common.idpwd.search");
			
			model.addAttribute("resultMsg", resultMsg);
		}
		
		commandMap.remove("p_userid_ok");
		commandMap.remove("p_userid_1");
		commandMap.remove("p_resno");
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	
	/**
	 * 회원가입 > 로그인화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/userLogin.do")
	public String userLogin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String url =  "usr/mem/userLogin";
		
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "로그인");
		commandMap.put("menu_sub", "3");
		commandMap.put("menu_main", "7");
		
		String strServerCert = "";
		String sessionID 		= request.getSession().getId();
		System.out.println("@@@@@@@@@  "+propertyService.getBoolean("Globals.isDevelopmentMode"));
		System.out.println("@@@@@@@@@  "+sessionID);
		if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {
		  
			//  epki-java 초기화
			EpkiApi.initApp();
				
			try
			{
				byte[] bsserverCert = null;
				
				// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
				ServerConf conf = new ServerConf();
						
				// 복호화를 위한 키관리용 인증서 설정
				X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
				
				bsserverCert = cert.getCert();
				
				// 서버인증서 Base64 인코딩
				Base64 encoder = new Base64();
				strServerCert = encoder.encode(bsserverCert);
			}
			catch (EpkiException e) 
			{
				log.info("getServerCert Error!<BR>");
				log.info(e.toString());
			} 
		}
		model.addAttribute("serverCert", strServerCert);
		model.addAttribute("sessionId", sessionID);
		model.addAttribute("isDevelopmentMode", propertyService.getBoolean("Globals.isDevelopmentMode")); 
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	/**
	 * 회원가입 > 개인정보처리방침
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/copyRight.do")
	public String copyRight01(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{

		
		model.addAllAttributes(commandMap);
		return "usr/mem/copyRight";
	}
	
	/**
	 * 마이페이지 > 개인정보처리방침
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/copyRight.do")
	public String copyRight02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{

		
		model.addAllAttributes(commandMap);
		return "usr/mem/copyRight";
	}
	@RequestMapping(value="/usr/mpg/copyRight1.do")
	public String copyRight1(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		model.addAllAttributes(commandMap);
		return "usr/mem/copyRight1";
	}
	
	
	
	/**
	 * 마이페이지 > 회원탈퇴시작
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/memberOut.do")
	public String memberOut(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{

		
		model.addAllAttributes(commandMap);
		return "usr/mpg/memberOut";
	}
	
	
	/**
	 * 마이페이지 > 회원탈퇴처리
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/memberOutAction.do")
	public String memberOutAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		Object p_isretire = commandMap.get("p_isretire");
		Object p_userid = commandMap.get("userid");
		
		String returnUrl = "/usr/mpg/memberOut.do";
		
		
		if(p_isretire != null)
		{
			if(p_isretire.equals("Y"))	//회원탈퇴가 정상적이라면
			{
				Map<String, Object> retriMap = new HashMap<String, Object>();
				retriMap.put("p_isretire", p_isretire);
				retriMap.put("p_userid", p_userid);
				
				//탈퇴 처리.......
				boolean isok = memberSearchService.updateMemberOutAndIn(retriMap);
				if(isok)
				{
					//로그아웃 처리
					HttpSession session = request.getSession();
					session.invalidate();
					
					returnUrl = "forward:/usr/mpg/memberOutResult.do";
				}
				
				
				
			}
		}
		
		model.addAllAttributes(commandMap);
		
		return returnUrl;
	}
	
	/**
	 * 마이페이지 > 회원탈퇴완료
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/memberOutResult.do")
	public String memberOutResult(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "usr/mpg/memberOutResult";
	}
	
	
	/**
	 * 마이페이지 > 비밀번호변경페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/pwdChange.do")
	public String pwdChange(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "usr/mpg/pwdChange";
	}
	
	
	
	
	/**
	 * 마이페이지 > 회원탈퇴완료
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mpg/pwdChangeAction.do")
	public String pwdChangeAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String returnUrl = "usr/mpg/pwdChange";
		//기존 비밀번호가 맞는지를 확인한다.
		int cnt = loginService.userOldPwdCount(commandMap);
		
		if(cnt > 0)
		{
			/*
			 * 비밀번호변경 및 변경일자 업데이트 
			 */
			int updated = loginService.updateUserPasswdChange(commandMap);
			
			if(updated > 0)
			{
				model.addAttribute("resultMsg", "정상적으로 비밀번호가 변경되었습니다.");
				returnUrl = "forward:/usr/hom/portalActionMainPage.do";
			}
			else
			{
				model.addAttribute("resultMsg", "죄송합니다. 비밀번호 변경을 실패하였습니다.\\n다시 한번 비밀번호변경을 시도해 주세요!");
			}
		}
		else
		{
			model.addAttribute("resultMsg", "입력하신 기존비밀번호와 일치하지 않습니다.\\n기존비밀번호를 정확히 입력해주세요!");
		}
		
		model.addAllAttributes(commandMap);
		return returnUrl;
	}
	
	/**
	 * 회원가입 > 학교검색
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/searchSchoolPop.do")
	public String searchSchoolPop(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String returnUrl = "usr/mem/schoolSearchPop";
		if(commandMap.get("p_school_nm") == null){
			commandMap.put("p_school_nm", "");
		}
		
		if(!commandMap.get("p_school_nm").equals("")){
			List list = memberSearchService.selectSearchSchool(commandMap);
			model.addAttribute("list", list);
		}
		
		model.addAllAttributes(commandMap);
		return returnUrl;
	}
	
	/**
	 *  마이페이지 >  아이디통합 내역 조회
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/idIntergrationIdSearch.do")
	public String idIntergrationIdSearch(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String returnUrl = "usr/mpg/intergrationIdInfoList";
		
			List list = memberSearchService.idIntergrationIdSearch(commandMap);
			model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return returnUrl;
	}
	
	/**
	 *  마이페이지 >  아이디통합
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/idIntergrationIdAction.do")
	public String idIntergrationIdAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		/*log.error("userIds["+commandMap.get("userIds")+"]");
		Map<String, Object> result = memberSearchService.idIntergrationIdAction(commandMap);
		
		System.out.println("successCnt ---> "+result.get("successCnt"));
		
		
		String resultMsg = "";
		String successCnt = result.get("successCnt") !=null ? result.get("successCnt").toString() : "";
		if(!"".equals(successCnt)){
			resultMsg = egovMessageSource.getMessage("success.request.msg");
		}
		
		model.addAllAttributes(commandMap);
		model.addAttribute("resultMsg", resultMsg);*/
		return "forward:/usr/mem/idIntergrationIdSearch.do";
	}
	
	
	/**
	 * 회원가입 > 교육청검색
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/mem/searchEducationOfficePop.do")
	public String searchEducationOfficePop(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String returnUrl = "usr/mem/searchEducationOfficePop";
		if(commandMap.get("p_school_nm") == null){
			commandMap.put("p_school_nm", "");
		}
		
		if(!commandMap.get("p_school_nm").equals("")){
			List list = memberSearchService.selectSearchEducationOfficePop(commandMap);
			model.addAttribute("list", list);
		}
		
		model.addAllAttributes(commandMap);
		return returnUrl;
	}
	
	@RequestMapping(value="/usr/mem/memDormantPage01.do")
	public String memDormantPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "usr/mem/memDormantPage01";
	}

	@RequestMapping(value="/usr/mem/memDormantPage02.do")
	public String memDormantPage02(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		String url =  "usr/mem/memDormantPage01";
		
		commandMap.put("p_userid_1", commandMap.get("p_userid_ok"));
		
		/*휴면 계정인지 아닌지 찾기
		휴면 계정이 아니면 휴면 계정이 아니고 리턴
		
		휴면 계정이면 비밀번호 업데이트 후 문자 발송
		*/
		boolean dormantOk = memberSearchService.selectDormantOk(commandMap);
		
		if(dormantOk){
			Map view = memberSearchService.selectIdPwdSearchOk(commandMap);
			
			commandMap.put("menu_tab_title", "회원가입");
			commandMap.put("menu_sub_title", "휴면 계정 관리");
			commandMap.put("menu_sub", "2");
			commandMap.put("menu_main", "7");
			
			
			if(view != null)
			{
				//비밀번호 초기화
				boolean ok = memberSearchService.updatePwdReset(commandMap);
				
				// 휴면 계정 풀음
				memberSearchService.updateDormantReset(commandMap);
				
				String p_handphone = view.get("handphone") + "";
				
				String pwd  = view.get("pwd") + "";
				String content = "회원님의 비밀번호는 [" + pwd + "]입니다. 로그인하신 후 꼭 비밀번호 변경부탁드립니다.";
				
				//문자발송
				if(ok == true && p_handphone != null && !p_handphone.equals(""))
				{
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("p_handphone", p_handphone);
					map.put("content", content);
					
					boolean isOk = SMSSenderDAO.dacomSmsSender(map);
					
					if(isOk)
					{
						model.addAttribute("view", view);
						
						url =  "usr/mem/memDormantPage01";
					}
					else
					{
						url =  "forward:/usr/mem/memDormantPage01.do";
						
						resultMsg = egovMessageSource.getMessage("fail.common.idpwd.search");
						
						model.addAttribute("resultMsg", resultMsg);
					}
				}
				else
				{
					url =  "forward:/usr/mem/memDormantPage01.do";
					
					resultMsg = egovMessageSource.getMessage("fail.common.idpwd.search");
					
					model.addAttribute("resultMsg", resultMsg);
				}
			}
			else
			{
				url =  "forward:/usr/mem/memDormantPage01.do";
				
				resultMsg = egovMessageSource.getMessage("fail.common.idpwd.search");
				
				model.addAttribute("resultMsg", resultMsg);
			}
			
			
		} 
		else{
			url =  "forward:/usr/mem/memDormantPage01.do";
			
			resultMsg = "휴면 계정이 아닙니다.";
		}
		model.addAttribute("resultMsg", resultMsg);
		commandMap.remove("p_userid_ok");
		commandMap.remove("p_userid_1");
		commandMap.remove("p_resno");
		model.addAllAttributes(commandMap);
		return url;
	}
	
	//이걸로 변경해야 함.	
	@RequestMapping(value="/usr/mem/admLogin.do")
	public String admLoginHtml5(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String url =  "usr/mem/admLoginHtml5";
		
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "로그인");
		commandMap.put("menu_sub", "3");
		commandMap.put("menu_main", "7");
		
		if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {
			// epki 인증 세팅
			epkiHtml5Service.getHtml5ServerCert(request, model);
		}		
		model.addAttribute("isDevelopmentMode", propertyService.getBoolean("Globals.isDevelopmentMode"));				
		model.addAllAttributes(commandMap);
		return url;
	}	
	
	/**
	 * 회원가입 > 로그인화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	//20180314 주석
	/*@RequestMapping(value="/usr/mem/admLogin.do")
	public String admLogin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String url =  "usr/mem/admLogin";
		
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "로그인");
		commandMap.put("menu_sub", "3");
		commandMap.put("menu_main", "7");
		
		String strServerCert = "";
		String sessionID 		= request.getSession().getId();
		System.out.println("@@@@@@@@@  "+propertyService.getBoolean("Globals.isDevelopmentMode"));
		System.out.println("@@@@@@@@@  "+sessionID);
		if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {
		  
			//  epki-java 초기화
			EpkiApi.initApp();
				
			try
			{
				byte[] bsserverCert = null;
				
				// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
				ServerConf conf = new ServerConf();
						
				// 복호화를 위한 키관리용 인증서 설정
				X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
				
				bsserverCert = cert.getCert();
				
				// 서버인증서 Base64 인코딩
				Base64 encoder = new Base64();
				strServerCert = encoder.encode(bsserverCert);
			}
			catch (EpkiException e) 
			{
				log.info("getServerCert Error!<BR>");
				log.info(e.toString());
			} 
		}
		model.addAttribute("serverCert", strServerCert);
		model.addAttribute("sessionId", sessionID);
		model.addAttribute("isDevelopmentMode", propertyService.getBoolean("Globals.isDevelopmentMode")); 
		model.addAllAttributes(commandMap);
		return url;
	}*/
	
	/**
	 * 홈 > 회원가입 > 회원가입 / EPKI HTML5
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	//@RequestMapping(value="/usr/mem/memJoinStepHtml502.do")
	@RequestMapping(value="/usr/mem/memJoinStep02.do")
	public String memJoinStepHtml502(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String url =  "usr/mem/memJoinStepHtml502";
		
		commandMap.put("menu_main", "7");
		commandMap.put("menu_sub", "1");
		commandMap.put("menu_tab_title", "회원가입");
		commandMap.put("menu_sub_title", "회원가입");
		
		
		model.addAllAttributes(commandMap);
		
		if(!propertyService.getBoolean("Globals.isDevelopmentMode")) {
			// epki 인증 세팅
			epkiHtml5Service.getHtml5ServerCert(request, model);
		}		
		return url;
	}
}
