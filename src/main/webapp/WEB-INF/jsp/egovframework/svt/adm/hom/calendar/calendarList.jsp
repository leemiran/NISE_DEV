<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
</form>

<div class="tbDetail">
	<table summary="" cellspacing="0" width="100%">
		<colgroup>
			<col width="15%">
			<col width="85%">
		</colgroup>
		<tbody>
			<tr>
				<th scope="row">일정안내 제목</th>
				<td scope="row">
					<input type="text" id="titleNm" name="titleNm" value="${calendarTitle.titleNm}" style="width:300px;"/>
					<div class="btn" style="display: inline-block;"><a href="javascript:void(0);return false;" class="btn01" onclick="updateCalendarTitle();"><span>저장</span></a></div>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<div class="listTop">	
	<div class="btnR"><a href="#" class="btn01" onclick="calendarReg();"><span>등록</span></a></div>
</div>

<div class="tbList">
	<table summary="" cellspacing="0" width="100%">
		<caption>목록</caption>
		<colgroup>
			<col width="100px">
			<col>
			<col width="150px">
		</colgroup>
		<thead>
			<tr>
				<th>No</th>
				<th>카테고리</th>
				<th>수정</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${!empty calendarList}">
					<c:forEach var="calendar" items="${calendarList}" varStatus="status">
						<tr>
							<td>${fn:length(calendarList) - status.index}</td>
							<td class="left"><div onclick="calendarDetail('${calendar.calendarSeq}');" style="cursor: pointer;">${calendar.categoryNm}</div></td>
							<td>
								<div class="btn">
									<a href="javascript:void(0);return false;" class="btn01" onclick="calendarPeriodList('${calendar.calendarSeq}');">
										<span>관리</span>
									</a>
								</div>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise><tr><td colspan="3">데이터가 없습니다.</td></tr></c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp"%>

<script type="text/javascript">
function calendarReg() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/calendar/calendarReg.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
function calendarDetail(calendarSeq) {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="calendarSeq" value="' + calendarSeq + '"/>')
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/calendar/calendarDetail.do');
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
function updateCalendarTitle() {
	if(!isValid()) return;
	if(!confirm('수정하시겠습니까?')) return;
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/hom/calendar/updateCalendarTitle.do'
		, type: 'post'
		, data: {
			calendarTitleSeq: '${calendarTitle.calendarTitleSeq}'
			, titleNm: $('#titleNm').val()
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}

function isValid() {
	if($.trim($('#titleNm').val()) == '') {
		alert('일정안내제목을 입력해 주세요.');
		$('#titleNm').focus();
		return false;
	}
	return true;
}
</script>