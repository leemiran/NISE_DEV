<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import = "java.util.Calendar" %>
<%@page import = "java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<script type="text/javascript" src="<c:url value='/js/egovframework/cmm/sym/cal/EgovCalPopup.js' />" ></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/cmm/sym/zip/EgovZipPopup.js' />" ></script>
<SCRIPT LANGUAGE="JavaScript">
<!--


//-->
</SCRIPT>


</head>
<body>

<!--
<div style="width:200px;float:left;">
	<table border=0>
	<tr>
		<td> 우편번호 찾기
		<form name="Form" action ="<c:url value='/sym/cmm/EgovCcmZipSearchPopup.do'/>" method="post">
			<input type="hidden" name="zip_url" value="<c:url value='/sym/cmm/EgovCcmZipSearchPopup.do'/>" />
			<input type="text" name="sZip" value="" size="6" readonly onClick="javascript:fn_egov_ZipSearch(document.Form, document.Form.sZip, document.Form.vZip, document.Form.sAddr);" />
			<input type="text" name="vZip" value="" size="7" readonly onClick="javascript:fn_egov_ZipSearch(document.Form, document.Form.sZip, document.Form.vZip, document.Form.sAddr);" />
			<img src="<c:url value='/images/egovframework/cmm/sym/zip/icon_zip_search.gif' />" onClick="javascript:fn_egov_ZipSearch(document.Form, document.Form.sZip, document.Form.vZip, document.Form.sAddr);">
			<input type="text" name="sAddr" value="" size="30" readonly onClick="javascript:fn_egov_ZipSearch(document.Form, document.Form.sZip, document.Form.vZip, document.Form.sAddr);" />
		</form>
		</td>
	</tr>
	<tr>
		<td> 일반달력
		<form name="Form1" action ="<c:url value='/sym/cmm/EgovNormalCalPopup.do'/>" method="post">
			<input type="hidden" name="cal_url" value="<c:url value='/sym/cmm/EgovNormalCalPopup.do'/>" />
			<input type="text" name="sDate" value="" size="8" readonly onClick="javascript:fn_egov_NormalCalendar(document.Form1, document.Form1.sDate, document.Form1.vDate);" />
			<input type="text" name="vDate" value="" size="10" readonly onClick="javascript:fn_egov_NormalCalendar(document.Form1, document.Form1.sDate, document.Form1.vDate);" />
			<img src="<c:url value='/images/egovframework/cmm/sym/cal/bu_icon_carlendar.gif' />" onClick="javascript:fn_egov_NormalCalendar(document.Form1, document.Form1.sDate, document.Form1.vDate);">
		</form>
		</td>
	</tr>
	<tr>
		<td> 행정달력
		<form name="Form2" action ="<c:url value='/sym/cmm/EgovAdministCalPopup.do'/>" method="post">
			<input type="hidden" name="cal_url" value="<c:url value='/sym/cmm/EgovAdministCalPopup.do'/>" />
			<input type="text" name="sDate" value="" size="8" readonly onClick="javascript:fn_egov_AdministCalendar(document.Form2, document.Form2.sDate, document.Form2.vDate);" />
			<input type="text" name="vDate" value="" size="10" readonly onClick="javascript:fn_egov_AdministCalendar(document.Form2, document.Form2.sDate, document.Form2.vDate);" />
			<img src="<c:url value='/images/egovframework/cmm/sym/cal/bu_icon_carlendar.gif' />" onClick="javascript:fn_egov_AdministCalendar(document.Form2, document.Form2.sDate, document.Form2.vDate);">
		</form>
		</td>
	</tr>
	<tr>
		<td>
		<a href="http://localhost:8080/EgovPageLink.do?link=cmm/utl/testlist" target=if>요소기술 테스트페이지</a>
		</td>
	</tr>
	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/ccc/EgovCcmCmmnClCodeList.do" target=if>공통분류코드</a>
		</td>
	</tr>
	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/cca/EgovCcmCmmnCodeList.do" target=if>공통코드</a>
		</td>
	</tr>
	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/cde/EgovCcmCmmnDetailCodeList.do" target=if>공통상세코드</a>
		</td>
	</tr>
	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/adc/EgovCcmAdministCodeList.do" target=if>행정코드</a>
		</td>
	</tr>
	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/zip/EgovCcmZipList.do" target=if>우편번호</a>
		</td>
	</tr>
	<tr>
		<td>
		<a href="http://localhost:8080/sym/cal/EgovRestdeList.do" target=if>휴일관리</a>
		</td>
	</tr>
	</table>
</div>

//-->

