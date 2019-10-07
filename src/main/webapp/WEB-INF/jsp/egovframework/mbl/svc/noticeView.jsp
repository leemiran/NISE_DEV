<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmTop.jsp" %>


	<!-- 라인맵-->
	<div class="location">
		<span class="home"><a>HOME</a></span>
		<span><a>연수안내</a></span>
		<span><a>공지사항</a></span>
	</div>

	<!-- 본문 -->
	<div id="wcontainer">
		<!-- 탭 -->
		<%@ include file = "./tabMenu.jsp" %>
		
		<!-- 상세보기 -->
		<div class="bview mrt20">
			<h3 class="tit_board">
			<c:if test="${view.noticeGubun eq 'A'}"><img src="/images/adm/ico/ico_notice.gif" alt="공지" /></c:if>
			<c:if test="${view.noticeGubun eq 'B'}"><img src="/images/adm/ico/ico_event.gif" alt="이벤트" /></c:if>
			<c:if test="${view.noticeGubun eq 'C'}"><img src="/images/adm/ico/ico_happy.gif" alt="축하" /></c:if>
			<c:if test="${view.noticeGubun eq 'D'}"><img src="/images/adm/ico/ico_guide.gif" alt="안내" /></c:if>
			<c:if test="${view.noticeGubun eq 'E'}"><img src="/images/adm/ico/ico_poll.gif" alt="설문" /></c:if>
			<c:if test="${view.noticeGubun eq 'F'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
			<c:if test="${view.noticeGubun eq 'G'}"><img src="/images/adm/ico/ico_others.gif" alt="기타" /></c:if>
			
			${view.adtitle}
			</h3>
			<table summary="작성자,작성일,조회수,첨부,내용으로 구성" cellspacing="0" width="100%" class="mrt10">
				<caption>${view.adtitle}</caption>
				<colgroup>
						<col width="15%" />
						<col width="18%" />
						<col width="15%" />
						<col width="" />
						<col width="15%" />
						<col width="15%" />
				</colgroup>
				<tr>
					<th scope="row">작성자</th>
					<td>${view.adname}</td>
					<th scope="row">작성일</th>
					<td>${fn2:getFormatDate(view.addate, 'yyyy.MM.dd')}</td>
					<th scope="row">조회수</th>
					<td>${view.cnt}</td>
				</tr>
				<tr>
					<th scope="row">첨부</th>
					<td colspan="5">
						<c:forEach items="${fileList}" var="result" varStatus="i">
                    		<a href="#none" onclick="javascript:fn_download('${result.realfile}', '${result.savefile}', 'bulletin')">
                    		<img src="/images/user/icon_file.gif" alt="첨부파일"/>
                    		<c:out value="${empty result.realfile ? result.savefile : result.realfile}"/>
                    		</a> <br/>        	
                    	</c:forEach>
					</td>
				</tr>
				<tr>
					<td colspan="6" class="view">
					
					<c:out value="${view.adcontent}" escapeXml="false"/>
					
					</td>
				</tr>
			</table>
		</div>
		<!-- //상세보기 -->

		<!-- button -->
		<ul class="btnR">
			<c:url var="selectBoardArticleUrl" value="/mbl/svc/noticeList.do">
              <c:param name="recordCountPerPage" value="${recordCountPerPage}" />
              <c:param name="firstIndex" value="${firstIndex}" />
              <c:param name="p_tabseq" value="${p_tabseq}" />
              <c:param name="p_seq" value="${result.seq}" />
            </c:url>
			<li><a href="${selectBoardArticleUrl}" class="btn02"><span>목록</span></a></li>
		</ul>
		
	</div>
	<!-- //본문 -->
</div>
</body>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>
