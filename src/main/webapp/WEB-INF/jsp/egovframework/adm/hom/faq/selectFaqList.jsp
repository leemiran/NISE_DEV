<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type="hidden" id="p_fnum" name="p_fnum">
	
	
	<!-- search wrap-->
		<div class="searchWrap">
			<div class="in">
				<select name="search_group">
					<option value='title'    >제목</option>
                	<option value='contents' >내용</option>
				</select>
				<input type="text" name="search_word" id="search_word" value="${search_word}" size="50" onkeypress="fn_keyEvent('doPageList')" style="ime-mode:active;"/>
				<a href="#none" class="btn_search01" onclick="doPageList()"><span>검색</span></a>
			</div>
		</div>
		<!-- // search wrap -->
        
        <div class="sub_tit">
			<h4 class="MR10">FAQ 카테고리명 검색 :</h4> 			
			<select name="p_faqcategory" onchange="doPageList()"> 
				<c:forEach items="${categoryList}" var="result" varStatus="i">
					<option value="${result.faqcategory}" <c:if test="${result.faqcategory eq p_faqcategory}">selected</c:if> >${result.faqcategorynm}</option> 
				</c:forEach>
			</select>			
			
			<div class="btnR"><a href="#" class="btn01" onclick="doView('')"><span>등록</span></a></div>
		</div>
		
		
	<!-- list table-->
	<div class="tbList mrb20">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
              <col width="10%" />
              <col width="85%" />
              <col width="5%" />		      
            </colgroup>
            <thead>
                  <tr>
                      <th scope="row">FAQ번호</th>
                      <th scope="row">제목</th>
                      <th scope="row">수정</th>		        
                  </tr>
            </thead>
			<tbody>
			
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td class="num">${result.fnum}</td>
					<td class="left">
					<a href="#none" onclick="doView('${result.fnum}')">
					${result.title}
					</a>
					</td>
					<td ><a href="#none" onclick="doView('${result.fnum}')" class="btn_search"><span>수정</span></a></td>
				</tr>
			</c:forEach>
			
			<c:if test="${empty list}">
				<tr>
					<td colspan="20">조회된 내용이 없습니다.</td>
				</tr>
			</c:if>
			
			
			</tbody>
		</table>
	</div>
	<!-- list table-->
	
	
	
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	var frm = eval('document.<c:out value="${gsMainForm}"/>');

	
	function doPageList() {
		frm.action = "/adm/hom/faq/selectFaqList.do";
		frm.target = "_self";
		frm.submit();
	}


	function doView(p_fnum){
		frm.p_fnum.value = p_fnum;
		frm.action = "/adm/hom/faq/selectFaqView.do";
		frm.target = "_self";
		frm.submit();
	}

</script>