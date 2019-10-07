<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
</form>

<div class="listTop">
	<div>카테고리 검색: 
		<select name="schCalendarSeq" id="schCalendarSeq">
			<c:forEach var="calendar" items="${calendarList}" varStatus="status">
				<option value="${calendar.calendarSeq}" <c:if test="${calendarSeq eq calendar.calendarSeq}">selected</c:if>>${calendar.categoryNm}</option>
			</c:forEach>
		</select>
	</div>	
	<div class="btnR"><a href="javascript:void(0);return false;" class="btn01" onclick="calendarPeriodReg();"><span>등록</span></a></div>
</div>

<div class="tbList">
	<table summary="" cellspacing="0" width="100%">
		<caption>목록</caption>
		<colgroup>
			<col width="100px">
			<col>
			<col>
			<col width="150px">
		</colgroup>
		<thead>
			<tr>
				<th>No</th>
				<th>신청기간</th>
				<th>연수기간</th>
				<th>등록일</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${!empty calendarPeriodList}">
					<c:forEach var="calendarPeriod" items="${calendarPeriodList}" varStatus="status">
						<tr>
							<td>${fn:length(calendarPeriodList) - status.index}</td>
							<td class="left"><div onclick="calendarPeriodDetail('${calendarPeriod.calendarPeriodSeq}');" style="cursor: pointer;">${calendarPeriod.requestPeriod}</div></td>
							<td>${calendarPeriod.trainPeriod}</td>
							<td>${calendarPeriod.regDate}</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise><tr><td colspan="4">데이터가 없습니다.</td></tr></c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp"%>

<script type="text/javascript">
function calendarPeriodReg() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="calendarSeq" value="${calendarSeq}"/>');
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/calendar/calendarPeriodReg.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
function calendarPeriodDetail(calendarPeriodSeq) {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="calendarPeriodSeq" value="' + calendarPeriodSeq + '"/>')
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/calendar/calendarPeriodDetail.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
function calendarPeriodList(calendarSeq) {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="calendarSeq" value="' + calendarSeq + '"/>')
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/calendar/calendarPeriodList.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
$(document).ready(function() {
	$('#schCalendarSeq').on('change', function() {
		calendarPeriodList($(this).val())
	});
});
</script>