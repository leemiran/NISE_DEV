<%@page language="java" contentType="text/html; charset=UTF-8"
        import="com.epki.api.EpkiApi, com.epki.conf.ServerConf,
                com.epki.cert.X509Certificate, com.epki.exception.EpkiException, com.epki.util.Base64"%>

<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

    String strServerCert = "";
    String testResult = "";
	
    try
    {
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
        testResult = e.getMessage();
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

            var envInputs = $("#env_test .api-btn-list").find("input");
            var envTexts = $("#env_test .table-input-text").find("textarea");
            envInputs.eq(0).click(function() {
                envTexts.val("");
            });
            envInputs.eq(1).click(function() {
                var select = $("#env_test").find("select");
                var algorithm = select.find("option:selected").text();

                /* Call Back Function Definition */
                var success = function(output) {
                    envTexts.eq(1).val(output);
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                var plainText = envTexts.eq(0).val();

                epki.envelop(algorithm, serverCert, plainText, success, error);
            });

            envInputs.eq(2).click(function() {
                $("#env_test").find("form").submit();
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
    <h2>공개키 암호화/복호화</h2>
    <div>이 페이지는 공개키로 암호화하고 개인키로 복호화하는 기능을 테스트합니다.</div>
    <section>
        <div id="env_test" class="api-description-box">
            <h3 class="api-description-msg">클라이언트 암호화, 서버 복호화</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    클라이언트에서 지정된 문자열을 공개키(인증서)로 암호화하고 이를 전달받은 서버가 개인키로 복호화하는 기능에 대하여 테스트 합니다.<br/><br/>

                원본 문자열은 대칭키로 암호화 하고, 대칭키 정보는 공개키(인증서)로 암호화 한 후 이를 하나의 암호메시지(EnvelopedData)로 구성합니다.<br/>
                원본 문자열 암호화에 사용되는 대칭키 암호알고리즘은 별도로 지정되어야 하며, 대칭키 정보 암호화에 사용되는 공개키 암호알고리즘은 인증서 내의 정보를 따릅니다.<br/>
                암호화에 사용되는 서버의 공개키(인증서)는 표준API 클라이언트 모듈 설치시 자동으로 배포되었습니다.<br/>
                실제 운용환경에서 본 기능을 사용하는 경우, 암호화에 사용하는 웹 서버의 인증서는 별도의 전달과정이 반드시 필요합니다.<br/>
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_env_test.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        암호화 <br/>알고리즘
                                    </td>
                                    <td class="table-input-text">
                                        <select name="algorithm">
                                            <option>SEED</option>
                                            <option>ARIA</option>
                                            <option>3DES</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        암호화할 <br/>메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=testResult%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        생성된 암호문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="envelopedData" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="초기화" />
                        <input class="kc-btn-blue" type="button" value="암호화" />
                        <input class="kc-btn-blue" type="button" value="복호화" />
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
