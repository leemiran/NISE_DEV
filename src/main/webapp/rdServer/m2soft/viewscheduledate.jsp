<%@ page import="java.io.*,java.util.*,java.util.Date,java.sql.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,java.text.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.logger.*" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="properties.h"%>
<html>
   <head>
      <title><%= Message.get("viewscheduledate_01") %></title>
       <style type="text/css">
       <%@ include file="default.css" %>
       </style>
   </head>
<body onBlur= "window.close();" bgcolor=#006699>

<%!

   String viewscheduledate_02 = Message.get("viewscheduledate_02");
   String viewscheduledate_03 = Message.get("viewscheduledate_03");

   RdmrdDBAgent agent = null;
   RDJDBCHelper rdhelper;

   String scheduledateQuery=viewscheduledate_02;
   String scid ="0";
   String scname ="";
   int parsescid=0,colCount=0,i=0;

   String parseDateString(String sdate) {

      Calendar rcalendar = new GregorianCalendar();
      SimpleDateFormat sformat = new SimpleDateFormat(viewscheduledate_03);

      rcalendar.set(Calendar.YEAR,Integer.parseInt(sdate.substring(0,4)));
      rcalendar.set(Calendar.MONTH,Integer.parseInt(sdate.substring(4,6))-1);
      rcalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(sdate.substring(6,8)));
      rcalendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(sdate.substring(8,10)));
      rcalendar.set(Calendar.MINUTE,Integer.parseInt(sdate.substring(10,12)));
      rcalendar.set(Calendar.SECOND,0);

      return sformat.format(rcalendar.getTime());
   }

%>

<table cellpadding=0 cellspacing=0 border=0 align=center>
<%
    String viewscheduledate_04 = Message.get("viewscheduledate_04");
    String viewscheduledate_05 = Message.get("viewscheduledate_05");

	scid = request.getParameter("scid");
	scname = request.getParameter("scname");

	if(scid.equals(""))
		scid = "0";

   String tmpStr;

	parsescid = Integer.parseInt(scid);
   agent = new RdmrdDBAgent(servicename,RdLogManager.getInstance().getScheduleLog());
	rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);

	try {

		rdhelper.select(scheduledateQuery);
		rdhelper.setInt(1,parsescid);
		rdhelper.execute();

		Vector colInfo = rdhelper.getSelectColumninfo();
		colCount = colInfo.size();

		while(rdhelper.next()) {
		
			String data2 = rdhelper.getString(2);
		
		   out.print("<tr>");
		   if(rdhelper.getInt(3) == 0)
		   	out.print("<td><font color=white>"+parseDateString(data2)+"</font></td>");
		   else
		   	out.print("<td><font color=white><img src=images/check.gif>" + parseDateString(data2)+"</font></td>");

			out.print("</tr>");
		}
   }catch (Exception e){
       out.print(viewscheduledate_04+e.getMessage());
	}finally{
		try{
			rdhelper.close();
			agent.disconnect();
		}catch (Exception d){
			out.print(viewscheduledate_05+d.getMessage());
		}
	 }

%>
</table>
</body>
</html>