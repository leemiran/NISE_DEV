package egovframework.usr.lgn.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;





import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epki.api.EpkiApi;
import com.epki.cert.CertValidator;
import com.epki.cert.X509Certificate;
import com.epki.cms.EnvelopedData;
import com.epki.cms.SignedData;
import com.epki.conf.ServerConf;
import com.epki.crypto.PrivateKey;
import com.epki.crypto.SecretKey;
import com.epki.exception.CertificateExpiredException;
import com.epki.exception.CertificateRevokedException;
import com.epki.exception.EpkiException;
import com.epki.session.SecureSession;
import com.epki.util.Base64;
import com.epki.vid.IdentityValidator;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.tmp.service.TempletManageService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.usr.lgn.service.LoginService;
import egovframework.svt.cert.EpkiHtml5Service;


@Controller
public class LoginController {

	/** log */
	protected static final Log log = LogFactory.getLog(LoginController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** loginService */
	@Resource(name = "loginService")
	LoginService loginService;
	
	/** templetManageService */
	@Resource(name = "templetManageService")
	TempletManageService templetManageService;
	
	@Resource(name = "propertiesService")
    protected EgovPropertyService  propertyService;
	
	@Autowired
	EpkiHtml5Service epkiHtml5Service;
	
	@RequestMapping(value="/usr/lgn/portalUserLogin.do")
	public String userLogin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
	
		String strAlgID = (String)commandMap.get("SelectedAlgID");
		String strEnvelopedData = (String)commandMap.get("userId");
		String strEnvelopedData1 = (String)commandMap.get("pwd");
		String loginGubun = (String)commandMap.get("loginGubun");
		String strDevelopedData = "";
		String strDevelopedData1 = "";
		
		System.out.println("strEnvelopedData================> " +strEnvelopedData);
		System.out.println("strEnvelopedData1================> " +strEnvelopedData1);
		
		if(loginGubun == null){
			
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
					bEnvelopedData = decoder.decode(strEnvelopedData);
					bEnvelopedData1 = decoder.decode(strEnvelopedData1);
					
					EnvelopedData envelopedData = new EnvelopedData(strAlgID);
					
					// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
					ServerConf conf = new ServerConf();
					
					// 복호화를 위한 키관리용 인증서 설정
					X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
					PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_KM, "");
					
					bDevelopData = envelopedData.develop(bEnvelopedData, cert, priKey);
					bDevelopData1 = envelopedData.develop(bEnvelopedData1, cert, priKey);
					strDevelopedData = new String(bDevelopData);
					strDevelopedData1 = new String(bDevelopData1);
					
					System.out.println("strDevelopedData===========> "+strDevelopedData);
					System.out.println("strDevelopedData1===========> "+strDevelopedData1);
				}
				catch (EpkiException e) 
				{
					System.out.println(strEnvelopedData);
					System.out.println("Develop Error!<BR>");
					System.out.println(e.toString());
				}
				catch (Exception e)
				{
					System.out.println("Develop Error!<BR>");
					System.out.println(e.toString());
				}
				
