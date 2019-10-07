<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" >
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
    <input type = "hidden" name="p_process"  value = "" />
    <input type = "hidden" name="p_seq"  value = "${view.seq }" />
	<!-- detail wrap -->
    <div id=idPrint_1>
	<table width="650" border="1" cellspacing="0" cellpadding="0">   
	<tr> 
    	<td align="center"><img src="/images/user/receipt_top.jpg"></td>
  	</tr>
  	<tr> 
    	<td background="/images/cert/bg_line.gif" style="padding: 0px 6px 0 6px;">
    		<table width="620" border="0" align="center" cellpadding="0" cellspacing="0" class="txtList" summary="성명, 소속학교, 주민등록번호, 과정명, 연수종별, 금액, 수강신청일로 구성">
            <caption>영수내역</caption>
            <colgroup>
					<col width="30%" />
					<col width="70%" />					                   
			</colgroup>
        		<tr> 
          			<td height="20" colspan="2" class="left last" bgcolor="#eaeaea"><strong>영수내역</strong></td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70" style="text-align:left;">소&nbsp;속&nbsp;기&nbsp;관 : </th>
          			<td class="left last"><input type="text" name="p_org_name" id="p_org_name" value="${view.orgName }" /></td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70" style="text-align:left;">성&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명 : </th>
          			<td class="left last">
          				<input type="text" name="p_uname" id="p_uname" value="${view.uname }" size="80"/>
          			</td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70" style="text-align:left;">과&nbsp;&nbsp;&nbsp;정 &nbsp;&nbsp;&nbsp;명 : </th>
          			<td class="left last"><input type="text" name="p_subj_nm" id="p_subj_nm" value="${view.subjNm }" size="80"/></td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70" style="text-align:left;">연&nbsp;수&nbsp;종&nbsp;별 : </th>
          			<td class="left last">
          				<input type="text" name="p_yonsu_name" id="p_yonsu_name" value="${view.yonsuName }" />
          			</td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70" style="text-align:left;">금&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;액 : </th>
          			<td class="left last"><input type="text" name="p_amount" id="p_amount" value="${view.amount }"/>원(면세) (숫자만 넣으세요.) </td>
        		</tr>
        		<tr> 
          			<th scope="col" width="70" style="text-align:left;">입&nbsp;&nbsp;&nbsp;금&nbsp;&nbsp;&nbsp;일 : </th>
          			<td class="left last"><input type="text" name="p_amount_date" id="p_amount_date" value="${view.amountDate }" maxlength="8"/>(예: 20150412)</td>
        		</tr>
      		</table> 
      	</td>
  	</tr>
  	<tr> 
    	<td>
    		<table width="300" border="0" cellspacing="0" cellpadding="0" align="center">
        		<tr> 
          			<td style="padding:10px;">
          				<strong>이  영수증은 소득공제용 현금영수증의 효력이 없습니다.<br/>
            			우리원은 국가기관으로 법인세법 72조의 2에 의거 현금영수증 가맹점 가입 범위에 제외됩니다.</strong>
            		</td>
        		</tr>
      		</table>
      	</td>
  	</tr>
  	<tr> 
    	<td align="center"><img src="/images/user/receipt_bottom.jpg"/></td>
  	</tr>
    <tr>
        <td height="4"></td>
    </tr>    
</table>

</div><br />
    <!-- // search wrap -->
    <center>
		<a href="#" class="btn02" onclick="whenCashPrintSave('update')"><span>수정</span></a>
		<a href="#" class="btn02" onclick="whenCashPrintSave('delete')"><span>삭제</span></a>
	</center>
	
    <!-- // detail wrap -->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">
	var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

	function whenCashPrintSave(mode) {

		if(thisForm.p_org_name.value == ''){
			alert("소속기관을 입력하세요.");
			thisForm.p_org_name.focus();
			return false;
		}

		if(thisForm.p_uname.value == ''){
			alert("연수대상자를 입력하세요.");
			thisForm.p_uname.focus();
			return false;
		}

		if(thisForm.p_subj_nm.value == ''){
			alert("과정명을 입력하세요.");
			thisForm.p_subj_nm.focus();
			return false;
		}

		if(thisForm.p_yonsu_name.value == ''){
			alert("연수종별을 입력하세요.");
			thisForm.p_yonsu_name.focus();
			return false;
		}

		if(thisForm.p_amount.value == ''){
			alert("금액을 입력하세요.");
			thisForm.p_amount.focus();
			return false;
		}

		if(thisForm.p_amount_date.value == ''){
			alert("입금일을 입력하세요.");
			thisForm.p_amount_date.focus();
			return false;
		}

		var str = "";
		if(mode == 'update'){
			str = "수정";
		}else{
			str = "삭제";
		}

		if(confirm(str+"하시겠습니까?")){
	        thisForm.action = "/adm/cash/cashPrintAction.do";
	        thisForm.p_process.value = mode;
	        thisForm.submit();
		}
	}
</script>