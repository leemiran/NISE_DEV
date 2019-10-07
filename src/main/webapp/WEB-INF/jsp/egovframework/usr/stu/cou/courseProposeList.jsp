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
	<!-- tab -->     
	<div class="conwrap2">
		<ul class="mtab2">
			<li><a href="#" onclick="changeTab('Study')">학습중인과정</a></li>
			<li><a href="#" onclick="changeTab('Propose')" class="on">수강신청한과정</a></li>
			<li class="end"><a href="#" onclick="changeTab('Stold')">복습기간/교육후기</a></li>                
		</ul>
	</div>
	<!-- //tab -->       
	<!-- search wrap-->
	<div class="stduyWrap">
		<ul class="floatL">
			<li>수강신청한 과정 내역입니다.</li>
		</ul>
	</div>
	<!-- // search wrap -->	
	<!-- list table-->
	<div class="studyList">
		<table summary="과정명, 신청기간, 연수기간, 승인여부로 구성" cellspacing="0" width="100%">
			<caption>수강신청한 과정</caption>
            <colgroup>
				<col  />
				<col width="20%" />					
				<col width="20%" />					
				<col width="8%" />					
			</colgroup>
			<thead>
				<tr>
					<th scope="row">과정명</th>
					<th scope="row">신청기간</th>
					<th scope="row">연수기간</th>
					<th scope="row" class="last">승인여부</th>						
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${list}" var="result">
				<tr>
<!--					<td class="left">${result.isonoffvalue}</td>						-->
					<td class="left">
						<a href="#" onclick="whenSubjInfoViewPopup('${result.subj}');" title="새창">
							<c:if test="${result.course ne '000000'}"><span style="color:#3366cc;">[패키지:${result.coursenm}]</span></c:if>${result.subjnm}
						</a>
					</td>
					<td>${fn2:getFormatDate(result.propstart, 'yy.MM.dd')} ~ ${fn2:getFormatDate(result.propend, 'yy.MM.dd')}</td>						
					<td>${fn2:getFormatDate(result.edustart, 'yy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yy.MM.dd')}</td>						
					<td class="num last">
						<c:choose>
							<c:when test="${result.chkfinal eq 'Y'}"><img src="/images/user/btn_approval.gif" alt="승인"/></c:when>
							<c:when test="${result.chkfinal eq 'N'}"><img src="/images/user/btn_return.gif" alt="반려"/></c:when>
							<c:when test="${result.chkfinal eq 'B'}">
									<img src="/images/user/ico_status.gif" alt="대기중"/>
<!--								<a href="#" onclick="doCancel('${result.subj}', '${result.year}', '${result.subjseq}')">-->
<!--									<img src="/images/user/btn_cancel.gif"/>-->
<!--								</a>-->
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>						
				</tr>					
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="4" class="num last">조회하신 자료가 없습니다.</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	<!-- list table-->
       
       
</form>
<script type="text/javascript">
//<![CDATA[
	function changeTab(type){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/usr/stu/cou/course"+type+"List.do";
		frm.target = "_self";
		frm.submit();
	}

	function doCancel(subj, year, subjseq ){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		alert("수강신청취소 작업중..");
		return;
		frm.action = "/usr/stu/cou/coursePropseCancel.do";
		frm.p_subj.value = subj;
		frm.p_year.value = year;
		frm.p_subjseq.value = subjseq;
		frm.target = "_self";
		frm.submit();

	}
//]]>	
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->