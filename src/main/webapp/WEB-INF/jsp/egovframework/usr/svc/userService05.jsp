<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%-- <%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %> --%>
<!--login check-->
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
<fieldset>
<legend>연수행정실</legend>
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_userid"     value = "${sessionScope.userid}" />
	<input type = "hidden" name="p_subj" />
	<input type = "hidden" name="p_year" />
	<input type = "hidden" name="p_subjseq" />
	<input type = "hidden" name="p_scoreYn" id="p_scoreYn" value="N" />
	<input type="hidden" name="p_upperclass"/>


		<p class="floatR"><a href="#none" onclick="open_permission()"><img src="/images/user/btn_dom_print.gif" alt="원격연수원 인가서 인쇄" /></a></p>
		<br/><br/>
		<div style="text-align:center;">
			<img src="/images/user/print_ingasu.jpg" alt="인가번호 제04-01호 인가서 교원등의연수에관한규정 제2조의 규정에 의하여 원격교육연수원으로 인가함. 명칭 : 국립특수교육원부설원격교육연수원 2004.2.26. 교육인적자원부장관 인">
		</div>

	</fieldset>
</form>

        
        
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

//연수원인가서 보기
function open_permission()
{
	window.open('/html/usr/hom/permission.html','permission_window','width=670,height=1000,scrollbars=yes');
}

//-->
</script>
        
        
        
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
