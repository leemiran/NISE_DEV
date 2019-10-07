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
<style>
<%@ include file="icis.css" %>
</style>
<body scroll="yes" bgcolor="#FFFFFF" text="#000000" leftmargin="15" topmargin="20" marginwidth="0" marginheight="0">

<%
   String setTabLabel ="update";
	JLTabCtrl tabCtrl = new JLTabCtrl(out);
	tabCtrl.init("logtab",745,465);
	tabCtrl.addTab("update",160,15,3,Message.get("jupdatestep2_00"),"","images/button_icon_arrowup.gif","JUpdateStep1.jsp");
	tabCtrl.addTab("updaterecovery",160,15,1,Message.get("JUpdateRecoveryStep1_01"),"","images/button_icon_arrowdown.gif","JUpdateRecoveryStep1.jsp");
	tabCtrl.printTabs();
   if(m_param.GetIntValue("id") == 200)
      setTabLabel = "reportlog";
%>

<script>

   JLTabCtrl_focusPage("logtab","<%=setTabLabel%>");

	function focusPage(idx)
	{
		if (idx == 0)
			JLTabCtrl_focusPage("logtab","update");
		if (idx == 1)
			JLTabCtrl_focusPage("logtab","updaterecovery");
	}


</script>

