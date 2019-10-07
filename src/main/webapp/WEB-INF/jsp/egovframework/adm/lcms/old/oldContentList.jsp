<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="subj" 		id="subj" 		value=""/>
	<input type="hidden" name="pageIndex"   id="pageIndex"  value="${pageIndex}"/> 
	<input type="hidden" id="contenttype" 	name="contenttype"/>
	<input type="hidden" id="height" 		name="height"/>
	<input type="hidden" id="width" 		name="width"/>
	<input type="hidden" id="preurl" 		name="preurl"/>
	<input type="hidden" name="returnUrl"     id="returnUrl"/>
	
	<!-- search wrap-->
	<div class="searchWrap">
		<div class="in" style="width:100%">
			<!-- 2017 추가 -->
			<strong class="shTit">최종콘텐츠</strong>
			<select name="searchFinalContentYn">
				<option value="Y" <c:if test="${searchFinalContentYn eq 'Y' }">selected</c:if>>YES</option>
				<option value="N" <c:if test="${searchFinalContentYn eq 'N' }">selected</c:if>>NO</option>
				<option value="" <c:if test="${searchFinalContentYn eq '' }">selected</c:if>>전체</option>
			</select>
			<!-- 2017 추가 끝 -->
			<strong class="shTit">과정분류</strong>
			<ui:code id="search_cour" selectItem="${search_cour}" gubun="cursBunryu" codetype=""  upper="" title="" className="" type="select" selectTitle="ALL" event="" />
			<strong class="shTit">과정명</strong>
			<input type="text" name="search_subjnm" id="search_subjnm" value="${search_subjnm}" style="ime-mode:active" onkeypress="javascript:fn_keyEvent('doPageList')"/>
			<strong class="shTit">과정코드</strong>
			<input type="text" name="search_subj" id="search_subj" value="${search_subj}" style="ime-mode:active" onkeypress="javascript:fn_keyEvent('doPageList')"/>
			<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
		</div>
	</div>
	<!-- // search wrap -->
	
	<div class="listTop">
		<div class="btnR"><a href="/adm/lcms/old/practiceStudyList.do?s_menu=29000000&s_submenu=29010000" class="btn01"><span>과정보기</span></a></div>
		<!-- 
		<div class="btnR"><a href="#" class="btn01" onclick="delBetaProgress();"><span>진도삭제</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doBetatest();"><span>모의학습</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doSample();"><span>맛보기</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="doPreview();"><span>미리보기</span></a></div>
		 -->
		<div class="btnR"><a href="#" class="btn01" onclick="itemList();"><span>콘텐츠구성</span></a></div>
		<!-- 
		<div class="btnR"><a href="#" class="btn01" onclick="whenMFConfig();"><span>M/F관리</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="whenInfo();"><span>기본정보</span></a></div>
		 -->
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <!--
                <col width="30px" />
				<col width="60px" />
				<col width="120px" />
				<col />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				-->
				<!-- 2017 추가 -->
				<col width="30px" />
				<col width="40px" />
				<col width="90px" />
				<col />
				<col width="80px" />
				<col width="100px" />
				<col width="50px" />
				<col width="50px" />
				<col width="50px" />
				<col width="80px" />
				<col width="60px" />
				<col width="70px" />
				<col width="50px" />
				<col width="60px" />
				<col width="80px" />
				<col width="80px" />
				<col width="50px" />
				<!-- 2017 추가 끝 -->
			</colgroup>
			<thead>
				<tr>
					<th>선택</th>
					<th>No</th>
					<th>과정코드</th>
					<th>과정명</th>
					<th>컨텐츠유형</th>
					<th>과정분류</th>
					<th>최종컨텐츠</th>
					<th>제작년도</th>
					<!-- 2017 추가 -->
					<th>리뉴얼년도</th>
					<!-- 2017 추가 끝 -->
					<th>최초사용<br/>년도</th>
					<th>리뉴얼<br/>차순</th>
					<th>모바일지원여부</th>
					<th>차시</th>
					<th>전체시간</th>
					<!-- 2017 추가 -->
					<th>포팅날짜</th>
					<th>콘텐츠수정날짜</th>
					<th>mp3보유</th>
					<!-- 2017 추가 끝 -->
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
					<td><input type="radio" id="courseRadio" name="courseRadio" value="${result.subj}" style="cursor:pointer;" ></td>
					<td><c:out value="${(pageTotCnt+1)-result.num}"/></td>
					<td><c:out value="${result.subj}"/></td>
					<td class="left">
						<c:out value="${result.subjnm}"/>
						<input type="hidden" id="${result.subj}" name="${result.subj}" contenttype="${result.contenttype}" height="${result.height}" width="${result.width}" preurl="${result.preurl}">
					</td>
					<td><c:out value="${result.contenttypenm}"/></td>
					<td><c:out value="${result.classname}"/></td>
					<!-- 2017 추가 -->
					<td>${result.finalContentYn }</td>
					<!-- 2017 추가 끝 -->
					<td><c:out value="${result.conyear}"/></td>
					<!-- 2017 추가 -->
					<td>${result.renewalYear }</td>
					<!-- 2017 추가 끝 -->
					<td><c:out value="${result.fyear}"/></td>
					<td><c:out value="${result.conrenum}"/></td>
					<td><c:out value="${result.mobile}"/></td>
					<td><c:out value="${result.contLesson}"/></td>
