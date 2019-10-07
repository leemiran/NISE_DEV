<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message " %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<%!
   String paramenvfilename=null;
   String fpath,envfilename;
%>
<%
	initArg(request,out);
%>

<%
   //sjs 04.14 get the current status of rd encryption
   crp = CurrentEncStatus.getRdEncStatus();
   CurrentEncStatus.setCurrentRdTab("schedulegroup");

   envfilename = fpath = RDUtil.getRdPropPath();
   paramenvfilename =  m_param.GetStrValue("envfilename");

   if(paramenvfilename != null){
      rp.put("server.servicename",m_param.GetStrValue("servicename"));
      rp.put("file.separator",m_param.GetStrValue("fileseparator"));
      rp.put("server.language",m_param.GetStrValue("language"));
      rp.put("server.userdefinecharset",m_param.GetStrValue("userdefinecharset"));
      rp.put("schedule.fileformatyyyyMMdd",m_param.GetStrValue("fileformatyyyyMMdd"));
/*
      //sunhee 04/30 for dmserver setting
      rp.put("dm.serverip",m_param.GetStrValue("dmserver"));
      rp.put("dm.port",m_param.GetStrValue("dmserverport"));
      rp.put("dm.webcontextpath",m_param.GetStrValue("dm_webcontextpath"));
      rp.put("dm.webserviceport",m_param.GetStrValue("dm_webserviceport"));
*/

      //sjs 04.10 also pass the encrypt variable
      rp.saveToFile(RDUtil.getRdPropPath(), crp);
      out.println("<body bgcolor="+m_sBtnFace+">");
      out.println("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
      debugPrint("<script>window.location='JEnvScheduleServ.jsp';</script>");
      return;
   }
%>
<style><%@ include file="addschedule.css" %></style>
<script language="JavaScript">

function submitUp() {

   var frm = document.upform;
   var filepath,ext;

	if(frm.servicename.value == ""){
		alert(document.setmessage.envfile_10.value);
		frm.servicename.focus();
		return;
	}
	frm.action="JEnvScheduleServ.jsp";
	frm.submit();
}
</script>
</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<form name="setmessage">
	<input type="hidden" name="envfile_10" value=<%= Message.get("envfile_10") %> >
</form>
<form name="upform" method="post" action="JEnvScheduleServ.jsp">
<table border=0 cellpadding=0 cellspacing=3 >
<td>
<INPUT type="hidden" name="envfilename" class="style4" readonly value="<%=RDUtil.getRdPropPath()%>" size=60>
<INPUT type="hidden" name="language" class="style4" readonly value="<%=language%>" size=60>
</td>
<tr>
	<td colspan=2>
		<img src="images/button16.gif" hsapce=10><%= Message.get("envfile_22") %>
	</td>
<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name="servicename" size=50 class="style2" value="<%=servicename%>">
	</td>

<tr>
	<td colspan=2>
     <img src="images/button16.gif" hsapce=10><%="User Define CharSet"%>
	</td>
<tr>
	<td colspan=2>
      <img src="images/blank.gif" width=20><input type=text name="userdefinecharset" size=50 class="style2" value="<%=userdefinecharset%>">
	</td>

<tr>
	<td colspan=2>
		<img src="images/button16.gif" hsapce=10><%= Message.get("envfile_26") %>
	</td>
<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name="fileseparator" size=50 class="style2" value="<%=fileseparator%>">
	</td>


<tr>
   <td colspan=2>
      <img src="images/button16.gif" hsapce=10>Schedule datafile format-(yyyyMMdd) 1 or 0<br>
      <img src="images/blank.gif" width=20><input type=text name="fileformatyyyyMMdd" size=50 class="style2" value="<%=fileformatyyyyMMdd%>">
   </td>
<!--
<tr>
   <td colspan=2>
     <img src="images/button16.gif" hsapce=10><%="File Upload Setup(DM Server)"%>
   </td>
<tr>
   <td colspan=2>
      <table>
      <td><img src="images/blank.gif" width=20></td>
      <td>
         <table border=0 cellpadding=0 cellspacing=0>
         <td colspan=4>Ip&nbsp;<input type=text name="dmserver" size=20 class="style2" value="<%=dmserver%>">
         &nbsp;Port&nbsp;<input type=text name="dmserverport" size=8 class="style2" value="<%=dmserverport%>"></td><tr>
         <td colspan=4><br><%=Message.get("Security12")%></td><tr>
         <td colspan=4>Context path<input type=text name="dm_webcontextpath" size=20 class="style2" value="<%=dm_webcontextpath%>">
         Web service port<input type=text name="dm_webserviceport" size=8 class="style2" value="<%=dm_webserviceport%>"></td><tr>
         <td colspan=4><br><%=Message.get("Security14")%></td>

         </table>
      </td>
      </table>
   </td>
<tr>
-->
</table>
<%
   {
      JLButton btnadd = new JLButton();
      btnadd.printButton(35,430,"envup",105,3,Message.get("envfile_29"),"javascript:submitUp();","images/edit.gif",20);
   }
%>

</form>
</body>
</html>
