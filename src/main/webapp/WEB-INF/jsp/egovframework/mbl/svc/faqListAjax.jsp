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
		
		<c:forEach items="${list}" var="result" varStatus="i">
			<li onclick="#">
				<dl class="blist">
					<dt>
					<c:url var="selectBoardArticleUrl" value="/mbl/svc/faqView.do">
		              <c:param name="recordCountPerPage" value="${recordCountPerPage}" />
		              <c:param name="firstIndex" value="${firstIndex}" />
		              <c:param name="p_faqcategory" value="${result.faqcategory}" />
		              <c:param name="p_fnum" value="${result.fnum}" />
		            </c:url>
		            
					<a href="${selectBoardArticleUrl}">
					${result.title}
					</a>
					</dt>
<!--					<dd><span class="tit">작성자</span> ${result.luserid}</dd>-->
					<%-- <dd><span class="tit">작성일</span> ${fn2:getFormatDate(result.indate, 'yyyy.MM.dd')}</dd> --%>
				</dl>
			</li>
		</c:forEach>
			
		
<input id="firstIndex" name="firstIndex" type="hidden" value="<c:out value='${firstIndex}'/>"/>
<input id="recordCountPerPage" name="recordCountPerPage" type="hidden" value="<c:out value='${recordCountPerPage+15}'/>"/>

			
		</ul>
		<!-- //목록 -->
		<!-- 더보기 -->
		<div class="more"><a href="#" onclick="CtlExecutor.requestAjaxSubj();return false;"  class="morebtn"><span>15개 더보기 (1-${fn:length(list)} / ${totCnt})</span></a></div>
		<!-- 맨위로 -->
		<div class="totop"><a href="#wrap">맨위로</a></div>
		

