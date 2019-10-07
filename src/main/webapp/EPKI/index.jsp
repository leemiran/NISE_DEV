<%@page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" Content="IE=edge">
    <title>Ksign CASE Test</title>

    <!-- 부트스트랩  CSS -->
    <link rel="stylesheet" href="../kcase/lib/css/bootstrap.min.css"/>

    <!-- KSign Style Sheet -->
    <link rel="stylesheet" href="../kcase/lib/css/kcase.css"/>

    <!-- Sample CSS -->
    <link rel="stylesheet" href="../kcase/lib/css/sample.css"/>

    <script src="../kcase/lib/js/jquery-1.11.3.js"></script>
    <script src="../kcase/lib/js/jquery-ui.js"></script>
    
    <script src="../kcase/EPKICommon.js"></script>

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
        <span>HTML5 KSignCase-Agent Server API Test</span>
        <br/>
        <div>KSign Toolkit Development Dept.</div>
    </h1>
</div>

<div style="width:100%; height:1px; background-color: mediumpurple"></div>

<div class="content">
    <h2>테스트 목록</h2>
    <div>이 테스트 페이지를 통하여 KCaseAgent API의 주요 기능을 테스트할 수 있습니다.</div>
    <section>
        <div class="index-box" onclick="window.location.href='./sign/kc_sign.jsp'" style="border-left: solid 5px #ce4844;">
            <h3 style="color:#ce4844;">전자서명 및 검증</h3>
            <h5>전자 서명 관련 API를 테스트 합니다.</h5>
        </div>
    </section>
    <section>
        <div class="index-box" onclick="window.location.href='./cipher/kc_cipher.jsp'" style="border-left: solid 5px orange;">
            <h3 style="color: orange">대칭키 암호화 및 복호화</h3>
            <h5>대칭키 암/복호화 관련 API를 테스트 합니다.</h5>
        </div>
    </section>
    <section>
        <div class="index-box" onclick="window.location.href='./env/kc_env.jsp'" style="border-left: solid 5px yellow">
            <h3 style="color: yellow">공개키 암호화 및 복호화</h3>
            <h5>공개키 암/복호화 관련 API를 테스트 합니다.</h5>
        </div>
    </section>
    <section>
        <div class="index-box" onclick="window.location.href='./channel/kc_channel.jsp'" style="border-left: solid 5px green;">
            <h3 style="color: green">인증서 로그인 및 채널보안</h3>
            <h5>인증서 로그인 및 채널보안 관련 API를 테스트 합니다.</h5>
        </div>
    </section>
    <section>
        <div class="index-box" onclick="window.location.href='./vid/kc_vid.jsp'" style="border-left: solid 5px blue;">
            <h3 style="color: blue">인증서 기반 신원확인</h3>
            <h5>인증서 기반 신원확인을 테스트합니다.</h5>
        </div>
    </section>
    <section>
        <div class="index-box" onclick="window.location.href='./mac/kc_hmac.jsp'" style="border-left: solid 5px darkblue;">
            <h3 style="color: darkblue">HMAC 생성 및 검증</h3>
            <h5>웹 서버 메시지 인증 코드 생성 및 검증을 테스트 합니다.</h5>
        </div>
    </section>
    <section>
        <div class="index-box" onclick="window.location.href='./hash/kc_hash.jsp'" style="border-left: solid 5px purple;">
            <h3 style="color: purple">Hash 생성</h3>
            <h5>웹 서버 Hash 생성을 테스트 합니다.</h5>
        </div>
    </section> 
</div>
<input type="hidden" id="test" value=""/> 
<footer style="position: absolute;">
    <div>
        Copyright 2016. KSIGN all rights reserved.
    </div>
</footer>
</body>
</html>
