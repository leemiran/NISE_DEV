<%@page language="java" contentType="text/html; charset=UTF-8"
        import="com.epki.api.EpkiApi,
				com.epki.crypto.*, com.epki.util.Base64, com.epki.exception.EpkiException" pageEncoding="utf-8"%>

<%
    String strAlgID = request.getParameter("algorithm");
    String strSymKey = request.getParameter("symkey");
    String strSymIv = request.getParameter("symiv");
    String strEncryptedData = request.getParameter("encryptedData");
    String strDecryptData = "";
    String testResult = "";

    try {

        //  epki-java 초기화
        EpkiApi.initApp();

        byte[] bSymKey = null;
        byte[] bSymIV = null;
        byte[] bencryptedData = null;
        byte[] bdecryptData = null;

        // SymKey, IV Base64 디코딩
        Base64 decoder = new Base64();
        bSymKey = decoder.decode(strSymKey);
        bSymIV = decoder.decode(strSymIv);
        // 대칭키 설정
        SecretKey secretKey = new SecretKey(strAlgID, bSymKey, bSymIV); 
				strAlgID = secretKey.getKeyAlg();
        // 전달된 키를 통해 복호화를 수행하도록 초기화
        Cipher cipher = Cipher.getInstance(secretKey.getKeyAlg());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // 암호화 데이터 Base64 디코딩
        bencryptedData = decoder.decode(strEncryptedData);

        // 데이터 복호화
        bdecryptData = cipher.doFinal(bencryptedData);

        String result = "";
        for (int i = 0; i < bdecryptData.length; i++) {
            result += Integer.toString((bdecryptData[i] & 0xff) + 0x100, 16).substring(1);
        }

        strDecryptData = new String(bdecryptData, "UTF-8");

				if(strAlgID == "DESEDE"){
					strAlgID = "3DES";
				}

        testResult = "TRUE";

    } catch (EpkiException e) {
        testResult = "FALSE " + e.toString();
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
                                    테스트 결과
                                </td>
                                <td class="table-input-text">
                                    <textarea><%=testResult%></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td class="table-input-name">
                                    클라이언트에서 전송된 암호 메시지
                                </td>
                                <td class="table-input-text">
                                    <textarea><%=strEncryptedData%></textarea>
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
                                    서버에서 복호화된 메시지
                                </td>
                                <td class="table-input-text">
                                    <textarea><%=strDecryptData%></textarea>
                                </td>
                            </tr>
                        </table>
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
