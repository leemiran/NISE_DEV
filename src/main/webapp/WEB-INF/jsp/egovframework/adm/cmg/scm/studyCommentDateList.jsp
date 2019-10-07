<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" name="p_hidden" id="p_hidden">
	<input type="hidden" name="pageType" id="pageType">
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doSubmit('N')"><span>안보기이 저장</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doSubmit('Y')"><span>보이기 저장</span></a></div>
	</div>
	
	<div class="conwrap2">
		<ul class="mtab2">
			<li><a class="on">등록순</a></li>
			<li><a href="#none" onclick="doPageList('Y')" class="off">과정별</a></li>
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
		if(type == undefined) type = "";
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/scm/studyCommentList.do";
		if( frm.ses_search_subj.value == "" && type != 'Y'){
			alert("과정을 선택하세요.");
			return;
		}
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
		frm.action = "/adm/cmg/scm/studyCommentUpdate.do";
		if( frm.ses_search_subj.value == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		if( <c:out value="${empty list}"/> ){
			alert("조회된 내용이 없습니다.");
			return;
		}
		frm.p_hidden.value = hidden;
		frm.target = "_self";
		frm.submit();
	}
</script>