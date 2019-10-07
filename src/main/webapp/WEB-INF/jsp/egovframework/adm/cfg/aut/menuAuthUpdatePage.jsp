<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	
	<div class="listTop">	
	    <div class="btnR"><a href="#" class="btn01" onclick="menuPage()"><span>메뉴코드리스트</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="update()"><span>저장</span></a></div>
	</div>
	
	<!-- list table-->
	<div class="tbList">
		<table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
				<col width="5%" />
				<col width=""/>
				<c:forEach begin="1" end="${fn:length(gadminList)}">
				<col width="<c:out value="${75 / fn:length(gadminList)}"/>%" />
				</c:forEach>
			</colgroup>
			<thead>
				<tr>
					<th scope="row" rowspan="2">구분</th>
					<th scope="row" rowspan="2">메뉴코드명</th>
					<th scope="row" colspan="<c:out value="${fn:length(gadminList)}"/>">권한</th>
				</tr>
				<tr>
				<c:forEach items="${gadminList}" var="result">
					<th scope="row"><c:out value="${result.gadminnm}"/></th>
				</c:forEach>
				</tr>
			</thead>
			<tbody>
			<c:set var="menuCount" value="1"/>
			<%int row = 0;%>
			<c:forEach items="${list}" var="result" varStatus="listRow">
				<c:set var="color" value="#FFFFFF"/>
				<c:if test="${result.levels eq 1 and result.seq eq 0}"><c:set var="color" value="#E1FFFF"/></c:if>
				<c:if test="${result.levels eq 2 and result.seq eq 0}"><c:set var="color" value="#E7E7E7"/></c:if>
				<tr bgcolor="<c:out value="${color}"/>">
					<td>
						<c:choose>
							<c:when test="${result.levels eq 1 and result.seq eq 0}"><b><c:out value="${menuCount}"/></b><c:set var="menuCount" value="${menuCount + 1}"/></c:when>
							<c:when test="${result.levels eq 2 and result.seq eq 0}">메뉴</c:when>
							<c:otherwise>모듈</c:otherwise>
						</c:choose>
					</td>
					<td class="left"><c:out value="${result.menunm}"/></td>
					<%
					List list = (ArrayList)request.getAttribute("list");
					List gadminList = (ArrayList)request.getAttribute("gadminList");
					for( int i=0; i<gadminList.size(); i++ ){
					%>
						<td>
					<%
							String rCheck = "";
							String wCheck = "";
							Map m = (Map)list.get(row);
							Map gadmin = (Map)gadminList.get(i);
							String chk = m.get("control"+i) + "";
							
							if(chk != null && !"".equals(chk))
							{
								if( chk.equals("r") || chk.equals("rw") ) 
									rCheck = "checked";
								if( chk.equals("w") || chk.equals("rw") ) 
									wCheck = "checked";
							}
					%>
							<input type="hidden" name="p_menu<%=gadmin.get("gadmin")%>" value="${result.menu}">
                        	<input type="hidden" name="p_menusubseq<%=gadmin.get("gadmin")%>" value="${result.seq }">
	                        <input type="hidden" name="p_gadmin<%=gadmin.get("gadmin")%>" value="<%=gadmin.get("gadmin")%>">
							<c:if test="${result.seq eq 0}">
							View
							<input type="checkbox" name="p_<%=gadmin.get("gadmin")%>R<%=row%>" <%=rCheck%> value="r"/>
							<input type="hidden"   name="p_<%=gadmin.get("gadmin")%>W<%=row%>" <%=wCheck%> value="w"/>
							</c:if>
							<c:if test="${result.seq ne 0}">
							R <input type="checkbox" name="p_<%=gadmin.get("gadmin")%>R<%=row%>" <%=rCheck%> value="r"/>
							W <input type="checkbox" name="p_<%=gadmin.get("gadmin")%>W<%=row%>" <%=wCheck%> value="w"/>
							</c:if>
						</td>
					<%	
					}
					row++;
					%>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- list table-->
</form>
	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">

	function menuPage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/amm/adminMenuMngList.do";
		frm.submit();
	}

	function update(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/aut/menuAuthorityUpdate.do";
		frm.submit();
	}
</script>