<%@page language="java" contentType="text/html; charset=UTF-8" import="javax.servlet.http.HttpSession,
                java.math.BigInteger, com.epki.api.EpkiApi, com.epki.session.SecureSession, com.epki.cert.X509Certificate,
				com.epki.cert.CertValidator, com.epki.crypto.*, com.epki.conf.ServerConf,
				com.epki.util.Base64, com.epki.exception.*, com.epki.cms.*, org.json.simple.*, java.io.IOException"%>
<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

    String loginRequestData = request.getParameter("reqSecLoginData");

    String testResult = "";

    String subjectDn = "";
    String issuerDn = "";
    BigInteger bserial = null;

    String strAlgID = "";
    String strSessionKey = "";
    String strSessionIV = "";

    try {
        EpkiApi.initApp();

        Base64 base64 = new Base64();

        byte[] bsessionKey = null;
        byte[] bsessionIV = null;
        byte[] bencryptedData = null;
        byte[] bdecryptData = null;

        // 서버 설정 객체를 생성합니다.
        ServerConf serverConf = new ServerConf();

        // 서버 인증서 정보를 가져옵니다.
        X509Certificate cert = serverConf.getServerCert(ServerConf.CERT_TYPE_KM);
        PrivateKey priKey = serverConf.getServerPriKey(ServerConf.CERT_TYPE_KM);
		
				// 채널보안 로그인 동시수행 메시지에서 채널보안 키 정보를 읽어옵니다.
				EnvelopedData envloped = new EnvelopedData();
				byte[] developedData = envloped.develop(base64.decode(loginRequestData), cert, priKey);
		
				// 채널보안에서 사용할 키를 가져옵니다.
        SecretKey secretKey = envloped.getSecretKey();
		
				SignedData signedData = new SignedData();
		
				// 채널보안 로그인 동시수행 메시지에서 로그인 요청 메시지를 읽어옵니다.
				signedData.verify(base64.decode(new String(developedData)));
				byte[] reqLoginBytes = signedData.getMessage();
		
				// JSON Parse Start
				JSONObject reqJsonData = (JSONObject)JSONValue.parse(new String(reqLoginBytes));

        // 로그인 요청 메시지에서 R값을 추출합니다.
        String rNumber = (String)reqJsonData.get("RN");

        // 로그인 요청 메시지에서 VID 값을 읽어옵니다.
        String vid = (String) reqJsonData.get("VID");
		
				// 인증 요청 메시지로부터 클라이언트 인증서를 획득합니다.
        X509Certificate loginCert = signedData.getSignerCert(0);
		
        if(vid != null) {
            // 요청자 VID 정보가 포함될 경우 신원확인을 합니다.
            loginCert.verifyVID(base64.decode(rNumber), vid);
        }

        // 인증서를 검증하기 위해 CertValidator 객체를 생성합니다.
        CertValidator validator = new CertValidator();

        // 클라이언트 인증서를 검증할 옵션을 설정합니다.
        validator.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);
        validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_CRL);

		// [기본 설정]
		// SCVP 검증
		//String scvpUrl = "http://scvp.epki.go.kr:8080";			// SCVP 검증서버 IP:PORT
		//validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_SCVP);
		//validator.setSCVPUrl(scvpUrl);
		//validator.setServerSignCert( //serverConf.getServerCert(ServerConf.CERT_TYPE_SIGN),serverConf.getServerPriKey(ServerConf.CERT_TYPE_SIGN));
        
        // CRL 검증
        //validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_CRL);

        // ARL,CRL 검증
        //validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ARL | CertValidator.REVOKE_CHECK_CRL);

        // NONE
        //validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_NONE);

        // OCSP 검증
        // 사용자 인증서 내 AIA 필드에 OCSP 서버 정보가 없을 경우 server.conf 내 OCSP_Server 변수에 기본 OCSP 검증 서버 설정 가능
        // validator.setServerSignCert( serverConf.getServerCert(ServerConf.CERT_TYPE_SIGN),serverConf.getServerPriKey(ServerConf.CERT_TYPE_SIGN));
        // validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_OCSP);
        
        // 클라이언트 인증서를 검증합니다.
        validator.validate(CertValidator.CERT_TYPE_SIGN, loginCert);

        strAlgID = secretKey.getKeyAlg();
        bsessionKey = secretKey.getKey();
        bsessionIV = secretKey.getIV();

        // 세션키 Base64 인코딩
        Base64 encoder = new Base64();
        strSessionKey = encoder.encode(bsessionKey);
        strSessionIV = encoder.encode(bsessionIV);

        m_Session.setAttribute("sessionKey",strSessionKey);
        m_Session.setAttribute("sessionIV",strSessionIV);
        m_Session.setAttribute("algorithm", strAlgID);

        // 요청자 인증서 정보 획득
        subjectDn = loginCert.getSubjectName();
        issuerDn = loginCert.getIssuerName();
        bserial = new BigInteger("1");
        bserial = loginCert.getSerialNumber();

        testResult = "성공";

    } catch (CertificateExpiredException e) {
        testResult = "유효기간이 만료된 인증서입니다. " + e.toString();
    } catch (CertificateRevokedException e) {
        testResult = "폐지된 인증서입니다. " + e.toString();
    } catch (EpkiException e) {
        testResult = e.toString();
    } 
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>KSign CASE Test</title>

    <!-- 부트스트랩  CSS -->
    <link rel="stylesheet" href="../../kcase/lib/css/bootstrap.min.css"/>

    <!-- KSign Style Sheet -->
    <link rel="stylesheet" href="../../kcase/lib/css/kcase.css"/>

    <!-- Sample CSS -->
    <link rel="stylesheet" href="../../kcase/lib/css/sample.css"/>

    <script src="../../kcase/lib/js/jquery-1.11.3.js"></script>
    <script src="../../kcase/lib/js/jquery-ui.js"></script>

    <script src="../../kcase/lib/js/kcase_os_check.js"></script>
    <script src="../../kcase/EPKICommon.js"></script>

    <script type="text/javascript">
        $(document).ready(function() {

            var sessionId = '<%=sessionID%>';

            /* init - server's sessionId */
            epki.init(sessionId);

            $("footer").css("top", $(window).scrollTop() + $(window).height() - 30);

            $(window).scroll(function () {
                $("footer").css("top", $(window).scrollTop() + $(window).height() - 30);
            });

            $(window).resize(function () {
                $("footer").css("top", $(window).scrollTop() + $(window).height() - 30);
            });
        });
    </script>
