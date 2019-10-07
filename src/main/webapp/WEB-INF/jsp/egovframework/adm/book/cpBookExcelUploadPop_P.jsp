<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>



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
				<h2>택배사 송장번호 엑셀업로드 결과</h2>
	         </div>
			<!-- contents -->
			<div class="popCon">
				<table width="97%" border="0" cellspacing="0" cellpadding="0"
		style="margin-top: 3px;">
		<tr>
			<td height="36" background="/images/abl/titlebg.gif" class="title">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class='title'><img src='/images/abl/title1.gif' width='8'
						height='16' align='absmiddle'>교재배송관리</td>
					<td class='title_ss'>- 운송장번호 업로드 결과</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	
	<br>
	
	<table class="table_out" cellspacing="1" cellpadding="5">
		<tr>
			<td colspan="12" class="table_top_line"></td>
		</tr>
	</table>
	
	
	<!-- Detail information -->
	<table cellspacing="1" cellpadding="5">
	
		<tr>
	
			<td><br>
			<b>${result}</b></td>
	
		</tr>
		<tr>
			<td><input type="button" value="목록"
				onClick="history.back(-1)"><br>
			<br>
			</td>
		</tr>
	
	</table>
		</div>
		<!-- // contents -->
	</div>
</div>


</form>
<!-- // popup wrapper -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

var frm = document.<c:out value="${gsPopForm}"/>;

// 엑셀출력 - 운송장번호 업로드를 위한 엑셀다운
function excelDownForComp() {
	
	frm.action='/adm/book/excelDownForComp.do';
	frm.submit();
} 

function insert_check() {

	if(blankCheck(frm.p_file.value)){
		alert("DB로 가져갈 파일을 선택해 주세요");
		return;
	}

	if(frm.p_file.value.length > 0){
		var data = frm.p_file.value;
		data = data.toUpperCase(data);
		if(data.indexOf(".XLS") < 0){
			alert("DB로 입력되는 파일종류는 xls파일만 가능합니다.");
			return;
		}
	}

	frm.action = "/adm/book/insertExcelFileToDB.do";
	frm.submit();
}

function blankCheck( msg )
{
	var mleng = msg.length;
	chk=0;
	
	for (i=0; i<mleng; i++)
	{
		if ( msg.substring(i,i+1)!=' ' && msg.substring(i,i+1)!='\n' && msg.substring(i,i+1)!='\r') chk++;
	}
	if ( chk == 0 ) return (true);
	
	return (false);
}

</script>