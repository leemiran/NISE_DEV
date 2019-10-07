<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

	
	
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_userid"  name="p_userid"/>
	<input type="hidden" id="p_gadmin"  name="p_gadmin"/>
	<input type="hidden" name="pageIndex"   id="pageIndex"  value="${pageIndex}"/>
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType" value="M">조회조건타입 : 타입별 세부조회조건</c:param>
		<c:param name="selectParameter" value="">조회조건 추가 : admSearchParameter.jsp 추가</c:param>
	</c:import>
	
</form>
	<!-- // search wrap -->
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="excelDown()"><span>엑셀 다운로드</span></a></div>
	</div>
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="100px" />
				<col />
				<col width="250px"/>
				<col width="250px"/>
				<col width="250px"/>
				<col width="250px"/>
				<col width="100px"/>
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row">ID</th>
					<th scope="row">성명</th>
					<th scope="row">권한</th>
					<th scope="row">권한사용기간</th>
					<th scope="row">권한부여자</th>
					<th scope="row">구분</th>
					<th scope="row">날짜</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td><c:out value="${(pageTotCnt+1)-result.num}"/></td>
					<td><c:out value="${result.userid}"/></td>
					<td><c:out value="${result.name}"/></td>
					<td>
						<a href="#none" onclick="view('<c:out value="${result.userid}"/>', '<c:out value="${result.gadmin}"/>')">
							<c:out value="${result.gadminnm}"/>
						</a>
					</td>
					<td><c:out value="${result.fmon}"/> ~ <c:out value="${result.tmon}"/></td>
					<td><c:out value="${result.lname}"/></td>
					<td><c:out value="${result.logmode}"/></td>
					<td><c:out value="${result.ldate}"/></td>					
				</tr>				
			</c:forEach>
				<c:if test="${empty list}">
				<tr>
					<td colspan="17">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	
		<!-- 페이징 시작 -->
	<div class="paging">
		<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
	</div>
	<!-- 페이징 끝 -->
	
	</div>
	<!-- list table-->
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

/* ********************************************************
 * 페이징처리 함수
 ******************************************************** */
 function doPageList() {
	 var frm = eval('document.<c:out value="${gsMainForm}"/>');
	 frm.action = "/adm/cfg/mng/managerLogList.do";
	 /* 2017 추가 */
	 frm.pageIndex.value = 1;
	 /* 2017 추가 끝 */
	 frm.target = "_self";
	 frm.submit();
}

/* ********************************************************
 * 페이징처리 함수
 ******************************************************** */
function doLinkPage(pageNo) {
	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	frm.action = "/adm/cfg/mng/managerLogList.do";
	frm.pageIndex.value = pageNo;
	frm.target = "_self";
	frm.submit();
}


	function searchList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mng/managerLogList.do";
		frm.submit();
	}
	
	function view(userid, gadmin){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_userid.value = userid;
		frm.p_gadmin.value = gadmin;
		frm.action = "/adm/cfg/mng/managerView.do";
		frm.submit();
	}
	
	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mng/managerInsertPage.do";
		frm.submit();
	}
	
	function excelDown(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mng/managerExcelDown.do";
		frm.submit();
	}

</script>