<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/smartEditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
</form>

<div class="tbDetail">
	<table summary="" cellspacing="0" width="100%">
		<colgroup>
			<col width="10%">
			<col width="90%">
		</colgroup>
		<tbody>
			<tr>
				<th scope="row">템플릿 명</th>
				<td scope="row"><input type="text" name="title" id="title" value="${templete.title}" maxlength="15" style="width:300px;"/></td>
			</tr>
			<tr>
				<th scope="row">사용여부</th>
				<td scope="row">
					<select id="useYn" name="useYn">
						<option value="Y" <c:if test="${'Y' == templete.useYn}">selected</c:if>>Y</option>
						<option value="N" <c:if test="${'N' == templete.useYn}">selected</c:if>>N</option>
					</select>
				</td>
			</tr>
			<tr>
				<th scope="row">내용</th>
				<td scope="row">
					<textarea name="content" id="content" rows="10" cols="100" style="width:100%; height:412px; display:none;">${templete.content}</textarea>
					<input type="hidden" name="content" id="content"/>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<div class="listTop">			
    <div class="btnR MR05"><a href="#" class="btn03" onclick="templeteList();"><span>취소</span></a></div>
    <c:choose>
    	<c:when test="${empty templete}">
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="insertTemplete();"><span>저장</span></a></div>
    	</c:when>
    	<c:otherwise>
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="deleteTemplete();"><span>삭제</span></a></div>
    		<div class="btnR MR05"><a href="javascript:void(0);return false;" class="btn02" onclick="updateTemplete();"><span>수정</span></a></div>
    	</c:otherwise>
    </c:choose>
	
</div>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp"%>

<script type="text/javascript">
var oEditors = [];
nhn.husky.EZCreator.createInIFrame({
oAppRef: oEditors,
elPlaceHolder: "content",
sSkinURI: "<%=request.getContextPath()%>/smartEditor/SmartEditor2Skin.html",
//sSkinURI: "<%=request.getContextPath()%>/smartEditor/SmartEditor2Skin_noimg.html",
fCreator: "createSEditor2"
});
var fileName="";
var upfileName="";
function pasteHTML(filepath){
    var sHTML = '<img src="http://iedu.nise.go.kr/dp/smartEditor/photo_uploader/uploadFolder/'+fileName+'"'+" alt="+upfileName+">";

    oEditors.getById["content"].exec("PASTE_HTML", [sHTML]); 

}

function insertTemplete() {
	if(!isValid()) return;
	if(!confirm('저장하시겠습니까?')) return;
	
	oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/snd/insertTemplete.do'
		, type: 'post'
		, data: {
			title: $('#title').val()
			, useYn: $('select[name=useYn] option:selected').val()
			, content: $('#content').val()
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				templeteList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);   
		}
	});
}
function updateTemplete() {
	if(!isValid()) return;
	if(!confirm('수정하시겠습니까?')) return;
	
	oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/snd/updateTemplete.do'
		, type: 'post'
		, data: {
			title: $('#title').val()
			, useYn: $('select[name=useYn] option:selected').val()
			, content: $('#content').val()
			, templeteSeq: '${templete.templeteSeq}'
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				templeteList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);   
		}
	});
}
function deleteTemplete() {
	if(!confirm('삭제하시겠습니까?')) return;
	
	$.ajax({
		url: '${pageContext.request.contextPath}/adm/snd/deleteTemplete.do'
		, type: 'post'
		, data: {
			templeteSeq: '${templete.templeteSeq}'
		}
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				templeteList();
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);   
		}
	});
}
function templeteList() {
	var $frm = $('#' + '${gsMainForm}');
	$frm.attr('action', '${pageContext.request.contextPath}/adm/snd/templeteList.do');
	$frm.attr('target', '_self');
	$frm.submit();
}

function isValid() {
	if($.trim($('input[name=title]').val()) == '') {
		alert('템플릿 명을 입력해 주세요.');
		$('input[name=title]').focus();
		return false;
	}
	return true;
}
</script>