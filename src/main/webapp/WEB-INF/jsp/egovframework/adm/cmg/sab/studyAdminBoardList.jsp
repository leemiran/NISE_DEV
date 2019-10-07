<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
	<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
	<input type="hidden" name="p_subjnm" 	id="p_subjnm"	value="${p_subjnm}">
	<input type="hidden" name="p_tabseq" 	id="p_tabseq"	value="${p_tabseq}">
	<input type="hidden" name="p_seq" 		id="p_seq"		>
	<input type="hidden" name="p_upfilecnt" id="p_upfilecnt">
	<input type="hidden" name="p_userid" 	id="p_userid"	>
	<input type="hidden" name="p_action" 	id="p_action"	>
	<input type="hidden" name="pageIndex" 	id="pageIndex"	value="${pageIndex}">
	
	
	<!-- 검색박스 시작-->
	<div class="searchWrap">
		<div class="in" style="width:100%">
			<select name="p_search" id="p_search">
				<option value="title"	<c:if test="${p_search eq 'title'}"		>selected</c:if>>제목</option>
				<option value="content"	<c:if test="${p_search eq 'content'}"	>selected</c:if>>내용</option>
				<option value="name"	<c:if test="${p_search eq 'name'}"		>selected</c:if>>작성자</option>
				<option value="userid"	<c:if test="${p_search eq 'userid'}"	>selected</c:if>>아이디</option>
				<option value="ldate"	<c:if test="${p_search eq 'ldate'}"		>selected</c:if>>작성일자</option>
			</select>
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" style="ime-mode:active" onkeypress="javascript:fn_keyEvent('doPageList')"/>
			<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
		</div>
	</div>
	<!-- 검색박스 끝 -->
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="parentListPage()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
		<b>${p_subjnm} - ${fn2:toNumber(p_subjseq)}기</b>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="50px" />
				<col />
				<col width="100px" />
				<col width="100px" />
				<col width="120px" />
				<col width="180px" />
				<col width="80px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>제목</th>
					<th>공개여부</th>
					<th>첨부파일</th>
					<th>등록일자</th>
					<th>작성자</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td>${i.count}</td>
					<td class="left">
						<c:if test="${result.levels > 1}">
						<img src = "/images/user/k/space.gif" width="${result.levels*15-30}" height ="10" border =0>
						<img src="/images/user/k/ico_re.gif" border="0">
						</c:if>
						<a href="#none" onclick="doView('${result.seq}', '${result.upfilecnt}', '${result.userid}')">${result.title}</a>
					</td>
					<td class="left">${result.isopen eq 'Y' ? '공개' : '비공개'}</td>
					<td>
						<c:if test="${result.upfilecnt > 0 }">
						<img src="/images/adm/ico/ico_file01.gif" border="0">
						</c:if>
					</td>
					<td>${result.indate}</td>
					<td>
						<c:if test="${result.gadmin ne 'ZZ'}">[${result.gadmin}]<br/></c:if>
						<a href="#none" onclick="whenMemberInfo('${result.userid}')">${result.name}(${result.userid})</a>
					</td>
					<td>${result.cnt}</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="7">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- 페이징 시작 -->
	<div class="paging">
		<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
	</div>
	<!-- 페이징 끝 -->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function parentListPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/sab/studyAdminList.do";
		frm.p_action.value = "go";
		frm.target = "_self";
		frm.submit();
	}
	
	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/sab/studyAdminBoardList.do";
		frm.target = "_self";
		frm.submit();
	}
	
	function doLinkPage(idx) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/sab/studyAdminBoardList.do";
		frm.pageIndex.value = idx;
		frm.target = "_self";
		frm.submit();
	}
	
	function doView(seq, upfilecnt, userid) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/sab/studyAdminBoardView.do";
		frm.p_seq.value = seq;
		frm.p_upfilecnt.value = upfilecnt;
		frm.p_userid.value = userid;
		frm.target = "_self";
		frm.submit();
	}

	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cmg/sab/studyAdminBoardInsertPage.do";
		frm.target = "_self";
		frm.submit();
	}

	function whenMemberInfo( p_userid ){
		alert("학습현황 > 종합학습현황 메뉴 참조 \n 작업중...");
	}

	
	
</script>