<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<body class="bg" onLoad="getid(document.frmLogin)" >
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
                    <ul>
                        <li class="pw01">회원가입 시 등록하신 아래 휴대전화 번호로 임시번호를 발송하였습니다.<br />
                        (임시 비밀번호로 로그인 하신 후 반드시 회원정보 수정메뉴에서 비밀번호를 수정해주세요)</li>
                        <li class="pw02"><span>휴대전화번호 :  <a href="" class="cyan"><strong>010-000-0000</strong></a></span></li>
                        <li class="pw03">만약 아래의 휴대전화 번호와 맞지 않는 경우 PC로 접속하시어 “<span class="cyan12">본인확인 서비스</span>”를 선택하시면 본인인증 후 신규 비밀번호를 등록 할 수 있습니다.</li>
                    </ul>
                    <p class="mrt30"><a href="#"><img src="/images/mbl/btn/btn_go.gif" alt="확인 로그인페이지로 이동" /></a></p>
                </div>
                
            </div>
            <!-- //본문 -->
</div>
<!-- //wrap-->
</body>

<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>