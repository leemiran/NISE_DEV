<%@page language="java" contentType="text/html; charset=UTF-8" import="javax.servlet.http.HttpSession,
                java.math.BigInteger, com.epki.api.EpkiApi, com.epki.session.SecureSession, com.epki.cert.X509Certificate,
				com.epki.cert.CertValidator, com.epki.crypto.*, com.epki.conf.ServerConf, com.epki.cms.EnvelopedData,
				com.epki.util.Base64, com.epki.exception.*, com.epki.cms.*, org.json.simple.*, java.io.IOException"%>
<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

    String encRequestData = request.getParameter("reqSecEncData");

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

        // SecureSession 객체를 생성합니다.
        SecureSession secureSession = new SecureSession();
		
				// 채널보안 암호화 동시수행 메시지에서 채널보안 키 정보를 읽어옵니다.
				EnvelopedData envloped = new EnvelopedData();
				byte[] developedData = envloped.develop(base64.decode(encRequestData), cert, priKey);
		
		// 채널보안에서 사용할 키를 가져옵니다.
        SecretKey secretKey = envloped.getSecretKey();
		
        strAlgID = secretKey.getKeyAlg();
        bsessionKey = secretKey.getKey();
        bsessionIV = secretKey.getIV();

        // 세션키 Base64 인코딩
        strSessionKey = base64.encode(bsessionKey);
        strSessionIV = base64.encode(bsessionIV);

        m_Session.setAttribute("sessionKey",strSessionKey);
        m_Session.setAttribute("sessionIV",strSessionIV);
        m_Session.setAttribute("algorithm", strAlgID);

        testResult = new String(base64.decode(new String(developedData)), "UTF-8");

    } catch (EpkiException e) {
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
    <h2>채널보안 암호화 동시수행</h2>
    <div>이 페이지는 채널보안을 통해 원문을 암호화하여 전송한 값에 대한 결과를 확인합니다.</div>
    <section>
        <div id="test_basic_login" class="api-description-box">
            <h3 class="api-description-msg">채널보안 암호화 동시수행 결과</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    채널보안을 통해 전송된 원문은 서버에서 복호화한 메시지 필드에서 확인할 수 있습니다.<br/>
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
                                        클라이언트에서 전송된 요청 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=encRequestData%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서버에서 복호화한 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=testResult%></textarea>
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
