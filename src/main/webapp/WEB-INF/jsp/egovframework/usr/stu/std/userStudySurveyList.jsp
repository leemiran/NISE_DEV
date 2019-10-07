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
				
				
				<h4 class="tit">설문</h4>
				<!-- search wrap-->
				<form name="<c:out value="${gsMainForm}"/>" method="post">
				<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userStudyHidden.jsp" %>
				<input type="hidden" name="s_subj" 			id="s_subj"  		value="ALL"/>
				<input type="hidden" name="s_grcode" 		id="s_grcode" 		value="ALL"/>
				<input type="hidden" name="p_sulpapernum" 	id="p_sulpapernum" 	value="${p_sulpapernum}" />
				<input type="hidden" name="pageType" 		id="pageType" 		value="pop"/>
				<input type="hidden" name="p_subjnm"/>
				<input type="hidden" name="p_userid"/>
				</form>
				<!-- // search wrap -->
				<!-- list -->
				<div class="tbList1">
					<table summary="제목, 진행상황, 나의기준, 기간으로 구성" style="width:100%">
						<caption>설문목록</caption>
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
						<c:forEach items="${EducationSubjectList}" var="result">
							<tr>
								<td class="left">
									<c:if test="${result.eachcnt > 0}">
									<a href="#none" onclick="alert('이미 응시하셨습니다.');">${result.sulpapernm}</a>
									</c:if>
									<c:if test="${result.eachcnt == 0}">
										<c:if test="${result.sultype == 1}">
											<c:if test="${result.progress <= result.tstep }">
												<c:if test="${result.indata ne 'Y'}">
													<a href="#none" onclick="alert('설문 응시기간이 아닙니다.');">${result.sulpapernm}</a>
												</c:if>
												<c:if test="${result.indata eq 'Y'}">
													<a href="#none" onclick="javascript:goSurveyInsertPage('${result.subj}', '${result.year}', '${result.subjseq}', '${result.sulpapernum}', '${result.subjnm}', '${result.userid}');">${result.sulpapernm}</a>
												</c:if>
											</c:if>
											<c:if test="${result.progress > result.tstep }">
												<a href="alert('설문응시 제한진도율보다 진도율이 부족합니다.');">${result.sulpapernm}</a>
											</c:if>
										</c:if>
										<c:if test="${result.sultype != 1}">
											<c:if test="${result.indata eq 'Y'}">
												<a href="#none" onclick="goSurveyInsertPage('${result.subj}', '${result.year}', '${result.subjseq}', '${result.sulpapernum}', '${result.subjnm}', '${result.userid}');">${result.sulpapernm}</a>
											</c:if>
											<c:if test="${result.indata ne 'Y'}">
												<a href="#none" onclick="alert('설문 응시기간이 아닙니다.');">${result.sulpapernm}</a>
											</c:if>
										</c:if>
									</c:if>
								</td>
								<td>
									<c:if test="${result.indata eq 'Y'}">
										<c:if test="${result.eachcnt > 0}"><font color="blue">응시함</font></c:if>
										<c:if test="${result.eachcnt == 0 }"><font color="orange">응시가능</font></c:if>										
										</c:if>
									<c:if test="${result.indata ne 'Y'}">-</c:if>
								</td>
								<td>
									<c:if test="${result.eachcnt > 0}"><font color="blue">응시(제출)</font></c:if>
									<c:if test="${result.eachcnt == 0}">미응시</c:if>
								</td>
								<td>${fn2:getFormatDate(result.aftersulsdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.aftersuledate, 'yyyy.MM.dd')}</td>
							</tr>
						</c:forEach>
						<tr>
							<td colspan="4"></td>
						</tr>
						</tbody>
					</table>
				</div>
				<br/>
            
		  	</div>
			
			<!-- //contents -->
		</div>
	</div>
</div>
<script type="text/javascript">
<!--
	/* ********************************************************
	 * 설문
	 ******************************************************** */
	function goSurveyInsertPage(v_subj, v_year, v_subjseq, v_sulpapernum ,v_subjnm, v_userid){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/std/userStudySurveyInsertPage.do?pageType=pop";
		frm.p_subj.value = v_subj;
		frm.p_year.value = v_year;
		frm.p_subjseq.value = v_subjseq;
		frm.p_sulpapernum.value = v_sulpapernum;
		frm.p_subjnm.value = v_subjnm;
		frm.p_userid.value = v_userid;
		frm.target = "_self";
		frm.submit();
	}
-->
</script>
</body>
</html>

