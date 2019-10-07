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
			<h2>용어사전 엑셀업로드</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" enctype="multipart/form-data">
		<input type="hidden" name="p_subj" id="p_subj" value="${search_subj}">
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="150px" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">과정명</th>
						<td class="bold">[${search_subj}] ${search_subjnm}</td>
					</tr>
					<tr>
						<th scope="col">Excel File</th>
						<td class="bold"><input name="p_file" type="FILE" class="input" style="width:98%"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:insertExcel();"><span>저장</span></a></li>
		</ul>
		<!-- // button -->
		
		<table>
			<tr>
				<td style="padding-left:10px;color:#FF7F50"><Strong>( 주의사항 - 필독 )</Strong></td>
			</tr>
			<tr>
				<td style="padding-left:10px;color:#FF7F50">1. 엑셀파일 저장시 엑셀 형식(xls)으로 저장하여 사용할것.</td>
			</tr>
			<tr>
				<td style="padding-left:10px;color:#FF7F50">2. 엑셀파일 작성시 [<font color="blue">용어, 구분, 설명</font>]의 순서로 만들 것.</td>
			</tr>
			<tr>
				<td style="padding-left:10px;color:#FF7F50">
					3. 샘플 파일을 참고할 것.( <a href="#neno" onclick="javascript:downloadSample()"><b><font color="black">샘플문서</font></b></a> )
				</td>
			</tr>
			<tr height="5px;"><td></td></tr>
			<tr>
				<td style="padding-left:10px;">
					<img src="/images/adm/sample/DicSubj.gif" border="0" style="width:99%">
				</td>
			</tr>
		</table>
		
		
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">


	function insertExcel(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.p_file.value.length > 0 ){
			var data = frm.p_file.value;
			data = data.toUpperCase(data);
			if( data.indexOf(".XLS") < 0 ){
				alert("선택된 파일의 종류는 XLS파일만 가능합니다.");
				return;
			}
		}else{
			alert("업로드할 파일을 선택하세요.");
			return;
		}
		
		frm.action = "/adm/cmg/ter/excelUploadInsert.do";
		frm.target = "_self";
		frm.submit();
	}

	function downloadSample(  ){
		var url = "/com/fileDownload.do?fileName=Sample_DicSubj.xls";
		window.location.href = url;
		return;
	}

	
</script>