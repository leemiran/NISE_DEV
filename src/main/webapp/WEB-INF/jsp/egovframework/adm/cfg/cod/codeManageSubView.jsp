<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_gubun" 		name="p_gubun" 		value="<c:out value="${p_gubun}"/>"/>
	<input type="hidden" id="p_code" 		name="p_code" 		value="<c:out value="${p_code}"/>"/>
	<input type="hidden" id="p_levels" 		name="p_levels" 	value="<c:out value="${p_levels}"/>"/>
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>취소</span></a></div>
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
                    <th>대분류코드</th>
                    <td><c:out value="${view.gubun}"/></td>
                </tr>
                <tr class="title">
                    <th>대분류코드명</th>
                    <td><c:out value="${view.gubunnm}"/></td>
                </tr>
                <tr class="title">
                    <th>소분류코드</th>
                    <td><c:out value="${subView.code}"/></td>
                </tr>
                <tr class="title">
                    <th>소분류코드명</th>
                    <td><c:out value="${subView.codenm}"/></td>
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
		frm.action = "/adm/cfg/cod/codeManageSubList.do"
		frm.submit();
	}
	
	function updatePage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/cod/codeManageSubUpdatePage.do"
		frm.submit();
	}
	
	function deleteData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( !confirm("정말 삭제하시겠습니까?") ){
			return;
		}
		frm.action = "/adm/cfg/cod/codeManageSubDelete.do"
		frm.submit();
	}
</script>