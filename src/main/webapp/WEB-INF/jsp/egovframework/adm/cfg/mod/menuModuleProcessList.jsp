<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<%@page import="org.hsqldb.Tokenizer"%><script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" name="p_menu"			id="p_menu" value="<c:out value="${p_menu}"/>"/>
	<input type="hidden" name="p_seq"			id="p_seq"  value="<c:out value="${p_seq}"/>"/>
	<input type="hidden" name="p_modulenm"		id="p_modulenm"  value="<c:out value="${p_modulenm}"/>"/>
	<input type="hidden" name="p_process"		id="p_process" />
	<input type="hidden" name="p_systemgubun"	id="p_systemgubun"/>
	
	<!-- search wrap-->
	<div class="searchWrap" style="text-align: left">
		<div >
			메뉴명 : <a href="#none" onclick="move_menu()"><b><c:out value="${menuName}"/></b></a>
			&nbsp;&nbsp;
			모듈명 : <a href="#none" onclick="move_module()"><b><c:out value="${p_modulenm}"/></b></a>
		</div>
	</div>
</form>
	<!-- // search wrap -->
	<div class="listTop">			
		<div class="btnR"><a href="#" class="btn01" onclick="insertData()"><span>등록</span></a></div>
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
				<col width="220px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">메뉴코드</th>
					<th scope="row">번호</th>
					<th scope="row">프로세스</th>
					<th scope="row">서블릿타입</th>
					<th scope="row">함수명</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><c:out value="${p_menu}"/></td>
					<td><c:out value="${p_seq}"/></td>
					<td>
						<a href="#none" onclick="view('<c:out value="${result.process}"/>')">
						<c:out value="${result.process}"/>
						</a>
					</td>
					<td>
						<c:if test="${ result.servlettype == 1 }">조회</c:if>
						<c:if test="${ result.servlettype == 2 }">조회(쓰기액션있음)</c:if>
						<c:if test="${ result.servlettype == 4 }">쓰기액션</c:if>
					</td>
					<td><c:out value="${result.method}"/></td>
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

	function move_module(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mod/menuModuleSubList.do";
		frm.submit();
	}
	
	function insertData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mod/menuModuleProcessInsertPage.do";
		frm.submit();
	}


	function view(process){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_process.value = process;
		frm.action = "/adm/cfg/mod/menuModuleProcessView.do";
		frm.submit();
	}

</script>