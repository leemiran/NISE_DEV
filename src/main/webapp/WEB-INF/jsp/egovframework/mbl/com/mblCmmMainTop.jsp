<%@ page pageEncoding="UTF-8" %>

            <!-- 공통헤더 -->
            <div id="header">
                <h1><a href="/mbl/main/index.do"><img src="/images/mbl/common/logo.png" alt="국립특수교육원부설 원격교육연수원" /></a></h1>
                <ul class="topbtn">                    
                    <li><a href="/mbl/main/index.do"><img src="/images/mbl/btn/btn_home.gif" alt="홈" /></a></li>
                    <c:if test="${empty sessionScope.userid}">
	                <!-- <li><a href="/mbl/main/login.do"><img src="/images/mbl/btn/btn_login.gif" alt="로그인" /></a></li> -->
	                <li><a href="/mbl/main/loginMobile.do"><img src="/images/mbl/btn/btn_login.gif" alt="로그인" /></a></li>
	                </c:if>
	                <c:if test="${not empty sessionScope.userid}">
	                <li><a href="/mbl/main/logout.do"><img src="/images/mbl/btn/btn_logout.gif" alt="로그아웃" /></a></li>
	                </c:if>
                </ul>
            </div>
            <!-- //공통헤더 -->