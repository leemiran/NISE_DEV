<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File, m2soft.rdsystem.server.log.l.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.update.*" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JLFrame.java"%>
<%@ include file="../control/lib/JLListCtrl.java" %>
<%@ include file="../control/lib/JLMultiFrame.java"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<%@ include file="../control/lib/JSContextMenu.jsp" %>
<%@ include file="../control/lib/JLContextMenu.java" %>

<%
   initArg(request,out);
   out.println("<body bgcolor="+m_sBtnFace+">");
%>

<style>
<%@ include file="icis.css" %>
</style>

<script language="JavaScript">

   function submitUp()
   {

   	for (i=0; i<=document.recovery.date.length-1; i++)
   	{
   		if (document.recovery.date[i].checked)
   		{
   			var d = document.recovery.date[i].value;
            d = d.substring(0,4) +document.setmessage.JUpdateRecoveryStep2_02.value+" "+d.substring(4,6) +document.setmessage.JUpdateRecoveryStep2_03.value+" "+d.substring(6,8) +document.setmessage.JUpdateRecoveryStep2_04.value+" "+d.substring(8,10) +document.setmessage.JUpdateRecoveryStep2_05.value+" "+d.substring(10,12) +document.setmessage.JUpdateRecoveryStep2_06.value+" "+ d.substring(12,14) +document.setmessage.JUpdateRecoveryStep2_07.value ;

   			answer=confirm(  d + document.setmessage.JUpdateRecoveryStep2_10.value);

   			if(answer==true)
   			{
   				document.recovery.action="JUpdateRecoveryFrame.jsp?mode=recovery&data="+ d;
   				document.recovery.submit();
   			}
   		}

   	}
   }

   var isIE=document.all?true:false;
   var isNS4=document.layers?true:false;
   var isNS6=!isIE&&document.getElementById?true:false;

   function replaceIt(size,f,per)
   {
      var _n = size;
      var str = '<table> <td width=600><p><img src=images/blank.gif width=70 height=5 hspace=0 vspace=0>'+f+'</p></td></tr></table>   <table><td> <img src=images/blank.gif width=70 height=5 hspace=0 vspace=0>  <img src=images/hline.gif width='+size+' height=25 hspace=0 vspace=0></td></tr></table>   <table width=609><tr><td width=145><p> <img src=images/blank.gif width=70 height=5 hspace=0 vspace=0>0% </p></td><td width=93> <p>&nbsp;</p></td><td width=188> <p align=center>( '+per+' )</p></td><td width=36><p>&nbsp;</p></td><td width=83> <p>&nbsp;</p> </td><td width=38> <p>100%</p> </td></tr></table>';

      if (isNS4)
      {
         document.disp.document.open();
         document.disp.document.write(str);
         document.disp.document.close();
      }
      if (isIE)
      {
         document.all.disp.innerHTML=str;
      }

      if(isNS6)
      {
         document.getElementById("disp").innerHTML=str;
      }
   }

</script>

<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">

<form name="setmessage">
	<input type="hidden" name="JUpdateRecoveryStep2_02" value='<%= Message.get("JUpdateRecoveryStep2_02") %>' >
	<input type="hidden" name="JUpdateRecoveryStep2_03" value='<%= Message.get("JUpdateRecoveryStep2_03") %>' >
	<input type="hidden" name="JUpdateRecoveryStep2_04" value='<%= Message.get("JUpdateRecoveryStep2_04") %>' >
	<input type="hidden" name="JUpdateRecoveryStep2_05" value='<%= Message.get("JUpdateRecoveryStep2_05") %>' >
	<input type="hidden" name="JUpdateRecoveryStep2_06" value='<%= Message.get("JUpdateRecoveryStep2_06") %>' >
	<input type="hidden" name="JUpdateRecoveryStep2_07" value='<%= Message.get("JUpdateRecoveryStep2_07") %>' >
	<input type="hidden" name="JUpdateRecoveryStep2_10" value='<%= Message.get("JUpdateRecoveryStep2_10") %>' >
