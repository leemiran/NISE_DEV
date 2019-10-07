<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<body class="bg" onLoad="getid(document.frmLogin)" >
<form id="frmLogin" name="frmLogin" method="post" action="" onSubmit="return loginMobile()">
	<input type="hidden" id="RSAModulus" value="${RSAModulus}" /><!-- 서버에서 전달한값을 셋팅한다. -->
	<input type="hidden" id="RSAExponent" value="${RSAExponent}" /><!-- 서버에서 전달한값을 셋팅한다. -->
	
	<input type="hidden" name="p_d_type" id="p_d_type"	value="O"/>

<fieldset>
<legend>회원로그인</legend>
<input type="hidden" name="loginGubun" value="mobile"/>
        <!-- skipmenu -->
        <div class="skipmenu">
            <dl>
                <dt class="hidden">바로가기 메뉴</dt>
                <dd><a href="#header" class="skip_link">Top영역</a></dd>
                <dd><a href="#container" class="skip_link">본문바로가기</a></dd>
            </dl>
        </div>
        <!-- //skipmenu -->
        <!-- wrap-->
        <div id="wrap">
            <!-- 공통헤더 -->
            <%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmMainTop.jsp" %>
            <!-- //공통헤더 -->
            <!-- 본문 -->
            <div id="container">
                <div class="wzone">
                    <h2><img src="/images/mbl/common/tit_login.png" alt="로그인" /></h2>
                    <div class="memzone">
                        <ul class="login">
                            <li class="bul">
                                <label for="userId"><img src="/images/mbl/common/tit_id.gif" alt="아이디" /></label>
                                <span><input type="text" name="userId" value="" id="userId" class="memput"/></span>
                            </li>
                            <li class="bul">
                                <label for="pwd"><img src="/images/mbl/common/tit_pw.gif"  alt="비밀번호" /></label>
                                <span class="#"><input type="password" name="pwd" value="" id="pwd" class="memput"/></span>
                            </li>
                        </ul>
                        <p class="btn"><input type="image" src="/images/mbl/btn/btn_login2.gif" alt="로그인" style="width:58px;height:65px;border:none;"/></p>
                        <ul class="text">
                            <li class="first">
                              <input type="checkbox" id="idsave" name="checksaveid" value="" class="chr"  />
                              <label for="idsave">아이디, 비밀번호 저장</label></li>
                            <li class="last"><a href="/mbl/main/idpwMobile.do">아이디/비밀번호 찾기</a></li>
                        </ul>
                    </div>
                </div>		
            </div>
            <!-- //본문 -->
</div>
<!-- //wrap-->
</fieldset>
</form>


</body>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>