<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>



<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" onsubmit="return false;">
    <input type="hidden" name="p_subj"         value="${p_subj}">
    <input type="hidden" name="p_gyear"        value="${p_gyear}">
    <input type="hidden" name="p_subjseq"      value="${p_subjseq}">
    <input type="hidden" name="p_lesson"       value="${p_lesson}">
    <input type="hidden" name="p_examtype"     value="${p_examtype}">
    <input type="hidden" name="p_papernum"     value="${p_papernum}">
    <input type="hidden" name="p_userid"       value="${p_userid}">
    <input type="hidden" name="p_retry"        value='${p_retry}'>    
    <input type="hidden" name="p_process" >    
	
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>사용자 재응시횟수 수정</h2>
         </div>
		<!-- contents -->
        
        
		<div class="searchWrap txtL">
			<ul class="datewrap">
				<li><strong class="shTit">과정명 : ${p_subjnm} </strong></li>
            </ul>
            <ul>
				<li><strong class="shTit">연도 : ${p_gyear}</strong></li>
                <li><strong class="shTit">기수 : ${p_subjseq}기</strong></li>
                <li><strong class="shTit">시험지번호 : ${p_papernum}</strong></li>
            </ul>					
		</div>
        
        
        
        <div class="btnCen ML10 MR10">
            	<p><b>재응시횟수</b> : <input name="p_userretrycnt" type="text" class="input" size="5" value='${p_userretry}'> (시험지 재응시 : ${p_retry}) </p>                           
		</div>
        
		<!-- // contents -->
		<br/>
		<br/>
		
		<!-- button -->
		<ul class="btnCen">
			<li><a href="javascript:fnRetry()" class="pop_btn01"><span>저장</span></a></li>  
            <li><a href="javascript:window.close()" class="pop_btn01"><span>닫기</span></a></li>            
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->
	

</form>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>



<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsPopForm}"/>');
	
	// 재응시 수정
	function fnRetry(){
		  if(confirm('사용자 재응시횟수를 수정 하시겠습니까?')){
		  
		  // 숫자체크
		  if(!numeric_chk(thisForm.p_userretrycnt)) return;

		  thisForm.p_process.value = 'update';
		  thisForm.action = "/adm/exm/examUserUpdateRetryAction.do";
		  thisForm.target = "_self";
		  thisForm.submit();
		  
		}
	}
</script>