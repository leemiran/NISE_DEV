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
	
	
	<!-- 검색박스 시작-->
	<div class="searchWrap txtL">
		<div>		
			<ul class="datewrap">
				<li class="floatL">
					<select name="p_search" id="p_search">
						<option value="adtitle"		<c:if test="${p_search eq 'adtitle'}">selected</c:if>>제목</option>
						<option value="adcontents"	<c:if test="${p_search eq 'adcontents'}">selected</c:if>>내용</option>
						<option value="addate"		<c:if test="${p_search eq 'addate'}">selected</c:if>>작성일자</option>
						<option value="adname"		<c:if test="${p_search eq 'adname'}">selected</c:if>>작성자</option>
						<option value="aduserid"	<c:if test="${p_search eq 'aduserid'}">selected</c:if>>아이디</option>
					</select>
					<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" size="50" onkeypress="fn_keyEvent('doPageList')" style="ime-mode:active;"/>
					<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
				</li>
			</ul>		
		</div>
	</div>
	<!-- 검색박스 끝 -->
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col />
				<col width="80px" />
				<col width="80px" />
				<col width="40px" />
				<col width="40px" />
				<col width="50px" />
				<col width="40px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>제목</th>
					<th>작성자</th>
					<th>등록일</th>
					<th>사용여부</th>
					<th>팝업여부</th>
					<th>로그인여부</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
			
<!--이벤트 : /ico/ico_event.gif-->
<!--축하 : /ico/ico_happy.gif-->
<!--안내 : /ico/ico_guide.gif-->
<!--설문 : /ico/ico_poll.gif -->
<!--긴급 : /ico/ico_busy.gif-->
<!--기타 : /ico/ico_others.gif			-->
			<c:forEach items="${allList}" var="result" varStatus="i">
				<tr>
					<td >*</td>
					<td class="left">
					<c:if test="${result.noticeGubun eq 'A'}"><img src="/images/adm/ico/ico_notice.gif" alt="공지" /></c:if>
					<c:if test="${result.noticeGubun eq 'B'}"><img src="/images/adm/ico/ico_event.gif" alt="이벤트" /></c:if>
					<c:if test="${result.noticeGubun eq 'C'}"><img src="/images/adm/ico/ico_happy.gif" alt="축하" /></c:if>
					<c:if test="${result.noticeGubun eq 'D'}"><img src="/images/adm/ico/ico_guide.gif" alt="안내" /></c:if>
					<c:if test="${result.noticeGubun eq 'E'}"><img src="/images/adm/ico/ico_poll.gif" alt="설문" /></c:if>
					<c:if test="${result.noticeGubun eq 'F'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
					<c:if test="${result.noticeGubun eq 'G'}"><img src="/images/adm/ico/ico_others.gif" alt="기타" /></c:if>
					<a href="#none" onclick="doView('${result.seq}')">
					${result.adtitle}
					</a>
					</td>
					<td >${result.adname}</td>
					<td >${result.addate}</td>
					<td >${result.useyn}</td>
					<td >${result.popup}</td>
					<td >${result.loginyn eq 'Y' ? '로그인후' : result.loginyn eq 'AL' ? '전체' : '로그인전'}</td>
					<td >${result.cnt}</td>
				</tr>
			</c:forEach>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td >${totCnt - result.rn + 1}</td>
					<td class="left">
					<c:if test="${result.noticeGubun eq 'A'}"><img src="/images/adm/ico/ico_notice.gif" alt="공지" /></c:if>
					<c:if test="${result.noticeGubun eq 'B'}"><img src="/images/adm/ico/ico_event.gif" alt="이벤트" /></c:if>
					<c:if test="${result.noticeGubun eq 'C'}"><img src="/images/adm/ico/ico_happy.gif" alt="축하" /></c:if>
					<c:if test="${result.noticeGubun eq 'D'}"><img src="/images/adm/ico/ico_guide.gif" alt="안내" /></c:if>
					<c:if test="${result.noticeGubun eq 'E'}"><img src="/images/adm/ico/ico_poll.gif" alt="설문" /></c:if>
					<c:if test="${result.noticeGubun eq 'F'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
					<c:if test="${result.noticeGubun eq 'G'}"><img src="/images/adm/ico/ico_others.gif" alt="기타" /></c:if>
					<a href="#none" onclick="doView('${result.seq}')">
					${result.adtitle}
					</a>
					</td>
					<td >${result.adname}</td>
					<td >${result.addate}</td>
					<td >${result.useyn}</td>
					<td >${result.popup}</td>
					<td >${result.loginyn eq 'Y' ? '로그인후' : result.loginyn eq 'AL' ? '전체' : '로그인전'}</td>
					<td >${result.cnt}</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="20">조회된 내용이 없습니다.</td>
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

	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/not/selectNoticeList.do";
		frm.target = "_self";
		frm.submit();
	}

	function doLinkPage(index) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/not/selectNoticeList.do";
		frm.pageIndex.value = index;
		frm.target = "_self";
		frm.submit();
	}

	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/hom/not/noticeInsertPage.do";
		frm.target = "_self";
		frm.submit();
	}


	function doView(p_seq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_seq.value = p_seq;
		frm.action = "/adm/hom/not/noticeViewPage.do";
		frm.target = "_self";
		frm.submit();
	}
	
</script>