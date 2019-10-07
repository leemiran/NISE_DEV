<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");doPageList();</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="p_action" 	id="p_action">
	<input type="hidden" name="p_subj" 		id="p_subj">
	<input type="hidden" name="p_year" 		id="p_year">
	<input type="hidden" name="p_subjseq" 	id="p_subjseq">
	<input type="hidden" name="p_subjnm" 	id="p_subjnm">
	<input type="hidden" name="p_subjseq2" 	id="p_subjseq2">
	
	<!-- 검색박스 시작-->
	<c:import url="/com/sch/admSearchBars.do">
		<c:param name="selectViewType"      value="AA"							>조회조건타입 : 타입별 세부조회조건		</c:param>
	</c:import>
	<!-- 검색박스 끝 -->
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="40px" />
				<col />
				<col width="50px" />
<!--				<col width="50px" />-->
				<c:forEach items="${noticeList}" var="result">
				<col width="${350 / fn:length(noticeList)}px" />
				</c:forEach>
			</colgroup>
			<thead>
				<tr>
					<th rowspan="2">No</th>
					<th rowspan="2">과정</th>
					<th rowspan="2">기수</th>
<!--					<th rowspan="2">교육구분</th>-->
					<th colspan="${fn:length(noticeList)}">공지구분</th>
				</tr>
				<tr>
					<c:forEach items="${noticeList}" var="result">
					<th>${result.codenm}</th>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
			<%
			List list = (ArrayList)request.getAttribute("list");
			if( list != null && list.size() > 0 ){
			for( int i=0; i<list.size(); i++ ){
				Map m = (Map)list.get(i);
				String isonoff = String.valueOf(m.get("isonoff"));
			%>
				<tr>
					<td><%=i+1%></td>
					<td class="left">
						<a href="#none" onclick="subList('<%=m.get("subj")%>','<%=m.get("year")%>','<%=m.get("subjseq")%>','<%=m.get("subjnm")%>','<%=m.get("seq")%>')">
							<%=m.get("subjnm")%>
						</a>
					</td>
					<td><%=m.get("seq")%>기</td>
<!--					<td><%=isonoff.equals("ON") ? "이러닝" : isonoff.equals("OFF") ? "집합" : "독서교육"%></td>-->
					<%
					String[] types = String.valueOf(m.get("types")).split("/");
					String[] typesCnt = String.valueOf(m.get("typescnt")).split("/");
					List notice = (ArrayList)request.getAttribute("noticeList");
					for( int j=0; j<notice.size(); j++ ){
						Map noticeMap = (Map)notice.get(j);
						String code = String.valueOf(noticeMap.get("code"));
						for( int k=0; k<types.length; k++ ){
							if( types[k].equals(code) ){
								String cnt = typesCnt[k].substring(1, typesCnt[k].length());
					%>
					<td><%=cnt.equals("") ? 0 : cnt%></td>
					<%
							}
						}
					}
					%>
				</tr>
			<%
			}
			}
			%>
			<c:if test="${empty list}">
				<tr>
					<td colspan="${fn:length(noticeList)+4}">조회된 내용이 없습니다.</td>
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

	/* ********************************************************
	 * 페이징처리 함수
	 ******************************************************** */
	 function doPageList() {
		 var frm = eval('document.<c:out value="${gsMainForm}"/>');
		 frm.action = "/adm/cmg/not/courseNoticeList.do";
		 frm.target = "_self";
		 frm.submit();
	}

	function subList(subj, year, subjseq, subjnm, p_subjseq2){
		 var frm = eval('document.<c:out value="${gsMainForm}"/>');
		 frm.p_subj.value = subj;
		 frm.p_year.value = year;
		 frm.p_subjseq.value = subjseq;
		 frm.p_subjnm.value = subjnm;
		 frm.p_subjseq2.value = p_subjseq2;
		 frm.action = "/adm/cmg/not/courseNoticeSubList.do";
		 frm.target = "_self";
		 frm.submit();
	}
	
</script>