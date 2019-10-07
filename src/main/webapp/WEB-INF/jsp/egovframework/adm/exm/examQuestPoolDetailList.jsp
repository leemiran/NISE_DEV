<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">
alert("${resultMsg}");
window.close();
opener.location.reload();
</c:if>
-->
</script>
<jsp:scriptlet>
pageContext.setAttribute("cr", "\r");
pageContext.setAttribute("lf", "\n");
pageContext.setAttribute("crlf", "\r\n");
</jsp:scriptlet>
<style>
table.ex1 {width:97%; margin:0 auto; text-align:left; border-collapse:collapse; text-decoration: none; }
 .ex1 th, .ex1 td {padding:0px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
</style>
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" action="">
 	<input type="hidden" name="p_process" 										value="">
	<input type="hidden" name="p_action"  										value="">
	<input type="hidden" name="t_subj"  											value="${t_subj}">
	<input type="hidden" name="t_subjnm"  										value="${t_subjnm}">
	<input type="hidden" name="p_grcode"  										value="">
	<input type="hidden" name="p_subj" 				id="p_subj" 			value="${p_subj}">
	<input type="hidden" name="p_subjnm" 			id="p_subjnm" 		value="${p_subjnm}">
	<input type="hidden" name="p_examnum"  		id="p_examnum" 		value="">
	<input type="hidden" name="p_examnums"  	id="p_examnums" 	value="">
	<input type="hidden" name="p_examtype"  									value="">
	<input type="hidden" name="p_chknum"  									value="">
	<input type="hidden" name="p_updateMove"  								value="${p_updateMove}">
<div id="popwrapper">
	<div class="popIn">
		<div class="tit_bg">
			<h2>[<c:out value="${p_subjnm}"></c:out>] 과정 문제 내역</h2>
		</div>
        
	  <!-- contents -->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="60px" />
				<col />
				<col width="80px"/>
				<col width="80px"/>
				<col width="60px"/>
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row">문제</th>
					<th scope="row">난의도</th>
					<th scope="row">문제분류</th>
					<th scope="row"><input type="checkbox" id="allcheck" class="allcheck" value=""></th>
				</tr>
			</thead>
			<tbody>
			<c:set var="titletxt" value=""/>		
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
					<td><c:out value="${status.count}"/></td>
					<td class="left"> 		
				<c:if test="${p_updateMove eq 'U'}">
						 <a href="javascript:doView('${result.subj}','${result.examnum}');">
		        		${fn:substring(result.examtext, 0, fn:indexOf(result.examtext, lf))}
		        		</a>
				</c:if>			
					<c:if test="${p_updateMove eq 'I'}">
			        <c:set var="titletxt" value="${fn:replace(result.examtext, lf, '<br/>')}"></c:set>
					<c:if test="${not empty titletxt}">
						<c:if test="${fn:indexOf(titletxt, '보기>') > -1}">
							<c:set var='titletxt'>${fn:replace(titletxt, "보기>", "<table class='ex1'><tr><th scope='col'>보기</th><td>")}</c:set>
							<c:set var="titletxt">${titletxt}</td></tr></table></c:set>
						</c:if>					
						${titletxt}
					</c:if>
				</c:if>
				 				
					</td>
					<td><c:out value="${result.levelsnm}"/></td>
					<td><c:out value="${result.examtypenm}"/></td>
					<td><input type="checkbox" name="p_checks" class="excheck" value="${result.subj}|${result.examnum}"></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">
			<c:if test="${p_updateMove eq 'I'}">
				<li><a href="javascript:InsertQuestion()" class="pop_btn01" onclick=""><span>등록</span></a></li>
			</c:if>
			<li><a href="javascript:questionDetailList()" class="pop_btn01" onclick=""><span>목록</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
		
</div>
</div>		

</form>

<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

$(function(){
	$("#allcheck").click(function(){
		if($(this).attr("checked")){
			$(".excheck").attr("checked", true);
		}else{
			$(".excheck").attr("checked", false);
		}
	});
});

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

//문제 보기 화면으로 이동
function doView(p_subj, p_examnum) {	
     farwindow = window.open("", "examQuestViewPopWindow", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,copyhistory=no, width = 800, height = 700, top=0, left=0");
     $("#p_subj").val(p_subj);
     $("#p_examnum").val(p_examnum);
     thisForm.target = "examQuestViewPopWindow"
     thisForm.action = "/adm/exm/examQuestView.do";
 	 thisForm.submit();

     farwindow.window.focus();
}

//선택한 문제번호 모으기
function chkSelected() {
	var selectedcnt = "";
	$(".excheck").each(function(){
		if($(this).attr("checked") == true){
			var param = new String($(this).val()).split("|");
			selectedcnt += param[1]+";";
		}
	});
	selectedcnt = selectedcnt.substring(0 , selectedcnt.length-1); 
	return selectedcnt; 
}

//문제번호 복사
function InsertQuestion(){
	var sels = chkSelected();
	if(sels == ""){
		alert("선택된 내역이 없습니다.");
		return;
	}
	thisForm.action = "/adm/exm/examQuestPoolCopy.do";
	thisForm.p_examnums.value = sels;
	thisForm.target = "_self";
	thisForm.submit();
}

function questionDetailList(){
	
	thisForm.p_subj.value = thisForm.t_subj.value;
	thisForm.p_subjnm.value = thisForm.t_subjnm.value;
	thisForm.action = "/adm/exm/examPoolSubjListPop.do";
	thisForm.target = "_self";
	thisForm.submit();
	
}
//-->
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
