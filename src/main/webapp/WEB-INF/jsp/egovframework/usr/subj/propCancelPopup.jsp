<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script language="javascript1.2">
<!--
<c:if test="${not empty resultMsg}">
	window.onload = function () {
	alert("${resultMsg}");
	opener.document.location.reload();
	window.close();
}
</c:if>
-->
</script>



<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
					<input type="hidden" name="p_reasoncd" id="p_reasoncd" value="01" />
					<input type="hidden" name="p_reason" id="p_reason" />
					<input type="hidden" name="p_process" id="p_process" />	
					<input type="hidden" name="p_subj" value="${p_subj}" />
					<input type="hidden" name="p_year" value="${p_year}" />
					<input type="hidden" name="p_subjseq" value="${p_subjseq}" />
					<input type="hidden"  name="p_order_id" value="${p_order_id}">
							
							
							
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>취소사유를 선택하세요</h2>
         </div>
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="80%" />
					<col width="20%" />
				</colgroup>
				<tbody>
					<tr>
						<td colspan="2">
						<ui:code id="p_cancelcd" selectItem="01" gubun="" codetype="0096"  upper="" title="" itemRowCount="1" className="" type="radio" selectTitle="취소사유" event="chkEtc" />
						</td>
					</tr>
					<tr>
						<td>
						<textarea name="p_cancelnm" id="p_canclenm" rows="5" cols="60" readOnly onclick="javascript:alertEtc()" title="취소사유"></textarea>
						</td>
						<td valign="bottom">
							<a href="#none" onclick="saveReason()" class="pop_btn01"><span>취소확인</span></a>
						</td>
					</tr>
									
				</tbody>
			</table>
		</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#none" onClick="window.close()" class="pop_btn01"><span>닫 기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
</form>




<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">
var frm = eval('document.<c:out value="${gsPopForm}"/>');


	// 취소사유 저장 후 취소 처리
	function saveReason(){
		/*
		if(chkSelected() < 1){
			alert('취소사유를 선택해 주세요.');
			return;
		}*/

		if(frm.p_cancelcd[3].checked == true) {
			frm.p_reason.value=frm.p_cancelnm.value;
			
			if(blankCheck(frm.p_reason.value)) {	
				alert("취소사유를 작성해 주세요.");
				//document.getElementById('p_canclenm').focus();
				return;
			}
			if(realsize(frm.p_reason.value) > 100) {	
				alert("취소사유는 100자 이하로 작성해주세요.");
				//document.getElementById('p_canclenm').focus();				
				return;
			}
		}
			
		frm.action='/usr/subj/propCancelAction.do';
		frm.target = "_self";
		frm.submit();
	}

	
	// 기타 항목 선택 시에만 textarea 활성화 시키기
	function chkEtc(obj) {
		var value = obj.value;
		if( value == 99) {
			document.getElementById('p_canclenm').value="";
			document.getElementById('p_canclenm').readOnly=false;
			document.getElementById('p_canclenm').focus();
		} else {
			document.getElementById('p_canclenm').value="";		
			document.getElementById('p_canclenm').readOnly=true;
		}		
		frm.p_reasoncd.value=value;
	}
	
	// 기타 항목 선택 시에만 textarea값 입력 가능하게 처리
	function alertEtc() {
		if(document.getElementById('p_canclenm').readOnly) {
			alert("취소사유를 기타로 선택한 경우에만 작성해 주세요.");
			return;
		}
	}
	
</script>