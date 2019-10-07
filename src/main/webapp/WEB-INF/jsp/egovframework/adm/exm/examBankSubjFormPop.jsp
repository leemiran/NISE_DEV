<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" onsubmit="return false;" method="post">
<input type="hidden" name="t_subj" 		id="t_subj" value="${p_subj}">
<input type="hidden" name="t_subjnm"  value="${p_subjnm}">
<input type="hidden" name="p_subj"  		id="p_subj" value="">
<input type="hidden" name="p_subjnm"  value="">
<input type="hidden" name="p_process" value="">
<input type="hidden" name="p_updateMove"  value="I">
<input type="hidden" name="p_exam_subj" id="p_exam_subj"  value="${examBankSubj.examSubj}">


<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>콘텐츠등록</h2>
		</div>
		
		<div class="searchWrap txtL MT10">
			<ul class="datewrap">
					<li><strong class="shTit">콘텐츠명 : </strong></li>			
					<li><input type="text" name="p_exam_subjnm" id="p_exam_subjnm" size="60" onkeypress="fn_checkEnter(event)" value="${examBankSubj.examSubjnm}"/></li>			
			</ul>
		</div>
				
		

		<!-- button -->
		<ul class="btnCen">
			<li><a href="javascript:InsertexamBankSubj()" class="pop_btn01" onclick=""><span>등록</span></a></li>			
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


//문제선택등록
function InsertexamBankSubj() {

	if($("#p_exam_subjnm").val() == ""){
		alert("콘텐츠명을 입력하여 주시기 바랍니다.")
		return;;
	}
	if($("#p_exam_subj").val() != ""){
		thisForm.p_process.value = 'modify';
	}else{
		thisForm.p_process.value = 'insert';	
	}
	
	
	thisForm.action = "/adm/exm/examBankSubjAction.do";
	thisForm.target = "_self";
	thisForm.submit();

}

//엔터입력시 
function fn_checkEnter(event){
	 if(event.keyCode == 13){
		 //fn_SearchDoPageList();
		 InsertexamBankSubj();
	 }
		 
}

</script>