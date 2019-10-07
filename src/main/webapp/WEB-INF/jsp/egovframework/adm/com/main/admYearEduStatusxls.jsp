<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "com.ziaan.research.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<% 
String year = request.getAttribute("ses_search_gyear")+"";
String realFileName = year+"년도 운영현황"; 
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
<title>결과보고서</title>
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

<!----------------- 결과보고서(이수현황) A  START---------------------------------------------------------------------------------------->
		  	
<!--계획 변수-->
<c:set var="sPlan" 			value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cPlan" 			value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fPlan" 			value="0"></c:set>	<!-- 무료과정 -->
<!--승인 변수-->
<c:set var="sChkfinal" 		value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cChkfinal" 		value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fChkfinal" 		value="0"></c:set>	<!-- 무료과정 -->
<!--실적 변수-->
<c:set var="sGraduated" 	value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cGraduated" 	value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fGraduated" 	value="0"></c:set>	<!-- 무료과정 -->
<!--과정내 이수율 변수-->
<c:set var="sSubChkfinal" 	value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cSubChkfinal" 	value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fSubChkfinal" 	value="0"></c:set>	<!-- 무료과정 -->
<!--이수율 변수-->
<c:set var="sSubComplet" value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cSubComplet" value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fSubComplet" value="0"></c:set>	<!-- 무료과정 -->
<!--종별 카운터 변수-->
<c:set var="sCount" 		value="0"></c:set>	<!-- 특별과정 -->
<c:set var="cCount" 		value="0"></c:set>	<!-- 일반과정 -->
<c:set var="fCount" 		value="0"></c:set>	<!-- 무료과정 -->

<c:set var="educationInsti" 			value="0"></c:set><!-- 교육청수입 -->
<c:set var="incomeAmount" 			value="0"></c:set><!-- 수입액 -->
<c:set var="contentDo" 				value="0"></c:set><!-- 만족도 -->
<c:set var="professorContentDo" 	value="0"></c:set><!-- 교수학습지도 향상 -->
<c:set var="propoCnt" 					value="0"></c:set><!-- 신청인원 -->
<c:set var="groupTotal" 				value="0"></c:set><!-- 단체신청인원(특별과정) -->
<c:set var="presentCnt" 				value="0"></c:set><!-- 제출인원 -->
<c:set var="normalProCnt" 			value="0"></c:set><!-- 일반수강인원 -->
<c:set var="groupProTottal" 			value="0"></c:set><!-- 단체신청인원 -->

		
		<!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" border="1">
<colgroup>
	<col width="75"/>
	<col width="55"/>
	<col width="40"/>
	<col width="226"/>
	<col width="50"/>
	<col width="50"/>
	<col width="50"/> 
	<col width="60"/>
	<col width="70"/> 
	<col width="90"/>
	<col width="90"/>
	<col width="50"/>
	<col width="65"/>
	<col width="110"/>
	<col width="110"/>
	<col width="40"/>
	<col width="190"/>
	<col width="60"/>
	<col width="120"/>
	<col width="60"/>
	<col width="60"/>
	<col width="60"/>
	<col width="212"/>
	<col width="60"/>
</colgroup>

<thead>     
<tr style="background-color: #bbbbbb;">
<th rowspan="2" >구분</th>
<th rowspan="2" >종별</th>
<th rowspan="2" >기별</th>
<th rowspan="2" >과 정 명</th>
<th colspan="3" >연수인원</th>
<th rowspan="2" >과정내</br>이수율</th>	
<th rowspan="2" >이수율</th>
<th rowspan="2" >교육청수입 </th>	
<th rowspan="2" >수입액 </th>
<th rowspan="2" >만족도</th>
<th rowspan="2" >교수학습지도 향상도</th>
<th rowspan="2" >신청기간</th>
<th rowspan="2" >교육시기</th>	
<th rowspan="2" >시간</th>	
<th rowspan="2" >연수대상</th>	
<th colspan="4" >특별과정 신청현황</th>
<th colspan="3" >수강현황</th>
</tr>

<tr style="background-color: #bbbbbb;">
<th >계획(명)</th>
<th >승인(명)</th>
<th >실적(명)</th>
<th >신청</br>인원</th>
<th >단체신청내역</br>(특별과정)</th>
<th >단체신청인원</th>
<th >제출</br>인원</th>
<th >일반수강인원</th>
<th >단체신청인원</th>
<th >단체신청인원수</th>
</tr>
</thead>

