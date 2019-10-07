<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>
<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
self.close();
alert("${resultMsg}");
opener.location.reload();
</c:if>
-->
</script>



<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
	<input type="hidden" id="p_subj"  		name="p_subj"    value="${p_subj}">
	<input type="hidden" id="p_year"  		name="p_year"    value="${p_year}">
	<input type="hidden" id="p_subjseq"  	name="p_subjseq"    value="${p_subjseq}">
	<input type="hidden" id="p_userid"  		name="p_userid"    value="${p_userid}">
	<input type="hidden" id="p_orderid"  		name="p_orderid"    value="${p_orderid}">
	
	
	
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>수강신청 결제방법 변경</h2>
         </div>
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="98%" class="popTb">
				<colgroup>
					<col width="25%" />
					<col width="75%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">결제방법</th>
						<td>
						
						<input type="radio" name="p_type" id="p_type" value="SC0010" checked/>신용카드
						<input type="radio" name="p_type" id="p_type" value="SC0030" />계좌이체
						</td>
					</tr>
					<tr>
						<th scope="col">주문번호</th>
						<td>
							<input type="text" name="p_neworderid" id="p_neworderid" size="30" value="" />
							 ※ 숫자로입력하세요!
						</td>
					</tr>
					<tr>
						<th scope="col">할부개월</th>
						<td>
							<select  id="p_card_period" name="p_card_period">
								<option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
							</select> 개월
						</td>
					</tr>
					<tr>
						<th scope="col">카드/은행명</th>
						<td>
							<input type="text" name="p_card_nm" id="p_card_nm" size="30"  value="" />
						</td>
					</tr>
					<tr>
						<th scope="col">카드번호</th>
						<td>
							<input type="text" name="p_card_no" id="p_card_no" size="30"  value="" />
							 ※ ex)1234********1234
						</td>
					</tr>
					
					<tr>
						<th scope="col">승인번호</th>
						<td>
							<input type="text" name="p_auth_no" id="p_auth_no" value="" />
							 ※ 숫자로입력하세요!
						</td>
					</tr>
					<tr>
						<th scope="col">승인/이체일자</th>
						<td>
							<input type="text" name="p_auth_date" id="p_auth_date" value="" />
							 ※ ex)2013-05-07, 11:57:33
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#none" onClick="whenPaySave()" class="pop_btn01"><span>저 장</span></a></li>
			<li><a href="#none" onClick="window.close()" class="pop_btn01"><span>닫 기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>


</form>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var frm = document.<c:out value="${gsPopForm}"/>;

//저장
function whenPaySave() {
	if(frm.p_neworderid.value == "")
	{
		alert("주문번호를 입력하세요!");
		return
	}

	if(frm.p_card_nm.value == "")
	{
		alert("카드/은행명을 입력하세요!");
		return
	}

	if(frm.p_card_no.value == "" && frm.p_type[0].checked)
	{
		alert("카드번호를 입력하세요!");
		return
	}

	if(frm.p_auth_no.value == "" && frm.p_type[0].checked)
	{
		alert("승인번호를 입력하세요!");
		return
	}


	if(frm.p_auth_date.value == "")
	{
		alert("승인/은행일자를 입력하세요!");
		return
	}

	if(confirm("결제방법을 변경하시겠습니까? 방법변경시 다시 변경이 불가합니다."))
	{
		frm.action = "/adm/prop/studentPayTypeAction.do";
		frm.target = "_self";
		frm.submit();
	}
}



</script>