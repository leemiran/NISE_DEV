<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_menu" 		name="p_menu" 			value="<c:out value="${p_menu}"/>"/>
	<input type="hidden" id="p_systemgubun" name="p_systemgubun" 	value="<c:out value="${p_systemgubun}"/>"/>
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>취소</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="insertData()"><span>저장</span></a></div>
	</div>
	
	<!-- detail wrap -->
    <div class="tbDetail">
        <table summary="" cellspacing="0" width="100%">
                <caption>목록</caption>
                <colgroup>
                <col width="150px" />
                <col width="" />
            </colgroup>
            <tbody>
                <tr class="title">
                    <th>메뉴코드</th>
                    <td><c:out value="${p_menu}"/></td>
                </tr>
                <tr class="title">
                    <th>메뉴코드명</th>
                    <td><c:out value="${menuName}"/></td>
                </tr>
                <tr class="title">
                    <th>모듈명</th>
                    <td><input type="text" name="p_modulenm" id="p_modulenm" style="width:80%;ime-mode:active"/></td>
                </tr>
                <tr class="title">
                    <th>서블릿</th>
                    <td><input type="text" name="p_servlet" id="p_servlet" style="width:80%;ime-mode:disabled"/></td>
                </tr>
                <tr class="title">
                    <th colspan="2" style="text-align: center">권&nbsp;&nbsp;&nbsp;&nbsp;한</th>
                </tr>
                <%
				List authList = (ArrayList)request.getAttribute("authList");
				for( int i=0; i<authList.size(); i++ ){
				%>
				<%
						String rCheck = "";
						String wCheck = "";
						Map auth = (Map)authList.get(i);
						String gadmin = (String)auth.get("gadmin");
				%>
                <tr class="title">
                    <th><%=auth.get("gadminnm")%></th>
					<td>
						<input type="hidden"   id="p_gadmin" name="p_gadmin" value="<%=gadmin%>">
						<input type="checkbox" id="p_<%=gadmin%>R" name="p_<%=gadmin%>R" value="r"/>R
						&nbsp;
						<input type="checkbox" id="p_<%=gadmin%>W" name="p_<%=gadmin%>W" value="w"/>W
					</td>
                </tr>
				<%	
				}
				%>
            </tbody>
        </table>				
    </div>
    <!-- // detail wrap -->
	<!-- list table-->
</form>

	<!-- // button -->
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp" %>

<script type="text/javascript">


	function pageList(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
		frm.action = "/adm/cfg/mod/menuModuleSubList.do";
		frm.submit();
	}

	function insertData(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
        frm.action = "/adm/cfg/mod/menuModuleInsert.do";
        frm.submit();
	}

</script>