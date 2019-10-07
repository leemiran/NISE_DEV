<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_userid" 		name="p_userid" 		value="<c:out value="${p_userid}"/>"/>
	<input type="hidden" id="p_gadmin" 		name="p_gadmin" 		value="<c:out value="${p_gadmin}"/>"/>
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="deleteData()"><span>삭제</span></a></div>
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
                    <th>성명</th>
                    <td><c:out value="${view.name}"/></td>
                </tr>
                <tr class="title">
                    <th>권한</th>
                    <td><c:out value="${view.gadminnm}"/></td>
                </tr>
                <tr class="title">
                    <th>교육그룹</th>
                    <td>
                    <c:forEach items="${grcodeList}" var="result" varStatus="i">
                    	<c:out value="${result.grcodenm}"/><c:if test="${not i.last}"><br/></c:if>
                    </c:forEach>
                    </td>
                </tr>
                <tr class="title">
                    <th>과정</th>
                    <td>
                    <c:forEach items="${subjList}" var="result" varStatus="i">
                    	<c:out value="${result.subjnm}"/><c:if test="${not i.last}"><br/></c:if>
                    </c:forEach>
                    </td>
                </tr>
                <tr class="title">
                    <th>회사</th>
                    <td>
                    <c:forEach items="${compList}" var="result" varStatus="i">
                    	<c:out value="${result.companynm}"/><c:if test="${not i.last}"><br/></c:if>
                    </c:forEach>
                    </td>
                </tr>
                <tr class="title">
                    <th>권한사용기간</th>
                    <td><c:out value="${view.fmon}"/> ~ <c:out value="${view.tmon}"/></td>
                </tr>
                <tr class="title">
                    <th>권한사용용도</th>
                    <td><c:out value="${view.commented}"/></td>
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
		frm.action = "/adm/cfg/mng/managerList.do";
		frm.submit();
	}

	function deleteData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if (frm.p_gadmin.value=="P1") {
            alert("강사관리에서 삭제해주십시요");
			 return;
		}
		if( !confirm("정말 삭제하시겠습니까?") ){
			return;
		}
		frm.action = "/adm/cfg/mng/managerDelete.do";
		frm.submit();
	}

	function updatePage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if (frm.p_gadmin.value=="P1") {
            alert("강사관리에서 수정해주십시요");
			 return;
		}
        frm.action = "/adm/cfg/mng/managerUpdatePage.do";
        frm.submit();
	}
</script>