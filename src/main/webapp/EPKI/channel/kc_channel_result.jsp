<%@page language="java" contentType="text/html; charset=UTF-8"
        import="javax.servlet.http.HttpSession, com.epki.api.EpkiApi, com.epki.conf.ServerConf, com.epki.cms.EnvelopedData,
                com.epki.cert.X509Certificate, com.epki.exception.EpkiException, com.epki.util.Base64,
                com.epki.crypto.*, com.epki.session.*, java.io.IOException"
%>

<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

    String requestMessage = request.getParameter("secureChannelRequest");
    String testResult = "";

    String strAlgID = "";
    String strSessionKey = "";
    String strSessionIV = "";
	
    try {
        EpkiApi.initApp();

				Base64 b64 = new Base64();
        byte[] bsessionKey = null;
        byte[] bsessionIV = null;

        ServerConf serverConf = new ServerConf();
		
        X509Certificate cert = serverConf.getServerCert(ServerConf.CERT_TYPE_KM);
        PrivateKey priKey = serverConf.getServerPriKey(ServerConf.CERT_TYPE_KM);
        
        EnvelopedData envloped = new EnvelopedData();
				byte[] developedData = envloped.develop(b64.decode(requestMessage), cert, priKey);
				SecretKey secretKey = envloped.getSecretKey();

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

				if(strAlgID == "DESEDE"){
					strAlgID = "3DES";
				}

        testResult = "성공";

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
        <div id="" class="api-description-box">
            <h3 class="api-description-msg">채널보안 결과</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    클라이언트와 서버 간 보안채널을 형성하는 기능을 테스트 합니다.<br/>
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
                                        클라이언트에서 전송된 <br/>요청 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=requestMessage%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        암호화 알고리즘
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strAlgID%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        세션 키
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strSessionKey%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        IV
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strSessionIV%></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" onclick="javascript:history.back()" type="button" value="이전" />
                        <input class="kc-btn-blue" onclick="javascript:location.href='kc_login.jsp'" type="button" value="다음" />
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
