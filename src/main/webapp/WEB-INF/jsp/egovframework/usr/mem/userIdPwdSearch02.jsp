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
	 
<c:if test="${p_mode ne 'pwdcheck'}">
	  <div class="idpw_bg">
            	<p class="id_txt">
            	<span class="tblue"><strong>${p_name}님의</strong></span>
            	</p>
                <p class="pw_txt">
                아이디는  <span class="tred"><strong>${view.userid}</strong></span>입니다.
               <a href="#" onclick="javascript:loginPage();"><img src="/images/user/btn_idpw_ok.gif" alt="확인" /></a>
                </p>
                <p class="gi_txt"><span class="tred">* 개인정보 변동으로 인한 문의는 관리자에게 문의바랍니다.</span></p>
          </div>
</c:if>
<c:if test="${p_mode eq 'pwdcheck'}">
	  <div class="idpw_bg">
            	<p class="id_txt">
            	<span class="tblue"><strong>회원님의 비밀번호는</strong></span>
            	</p>
                <p class="pw_txt">
          
                <span class="tred"><strong> ${view.handphone}</strong></span>번으로 발송되었습니다.
              
                <a href="#" onclick="javascript:loginPage();"><img src="/images/user/btn_idpw_ok.gif" alt="확인" /></a>
                </p>
                <p class="gi_txt"><span class="tred">* 개인정보 변동으로 인한 문의는 관리자에게 문의바랍니다.</span></p>
          </div>
</c:if>    
</form>
<script type="text/javascript">
<!--
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
function loginPage()
{
	window.location.replace('http://iedu.nise.go.kr/usr/mem/userLogin.do');
}
//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->