<%@ page import="java.net.*,java.util.*,java.text.*,java.io.*,m2soft.rdsystem.server.core.rdscheduler.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.beans.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="properties.h"%>

<html><head><title><%= Message.get("main_01") %></title>
<style>
<!--
<%@ include file="addschedule.css" %>
//-->
</style>

</head>
<body>
<form name="scform" method="post">

<table border="0" width="100%" cellpadding=1 cellspacing=0>
   <td height="20" align="left" bgcolor="#6699FF">
      <font color="white"><b><%= Message.get("useradd_01") %></b></font>
   </td>
</table>

<table border="0" width="300" cellpadding=0 cellspacing=3>
<tr>
	<th width = "100" height = "0"></th><th width = "100" height = "0"></th>
</tr>
<tr>
	<td>
		<img src="images/item.gif"><%= Message.get("useradd_02") %>
	</td>
	<td>
		<input type="text" name="userID" size="30" values="123" class="style1" enable=false >
	</td>
</tr>
<tr>
	<td>
		<img src="images/item.gif"><%= Message.get("useradd_03") %>
	</td>
	<td>
		<input type="text" name="username" size="30" values="123" class="style1" enable=false >
	</td>
</tr>
<tr>
	<td>
		<img src="images/item.gif"><%= Message.get("useradd_04") %>
	</td>
	<td>
		<input type="text" name="password" size="30" values="123" class="style1" enable=false >
	</td>
</tr>
<tr>
	<td>
		<img src="images/item.gif"><%= Message.get("useradd_05") %>
	</td>
	<td>
		<input type="text" name="password1" size="30" values="123" class="style1" enable=false >
	</td>
</tr>
<tr>
	<td>
		<img src="images/item.gif"><%= Message.get("useradd_06") %>
	</td>
	<td>
		<input type="text" name="email" size="30" values="123" class="style1" enable=false >
	</td>
</tr>
<tr>
	<td colspan=3 align=right>
   	<img src="images/uadd.gif" border=0 hsapce=0 vspace=0></a>
	</td>
</tr>
</table>

</form>
</body>
</html>