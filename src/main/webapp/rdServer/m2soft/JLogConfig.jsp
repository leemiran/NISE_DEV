<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File " %>
<% String contentType1 = m2soft.rdsystem.server.core.install.Message.getcontentType(); response.setContentType(contentType1); %>

<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JLMenuBar.java"%>
<%@ include file="../control/lib/JLToolBar.java"%>
<%@ include file="../control/lib/JLSplitter.java"%>
<%@ include file="../control/lib/JLFrame.java"%>
<%@ include file="../control/lib/JLMultiFrame.java"%>
<%@ include file="../control/lib/JLContextMenu.java"%>
<%@ include file="../control/lib/JLPopupMenu.java"%>
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
   //sjs 04.14 get the current status of log encryption
   if (CurrentEncStatus.getLogEncStatus() == null)
      status = request.getParameter("logEncStatus");
   else if (request.getParameter("encStatus") == null)
      status = CurrentEncStatus.getLogEncStatus();
   else
      status = request.getParameter("encStatus");

   CurrentEncStatus.setLogEncStatus(status);
   status = CurrentEncStatus.getLogEncStatus();

%>
<script language="JavaScript">
function encrypt() {

   var frm = document.encForm;
   var status = "<%=status%>";

   if(status == "true"){
      frm.action="JLogConfig.jsp?encStatus=false";
   }
   else {
      frm.action="JLogConfig.jsp?encStatus=true";
   }
   frm.submit();
}
</script>
<style><%@ include file="addschedule.css" %></style>
</head>
<body scroll="yes" bgcolor="#FFFFFF" text="#000000" leftmargin="15" topmargin="15" marginwidth="0" marginheight="0">
<!--sjs 04.13-->
<form name="encForm" action="" method="post">
<div align="left"><br>
   <input type="checkbox" name="enc" OnClick="javascript:encrypt();" <%if ( status.equals("true")) out.println("Checked");%>><font class="style4"><%out.println(Message.get("envfile_30"));%></font><br>
</div>
<br>
<%
   String setTabLabel ="generallog";
	JLTabCtrl tabCtrl = new JLTabCtrl(out);
	tabCtrl.init("logtab",745,445);
	tabCtrl.addTab("generallog",160,15,3,Message.get("jlogconfigrpt_17"),"","images/log.gif","JLogConfigGeneral.jsp");
	tabCtrl.addTab("errorlog",160,15,3,Message.get("jlogconfigrpt_11"),"","images/log.gif","JLogConfigError.jsp");
	tabCtrl.addTab("reportlog",160,15,1,Message.get("jlogconfigrpt_12"),"","images/log.gif","JLogConfigRpt.jsp");
//	tabCtrl.addTab("ejblog",160,15,1,Message.get("jlogconfigrpt_18"),"","images/log.gif","JLogConfigEJB.jsp");
	tabCtrl.printTabs();
   if(m_param.GetIntValue("id") == 200)
   {
      setTabLabel = "reportlog";
   }
%>

<script>

   JLTabCtrl_focusPage("logtab","<%=setTabLabel%>");

	function focusPage(idx)
	{
		if (idx == 0)
			JLTabCtrl_focusPage("logtab","generallog");
		if (idx == 1)
			JLTabCtrl_focusPage("logtab","errorlog");
		if (idx == 2)
			JLTabCtrl_focusPage("logtab","reportlog");
		if (idx == 3)
			JLTabCtrl_focusPage("logtab","ejblog");
	}


</script>

