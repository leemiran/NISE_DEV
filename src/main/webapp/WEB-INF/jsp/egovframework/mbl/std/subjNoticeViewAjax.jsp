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

		
<!-- 상세보기 -->
<div class="bview mrt20">
	<h3 class="tit_board">
	<c:if test="${selectGong.types eq 'A'}"><img src="/images/adm/ico/ico_notice.gif" alt="공지" /></c:if>
	<c:if test="${selectGong.types eq 'B'}"><img src="/images/adm/ico/ico_event.gif" alt="이벤트" /></c:if>
	<c:if test="${selectGong.types eq 'C'}"><img src="/images/adm/ico/ico_happy.gif" alt="축하" /></c:if>
	<c:if test="${selectGong.types eq 'D'}"><img src="/images/adm/ico/ico_guide.gif" alt="안내" /></c:if>
	<c:if test="${selectGong.types eq 'E'}"><img src="/images/adm/ico/ico_poll.gif" alt="설문" /></c:if>
	<c:if test="${selectGong.types eq 'F'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
	<c:if test="${selectGong.types eq 'G'}"><img src="/images/adm/ico/ico_others.gif" alt="기타" /></c:if>
	
	${selectGong.title}
	</h3>
	<table summary="작성자,작성일,조회수,첨부,내용으로 구성" cellspacing="0" width="100%" class="mrt10">
		<caption>${selectGong.title}</caption>
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
			<td>${selectGong.gadmin}</td>
			<th scope="row">작성일</th>
			<td>${fn2:getFormatDate(selectGong.addate, 'yyyy.MM.dd')}</td>
			<th scope="row">조회수</th>
			<td>${selectGong.cnt}</td>
		</tr>
		<tr>
			<th scope="row">첨부</th>
			<td colspan="5">
				<c:if test="${empty selectGong.upfile}">첨부파일이 없습니다.</c:if>
				<c:if test="${not empty selectGong.upfile}">
					<a href="#none" onclick="fn_download('${selectGong.realfile}', '${selectGong.upfile}', 'bulletin')"><c:out value="${selectGong.realfile}"/></a>
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan="6" class="view">
			
			<c:out value="${selectGong.adcontent}" escapeXml="false"/>
			
			</td>
		</tr>
	</table>
</div>
<!-- //상세보기 -->

<!-- button -->
<ul class="btnR">
	<li><a href="#" onclick="getSubjNoticeList();" class="btn02"><span>목록</span></a></li>
</ul>