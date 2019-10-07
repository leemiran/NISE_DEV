<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>

<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<style>
table.ex1 {width:97%; margin:0 auto; text-align:left; border-collapse:collapse; text-decoration: none; }
 .ex1 th, .ex1 td {padding:0px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
</style>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">

 	<input type="hidden" name="p_process" value="">
	<input type="hidden" name="p_action"  value="">
	<input type="hidden" name="p_grcode"  value="">
	<input type="hidden" name="p_subj"  value="">
	<input type="hidden" name="p_examnum"  value="">
	<input type="hidden" name="p_examtype"  value="">
	<input type="hidden" name="p_chknum"  value="">
	
	
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="AA"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->

		<div class="listTop">
		  <!-- 
		  <div class="sub_tit floatL">
			<h4>과정명 : [${ses_search_subjnm}]</h4>	
		  </div>		
		   -->
		  	<!-- 
                <div class="btnR MR05">
               		<a href="javascript:previewQuestion();" class="btn01"><span>미리보기</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="javascript:deleteQuestion();" class="btn01"><span>삭제</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="javascript:insertFileToDB();" class="btn01"><span>파일로추가</span></a>
                </div>
		  	 -->
                <div class="btnR MR05">
               		<a href="javascript:insertPool();" class="btn01"><span>등록</span></a>
                </div>
                <div class="btnR MR05">
               		<a href="javascript:doView('','')" class="btn01"><span>Pool추가</span></a>
                </div>                 
		</div>
        
		
	  <!-- contents -->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="60px" />
				<col width="250px"/>
				<col/>
				<col width="80px"/>
				<col width="80px"/>
				<col width="60px"/>
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row">과정명</th>
					<th scope="row">문제</th>
					<th scope="row">난의도</th>
					<th scope="row">문제분류</th>
					<th scope="row">등록</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
					<td><c:out value="${status.count}"/></td>
					<td class="left"><c:out value="${result.subjnm}"/></td>
					<td class="left"><c:out value="${result.examtext}"/></td>
					<td><c:out value="${result.levelsnm}"/></td>
					<td><c:out value="${result.examtypenm}"/></td>
					<td><input type="checkbox" name="p_checks" value="${result.subj}|${result.examnum}"></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
		<!-- // contents -->
		<!-- button 
		<ul class="btnCen">
			<li><a href="javascript:InsertQuestion()" class="pop_btn01" onclick=""><span>저장</span></a></li>
			
		</ul>
		-->
		<!-- // button -->
		
		
		

</form>




<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');



//선택한 문제번호 모으기
function chkExamNum(frm) {
	var v_chkcnt = 0;
	var v_chknum = "";
	
	if (frm.all['p_checks']) {
          
		if (frm.p_checks != null && frm.p_checks.length > 0) {
			for (i=0; i<frm.p_checks.length; i++) {
				if (frm.p_checks[i].checked == true) {

					v_chkcnt += 1;
					v_chknum += frm.p_checks[i].value + ",";
				}
			}
			v_chknum = v_chknum.substring(0,v_chknum.length-1);
		} else if ( frm.p_checks != null ) {
			if (frm.p_checks.checked) {

				v_chkcnt = 1;
				v_chknum = frm.p_checks.value;
			}
		}
		if (v_chkcnt==0) {
			alert("문제를 선택하세요");
			return "-1";           
		}
	} else {
		alert("선택할 문제가 없습니다.");
		return "-1";
	}
	return v_chknum;
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {

	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return;
	}
	
	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}


	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	//thisForm.action = "/adm/exm/examQuestList.do";
	 thisForm.p_subj.value = $("#ses_search_subj").val();
		thisForm.action = "/adm/exm/examQuestPoolList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/exm/examQuestList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}



//등록 / 수정
function doView(p_subj, p_examnum) {
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return;
	}
	
	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}

	
     farwindow = window.open("", "examQuestViewPopWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 800, height = 700, top=0, left=0");
     thisForm.p_subj.value = $("#ses_search_subj").val();
     thisForm.p_examnum.value = p_examnum;
     thisForm.target = "examQuestViewPopWindow"
     thisForm.action = "/adm/exm/examQuestView.do";
 	 thisForm.submit();

     farwindow.window.focus();
}




