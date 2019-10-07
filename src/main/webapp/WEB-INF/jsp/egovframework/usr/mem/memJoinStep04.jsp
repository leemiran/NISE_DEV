<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLogOutCheck.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
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
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<div class="wrap_step">
		<ul>
			<li><img src="/images/user/img_step01_off.gif" alt="약관동의" /></li>
			<li><img src="/images/user/img_step02_off.gif" alt="가입인증" /></li>
			<li><img src="/images/user/img_step03_off.gif" alt="기본정보입력" /></li>
			<li><img src="/images/user/img_step04_on.gif" alt="현재단계 가입완료" /></li>
		</ul>
	</div>
    <div class="member_enb m30">            	
         <span class="pos"><a href="javascript:homsubmit();"><img src="/images/user/btn_ok.gif" alt="확인"/></a></span>
	</div>
</form>
<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
function homsubmit(){
	//thisForm.action = "http://iedu.nise.go.kr/usr/hom/portalActionMainPage.do";
	thisForm.action="http://localhost:8080/usr/hom/portalActionMainPage.do";
	thisForm.target	= "_self";
	thisForm.submit();
}
document.title="회원가입(회원가입완료) :회원가입";
//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->