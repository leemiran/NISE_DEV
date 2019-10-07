<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" name="p_hidden" 	id="p_hidden">
	<input type="hidden" name="pageType" 	id="pageType">
	<input type="hidden" name="p_subj" 		id="p_subj">
	<input type="hidden" name="p_year" 		id="p_year">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq">
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	
	<div class="conwrap2">
		<ul class="mtab2">
			<li><a href="#none" onclick="doPageList('')" class="off">등록순</a></li>
			<li><a class="on">과정별</a></li>
		</ul>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="70px" />
				<col />
				<col width="120px" />
				<col width="180px" />
				<col width="70px" />
				<col width="120px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>과정명</th>
					<th>과정기수</th>
					<th>교육구분</th>
					<th>게시물수</th>
					<th>교육후기 list 조회</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>${i.count}</td>
					<td class="left">${result.subjnm}</td>
					<td>${fn2:toNumber(result.subjseq)}</td>
					<td>${result.isonoff eq 'ON' ? 'e-러닝' : result.isonoff eq 'OFF' ? '집합' : '독서교육'}</td>
					<td>${result.cnt}</td>
					<td><a href="#" class="btn01" onclick="parent.doCommentList('${result.subj}', '${result.year}', '${result.subjseq}')"><span>리스트 보기</span></a></td>
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
	function doPageList(type) {
		if(type == undefined) type = "Y";
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/scm/studyCommentList.do";
		if( frm.ses_search_subj.value == "" && type == 'Y' ){
			alert("과정을 선택하세요.");
			return;
		}
		frm.pageType.value = type;
		frm.target = "_self";
		frm.submit();
	}

	
	function doCommentList( subj, year, subjseq ){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/scm/studyCommentSubList.do";
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.target = "_self";
		frm.submit();
	}
</script>