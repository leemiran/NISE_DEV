<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" name="p_hidden" 	id="p_hidden"	value="${p_hidden}">
	<input type="hidden" name="pageType" 	id="pageType"	value="${pageType}">
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doSubmit('N')"><span>안보기이 저장</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doSubmit('Y')"><span>보이기 저장</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doPageList('Y')"><span>목록</span></a></div>
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
				<col width="100px" />
				<col width="30px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>교육후기</th>
					<th>등록일자</th>
					<th>작성자</th>
					<th>보여주기여부</th>
					<th><input type="checkbox" id="allCheck" name="allCheck" onclick="allChecked()"></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>${result.seq}</td>
					<td class="left">${result.comments}</td>
					<td>${result.ldate}</td>
					<td>${result.name}[${result.userid}]</td>
					<td>${result.hiddenYn eq 'Y' ? '보여줌' : '안보여줌'}</td>
					<td><input type="checkbox" name="p_chk" value="${result.seq},${result.subj},${result.year},${result.subjseq},${result.userid}"></td>
					
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
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/scm/studyCommentList.do";
		frm.pageType.value = type;
		frm.target = "_self";
		frm.submit();
	}

	//전체선택
	function allChecked(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var chkBox = frm.p_chk;
		if( chkBox ){
			if( chkBox.length ){
				for( i=0; i<chkBox.length; i++ ){
					chkBox[i].checked = frm.allCheck.checked;
				}
			}else{
				chkBox.checked = frm.allCheck.checked;
			}
		}
	}
	
	function doSubmit(hidden) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/scm/studyCommentSubUpdate.do";
		if( <c:out value="${empty list}"/> ){
			alert("조회된 내용이 없습니다.");
			return;
		}
		frm.p_hidden.value = hidden;
		frm.target = "_self";
		frm.submit();
	}
</script>