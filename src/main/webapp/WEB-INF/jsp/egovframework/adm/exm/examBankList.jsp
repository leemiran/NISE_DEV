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

 	<input type="hidden" name="p_process" 		value="">
	<input type="hidden" name="p_action"  		value="">
	<input type="hidden" name="p_grcode"  		value="">
	<input type="hidden" name="p_subj"			id="p_subj"			value="">
	<input type="hidden" name="p_subjnm"		id="p_subjnm"		value="">
	<input type="hidden" name="p_examnum"  		value="">
	<input type="hidden" name="p_examtype"  		value="">
	<input type="hidden" name="p_chknum"  		value="">
	<input type="hidden" name="p_updateMove"  	value="U">
	<input type="hidden" name="p_type"  	value="">
	<input type="hidden" name="p_exam_subj"  	value="">
	
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value=""							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	<div class="searchWrap txtL MT10">
	<ul class="datewrap">
			<li><strong class="shTit">검색 : <select name="p_searchtype" class="input">
              <option value="1" <c:if test="${p_searchtype eq '1'}">selected</c:if>>콘텐츠명</option>
            </select></strong></li>
			
			<li><input type="text" name="p_searchtext" id="p_searchtext" size="60" value="${p_searchtext}"/></li>
			<li><a href="#none" class="btn_search" onclick="doPageList('1')"><span>검색</span></a></li>
	</ul>
</div>

		<div class="listTop">
                <div class="btnR MR05">
               		<a href="javascript:examSubj('insert', '');" class="btn01"><span>콘텐츠생성</span></a>
               		<a href="javascript:insertExam();" class="btn01"><span>개별문항생성</span></a>
               		<a href="javascript:insertExamExcel();" class="btn01"><span>엑셀일괄등록</span></a>
               		<a href="javascript:examBankExcelDown();" class="btn01"><span>엑셀다운로드</span></a>
                </div>
		</div>
		
	  <!-- contents -->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="50px" />
				<col width="100px" />
				<col />
				<col width="50px" />
				<col width="50px" />
				<col width="50px" />				
				<col width="50px"/>					
				<col width="50px"/>
				<col width="50px"/>
				<col width="50px"/>
			</colgroup>
			<thead>
				<tr>
					<th rowspan="2" scope="row">No</th>					
					<th rowspan="2" scope="row">콘텐츠코드</th>
					<th rowspan="2" scope="row">콘텐츠명</th>
					<th rowspan="2" scope="row">사용</th>
					<th rowspan="2" scope="row">중지</th>
					<th rowspan="2" scope="row">총</th>
					<th colspan="3" scope="row">문항Type</th>
					<th colspan="3" scope="row">난이도</th>		
					<th rowspan="2" scope="row">등록</th>					
				</tr>
				<tr>					
					<th scope="row">객관식</th>	
					<th scope="row">주관식</th>	
					<th scope="row">OX</th>										
					<th scope="row">상</th>
					<th scope="row">중</th>
					<th scope="row">하</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
					<td><c:out value="${result.rn}"/></td>					
					<td><a href="#" onclick="javascript:examSubj('modify','${result.examSubj}');"><c:out value="${result.examSubj}"/></a></td>
					<td class="left"><a href="#" onclick="questionDetailList('${result.examSubj}');"><c:out value="${result.examSubjnm}"/></a></td>
					<td><c:out value="${result.useYCnt}"/></td>
					<td><c:out value="${result.useNCnt}"/></td>
					<td><c:out value="${result.examCnt}"/></td>	
					<td><c:out value="${result.examtype1Cnt}"/></td>
					<td><c:out value="${result.examtype2Cnt}"/></td>
					<td><c:out value="${result.examtype3Cnt}"/></td>
					<td><c:out value="${result.levels1Cnt}"/></td>
					<td><c:out value="${result.levels2Cnt}"/></td>
					<td><c:out value="${result.levels3Cnt}"/></td>									
					<td><input type="checkbox" name="p_checks" id="p_${result.examSubj}" class="p_subj" value="${result.examSubj}"></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
</form>
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

$(function(){
	$("input:checkbox").change(function(){
		var subj = $(this).val();
		var subjTrue = $(this).attr("checked");
		$(".p_subj").attr("checked", false);			
		if(subjTrue == true){
			$("#p_"+subj).attr("checked", true);	
		}else{
			$("#p_subj").val("");
		}
	});
});

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {

	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	//thisForm.action = "/adm/exm/examQuestList.do";
	// thisForm.p_subj.value = $("#ses_search_subj").val();
		thisForm.action = "/adm/exm/examBankList.do";
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


//콘텐츠 추가 
function examSubj(p_type, p_exam_subj) {	
	
	thisForm.p_type.value = p_type;
	thisForm.p_exam_subj.value = p_exam_subj;
	
    farwindow = window.open("", "examBankSubjFormPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 900, height = 700, top=0, left=0");
    thisForm.target = "examBankSubjFormPopWindow";
    //thisForm.target = "_self";
    thisForm.action = "/adm/exm/examBankSubjFormPop.do";
	thisForm.submit();

    farwindow.window.focus(); 
}


//개별문항생성 
function insertExam() {	
	$(".p_subj").each(function(){
		if($(this).attr("checked") == true){
			$("#p_subj").val($(this).val());
	 	}	 
	});
	
	if($("#p_subj").val() == ""){
		alert("선택된 콘텐츠가 없습니다.")
		return;;
	}
	
    farwindow = window.open("", "examBankSubjInsertPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 950, height = 700, top=0, left=0");
    thisForm.target = "examBankSubjInsertPopWindow";
    //thisForm.target = "_self";
    thisForm.action = "/adm/exm/examBankView.do";
	thisForm.submit();
	
	$("#p_subj").val("");
	
    farwindow.window.focus(); 
}

//엑셀문제은행 등록 
function insertExamExcel() {	
	$(".p_subj").each(function(){
		if($(this).attr("checked") == true){
			$("#p_subj").val($(this).val());
	 	}	 
	});
	
	if($("#p_subj").val() == ""){
		alert("선택된 콘텐츠가 없습니다.")
		return;;
	}
	
    farwindow = window.open("", "examBankSubjExcelPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 950, height = 700, top=0, left=0");
    thisForm.target = "examBankSubjExcelPopWindow";
    //thisForm.target = "_self";
    thisForm.action = "/adm/exm/examBankFileUpload.do";
	thisForm.submit();

    farwindow.window.focus(); 
}

function questionDetailList(subj){
	
	farwindow = window.open("", "examBankPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 900, height = 700, top=0, left=0");
	thisForm.p_subj.value = subj;	
	thisForm.action = "/adm/exm/examBankDetailList.do";
    thisForm.target = "examBankPopWindow";
	thisForm.submit();
	
    farwindow.window.focus(); 
}

function examBankExcelDown(){
	
	$(".p_subj").each(function(){
		if($(this).attr("checked") == true){
			$("#p_subj").val($(this).val());
	 	}	 
	});
	
	if($("#p_subj").val() == ""){
		alert("선택된 콘텐츠가 없습니다.")
		return;;
	}
	
	thisForm.action = "/adm/exm/examBankExcelDown.do";
	thisForm.submit();
	
}

//-->
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
