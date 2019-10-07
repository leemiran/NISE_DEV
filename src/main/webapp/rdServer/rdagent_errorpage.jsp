<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page language="java" isErrorPage="true" %>
<%@ page import="java.lang.Exception,java.util.*,java.sql.*,m2soft.rdsystem.server.core.install.Message" %>
<%!
    String rdagent_errorpage_01 = Message.get("rdagent_errorpage_01");
%>  

<%
   out.print(rdagent_errorpage_01);
	exception.printStackTrace();
	// JspException
	if( exception instanceof JspException) {	
		exception = ((JspException)exception).getRootCause();
      exception.printStackTrace();
	}

	// CoreException 
	if( exception instanceof Exception ) {
	  
		exception.printStackTrace();	
      out.print(exception.getMessage());
      
   } else if ( exception instanceof ServletException || exception instanceof IllegalStateException){ exception.printStackTrace();
   %> <%= Message.get("rdagent_errorpage_02") %> <%=exception.getMessage()%>
<%	}%>