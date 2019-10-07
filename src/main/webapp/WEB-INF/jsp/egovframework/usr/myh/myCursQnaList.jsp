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
<fieldset>
<legend>나의질문</legend>
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_seq"     value = "" />
	<input type = "hidden" name="p_tabseq"     value = "" />
	<input type = "hidden" name="p_kind"     value = "" />
	
	
	<!-- 	<div class="sub_title">
			<ul>
                <li class="tred">※ 연수 중 질문내역입니다.</li>
            </ul>
		</div> -->
		
		
		<!-- search wrap-->
		<div class="searchWrap">
			<div class="in">
					<select name="p_search" title="항목선택">
	                      <option value='title' 	<c:if test="${p_search eq 'title'}">selected</c:if>>제목</option>
	                      <option value='content' 	<c:if test="${p_search eq 'content'}">selected</c:if>>내용</option>
	              </select>
					<input type="text" name="p_searchtext" id="p_searchtext" value="<c:out value="${p_searchtext}"  escapeXml="true" />" size="50" onkeypress="fn_keyEvent('doPageList')" style="ime-mode:active;" title="검색어"/>
				<a href="#none" onclick="doPageList()"><img src="/images/user/btn_search.gif" alt="검색" /></a>
			</div>
		</div>
		<!-- // search wrap -->
		
		
		<!-- list table-->
		<div class="tbList">
			<table summary="번호, 과정, 제목, 등록일, 조회수로 구성" cellspacing="0" width="100%">
				<caption>나의질문내역</caption>
                <colgroup>
					<col width="7%" />
					<col width="38%" />
					<col width="38%" />
					<col width="10%" />
                    <col width="10%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="col">번호</th>
						<th scope="col">과정</th>
						<th scope="col">제목</th>
						<th scope="col">등록일</th>
                        <th scope="col">조회수</th>
					</tr>
				</thead>
				<tbody>
			<c:forEach items="${list}" var="result" varStatus="status">
				<tr>
						<td class="num"><c:out value="${(pageTotCnt+1)-result.rn}"/></td>
						<td class="left">
							<c:out value="${result.subjnm}"/>
						</td>
						<td class="left">
							<%-- <a href="#" onclick="doPageView(<c:out value="${result.tabseq}"/>, <c:out value="${result.seq}"/>)"> --%>
							<a href="#" onclick="doQnaPageView(<c:out value="${result.tabseq}"/>, <c:out value="${result.seq}"/>, <c:out value="${result.kind}"/>)">
								<c:out value="${result.title}" escapeXml="true" />
							</a>							
							
						</td>
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
</fieldset>       
</form>



        
        
<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');



function doPageList() {
	thisForm.action = "/usr/myh/myCursQnaList.do";
	thisForm.pageIndex.value = '1';
	thisForm.target = "_self";
	thisForm.submit();
}


function doLinkPage(index) {
	thisForm.action = "/usr/myh/myCursQnaList.do";
	thisForm.pageIndex.value = index;
	thisForm.target = "_self";
	thisForm.submit();
}



function doPageView(p_tabseq, p_seq){
	thisForm.p_tabseq.value = p_tabseq;
	thisForm.p_seq.value = p_seq;
	thisForm.action = "/usr/myh/myCursQnaView.do";
	thisForm.target = "_self";
	thisForm.submit();
}


function doQnaPageView(p_tabseq, p_seq, p_kind){
	thisForm.p_tabseq.value = p_tabseq;
	thisForm.p_seq.value = p_seq;
	thisForm.p_kind.value = p_kind;
	thisForm.action = "/usr/myh/myCursQnaView.do";
	thisForm.target = "_self";
	thisForm.submit();
}


//-->
</script>
        
        
        
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
