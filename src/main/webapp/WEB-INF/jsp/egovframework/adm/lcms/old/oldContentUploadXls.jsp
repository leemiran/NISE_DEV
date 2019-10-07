<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.doPageReload();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:165px">
    	<div class="tit_bg">
			<h2>
			엑셀파일업로드 -
			<c:if test="${mobile_yn eq 'Y'}">모바일동영상용</c:if>
			<c:if test="${mobile_yn ne 'Y'}">웹컨텐츠용</c:if>
			</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" enctype="multipart/form-data">
		<input type="hidden" name="subj" value="${subj}">
		<input type="hidden" name="mobile_yn" value="${mobile_yn}">
		
		<div class="list">	
			<c:if test="${mobile_yn eq 'Y'}">
			<div class="btnR"><a href="#" class="btn01" onclick="javascript:doDownloadSample('Mobile_Sample.xls')"><span>모바일동영상용엑셀샘플</span></a></div>
			</c:if>
			<c:if test="${mobile_yn ne 'Y'}">
			<div class="btnR"><a href="#" class="btn01" onclick="javascript:doDownloadSample('CA_Sample.xls')"><span>웹컨텐츠용엑셀샘플</span></a></div>
			</c:if>
		</div>
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="120px" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">엑셀파일</th>
						<td class="bold">
							<div id="fileBox">
							<input type="file" id="p_file" name="p_file" style="width:100%">
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:upload()"><span>저장</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function upload(){
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
		
		frm.action = "/adm/lcms/old/oldContentUploadXls.do";
		frm.target = "_self";
		frm.submit();
	}

	function doDownloadSample(fileName){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
	 	var link = '/com/fileDownload.do?fileName=' + fileName;
		window.location.href = link;
		return;
	}
</script>