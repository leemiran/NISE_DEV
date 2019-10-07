<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script language="javascript1.2">
<!--
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
	opener.doPageList();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:480px">
    	<div class="tit_bg">
			<h2>sample download</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="20%" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">sample</th>
						<td class="bold">
							<select id="sample">
								<option value="C">중공교</option>
								<option value="P">동영상</option>
								<option value="N">Nonscorm</option>
							</select>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doDownload()"><span>다운로드</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function doModuleUpdate(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.moduleName.value == "" ){
			alert("Module명을 입력하세요.");
			return;
		}
		frm.target = "_self";
		frm.action = "/adm/lcms/nct/lcmsModuleUpdate.do";
		frm.submit();
	}

	function doDownload() {
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
	 	var link = "";
	 	var sample = frm.sample.value;

	 	switch(sample){
			case 'C':
				link = '/com/fileDownload.do?fileName=lcmsCotiSample.xls';
				break;

			case 'P':
				link = '/com/fileDownload.do?fileName=lcmsPlayerSample.xls';
				break;

			default :
				link = '/com/fileDownload.do?fileName=lcmsNonscormSample.xls';
		}
	 	
		window.location.href = link;
		return;
	}
</script>