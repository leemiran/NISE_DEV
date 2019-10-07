<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
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

<c:forEach items="${noticeList}" var="result" varStatus="i">
	<li onclick="#">
		<dl class="blist">
			<dt>
			<a href="#none" onclick="getSubjNoticeView('${result.seq}','${result.subj}','${result.year}','${result.subjseq}')" title="새창">
				<c:if test="${result.types eq 'A'}">[공지]</c:if>
				<c:if test="${result.types eq 'B'}">[이벤트]</c:if>
				<c:if test="${result.types eq 'C'}">[축하]</c:if>
				<c:if test="${result.types eq 'D'}">[안내]</c:if>
				<c:if test="${result.types eq 'E'}">[설문]</c:if>
				<c:if test="${result.types eq 'F'}">[긴급]</c:if>
				<c:if test="${result.types eq 'G'}">[기타]</c:if>
				${result.title}
			</a>
			</dt>
			<dd><span class="tit">작성자</span> ${result.name}</dd>
			<dd><span class="tit">작성일</span> ${fn2:getFormatDate(result.addate, 'yyyy.MM.dd')}</dd>
		</dl>
	</li>
</c:forEach>
</ul>

<input id="firstIndex" name="firstIndex" type="hidden" value="<c:out value='${firstIndex}'/>" />
<input id="recordCountPerPage" name="recordCountPerPage" type="hidden" value="<c:out value='${recordCountPerPage+15}'/>" />
