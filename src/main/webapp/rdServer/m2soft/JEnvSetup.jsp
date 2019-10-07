<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message "%>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page session="false" %>
<%@ include file="session.h"%>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>

<%@ include file="../control/lib/JLTabCtrl.java"%>
<%@ include file="../control/lib/JSTabCtrl.jsp"%>
<%
	initArg(request,out);
%>
<%!
   //sjs 04.14
   private String status = null;
%>
<%
   //sjs 04.14 get the current status of rd encryption
   if (CurrentEncStatus.getRdEncStatus() == null)
      status = request.getParameter("rdEncStatus");
   else if (request.getParameter("encStatus") == null)
      status = CurrentEncStatus.getRdEncStatus();
   else
      status = request.getParameter("encStatus");

   CurrentEncStatus.setRdEncStatus(status);
   if(status == null)
     System.out.println("status is null");
%>
<head>
<script language="JavaScript">
function encrypt() {

   var frm = document.encForm;
   var status = "<%=status%>";

   if(status == "true"){
      frm.action="JEnvSetup.jsp?encStatus=false";
   }
   else {
      frm.action="JEnvSetup.jsp?encStatus=true";
   }
   frm.submit();
}
</script>
<style><%@ include file="addschedule.css" %></style>
</head>
<body scroll="yes" bgcolor="#FFFFFF" text="#000000" leftmargin="15" topmargin="15" marginwidth="0" marginheight="0">
<!--sjs 04.13-->
<form name="encForm" action="" method="post">
<div align="left">
   <input type="checkbox" name="enc" OnClick="javascript:encrypt();" <%if ( status.equals("true")) out.println("Checked");%>><font class="style4"><%out.println(Message.get("envfile_30"));%></font><br>
</div>
</form>
<%
	JLTabCtrl tabCtrl = new JLTabCtrl(out);
	tabCtrl.init("camp",745,480);
   tabCtrl.addTab("pathgroup",160,20,1,Message.get("jenvsetup_01"),"","images/segment.gif","JEnvPath.jsp");
   /*
   if(language.indexOf("korean") != -1)
      tabCtrl.addTab("mailgroup",160,20,1,Message.get("jenvsetup_02"),"","images/segment.gif","JEnvMail.jsp");
   else
	   tabCtrl.addTab("mailgroup",160,20,1,Message.get("jenvsetup_02"),"","images/segment.gif","JEnvMail.jsp");
   */
	tabCtrl.addTab("webservgroup",160,20,1,Message.get("jenvsetup_03"),"","images/segment.gif","JEnvWebServ.jsp");
	tabCtrl.addTab("schedulegroup",160,20,1,Message.get("jenvsetup_04"),"","images/segment.gif","JEnvScheduleServ.jsp");
	tabCtrl.addTab("reportsgroup",160,20,1,Message.get("jenvsetup_05"),"","images/segment.gif","JEnvReports.jsp");

   //sjs 04.14 For setting the current tab
   if (CurrentEncStatus.getCurrentRdTab() != null)
      tabCtrl.setInitialPage(CurrentEncStatus.getCurrentRdTab());
	tabCtrl.printTabs();
%>
