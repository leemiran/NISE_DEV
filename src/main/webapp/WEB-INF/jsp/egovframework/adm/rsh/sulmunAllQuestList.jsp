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
	<div class="searchWrap txtL">
		<div>		
			<ul class="datewrap">
				<li class="floatL">
				<strong class="shTit">설문분류 :</strong>

					<ui:code id="p_search_distcode" selectItem="${p_search_distcode}" gubun="" codetype="0054" levels="1" upper="" title="설문분류" className="" type="select" selectTitle="::ALL::" event="" />
					<a href="#none" class="btn_search" onclick="doPageList()"><span>검색</span></a>
				</li>
			</ul>		
		</div>
	</div>
	<!-- 검색박스 끝 -->
	
	
	<div class="listTop">
				<div class="btnL MR05">
               		<a href="#" class="btn01" onclick="scaleListPop()"><span>설문척도</span></a>
                </div>			
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
		      <col width="%"/>
		      <col width="17%"/>
		      <col width="8%"/>
	        </colgroup>
		    <thead>
		      <tr>
		        <th scope="row">No</th>
		        <th scope="row">문제</th>
		        <th scope="row">설문분류</th>
		        <th scope="row">문제분류</th>
	          </tr>
	        </thead>
		    <tbody>
<c:forEach items="${list}" var="result" varStatus="status">		    
		      <tr>
		        <td class="num">${status.count}</td>
		        <td class="left"><a href="#none" onclick="doView('${result.sulnum}')">${result.sultext}</a></td>
		        <td>${result.distcodenm}</td>
		        <td>${result.sultypenm}</td>
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
	thisForm.action = "/adm/rsh/sulmunAllQuestList.do";
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
	thisForm.action = "/adm/rsh/sulmunAllQuestList.do";
	thisForm.target = "_self";
	thisForm.submit();
	
	
}

//설문문항보기
function doView(p_sulnum) {
	var url = '/adm/rsh/sulmunAllQuestView.do'
		+ "?p_sulnum=" + p_sulnum
		;
			
	window.open(url,"sulmunAllQuestViewPopupWindowPop","width=800,height=650,scrollbars=yes");
}


//설문척도리스트
function scaleListPop() {
	var url = '/adm/rsh/scaleList.do';
			
	window.open(url,"searchScaleListPopupWindowPop","width=800,height=650,scrollbars=yes");
}


//-->
</script>





<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>
