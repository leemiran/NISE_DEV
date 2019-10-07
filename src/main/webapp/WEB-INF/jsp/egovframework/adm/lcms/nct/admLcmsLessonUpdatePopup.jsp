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
	<div class="popIn" style="height:265px">
    	<div class="tit_bg">
			<h2>Lesson 수정</h2>
		</div>
		<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">
		<input type="hidden" id="subj" 	 name="subj" 	value="${subj}"/>
		<input type="hidden" id="module" name="module" 	value="${module}"/>
		<input type="hidden" id="lesson" name="lesson" 	value="${lesson}"/>
		
		<!-- contents -->
		<div class="popCon">
			<table summary="" width="100%" class="popTb">
				<colgroup>
					<col width="20%" />
					<col width="" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="col">Lesson</th>
						<td class="bold">
							<c:out value="${lesson}"/>
						</td>
					</tr>
					<tr>
						<th scope="col">Lesson명</th>
						<td class="bold">
							<input type="text" id="lessonName" name="lessonName" class="ipt" style="width:98%;IME-MODE:active" value="<c:out value="${data.lessonName}"/>" onfocus="this.select()"/>
						</td>
					</tr>
					<tr>
						<th scope="col">시작위치</th>
						<td class="bold">
							<input type="text" id="starting" name="starting" class="ipt" style="width:98%;IME-MODE:disabled" value="<c:out value="${data.starting}"/>" onfocus="this.select()"/>
						</td>
					</tr>
					<tr <c:if test="${data.contentType != 'C'}">style="display:none"</c:if>>
						<th scope="col">페이지수</th>
						<td class="bold">
							<input type="text" name="pageCount" id="pageCount" class="ipt" value="${data.pageCount}" style="width:10%" onfocus="this.select()"/>
						</td>
					</tr>
					<tr <c:if test="${data.contentType != 'P'}">style="display:none"</c:if>>
						<th scope="col">교육시간</th>
						<td class="bold">
							<input type="text" name="eduTime" id="eduTime" class="ipt" value="${data.eduTime}" style="width:10%" onfocus="this.select()"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<li><a href="#" class="pop_btn01" onclick="javascript:doLessonUpdate()"><span>수정</span></a></li>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function doLessonUpdate(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.lessonName.value == "" ){
			alert("Lesson명을 입력하세요.");
			return;
		}
		frm.target = "_self";
		frm.action = "/adm/lcms/nct/lcmsLessonUpdate.do";
		frm.submit();
	}
</script>