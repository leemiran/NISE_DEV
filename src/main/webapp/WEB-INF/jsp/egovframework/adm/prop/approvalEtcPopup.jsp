<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>


<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");window.close();</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
	<input type="hidden" id="p_grcode"  	name="p_grcode"    value="${sessionScope.grcode}">
	<input type="hidden" id="p_subj"  		name="p_subj"    value="${p_subj}">
	<input type="hidden" id="p_year"  		name="p_year"    value="${p_year}">
	<input type="hidden" id="p_subjseq"  	name="p_subjseq"    value="${p_subjseq}">
	<input type="hidden" id="p_userid"  		name="p_userid"    value="${p_userid}">
	
	
	
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>수강신청 비고관리</h2>
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
						<th scope="col">비고</th>
						<td>
						<Textarea name="p_etc" cols="6" rows="17" onkeyup="fc_chk_byte(this,450);" style="width:95%;"><c:out value="${view.etc}" escapeXml="false"/></Textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#none" onClick="whenEtcSave()" class="pop_btn01"><span>저 장</span></a></li>
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
function whenEtcSave() {
	frm.action = "/adm/prop/approvalEtcAction.do";
	frm.target = "_self";
	frm.submit();
}


//textarea 글자 제한하기
function fc_chk_byte(aro_name,ari_max)
{

   var ls_str     = aro_name.value; // 이벤트가 일어난 컨트롤의 value 값
   var li_str_len = ls_str.length;  // 전체길이

   // 변수초기화
   var li_max      = ari_max; // 제한할 글자수 크기
   var i           = 0;  // for문에 사용
   var li_byte     = 0;  // 한글일경우는 2 그밗에는 1을 더함
   var li_len      = 0;  // substring하기 위해서 사용
   var ls_one_char = ""; // 한글자씩 검사한다
   var ls_str2     = ""; // 글자수를 초과하면 제한할수 글자전까지만 보여준다.

   for(i=0; i< li_str_len; i++)
   {
      // 한글자추출
      ls_one_char = ls_str.charAt(i);

      // 한글이면 2를 더한다.
      if (escape(ls_one_char).length > 4)
      {
         li_byte += 2;
      }
      // 그밗의 경우는 1을 더한다.
      else
      {
         li_byte++;
      }

      // 전체 크기가 li_max를 넘지않으면
      if(li_byte <= li_max)
      {
         li_len = i + 1;
      }
   }
   
   // 전체길이를 초과하면
   if(li_byte > li_max)
   {
      alert(" 글자를 초과 입력할수 없습니다. \n 초과된 내용은 자동으로 삭제 됩니다. ");
      ls_str2 = ls_str.substr(0, li_len);
      aro_name.value = ls_str2;
      
   }
   aro_name.focus();   
}



</script>