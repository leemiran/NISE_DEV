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
 	
 	<input type="hidden" name="p_process" id="p_process" value="change"/>
	<input type="hidden" name="p_exam_subj" id="p_exam_subj" value="${p_exam_subj}"/>
	<input type="hidden" name="p_setexamcnt" id="p_setexamcnt" value="${p_setexamcnt}"/>
	<input type="hidden" name="p_setexamlessoncnt" id="p_setexamlessoncnt" value="${p_setexamlessoncnt}"/>				
	<input type="hidden" name="ses_search_subj" id="ses_search_subj" value="${ses_search_subj}"/>
	<input type="hidden" name="ses_search_year" id="ses_search_year" value="${ses_search_year}"/>
	<input type="hidden" name="ses_search_subjseq" id="ses_search_subjseq" value="${ses_search_subjseq}"/>
	<input type="hidden" name="p_papernum" id="p_papernum" value="${p_papernum}"/>
	<input type="hidden" name="p_examtype" id="p_examtype" value="${p_examtype}"/>
	<input type="hidden" name="p_examexamtype" id="p_examexamtype" value="${p_examexamtype }"/>
	<input type="hidden" name="p_examlevels" id="p_examlevels" value="${p_examlevels }"/>
	<input type="hidden" name="p_examnum" id="p_examnum" value="${p_examnum }"/>
	<input type="hidden" name="p_lesson" id="p_lesson" value="${p_lesson}"/>	
	<input type="hidden" name="p_change_examnum" id="p_change_examnum"  value="">
	
	
<div id="popwrapper">
	<div class="popIn">
		<div class="tit_bg">
			<h2>[<c:out value="${list[0].subjnm}"></c:out>] 과정 문제 내역</h2>
		</div>
        
        <!-- button -->
		<ul class="btnCen">
			<li><a href="javascript:PaperExamChangeAction('change')" class="pop_btn01"><span>대 체</span></a></li>
		</ul>
		<!-- // button -->
		
	
		<table summary="" cellspacing="0" width="100%">
			<tr>
				<td style="padding-left:10px;">
					<c:set var="titletxt" value="${fn:replace(view.examtext, lf, '<br/>')}"></c:set>			
					<c:if test="${not empty titletxt}">
						<c:if test="${fn:indexOf(titletxt, '보기>') > -1}">
							<c:set var='titletxt'>${fn:replace(titletxt, "보기>", "<table class='ex1'><tr><th scope='col'>보기</th><td>")}</c:set>
							<c:set var="titletxt">${titletxt}</td></tr></table><br/></c:set>
						</c:if>
						${titletxt}
					</c:if>
				</td>
			</tr>
			<tr>
				<td style="padding-left:10px;">
					<c:forEach items="${examSellist}" var="result" varStatus="status">	
						<c:choose>
							<c:when test="${view.examtype eq '1'}">
										<li> ${result.selnum}) ${result.seltext}</li>
							</c:when>
							<c:when test="${view.examtype eq '2'}">
										<li></li>
							</c:when>
							<c:when test="${view.examtype eq '3'}">
										<li>${result.selnum}) ${result.seltext}</li>
							</c:when>
							<c:otherwise>
										<li>${result.selnum}) ${result.seltext}</li>
							</c:otherwise>
						</c:choose>
					
					</c:forEach>
				</td>
			</tr>
		</table>					
							
	  <!-- contents -->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="60px" />
				<col />
				<col width="80px"/>
				<col width="80px"/>
				<col width="80px"/>
				<col width="60px"/>
				<col width="60px"/>
				<col width="60px"/>
				<col width="60px"/>
				<col width="60px"/>				
				<col width="60px"/>
				<col width="60px"/>
			</colgroup>
			<thead>
				<tr>
					<th scope="row">No</th>
					<th scope="row">문제</th>
					<th scope="row">출제자</th>
					<th scope="row">난의도</th>
					<th scope="row">문제분류</th>
					<th scope="row">등록연도</th>					
					<th scope="row">최종수정</th>
					<th scope="row">최초사용</th>
					<th scope="row">최종사용</th>										
					<th scope="row">사용횟수</th>
					<th scope="row">사용여부</th>
					<th scope="row">선택</th>
				</tr>
			</thead>
			<tbody>
			<c:set var="titletxt" value=""/>		
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
					<td><c:out value="${status.count}"/></td>
					<td class="left">${fn:substring(result.examtext, 0, fn:indexOf(result.examtext, lf))}</td>
					<td><c:out value="${result.examiner}"/></td>
					<td><c:out value="${result.levelsnm}"/></td>
					<td><c:out value="${result.examtypenm}"/></td>					
					<td><c:out value="${result.regyear}"/></td>
					<td><c:out value="${result.ldate}"/></td>
					<td><c:out value="${result.firstyear}"/></td>
					<td><c:out value="${result.lastyear}"/></td>					
					<td><c:out value="${result.usecnt}"/></td>
					<td><c:out value="${result.isuse}"/></td>					
					<td><input type="checkbox" name="p_checks" id="p_${result.examnum}" class="p_subj" value="${result.examnum}"></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
		<!-- // contents -->
		<!-- button -->
		<ul class="btnCen">			
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
	$("input:checkbox").change(function(){
		var subj = $(this).val();		
		var subjTrue = $(this).attr("checked");
		$(".p_subj").attr("checked", false);			
		if(subjTrue == true){
			$("#p_"+subj).attr("checked", true);
			$("#p_change_examnum").val(subj);
		}else{
			$("#p_change_examnum").val("");
		} 
	});
});


//평가 문제지 미리보기 
function PaperExamChangeAction(p_process) {
	
	var msg = "";
	
	msg = "대체 하시겠습니까?";
	
	if (!confirm(msg)) {
		return;
	}
	 
	if (!chkData()) {
		return;
	}	

	thisForm.action = "/adm/exm/examBankPaperExamChangeAction.do";
	thisForm.submit();
}
function chkData() {
	
	if($("#p_change_examnum").val() == '')
	{	
		alert("대체할 문제를 선택하세요");
		return false;
	}
	return true;	
}	

//-->
</script>

<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
