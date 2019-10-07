<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" enctype="multipart/form-data">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	
	<!-- contents -->
	<div class="tbDetail">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="150px" />
				<col width="" />
			</colgroup>
			<tbody>
				<tr>
					<th scope="col">Excel File</th>
					<td class="bold"><input name="p_file" type="FILE" class="input" style="width:98%"></td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- // contents -->
	<!-- button -->
	<ul class="btnCen">
		<li><a href="#" class="pop_btn01" onclick="insertExcel();"><span>저장</span></a></li>
	</ul>
	<!-- // button -->
	
	<!-- s : 주의사항  -->
	<table cellspacing="0" cellpadding="0">
		<tr> 
			<td><B>(주의사항)</B></td>
		</tr>
		<tr> 
			<td height="8"></td>
		</tr>
		<tr> 
			<td>1. 엑셀파일 저장시 엑셀 형식 <font color="red">[Excel 97-2003통합문서 (*.xls)]</font>으로 저장하여 사용할 것</td>
		</tr>
		<tr> 
			<td>2. 엑셀파일 작성시 [<b><font color="#003AEA">수험번호, 점수</font></b>]의 순서로 만들 것.</td>
		</tr>
		<tr> 
			<td>3. <font color="red">샘플 양식 변경시 오류가 발생할 수 있습니다.</font></td>
		</tr>
		<tr>
			<td>4. 점수는 가중치를 적용하지 않은 100점에 대한 점수를 입력합니다.</td>
		</tr>
		<tr>
			<td>5. 점수가 없는 경우 0점으로 입력하여야 합니다.</td>
		</tr>
	</table>R
	<!-- e : 주의사항 -->
	<!--  샘플 파일 예시  -->
	<table width="97%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><img src="/images/adm/sample/Offsubj.jpg" border="0" galleryimg="no"></td>
		</tr>
	</table>      
   	<!--  샘플 파일 예시 -->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	/* ********************************************************
	 * 등록
	 ******************************************************** */
	function insertExcel() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
  
  		if (frm.ses_search_subj.value == '----') {
    		alert("과정을 선택하세요.");
    		return;
  		}
  
		if(frm.p_file.value == ""){
			alert("DB로 가져갈 파일을 선택하세요.");
		  	return;
		}
		
		// 파일 확장자가 xls인지 체크
		if (frm.p_file.value.length > 0 ){
			var data = frm.p_file.value;
		  	data = data.toUpperCase(data);
		  	if (data.indexOf(".XLS") < 0) {
		    	alert("DB로 입력되는 파일종류는 xls파일만 가능합니다.");
		    	return;
			}
		}
		
		frm.action = "/adm/fin/finishExcelInsert.do";
		frm.target = "_self";
		frm.submit();
	}

	function doPageList() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/fin/finishInputPage.do";
		frm.target = "_self";
		frm.submit();
	}


	
</script>