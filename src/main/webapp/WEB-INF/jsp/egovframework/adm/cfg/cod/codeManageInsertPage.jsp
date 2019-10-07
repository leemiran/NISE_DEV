<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
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
                    <td>* 시스템 자동부여</td>
                </tr>
                <tr class="title">
                    <th>대분류코드명</th>
                    <td>
                    	<input type="hidden" name="p_maxlevel" value="1">
                    	<input type="text" id="p_gubunnm" name="p_gubunnm" style="width:90%;ime-mode:active;" onfocus="this.select">
                    </td>
                </tr>
                <tr class="title">
                    <th>코드자동등록여부</th>
                    <td>
                    	<select id="p_issystem" name="p_issystem">
                    		<option value="N" >자동등록</option>
                    		<option value="Y" >수동등록</option>
                    	</select>
                    </td>
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
				<col width="150px"/>
				<col />
				<col width="120px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">대분류코드</th>
					<th scope="row">대분류코드명</th>
					<th scope="row">코드자동등록여부</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><c:out value="${result.gubun}"/></td>
					<td class="left">
						<c:out value="${result.gubunnm}"/>
					</td>
					<td><c:out value="${result.issystem == 'N' ? '자동' : '수동'}"/>등록</td>
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
		frm.action = "/adm/cfg/cod/codeManageList.do"
		frm.submit();
	}
	
	function insertData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( frm.p_gubunnm.value == "" ){
			alert("대분류코드명을 입력하세요.");
			frm.p_gubunnm.focus();
			return;
		}
		frm.action = "/adm/cfg/cod/codeManageInsert.do"
		frm.submit();
	}
	
</script>