</head>
<body>

<div class="header">
    <h1 style="margin: 0px;">
        <div style="height:30px;"></div>
        <span onclick="javascript:location.href='../index.jsp'">HTML5 KSignCase-Agent Server API Test</span>
        <br/>
        <div>KSign Toolkit Development Dept.</div>
    </h1>
</div>

<div style="width:100%; height:1px; background-color: mediumpurple"></div>

<div class="content">
    <h2>인증서 로그인 및 채널보안</h2>
    <div>이 페이지는 인증서 로그인과 채널보안 및 신원확인에 대하여 테스트합니다.</div>
    <section>
        <div id="test_basic_login" class="api-description-box">
            <h3 class="api-description-msg">인증서 로그인 결과</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    인증서 로그인 요청에 대한 서버의 처리결과는 "로그인한 사용자 DN" 필드에서 확인할 수 있습니다.<br/>
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        로그인 결과
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=testResult%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        클라이언트에서 전송된 요청 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=loginRequestData%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        로그인한 사용자 DN
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=subjectDn%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        발급자 DN
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=issuerDn%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        인증서 시리얼 넘버
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=bserial%></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" onclick="javascript:history.back()" type="button" value="이전" />
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<footer style="position: absolute;">
    <div>
        Copyright 2016. KSIGN all rights reserved.
    </div>
</footer>
</body>
</html>
