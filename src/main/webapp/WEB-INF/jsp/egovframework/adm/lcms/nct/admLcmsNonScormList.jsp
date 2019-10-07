<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="subj" 		id="subj" 	value=""/>
	<input type="hidden" name="module" 		id="module" 	value=""/>
	<input type="hidden" name="lesson" 		id="lesson" 	value=""/>
	<input type="hidden" name="listUrl"     id="listUrl"	value="">
	<input type="hidden" name="uploadType" 	id="uploadType" value=""/>
	<input type="hidden" name="objSeq" 		id="objSeq" 	value=""/>
	<input type="hidden" name="objType" 	id="objType" 	value=""/>
	<input type="hidden" name="contentType" id="contentType" value="N"/>
	<input type="hidden" name="preview"     id="preview"     value="Y"/>
	<input type="hidden" name="pageIndex"   id="pageIndex"  value="<c:out value='${pageIndex}'/>"/> 
	<input type="hidden" name="returnUrl"     id="returnUrl"     value=""/>
	<input type="hidden" name="itemSeq"     id="itemSeq"     value=""/>
	<input type="hidden" name="p_studyType"     id="p_studyType"     value="NEW"/>
	
	<!-- search wrap-->
	<div class="searchWrap">
		<div class="in" style="width:100%">
			<strong class="shTit">과정분류</strong>
			<ui:code id="search_cour" selectItem="${search_cour}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="" />
			<strong class="shTit">콘텐츠유형</strong>
			<select id="search_contenttype" name="search_contenttype">
				<option value="">ALL</option>
			<c:forEach items="${codeList}" var="result">
				<option value="${result.code}" <c:if test="${search_contenttype eq result.code}">selected</c:if>>${result.codenm}</option>
			</c:forEach>
			</select>
			<strong class="shTit">과정명</strong>
			<input type="text" name="search_subjnm" id="search_subjnm" value="${search_subjnm}" style="ime-mode:active" onkeypress="javascript:fn_keyEvent('doPageList')"/>
			<strong class="shTit">과정코드</strong>
			<input type="text" name="search_subj" id="search_subj" value="${search_subj}" style="ime-mode:active" onkeypress="javascript:fn_keyEvent('doPageList')"/>
			<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
		</div>
	</div>
	<!-- // search wrap -->
	
	<div class="listTop">	
		<div class="btnR"><a href="#" class="btn01" onclick="doMasterform()"><span>MF관리</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doSampleExcel()"><span>샘플엑셀다운</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doExcelDownload()"><span>엑셀다운로드</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doContentUpload('NONSCORM')"><span>콘텐츠업로드</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="30px" />
				<col width="50px" />
				<col width="100px" />
				<col width="430px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
				<tr>
					<th>선택</th>
					<th>No</th>
					<th>과정코드</th>
					<th>과정명</th>
					<th>콘텐츠유형</th>
					<th>차시수</th>
					<th>과정분류</th>
					<th>제작(리뉴얼)<br/>년도</th>
					<th>최초사용<br/>년도</th>
					<th>리뉴얼<br/>차순</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td>
						<input type="radio" id="courseRadio" name="courseRadio" value="${result.subj}" style="cursor:pointer;">
						<input type="hidden" id="${result.subj}" name="${result.subj}" contenttype="<c:out value="${result.contenttype}"/>">
					</td>
					<td><c:out value="${(pageTotCnt+1)-result.num}"/></td>
					<td><c:out value="${result.subj}"/></td>
					<td class="left">
						<a href="#none" onclick="doOrgView('<c:out value="${result.subj}"/>', '<c:out value="${result.contenttype}"/>')">
							<c:out value="${result.subjnm}"/>
						</a>
					</td>
					<td><c:out value="${result.contenttypenm}"/></td>
					<td><c:out value="${result.orgCount}"/></td>
					<td><c:out value="${result.classname}"/></td>
					<td><c:out value="${result.conyear}"/></td>
					<td><c:out value="${result.fyear}"/></td>
					<td><c:out value="${result.conrenum}"/></td>
				</tr>
				<tr id="tr<c:out value="${result.subj}"/>" style="display:none;">
					<td colspan="6">
						<iframe id="iframe${result.subj}" name="iframe${result.subj}" scrolling="no" frameborder="0" style="width:100%; height:0px"><c:out value="${result.subj}"/></iframe>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
	<!-- 페이징 시작 -->
	<div class="paging">
		<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
	</div>
	<!-- 페이징 끝 -->
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

