<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File ,m2soft.rdsystem.server.core.install.Message" %>
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
   CurrentEncStatus.setCurrentRdTab("reportsgroup");


   envfilename = fpath = RDUtil.getRdPropPath();
   paramenvfilename =  m_param.GetStrValue("envfilename");

   if(paramenvfilename != null){
      rp.put("server.sessiontimeout",m_param.GetStrValue("sessiontimeout"));
      //sjs 04.10 also pass the encrypt variable
      rp.saveToFile(RDUtil.getRdPropPath(), crp);
      out.println("<body bgcolor="+m_sBtnFace+">");
      out.println("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
      debugPrint("<script>window.location='JEnvReports.jsp';</script>");
      return;
   }
%>

<style><%@ include file="addschedule.css" %></style>
<script language="JavaScript">

function submitUp() {

   var frm = document.upform;

	if(frm.sessiontimeout.value == ""){
		alert(document.setmessage.envfile_9.value);
		frm.sessiontimeout.focus();
		return;
	}

	frm.action="JEnvReports.jsp";
	frm.submit();

}
</script>
</head>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">

<form name="setmessage">
	<input type="hidden" name="envfile_9" value=<%= Message.get("envfile_09") %> >
</form>
<form name="upform" method="post" action="JEnvReports.jsp">

<table border=0 cellpadding=0 cellspacing=5 >
<tr>
<td>
   <INPUT type="hidden" name="envfilename" class="style4" readonly value="<%=RDUtil.getRdPropPath()%>" size=60><br>
</td>
<tr>
	<td colspan=2>
		<img src="images/button16.gif" hspace=5><%= Message.get("envfile_21_1") %>
	</td>
<tr>
	<td colspan=2>
		<img src="images/blank.gif" width=20><input type=text name="sessiontimeout" size=80 class="style2" value="<%=sessiontimeout%>">
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
