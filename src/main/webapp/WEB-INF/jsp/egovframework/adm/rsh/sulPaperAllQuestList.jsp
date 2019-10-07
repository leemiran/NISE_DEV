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

	
	
	
	<div class="listTop">
                <div class="btnR MR05">
               		<a href="#none" onclick="doView('')" class="btn01"><span>등록</span></a>
                </div>                 
    </div>
		
		<!-- list table-->
		<div class="tbList">
		  <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
		      <col width="5%"/>		      
		      <col width="15%"/>
		      <col width="60%"/>
		      <col width="10%"/>
		      <col width="10%"/>
	        </colgroup>
		    <thead>
		      <tr>
		        <th scope="row">No</th>
		        <th scope="row">구분</th>
		        <th scope="row">설문지명</th>
		        <th scope="row">문제수</th>
		        <th scope="row">기능</th>
	          </tr>
	        </thead>
		    <tbody>
<c:forEach items="${list}" var="result" varStatus="status">		    
		      <tr>
		        <td class="num">${status.count}</td>
		       <td>
		       <c:choose>
		       		<c:when test="${result.sultype eq '1'}">과정설문</c:when>
		       		<c:when test="${result.sultype eq '2'}">사전설문</c:when>
		       		<c:when test="${result.sultype eq '3'}">사후설문</c:when>
		       </c:choose>
		       </td>
		        <td class="left"><a href="#none" onclick="doView('${result.sulpapernum}')">${result.sulpapernm}</a></td>
		        <td>${result.totcnt}</td>
		        <td><a href="#none" onclick="doPreview('${result.sulpapernum}')" class="btn_search01"><span>미리보기</span></a></td>
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
* 페이징처리 함수
******************************************************** */
function doPageList(pageNo) {
	if(pageNo == "" || pageNo == undefined) pageNo = 1;
	thisForm.action = "/adm/rsh/sulPaperAllQuestList.do";
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
	thisForm.action = "/adm/rsh/sulPaperAllQuestList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

//설문문항보기
function doView(p_sulpapernum) {
	var url = '/adm/rsh/sulPaperAllQuestView.do'
		+ "?p_sulpapernum=" + p_sulpapernum
		;
			
	window.open(url,"sulPaperAllQuestViewPopupWindowPop","width=1000,height=650,scrollbars=yes");
}

//설문문항미리보기
function doPreview(p_sulpapernum) {
	var url = '/adm/rsh/sulPaperPreviewList.do'
		+ "?p_sulpapernum=" + p_sulpapernum
		;
			
	window.open(url,"sulPaperPreviewListPopupWindowPop","width=800,height=650,scrollbars=yes");
}




//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
