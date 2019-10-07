<%@page language="java" contentType="text/html; charset=UTF-8" %>
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

            var hmacInputs = $("#hmac_test .api-btn-list").find("input");
            var hmacTexts = $("#hmac_test .table-input-text").find("input");

            hmacInputs.eq(0).click(function() {
                hmacTexts.val("");
                $("textarea").val("");
            });
            hmacInputs.eq(1).click(function () {
                $("#hmac_test").find("form").submit();
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
    <h2>HMAC 생성/검증</h2>
    <div>이 페이지는 선택한 알고리즘과 패스워드를 이용하여 서버에서 HMAC 생성 및 검증을 합니다.</div>
    <section>
        <div id="hmac_test" class="api-description-box">
            <h3 class="api-description-msg">HMAC 생성/검증</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    사용자가 선택한 알고리즘과 패스워드를 입력 받아 메세지 인증 코드를 생성하고 검증하는 기능에 대하여 테스트 합니다.
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_hmac_result.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        HMAC 알고리즘
                                    </td>
                                    <td>
                                        <select name="algorithm">
                                            <option value="SHA1HMAC">HMAC-SHA1</option>
                                            <option value="SHA256HMAC">HMAC-SHA256</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        Password
                                    </td>
                                    <td class="table-input-text">
                                        <input name="password" type="password" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        평문
                                    </td>
                                    <td class="table-input-text">
                                        <textarea name="plainText" ondblclick="textareaResize(this)"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" type="button" value="초기화" />
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
