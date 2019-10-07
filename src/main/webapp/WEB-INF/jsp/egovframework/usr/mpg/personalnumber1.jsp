<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
<title>나이스 개인번호 확인방법 팝업</title>
<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/user/import.css"  />
			<script language='javascript'>     
		      	//alert("인증성공!! ^^");    
		      	
function goClose(){
    window.close();
}
</script>
</head> 
<body>
	<table width="635" height="367">
		<tr>
			<td>
				<img src="/images/user/pertitle1.gif" width=635, height=35 alt="나이스 개인번호 확인 방법"/>
			</td>
		</tr>
		<tr>
			<td>
				<img src="/images/user/pertitle2.gif" width=635, height=25 alt="1.업무포털에 로그인 -> '나이스' 클릭하기"/>
			</td>
		</tr>
		<tr>
			<td>
				<img src="/images/user/nise1.gif" width=635, height=367 alt="1.업무포털에 로그인 -> '나이스' 클릭하기"/>
			</td>
		</tr>
		<tr>
			<td>
				<img src="/images/user/pertitle3.gif" width=635, height=25 alt="2.나이스 접속 화면에서 '나의 메뉴 -> 인사기록' 클릭하기"/>
			</td>
		</tr>
		<tr>
			<td>
				<img src="/images/user/nise2.gif" width=635, height=367 alt="2.나이스 접속 화면에서 '나의 메뉴 -> 인사기록' 클릭하기"/>
			</td>
		</tr>
		<tr>
			<td>
				<img src="/images/user/pertitle4.gif" width=635, height=25 alt="3.'나의 메뉴 -> 인사기록 -> 기본사항' 클릭 후 개인번호 확인하기"/>
			</td>
		</tr>
		<tr>
			<td>
				<img src="/images/user/nise3.gif" width=635, height=367 alt="3.'나의 메뉴 -> 인사기록 -> 기본사항' 클릭 후  4.개인번호 확인개인번호 확인하기 "/>
			</td>
		</tr>
		<tr>
			<td height="50" align="center">
				<a href="#" class="pop_btn01" onclick="goClose()" title="닫기"><span>닫기</span></a>
			</td>
		</tr>
	</table>
</body>
</html>