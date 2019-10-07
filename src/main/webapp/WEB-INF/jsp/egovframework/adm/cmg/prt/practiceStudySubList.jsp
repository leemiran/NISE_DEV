<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" id="p_subj" name="p_subj" value="${p_subj}">
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01_off" onclick="parentListPage()"><span>목록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="80px" />
				<col width="150px" />
				<col />
				<col width="250px" />
				<col width="250px" />
			</colgroup>
			<thead>
				<tr>
					<th>NO</th>
					<th>과정기수</th>
					<th>교육기간</th>
					<th>현재상태</th>
					<th>학습화면으로</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td >${i.count}</td>
					<td >${fn2:toNumber(result.subjseq)}</td>
					<td >
						${result.edustart} ~ ${result.eduend}
					</td>
					<td >${result.status}</td>
					<td >
						<a href="#none" class="btn01" onclick="doPracticeStudy('${result.subj}', '${result.year}', '${result.subjseq}', '${result.contenttype}', '${result.studyType}')"><span>모의학습</span></a>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="2">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	
</form>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
<input type="hidden" name="preview"/>
<input type="hidden" name="p_review_study_yn"/>
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	function parentListPage() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/prt/practiceStudyList.do";
		frm.target = "_self";
		frm.submit();
	}


	
	function doPracticeStudy(subj, year, subjseq, contenttype, studytype){
		var frm = eval('document.studyForm');
		frm.p_contenttype.value = contenttype;
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.preview.value = "Y";
		frm.p_review_study_yn.value = "Y";

		var width  	= 790;
       	var height 	= 740;
       	var target  = "newStudy";
       	
		var options = "toolbar=0,location=0,directories=0,status=no,menubar=0,scrollbars=yes,resizable=no,top=30,left=30,copyhistory=0,width=" + width + ",height=" + height + "";
        window.open('', target, options);

		//frm.action = "/usr/stu/std/userStudyHome.do";
		frm.action = "/usr/stu/std/userStudyItemList.do";		
		frm.target = target;
		frm.submit();
	}
	
</script>