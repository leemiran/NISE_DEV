<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	<input type="hidden" id="p_process" 			name="p_process">

			
	
<!--	  <div class="listTop">					-->
<!--                <div class="btnR MR05">-->
<!--               		<a href="#" class="btn01"><span>엑셀출력</span></a>-->
<!--                </div>                 -->
<!--		</div>-->
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="20%" />
					<col width="40%" />
					<col width="40%" />										
				</colgroup>
				<thead>
					<tr>
						<th scope="row">구분</th>
						<th scope="row">회원수</th>
						<th scope="row">비율(%)</th>
					</tr>
				</thead>
				<tbody>
<c:set var="totSum" value="0.00000"></c:set>
<c:forEach items="${list}" var="result" varStatus="status" >
	<c:set var="totSum">${result.cnt + totSum}</c:set>
</c:forEach>

<c:forEach items="${list}" var="result" varStatus="status" >
<c:set var="vcnt" value="0.00000"></c:set>
<c:set var="vcnt" value="${vcnt + result.cnt}"></c:set>
					<tr>
						<td>${result.empGubun}</td>
						<td class="name"><fmt:formatNumber value="${vcnt}" type="number"/></td>
						<td><fmt:formatNumber value="${vcnt/totSum*100.00}" type="number" pattern="###.##"/>%</td>
					</tr>
</c:forEach>				

					<tr class="bg">
						<td><strong>합계</strong></td>
						<td class="point"><strong><fmt:formatNumber value="${totSum}" type="number"/></strong></td>
						<td>100.00%</td>
					</tr>	
				</tbody>
			</table>
		</div>
		<!-- list table--> 
		
		
		
		
</form>




<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');



/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	thisForm.action = "/adm/sat/memberSatisticsExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
