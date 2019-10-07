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
		<div class="btnR"><a href="#" class="btn01" onclick="updatePage()"><span>수정</span></a></div>
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
                    	<c:if test="${ view.servlettype == 1 }">조회</c:if>
						<c:if test="${ view.servlettype == 2 }">조회(쓰기액션있음)</c:if>
						<c:if test="${ view.servlettype == 4 }">쓰기액션</c:if>
                    </td>
                </tr>
                <tr class="title">
                    <th>함수명</th>
                    <td><c:out value="${view.method}"/></td>
                </tr>
            </tbody>
        </table>				
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

	function updatePage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
        frm.action = "/adm/cfg/mod/menuModuleProcessUpdatePage.do";
        frm.submit();
	}
</script>