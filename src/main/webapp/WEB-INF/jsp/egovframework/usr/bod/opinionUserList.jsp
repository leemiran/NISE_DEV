<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>


<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_seq"     value = "" />
	<input type = "hidden" name="p_process"     value = "" />
	<input type = "hidden" name="p_tabseq"     value = "${p_tabseq}" />
	
	    <!-- search wrap-->
		<div class="searchWrap">
			<div class="in">
					<select name="p_search" title="검색항목">
	                      <option value='name'	 	<c:if test="${p_search eq 'name'}">selected</c:if>>작성자</option>
	                      <option value='title' 	<c:if test="${p_search eq 'title'}">selected</c:if>>제목</option>
	                      <option value='content' 	<c:if test="${p_search eq 'content'}">selected</c:if>>내용</option>
	                      <option value='userid' 	<c:if test="${p_search eq 'userid'}">selected</c:if>>아이디</option>
	                      <option value='ldate'  	<c:if test="${p_search eq 'ldate'}">selected</c:if>>작성일자</option>
	              </select>
					<input type="text" name="p_searchtext" id="p_searchtext" value="${p_searchtext}" size="50" onkeypress="fn_keyEvent('doSearchList')" style="ime-mode:active;" title="검색어"/>
				<a href="#none" onclick="doSearchList()"><img src="/images/user/btn_search.gif" alt="검색" /></a>
			</div>
		</div>
		<!-- // search wrap -->		
		
		<!-- list table-->
		<div class="tbList">
			<table summary="번호, 제목, 작성자, 등록일, 조회수로 구성" cellspacing="0" width="100%">
                <caption>의견목록</caption>
                <colgroup>
					<col width="10%" />
					<col width="%" />
					<col width="8%" />
					<col width="10%" />
                    <col width="10%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="row">번호</th>
						<th scope="row">제목</th>
						<th scope="row">작성자</th>
						<th scope="row">등록일</th>
                        <th scope="row">조회수</th>
					</tr>
				</thead>
				<tbody>
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
						<td class="num"><c:out value="${(pageTotCnt+1)-result.rn}"/></td>
						<td class="left">
						
						<c:if test="${result.levels > 1}">
							<img src="/images/user/btn_replay.gif" alt="답변" class="vrM" style="padding-left:${result.levels*15-30}px;"/>
						</c:if>
						
							<a href="#none" onclick="doView('${result.seq}')">${result.title}</a>
						</td>
						<td>${result.name}</td>
						<td >${fn2:getFormatDate(result.indate, 'yyyy.MM.dd')}</td>
                        <td class="num">${result.cnt}</td>
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

		<!-- 페이징 시작 -->
		<div class="paging">
			<ui:pagination paginationInfo = "${paginationInfo}"	type="image" jsFunction="doLinkPage"/>
		</div>
		<!-- 페이징 끝 -->
        <!-- button -->
		<ul class="btnR">
        	<li><a href="#none"  onclick="doEdit('')"><img src="/images/user/btn_write.gif" alt="등록" /></a></li>
		</ul> 
		<!-- // button -->
        
        
</form>

<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList() {
	thisForm.action = "/usr/bod/opinionUserList.do";
	thisForm.target = "_self";
	thisForm.pageIndex.value = "1";
	thisForm.submit();
}

function doSearchList() {

	if(thisForm.p_searchtext.value == ''){
		if(!confirm("검색어 없이 검색하시면 전체가 검색됩니다. 검색하시겠습니까?")){
			thisForm.p_searchtext.focus(); 
			return;
		}else{
			thisForm.action = "/usr/bod/opinionUserList.do";
			thisForm.target = "_self";
			thisForm.pageIndex.value = "1";
			thisForm.submit();
			
		}
	}else{
		
		thisForm.action = "/usr/bod/opinionUserList.do";
		thisForm.target = "_self";
		thisForm.pageIndex.value = "1";
		thisForm.submit();
	}
	
}

function doLinkPage(index) {
	thisForm.action = "/usr/bod/opinionUserList.do";
	thisForm.pageIndex.value = index;
	thisForm.target = "_self";
	thisForm.submit();
}



function doView(p_seq){
	thisForm.p_seq.value = p_seq;
	thisForm.p_process.value = '';
	thisForm.action = "/usr/bod/opinionUserView.do";
	thisForm.target = "_self";
	thisForm.submit();
}

function doEdit(){
	thisForm.p_seq.value = '';
	thisForm.p_process.value = 'insert';
	thisForm.action = "/usr/bod/opinionUserEdit.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//-->
</script>

<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->