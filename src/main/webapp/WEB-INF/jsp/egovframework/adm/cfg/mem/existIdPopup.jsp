<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>

<!-- popup wrapper 팝업사이즈 650*370-->
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>I D중복확인</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<!-- contents -->
		<div class="tbDetail" style="padding-left:0px;">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="15%" />
				<col width="35%" />
				<col width="15%" />
				<col width="35%" />                    
				</colgroup>
				<tbody>
					<tr class="title">
						<th>
						<c:if test="${empty isOk}">
							<div class="popCon">
								<font color="red">${p_userid}</font>는(은) 이미 등록된 아이디입니다.
								<table summary="" width="100%" class="popTb">
									<colgroup>
										<col width="20%" />
										<col width="" />
									</colgroup>
									<tbody>
										<tr>
										</tr>
										<tr>
											<th scope="col">ID</th>
											<td class="bold">
												<input type="text" id="p_userid" name="p_userid" class="ipt" style="IME-MODE:disabled" size="30" onfocus="this.select()" onkeypress="fn_keyEvent('existID')"/>
											<c:out value="${isOk}"/>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</c:if>
						<c:if test="${not empty isOk}">
							<div class="popCon" align="center">
								<font color="red">${p_userid}</font>는(은)<br/> 사용할 수 있는 아이디입니다.
								<input type="hidden" id="p_userid" name="p_userid" value="${p_userid}"/>
							</div>
						</c:if>
						</th>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
		<c:if test="${empty isOk}">
			<li><a href="#" class="pop_btn01" onclick="existID()"><span>중복확인</span></a></li>
		</c:if>
		<c:if test="${not empty isOk}">
			<li><a href="#" class="pop_btn01" onclick="useId()"><span>사용하기</span></a></li>
		</c:if>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function existID(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.p_userid.value == "" ){
			alert("아이디를 입력하세요.");
			frm.p_userid.focus();
			return;
		}
		frm.action = "/adm/cfg/mem/existIdPopup.do";
		frm.target = "_self";
		frm.submit();
	}

	function useId(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		opener.document.getElementById("p_userid").value = frm.p_userid.value;
		opener.document.getElementById("p_userid_temp").value = frm.p_userid.value;
		window.close();
	}
</script>