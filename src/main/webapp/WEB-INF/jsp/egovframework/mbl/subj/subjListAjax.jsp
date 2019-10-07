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



		<!-- 목록 -->
		<ul class="imgList mrt5">
		
		<c:forEach items="${list}" var="result" varStatus="i">
			<li>
				<div class="listimg">
					<img src="/images/mbl/temp/sample.jpg" alt="" />
				</div>
				<ul class="listText">
					<c:url var="selectBoardArticleUrl" value="/mbl/subj/subjView.do">
		              <c:param name="recordCountPerPage" value="${recordCountPerPage}" />
		              <c:param name="firstIndex" value="${firstIndex}" />
		              <c:param name="search_upperclass" value="${search_upperclass}" />
		              <c:param name="search_text" value="${search_text}" />
		              <c:param name="p_subj" value="${result.subj}" />
		            </c:url>
		            
					<li>
					<a href="${selectBoardArticleUrl}">
					<h3>
					<strong>[${result.upperclassnm}]</strong>
					${result.subjnm}
					</h3>
					</a>
					</li>
					<li><span class="tdark">신청기간 : </span>${fn2:getFormatDate(result.propstart, 'yyyy.MM.dd')}~${fn2:getFormatDate(result.propend, 'yyyy.MM.dd')}</li>
					<li><span class="tdark">교육기간 : </span>${fn2:getFormatDate(result.edustart, 'yyyy.MM.dd')}~${fn2:getFormatDate(result.eduend, 'yyyy.MM.dd')}</li>
                    <li>
                    			<c:choose>
									<c:when test="${result.empGubun eq 'P' && fn:indexOf(result.subj, 'PAR') != -1}">
									<a class="btn01"><span>신청불가</span></a>
									</c:when>
									<c:otherwise>
										<c:if test="${result.propstatus eq 'A'}">
	                                    	<c:if test="${result.isPropose > 0}">
	                                    		<a class="btn01"><span>신청중</span></a>
	                                    	</c:if>
	                                    	<c:if test="${result.isPropose <= 0}">
	                                    		 <a class="btn_blu"><span>신청가능</span></a>
	                                    	</c:if>
	                                    </c:if>
	                                    <c:if test="${result.propstatus eq 'B'}">
	                                    	 <a class="btn_blu"><span>신청전</span></a>
	                                    </c:if>
	                                    <c:if test="${result.propstatus eq 'C'}">
	                                    	<!-- <a class="btn01"><span>신청마감</span></a> -->
	                                    </c:if>
	                                    <c:if test="${result.propstatus eq 'D'}">
	                                    	<a class="btn01"><span>신청불가</span></a>
	                                    </c:if>	
									</c:otherwise>
								</c:choose>
                    </li>
				</ul>
			</li>
		</c:forEach>
			
		
<input id="firstIndex" name="firstIndex" type="hidden" value="<c:out value='${firstIndex}'/>"/>
<input id="recordCountPerPage" name="recordCountPerPage" type="hidden" value="<c:out value='${recordCountPerPage+15}'/>"/>

			
		</ul>
		<!-- //목록 -->
		<!-- 더보기 -->
		<div class="more"><a href="javascript:CtlExecutor.requestAjaxSubj()"  class="morebtn"><span>15개 더보기 (1-${fn:length(list)} / ${totCnt})</span></a></div>
		<!-- 맨위로 -->
		<div class="totop"><a href="#wrap">맨위로</a></div>
		