</form>

<%	String checkstep = request.getParameter("checkstep");
		String postmessage = request.getParameter("postmessage");

     if( checkstep.equals("step1") )
     { %><table border="0" width="663">
									<tr>
												<td width="27">
            <p>&nbsp;</p>
												</td>
												<td width="137">
            <p align="center"><font size="2">&nbsp;</font></p>
												</td>
												<td width="233">
															<p align="center">
&nbsp;
															</p>
												</td>
												<td width="248">
															<p align="center">
&nbsp;
															</p>
												</td>
									</tr>
									<tr>
												<td width="27">
            <p>&nbsp;</p>
												</td>
												<td width="626" colspan="3">
<hr>
												</td>
									</tr>
									<tr>
												<td width="27">
            <p>&nbsp;</p>
												</td>

												<td width="137">
            <p align="center">																																																						<b><%=Message.get("JUpdateRecoveryStep2_01")%></b></p>
												</td>

												<td width="485" colspan="2">
            <p align="center">

<%
	RollBack a = new RollBack();
	String[] list = a.getRollBackList();

	out.println( "<form name=recovery action=JUpdateRecoveryFrame.jsp?mode=recovery method=post>" );

	if(list.length != 0)
	{
		for(int i=0 ; i<list.length ; i++)
		{
			if(i >= list.length-1)
				out.println( "<input type=radio name=date value="+list[i]+"  checked >"+list[i].substring(0,4) +Message.get("JUpdateRecoveryStep2_02")+" "+list[i].substring(4,6) +Message.get("JUpdateRecoveryStep2_03")+" "+list[i].substring(6,8) +Message.get("JUpdateRecoveryStep2_04")+" "+list[i].substring(8,10) +Message.get("JUpdateRecoveryStep2_05")+" "+list[i].substring(10,12) +Message.get("JUpdateRecoveryStep2_06")+ list[i].substring(12,14) +Message.get("JUpdateRecoveryStep2_07")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			else
				out.println( "<input type=radio name=date value="+list[i]+">"+list[i].substring(0,4) +Message.get("JUpdateRecoveryStep2_02")+" "+list[i].substring(4,6) +Message.get("JUpdateRecoveryStep2_03")+" "+list[i].substring(6,8) +Message.get("JUpdateRecoveryStep2_04")+" "+list[i].substring(8,10) +Message.get("JUpdateRecoveryStep2_05")+" "+list[i].substring(10,12) +Message.get("JUpdateRecoveryStep2_06")+ list[i].substring(12,14) +Message.get("JUpdateRecoveryStep2_07")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		JLButton btnadd = new JLButton();
  		btnadd.printButton(280,350,"envup",170,2, "<font size=2><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Message.get("jupdatestep1_05")+"</b></font>" ,"javascript:submitUp();","images/admin_update.gif",20);
		out.println("<input type=hidden name=checkstep value=step2>");
		out.println( "</form>" );
	} else {
		out.println(Message.get("JUpdateRecoveryStep2_09"));
	}

%>												</td>
									</tr>
									<tr>
												<td width="27">
            <p>&nbsp;</p>
												</td>
												<td width="626" colspan="3">
<hr>
												</td>
									</tr>
									<tr>
												<td width="27">
            <p>&nbsp;</p>
												</td>



				<td width="626" colspan="3">
            <p align="center">&nbsp;</p>
</td>				</tr>

						</table>

<p>
<% } else if( checkstep.equals("step2") ) { %>
</p>
<p align="left"><b><%
		String date = request.getParameter("date");
		out.println("<hr>");
      out.println("<img src=images/blank.gif width='200' height=5 hspace=0 vspace=0>"+date.substring(0,4)+Message.get("JUpdateRecoveryStep2_02")+" "+date.substring(4,6) +Message.get("JUpdateRecoveryStep2_03")+" "+date.substring(6,8) +Message.get("JUpdateRecoveryStep2_04")+" "+date.substring(8,10) +Message.get("JUpdateRecoveryStep2_05")+" "+date.substring(10,12) +Message.get("JUpdateRecoveryStep2_06")+" "+date.substring(12,14)+Message.get("JUpdateRecoveryStep2_07")+" "+Message.get("JUpdateRecoveryStep2_08"));
%></b>
<hr>
<div id=disp>
<table><tr><td width=600><p> <img src=images/blank.gif width=70 height=5 hspace=0 vspace=0> </p></td></tr></table>
<table><td> <img src=images/blank.gif width=70 height=5 hspace=0 vspace=0> <img src=images/hline.gif width='2' height=25 hspace=0 vspace=0></td></tr></table>
<table width=609><tr><td width=145><p> <img src=images/blank.gif width=70 height=5 hspace=0 vspace=0>0% </p></td><td width=93> <p>&nbsp;</p></td><td width=188> <p align=center>( 0 / 0 )</p></td><td width=36><p>&nbsp;</p></td><td width=83> <p>&nbsp;</p> </td><td width=38> <p>100%</p> </td></tr></table>
</div>
<hr>

<%
      int width = 0;
      RollBack rb = new RollBack();
      Vector vlist = rb.initRollBack(date);
      rb.setRollBackTotalSize(vlist);
      rb.startRollBackRun(rb,vlist);

      String file = "", rbcompletefile="", per="";
      long p = 500/rb.getRollBackTotalSize();

      file = rb.getRollBackDestFileName(0)+"<img src=images/blank.gif width=50 height=5 hspace=0 vspace=0>";
      out.println("<script>javascript:replaceIt(5,'"+file+"')</script>");
      out.flush();

      while(true)
      {
         p=(rb.getRollBackFileSize()*500)/rb.getRollBackTotalSize();
         per = Long.toString(rb.getRollBackFileSize()) +" byte / "+ Long.toString(rb.getRollBackTotalSize())+ " byte";

         file = rb.getRollBackDestFileName()+Message.get("RollBack_rollBackStart_03")+"<img src=images/blank.gif width=50 height=5 hspace=0 vspace=0>";
         out.println( "<script>javascript:replaceIt("+ p +",'"+file+"','"+per+"')</script>" );

         if( rbcompletefile.equals(rb.getReturnMessage()) )
            	rbcompletefile = rb.getReturnMessage();
         else{
         	rbcompletefile = rb.getReturnMessage();
		width = 410 - rbcompletefile.length();
            	out.println( rbcompletefile+"<img src=images/blank.gif width="+width+" height=5 hspace=0 vspace=0>" );
         }

         out.flush();

         if(rb.getRollBackTotalSize()<=rb.getRollBackFileSize())
            break;

         if(rb.rbcomplete)
            break;

	try{ Thread.sleep(100); } catch (Exception e) {}
      }

	per = Long.toString(rb.getRollBackTotalSize()) +" / "+ Long.toString(rb.getRollBackTotalSize());
	out.println("<script>javascript:replaceIt(500,'"+Message.get("RollBack_rollBackStart_04")+"','"+per+"')</script>");
	out.println("<hr>");

      // 성공의 여부
      if(rb.getRollbackSuccess())
      {
         out.println("<p></p><b>"+Message.get("JUpdateRecoveryStep2_12")+"</b>"+"<p></p><b>"+rb.getLastMessage()+"</b>");
      }
      else
      {
         int failnum = rb.filenum - rb.getFailFileEtc();
         out.println("<b>"+Message.get("JUpdateRecoveryStep2_13")+"<p></p></b>");
         out.println("<b><p align=center>"+rb.filenum+Message.get("JUpdateRecoveryStep2_17")+" "+failnum+Message.get("JUpdateRecoveryStep2_15")+"</p></b>");
         out.println("<b><p align=center>"+Message.get("JUpdateRecoveryStep2_14")+"</p></b>");
      }

%>
</p>
<hr>
<% } else {
			response.sendRedirect("JUpdateMain.jsp");
} %>
