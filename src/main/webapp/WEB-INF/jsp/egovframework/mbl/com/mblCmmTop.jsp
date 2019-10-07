<%@ page pageEncoding="UTF-8" %>
<body>
<!-- skipmenu -->
<div class="skipmenu">
	<dl>
		<dt class="hidden">바로가기 메뉴</dt>
		<dd><a href="#header" class="skip_link">Top영역</a></dd>
		<dd><a href="#wcontainer" class="skip_link">본문바로가기</a></dd>
	</dl>
</div>
<!-- //skipmenu -->
<!-- wrap-->
<div id="wrap">
	<!-- 공통헤더 화이트배경은 class=gray 추가됨 -->
	<div id="header" class="gray">
		<h1><a href="/mbl/main/index.do"><img src="/images/mbl/common/logo.png" alt="국립특수교육원부설 원격교육연수원" /></a></h1>
		<ul class="topbtn">        
                <li><a href="javascript:history.back()"><img src="/images/mbl/btn/btn_prev.gif" alt="이전" /></a></li>
                <li><a href="/mbl/main/index.do"><img src="/images/mbl/btn/btn_home.gif" alt="홈" /></a></li>
                <c:if test="${empty sessionScope.userid}">
                <li><a href="/mbl/main/loginMobile.do"><img src="/images/mbl/btn/btn_login.gif" alt="로그인" /></a></li>
                </c:if>
                <c:if test="${not empty sessionScope.userid}">
                <li><a href="/mbl/main/logout.do"><img src="/images/mbl/btn/btn_logout.gif" alt="로그아웃" /></a></li>
                </c:if>
		</ul>
	</div>
	<!-- //공통헤더 -->
