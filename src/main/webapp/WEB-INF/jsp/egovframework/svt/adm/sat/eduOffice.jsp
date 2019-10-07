<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<script type="text/javascript">
	<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
</script>
<style>
.tbDetail th, .tbDetail td {
    padding: 5px 5px 5px 5px;
}
</style>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType" value="EDU_OFFICE">조회조건타입 : 타입별 세부조회조건</c:param>
		<c:param name="selectParameter" value="">조회조건 추가 : admSearchParameter.jsp 추가</c:param>
	</c:import>
	
	<div class="listTop">
		<div class="btnR MR05">
			<a href="#none" onclick="excelDown();" class="btn_excel"><span>엑셀출력</span></a>
		</div>
	</div>
	
	<!-- detail wrap -->
    <div class="tbDetail">    
		<table summary="" cellspacing="0" width="100%">
			<caption>목록</caption>
			<colgroup>
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
			<col width="" />
		</colgroup>
		<thead>
			<tr>
				<td rowspan="2" colspan="2" scope="row" style="text-align: center;">연수기간</td>
				<td colspan="6" scope="row" style="text-align: center;">30시간 미만과정</td>
				<td colspan="30" scope="row" style="text-align: center;">
					<c:if test="${ses_search_gmonth eq '03'}">${ses_search_gyear}.3.8(수) ~ 3.21(화)</c:if>
					<c:if test="${ses_search_gmonth eq '04'}">${ses_search_gyear}.4.5(수) ~ 4.18(화)</c:if>
					<c:if test="${ses_search_gmonth eq '05'}">${ses_search_gyear}.5.10(수) ~ 5.23(화)</c:if>
					<c:if test="${ses_search_gmonth eq '06'}">${ses_search_gyear}.6.7(수) ~ 6.20(화)</c:if>
					<c:if test="${ses_search_gmonth eq '07'}">${ses_search_gyear}.7.5(수) ~ 7.18(화)</c:if>
					<c:if test="${ses_search_gmonth eq '08'}">${ses_search_gyear}.8.9(수) ~ 8.22(화)</c:if>
					<c:if test="${ses_search_gmonth eq '09'}">${ses_search_gyear}.9.6(수) ~ 9.19(화)</c:if>
					<c:if test="${ses_search_gmonth eq '10'}">${ses_search_gyear}.10.11(수) ~ 10.24(화)</c:if>
					<c:if test="${ses_search_gmonth eq '11'}">${ses_search_gyear}.11.8(수) ~ 11.21(화)</c:if>
					<c:if test="${empty ses_search_gmonth}">${ses_search_gyear}</c:if>
				</td>
				<th rowspan="3" colspan="3" scope="row" style="text-align: center;">총합계</th>
			</tr>
			<tr>
				<td colspan="6" scope="row" style="text-align: center;">30시간 이상과정</td>
				<td colspan="30" scope="row" style="text-align: center;">
					<c:if test="${ses_search_gmonth eq '03'}">${ses_search_gyear}.3.8(수) ~ 3.28(화)</c:if>
					<c:if test="${ses_search_gmonth eq '04'}">${ses_search_gyear}.4.5(수) ~ 4.25(화)</c:if>
					<c:if test="${ses_search_gmonth eq '05'}">${ses_search_gyear}.5.10(수) ~ 5.30(화)</c:if>
					<c:if test="${ses_search_gmonth eq '06'}">${ses_search_gyear}.6.7(수) ~ 6.27(화)</c:if>
					<c:if test="${ses_search_gmonth eq '07'}">${ses_search_gyear}.7.5(수) ~ 7.25(화)</c:if>
					<c:if test="${ses_search_gmonth eq '08'}">${ses_search_gyear}.8.9(수) ~ 8.29(화)</c:if>
					<c:if test="${ses_search_gmonth eq '09'}">${ses_search_gyear}.9.6(수) ~ 9.26(화)</c:if>
					<c:if test="${ses_search_gmonth eq '10'}">${ses_search_gyear}.10.11(수) ~ 10.31(화)</c:if>
					<c:if test="${ses_search_gmonth eq '11'}">${ses_search_gyear}.11.8(수) ~ 11.28(화)</c:if>
					<c:if test="${empty ses_search_gmonth}">${ses_search_gyear}</c:if>
				</td>
			</tr>
			<tr>
				<td rowspan="2" scope="row" style="text-align: center;">연수<br/>구분</td>
				<th scope="row" style="text-align: center;">시도 교육청명</th>
				<th colspan="2" scope="row" style="text-align: center;">서울</th>
				<th colspan="2" scope="row" style="text-align: center;">부산</th>
				<th colspan="2" scope="row" style="text-align: center;">대구</th>
				<th colspan="2" scope="row" style="text-align: center;">인천</th>
				<th colspan="2" scope="row" style="text-align: center;">광주</th>
				<th colspan="2" scope="row" style="text-align: center;">대전</th>
				<th colspan="2" scope="row" style="text-align: center;">울산</th>				
				<th colspan="2" scope="row" style="text-align: center;">경기</th>
				<th colspan="2" scope="row" style="text-align: center;">강원</th>
				<th colspan="2" scope="row" style="text-align: center;">충북</th>
				<th colspan="2" scope="row" style="text-align: center;">세종</th>
				<th colspan="2" scope="row" style="text-align: center;">충남</th>
				<th colspan="2" scope="row" style="text-align: center;">전북</th>
				<th colspan="2" scope="row" style="text-align: center;">전남</th>
				<th colspan="2" scope="row" style="text-align: center;">경북</th>
				<th colspan="2" scope="row" style="text-align: center;">경남</th>
				<th colspan="2" scope="row" style="text-align: center;">제주</th>
				<th colspan="2" scope="row" style="text-align: center;">기타</th>	
			</tr>
			<tr>
				<td scope="row" style="text-align: center;">과정명 / 수강 및 이수현황</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<td scope="row" style="text-align: center;">수강</td>
				<td scope="row" style="text-align: center;">이수</td>
				<th scope="row" style="text-align: center;">수강</th>
				<th scope="row" style="text-align: center;">이수</th>
				<th scope="row" style="text-align: center;">이수율</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${not empty eduOffice}">
					<c:forEach items="${eduOffice}" var="result" varStatus="status">
						<c:if test="${result.groupingUpperclass == 0 and result.groupingSubj == 0}">
							<tr>
								<td class="gubun" style="text-align: center;">${result.classname}</td>
								<td style="text-align: left;">${result.subjnm}</td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.seoul}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.seoulIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.busan}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.busanIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.daegu}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.daeguIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.incheon}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.incheonIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.gwangju}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.gwangjuIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.daejeon}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.daejeonIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.ulsan}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.ulsanIsuY}" groupingUsed="true"/></td>								
								<td style="text-align: right;"><fmt:formatNumber value="${result.gyeonggi}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.gyeonggiIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.gangwon}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.gangwonIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.chungbuk}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.chungbukIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.sejong}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.sejongIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.chungnam}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.chungnamIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.jeonbuk}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.jeonbukIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.jeonnam}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.jeonnamIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.gyeongbuk}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.gyeongbukIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.gyeongnam}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.gyeongnamIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.jeju}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.jejuIsuY}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.edu}" groupingUsed="true"/></td>
								<td style="text-align: right;"><fmt:formatNumber value="${result.eduIsuY}" groupingUsed="true"/></td>
								<th style="text-align: right;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.studentCntIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;">${fn2:toPercent(result.studentCntIsuY, result.studentCnt)}</th>
							</tr>
						</c:if>
						<c:if test="${result.groupingUpperclass == 0 and result.groupingSubj == 1}">
							<tr>
								<td class="gubun" style="text-align: center;">${result.classname}</td>
								<th style="text-align: center;">계</th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.seoul}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.seoulIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.busan}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.busanIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.daegu}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.daeguIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.incheon}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.incheonIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gwangju}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gwangjuIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.daejeon}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.daejeonIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.ulsan}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.ulsanIsuY}" groupingUsed="true"/></th>								
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeonggi}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeonggiIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gangwon}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gangwonIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.chungbuk}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.chungbukIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.sejong}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.sejongIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.chungnam}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.chungnamIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeonbuk}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeonbukIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeonnam}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeonnamIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeongbuk}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeongbukIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeongnam}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeongnamIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeju}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jejuIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.edu}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.eduIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.studentCntIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;">${fn2:toPercent(result.studentCntIsuY, result.studentCnt)}</th>
							</tr>
						</c:if>
						<c:if test="${result.groupingUpperclass == 1 and result.groupingSubj == 1}">
							<tr>
								<th colspan="2" style="text-align: center;">총합계</th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.seoul}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.seoulIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.busan}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.busanIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.daegu}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.daeguIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.incheon}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.incheonIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gwangju}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gwangjuIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.daejeon}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.daejeonIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.ulsan}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.ulsanIsuY}" groupingUsed="true"/></th>
								
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeonggi}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeonggiIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gangwon}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gangwonIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.chungbuk}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.chungbukIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.sejong}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.sejongIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.chungnam}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.chungnamIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeonbuk}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeonbukIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeonnam}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeonnamIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeongbuk}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeongbukIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeongnam}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.gyeongnamIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jeju}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.jejuIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.edu}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.eduIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.studentCnt}" groupingUsed="true"/></th>
								<th style="text-align: right;"><fmt:formatNumber value="${result.studentCntIsuY}" groupingUsed="true"/></th>
								<th style="text-align: right;">${fn2:toPercent(result.studentCntIsuY, result.studentCnt)}</th>
							</tr>
						</c:if>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="39" style="text-align: center;">데이터가 존재하지 않습니다.</td></tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<script>
		$(".gubun").each(function () {
            var rows = $(".gubun:contains('" + $(this).text() + "')");
            if (rows.length > 1) {
                rows.eq(0).attr("rowspan", rows.length);
                rows.not(":eq(0)").remove(); 
            }
       	});
	</script>
