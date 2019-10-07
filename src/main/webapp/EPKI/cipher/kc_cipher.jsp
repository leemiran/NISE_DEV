<%@page language="java" contentType="text/html; charset=UTF-8" import="com.epki.api.EpkiApi,
				com.epki.cms.SignedData, com.epki.cert.X509Certificate, com.epki.cert.CertValidator,
				com.epki.crypto.*, com.epki.util.Base64, com.epki.conf.ServerConf,
				com.epki.exception.*, java.io.IOException"%>

<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();
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

            var keygenInputs = $("#cipher_test_keygen .api-btn-list").find("input");
            var keygenTextareas = $("#cipher_test_keygen .table-input-text").find("textarea");

            keygenInputs.eq(0).click(function() {
                keygenTextareas.val("");
            });
            keygenInputs.eq(1).click(function() {
                var select = $("#cipher_test_keygen .table-input-text").find("select");
                var algorithmId = select.find("option:selected").text();

                /* Call Back Function Definition */
                var success = function (output) {
                    keygenTextareas.eq(0).val(output.key);
                    keygenTextareas.eq(1).val(output.iv);
                };
                var error = function (errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                /* gen key */
                epki.genSymmetricKey(algorithmId, success, error);
            });

            var cipherCliInputs = $("#cipher_test_client .api-btn-list").find("input");
            var cipherCliTexts = $("#cipher_test_client .table-input-text").find("textarea");

            var tdNames = $("#cipher_test_client .table-input-name");

            tdNames.eq(1).dblclick(function() {
                cipherCliTexts.eq(0).val(keygenTextareas.eq(0).val());
            });
            tdNames.eq(2).dblclick(function() {
                cipherCliTexts.eq(1).val(keygenTextareas.eq(1).val());
            });

            cipherCliInputs.eq(0).click(function() {
                cipherCliTexts.val("");
            });
            cipherCliInputs.eq(1).click(function() {
                var select = $("#cipher_test_client .table-input-text").find("select");
                var algorithmId = select.find("option:selected").text();

                /* Call Back Function Definition */
                var success = function(output) {
                    cipherCliTexts.eq(3).val(output);
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                var plainText = cipherCliTexts.eq(2).val();

                var symKey = {
                    key: cipherCliTexts.eq(0).val(),
                    iv: cipherCliTexts.eq(1).val()
                };

                /* block encrypt */
                epki.encrypt(algorithmId, symKey, plainText, success, error);
            });
            cipherCliInputs.eq(2).click(function() {
                $("#cipher_test_client").find("form").submit();
            });

            var cipherSerInputs = $("#cipher_test_server .api-btn-list").find("input");
						var cipherSerTexts = $("#cipher_test_server .table-input-text").find("textarea");

						cipherSerInputs.eq(0).click(function() {
                cipherSerTexts.val("");
            });
            cipherSerInputs.eq(1).click(function() {
                $("#cipher_test_server").find("form").submit();
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
    <h2>대칭키 암호화/복호화</h2>
    <div>이 페이지는 블록암호에 대한 대칭키 암호화/복호화 기능에 대하여 테스트합니다.</div>
    <section>
        <div id="cipher_test_keygen" class="api-description-box">
            <h3 class="api-description-msg">대칭키, IV 생성</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content"><h5>대칭키와 IV를 생성합니다.</h5></div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
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
                                    생성된 대칭키
                                </td>
                                <td class="table-input-text">
                                    <textarea></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td class="table-input-name">
                                    생성된 IV
                                </td>
                                <td class="table-input-text">
                                    <textarea></textarea>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="초기화" />
                        <input class="kc-btn-blue" type="button" value="생성" />
                    </div>
                </div>
            </div>
        </div>
        <div id="cipher_test_client" class="api-description-box">
            <h3 class="api-description-msg">클라이언트 암호화, 서버 복호화</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    클라이언트에서 지정된 문자열을 대칭키로 암호화하고 이를 전달받은 서버가 대칭키로 복호화하는 기능에 대하여 테스트 합니다.<br/><br/>
                    테스트를 위하여 클라이언트에서 임의로 생성한 대칭키를 서버로 전송합니다.<br/>
                    실제 운용환경에서 본 기능을 사용하는 경우, 생성된 대칭키는 서버의 공개키로 암호화하여 전송하거나 별도의 안전한 전달과정이 반드시 필요합니다.
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_cipher_client.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        암호화 알고리즘
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
                                        대칭키
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="symkey"></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        IV
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="symiv"></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        평문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="plainText"></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        암호문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="encryptedData"></textarea>
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
        <div id="cipher_test_server" class="api-description-box">
            <h3 class="api-description-msg">서버 암호화, 클라이언트 복호화</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    서버에서 지정된 문자열을 대칭키로 암호화하고 이를 전달받은 클라이언트가 대칭키로 복호화하는 기능에 대하여 테스트 합니다.<br/><br/>
                    테스트를 위하여 클라이언트에서 임의로 생성한 대칭키를 서버로 전송합니다. 실제 운용환경에서 본 기능을 사용하는 경우, 생성된 대칭키는 서버에 공개키로 암호화하여 전송하거나 별도의 안전한 전달과정이 반드시 필요합니다.
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_cipher_server.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        암호화 알고리즘
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
                                        원문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="plainText"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="초기화" />
                        <input class="kc-btn-blue" type="button" value="암호화" />
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
