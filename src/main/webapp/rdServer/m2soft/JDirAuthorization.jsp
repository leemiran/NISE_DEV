<%@page import="m2soft.rdsystem.server.core.rddbagent.beans.*" %>
<%@ include file="include.jsp"%>

<html>
	<head>
		<style type="text/css">
			<%@ include file="default.css" %>
		</style>
		</head>
	<title><%= Message.get("JDirAuthorization_01") %></title>

	<%
		Vector vDirUser = new Vector();
		Vector vScUser = new Vector();
		String dir = request.getParameter("dir");
		String userlist = request.getParameter("userlist");
		String opcode = request.getParameter("opcode");
		
		//kokim 2004.10.15
		//Authority Management of Directory by file 
		// in the case of management by file
		if(docinfo.equals("no"))
		{
			BeansDirAuthentication DirAuth = new BeansDirAuthentication();
			if(opcode != null && opcode.equals("delete"))
				DirAuth.delete(dir, userlist);
			if(opcode != null && opcode.equals("insert"))
				DirAuth.insert(dir, userlist);
			
			vDirUser = DirAuth.getDirectoryUser(dir);
			vScUser = DirAuth.getNoDirectoryUser(dir);
		}
		// in the case of management by DB
		else
		{
			Directoryauthorization DirAuth = new Directoryauthorization();
			if(opcode != null && opcode.equals("delete"))
				DirAuth.delete(dir, userlist, servicename);
			if(opcode != null && opcode.equals("insert"))
				DirAuth.insert(dir, userlist, servicename);
			
			vDirUser = DirAuth.getDirectoryUser(dir, servicename);
			vScUser = DirAuth.getNoDirectoryUser(dir);
		}

		
		if(vDirUser.size() == 0)
		{
			out.println(Message.get("JDirAuthorization_04"));
		}
		else
		{
	%>	

	<script language="JavaScript">
		function AddUser()
		{
			var n = document.UserList.elements.length;
			var str = "";
			var cnt = 1;
			for (var i=0; i<n; i++) {
				if (document.UserList.elements[i].checked && document.UserList.elements[i].name == "scuserid") {
					if ( cnt == 1 ) 	
						str += document.UserList.elements[i].value;
					else 
						str += ";" + document.UserList.elements[i].value;
					cnt++;
				}
			}
			if(cnt == 1)
				return;			
			<% dir = dir.replace('\\', '/'); %>
			document.UserList.action="JDirAuthorization.jsp?dir=<%=dir%>&opcode=insert&userlist="+str+";";
			window.open(document.UserList.action,window.name);
		}
		
		function DelUser()
		{
			var n = document.UserList.elements.length;
			var str = "";
			var cnt = 1;
			for (var i=0; i<n; i++) {
				if (document.UserList.elements[i].checked && document.UserList.elements[i].name == "diruserid") {
					if ( cnt == 1 ) 	
						str += document.UserList.elements[i].value;
					else
						str += ";" + document.UserList.elements[i].value;
					cnt++;
				}
			}
			if(cnt == 1)
				return;
			<% dir = dir.replace('\\', '/'); %>
			document.UserList.action="JDirAuthorization.jsp?dir=<%=dir%>&opcode=delete&userlist="+str+";";
			window.open(document.UserList.action,window.name);
		}
		
	</script>

	<body scroll=auto>
	<form name="UserList">
	<table border="0" width=100% cellspacing="1" cellpadding="0" bordercolordark="white" bordercolorlight="7c7c7c">
		<tr bgcolor=<%=m_sBtnFace%>>
			<td align=center width="40%" height=25 border=0><b><font color=white><%= Message.get("JDirAuthorization_02") %></td>
			<td align=center width="20%" height=25 border=0><b><font color=white></td>
			<td align=center width="40%" height=25 border=0><b><font color=white><%= Message.get("JDirAuthorization_03") %></td>
		</tr>
	
	<%
		int count = 0;
		if(vDirUser.size() > vScUser.size())
			count = vDirUser.size();
		else
			count = vScUser.size();
			
		if(count < 2)
			count = 2;
			
		for(int i=0; i<count; i++)
		{
			out.println("<tr>");
			
			if(i < vDirUser.size())
				out.println("<td width=\"40%\"><input type=checkbox name=diruserid value="+(String)vDirUser.elementAt(i)+" class=\"style1\">"+(String)vDirUser.elementAt(i)+"</td>");
			else
				out.println("<td width=\"40%\"></td>");

			if(i == 0)
				out.println("<td align=center width=\"20%\"><a href=\"javascript:AddUser();\"><img src=\"images/arrow_left.gif\" border=0></td>");
			else if(i == 1)
				out.println("<td align=center width=\"20%\"><a href=\"javascript:DelUser();\"><img src=\"images/arrow_right.gif\" border=0></td>");
			else
				out.println("<td align=center width=\"20%\"></td>");

			if(i < vScUser.size())
				out.println("<td width=\"40%\"><input type=checkbox name=scuserid value="+(String)vScUser.elementAt(i)+" class=\"style1\">"+(String)vScUser.elementAt(i)+"</td>");
			else
				out.println("<td width=\"40%\"></td>");
				
			out.println("</tr>");
		}			
	%>
	</table>
	<center><input type="button" name="ok" value="O K" onClick="window.close()"></center>
	</form>
	</body>
	<% } %>
</html>