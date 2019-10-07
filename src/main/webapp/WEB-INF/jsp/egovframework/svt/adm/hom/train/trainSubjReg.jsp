<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/svt/jquery.form.js"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
</form>

<form id="trainSubjFrm">
	<div class="tbDetail">
		<table summary="" cellspacing="0" width="100%">
			<colgroup>
				<col width="15%">
				<col width="85%">
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">과정명</th>
					<td scope="row"><input type="text" name="subjNm" value="${trainSubj.subjNm}" maxlength="50" style="width:300px;"/></td>
				</tr>
				<tr>
					<th scope="row">차시</th>
					<td scope="row"><input type="text" name="lessonNum" value="${trainSubj.lessonNum}" maxlength="3" style="width:300px;"/></td>
				</tr>
				<tr>
					<th scope="row">이미지</th>
					<td scope="row">
						<input type="file" name="imgFile"/>
						<c:if test="${!empty trainSubj.imgId}">
							<div id="imgDiv" style="margin-top: 10px;">
								<img src="/dp/train/${trainSubj.imgId}" border="0" width="210">
								<div style="display: inline-block; vertical-align: bottom;"><strong>${trainSubj.imgOriNm}</strong></div>
								<div style="display: inline-block; vertical-align: bottom;" class="btn"><a href="#" class="btn01" onclick="deleteTrainSubjImg();"><span>삭제</span></a></div>
							</div>
						</c:if>
					</td>
				</tr>
				<tr>
					<th scope="row">링크주소</th>
					<td scope="row">
						<input type="text" name="linkUrl" value="${trainSubj.linkUrl}" maxlength="100" style="width:300px;" placeholder="p_subj=과정코드"/>
						<strong style="color: red;">예) p_subj=COU160002</strong>
					</td>
				</tr>
				<tr>
					<th scope="row">사용여부</th>
					<td scope="row">
						<select name="useYn">
							<option value="Y" <c:if test="${'Y' eq trainSubj.useYn}">selected</c:if>>Y</option>
							<option value="N" <c:if test="${'N' eq trainSubj.useYn}">selected</c:if>>N</option>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</form>

<div class="listTop">			
    <div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn03" onclick="trainSubjList();"><span>취소</span></a></div>
    <c:choose>
    	<c:when test="${empty trainSubj}">
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="insertTrainSubj();"><span>저장</span></a></div>
    	</c:when>
    	<c:otherwise>
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="deleteTrainSubj();"><span>삭제</span></a></div>
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="updateTrainSubj();"><span>수정</span></a></div>
    	</c:otherwise>
    </c:choose>
	
</div>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp"%>

<script type="text/javascript">
var trainSeq;

$(document).ready(function() {
	// 상세보기
	if('${trainSubj}') 
	{
		trainSeq = '${trainSubj.trainSeq}'
	}
	// 등록
	else 
	{
		trainSeq = '${trainSeq}'
	}
});

function insertTrainSubj() {
	if(!isValid()) return;
	if(!confirm('저장하시겠습니까?')) return;
	
	$('#trainSubjFrm').ajaxSubmit({
		url: "${pageContext.request.contextPath}/adm/hom/train/insertTrainSubj.do"
		, type: 'post'
		, data: {
			trainSeq: trainSeq
		}
		, enctype: "multipart/form-data"
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				trainSubjList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}
function updateTrainSubj() {
	if(!isValid()) return;
	if(!confirm('수정하시겠습니까?')) return;
	
	$('#trainSubjFrm').ajaxSubmit({
		url: "${pageContext.request.contextPath}/adm/hom/train/updateTrainSubj.do"
		, type: 'post'
		, data: {
			trainSubjSeq: '${trainSubj.trainSubjSeq}'
		}
		, enctype: "multipart/form-data"
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				trainSubjList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}
function deleteTrainSubj() {
	if(!confirm('삭제하시겠습니까?')) return;
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/hom/train/deleteTrainSubj.do'
		, type: 'post'
		, data: {
			trainSubjSeq: '${trainSubj.trainSubjSeq}'
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				trainSubjList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}
function deleteTrainSubjImg() {
	if(!confirm('이미지삭제하시겠습니까?')) return;
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/hom/train/deleteTrainSubjImg.do'
		, type: 'post'
		, data: {
			imgId: '${trainSubj.imgId}'
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				$('#imgDiv').remove();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}
function trainSubjList() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.append('<input type="hidden" name="trainSeq" value="' + trainSeq + '"/>')
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/train/trainSubjList.do');
	$frm.attr('target', '_self');
	$frm.submit();
}

function isValid() {
	if($.trim($('input[name=subjNm]').val()) == '') {
		alert('과정명을 입력해 주세요.');
		$('input[name=subjNm]').focus();
		return false;
	}
	if($.trim($('input[name=lessonNum]').val()) == '') {
		alert('차수를 입력해 주세요.');
		$('input[name=lessonNum]').focus();
		return false;
	}
	return true;
}
</script>