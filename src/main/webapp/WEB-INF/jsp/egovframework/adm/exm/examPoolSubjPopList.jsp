<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
<input type="hidden" name="t_subj" 		id="t_subj" value="${p_subj}">
<input type="hidden" name="t_subjnm"  value="${p_subjnm}">
<input type="hidden" name="p_subj"  		id="p_subj" value="">
<input type="hidden" name="p_subjnm"  value="">
<input type="hidden" name="p_process" value="">
<input type="hidden" name="p_updateMove"  value="I">

<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>문제를 가져올 과정 내역</h2>
		</div>
		
<div class="searchWrap txtL MT10">
	<ul class="datewrap">
			<li><strong class="shTit">검색 : <select name="p_searchtype" class="input">
              <option value="1" <c:if test="${p_searchtype eq '1'}">selected</c:if>>과정명</option>
            </select></strong></li>
			
			<li><input type="text" name="p_searchtext" id="p_searchtext" size="60" value="${p_searchtext}"/></li>
			<li><a href="#none" class="btn_search" onclick="searchList()"><span>검색</span></a></li>
	</ul>
</div>
		
		<!-- contents -->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="60px" />
				<col width="100px" />
				<col width="100px" />
				<col/>
				<col width="100px"/>			
				<col width="60px"/>
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
					<td><c:out value="${status.count}"/></td>
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
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="javascript:InsertQuestion()" class="pop_btn01" onclick=""><span>등록</span></a></li>
			
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
		
		<br/>
	</div>
</div>

</form>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

$(function(){
	$("input:checkbox").change(function(){
		var subj = $(this).val();
		var subjTrue = $(this).attr("checked");
		$(".p_subj").attr("checked", false);			
		if(subjTrue == true)$("#p_"+subj).attr("checked", true);	
	});
});

function searchList() {

	thisForm.action = "/adm/exm/examPoolSubjListPop.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//문제선택등록
function InsertQuestion() {
	 $(".p_subj").each(function(){
		 if($(this).attr("checked") == true){
			$("#p_subj").val($(this).val());
		 }	 
	 });
	 
	 if($("#p_subj").val() == ""){
		 alert("선택된 과정이 없습니다.")
		 return;;
	 }
	
	//alert('Pool추가를 이용하여 문제를 등록하면 각 문제들이 과정1차시로 기본세팅됩니다.\n\n문제의 차시정보를 수정하려면 문제명을 클릭하여 수정하시기 바랍니다.'); 
	
	thisForm.p_process.value = 'poolinsert';
	thisForm.action = "/adm/exm/examQuestPoolCopy.do";
	thisForm.target = "_self";
	thisForm.submit();

}

//문제선택 여부
function chkSelected() {
	var selectedcnt = 0;
	$(".p_subj").each(function(){
		if($(this).attr("checked") == true){
			selectedcnt++;
		}
	});
	return selectedcnt; 
}

function questionDetailList(subj, subjnm){
	thisForm.p_subj.value = subj;
	thisForm.p_subjnm.value = subjnm;
	thisForm.action = "/adm/exm/examQuestionDetailList.do";
	thisForm.target = "_self";
	thisForm.submit();
}

</script>