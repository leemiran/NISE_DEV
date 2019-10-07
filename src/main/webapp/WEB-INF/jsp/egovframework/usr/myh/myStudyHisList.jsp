<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
<!--login check-->
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>



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


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<fieldset>
<legend>나의 교육이력</legend>
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_userid"   value = "${sessionScope.userid}" />
	<input type = "hidden" name="p_subj" />
	<input type = "hidden" name="p_year" />
	<input type = "hidden" name="p_subjseq" />
	<input type = "hidden" name="p_scoreYn" id="p_scoreYn" value="N" />
	<input type="hidden" name="p_upperclass"/>
	<input type="hidden" name="p_schoolparent" id="p_schoolparent">
	





<!-- list table-->
		<div class="studyList">
			<table summary="교육구분, 과정명, 수강료, 연수기간, 복습기간, 성적보기, 이수증으로 구성" cellspacing="0" width="100%">
				<caption>나의 교육이력</caption>
                <colgroup>
					<col width="6%" />
					<col width="6%" />
					<col width="*" />
					<col width="7%" />
                    <col width="10%" />
					<col width="10%" />	
                    <col width="8%" />
                    <col width="12%" />
                    <col width="8%" />
                    <col width="5%" />	
                    <col width="5%" />									
				</colgroup>
				<thead>
					<tr>
						<th scope="col">연수<br>구분</th>
                    	<th scope="col">운영<br>형태</th>
						<th scope="col">과정명</th>
                        <th scope="col">수강료</th>
                        <th scope="col">연수기간</th>
                        <th scope="col">복습기간</th>
                        <th scope="col">연수<br>진행상태</th>
                        <th scope="col">연수<br>지명번호</th>
                        <th scope="col">성적보기</th>
                        <th scope="col">이수증</th>
                        <th scope="col" class="last">성적</br>보기</th>
					</tr>
				</thead>
				<tbody>
				
<c:forEach items="${list}" var="result" varStatus="status">				
					<tr>
						<td>
						<c:if test="${result.upperclass eq 'PRF'}"><img src="/images/user/upperclass_01.gif" alt="교원직무연수"/></c:if>
						<c:if test="${result.upperclass eq 'EXT'}"><img src="/images/user/upperclass_02.gif" alt="보조인력연수"/></c:if>
						<c:if test="${result.upperclass eq 'PAR'}"><img src="/images/user/upperclass_03.gif" alt="교양연수"/></c:if>
						<c:if test="${result.upperclass eq 'OTH'}"><img src="/images/user/upperclass_04.gif" alt="시범연수"/></c:if>
						<c:if test="${result.upperclass eq 'SCP'}"><img src="/images/user/upperclass_05.gif" alt="학부모연수"/></c:if>
	                    </td>
	                    <td>
	                     <c:if test="${result.ischarge eq 'C'}"><img src="/images/user/ischarge_01.png" alt="정규"/></c:if>
	                     <c:if test="${result.ischarge eq 'S'}"><img src="/images/user/ischarge_02.png" alt="특별"/></c:if>
	                     <c:if test="${result.ischarge eq 'F'}"><img src="/images/user/ischarge_03.png" alt="무료"/></c:if>
	                    </td>		
						<td class="left">
						<%-- <a href="#none" onclick="whenSubjInfoViewPopup('${result.subj}', '')"  title="새창"> --%>
						${result.subjnm}
						<!-- </a> -->
						</td>
                        <td class="num">
                        <c:if test="${result.biyong eq '0'}">무료</c:if>
                        <c:if test="${result.biyong ne '0'}"><fmt:formatNumber value="${result.biyong}" type="number"/>원</c:if></td>
                        <td class="num">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
                        <td class="num">
<!--                        ${result.eduend}/-->
<!--                        ${fn2:getFormatDateNow('yyyyMMddHH')}-->
                        <c:choose>
                        	<c:when test="${result.isreview eq 'Y'}">
                        	${fn2:getFormatDate(result.studystart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.studyend, 'yyyy.MM.dd')}
                        	</c:when>
                        	
                        	<c:otherwise>
                        	-
                        	</c:otherwise>
                        	
                        </c:choose>
                        </td>
                         <td class="num">
