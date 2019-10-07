<%@ page import="java.io.*,java.util.*,java.text.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.rddbagent.*,m2soft.rdsystem.server.logger.*,m2soft.rdsystem.server.core.install.*,m2soft.rdsystem.server.core.rddbagent.jdbc.L" contentType ="text/html; charset=euc-kr" %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="logproperties.h"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
  String mrd,param,param1,param2,opcode;
  String[] allLogPath = null;
  SimpleDateFormat d = null;
  int curIndex = 0;

  if (AgentProcess.getScheduleServer() != null)
  {
	  allLogPath = UtilFile.getFileList(RDUtil.getRDSERVER_LOG_DIR(), "schedule_");
	  curIndex = allLogPath.length - 1;
	
	   opcode = request.getParameter("opcode");
	   System.out.println("opcode; "+opcode);
	   if(opcode != null)
	   {
	      try{
	         curIndex = Integer.parseInt(opcode);
	      }catch(NumberFormatException e){
	         e.printStackTrace();
	      }
	   }
	
	  if(curIndex < 0)
	    curIndex = 0;
	
	  if(curIndex >= allLogPath.length)
	     curIndex = allLogPath.length -1;

	  d = new SimpleDateFormat("'['yyyy-MM-dd']'");///... 
	  param1 = "all";
      param2 = d.format( new Date() );
      param = "/rp [" + param1 + "] ["+param2+ "] /rcombononullstr /rfn ["+cabpath+"/download.jsp?filename="+allLogPath[curIndex]+"]";
  }
  else
  {
      param = "/rdonotreport";
  }
  
  if(L.isUnicodeVersion())
	  mrd = cabpath+"/m2soft/sclog_u.mrd";
  else
	  mrd = cabpath+"/m2soft/sclog.mrd";
%>
<SCRIPT>
function rdOpen()
{
   <%if(Message.getLanguage().equals("japanese")){
      out.println("Rdviewer.ApplyLicense(\"" + cabpath + "/rdagent.jsp\");");
   }%>
   Rdviewer.SetBackgroundColor(255,255,255);
   Rdviewer.SetPageLineColor(255,255,255);
   Rdviewer.SetPageColor(255,255,255);
   Rdviewer.HideStatusBar();
   Rdviewer.AutoAdjust=0;
   Rdviewer.FileOpen("<%=mrd%>","<%=param%>");
}

function prev(){
   <%if(curIndex > 0){%>
         document.navigation.action = "JLogScheduleview.jsp?opcode=<%=curIndex - 1%>";
         document.navigation.submit();
   <%}%>
}

function next(){
   <%
   if (allLogPath != null)
   {
	   if(curIndex < allLogPath.length-1)
	   {%>
	         document.navigation.action = "JLogScheduleview.jsp?opcode=<%=curIndex + 1%>";
	         document.navigation.submit();
	   <%
	   }
   }
   %>
}


/////////////////////
function setRequestOpcode(opcode)
{
   document.req.opcode.value = opcode;
	document.req.submit();
}

/////////////////////
</SCRIPT>
<body leftmargin=0 rightMargin=0 topmargin=0  onLoad="javascript:rdOpen()">

<FORM NAME="navigation" METHOD=POST ACTION="JLogScheduleview.jsp">
<%
   if (allLogPath != null)
   {
	   if(allLogPath.length > 1)
	   {
		%>
	      <input type="button" value="Prev" onClick="javascript:prev();" style="font-size:9pt; width:100;height:18;border-width:1px; border-color:gray; border-style:solid; background-color:#e4e4e4; text-align:center">
	      <input type="button" value="Next" onClick="javascript:next();" style="font-size:9pt; width:100;height:18;border-width:1px; border-color:gray; border-style:solid; background-color:#e4e4e4; text-align:center">
	      <!-- <font size=-1><%= allLogPath[curIndex] %></font> -->
		<%
	   }
   }
   else
   {
   %>
   		<br>
   		<table align=center>RDServer: RD Scheduler may not have been started..</table>
   <%
   }
%>
</FORM>



<%@include file="ocx.h"%>
</body>
</html>
