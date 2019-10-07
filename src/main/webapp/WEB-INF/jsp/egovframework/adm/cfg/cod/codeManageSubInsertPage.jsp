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
	<input type="hidden" id="p_upper" 		name="p_upper" 		value="<c:out value="${p_upper}"/>"/>
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>취소</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertData()"><span>저장</span></a></div>
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
                    <td>* 시스템 자동부여</td>
                </tr>
                <tr class="title">
                    <th>소분류코드명</th>
                    <td><input type="text" id="p_codenm" name="p_codenm" style="width:90%;ime-mode:active" onfocus="this.select"></td>
                </tr>
            </tbody>
        </table>				
    </div>
    <!-- // detail wrap -->
    <br/>
    <!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="200px"/>
				<col />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">소분류코드</th>
					<th scope="row">소분류코드명</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><c:out value="${result.code}"/></td>
					<td class="left">
						<c:out value="${result.codenm}"/>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/cod/codeManageSubList.do"
		frm.submit();
	}
	
	function insertData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( frm.p_codenm.value == "" ){
			alert("소분류코드명을 입력하세요.");
			frm.p_codenm.focus();
			return;
		}
		frm.action = "/adm/cfg/cod/codeManageSubInsert.do"
		frm.submit();
	}
	
</script>