<%@ page import="java.util.*,
		java.text.*,
		java.io.*,
		m2soft.rdsystem.server.MasterBase,
		m2soft.rdsystem.server.core.rddbagent.*,
		m2soft.rdsystem.server.core.install.Message,
		m2soft.rdsystem.server.log.report.*,
		m2soft.rdsystem.server.master.manager.ReportLogWriter,
		m2soft.rdsystem.server.core.rddbagent.jdbc.L" 
		contentType ="text/html; charset=euc-kr" %><%@ include file="properties.h"%><HTML>
<HEAD><TITLE></TITLE></HEAD>
<BODY onLoad="javascript:rdOpen()"><%
	String mrdPath = "";
	if(L.isUnicodeVersion())
		mrdPath = cabpath + "/m2soft/LogManager_u.mrd";
	else
		mrdPath = cabpath + "/m2soft/LogManager.mrd";
	String param = "/rfn ["+cabpath+"/m2soft/LogManager_Data.jsp]";
%>
<SCRIPT>
function rdOpen()
{
   Rdviewer.SetBackgroundColor(255,255,255);
   Rdviewer.SetPageLineColor(255,255,255);
   Rdviewer.SetPageColor(255,255,255);
   Rdviewer.AutoAdjust=0;
<%
   if (Message.getLanguage().equals("japanese"))
   {
      out.println("   Rdviewer.ApplyLicense (\"" + cabpath + "/rdagent.jsp\");");
   }
%>
   Rdviewer.FileOpen("<%=mrdPath%>","<%=param%>");
}
</SCRIPT>
<%@include file="ocx.h"%>
</BODY>
</HTML>