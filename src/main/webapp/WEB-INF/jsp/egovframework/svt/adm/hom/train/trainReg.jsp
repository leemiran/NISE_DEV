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
				<th scope="row">카테고리 명</th>
				<td scope="row"><input type="text" name="categoryNm" value="${train.categoryNm}" maxlength="15" style="width:300px;"/></td>
			</tr>
		</tbody>
	</table>
</div>

<div class="listTop">			
    <div class="btnR MR05"><a href="#" class="btn03" onclick="trainList();"><span>취소</span></a></div>
    <c:choose>
    	<c:when test="${empty train}">
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="insertTrain();"><span>저장</span></a></div>
    	</c:when>
    	<c:otherwise>
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="deleteTrain();"><span>삭제</span></a></div>
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="updateTrain();"><span>수정</span></a></div>
    	</c:otherwise>
    </c:choose>
	
</div>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp"%>

<script type="text/javascript">
function insertTrain() {
	if(!isValid()) return;
	if(!confirm('저장하시겠습니까?')) return;
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/hom/train/insertTrain.do'
		, type: 'post'
		, data: {
			categoryNm: $('input[name=categoryNm]').val()
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				trainList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);   
		}
	});
}
function updateTrain() {
	if(!isValid()) return;
	if(!confirm('수정하시겠습니까?')) return;
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/hom/train/updateTrain.do'
		, type: 'post'
		, data: {
			categoryNm: $('input[name=categoryNm]').val()
			, trainSeq: '${train.trainSeq}'
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				trainList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);   
		}
	});
}
function deleteTrain() {
	if(!confirm('삭제하시겠습니까?')) return;
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/hom/train/deleteTrain.do'
		, type: 'post'
		, data: {
			trainSeq: '${train.trainSeq}'
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				trainList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);   
		}
	});
}
function trainList() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.attr('action', '${pageContext.request.contextPath}/adm/hom/train/trainList.do');
	$frm.attr('target', '_self');
	$frm.submit();
}

function isValid() {
	if($.trim($('input[name=categoryNm]').val()) == '') {
		alert('카테고리명을 입력해 주세요.');
		$('input[name=categoryNm]').focus();
		return false;
	}
	return true;
}
</script>