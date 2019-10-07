<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">

	<input type="hidden" name="p_subj"      value="${p_subj}">
	<input type="hidden" name="p_year"      value="${p_year}">
	<input type="hidden" name="p_subjseq"   value="${p_subjseq}">
	<input type="hidden" name="p_deptNm"   value="${p_deptNm}">

	
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>교육청수납일자</h2>
         </div>
		<!-- contents -->
		<div class="popCon MR20">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="40%" />
                    <col width="60%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">교육청수납일자</th>						
						<td>
                        	<span>
								<input type="text" value="${view.depositDate}" size="10" id="ipdate" name="ipdate" maxlength="10" readonly/>
								<a href="#none" onclick="popUpCalendar(this, document.all.ipdate, 'yyyy-mm-dd')"><img src="../../images/adm/ico/ico_calendar.gif" alt="달력" /></a>								
                          </span>
                        </td>
					</tr>
									
				</tbody>
			</table>
</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
      		<c:if test="${view.isclosed eq 'Y'}">
      		<li><a href="#none" onclick="alert('수료처리되어 수정할 수 없습니다.');" class="pop_btn01"><span>저장</span></a></li>
			</c:if>
			<c:if test="${view.isclosed ne 'Y'}">
      		<li><a href="#none" onclick="whenSubmit()" class="pop_btn01"><span>저장</span></a></li>
      		</c:if>
			<li><a href="#none" onclick="window.close()" class="pop_btn01"><span>닫기</span></a></li>            
		</ul>
		<!-- // button -->
	</div>
</div>
</form>


<script type="text/javascript">

var frm = document.<c:out value="${gsPopForm}"/>;

//입력값 체크
function weightChk(){
	var sum = 0;
	ff = frm;
	
	if (ff.ipdate.value=="") {
		alert("교육청수납일자를 입력하세요.");
		return false;
	}	
	return true;
}


//저장
function whenSubmit(){
	var result;	
	result = weightChk();
	if(!result){
		//alert('다시 입력하여 주십시오.');
		return;
	}
	frm.action='/adm/cou/subjReceipMomeyObIpdateAction.do';	       
	frm.target = "_self";
	frm.submit();
}

</script>

<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>

