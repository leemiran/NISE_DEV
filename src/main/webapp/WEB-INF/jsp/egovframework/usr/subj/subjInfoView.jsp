<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>

<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
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

<%//과정정보 인클루드 %>
<%@ include file = "./subjInfoViewInclude.jsp" %>

<!--아래는 팝업과 뷰를 따로 스크립트를 프로그램한다.-->
<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList()
{
	<c:if test="${search_knise_subj_module eq 'allSearch'}">
	thisForm.action = "/usr/subj/subjInfoSearchList.do";
	</c:if>
	<c:if test="${search_knise_subj_module ne 'allSearch'}">
	//thisForm.action = "/usr/subj/subjInfoList.do";
		thisForm.action = "/usr/subj/subjInfoSearchList.do";
	</c:if>
	thisForm.target = "_self";
	thisForm.submit();
}

//교육후기 페이징
function doLinkPage(index) {
	thisForm.action = "/usr/subj/subjInfoView.do";
	thisForm.pageIndex.value = index;
	thisForm.p_process.value = "commentsList";
	thisForm.target = "_self";
	thisForm.submit();
}

//교육후기 페이징 때문에 추가함 = p_process == commentsList 이면 교육후기를 보여준다.
window.onload = function () 
{
	if('${p_process}' == 'commentsList')
	{
		displayTabMenu(3);
	}

	//팝업일 경우는 id = PopupSubjPageList 를 숨기도록 한다.
	//팝업에서는 리스트로 가기가 없다 
	document.all("PopupSubjPageList").style.display = "block";
}

<c:if test="${search_knise_subj_module eq 'allSearch'}">
document.title="개설교육과정/신청("+"<c:out value='${view.subjnm}' />"+")"+" : 개설교육과정";
</c:if>
<c:if test="${search_knise_subj_module ne 'allSearch'}">
document.title="개설교육과정/신청("+"<c:out value='${view.subjnm}' />"+")"+" : 신청 가능한 연수";
</c:if>

//-->
</script>

<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
