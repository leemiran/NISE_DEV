<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp" %>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" action="">
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" id="p_menu" 		name="p_menu" 		value="<c:out value="${p_menu}"/>"/>
	<input type="hidden" id="p_seq" 		name="p_seq" 		value="<c:out value="${p_seq}"/>"/>
	<!-- // search wrap -->
	<div class="listTop">			
	    <div class="btnR"><a href="#" class="btn01" onclick="pageList()"><span>목록</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="deleteData()"><span>삭제</span></a></div>
		<div class="btnR"><a href="#" class="btn01" onclick="updatePage()"><span>수정</span></a></div>
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
                    <td><c:out value="${view.menu}"/></td>
                </tr>
                <tr class="title">
                    <th>메뉴코드명</th>
                    <td><c:out value="${menuName}"/></td>
                </tr>
                <tr class="title">
                    <th>모듈번호</th>
                    <td><c:out value="${view.seq}"/></td>
                </tr>
                <tr class="title">
                    <th>모듈명</th>
                    <td><c:out value="${view.modulenm}"/></td>
                </tr>
                <tr class="title">
                    <th>서블릿</th>
                    <td><c:out value="${view.servlet}"/></td>
                </tr>
                <tr class="title">
                    <th colspan="2">권한</th>
                </tr>
                <%
				List authList = (ArrayList)request.getAttribute("authList");
				for( int i=0; i<authList.size(); i++ ){
				%>
				<%
						String rCheck = "";
						String wCheck = "";
						Map auth = (Map)authList.get(i);
						if( auth.get("control") != null ){
							String chk = (String)auth.get("control");
							if( chk.equals("r") || chk.equals("rw") ) rCheck = "읽기";
							if( chk.equals("w") || chk.equals("rw") ) wCheck = "쓰기";
						}
				%>
                <tr class="title">
                    <th><%=auth.get("gadminnm")%></th>
					<td><%=rCheck%>&nbsp;&nbsp;<%=wCheck%></td>
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

	function updatePage(){
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
        frm.action = "/adm/cfg/mod/menuModuleUpdatePage.do";
        frm.submit();
	}

	function deleteData(){
		if( !confirm("정말 삭제하시겠습니까?") ){
			return;
		}
		var frm = eval('document.<c:out value="${gsMainForm}"/>');
        frm.action = "/adm/cfg/mod/menuModuleDelete.do";
        frm.submit();
	}
</script>