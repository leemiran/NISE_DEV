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
<table border="0" width="100%" cellpadding=1 cellspacing=0>
   <td height="20" align="left" bgcolor="#6699FF">
      <font color="white"><b><%= Message.get("useradd_08") %></b></font>
   </td>
</table>

<table border="0" width="100%" cellpadding=2 cellspacing=0>
<tr>
   <td colspan=2>
      <iframe frameborder="0" height="400" scrolling="auto" src="JUserList.jsp?add=yes" width="517" name="body" STYLE="border-style:solid;border-width:1;"></iframe><br>
   </td>
</tr>
</table>
</body>
</html>