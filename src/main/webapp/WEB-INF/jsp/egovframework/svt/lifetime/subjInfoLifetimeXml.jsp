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
	  			<lClassCd>03</lClassCd>
	  			<mClassCd>33</mClassCd>
	  			<gedTiesLevel></gedTiesLevel>
	  			<gedTiesSubject></gedTiesSubject>
	  			<orgClassId><![CDATA[${result.subj}]]></orgClassId>
	  			<orgClassNm><![CDATA[${result.subjnm}]]></orgClassNm>
	  			<orgClassLevel></orgClassLevel>
	  			<evalMethod><![CDATA[${result.evalMethod}]]></evalMethod>
	 		</list>
 		</c:forEach>
</DATA_STREAM>
</c:if>