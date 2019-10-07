<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<body class="bg">
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
        <!-- skipmenu -->
        <div class="skipmenu">
            <dl>
                <dt class="hidden">바로가기 메뉴</dt>
                <dd><a href="#header" class="skip_link">Top영역</a></dd>
                <dd><a href="#maincon" class="skip_link">본문바로가기</a></dd>
            </dl>
        </div>
        <!-- //skipmenu -->
        <!-- wrap-->
        <div id="wrap">
            <!-- 공통헤더 -->
            <%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmMainTop.jsp" %>
            <!-- //공통헤더 -->
            <!-- 본문 -->	
            <div id="maincon">
                <ul class="mainmenu">
                    <li><a href="/mbl/subj/subjList.do"><img src="/images/mbl/common/main_menu01.png" alt="교육과정" /><span>교육과정</span></a></li>
                    <li><a href="/mbl/prop/propList.do"><img src="/images/mbl/common/main_menu02.png" alt="과정신청내역" /><span>과정신청내역</span></a></li>
                    <li><a href="/mbl/fin/finishList.do"><img src="/images/mbl/common/main_menu03.png" alt="이수현황조회" /><span>이수현황조회</span></a></li>
                    <li><a href="/mbl/std/studyList.do"><img src="/images/mbl/common/main_menu04.png" alt="나의학습방" /><span>나의학습방</span></a></li>
                    <li><a href="/mbl/svc/noticeList.do"><img src="/images/mbl/common/main_menu06.png" alt="연수안내" /><span>연수안내</span></a></li>
                    <li><a href="/mbl/inf/informationView.do"><img src="/images/mbl/common/main_menu05.png" alt="고객센터" /><span>고객센터</span></a></li>
                    
                </ul>
            </div>	
            <!-- //본문 -->
        </div>
        <!-- //wrap-->
</form>
</body>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>