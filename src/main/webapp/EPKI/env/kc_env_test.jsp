<%@page language="java" contentType="text/html; charset=UTF-8" import="com.epki.api.EpkiApi,
				com.epki.util.Base64, com.epki.cms.EnvelopedData, com.epki.cert.X509Certificate,
				com.epki.crypto.*, com.epki.conf.ServerConf, com.epki.exception.EpkiException"%>

<%
		
    String strAlgID = request.getParameter("algorithm");
    String strEnvelopedData = request.getParameter("envelopedData");
    String strDevelopedData = "";

    String testResult = "";

    try
    {
        //  epki-java 초기화
        EpkiApi.initApp();

        byte[] bEnvelopedData = null;
        byte[] bDevelopData = null;

        // 전송 데이터 Base64 디코딩
        Base64 decoder = new Base64();
        bEnvelopedData = decoder.decode(strEnvelopedData);

        EnvelopedData envelopedData = new EnvelopedData(strAlgID);

        // server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
        ServerConf conf = new ServerConf();

        // 복호화를 위한 키관리용 인증서 설정
        X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);
        PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_KM);

        bDevelopData = envelopedData.develop(bEnvelopedData, cert, priKey);
        strDevelopedData = new String(bDevelopData);

        testResult = "성공";
    } catch (EpkiException e) {
        testResult = "Develop Error!" + e.toString();
    } catch (Exception e) {
        testResult = "Develop Error!" + e.toString();
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
    <h2>공개키 암호화/복호화</h2>
    <div>이 페이지는 공개키로 암호화하고 개인키로 복호화하는 기능을 테스트합니다.</div>
    <section>
        <div id="env_test" class="api-description-box">
            <h3 class="api-description-msg">클라이언트 암호화, 서버 복호화</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    암호화 결과는 "클라이언트에서 전송된 암호화 메시지" 필드에서 확인할 수 있습니다.<br/>
                    복호화 결과는 "서버에서 복호화한 값" 필드에서 확인할 수 있습니다.
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
                                        클라이언트에서 전송된 암호화 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strEnvelopedData%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서버에서 복호화한 값
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strDevelopedData%></textarea>
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
