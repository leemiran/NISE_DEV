<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_menu" 		name="p_menu" 		value="<c:out value="${view.menu}"/>"/>
	<input type="hidden" id="p_view" 		name="p_view" 		value="Y"/>
	<input type="hidden" id="p_levels" 		name="p_levels" 	value="<c:out value="${view.levels}"/>"/>
	<input type="hidden" id="p_parent" 		name="p_parent" 	value="<c:out value="${view.parent}"/>"/>
	<input type="hidden" id="p_upper" 		name="p_upper" 		value="<c:out value="${view.upper}"/>"/>
	<input type="hidden" id="p_oldupper" 	name="p_oldupper" 	value="<c:out value="${view.levels > 1 ? '' : upperInfo.menu}"/>"/>
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>취소</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="update()"><span>수정</span></a></div>
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
            <c:if test="${p_levels > 1}">
                <tr class="title">
                    <th>상위메뉴코드</th>
                    <td><c:out value="${upperInfo.menu}"/></td>
                </tr>
                <tr class="title">
                    <th>상위메뉴코드명</th>
                    <td><c:out value="${upperInfo.menunm}"/></td>
                </tr>
            </c:if>
                <tr class="title">
                    <th>메뉴코드</th>
                    <td>
                    	<c:out value="${view.menu}"/>
                    </td>
                </tr>
                <tr class="title">
                    <th>메뉴코드명</th>
                    <td><input type="text" name="p_menunm"  id="p_menunm" value="<c:out value="${view.menunm}"/>" /></td>
                </tr>
                <tr class="title">
                    <th>관련 Program</th>
                    <td><input type="text" name="p_pgm"  id="p_pgm"  value="<c:out value="${view.pgm}"/>" style="width:95%"/></td>
                </tr>
                <tr class="title">
                    <th>Parameter</th>
                    <td>
                    	<input type="text" name="p_para1"  id="p_para1"  value="<c:out value="${view.para1}"/>"/>&
                    	<input type="text" name="p_para2"  id="p_para2"  value="<c:out value="${view.para2}"/>"/>&
                    	<input type="text" name="p_para3"  id="p_para3"  value="<c:out value="${view.para3}"/>"/>&
                    	<input type="text" name="p_para4"  id="p_para4"  value="<c:out value="${view.para4}"/>"/>&
                    	<input type="text" name="p_para5"  id="p_para5"  value="<c:out value="${view.para5}"/>"/>&
                    	<input type="text" name="p_para6"  id="p_para6"  value="<c:out value="${view.para6}"/>"/>&
                    	<input type="text" name="p_para7"  id="p_para7"  value="<c:out value="${view.para7}"/>"/>&
                    	<input type="text" name="p_para8"  id="p_para8"  value="<c:out value="${view.para8}"/>"/>&
                    	<input type="text" name="p_para9"  id="p_para9"  value="<c:out value="${view.para9}"/>"/>&
                    	<input type="text" name="p_para10" id="p_para10" value="<c:out value="${view.para10}"/>"/>&
                    	<input type="text" name="p_para11" id="p_para11" value="<c:out value="${view.para11}"/>"/>&
                    	<input type="text" name="p_para12" id="p_para12" value="<c:out value="${view.para12}"/>"/>
                    </td>
                </tr>
                <tr class="title">
                    <th>메뉴순서</th>
                    <td><input type="text" name="p_orders" id="p_orders" value="<c:out value="${view.orders}"/>"/></td>
                </tr>
                <tr class="title">
                    <th>사용여부</th>
                    <td>
                    	<input type="radio" id="p_isdisplay" name="p_isdisplay" value="Y" <c:if test="${view.isdisplay == 'Y'}">checked</c:if>/> 사용
                    	<input type="radio" id="p_isdisplay" name="p_isdisplay" value="N" <c:if test="${view.isdisplay == 'N'}">checked</c:if>/> 미사용
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
				<col width="120px" />
				<col width="250px"/>
				<col />
				<col width="60px" />
				<col width="80px" />
			</colgroup>
			<thead>
				<tr>
					<th scope="row">메뉴코드</th>
					<th scope="row">메뉴코드명</th>
					<th scope="row">관련 Program</th>
					<th scope="row">메뉴순서</th>
					<th scope="row">사용여부</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><c:out value="${result.menu}"/></td>
					<td class="left">
						<a href="#none" onclick="view('<c:out value="${result.menu}"/>', '<c:out value="${result.levels}"/>', '<c:out value="${result.parent}"/>', '<c:out value="${result.upper}"/>')">
							<c:out value="${result.menunm}"/>
						</a>
					</td>
					<td class="left">
						<c:out value="${result.pgm}"/>
						<c:out value="${not empty result.para1  ? ''  : ''}"/><c:out value="${result.para1}"/>
						<c:out value="${not empty result.para2  ? '&' : ''}"/><c:out value="${result.para2}"/>
						<c:out value="${not empty result.para3  ? '&' : ''}"/><c:out value="${result.para3}"/>
						<c:out value="${not empty result.para4  ? '&' : ''}"/><c:out value="${result.para4}"/>
						<c:out value="${not empty result.para5  ? '&' : ''}"/><c:out value="${result.para5}"/>
						<c:out value="${not empty result.para6  ? '&' : ''}"/><c:out value="${result.para6}"/>
						<c:out value="${not empty result.para7  ? '&' : ''}"/><c:out value="${result.para7}"/>
						<c:out value="${not empty result.para8  ? '&' : ''}"/><c:out value="${result.para8}"/>
						<c:out value="${not empty result.para9  ? '&' : ''}"/><c:out value="${result.para9}"/>
						<c:out value="${not empty result.para10 ? '&' : ''}"/><c:out value="${result.para10}"/>
						<c:out value="${not empty result.para11 ? '&' : ''}"/><c:out value="${result.para11}"/>
						<c:out value="${not empty result.para12 ? '&' : ''}"/><c:out value="${result.para12}"/>
					</td>
					<td><c:out value="${result.orders}"/></td>
					<td><c:out value="${result.isdisplay == 'Y' ? '사용' : '미사용'}"/></td>
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

	function view(menu, levels, parent, upper){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.p_menu.value = menu;
		frm.p_levels.value = levels;
		frm.p_parent.value = parent;
		frm.p_upper.value = upper;
		frm.action = "/adm/cfg/amm/adminMenuMngView.do";
		frm.submit();
	}

	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/amm/adminMenuMngList.do";
		frm.submit();
	}

	function update(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if(frm.p_menunm.value == "") {
            alert("메뉴명를 입력하세요");
            frm.p_menunm.focus();
            return;
        }
        if(realsize(frm.p_menunm.value) > 40) {
            alert("메뉴명는 한글기준 20자를 초과하지 못합니다.");
            frm.p_menunm.focus();
            return;
        }
        if(!numeric_chk(frm.p_orders)) return;

        frm.action = "/adm/cfg/amm/adminMenuMngUpdate.do";
        frm.submit();
	}
</script>