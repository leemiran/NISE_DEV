<%@page language="java" contentType="text/html; charset=UTF-8" import="javax.servlet.http.HttpSession,
                java.math.BigInteger, com.epki.api.EpkiApi, com.epki.session.SecureSession, com.epki.cert.X509Certificate,
				com.epki.cert.CertValidator, com.epki.crypto.*, com.epki.conf.ServerConf,
				com.epki.util.Base64, com.epki.exception.*"%>	

<%
	HttpSession m_Session = request.getSession();
	String sessionID = m_Session.getId();
	String strRequestData = request.getParameter("SessionRequestData");
	
	String strAlgID = "";
	String strSessionKey = "";
	String strSessionIV = "";
	String strUserDN = new String();
	String strIssuerDN = new String();
	
	String chkValue = "";
	//  epki-java 초기화
	EpkiApi.initApp();
		
	try
	{
		if(strRequestData ==null) throw new Exception("전달 파라미터 값이 없습니다. 로그인 후 다시 사용하세요");

		byte[] bsessionKey = null;
		byte[] bsessionIV = null;
		SecureSession secureSession = new SecureSession();
		secureSession.setSessionID(sessionID);
		
		// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
		ServerConf conf = new ServerConf();
				
		// 복호화를 위한 키관리용 인증서 설정
		X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
		
		PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_KM, "");
		
		secureSession.response(cert, priKey, strRequestData);
		
		// 보안 세션 요청자 인증서 획득
		X509Certificate clientCert;
		clientCert = secureSession.getClientCert();
		
		// 요청자 인증서 검증
		CertValidator validator = new CertValidator();	
			
		// 인증서 검증 옵션 지정
		// 인증서 체인 구성 옵션	
		validator.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);

		// [기본 설정]
		// ARL,CRL 검증
		validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ARL | CertValidator.REVOKE_CHECK_CRL);
		// OCSP 검증 
		// 사용장 인증서 내 AIA 필드에 OCSP 서버 정보가 없을 경우 server.conf 내 OCSP_Server 변수에 기본 OCSP 검증 서버 설정 가능
		// PrivateKey priKey_Sign = conf.getServerPriKey(ServerConf.CERT_TYPE_SIGN, "");
		// validator.setOCSPCert(conf.getServerCert(1),priKey_Sign);
		// validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ARL | CertValidator.REVOKE_CHECK_CRL);		
		
		validator.validate(CertValidator.CERT_TYPE_SIGN, clientCert);
		
		// 요청자 인증서 정보 획득
		strUserDN = clientCert.getSubjectName();
		strIssuerDN = clientCert.getIssuerName();
		BigInteger bserial = new BigInteger("1");
		bserial = clientCert.getSerialNumber();
		
		// 세션키 획득
		SecretKey secretKey = secureSession.getSK();
		
		strAlgID = secretKey.getKeyAlg();
		bsessionKey = secretKey.getKey();
		bsessionIV = secretKey.getIV();
		
		// 세션키 Base64 인코딩
		Base64 encoder = new Base64();
		strSessionKey = encoder.encode(bsessionKey);
		strSessionIV = encoder.encode(bsessionIV);
		
		m_Session.setAttribute("sessionKey",strSessionKey);
		m_Session.setAttribute("sessionIV",strSessionIV);
		m_Session.setAttribute("AlgID", strAlgID);
		m_Session.setAttribute("UserDN", strUserDN);
		m_Session.setAttribute("IssuerDN", strIssuerDN);
		m_Session.setAttribute("Serial", bserial);

		out.println("	<SCRIPT>													");
		out.println("		alert('인증에  성공하였습니다.');									");
		out.println("		document.formSub.action='/usr/mem/memJoinStep03.do';	");
		out.println("		document.formSub.target='_self';						");
		out.println("		document.formSub.submit();								");
		out.println("	</SCRIPT>													");
//		out.println("아놔 왜 안가는겨....");
		response.sendRedirect("/usr/mem/memJoinStep03.do?developMode=Z");
	}
	catch (EpkiException e) 
	{
		out.println("Session Response Error!<BR>");
		out.println(e.toString());
		return;
	}catch(CertificateExpiredException e){
//		out.println("유효하지 않은 인증서입니다.!<BR>");
//		out.println(e.toString());
		out.println("<SCRIPT language='javascript'>");
		out.println("alert('유효하지 않은 인증서입니다.')");
		out.println("history.back()");		
		out.println("</SCRIPT>");		
		return;
	}
	catch (Exception e) 
	{
		out.println("Session Response Error!<BR>");
		out.println(e.toString());
		return;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>인증서 로그인 및 채널보안</title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8" /> 
<link href="default.css" rel="stylesheet" type="text/css" media="screen" />
<!-- EPKI Client Toolkit 공용 스크립트 -->
<SCRIPT language="javascript" src="./EPKICommon.js"></SCRIPT>

</head>
<body>
<div id="header">


	<form name="formSub" id="formSub" method="post">
		<input name="developMode" type="hidden" value="Z"/>		
	</form>
	</div>
</body>
</html>