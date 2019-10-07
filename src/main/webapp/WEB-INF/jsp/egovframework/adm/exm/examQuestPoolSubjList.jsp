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
            
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value=""							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	<div class="searchWrap txtL MT10">
	<ul class="datewrap">
			<li><strong class="shTit">검색 : <select name="p_searchtype" class="input">
              <option value="1" <c:if test="${p_searchtype eq '1'}">selected</c:if>>과정명</option>
            </select></strong></li>
			
			<li><input type="text" name="p_searchtext" id="p_searchtext" size="60" value="${p_searchtext}"/></li>
			<li><a href="#none" class="btn_search" onclick="doPageList('1')"><span>검색</span></a></li>
	</ul>
</div>

		<div class="listTop">
                <div class="btnR MR05">
               		<a href="javascript:insertPool();" class="btn01"><span>등록</span></a>
                </div>
		</div>
		
	  <!-- contents -->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
				<col />
				<col width="100px"/>				
				<col width="100px"/>			
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row">년도</th>
					<th scope="row">과정코드</th>
					<th scope="row">과정명</th>
					<th scope="row">문제수</th>		
					<th scope="row">등록</th>					
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
					<td><c:out value="${result.rn}"/></td>
					<td><c:out value="${result.year}"/></td>
					<td><c:out value="${result.subj}"/></td>
					<td class="left"><a href="#" onclick="questionDetailList('${result.subj}','${result.subjnm}');"><c:out value="${result.subjnm}"/></a></td>
					<td><c:out value="${result.examCnt}"/></td>
					<td><input type="checkbox" name="p_checks" id="p_${result.subj}" class="p_subj" value="${result.subj}"></td>
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


//폴 추가 
function insertPool() {	
 $(".p_subj").each(function(){
	 if($(this).attr("checked") == true){
		$("#p_subj").val($(this).val());
	 }	 
 });
 
 if($("#p_subj").val() == ""){
	 alert("선택된 과정이 없습니다.")
	 return;;
 }
	
    farwindow = window.open("", "examQuestPoolListPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 900, height = 700, top=0, left=0");
    thisForm.target = "examQuestPoolListPopWindow";
    //thisForm.target = "_self";
    thisForm.action = "/adm/exm/examPoolSubjListPop.do";
	thisForm.submit();

    farwindow.window.focus(); 
}

function questionDetailList(subj, subjnm){
	
	farwindow = window.open("", "examQuestPoolPopWindow", "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 900, height = 700, top=0, left=0");
	thisForm.p_subj.value = subj;
	thisForm.p_subjnm.value = subjnm;
	thisForm.action = "/adm/exm/examQuestionDetailList.do";
    thisForm.target = "examQuestPoolPopWindow";
	thisForm.submit();
	
    farwindow.window.focus(); 
}

//-->
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
