<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>


<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype" value="<c:out value="${p_contenttype}"  escapeXml="true" />" />
<input type="hidden" name="p_subj" value="<c:out value="${p_subj}"  escapeXml="true" />" />
<input type="hidden" name="p_year" value="<c:out value="${p_year}"  escapeXml="true" />" />
<input type="hidden" name="p_subjseq" value="<c:out value="${p_subjseq}"  escapeXml="true" />" />
<input type="hidden" name="p_studytype" value="<c:out value="${p_studytype}"  escapeXml="true" />" />
<input type="hidden" name="p_process" value="<c:out value="${p_process}"  escapeXml="true" />" />
<input type="hidden" name="p_next_process" value="<c:out value="${p_next_process}"  escapeXml="true" />" />
<input type="hidden" name="p_height" value="<c:out value="${p_height}"  escapeXml="true" />" />
<input type="hidden" name="p_width" value="<c:out value="${p_width}"  escapeXml="true" />" />
<input type="hidden" name="p_lcmstype" value="<c:out value="${p_lcmstype}"  escapeXml="true" />" />
</form>
<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>

	<input type = "hidden" name="p_seq"     value = "<c:out value="${p_seq}"  escapeXml="true" />" />
	<input type="hidden" id="pageIndex" name="pageIndex" value = "<c:out value="${pageIndex}"  escapeXml="true" />" />
	<input type = "hidden" name="p_tabseq"  value = "<c:out value="${p_tabseq}"  escapeXml="true" />" />
	<input type = "hidden" name="p_type"   value = "<c:out value="${p_type}"  escapeXml="true" />" />
	
	    <!-- search wrap-->
		<div class="searchWrap">
			<div class="in">
					<select name="p_search" id="p_search" title="검색항목">
						<option value="adtitle"		<c:if test="${p_search eq 'adtitle'}">selected</c:if>>제목</option>
						<option value="adcontents"	<c:if test="${p_search eq 'adcontents'}">selected</c:if>>내용</option>
						<option value="addate"		<c:if test="${p_search eq 'addate'}">selected</c:if>>작성일자</option>
						<option value="adname"		<c:if test="${p_search eq 'adname'}">selected</c:if>>작성자</option>
						<option value="aduserid"	<c:if test="${p_search eq 'aduserid'}">selected</c:if>>아이디</option>
					</select>
					<input type="text" name="p_searchtext" id="p_searchtext" value="<c:out value="${p_searchtext}"  escapeXml="true" />" size="50"  onkeypress="fn_keyEvent('doSearchList')" style="ime-mode:active;" title="검색어"/>
				<a href="#none" onclick="doSearchList()"><img src="/images/user/btn_search.gif" alt="검색" /></a>
			</div>
		</div>
		<!-- // search wrap -->		
		
		<!-- list table-->
		<div class="tbList">
			<table summary="번호, 제목, 첨부파일, 작성자 , 등록일로 구분" cellspacing="0" width="100%">
            	<caption>공지사항 목록</caption>
				<colgroup>
					<col width="10%" />
					<col width="54%" />
					<col width="8%" />
					<col width="8%" />
					<col width="10%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="col">번호</th>
						<th scope="col">제목</th>
						<th scope="col">첨부파일</th>
						<th scope="col">작성자</th>
						<th scope="col">등록일</th>
					</tr>
				</thead>
				<tbody>
				
			<c:forEach items="${allList}" var="result" varStatus="i">
				<tr>
					<td >*</td>
					<td class="left">
					<c:if test="${result.noticeGubun eq 'A'}"><img src="/images/adm/ico/ico_notice.gif" alt="공지" /></c:if>
					<c:if test="${result.noticeGubun eq 'B'}"><img src="/images/adm/ico/ico_event.gif" alt="이벤트" /></c:if>
					<c:if test="${result.noticeGubun eq 'C'}"><img src="/images/adm/ico/ico_happy.gif" alt="축하" /></c:if>
					<c:if test="${result.noticeGubun eq 'D'}"><img src="/images/adm/ico/ico_guide.gif" alt="안내" /></c:if>
					<c:if test="${result.noticeGubun eq 'E'}"><img src="/images/adm/ico/ico_poll.gif" alt="설문" /></c:if>
					<c:if test="${result.noticeGubun eq 'F'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
					<c:if test="${result.noticeGubun eq 'G'}"><img src="/images/adm/ico/ico_others.gif" alt="기타" /></c:if>
					<a href="#none" onclick="doView('${result.seq}')">
					${result.adtitle}
					</a>
					</td>
					<td >
						<c:if test="${result.filecnt > 0}">
						<img src="/images/user/icon_file.gif" alt="첨부파일"/>
						</c:if>
						<c:if test="${result.filecnt == 0}">-</c:if>
					</td>
					<td >${result.adname}</td>
					<td >${fn2:getFormatDate(view.addate, 'yyyy.MM.dd')}</td>
					
				</tr>
			</c:forEach>
			<c:forEach items="${list}" var="result" varStatus="i">
				<tr>
					<td >${totCnt - result.rn + 1}</td>
					<td class="left">
					<c:if test="${result.noticeGubun eq 'A'}"><img src="/images/adm/ico/ico_notice.gif" alt="공지" /></c:if>
					<c:if test="${result.noticeGubun eq 'B'}"><img src="/images/adm/ico/ico_event.gif" alt="이벤트" /></c:if>
					<c:if test="${result.noticeGubun eq 'C'}"><img src="/images/adm/ico/ico_happy.gif" alt="축하" /></c:if>
					<c:if test="${result.noticeGubun eq 'D'}"><img src="/images/adm/ico/ico_guide.gif" alt="안내" /></c:if>
					<c:if test="${result.noticeGubun eq 'E'}"><img src="/images/adm/ico/ico_poll.gif" alt="설문" /></c:if>
					<c:if test="${result.noticeGubun eq 'F'}"><img src="/images/adm/ico/ico_busy.gif" alt="긴급" /></c:if>
					<c:if test="${result.noticeGubun eq 'G'}"><img src="/images/adm/ico/ico_others.gif" alt="기타" /></c:if>
					<a href="#none" onclick="doView('${result.seq}')">
					${result.adtitle}
					</a>
					</td>
					<td >
						<c:if test="${result.filecnt > 0}">
						<img src="/images/user/icon_file.gif" alt="첨부파일"/>
						</c:if>
						<c:if test="${result.filecnt == 0}">-</c:if>
					</td>
					<td >${result.adname}</td>
					<td >${fn2:getFormatDate(view.addate, 'yyyy.MM.dd')}</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list && empty allList}">
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
           
</form>

<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');

function doPageList() {

	thisForm.action = "/usr/bod/noticeList.do";
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
			thisForm.action = "/usr/bod/noticeList.do";
			thisForm.target = "_self";
			thisForm.pageIndex.value = "1";
			thisForm.submit();
			
		}
	}else{
		
			thisForm.action = "/usr/bod/noticeList.do";
			thisForm.target = "_self";
			thisForm.pageIndex.value = "1";
			thisForm.submit();
	}
	
}

function doLinkPage(index) {
	thisForm.action = "/usr/bod/noticeList.do";
	thisForm.pageIndex.value = index;
	thisForm.target = "_self";
	thisForm.submit();
}



function doView(p_seq){
	thisForm.p_seq.value = p_seq;
	thisForm.action = "/usr/bod/noticeView.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//-->
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->