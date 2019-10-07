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
	<input type="hidden" id="subj" 		name="subj"			value="">	

   	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="A"							>조회조건타입 : 타입별 세부조회조건		</c:param>
		<c:param name="selectParameter"     value=""							>조회조건 추가 : admSearchParameter.jsp 추가	</c:param>
	</c:import>
	<!-- 검색박스 끝 -->

	

		<div class="listTop">			
<!--			<div class="btnR">-->
<!--			<a href="#" class="btn01"><span>파일다운로드</span></a>-->
<!--			</div>-->
            <div class="btnR MR05">
			<a href="#" onclick="doPageView('')" class="btn01"><span>과정개설</span></a>
			</div>            
		</div>
		
		<!-- list table-->
		<div class="tbList">
			<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
					<col width="45px" />
					<col width="100px" />
					<col width="100px" />
					<col width="100px" />
					<col width="" />
					<col width="100px" />
					<col width="100px" />
				</colgroup>
				<thead>
					<tr>
						<th scope="row">No</th>
						<th scope="row"><a href="#none" onclick="doOderList('codenm')" >교육구분</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('upperclass')" >과정분류</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('subj')" >과정코드</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('subjnm')" >과정명</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('isuse')" >과정사용</a></th>
						<th scope="row"><a href="#none" onclick="doOderList('mobile')" >모바일지원여부</a></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="result">				
					<tr>
						<td class="num"><c:out value="${(pageTotCnt+1)-result.num}"/></td>
						<td><c:out value="${result.codenm}"/></td>
						<td><c:out value="${result.classname}"/></td>
						<td><c:out value="${result.subj}"/></td>
						<td class="left">
						<a href="#none" onclick="doPageView('<c:out value="${result.subj}"/>')">
						<c:out value="${result.subjnm}"/>
						</a>
						</td>
						<td><c:out value="${result.isuse}"/></td>
						<td><c:out value="${result.mobile}"/></td>
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

</form>




<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


/* ********************************************************
 * 페이징처리 함수
 ******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/cou/subjectList.do";
	thisForm.pageIndex.value = pageNo;
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 등록/수정 함수
******************************************************** */
function doPageView(subj) {
	thisForm.subj.value  = subj;
	thisForm.action = "/adm/cou/subjectView.do";
	thisForm.target = "_self";
	thisForm.submit();
}

/* ********************************************************
* 정렬처리 함수
******************************************************** */
function doOderList(column) {
	
	thisForm.search_orderType.value = (thisForm.search_orderType.value == "DESC") ? "ASC" : "DESC";
	thisForm.search_orderColumn.value = column;
	thisForm.target = "_self";
	thisForm.submit();
	
	
}


//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
