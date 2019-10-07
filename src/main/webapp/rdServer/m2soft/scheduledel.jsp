<%@ page import="java.util.*,java.text.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.beans.*,m2soft.rdsystem.server.core.install.Message"%>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
initArg(request,out);

String scname = request.getParameter("scname");
String del = request.getParameter("del");
String scid = request.getParameter("scid");

if(del.equals("yes")) {
   try{
      
      if(scid != null) {
         Delschedule delsc = new Delschedule(servicename,logpath,scid,out);
         delsc.DelList();
      }

   }catch (Exception e){
      out.println(e.getMessage());
   }

   //response.sendRedirect("schedulelist.jsp?rowcount=1&rows="+ROWCOUNT);
   //response.sendRedirect("JMainHome.jsp");
  String url = "JMainHome.jsp?rowcount=1&rows="+ROWCOUNT;
  debugPrint("<script>window.location='"+url+"';</script>");
  return;     
}
      
%>