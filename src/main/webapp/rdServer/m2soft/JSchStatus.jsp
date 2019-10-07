<%@ page import="java.io.*,java.util.*,m2soft.rdsystem.server.core.rddbagent.jdbc.L" %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="logproperties.h"%>

<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<% response.setHeader ("Refresh", rMilisecond+"; URL='JSchStatus.jsp'"); %>
<%
  initArg(request,out);
  String mrd,param;
  String today = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new Date());
  if(L.isUnicodeVersion())
	  mrd = cabpath+"/m2soft/scstatus_u.mrd";
  else
     mrd = cabpath+"/m2soft/scstatus.mrd";
  param = "/rfn ["+cabpath+"/rdagent.jsp] "+"/rsn ["+servicename+"] "+"/rp "+today;

%>
<SCRIPT>
function rdOpen() {
   Rdviewer.SetBackgroundColor(255,255,255);
   Rdviewer.SetPageLineColor(255,255,255);
   Rdviewer.SetPageColor(255,255,255);
   Rdviewer.HideStatusBar();
   Rdviewer.AutoAdjust=0;
   Rdviewer.ZoomRatio = 100;
   //Rdviewer.HideToolBar();
   Rdviewer.CurrentPageNo = Rdviewer.GetTotalPageNo;

   Rdviewer.FileOpen("<%=mrd%>","<%=param%>");
}
function refresh(){
   alert('dk');
}
</SCRIPT>
<body leftmargin=1px rightMargin=1px topmargin=0 marginwidth=1 marginheight=1 onLoad="javascript:rdOpen()" style="overflow-x:hidden;overflow-y:hidden">


<%@include file="ocx.h" %>



</body>
</html>
