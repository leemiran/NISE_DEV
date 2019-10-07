<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_seq" 		id="p_seq">
	<input type="hidden" name="p_subj" 		id="p_subj">
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="A"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value="DIC"							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01_off" onclick="excelUpload()"><span>엑셀업로드</span></a></div>
		<div class="btnR"><a href="#" class="btn01_off" onclick="insertPage()"><span>등록</span></a></div>
		<div class="btnR"><a href="#" class="btn01_off" onclick="doPreview()"><span>미리보기</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="50px" />
				<col width="80px" />
				<col />
				<col width="200px" />
				<col width="120px" />
				<col width="180px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>분류</th>
					<th>용어</th>
					<th>작성자</th>
					<th>등록일자</th>
					<th>기능</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>${i.count}</td>
					<td>${result.groups}</td>
					<td>${result.words}</td>
					<td>
						<c:if test="${result.gadmin eq 'P'}">[강사]<br/></c:if>
						<c:if test="${result.gadmin ne 'P' and result.gadmin ne ''}">[운영자]<br/></c:if>
						${result.name}(${result.luserid})
					</td>
					<td>${result.ldate}</td>
					<td>
						<a href="#" class="btn01" onclick="updatePage('${result.subj}', '${result.seq}')"><span>수정</span></a>
						<a href="#" class="btn01" onclick="deleteData('${result.subj}', '${result.seq}')"><span>삭제</span></a>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="6">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/ter/termDictionaryList.do";
		if( frm.ses_search_subj.value == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		frm.target = "_self";
		frm.submit();
	}

	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/ter/termDictionaryInsertPage.do";
		if( frm.ses_search_subj.value == "" ){
		 alert("과정을 선택하세요.");
		 return;
		}
		frm.target = "_self";
		frm.submit();
	}

	function updatePage(subj, seq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/ter/termDictionaryUpdatePage.do";
		frm.p_seq.value = seq;
		frm.p_subj.value = subj;
		frm.target = "_self";
		frm.submit();
	}

	function deleteData(subj, seq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/ter/termDictionaryDelete.do";
		if( !confirm("정말 삭제 하시겠습니까?")){
			return;
		}
		frm.p_seq.value = seq;
		frm.p_subj.value = subj;
		frm.target = "_self";
		frm.submit();
	}

	function excelUpload(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'uploadPop', 'width=750,height=520');
		frm.action = "/adm/cmg/ter/excelUploadPopup.do";
		frm.target = "uploadPop";
		frm.submit();
	}

	function doPreview(){
		alert("디자인 미적용...");
	}
	
</script>