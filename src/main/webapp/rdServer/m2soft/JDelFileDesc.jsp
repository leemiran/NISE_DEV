<%@page import="java.lang.*,java.io.*,java.util.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.beans.*"%>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@include file="properties.h"%>
<% 
	String filename = request.getParameter("filename");

	// in the case of management by file
	if(docinfo.equals("no"))
	{
		BeansFileDescription fd = new BeansFileDescription();
		fd.delete(filename);
	}
	// in the case of management by DB
	else
	{
		FileDescription fd = new FileDescription();
		fd.delete(filename, servicename);
	}

	out.println("<script>window.location='JMainTree.jsp';</script>");
%>
