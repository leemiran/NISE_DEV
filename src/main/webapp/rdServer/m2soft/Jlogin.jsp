<%@page import="java.io.*,java.util.*,m2soft.rdsystem.server.core.rddbagent.beans.*,m2soft.rdsystem.server.core.install.Message" %> 
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@include file="properties.h"%>
<%
	String user = request.getParameter("USER");
	String pass = request.getParameter("PASSWORD");
%>
<jsp:useBean id="rdAuth" class="m2soft.rdsystem.server.core.rddbagent.beans.BeansAuthentication" />
<jsp:setProperty name="rdAuth" property="servicename" value="<%=servicename%>" />
<jsp:setProperty name="rdAuth" property="name" value="<%=user%>" />
<jsp:setProperty name="rdAuth" property="pass" value="<%=pass%>" />
<jsp:setProperty name="rdAuth" property="writer" value="<%=out%>" />
<jsp:setProperty name="rdAuth" property="session" value="<%=session%>" />
<jsp:setProperty name="rdAuth" property="request" value="<%=request%>" />
   
<%
	String resultString = "";
	
	try{
   		resultString = rdAuth.getRoleDllAuthentication(user,pass);
		if( resultString.equals("Users") || resultString.equals("Administrator") )
		{
                        session.putValue("sc.user",user);
                        session.putValue("sc.group",resultString);
	                out.println("<script>window.location='JMainFrame.jsp';</script>");          
		}
   	} catch (Exception e) {
		out.println("<html><head>");
		out.println("<style type='text/css'> include file='default.css' </style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<center><br><br><table>");
		out.println("<td>");
		out.println("<center> "+e.getMessage()+" <img src='images/itemplet.gif' hspace=0 vspace=0><a href=index.jsp>[LOGIN]</a>");
		out.println("</td>");
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");
   	}
%>