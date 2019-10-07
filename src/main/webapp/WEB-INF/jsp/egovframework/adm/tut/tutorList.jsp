<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex"  			name="pageIndex"				value="${pageIndex}"/>
	<input type="hidden" id="search_orderColumn" 	name="search_orderColumn"		value="${search_orderColumn}">
	<input type="hidden" id="search_orderType" 		name="search_orderType"			value="${search_orderType}">
	<input type="hidden" id="p_process" 			name="p_process">
	<input type="hidden" id="p_userid" 				name="p_userid">

			
   	<!-- search wrap-->
		<div class="searchWrap">
			<div class="in">
				<select name="p_selectType">
					<option value="1" <c:if test="${p_selectType eq '1'}">selected</c:if>>강사명</option>
				  	<option value="2" <c:if test="${p_selectType eq '2'}">selected</c:if>>아이디</option>
				  	<option value="3" <c:if test="${p_selectType eq '3'}">selected</c:if>>담당과정</option>
				  	<option value="4" <c:if test="${p_selectType eq '4'}">selected</c:if>>전공분야</option>
				</select>
				
				
				<input name="p_selectvalue" type="text"  value="${p_selectvalue}">
				<a href="#none" onclick="doPageList('1')" class="btn_red"><span>GO</span></a>
			</div>
		</div>
		<!-- // search wrap -->
        
        <div class="listTop">					
                <div class="btnR MR05">
               		<a href="#none" onclick="whenXlsDownLoad()" class="btn_excel"><span>엑셀출력</span></a>
                </div>
                
		</div>
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="%" />
					<col width="%" />
					<col width="%" />
					<col width="%" />
					<col width="%" />
                    <col width="%" />
                    <col width="%" />
					<col width="30%" />					
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row"><a href="javascript:doOderList('name')">운영강사</a></th>
						<th scope="row"><a href="javascript:doOderList('userid')">아이디</a></th>
						<th scope="row">조직</th>
						<th scope="row">연락처</th>
						<th scope="row">은행</th>
						<th scope="row">계좌번호</th>
                        <th scope="row"><a href="javascript:doOderList('ismanager')">운영강사권한(기간)</a></th>
					</tr>
				</thead>
				<tbody>
<c:forEach items="${list}" var="result" varStatus="status" >				
					<tr>
						<td class="num"><c:out value="${(pageTotCnt+1)-result.num}"/></td>
						<td class="name">
						 <a href = "javascript:doView('${result.userid}')" class="a"><c:out value="${result.name}"></c:out></a>
						</td>
						<td><c:out value="${result.userid}"></c:out></td>
						<td><c:out value="${result.positionNm}"></c:out></td>
						<td><c:out value="${result.handphone}"></c:out></td>
						<td><c:out value="${result.banknm}"></c:out></td>
						<td><c:out value="${result.account}"></c:out></td>
                        <td><c:out value="${result.ismanager}"></c:out> 
                        (<c:out value="${fn2:getFormatDate(result.fmon, 'yyyy.MM.dd')}"/> - <c:out value="${fn2:getFormatDate(result.tmon, 'yyyy.MM.dd')}"/>) </td>
					</tr>
</c:forEach>					
				</tbody>
			</table>
		</div>
		<!-- list table-->

		<!-- paging -->
		<div class="paging">
		<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doPageList"/>
			<!-- a href="#" class="btn"><img src="./img/pg_btnPrv1.gif" alt="처음" /></a>
			<a href="#" class="btn"><img src="./img/pg_btnPrv2.gif" alt="이전" /></a>
			<a href="#" class="on">1</a>
			<a href="#">2</a>
			<a href="#">3</a>
			<a href="#">4</a>
			<a href="#" class="btn"><img src="./img/pg_btnPrv2.gif" alt="다음" /></a>
			<a href="#" class="btn"><img src="./img/pg_btnNext1.gif" alt="마지막" /></a-->
		</div>
		<!-- // paging -->
        
	    <!-- button -->		
		<div class="btnR MR05">
			<li><a href="#none" onclick="doView('')" class="btn02"><span>등록</span></a></li>
		</div>

</form>




<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');



/* ********************************************************
 * 엑셀다운로드 함수
 ******************************************************** */
function whenXlsDownLoad() {
	thisForm.action = "/adm/tut/tutorExcelDown.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/tut/tutorList.do";
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
	thisForm.action = "/adm/tut/tutorList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

function doView(p_userid) {
	thisForm.p_userid.value = p_userid;
    thisForm.action = "/adm/tut/tutorView.do";
    thisForm.target = "_self";
    thisForm.submit();
}

//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
