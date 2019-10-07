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
	<div class="searchWrap">
		<div class="in">
			<select id="p_search" name="p_search">				
				<option value="userid" <c:if test="${p_search == 'userid'}">selected</c:if>>아이디</option>
				<option value="name"   <c:if test="${p_search == 'name'}">selected</c:if>>성명</option>
			</select>
			<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}"/>
			<a href="#none" class="btn_search" onclick="searchList()"><span>검색</span></a>
		</div>
	</div>
</form>
	<!-- // search wrap -->
	<div class="listTop">		
		<select id="ss_gadmin" name="ss_gadmin">
			<option value="ALL">==전 체==</option>
		<c:forEach items="${authList}" var="result">
			<option value="<c:out value="${result.gadmin}"/>"   <c:if test="${ss_gadmin == result.gadmin}">selected</c:if>><c:out value="${result.gadminnm}"/></option>
		</c:forEach>
		</select>	
		<div class="btnR"><a href="#" class="btn01" onclick="goLog()"><span>로그</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
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
					<th scope="row">날짜</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td><c:out value="${i.count}"/></td>
					<td><c:out value="${result.userid}"/></td>
					<td><c:out value="${result.name}"/></td>
					<td>
						<a href="#none" onclick="view('<c:out value="${result.userid}"/>', '<c:out value="${result.gadmin}"/>')">
							<c:out value="${result.gadminnm}"/>
						</a>
					</td>
					<td><c:out value="${result.fmon}"/> ~ <c:out value="${result.tmon}"/></td>
					<td><c:out value="${result.lname}"/></td>
					<td><c:out value="${result.ldate}"/></td>					
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">



	function searchList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mng/managerList.do";
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
	
	function goLog(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mng/managerLogList.do";
		frm.submit();
	}

</script>