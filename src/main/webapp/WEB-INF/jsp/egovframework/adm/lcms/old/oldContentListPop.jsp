<%@page import="egovframework.com.cmm.service.Globals"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script language="javascript1.2">
<!--
<c:if test="${!empty finish}">
	opener.doPageReload();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" align="center">
    	<div class="tit_bg">
			<h2>콘텐츠관리</h2>
		</div>
		<div class="popCon" style="width:97%;">
			<table summary="" width="100%" class="popTb" style="margin:10px 0px 10px 0px;">
				<colgroup>
					<col width="20%" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">경로</th>
						<td class="bold">
							/condata/<c:out value="${path}"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" id="subj" 		 name="subj" 		value="${subj}"/>
		<input type="hidden" id="path" 		 name="path" 		value="${path}"/>
		<input type="hidden" id="eventType"  name="eventType"/>
		<input type="hidden" id="folder"     name="folder"/>
		<!-- contents -->
		
		<!-- // contents -->
		
		
		
		<div class="listTop">	
			<div class="btnR"><a href="#" class="btn01" onclick="doCreateFolder()"><span>폴더생성</span></a></div>
			<div class="btnR"><a href="#" class="btn01" onclick="doDeleteFile()"><span>삭제</span></a></div>
		</div>
		
		
		<!-- list table-->
		<div style="height:320px; overflow-y:scroll;">
			<div class="tbList" style="width:97%">
				<table summary="" cellspacing="0" width="100%">
	                <caption>목록</caption>
	                <colgroup>
						<col width="5%" />					
						<col width="*" />
						<col width="25%" />
					</colgroup>
					<thead>
						<tr>
							<th><input type="checkbox" id="checkAll" onclick="doCheckAll(this)"></th>
							<th>파일명</th>
							<th>등록/수정일</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${'contents' != path}">
							<tr>
								<td></td>
								<td class="left">
									<a href="#none" onclick="doContentFile('', 'BAK')"><img src="/images/dtree/img/folder.gif"/><b>..</b></a>
								</td>
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
									<c:if test="${result.fileType != 1}"><c:out value="${result.fileName}"/></c:if>
								</td>
								<td><c:out value="${result.modified}"/></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<!-- list table-->
		</form>
		
		
		
		<iframe id="fileFrm" src="/innorix/InnoDS/index.html" width="520" height="302" scrolling="no" frameborder="0" marginwidth="0" marginheight="0"></iframe>
		
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function getParam(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		var arr = new Array();
		arr[0] = frm.subj.value;
		arr[1] = "<%=Globals.CONTENT_PATH%>" + frm.path.value;
		arr[2] = "NORMAL";
		return arr;
	}

	function doContentFile(dir, type){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.eventType.value = type;
		frm.folder.value = dir;
		frm.target = "_self";
		frm.action = "/adm/lcms/old/oldContentListPop.do";
		frm.submit();
	}
	
	//폴더생성
	function doCreateFolder(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		window.open('', 'createFolder', 'top=100px, left=100px, height=180px, width=600px, scrolls=auto');
		frm.target = "createFolder";
		frm.action = "/adm/lcms/old/createContentFolderPopup.do";
		frm.submit();
	}
	
	function upload(){
		doPageReload();
	}
	
	//페이지 리로드
	function doPageReload(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		frm.eventType.value = "IN";
		frm.folder.value = "";
		frm.target = "_self";
		frm.action = "/adm/lcms/old/oldContentListPop.do";
		frm.submit();
	}
	
	//삭제
	function doDeleteFile(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( checkRadio(frm.chk) == "" ){
			alert("선택된 파일이 없습니다.");
			return;
		}
		if( confirm("선택된 파일을 복원되지 않습니다. 삭제 하시겠습니까?") ){
			frm.eventType.value = "IN";
			frm.folder.value = "";
			frm.target = "_self";
			frm.action = "/adm/lcms/old/deleteContentFile.do";
			frm.submit();
        }
	}
	
	function doCheckAll(obj){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		for(var i=0; i<frm.chk.length; i++){
			frm.chk[i].checked = obj.checked;
		}
	}

</script>