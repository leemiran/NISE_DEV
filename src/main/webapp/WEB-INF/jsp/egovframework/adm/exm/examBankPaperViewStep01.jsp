<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>



<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
<input type="hidden" name="p_subj"  value="${p_subj}">
<input type="hidden" name="p_gyear"  value="${p_gyear}">
<input type="hidden" name="p_subjseq"  value="${p_subjseq}">
<input type="hidden" name="p_papernum"  value="${p_papernum}">

<input type="hidden" name="p_exam_subj"  value="${p_exam_subj}">
<input type="hidden" name="o_subj"  value="${o_subj}">

<input type="hidden" name="p_lesson" value="${not empty p_lesson ? p_lesson : '001'}">

<input type="hidden" name="p_lessonstart" value="001">
<input type="hidden" name="p_lessonend" value="001">
<input type="hidden" name="p_process" value="${p_process}">
    

<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>평가문제지관리</h2>
         </div>
		<!-- contents -->
        
		<div>
			<p class="pop_tit">평가문제지관리 Step 1</p>			
		</div>
        
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
                            <th scope="col">평가종류</th>
                            <td>
<c:if test="${empty view}">
                            <ui:code id="p_examtype" selectItem="${p_examtype}" gubun="" codetype="0012"  levels="1"  condition=""
							upper="" title="평가종류" className="" type="select" selectTitle="" event="" />
</c:if>
<c:if test="${not empty view}">
                            <input type="hidden" name="p_examtype" value="${p_examtype}"/>
                            ${view.examtypenm}
</c:if>                            
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">결과점수공개</th>
                            <td>
                            <ui:code id="p_isopenanswer" selectItem="${empty view.isopenanswer ? 'Y' : view.isopenanswer}" gubun="defaultYN" codetype=""  levels=""  condition=""
							upper="" title="결과점수공개" className="vrM" type="radio" selectTitle="" event="" />
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">결과정답(해설)공개</th>
                            <td>
                            	<ui:code id="p_isopenexp" selectItem="${empty view.isopenexp ? 'N' : view.isopenexp}" gubun="defaultYN" codetype=""  levels=""  condition=""
								upper="" title="결과점수공개" className="" type="radio" selectTitle="" event="" />
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">시험기간</th>
                          <td>
                                <span>
                                    <input id="p_startdt" name="p_startdt" type="text" value="${fn2:getFormatDate(view.startdt, 'yyyy-MM-dd')}" size="10" maxlength="10" tabindex="15" readonly>
                                    <a href="#none" onclick="popUpCalendar(this, document.all.p_startdt, 'yyyy-mm-dd')"><img src="/images/adm/ico/ico_calendar.gif" alt="달력" /></a> ~
                                    <input id="p_enddt" name="p_enddt" type="text" value="${fn2:getFormatDate(view.enddt, 'yyyy-MM-dd')}" size="10" maxlength="10" tabindex="15" readonly>
                                	<a href="#none" onclick="popUpCalendar(this, document.all.p_enddt, 'yyyy-mm-dd')"><img src="/images/adm/ico/ico_calendar.gif" alt="달력" /></a>


                                        <a href="javascript:UpdateDate()" class="pop_btn01"><span>시험기간연장</span></a>
                            </span>                          		
                          </td>
                        </tr>
                        
                        <tr>
                            <th scope="col">시험시간</th>
                            <td>
                            	<ui:code id="p_examtime" selectItem="${view.examtime}" gubun="" codetype="0094"  levels="1"  condition=""
								upper="" title="시험시간" className="" type="select" selectTitle="" event="" />
                            </td>
                        </tr>
                        <tr>
                          <th scope="col">재응시</th>
                          <td><input name="p_retrycnt" type="text" class="input" size="3" value="${empty view.retrycnt ? '0' : view.retrycnt}"> 회</td>
                        </tr>					
                    </tbody>
                </table>
      	  </div>      
        
  <!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="javascript:doAction()" class="pop_btn01"><span>다 음</span></a></li> 
			
			<c:if test="${paperCnt == 0}">
				<li><a href="javascript:doDelete()" class="pop_btn01"><span>삭 제</span></a></li> 
			</c:if>
            <li><a href="javascript:window.close()" class="pop_btn01"><span>취 소</span></a></li>                        
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->

</form>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var thisForm = eval('document.<c:out value="${gsPopForm}"/>');

//다음단계로
function doAction() {
  if (!chkData()) {
    return;
  }
  thisForm.action = '/adm/exm/examBankPaperViewStep02.do';
  thisForm.target = '_self';
  thisForm.submit();
}



//삭제하기
function doDelete() {
  if (!confirm("평가문제지를 삭제하시겠습니까?")) {
    return;
  }
  thisForm.p_process.value   = "delete";
  thisForm.action = '/adm/exm/examPaperAction.do';
  thisForm.target = '_self';
  thisForm.submit();
}


//시험기간 연장하기
function UpdateDate() {
  if (!confirm("평가문제지 시험기간을 연장하시겠습니까?")) {
    return;
  }
  thisForm.p_process.value   = "updatedate";
  thisForm.action = '/adm/exm/examPaperAction.do';
  thisForm.target = '_self';
  thisForm.submit();
}


// 등록 여부 체크
function chkData() {

  var _startdt	= thisForm.p_startdt.value.replace(/-/g,"");
  var _enddt	= thisForm.p_enddt.value.replace(/-/g,"");

  if (_startdt == "") {
  	alert("시험 시작일을 입력하세요.");
  	return false;
  } else if (_enddt == "") {
  	alert("시험 종료일을 입력하세요.");
  	return false;
  } else if (_startdt*1 > _enddt*1) {
    alert("시험 시작일이 종료일 이후 입니다.");
  	return false;
  }

  var v_examtime = parseInt(thisForm.p_examtime.value);
  if (v_examtime < 1) {
    thisForm.p_examtime.focus();
    alert('시험시간을 입력해 주십시요.');
    return false;
  }
  
  if (thisForm.p_lesson.value.length < 1) {
    alert('과정의 차시가 등록되어 있지 않습니다.');
    return false;
  }  

  thisForm.p_startdt.value = _startdt;
  thisForm.p_enddt.value = _enddt;

  
  return true;
}
</script>