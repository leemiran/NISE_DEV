<%@ page session="false" %>
<%@ include file="session.h"%>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="properties.h"%>

<html>
<head>
<title><%= Message.get("completefilelist_02") %></title>
<style>
<%@ include file="icis.css" %>
</style>

<body bgcolor="#FFFFFF" SCROLL ="yes" text="#000000" leftmargin="10" topmargin="10" marginwidth="0" marginheight="0">
<table border="0" width=100% cellpadding=1 cellspacing=0>
   <td height="25" align="left" bgcolor="#d4d0c8">
      <font color="white"><b><%= Message.get("schedulelist_01") %></b></font>
   </td>
</table><br>
<% if(language.equals("korean")){ %>
			<iframe src="JSchList.jsp" name="schlist", width="750" height="424" frameborder=0 scrolling=auto STYLE="border-style:ridge;border-width:1;color:dfdfdf"></iframe>
<% }else{ %>
			<iframe src="JSchList.jsp" name="schlist", width="945" height="424" frameborder=0 scrolling=auto STYLE="border-style:ridge;border-width:1;color:dfdfdf"></iframe>
<% } %>
</body>
</html>
