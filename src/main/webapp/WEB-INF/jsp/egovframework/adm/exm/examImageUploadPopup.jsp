<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script language="javascript1.2">
<!--
<c:if test="${!empty img_path}">
	opener.doSetImageTag("${img_path}", "${size_w}", "${size_h}", '${imgNo}', '${imgSelNo}');
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" style="height:265px">
    	<div class="tit_bg">
			<h2>문제이미지등록</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post" enctype="multipart/form-data">
		<input type="hidden" name="imgNo" value="${imgNo}">
		<input type="hidden" name="imgSelNo" value="${imgSelNo}">
		<!-- contents -->
		<div class="popCon" style="width:95%;">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="20%" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">이미지파일</th>
						<td class="bold">
							<input type="file" id="img_file" name="img_file"/>
						</td>
					</tr>
					<tr>
						<th scope="col">이미지크기</th>
						<td class="bold">
							가로 : <input type="text" id="size_w" name="size_w" value="600" class="ipt" style="width:20px;IME-MODE:disabled" maxlength="3" onfocus="this.select()"/>
							&nbsp;&nbsp;
							세로 : <input type="text" id="size_h" name="size_h" value="300" class="ipt" style="width:20px;IME-MODE:disabled" maxlength="3" onfocus="this.select()"/>
						</td>
					</tr>
				</tbody>
			</table>
			※파일종류는 jpg, gif, png 파일만 가능합니다.
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doImageUpload()"><span>등록</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function doImageUpload(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if(frm.img_file.value == ""){
			alert("이미지파일을 선택하세요.");
			return;
		}else{
			var data = frm.img_file.value;
	        data = data.toUpperCase(data);
	        if ( !(data.lastIndexOf(".JPG") > 0 || data.lastIndexOf(".GIF") > 0 || data.lastIndexOf(".PNG") > 0) ) {
	            alert("파일종류는 jpg, gif, png 파일만 가능합니다.");
	            return;
	        }
			
		}
		if(frm.size_w.value * 0 != 0){
			alert("이미지 크기를 입력하세요.");
			frm.size_w.focus();
			return;
		}
		if(frm.size_h.value * 0 != 0){
			alert("이미지 크기를 입력하세요.");
			frm.size_h.focus();
			return;
		}
		frm.action = "/adm/exm/examImageUploadAction.do";
		frm.submit();
		
	}
</script>