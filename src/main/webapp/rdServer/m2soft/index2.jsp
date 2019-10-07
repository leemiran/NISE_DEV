<%@page import="java.io.*,java.util.*,java.net.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.install.Message"%>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>

<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<%
response.setHeader("Pragma","No-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0);
response.setHeader ("Cache-Control", "no-cache");
	initArg(request,out);
%>
<html>
<head>
<style>
<%@ include file="icis.css" %>
</style>
<title><%= Message.get("index_01") %></title>
</head>
<body background="images/login/bg.gif" onLoad="javascript:document.login.USER.focus();" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
<form name="login" action="Jlogin.jsp" method="post">
<img src="images/blank.gif" hspace=0 vspace=0 height="80"><br>


<table border="0" cellspacing=0 cellpadding=0 height=295>
  <td><img src="images/blank.gif" width=50 hspace=0 vspace=0></td>
  <td valign=top><img src="images/login/left_img1.gif" hspace=0 vspace=0></td>
  <td height=290>
            <table border=0 cellpadding=0 cellspacing=0>
            <tr>
               <td  align=top colspan=3 bgcolor=#ffffff>
               <%if(language.equals("korean")){ %>
                  <img src="images/login/top_bar.gif" vspace=0 hspace=0>
               <% }else{ %>
               	<img src="images/login/top_bar_eng.gif" vspace=0 hspace=0>
               <% } %>
               </td>
            </tr>
            <tr>

              <td  bgcolor=#ffffff><img src="images/login/login_middle_left.gif" hspace=0 vspace=0></td>

              <td  bgcolor=#ffffff>
                 <img src="images/login/id.gif" vspace=0 hspace=0><input type="text" name="USER" maxlength=40 size=12 class="style2"><br>
                 <img src="images/login/password.gif" vspace=0 hspace=0><input type="password" name="PASSWORD" maxlength=40 size=12 class="style2" onkeydown="if(event.keyCode == 13) javascript:login.submit();">

              </td>

              <td  bgcolor=#ffffff><img src="images/login/login_middle_right.gif" hspace=0 vspace=0></td>
            </tr>
            <tr>
            <td colspan=3 bgcolor=#ffffff>&nbsp;<td>
            </tr>
            <tr>
              <td colspan=3 bgcolor=#ffffff>
              <table cellspacing=0 celpadding=0 border=0 height=71>
              <td height=28 align=center><% if(L.isUnicodeVersion()) out.println(Message.get("ServerManagerVersionUnicode"));else out.println(Message.get("ServerManagerVersion"));%><td><tr>
              <td height=43><img src="images/login/img.gif" vspace=0 hspce=0></td>
              </table>

              </td>
            </tr>
            </table>

  </td>

</table>


<%
   {
      JLButton btnadd = new JLButton();
      btnadd.printButton(490,246,"envup",105,2," ","javascript:login.submit();","images/login/login.gif",20);
   }
%>
</form>
</body>
</html>

