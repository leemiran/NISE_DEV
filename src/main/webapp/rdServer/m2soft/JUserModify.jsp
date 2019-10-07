<%@page import="m2soft.rdsystem.server.core.rddbagent.beans.*" %>
<%@ include file="include.jsp"%>
<html>

	<head>
		<style type="text/css">
			<%@ include file="default.css" %>
		</style>
	</head>

	<title>
	<%= Message.get("JUserModify_01") %>
	</title>

	<%

		String opcode = request.getParameter("opcode");
		String userid = request.getParameter("userid");
		String username = request.getParameter("username");
		String useremail = request.getParameter("useremail");
		String userpass = request.getParameter("userpass1");
		String usergroup = request.getParameter("usergroup");

      if(dbencoding)
      {
         userid = RDUtil.toHangleDecode(userid);
         username = RDUtil.toHangleDecode(username);
         useremail = RDUtil.toHangleDecode(useremail);
         userpass = RDUtil.toHangleDecode(userpass);
         usergroup = RDUtil.toHangleDecode(usergroup);
      }
	%>

   <jsp:useBean id="scUser" scope="page" class="m2soft.rdsystem.server.core.rddbagent.beans.BeansScuser" />
   <jsp:setProperty name="scUser" property="servicename" value="<%=servicename%>" />
   <jsp:setProperty name="scUser" property="logpath"     value="<%=logpath%>"     />
   <jsp:setProperty name="scUser" property="id"          value="<%=userid%>"      />

	<%if(opcode != null && opcode.equals("update")) { %>
	   <jsp:setProperty name="scUser" property="name"      value="<%=username%>"  />
	   <jsp:setProperty name="scUser" property="email"     value="<%=useremail%>" />
	   <jsp:setProperty name="scUser" property="password"  value="<%=userpass%>"  />
	   <jsp:setProperty name="scUser" property="usergroup" value="<%=usergroup%>" />
	<%
		scUser.getUpdate();
	   out.println("<script>var oPenerFrame = opener;</script>");
      out.println("<script>oPenerFrame.location ='JManageAddress.jsp';</script>");
      out.println("<script>self.close();</script>");
		}
	%>

	<%
		scUser.getSelectFromId();
	%>

	<script language="JavaScript">
		function onok()
		{
			if(!document.modifyuser.userid.value){
				alert(document.setmessage.scuser_12.value);
				document.modifyuser.userid.focus();
				return;
			}

			if(!document.modifyuser.username.value){
				alert(document.setmessage.scuser_13.value);
				document.modifyuser.username.focus();
				return;
			}

			if(!document.modifyuser.useremail.value){
				alert(document.setmessage.scuser_14.value);
				document.modifyuser.useremail.focus();
				return;
			}

         if(document.modifyuser.userpass1.value != document.modifyuser.userpass2.value){
            alert(document.setmessage.scuser_19.value);
            document.modifyuser.userpass1.focus();
            return;
         }

         document.modifyuser.submit();
		}

		function oncancel()
		{
			self.close();
		}
	</script>

	<body scroll=auto>

		<form name="setmessage">
			<input type="hidden" name="scuser_12" value=<%= Message.get("scuser_12") %> >
			<input type="hidden" name="scuser_13" value=<%= Message.get("scuser_13") %> >
			<input type="hidden" name="scuser_14" value=<%= Message.get("scuser_14") %> >
		   <input type="hidden" name="scuser_19" value=<%= Message.get("scuser_19") %> >
		</form>

		<form name="modifyuser" method="post" action="JUserModify.jsp?opcode=update">
		<table border="0"  width=100% cellspacing="1" cellpadding="1" bordercolordark="white" bordercolorlight="7c7c7c">

		   <tr bgcolor=<%=m_sBtnFace%>>
		      <td align=center width=40% height=25><b><font color=white><%= Message.get("scuser_07") %></td>
		      <td align=center wdith=60%><b><font color=white><%= Message.get("scuser_08") %></td>
		   </tr>
		   <tr>
				<td>
				<%= Message.get("scuser_09") %> <!-- user id -->
				</td>

				<td>
				<input type="text" name="userid" value="<%=scUser.getId()%>" size="50" class=style1 >
				</td>
			</tr>
			<tr>
				<td>
				<%= Message.get("scuser_10") %> <!-- user name -->
				</td>

				<td>
				<input type="text" name="username" value="<%=scUser.getName()%>" size="50" class=style1 >
				</td>
			</tr>
			<tr>
				<td>
				<%= Message.get("scuser_11") %> <!-- user email -->
				</td>

				<td>
				<input type="text" name="useremail" value="<%=scUser.getEmail()%>" size="50" class=style1 >
				</td>
			</tr>
			<tr>
				<td>
				<%= Message.get("envfiledb_09") %> <!-- pwassword -->
				</td>

				<td>
				<input type="password" name="userpass1" maxlength=29 size=12 class="style1">
				Confirm Passwrd&nbsp;
				<input type="password" name="userpass2" maxlength=29 size=12 class="style1">
				</td>
			</tr>
			<tr>
				<td>
				<%= Message.get("scuser_18") %> <!-- user group -->
				</td>

				<td>
				<select name="usergroup" class=style1>
				<% if(scUser.getUsergroup().equals("Users")) { %>
					<option value="Users" selected>Users</option>
					<option value="Administrator">Administrator</option>
				<% }else{ %>
					<option value="Users">Users</option>
					<option value="Administrator" selected>Administrator</option>
				<% } %>
				</select>
				</td>
			</tr>

		</table>
		</form>

		<%
      JLButton btnadd = new JLButton();
      btnadd.printButton(100,150,"ok",    105,3,Message.get("JUserModify_02"),"onok()"    ,"images/button_icon_add.gif",20);
      btnadd.printButton(250,150,"cancel",105,3,Message.get("JUserModify_03"),"oncancel()","images/button_icon_del.gif",20);
		%>
	</body>

</html>