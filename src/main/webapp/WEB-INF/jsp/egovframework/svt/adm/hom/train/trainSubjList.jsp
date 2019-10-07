<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
</form>

<div class="searchWrap txtL">
	<div>		
		<ul class="datewrap">
			<li class="floatL">
				<select name="schType" id="schType">
					<option value="subjNm" <c:if test="${'subjNm' == schType}">selected</c:if>>과정명</option>
					<option value="lessonNum" <c:if test="${'lessonNum' == schType}">selected</c:if>>차시</option>
				</select>
				<input type="text" name="keyword" id="keyword" value="${keyword}" size="50" onkeypress="fn_keyEvent('doPageList')" style="ime-mode:active;">
				<a href="#none" class="btn_search" onclick="schList();"><span>검색</span></a>
			</li>
		</ul>		
	</div>
</div>

<div class="listTop">
	<div>카테고리 검색: 
		<select name="schTrainSeq" id="schTrainSeq">
			<c:forEach var="train" items="${trainList}" varStatus="status">
				<option value="${train.trainSeq}" <c:if test="${trainSeq eq train.trainSeq}">selected</c:if>>${train.categoryNm}</option>
			</c:forEach>
		</select>
	</div>	
	<div class="btnR"><a href="javascript:void(0);return false;" class="btn01" onclick="trainSubjReg();"><span>등록</span></a></div>
</div>

<div class="tbList">
	<table summary="" cellspacing="0" width="100%">
		<caption>목록</caption>
		<colgroup>
			<col width="100px">
			<col>
			<col width="150px">
			<col width="150px">
			<col width="150px">
		</colgroup>
		<thead>
			<tr>
				<th>No</th>
				<th>과정명</th>
				<th>사용여부</th>
				<th>차시</th>
				<th>등록일</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${!empty trainSubjList}">
					<c:forEach var="trainSubj" items="${trainSubjList}" varStatus="status">
						<tr>
							<td>${fn:length(trainSubjList) - status.index}</td>
							<td class="left"><div onclick="trainSubjDetail('${trainSubj.trainSubjSeq}');" style="cursor: pointer;">${trainSubj.subjNm}</div></td>
							<td>${trainSubj.useYn}</td>
							<td>${trainSubj.lessonNum}</td>
							<td>${trainSubj.regDate}</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise><tr><td colspan="5">데이터가 없습니다.</td></tr></c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp"%>

<script type="text/javascript">
function trainSubjReg() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="trainSeq" value="${trainSeq}"/>');
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/train/trainSubjReg.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
function trainSubjDetail(trainSubjSeq) {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="trainSubjSeq" value="' + trainSubjSeq + '"/>');
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/train/trainSubjDetail.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
function trainSubjList(trainSeq) {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="trainSeq" value="' + trainSeq + '"/>');
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/train/trainSubjList.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
function schList() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="schType" value="' + $('#schType option:selected').val() + '"/>');
	$frm.append('<input type="hidden" name="keyword" value="' + $('#keyword').val() + '"/>');
	trainSubjList('${trainSeq}');
}
$(document).ready(function() {
	$('#schTrainSeq').on('change', function() {
		trainSubjList($(this).val());
	});
});
</script>