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
	<meta http-equiv="X-UA-Compatible" Content="IE=edge">
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
			//npPfsStartup(null, false, true, false, false, "npkencrypt", "on");
            var sessionId = '<%=sessionID%>';
            var serverCert = '<%=strServerCert%>';

            /* init - server's sessionId */
            epki.init(sessionId);

            var secureChannelInputs = $("#secureChannel_test .api-btn-list").find("input");
            var secureChannelTexts = $("#secureChannel_test .table-input-text").find("textarea");

            secureChannelInputs.eq(0).click(function() {
                var select = $("#secureChannel_test .table-input-text").find("select");
                var algorithm = select.find("option:selected").text();
                /* Call Back Function Definition */
                var success = function(output) {
                    secureChannelTexts.eq(0).val(output);
                    $("#secureChannel_test").find("form").submit();
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };
                epki.requestSession(serverCert, algorithm, sessionId, success, error);
            });

            /* generate secure channel and login message */
            var channelAndLoginInputs = $("#channelAndLogin .api-btn-list").find("input");
            var channelAndLoginTexts = $("#channelAndLogin .table-input-text").find("textarea");

            channelAndLoginInputs.eq(0).click(function() {
                var select = $("#channelAndLogin .table-input-text").find("select");
                var algorithm = select.find("option:selected").text();

                /* Call Back Function Definition */
                var success = function(output) {
                    channelAndLoginTexts.eq(0).val(output);

                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                epki.reqSecChannelAndLogin(serverCert, algorithm, sessionId, success, error);
            });
            channelAndLoginInputs.eq(1).click(function() {
                // submit form
                $("#channelAndLogin").find("form").submit();
            });

            /* generate secure channel and encrypt message */
            var channelAndEncInputs = $("#channelAndEncrypt .api-btn-list").find("input");
            var channelAndEncTexts = $("#channelAndEncrypt .table-input-text").find("textarea");

            channelAndEncInputs.eq(0).click(function() {
                var select = $("#channelAndEncrypt .table-input-text").find("select");
                var algorithm = select.find("option:selected").text();

                /* Call Back Function Definition */
                var success = function(output) {
                    channelAndEncTexts.eq(1).val(output);

                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                var input = channelAndEncTexts.eq(0).val();

                epki.reqSecChannelAndEncrypt(serverCert, algorithm, input, success, error);
            });
            channelAndEncInputs.eq(1).click(function() {
                // submit form
                $("#channelAndEncrypt").find("form").submit();
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
        <div id="secureChannel_test" class="api-description-box">
            <h3 class="api-description-msg">채널보안 생성</h3>
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
                        <form action="kc_channel_result.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        암호화 알고리즘
                                    </td>
                                    <td class="table-input-text">
                                        <select>
                                            <option>SEED</option>
                                            <option>ARIA</option>
                                            <option>3DES</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        세션 아이디
                                    </td>
                                    <td class="table-input-text">
                                        <%=sessionID%>
                                    </td>
                                </tr>
                                <tr style="display: none">
                                    <td class="table-input-name">
                                        채널 보안 요청 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="secureChannelRequest" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="채널 생성" />
                    </div>
                </div>
            </div>
        </div>
    </section>
    <section>
        <div id="channelAndLogin" class="api-description-box">
            <h3 class="api-description-msg">채널보안 로그인 동시수행</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    클라이언트와 서버 간 보안채널을 형성함과 동시에 로그인 절차를 수행합니다.<br/>
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_channelandlogin_result.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        암호화 알고리즘
                                    </td>
                                    <td class="table-input-text">
                                        <select>
                                            <option>SEED</option>
                                            <option>ARIA</option>
                                            <option>3DES</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        세션 아이디
                                    </td>
                                    <td class="table-input-text">
                                        <%=sessionID%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        채널보안 로그인 동시수행 요청 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="reqSecLoginData" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="로그인" />
                        <input class="kc-btn-blue" type="button" value="요청" />
                    </div>
                </div>
            </div>
        </div>
    </section>
    <section>
        <div id="channelAndEncrypt" class="api-description-box">
            <h3 class="api-description-msg">채널보안 암호화 동시수행</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    클라이언트와 서버 간 보안채널을 형성함과 동시에 원문을 암호화하여 전송합니다.<br/>
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_channelandencrypt_result.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        암호화 알고리즘
                                    </td>
                                    <td class="table-input-text">
                                        <select>
                                            <option>SEED</option>
                                            <option>ARIA</option>
                                            <option>3DES</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        세션 아이디
                                    </td>
                                    <td class="table-input-text">
                                        <%=sessionID%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        암호화할 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        채널보안 암호화 동시수행 요청 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="reqSecEncData" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="암호화" />
                        <input class="kc-btn-blue" type="button" value="요청" />
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
