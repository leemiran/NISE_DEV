<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmTop.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/userLoginCheck.jsp" %>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">


<!-- 라인맵-->
	<div class="location">
		<span class="home"><a>HOME</a></span>
		<span><a>나의학습방</a></span>
	</div>

	<!-- caution -->
	<p class="sbg caution">나의 학습방에서는 신청한 모바일 과정을 수강하실 수 있습니다.
		<span class="tsky">3G환경</span>에서는 <span class="tsky">접속이 지연</span>되거나 화면이 끊어질 수 있으니 보다 원할한 학습을 위해 
		<span class="tsky">Wi-Fi</span> 환경에서 학습해 주시기 바랍니다 .
	</p>

	<!-- 본문 -->
	<div id="wcontainer">
	
	
		<!-- 목록 -->
		<ul class="imgList mrt5">
		<c:forEach items="${list}" var="result">
			<li>
				<h3 class="bul"><strong>[모바일]</strong> ${result.subjnm}</h3>
				<div class="listimg2">
					<img src="/images/mbl/temp/sample.jpg" alt="" />
				</div>
				<ul class="listText2">
					<li><span class="tdark">교육기간 : </span>${fn2:getFormatDate(result.studystart, 'yyyy-MM-dd')} ~ ${fn2:getFormatDate(result.studyend, 'yyyy-MM-dd')}</li>
					<li class="graphli">
					<span class="tdark">전체진도율 : </span>
					<c:if test="${result.lcmstype eq 'OLD' && result.mobile eq 'Y'}">
						<span class="toran">${result.tstep}%</span> 
						<div class="graph"><div class="graph_in" style="width:${result.tstep}%"></div></div>
					</c:if>
					<c:if test="${result.lcmstype ne 'OLD' || result.mobile ne 'Y'}">
						<span class="toran">${result.myprogress}%</span> 
						<div class="graph"><div class="graph_in" style="width:${result.myprogress}%"></div></div>
					</c:if>
					</li>
					<li>
					<c:url var="selectBoardArticleUrl" value="/mbl/std/studyItemList.do">
		              <c:param name="recordCountPerPage" value="${recordCountPerPage}" />
		              <c:param name="firstIndex" value="${firstIndex}" />
		              <c:param name="p_subj" value="${result.subj}" />
		              <c:param name="p_year" value="${result.year}" />
		              <c:param name="p_subjseq" value="${result.subjseq}" />
		              <c:param name="p_contenttype" value="${result.contenttype}" />
		              <c:param name="p_lcmstype" value="${result.lcmstype}" />
		            </c:url>
					<a href="${selectBoardArticleUrl}" class="btn_blu"><span>학습하기</span></a>
					</li>
				</ul>
			</li>
		</c:forEach>	
		<c:if test="${empty list}">
			<li>
				<h3 class="bul">모바일 과정이 존재하지 않습니다.</h3>
			</li>
		</c:if>
		</ul>
		


		<!-- //목록 -->
		<!-- 더보기 -->
		<div class="more"></div>
		<!-- 맨위로 -->
		<div class="totop"><a href="#wrap">맨위로</a></div>
		
	</div>
	<!-- //본문 -->




</form>
</div>
</body>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>