<%@ page pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.net.*" %>

<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="/css/base.css" rel="stylesheet" type="text/css" />
<link href="/css/main.css" rel="stylesheet" type="text/css" />
<link href="/css/lcms/common.css" rel="stylesheet" type="text/css" />

<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<meta http-equiv="Expire-time" content="-1">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="imagetoolbar" content="no">
<meta name="Keywords" lang="utf-8" content="<c:out value="${gsServerTitle}"/>">
<meta name="Description" content="<c:out value="${gsServerTitle}"/>">
<meta name="robots" content="noindex,nofollow" />

<title>통합교원연수지원시스템 [<%=InetAddress.getLocalHost().getHostName()%>]</title>

<script type="text/javascript">

	//관리자 전용 로그인 페이지
	function actionLogin() {

		if (document.loginForm.userId.value =="") {
			alert("아이디를 입력하세요");
		//} else if (document.loginForm.password.value =="") {
		//	alert("비밀번호를 입력하세요");
		} else {
			document.loginForm.action="<c:url value='/adm/lgn/actionLogin.do'/>";
			document.loginForm.submit();
		}
	}

	//로그인 페이지 시작시 결과 메시지
	function fnInit() {
		var message = document.loginForm.message.value;
		if (message != "") {
			alert(message);
		}


	}

</script>


</head>
<body onLoad="fnInit();">

	<!-- 로그인 폼 -->
	<form name="loginForm" action ="" method="post">
	<input type="hidden" name="message" value="${message}">
	<input type="hidden" name="ASgubun" value="S">

	<!-- div id="content">
		<div id="login">
			<fieldset>
				<legend>회원로그인</legend>
				<p class="id">
					<label for="login-id"><img src="/images/main/txt_id.gif" alt="아이디" /></label>
					<input id="login-id" type="text" title="아이디" name="userId" size="10" value="thinklee"/>
					<input id="login-id" type="text" title="ASP"   name="asp_code" size="10" value="1340535" onclick="actionLogin();" />
					<select name="ASgubun">
						<option value="S">사이버</option>
						<option value="A">집합</option>
					</select>
				</p>

			</fieldset>
		</div>
	</div-->
	<div style="position: absolute;left:80px;top:700px;width:50px;height:50px;filter: Alpha(Opacity=100);" onclick="actionLogin();" >&nbsp;&nbsp;&nbsp;</div>


	<div id="loginWrap">
		<div class="loginBox">
			<h1><img src="/css/lcms/img/login_tit.gif" alt="로그인" /></h1>		
			<table style="margin:145px 0px 0px 115px;" width="283" >
	          <tr>
	            <td width="189"><input name="userId" type="text" id="textfield" size="30" value="admin" /></td>
	            <td width="82" rowspan="2"><img src="/css/lcms/img/login_btn.gif" width="73" height="51" onclick="actionLogin();"/></td>
	          </tr>
	          <tr>
	            <td><input type="text" name="asp_code" id="textfield" size="30" value="um2madmin" /></td>
	          </tr>
	        </table>
		</div>
	</div-->
	</form>

	<!-- 팝업 폼 -->
    <form name="defaultForm" action ="<c:url value='/adm/lgn/actionLogin.do'/>" method="post" target="_blank">
    </form>

<%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
</body>


</html>
