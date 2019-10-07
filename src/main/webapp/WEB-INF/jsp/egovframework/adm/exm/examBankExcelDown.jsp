<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "com.ziaan.research.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="subjnm" value="${list[0].subjnm}"/>
<c:out  value="subjnm"/>
<% 
String year = request.getAttribute("ses_search_gyear")+"";

List list = (List)request.getAttribute("list");
Map mList = (Map)list.get(0);

String realFileName = mList.get("subjnm").toString()+" - 문제은행"; 


//response.reset();
response.setHeader("Pragma", "no-cache;");
response.setHeader("Expires", "-1;");
response.setHeader("Content-Transfer-Encoding", "binary;");
response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
//out.print("<META HTTP-EQUIV='Content-Type' content=\"text/html; charset=euc-kr\">");

System.out.println("realFileName["+realFileName+"]");


	DecimalFormat  df = new DecimalFormat("0.00");

	
%>

<html>
<head>
<title>문제은행</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<style>
.xl26
	{mso-style-parent:style0;
	color:black;
	font-weight:700;
	text-align:center;
	border:.5pt solid black;
	background:#C2C2C2;
	mso-pattern:auto none;
	white-space:normal;}
.graph { /* 그래프 이미지 */
        height: 20px;
	    border-style: none;
	    background-color: orange;
	    background-repeat: repeat-x;
         }  	
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">

	
<div class="tbList">
	<!-- list table-->
	<table summary="" cellspacing="0" border="1">
		<colgroup>
			<col width="150"/>
			<col width="150"/>
			<col width="150"/>
			<col width="60"/>
			<col width="150"/>
			<col width="150"/>
			<col width="150"/>
			<col width="150"/>
			<col width="150"/>
			<col width="150"/>
			<col width="100"/>
			<col width="150"/>
			<col width="150"/>
			<col width="150"/>
			<col width="150"/>
			<col width="150"/>
			<col width="150"/>
			<col width="50"/>
			<col width="50"/>
		</colgroup>

		<thead>     
			<tr style="background-color: #bbbbbb;">
				<th>객(1)/주(2)/OX(3)</th>
				<th>문제 TEXT</th>
				<th>설명 TEXT</th>
				<th>보기수</th>
				<th>보기1</th>
				<th>보기2</th>	
				<th>보기3</th>
				<th>보기4 </th>	
				<th>보기5 </th>
				<th>정답(보기번호)</th>
				<th>출제자</th>
				<th>난이도 상(1)/중(2)/하(3)</th>
				<th>차시</th>				
				<th>사용여부</th>				
				<!-- <th>등록연도</th>	
				<th>최종수정</th>	
				<th>최초사용</th>
				<th>최종사용</th>				
				<th>사용횟수</th> -->
			</tr>			
		</thead>
		
		<tbody>

			<c:forEach var="result" items="${list}" varStatus="status">
		 		<tr>
					<td>${result.examtype}</td>																											<!--구분 a-->
					<td>${result.examtext}</td>																												<!--종별 b-->
					<td>${result.exptext}</td>																													<!--기별 c-->
					<td>${result.examselcnt}</td>
					<td>${result.seltext1}</td>																													<!--계획(e) -->
					<td>${result.seltext2}</td>
					<td>${result.seltext3}</td>
					<td>${result.seltext4}</td>
					<td>${result.seltext5}</td>
					<td>${result.isanswer}</td>
					<td>${result.examiner}</td>
					<td>${result.levels}</td>
					<td>${result.lessonnum}</td>					
					<td>${result.isuse}</td>					
					<%-- <td>${result.regyear}</td>
					<td>${result.ldate}</td>
					<td>${result.firstyear}</td>
					<td>${result.lastyear}</td>					
					<td>${result.usecnt}</td> --%>
				</tr>	
			</c:forEach>
	
			<c:if test="${empty list}">
				<tr>
					<td colspan="22" align="center">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
		</tbody>
	</table>
	<!-- list table-->	
</div>
		
</body>
</html>
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--


//-->
</script>