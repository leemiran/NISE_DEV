<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_menu" 		name="p_menu" 		value="<c:out value="${p_menu}"/>"/>
	<input type="hidden" id="p_seq" 		name="p_seq" 		value="<c:out value="${p_seq}"/>"/>
	<input type="hidden" id="p_modulenm" 	name="p_modulenm" 	value="<c:out value="${p_modulenm}"/>"/>
	<input type="hidden" id="p_process" 	name="p_process" 	value="<c:out value="${p_process}"/>"/>
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="updateData()"><span>저장</span></a></div>
	</div>
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="150px" />
                <col width="" />
            </colgroup>
            <tbody>
                <tr class="title">
                    <th>메뉴코드</th>
                    <td>
                    	<c:out value="${p_menu}"/>
                    </td>
                </tr>
                <tr class="title">
                    <th>메뉴코드명</th>
                    <td><c:out value="${menuName}"/></td>
                </tr>
                <tr class="title">
                    <th>모듈번호</th>
                    <td><c:out value="${p_seq}"/></td>
                </tr>
                <tr class="title">
                    <th>모듈명</th>
                    <td><c:out value="${p_modulenm}"/></td>
                </tr>
                <tr class="title">
                    <th>프로세스</th>
                    <td><c:out value="${view.process}"/></td>
                </tr>
                <tr class="title">
                    <th>서블릿타입</th>
                    <td>
                    	<input type="radio" id="p_servlettype" name="p_servlettype" value="1" <c:if test="${ view.servlettype == 1 }">checked</c:if>>조회
                    	<input type="radio" id="p_servlettype" name="p_servlettype" value="2" <c:if test="${ view.servlettype == 2 }">checked</c:if>>조회(쓰기액션있음)
                    	<input type="radio" id="p_servlettype" name="p_servlettype" value="4" <c:if test="${ view.servlettype == 4 }">checked</c:if>>쓰기액션
                    </td>
                </tr>
                <tr class="title">
                    <th>함수명</th>
                    <td>
                    	<input type="text" name="p_method" id="p_method" value="<c:out value="${view.method}"/>" style="width:80%">
                    </td>
                </tr>
            </tbody>
        </table>	
        <br/>
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
    </div>
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mod/menuModuleProcessList.do";
		frm.submit();
	}

	function updateData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
        frm.action = "/adm/cfg/mod/menuModuleProcessUpdate.do";
        frm.submit();
	}

	function view(process){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_process.value = process;
		frm.action = "/adm/cfg/mod/menuModuleProcessView.do";
		frm.submit();
	}
</script>