<div style="width:200px;float:left;">
	<table border=0>

	<tr>
		<td> 달력팝업
		<form name="cal" action ="<c:url value='/sym/cmm/callCalPopup.do'/>" method="post">
			<input type="text" name="sDate" value="" size="8" readonly onClick="javascript:fn_egov_Calendar(document.cal, document.cal.sDate, document.cal.vDate);" />
			<input type="text" name="vDate" value="" size="10" readonly onClick="javascript:fn_egov_Calendar(document.cal, document.cal.sDate, document.cal.vDate);" />
			<img src="<c:url value='/images/egovframework/cmm/sym/cal/bu_icon_carlendar.gif' />" onClick="javascript:fn_egov_Calendar(document.cal, document.cal.sDate, document.cal.vDate);">
		</form>
		</td>
	</tr>

	<tr>
		<td> 일반달력
		<form name="Form1" action ="<c:url value='/sym/cmm/EgovNormalCalPopup.do'/>" method="post">
			<input type="hidden" name="cal_url" value="<c:url value='/sym/cmm/EgovNormalCalPopup.do'/>" />
			<input type="text" name="sDate" value="" size="8" readonly onClick="javascript:fn_egov_NormalCalendar(document.Form1, document.Form1.sDate, document.Form1.vDate);" />
			<input type="text" name="vDate" value="" size="10" readonly onClick="javascript:fn_egov_NormalCalendar(document.Form1, document.Form1.sDate, document.Form1.vDate);" />
			<img src="<c:url value='/images/egovframework/cmm/sym/cal/bu_icon_carlendar.gif' />" onClick="javascript:fn_egov_NormalCalendar(document.Form1, document.Form1.sDate, document.Form1.vDate);">
		</form>
		</td>
	</tr>
	<tr>
		<td> 행정달력
		<form name="Form2" action ="<c:url value='/sym/cmm/EgovAdministCalPopup.do'/>" method="post">
			<input type="hidden" name="cal_url" value="<c:url value='/sym/cmm/EgovAdministCalPopup.do'/>" />
			<input type="text" name="sDate" value="" size="8" readonly onClick="javascript:fn_egov_AdministCalendar(document.Form2, document.Form2.sDate, document.Form2.vDate);" />
			<input type="text" name="vDate" value="" size="10" readonly onClick="javascript:fn_egov_AdministCalendar(document.Form2, document.Form2.sDate, document.Form2.vDate);" />
			<img src="<c:url value='/images/egovframework/cmm/sym/cal/bu_icon_carlendar.gif' />" onClick="javascript:fn_egov_AdministCalendar(document.Form2, document.Form2.sDate, document.Form2.vDate);">
		</form>
		</td>
	</tr>

	<tr>
		<td>
		<a href="http://localhost:8080/ssi/syi/sim/getSystemCntcList.do" target=if>시스템연계</a>
		</td>
	</tr>
	<tr>
		<td>
		<a href="http://localhost:8080/ssi/syi/scm/getConfirmSystemCntcList.do" target=if>시스템연계 승인</a>
		</td>
	</tr>

	<tr>
		<td>
		<a href="http://localhost:8080/ssi/syi/iis/getCntcInsttList.do" target=if>연계기관</a>
		</td>
	</tr>

	<tr>
		<td>
		<a href="http://localhost:8080/ssi/syi/ims/getCntcMessageList.do" target=if>연계메시지</a>
		</td>
	</tr>

	<tr>
		<td>
		<a href="http://localhost:8080/ssi/syi/ist/getCntcSttusList.do" target=if>연계현황</a>
		</td>
	</tr>

	<tr>
		<td>
		<a href="http://localhost:8080/sym/log/SelectTrsmrcvLogList.do" target=if>연계LOG</a>
		</td>
	</tr>
	
	
	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/icr/getInsttCodeRecptnMainList.do" target=if>기관코드연계 개인화페이지용</a>
		</td>
	</tr>


	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/icr/getInsttCodeRecptnList.do" target=if>기관코드연계</a>
		</td>
	</tr>

	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/icr/addInsttCode.do" target=if>기관코드연계TEST</a>
		</td>
	</tr>
	
	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/acr/getAdministCodeRecptnList.do" target=if>행정(법정동)코드연계</a>
		</td>
	</tr>

	<tr>
		<td>
		<a href="http://localhost:8080/sym/ccm/acr/addAdministCode.do" target=if>행정(법정동)코드연계TEST</a>
		</td>
	</tr>
	
	</table>
</div>
	



<div style="width:700px;float:left;">
	<table>
	<tr>
		<td>
		<iframe name="if" width="800" height="600" frameborder="0"></iframe>
		</td>
	</tr>
	</table>
</div>


</body>
</html>



