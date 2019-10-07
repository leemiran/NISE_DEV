<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>

<% 
	String realFileName = "과정별교육실적"; 
	//response.reset();
	response.setHeader("Pragma", "no-cache;");
	response.setHeader("Expires", "-1;");
	response.setHeader("Content-Transfer-Encoding", "binary;");
	response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
	response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
	//out.print("<META HTTP-EQUIV='Content-Type' content=\"text/html; charset=euc-kr\">");

%>
<html>
<head>
<title>과정별교육실적</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<style>
.xl26
	{mso-style-parent:style0;
	color:white;
	font-weight:700;
	text-align:center;
	border:.5pt solid black;
	background:#C2C2C2;
	mso-pattern:auto none;
	white-space:normal;}
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">

<table cellspacing="1" cellpadding="1" border="1">
			<thead>
				<tr>
					<th class="xl26">No</th>
					<th class="xl26">시도교육청</th>
					<th class="xl26">하위교육청</th>
					<th class="xl26">학교명</th>
					<th class="xl26">구분</th>
					<th class="xl26">ID</th>
					<th class="xl26">성명</th>
					<th  class="xl26">휴대폰번호</th>
					  <th class="xl26"  >우편번호</th>
					  <th class="xl26"  >주소</th>
					  <th class="xl26"  >교재수령지</th>
					  
					  
					<th class="xl26">기수</th>
					<th class="xl26">연수과정명</th>
					
					<th class="xl26">연수시작일</th>
					<th class="xl26">연수종료일</th>
					<th class="xl26">연수구분</th>
					
					<th class="xl26">연수시간</th>
					<th class="xl26">참여도평가</th>
					<th class="xl26">온라인시험</th>
					<th class="xl26">출석시험</th>
					<th class="xl26">온라인과제</th>
					<th class="xl26">총점</th>
					<th class="xl26">조정점수</th>
					<th class="xl26">이수여부</th>
					<th class="xl26">이수번호</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>${i.count}</td>
					<td>${result.deptCdnm}</td>
					<td>${result.agencyCdnm}</td>
					<td><c:out value="${result.userPath}" /></td>
					<td>
						<c:if test="${result.empGubun eq 'T' }">교원</c:if>
						<c:if test="${result.empGubun eq 'E' }">보조인력</c:if>
						<c:if test="${result.empGubun eq 'P' }">일반회원(학부모 등)</c:if>
						<c:if test="${result.empGubun eq 'W' }">기타</c:if>
					</td>
					<td>${result.userid}</td>
					<td>${result.name}</td>
					<td>${result.handphone}</td>
					
					<c:if test="${result.hrdc eq 'C'}">
						<td>${result.zipCd1}</td>
						<td>${result.address1}</td>
						<td>직장</td>
					</c:if>
					<c:if test="${result.hrdc ne 'C'}">
						<td>${result.zipCd}</td>
						<td>${result.address}</td>
						<td>자택</td>
					</c:if>
					
					<td><c:out value="${fn2:toNumber(result.subjseq)}" /></td>
					<td><c:out value="${result.subjnm}" /></td>
					
					<td>${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')}</td>
					<td>${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
					<td><c:out value="${result.upperclassnm}" /></td>
					
					<td><c:out value="${result.edutimes}" /></td>
					<td><c:out value="${result.avetc2}" /></td>
					<td><c:out value="${result.avftest}" /></td>
					<td><c:out value="${result.avmtest}" /></td>
					<td><c:out value="${result.avreport}" /></td>
					<td><c:out value="${result.score}" /></td>
					<td>
					<c:if test="${result.edutimes >= 60 and fn:substring(p_subj, 0, 3) ne 'PAR'}">
						<c:out value="${result.editscore}" />
					</c:if>
					</td>
					<td><c:out value="${result.isgraduatedname}" /></td>
					
					
					
					<td>
						<c:if test="${not empty result.serno}">
							<c:if test="${result.upperclassnm ne '일반연수'}">
							특수-${fn:replace(fn:replace(result.upperclassnm, '특별과정','교원'), '기타','')} 직무-${fn2:getFormatDate(result.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}
							</c:if>
							<c:if test="${result.upperclassnm eq '일반연수'}">
							특수-사이버-${fn2:getFormatDate(result.edustart, 'yyyy')}-${fn2:toNumber(result.subjseq)}-${result.serno}
							</c:if>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="16">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
</table>

</body>
</html>
