<%@page language="java" contentType="text/html; charset=UTF-8"
        import="javax.servlet.http.HttpSession, com.epki.api.EpkiApi, com.epki.conf.ServerConf,
                com.epki.cert.X509Certificate, com.epki.exception.EpkiException, com.epki.util.Base64,
                com.epki.crypto.*, com.epki.session.*, java.io.IOException"
%>
<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

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

			// 키보드 보안 초기화
			//npPfsStartup(null, false, true, false, false, "npkencrypt", "on");
		
            var sessionId = '<%=sessionID%>';

            /* init - server's sessionId */
            epki.init(sessionId);

            var signInputs = $("#sign_test_client .api-btn-list").find("input");
            var textAreas = $("#sign_test_client .table-input-text").find("textarea");
            signInputs.eq(0).click(function() {
                textAreas.val("");
            });
            signInputs.eq(1).click(function() {
                /* Call Back Function Definition */
                var success = function(output) {
                    textAreas.eq(1).val(output);
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                /* get plain text from textarea */
                var plainText = textAreas.eq(0).val();

                /* generate signedData */
                epki.sign(1, "", plainText, success, error);

            });
            signInputs.eq(2).click(function() {
                var signedData = textAreas.eq(1).val();
                if(signedData.length <= 0) {
                    alert("서명값이 없습니다.");
                    return;
                }
                var form = $("#sign_test_client").find("form");

                form.submit();
            });
            signInputs.eq(3).click(function() {
                epki.invokeCMDlg();
            });

            var signServerInputs = $("#sign_test_server .api-btn-list").find("input");
            var signServerTextAreas = $("#sign_test_server .table-input-text").find("textarea");
            var signServerForm = $("#sign_test_server").find("form");

            signServerInputs.eq(0).click(function() {
                if(signServerTextAreas.eq(0).val().length <= 0) {
                    alert("전자서명 원문을 입력해주십시오.");
                    return;
                }

                signServerForm.submit();
            });

            var testCertInputs = $("#sign_test_cert .api-btn-list").find("input");
            var testCertTextAreas = $("#sign_test_cert .table-input-text").find("textarea");
            var testCertForm = $("#sign_test_cert").find("form");

            testCertInputs.eq(0).click(function() {
                if(testCertTextAreas.eq(0).val().length <= 0) {
                    alert("전자서명 값을 입력해주십시오.");
                    return;
                }

                testCertForm.submit();
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
    <h2>전자서명 및 검증</h2>
    <div>이 페이지는 전자서명을 수행하고 검증하는 기능에 대하여 테스트 합니다.</div>
    <section>
        <div id="sign_test_client" class="api-description-box">
            <h3 class="api-description-msg">전자서명 테스트(클라이언트)</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content"><h5>클라이언트에서 전자서명을 생성하고 서버에서 검증합니다.</h5></div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_sign_client.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        경로검증 옵션
                                    </td>
                                    <td class="table-input-text">
                                        <select name="certPathOption">
                                            <option>CERT_VERIFY_NONE</option>
                                            <option>CERT_VERIFY_FULLPATH</option>
                                            <option>CERT_VERIFY_CACERT</option>
                                            <option>CERT_VERIFY_USERCERT</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        폐지여부 검증 옵션
                                    </td>
                                    <td class="table-input-text">
                                        <select name="revokeOption">
                                        	<option>REVOKE_CHECK_SCVP</option>
                                            <option>REVOKE_CHECK_NONE</option>
                                            <option>REVOKE_CHECK_CRL</option>
                                            <option>REVOKE_CHECK_OCSP</option>
                                            <option>REVOKE_CHECK_ARL | REVOKE_CHECK_CRL</option>
                                            <option>REVOKE_CHECK_ARL | REVOKE_CHECK_OCSP</option>
                                            <option>REVOKE_CHECK_CRL | REVOKE_CHECK_OCSP</option>
                                            <option>REVOKE_CHECK_ALL</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        전자서명 원문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="plainText" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서명값
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="signedData" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="초기화" />
                        <input class="kc-btn-blue" type="button" value="생성" />
                        <input class="kc-btn-blue" type="button" value="검증" />
                        <input class="kc-btn-blue" type="button" value="인증서관리창" />
                    </div>
                </div>
            </div>
        </div>
        <div id="sign_test_server" class="api-description-box">
            <h3 class="api-description-msg">전자서명 테스트(서버)</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content"><h5>서버에서 생성된 전자서명을 클라이언트에서 검증합니다.</h5></div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_sign_server.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        전자서명 원문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="plainText" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="생성 및 검증" />
                    </div>
                </div>
            </div>
        </div>
        <div id="sign_test_cert" class="api-description-box">
            <h3 class="api-description-msg">전자서명 데이터 내 인증서 정보 추출</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content"><h5>입력된 전자서명 데이터로 부터 인증서 정보를 추출하는 기능에 대하여 테스트 합니다.</h5></div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_sign_cert.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        서명값
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="signedData" ondblclick="textareaResize(this)"></textarea>
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
