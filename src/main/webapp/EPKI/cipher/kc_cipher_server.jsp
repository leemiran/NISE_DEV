<%@page language="java" contentType="text/html; charset=utf-8" import="com.epki.api.EpkiApi,
				com.epki.crypto.*, com.epki.util.Base64, com.epki.exception.EpkiException"%>
<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();
    String strAlgID = request.getParameter("algorithm");
    String tempPlainData = request.getParameter("plainText");
    String strSymKey = "";
    String strSymIv = "";
    String strEncryptData = "";

    String strPlainData = new String(tempPlainData.getBytes("ISO-8859-1"), "UTF-8");

    String testResult = "";

    try
    {
        // epki-java 초기화
        EpkiApi.initApp();

        byte[] bplainData = null;
        byte[] bencryptData = null;
        byte[] bkey = null;
        byte[] bIV = null;

        // Client에서 올라온 알고리즘을 사용하는 임의의 대칭키 생성
        KeyGenerator keyGenerator = KeyGenerator.getInstance(strAlgID);
        SecretKey secretKey = keyGenerator.generateKey();

        bkey = secretKey.getKey();
        bIV = secretKey.getIV();

        Base64 base64 = new Base64();

        strSymKey = new String(base64.encode(bkey));
        strSymIv = new String(base64.encode(bIV));
				strAlgID = secretKey.getKeyAlg();
        // 지정한 알고리즘으로 Cipher Instance 획득
        Cipher cipher = Cipher.getInstance(strAlgID);

        // 생성된 키를 통해 Encrypt를 수행하도록 초기화
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // 데이터 암호화
        //평문 UTF-8 인코딩 추가
        bplainData = strPlainData.getBytes("UTF-8");
        bencryptData = cipher.doFinal(bplainData);

        strEncryptData = base64.encode(bencryptData);
        
				if(strAlgID == "DESEDE"){
					strAlgID = "3DES";
				}
				
        testResult = "성공";
    } catch (EpkiException e) {
        testResult = "실패 " + e.toString();
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

            var cipherTestInputs = $("#cipher_test_server_result .api-btn-list").find("input");
            var cipherTestTexts = $("#cipher_test_server_result .table-input-text").find("textarea");

            cipherTestInputs.eq(1).click(function() {
                var reqAlg = cipherTestTexts.eq(2).val();

                /* Call Back Function Definition */
                var success = function(output) {
                    cipherTestTexts.eq(6).val(output);
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                var encryptedData = cipherTestTexts.eq(5).val();

                var symKey = {
                    key: cipherTestTexts.eq(3).val(),
                    iv: cipherTestTexts.eq(4).val()
                };

                /* block decrypt */
                epki.decrypt(reqAlg, symKey, encryptedData, success, error);

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
        <div id="cipher_test_server_result" class="api-description-box">
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
                        <form action="" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        테스트 결과
                                    </td>
                                    <td class="table-input-text">
                                        <textarea><%=testResult%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        원문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea><%=strPlainData%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        암호화 알고리즘
                                    </td>
                                    <td class="table-input-text">
                                        <textarea><%=strAlgID%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        대칭키
                                    </td>
                                    <td class="table-input-text">
                                        <textarea><%=strSymKey%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        IV
                                    </td>
                                    <td class="table-input-text">
                                        <textarea><%=strSymIv%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서버에서 암호화된 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea><%=strEncryptData%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        클라이언트에서 복호화된 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" onclick="javascript:history.back()" type="button" value="이전" />
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