				commandMap.remove("userId");
				commandMap.remove("pwd");
				commandMap.put("userId", strDevelopedData);
				commandMap.put("pwd", strDevelopedData1);
			}
		}
		
		// 회사별로 포탈 로그인 가능여부에 따라 제한을 둔다.
		int isOk = loginService.checkPortalYn(commandMap);
		
		int lgexceptcnt  = Integer.valueOf(Globals.LOGIN_EXCEPT_VALUE);
		String lgexcept = Globals.LOGIN_EXCEPT_ISUSE;
		
		String resultMsg = "";
		String issso = (String)commandMap.get("p_issso");
		String check = (String)commandMap.get("p_chk");
		String returnUrl = "forward:/usr/hom/portalActionMainPage.do";
		
		//휴면 회원 비로그인		
		int isDormantOk = loginService.checkDormantYn(commandMap);
		if( isDormantOk < 1 ){
			resultMsg = "휴면회원입니다. 본인인증 후 사용하시기 바랍니다.";
			returnUrl = "forward:/usr/mem/memDormantPage01.do";
			
			commandMap.remove("userId");
			commandMap.remove("pwd");
			commandMap.put("menu_main", "7");
			commandMap.put("menu_sub", "5");
			commandMap.put("menu_tab_title", "회원가입");
			commandMap.put("menu_sub_title", "휴면계정전환");
			model.addAttribute("resultMsg", resultMsg);
			model.addAllAttributes(commandMap);
			return returnUrl;
		}
				

		
		Map userInfo;
		
		if( isOk < 1 ){
			if( issso != null && issso.equals("Y") ){
				resultMsg = "존재하지 않는 아이디입니다.\\n관리자에게 문의하시기 바랍니다.";
			}else{
				resultMsg = "존재하지 않는 아이디입니다.\\n관리자에게 문의하시기 바랍니다.";
			}
		}else{
			if( check != null && !check.equals("Y") && !request.getMethod().equals("POST") ){
				isOk = -7;
			}else{
				isOk = loginService.login(request, commandMap);
				if( isOk == -1 ){
					resultMsg = "아이디 또는 비밀번호가 일치하지 않습니다.\\n다시한번 입력하여 주시기 바랍니다. ";
				}else if( isOk == -2 ){
					resultMsg = "죄송합니다.\\n재직자만 로그인이 가능합니다."; // 퇴직자
				}else if( isOk == -3 ){
					if (lgexcept.equals("true")) {
						if ((Integer)commandMap.get("p_lgfailcnt") >  lgexceptcnt ) {
							resultMsg = "비밀번호 " + (String)commandMap.get("p_lgfailcnt") + "회 오류입니다.\\n" + lgexceptcnt + "회연속 오류시 관리자에게 문의하여 주십시오."; // 퇴직자
						} else {
							resultMsg = "아이디 또는 비밀번호가 일치하지 않습니다.\\n다시한번 입력하여 주시기 바랍니다.";
						}
					} else {
						resultMsg = "아이디 또는 비밀번호가 일치하지 않습니다.\\n다시한번 입력하여 주시기 바랍니다."; // 퇴직자
					}
				}else if( isOk == -4 ){
					resultMsg = "비밀번호가 " + lgexceptcnt + "회 연속 오류로 로그인 불가합니다.\\n관리자에게 문의하여 주십시오."; // 퇴직자
				}else if( isOk == -5 ){
					resultMsg = "관리자의 인증 후 로그인 하실 수 있습니다."; 
				}else if( isOk == -6 ){
					resultMsg = "회원님께서는 탈퇴하셨습니다.\\n현 사이트에서의 재가입은 탈퇴신청일로 부터 3개월 이후 가능합니다.\\n좀 더 빠른 시일내에 재가입을 원하신다면 운영자 " + EgovProperties.getProperty("admin.tel") + " 에게 연락해 주시기 바랍니다.";
				}else if( isOk == -7 ){
					resultMsg = "바람직하지 않는 방법으로 접근하셨습니다.";
				}else if( isOk == -8 ){
					resultMsg = "5분 후에 로그인을 다시 해주시기 바랍니다.";
				}else if( isOk == -9 ){
					resultMsg = "인증서로그인을 하여 주시기 바랍니다.";
				}else{ 
					//로그인 성공
					HttpSession session = request.getSession();
					
					session.setAttribute("p_after", 1);
					if( !session.getAttribute("grcode").equals("") && !session.getAttribute("grcode").equals("N000001") ){
						//로그인시 교육그룹이 있을 경우 게이트 페이지로 이동시킨다.
						Map inputMap = new HashMap();
						inputMap.put("grcode", session.getAttribute("grcode"));
						String grcodeType = templetManageService.getGrcodeType(commandMap);
						
						session.setAttribute("tem_type", grcodeType);
						session.setAttribute("tem_grcode", session.getAttribute("grcode"));
						session.setAttribute("s_site_gubun", "gate");
						//v_url += "/servlet/controller.homepage.MainServlet";
					}else if (session.getAttribute("grcode").equals("N000001") || session.getAttribute("grcode").equals("")) {
						session.setAttribute("tem_type", "");
						session.setAttribute("tem_grcode", "");
						session.setAttribute("s_site_gubun", "");
						returnUrl = "forward:/usr/hom/portalActionMainPage.do";
					}
					if ( commandMap.get("p_reurl") != null && !commandMap.get("p_reurl").equals("") ){ // 이벤트페이지에서 넘어올
						// 경우
						//v_url += box.getString("p_reurl");
						returnUrl = (String)commandMap.get("p_reurl");
						commandMap.put("p_process", commandMap.get("p_reprocess"));
						commandMap.put("p_tabseq", commandMap.get("p_tabseq"));
						commandMap.put("p_seq", commandMap.get("p_seq"));
					}
					
					if(isOk == 990)
					{
						returnUrl = "forward:/usr/mpg/memMyPage.do";
						
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "3");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "개인정보수정");
						
						resultMsg = "중복된 아이디가 있습니다. \\n아이디 통합 화면으로 이동합니다.";
					}
					
					if(isOk == 998)
					{
						returnUrl = "forward:/usr/mpg/memMyPage.do";
						
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "3");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "개인정보수정");
						
						resultMsg = "나이스 개인번호를 입력하셔야 합니다.\\n개인정보 수정으로 이동합니다.";
					}
						
					
					if(isOk == 997)
					{
						returnUrl = "forward:/usr/mpg/memMyPage.do";
						
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "3");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "개인정보수정");
						
						resultMsg ="학교코드가 초기화 되었습니다.\\n개인정보 수정으로 이동합니다.";
					}
					/*
					 * 비밀번호변경일자가 3개월이 지난 사용자는 비밀번호 변경페이지로 이동시킨다.
					 * isOk == 999
					 * 마이페이지 >> 비밀번호변경페이지
					 */
					if(isOk == 999)
					{
						returnUrl = "forward:/usr/mpg/pwdChange.do";
						
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "6");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "비밀번호변경");
					}
					
					
					if( session.getAttribute("email").equals("") || session.getAttribute("handphone").equals("") ){
						//v_url += "/servlet/controller.homepage.MemberServlet?p_process=updatePage";
						
						StringBuffer sb = new StringBuffer();
						/*if (session.getAttribute("resno").equals(""))
						sb.append(",주민번호");*/
						if (session.getAttribute("email").equals(""))
						sb.append(",이메일");
						if (session.getAttribute("handphone").equals(""))
						sb.append(",핸드폰");
						
						resultMsg = "개인정보 수정(" + sb.substring(1) + ") 부탁드립니다.\\n개인정보 수정으로 이동합니다.";
						
					}
					else if ( commandMap.get("pwd").equals(session.getAttribute("birthDate"))) {
						returnUrl = "forward:/usr/mpg/pwdChange.do";
						resultMsg = "보안에 취약한 비밀번호입니다. 비밀번호변경 부탁드립니다.\\n개인정보 수정으로 이동합니다.";
					}
				}
			}
		}
		

		//모바일에서 로그인한 경우라면.. 모바일로 url을 보낸다.
		if(loginGubun != null && loginGubun.equals("mobile"))
		{
			returnUrl = "forward:/mbl/main/index.do";
		}
		
		System.out.println("#################### returnUrl  "+returnUrl);
		
		if( !resultMsg.equals("") ){
			model.addAttribute("resultMsg", resultMsg);
		}
		commandMap.remove("userId");
		commandMap.remove("pwd");
		model.addAllAttributes(commandMap);
		return returnUrl;
	}
	
	/**
	 * 인증서 로그인을 처리한다
	 * @param vo - 주민번호가 담긴 LoginVO
	 * @return result - 로그인결과(세션정보)
	 * @exception Exception
	 */
    @RequestMapping(value="/usr/lgn/actionCrtfctLogin.action")
    public String actionCrtfctLogin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
    	System.out.println("==========================");
    	String returnUrl = "";
    	String resultMsg = "";
    	//개발모드면 일반로그인으로 이동한다
    	if(propertyService.getBoolean("Globals.isDevelopmentMode")) {
    		return "forward:/usr/mem/userLogin.do";
    	}
    	int isOk = loginService.checkPortalYn(commandMap);
    	Map rntMap = null;
    	String requestData = (String)commandMap.get("requestData");
    	
    	commandMap.put("dn" , this.getEpkiDN(request.getSession().getId(), requestData));
    	
    	System.out.println("dn ==============>  "+commandMap.get("dn").toString());
    	
    	isOk = loginService.actionCrtfctLogin(request, commandMap);
    	System.out.println("@@@ isOk  "+isOk);
    	if( isOk < 1 ){
    		resultMsg = "존재하지 않는 아이디입니다.\\n관리자에게 문의하시기 바랍니다.";
    		returnUrl = "forward:/usr/mem/userLogin.do";
    	}else if(isOk == 99){
    		resultMsg = "인증서가 등록되어 있지 않습니다.\\n개인정보수정에서 인증서를 등록하여 주시길 바랍니다.";
    		returnUrl = "forward:/usr/mem/userLogin.do";
    	}else if(isOk == 98){
    		returnUrl = "forward:/usr/mpg/memMyPage.do";
			
			//메뉴코드값 강제 등록
			commandMap.put("menu_main", "6");
			commandMap.put("menu_sub", "3");
			commandMap.put("menu_tab_title", "마이페이지");
			commandMap.put("menu_sub_title", "개인정보수정");
			
			resultMsg = "나이스 개인번호를 입력하셔야 합니다.\\n개인정보 수정으로 이동합니다.";
    	}else if(isOk == 97){
    		returnUrl = "forward:/usr/mpg/memMyPage.do";
			
			//메뉴코드값 강제 등록
			commandMap.put("menu_main", "6");
			commandMap.put("menu_sub", "3");
			commandMap.put("menu_tab_title", "마이페이지");
			commandMap.put("menu_sub_title", "개인정보수정");
			
			resultMsg = "학교코드가 초기화 되었습니다.\\n개인정보 수정으로 이동합니다.";
    	}else{
    		returnUrl = "forward:/usr/hom/portalActionMainPage.do";
    	}
    	if( !resultMsg.equals("") ){
			model.addAttribute("resultMsg", resultMsg);
		}
    	model.addAllAttributes(commandMap);
		return returnUrl;
    }
    
    /**
	 * 인증서 로그인을 처리한다
	 * @param vo - 주민번호가 담긴 LoginVO
	 * @return result - 로그인결과(세션정보)
	 * @exception Exception
	 */
    @RequestMapping(value="/usr/lgn/actionCrtfctLogin.do")
    public String nonActivXactionCrtfctLogin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
    	System.out.println("==========================");
    	String returnUrl = "";
    	String resultMsg = "";
    	//개발모드면 일반로그인으로 이동한다
    	if(propertyService.getBoolean("Globals.isDevelopmentMode")) {
    		return "forward:/usr/mem/userLogin.do";
    	}
    	int isOk = loginService.checkPortalYn(commandMap);
    	Map rntMap = null;
    	String requestData = (String)commandMap.get("requestData");
    	
    	commandMap.put("dn" , this.getEpkiDN(request.getSession().getId(), requestData));
    	
    	System.out.println("dn ==============>  "+commandMap.get("dn").toString());
    	
    	isOk = loginService.actionCrtfctLogin(request, commandMap);
    	System.out.println("@@@ isOk  "+isOk);
    	if( isOk < 1 ){
    		resultMsg = "존재하지 않는 아이디입니다.\\n관리자에게 문의하시기 바랍니다.";
    		returnUrl = "forward:/usr/mem/userLogin.do";
    	}else if(isOk == 99){
    		resultMsg = "인증서가 등록되어 있지 않습니다.\\n개인정보수정에서 인증서를 등록하여 주시길 바랍니다.";
    		returnUrl = "forward:/usr/mem/userLogin.do";
    	}else if(isOk == 98){
    		returnUrl = "forward:/usr/mpg/memMyPage.do";
			
			//메뉴코드값 강제 등록
			commandMap.put("menu_main", "6");
			commandMap.put("menu_sub", "3");
			commandMap.put("menu_tab_title", "마이페이지");
			commandMap.put("menu_sub_title", "개인정보수정");
			
			resultMsg = "나이스 개인번호를 입력하셔야 합니다.\\n개인정보 수정으로 이동합니다.";
    	}else if(isOk == 97){
    		returnUrl = "forward:/usr/mpg/memMyPage.do";
			
			//메뉴코드값 강제 등록
			commandMap.put("menu_main", "6");
			commandMap.put("menu_sub", "3");
			commandMap.put("menu_tab_title", "마이페이지");
			commandMap.put("menu_sub_title", "개인정보수정");
			
			resultMsg = "학교코드가 초기화 되었습니다.\\n개인정보 수정으로 이동합니다.";
    	}else{
    		returnUrl = "forward:/usr/hom/portalActionMainPage.do";
    	}
    	if( !resultMsg.equals("") ){
			model.addAttribute("resultMsg", resultMsg);
		}
    	model.addAllAttributes(commandMap);
		return returnUrl;
    }
    
