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
	<input type="hidden" id="search_att_name_value" 			name="search_att_name_value">
	<input type="hidden" id="rd_gubun" 			name="rd_gubun" value="Y">
			
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="C"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->



<!--		<div class="listTop">-->
<!--                <div class="btnR MR05">-->
<!--               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>-->
<!--                </div>-->
<!--  		</div>-->
  		<div class="listTop">
  		<div class="btnR MR05">
		<a href="#" onclick="yearSubjectPrint()" class="btn01"><span>인쇄</span></a>
		</div>
		</div>
	    <!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                      <col width="%"/>
                      <col width="%"/>
                      <col width="50%"/>                      
                      <col width="8%"/>
                      <col width="8%"/>
                      <col width="8%"/>
                      <col width="8%"/>
                </colgroup>
                <thead>
                      <tr>
                          <th scope="row">분야</th>
                          <th scope="row">과정코드</th>
                          <th scope="row">과정</th>
                          <th scope="row">교육</th>
                          <th scope="row">수료</th>
                          <th scope="row">미수료</th>
                          <th scope="row">수료율(%)</th>
                      </tr>
                </thead>
                <tbody>


<c:set var="totEduCnt" value="0.0"></c:set>
<c:set var="totGradCnt" value="0.0"></c:set>
<c:set var="totNotGradCnt" value="0.0"></c:set>

<c:set var="totGradPer" value="0.0"></c:set>
<c:set var="v_eduCnt" value="0.0"></c:set>
<c:set var="v_gradCnt" value="0.0"></c:set>
         
<c:forEach items="${list}" var="result" varStatus="status" >  
 
<c:set var="totEduCnt">${totEduCnt + result.educnt}</c:set>
<c:set var="totGradCnt">${totGradCnt + result.gradcnt}</c:set>
<c:set var="totNotGradCnt">${totNotGradCnt + (result.educnt - result.gradcnt)}</c:set>
<c:set var="v_eduCnt">${result.educnt}</c:set>
<c:set var="v_gradCnt">${result.gradcnt}</c:set>

             
                      <tr>
                        <td>${result.classname}</td>
                        <td>${result.subj}</td>
                        <td class="left">${result.subjnm}</td>
                        <td><fmt:formatNumber value="${result.educnt}" type="number"/></td>
                        <td><fmt:formatNumber value="${result.gradcnt}" type="number"/></td>
                        <td><fmt:formatNumber value="${result.educnt - result.gradcnt}" type="number"/></td>
                        <td>
                        <c:set var="gradCntPer" value="0.0"></c:set>
                        <c:if test="${v_eduCnt > 0.0}">
                        	<c:set var="gradCntPer">${v_gradCnt/v_eduCnt * 100.00}</c:set>
                        </c:if>
                        
                        <fmt:formatNumber value="${gradCntPer}" type="number" pattern=",##0.00"/>%
                        </td>
                      </tr>
</c:forEach>                      
                      <tr class="bg">
                        <td colspan="3"><strong>총합계</strong></td>
                        <td class="point"><strong><fmt:formatNumber value="${totEduCnt}" type="number"/></strong></td>
                        <td class="point"><strong><fmt:formatNumber value="${totGradCnt}" type="number"/></strong></td>
                        <td class="point"><strong><fmt:formatNumber value="${totNotGradCnt}" type="number"/></strong></td>
                        <td class="point"><strong>
                      
                        <c:set var="totGradCntPer" value="0.0"></c:set>
                        <c:if test="${totEduCnt > 0.0}">
                        	<c:set var="totGradCntPer">${totGradCnt/totEduCnt * 100.00}</c:set>
                        </c:if>
                        
                        <fmt:formatNumber value="${totGradCntPer}" type="number" pattern=",##0.00"/>%
                        
                        
                        </strong></td>
                      </tr>
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
	thisForm.action = "/adm/sat/yearSubjectSatisExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/sat/yearSubjectSatisList.do";
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
	thisForm.action = "/adm/sat/yearSubjectSatisList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

//통계 RD 출력
function yearSubjectPrint(){
	//var frm = eval('document.<c:out value="${gsMainForm}"/>');
	window.open('', 'yearSubjectPop', 'left=100,top=100,width=810,height=700,scrollbars=yes');
	thisForm.search_att_name_value.value=$("#ses_search_att option:selected").text();
	thisForm.action = "/adm/sta/yearSubjectPrint.do";
	thisForm.target = "yearSubjectPop";
	thisForm.submit();
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
