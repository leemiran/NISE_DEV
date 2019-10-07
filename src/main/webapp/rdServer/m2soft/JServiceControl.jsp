<%@ page import="java.util.*,java.sql.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.core.rddbagent.*,m2soft.rdsystem.server.core.rdscheduler.*" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);
	String command = request.getParameter("cmd");
	String contents = request.getParameter("contents");
	if(contents == null) contents ="";
%>

<html><head><title><%= Message.get("scservice_01") %></title>
<style>
<%@ include file="addschedule.css" %>
</style>
<script language=Javascript>

function getStatus(){
   document.schedulestatus.cmd.value = 'start';
	document.schedulestatus.submit();
}

function startSchedule(){
    document.schedulestatus.cmd.value = 'start';
    document.schedulestatus.submit();
}

function stop1Schedule(){
   document.schedulestatus.cmd.value = 'stop';
   document.schedulestatus.submit();

}

</script>

<body leftmargin="10" topmargin="10"  marginwidth="0" marginheight="0">
<form name="schedulestatus" method = "post" action="JServiceControl.jsp">
<input type="hidden" name=cmd>
<table border="0" width=100% cellpadding=3 cellspacing=0>
	<td height="25" align="left" bgcolor=<%=m_sBtnFace%>>
		<font color="white" style="FONT: 9pt ±¼¸²"><b><%= Message.get("scservice_02") %></b></font>
	</td>
</table>

<%
RDScheduleMan scman = AgentProcess.getScheduleServer();
if(command == null)
{
   out.println("<textarea name=contents rows=20 cols=95 class=style5>"+ contents + "\n"+ RDUtil.getCurTimeStr() + " " + RDScheduleMan.getServerStatus() + "</textarea>");
}
else if(command.equals("start"))
{
   if(scman != null)
      scman.startSchedule();

   String ret = RDUtil.getCurTimeStr() + " " + scman.getServerStatus();
   if(ret != null)
   	out.println("<textarea name=contents rows=20 cols=95 class=style5>"+ contents + "\n" + ret + "</textarea>");
}
else if(command.equals("stop"))
{
   scman.stopSchedule();
   String ret = RDUtil.getCurTimeStr() +" " +scman.getServerStatus();
   if(ret != null)
   	out.println("<textarea name=contents rows=20 cols=95 class=style5>"+ contents + "\n" + ret + "</textarea>");
}

%>

<%
   int btnX = 385;
   int btnY = 350;

   JLButton btnadd = new JLButton();
   btnadd.printButton(btnX,btnY,"start",105,3,Message.get("scservice_05"),"startSchedule()","images/ok.gif",20);
   btnadd.printButton(btnX+110,btnY,"stop",105,3,Message.get("scservice_06"),"stop1Schedule()","images/icon_del.gif",20);

%>
</form>


</body>
</html>