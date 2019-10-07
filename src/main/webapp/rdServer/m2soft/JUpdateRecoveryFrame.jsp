<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File, m2soft.rdsystem.server.log.l.*,m2soft.rdsystem.server.core.install.Message" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>

<html>
<head>
<%
	String mode = request.getParameter("mode");
%>
</head>

<body bgcolor="#d4d0c8" text="#d4d0c8" link="blue" vlink="purple" alink="red">

<%
if(mode.equals("update"))
{
   out.println("<iframe frameborder=0 height=438 scrolling=auto src='JUpdateStep2.jsp?checkstep=step2' width=723 name=body STYLE='border-style:solid;border-width:1;'></iframe><br>");
} else if(mode.equals("recovery")) {
   String date = request.getParameter("date");
   out.println("<iframe frameborder=0 height=438 scrolling=auto src='JUpdateRecoveryStep2.jsp?checkstep=step2&date="+date+"' width=723 name=body STYLE='border-style:solid;border-width:1;'></iframe><br>");
} else {
   response.sendRedirect("JUpdateMain.jsp");
}
%>

</body>
</html>