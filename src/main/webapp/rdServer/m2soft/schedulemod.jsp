<%@ page import="java.net.*,java.util.*,java.text.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.beans.*,m2soft.rdsystem.server.core.rdscheduler.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.core.rddbagent.*" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
   initArg(request,out);

   String schedulemod_01 = Message.get("schedulemod_01");

   if ((request.getParameter("jsppath")) != null)
   {
      if (!(request.getParameter("jsppath")).equals(""))
      {
         try{
               //sjs 06.02
               ////////////////At the time of registration, using user defination to preliminary check/////////////////
               m2soft.rdsystem.server.core.rddbagent.util.URLData ud = new m2soft.rdsystem.server.core.rddbagent.util.URLData();
               StringTokenizer st = new StringTokenizer(ud.get(request.getParameter("jsppath")),":");
               String eachToken = "";
               while (st.hasMoreTokens())
               {
                  eachToken = st.nextToken();
                  if (eachToken.length() != 14)
                  {
                     throw new StringIndexOutOfBoundsException();
                  }
               }

         }catch(FileNotFoundException fe) {
            %><script>alert("<%= Message.get("scuserdef_02") %>");history.back();</script><%
         }catch(StringIndexOutOfBoundsException se){
            %><script>alert("<%= Message.get("scuserdef_01") %>");history.back();</script><%
         }catch (Exception e){
            %><script>alert("Error: <%=e%>");history.back();</script><%
         }
      }
   }

   //////////update the content of scheduling modified by user//////////////
   try{
      String scname = request.getParameter("schedulejob_name");
      String rowcount = request.getParameter("rowcount"); //PAGE NO

      if(scname != null) {
         Modschedule modsc = new Modschedule(out,response,request,servicename,logpath,dbencoding);
         modsc.ModList();

         if(AgentProcess.getScheduleServer() != null) AgentProcess.getScheduleServer().startSchedule();

/*         Socket connSock = null;
         DataInputStream sin = null;
         DataOutputStream sout = null;
         try {
            //Create Socket
            connSock = new Socket ("localhost", 4989);

            //Get DataInputStream and DataOutputStream of connectioned socket
            sin = new DataInputStream(connSock.getInputStream());
            sout = new DataOutputStream(connSock.getOutputStream());
            sout.writeUTF("Anonymous");
            sout.flush();
            sout.writeUTF("scstart");
            sout.flush();

         }catch (Exception ex){
            if(ex instanceof ConnectException)
               out.println(schedulemod_01);
            else
               out.println(ex);
         }
*/

         String url = "JMainHome.jsp?rowcount=1&rows="+ROWCOUNT;
         debugPrint("<script>window.location='"+url+"';</script>");
         return;

      }

   }catch (Exception e){
      out.println(e.getMessage());
   }

%>
