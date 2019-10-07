<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrStudyHead.jsp" %>
</head> 
<body>
<form name="<c:out value="${gsMainForm}"/>" method="post">
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
<input type="hidden" name="subj" 		id="subj"				value="${p_subj}"/>
<input type="hidden" name="year" 		id="year"				value="${p_year}"/>
<input type="hidden" name="subjseq" 	id="subjseq"			value="${p_subjseq}"/>
<input type="hidden" name="p_studyType" id="p_studyType"		value="${p_studyType}"/>
<input type="hidden" name="contentType" id="contentType"		value="${p_contenttype}"/>
<input type="hidden" name="orgSeq" 		id="orgSeq"/>
<input type="hidden" name="itemId" 		id="itemId"/>
<input type="hidden" name="module" 		id="module"/>
<input type="hidden" name="lesson" 		id="lesson"/> 
<input type="hidden" name="rsrcSeq" 	id="rsrcSeq"/>
<input type="hidden" name="suryoLessonTime" 	id="suryoLessonTime" value=""/>
</form>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2" style="height:690px; overflow: auto; ">
		<div class="popCon">
			<!-- header -->
			<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonTop.jsp" %>
			<!-- //header -->
			<!-- contents -->
			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				<h4 class="tit">학습시작(목록)</h4>
				
				<!-- list -->
				<div style="height:470px; overflow: auto;">
				<c:if test="${p_studyType eq 'NEW'}">
					<c:if test="${p_contenttype eq 'S'}">
						<%@ include file = "/WEB-INF/jsp/egovframework/usr/stu/std/newScormItemList.jsp" %>
					</c:if>
					<c:if test="${p_contenttype eq 'X'}">
						<%@ include file = "/WEB-INF/jsp/egovframework/usr/stu/std/newXiniceItemList.jsp" %>
					</c:if>
					<c:if test="${p_contenttype ne 'S' and p_contenttype ne 'X'}">
						<%@ include file = "/WEB-INF/jsp/egovframework/usr/stu/std/newNonScormItemList.jsp" %>
					</c:if>
				</c:if>
				<c:if test="${p_studyType eq 'OLD'}">
					<%@ include file = "/WEB-INF/jsp/egovframework/usr/stu/std/oldItemList.jsp" %>
				</c:if>
            	</div>
		  	</div>
			<!-- //contents -->
		</div>
	</div>
</div>
</body>
</html>

<script type="text/javascript">

	/* ********************************************************
	 * 조회
	 ******************************************************** */
	function doPageReload(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyItemList.do";
		frm.target = "_self";
		frm.submit();
	}

	function doStudyPopup(orgSeq, itemId, rsrcSeq, lTime){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');

		if( '<c:out value="${p_mobileCheck}"/>' == 'Y' ){
			alert("모바일과정 학습은 모바일을 통해 학습하시기 바랍니다.");
			return;
		}
		
		frm.orgSeq.value = orgSeq;
		frm.itemId.value = itemId;
		frm.module.value = orgSeq;
		frm.lesson.value = itemId;
		frm.rsrcSeq.value = rsrcSeq;
		frm.suryoLessonTime.value = lTime;
		//alert(orgSeq);
    	window.open('', 'study', 'top=100px, left=100px, height=815px, width=1324px, scrollbars=yes, resizable=yes');
    	frm.action = "/com/lcms/len/learning.do";
		frm.target = "study"; 
		frm.submit();
	}
</script>
