<%@page import="java.io.*,java.util.*,java.net.*"%>
<%
response.setHeader("Pragma","No-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0);
response.setHeader ("Cache-Control", "no-cache");
   //InetAddress localAddress = InetAddress.getLocalHost();
   //String ip   = localAddress.toString();
   //ip = ip.substring(ip.indexOf("/")+1, ip.length() );
   String ip = request.getServerName();
   String port = String.valueOf(request.getServerPort());
   String uri = request.getRequestURI();
   uri = uri.substring(0, uri.indexOf("/m2soft/index.jsp"));
   String url = "http://"+ip+":"+port+uri+"/rdagent.jsp";

   int tmp = 0;
   URL noCompress = new URL(url);
   HttpURLConnection huc = (HttpURLConnection)noCompress.openConnection();
   huc.connect();

   ByteArrayOutputStream baos = new ByteArrayOutputStream();
   InputStream is = huc.getInputStream();
   while((tmp = is.read()) != -1) {
      baos.write(tmp);
   }
   is.close();

   pageContext.forward("index2.jsp");
%>