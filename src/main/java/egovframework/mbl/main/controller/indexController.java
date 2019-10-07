package egovframework.mbl.main.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epki.api.EpkiApi;
import com.epki.cert.X509Certificate;
import com.epki.cms.EnvelopedData;
import com.epki.conf.ServerConf;
import com.epki.exception.EpkiException;
import com.epki.util.Base64;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.tmp.service.TempletManageService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.usr.lgn.service.LoginService;


import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;



@Controller
public class indexController {

	/** log */
	protected static final Log log = LogFactory.getLog(indexController.class);
	
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
	
	
	/**
	 * 모바일 > 문의안내
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/main/index.do")
	public String index(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "mbl/main/index";
	}
	
	
	/**
	 * 모바일 > 로그인
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/main/login.do")
	public String login(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		model.addAllAttributes(commandMap);
		return "mbl/main/login";
	}
	
	/**
	 * 모바일 > 로그아웃
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/main/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		
		session.invalidate();
		
		return "forward:/mbl/main/index.do";
	}
	
	/**
	 * 모바일 > 로그인
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mbl/main/loginMobile.do")
	public String loginTest(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024);
		KeyPair keyPair = generator.genKeyPair();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
 
		session.setAttribute("_RSA_WEB_Key_", privateKey);   //세션에 RSA 개인키를 세션에 저장한다.
		RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		String publicKeyModulus = publicSpec.getModulus().toString(16);
		String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
 
		request.setAttribute("RSAModulus", publicKeyModulus);  //로그인 폼에 Input Hidden에 값을 셋팅하기위해서
		request.setAttribute("RSAExponent", publicKeyExponent);   //로그인 폼에 Input Hidden에 값을 셋팅하기위해서

		
		model.addAllAttributes(commandMap);
		return "mbl/main/loginMobile";
	}
	
	
	@RequestMapping(value="/usr/lgn/portalUserLoginMobile.do")
	public String userLoginTest(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		HttpSession session = request.getSession();
		
		String strAlgID = (String)commandMap.get("SelectedAlgID");
		String strEnvelopedData = (String)commandMap.get("userId");
		String strEnvelopedData1 = (String)commandMap.get("pwd");
		String loginGubun = (String)commandMap.get("loginGubun");
		String strDevelopedData = "";
		String strDevelopedData1 = "";
		
		try {
						
			
			System.out.println("strEnvelopedData================> " +strEnvelopedData);
			System.out.println("strEnvelopedData1================> " +strEnvelopedData1);
			
			PrivateKey privateKey = (PrivateKey) session.getAttribute("_RSA_WEB_Key_");  //로그인전에 세션에 저장된 개인키를 가져온다.
			
			
			//암호화처리된 사용자계정정보를 복호화 처리한다.
			strEnvelopedData = decryptRsa(privateKey, strEnvelopedData);
			strEnvelopedData1 = decryptRsa(privateKey, strEnvelopedData1);
			//복호화 처리된 계정정보를 map에 담아서 iBatis와 연동한다.
			
			System.out.println("strEnvelopedData 22222 ================> " +strEnvelopedData);
			System.out.println("strEnvelopedData1 22222 ================> " +strEnvelopedData1);
			
			commandMap.remove("userId");
			commandMap.remove("pwd");	
			commandMap.put("userId", strEnvelopedData);
			commandMap.put("pwd", strEnvelopedData1);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("e ---> "+e);
		}
		
		
		
		/*if(loginGubun == null){
			System.out.println("@@@@");
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
		}*/
		
		
		// 회사별로 포탈 로그인 가능여부에 따라 제한을 둔다.
		int isOk = loginService.checkPortalYn(commandMap);
		
		int lgexceptcnt  = Integer.valueOf(Globals.LOGIN_EXCEPT_VALUE);
		String lgexcept = Globals.LOGIN_EXCEPT_ISUSE;
		
		String resultMsg = "";
		String issso = (String)commandMap.get("p_issso");
		String check = (String)commandMap.get("p_chk");
		String returnUrl = "forward:/usr/hom/portalActionMainPage.do";
		
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
							resultMsg = "비밀번호 " + (String)commandMap.get("p_lgfailcnt")
							+ "회 오류입니다.\\n" + lgexceptcnt
							+ "회연속 오류시 관리자에게 문의하여 주십시오."; // 퇴직자
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
					resultMsg = "회원님께서는 탈퇴하셨습니다.\\n현 사이트에서의 재가입은 탈퇴신청일로 부터 3개월 이후 가능합니다.\\n좀 더 빠른 시일내에 재가입을 원하신다면 운영자 "
							+ EgovProperties.getProperty("admin.tel")
							+ " 에게 연락해 주시기 바랍니다.";
				}else if( isOk == -7 ){
					resultMsg = "바람직하지 않는 방법으로 접근하셨습니다.";
				}else if( isOk == -8 ){
					resultMsg = "5분 후에 로그인을 다시 해주시기 바랍니다.";
				}else if( isOk == -9 ){
					resultMsg = "접근이 제한 되었습니다. 시스템관리자에게 문의하시기 바랍니다.";
				}else if( isOk == -10 ){
					resultMsg = "접근이 제한 되었습니다. 시스템관리자에게 문의하시기 바랍니다...";				
				}else{ //로그인 성공
					
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
	
	
	public String decryptRsa(PrivateKey privateKey, String securedValue) {
		 String decryptedValue = "";
		 try{
			Cipher cipher = Cipher.getInstance("RSA");
		   /**
			* 암호화 된 값은 byte 배열이다.
			* 이를 문자열 폼으로 전송하기 위해 16진 문자열(hex)로 변경한다. 
			* 서버측에서도 값을 받을 때 hex 문자열을 받아서 이를 다시 byte 배열로 바꾼 뒤에 복호화 과정을 수행한다.
			*/
			byte[] encryptedBytes = hexToByteArray(securedValue);
			cipher.init(Cipher.DECRYPT_MODE, (Key)privateKey);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
		 }catch(Exception e)
		 {
			 //logger.info("decryptRsa Exception Error : "+e.getMessage());
		 }
			return decryptedValue;
	} 
	/** 
	 * 16진 문자열을 byte 배열로 변환한다. 
	 */
	 public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[]{};
		}
	 
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}
	
}
