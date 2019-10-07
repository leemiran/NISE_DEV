<%@page language="java" contentType="text/html; charset=UTF-8"
        import="javax.servlet.http.HttpSession, com.epki.api.EpkiApi, com.epki.conf.ServerConf,
                com.epki.cert.X509Certificate, com.epki.exception.EpkiException, com.epki.util.Base64"%>

<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

    String strServerCert = "";

    try {
        //  epki-java 초기화
        EpkiApi.initApp();

        byte[] bsserverCert = null;

        // server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
        ServerConf conf = new ServerConf();

        // 복호화를 위한 키관리용 인증서 설정
        X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);

        bsserverCert = cert.getCert();

        // 서버인증서 Base64 인코딩
        Base64 encoder = new Base64();
        strServerCert = encoder.encode(bsserverCert);
    } catch (EpkiException e) {
        out.println("Session Request Error!<BR>");
        out.println(e.toString());
        return;
    } catch (Exception e) {
        out.println("Session Request Error!<BR>");
        out.println(e.toString());
        return;
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
            var serverCert = '<%=strServerCert%>';

            /* init - server's sessionId */
            epki.init(sessionId);

            $("#genBtn").click(function() {
                epki.requestSession(serverCert, "SEED", sessionId, getReqSessionData, error);

                function error(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                function getReqSessionData(output) {
                    $("#reqSessionData").text(output);
                    epki.login(serverCert, getLoginData, error);
                }

                function getLoginData(output) {
                    $("#reqLoginData").text(output);

                    // Submit call
                    $("#reqForm").submit();
                }
            });

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
    <input type="button" class="kc-btn-blue" id="genBtn" value="생성"/>
    <form name="request" id="reqForm" method="post" action="./kc_channelandlogin_result.jsp">
        <textarea name="reqSessionData" id="reqSessionData"></textarea>
        <textarea name="reqLoginData" id="reqLoginData"></textarea>
    </form>
</div>
<footer style="position: absolute;">
    <div>
        Copyright 2016. KSIGN all rights reserved.
    </div>
</footer>
</body>
</html>
