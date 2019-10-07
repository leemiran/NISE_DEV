<%@page language="java" contentType="text/html; charset=UTF-8" import="javax.servlet.http.HttpSession,
                java.math.BigInteger, com.epki.api.EpkiApi, com.epki.session.SecureSession, com.epki.cert.X509Certificate,
				com.epki.cert.CertValidator, com.epki.crypto.*, com.epki.conf.ServerConf,
				com.epki.util.Base64, com.epki.exception.*, com.epki.cms.*, org.json.simple.*, java.io.IOException"%>

<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

    String loginReqestData = request.getParameter("vidRequest");

    String testResult = "";

    String subjectDn = "";
    String issuerDn = "";
    BigInteger bserial = null;
    String reqVid = "";

    try {
        EpkiApi.initApp();
        ServerConf serverConf = new ServerConf();
        X509Certificate cert = serverConf.getServerCert(ServerConf.CERT_TYPE_KM);

        PrivateKey priKey = serverConf.getServerPriKey(ServerConf.CERT_TYPE_KM);

        Base64 b64 = new Base64();

        EnvelopedData envloped = new EnvelopedData();
        byte[] developedData = envloped.develop(b64.decode(loginReqestData), cert, priKey);

        SignedData signedData = new SignedData();

        signedData.verify(new String(developedData));
        byte[] plainBytes = signedData.getMessage();
				
        X509Certificate loginCert = signedData.getSignerCert(0);
				
        // JSON Parse Start
        JSONObject reqJsonData = (JSONObject)JSONValue.parse(new String(plainBytes));

        // Get RNumber
        String rNumber = (String)reqJsonData.get("RN");

        // Vid
        String vid = (String)reqJsonData.get("VID");
				
        if(vid != null) {
            // 요청자 신원확인 정보 검증
            loginCert.verifyVID(b64.decode(rNumber), vid);
				    reqVid = vid;
		}
		
		/* 인증서 유효성 검증을 하기 위해서는 아래 CertValidator 를 사용하여야 합니다. */
		
		
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
		//validator.setServerSignCert( serverConf.getServerCert(ServerConf.CERT_TYPE_SIGN),serverConf.getServerPriKey(ServerConf.CERT_TYPE_SIGN));
        
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
       
        
        // 요청자 인증서 정보 획득
        subjectDn = loginCert.getSubjectName();
        issuerDn = loginCert.getIssuerName();
        bserial = new BigInteger("1");
        bserial = loginCert.getSerialNumber();

        testResult = "성공";

    } catch (EpkiException e) {
    		e.printStackTrace();
        testResult = e.toString();
    } catch (IOException e) {
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
    <h2>인증서 기반 신원확인</h2>
    <div>이 페이지는 사용자 인증서를 기반으로 서버에서 신원을 확인하는 기능을 테스트합니다.</div>
    <section>
        <div id="vid_test" class="api-description-box">
            <h3 class="api-description-msg">인증서 기반 신원확인</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    인증서 내에 주입된 주민등록번호 및 사업자등록번호 등과 같은 식별번호를 이용하여 신원확인하는 기능에 대하여 테스트 합니다.<br/>
                    개인인 경우에는 주민등록번호를 이용합니다.<br/>
                    법인인 경우에는 사업자등록번호를 이용합니다.<br/>
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
                                        테스트 결과
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
                                        <textarea ondblclick="textareaResize(this)"><%=loginReqestData%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        요청 식별번호
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=reqVid%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        사용자 DN
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=subjectDn%></textarea>
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
