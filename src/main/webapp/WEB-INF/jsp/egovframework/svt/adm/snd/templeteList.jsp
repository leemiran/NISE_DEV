<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
</form>

<div class="listTop">	
	<div class="btnR"><a href="#" class="btn01" onclick="templeteReg();"><span>등록</span></a></div>
</div>

<div class="tbList">
	<table summary="" cellspacing="0" width="100%">
		<caption>목록</caption>
		<colgroup>
			<col width="100px">
			<col>
			<col width="150px">
			<col width="150px">
		</colgroup>
		<thead>
			<tr>
				<th>No</th>
				<th>템플릿 명</th>
				<th>사용여부</th>
				<th>등록일</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${!empty templeteList}">
					<c:forEach var="templete" items="${templeteList}" varStatus="status">
						<tr>
							<td>${fn:length(templeteList) - status.index}</td>
							<td class="left"><div onclick="templeteDetail('${templete.templeteSeq}');" style="cursor: pointer;">${templete.title}</div></td>
							<td>${templete.useYn}</td>
							<td>${templete.ldate}</td>
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
function templeteReg() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.attr('action', '${pageContext.request.contextPath}/adm/snd/templeteReg.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
function templeteDetail(templeteSeq) {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="templeteSeq" value="' + templeteSeq + '"/>')
	$frm.attr('action', '${pageContext.request.contextPath}/adm/snd/templeteDetail.do');
	$frm.attr('target', '_self');
	$frm.submit();
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