<tbody>
<c:forEach var="result" items="${resultList}" varStatus="status">
<c:set var="educationInsti" 			value="${educationInsti		+result.educationInsti}"></c:set>
<c:set var="incomeAmount" 			value="${incomeAmount		+result.incomeAmount}"></c:set>
<c:set var="contentDo" 				value="${contentDo				+result.contentDo}"></c:set>
<c:set var="professorContentDo" 	value="${professorContentDo	+result.professorContentDo}"></c:set>
<c:set var="propoCnt" 					value="${propoCnt				+result.propoCnt}"></c:set>
<c:set var="groupTotal" 				value="${groupTotal				+result.groupTotal}"></c:set>
<c:set var="presentCnt" 				value="${presentCnt				+result.presentCnt}"></c:set>
<c:set var="normalProCnt" 			value="${normalProCnt			+result.normalProCnt}"></c:set>
<c:set var="groupProTottal" 			value="${groupProTottal		+result.groupProTotal}"></c:set>
<c:choose>
	<c:when test="${result.ischarge eq 'S'}">
		<c:set var="sPlan" 				value="${sPlan+result.planCnt}"></c:set>					<!-- 계획 -->
		<c:set var="sChkfinal" 			value="${sChkfinal+result.chkfinalCnt}"></c:set>			<!-- 승인 -->
		<c:set var="sGraduated" 		value="${sGraduated+result.graduatedCnt}"></c:set>	<!-- 실적 -->
		<c:set var="sSubChkfinal" 		value="${sSubChkfinal+result.subjChkfinal}"></c:set>	<!-- 과정냉 이수율 -->
		<c:set var="sSubComplet" 	value="${sSubComplet+result.subjPlan}"></c:set>			<!-- 이수율 -->
		<c:set var="sCount" 			value="${sCount+1}"></c:set>									<!-- 종별 카운터 -->
	</c:when>
	<c:when test="${result.ischarge eq 'C'}">
		<c:set var="cPlan" 				value="${cPlan+result.planCnt}"></c:set>					<!-- 계획 -->
		<c:set var="cChkfinal" 			value="${cChkfinal+result.chkfinalCnt}"></c:set>			<!-- 승인 -->
		<c:set var="cGraduated" 		value="${cGraduated+result.graduatedCnt}"></c:set>	<!-- 실적 -->
		<c:set var="cSubChkfinal" 		value="${cSubChkfinal+result.subjChkfinal}"></c:set>	<!-- 과정냉 이수율 -->
		<c:set var="cSubComplet" 	value="${cSubComplet+result.subjPlan}"></c:set>			<!-- 이수율 -->
		<c:set var="cCount" 			value="${cCount+1}"></c:set>									<!-- 종별 카운터 -->
	</c:when>
	<c:when test="${result.ischarge eq 'F'}">
		<c:set var="fPlan" 				value="${fPlan+result.planCnt}"></c:set>					<!-- 계획 -->
		<c:set var="fChkfinal" 			value="${fChkfinal+result.chkfinalCnt}"></c:set>			<!-- 승인 -->
		<c:set var="fGraduated" 		value="${fGraduated+result.graduatedCnt}"></c:set>	<!-- 실적 -->
		<c:set var="fSubChkfinal" 		value="${fSubChkfinal+result.subjChkfinal}"></c:set>		<!-- 과정냉 이수율 -->
		<c:set var="fSubComplet" 	value="${fSubComplet+result.subjPlan}"></c:set>			<!-- 이수율 -->
		<c:set var="fCount" 			value="${fCount+1}"></c:set>									<!-- 종별 카운터 -->
	</c:when>
	
	<c:otherwise>
		
	</c:otherwise>