public String getEpkiDN(String strSessionID, String strRequestData){
		
		String strAlgID			= "";
		String strSessionKey	= "";
		String strSessionIV		= "";
		String strUserDN		= new String();
		String strIssuerDN		= new String();
			
		try
		{
			//  epki-java 초기화
			EpkiApi.initApp();
			System.out.println("@@@@@@");
			byte[] bsessionKey	= null;
			byte[] bsessionIV	= null;
			SecureSession secureSession = new SecureSession();
			secureSession.setSessionID(strSessionID);
			// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
			ServerConf conf = new ServerConf();
			// 복호화를 위한 키관리용 인증서 설정
			X509Certificate cert	= conf.getServerCert(ServerConf.CERT_TYPE_KM);
			PrivateKey priKey		= conf.getServerPriKey(ServerConf.CERT_TYPE_KM, "");
			secureSession.response(cert, priKey, strRequestData);
			// 보안 세션 요청자 인증서 획득
			X509Certificate clientCert;
			clientCert = secureSession.getClientCert();
			// 요청자 인증서 검증
			CertValidator validator = new CertValidator();	
			// 인증서 검증 옵션 지정			
			validator.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);
			validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_CRL);
			validator.validate(CertValidator.CERT_TYPE_SIGN, clientCert);
			long lngStart = System.currentTimeMillis();
			// 요청자 인증서 정보 획득
			strUserDN = clientCert.getSubjectName();
			long lngEnd = System.currentTimeMillis();
		}
		catch (EpkiException e) 
		{
			log.error("Epki Session Response Error!<BR>");
			log.error(e.toString());
		}
		catch (Exception e) 
		{
			log.error("Epki Session Response Error!<BR>");
			log.error(e.toString());
		}
		
			return strUserDN;
	}
	
	@RequestMapping(value="/usr/lgn/portalUserMainLogin.do")
	public String userMainLogin(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {

		// 회사별로 포탈 로그인 가능여부에 따라 제한을 둔다.
		int isOk;

		int lgexceptcnt  = Integer.valueOf(Globals.LOGIN_EXCEPT_VALUE);
		String lgexcept = Globals.LOGIN_EXCEPT_ISUSE;
		
		String resultMsg = "";
		String check = (String)commandMap.get("p_chk");
		String loginGubun = (String)commandMap.get("loginGubun");
		String returnUrl = "forward:/usr/hom/portalActionMainPage.do";

		// 아이디 존재 여부 확인
		if( loginService.checkPortalYn(commandMap) < 1 )
		{
			resultMsg = "존재하지 않는 아이디입니다.\\n관리자에게 문의하시기 바랍니다.";
			returnUrl = "forward:/usr/mem/userLogin.do";			
		}
		else
		{
			//휴면 회원 비로그인		
			int isDormantOk = loginService.checkDormantYn(commandMap);
			System.out.println("========"+isDormantOk);
			if( isDormantOk > 0 ){
				resultMsg = "휴면회원입니다. 본인인증 후 사용하시기 바랍니다.";
				returnUrl = "forward:/usr/mem/memDormantPage01.do";
				
				commandMap.remove("userId");
				commandMap.remove("pwd");
				commandMap.put("menu_main", "7");
				commandMap.put("menu_sub", "5");
				commandMap.put("menu_tab_title", "회원가입");
				commandMap.put("menu_sub_title", "휴면계정전환");
				model.addAttribute("resultMsg", resultMsg);
				model.addAllAttributes(commandMap);
				return returnUrl;
			}
			
			
			if( check != null && !check.equals("Y") && !request.getMethod().equals("POST") ){
				isOk = -7;
				resultMsg = "바람직하지 않는 방법으로 접근하셨습니다.";
			}else{
				isOk = loginService.login(request, commandMap);
				
				if( isOk == -1 )
				{
					resultMsg = "아이디 또는 비밀번호가 일치하지 않습니다.\\n다시한번 입력하여 주시기 바랍니다. ";
				}
				else if( isOk == -2 )
				{
					resultMsg = "죄송합니다.\\n재직자만 로그인이 가능합니다."; // 퇴직자
				}
				else if( isOk == -3 )
				{
					if (lgexcept.equals("true"))
					{
						if ((Integer)commandMap.get("p_lgfailcnt") >  lgexceptcnt )
						{
							resultMsg = "비밀번호 " + (String)commandMap.get("p_lgfailcnt") + "회 오류입니다.\\n" + lgexceptcnt + "회연속 오류시 관리자에게 문의하여 주십시오.";
						}
						else
						{
							resultMsg = "아이디 또는 비밀번호가 일치하지 않습니다.\\n다시한번 입력하여 주시기 바랍니다.";
						}
					}
					else
					{
						resultMsg = "아이디 또는 비밀번호가 일치하지 않습니다.\\n다시한번 입력하여 주시기 바랍니다.";
					}
				}
				else if( isOk == -4 )
				{
					resultMsg = "비밀번호가 " + lgexceptcnt + "회 연속 오류로 로그인 불가합니다.\\n관리자에게 문의하여 주십시오."; // 퇴직자
				}
				else if( isOk == -5 )
				{
					resultMsg = "관리자의 인증 후 로그인 하실 수 있습니다."; 
				}
				else if( isOk == -6 )
				{
					resultMsg = "회원님께서는 탈퇴하셨습니다.\\n현 사이트에서의 재가입은 탈퇴신청일로 부터 3개월 이후 가능합니다.\\n좀 더 빠른 시일내에 재가입을 원하신다면 운영자 "
							+ EgovProperties.getProperty("admin.tel")
							+ " 에게 연락해 주시기 바랍니다.";
				}
				else if( isOk == -7 )
				{
					resultMsg = "바람직하지 않는 방법으로 접근하셨습니다.";
				}
				else if( isOk == -8 )
				{
					resultMsg = "5분 후에 로그인을 다시 해주시기 바랍니다.";
				}else if( isOk == -9 ){
					resultMsg = "접근이 제한 되었습니다. 시스템관리자에게 문의하시기 바랍니다.";
				}else if( isOk == -10 ){
					resultMsg = "접근이 제한 되었습니다. 시스템관리자에게 문의하시기 바랍니다...";				
				}else{//로그인 성공				
					HttpSession session = request.getSession();
					session.setAttribute("p_after", 1);
					
					if (!session.getAttribute("grcode").equals("") && !session.getAttribute("grcode").equals("N000001"))
					{
						//로그인시 교육그룹이 있을 경우 게이트 페이지로 이동시킨다.
						Map inputMap = new HashMap();
						inputMap.put("grcode", session.getAttribute("grcode"));
						String grcodeType = templetManageService.getGrcodeType(commandMap);
						
						session.setAttribute("tem_type", grcodeType);
						session.setAttribute("tem_grcode", session.getAttribute("grcode"));
						session.setAttribute("s_site_gubun", "gate");
						//v_url += "/servlet/controller.homepage.MainServlet";
					}
					else if (session.getAttribute("grcode").equals("N000001") || session.getAttribute("grcode").equals(""))
					{
						session.setAttribute("tem_type", "");
						session.setAttribute("tem_grcode", "");
						session.setAttribute("s_site_gubun", "");
						//returnUrl = "forward:/usr/hom/portalActionMainPage.do";
						returnUrl = "forward:/usr/hom/portalActionMainPage.do";
					}
					if (commandMap.get("p_reurl") != null && !commandMap.get("p_reurl").equals(""))
					{ // 이벤트페이지에서 넘어올 경우
						//v_url += box.getString("p_reurl");
						returnUrl = (String)commandMap.get("p_reurl");
						commandMap.put("p_process", commandMap.get("p_reprocess"));
						commandMap.put("p_tabseq", commandMap.get("p_tabseq"));
						commandMap.put("p_seq", commandMap.get("p_seq"));
					}
						
					if (isOk == 990)
					{
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "8");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "아이디통합");
						
						resultMsg = "중복된 아이디가 있습니다. \\n관리자에게 아이디 통합 요청 하여 주시기 바랍니다.";
						returnUrl = "forward:/usr/mem/idIntergrationIdSearch.do";
					}
					
					if (isOk == 998)
					{
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "3");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "개인정보수정");
						
						resultMsg = "나이스 개인번호를 입력하셔야 합니다.\\n개인정보 수정으로 이동합니다.";
						returnUrl = "forward:/usr/mpg/memMyPage.do";
						
						model.addAttribute("resultMsg", resultMsg);
						model.addAllAttributes(commandMap);
						
						return returnUrl;
					}
					
					if(isOk == 997)
					{
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "3");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "개인정보수정");
						
						resultMsg ="근무기관명을 신규 코드로 등록해 주세요.\\n개인정보 수정페이지로 이동합니다.";
						returnUrl = "forward:/usr/mpg/memMyPage.do";
						
						model.addAttribute("resultMsg", resultMsg);
						model.addAllAttributes(commandMap);

						return returnUrl;
					}
					
					/*
					 * 비밀번호변경일자가 3개월이 지난 사용자는 비밀번호 변경페이지로 이동시킨다.
					 * isOk == 999
					 * 마이페이지 >> 비밀번호변경페이지
					 */
					if(isOk == 999)
					{
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "6");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "비밀번호변경");
						resultMsg = "비밀번호변경일자가 3개월이 지났습니다. 수정하여 주시기 바랍니다..";	
						returnUrl = "forward:/usr/mpg/pwdChange.do";
						//returnUrl = "forward:/usr/hom/portalActionMainPage.do";
					}
					
					if(isOk == 1000 || isOk == 1100)
					{						
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "3");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "개인정보수정");						

						if(isOk == 1000)
						{
							resultMsg ="5자리 우편번호 변경으로 인한 우편번호 및 기타 개인정보를 수정부탁드립니다.\\n개인정보 수정페이지로 이동합니다.";
						}
						else // isOk == 1100
						{
							resultMsg ="주소를 수정하여 주시기 바랍니다. \\n개인정보 수정페이지로 이동합니다.";
						}						
						returnUrl = "forward:/usr/mpg/memMyPage.do";

						model.addAttribute("resultMsg", resultMsg);
						model.addAllAttributes(commandMap);

						return returnUrl;
					}
					
					if(isOk == 1200 || isOk == 1300)
					{						
						//메뉴코드값 강제 등록
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "3");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "개인정보수정");						

						if(isOk == 1200)
						{
							resultMsg ="5자리 우편번호 변경으로 인한 근무지 우편번호 및 기타 개인정보를 수정부탁드립니다.\\n개인정보 수정페이지로 이동합니다.";
						}
						else // isOk == 1300
						{
							resultMsg ="근무지 주소를 수정하여 주시기 바랍니다. \\n개인정보 수정페이지로 이동합니다.";
						}						
						returnUrl = "forward:/usr/mpg/memMyPage.do";

						model.addAttribute("resultMsg", resultMsg);
						model.addAllAttributes(commandMap);

						return returnUrl;
					}
					
					if (null == session.getAttribute("email") || null == session.getAttribute("handphone") || session.getAttribute("email").equals("") || session.getAttribute("handphone").equals(""))
					{
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "3");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "개인정보 수정");
						
						StringBuffer sb = new StringBuffer();
						if (null == session.getAttribute("email") || session.getAttribute("email").equals(""))
						{
							sb.append(",이메일");
						}
						if (null == session.getAttribute("handphone") || session.getAttribute("handphone").equals(""))
						{
							sb.append(",핸드폰");
						}

						resultMsg = "개인정보 수정(" + sb.substring(1) + ") 부탁드립니다.\\n개인정보 수정으로 이동합니다.";
						//returnUrl = "forward:/usr/mpg/memMyPage.do";
						returnUrl = "forward:/usr/hom/portalActionMainPage.do";
					}
					else if (commandMap.get("pwd").equals(session.getAttribute("birthDate")))
					{
						commandMap.put("menu_main", "6");
						commandMap.put("menu_sub", "6");
						commandMap.put("menu_tab_title", "마이페이지");
						commandMap.put("menu_sub_title", "비밀번호변경");
						resultMsg = "보안에 취약한 비밀번호입니다. 비밀번호변경 부탁드립니다.\\n개인정보 수정으로 이동합니다.";
						//returnUrl = "forward:/usr/mpg/pwdChange.do";
						returnUrl = "forward:/usr/hom/portalActionMainPage.do";
					}
				}
			}
		}

		//모바일에서 로그인한 경우라면.. 모바일로 url을 보낸다.
		if(loginGubun != null && loginGubun.equals("mobile"))
		{
			returnUrl = "forward:/mbl/main/index.do";
		}

		if( !resultMsg.equals("") ){
			model.addAttribute("resultMsg", resultMsg);
		}
		model.addAllAttributes(commandMap);
		
		return returnUrl;
	}
	
	@RequestMapping(value="/usr/lgn/portalUserLogout.do")
	public String portalUserLogout(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		String auth = (String)session.getAttribute("gadmin");
		if (auth != null && auth.equals("P1")) { // 권한이 강사일 경우 강사 로그아웃 정보를 입력한다.
			loginService.tutorLogout(commandMap);
		}
		
		session.invalidate();
		
		return "forward:/usr/hom/portalActionMainPage.do";
	}
	
	/**
	 * 인증서등록
	 * @param model
	 * @param commandMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/lgn/userRegistDn.do")
    public String registDn(ModelMap model, Map<String, Object> commandMap, HttpServletRequest request) throws Exception {
    	String url="usr/mem/userRegistDn";
    	String requestData = (String)commandMap.get("requestData");
    	String userId = (String)commandMap.get("userId");
    	String closeYn = (String)commandMap.get("closeYn");
	    String tIdNo = (String)commandMap.get("tIdNo");
	    //String idNo = loginService.selectUserIdNo(commandMap);
	    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  tIdNo "+tIdNo);
	   // if(idNo == null){
	   // 	idNo = "0000000000000";
	   // }
	   // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  idNo "+idNo);
	   // if(idNo.equals(tIdNo)){
	   // 	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	   // }else{
	    //	String resultMsg = "입력한 주민번호와 가입된 주민번호와 맞지 않습니다. 다시 한번 확인해 주세요.";
	    //	System.out.println("@@@@@@@@@@@  "+resultMsg);
	    //	model.addAttribute("resultMsg", resultMsg);
	    //	return "forward:/usr/mpg/memMyPage.do";
	    //}
	    
    	String strUserDN	= "";
    	String strJumin		= "";
    	//strUserDN = getEpkiDN2(request.getSession().getId(), requestData);
     	boolean blnTrue = false;
    //  epki-java 초기화
    	EpkiApi.initApp();
    		
    	try
    	{
    		// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
    		ServerConf conf = new ServerConf();
    				
    		// 복호화를 위한 키관리용 인증서 설정
    		X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
    		
    		PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_KM, "");
    				
    		// 요청자 신원확인 정보 검증
    		IdentityValidator vidData = new IdentityValidator();
    		//out.println(strRequestData);
    		//vidData.verify(cert, priKey, strRequestData, strUserID);
    		vidData.verify(cert, priKey, requestData, "");
    		
    		// 요청 데이터에 신원확인 정보가 포함되어 있는 경우
    		//vidData.verify(cert, priKey, strRequestData, "");
    		
    		// 요청자 인증서 획득
    		X509Certificate clientCert;
    		clientCert = vidData.getUserCert();
    		
    		// 요청자 인증서 검증
    		CertValidator validator = new CertValidator();	
    			
    		// 인증서 검증 옵션 지정			
    		validator.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);
    		validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ALL);
    		
    		validator.validate(CertValidator.CERT_TYPE_SIGN, clientCert);
    		
    		// 요청자 인증서 정보 획득
    		strUserDN = clientCert.getSubjectName();
    	}
    	catch (EpkiException e) 
    	{
    		System.out.println("Verify VID Error!<BR>");
    		System.out.println(e.toString());
    	}
     
        if(!strUserDN.equals("")){
        	commandMap.put("epkiDn", strUserDN);
      		
    		int irows = loginService.updateDn(commandMap);
    	  
    		if(irows > 0) {
    			blnTrue = true;
    		} else {
    			blnTrue = false;
    		}
      	}
      	
      	model.addAttribute("result", blnTrue);
      	model.addAttribute("closeYn", closeYn);
      	model.addAttribute("userDn", strUserDN);
      	
        return url;
    }
	
public String getEpkiDN2(String strSessionID, String strRequestData){
		
		
		String strAlgID			= "";
		String strSessionKey	= "";
		String strSessionIV		= "";
		String strUserDN		= new String();
		String strIssuerDN		= new String();
			
		try
		{
			//  epki-java 초기화
			EpkiApi.initApp();
			
			// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
			ServerConf conf = new ServerConf();
					
			// 복호화를 위한 키관리용 인증서 설정
			X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
			
			PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_KM, "");
					
			// 요청자 신원확인 정보 검증
			IdentityValidator vidData = new IdentityValidator();
			//out.println(strRequestData);
			//vidData.verify(cert, priKey, strRequestData, strUserID);
			vidData.verify(cert, priKey, strRequestData, "");
			
			// 요청 데이터에 신원확인 정보가 포함되어 있는 경우
			//vidData.verify(cert, priKey, strRequestData, "");
			
			// 요청자 인증서 획득
			X509Certificate clientCert;
			clientCert = vidData.getUserCert();
			
			// 요청자 인증서 검증
			CertValidator validator = new CertValidator();	
				
			// 인증서 검증 옵션 지정			
			validator.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);
			validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_CRL);
			
			validator.validate(CertValidator.CERT_TYPE_SIGN, clientCert);
			
			// 요청자 인증서 정보 획득
			strUserDN = clientCert.getSubjectName();
		}
		catch (EpkiException e) 
		{
			log.error("Epki Session Response Error!<BR>");
			log.error(e.toString());
		}
		catch (Exception e) 
		{
			log.error("Epki Session Response Error!<BR>");
			log.error(e.toString());
		}
		
		return strUserDN;
	}

	/**
	 * html5 인증서 로그인을 처리한다
	 * @param vo - 주민번호가 담긴 LoginVO
	 * @return result - 로그인결과(세션정보)
	 * @exception Exception
	 */
	@RequestMapping(value="/usr/lgn/actionCrtfctLoginHtml5.do")
	public String actionCrtfctLoginHtml5(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		System.out.println("==========================");
		String returnUrl = "";
		String resultMsg = "";
		
		//개발모드면 일반로그인으로 이동한다
		if(propertyService.getBoolean("Globals.isDevelopmentMode")) {
			return "forward:/usr/mem/userLogin.do";
		}
		
		int isOk = loginService.checkPortalYn(commandMap);
		
		//EPKI DN
	    epkiHtml5Service.getHtml5Dn(request, model, commandMap);
		
		isOk = loginService.actionCrtfctLoginHtml5(request, commandMap);
		System.out.println("@@@ isOk  "+isOk);
		if( isOk < 1 ){
			resultMsg = "존재하지 않는 아이디입니다.\\n관리자에게 문의하시기 바랍니다.";
			returnUrl = "forward:/usr/mem/userLogin.do";
		}else if(isOk == 99){
			resultMsg = "인증서가 등록되어 있지 않습니다.\\n개인정보수정에서 인증서를 등록하여 주시길 바랍니다.";
			returnUrl = "forward:/usr/mem/userLogin.do";
		}else if(isOk == 98){
			returnUrl = "forward:/usr/mpg/memMyPage.do";
			
			//메뉴코드값 강제 등록
			commandMap.put("menu_main", "6");
			commandMap.put("menu_sub", "3");
			commandMap.put("menu_tab_title", "마이페이지");
			commandMap.put("menu_sub_title", "개인정보수정");
			
			resultMsg = "나이스 개인번호를 입력하셔야 합니다.\\n개인정보 수정으로 이동합니다.";
		}else if(isOk == 97){
			returnUrl = "forward:/usr/mpg/memMyPage.do";
			
			//메뉴코드값 강제 등록
			commandMap.put("menu_main", "6");
			commandMap.put("menu_sub", "3");
			commandMap.put("menu_tab_title", "마이페이지");
			commandMap.put("menu_sub_title", "개인정보수정");
			
			resultMsg = "학교코드가 초기화 되었습니다.\\n개인정보 수정으로 이동합니다.";
		}else{
			returnUrl = "forward:/usr/hom/portalActionMainPage.do";
		}
		if( !resultMsg.equals("") ){
			model.addAttribute("resultMsg", resultMsg);
		}
		model.addAllAttributes(commandMap);
		return returnUrl;
	}
	
	/**
	 * 인증서등록
	 * @param model
	 * @param commandMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/lgn/userRegistDnHtml5.do")
    public String userRegistDnHtml5(ModelMap model, Map<String, Object> commandMap, HttpServletRequest request) throws Exception {
    	String url="usr/mem/userRegistDn";
    	
    	//EPKI DN
		epkiHtml5Service.getHtml5Dn(request, model, commandMap);
		
      	String strUserDN = commandMap.get("dn")  !=null ?  commandMap.get("dn").toString() : "";
      	String closeYn = (String)commandMap.get("closeYn");
      	
      	boolean blnTrue = false;
      	
		 if(!strUserDN.equals("")){
	        	commandMap.put("epkiDn", strUserDN);
	      		
	    		int irows = loginService.updateDn(commandMap);
	    	  
	    		if(irows > 0) {
	    			blnTrue = true;
	    		} else {
	    			blnTrue = false;
	    		}
	      	}
		 
      	model.addAttribute("result", blnTrue);
      	model.addAttribute("closeYn", closeYn);
      	model.addAttribute("userDn", strUserDN);
      	
        return url;
    }
	
}
