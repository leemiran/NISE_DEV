<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/xml; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<RESPONSE>
	<RES_CODE><![CDATA[100]]></RES_CODE>
	<ID><![CDATA[${userid}]]></ID>
	<NAME><![CDATA[${name}]]></NAME>
	<SCHEDULE_NO><![CDATA[${CONTENT_ID}]]></SCHEDULE_NO>
	<COURSE_NO><![CDATA[${view.subj}]]></COURSE_NO>
	<COURSE_NAME><![CDATA[${view.subjnm}]]></COURSE_NAME>
	<CONTINUE_TIME><![CDATA[${(!empty view.courseComplete && view.courseComplete eq 'Y' ? 0 : view.sessionTime)}]]></CONTINUE_TIME>
	<COURSE_COMPLETE><![CDATA[${(!empty view.courseComplete && view.courseComplete eq 'Y' ? 'TRUE' : 'FALSE')}]]></COURSE_COMPLETE>
	<LAST_CHAPTER_ID><![CDATA[${view.lesson}]]></LAST_CHAPTER_ID>
	<BOOK_MARK><![CDATA[${view.bookMark}]]></BOOK_MARK>
    <UNIT_NO><![CDATA[${view.module}]]></UNIT_NO>
    <UNIT_NAME><![CDATA[${view.moduleNm}]]></UNIT_NAME>
    <CAPTION_URL><![CDATA[${view.captionUrl}]]></CAPTION_URL>
    <DAY_LIMIT><![CDATA[${view.dayLimit}]]></DAY_LIMIT>
    
<c:if test="${not empty list}">    
     <CHAPTERS>
	<c:forEach items="${list}" var="result" varStatus="status">     
        <CHAPTER>
            <CHAPTER_ID><![CDATA[${result.lesson}]]></CHAPTER_ID><!-- 챕터 아이디 -->
            <START_TIME><![CDATA[${result.mStart}]]></START_TIME><!-- 챕터 시작시간(초단위) -->
            <END_TIME><![CDATA[${result.mEnd}]]></END_TIME><!-- 챕터 종료시간(초단위) -->
        </CHAPTER>
	</c:forEach>        
    </CHAPTERS>
</c:if>
    
</RESPONSE>
