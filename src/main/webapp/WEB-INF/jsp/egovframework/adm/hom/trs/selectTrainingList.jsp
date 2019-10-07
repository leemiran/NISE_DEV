<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_tabseq"     value = "${p_tabseq}" />
	<input type = "hidden" name="p_seq"     value = "" />
	<input type = "hidden" name="p_type"     value = "${p_type}" />
	
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="70px" />				
				<col width="70px" />
				<col />
				<col width="200px" />
				<col width="100px" />
				<col width="100px" />
				<col width="150px" />
				<col width="150px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>년도</th>
					<th>제목</th>
					<th>등록일</th>
					<th>등록자</th>
					<th>사용여부</th>
					<th>연간연수 일정</th>					
					<th>원격연수 연수과정</th>
				</tr>
			</thead>
			<tbody>
			
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td >${totCnt - result.rn + 1}</td>
					<td class="left">${result.year}</td>
					<td ><a href="#none" onclick="doView('${result.iseq}')">${result.subject}</a></td>					
					<td >${result.ldate}</td>
					<td >${result.name}</td>
					<td >${result.useYn}</td>					
					<td ><a href="#none" onclick="doTrainingSchedule('${result.iseq}')">세부일정</a></td>
					<td ><a href="#none" onclick="doTrainingCourse('${result.iseq}')">연수과정</a></td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="50">조회된 내용이 없습니다.</td>
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

	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	
	function doPageList() {		
		frm.action = "/adm/hom/trs/selectTrainingList.do";
		frm.target = "_self";
		frm.submit();
	}

	function doLinkPage(index) {		
		frm.action = "/adm/hom/trs/selectTrainingList.do";
		frm.pageIndex.value = index;
		frm.target = "_self";
		frm.submit();
	}

	function insertPage(){		
		frm.action = "/adm/hom/trs/trainingInsertPage.do";
		frm.target = "_self";
		frm.submit();
	}

	function doView(p_seq){		
		frm.p_seq.value = p_seq;
		frm.action = "/adm/hom/trs/selectTrainingViewPage.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function doTrainingSchedule(p_seq){
		frm.p_seq.value = p_seq;
		frm.action = "/adm/hom/trs/selectTrainingScheduleList.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function doTrainingCourse(p_seq){
		frm.p_seq.value = p_seq;
		frm.action = "/adm/hom/trs/selectTrainingCourseList.do";
		frm.target = "_self";
		frm.submit();
	}
	
</script>