<!--                        ${result.eduend}/-->
<!--                        ${fn2:getFormatDateNow('yyyyMMddHH')}-->
                        
                        <c:choose>
                        	<c:when test="${empty result.isgraduated && (result.edustart > fn2:getFormatDateNow('yyyyMMddHH'))}">
                        	수강전
                        	</c:when>
                        	
                        	<c:when test="${empty result.isgraduated && (result.eduend >= fn2:getFormatDateNow('yyyyMMddHH'))}">
                        	수강중
                        	</c:when>
                        	
                        	<c:when test="${empty result.isgraduated && (result.eduend < fn2:getFormatDateNow('yyyyMMddHH'))}">
                        	수료처리중
                        	</c:when>
                        	<c:otherwise>
                        	${result.isgraduatedValue}
                        	</c:otherwise>
                        	
                        </c:choose>
                        </td>
                        <td>${result.lecselno }</td>
                        <td>
                        	<c:if test="${result.year >= 2010}">
                        	<a href="#none" onclick="fn_statusPopup('${result.subj}', '${result.year}', '${result.subjseq}', '${result.userid}');" title="새창">
                        		<img src="/images/user/btn_study_view.gif" alt="성적보기" />
                        	</a>
                        	</c:if>
                        	<c:if test="${result.year < 2010}">
                        	-
                        	</c:if>
                        </td>
                        <td class="last">
                        <c:choose>
	                        <c:when test="${result.isgraduated eq 'Y'}">
	                        	<c:choose>
	                        		<c:when test="${result.suroyprint eq 'Y'}">
	                        			<a href="javascript:suRoyJeung('${result.curYear}','${result.subj}', '${result.year}', '${result.subjseq}', '${result.lecselno}', '${result.upperclass }', '${result.schoolparent}')"><img src="/images/user/icon_print.gif" alt="수료증" /></a>
	                        		</c:when>
	                        		<c:otherwise>	                        		
	                        			수료처리중.    		
	                        		</c:otherwise>
	                        	</c:choose>
	                        </c:when>
	                        <c:otherwise>
	                        	-
	                        </c:otherwise>
                        </c:choose>
                        </td>
                         <td class="last num">
                        	<input type="checkbox" name="p_check" id="p_check" value="${status.count }" <c:if test="${result.edutimes <= '30'}">disabled</c:if>>
                        </td>                        
					</tr>	
</c:forEach>


<!--과거이수내역-->
<c:forEach items="${oldList}" var="result" varStatus="status">				
					<tr>
						<td>${result.gubun}</td>						
						<td class="left">${result.couname}</td>
                        <td class="num">-</td>
                        <td class="num">${result.stDate} ~ ${result.edDate}</span></td>
                        <td class="num">교육완료</td>
                        <td>
                        
                        <c:choose>
	                        <c:when test="${result.score ne '미이수' && result.score ne '포기'}">
	                        	${result.score}점
	                        </c:when>
	                        <c:otherwise>
	                        	${result.score}
	                        </c:otherwise>
                        </c:choose>
                        </td>
                        <td class="last">
                        <c:choose>
	                        <c:when test="${result.score ne '미이수' && result.score ne '포기'}">
	                        	<a href="javascript:oldSuRoyJeung('${result.seq}')"><img src="/images/user/icon_print.gif" alt="수료증" /></a>
	                        </c:when>
	                        <c:otherwise>
	                        	-
	                        </c:otherwise>
                        </c:choose>
                        </td>                        
					</tr>	
</c:forEach>

					<c:if test="${empty list && empty oldList}">
					<tr>
						<td colspan="8">조회하신 자료가 없습니다.</td>
					</tr>
					</c:if>
					
				</tbody>
			</table>
		</div>
		<!-- list table-->
</fieldset>
</form>



        
        
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

//수료증출력
function suRoyJeung(p_cur_year, p_subj, p_year, p_subjseq,p_lecselno,p_upperclass, p_schoolparent){
	
	
	if(p_upperclass == 'PRF' && p_year >= '2015') {	
		
		if(p_lecselno == 'null'){
			alert('연수지명번호 미입력되셨습니다.\n연수지명번호 입력시 이수증 출력 가능합니다.');
			return;
		}
		if(p_lecselno == ''){
			alert('연수지명번호 미입력되셨습니다.\n연수지명번호 입력시 이수증 출력 가능합니다.');
			return;
		}
	
	}
	
	$('input:checkbox[name="p_check"]').each(function(){
		if(this.checked){
			if(this.value == cnt){
				$("#p_scoreYn").val("Y");
				return false;
			}
		}else{
			$("#p_scoreYn").val("N");
		}
	});
	
	window.open('', 'suRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
	thisForm.p_subj.value = p_subj;
	thisForm.p_year.value = p_year;
	thisForm.p_subjseq.value = p_subjseq;
	thisForm.p_upperclass.value = p_upperclass;
	thisForm.p_schoolparent.value = p_schoolparent;
	
	thisForm.action = "/usr/myh/suRoyJeungPrint.do";
	thisForm.target = "suRoyJeungPop";
	thisForm.submit();
}

//수료증출력
function oldSuRoyJeung(p_seq){
	window.open('', 'oldSuRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
	thisForm.action = "/usr/myh/oldSuRoyJeungPrint.do?p_seq=" + p_seq;
	thisForm.target = "oldSuRoyJeungPop";
	thisForm.submit();
}
//-->
</script>
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
