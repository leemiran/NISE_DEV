<%@page import="java.io.*,java.net.*,m2soft.rdsystem.server.core.rddbagent.util.URLData" %>
<%@ include file="properties.h"%>
<%
   InetAddress localAddress = InetAddress.getLocalHost(); 
   String ip   = localAddress.toString();
   ip = ip.substring(ip.indexOf("/")+1, ip.length() );
   String port = String.valueOf(request.getServerPort());
   String uri = request.getRequestURI();
   uri = uri.substring(0, uri.indexOf("/m2soft/JRptStart.jsp"));
   String url = "http://"+ip+":"+port+uri+"/rdagent.jsp";

  try{
    new URLData().get(url);
  }
  catch (Exception e)
  {
    String errmsg = e.getMessage() + " " + url;
    out.println("<script>alert(\"" + errmsg + "\");close();</script>");
  }
%>
<body onload="move()">
<script language="JavaScript">
   function move() {
      opener.location.reload();
      close();
  }
</script>