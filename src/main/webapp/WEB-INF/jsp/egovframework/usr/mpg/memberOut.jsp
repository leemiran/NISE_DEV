<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logIn check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>


<script type="text/javascript">
//<![CDATA[
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
//]]>
</script>
<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
	<fieldset>
    <legend>회원탈퇴</legend>
	<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
    <!-- 회원탈퇴 내용부분 -->
    <!-- 회원탈퇴 값..  -->
    <input type="hidden" name="p_isretire" value="N" />
    <div class="mypage_out">
        <img src="/images/user/mypage_out1.gif" alt="회원탈퇴 안내" longdesc="#msg"/><br />
        <div class="blind" id="msg">
        01.회원탈퇴 시 회원님의 교육이력을 제외한 개인정보가 모두 삭제되며, 가입된 커뮤니티에서도 모두 탈퇴처리됩니다.
        02.원격교육연수원 시스템을 회원탈퇴를 하시면 홈페이지 로그인이 제한되며, 재가입이 불가능합니다.
        03.회원탈퇴시 이수증 출력 및 결제 영수증, 교육확인증 출력은 불가능 하며, 탈퇴 후 재가입이 불가능 합니다.
        </div>
        <span class="pL450"><a href="#" onclick="go_action()"><img src="/images/user/mypage_outbtn1.gif" alt="회원탈퇴하기" /></a></span>
    </div>
    <!-- // 회원탈퇴 내용부분 -->
    </fieldset>
</form>   
<script type="text/javascript">
//<![CDATA[
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
function go_action() {
	if(confirm("원격교육연수원 회원탈퇴를 정말로 진행하시겠습니까?\n회원탈퇴시 로그인이 제한되며 재가입이 불가능 합니다."))
	{
		thisForm.p_isretire.value = 'Y';
		thisForm.target = "_self";
		thisForm.action = "/usr/mpg/memberOutAction.do";
		thisForm.submit();
	}
}
//]]>
</script>        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
