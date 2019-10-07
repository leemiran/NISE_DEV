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

            var hashInputs = $("#hash_test .api-btn-list").find("input");

            hashInputs.eq(0).click(function() {
                $("textarea").val("");
            });
            hashInputs.eq(1).click(function() {
                $("#hash_test").find("form").submit();
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
    <h2>Hash 생성</h2>
    <div>이 페이지는 서버에서 평문에 대한 해시 값을 생성합니다.</div>
    <section>
        <div id="hash_test" class="api-description-box">
            <h3 class="api-description-msg">Hash 생성</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content">
                <h5>
                    사용자가 선택한 알고리즘을 통하여 입력 데이터에 대한 해시값을 생성하는 기능에 대하여 테스트 합니다.
                </h5>
            </div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="kc_hash_result.jsp" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        HASH 알고리즘
                                    </td>
                                    <td>
                                        <select name="algorithm">
                                            <option>SHA1</option>
                                            <option>SHA256</option>
                                            <option>MD5</option>
                                            <option>HAS160</option>
                                        </select>
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
