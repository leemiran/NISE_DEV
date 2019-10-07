<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" name="p_menu"			id="p_menu" 		value="<c:out value="${p_menu}"/>"/>
	<input type="hidden" name="p_seq"			id="p_seq"  		value="<c:out value="${p_seq}"/>"/>
	<input type="hidden" name="p_modulenm"		id="p_modulenm"  />
	<input type="hidden" name="p_systemgubun"	id="p_systemgubun"/>
	
	<!-- search wrap-->
	<div class="searchWrap" style="text-align: left">
		<div >
			메뉴명 : <a href="#none" onclick="move_menu()"><b><c:out value="${menuName}"/></b></a>
		</div>
	</div>
</form>
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertPage()"><span>등록</span></a></div>
	</div>
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="200px" />
				<col width="80px"/>
				<col width="250px"/>
				<col />
				<col width="120px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">메뉴코드</th>
					<th scope="row">번호</th>
					<th scope="row">서블릿</th>
					<th scope="row">모듈이름</th>
					<th scope="row">프로세스리스트</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><c:out value="${result.menu}"/></td>
					<td><c:out value="${result.seq}"/></td>
					<td><c:out value="${result.servlet}"/></td>
					<td><a href="#none" onclick="view('<c:out value="${result.seq}"/>')"><c:out value="${result.modulenm}"/></a></td>
					<td>
						<c:if test="${result.seq ne 0}">
						<a href="#none" onclick="move_process('<c:out value="${result.seq}"/>', '<c:out value="${result.modulenm}"/>')">프로세스리스트</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function move_menu(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mod/menuModuleList.do";
		frm.submit();
	}

	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mod/menuModuleList.do";
		frm.submit();
	}

	function insertPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mod/menuModuleInsertPage.do";
		frm.submit();
	}


	function move_process(seq, modulenm){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_seq.value = seq;
		frm.p_modulenm.value = modulenm;
		frm.action = "/adm/cfg/mod/menuModuleProcessList.do";
		frm.submit();
	}

	function view(seq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_seq.value = seq;
		frm.action = "/adm/cfg/mod/menuModuleView.do";
		frm.submit();
	}

</script>