</c:choose>

 	<tr>
		<td>${result.upperclassNm}</td>																											<!--구분 a-->
		<td>${result.ischargeNm}</td>																												<!--종별 b-->
		<td>${result.subjSeq}</td>																													<!--기별 c-->
		<td style="text-align: left">${result.subjNm}</td>																						<!--과정명 d-->
		<td>${result.planCnt}</td>																													<!--계획(e) -->
		<td><c:if test="${result.chkfinalCnt ne '0'}">	<fmt:formatNumber value="${result.chkfinalCnt}"  pattern="#,###"/></c:if></td><!--승인 f -->
		<td><fmt:formatNumber value="${result.graduatedCnt}"  pattern="#,###"/></td><!--실적 g-->
		<td><c:if test="${result.subjChkfinal ne '0'}">${result.subjChkfinal}%</c:if></td>											<!--과정내이수율 h-->
		<td>${result.subjPlan}%</td>																												<!--이수율 i-->
		<td>${result.educationInsti}</td>																											<!--교육청수입 j-->
		<td>${result.incomeAmount}</td>																											<!--수입액 k-->
		<td><c:if test="${result.contentDo ne '00.0'}">${result.contentDo}%</c:if></td>											<!--만족도 l-->
		<td><c:if test="${result.professorContentDo ne '00.0'}">${result.professorContentDo}%</c:if></td>					<!--교수학습지도향상도 m-->
		<td>${result.propDate}</td>																													<!--등록기간(수강신청기간) n-->
		<td>${result.eduDate}</td>																													<!--연수시기(교육기간) o-->
		<td>${result.edutimes}</td>																													<!--시간 p-->
		<td>${result.edumans}</td>																													<!--연수대상 q-->
		<td>${result.propoCnt}</td>																													<!--신청인원 r-->
		<td>${result.groupCnt}</td>																													<!--단체신청내역 s-->
		<td>${result.groupTotal}</td>																												<!--단체신청인원-->
		<td>${result.presentCnt}</td>																												<!--제출인원 t-->
		<td><c:if test="${result.normalProCnt ne '0'}">${result.normalProCnt}</c:if></td>											<!--일반수강인원 u-->
		<td>${result.groupProCnt}</td>																												<!--단체신청인원 v-->
		<td>${result.groupProTotal}</td> <!--단체신청인원 수 w-->
	</tr>
	<c:if test="${result.upperclass ne result.upperclassLead}">
	<tr style="background-color: #eeeeee; ">
		<td></td>
		<td></td>
		<td></td>
		<td style="text-align: center"><strong>소계(${result.upperclassCnt})</strong></td>
		<td>${result.planTotal}</td>
		<td><strong><fmt:formatNumber value="${result.chkfinalTotal}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${result.graduatedTotal}"  pattern="#,###"/></strong></td>
		<td><strong>${result.subjChkfinalAvg}%</strong></td>
		<td>${result.subjPlanAvg}%</td>
		<td>${result.educationInstiTotal}</td>
		<td>${result.incomeAmountTotal}</td>
		<td><strong>${result.contentDoAvg}%</strong></td>
		<td><strong>${result.professorContentDoAvg}%</strong></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td>${result.propoTotal}</td>
		<td></td>
		<td>${result.groupTotalCnt}</td>
		<td>${result.presentTotal}</td>
		<td><strong><fmt:formatNumber value="${result.normalProTotal}"  pattern="#,###"/></strong></td>
		<td colspan="2"><strong><fmt:formatNumber value="${result.groupProTotalCnt}"  pattern="#,###"/></strong></td>
	</tr>
	</c:if>
