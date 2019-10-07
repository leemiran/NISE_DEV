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

<%
	initArg(request,out);

%>
<%!
   String paramenvfilename=null;
   String fpath,envfilename;
%>
<%
   //sjs 04.14 get the current status of rd encryption
   crp = CurrentEncStatus.getRdEncStatus();
   CurrentEncStatus.setCurrentRdTab("mailgroup");

   envfilename = fpath = RDUtil.getRdPropPath();
   paramenvfilename =  m_param.GetStrValue("envfilename");

   if(paramenvfilename != null){
      rp.put("server.adminemail",m_param.GetStrValue("adminemail"));

      if(language.indexOf("korean") != -1)

      rp.put("sms.id",m_param.GetStrValue("smsid"));
      rp.put("sms.pass",m_param.GetStrValue("smspass"));
      rp.put("sms.sender",m_param.GetStrValue("smssender"));

      //sjs 04.10 also pass the encrypt variable
      rp.saveToFile(RDUtil.getRdPropPath(), crp);

      out.println("<body bgcolor="+m_sBtnFace+">");
      out.println("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
      debugPrint("<script>window.location='JEnvMail.jsp';</script>");
      return;
   }
%>

<style><%@ include file="addschedule.css" %></style>
<script language="JavaScript">

function submitUp() {

   var frm = document.upform;

	if(frm.smtpname.value == ""){
		alert(document.setmessage.envfile_07.value);
		frm.ocxhtmpath.focus();
		return;
	}


	if(frm.adminemail.value == ""){
		alert(document.setmessage.envfile_8.value);
		frm.adminemail.focus();
		return;
	}

	frm.action="JEnvMail.jsp";
	frm.submit();

}
</script>
</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">

<form name="setmessage">
	<input type="hidden" name="envfile_07" value=<%= Message.get("envfile_07") %> >
	<input type="hidden" name="envfile_08" value=<%= Message.get("envfile_08") %> >
</form>
<form name="upform" method="post" action="JEnvPath.jsp">

<table border=0 cellpadding=0 cellspacing=5 >
<tr>
<td>
   <INPUT type="hidden" name="envfilename" class="style4" readonly value="<%=RDUtil.getRdPropPath()%>" size=60><br>
</td>
<!-- sjs 1230 deleting server.smtpname
<tr>
	<td colspan=2>
		<img src="images/button16.gif" hspace=5><%= Message.get("envfile_19") %>
	</td>

<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name="smtpname" size=80 class="style2" value="<%=smtpname%>">
	</td>
-->
<tr>
	<td colspan=2>
		<img src="images/button16.gif" hspace=5><%= Message.get("envfile_20") %>
	</td>

<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name="adminemail" size=80 class="style2" value="<%=adminemail%>">
	</td>
<tr>

   <td colspan=2>
      <img src="images/button16.gif" hspace=5>SMS Server Login Id
   </td>

<tr>
   <td colspan=2>
      <img src="images/blank.gif" width=20><input type=text name="smsid" size=80 class="style2" value="<%=smsid%>">
   </td>
<tr>

   <td colspan=2>
      <img src="images/button16.gif" hspace=5>SMS Server Login Password
   </td>

<tr>
   <td colspan=2>
      <img src="images/blank.gif" width=20><input type=text name="smspass" size=80 class="style2" value="<%=smspass%>">
   </td>
<tr>

   <td colspan=2>
      <img src="images/button16.gif" hspace=5>SMS Sender Phone Number
   </td>

<tr>
   <td colspan=2>
      <img src="images/blank.gif" width=20><input type=text name="smssender" size=80 class="style2" value="<%=smssender%>">
   </td>
<tr>




</table>
<%
   {
      JLButton btnadd = new JLButton();
      btnadd.printButton(35,420,"envup",105,3,Message.get("envfile_29"),"javascript:submitUp();","images/edit.gif",20);
   }
%>

</form>
</body>