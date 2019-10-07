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
	<div class="popIn">
    	<div class="tit_bg">
			<h2>Lesson<c:out value="${lesson == '' ? '추가' : '수정'}"/></h2>
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
							<c:out value="${lesson_info.module}"/> - <c:out value="${lesson_info.modulenm}"/>
							<input type="hidden" name="module" id="module" value="<c:out value="${module}"/>"/>
							<input type="hidden" name="subj" id="subj" value="${subj}"/>
						</td>
					</tr>
					<tr>
						<th scope="col">Lesson</th>
						<td class="bold">
							<input type="hidden" name="lesson" id="lesson" class="ipt" value="${lesson_info.lesson}"/>
							<c:out value="${lesson_info.lesson}"/>
						</td>
					</tr>
					<tr>
						<th scope="col">Lesson명</th>
						<td class="bold">
							<input type="text" name="lessonName" id="lessonName" class="ipt" value="${lesson_info.lessonnm}" style="width:95%" onfocus="this.select()"/>
						</td>
					</tr>
					<tr>
						<th scope="col">Lesson 시간(초)</th>
						<td class="bold">
						<!-- 	<input type="text" name="lesson_minute" id="lesson_minute" class="ipt" value="${lesson_info.lessonMinute}" size="10" onKeyUp="return numeric_chk(this)"/>분 --> 
							<input type="text" name="lesson_time" id="lesson_time" class="ipt" value="${lesson_info.lessonTime}" size="10" onKeyUp="return numeric_chk(this)"/>초
						</td>
					</tr>
					<tr>
						<th scope="col">시작위치</th>
						<td class="bold">
							<input type="text" name="starting" id="starting" class="ipt" value="${lesson_info.starting}" style="width:95%" onfocus="this.select()"/>
						</td>
					</tr>
					
					<tr>
						<th scope="col">모바일시간(초)</th>
						<td class="bold">
							Chapter 시작 : <input type="text" name="m_start" id="m_start" class="ipt" value="${lesson_info.mStart}" size="10" onKeyUp="return numeric_chk(this)"/>(초) ~ 
							Chapter 종료 : <input type="text" name="m_end" id="m_end" class="ipt" value="${lesson_info.mEnd}" size="10" onKeyUp="return numeric_chk(this)"/>(초)
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
		<!-- // contents -->
		</form>
		<!-- button -->
		<ul class="btnCen">
			<c:choose>
				<c:when test="${empty lesson}">
					<li><a href="#" class="pop_btn01" onclick="javascript:doLessonInsert()"><span>등록</span></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="#" class="pop_btn01" onclick="javascript:doLessonUpdate()"><span>수정</span></a></li>
				</c:otherwise>
			</c:choose>
			<li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫기</span></a></li>
		</ul>
		<!-- // button -->
	</div>
</div>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>
<script type="text/javascript">

	function doLessonInsert(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( !this.doLessonCheck() ) return;
		
		frm.target = "_self";
		frm.action = "/adm/lcms/old/lcmsLessonInsert.do";
		frm.submit();
	}

	function doLessonUpdate(){
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( !this.doLessonCheck() ) return;
		
		frm.target = "_self";
		frm.action = "/adm/lcms/old/lcmsLessonUpdate.do";
		frm.submit();
	}

	function doLessonCheck(){
		var returnType = true;
		var frm = eval('document.<c:out value="${gsPopForm}"/>');
		if( frm.lesson.value == "" ){
			alert("Lesson을 입력하세요.");
			returnType = false;
		}else if( frm.lessonName.value == "" ){
			alert("Lesson명을 입력하세요.");
			returnType = false;
		}else if( frm.starting.value == "" ){
			alert("시작위치를 입력하세요.");
			returnType = false;
		}
		return returnType;
	}

</script>