<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logout check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
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
	<input type="hidden" id="pageIndex" name="pageIndex" />
	<input type = "hidden" name="p_seq"     value = "" />
	<input type = "hidden" name="p_tabseq"     value = "" />
		<!-- list table-->
		<div class="tbList">
			<table summary="번호, 제목, 상태, 작성자, 등록일, 조회수로 구성" cellspacing="0" width="100%">
				<caption>나의 상담내역</caption>
                <colgroup>
					<col width="10%" />
					<col width="52%" />
					<col width="10%" />
					<col width="8%" />
					<col width="10%" />
                    <col width="10%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="row">번호</th>
						<th scope="row">제목</th>
						<th scope="row">상태</th>
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
							<a href="#" onclick="doPageView(<c:out value="${result.tabseq}"/>, <c:out value="${result.seq}"/>)">
							<c:out value="${result.title}"/>
							</a>
						</td>
						<td>						
							<c:if test="${result.hasanswer eq 'Y'}">
								<img src="/images/user/end.gif" alt="완료"/>
							</c:if>
							<c:if test="${result.hasanswer ne 'Y'}">
								<img src="/images/user/ing.gif" alt="처리중"/>
							</c:if>						
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
</form>
       
<script type="text/javascript">
//<![CDATA[
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
function doLinkPage(index) {
	thisForm.action = "/usr/mpg/memMyQnaList.do";
	thisForm.pageIndex.value = index;
	thisForm.target = "_self";
	thisForm.submit();
}
function doPageView(p_tabseq, p_seq){
	thisForm.p_tabseq.value = p_tabseq;
	thisForm.p_seq.value = p_seq;
	thisForm.action = "/usr/mpg/memMyQnaView.do";
	thisForm.target = "_self";
	thisForm.submit();
}

//]]>
</script>
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
