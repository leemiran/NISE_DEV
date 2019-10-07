<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj">
	<input type="hidden" name="p_year" 		id="p_year">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq">
	<input type="hidden" name="p_subjnm" 	id="p_subjnm">
	<input type="hidden" name="p_tabseq" 	id="p_tabseq">
	<input type="hidden" name="p_action" 	id="p_action">
	
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="50px" />
				<col width="200px" />
				<col />
				<col width="60px" />
				<col width="100px" />
				<col width="180px" />
				<col width="60px" />
				<col width="120px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>패키지</th>
					<th>과정명</th>
					<th>기수</th>
					<th>교육구분</th>
					<th>교육기간</th>
					<th>질문수</th>
					<th>질문 list조회</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>${i.count}</td>
					<td class="left" colspan="2">${result.subjnm}</td>
					<td>${fn2:toNumber(result.subjseq)}</td>
					<td>
						<c:if test="${result.isonoff eq 'ON'}">e-러닝</c:if>
						<c:if test="${result.isonoff eq 'OFF'}">집합</c:if>
						<c:if test="${result.isonoff ne 'ON' and result.isonoff ne 'OFF'}">독서교육</c:if>
					</td>
					<td>${result.edustart} ~ ${result.eduend}</td>
					<td>${result.cnt}</td>
					<td>
						<a href="#none" class="btn01" onclick="doDataBoardList('${result.subj}', '${result.year}', '${result.subjseq}', '${result.subjnm}', '${result.tabseq}')"><span>질문리스트</span></a>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="8">조회된 내용이 없습니다.</td>
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
		frm.action = "/adm/cmg/sab/studyAdminList.do";
		if( frm.ses_search_gyear.value == "" ){
			alert("연도를 선택하세요.");
			return;
		}
		frm.target = "_self";
		frm.submit();
	}

	function doDataBoardList(subj, year, subjseq, subjnm, tabseq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/sab/studyAdminBoardList.do";
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.p_subjnm.value = subjnm;
		frm.p_tabseq.value = tabseq;
		frm.target = "_self";
		frm.submit();
	}
	
</script>