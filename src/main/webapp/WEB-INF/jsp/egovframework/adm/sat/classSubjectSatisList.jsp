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
	<input type="hidden" id="search_att_name_value" name="search_att_name_value">

	
			
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="C"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->

	<div class="listTop">
		<div class="btnR MR05">
			<a href="#none" onclick="rd_print()" class="btn01"><span>인쇄</span></a>
		</div>
  	</div>

<!--		<div class="listTop">-->
<!--                <div class="btnR MR05">-->
<!--               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>-->
<!--                </div>-->
<!--  		</div>-->
  		
  		
		
		<!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                      <col width="26%"/>
                      <col width="26%"/>
                      <col width="12%"/>                      
                      <col width="12%"/>
                      <col width="12%"/>
                      <col width="12%"/>                      
                </colgroup>
                <thead>
                      <tr>
                          <th colspan="2" scope="row">구분</th>
                          <th scope="row">입과인원</th>
                          <th scope="row">수료인원</th>
                          <th scope="row">미수료인원</th>
                          <th scope="row">수료율</th>
                      </tr>
                </thead>
                <tbody>
<c:set var="totEduCnt">0.0000000</c:set>
<c:set var="totGradCnt">0.0000000</c:set>
<c:set var="totNotGradCnt">0.0000000</c:set>

<c:set var="totGradPer">0.0000000</c:set>
<c:set var="v_eduCnt">0.0000000</c:set>
<c:set var="v_gradCnt">0.0000000</c:set>

<c:set var="v_checker"></c:set>
         
<c:forEach items="${list}" var="result" varStatus="status" >  
 
	
	<c:if test="${empty v_checker}">
			<c:set var="v_checker">${result.upclassname}</c:set>
	</c:if>		
	             
             
             
             
	<c:if test="${not empty v_checker && v_checker != result.upclassname}">
		<c:set var="v_checker">${result.upclassname}</c:set>

		
					<tr class="bg">
                        <td colspan="2" class="point"><strong>총합계</strong></td>
                        <td class="point"><strong><fmt:formatNumber value="${totEduCnt}" type="number"/></strong></td>
                        <td class="point"><strong><fmt:formatNumber value="${totGradCnt}" type="number"/></strong></td>
                        <td class="point"><strong><fmt:formatNumber value="${totNotGradCnt}" type="number"/></strong></td>
                        <td class="point"><strong>
                      
                        <c:set var="totGradCntPer">0.0000000</c:set>
                        <c:if test="${totEduCnt > 0.0}">
                        	<c:set var="totGradCntPer">${totGradCnt/totEduCnt * 100.00}</c:set>
                        </c:if>
                        
                        <fmt:formatNumber value="${totGradCntPer}" type="number" pattern=",##0.00"/>%
                        
                        
                        </strong></td>
                      </tr>
                      
		<c:set var="totEduCnt">0.0000000</c:set>
		<c:set var="totGradCnt">0.0000000</c:set>
		<c:set var="totNotGradCnt">0.00000</c:set>
		
		<c:set var="totGradPer">0.0000000</c:set>
		<c:set var="v_eduCnt">0.0000000</c:set>
		<c:set var="v_gradCnt">0.0000000</c:set>                      
	</c:if>            
	
	<c:if test="${result.educnt > 0}">
		<c:set var="totEduCnt">${totEduCnt + result.educnt}</c:set>
		<c:set var="totNotGradCnt">${totNotGradCnt + (result.educnt - result.gradcnt)}</c:set>
	</c:if>
	<c:if test="${result.gradcnt > 0}">
		<c:set var="totGradCnt">${totGradCnt + result.gradcnt}</c:set>
	</c:if>
	
	<c:set var="v_eduCnt">${result.educnt}</c:set>
	<c:set var="v_gradCnt">${result.gradcnt}</c:set>
	             
                      <tr>
                        <td>${result.grseqnm}</td>
                        <td class="left">${result.upclassname}</td>
                        <td><fmt:formatNumber value="${result.educnt}" type="number"/></td>
                        <td><fmt:formatNumber value="${result.gradcnt}" type="number"/></td>
                        <td><fmt:formatNumber value="${result.educnt - result.gradcnt}" type="number"/></td>
                        <td>
                        <c:set var="gradCntPer">0.0000000</c:set>
                        <c:if test="${v_eduCnt > 0.0}">
                        	<c:set var="gradCntPer">${v_gradCnt/v_eduCnt * 100.00}</c:set>
                        </c:if>
                        
                        <fmt:formatNumber value="${gradCntPer}" type="number" pattern=",##0.00"/>%
                        </td>
                      </tr>
	          
</c:forEach>                      
                      <tr class="bg">
                        <td colspan="2" class="point"><strong>총합계</strong></td>
                        <td class="point"><strong><fmt:formatNumber value="${totEduCnt}" type="number"/></strong></td>
                        <td class="point"><strong><fmt:formatNumber value="${totGradCnt}" type="number"/></strong></td>
                        <td class="point"><strong><fmt:formatNumber value="${totNotGradCnt}" type="number"/></strong></td>
                        <td class="point"><strong>
                      
                        <c:set var="totGradCntPer">0.0000000</c:set>
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
	thisForm.action = "/adm/sat/classSubjectSatisExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/sat/classSubjectSatisList.do";
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
	thisForm.action = "/adm/sat/classSubjectSatisList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

//출력
function rd_print(){
	window.open('', 'classSubjectSatisListRd', 'left=100,top=100,width=800,height=600,scrollbars=yes');
	thisForm.search_att_name_value.value=$("#ses_search_att option:selected").text();
	thisForm.action = "/adm/sat/classSubjectSatisListRd.do";
	thisForm.target = "classSubjectSatisListRd";
	thisForm.submit();
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
