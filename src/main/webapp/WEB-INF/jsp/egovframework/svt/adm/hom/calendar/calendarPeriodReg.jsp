<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/svt/jquery.form.js"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
</form>

<form id="calendarPeriodFrm">
	<div class="tbDetail">
		<table summary="" cellspacing="0" width="100%">
			<colgroup>
				<col width="15%">
				<col width="85%">
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">신청기간</th>
					<td scope="row"><input type="text" name="requestPeriod" value="${calendarPeriod.requestPeriod}" maxlength="25" style="width:300px;"/></td>
				</tr>
				<tr>
					<th scope="row">연수기간</th>
					<td scope="row"><input type="text" name="trainPeriod" value="${calendarPeriod.trainPeriod}" maxlength="25" style="width:300px;"/></td>
				</tr>
			</tbody>
		</table>
	</div>
</form>

<div class="listTop">			
    <div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn03" onclick="calendarPeriodList();"><span>취소</span></a></div>
    <c:choose>
    	<c:when test="${empty calendarPeriod}">
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="insertCalendarPeriod();"><span>저장</span></a></div>
    	</c:when>
    	<c:otherwise>
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="deleteCalendarPeriod();"><span>삭제</span></a></div>
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="updateCalendarPeriod();"><span>수정</span></a></div>
    	</c:otherwise>
    </c:choose>
	
</div>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp"%>

<script type="text/javascript">
var calendarSeq;

$(document).ready(function() {
	// 상세보기
	if('${calendarPeriod}') 
	{
		calendarSeq = '${calendarPeriod.calendarSeq}'
	}
	// 등록
	else 
	{
		calendarSeq = '${calendarSeq}'
	}
});

function insertCalendarPeriod() {
	if(!isValid()) return;
	if(!confirm('저장하시겠습니까?')) return;
	
	$('#calendarPeriodFrm').ajaxSubmit({
		url: "${pageContext.request.contextPath}/adm/hom/calendar/insertCalendarPeriod.do"
		, type: 'post'
		, data: {
			calendarSeq: calendarSeq
		}
		, enctype: "multipart/form-data"
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				calendarPeriodList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}
function updateCalendarPeriod() {
	if(!isValid()) return;
	if(!confirm('수정하시겠습니까?')) return;
	
	$('#calendarPeriodFrm').ajaxSubmit({
		url: "${pageContext.request.contextPath}/adm/hom/calendar/updateCalendarPeriod.do"
		, type: 'post'
		, data: {
			calendarPeriodSeq: '${calendarPeriod.calendarPeriodSeq}'
		}
		, enctype: "multipart/form-data"
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				calendarPeriodList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}
function deleteCalendarPeriod() {
	if(!confirm('삭제하시겠습니까?')) return;
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/hom/calendar/deleteCalendarPeriod.do'
		, type: 'post'
		, data: {
			calendarPeriodSeq: '${calendarPeriod.calendarPeriodSeq}'
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				calendarPeriodList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}
function calendarPeriodList() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="calendarSeq" value="' + calendarSeq + '"/>')
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/calendar/calendarPeriodList.do');
	$frm.attr('target', '_self');
	$frm.submit();
}

function isValid() {
	if($.trim($('input[name=requestPeriod]').val()) == '') {
		alert('신청기간을 입력해 주세요.');
		$('input[name=requestPeriod]').focus();
		return false;
	}
	if($.trim($('input[name=trainPeriod]').val()) == '') {
		alert('연수기간을 입력해 주세요.');
		$('input[name=trainPeriod]').focus();
		return false;
	}
	return true;
}
</script>