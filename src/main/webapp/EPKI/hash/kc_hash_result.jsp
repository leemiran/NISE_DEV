<%@page language="java" contentType="text/html; charset=UTF-8" import="com.epki.api.EpkiApi,
					com.epki.crypto.*, com.epki.util.Base64, com.epki.exception.EpkiException"%>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
<%
    String strAlgID = request.getParameter("algorithm");
    String strMessage = request.getParameter("plainText");
    String testResult = "";
	String plainText = "";

    try
    {
        // epki-java 초기화
        EpkiApi.initApp();

        byte[] bInputData = null;
        byte[] bMDData = null;

				plainText = new String(strMessage.getBytes("ISO-8859-1"), "UTF-8");
				
        // MessageDigest 객체 생성
        MessageDigest md = MessageDigest.getInstance(strAlgID);

        // MessageDigest 데이터 생성
        bInputData = strMessage.getBytes();
        bMDData = md.digest(bInputData);

        // MD Data Base64 인코딩
        Base64 encoder = new Base64();
        testResult = encoder.encode(bMDData);
    } catch (EpkiException e) {
        testResult = "해시 생성 실패" + e.toString();
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
    <h2>Hash 생성</h2>
    <div>이 페이지는 서버에서 평문에 대한 해시 값을 생성합니다.</div>
    <section>
        <div id="" class="api-description-box">
            <h3 class="api-description-msg">Hash 생성</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    사용자가 선택한 알고리즘을 통하여 입력 데이터에 대한 해쉬값을 생성하는 기능에 대하여 테스트 합니다.
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
                                        평문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=plainText%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서버에서 생성된 해시값
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=testResult%></textarea>
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
