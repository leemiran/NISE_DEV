<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<%@page import="egovframework.com.cmm.service.Globals"%>
<%@page import="egovframework.com.cmm.service.EgovProperties"%><script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/lcms/selectbox_move.js"></script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="subj" 		id="subj" 		value="${subj}"/>
	<input type="hidden" name="returnUrl" 	id="returnUrl" 	value="${returnUrl}">
	<input type="hidden" name="pageIndex" 	id="pageIndex" 	value="${pageIndex}">
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doPageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doUpdateData()"><span>저장</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbDetail">
		<table summary="" width="100%" >
			<colgroup>
				<col width="20%" />
				<col width="" />
			</colgroup>
			<tbody>
				<tr>
					<th scope="col">[과정코드] 과정명</th>
					<td class="bold">
						[<c:out value="${data.subj}"/>] ${data.subjnm }
					</td>
				</tr>
				<tr>
					<th scope="col">컨텐츠 위치</th>
					<td class="bold">
						<%=EgovProperties.getProperty("Globals.contentPath")%>${data.subj}
					</td>
				</tr>
				<tr>
					<th scope="col">컨텐츠 유형</th>
					<td class="bold">
						${data.contenttypenm}
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<br/>
	<!-- list table-->
	<div class="tbDetail">
		<table summary="" width="100%" >
			<colgroup>
				<col width="20%" />
				<col width="" />
			</colgroup>
			<tbody>
				<tr>
					<th scope="col">창크기</th>
					<td class="bold">
						Width : <input type="text" size="3" maxlength="4" value="${data.width}" onfocus="this.select()" name="p_width"/>
						Height : <input type="text" size="3" maxlength="4" value="${data.height}" onfocus="this.select()" name="p_height"/>
					</td>
				</tr>
				<tr>
					<th scope="col">메뉴선택</th>
					<td class="bold" valign="center">
						<select style="width:180px;height:225px;" multiple size="15" name="menuList" id="menuList" ondblclick="javascript:addMenu('menuList', 'applyMenuList', true);">
						<c:forEach items="${mfList}" var="result">
							<option value="${result.menu}">
								<c:if test="${result.isrequired eq 'Y'}">[필수]</c:if> ${result.menunm}
							</option>
						</c:forEach>
						</select>
						&nbsp;
						&nbsp;
						<select style="width:180px;height:225px;" multiple size="15" name="applyMenuList" id="applyMenuList" ondblclick="javascript:removeMenu('applyMenuList', 'menuList', true);">
						<c:forEach items="${subList}" var="result">
							<option value="${result.menu}">
								<c:if test="${result.isrequired eq 'Y'}">[필수]</c:if> ${result.menunm}
							</option>
						</c:forEach>
						</select>
						<br/>
						<span style="padding-left:200px;"></span>
						<a href="javascript:moveUp('applyMenuList')"><img src="/images/adm/button/btn_up2.gif" width="18" height="18" alt="" border=0></a> 
						<a href="javascript:moveDown('applyMenuList')"><img src="/images/adm/button/btn_down2.gif" width="18" height="18" alt="" border=0 style='margin-left:2px'></a> 
						<a href="javascript:moveFirstUp('applyMenuList')"><img src="/images/adm/button/btn_up3.gif" width="18" height="18" alt="" border=0 style='margin-left:2px'></a> 
						<a href="javascript:moveLastDown('applyMenuList')"><img src="/images/adm/button/btn_down3.gif" width="18" height="18" alt="" border=0 style='margin-left:2px'></a>
						<a href="javascript:setDefaultMenu();"><img src="/images/adm/button/btn_default_set.gif" border="0"></a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- list table-->
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	//목록으로
	function doPageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var url = frm.returnUrl.value;
		frm.target = "_self";
		frm.action = url;
		frm.submit();
	}

	// 메뉴설정 - 기본구성 설정
	function setDefaultMenu() {
		selectAll('applyMenuList', true);
		removeMenu('applyMenuList','menuList', true);
		setTimeout( "setMenu()", 100);
	}

	function setMenu() {
		// Default Menu 설정
		var menu = ["03","04","07","05","06","09","10","12"];
		for ( var i=menu.length; i>0; i-- ) {
			if ( selectMenuByValue('menuList', menu[i-1]) ) {
				addMenu( 'menuList', 'applyMenuList', true );
			}
		}
	}

	function doUpdateData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		selectAll('applyMenuList', true);
		frm.target = "_self";
		frm.action = "/adm/lcms/com/masterFormUpdate.do";
		frm.submit();
	}
	
</script>