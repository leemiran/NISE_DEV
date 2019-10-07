<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrStudyHead.jsp" %>
</head> 

<body>
<div id="mystudy"><!-- 팝업 사이즈 800*650 -->
	<div class="con2" style="height:690px;">
		<div class="popCon">
			<!-- header -->
			<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonTop.jsp" %>
			<!-- //header -->
			<!-- contents -->
			<div class="mycon">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyCommonBox.jsp" %>
				
				
				<h4 class="tit">과제</h4>
				<!-- search wrap-->
				<form name="<c:out value="${gsMainForm}"/>" method="post">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
				<input type="hidden" name="p_class" id="p_class" value="${p_class}">
				<input type="hidden" name="p_ordseq" id="p_ordseq" value="${p_ordseq}">
				</form>
				<!-- // search wrap -->
				<!-- list -->
				<div class="tbList1">
					<table summary="제목, 진행상황, 나의기준, 기간으로 구성" style="width:100%">
						<caption>과제</caption>
						<colgroup>
							<col width="" />
							<col width="100px" />
							<col width="90px" />
							<col width="150px" />
						</colgroup>
						<thead>
							<tr>
								<th scope="col">제목</th>
								<th scope="col">진행상황</th>
								<th scope="col">나의기준</th>
								<th scope="col">기간</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${report_list}" var="result">
							<tr>
								<td class="left">
									<a href="#none" onclick="goReportInsertPage('${result.class}', '${result.ordseq}');">${result.title}</a>
								</td>
								<td>
									<c:if test="${result.indata eq 'Y'}"><font color="orange">응시가능</font></c:if>
									<c:if test="${result.indata ne 'Y'}">-</c:if>
								</td>
								<td>
									<c:if test="${result.answerYn eq 'Y'}"><font color="blue">제출완료</font></c:if>
									<c:if test="${result.answerYn ne 'Y'}">미응시</c:if>
								</td>
								<td>${fn2:getFormatDate(result.startdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.expiredate, 'yyyy.MM.dd')}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<br/>
            
		  	</div>
			
			<!-- //contents -->
		</div>
	</div>
</div>
</body>
</html>
<script type="text/javascript">

	/* ********************************************************
	 * 과제
	 ******************************************************** */
	function goReportInsertPage(v_class, v_ordseq){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudyReportInsertPage.do";
		frm.p_class.value = v_class;
		frm.p_ordseq.value = v_ordseq;
		
		frm.target = "_self";
		frm.submit();
	}
</script>
