<%@page language="java" import="m2soft.rdsystem.server.core.rddbagent.*,m2soft.rdsystem.server.*,m2soft.rdsystem.server.servlet.*, m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.jdbc.waspool.*,m2soft.rdsystem.server.core.cache.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.log.l.*,m2soft.rdsystem.server.servlet.*" contentType="application/octet-stream"%><%!
   private static boolean waspool = false;
   private static boolean iencoding = false;
   private static boolean oencoding = false;
   private static boolean compress = false;
   private static boolean userpool = false;
   private static boolean schedule = false;
   private static int managerport = 8089;
   private GuideProcess p = null;
   public void jspInit(){
		RDUtil.setRDServerSetupConfigDir("/usr/local/tomcat6/webapps/rdServer");
      p = new AgentProcess(getServletConfig().getServletContext(), waspool,userpool,schedule,managerport);
      p.init();
   }
   public void jspDestroy()
   {
      p.destroy();
   }%><%
      Server.setServerPort(request.getServerPort()); RDDBAgent rdAgent = null;
      RDOutputStream rdout = null;
      rdAgent = new RDDBAgent(AgentProcess.getConnectionManager());
      try
      {  rdout = new RDOutputStream(response.getOutputStream());
         String content_type = request.getHeader("Content-Type");
         if(content_type != null)
         {
            if(content_type.toLowerCase().indexOf("octet-stream") != -1)
               rdAgent.requestService(rdout,new RDServletHttpResponseImpl(response),new RDParams(request),iencoding,oencoding,compress);
            else
               rdAgent.requestService(rdout,new RDServletHttpResponseImpl(response),new RDServletHttpRequestImpl(request),iencoding,oencoding,compress);
         }else   rdAgent.requestService(rdout,new RDServletHttpResponseImpl(response),new RDServletHttpRequestImpl(request),iencoding,oencoding,compress);
      }catch (Throwable je){RdLog.Log("rdagent.jsp","Exception",je,0);}finally{rdout.flush();}%>
