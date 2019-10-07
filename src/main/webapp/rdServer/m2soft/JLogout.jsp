<%-- the realization of user authentication JSP--%>
<%@page import="java.io.*,java.util.*,m2soft.rdsystem.server.core.rddbagent.beans.*,m2soft.rdsystem.server.core.install.*" %> 
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page session="true" %>
<% session.invalidate(); 
   out.println("<script>window.location='index.jsp';</script>");

%>