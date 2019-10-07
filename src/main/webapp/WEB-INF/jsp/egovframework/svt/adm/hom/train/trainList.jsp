<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
</form>
	
<div class="listTop">	
	<div class="btnR"><a href="#" class="btn01" onclick="trainReg();"><span>등록</span></a></div>
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
				<c:when test="${!empty trainList}">
					<c:forEach var="train" items="${trainList}" varStatus="status">
						<tr>
							<td>${fn:length(trainList) - status.index}</td>
							<td class="left"><div onclick="trainDetail('${train.trainSeq}');" style="cursor: pointer;">${train.categoryNm}</div></td>
							<td>
								<div class="btn">
									<a href="javascript:void(0);return false;" class="btn01" onclick="trainSubjList('${train.trainSeq}');">
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
function trainReg() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/train/trainReg.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
function trainDetail(trainSeq) {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="trainSeq" value="' + trainSeq + '"/>')
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/train/trainDetail.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
function trainSubjList(trainSeq) {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="trainSeq" value="' + trainSeq + '"/>')
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/train/trainSubjList.do');
	$frm.attr('target', '_self');
	$frm.submit();
}
</script>