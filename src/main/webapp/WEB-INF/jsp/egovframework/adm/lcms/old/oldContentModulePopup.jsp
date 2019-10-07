<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<script language="javascript1.2">
<!--
<c:if test="${!empty finish}">
	opener.doPageReload();
	window.close();
</c:if>
-->
</script>
<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn" >
    	<div class="tit_bg">
			<h2>Module<c:out value="${module == '' ? '추가' : '수정'}"/></h2>
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
						<th scope="col">Module</th>
						<td class="bold">
							<c:out value="${empty module_info ? module_key : module_info.module}"/>
							<input type="hidden" name="module" id="module" value="<c:out value="${empty module_info ? module_key : module_info.module}"/>"/>
							<input type="hidden" name="subj" id="subj" value="${subj}"/>
						</td>
					</tr>
					<tr>
						<th scope="col">Module명</th>
						<td class="bold">
							<input type="text" name="moduleName" id="moduleName" class="ipt" value="${module_info.sdesc}" style="width:95%" onkeypress="javascript:doKeyEvent()"/>
						</td>
					</tr>
<!--					<tr>-->
<!--						<th scope="col">웹 전체시간<br>(분/초)</th>-->
<!--						<td class="bold">-->
<!--							<input type="text" name="" id="" class="ipt" size="10" value="25">분 / -->
<!--							<input type="text" name="" id="" class="ipt" size="10" value="30">초 -->
<!--						</td>-->
<!--					</tr>-->
					<tr>
						<th scope="col">Mobile URL</th>
						<td class="bold">
							<input type="text" name="mobile_url" id="mobile_url" class="ipt" value="${module_info.mobileUrl}" style="width:95%" onkeypress="javascript:doKeyEvent()"/>
						</td>
					</tr>
<!--					<tr>-->
<!--						<th scope="col">Mobile 전체시간<br>(분/초)</th>-->
<!--						<td class="bold">-->
<!--							<input type="text" name="" id="" class="ipt" size="10" value="25">분 / -->
<!--							<input type="text" name="" id="" class="ipt" size="10" value="30">초 -->
<!--						</td>-->
<!--					</tr>-->
					
					
			
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<c:choose>
				<c:when test="${empty module_info}">
					<li><a href="#" class="pop_btn01" onclick="javascript:doModuleInsert()"><span>등록</span></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="#" class="pop_btn01" onclick="javascript:doModuleUpdate()"><span>수정</span></a></li>
				</c:otherwise>
			</c:choose>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function doModuleInsert(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.moduleName.value == "" ){
			alert("Module명을 입력하세요.");
			return;
		}
		frm.target = "_self";
		frm.action = "/adm/lcms/old/lcmsModuleInsert.do";
		frm.submit();
	}

	function doModuleUpdate(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.moduleName.value == "" ){
			alert("Module명을 입력하세요.");
			return;
		}
		frm.target = "_self";
		frm.action = "/adm/lcms/old/lcmsModuleUpdate.do";
		frm.submit();
	}

	function doKeyEvent(){
		if( event.keyCode == 13 ){
			if(<c:out value="${empty module_info}"/>){
				this.doModuleInsert();
			}else{
				this.doModuleUpdate();
			}
		}
	}
</script>