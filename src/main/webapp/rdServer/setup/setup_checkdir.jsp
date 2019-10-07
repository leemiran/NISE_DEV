<%@ page import="java.io.*" contentType="text/html; charset=euc-kr" %>
<%
  String checking_dir = request.getParameter("server_dir");
  String chktype = request.getParameter("chktype");
%>
<html>
	<style>
	<%@ include file="setup.css" %>
	</style>
<head><title>Directory Check</title></head>
<script language="JavaScript">
<%
	if(chktype != null && chktype.equals("step1"))
	{
	   out.println("function exit(){");

      out.println("opener.document.step1.server_home.focus();");

	  	out.println("close();}");
  	}
  	else
  	{
  		if(chktype != null && chktype.equals("step2"))
  		{
			out.println("function exit(){");

			out.println("opener.document.step2.server_lib.focus();");

			out.println("close();}");
	  	}
  	}

%>
</script>
<body>
<center>
<table>
<td align=center>
<%
  if(new File(checking_dir).exists())
  		out.println("The specified directory does exist.");
  else
  {
  	   if(chktype != null && chktype.equals("step1")) 
  			out.println("The specified directory does not exist.<br> Please check the entered RDServer Home.");

  		if(chktype != null && chktype.equals("step2")) 
  		{
  			out.println("The specified directory does not exist.<br> Please check the entered CLASSPATH (Directory).");
  		}

  }
%>
<br>
<a href="javascript:exit();"><img src="images/enter_up.gif" border=0 align=center></a>
</td>
</table>
</body>
</html>

