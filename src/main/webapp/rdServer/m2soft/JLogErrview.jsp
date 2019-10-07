<%@ page import="java.io.*,java.util.*,m2soft.rdsystem.server.logger.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.rddbagent.jdbc.L" %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="logproperties.h"%>

<%
	response.setHeader ("Refresh", rMilisecond+"; URL='JLogErrview.jsp?filename="+RdLogManager.getInstance().getErrorLog().getLogPath()+"'");
	initArg(request,out);

   String mrd,param, opcode;
   String[] allLogPath = UtilFile.getFileList(RDUtil.getRDSERVER_LOG_DIR(), "error_");
   int curIndex = allLogPath.length - 1;

   opcode = request.getParameter("opcode");
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

	if(L.isUnicodeVersion())
		mrd = cabpath+"/m2soft/errlog_u.mrd";
	else
		mrd = cabpath+"/m2soft/errlog.mrd";

	param = "/rfn ["+cabpath+"/download.jsp?filename="+allLogPath[curIndex]+"]";
%>

<SCRIPT>
	function rdOpen() {
		Rdviewer.SetBackgroundColor(255,255,255);
		Rdviewer.SetPageLineColor(255,255,255);
		Rdviewer.SetPageColor(255,255,255);
		Rdviewer.HideStatusBar();
		Rdviewer.AutoAdjust=0;
		Rdviewer.ZoomRatio = 85;
		Rdviewer.FileOpen("<%=mrd%>","<%=param%>");
		Rdviewer.CurrentPageNo = Rdviewer.GetTotalPageNo;
	}

   function prev(){
      <%if(curIndex > 0){%>
            document.navigation.action = "JLogErrview.jsp?opcode=<%=curIndex - 1%>";
            document.navigation.submit();
      <%}%>
   }

   function next(){
      <%if(curIndex < allLogPath.length-1){%>
            document.navigation.action = "JLogErrview.jsp?opcode=<%=curIndex + 1%>";
            document.navigation.submit();
      <%}%>
   }

</SCRIPT>

<body leftmargin=1px rightMargin=1px topmargin=0 marginwidth=1 marginheight=1 onLoad="javascript:rdOpen()" style="overflow-x:hidden;overflow-y:hidden">

<FORM NAME="navigation" METHOD=POST ACTION="JLogErrview.jsp">
<%
   if(allLogPath.length > 1)
   {
%>
      <input type="button" value="Prev" onClick="javascript:prev();" style="font-size:9pt; width:100;height:18;border-width:1px; border-color:gray; border-style:solid; background-color:#e4e4e4; text-align:center">
      <input type="button" value="Next" onClick="javascript:next();" style="font-size:9pt; width:100;height:18;border-width:1px; border-color:gray; border-style:solid; background-color:#e4e4e4; text-align:center">
      <!-- <font size=-1><%= allLogPath[curIndex] %></font> -->
<%
   }
%>
</FORM>

<%@include file="ocx.h" %>

</body>
</html>
