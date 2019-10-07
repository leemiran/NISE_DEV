<%@ page import="java.io.*,java.net.*,java.util.*" %>
<%@ include file="logproperties.h" %>
<%
  String mrd = "http://192.168.1.22/RDServer/m2soft/uclog.mrd";
  String param = "/rfn [http://"+InetAddress.getLocalHost().getHostAddress()+"../download.jsp?filename="+logFile+"]";
%>
<html>
<head><title>Report Designer View Monitering log</title>
</head>
<body leftmargin=1px rightMargin=1px topmargin=0 marginwidth=1 marginheight=1 onLoad="javascript:rdOpen()" style="overflow-x:hidden;overflow-y:hidden">

<SCRIPT LANGUAGE = JavaScript>
function rdOpen() {
	Rdviewer.SetBackgroundColor(255,255,255);
	Rdviewer.SetPageLineColor(255,255,255);
	Rdviewer.SetPageColor(255,255,255);
	Rdviewer.HideStatusBar();
	Rdviewer.AutoAdjust=0;
	//Rdviewer.HideToolBar();
	Rdviewer.FileOpen("<%=mrd%>","<%=param%>");
}
</SCRIPT>

<OBJECT id=Rdviewer
   classid="clsid:8068959B-E424-45ad-B62B-A3FA45B1FBAF"
   name=Rdviewer width=100% height=100%>
</OBJECT>

</body>
</html>
