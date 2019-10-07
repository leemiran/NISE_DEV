<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<%@page import="egovframework.com.cmm.service.Globals"%><script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="subj" id="subj" value="${subj}"/>
	<input type="hidden" name="lesson" id="lesson" value=""/>
	<input type="hidden" name="selectCrsName" id="selectCrsName" value="${selectCrsName}"/>
	<input type="hidden" name="selectCrsCode" id="selectCrsCode" value="${selectCrsCode}"/>
	<input id="pageIndex" name="pageIndex" type="hidden" value="<c:out value='${pageIndex}'/>"/>
	<input type="hidden" name="returnUrl" id="returnUrl" value="${returnUrl}">
	
	<input type="hidden" name="path" id="path" value="${path}">
	<input type="hidden" name="eventType" id="eventType">
	<input type="hidden" name="folder" id="folder">
	<input type="hidden" name="uploadType" id="uploadType">
	<input type="hidden" name="changeFile" id="changeFile">
	
	<!-- search wrap-->
	<div class="searchWrap">
		<div class="in" style="width:700px;text-align:left">
			<strong class="shTit">경로</strong>
			<%=Globals.CONTENT_PATH%><c:out value="${path}"/>
		</div>
	</div>
	<!-- // search wrap -->
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doPageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doCreateFolder()"><span>폴더생성</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doUploadFile('FILE')"><span>파일등록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doDeleteFile()"><span>삭제</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="5%" />					
					<col width="30%" />
					<col width="45%" />
					<col width="20%" />
				</colgroup>
				<thead>
					<tr>
						<th><input type="checkbox" id="checkAll"></th>
						<th>파일명</th>
						<th>파일 size</th>
						<th>등록/수정일</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${subj != path}">
						<tr>
							<td></td>
							<td class="left">
								<a href="#none" onclick="doContentFile('', 'BAK')"><img src="/images/dtree/img/folder.gif"/><b>..</b></a>
							</td>
							<td></td>
							<td></td>
						</tr>
					</c:if>
					<c:forEach items="${fileList}" var="result">
						<tr>
							<td><input type="checkbox" id="chk" name="chk" value="${result.fileName}"></td>
							<td class="left">
								<c:if test="${result.fileType == 1}">
									<a href="#none" onclick="doContentFile('<c:out value="${result.fileName}"/>', 'IN')">
									<img src="/images/dtree/img/folder.gif"/>
									<b><c:out value="${result.fileName}"/></b>
									</a>
								</c:if>
								<c:if test="${result.fileType != 1}">&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${result.fileName}"/></c:if>
							</td>
							<td style="text-align: right">
								<c:if test="${result.fileType != 1}">
									<c:out value="${result.byte}"/> Byte
								</c:if>
							</td>
							<td><c:out value="${result.modified}"/></td>
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

	//목록으로
	function doPageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var url = frm.returnUrl.value;
		frm.target = "_self";
		frm.action = url;
		frm.submit();
	}

	//파일관리 리스트
	function doContentFile( dir, type ){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.eventType.value = type;
		frm.folder.value = dir;
		frm.target = "_self";
		frm.action = "/adm/lcms/nct/lcmsNonScormContentList.do";
		frm.submit();
	}

	// 파일리스트 리로드
	function doPageReload(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.eventType.value = "IN";
		frm.folder.value = "";
		frm.target = "_self";
		frm.action = "/adm/lcms/nct/lcmsNonScormContentList.do";
		frm.submit();
	}

	//삭제
	function doDeleteFile(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( checkRadio(frm.chk) == "" ){
			alert("선택된 파일이 없습니다.");
			return;
		}
		if( confirm("선택된 파일을 복원되지 않습니다. 삭제 하시겠습니까?") ){
			frm.eventType.value = "IN";
			frm.folder.value = "";
			frm.target = "_self";
			frm.action = "/adm/lcms/nct/deleteContentFile.do";
			frm.submit();
        }
	}

	//파일등록
	function doUploadFile(type){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'fileUpload', 'top=100px, left=100px, height=150px, width=600px, scrolls=auto');
		frm.uploadType.value = type;
		frm.target = "fileUpload";
		frm.action = "/com/lcms/inn/innoDSFileUploadForm.do";
		frm.submit();
	}

	//폴더생성
	function doCreateFolder(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'createFolder', 'top=100px, left=100px, height=180px, width=600px, scrolls=auto');
		frm.target = "createFolder";
		frm.action = "/adm/lcms/cts/createContentFolderPopup.do";
		frm.submit();
	}

	
</script>