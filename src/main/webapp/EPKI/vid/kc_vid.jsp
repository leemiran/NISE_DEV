<%@page language="java" contentType="text/html; charset=UTF-8"
        import="com.epki.api.EpkiApi, com.epki.conf.ServerConf,
                com.epki.cert.X509Certificate, com.epki.exception.EpkiException, com.epki.util.Base64"%>

<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

    String strServerCert = "";

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
        strServerCert = "Error" + e.toString();
    } catch (Exception e) {
        strServerCert = "Error" + e.toString();
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

            var vidInputs = $("#vid_test .api-btn-list").find("input");
            var vidInput = $("#vid_test .table-input-text").find("input");
            var vidTexts = $("#vid_test .table-input-text").find("textarea");

            vidInputs.eq(0).click(function() {
                /* Call Back Function Definition */
                var success = function(output) {
                    vidTexts.eq(0).val(output);
                    //alert(output);
                    $("#vid_test").find("form").submit();
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                var userId = vidInput.eq(0).val();

                epki.requestVerifyVID(serverCert, userId, success, error);
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
                        <form action="kc_vid_result.jsp" method="post">
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
                                        요청 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="vidRequest" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="확인" />
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
