<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*, java.io.File, m2soft.rdsystem.server.core.install.Message, m2soft.rdsystem.server.core.rddbagent.util.*"%>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
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
<head>
<style>
<%@ include file="icis.css" %>
</style>
</head>
<script>
   function hide(){
      window.status =' ';
   }

</script>

<body scroll="yes" bgcolor="#FFFFFF" text="#000000" leftmargin="15" topmargin="20" marginwidth="0" marginheight="0" onLoad='hide();'>

<%
	JLTabCtrl tabCtrl = new JLTabCtrl(out);
	tabCtrl.init("camp",745,445);
	tabCtrl.addTab("monitergroup",160,15,3,Message.get("jmainframe_16"),"","images/svy.gif","JRptMoniter.jsp");
   tabCtrl.addTab("jdbcgroup",160,15,1,Message.get("jmainframe_17"),"","images/db_folder.gif","JRptJdbc.jsp?dbEncStatus=true");
	if(isNongHyup)
		tabCtrl.addTab("xmlgroup",160,15,1,"Xml","","images/icon_img_del.gif","JRptXml.jsp");
   tabCtrl.addTab("securitygroup",160,15,1,Message.get("Security15"),"","images/targetfolder.gif","JRptSecurity.jsp");

	//sunhee 2003.07.23 
	//tabCtrl.addTab("waspoolgroup",160,15,1,"WAS Pool Usage Setting","","images/segment.gif","JRptWasPool.jsp");
	//tabCtrl.addTab("charsetgroup",160,15,1,"CharacterSet Setting","","images/segment.gif","JRptCharSet.jsp");
   //tabCtrl.addTab("cachegroup",160,15,1,"Cache function Setting","","images/grp.gif","JRptCache.jsp");
	tabCtrl.printTabs();
%>


<form name=param><input type=hidden name=id value=""></form>

<script>
	JLTabCtrl_focusPage("camp","monitergroup");

	function focusPage(idx)
	{
	   if (idx == 0)
		  JLTabCtrl_focusPage("camp","monitergroup");
		if (idx == 1)
			JLTabCtrl_focusPage("camp","jdbcgroup");
		if (idx == 2)
			JLTabCtrl_focusPage("camp","xmlgroup");
		if (idx == 3)
         JLTabCtrl_focusPage("camp","securitygroup");
		if (idx == 4)
			JLTabCtrl_focusPage("camp","waspoolgroup");
		if (idx == 5)
			JLTabCtrl_focusPage("camp","charsetgroup");
		if (idx == 6)
         JLTabCtrl_focusPage("camp","cachegroup");
	}

	function _onFocusPage(sTabName,id)
	{
		if (id == "jdbcgroup")
		{
			var sFrame = JLTabCtrl_getPageFrameName(sTabName,id);
			var form = document.param;
			form.target = sFrame;
			form.action = "JRptJdbc.jsp";
			form.method = "post";
			form.submit();
		}
	}
</script>