<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" 	 uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" 		 uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" 	 uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" 	 uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" 		 uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" 	 uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"  xml:lang="ko" lang="ko">
<head>
<style>
	a{text-decoration:none}
	a:link{font-size:16px;color:#333;text-decoration:none}
a:visited{font-size:16px;color:#333;text-decoration:none}
a:hover{font-size:16px;color:#ff3c00;text-decoration:underline}
</style>
<script language='javascript'>

function closePopup(){
	window.close();		
}

</script>
</head> 
<body>
<table width="450" height="380" border="0">
	<tr>
		<td align="center" valign="top">
			<table width="450" height="380" border="0">
				<tr>
					<td height="93">
						<img src="/images/user/noticetitle.gif">
					</td>
				</tr>
				<tr>
					<td height="100" valign="top">
					<font size=3>
						&nbsp;&nbsp;&nbsp;<b><font color=red>회원근무정보(나이스 개인번호, 자격등급, 학교명)</font>의 정보<p>
						&nbsp;&nbsp;&nbsp;가 부정확 시 <font color=blue>나이스 시스템 교원이수 등록에 누락처리</font><p>
						&nbsp;&nbsp;&nbsp;되니 반드시 재확인 및 변경사항 수정,&nbsp;<b><font color=red>나이스개인번호 중복확인 버튼</font></b>을 클릭하 <p>
						&nbsp;&nbsp;&nbsp;여 주시기 바랍니다.<p>
						&nbsp;&nbsp;&nbsp;<font color=red>※ 개인정보 보호법 시행령 제30조(개인정보의 안정성</font>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red>확보조치)에 의거</font></b>
					</font></td>
				</tr>
				<tr>
					<td align="center" height="30"> <a href="#" onclick="closePopup()"><b>닫기</b></a> </td>
				</tr>
			</table> 
		</td>
	</tr>
</table> 
</body>
</html>