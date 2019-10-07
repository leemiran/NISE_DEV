<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmTop.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/userLoginCheck.jsp" %>

	<!-- 라인맵-->
	<div class="location">
		<span class="home"><a>HOME</a></span>
		<span><a>이수현황조회</a></span>
	</div>
	
	<!-- 본문 -->
	<div id="wcontainer">
		<!-- 교육과정 상세 -->
		
<c:forEach items="${list}" var="result" varStatus="i">
		<div class="yun mrt15">
			<h3 class="tline mrb10">${result.subjnm}</h3>
			<div class="bview">
				<table summary="교육구분,연수구분,학습기간,성적,이수여부로 구분" cellspacing="0" width="100%">
					<caption>이수현황목록</caption>
					<colgroup>
							<col width="20%" />
							<col width="" />
							<col width="20%" />
							<col width="25%" />
					</colgroup>			
					<tr>
						<th scope="col">교육구분</th>
						<td>${result.codenm}</td>
						<th scope="col">연수구분</th>
						<td>${result.upperclassnm}</td>
					</tr>
					<tr>
						<th scope="col">연수기간</th>
						<td colspan="3">${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')}~${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</td>
					</tr>
					<tr>
						<th scope="col">성적</th>
						<td><strong class="toran">
						<c:if test="${result.edutimes >= 60 and fn:substring(p_subj, 0, 3) eq 'PAR'}">
						<c:out value="${result.editscore}" />
						</c:if>
						<c:if test="${fn:substring(p_subj, 0, 3) ne 'PAR'}">
						<c:out value="${result.score}" />
						</c:if>
						점
						</strong></td>
						<th scope="col">이수여부</th>
						<td><strong class="toran">
								<c:if test="${result.isgraduated eq 'Y'}">수료</c:if>
								<c:if test="${result.isgraduated eq 'N'}">미수료</c:if>
								<c:if test="${empty result.isgraduated}">수료처리중</c:if>
						</strong></td>
					</tr>
				</table>
			</div>
		</div>
</c:forEach>
		
		
		<!-- //교육과정 상세 -->

	



		
		<!-- 더보기 -->
		<div class="more"></div>
		<!-- 맨위로 -->
		<div class="totop"><a href="#wrap">맨위로</a></div>
		
	</div>
	<!-- //본문 -->
</div>
</body>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>
