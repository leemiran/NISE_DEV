<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.doPageList();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>과제 채점정보 Excel반영</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" enctype="multipart/form-data">
		<input type="hidden" name="p_subj" 		id="p_subj"		value="${p_subj}">
		<input type="hidden" name="p_year" 		id="p_year"		value="${p_year}">
		<input type="hidden" name="p_subjseq" 	id="p_subjseq"	value="${p_subjseq}">
		<input type="hidden" name="p_ordseq" 	id="p_ordseq"	value="${p_ordseq}">
		<!-- contents -->
		<div class="popCon">
			<ul class="topMent">
		      <li><span class="strBlue">* 샘플양식을 다운받은후에 양식에 맞게 작성해 주시기 바랍니다.</span></li> <%//반영하시는 Excel파일은 포맷이 일치해야 합니다.%>
		      <li><span class="strBlue">* 다운받은 엑셀파일의 포맷을 변경하시면 심각한 에러가 발생할수 있습니다.</span></li> <%//반영하시는 Excel파일은 포맷이 일치해야 합니다.%>
		      <li><span class="strBlue">* 다운받은 엑셀파일의 평가점수를 수정하여 저장하면 감점이 자동반영되어 취득점수가 계산됩니다.</span></li> <%//오른쪽 상단의 학생목록을 다운받아 채점하시기 바랍니다.%>
		      <li><span class="strBlue">* 미제출자는 엑셀파일에 정보가 존재하여도, 점수가 반영되지 않습니다.</span></li> <%//오른쪽 상단의 학생목록을 다운받아 채점하시기 바랍니다.%>
		    </ul>
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="20%" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">과제명</th>
						<td class="bold">${view.title}</td>
					</tr>
					<tr>
						<th scope="col">반영할 파일</th>
						<td class="bold">
							<input type="file" style="width:95%" name="p_file"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doUpload()"><span>저장</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function doUpload(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.p_file.value.length > 0 ){
			var data = frm.p_file.value;
			data = data.toUpperCase(data);
			if( data.indexOf(".XLS") < 0 ){
				alert("선택된 파일의 종류는 XLS파일만 가능합니다.");
				return;
			}
		}else{
			alert("반영할 Excel 파일을 선택하세요.");
			return;
		}
		if(!confirm("작성하신 채점정보가 현재 과제정보와 일치하는지 확인하셨습니까?")) { // 작성하신 채점정보가 현재 과제정보와 일치하는지 확인하셨습니까?
	    	return false;
	    }
		frm.target = "_self";
		frm.action = "/adm/rep/reportResultExcelInsertData.do";
		frm.submit();
	}
</script>