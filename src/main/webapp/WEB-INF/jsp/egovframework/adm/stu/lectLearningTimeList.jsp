<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	<input type="hidden" id="p_process" 			name="p_process">

			
   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="B"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value="STUDENT"							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->


		<div class="listTop">
			<p class="floatL point">TOTAL : <span class="num1"><c:out value="${fn:length(list)}"/></span> 명</p>
            	
                <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>
                </div>
  		</div>
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="4%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
                    <col width="%"/>
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row">과정명</th>
						<th scope="row"><a href="#none" onclick="doOderList('area_codenm')" >지역</a></th>
						<th scope="row">교육구분</th>
						<th scope="row">기수</th>
						<th scope="row"><a href="#none" onclick="doOderList('c.userid')" >ID</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('c.name')" >성명</a></th>
						<th scope="row">진도율(%)</th>
						<th scope="row">최초<br/>학습일</th>
						<th scope="row">최근<br/>학습시작일</th>
						<th scope="row">최근<br/>학습종료일</th>
						<th scope="row">총<br/>학습시간</th>
					</tr>
				</thead>
				<tbody>


				
<c:forEach items="${list}" var="result" varStatus="status" >

<c:set var="v_totaltime">${result.totalTime}</c:set>

<c:set var="v_totalminute">${result.totalMinute}</c:set>

<c:set var="v_totalsec">${result.totalSec}</c:set>

<c:set var="v_edutotmin">${((v_totaltime*60*60) + (v_totalminute*60) + v_totalsec)/60}</c:set>

					<tr>
						<td class="num">
							<c:out value="${status.count}"/>
						</td>
						<td class="left"><c:out value="${result.subjnm}"/></td>
						<td>${result.areaCodenm}</td>
						<td class="num"><c:out value="${result.isonoff}"/></td>
						<td><c:out value="${fn2:toNumber(result.subjseq)}"/></td>
						<td>
						<a href="javascript:whenMemberInfo('${result.userid}')"><c:out value="${result.userid}"/></a>
						</td>
						<td><a href="javascript:whenAttend('${result.subj}','${result.year}','${result.subjseq}','${result.userid}','${result.name}')"><c:out value="${result.name}"/></a></td>
						<td><fmt:formatNumber value="${result.tstep}" 	 type="number" pattern=",###.#"/></td>
						<td><c:out value="${fn2:getFormatDate(result.firstEdu, 'yyyy.MM.dd')}"/></td>
						<td><c:out value="${fn2:getFormatDate(result.ldateStart, 'yyyy.MM.dd')}"/></td>
						<td><c:out value="${fn2:getFormatDate(result.ldateEnd, 'yyyy.MM.dd')}"/></td>
						<td>
						<a href="#none" onclick="whenPersonalTimeList('${result.subj}','${result.year}','${result.subjseq}','${result.userid}')">						
						<%-- <fmt:formatNumber value="${v_edutotmin}" pattern=",###.#"/> Min --%>
						<c:out value="${result.hrMinuteSec}"/>
						</a>
						</td>
					</tr>
</c:forEach>			
				</tbody>
			</table>
    </div>
		<!-- list table-->

</form>




<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');



/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	thisForm.action = "/adm/stu/lectLearningTimeExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/stu/lectLearningTimeList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.action = "/adm/stu/lectLearningTimeList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}


//개인별 학습창 접근 로그 팝업
function whenPersonalTimeList(subj, year, subjseq, userid) {

	var url = '/adm/stu/personalTimeListPopUp.do'
	+ '?p_grcode=<c:out value="${sessionScope.grcode}"/>'
	+ '&p_subj=' + subj
	+ '&p_year=' + year
	+ '&p_subjseq=' + subjseq
	+ '&p_userid=' + userid
	;
		
	window.open(url,"personalTimeListPopupWindowPop","width=1000,height=600,scrollbars=yes");
}



//출석부
function whenAttend(subj, year, subjseq, userid, name) {

	var url = '/adm/stu/personalAttendListPopUp.do'
	+ '?p_grcode=<c:out value="${sessionScope.grcode}"/>'
	+ '&p_subj=' + subj
	+ '&p_year=' + year
	+ '&p_subjseq=' + subjseq
	+ '&p_userid=' + userid
	+ '&p_name=' + name
	;
		
	window.open(url,"personalAttendListPopupWindowPop","width=660,height=600,scrollbars=yes");
}


//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
