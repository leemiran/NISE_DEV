<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<base target="_self">
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	parent.pageReload();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈  width=580,height=190-->
<div id="popwrapper">
	<div class="popIn" style="height:410px">
    	<div class="tit_bg">
			<h2>과정분류 등록</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="120px" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col" rowspan="2">등록 코드타입</th>
						<td class="bold">
							<input type="radio" name="p_classtype" value="upper" 	<c:if test="${p_classtype eq 'upper'}">checked</c:if> onClick="viewSubjectClass('upperclasslay', 'middleclasslay', 'upper')">
							대분류코드
							<input type="radio" name="p_classtype" value="middle" 	<c:if test="${p_classtype eq 'middle'}">checked</c:if> onClick="viewSubjectClass('upperclasslay', 'middleclasslay', 'middle')">
							중분류코드
							<input type="radio" name="p_classtype" value="lower" 	<c:if test="${p_classtype eq 'lower'}">checked</c:if> onClick="viewSubjectClass('upperclasslay', 'middleclasslay', 'lower')">
							소분류코드
						</td>
					</tr>
					<tr>
						<td class="bold">
							
							<span id="upperclasslay" style="visibility:<c:out value="${p_classtype eq 'upper' ? 'hidden' : 'visible'}"/>">
								* 대분류
								<select id="p_upperclass" name="p_upperclass" onchange="javascript:changeClass()">
								<c:forEach items="${upperList}" var="result">
									<option value="<c:out value="${result.code}"/>" <c:if test="${p_upperclass eq result.code}">selected</c:if>><c:out value="${result.codeName}"/></option>
								</c:forEach>
								</select>
							</span> 
							<span id="middleclasslay" style="visibility:<c:out value="${p_classtype eq 'lower' ? 'visible' : 'hidden'}"/>;">
								* 중분류
								<select id="p_middleclass" name="p_middleclass" onchange="javascript:changeClass()">
								<c:forEach items="${middleList}" var="result">
									<option value="<c:out value="${result.code}"/>" <c:if test="${p_middleclass eq result.code}">selected</c:if>><c:out value="${result.codeName}"/></option>
								</c:forEach>
								</select>
							</span>
						</td>
					</tr>
					<tr>
						<th scope="col">분류코드</th>
						<td class="bold">
							<c:out value="${p_classcode}"/>
							<input type="hidden" name="p_classcode" id="p_classcode" value="<c:out value="${p_classcode}"/>">
						</td>
					</tr>
					<tr>
						<th scope="col">분류코드명</th>
						<td class="bold"><input type="text" id="p_classname" name="p_classname" style="width:90%;ime-mode:active" onfocus="this.select"></td>
					</tr>
				</tbody>
			</table>
			<ul class="btnCen">
				<li><a href="#" class="pop_btn01" onclick="javascript:insertData()"><span>저장</span></a></li>
				<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
			</ul>
			<b>[분류코드 생성방법]</b><br/>
			<table width="100%">
				<tr>
					<td width="5px;"></td>
					<td width="70px;">대분류 </td>
					<td>분류코드 길이는 총 3자리로 영문 + 숫자의 조합으로 구성(자동세팅).</td>
				</tr>
				<tr>
					<td width="5px;"></td>
					<td width="70px;">중분류 </td>
					<td>분류코드 길이는 총 3자리로 영문 + 숫자의 조합으로 구성(자동세팅).</td>
				</tr>
				<tr>
					<td width="5px;"></td>
					<td width="70px;">소분류 </td>
					<td>분류코드 길이는 총 3자리로 영문 + 숫자의 조합으로 구성(자동세팅).</td>
				</tr>
			</table>
			<br/>
			<b>예)</b><br/>
			<table width="100%">
				<tr>
					<td width="5px;"></td>
					<td width="70px;">대분류 </td>
					<td>A01, A02, A03....</td>
				</tr>
				<tr>
					<td width="5px;"></td>
					<td width="70px;">중분류 </td>
					<td>B01, B02, B03....</td>
				</tr>
				<tr>
					<td width="5px;"></td>
					<td width="70px;">소분류 </td>
					<td>C01, C02, C03....</td>
				</tr>
			</table>
			
		</div>
		</form>
		<!-- // contents -->
		
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">
	function insertData(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.p_classname.value == "" ){
			alert("분류코드명을 입력하세요.");
			frm.p_classname.focus();
			return;
		}
		frm.action = "/adm/cfg/ccm/courseClassificationInsert.do";
		frm.submit();
	}

	function viewSubjectClass(p_upperlay, p_middlelay, p_type){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if (p_type == "upper") {
			document.getElementById(p_upperlay).style.visibility = "hidden";
			document.getElementById(p_middlelay).style.visibility = "hidden";
		} else if (p_type == "middle") {
			document.getElementById(p_upperlay).style.visibility = "visible";
			document.getElementById(p_middlelay).style.visibility = "hidden";
		} else if (p_type == "lower") {
			document.getElementById(p_upperlay).style.visibility = "visible";
			document.getElementById(p_middlelay).style.visibility = "visible";
		}
		this.changeClass();
	}

	function changeClass(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.action = "/adm/cfg/ccm/courseClassificationInsertPage.do";
		frm.submit();
	}
</script>