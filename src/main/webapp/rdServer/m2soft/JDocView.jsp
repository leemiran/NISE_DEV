<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.net.*"%>
<% String contentType1 = m2soft.rdsystem.server.core.install.Message.getcontentType(); response.setContentType(contentType1);%>
<%@ page session="false" %>
<%@ include file="session.h"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JLJsp.jsp" %>
<%@ include file="../control/lib/JLObj.java" %>
<%@ include file="../control/lib/JLRuntimeClass.java" %>
<%@ include file="../control/lib/JLHttp.java" %>
<%
   initArg(request,out);
   String targetmrd = null;

   targetmrd =m_param.GetStrValue("targetpath");

   if(targetmrd == null)
      return;
   /*
   // RDServer 의 URL 을 구한다. 예) http://192.168.1.73:8080/RDServer
   InetAddress localAddress = InetAddress.getLocalHost();
   String ip   = localAddress.toString();
   ip = ip.substring(ip.indexOf("/")+1, ip.length() );
   String port = String.valueOf(request.getServerPort());
   String uri = request.getRequestURI();
   uri = uri.substring(0, uri.indexOf("/m2soft/JDocView.jsp"));

   String url = "http://"+ip+":"+port+uri;
   */

   String url = "http://"+ serverip +"/RDServer";

%>
<html>
<head><title>Reports Designer Document PreView</title>
<SCRIPT LANGUAGE = JavaScript>
   function rdOpen() {
      Rdviewer.SetBackgroundColor(255,255,238);
      Rdviewer.AutoAdjust=0;
      Rdviewer.FileOpen("<%=url%>/download.jsp?filename=<%=mrdpath+targetmrd%>","/rfn [<%=url%>/rdagent.jsp]");
   }
</SCRIPT>
</head>
<body leftmargin=1px rightMargin=1px topmargin=0 marginwidth=1 marginheight=1 onLoad="javascript:rdOpen()" style="overflow-x:hidden;overflow-y:hidden;cursor:auto">
<%
   debugPrint( targetmrd.substring(2)+ " "+ Message.get("JDocView_01") );
%>

<%@include file="ocx.h" %>
</body>
</html>

