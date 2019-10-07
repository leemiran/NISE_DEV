<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<style>
table.ex1 {width:100%; margin:0 auto; text-align:left; border-collapse:collapse; text-decoration: none; }
 .ex1 th, .ex1 td {padding:0px 10px;text-align:left;}
 .ex1 tbody tr td {background:#F3F5BB; text-align:left;}
 .ex1 tbody tr th {background:#F2F684; color:#1BA6B2; text-align:center; width:10%; border-right:1px solid #fff}
</style>

<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
<input type="hidden" name="p_subj"  value="${p_subj}">
<input type="hidden" name="p_quesnum"    value="${p_quesnum}">
<input type="hidden" name="p_lesson" value="001">
<input type="hidden" name="p_process" value="">
<input type="hidden" name="imgNo" value="">
<input type="hidden" name="imgSelNo" value="">



<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>과제 문항 관리</h2>
         </div>
		<!-- contents -->
		        
            <div class="popCon MR20">
                <table summary="" width="100%" class="popTb">
                    <colgroup>
                        <col width="10%" />
                        <col width="40%" />
                        <col width="10%" />
                        <col width="40%" />
                    </colgroup>
                    <tbody>                        
                        <tr>
                          <th scope="col">사용여부</th>
                            <td>
                                <ui:code id="p_isuse" selectItem="${view.isuse}" gubun="defaultYN" codetype=""  upper="" title="사용여부" className="" type="select" selectTitle="" event="" />
                        	
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">출제자</th>
                            <td>
                                <input type="text" id="p_examiner" name="p_examiner" value="${view.examiner}" alt="출제자">
                            </td>
                        </tr>                      
                        <tr>
                            <th scope="col">과제물 주제</th>
                            <td>
                            	<textarea name="p_questext" style="width:95%" rows="10">${view.questext}</textarea>
                            </td>
                        </tr>					
                    </tbody>
                </table>
      	  </div>        
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
		<c:if test="${empty p_quesnum}">
			<li><a href="javascript:ActionPage('insert');" class="pop_btn01"><span>등록</span></a></li>
		</c:if>	
		<c:if test="${not empty p_quesnum}">
			<li><a href="javascript:ActionPage('update');" class="pop_btn01"><span>수정</span></a></li> 
            <c:if test="${view.useyn eq 'N' }">
            	<li><a href="javascript:ActionPage('delete');" class="pop_btn01"><span>삭제</span></a></li>
            </c:if>
            
        </c:if>
             
            <li><a href="javascript:window.close();" class="pop_btn01"><span>닫기</span></a></li>            
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->

</form>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var thisForm = eval('document.<c:out value="${gsPopForm}"/>');


//문제입력여부체크 
function chkData() {
  if (blankCheck(thisForm.p_questext.value)) {
    thisForm.p_questext.focus();
    alert('과제물 주제를 입력하십시요.');
    return false;
  }

  return true;
}


//문제 추가 등록
function ActionPage(p_process) {
    if(!chkData()) {
      return;
    }

    var msg = "";
    if(p_process == 'insert') msg = "등록하시겠습니까?";
    if(p_process == 'update') msg = "수정하시겠습니까?";
    if(p_process == 'delete') msg = "삭제하시겠습니까?";

	if(confirm(msg))
	{
		thisForm.action = "/adm/rep/reportQuestionAction.do";		
		thisForm.p_process.value = p_process;
		thisForm.target = "_self";
		thisForm.submit();
	}
}


</script>