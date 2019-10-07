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
		<c:param name="selectViewType"      value="CC"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->

 <div class="sub_tit">
	           	 <h4 style="font-size:14px; width:600px;">접속통계  ${ses_search_gyear}년 ${ses_search_gmonth}월 <span class="point">(Total :6032)</span></h4>
	          </div>
                
<!--	          <div class="listTop">-->
<!--                    <div class="floatL MR05"> <a href="#" class="btn01"><span>월/일보기</span></a> </div>-->
<!--                    <div class="floatL MR05"> <a href="#" class="btn01"><span>일/시 보기</span></a> </div>-->
<!--                    <div class="floatL MR05"> <a href="#" class="btn01"><span>월/시 보기</span></a> </div>-->
<!--                    <div class="floatL MR05"> <a href="#" class="btn01"><span>월/요일 보기</span></a> </div>-->
<!--	          </div>-->
                
	          <!-- list table-->
	          <div class="tbList">
	            <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
	                </colgroup>
	              <thead>
                        <tr>
                              <th scope="row">${ses_search_gyear}년</th>
                              <th scope="row">1월</th>
                              <th scope="row">2월</th>
                              <th scope="row">3월</th>
                              <th scope="row">4월</th>
                              <th scope="row">5월</th>
                              <th scope="row">6월</th>
                              <th scope="row">7월</th>
                              <th scope="row">8월</th>
                              <th scope="row">9월</th>
                              <th scope="row">10월</th>
                              <th scope="row">11월</th>
                              <th scope="row">12월</th>
                          </tr>
	              </thead>
	              <tbody>
                        <tr>
                              <td>접속자수</td>
                              
				<c:set var="totMonthPer">1</c:set>                              
                              
                <c:forEach var="xMonth" begin="1" end="12">
					<c:set var="xMonthStr">${fn2:getNumberToString(xMonth)}</c:set>
					<td>
					<c:forEach items="${yearList}" var="result" varStatus="status" >
						<c:if test="${result.dateMonth eq xMonthStr}">
							<c:if test="${result.dateMonth eq ses_search_gmonth}">
								<c:set var="totMonthPer">${result.cnt}</c:set>
							</c:if>
							<fmt:formatNumber value="${result.cnt}" type="number"/>
						</c:if>
					</c:forEach>
					</td>
				</c:forEach> 
                              

                          </tr>
	                </tbody>
	              </table>
	            </div>
	          <!-- list table-->
              
              <!-- list table-->
	          <div class="tbList MT20">
                    <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                                <col width="10%"/>
                                <col width="90%"/>
                            </colgroup>
                          <thead>
                                <tr>
                                      <th scope="row">일</th>
                                      <th scope="row">[${ses_search_gyear}년 ${ses_search_gmonth}월] 일자별 접속자수입니다.</th>
                                </tr>
                          </thead>
                          <tbody>
<c:set var="dayPer">0.000000</c:set>
<c:set var="dayCnt">0.000000</c:set>
<c:set var="totCnt">0.000000</c:set>


<c:forEach items="${monthList}" var="result" varStatus="status" >
<c:set var="dayCnt">${result.cnt}</c:set>

<c:if test="${totMonthPer > 0}">
	<c:set var="dayPer"><fmt:formatNumber value="${dayCnt/totMonthPer*100.00}" type="number" pattern="##0.00"/></c:set>
</c:if>

<c:if test="${empty totMonthPer || totMonthPer == 0}">
	<c:set var="dayPer">0</c:set>
</c:if>


<c:set var="totCnt">${totCnt + dayPer}</c:set>


                                <tr>
                                      <td>${result.dateDay}일</td>
                                      <td class="left">
                                      <div class="graph_1 floatL" style="width:${dayPer}%;"></div> 
                                      <span class="ML10"><strong><fmt:formatNumber value="${result.cnt}" type="number"/>회 (${dayPer}%)</strong></span>
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
	thisForm.action = "/adm/sat/countMemberSatisListExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/sat/countMemberSatisList.do";
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
	thisForm.action = "/adm/sat/countMemberSatisList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