var e;
var oldTr = "";

	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	 function doPageList() {
		 var frm = eval('document.<c:out value="${gsMainForm}"/>');
		 frm.action = "/adm/lcms/nct/lcmsNonScormList.do";
		 frm.target = "_self";
		 frm.submit();
	}
	
	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	function doLinkPage(pageNo) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/lcms/nct/lcmsNonScormList.do";
		frm.pageIndex.value = pageNo;
		frm.target = "_self";
		frm.submit();
	}
	
	/* ********************************************************
	 * 콘텐츠업로드 함수
	 ******************************************************** */
	function doContentUpload( type ){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = checkRadio(frm.courseRadio);
		if( p_subj == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		frm.subj.value = p_subj;
		frm.uploadType.value = type;
		frm.contentType.value = document.getElementById(p_subj).getAttribute("contenttype");
		var win = window.open('', 'fileUpload', 'top=100px, left=100px, height=429px, width=600px, scrolls=auto');
		frm.action = "/com/lcms/inn/innoDSFileUploadForm.do";
		frm.target = "fileUpload";
		frm.submit();
		win.focus();
	}
	
	
	// 차시리스트 오픈
	function doOrgView( p_subj, contenttype ){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		this.doRemoveTr();
		if( oldTr != p_subj ){
			oldTr = p_subj;
			document.getElementById("tr"+p_subj).style.display = "";
			frm.subj.value = p_subj;
			frm.contentType.value = contenttype;
			e = document.getElementById("iframe"+p_subj);
			frm.action = "/adm/lcms/nct/nonScormOrgList.do";
			frm.target = "iframe"+p_subj;
			frm.submit();
		}else{
			frm.subj.value = "";
			oldTr = "";
		}
	
	}

	// 차시리스트 닫기
	function doRemoveTr(){
		if( oldTr != "" ){
			document.getElementById("tr"+oldTr).style.display = "none";
		}
	}

	function doContentFile( module, lesson ){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.module.value = module;
		frm.lesson.value = lesson;
		frm.returnUrl.value = "/adm/lcms/nct/lcmsNonScormList.do";
		frm.target = "_self";
		frm.action = "/adm/lcms/nct/lcmsNonScormContentList.do";
		frm.submit();
	}

	//차시정보 삭제
	function doRemoveOrg(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = frm.subj.value;
		var target = eval("document.iframe"+p_subj+".document");
		if( !eval("iframe"+p_subj).doCheckYn() ){
			return;
		}
		var submitForm = eval("iframe"+p_subj).orgForm;
		if( confirm("정말 삭제하시겠습니까?") ){
			submitForm.target = "iframe"+p_subj;
			submitForm.action = "/adm/lcms/nct/LcmsNonScormModuleDelete.do";
			submitForm.submit();
		}
	}

	function doCheckSelect(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = frm.subj.value;
		return eval("iframe"+p_subj).doSelectCheckBox();
	}



	function doDeleteLog(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = frm.subj.value;
		var str = eval("iframe"+p_subj).doOrgCheckList();
		return str;
	}

	//차시순서변경
	function doMoveOrg(moveStatus){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = frm.subj.value;
		
		var selectRow = this.doCheckSelect();

		if( selectRow >= 0 ){
			if( selectRow == 0 && moveStatus == "UP" || selectRow + 1 == eval("iframe"+p_subj).orgTable.rows.length && moveStatus == "DOWN" ){
				return;
			}else{
				if( moveStatus == "UP" ){
					eval("iframe"+p_subj).orgTable.moveRow(selectRow - 1, selectRow);
				}else{
					eval("iframe"+p_subj).orgTable.moveRow(selectRow, selectRow + 1);
				}
				doUpdateSort();
			}
		}
	
	}

	function doUpdateSort(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = frm.subj.value;
		var org = eval("iframe"+p_subj).doOrgList();
		
		var url = "<c:out value="${gsDomainContext}" />/com/aja/lcm/updateOrgSort.do";
		var param = "?p_org="+org;
		goGetURL(url, param);
	}


	//모듈수정팝업
	function doModuleUpdatePopup(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = frm.subj.value;

		var target = eval("document.iframe"+p_subj+".document");
		var p_module = eval("iframe"+p_subj).doSelectModule();
		if( p_module == "" ){
			return;
		}
		frm.module.value = p_module;
		frm.subj.value = p_subj;

		window.open('', 'modulePupup', 'top=100px, left=100px, height=210px, width=600px');
		frm.target = "modulePupup";
		frm.action = "/adm/lcms/nct/lcmsModuleUpdatePage.do";
		frm.submit();

	}

	//레슨수정팝업
	function doUpdatePopup(p_module, p_lesson){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = frm.subj.value;
		frm.module.value = p_module;
		frm.lesson.value = p_lesson;

		window.open('', 'lessonPupup', 'top=100px, left=100px, height=280px, width=600px');
		frm.target = "lessonPupup";
		frm.action = "/adm/lcms/nct/lcmsLessonUpdatePage.do";
		frm.submit();
	}

	//엑셀 다운로드
	function doExcelDownload(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		var p_subj = checkRadio(frm.courseRadio);
		if( p_subj == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		frm.subj.value = p_subj;
		frm.target = "_self";
		frm.action = "/adm/lcms/nct/lcmsNonScormExcelDown.do";
		frm.submit();
	}


	// CA정보 삭제 (module, lesson)
	function doRemoveCA(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		if( confirm("정말 삭제하시겠습니까?") ){
			frm.target = "_self";
			frm.action = "/adm/lcms/nct/deleteLcmsCA.do";
			frm.submit();
		}
	}

	//학습로그팝업
	function doProgressLog(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = frm.subj.value;
		if( !eval("iframe"+p_subj).doCheckYn() ){
			return;
		}
		var submitForm = eval("iframe"+p_subj).orgForm;
		window.open('', 'preview', 'top=100px, left=100px, height=450px, width=820px');
		submitForm.target = "preview";
		submitForm.action = "/adm/lcms/nct/progressLogPopup.do";
		submitForm.submit();
	}

	function doPreview(p_module, p_lesson) {  

        var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = frm.subj.value;
			
		var orgSeq = null;
		var selectRow = this.doCheckSelect();
		if( selectRow >= 0 ){
			orgSeq = eval("iframe"+p_subj).doCheckOrgVal();
		}else{
			return;
		}

		frm.module.value = orgSeq;
		frm.lesson.value = p_lesson;
		
		
	
	
		window.open('', 'preview', 'top=100px, left=100px, height=670px, width=1024px');
		frm.target = "preview";
		frm.action = "/com/lcms/len/learning.do";
		frm.submit();
	}

	function doSampleExcel(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		window.open('', 'sampleExcel', 'top=100px, left=100px, height=130px, width=650px');
		frm.target = "sampleExcel";
		frm.action = "/adm/lcms/nct/sampleExcelPopup.do";
		frm.submit();
	}


	function doMasterform(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var radio = checkRadio(frm.courseRadio);
		if( radio == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		frm.target = "_self";
		frm.subj.value = radio;
		frm.returnUrl.value = "/adm/lcms/nct/lcmsNonScormList.do";
		frm.action = "/adm/lcms/com/masterFormUpdatePage.do";
		frm.submit();
	}
	
</script>