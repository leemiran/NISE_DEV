<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>



<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" enctype="multipart/form-data">
	<input type="hidden" id="p_grcode"  	name="p_grcode"    value="${sessionScope.grcode}">
	<input type="hidden" id="p_subj"  		name="p_subj"    value="${p_subj}">
	<input type="hidden" id="p_year"  		name="p_year"    value="${p_year}">
	<input type="hidden" id="p_subjseq"  	name="p_subjseq"    value="${p_subjseq}">
	<input type="hidden" id="p_userid"  		name="p_userid"    value="${p_userid}">
	
	
	
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>택배사 송장번호 엑셀업로드</h2>
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
						<th scope="col">과정명</th>
						<td>[<c:out value="${infoMap.subj}"/>]<c:out value="${infoMap.subjnm}"/> - <c:out value="${infoMap.numberSubjseq}"/></td>
					</tr>
					<tr>
						<th scope="col">학습기간</th>
						<td><c:out value="${infoMap.edustart}"/> ~ <c:out value="${infoMap.eduend}"/></td>
					</tr>
				</tbody>
			</table>
			<table width="97%" border="0" cellspacing="0" cellpadding="0">
			<tr> 
          		<td align="right"> 
					<table width="97%" border="0" cellspacing="0" cellpadding="0">
						<tr> 
					    	<td align="right"> 
					      		<input name="p_file" type="FILE" class="input" size=100>
					 			<a href="javascript:insert_check()"><img src="/images/adm/button/btn_add.gif"  border="0" align="absmiddle"></a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			</table>
			<!--  사이 간격   -->
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td height="10px"></td>
				</tr>
			</table>
			<!--  사이 간격   -->	
			<br>
	      	<!-- s :  주의사항  -->
	      	<table width="97%" border="0" cellspacing="0" cellpadding="0">
		        <tr>
		          <td class="dir_txt" align="center" ><b><font size="3">(주의사항 - 필독)</font></b></td>
		        </tr>
		        <tr>
		          <td height="8"></td>
		        </tr>
		        <tr>
		          <td height="20">참고 :  <a href="javascript:excelDownForComp()" class="b"><font color="#E60873">[택배사코드 다운로드]</font></a></td>
		        </tr>		        
		        <tr>
		          <td height="20">1. <font color="#E60873">다운받은 엑셀파일의 택배사코드, 운송장번호 내용을 입력합니다.</font></td>
		        </tr>
		        <tr>
		          <td height="20">2. <font color="#E60873">기존에 입력된 운송장번호가 있으면 업로드된 데이터로 수정됩니다.</font></td>
		        </tr>
		        <tr>
		          <td height="20">3. <font color="#E60873">샘플 양식 폼을 변경할 시 치명적인 오류가 발생할 수 있습니다.</font></td>
		        </tr>
			</table>
	      	<!-- e :  주의사항  -->
	      	<br>
	      	<!-- 예제파일   -->
			<table width="97%" border="0" cellspacing="0" cellpadding="0">
		        <tr>
		          <td align='left'>다운받은 엑셀파일 예시</td>
		        </tr>
		        <tr>
		          <td><img src="/images/book_upload.gif" border="0"></td>
		        </tr>
			</table>
			<!-- 예제파일  -->
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

$(window).bind('unload', function(){
	opener.doPageList();
	
});

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