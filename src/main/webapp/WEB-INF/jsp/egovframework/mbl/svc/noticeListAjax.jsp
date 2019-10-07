<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" 	 uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" 		 uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" 	 uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" 	 uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" 		 uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" 	 uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>


		<%@ include file = "./tabMenu.jsp" %>
		
		
		<!-- 목록 -->
		<ul class="imgList mrt5">
		
		<c:forEach items="${allList}" var="result" varStatus="i">
			<li onclick="#">
				<dl class="blist">
					<dt>
					<a href="#none" onclick="doView('${result.seq}')">
					<c:if test="${result.noticeGubun eq 'A'}">[공지]</c:if>
					<c:if test="${result.noticeGubun eq 'B'}">[이벤트]</c:if>
					<c:if test="${result.noticeGubun eq 'C'}">[축하]</c:if>
					<c:if test="${result.noticeGubun eq 'D'}">[안내]</c:if>
					<c:if test="${result.noticeGubun eq 'E'}">[설문]</c:if>
					<c:if test="${result.noticeGubun eq 'F'}">[긴급]</c:if>
					<c:if test="${result.noticeGubun eq 'G'}">[기타]</c:if>
					${result.adtitle}
					</a>
					</dt>
					<dd><span class="tit">작성자</span> ${result.adname}</dd>
					<dd><span class="tit">작성일</span> ${fn2:getFormatDate(view.addate, 'yyyy.MM.dd')}</dd>
					<dd><span class="tit">조회수</span> ${result.cnt}</dd>
				</dl>
			</li>
		</c:forEach>
		
		<c:forEach items="${list}" var="result" varStatus="i">
			<li onclick="#">
				<dl class="blist">
					<dt>
					<c:url var="selectBoardArticleUrl" value="/mbl/svc/noticeView.do">
		              <c:param name="recordCountPerPage" value="${recordCountPerPage}" />
		              <c:param name="firstIndex" value="${firstIndex}" />
		              <c:param name="p_tabseq" value="${p_tabseq}" />
		              <c:param name="p_seq" value="${result.seq}" />
		            </c:url>
		            
					<a href="${selectBoardArticleUrl}">
					<c:if test="${result.noticeGubun eq 'A'}">[공지]</c:if>
					<c:if test="${result.noticeGubun eq 'B'}">[이벤트]</c:if>
					<c:if test="${result.noticeGubun eq 'C'}">[축하]</c:if>
					<c:if test="${result.noticeGubun eq 'D'}">[안내]</c:if>
					<c:if test="${result.noticeGubun eq 'E'}">[설문]</c:if>
					<c:if test="${result.noticeGubun eq 'F'}">[긴급]</c:if>
					<c:if test="${result.noticeGubun eq 'G'}">[기타]</c:if>
					${result.adtitle}
					</a>
					</dt>
					<dd><span class="tit">작성자</span> ${result.adname}</dd>
					<dd><span class="tit">작성일</span> ${result.addate}</dd>
					<dd><span class="tit">조회수</span> ${result.cnt}</dd>
				</dl>
			</li>
		</c:forEach>
		</ul>	
		
        <input id="firstIndex" name="firstIndex" type="hidden" value="<c:out value='${firstIndex}'/>"/>
        <input id="recordCountPerPage" name="recordCountPerPage" type="hidden" value="<c:out value='${recordCountPerPage+15}'/>"/>
		
		<!-- //목록 -->
		<!-- 더보기 -->
		<div class="more"><a href="#" onclick="CtlExecutor.requestAjaxSubj();return false;"  class="morebtn"><span>15개 더보기 (1-${fn:length(list)} / ${totCnt})</span></a></div>
		<!-- 맨위로 -->
		<div class="totop"><a href="#wrap">맨위로</a></div>
		

