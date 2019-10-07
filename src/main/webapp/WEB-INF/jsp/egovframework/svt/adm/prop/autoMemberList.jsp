<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/svt/jquery.form.js"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>

	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType" value="B">조회조건타입: 타입별 세부조회조건</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
</form>
<c:if test="${!empty ses_search_subj and !empty ses_search_year and !empty ses_search_subjseq}">
	<div class="listTop">
		<div class="btnR MR05">
			<a href="#none" onclick="downloadSample()" class="btn01"><span>자동승인샘플 다운로드</span></a>
		</div>
		<div class="btnR MR05">
			<a href="#none" onclick="insertExcel()" class="btn01"><span>자동승인등록</span></a>
		</div>
		<div class="btnR MR05">
			<form id="autoMemberFrm">
				<strong>첨부파일: </strong>
				<input type="file" name="excelFile"/>
			</form>
		</div>
	</div>
	
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
			<caption>목록</caption>
			<colgroup>
				<col width="100px">
				<col width="150px">
				<col>
				<col width="500px">
				<col width="200px">
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>교원 / 보조인력</th>
					<th>이름</th>
					<th>나이스개인번호(교원) / 생년월일(보조인력)</th>
					<th>등록일</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${!empty autoMemberList}">
						<c:forEach var="autoMember" items="${autoMemberList}" varStatus="status">
							<tr>
								<td>${fn:length(autoMemberList) - status.index}</td>
								<td>
									<c:if test="${'T' eq autoMember.empGubun}">교원</c:if>
									<c:if test="${'E' eq autoMember.empGubun}">보조인력</c:if>
								</td>
								<td>${autoMember.name}</td>
								<td>
									<c:if test="${'T' eq autoMember.empGubun}">${autoMember.nicePersonalNum}</c:if>
									<c:if test="${'E' eq autoMember.empGubun}">${autoMember.birthDate}</c:if>
								</td>
								<td>${autoMember.regDate}</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise><tr><td colspan="5">데이터가 없습니다.</td></tr></c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</c:if>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp"%>

<script type="text/javascript">
function downloadSample(){
	window.location.href = '${pageContext.request.contextPath}/com/fileDownload.do?fileName=autoList.xls';
	return;
}

function insertExcel(){
	if(!isValid()) return;
	if(!confirm('저장하시겠습니까?')) return;
	$('#autoMemberFrm').ajaxSubmit({
		url: "${pageContext.request.contextPath}/adm/prop/insertAutoMember.do"
		, type: 'post'
		, data: {
			subj: '${ses_search_subj}'
			, year: '${ses_search_year}'
			, subjseq: '${ses_search_subjseq}'
		}
		, enctype: "multipart/form-data"
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 1) {
				alert(result.resultMsg);
				location.reload();
			} else {
				alert((Number(result.errorIdx) + 2) + '째 줄: ' + 
						result.errorMember.excelEmpGubun + ', ' + 
						result.errorMember.name + '\n' +
						result.errorMsg);
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);
		}
	});
}

function isValid() {
	$excelFile = $('input[name=excelFile]');
	if($excelFile.val().length > 0) {
		if(!/(\.xls)$/g.test($excelFile.val().toLowerCase())) {
			alert("선택된 파일의 종류는 XLS파일만 가능합니다.");
			return false;
		}
	} else {
		alert("업로드할 파일을 선택하세요.");
		return false;
	}
	return true;
}

function doPageList() {
	$('#${gsMainForm}').attr('action', '/adm/prop/autoMemberList.do');
	$('#${gsMainForm}').submit();
}
</script>