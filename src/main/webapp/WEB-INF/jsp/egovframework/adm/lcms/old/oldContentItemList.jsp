<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="subj" 			id="subj" 			value="${subj}">
	<input type="hidden" name="search_cour" 			id="search_cour" 			value="${search_cour}">
	<input type="hidden" name="search_subjnm" 			id="search_subjnm" 			value="${search_subjnm}">
	<input type="hidden" name="search_subj" 			id="search_subj" 			value="${search_subj}">
	
	
	<input type="hidden" name="module" 			id="module">
	<input type="hidden" name="lesson" 			id="lesson">
	
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doPageList()"><span>목록</span></a></div>
		<!-- <div class="btnR"><a href="#" class="btn01" onclick="doExcelDownload()"><span>엑셀출력</span></a></div> -->
		<div class="btnR">&nbsp;&nbsp;&nbsp;</div>
		<!-- 
		<div class="btnR"><a href="#" class="btn01" onclick="doExcelUpload('Y')"><span>모바일엑셀등록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doExcelUpload('N')"><span>컨텐츠엑셀등록</span></a></div>
		-->
		<div class="btnR"><a href="#" class="btn01" onclick="doLessonUpload('Y')"><span>Lesson 엑셀등록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doModuleUpload('N')"><span>Module 엑셀등록</span></a></div>
		<!-- <div class="btnR">&nbsp;&nbsp;&nbsp;</div> -->
		<!-- 
		<div class="btnR"><a href="#" class="btn01" onclick="doContentListPop()"><span>컨텐츠파일관리</span></a></div>
		<div class="btnR">&nbsp;&nbsp;&nbsp;</div>
		 -->
		<div class="btnR">&nbsp;&nbsp;&nbsp;</div>
		<div class="btnR"><a href="#" class="btn01" onclick="doModulePop('')"><span>Module추가</span></a></div>
		<!-- <div class="btnR"><a href="#" class="btn01" onclick="doDeleteCA()"><span>CA정보삭제</span></a></div> -->
		
		<div class="btnR">&nbsp;&nbsp;&nbsp;</div>
		<div class="btnR"><a href="#" class="btn01" onclick="doLessonRecovery()"><span>Lesson 백업</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doModuleRecovery()"><span>Module 백업</span></a></div>
		
		<div class="btnR">&nbsp;&nbsp;&nbsp;</div>
		<div class="btnR"><a href="#" class="btn01" onclick="doLessonExcelDownload()"><span>Lesson 엑셀 다운로드</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doModuleExcelDownload()"><span>Module 엑셀 다운로드</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
			<caption>교육참가자명단</caption>
			<colgroup>
				<col width="40px" />
				<col />
				<col width="200px" />
				<col width="250px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th>No</th>
					<th>Module</th>
					<th>Lesson</th>
					<th>위치</th>
					<th>Chapter<br/>시작시간(초)</th>
					<th>Chapter<br/>종료시간(초)</th>
					<th>미리보기</th>
				</tr>
			</thead>
			<tbody>
			<c:set var="oldModule" value=""/>
			<c:forEach items="${list}" var="result" varStatus="i">
				<c:set var="lessonCnt" value="${result.lessonCnt}"/>
				<c:set var="newModule" value="${result.module}"/>
				<tr>
					<td><c:out value="${i.count}"/></td>
				<c:if test="${oldModule != newModule}">
					<td class="left" rowspan="<c:out value="${lessonCnt}"/>">
						<a href="#none" onclick="doModulePop('<c:out value="${result.module}"/>')">
						<c:out value="${result.module}"/> - <c:out value="${result.modulenm}"/> <br><b>(웹 전체시간 : ${result.webTime })</b>
						</a>
						<br/>
						<b>
						+ 모바일 : <a href="${result.mobileUrl}" target="_blank"><c:out value="${result.mobileUrl}"/>
						<br> (모바일 전체시간 : ${result.mTime })
						</a>
						</b>
						<br/><br/>
						<div class="btn"><a href="#" class="btn01" onclick="doLessonPop('<c:out value="${result.module}"/>','')"><span>Lesson추가</span></a></div>
						<c:if test="${!empty result.lesson}">
						</c:if>
					</td>
					<c:set var="oldModule" value="${result.module}"/>
				</c:if>
					<td class="left">
						<c:if test="${!empty result.lesson}">
							<a href="#none" onclick="doLessonPop('<c:out value="${result.module}"/>','<c:out value="${result.lesson}"/>')">
							<c:out value="${result.lesson}"/> - <c:out value="${result.lessonnm}"/>
							</a>
						</c:if>
					</td>
					<td class="left"><c:out value="${result.starting}" /></td>
					
					<td class="center"><c:out value="${result.mStart}" />(초)</td>
					<td class="center"><c:out value="${result.mEnd}" />(초)</td>
					<td class="left">
						<div class="btn"><a href="<c:out value="${result.starting}" />" target="_blank" class="btn01" ><span>미리보기</span></a></div>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="20">등록된 내용이 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	 function doPageList() {
		 var frm = eval('document.<c:out value="${gsMainForm}"/>');
		 frm.action = "/adm/lcms/old/oldContentList.do";
		 frm.target = "_self";
		 frm.submit();
	}

	function doPageReload(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		 frm.action = "/adm/lcms/old/oldContentItemList.do";
		 frm.target = "_self";
		 frm.submit();
	}
	

	// CA삭제
	function doDeleteCA(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( confirm("정말 삭제하시겠습니까?") ){
			frm.action = "/adm/lcms/old/oldContentItemDelete.do";
			frm.target = "_self";
			frm.submit();
		}
	}

	function doModulePop( p_module ){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'modulePopup', 'top=100px, left=100px, height=550px, width=600px, scrolls=no');
		frm.module.value = p_module;
		frm.action = "/adm/lcms/old/oldContentModulePopup.do";
		frm.target = "modulePopup";
		frm.submit();
	}

	function doLessonPop( p_module, p_lesson ){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'lessonPopup', 'top=100px, left=100px, height=340px, width=600px, scrolls=no');
		frm.module.value = p_module;
		frm.lesson.value = p_lesson;
		frm.action = "/adm/lcms/old/oldContentLessonPopup.do";
		frm.target = "lessonPopup";
		frm.submit();
	}

	function doExcelUpload(mobile_yn){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'uploadPopup', 'top=100px, left=100px, height=180px, width=600px, scrolls=no');
		frm.action = "/adm/lcms/old/oldContentUploadXlsPage.do?mobile_yn=" + mobile_yn;
		frm.target = "uploadPopup";
		frm.submit();
	}

	function doExcelDownload(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.target = "_self";
		frm.action = "/adm/lcms/old/oldContentExcelDown.do";
		frm.submit();
	}

	function doPreview( subj, module, str){
		var url 	= "/servlet/controller.lcms.EduStart";
	   	var addval  = "";
	   		addval = addval  +"?p_process=main";
	   		addval = addval  +"&p_subj="+subj;
	   		addval = addval  +"&p_subjseq=0000";
	   		addval = addval  +"&p_year=0000";
	   		addval = addval  +"&p_review=Y";
	   		addval = addval  +"&p_module="+module;

		url += addval;
		var options = "toolbar=no,menubar=no,scrollbars=yes,resizable=no,left=0,top=0,width=1024px;height:768px;";
		window.open(url,"studyMainForm",options);
	}
	
	
	function doContentListPop(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var win = window.open('', 'contentListPop', 'top=100px, left=100px, height=800px, width=600px, scrolls=auto');
		frm.target = "contentListPop";
		frm.action = "/adm/lcms/old/oldContentListPop.do";
		frm.submit();
		win.focus();
	}
	
	
	function doModuleUpload(mobile_yn){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'uploadPopup', 'top=100px, left=100px, height=740px, width=1000px, scrolls=no');
		frm.action = "/adm/lcms/old/moduleUploadPage.do";
		frm.target = "uploadPopup";
		frm.submit();
	}
	
	function doLessonUpload(mobile_yn){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'uploadPopup', 'top=100px, left=100px, height=740px, width=1000px, scrolls=no');
		frm.action = "/adm/lcms/old/lessonUploadPage.do";
		frm.target = "uploadPopup";
		frm.submit();
	}
	
	function doModuleRecovery() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'uploadPopup', 'top=100px, left=100px, height=700px, width=600px, scrolls=no');
		frm.action = "/adm/lcms/old/moduleRecoveryPage.do";
		frm.target = "uploadPopup";
		frm.submit();
	}
	
	function doLessonRecovery() {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'uploadPopup', 'top=100px, left=100px, height=700px, width=600px, scrolls=no');
		frm.action = "/adm/lcms/old/lessonRecoveryPage.do";
		frm.target = "uploadPopup";
		frm.submit();
	}
	
	function doModuleExcelDownload(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.target = "_self";
		frm.action = "/adm/lcms/old/moduleExcelDown.do";
		frm.submit();
	}
	
	function doLessonExcelDownload(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.target = "_self";
		frm.action = "/adm/lcms/old/lessonExcelDown.do";
		frm.submit();
	}
</script>