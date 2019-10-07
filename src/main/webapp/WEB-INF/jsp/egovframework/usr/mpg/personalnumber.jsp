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
<title>나이스 개인번호 구성안내</title>
<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/user/import.css"  />
			<script language='javascript'>     
		      	//alert("인증성공!! ^^");    
		      	
function goClose(){
    window.close();
}
</script>
</head> 
<body>
	<table width="401" height="530">
		<tr>
			<td>
				<img src="/images/user/personal_number.gif" width=401, height=480 alt="<나이스 개인번호 구성 체계(예시)> K101100000, 1~3자리값 시도구분 K10, 네번째 자리 신분구분 1, 5~10 자리값 일련번호 100000. <1~3자리 : 시도 구분을 나타냄> 교육부(공통) A00, 서울 B10, 부산 C10, 대구 D10, 인천 E10, 광주 F10, 대전 G10, 울산 H10, 세종 I10, 경기 J10, 강원 K10, 충북 M10, 충남 N10, 전북 P10, 전남 Q10, 경북 R10, 경남 S10, 제주 T10. <4자리 : 신분구분(공무원구분)> 교원 1, 지방공무원 6, 원어민 보조교사 1, 기타직 6, 영어회원 전문강사 1"/>
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