<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/xml; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>   
<c:if test="${not empty list}">    
	<DATA_STREAM>
		<c:forEach items="${list}" var="result" varStatus="status">
		<list>
	  		<orgClassId><![CDATA[${result.subj}]]></orgClassId>	  		
	  		<classId><![CDATA[${result.yearSubjSubjseq}]]></classId>
	  		<classNm><![CDATA[${result.subjnmSubjseq}]]></classNm>
	  		<strtDt><![CDATA[${result.edustart}]]></strtDt>
	  		<endDt><![CDATA[${result.eduend}]]></endDt>
	  		<eduPeriod><![CDATA[${result.eduperiod}]]></eduPeriod>
	  		<classTotalTm><![CDATA[${result.edutimes}]]></classTotalTm>
	  		<weekClassTm></weekClassTm>
	  		<classGoal><![CDATA[${result.intro}]]></classGoal>
	  		<classUrl><![CDATA[${result.classUrl}]]></classUrl>
	  		<textNm><![CDATA[ ]]></textNm>
	  		<teacherNm><![CDATA[ ]]></teacherNm>
	  		<sublist>
	   			<rowNo></rowNo>
	   			<contents><![CDATA[ ]]></contents>
	  		</sublist> 
	 	</list> 
		</c:forEach>		
	</DATA_STREAM>
</c:if>