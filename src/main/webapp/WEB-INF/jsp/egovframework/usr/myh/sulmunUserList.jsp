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
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_userid"     value = "${sessionScope.userid}" />
	<input type = "hidden" name="p_subj" />
	<input type = "hidden" name="p_year" />
	<input type = "hidden" name="p_subjseq" />



		<div class="sub_title">
			<ul>
            	<li>설문지명을 클릭하세요!</li>                
            </ul>
		</div>

<!-- list table-->
		<div class="studyList">
			<table summary="번호, 교육구분, 과정, 교육기간, 설문지명, 진도율, 상태로 구분" cellspacing="0" width="100%">
                <caption>설문목록</caption>
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
						<th scope="row">번호</th>
						<th scope="row">교육구분</th>
						<th scope="row">과정</th>
                        <th scope="row">교육기간</th>
						<th scope="row">설문지명</th>	
                        <th scope="row">진도율</th>					
						<th scope="row" class="last">상태</th>                        
					</tr>
				</thead>
				<tbody>		
<c:forEach items="${list}" var="result" varStatus="status">							
                    <tr>
						<td class="num">${status.count}</td>						
						<td>${result.isonoffvalue}</td>
                        <td class="left">${result.subjnm}</td>
                        <td class="num">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>						
						<td>${result.sulpapernm}</td>
                        <td class="num">${result.tstep} / ${result.progress}</td>
						<td class="last">
						<c:choose>
							<c:when test="${result.sultype eq '1'}">
								<c:if test="${result.eachcnt > 0}">완료</c:if>
								<c:if test="${result.eachcnt <= 0}">응시가능</c:if>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${result.eachcnt > 0}">완료</c:when>
									<c:when test="${fn2:getFormatDateNow('yyyyMMddhh24') <= result.edate}">응시가능</c:when>
									<c:otherwise>기간종료</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						
						</td>                        
					</tr>    
</c:forEach>					                               	
				</tbody>
			</table>
  		</div>        
		<!-- list table-->   
				





</form>



        
        
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');





//-->
</script>
        
        
        
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
