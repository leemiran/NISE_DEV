<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>



<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
<input type="hidden" name="p_subj"  value="${p_subj}">
<input type="hidden" name="p_process" value="">






<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>Pool등록</h2>
		</div>
		
		
<div class="searchWrap txtL MT10">
	<ul class="datewrap">
			<li><strong class="shTit">검색 : <select name="p_searchtype" class="input">
              <option value="1" <c:if test="${p_searchtype eq '1'}">selected</c:if>>과정명</option>
              <option value="2" <c:if test="${p_searchtype eq '2'}">selected</c:if>>문제</option>
              <option value="3" <c:if test="${p_searchtype eq '3'}">selected</c:if>>난이도</option>
              <option value="4" <c:if test="${p_searchtype eq '4'}">selected</c:if>>문제분류</option>
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


		<!-- button -->
		<ul class="btnCen">
			<li><a href="javascript:InsertQuestion()" class="pop_btn01" onclick=""><span>저장</span></a></li>
			
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


function searchList() {

	thisForm.action = "/adm/exm/examQuestPoolList.do";
	thisForm.target = "_self";
	thisForm.submit();
}



//문제선택등록
function InsertQuestion() {
	var chkselected = chkSelected();
	if (chkselected < 1) {
	 alert('문제를 선택하세요.');
	 return;
	}
	alert('Pool추가를 이용하여 문제를 등록하면 각 문제들이 과정1차시로 기본세팅됩니다.\n\n문제의 차시정보를 수정하려면 문제명을 클릭하여 수정하시기 바랍니다.'); 
	
	
	thisForm.p_process.value = 'poolinsert';
	thisForm.action = "/adm/exm/examQuestAction.do";
	thisForm.target = "_self";
	thisForm.submit();

}

//문제선택 여부
function chkSelected() {
	var selectedcnt = 0;
	if(thisForm.all['p_checks']) {
	 if (thisForm.p_checks.length > 0) {
	   for (i=0; i<thisForm.p_checks.length; i++) {
	     if (thisForm.p_checks[i].checked == true) {
		      selectedcnt++;
		    }  
	   }
	 } else {
	   if (thisForm.p_checks.checked == true) {
		    selectedcnt++;
	   }
	 }
	}
	return selectedcnt; 
}





</script>