</div>

<div id="chartdiv" style="width: 100%; height: 500px;"></div>

<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	thisForm.action = "/adm/sat/eduOffice.do";
	thisForm.target = "_self";
	thisForm.submit();
}

function excelDown() {
	thisForm.action = "/adm/sat/eduOfficeExcel.do";
	thisForm.target = "_self";
	thisForm.submit();
}
</script>

<c:if test="${not empty ses_search_gmonth}"><c:set var="p_gmonth" value="${ses_search_gmonth}월"/></c:if>
<c:if test="${empty ses_search_gmonth}"><c:set var="p_gmonth" value=" "/></c:if>


<script type="text/javascript" src="${pageContext.request.contextPath}/script/amcharts/amcharts.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/amcharts/serial.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/amcharts/themes/light.js"></script>
<!-- graph -->
<script type="text/javascript">
	var dataList = [];
	
	var mapSeoul = {};
	var mapBusan = {};
	var mapDaegu = {};
	var mapIncheon = {};
	var mapGwangju = {};
	var mapDaejeon = {};
	var mapUlsan = {};	
	var mapGyeonggi = {};
	var mapGangwon = {};
	var mapChungbuk = {};
	var mapSejong = {};
	var mapChungnam = {};
	var mapJeonbuk = {};
	var mapJeonnam = {};
	var mapGyeongbuk = {};
	var mapGyeongnam = {};
	var mapJeju = {};
	var mapEdu = {};
	
	mapSeoul.category = '서울';
	mapBusan.category = '부산';
	mapDaegu.category = '대구';
	mapIncheon.category = '인천';
	mapGwangju.category = '광주';
	mapDaejeon.category = '대전';
	mapUlsan.category = '울산';	
	mapGyeonggi.category = '경기';
	mapGangwon.category = '강원';
	mapChungbuk.category = '충북';
	mapSejong.category = '세종';
	mapChungnam.category = '충남';
	mapJeonbuk.category = '전북';
	mapJeonnam.category = '전남';
	mapGyeongbuk.category = '경북';
	mapGyeongnam.category = '경남';
	mapJeju.category = '제주';
	mapEdu.category = '기타';
	
	var graphList = [];
	
	var count = 1;
	<c:forEach items="${eduOffice}" var="result" varStatus="status">
		<c:if test="${result.groupingUpperclass == 0 and result.groupingSubj == 1}">
			mapSeoul['${result.upperclass}'] = '${result.seoulIsuY}';
			mapBusan['${result.upperclass}'] = '${result.busanIsuY}';
			mapDaegu['${result.upperclass}'] = '${result.daeguIsuY}';
			mapIncheon['${result.upperclass}'] = '${result.incheonIsuY}';
			mapGwangju['${result.upperclass}'] = '${result.gwangjuIsuY}';
			mapDaejeon['${result.upperclass}'] = '${result.daejeonIsuY}';
			mapUlsan['${result.upperclass}'] = '${result.ulsanIsuY}';			
			mapGyeonggi['${result.upperclass}'] = '${result.gyeonggiIsuY}';
			mapGangwon['${result.upperclass}'] = '${result.gangwonIsuY}';
			mapChungbuk['${result.upperclass}'] = '${result.chungbukIsuY}';
			mapSejong['${result.upperclass}'] = '${result.sejongIsuY}';
			mapChungnam['${result.upperclass}'] = '${result.chungnamIsuY}';
			mapJeonbuk['${result.upperclass}'] = '${result.jeonbukIsuY}';
			mapJeonnam['${result.upperclass}'] = '${result.jeonnamIsuY}';
			mapGyeongbuk['${result.upperclass}'] = '${result.gyeongbukIsuY}';
			mapGyeongnam['${result.upperclass}'] = '${result.gyeongnamIsuY}';
			mapJeju['${result.upperclass}'] = '${result.jejuIsuY}';
			mapEdu['${result.upperclass}'] = '${result.eduIsuY}';
			
			var graphMap = {
				"balloonText": "[[category]], [[title]]: [[value]]",
				"fillAlphas": 1,
				"type": "column",
				"id": "AmGraph-" + count,
				"title": "${result.classname}",
				"valueField": "${result.upperclass}"
			};
			
			graphList.push(graphMap);
			count ++;
		</c:if>
	</c:forEach>
	
	dataList.push(mapSeoul);
	dataList.push(mapBusan);
	dataList.push(mapDaegu);
	dataList.push(mapIncheon);
	dataList.push(mapGwangju);
	dataList.push(mapDaejeon);
	dataList.push(mapUlsan);	
	dataList.push(mapGyeonggi);
	dataList.push(mapGangwon);
	dataList.push(mapChungbuk);
	dataList.push(mapSejong);
	dataList.push(mapChungnam);
	dataList.push(mapJeonbuk);
	dataList.push(mapJeonnam);
	dataList.push(mapGyeongbuk);
	dataList.push(mapGyeongnam);
	dataList.push(mapJeju);
	dataList.push(mapEdu);
	
	AmCharts.makeChart("chartdiv",
			{
				"type": "serial",
				"categoryField": "category",
				"theme": "light",
				"categoryAxis": {
					"gridPosition": "start"
				},
				"trendLines": [],
				"graphs": graphList,
				"guides": [],
				"valueAxes": [
					{
						"id": "ValueAxis-1",
						"stackType": "regular",
						"title": "이수자"
					}
				],
				"allLabels": [],
				"balloon": {},
				"legend": {
					"enabled": true,
					"useGraphSettings": true
				},
				"titles": [
					{
						"id": "Title-1",
						"size": 15,
						"text": "${ses_search_gyear}년도   ${p_gmonth} 교육청 이수자"
					}
				],
				"dataProvider": dataList
			}
		);
</script>
<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>