//문제 미리보기
function previewQuestion() {

	var frm = thisForm;
	
	var v_chknum = chkExamNum(frm);
	if (v_chknum != "-1") {
		window.open("", "popExamQuestionPreview", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes, resizable=yes,copyhistory=no, width=800, height=600, top=0, left=0");
	
	  	frm.target = "popExamQuestionPreview";
	  	frm.action = "/adm/exm/examQuestPreview.do";
	  	frm.p_subj.value = $("#ses_search_subj").val();
	  	frm.p_chknum.value = v_chknum;
		frm.submit();
	}
}


//폴 추가 
function insertPool() {//doPageList
	if($("#ses_search_gyear").val() == '')
	{	
		alert("연도를 선택하세요");
		return;
	}

	if($("#ses_search_grseq").val() == '')
	{	
		alert("교육기수를 선택하세요");
		return; 
	}
	
	if($("#ses_search_subj").val() == '')
	{	
		alert("과정을 선택하세요");
		return;
	}

	
    //farwindow = window.open("", "examQuestPoolListPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 1017, height = 700, top=0, left=0");
    thisForm.p_subj.value = $("#ses_search_subj").val();
    //thisForm.target = "examQuestPoolListPopWindow";
    thisForm.target = "_self";
    thisForm.action = "/adm/exm/examQuestPoolList.do";
	thisForm.submit();

    farwindow.window.focus(); 
}


	

//파일로 추가
function insertFileToDB() {
		if($("#ses_search_gyear").val() == '')
		{	
			alert("연도를 선택하세요");
			return;
		}
	
		if($("#ses_search_grseq").val() == '')
		{	
			alert("교육기수를 선택하세요");
			return;
		}
		
		if($("#ses_search_subj").val() == '')
		{	
			alert("과정을 선택하세요");
			return;
		}
	
		farwindow = window.open("", "examQuestFileUploadPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 830, height = 600, top=0, left=0");
		
		thisForm.p_subj.value = $("#ses_search_subj").val();
		thisForm.action = "/adm/exm/examQuestFileUpload.do";
		thisForm.target = "examQuestFileUploadPopWindow";
		thisForm.submit();
	
}



	 
function whenAllSelect() {
	if(thisForm.all['p_checks']) {
		if (thisForm.p_checks.length > 0) {
			for (i=0; i<thisForm.p_checks.length; i++) {
				thisForm.p_checks[i].checked = true;
			}
		} else {
			thisForm.p_checks.checked = true;
		}
	}
}

function whenAllSelectCancel() {
	if(thisForm.all['p_checks']) {
		if (thisForm.p_checks.length > 0) {
			for (i=0; i<thisForm.p_checks.length; i++) {
				thisForm.p_checks[i].checked = false;
			}
		} else {
			thisForm.p_checks.checked = false;
		}
	}
}

function chkeckall(){
    if(thisForm.p_chkeckall.checked){
      whenAllSelect();
    }
    else{
      whenAllSelectCancel();
    }
}



//문제 삭제하기
function deleteQuestion() {

	var frm = thisForm;
	
	var v_chknum = chkExamNum(frm);

	if (v_chknum != "-1") {

	    if(!confirm("정말 삭제하시겠습니까?\n이미 사용되어진 문제는 삭제되지 않습니다.")) {
	        return;
	    }
	
	  	
	  	frm.p_subj.value = $("#ses_search_subj").val();
	  	frm.p_chknum.value = v_chknum;
		frm.p_process.value = "checkDelete";
		frm.target = "_self";
	  	frm.action = "/adm/exm/examQuestAction.do";
		frm.submit();
	}
}


//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
