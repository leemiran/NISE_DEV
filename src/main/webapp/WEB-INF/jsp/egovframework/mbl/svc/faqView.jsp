<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmTop.jsp" %>


	<!-- 라인맵-->
	<div class="location">
		<span class="home"><a>HOME</a></span>
		<span><a>연수안내</a></span>
		<span><a>자주하는 질문</a></span>
	</div>

	<!-- 본문 -->
	<div id="wcontainer">
		<!-- 탭 -->
		<%@ include file = "./tabMenu.jsp" %>
		
		<!-- 상세보기 -->
		<div class="bview mrt20">
			<h3 class="tit_board">
			${view.title}
			</h3>
			<table summary="작성일,첨부,내용으로 구성" cellspacing="0" width="100%" class="mrt10">
				<caption>${view.title}</caption>
				<colgroup>
						<col width="15%" />
						<col width="38%" />
						<col width="15%" />
						<col width="35%" />
				</colgroup>
				<tr>
<!--					<th scope="row">작성자</th>-->
<!--					<td>${view.luserid}</td>-->
					<th scope="row">작성일</th>
					<td colspan="5">${fn2:getFormatDate(view.indate, 'yyyy.MM.dd')}</td>
				</tr>
				<tr>
					<th scope="row">첨부</th>
					<td colspan="5">
								<a href="#none" onclick="javascript:fn_download('${view.realFile}', '${view.saveFile}', 'faq_upload')">
	                    			<c:out value="${empty view.realFile ? view.saveFile : view.realFile}"/>
	                    		</a>
					</td>
				</tr>
				<tr>
					<td colspan="6" class="view">
					
					<c:out value="${view.contents}" escapeXml="false"/>
					
					</td>
				</tr>
			</table>
		</div>
		<!-- //상세보기 -->

		<!-- button -->
		<ul class="btnR">
			<c:url var="selectBoardArticleUrl" value="/mbl/svc/faqList.do">
              <c:param name="recordCountPerPage" value="${recordCountPerPage}" />
              <c:param name="firstIndex" value="${firstIndex}" />
            </c:url>
			<li><a href="${selectBoardArticleUrl}" class="btn02"><span>목록</span></a></li>
		</ul>
		
	</div>
	<!-- //본문 -->
</div>
</body>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>