<!--					<td><c:out value="${result.countSco}"/></td>-->
					<td>${result.subjTotalTime }</td>
					<!-- 2017 추가 -->
					<td>${result.portingDate }</td>
					<td>${result.contentModifyDate }</td>
					<td>${result.mp3PossessYn }</td>
					<!-- 2017 추가 끝 -->
				</tr>
				<tr id="tr<c:out value="${result.subj}"/>" style="display:none;">
					<td colspan="5">
						<iframe id="iframe${result.subj}" name="iframe${result.subj}" scrolling="no" frameborder="0" style="width:700px; height:0px"><c:out value="${result.subj}"/></iframe>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="17">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
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
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_subj"			id="p_subj">
<input type="hidden" name="p_year"			id="p_year">
<input type="hidden" name="p_subjseq"		id="p_subjseq">
<input type="hidden" name="p_studytype"		id="p_studytype">
<input type="hidden" name="p_contenttype"	id="p_contenttype">
<input type="hidden" name="p_next_process"	id="p_next_process">
<input type="hidden" name="p_process"		id="p_process">
<input type="hidden" name="s_action"		id="s_action">
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
		 /* 2017 추가 */
		 frm.pageIndex.value = 1;
		 /* 2017 추가 끝 */
		 frm.target = "_self";
		 frm.submit();
	}
	
	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	function doLinkPage(pageNo) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/lcms/old/oldContentList.do";
		frm.pageIndex.value = pageNo;
		frm.target = "_self";
		frm.submit();
	}

	//선택과정 기본정보 셋팅
	function setSubjValue(p_subj){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var target = document.getElementById(p_subj);
		frm.subj.value = p_subj;
		frm.contenttype.value = target.getAttribute("contenttype");
		frm.height.value = target.getAttribute("height");
		frm.width.value = target.getAttribute("width");
		frm.preurl.value = target.getAttribute("preurl");
	}


	// 기본정보페이지
	function whenInfo(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = checkRadio(frm.courseRadio);
		if( p_subj == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		setSubjValue(p_subj);
		frm.target = "_self";
		frm.action = "/adm/lcms/old/oldContentInfoView.do";
		frm.submit();
	}

	// MF관리
	function whenMFConfig(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var radio = checkRadio(frm.courseRadio);
		if( radio == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		frm.target = "_self";
		frm.subj.value = radio;
		frm.returnUrl.value = "/adm/lcms/old/oldContentList.do";
		frm.action = "/adm/lcms/com/masterFormUpdatePage.do";
		frm.submit();
	}

	// 차시리스트
	function itemList( p_subj ){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = checkRadio(frm.courseRadio);
		if( p_subj == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		setSubjValue(p_subj);
		frm.action = "/adm/lcms/old/oldContentItemList.do";
		frm.target = "_self";
		frm.submit();
	
	}

	function doPreview(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = checkRadio(frm.courseRadio);
		if( p_subj == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		setSubjValue(p_subj);

		var studyType = "preview";
		
		if ( frm.contenttype.value == "S" ) {	// SCORM2004 일 때만 예외처리
			studyType = "preview"; 
		}

		var p_contenttype = frm.contenttype.value;
		var p_height = frm.height.value;
		var p_width = frm.width.value;
		
		this.goPreview( p_contenttype, p_subj, studyType, p_height, p_width );
	}

	function goPreview( contentType, subj, studyType, height, width ) {
		var form = document.studyForm;
		
		form.p_contenttype.value = contentType;
		form.p_subj.value = subj;
		
		form.p_year.value = "2000";
		form.p_subjseq.value = "0001";
		form.p_studytype.value = studyType;
		form.p_next_process.value = "";

		switch( contentType ) {
			case "N":
			case "O":
				form.p_process.value = "main";
				form.action = "/servlet/controller.lcms.EduStart";
				break;
			case "OA":
				form.p_process.value = "main";
				form.action = "/servlet/controller.lcms.NewEduStart";
				break;
			case "S":
				form.p_process.value = "mappingInfoList";
				form["p_next_process"].value = "testeduMain";
				form.action = "/servlet/controller.scorm2004.ScormStudyServlet";
				break;
			case "K":
				form.p_process.value = "main";
	    		form.action = "/servlet/controller.lcms.KTEduStart";		
				break;
		}

		if(height == 99999 && width == 99999){
			
			studyPop = window.open("","studyPopup","channelmode");
	 	}
		else{
			studyPop = window.open("","studyPopup","toolbar=no,status=yes,menubar=no,scrollbars=no,resizable=yes,width=685,height=400,left=0,top=0");
		}
	    
		studyPop.focus();

	    form.target = "studyPopup";
		form.submit();

		form.target = window.self.name;
	}

	function doSample(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = checkRadio(frm.courseRadio);
		if( p_subj == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		setSubjValue(p_subj);
		var contentType = frm.contenttype.value;
		var p_preurl = frm.preurl.value;
		var url = p_preurl;
		switch( contentType ) {
		case "N":
			
			url = p_preurl;
			//url = "/content/lcms/" + subj + "/guest/guest.html";
			break;
		case "L":
			
			url = p_preurl;
			//url = "/content/lcms/" + subj + "/guest/guest.html";
			break;			
		case "O":
			url = "/servlet/controller.lcms.EduStart?p_subj=" + p_subj + "&p_year=PREV";
			break;
		case "OA":
			url = "/servlet/controller.lcms.NewEduStart?p_subj=" + p_subj + "&p_year=PREV";
			break;
		case "S":
			url = "/servlet/controller.scorm2004.ScormStudyServlet?p_process=previewMain&p_subj=" + p_subj;
			break;
		case "K":
			url = "/preview/" + p_subj.substring(0,4) + "/index.html";
			break;
	}

    var studyPop = window.open(url,"studyPopup","toolbar=no,status=yes,menubar=no,scrollbars=no,resizable=yes,width=1014,height=676,left=0,top=0");
    studyPop.focus();
	}

	/**
	 * 베타테스트 : 해당 CP 및 총괄관리자 베타테스트 실행
	 */
	function doBetatest( ) {
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = checkRadio(frm.courseRadio);
		if( p_subj == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		setSubjValue(p_subj);
		var studyType = "betatest";
		var year = "0000";
		var subjseq = "0000";
		var p_contentType = frm.contenttype.value;
		var p_height = frm.height.value;
		var p_width = frm.width.value;
		

		goStudy( p_contentType, p_subj, year, subjseq, studyType, p_height, p_width );
	}

	function goStudy( contentType, subj, year, subjseq, studyType, height, width ) {
		var form = document.studyForm;
		
		form.p_contenttype.value = contentType;
		form.p_subj.value = subj;
		form.p_year.value = year;
		form.p_subjseq.value = subjseq;
		form.p_studytype.value = studyType;
		form.p_next_process.value = "";

		switch( contentType ) {
			case "N":
			case "O":
				form.p_process.value = "main";
				form.action = "/servlet/controller.lcms.EduStart";
				break;
			case "OA":
				form.p_process.value = "main";
				form.action = "/servlet/controller.lcms.NewEduStart";
				break;
			case "S":
				form.p_process.value = "mappingInfoList";
				if (studyType == "betatest")
					form.p_next_process.value = "betatestMain";
				else
					form.p_next_process.value = "studyMain";
				form.action = "/servlet/controller.scorm2004.ScormStudyServlet";
				break;
			case "K":
				form.p_process.value = "main";
	    		form.action = "/servlet/controller.lcms.KTEduStart";		
				break;
		}

		if(height == 99999 && width == 99999){
			studyPop = window.open("","studyPopup","channelmode","toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no,left=0,top=0");
	 	}
		else{
			studyPop = window.open("","studyPopup","toolbar=no,status=yes,menubar=no,scrollbars=no,resizable=yes,width=685,height=400,left=0,top=0");
		}
	    studyPop.focus();

	    form.target = "studyPopup";
		form.submit();

		form.target = window.self.name;
	}

	function delBetaProgress() {

		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		var p_subj = checkRadio(frm.courseRadio);
		if( p_subj == "" ){
			alert("과정을 선택하세요.");
			return;
		}
		setSubjValue(p_subj);
		var contentType = frm.contenttype.value;
		
		if ( confirm("베타 테스트 진도정보를 삭제하시겠습니까?") ) {
			frm.action = "/adm/lcms/old/oldContentBetaProgressDelete.do";
			frm.target = "_self";
			frm.submit();

		}
	}
	
</script>