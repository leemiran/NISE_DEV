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

            epki.init(sessionId);

            var basicLoginInputs = $("#test_basic_login .api-btn-list").find("input");
            var basicLoginTexts = $("#test_basic_login .table-input-text").find("textarea");
            basicLoginInputs.eq(0).click(function() {

                /* Call Back Function Definition */
                var success = function(output) {
                    basicLoginTexts.eq(0).val(output);
                    $("#test_basic_login").find("form").submit();
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                epki.login(serverCert, success, error);

            });

            var vidLoginInputs = $("#test_vid_login .api-btn-list").find("input");
            var vidValue = $("#test_vid_login .table-input-text").find("input");
            var vidLoginTexts = $("#test_vid_login .table-input-text").find("textarea");

            vidLoginInputs.eq(0).click(function() {

                var vid = vidValue.eq(0).val();

                if(vid.length == 0) {
                    alert("식별 번호를 입력해 주십시오.");
                    return;
                }

                /* Call Back Function Definition */
                var success = function(output) {
                    vidLoginTexts.eq(0).val(output);
                    $("#test_vid_login").find("form").submit();
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                epki.vidLogin(serverCert, vid, success, error);
            });

            var channelEncryptInputs = $("#channel_encrypt_test .api-btn-list").find("input");
            var channelEncryptTexts = $("#channel_encrypt_test .table-input-text").find("textarea");

            channelEncryptInputs.eq(0).click(function() {
                /* Call Back Function Definition */
                var success = function(output) {
                    channelEncryptTexts.eq(1).val(output);
                    $("#channel_encrypt_test").find("form").submit();
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                var plainData = channelEncryptTexts.eq(0).val();

                epki.sessionEncrypt(sessionId, plainData, success, error);
            });

            var channelDecryptInputs = $("#channel_decrypt_test .api-btn-list").find("input");
            var channelDecryptTexts = $("#channel_decrypt_test .table-input-text").find("textarea");

            channelDecryptInputs.eq(0).click(function() {
                if(channelDecryptTexts.eq(0).val().length <= 0) {
                    alert("암호화할 메시지를 입력해주십시오.");
                    return;
                }

                $("#channel_decrypt_test").find("form").submit();
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
    <h2>인증서 로그인 및 채널보안</h2>
    <div>이 페이지는 인증서 로그인과 채널보안 및 신원확인에 대하여 테스트합니다.</div>
    <section>
        <div id="test_basic_login" class="api-description-box">
            <h3 class="api-description-msg">인증서 로그인</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    클라이언트에서 인증서를 이용하여 로그인하는 기능에 대하여 테스트 합니다.<br/>
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_login_basic.jsp" method="post" style="display: none">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        인증서 로그인<br/>요청 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="loginRequest" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="공인인증서 로그인" />
                    </div>
                </div>
            </div>
        </div>
        <div id="test_vid_login" class="api-description-box">
            <h3 class="api-description-msg">인증서 로그인(신원확인 동시 수행)</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    클라이언트에서 인증서를 이용하여 로그인하고 보안채널을 구성하는 기능에 대하여 테스트 합니다.<br/>
                    인증서 로그인이 성공되는 경우 보안채널이 자동으로 구성됩니다.<br/>
                    인증서 로그인 후 구성된 보안채널 정보(세션정보)를 이용하여 웹 폼데이터에 대한 대칭키 암호화/복호화가 가능합니다.</br>
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_login_vid.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        식별번호
                                    </td>
                                    <td class="table-input-text">
                                        <input type="password" />
                                    </td>
                                </tr>
                                <tr style="display: none">
                                    <td class="table-input-name">
                                        인증서 로그인<br/>요청 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="loginRequest" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="공인인증서 로그인" />
                    </div>
                </div>
            </div>
        </div>
        <div id="channel_encrypt_test" class="api-description-box">
            <h3 class="api-description-msg">채널보안 암호화</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    채널보안을 생성한 후 클라이언트에서 지정된 문자열을 대칭키로 암호화하고 이를 전달받은 서버가 대칭키로 복호화하는 기능에 대하여 테스트합니다.<br/><br/>
                    테스트를 위하여 보안채널 구성이 사전에 수립되어야 합니다.
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_channel_client.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        암호화할 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                                <tr style="display: none">
                                    <td class="table-input-name">
                                        암호문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="encryptedMessage" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="다음" />
                    </div>
                </div>
            </div>
        </div>
        <div id="channel_decrypt_test" class="api-description-box">
            <h3 class="api-description-msg">채널보안 복호화</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    채널보안을 생성한 후 서버에서 지정된 문자열을 대칭키로 암호화하고 이를 전달받은 클라이언트가 대칭키로 복호화하는 기능에 대하여 테스트합니다.<br/><br/>
                    테스트를 위하여 보안채널 구성이 사전에 수립되어야 합니다.
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_channel_server.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        서버에서 암호화할 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="plainText" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="다음" />
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
