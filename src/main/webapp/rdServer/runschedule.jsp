<%@page import="java.io.*, m2soft.rdsystem.server.core.rdscheduler.*,m2soft.rdsystem.server.core.install.*" %><%
   String scheduleName = request.getParameter("schedulename");
   String thread = request.getParameter("runthread");
   String parameters = request.getParameter("parameters");
   boolean runThread = false; boolean encodingFlag = true;
   if(thread != null && !thread.equals(""))  runThread = true;
   RunScheduler schedule = new RunScheduler(scheduleName,runThread,parameters,encodingFlag);
   boolean ret = schedule.startSchedule();
   if(ret){out.println(ret);} else out.println("0|"+schedule.getLastErrMsg());%>