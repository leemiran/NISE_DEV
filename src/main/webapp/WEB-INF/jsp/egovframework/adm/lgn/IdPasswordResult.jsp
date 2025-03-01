<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
 /**
  * @Class Name : EgovIdPasswordResult.jsp
  * @Description : 아이디/비밀번호 찾기 결과화면
  * @Modification Information
  * @
  * @  수정일         수정자                   수정내용
  * @ -------    --------    ---------------------------
  * @ 2009.03.17    박지욱          최초 생성
  *
  *  @author 공통서비스 개발팀 박지욱
  *  @since 2009.03.17
  *  @version 1.0
  *  @see
  *  
  */
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/css/egovframework/cmm/uat/uia/com.css" type="text/css">
<title>MOPAS 아이디/비밀번호 찾기 결과</title>
<script>
/* ********************************************************
 * 뒤로 처리 함수
 ******************************************************** */
function fnBack(){
	document.backForm.action = "<c:url value='/cmm/uat/uia/egovIdPasswordSearch.do'/>";
	document.backForm.submit();
}
</script>
</head>
<body>
  <form name="backForm"/>
  <table width="303" border="0" cellspacing="0" cellpadding="0">
    <tr>
	  <td width="40%"class="title_left"><img src="/images/egovframework/cmm/uat/uia/tit_icon.gif" width="16" height="16" hspace="3" align="absmiddle">&nbsp;아이디/비밀번호 찾기</td>
    </tr>
    <tr>
	  <td width="303" height="210" valign="top" style="background-image: url(/images/egovframework/cmm/uat/uia/login_bg01.gif);">
		<table width="260" border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
		    <td height="65">&nbsp;</td>
		  </tr>
		  <tr>
		    <td class="title" nowrap>${resultInfo}</td>
		  </tr>
		  <tr>
		    <td height="80">&nbsp;</td>
		  </tr>
		</table>
	  </td>
    </tr>
  </table>
</body>
</html>

