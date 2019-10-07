<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page import="egovframework.com.utl.cas.service.EgovSessionCookieUtil" %>
 <%
    String userId = (String)session.getAttribute("suserId");
 %>

<form id="login"  Name="login" method="post">  
</form>
  
<script type="text/javaScript" language="javascript" >
<!-- 
var varFrom = document.getElementById("login");
var userid = "<%=userId%>";

if (userid == "" ) {
    varFrom.action = "<c:url value='/adm/lgn/LoginUsr.do?login_error=1'/>";
}else{
    varFrom.action = "<c:url value='/adm/lgn/actionMain.do'/>";
}

varFrom.submit();      

-->
</script>


