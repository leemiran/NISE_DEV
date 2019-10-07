<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>

<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
<input type="hidden" name="s_subj" value="ALL"/>
<input type="hidden" name="s_grcode" value="ALL"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_seq"/>
<input type="hidden" name="p_subjnm"/>
<input type="hidden" name="p_userid"/>
<input type="hidden" name="p_sulpapernum" 	id="p_sulpapernum" 	value="${p_sulpapernum}" />
<!-- <input type="hidden" id="studyPopup" name="studyPopup"  value="Y" />
<input type="hidden" id="popupClose" name="popupClose"  value="Y" /> -->

     <div class="studyList">
			<table summary="번호, 교육구분, 과정, 교육기간, 설문지명, 진도율, 상태로 구분" cellspacing="0" width="100%">
                <caption>나의학습 목록</caption>
                <colgroup>
					<col width="5%" />
					<col width="10%" />
					<col width="30%" />					
					<col width="15%" /> 
                    <col width="20%" />
					<col width="10%" />					
					<col width="10%" />                   
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row">교육구분</th>
						<th scope="row">과정</th>
                        <th scope="row">교육기간</th>
						<th scope="row">설문지명</th>	
                        <th scope="row">진도율</th>					
						<th scope="row" class="last">상태</th>                        
					</tr>
				</thead>
				<tbody>					
                   <c:forEach items="${EducationSubjectList}" var="result" varStatus="status">
							<tr>
								<td class="num">${status.count }</td>
								<td>${result.isonoffvalue }</td>
								<td class="left">${result.subjnm }</td>	
								<td  class="num">
									${fn2:getFormatDate(result.aftersulsdate, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.aftersuledate, 'yyyy.MM.dd')}
								</td>
							
								<td class="left">
									<c:if test="${result.eachcnt > 0}">
									<a href="#none" onclick="alert('이미 응시하셨습니다.');">${result.sulpapernm}</a>
									</c:if>
									<c:if test="${result.eachcnt == 0}">
										<c:if test="${result.sultype == 1}">
											<c:if test="${result.progress <= result.tstep }">
												<a href="#none" onclick="goSurveyInsertPage('${result.subj}', '${result.year}', '${result.subjseq}', '${result.sulpapernum}', '${result.subjnm}', '${result.userid}');">${result.sulpapernm}</a>
											</c:if>
											<c:if test="${result.progress > result.tstep }">
												<a href="javascript:alert('설문응시 제한진도율보다 진도율이 부족합니다.');">${result.sulpapernm}</a>
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
								
								<td class="num">${result.progress} / ${result.tstep}진도율</td>
								<td class="last">
									<c:if test="${result.eachcnt > 0}"><font color="blue">응시</font></c:if>
									<c:if test="${result.eachcnt == 0}">미응시</c:if>
								</td>
							</tr>
						</c:forEach>                             	
				</tbody>
			</table>
  		</div>        
	<!-- list table-->
</form>
<script type="text/javascript">
//<![CDATA[
/* ********************************************************
 * 과제
 ******************************************************** */
function goSurveyInsertPage(v_subj, v_year, v_subjseq, v_sulpapernum ,v_subjnm ,v_userid){
	var frm = eval('document.<c:out value="${gsMainForm}"/>');

	frm.action = "/usr/stu/std/userStudySurveyInsertPage.do?studyPopup=N";

	frm.p_subj.value = v_subj;
	frm.p_year.value = v_year;
	frm.p_subjseq.value = v_subjseq;
	frm.p_sulpapernum.value = v_sulpapernum;
	frm.p_subjnm.value = v_subjnm;
	frm.p_userid.value = v_userid;
	frm.target = "_self";
	frm.submit();
}
//]]>
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->