</c:forEach>
<c:if test="${not empty resultList}">
<tr style="background-color: #ccccdd; ">
		<td></td>
		<td></td>
		<td></td>
		<td style="text-align: center"><strong>시.도 특별과정 계</strong></td>
		<td><strong><fmt:formatNumber value="${sPlan}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${sChkfinal}"  pattern="#,###.#"/></strong></td>
		<td><strong><fmt:formatNumber value="${sGraduated}"  pattern="#,###.#"/></strong></td>
		<td><strong><c:if test="${sSubChkfinal ne 0 && sCount ne 0}"><fmt:formatNumber value="${sSubChkfinal/sCount}"  pattern="#,###.#"/></c:if><c:if test="${sSubChkfinal eq 0 || sCount eq 0}">0</c:if>%</strong></td>
		<td><c:if test="${sSubComplet ne 0 && sCount ne 0}"><fmt:formatNumber value="${sSubComplet/sCount}"  pattern="#,###.#"/></c:if><c:if test="${sSubComplet eq 0 || sCount eq 0}">0</c:if>%</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td colspan="2"></td>
	</tr>
	<tr style="background-color: #ccccdd; ">
		<td></td>
		<td></td>
		<td></td>
		<td style="text-align: center"><strong>일반과정 계(정규+무료)</strong></td>
		<td><strong><fmt:formatNumber value="${cPlan+fPlan}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${cChkfinal+fChkfinal}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${cGraduated+fGraduated}"  pattern="#,###"/></strong></td>
		<td><strong><c:if test="${(cSubChkfinal+fSubChkfinal) ne 0 && (cCount+fCount) ne 0}"><fmt:formatNumber value="${(cSubChkfinal+fSubChkfinal)/(cCount+fCount)}"  pattern="#,###.#"/></c:if>	<c:if test="${(cSubChkfinal+fSubChkfinal) eq 0 || (cCount+fCount) eq 0}">0</c:if>%</strong></td>
		<td><c:if test="${(cSubComplet+fSubComplet) ne 0 && (cCount+fCount) ne 0}"><fmt:formatNumber value="${(cSubComplet+fSubComplet)/(cCount+fCount)}"  pattern="#,###.#"/></c:if><c:if test="${(cSubComplet+fSubComplet) eq 0 || (cCount+fCount) eq 0}">0</c:if>%</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td colspan="2"></td>
	</tr>
	<tr style="background-color: #ccddcc; ">
		<td></td>
		<td></td>
		<td></td>
		<td style="text-align: center"><strong>유료과정 계(정규+특별)</strong></td>
		<td><strong><fmt:formatNumber value="${cPlan+sPlan}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${cChkfinal+sChkfinal}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${cGraduated+sGraduated}"  pattern="#,###"/></strong></td>
		<td><strong><c:if test="${(cSubChkfinal+sSubChkfinal) ne 0 && (cCount+sCount) ne 0}"><fmt:formatNumber value="${(cSubChkfinal+sSubChkfinal)/(cCount+sCount)}"  pattern="#,###.#"/></c:if><c:if test="${(cSubChkfinal+sSubChkfinal) eq 0 || (cCount+sCount) eq 0}">0</c:if>%</strong></td>
		<td><c:if test="${(cSubComplet+sSubComplet) ne 0 && (cCount+sCount) ne 0}"><fmt:formatNumber value="${(cSubComplet+sSubComplet)/(cCount+sCount)}"  pattern="#,###.#"/></c:if><c:if test="${(cSubComplet+sSubComplet) eq 0 || (cCount+sCount) eq 0}">0</c:if>%</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>	
		<td colspan="2"></td>
	</tr>
	<tr style="background-color: #ccddcc; ">
		<td></td>
		<td></td>
		<td></td>
		<td style="text-align: center"><strong>무료과정 계</strong></td>
		<td><strong><fmt:formatNumber value="${fPlan}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${fChkfinal}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${fGraduated}"  pattern="#,###"/></strong></td>
		<td><strong><c:if test="${fSubChkfinal ne 0 && fCount ne 0}"><fmt:formatNumber value="${fSubChkfinal/fCount}"  pattern="#,###.#"/></c:if><c:if test="${fSubChkfinal eq 0 || fCount eq 0}">0</c:if>%</strong></td>
		<td><c:if test="${fSubComplet ne 0 && fCount ne 0}"><fmt:formatNumber value="${fSubComplet/fCount}"  pattern="#,###.#"/></c:if><c:if test="${fSubComplet eq 0 || fCount eq 0}">0</c:if>%</td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td colspan="2"></td>
	</tr>
	<tr style="background-color: #aaaaaa; ">
		<td></td>
		<td></td>
		<td></td>
		<td style="text-align: center"><strong>전체 소계(${sCount+cCount+fCount})</strong></td>
		<td><strong><fmt:formatNumber value="${sPlan+cPlan+fPlan}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${sChkfinal+cChkfinal+fChkfinal}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${sGraduated+cGraduated+fGraduated}"  pattern="#,###"/></strong></td>
		<td><strong><c:if test="${(sSubChkfinal+cSubChkfinal+fSubChkfinal) ne 0 && (sCount+cCount+fCount) ne 0}"><fmt:formatNumber value="${(sSubChkfinal+cSubChkfinal+fSubChkfinal)/(sCount+cCount+fCount)}"  pattern="#,###.#"/></c:if><c:if test="${(sSubChkfinal+cSubChkfinal+fSubChkfinal) eq 0 || (sCount+cCount+fCount) eq 0}">0</c:if>%</strong></td><!--과정내이수율 -->
		<td><fmt:formatNumber value="${(sSubComplet+cSubComplet+fSubComplet)/(sCount+cCount+fCount)}"  pattern="#,###.#"/>%</td>
		<td><strong><fmt:formatNumber value="${educationInsti}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${incomeAmount}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${contentDo/(sCount+cCount+fCount)}"  pattern="#,###.#"/>%</strong></td>
		<td><strong><fmt:formatNumber value="${professorContentDo/(sCount+cCount+fCount)}"  pattern="#,###.#"/>%</strong></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td><strong><fmt:formatNumber value="${propoCnt}"  pattern="#,###"/></strong></td>
		<td></td>
		<td><strong><fmt:formatNumber value="${groupTotal}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${presentCnt}"  pattern="#,###"/></strong></td>
		<td><strong><fmt:formatNumber value="${normalProCnt}"  pattern="#,###"/></strong></td>
		<td colspan="2"><strong><fmt:formatNumber value="${groupProTottal}"  pattern="#,###"/></strong></td>
	</tr>
</c:if>	
<c:if test="${empty resultList}">
	<tr>
		<td colspan="22" align="center">조회된 내용이 없습니다.</td>
	</tr>
</c:if>
</tbody>
</table>
<br><br><Br>
		</div>
		<!-- list table-->
<!----------------- 결과보고서(이수현황)  END ---------------------------------------------------------------------------------------->  

</body>
</html>
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--


//-->
</script>